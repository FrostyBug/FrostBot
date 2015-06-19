package frostbug.chess;

import static frostbug.chess.Constants.*;

/**
 * Static move-related utilities
 * @author FrostBug
 */
public class Moves {
	/*
	 0000 0000 0000 0000 0000 0000 0111 1111 -> to
	 0000 0000 0000 0000 0011 1111 1000 0000 -> from
	 0000 0000 0000 0000 0100 0000 0000 0000 -> special
	 0000 0000 0000 0111 1000 0000 0000 0000 -> movPiece
	 0000 0000 0111 1000 0000 0000 0000 0000 -> content
	 0000 0000 1000 0000 0000 0000 0000 0000 -> pawnStart
	 */

	public final static int MOVE_SHIFT = 32;

	public final static int FLAG_SPECIAL = 0x4000;
	public final static int FLAG_PSTART = 0x800000;

	public final static int LENGTH_MOVE = 0x7F;
	public final static int LENGTH_PIECE = 0x0F;
	public final static int SHIFT_FROM = 7;
	public final static int SHIFT_SPECIAL = 14;
	public final static int SHIFT_MOVPIECE = 15;
	public final static int SHIFT_CONTENT = 19;
	public final static int SHIFT_PSTART = 23;

	public static int generateMove(int from, int to, int special, int movPiece, int content, int pawnStart) {
		return (to)
				| (from << SHIFT_FROM)
				| (special << SHIFT_SPECIAL)
				| (movPiece << SHIFT_MOVPIECE)
				| (content << SHIFT_CONTENT)
				| (pawnStart << SHIFT_PSTART);
	}

	public static long setMoveEvaluation(int move, int evaluation) {
		return ((long) evaluation << 32) | move;
	}

	public static long getEvaluation(long move) {
		return move >> 32;
	}

	public static boolean isSpecial(int move) {
		return (move & FLAG_SPECIAL) != 0;
	}

	public static boolean isPawnStart(int move) {
		return (move & FLAG_PSTART) != 0;
	}

	public static int getToSquare(int move) {
		return move & LENGTH_MOVE;
	}

	public static int getFromSquare(int move) {
		return (move >> SHIFT_FROM) & LENGTH_MOVE;
	}

	public static int getMovPiece(int move) {
		return (move >> SHIFT_MOVPIECE) & LENGTH_PIECE;
	}

	public static int getContentPiece(int move) {
		return (move >> SHIFT_CONTENT) & LENGTH_PIECE;
	}

	public static long getContentPiece(long move) {
		return (move >> SHIFT_CONTENT) & LENGTH_PIECE;
	}

	public static boolean isCastling(int move) {
		return isSpecial(move) && KING_ARRAY[getMovPiece(move)];
	}

	public static boolean isEnPassant(int move) {
		return isSpecial(move) && PAWN_ARRAY[getMovPiece(move)];
	}

	//Assumes that we do NOT promote to Knight or Bishop
	public static boolean isPromotion(int move) {
		return isSpecial(move) && ROOK_QUEEN_ARRAY[getMovPiece(move)];
	}

	public static void printMoveLong(long lMove) {
		int move = (int) lMove;
		int to = getToSquare(move);
		int from = getFromSquare(move);
		System.out.println("Move [" + Long.toBinaryString(move) + "]:");
		System.out.println("From: " + ((char) ('a' + FILES_MAP[from])) + ((char) ('1' + RANKS_MAP[from])));
		System.out.println("To: " + ((char) ('a' + FILES_MAP[to])) + ((char) ('1' + RANKS_MAP[to])));
		System.out.println("MovPiece: " + getMovPiece(move));
		System.out.println("Content: " + getContentPiece(move));
		System.out.println("Special: " + isSpecial(move) + "\n");
		System.out.println("PawnStart: " + isPawnStart(move));
		System.out.println("Evaluation: " + getEvaluation(lMove));
	}

	public static void printMoveShort(long lMove) {
		int move = (int) lMove;
		int to = getToSquare(move);
		int from = getFromSquare(move);
		System.out.println(squareToString(from) + " to " + squareToString(to) + " (eval: " + getEvaluation(lMove) + ")");
	}

	public static String moveToString(int move) {
		int to = getToSquare(move);
		int from = getFromSquare(move);
		return (squareToString(from) + " to " + squareToString(to));
	}

	public static String squareToString(int square) {
		return "" + ((char) ('a' + FILES_MAP[square]))
				+ ((char) ('1' + RANKS_MAP[square]));
	}

	public static void printMoves(long[] moves, int count) {
		System.out.println("Count: " + count);
		for (int i = 0; i < count; i++) {
			System.out.print("Move #" + i + ": ");
			printMoveShort(moves[i]);
		}
	}

}
