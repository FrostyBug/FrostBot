package frostbug.chess;

import static frostbug.chess.Constants.*;
import frostbug.chess.visual.ChessUI;
import java.util.Arrays;

/**
 * Main logic class; Searching goes on in here
 *
 * @author FrostBug
 */
public class ChessEngine {

	private ChessUI uiBoard; //Graphical UI
	private MoveGenerator moveGen; //Move generator instance
	private BoardState board; //Everything about the current board
	private TranspositionTable tp;//TranspositionTable; zobrist hashing

	//Tells the UI whether now is a good time to make changes
	private boolean busy;

	//AlphaBeta related variables
	private long startTime;
	private int nodes;
	private int searchTime;
	private boolean stop;
	private float failHigh, failHighFirst;
	private int completedDepth;
	private boolean mate;

	public static void main(String[] args) {
		new ChessEngine().init();
	}

	private void init() {
		this.uiBoard = new ChessUI(this);
		this.moveGen = new MoveGenerator();
		this.board = new BoardState();
		this.board.parseFEN(FEN_START);
		this.tp = new TranspositionTable(300000);

		this.busy = false;
		this.uiBoard.setLocationRelativeTo(null);
		this.uiBoard.setVisible(true);
	}

	/**
	 * Set the max search depth
	 *
	 * @param time
	 */
	public void setSearchTime(int time) {
		this.searchTime = time;
	}

	/**
	 * Get the current state of the game
	 *
	 * @return BoardState instance
	 */
	public BoardState getBoardState() {
		return board;
	}

	/**
	 * Load a state represented by an FEN notation string
	 *
	 * @param fen FEN representation of a board state
	 */
	public void loadState(String fen) {
		try {
			board.parseFEN(fen);
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
			System.out.println("One or more FEN fields invalid or missing!");
		}
	}

	/**
	 * Is the engine busy or not?
	 *
	 * @return true if the engine is busy right now
	 */
	public boolean isBusy() {
		return busy;
	}

	public boolean isMate() {
		return mate;
	}

	public int getCompletedDepth() {
		return completedDepth;
	}

	/**
	 * Start a search using iterative deepening of alphaBeta
	 *
	 * @return
	 */
	public int iterativeSearch() {
		clearSearch();

		int bestScore, curDepth;
		int bestMove = 0;
		startTime = System.currentTimeMillis();
		for (curDepth = 1; curDepth <= MAX_DEPTH; curDepth++) {
			System.out.println("Starting new iteration: " + curDepth + " cutoff rate: " + (failHighFirst / failHigh));
			bestScore = alphaBeta(-INF, INF, curDepth);

			if (stop) {
				System.out.println("Total nodes: " + nodes);
				if (Math.abs(bestScore) > 25000) {
					System.out.println("MATE");
					mate = true;
				}
				return bestMove;
			}
			completedDepth = curDepth;
			tp.generatePVLine(board, curDepth);
			bestMove = board.pvLine[0];
			
			System.out.println("PV Line:");
			tp.generatePVLine(board, curDepth);
			for (int m : board.pvLine) {
				if(m == 0) {
					break;
				}
				Moves.printMoveShort(m);
			}

		}
		return bestMove;
	}

	public void orderMoves(int moveCount, int curCount, long[] moves) {
		long bestEval = 0;
		int bestCount = curCount;
		for (int i = curCount; i < moveCount; i++) {
			if (Moves.getEvaluation(moves[i]) > bestEval) {
				bestEval = Moves.getEvaluation(moves[i]);
				bestCount = i;
			}
		}
		long m = moves[curCount];
		moves[curCount] = moves[bestCount];
		moves[bestCount] = m;
	}

	private void principleVariationCheck(int pv, long[] moves, int count) {
		if (pv > -1) {
			for (int i = 0; i < count; i++) {
				if ((int) moves[i] == pv) {
					moves[i] = Moves.setMoveEvaluation(pv, 2000000);
					break;
				}
			}
		}
	}

