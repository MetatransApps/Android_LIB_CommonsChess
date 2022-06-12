package org.metatrans.commons.chess.cfg.pieces;


import org.metatrans.commons.chess.logic.BoardConstants;
import org.metatrans.commons.chess.utils.CachesBitmap;
import org.metatrans.commons.ui.images.BitmapCacheBase;

import android.content.Context;
import android.graphics.Bitmap;


public abstract class Config_Pieces_BaseImpl implements IConfigurationPieces, BoardConstants {
	
	
	private Context context;


	public Config_Pieces_BaseImpl(Context _context) {

		context = _context;
	}
	
	
	protected Context getContext() {
		return context;
	}
	
	
	@Override
	public Bitmap getPiece(int pieceID) {

		int bitmapResID = getBitmapResID(pieceID);

		return ((BitmapCacheBase) CachesBitmap.getSingletonFullSized()).getBitmap(
				getContext(),
				bitmapResID,
				getPieceHeightScaleFactor(pieceID),
				getPieceWidthScaleFactor(pieceID)
		);
	}


	@Override
	public float getPieceHeightScaleFactor(int pieceID) {

		/*if (true) {

			//return 0.75f;
			return 1f;
		}*/


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
				return 0.93f;
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
				return 0.93f;
			default:
				throw new IllegalStateException("Illegal pieceID: " + pieceID);
		}
	}


	public float getPieceWidthScaleFactor(int pieceID) {

		switch(pieceID) {
			case ID_PIECE_B_KING:
				return 0.83f;
			case ID_PIECE_B_QUEEN:
				return 0.85f;
			case ID_PIECE_B_ROOK:
				return 0.71f;
			case ID_PIECE_B_BISHOP:
				return 0.83f;
			case ID_PIECE_B_KNIGHT:
				return 0.8f;
			case ID_PIECE_B_PAWN:
				return 0.61f;
			case ID_PIECE_W_KING:
				return 0.83f;
			case ID_PIECE_W_QUEEN:
				return 0.85f;
			case ID_PIECE_W_ROOK:
				return 0.71f;
			case ID_PIECE_W_BISHOP:
				return 0.83f;
			case ID_PIECE_W_KNIGHT:
				return 0.8f;
			case ID_PIECE_W_PAWN:
				return 0.61f;
			default:
				throw new IllegalStateException("Illegal pieceID: " + pieceID);
		}
	}
}
