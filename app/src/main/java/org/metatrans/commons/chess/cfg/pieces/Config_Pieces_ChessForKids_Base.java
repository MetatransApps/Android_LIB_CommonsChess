package org.metatrans.commons.chess.cfg.pieces;


import android.content.Context;


public abstract class Config_Pieces_ChessForKids_Base extends Config_Pieces_BaseImpl {


	public Config_Pieces_ChessForKids_Base(Context _context) {
		super(_context);
	}


	@Override
	public float getPieceHeightScaleFactor(int pieceID) {

		switch(pieceID) {
			case ID_PIECE_B_KING:
				return 1f;
			case ID_PIECE_B_QUEEN:
				return 1f;
			case ID_PIECE_B_ROOK:
				return 0.93f;
			case ID_PIECE_B_BISHOP:
				return 1f;
			case ID_PIECE_B_KNIGHT:
				return 0.97f;
			case ID_PIECE_B_PAWN:
				return 0.77f;
			case ID_PIECE_W_KING:
				return 1f;
			case ID_PIECE_W_QUEEN:
				return 1f;
			case ID_PIECE_W_ROOK:
				return 0.93f;
			case ID_PIECE_W_BISHOP:
				return 1f;
			case ID_PIECE_W_KNIGHT:
				return 0.97f;
			case ID_PIECE_W_PAWN:
				return 0.77f;
			default:
				throw new IllegalStateException("Illegal pieceID: " + pieceID);
		}
	}


	@Override
	public float getPieceWidthScaleFactor(int pieceID) {

		switch(pieceID) {
			case ID_PIECE_B_KING:
				return 0.77f;
			case ID_PIECE_B_QUEEN:
				return 0.77f;
			case ID_PIECE_B_ROOK:
				return 0.85f;
			case ID_PIECE_B_BISHOP:
				return 0.5f;
			case ID_PIECE_B_KNIGHT:
				return 0.77f;
			case ID_PIECE_B_PAWN:
				return 0.77f;
			case ID_PIECE_W_KING:
				return 0.77f;
			case ID_PIECE_W_QUEEN:
				return 0.77f;
			case ID_PIECE_W_ROOK:
				return 0.85f;
			case ID_PIECE_W_BISHOP:
				return 0.5f;
			case ID_PIECE_W_KNIGHT:
				return 0.77f;
			case ID_PIECE_W_PAWN:
				return 0.77f;
			default:
				throw new IllegalStateException("Illegal pieceID: " + pieceID);
		}
	}
}