	public int alphaBeta(int a, int b, int depth) {
		assert (board.validate());

		if ((nodes & 2048) != 0) {
			if (System.currentTimeMillis() - startTime >= searchTime) {
				stop = true;
				return -1;
			}
		}

		if (depth == 0) {
			nodes++;
			return board.evaluate();
			//return quiescence(a, b);
		}
		nodes++;
		if ((board.isRepeatedState() || board.fiftyMoves >= 100) && board.ply > 0) {
			return 0;
		}
		if (board.ply >= MAX_DEPTH) {
			return board.evaluate();
		}

		boolean isCheck = board.isSquareThreatened(board.pieceArray[KING_BY_COLOR[board.turn]][0], board.turn ^ 1);
		if (isCheck) {
			depth++;
		}

		long[] moves = new long[MAX_MOVES_COUNT];
		int count = MoveGenerator.generateMoves(board, moves);

		int score;
		int tmp_a = a;
		int bestMove = 0;
		int legalMoves = 0;
		int pv = tp.probeEntry(board.zobristKey);
		principleVariationCheck(pv, moves, count);

		for (int i = 0; i < count; i++) {
			orderMoves(count, i, moves);
            
			if (!board.makeMove(moves[i])) {
				continue;
			}
			legalMoves++;
			score = -alphaBeta(-b, -a, depth - 1);
			board.undoMove();

			if (score > a) {
				if (score >= b) {
					if (legalMoves == 1) {
						failHighFirst++;
					}
					failHigh++;

					if (Moves.getContentPiece(moves[i]) == EMPTY) {
						board.killerMoves[1][board.ply] = board.killerMoves[0][board.ply];
						board.killerMoves[0][board.ply] = (int) moves[i];
					}

					return b;
				}
				a = score;
				bestMove = (int) moves[i];
				if (Moves.getContentPiece(moves[i]) == EMPTY) {
					board.searchHistory[board.pieces[Moves.getFromSquare(bestMove)]][Moves.getToSquare(bestMove)] += depth;
				}
			}
		}

		if (legalMoves == 0) {
			if (isCheck) {
				return -MATE + board.ply;
			} else {
				return 0;
			}
		}

		if (a != tmp_a) {
			tp.storeEntry(board.zobristKey, bestMove);
		}
		return a;
	}

	public int quiescence(int a, int b) {
		nodes++;

		if ((board.isRepeatedState() || board.fiftyMoves >= 100) && board.ply > 0) {
			return 0;
		}
		if (board.ply >= MAX_DEPTH) {
			return board.evaluate();
		}

		int stand_pat = board.evaluate();
		if (stand_pat >= b) {
			return b;
		}
		if (stand_pat > a) {
			a = stand_pat;
		}

		long[] moves = new long[MAX_MOVES_COUNT];
		int count = MoveGenerator.generateCaptures(board, moves);

		int tmp_a = a;
		int bestMove = 0;
		int legalMoves = 0;
		int score;

		for (int i = 0; i < count; i++) {
			orderMoves(count, i, moves);

			if (!board.makeMove(moves[i])) {
				continue;
			}
			legalMoves++;
			score = -quiescence(-b, -a);
			board.undoMove();

			if (score > a) {
				if (score >= b) {
					if (legalMoves == 1) {
						failHighFirst++;
					}
					failHigh++;
					return b;
				}
				a = score;
				bestMove = (int) moves[i];
			}
		}

		if (a != tmp_a) {
			tp.storeEntry(board.zobristKey, bestMove);
		}
		return a;
	}

	public void clearSearch() {
		Arrays.fill(board.killerMoves[0], 0);
		Arrays.fill(board.killerMoves[1], 0);
		for (int i = 0; i < 13; i++) {
			Arrays.fill(board.searchHistory[i], 0);
		}
		this.board.ply = 0;
		this.mate = false;
		this.completedDepth = 0;
		this.stop = false;
		this.nodes = 0;
		failHigh = failHighFirst = 0f;
	}
}
