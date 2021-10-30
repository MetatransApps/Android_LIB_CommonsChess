package org.metatrans.commons.chess.logic;


public interface BoardConstants {

	public static final int ID_PIECE_NONE			= 0;
	
	public static final int ID_PIECE_W_KING			= 1;
	public static final int ID_PIECE_W_QUEEN		= 2;
	public static final int ID_PIECE_W_ROOK			= 3;
	public static final int ID_PIECE_W_BISHOP		= 4;
	public static final int ID_PIECE_W_KNIGHT		= 5;
	public static final int ID_PIECE_W_PAWN			= 6;
	
	public static final int ID_PIECE_B_KING			= 7;
	public static final int ID_PIECE_B_QUEEN		= 8;
	public static final int ID_PIECE_B_ROOK			= 9;
	public static final int ID_PIECE_B_BISHOP		= 10;
	public static final int ID_PIECE_B_KNIGHT		= 11;
	public static final int ID_PIECE_B_PAWN			= 12;
	public static final int ID_PIECE_B_MAX			= 13;
	
	public static final int COLOUR_PIECE_NONE		= 0;
	public static final int COLOUR_PIECE_WHITE		= 1;
	public static final int COLOUR_PIECE_BLACK		= 2;
	public static final int COLOUR_PIECE_ALL		= 3;
	
	public static final int TYPE_PAWN				= 1;
	public static final int TYPE_KNIGHT				= 2;
	public static final int TYPE_BISHOP				= 3;
	public static final int TYPE_ROOK				= 4;
	public static final int TYPE_QUEEN				= 5;
	public static final int TYPE_KING				= 6;
	
	
	public static final String[] PIECES_SIGN = new String[] {
		"X",
		"K",
		"Q",
		"R",
		"B",
		"N",
		"P",
		"k",
		"q",
		"r",
		"b",
		"n",
		"p",
	};
	
	
	public static final int[][] INITIAL_PIECES = new int[][] {
			{ ID_PIECE_B_ROOK, ID_PIECE_B_PAWN, ID_PIECE_NONE,
					ID_PIECE_NONE, ID_PIECE_NONE, ID_PIECE_NONE,
					ID_PIECE_W_PAWN, ID_PIECE_W_ROOK },
			{ ID_PIECE_B_KNIGHT, ID_PIECE_B_PAWN, ID_PIECE_NONE,
					ID_PIECE_NONE, ID_PIECE_NONE, ID_PIECE_NONE,
					ID_PIECE_W_PAWN, ID_PIECE_W_KNIGHT },
			{ ID_PIECE_B_BISHOP, ID_PIECE_B_PAWN, ID_PIECE_NONE,
					ID_PIECE_NONE, ID_PIECE_NONE, ID_PIECE_NONE,
					ID_PIECE_W_PAWN, ID_PIECE_W_BISHOP },
			{ ID_PIECE_B_QUEEN, ID_PIECE_B_PAWN, ID_PIECE_NONE,
					ID_PIECE_NONE, ID_PIECE_NONE, ID_PIECE_NONE,
					ID_PIECE_W_PAWN, ID_PIECE_W_QUEEN },
			{ ID_PIECE_B_KING, ID_PIECE_B_PAWN, ID_PIECE_NONE,
					ID_PIECE_NONE, ID_PIECE_NONE, ID_PIECE_NONE,
					ID_PIECE_W_PAWN, ID_PIECE_W_KING },
			{ ID_PIECE_B_BISHOP, ID_PIECE_B_PAWN, ID_PIECE_NONE,
					ID_PIECE_NONE, ID_PIECE_NONE, ID_PIECE_NONE,
					ID_PIECE_W_PAWN, ID_PIECE_W_BISHOP },
			{ ID_PIECE_B_KNIGHT, ID_PIECE_B_PAWN, ID_PIECE_NONE,
					ID_PIECE_NONE, ID_PIECE_NONE, ID_PIECE_NONE,
					ID_PIECE_W_PAWN, ID_PIECE_W_KNIGHT },
			{ ID_PIECE_B_ROOK, ID_PIECE_B_PAWN, ID_PIECE_NONE,
					ID_PIECE_NONE, ID_PIECE_NONE, ID_PIECE_NONE,
					ID_PIECE_W_PAWN, ID_PIECE_W_ROOK }, };
}
