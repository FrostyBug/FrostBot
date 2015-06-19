package frostbug.chess;

import static frostbug.chess.Constants.*;

/**
 * Bitboard utility class
 * @author FrostBug
 */
public class BitBoards {

    private final static byte[] firstBit16;
    private final static long[] setMask;
    private final static long[] clearMask;

    static {
        firstBit16 = new byte[65536];
        firstBit16[0] = -1;
        for (int i = 1; i < 65536; i++) {
            for (byte j = 0; j < 16; j++) {
                if ((i & (1L << j)) != 0) {
                    firstBit16[i] = j;
                    break;
                }
            }
        }

        clearMask = new long[64];
        setMask = new long[64];
        for (int i = 0; i < 64; i++) {
            setMask[i] = 1L << i;
            clearMask[i] = ~setMask[i];
        }
    }

    public static long getSetMask(int bit) {
        return setMask[bit];
    }

    public static long getClearMask(int bit) {
        return clearMask[bit];
    }

    public static void printBoard(long bb) {
        int sq;
        long shift = 1L;
        StringBuilder sb = new StringBuilder("Board: \n");
        for (int rank = RANK_8; rank >= RANK_1; rank--) {
            for (int file = FILE_A; file <= FILE_H; file++) {
                sq = BoardState.getSquareBB(file, rank);
                if (((shift << sq) & bb) != 0) {
                    sb.append(" X ");
                } else {
                    sb.append(" - ");
                }
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }

    public static int indexOfHighestBit(long bb) {
        return Long.numberOfLeadingZeros(bb);
    }

    public static int indexOfLowestBit(long bb) {
        return Long.numberOfTrailingZeros(bb);
    }

    public static int[] getHighIndices(long bb) {
        int[] bits = new int[64];
        int set = 0;
        while (bb != 0) {
            bits[set] = getFirstBit16(bb);
            bb ^= (1L << bits[set++]);
        }
        bits[set] = -1;
        return bits;
    }

    private static int getFirstBit16(long bitboard) {
        if ((bitboard & 0xffff000000000000L) != 0) {
            return firstBit16[(int) (bitboard >>> 48L)] + 48;
        }
        if ((bitboard & 0x0000ffff00000000L) != 0) {
            return firstBit16[(int) (bitboard >>> 32L)] + 32;
        }
        if ((bitboard & 0x00000000ffff0000L) != 0) {
            return firstBit16[(int) (bitboard >>> 16L)] + 16;
        }
        return firstBit16[(int) (bitboard)];
    }

    public static void printArray(int[] array) {
        System.out.print("int[]{");
        for (int i : array) {
            System.out.print(i + " ");
        }
        System.out.println("}");
    }
}
