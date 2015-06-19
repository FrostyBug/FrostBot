package frostbug.chess;

import java.util.Arrays;

/**
 * Various constants and conversion arrays
 * @author FrostBug
 */
public class Constants {

	public final static int OFFBOARD = 0x79;

	public final static int[] HEX_TO_BB = {
		0, 1, 2, 3, 4, 5, 6, 7, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD,
		8, 9, 10, 11, 12, 13, 14, 15, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD,
		16, 17, 18, 19, 20, 21, 22, 23, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD,
		24, 25, 26, 27, 28, 29, 30, 31, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD,
		32, 33, 34, 35, 36, 37, 38, 39, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD,
		40, 41, 42, 43, 44, 45, 46, 47, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD,
		48, 49, 50, 51, 52, 53, 54, 55, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD,
		56, 57, 58, 59, 60, 61, 62, 63, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD
	};
	public final static int[] BB_TO_HEX = {
		0, 1, 2, 3, 4, 5, 6, 7,
		16, 17, 18, 19, 20, 21, 22, 23,
		32, 33, 34, 35, 36, 37, 38, 39,
		48, 49, 50, 51, 52, 53, 54, 55,
		64, 65, 66, 67, 68, 69, 70, 71,
		80, 81, 82, 83, 84, 85, 86, 87,
		96, 97, 98, 99, 100, 101, 102, 103,
		112, 113, 114, 115, 116, 117, 118, 119
	};

	public final static int[] CENTER_DISTANCE_KNIGHT_EVAL = {
		-6, -3, 0, 3, 3, 0, -3, -6, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD,
		-3, 0, 3, 6, 6, 3, 0, -3, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD,
		0, 3, 6, 9, 9, 6, 3, 0, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD,
		3, 6, 9, 12, 12, 9, 6, 3, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD,
		3, 6, 9, 12, 12, 9, 6, 3, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD,
		0, 3, 6, 9, 9, 6, 3, 0, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD,
		-3, 0, 3, 6, 6, 3, 0, -3, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD,
		-6, -3, 0, 3, 3, 0, -3, -6, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD, OFFBOARD
	};

	public final static int[] CASTLE_MASKS = {
		13, 15, 15, 15, 12, 15, 15, 14, 15, 15, 15, 15, 15, 15, 15, 15,
		15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
		15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
		15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
		15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
		15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
		15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
		7 , 15, 15, 15, 3 , 15, 15, 11, 15, 15, 15, 15, 15, 15, 15, 15
	};

	public final static int[] KING_MASK = {
		4, 4, 4, 4, 4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0,
		4, 3, 3, 3, 3, 3, 3, 4, 0, 0, 0, 0, 0, 0, 0, 0,
		4, 3, 2, 2, 2, 2, 3, 4, 0, 0, 0, 0, 0, 0, 0, 0,
		4, 3, 2, 1, 1, 2, 3, 4, 0, 0, 0, 0, 0, 0, 0, 0,
		4, 3, 2, 1, 1, 2, 3, 4, 0, 0, 0, 0, 0, 0, 0, 0,
		4, 3, 2, 2, 2, 2, 3, 4, 0, 0, 0, 0, 0, 0, 0, 0,
		4, 3, 3, 3, 3, 3, 3, 4, 0, 0, 0, 0, 0, 0, 0, 0,
		4, 4, 4, 4, 4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0
	};

	public final static int[] KING_MASK_MATE = {
		32, 32, 32, 32, 32, 32, 32, 32, 0, 0, 0, 0, 0, 0, 0, 0,
		32, 24, 24, 24, 24, 24, 24, 32, 0, 0, 0, 0, 0, 0, 0, 0,
		32, 24, 16, 16, 16, 16, 24, 32, 0, 0, 0, 0, 0, 0, 0, 0,
		32, 24, 16, 8 , 8 , 16, 24, 32, 0, 0, 0, 0, 0, 0, 0, 0,
		32, 24, 16, 8 , 8 , 16, 24, 32, 0, 0, 0, 0, 0, 0, 0, 0,
		32, 24, 16, 16, 16, 16, 24, 32, 0, 0, 0, 0, 0, 0, 0, 0,
		32, 24, 24, 24, 24, 24, 24, 32, 0, 0, 0, 0, 0, 0, 0, 0,
		32, 32, 32, 32, 32, 32, 32, 32, 0, 0, 0, 0, 0, 0, 0, 0
	};

