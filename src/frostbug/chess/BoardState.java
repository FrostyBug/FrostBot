package frostbug.chess;

import static frostbug.chess.Constants.*;
import frostbug.chess.visual.ChessPiece;
import java.security.SecureRandom;

/**
 * Chessboard data-model
 * @author FrostBug
 */
public class BoardState {

    public final int pieces[];
    public int turn;
    public int enPas;
    public int fiftyMoves;
    public int ply;
    public int plyTotal;
    public int castlePerms;
    public UMove[] history;
    public final long[] bbPawns;
    public int[] pieceNum;
    public final int[][] pieceArray;
    public final int[] material;
	
    //Zobrist values
    private final long[][] pieceZValues;
    private final long[] castleZValues;
    private long turnZValue;
    public long zobristKey;
    public int[] pvLine;
    public int[][] killerMoves;
    public int[][] searchHistory;
    public boolean[] hasCastled;
    private final static SecureRandom rand = new SecureRandom();

    public BoardState() {
        this.pieces = new int[128]; //Board map
        this.history = new UMove[MAX_MOVES_GAME];

        this.pieceNum = new int[13]; //Numbers of pieces still active
        this.pieceArray = new int[13][10]; //Keep track of pieces

        this.material = new int[2];
        this.bbPawns = new long[3];
        this.castlePerms = 0b1111; //Castling permission register
        this.pvLine = new int[MAX_DEPTH];
        this.killerMoves = new int[2][MAX_DEPTH];
        this.searchHistory = new int[13][128];

        this.hasCastled = new boolean[2];

        this.pieceZValues = new long[13][128];
        this.castleZValues = new long[16];
        this.turnZValue = 0;

        initZobristValues();
    }

