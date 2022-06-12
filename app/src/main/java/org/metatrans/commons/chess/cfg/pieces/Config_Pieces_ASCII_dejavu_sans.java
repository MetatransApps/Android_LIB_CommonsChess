package org.metatrans.commons.chess.cfg.pieces;


import org.metatrans.commons.chess.R;

import android.content.Context;


public class Config_Pieces_ASCII_dejavu_sans extends Config_Pieces_BaseImpl {
	
	
	public Config_Pieces_ASCII_dejavu_sans(Context _context) {
		super(_context);
	}
	
	
	@Override
	public int getID() {
		return CFG_PIECES_ASCII_dejavu_sans;
	}
	
	
	@Override
	public int getName() {
		return R.string.pieces_scheme_ascii_dejavu_sans;
	}
	
	
	@Override
	public int getIconResID() {
		return R.drawable.ic_pieces_ascii_dejavu_sans;
	}
	
	
	@Override
	public int getBitmapResID(int pieceID) {
		switch(pieceID) {
			case ID_PIECE_B_KING:
				return R.drawable.ascii_dejavu_sans_king_b;
			case ID_PIECE_B_QUEEN:
				return R.drawable.ascii_dejavu_sans_queen_b;
			case ID_PIECE_B_ROOK:
				return R.drawable.ascii_dejavu_sans_rook_b;
			case ID_PIECE_B_BISHOP:
				return R.drawable.ascii_dejavu_sans_bishop_b;
			case ID_PIECE_B_KNIGHT:
				return R.drawable.ascii_dejavu_sans_knight_b;
			case ID_PIECE_B_PAWN:
				return R.drawable.ascii_dejavu_sans_pawn_b;
			case ID_PIECE_W_KING:
				return R.drawable.ascii_dejavu_sans_king_w;
			case ID_PIECE_W_QUEEN:
				return R.drawable.ascii_dejavu_sans_queen_w;
			case ID_PIECE_W_ROOK:
				return R.drawable.ascii_dejavu_sans_rook_w;
			case ID_PIECE_W_BISHOP:
				return R.drawable.ascii_dejavu_sans_bishop_w;
			case ID_PIECE_W_KNIGHT:
				return R.drawable.ascii_dejavu_sans_knight_w;
			case ID_PIECE_W_PAWN:
				return R.drawable.ascii_dejavu_sans_pawn_w;
			default:
				throw new IllegalStateException("Illegal pieceID: " + pieceID);
		}
	}
}
