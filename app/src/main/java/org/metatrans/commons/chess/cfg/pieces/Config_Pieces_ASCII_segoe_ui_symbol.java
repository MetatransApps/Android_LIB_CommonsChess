package org.metatrans.commons.chess.cfg.pieces;


import org.metatrans.commons.chess.R;

import android.content.Context;


public class Config_Pieces_ASCII_segoe_ui_symbol extends Config_Pieces_BaseImpl {
	
	
	public Config_Pieces_ASCII_segoe_ui_symbol(Context _context) {
		super(_context);
	}
	
	
	@Override
	public int getID() {
		return CFG_PIECES_ASCII_segoe_ui_symbol;
	}
	
	
	@Override
	public int getName() {
		return R.string.pieces_scheme_ascii_segoe_ui_symbol;
	}
	
	
	@Override
	public int getIconResID() {
		return R.drawable.ic_pieces_ascii_segoe_ui_symbol;
	}
	
	
	@Override
	public int getBitmapResID(int pieceID) {
		switch(pieceID) {
			case ID_PIECE_B_KING:
				return R.drawable.ascii_segoe_ui_symbol_king_b;
			case ID_PIECE_B_QUEEN:
				return R.drawable.ascii_segoe_ui_symbol_queen_b;
			case ID_PIECE_B_ROOK:
				return R.drawable.ascii_segoe_ui_symbol_rook_b;
			case ID_PIECE_B_BISHOP:
				return R.drawable.ascii_segoe_ui_symbol_bishop_b;
			case ID_PIECE_B_KNIGHT:
				return R.drawable.ascii_segoe_ui_symbol_knight_b;
			case ID_PIECE_B_PAWN:
				return R.drawable.ascii_segoe_ui_symbol_pawn_b;
			case ID_PIECE_W_KING:
				return R.drawable.ascii_segoe_ui_symbol_king_w;
			case ID_PIECE_W_QUEEN:
				return R.drawable.ascii_segoe_ui_symbol_queen_w;
			case ID_PIECE_W_ROOK:
				return R.drawable.ascii_segoe_ui_symbol_rook_w;
			case ID_PIECE_W_BISHOP:
				return R.drawable.ascii_segoe_ui_symbol_bishop_w;
			case ID_PIECE_W_KNIGHT:
				return R.drawable.ascii_segoe_ui_symbol_knight_w;
			case ID_PIECE_W_PAWN:
				return R.drawable.ascii_segoe_ui_symbol_pawn_w;
			default:
				throw new IllegalStateException("Illegal pieceID: " + pieceID);
		}
	}
}