    private void initZobristValues() {
        this.turnZValue = rand.nextLong();
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 128; j++) {
                this.pieceZValues[i][j] = rand.nextLong();
            }
        }
        for (int i = 0; i < 16; i++) {
            this.castleZValues[i] = rand.nextLong();
        }
    }

    public static long randLong() {
        return rand.nextLong();
    }

    public void reset() {
        for (int i = 0; i < 128; i++) {
            this.pieces[i] = OFFBOARD;
        }
        for (int i = 0; i < 64; i++) {
            this.pieces[BB_TO_HEX[i]] = EMPTY;
        }
        for (int i = 0; i < 2; i++) {
            this.material[i] = 0;
        }
        this.bbPawns[0] = bbPawns[1] = bbPawns[2] = 0;

        for (int i = 0; i < 13; i++) {
            this.pieceNum[i] = 0;
        }

        this.turn = BOTH;
        this.enPas = OFFBOARD;
        this.fiftyMoves = this.ply = this.plyTotal = this.castlePerms = 0;
    }

    public void setPawnBit(int color, int index) {
        this.bbPawns[color] |= BitBoards.getSetMask(index);
    }

    public void clearPawnBit(int color, int index) {
        this.bbPawns[color] &= BitBoards.getClearMask(index);
    }

    public long getPawnBB(int color) {
        return this.bbPawns[color];
    }

    public static boolean isOnboard(int piece) {
        return (piece & 0x88) == 0;
    }

    public static int getSquareHex(int file, int rank) {
        return file + (rank * 16);
    }

    public static int getSquareBB(int file, int rank) {
        return file + (rank * 8);
    }

    public long generateZobristKey() {
        long key = 0L;
        int piece;
        for (int sq = 0; sq < pieces.length; sq++) {
            piece = pieces[sq];
            if (piece != OFFBOARD && piece != EMPTY) {
                key ^= pieceZValues[piece][sq];
            }
        }
        if (turn == WHITE) {
            key ^= turnZValue;
        }
        if (enPas != OFFBOARD) {
            key ^= pieceZValues[EMPTY][enPas];
        }
        key ^= castleZValues[castlePerms];
        return key;
    }

    public int getThreatsByMinor(int pos, int opponentColor) {
        int piece, offset, sq;
        int count = 0;
        for (int i = 0; i < 8; i++) {
            sq = pos + MOVES_KNIGHT[i];
            if (isOnboard(sq)) {
                piece = pieces[sq]; //Knights
                if (KNIGHT_ARRAY[piece] && PIECE_COLORS[piece] == opponentColor) {
                    if (++count >= 2) {
                        return count;
                    }
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            offset = MOVES_BISHOP[i]; //Bishops
            sq = pos + offset;
            while (isOnboard(sq)) {
                piece = pieces[sq];
                if (piece != EMPTY) {
                    if (BISHOP_ARRAY[piece] && PIECE_COLORS[piece] == opponentColor) {
                        if (++count >= 2) {
                            return count;
                        }
                    }
                    break;
                }
                sq += offset;
            }
        }
        return count;
    }

    public boolean isSquareThreatened(int pos, int opponentColor) {

        int piece, offset, sq;

        //Pawn
        if (opponentColor == WHITE) {
            if ((isOnboard(sq = pos + MOVES_PAWN_BLACK[0]) && pieces[sq] == W_PAWN)
                    || (isOnboard(sq = pos + MOVES_PAWN_BLACK[1]) && pieces[sq] == W_PAWN)) {
                return true;
            }
        } else {
            if ((isOnboard(sq = pos + MOVES_PAWN_WHITE[0]) && pieces[sq] == B_PAWN)
                    || (isOnboard(sq = pos + MOVES_PAWN_WHITE[1]) && pieces[sq] == B_PAWN)) {
                return true;
            }
        }

        //King & Knight
        for (int i = 0; i < 8; i++) {
            sq = pos + MOVES_KING[i];
            if (isOnboard(sq)) {
                piece = pieces[sq]; //King
                if (KING_ARRAY[piece] && PIECE_COLORS[piece] == opponentColor) {
                    return true;
                }
            }

            sq = pos + MOVES_KNIGHT[i];
            if (isOnboard(sq)) {
                piece = pieces[sq]; //Knights
                if (KNIGHT_ARRAY[piece] && PIECE_COLORS[piece] == opponentColor) {
                    return true;
                }
            }
        }

        //Sliding pieces
        for (int i = 0; i < 4; i++) {
            offset = MOVES_ROOK[i]; //Rooks & Queen
            sq = pos + offset;
            while (isOnboard(sq)) {
                piece = pieces[sq];
                if (piece != EMPTY) {
                    if (ROOK_QUEEN_ARRAY[piece] && PIECE_COLORS[piece] == opponentColor) {
                        return true;
                    }
                    break;
                }
                sq += offset;
            }
            offset = MOVES_BISHOP[i]; //Bishops & Queen
            sq = pos + offset;
            while (isOnboard(sq)) {
                piece = pieces[sq];
                if (piece != EMPTY) {
                    if (BISHOP_QUEEN_ARRAY[piece] && PIECE_COLORS[piece] == opponentColor) {
                        return true;
                    }
                    break;
                }
                sq += offset;
            }
        }
        return false;
    }

    public void undoMove() {
        assert (validate());
        this.plyTotal--;
        this.ply--;

        UMove umove = this.history[plyTotal];

        boolean _spe = Moves.isSpecial(umove.move);
        int _from = Moves.getFromSquare(umove.move);
        int _to = Moves.getToSquare(umove.move);
        int _piece = Moves.getMovPiece(umove.move);
        int _cont = Moves.getContentPiece(umove.move);

        assert (isOnboard(_from));
        assert (isOnboard(_to));

        this.castlePerms = umove.castlePerms;
        this.fiftyMoves = umove.fiftyMove;
        //System.out.printf("ZKey undone to: %X\n", zobristKey);
        this.enPas = umove.enPas;

        this.turn ^= 1;

        if (_spe && PAWN_ARRAY[_piece]) {
            //En passant
            if (this.turn == WHITE) {
                addPiece(_to - 16, B_PAWN);
            } else {
                addPiece(_to + 16, W_PAWN);
            }
        } else if (_spe && KING_ARRAY[_piece]) {
            //Castle
            switch (_to) {
                case C1:
                    movePiece(D1, A1);
                    break;
                case G1:
                    movePiece(F1, H1);
                    break;
                case C8:
                    movePiece(D8, A8);
                    break;
                case G8:
                    movePiece(F8, H8);
            }
        }

        movePiece(_to, _from);

        if (_cont != EMPTY) {
            assert (_cont != EMPTY);
            addPiece(_to, _cont);
        }

        if (_spe && ROOK_QUEEN_ARRAY[_piece]) { //Undo promotion
            removePiece(_from);
            addPiece(_from, (turn == WHITE ? W_PAWN : B_PAWN));
        }

        this.zobristKey = umove.zobristKey;
        assert (validate());
    }

    public boolean makeMove(long eMove) {

        assert (validate());
        int move = (int) eMove; //Cuts off the evaluation bits

        int from = Moves.getFromSquare(move);
        int to = Moves.getToSquare(move);
        boolean spe = Moves.isSpecial(move);
        int piece = Moves.getMovPiece(move);
        int cont = Moves.getContentPiece(move);

        assert (isOnboard(from));
        assert (isOnboard(to));

        UMove umove = new UMove();
        umove.zobristKey = this.zobristKey;
        if (spe && PAWN_ARRAY[piece]) {
            //En passant
            removePiece(to + ((turn == WHITE) ? -16 : 16));
        } else if (spe && KING_ARRAY[piece]) {
            //Castle
            switch (to) {
                case C1:
                    movePiece(A1, D1);
                    break;
                case G1:
                    movePiece(H1, F1);
                    break;
                case C8:
                    movePiece(A8, D8);
                    break;
                case G8:
                    movePiece(H8, F8);
            }
        }

        umove.move = move;
        umove.fiftyMove = fiftyMoves;
        umove.enPas = enPas;
        umove.castlePerms = castlePerms;

        this.zobristKey ^= castleZValues[castlePerms];
        this.castlePerms &= CASTLE_MASKS[from];
        this.castlePerms &= CASTLE_MASKS[to];
        this.zobristKey ^= castleZValues[castlePerms];

        if (isOnboard(enPas)) {
            this.zobristKey ^= pieceZValues[EMPTY][enPas];
            this.enPas = OFFBOARD;
        }

        this.history[plyTotal] = umove;
        this.fiftyMoves++;

        if (cont != EMPTY) { //Remove captured
            removePiece(to);
            this.fiftyMoves = 0;
        }

        this.plyTotal++;
        this.ply++;
        if (PAWN_ARRAY[piece]) { //Add ep square
            this.fiftyMoves = 0;
            if (Moves.isPawnStart(move)) {
                this.enPas = from + ((turn == WHITE) ? 16 : -16);
                this.zobristKey ^= pieceZValues[EMPTY][enPas];
                assert (RANKS_MAP[enPas] == RANK_3 || RANKS_MAP[enPas] == RANK_6);
            }
        }

        movePiece(from, to); //Move the piece

        if (spe && QUEEN_ARRAY[piece]) {
            removePiece(to);
            addPiece(to, piece);
        }
        this.turn ^= 1;
        this.zobristKey ^= turnZValue;
//		System.out.printf("ZKey updated to %X by makeMove\n", zobristKey);
        assert (validate());

        if (isSquareThreatened(pieceArray[KING_BY_COLOR[turn ^ 1]][0], turn)) {
            undoMove();
            return false;
        }

        return true;
    }

    private void removePiece(int square) {
        assert (isOnboard(square));

        int piece = pieces[square];
        int color = PIECE_COLORS[piece];

        this.pieces[square] = EMPTY;
        this.zobristKey ^= pieceZValues[piece][square];
        //System.out.printf("ZKey updated to %X by removePiece\n", zobristKey);

        this.material[color] -= PIECE_VALUES[piece];

        if (PAWN_ARRAY[piece]) {
            clearPawnBit(color, HEX_TO_BB[square]);
            clearPawnBit(BOTH, HEX_TO_BB[square]);
        }

        int tmp = -1;
        for (int i = 0; i < pieceNum[piece]; i++) {
            if (pieceArray[piece][i] == square) {
                tmp = i;
                break;
            }
        }
        assert (tmp > -1);
        this.pieceNum[piece]--;
        this.pieceArray[piece][tmp] = pieceArray[piece][pieceNum[piece]];
    }

    private void addPiece(int square, int piece) {
        assert (isOnboard(square));
        assert (piece != EMPTY);

        int color = PIECE_COLORS[piece];
        this.pieces[square] = piece;
        this.zobristKey ^= pieceZValues[piece][square];
//		System.out.printf("ZKey updated to %X by addPiece\n", zobristKey);

        if (PAWN_ARRAY[piece]) {
            setPawnBit(color, HEX_TO_BB[square]);
            setPawnBit(BOTH, HEX_TO_BB[square]);
        }
        this.material[color] += PIECE_VALUES[piece];
        this.pieceArray[piece][pieceNum[piece]++] = square;
    } //O(1)

    public boolean isRepeatedState() {
        for (int i = plyTotal - fiftyMoves; i < plyTotal - 1; i++) {
            if (history[i] != null && history[i].zobristKey == zobristKey) {
                return true;
            }
        }
        return false;
    }

    public void printLastMove() {
        long move = history[plyTotal - 1].move;
        Moves.printMoveShort(move);
    }

    public void printMoveHistory() {
        int move;
        while (plyTotal > 0 && (move = history[--plyTotal].move) != 0) {
            Moves.printMoveShort(move);
        }
    }

    public void movePiece(int from, int to) {
        assert (isOnboard(from));
        assert (isOnboard(to));

        int piece = pieces[from];
        assert (piece != EMPTY);
        assert (pieces[to] == EMPTY);
        int c = PIECE_COLORS[piece];
        this.pieces[from] = EMPTY;
        this.pieces[to] = piece;

        this.zobristKey ^= pieceZValues[piece][from];
        this.zobristKey ^= pieceZValues[piece][to];
//		System.out.printf("ZKey updated to %X by movePiece\n", zobristKey);

        if (PAWN_ARRAY[piece]) {
            clearPawnBit(c, HEX_TO_BB[from]);
            clearPawnBit(BOTH, HEX_TO_BB[from]);
            setPawnBit(c, HEX_TO_BB[to]);
            setPawnBit(BOTH, HEX_TO_BB[to]);
        }

        for (int i = 0; i < pieceNum[piece]; i++) {
            if (pieceArray[piece][i] == from) {
                this.pieceArray[piece][i] = to;
                break;
            }
        }
    }

    public void parseFEN(String fen) throws NumberFormatException, ArrayIndexOutOfBoundsException {
        reset();
        String[] parts = fen.split(" ");

        //Parse content
        String[] ranks = parts[0].split("/");
        for (int i = RANK_8; i >= RANK_1; i--) {
            String rank = ranks[i];
            int count = 0;
            for (int j = 0; j < rank.length(); j++) {
                char c = rank.charAt(j);
                if (Character.isDigit(c)) {
                    count += Character.digit(c, 10);
                } else {
                    ChessPiece cp = ChessPiece.forChar(c);
                    if (cp != ChessPiece.EMPTY) {
                        int sq = getSquareHex(count, 7 - i);
                        pieces[sq] = cp.getId();
                        count++;
                    }
                }
            }
        }

        //Parse turn
        this.turn = parts[1].equals("w") ? WHITE : BLACK;

        //Parse castling perms
        String castle = parts[2];
        for (int i = 0; i < castle.length(); i++) {
            switch (castle.charAt(i)) {
                case 'K':
                    castlePerms |= WKCA;
                    break;
                case 'Q':
                    castlePerms |= WQCA;
                    break;
                case 'k':
                    castlePerms |= BKCA;
                    break;
                case 'q':
                    castlePerms |= BQCA;
                    break;
            }
        }

        //Parse en-pas
        char ef = parts[3].charAt(0);
        if (ef != '-') {
            char er = parts[3].charAt(1);
            int file = ef - 'a';
            int rank = er - '1';
            this.enPas = getSquareHex(file, rank);
        }

        //Parse 50-move count
        this.fiftyMoves = Integer.parseInt(parts[4]);

        //Parse ply
        this.plyTotal = Integer.parseInt(parts[5]) + turn;

        this.zobristKey = generateZobristKey();
        update();
    }

    public void print() {
        System.out.println("Board:");
        for (int rank = RANK_8; rank >= RANK_1; rank--) {
            System.out.print((rank + 1) + "  ");
            for (int file = FILE_A; file <= FILE_H; file++) {
                int sq = getSquareHex(file, rank);
                int piece = pieces[sq];
                System.out.print(PIECE_CHARS[piece] + " ");
            }
            System.out.println();
        }

        System.out.print("\n   ");
        for (int file = FILE_A; file <= FILE_H; file++) {
            System.out.print((char) ('a' + file) + " ");
        }
        System.out.println();
        System.out.println("Turn: " + TURN_CHARS[this.turn]);
        System.out.println("EnPas: " + this.enPas);
        System.out.println("Castle: "
                + ((castlePerms & WKCA) != 0 ? 'K' : '-')
                + ((castlePerms & WQCA) != 0 ? 'Q' : '-')
                + ((castlePerms & BKCA) != 0 ? 'k' : '-')
                + ((castlePerms & BQCA) != 0 ? 'q' : '-'));
        System.out.printf("UniqueKey: %X\n\n", this.zobristKey);
    }

    public void update() {
        int color, piece;
        for (int i = 0; i < 128; i++) {
            piece = pieces[i];
            if (piece != OFFBOARD && piece != EMPTY) {
                color = PIECE_COLORS[piece];

                this.material[color] += PIECE_VALUES[piece];
                this.pieceArray[piece][pieceNum[piece]] = i;
                this.pieceNum[piece]++;

                if (piece == W_PAWN) {
                    setPawnBit(WHITE, HEX_TO_BB[i]);
                    setPawnBit(BOTH, HEX_TO_BB[i]);
                } else if (piece == B_PAWN) {
                    setPawnBit(BLACK, HEX_TO_BB[i]);
                    setPawnBit(BOTH, HEX_TO_BB[i]);
                }
            }
        }
    }

    public int evaluate() {
        int score = material[turn] - material[turn ^ 1];
        int pNum, sq, col;

        for (int piece = W_PAWN; piece <= B_KING; piece++) {
            col = PIECE_COLORS[piece];
            for (pNum = 0; pNum < pieceNum[piece]; pNum++) {
                sq = pieceArray[piece][pNum];
                //Score by position
                if (turn == col) {
                    score += POSITION_VALUES[piece][HEX_TO_BB[sq]];
                } else {
                    score -= POSITION_VALUES[piece][HEX_TO_BB[sq]];
                }
                if (PAWN_ARRAY[piece]) {
                    //Double pawns
                    if (((bbPawns[col] & BB_FILE_MASKS[FILES_MAP[sq]]) & BitBoards.getClearMask(HEX_TO_BB[sq])) != 0) {
                        if (turn == col) {
                            score -= 8;
                        } else {
                            score += 8;
                        }
                    }
                    //Pass pawns
                    if ((bbPawns[col ^ 1] & PASS_PAWN_MASKS[col][HEX_TO_BB[sq]]) == 0) {
                        if (turn == col) {
                            score += PASS_PAWN_BONUSES[col][RANKS_MAP[sq]];
                        } else {
                            score -= PASS_PAWN_BONUSES[col][RANKS_MAP[sq]];
                        }
                    }
                }
            }
        }

        //Endgame and mating
        if (Math.min(material[WHITE], material[BLACK]) <= 31500) {
            score -= KING_MASK[pieceArray[KING_BY_COLOR[turn]][0]];
            score += KING_MASK[pieceArray[KING_BY_COLOR[turn ^ 1]][0]];
        } else if (Math.min(material[WHITE], material[BLACK]) <= 30600) {
            int minColor = material[WHITE] < material[BLACK] ? 0 : 1;
            int cSq = pieceArray[KING_BY_COLOR[minColor]][0];
            int oSq = pieceArray[KING_BY_COLOR[minColor ^ 1]][0];
            if (minColor == turn) {
                score -= KING_MASK_MATE[cSq];
                int kDist = Math.max(Math.abs(FILES_MAP[cSq] - FILES_MAP[oSq]), Math.abs(RANKS_MAP[cSq] - RANKS_MAP[oSq]));
                score += kDist * 2;
            } else {
                score += KING_MASK_MATE[cSq];
                int kDist = Math.max(Math.abs(FILES_MAP[cSq] - FILES_MAP[oSq]), Math.abs(RANKS_MAP[cSq] - RANKS_MAP[oSq]));
                score -= kDist * 2;
            }
        }

        //Castle bonus
        if (hasCastled[turn]) {
            score += 16;
        } else if (hasCastled[turn ^ 1]) {
            score -= 16;
        }
        return score;
    }

    public int getCoveredSqCount(int piece, int sq, int color) {
        int dir, tmp_sq, tmp_piece, count = 0;
        for (int index = 0; index < MOVES_ALL[piece].length; index++) {
            dir = MOVES_ALL[piece][index];
            tmp_sq = sq;

            //Slide loop
            while (isOnboard(tmp_sq += dir)) {
                tmp_piece = pieces[tmp_sq];
                if (tmp_piece != EMPTY) {
                    if (PIECE_COLORS[tmp_piece] == (color ^ 1)) {
                        count++;
                    }
                    break; //Stop sliding
                } else {
                    count++;
                }
            }
        }
        return count;
    }

    public String toFEN() {
        int count = 0;
        StringBuilder sb = new StringBuilder();
        for (int rank = RANK_8; rank >= RANK_1; rank--) {
            for (int file = FILE_A; file <= FILE_H; file++) {
                int sq = getSquareHex(file, rank);
                if (pieces[sq] != EMPTY) {
                    if (count > 0) {
                        sb.append(count);
                        count = 0;
                    }
                    sb.append(ChessPiece.forId(pieces[sq]).getFENChar());
                } else {
                    count++;
                }
            }
            if (count > 0) {
                sb.append(count);
                count = 0;
            }
            sb.append("/");
        }
        //"8/3q1p2/8/5P2/4Q3/8/8/8 w - - 0 2";
        sb.append(' ').append(turn == WHITE ? 'w' : 'b');
        sb.append(' ');
        if (castlePerms == 0) {
            sb.append('-');
        } else {
            if ((castlePerms & WKCA) != 0) {
                sb.append('K');
            }
            if ((castlePerms & WQCA) != 0) {
                sb.append('Q');
            }
            if ((castlePerms & BKCA) != 0) {
                sb.append('k');
            }
            if ((castlePerms & BQCA) != 0) {
                sb.append('q');
            }
        }
        sb.append(' ');
        sb.append((enPas != OFFBOARD) ? Moves.squareToString(enPas) : '-');
        sb.append(' ').append(fiftyMoves);
        sb.append(' ').append(plyTotal / 2);
        return sb.toString();
    }

    //Testing purposes
    public boolean validate() {
        int[] tmp_pieceNum = new int[13];
        int[] tmp_material = new int[2];
        long[] tmp_pawns = new long[3];
        System.arraycopy(bbPawns, 0, tmp_pawns, 0, tmp_pawns.length);
        for (int i = W_PAWN; i <= B_KING; i++) {
            for (int j = 0; j < pieceNum[i]; j++) {
                int hex = pieceArray[i][j];
                assert (pieces[hex] == i);
            }
        }
        for (int i = 0; i < 64; i++) {
            int hex = BB_TO_HEX[i];
            int tmp_piece = pieces[hex];
            tmp_pieceNum[tmp_piece]++;
            if (tmp_piece != EMPTY) {
                int color = PIECE_COLORS[tmp_piece];
                tmp_material[color] += PIECE_VALUES[tmp_piece];
            }
        }
        for (int i = W_PAWN; i <= B_KING; i++) {
            assert (tmp_pieceNum[i] == pieceNum[i]);
        }

        assert (Long.bitCount(tmp_pawns[WHITE]) == tmp_pieceNum[W_PAWN]);
        assert (Long.bitCount(tmp_pawns[BLACK]) == tmp_pieceNum[B_PAWN]);
        assert (Long.bitCount(tmp_pawns[BOTH]) == tmp_pieceNum[W_PAWN] + tmp_pieceNum[B_PAWN]);
        while (tmp_pawns[WHITE] != 0) {
            int index = BitBoards.indexOfLowestBit(tmp_pawns[WHITE]);
            tmp_pawns[WHITE] &= BitBoards.getClearMask(index);
            assert (pieces[BB_TO_HEX[index]] == W_PAWN);
        }
        while (tmp_pawns[BLACK] != 0) {
            int index = BitBoards.indexOfLowestBit(tmp_pawns[BLACK]);
            tmp_pawns[BLACK] &= BitBoards.getClearMask(index);
            assert (pieces[BB_TO_HEX[index]] == B_PAWN);
        }
        while (tmp_pawns[BOTH] != 0) {
            int index = BitBoards.indexOfLowestBit(tmp_pawns[BOTH]);
            tmp_pawns[BOTH] &= BitBoards.getClearMask(index);
            assert (pieces[BB_TO_HEX[index]] == W_PAWN || pieces[BB_TO_HEX[index]] == B_PAWN);
        }
        assert (tmp_material[WHITE] == this.material[WHITE]);
        assert (tmp_material[BLACK] == this.material[BLACK]);

        assert (this.turn != BOTH);
        assert (this.enPas == OFFBOARD
                || (RANKS_MAP[enPas] == RANK_6 && turn == WHITE)
                || (RANKS_MAP[enPas] == RANK_3 && turn == BLACK));
        assert (pieces[pieceArray[KING_BY_COLOR[WHITE]][0]] == W_KING);
        assert (pieces[pieceArray[KING_BY_COLOR[BLACK]][0]] == B_KING);
//		System.out.printf("Is: %X Should be: %X\n", zobristKey, generateZobristKey());
        assert (zobristKey == generateZobristKey());
        return true;
    }

    //For debugging
    public void printAttackBy(int side) {
        int rank;
        int file;
        int sq;

        System.out.println("\nSquares attacked:");
        for (rank = RANK_8; rank >= RANK_1; rank--) {
            for (file = FILE_A; file <= FILE_H; file++) {
                sq = getSquareHex(file, rank);
                if (isSquareThreatened(sq, side)) {
                    System.out.print("X ");
                } else {
                    System.out.print("- ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