	public final static int RANK_1 = 0;
	public final static int RANK_2 = 1;
	public final static int RANK_3 = 2;
	public final static int RANK_4 = 3;
	public final static int RANK_5 = 4;
	public final static int RANK_6 = 5;
	public final static int RANK_7 = 6;
	public final static int RANK_8 = 7;
	public final static int FILE_A = 0;
	public final static int FILE_B = 1;
	public final static int FILE_C = 2;
	public final static int FILE_D = 3;
	public final static int FILE_E = 4;
	public final static int FILE_F = 5;
	public final static int FILE_G = 6;
	public final static int FILE_H = 7;

	public final static int[] FILES_MAP;
	public final static int[] RANKS_MAP;

	public final static int MATE = 29000;
	public final static int INF = 30000;

	public final static int EMPTY = 0;
	public final static int W_PAWN = 1;
	public final static int W_KNIGHT = 2;
	public final static int W_BISHOP = 3;
	public final static int W_ROOK = 4;
	public final static int W_QUEEN = 5;
	public final static int W_KING = 6;
	public final static int B_PAWN = 7;
	public final static int B_KNIGHT = 8;
	public final static int B_BISHOP = 9;
	public final static int B_ROOK = 10;
	public final static int B_QUEEN = 11;
	public final static int B_KING = 12;

	public final static int[] VICTIM_VALUES = {0, 100, 200, 300, 400, 500, 600, 100, 200, 300, 400, 500, 600};
	public final static int[][] MVV_LVA_VALUES;

	public final static long[] BB_FILE_MASKS;
	public final static long[] BB_RANK_MASKS;

	public final static long[] PASS_PAWN_MASK_W;
	public final static long[] PASS_PAWN_MASK_B;

	static {
		FILES_MAP = new int[128];
		RANKS_MAP = new int[128];
		MVV_LVA_VALUES = new int[13][13];
		BB_RANK_MASKS = new long[8];
		BB_FILE_MASKS = new long[8];
		PASS_PAWN_MASK_W = new long[64];
		PASS_PAWN_MASK_B = new long[64];

		Arrays.fill(FILES_MAP, OFFBOARD);
		Arrays.fill(RANKS_MAP, OFFBOARD);
		for (int rank = RANK_1; rank <= RANK_8; rank++) {
			for (int file = FILE_A; file <= FILE_H; file++) {
				int pos = BoardState.getSquareHex(file, rank);
				FILES_MAP[pos] = file;
				RANKS_MAP[pos] = rank;
			}
		}

		for (int i = W_PAWN; i <= B_KING; i++) {
			for (int j = W_PAWN; j <= B_KING; j++) {
				MVV_LVA_VALUES[i][j] = (VICTIM_VALUES[j] + 6) - (VICTIM_VALUES[i] / 100);
			}
		}

		int sq;
		Arrays.fill(BB_FILE_MASKS, 0);
		Arrays.fill(BB_RANK_MASKS, 0);
		for (int i = RANK_8; i >= RANK_1; i--) {
			for (int j = FILE_A; j < FILE_H; j++) {
				sq = i * 8 + j;
				BB_RANK_MASKS[i] |= (1L << sq);
				BB_FILE_MASKS[j] |= (1L << sq);
			}
		}

		Arrays.fill(PASS_PAWN_MASK_B, 0);
		Arrays.fill(PASS_PAWN_MASK_W, 0);

		int tmp;
		for (int i = 0; i < 64; i++) {
			tmp = i + 8;
			while (tmp < 64) {
				PASS_PAWN_MASK_W[i] |= (1L << tmp);
				tmp += 8;
			}
			tmp = i - 8;
			while (tmp >= 0) {
				PASS_PAWN_MASK_B[i] |= (1L << tmp);
				tmp -= 8;
			}
			if (FILES_MAP[BB_TO_HEX[i]] > FILE_A) {
				tmp = i + 7;
				while (tmp < 64) {
					PASS_PAWN_MASK_W[i] |= (1L << tmp);
					tmp += 8;
				}
				tmp = i - 9;
				while (tmp >= 0) {
					PASS_PAWN_MASK_B[i] |= (1L << tmp);
					tmp -= 8;
				}
			}
			if (FILES_MAP[BB_TO_HEX[i]] < FILE_H) {
				tmp = i + 9;
				while (tmp < 64) {
					PASS_PAWN_MASK_W[i] |= (1L << tmp);
					tmp += 8;
				}
				tmp = i - 7;
				while (tmp >= 0) {
					PASS_PAWN_MASK_B[i] |= (1L << tmp);
					tmp -= 8;
				}
			}
		}
	}
	
