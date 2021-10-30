package org.metatrans.commons.chess.cfg.pieces;


import org.metatrans.commons.chess.logic.BoardConstants;
import org.metatrans.commons.chess.utils.CachesBitmap;

import android.content.Context;
import android.graphics.Bitmap;


abstract class Config_PiecesBase implements IConfigurationPieces, BoardConstants {
	
	
	private Context context;
	
	
	public Config_PiecesBase(Context _context) {
		context = _context;
	}
	
	
	protected Context getContext() {
		return context;
	}
	
	
	@Override
	public Bitmap getPiece(int pieceID) {
		return CachesBitmap.getSingletonFullSized().getBitmap(getContext(), getBitmapResID(pieceID));
	}
}
