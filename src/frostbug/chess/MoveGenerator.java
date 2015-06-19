package frostbug.chess;

import static frostbug.chess.Constants.*;

/**
 * Generate all legal chess moves
 * @author FrostBug
 */
public class MoveGenerator {

    private static int addMove(int move, long[] moves, int moveCount, int score) {
        moves[moveCount] = Moves.setMoveEvaluation(move, score + 1000000);
        return moveCount + 1;
    }

    private static int addMove(BoardState bs, int move, long[] moves, int moveCount) {
        long lMove;
        if (bs.killerMoves[0][bs.ply] == move) {
            lMove = Moves.setMoveEvaluation(move, 900000);
        } else if (bs.killerMoves[1][bs.ply] == move) {
            lMove = Moves.setMoveEvaluation(move, 800000);
        } else {
            int eval = bs.searchHistory[bs.pieces[Moves.getFromSquare(move)]][Moves.getToSquare(move)];
            lMove = Moves.setMoveEvaluation(move, eval);
        }
        moves[moveCount] = lMove;
        return moveCount + 1;
    }

    private static int addCaptureMove(BoardState bs, int move, long[] moves, int moveCount) {
        int score = MVV_LVA_VALUES[Moves.getContentPiece(move)][bs.pieces[Moves.getFromSquare(move)]] + 1000000;
        moves[moveCount] = Moves.setMoveEvaluation(move, score);
        return moveCount + 1;
    }

    private static int addPawnMove(BoardState bs, int from, int to, int capt, int pStart, int turn, long[] moves, int moveCount) {
        if ((RANKS_MAP[from] == RANK_7 && turn == WHITE) || (RANKS_MAP[from] == RANK_2 && turn == BLACK)) {
            int move = Moves.generateMove(from, to, 1, QUEEN_BY_COLOR[turn], capt, 0);
            if (capt == EMPTY) {
                return addMove(bs, move, moves, moveCount);
            } else {
                return addCaptureMove(bs, move, moves, moveCount);
            }
        } else {
            int move = Moves.generateMove(from, to, 0, PAWN_BY_COLOR[turn], capt, pStart);
            if (capt == EMPTY) {
                return addMove(bs, move, moves, moveCount);
            } else {
                return addCaptureMove(bs, move, moves, moveCount);
            }
        }
    }