	public final static long[][] PASS_PAWN_MASKS = {PASS_PAWN_MASK_W, PASS_PAWN_MASK_B};

	public final static int WHITE = 0;
	public final static int BLACK = 1;
	public final static int BOTH = 2;

	public final static String FEN_START = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
	public final static String FEN_MOVE1 = "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1";
	public final static String FEN_MOVE2 = "rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2";
	public final static String FEN_MOVE3 = "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2";
	public final static String FEN_MOVE4 = "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1";
	public final static String FEN_QUEENS = "8/3q4/8/8/4Q3/8/8/8 w - - 0 2";
	public final static String FEN_QUEENS2 = "8/3q1p2/8/5P2/4Q3/8/8/8 w - - 0 2";
	public final static String FEN_PAWNS = "8/3p4/8/8/4P3/8/8/8 w - - 0 2";
	public final static String FEN_MATE3 = "2rr3k/pp3pp1/1nnqbN1p/3pN3/2pP4/2P3Q1/PPB4P/R4RK1 w - - 0 2";

	public static boolean[] PIECES_BIG = {false, false, true, true, true, true, true, false, true, true, true, true, true};
	public static boolean[] PIECES_MAJOR = {false, false, false, false, true, true, true, false, false, false, true, true, true};
	public static boolean[] PIECES_MINOR = {false, false, true, true, false, false, false, false, true, true, false, false, false};
	public static int[] PIECE_VALUES = {0, 100, 300, 300, 500, 900, 30000, 100, 300, 300, 500, 900, 30000};
	public static int[] PIECE_COLORS = {BOTH, WHITE, WHITE, WHITE, WHITE, WHITE, WHITE, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK};

	public static int[] CASTLE_VALUES = {1, 2, 4, 8};
	
	public static int[] KING_BY_COLOR = {6, 12};

	public final static int A8 = 0x70;
	public final static int B8 = 0x71;
	public final static int C8 = 0x72;
	public final static int D8 = 0x73;
	public final static int E8 = 0x74;
	public final static int F8 = 0x75;
	public final static int G8 = 0x76;
	public final static int H8 = 0x77;
	public final static int A7 = 0x60;
	public final static int B7 = 0x61;
	public final static int C7 = 0x62;
	public final static int D7 = 0x63;
	public final static int E7 = 0x64;
	public final static int F7 = 0x65;
	public final static int G7 = 0x66;
	public final static int H7 = 0x67;
	public final static int A6 = 0x50;
	public final static int B6 = 0x51;
	public final static int C6 = 0x52;
	public final static int D6 = 0x53;
	public final static int E6 = 0x54;
	public final static int F6 = 0x55;
	public final static int G6 = 0x56;
	public final static int H6 = 0x57;
	public final static int A5 = 0x40;
	public final static int B5 = 0x41;
	public final static int C5 = 0x42;
	public final static int D5 = 0x43;
	public final static int E5 = 0x44;
	public final static int F5 = 0x45;
	public final static int G5 = 0x46;
	public final static int H5 = 0x47;
	public final static int A4 = 0x30;
	public final static int B4 = 0x31;
	public final static int C4 = 0x32;
	public final static int D4 = 0x33;
	public final static int E4 = 0x34;
	public final static int F4 = 0x35;
	public final static int G4 = 0x36;
	public final static int H4 = 0x37;
	public final static int A3 = 0x20;
	public final static int B3 = 0x21;
	public final static int C3 = 0x52;
	public final static int D3 = 0x23;
	public final static int E3 = 0x24;
	public final static int F3 = 0x25;
	public final static int G3 = 0x26;
	public final static int H3 = 0x27;
	public final static int A2 = 0x10;
	public final static int B2 = 0x11;
	public final static int C2 = 0x12;
	public final static int D2 = 0x13;
	public final static int E2 = 0x14;
	public final static int F2 = 0x15;
	public final static int G2 = 0x16;
	public final static int H2 = 0x17;
	public final static int A1 = 0x00;
	public final static int B1 = 0x01;
	public final static int C1 = 0x02;
	public final static int D1 = 0x03;
	public final static int E1 = 0x04;
	public final static int F1 = 0x05;
	public final static int G1 = 0x06;
	public final static int H1 = 0x07;

