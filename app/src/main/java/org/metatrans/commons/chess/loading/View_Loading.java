package org.metatrans.commons.chess.loading;


import android.content.Context;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

import org.metatrans.commons.chess.R;
import org.metatrans.commons.chess.cfg.pieces.ConfigurationUtils_Pieces;
import org.metatrans.commons.chess.cfg.pieces.IConfigurationPieces;
import org.metatrans.commons.chess.logic.BoardConstants;
import org.metatrans.commons.chess.utils.CachesBitmap;
import org.metatrans.commons.loading.View_Loading_Base;


public class View_Loading extends View_Loading_Base {
	
	
	private Bitmap[] bitmap_commons;
	
	
	public View_Loading(Context context) {
		
		super(context);
	}
	
	
	@Override
	protected Bitmap[] getCommonBitmaps() {
		return bitmap_commons;
	}


	@Override
	protected Bitmap getBitmapBackground() {
		return null;
	}


	@Override
	public void initPiecesBitmaps() {
		
		bitmap_commons = new Bitmap[] {
				getImageBitmap(R.drawable.ic_computer_moving),
				getImageBitmap(R.drawable.ic_logo_cafk),
		};
		
		List<Bitmap> bitmap_others_arr = new ArrayList<Bitmap>();
		
		IConfigurationPieces[] piecesCfgs = ConfigurationUtils_Pieces.getAll();
		
		for (int i=0; i<piecesCfgs.length; i++) {
			
			IConfigurationPieces piecesCfg = piecesCfgs[i]; 
			
			//Includes IDs: 1,3,4,5,
			
			if (piecesCfg.getID() == IConfigurationPieces.CFG_PIECES_CUSTOM_2
					|| piecesCfg.getID() == IConfigurationPieces.CFG_PIECES_CUSTOM_6
					|| piecesCfg.getID() == IConfigurationPieces.CFG_PIECES_CUSTOM_7
					|| piecesCfg.getID() == IConfigurationPieces.CFG_PIECES_CUSTOM_8
					|| piecesCfg.getID() == IConfigurationPieces.CFG_PIECES_ASCII_droid_sans_fallback_1
					|| piecesCfg.getID() == IConfigurationPieces.CFG_PIECES_ASCII_droid_sans_fallback_2
					|| piecesCfg.getID() == IConfigurationPieces.CFG_PIECES_ASCII_arial_unicode_ms
					|| piecesCfg.getID() == IConfigurationPieces.CFG_PIECES_ASCII_code2000
					|| piecesCfg.getID() == IConfigurationPieces.CFG_PIECES_ASCII_dejavu_sans
					|| piecesCfg.getID() == IConfigurationPieces.CFG_PIECES_ASCII_freeserif
					|| piecesCfg.getID() == IConfigurationPieces.CFG_PIECES_ASCII_segoe_ui_symbol
					|| piecesCfg.getID() == IConfigurationPieces.CFG_PIECES_ASCII_y_oz_font
					) {
				continue;
			}		
			
			bitmap_others_arr.add(getPieceBitmap(piecesCfg, BoardConstants.ID_PIECE_W_KING));
			createEntry(bitmap_others_arr.get(bitmap_others_arr.size() - 1));
			bitmap_others_arr.add(getPieceBitmap(piecesCfg, BoardConstants.ID_PIECE_W_QUEEN));
			createEntry(bitmap_others_arr.get(bitmap_others_arr.size() - 1));
			bitmap_others_arr.add(getPieceBitmap(piecesCfg, BoardConstants.ID_PIECE_W_BISHOP));
			createEntry(bitmap_others_arr.get(bitmap_others_arr.size() - 1));
			bitmap_others_arr.add(getPieceBitmap(piecesCfg, BoardConstants.ID_PIECE_W_KNIGHT));
			createEntry(bitmap_others_arr.get(bitmap_others_arr.size() - 1));
			bitmap_others_arr.add(getPieceBitmap(piecesCfg, BoardConstants.ID_PIECE_W_ROOK));
			createEntry(bitmap_others_arr.get(bitmap_others_arr.size() - 1));
			bitmap_others_arr.add(getPieceBitmap(piecesCfg, BoardConstants.ID_PIECE_W_PAWN));
			createEntry(bitmap_others_arr.get(bitmap_others_arr.size() - 1));
			
			bitmap_others_arr.add(getPieceBitmap(piecesCfg, BoardConstants.ID_PIECE_B_KING));
			createEntry(bitmap_others_arr.get(bitmap_others_arr.size() - 1));
			bitmap_others_arr.add(getPieceBitmap(piecesCfg, BoardConstants.ID_PIECE_B_QUEEN));
			createEntry(bitmap_others_arr.get(bitmap_others_arr.size() - 1));
			bitmap_others_arr.add(getPieceBitmap(piecesCfg, BoardConstants.ID_PIECE_B_BISHOP));
			createEntry(bitmap_others_arr.get(bitmap_others_arr.size() - 1));
			bitmap_others_arr.add(getPieceBitmap(piecesCfg, BoardConstants.ID_PIECE_B_KNIGHT));
			createEntry(bitmap_others_arr.get(bitmap_others_arr.size() - 1));
			bitmap_others_arr.add(getPieceBitmap(piecesCfg, BoardConstants.ID_PIECE_B_ROOK));
			createEntry(bitmap_others_arr.get(bitmap_others_arr.size() - 1));
			bitmap_others_arr.add(getPieceBitmap(piecesCfg, BoardConstants.ID_PIECE_B_PAWN));
			createEntry(bitmap_others_arr.get(bitmap_others_arr.size() - 1));
		}
	}
	
	
	protected Bitmap getPieceBitmap(IConfigurationPieces piecesCfg, int pieceID) {
		
		int imageResID = piecesCfg.getBitmapResID(pieceID);
		Bitmap bitmap = CachesBitmap.getSingletonPiecesBoard((int) getSquareSize()).getBitmap(getContext(), imageResID);
		if (bitmap == null) {
			piecesCfg.getPiece(pieceID); //This call will be added into the cache
			bitmap = CachesBitmap.getSingletonPiecesBoard((int) getSquareSize()).getBitmap(getContext(), imageResID);	
		}
		return bitmap;
	}
	
	
	protected Bitmap getImageBitmap(int imageResID) {
		return getImageBitmap(imageResID, 1);
	}
	
	
	private Bitmap getImageBitmap(int imageResID, int icon_size_multy) {
		Bitmap bitmap = CachesBitmap.getSingletonPiecesBoard((int) (icon_size_multy * getSquareSize())).getBitmap(getContext(), imageResID);
		if (bitmap == null) {
			bitmap = CachesBitmap.getSingletonFullSized().getBitmap(getContext(), imageResID);
			bitmap = CachesBitmap.getSingletonPiecesBoard((int) getSquareSize()).getBitmap(getContext(), imageResID);	
		}
		return bitmap;
	}
}
