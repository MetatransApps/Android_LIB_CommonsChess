package org.metatrans.commons.chess.menu;


import android.content.Context;
import android.graphics.Bitmap;

import org.metatrans.commons.chess.GlobalConstants;
import org.metatrans.commons.chess.cfg.pieces.IConfigurationPieces;
import org.metatrans.commons.chess.utils.CachesBitmap;
import org.metatrans.commons.ui.utils.BitmapUtils;


public class MenuActivity_BasePieces extends MenuActivity_Base implements GlobalConstants {
	
	
	protected void ensureBitmapExists(int iconResID, IConfigurationPieces pieceCfg, int pieceID, int bcolour) {
		
		/*Bitmap bitmap = CachesBitmap.getSingletonFullSized().getBitmap(this, iconResID);
		
		if (bitmap == null) {

			bitmap = createIconBitmap(this, pieceCfg, GlobalConstants.ICON_FULL_SIZE, pieceID, bcolour);

			bitmap = BitmapUtils.cropTransparantPart(bitmap);

			bitmap = BitmapUtils.generateTransparantPart(bitmap, pieceCfg.getPieceHeightScaleFactor(pieceID));

			CachesBitmap.getSingletonFullSized().addBitmap(iconResID, bitmap);
		}
		*/
	}
	
	
	protected Bitmap createIconBitmap(Context context, IConfigurationPieces piecesCfg, int icon_size, int piece_type, int bcolour) {
		
		Bitmap bitmap_background = BitmapUtils.createFromColour(icon_size, bcolour);

		Bitmap bitmap_piece = piecesCfg.getPiece(piece_type);

        bitmap_piece = BitmapUtils.createScaledBitmap(bitmap_piece, icon_size, icon_size);
		
		Bitmap result = BitmapUtils.overlay(bitmap_background, bitmap_piece);
		
		return result;
	}
}