	public final static int WKCA = 1;
	public final static int WQCA = 2;
	public final static int BKCA = 4;
	public final static int BQCA = 8;

	public final static char[] PIECE_CHARS = ".PNBRQKpnbrqk".toCharArray();
	public final static char[] TURN_CHARS = "wb-".toCharArray();

	public final static int MAX_MOVES_GAME = 2048;
	public final static int MAX_MOVES_COUNT = 256;
	public final static int MAX_DEPTH = 64;

	public final static int[] MOVES_KNIGHT = {0x21, 0x1F, 0x12, 0x0E, -0x21, -0x1F, -0x12, -0x0E};
	public final static int[] MOVES_BISHOP = {0x11, 0x0F, -0x0F, -0x11};
	public final static int[] MOVES_ROOK = {0x01, 0x10, -0x01, -0x10};
	public final static int[] MOVES_KING = {0x01, 0x10, 0x11, 0x0F, -0x01, -0x10, -0x0F, -0x11};
	public final static int[] MOVES_PAWN_WHITE = {17, 15};
	public final static int[] MOVES_PAWN_BLACK = {-17, -15};
	public final static int[] MOVES_PAWN_STRAIGHT = {16, -16};
	public final static int[][] MOVES_PAWNS = {MOVES_PAWN_WHITE, MOVES_PAWN_BLACK};
	public final static int[][] MOVES_ALL = {null, null, MOVES_KNIGHT, MOVES_BISHOP, MOVES_ROOK, MOVES_KING, MOVES_KING, null, MOVES_KNIGHT, MOVES_BISHOP, MOVES_ROOK, MOVES_KING, MOVES_KING};

	public final static int[] PAWN_BY_COLOR = {W_PAWN, B_PAWN};
	public final static int[] QUEEN_BY_COLOR = {W_QUEEN, B_QUEEN};

	public final static int[] SLIDERS = {W_BISHOP, W_ROOK, W_QUEEN, -1, B_BISHOP, B_ROOK, B_QUEEN, -1};
	public final static int[] MOVERS = {W_KNIGHT, W_KING, -1, B_KNIGHT, B_KING, -1};
	public final static int[] SLIDERS_INDEX = {0, 4};
	public final static int[] MOVERS_INDEX = {0, 3};

	public final static int[] THREAT_PENALTIES = {2, -10, -50};
	public final static int[] PASS_PAWN_BONUS_W = {0, 5, 10, 20, 35, 60, 100, 200}; 
	public final static int[] PASS_PAWN_BONUS_B = {200, 100, 60, 35, 20, 10, 5, 0};
	public final static int[][] PASS_PAWN_BONUSES = {PASS_PAWN_BONUS_W, PASS_PAWN_BONUS_B};

	public final static boolean[] PAWN_ARRAY = {false, true, false, false, false, false, false, true, false, false, false, false, false};
	public final static boolean[] KNIGHT_ARRAY = {false, false, true, false, false, false, false, false, true, false, false, false, false};
	public final static boolean[] KING_ARRAY = {false, false, false, false, false, false, true, false, false, false, false, false, true};
	public final static boolean[] ROOK_QUEEN_ARRAY = {false, false, false, false, true, true, false, false, false, false, true, true, false};
	public final static boolean[] ROOK_ARRAY = {false, false, false, false, true, false, false, false, false, false, true, false, false};
	public final static boolean[] BISHOP_QUEEN_ARRAY = {false, false, false, true, false, true, false, false, false, true, false, true, false};
	public final static boolean[] BISHOP_ARRAY = {false, false, false, true, false, false, false, false, false, true, false, false, false};
	public final static boolean[] QUEEN_ARRAY = {false, false, false, false, false, true, false, false, false, false, false, true, false};
	public final static boolean[] SLIDE_ARRAY = {false, false, false, true, true, true, false, false, false, true, true, true, false};