    /**
     * Generate an array of possible moves
     *
     * @param bs the current board state
     * @param moves pre allocated array to hold the moves
     * @return the number of generated moves
     */
    public static int generateMoves(BoardState bs, long[] moves) {
        int count = 0;
        //int from, int to, int special, int movPiece, int content, int pawnStart
        int piece, tmp_piece, pIndex, index;
        int sq, tmp_sq, pieceNum, dir;

        int turn = bs.turn;
        int opponent = turn ^ 1;
        int pawn = PAWN_BY_COLOR[turn];

        //PAWNS
        for (pieceNum = 0; pieceNum < bs.pieceNum[pawn]; pieceNum++) {
            sq = bs.pieceArray[pawn][pieceNum];
            assert (isOnboard(sq));

            tmp_sq = sq + MOVES_PAWN_STRAIGHT[turn];
            if (bs.pieces[tmp_sq] == EMPTY) {
                count = addPawnMove(bs, sq, tmp_sq, 0, 0, turn, moves, count);
                tmp_sq += MOVES_PAWN_STRAIGHT[turn];
                if (((RANKS_MAP[sq] == RANK_7 && turn == BLACK)
                        || (RANKS_MAP[sq] == RANK_2 && turn == WHITE))
                        && bs.pieces[tmp_sq] == EMPTY) {
                    count = addPawnMove(bs, sq, tmp_sq, 0, 1, turn, moves, count);
                }
            }

            tmp_sq = sq + MOVES_PAWNS[turn][0];
            if (isOnboard(tmp_sq) && PIECE_COLORS[bs.pieces[tmp_sq]] == opponent) {
                count = addPawnMove(bs, sq, tmp_sq, bs.pieces[tmp_sq], 0, turn, moves, count);
            }
            if (isOnboard(bs.enPas) && bs.enPas == tmp_sq) { //En passant check
                int move = Moves.generateMove(sq, tmp_sq, 1, pawn, EMPTY, 0);
                count = addMove(move, moves, count, 105);
            }

            tmp_sq = sq + MOVES_PAWNS[turn][1];
            if (isOnboard(tmp_sq) && PIECE_COLORS[bs.pieces[tmp_sq]] == opponent) {
                count = addPawnMove(bs, sq, tmp_sq, bs.pieces[tmp_sq], 0, turn, moves, count);
            }
            if (isOnboard(bs.enPas) && bs.enPas == tmp_sq) { //En passant check
                int move = Moves.generateMove(sq, tmp_sq, 1, pawn, EMPTY, 0);
                count = addMove(move, moves, count, 105);
            }
        }

        //Bishops, Rooks & Queens
        pIndex = SLIDERS_INDEX[turn];
        piece = SLIDERS[pIndex++];
        while (piece != -1) {
            for (pieceNum = 0; pieceNum < bs.pieceNum[piece]; pieceNum++) {
                sq = bs.pieceArray[piece][pieceNum];
                for (index = 0; index < MOVES_ALL[piece].length; index++) {
                    dir = MOVES_ALL[piece][index];
                    tmp_sq = sq;

                    //Slide loop
                    while (isOnboard(tmp_sq += dir)) {
                        tmp_piece = bs.pieces[tmp_sq];
                        if (tmp_piece != EMPTY) {
                            if (PIECE_COLORS[tmp_piece] == opponent) {
                                int move = Moves.generateMove(sq, tmp_sq, 0, piece, tmp_piece, 0);
                                count = addCaptureMove(bs, move, moves, count);
                            }
                            break; //Stop sliding
                        } else {
                            int move = Moves.generateMove(sq, tmp_sq, 0, piece, EMPTY, 0);
                            count = addMove(bs, move, moves, count);
                        }
                    }
                }
            }
            piece = SLIDERS[pIndex++];
        }
        //Kings & Knights
        pIndex = MOVERS_INDEX[turn];
        while ((piece = MOVERS[pIndex]) != -1) {
            for (pieceNum = 0; pieceNum < bs.pieceNum[piece]; pieceNum++) {
                sq = bs.pieceArray[piece][pieceNum];
                for (index = 0; index < MOVES_ALL[piece].length; index++) {
                    dir = MOVES_ALL[piece][index];
                    tmp_sq = sq + dir;
                    if (isOnboard(tmp_sq)) {
                        tmp_piece = bs.pieces[tmp_sq];
                        if (tmp_piece != EMPTY) {
                            if (PIECE_COLORS[tmp_piece] == opponent) {
                                int move = Moves.generateMove(sq, tmp_sq, 0, piece, tmp_piece, 0);
                                count = addCaptureMove(bs, move, moves, count);
                            }
                        } else {
                            int move = Moves.generateMove(sq, tmp_sq, 0, piece, EMPTY, 0);
                            count = addMove(bs, move, moves, count);
                        }
                    }
                }
            }
            pIndex++;
        }

        //Castling
        if (turn == WHITE) {
            if ((bs.castlePerms & WKCA) != 0
                    && bs.pieces[F1] == EMPTY
                    && bs.pieces[G1] == EMPTY
                    && !bs.isSquareThreatened(E1, BLACK)
                    && !bs.isSquareThreatened(F1, BLACK)) {
                int move = Moves.generateMove(E1, G1, 1, W_KING, EMPTY, 0);
                count = addMove(bs, move, moves, count);
            }
            if ((bs.castlePerms & WQCA) != 0
                    && bs.pieces[D1] == EMPTY
                    && bs.pieces[C1] == EMPTY
                    && bs.pieces[B1] == EMPTY
                    && !bs.isSquareThreatened(E1, BLACK)
                    && !bs.isSquareThreatened(D1, BLACK)) {
                int move = Moves.generateMove(E1, C1, 1, W_KING, EMPTY, 0);
                count = addMove(bs, move, moves, count);
            }
        } else {
            if ((bs.castlePerms & BKCA) != 0
                    && bs.pieces[F8] == EMPTY
                    && bs.pieces[G8] == EMPTY
                    && !bs.isSquareThreatened(E8, WHITE)
                    && !bs.isSquareThreatened(F8, WHITE)) {
                int move = Moves.generateMove(E8, G8, 1, B_KING, EMPTY, 0);
                count = addMove(bs, move, moves, count);
            }
            if ((bs.castlePerms & BQCA) != 0
                    && bs.pieces[D8] == EMPTY
                    && bs.pieces[C8] == EMPTY
                    && bs.pieces[B8] == EMPTY
                    && !bs.isSquareThreatened(E8, WHITE)
                    && !bs.isSquareThreatened(D8, WHITE)) {
                int move = Moves.generateMove(E8, C8, 1, B_KING, EMPTY, 0);
                count = addMove(bs, move, moves, count);
            }
        }

        return count;
    }

