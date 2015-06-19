package frostbug.chess;

/**
 * Transposition table
 *
 * @author FrostBug
 */
public class TranspositionTable {

	private final int[] moves;
	private final long[] zKeys;
	private final int capacity;

	public TranspositionTable(int capacity) {
		this.moves = new int[capacity];
		this.zKeys = new long[capacity];
		this.capacity = capacity;
	}

	public void storeEntry(long zKey, int move) {
		int index = (int) (zKey % capacity);
		if (index < 0) {
			index = -index;
		}
		moves[index] = move;
		zKeys[index] = zKey;
	}

	public int probeEntry(long zKey) {
		int index = (int) (zKey % capacity);
		if (index < 0) {
			index = -index;
		}
		if (zKeys[index] == zKey) {
			return moves[index];
		}
		return -1;
	}

	/**
	 * Generate a principle variation line
	 *
	 * @param bs The board whose line is being updated
	 * @param depth Depth for which to update the line
	 * @return The depth of the generated line; May have been cut off before the
	 * input depth was reached
	 */
	public int generatePVLine(BoardState bs, int depth) {
		int move = probeEntry(bs.zobristKey);
		int count = 0;

		while (move != -1 && count < depth) {
			if (MoveGenerator.isMoveLegal(bs, move)) {
				bs.makeMove(move);
				bs.pvLine[count++] = move;
			} else {
				break;
			}
			move = probeEntry(bs.zobristKey);
		}

		//Unwind
		while (bs.ply > 0) {
			bs.undoMove();
		}
		return count;
	}

}
