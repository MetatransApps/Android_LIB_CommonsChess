package org.metatrans.commons.chess.cfg.pieces;


import org.metatrans.commons.chess.cfg.IConfigurationEntry;

import android.graphics.Bitmap;


public interface IConfigurationPieces extends IConfigurationEntry {
	
	
	public static int CFG_PIECES_ASCII_droid_sans_fallback_1	= 1;
	public static int CFG_PIECES_ASCII_droid_sans_fallback_2	= 2;
	public static int CFG_PIECES_CUSTOM_1		 				= 3;
	public static int CFG_PIECES_CUSTOM_2		 				= 4;
	public static int CFG_PIECES_CUSTOM_3		 				= 5;
	public static int CFG_PIECES_CUSTOM_4		 				= 6;
	public static int CFG_PIECES_CUSTOM_5		 				= 7;
	public static int CFG_PIECES_CUSTOM_6		 				= 8;
	public static int CFG_PIECES_CUSTOM_7		 				= 9;
	public static int CFG_PIECES_CUSTOM_8		 				= 10;
	public static int CFG_PIECES_ASCII_dejavu_sans				= 11;
	public static int CFG_PIECES_ASCII_code2000					= 12;
	public static int CFG_PIECES_ASCII_arial_unicode_ms			= 13;
	public static int CFG_PIECES_ASCII_freeserif				= 14;
	public static int CFG_PIECES_ASCII_segoe_ui_symbol			= 15;
	public static int CFG_PIECES_ASCII_y_oz_font				= 16;
	public static int CFG_PIECES_BAGATURS_V1					= 17;
	public static int CFG_PIECES_BAGATURS_V2					= 18;


	public int getBitmapResID(int pieceID);

	public Bitmap getPiece(int pieceID);

	public float getPieceHeightScaleFactor(int pieceID);

	public float getPieceWidthScaleFactor(int pieceID);
}
