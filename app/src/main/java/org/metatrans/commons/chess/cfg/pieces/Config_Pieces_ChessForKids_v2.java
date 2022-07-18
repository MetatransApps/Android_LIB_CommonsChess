package org.metatrans.commons.chess.cfg.pieces;


import android.content.Context;

import org.metatrans.commons.chess.R;


public class Config_Pieces_ChessForKids_v2 extends Config_Pieces_ChessForKids_Base {


	public Config_Pieces_ChessForKids_v2(Context _context) {
		super(_context);
	}
	
	
	@Override
	public int getID() {
		return CFG_PIECES_CHESSFORKIDS_V2;
	}
	
	
	@Override
	public int getName() {
		return R.string.pieces_scheme_chessforkids_v2;
	}
	
	
	@Override
	public int getIconResID() {
		return R.drawable.ic_pieces_chessforkids_v2;
	}
	
	
	@Override
	public int getBitmapResID(int pieceID) {
		switch(pieceID) {
			case ID_PIECE_B_KING:
				return R.drawable.chessforkids_v2_king_b;
			case ID_PIECE_B_QUEEN:
				return R.drawable.chessforkids_v2_queen_b;
			case ID_PIECE_B_ROOK:
				return R.drawable.chessforkids_v2_rook_b;
			case ID_PIECE_B_BISHOP:
				return R.drawable.chessforkids_v2_bishop_b;
			case ID_PIECE_B_KNIGHT:
				return R.drawable.chessforkids_v2_knight_b;
			case ID_PIECE_B_PAWN:
				return R.drawable.chessforkids_v2_pawn_b;
			case ID_PIECE_W_KING:
				return R.drawable.chessforkids_v2_king_w;
			case ID_PIECE_W_QUEEN:
				return R.drawable.chessforkids_v2_queen_w;
			case ID_PIECE_W_ROOK:
				return R.drawable.chessforkids_v2_rook_w;
			case ID_PIECE_W_BISHOP:
				return R.drawable.chessforkids_v2_bishop_w;
			case ID_PIECE_W_KNIGHT:
				return R.drawable.chessforkids_v2_knight_w;
			case ID_PIECE_W_PAWN:
				return R.drawable.chessforkids_v2_pawn_w;
			default:
				throw new IllegalStateException("Illegal pieceID: " + pieceID);
		}
	}
}
