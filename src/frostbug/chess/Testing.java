package frostbug.chess;

import static frostbug.chess.Constants.*;

/**
 *
 * @author FrostBug
 */
public class Testing {

	public static void main(String[] args) {
		BoardState bs = new BoardState();
		bs.parseFEN(FEN_MOVE4);

		perftTest(5, new MoveGenerator(), bs);
	}

	private static long leaves = 0;

	private static void perft(int depth, MoveGenerator mg, BoardState bs) {
		//assert (bs.validate());

		if (depth == 0) {
			leaves++;
			return;
		}

		long[] moves = new long[MAX_MOVES_COUNT];
		int count = mg.generateMoves(bs, moves);
		for (int i = 0; i < count; i++) {
			if (!bs.makeMove(moves[i])) {
				continue;
			}
			perft(depth - 1, mg, bs);
			bs.undoMove();
		}
	}

	public static void perftTest(int depth, MoveGenerator mg, BoardState bs) {
		assert (bs.validate());
		System.out.println("Perft to depth " + depth);
		leaves = 0;

		long[] moves = new long[MAX_MOVES_COUNT];
		int count = mg.generateMoves(bs, moves);
		
		int move;
		for (int i = 0; i < count; i++) {
			move = (int)moves[i];
			if (!bs.makeMove(move)) {
				continue;
			}
			long cn = leaves;
			perft(depth - 1, mg, bs);
			bs.undoMove();
			long on = leaves - cn;
			System.out.print("Move #" + i + ": ");
			Moves.printMoveShort(move);
			System.out.println("Old: " + on);
		}
		
		System.out.println("Completed. Nodes: " + leaves);
	}
}
