package org.metatrans.commons.chess.cfg.pieces;


import org.metatrans.commons.chess.R;

import android.content.Context;


public class Config_Pieces_PaintClassic extends Config_PiecesBase {
	
	
	public Config_Pieces_PaintClassic(Context _context) {
		super(_context);
	}
	
	
	@Override
	public int getID() {
		return CFG_PIECES_CUSTOM_2;
	}

	
	@Override
	public int getName() {
		return R.string.pieces_scheme_custom2;
	}
	
	
	@Override
	public int getIconResID() {
		return R.drawable.ic_pieces_classic;
	}
	
	
	@Override
	public int getBitmapResID(int pieceID) {
		switch(pieceID) {
			case ID_PIECE_B_KING:
				return R.drawable.set1_kdt60;
			case ID_PIECE_B_QUEEN:
				return R.drawable.set1_qdt60;
			case ID_PIECE_B_ROOK:
				return R.drawable.set1_rdt60;
			case ID_PIECE_B_BISHOP:
				return R.drawable.set1_bdt60;
			case ID_PIECE_B_KNIGHT:
				return R.drawable.set1_ndt60;
			case ID_PIECE_B_PAWN:
				return R.drawable.set1_pdt60;
			case ID_PIECE_W_KING:
				return R.drawable.set1_klt60;
			case ID_PIECE_W_QUEEN:
				return R.drawable.set1_qlt60;
			case ID_PIECE_W_ROOK:
				return R.drawable.set1_rlt60;
			case ID_PIECE_W_BISHOP:
				return R.drawable.set1_blt60;
			case ID_PIECE_W_KNIGHT:
				return R.drawable.set1_nlt60;
			case ID_PIECE_W_PAWN:
				return R.drawable.set1_plt60;
			default:
				throw new IllegalStateException("Illegal pieceID: " + pieceID);
		}
	}
}