    public static int generateCaptures(BoardState bs, long[] moves) {
        int count = 0;
        int piece, tmp_piece, pIndex, index;
        int sq, tmp_sq, pieceNum, dir;

        int turn = bs.turn;
        int opponent = turn ^ 1;
        int pawn = PAWN_BY_COLOR[turn];

        //PAWNS
        for (pieceNum = 0; pieceNum < bs.pieceNum[pawn]; pieceNum++) {
            sq = bs.pieceArray[pawn][pieceNum];
            assert (isOnboard(sq));

            tmp_sq = sq + MOVES_PAWNS[turn][0];
            if (isOnboard(tmp_sq) && PIECE_COLORS[bs.pieces[tmp_sq]] == opponent) {
                count = addPawnMove(bs, sq, tmp_sq, bs.pieces[tmp_sq], 0, turn, moves, count);
            }
            if (isOnboard(bs.enPas) && bs.enPas == tmp_sq) { //En passant check
                int move = Moves.generateMove(sq, tmp_sq, 1, pawn, EMPTY, 0);
                count = addMove(move, moves, count, 105);
            }

            tmp_sq = sq + MOVES_PAWNS[turn][1];
            if (isOnboard(tmp_sq) && PIECE_COLORS[bs.pieces[tmp_sq]] == opponent) {
                count = addPawnMove(bs, sq, tmp_sq, bs.pieces[tmp_sq], 0, turn, moves, count);
            }
            if (isOnboard(bs.enPas) && bs.enPas == tmp_sq) { //En passant check
                int move = Moves.generateMove(sq, tmp_sq, 1, pawn, EMPTY, 0);
                count = addMove(move, moves, count, 105);
            }
        }

        //Bishops, Rooks & Queens
        pIndex = SLIDERS_INDEX[turn];
        piece = SLIDERS[pIndex++];
        while (piece != -1) {
            for (pieceNum = 0; pieceNum < bs.pieceNum[piece]; pieceNum++) {
                sq = bs.pieceArray[piece][pieceNum];
                for (index = 0; index < MOVES_ALL[piece].length; index++) {
                    dir = MOVES_ALL[piece][index];
                    tmp_sq = sq;

                    //Slide loop
                    while (isOnboard(tmp_sq += dir)) {
                        tmp_piece = bs.pieces[tmp_sq];
                        if (tmp_piece != EMPTY) {
                            if (PIECE_COLORS[tmp_piece] == opponent) {
                                int move = Moves.generateMove(sq, tmp_sq, 0, piece, tmp_piece, 0);
                                count = addCaptureMove(bs, move, moves, count);
                            }
                            break; //Stop sliding
                        }
                    }
                }
            }
            piece = SLIDERS[pIndex++];
        }
        //Kings & Knights
        pIndex = MOVERS_INDEX[turn];
        while ((piece = MOVERS[pIndex]) != -1) {
            for (pieceNum = 0; pieceNum < bs.pieceNum[piece]; pieceNum++) {
                sq = bs.pieceArray[piece][pieceNum];
                for (index = 0; index < MOVES_ALL[piece].length; index++) {
                    dir = MOVES_ALL[piece][index];
                    tmp_sq = sq + dir;
                    if (isOnboard(tmp_sq)) {
                        tmp_piece = bs.pieces[tmp_sq];
                        if (tmp_piece != EMPTY) {
                            if (PIECE_COLORS[tmp_piece] == opponent) {
                                int move = Moves.generateMove(sq, tmp_sq, 0, piece, tmp_piece, 0);
                                count = addCaptureMove(bs, move, moves, count);
                            }
                        }
                    }
                }
            }
            pIndex++;
        }
        return count;
    }

    public static boolean isMoveLegal(BoardState bs, int move) {
        long[] moves = new long[MAX_MOVES_COUNT];
        int count = generateMoves(bs, moves);
        for (int i = 0; i < count; i++) {
            if (!bs.makeMove(moves[i])) {
                continue;
            }
            bs.undoMove();
            if ((int) moves[i] == move) {
                return true;
            }
        }
        return false;
    }

    public static boolean isOffboard(int square) {
        return (square & 0x88) != 0;
    }

    public static boolean isOnboard(int square) {
        return (square & 0x88) == 0;
    }
}
