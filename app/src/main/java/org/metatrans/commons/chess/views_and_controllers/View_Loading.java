package org.metatrans.commons.chess.views_and_controllers;


import android.content.Context;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.chess.R;
import org.metatrans.commons.chess.app.Application_Chess_BaseImpl;
import org.metatrans.commons.chess.cfg.pieces.ConfigurationUtils_Pieces;
import org.metatrans.commons.chess.cfg.pieces.IConfigurationPieces;
import org.metatrans.commons.chess.logic.BoardConstants;
import org.metatrans.commons.chess.utils.CachesBitmap;
import org.metatrans.commons.loading.View_Loading_Base;
import org.metatrans.commons.ui.images.BitmapCacheBase;


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

		int moving_computer_icon_id = ((Application_Chess_BaseImpl) Application_Base.getInstance()).getMovingComputerIconID();

		bitmap_commons = new Bitmap[] {
				getImageBitmap(moving_computer_icon_id),
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

		Bitmap bitmap = ((BitmapCacheBase) CachesBitmap.getSingletonPiecesBoard((int) getSquareSize())).getBitmap(
				getContext(),
				imageResID,
				piecesCfg.getPieceHeightScaleFactor(pieceID),
				piecesCfg.getPieceWidthScaleFactor(pieceID)
			);

		return bitmap;
	}
	

	protected Bitmap getImageBitmap(int imageResID) {

		return getImageBitmap(imageResID, 1, 1);
	}

	
	private Bitmap getImageBitmap(int imageResID, float scale_height, float scale_width) {

		int square_size = (int) getSquareSize();

		Bitmap bitmap = ((BitmapCacheBase) CachesBitmap.getSingletonPiecesBoard(square_size)).getBitmap(
				getContext(), imageResID, scale_height, scale_width);

		return bitmap;
	}
}