	public final static float[] WHITE_PAWN_MASK = {
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		-2, 0, 3, 4, 5, 1, -2, -2, 0, 0, 0, 0, 0, 0, 0, 0,
		-4, -1, 3.5f, 5, 6.5f, 0.5f, -4, -4, 0, 0, 0, 0, 0, 0, 0, 0,
		-4, 0, 6, 8, 10, 2, -4, -4, 0, 0, 0, 0, 0, 0, 0, 0,
		-3, 2, 9.5f, 12, 14.5f, 4.5f, -3, -3, 0, 0, 0, 0, 0, 0, 0, 0,
		8, 14, 23, 26, 29, 17, 8, 8, 0, 0, 0, 0, 0, 0, 0, 0,
		23, 30, 41.5f, 44, 47.5f, 33.5f, 23, 23, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
	};

	public final static float[] BLACK_PAWN_MASK = {
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		23, 30, 41.5f, 44, 47.5f, 33.5f, 23, 23, 0, 0, 0, 0, 0, 0, 0, 0,
		8, 14, 23, 26, 29, 17, 8, 8, 0, 0, 0, 0, 0, 0, 0, 0,
		-3, 2, 9.5f, 12, 14.5f, 4.5f, -3, -3, 0, 0, 0, 0, 0, 0, 0, 0,
		-4, 0, 6, 8, 10, 2, -4, -4, 0, 0, 0, 0, 0, 0, 0, 0,
		-4, -1, 3.5f, 5, 6.5f, 0.5f, -4, -4, 0, 0, 0, 0, 0, 0, 0, 0,
		-2, 0, 3, 4, 5, 1, -2, -2, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0

	};

