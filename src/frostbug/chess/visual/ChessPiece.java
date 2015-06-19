package frostbug.chess.visual;

/**
 *
 * @author FrostBug
 */
public enum ChessPiece {

	EMPTY(" ", -1, false, "Empty", 0, " "),
    WHITE_PAWN("♙", 5, false, "Pawn", 1, "P"),
    WHITE_KNIGHT("♘", 4, false, "Knight", 2, "N"),
    WHITE_BISHOP("♗", 3, true, "Bishop", 3, "B"),
    WHITE_ROOK("♖", 2, true, "Rook", 4, "R"),
    WHITE_QUEEN("♕", 1, true, "Queen", 5, "Q"),
    WHITE_KING("♔", 0, false, "King", 6, "K"),
    BLACK_PAWN("♟", 6, false, "Pawn", 7, "p"),
    BLACK_KNIGHT("♞", 4, false, "Knight", 8, "n"),
    BLACK_BISHOP("♝", 3, true, "Bishop", 9, "b"),
    BLACK_ROOK("♜", 2, true, "Rook", 10, "r"),
    BLACK_QUEEN("♛", 1, true, "Queen", 11, "q"),
    BLACK_KING("♚", 0, false, "King", 12, "k");

    private final String ascii, name, fen;
    private final int index, id;
    private final boolean continous;

    ChessPiece(String ascii, int index, boolean continous, String name, int id, String fen) {
        this.ascii = ascii;
        this.index = index;
        this.continous = continous;
        this.name = name;
        this.id = id;
		this.fen = fen;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
    
    public String getFENChar() {
        return fen;
    }

    public String getAscii() {
        return ascii;
    }

    public int getIndex() {
        return index;
    }

    public boolean isContinous() {
        return continous;
    }

    public static ChessPiece[] baseValues() {
        return new ChessPiece[]{
            WHITE_KING,
            WHITE_QUEEN,
            WHITE_ROOK,
            WHITE_BISHOP,
            WHITE_KNIGHT,
            WHITE_PAWN,
            BLACK_PAWN
        };
    }
	
	public static ChessPiece forChar(char fen) {
		for(ChessPiece cp : values()) {
			if(cp.fen.equals(Character.toString(fen))) {
				return cp;
			}
		}
		return null;
	}
    
    public static ChessPiece forId(int id) {
        for(ChessPiece cp : values()) {
            if(cp.getId() == id) {
                return cp;
            }
        }
        return null;
    }
}
