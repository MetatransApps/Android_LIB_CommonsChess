package org.metatrans.commons.chess.cfg.pieces;


import org.metatrans.commons.chess.R;

import android.content.Context;


public class Config_Pieces_ClayLight extends Config_Pieces_BaseImpl {
	
	
	public Config_Pieces_ClayLight(Context _context) {
		super(_context);
	}
	
	
	@Override
	public int getID() {
		return CFG_PIECES_CUSTOM_3;
	}

	
	@Override
	public int getName() {
		return R.string.pieces_scheme_custom3;
	}
	
	
	@Override
	public int getIconResID() {
		return R.drawable.ic_pieces_claylight;
	}
	
	
	@Override
	public int getBitmapResID(int pieceID) {
		switch(pieceID) {
			case ID_PIECE_B_KING:
				return R.drawable.custom3_king_b;
			case ID_PIECE_B_QUEEN:
				return R.drawable.custom3_queen_b;
			case ID_PIECE_B_ROOK:
				return R.drawable.custom3_rook_b;
			case ID_PIECE_B_BISHOP:
				return R.drawable.custom3_bishop_b;
			case ID_PIECE_B_KNIGHT:
				return R.drawable.custom3_knight_b;
			case ID_PIECE_B_PAWN:
				return R.drawable.custom3_pawn_b;
			case ID_PIECE_W_KING:
				return R.drawable.custom3_king_w;
			case ID_PIECE_W_QUEEN:
				return R.drawable.custom3_queen_w;
			case ID_PIECE_W_ROOK:
				return R.drawable.custom3_rook_w;
			case ID_PIECE_W_BISHOP:
				return R.drawable.custom3_bishop_w;
			case ID_PIECE_W_KNIGHT:
				return R.drawable.custom3_knight_w;
			case ID_PIECE_W_PAWN:
				return R.drawable.custom3_pawn_w;
			default:
				throw new IllegalStateException("Illegal pieceID: " + pieceID);
		}
	}
}
