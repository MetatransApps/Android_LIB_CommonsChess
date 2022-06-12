package org.metatrans.commons.chess.cfg.pieces;


import org.metatrans.commons.chess.R;

import android.content.Context;


public class Config_Pieces_PaintGosts extends Config_Pieces_BaseImpl {
	
	
	public Config_Pieces_PaintGosts(Context _context) {
		super(_context);
	}
	
	
	@Override
	public int getID() {
		return CFG_PIECES_CUSTOM_1;
	}
	
	
	@Override
	public int getName() {
		return R.string.pieces_scheme_custom1;
	}
	
	
	@Override
	public int getIconResID() {
		return R.drawable.ic_pieces_gosts;
	}
	
	
	@Override
	public int getBitmapResID(int pieceID) {
		switch(pieceID) {
			case ID_PIECE_B_KING:
				return R.drawable.set2_king_b;
			case ID_PIECE_B_QUEEN:
				return R.drawable.set2_queen_b;
			case ID_PIECE_B_ROOK:
				return R.drawable.set2_rook_b;
			case ID_PIECE_B_BISHOP:
				return R.drawable.set2_bishop_b;
			case ID_PIECE_B_KNIGHT:
				return R.drawable.set2_knight_b;
			case ID_PIECE_B_PAWN:
				return R.drawable.set2_pawn_b;
			case ID_PIECE_W_KING:
				return R.drawable.set2_king_w;
			case ID_PIECE_W_QUEEN:
				return R.drawable.set2_queen_w;
			case ID_PIECE_W_ROOK:
				return R.drawable.set2_rook_w;
			case ID_PIECE_W_BISHOP:
				return R.drawable.set2_bishop_w;
			case ID_PIECE_W_KNIGHT:
				return R.drawable.set2_knight_w;
			case ID_PIECE_W_PAWN:
				return R.drawable.set2_pawn_w;
			default:
				throw new IllegalStateException("Illegal pieceID: " + pieceID);
		}
	}
}
