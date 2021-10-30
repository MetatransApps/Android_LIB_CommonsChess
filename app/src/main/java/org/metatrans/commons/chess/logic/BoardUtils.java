package org.metatrans.commons.chess.logic;


import org.metatrans.commons.storage.ObjectUtils;

import com.chessartforkids.model.BoardData;


public class BoardUtils implements BoardConstants {
	
	
	public static final int getColour( int pid) {
		if (pid >= ID_PIECE_W_KING && pid <= ID_PIECE_W_PAWN) {
			return COLOUR_PIECE_WHITE;
		} else if (pid >= ID_PIECE_B_KING && pid <= ID_PIECE_B_PAWN) {
			return COLOUR_PIECE_BLACK;
		} else {
			throw new IllegalStateException("PID: " + pid);
		}
	}
	
	
	public static final int switchColour(int colour) {
		switch(colour) {
			case COLOUR_PIECE_WHITE:
				return COLOUR_PIECE_BLACK;
			case COLOUR_PIECE_BLACK:
				return COLOUR_PIECE_WHITE;
			default:
				throw new IllegalStateException("colour=" + colour);
		}
	}
	
	
	public static final int getType( int pid) {
		switch (pid) {
			case ID_PIECE_NONE:
				throw new IllegalStateException("pid=" + pid);
			case ID_PIECE_W_KING:
				return TYPE_KING;
			case ID_PIECE_W_QUEEN:
				return TYPE_QUEEN;
			case ID_PIECE_W_ROOK:
				return TYPE_ROOK;
			case ID_PIECE_W_BISHOP:
				return TYPE_BISHOP;
			case ID_PIECE_W_KNIGHT:
				return TYPE_KNIGHT;
			case ID_PIECE_W_PAWN:
				return TYPE_PAWN;
			case ID_PIECE_B_KING:
				return TYPE_KING;
			case ID_PIECE_B_QUEEN:
				return TYPE_QUEEN;
			case ID_PIECE_B_ROOK:
				return TYPE_ROOK;
			case ID_PIECE_B_BISHOP:
				return TYPE_BISHOP;
			case ID_PIECE_B_KNIGHT:
				return TYPE_KNIGHT;
			case ID_PIECE_B_PAWN:
				return TYPE_PAWN;
			default:
				throw new IllegalStateException("pid=" + pid);
		}
	}
	
	
	public static BoardData createBoardDataForNewGame() {
		BoardData data = new BoardData();
		
		try {
			data.board = (int[][]) ObjectUtils.copyObject(BoardConstants.INITIAL_PIECES);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		data.captured_w = new int[16];
		for (int i=0; i<data.captured_w.length; i++) {
			data.captured_w[i] = ID_PIECE_NONE;
		}
		data.size_captured_w = 0;
		data.captured_b = new int[16];
		for (int i=0; i<data.captured_b.length; i++) {
			data.captured_b[i] = ID_PIECE_NONE;
		}
		data.size_captured_b = 0;
		data.colourToMove = BoardConstants.COLOUR_PIECE_WHITE;
		data.w_kingSideAvailable = true;
		data.w_queenSideAvailable = true;
		data.b_kingSideAvailable = true;
		data.b_queenSideAvailable = true;
		data.enpassantSquare = null;
		data.halfMoves = 0;
		data.fullMoves = 0;
				
		return data;
	}
	
	
	public static String convertBoard2FEN(int[][] pieces, int colour,
			boolean w_kingSideAvailable, boolean w_queenSideAvailable, boolean b_kingSideAvailable, boolean b_queenSideAvailable,
			String enpassantSquare, int halfMoves, int fullMoves) {
		
		String fen = "";
		
		int empty = 0;
		
		for (int digit=0; digit<8; digit++) {
			
			String line = "";
			
			for (int letter=0; letter<8; letter++) {
				
				int pid = pieces[letter][digit];
				
				if (pid == BoardConstants.ID_PIECE_NONE) {
					empty++;
				} else {
					if (empty != 0) {
						line += empty;
					}
					
					String sign = BoardConstants.PIECES_SIGN[pid];
					line += sign;
					empty = 0;
				}
				
				if (letter == 7 && empty != 0) {
					line += empty;
					empty = 0;
				}
			}
			
			if (digit != 7) {
				line += "/";
			}
			
			fen = fen + line;
		}
		
		fen += " ";
		fen += colour == BoardConstants.COLOUR_PIECE_WHITE ? "w" : "b";
		
		fen += " ";
		if (w_kingSideAvailable) {
			fen += "K";
		}
		if (w_queenSideAvailable) {
			fen += "Q";
		}
		if (b_kingSideAvailable) {
			fen += "k";
		}
		if (b_queenSideAvailable) {
			fen += "q";
		}
		
		if (fen.endsWith(" ")) {
			fen += "-";
		}
		
		fen += " ";
		if (enpassantSquare != null) {
			fen += enpassantSquare;
		} else {
			fen += "-";
		}
		
		fen += " ";
		fen += halfMoves;
		
		fen += " ";
		fen += fullMoves;

		return fen;
	}
}