	public final static int[] BB_MIRROR = {
		56, 57, 58, 59, 60, 61, 62, 63,
		48, 49, 50, 51, 52, 53, 54, 55,
		40, 41, 42, 43, 44, 45, 46, 47,
		32, 33, 34, 35, 36, 37, 38, 39,
		24, 25, 26, 27, 28, 29, 30, 31,
		16, 17, 18, 19, 20, 21, 22, 23,
		8, 9, 10, 11, 12, 13, 14, 15,
		0, 1, 2, 3, 4, 5, 6, 7
	};
	public final static int[][] POSITION_VALUES = {
		new int[256], //BLANK
		{ //BLACK PAWN
			0, 0, 0, 0, 0, 0, 0, 0,
			-1, -1, 1, 5, 6, 1, -1, -1,
			-4, -4, 0, 4, 6, 0, -4, -4,
			-4, -4, 0, 6, 8, 0, -4, -4,
			-3, -3, 2, 9, 11, 2, -3, -3,
			-2, -2, 4, 12, 15, 4, -2, -2,
			7, 7, 13, 23, 26, 13, 7, 7,
			0, 0, 0, 0, 0, 0, 0, 0
		},
		{ //BLACK KNIGHT
			-7, -5, -4, -2, -2, -4, -5, -7,
			-5, -3, -1, 0, 0, -1, -3, -5,
			-3, 1, 3, 4, 4, 3, 1, -3,
			0, 5, 8, 9, 9, 8, 5, 0,
			3, 10, 14, 14, 14, 14, 10, 3,
			5, 11, 18, 19, 19, 18, 11, 5,
			1, 4, 12, 13, 13, 12, 4, 1,
			-2, 2, 7, 9, 9, 7, 2, -2
		},
		{ // BLACK BISHOP
			0, 0, 0, 0, 0, 0, 0, 0,
			5, 5, 5, 3, 3, 5, 5, 5,
			4, 5, 5, -2, -2, 5, 5, 4,
			4, 5, 6, 8, 8, 6, 5, 4,
			3, 5, 7, 7, 7, 7, 5, 3,
			3, 5, 6, 6, 6, 6, 5, 3,
			4, 7, 7, 7, 7, 7, 7, 4,
			2, 3, 4, 4, 4, 4, 3, 2
		},
		{ //BLACK ROOK
			0, 0, 0, 0, 0, 0, 0, 0,
			3, 4, 4, 6, 6, 4, 4, 3,
			4, 5, 5, 5, 5, 5, 5, 5,
			6, 6, 5, 6, 6, 5, 6, 6,
			8, 8, 8, 9, 9, 8, 8, 8,
			9, 10, 10, 11, 11, 10, 10, 9,
			4, 6, 7, 9, 9, 7, 6, 4,
			9, 9, 11, 10, 11, 9, 9, 9
		},
		{ //BLACK QUEEN
			0, 0, 0, 0, 0, 0, 0, 0,
			2, 2, 2, 2, 2, 2, 2, 2,
			2, 2, 2, 3, 3, 2, 2, 2,
			2, 3, 3, 4, 4, 3, 3, 2,
			3, 3, 4, 4, 4, 4, 3, 3,
			3, 4, 4, 4, 4, 4, 4, 3,
			2, 3, 4, 4, 4, 4, 3, 2,
			2, 3, 4, 3, 4, 3, 3, 2
		},
		new int[256], //BLACK KING
		{ //WHITE PAWN
			0, 0, 0, 0, 0, 0, 0, 0,
			7, 7, 13, 23, 26, 13, 7, 7,
			-2, -2, 4, 12, 15, 4, -2, -2,
			-3, -3, 2, 9, 11, 2, -3, -3,
			-4, -4, 0, 6, 8, 0, -4, -4,
			-4, -4, 0, 4, 6, 0, -4, -4,
			-1, -1, 1, 5, 6, 1, -1, -1,
			0, 0, 0, 0, 0, 0, 0, 0
		},
		{ //WHITE KNIGHT
			-2, 2, 7, 9, 9, 7, 2, -2,
			1, 4, 12, 13, 13, 12, 4, 1,
			5, 11, 18, 19, 19, 18, 11, 5,
			3, 10, 14, 14, 14, 14, 10, 3,
			0, 5, 8, 9, 9, 8, 5, 0,
			-3, 1, 3, 4, 4, 3, 1, -3,
			-5, -3, -1, 0, 0, -1, -3, -5,
			-7, -5, -4, -2, -2, -4, -5, -7
		},
		{ // WHITE BISHOP
			2, 3, 4, 4, 4, 4, 3, 2,
			4, 7, 7, 7, 7, 7, 7, 4,
			3, 5, 6, 6, 6, 6, 5, 3,
			3, 5, 7, 7, 7, 7, 5, 3,
			4, 5, 6, 8, 8, 6, 5, 4,
			4, 5, 5, -2, -2, 5, 5, 4,
			5, 5, 5, 3, 3, 5, 5, 5,
			0, 0, 0, 0, 0, 0, 0, 0
		},
		{ //WHITE ROOK
			9, 9, 11, 10, 11, 9, 9, 9,
			4, 6, 7, 9, 9, 7, 6, 4,
			9, 10, 10, 11, 11, 10, 10, 9,
			8, 8, 8, 9, 9, 8, 8, 8,
			6, 6, 5, 6, 6, 5, 6, 6,
			4, 5, 5, 5, 5, 5, 5, 5,
			3, 4, 4, 6, 6, 4, 4, 3,
			0, 0, 0, 0, 0, 0, 0, 0
		},
		{ //WHITE QUEEN
			2, 3, 4, 3, 4, 3, 3, 2,
			2, 3, 4, 4, 4, 4, 3, 2,
			3, 4, 4, 4, 4, 4, 4, 3,
			3, 3, 4, 4, 4, 4, 3, 3,
			2, 3, 3, 4, 4, 3, 3, 2,
			2, 2, 2, 3, 3, 2, 2, 2,
			2, 2, 2, 2, 2, 2, 2, 2,
			0, 0, 0, 0, 0, 0, 0, 0
		},
		new int[256] //WHITE KING
	};
}
