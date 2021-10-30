package org.metatrans.commons.chess.cfg.pieces;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import org.metatrans.commons.chess.GlobalConstants;
import org.metatrans.commons.chess.R;
import org.metatrans.commons.chess.utils.CachesBitmap;
import org.metatrans.commons.ui.utils.BitmapUtils;


public class Config_Pieces_ASCII_droid_sans_fallback_2 extends Config_PiecesBase {


	private static String UNICODE_WHITE_KING 	= "\u2654";
	private static String UNICODE_WHITE_QUEEN 	= "\u2655";
	private static String UNICODE_WHITE_ROOK 	= "\u2656";
	private static String UNICODE_WHITE_BISHOP 	= "\u2657";
	private static String UNICODE_WHITE_KNIGHT 	= "\u2658";
	private static String UNICODE_WHITE_PAWN 	= "\u2659";
	
	
	public Config_Pieces_ASCII_droid_sans_fallback_2(Context _context) {
		super(_context);
	}
	
	
	@Override
	public int getID() {
		return CFG_PIECES_ASCII_droid_sans_fallback_2;
	}
	
	
	@Override
	public int getName() {
		return R.string.pieces_scheme_ascii_droid_sans_fallback_2;
	}
	
	
	@Override
	public int getIconResID() {
		return R.drawable.ic_pieces_ascii_droid_sans_fallback_2;
	}
	
	
	@Override
	public int getBitmapResID(int pieceID) {
		switch(pieceID) {
			case ID_PIECE_B_KING:
				return R.drawable.piece_ascii2_king_b;
			case ID_PIECE_B_QUEEN:
				return R.drawable.piece_ascii2_queen_b;
			case ID_PIECE_B_ROOK:
				return R.drawable.piece_ascii2_rook_b;
			case ID_PIECE_B_BISHOP:
				return R.drawable.piece_ascii2_bishop_b;
			case ID_PIECE_B_KNIGHT:
				return R.drawable.piece_ascii2_knight_b;
			case ID_PIECE_B_PAWN:
				return R.drawable.piece_ascii2_pawn_b;
			case ID_PIECE_W_KING:
				return R.drawable.piece_ascii2_king_w;
			case ID_PIECE_W_QUEEN:
				return R.drawable.piece_ascii2_queen_w;
			case ID_PIECE_W_ROOK:
				return R.drawable.piece_ascii2_rook_w;
			case ID_PIECE_W_BISHOP:
				return R.drawable.piece_ascii2_bishop_w;
			case ID_PIECE_W_KNIGHT:
				return R.drawable.piece_ascii2_knight_w;
			case ID_PIECE_W_PAWN:
				return R.drawable.piece_ascii2_pawn_w;
			default:
				throw new IllegalStateException("Illegal pieceID: " + pieceID);
		}
	}
	
	
	@Override
	public Bitmap getPiece(int pieceID) {
		
		int bitmapResID = getBitmapResID(pieceID);
		
		Bitmap bitmap = CachesBitmap.getSingletonFullSized().getBitmap(getContext(), bitmapResID);
		if (bitmap == null) {
			String ascii = getASCII(pieceID);
			int size = GlobalConstants.ICON_FULL_SIZE;// - GlobalConstants.ICON_FULL_SIZE / 10;
			bitmap = BitmapUtils.createFromText(size, ascii, getColour_Piece(pieceID));
			CachesBitmap.getSingletonFullSized().addBitmap(bitmapResID, bitmap);
		}
		
		return bitmap;
	}
	
	
	public String getASCII(int pieceID) {
		switch(pieceID) {
			case ID_PIECE_B_KING:
				return UNICODE_WHITE_KING;
			case ID_PIECE_B_QUEEN:
				return UNICODE_WHITE_QUEEN;
			case ID_PIECE_B_ROOK:
				return UNICODE_WHITE_ROOK;
			case ID_PIECE_B_BISHOP:
				return UNICODE_WHITE_BISHOP;
			case ID_PIECE_B_KNIGHT:
				return UNICODE_WHITE_KNIGHT;
			case ID_PIECE_B_PAWN:
				return UNICODE_WHITE_PAWN;
			case ID_PIECE_W_KING:
				return UNICODE_WHITE_KING;
			case ID_PIECE_W_QUEEN:
				return UNICODE_WHITE_QUEEN;
			case ID_PIECE_W_ROOK:
				return UNICODE_WHITE_ROOK;
			case ID_PIECE_W_BISHOP:
				return UNICODE_WHITE_BISHOP;
			case ID_PIECE_W_KNIGHT:
				return UNICODE_WHITE_KNIGHT;
			case ID_PIECE_W_PAWN:
				return UNICODE_WHITE_PAWN;
			
			default:
				throw new IllegalStateException("Illegal pieceID: " + pieceID);
		}
	}
	
	
	private int getColour_Piece(int pieceID) {
		switch(pieceID) {
			case ID_PIECE_NONE:
				return 0;
			case ID_PIECE_B_KING:
				return Color.BLACK;
			case ID_PIECE_B_QUEEN:
				return Color.BLACK;
			case ID_PIECE_B_ROOK:
				return Color.BLACK;
			case ID_PIECE_B_BISHOP:
				return Color.BLACK;
			case ID_PIECE_B_KNIGHT:
				return Color.BLACK;
			case ID_PIECE_B_PAWN:
				return Color.BLACK;
			case ID_PIECE_W_KING:
				return Color.WHITE;
			case ID_PIECE_W_QUEEN:
				return Color.WHITE;
			case ID_PIECE_W_ROOK:
				return Color.WHITE;
			case ID_PIECE_W_BISHOP:
				return Color.WHITE;
			case ID_PIECE_W_KNIGHT:
				return Color.WHITE;
			case ID_PIECE_W_PAWN:
				return Color.WHITE;
			default:
				throw new IllegalStateException("Illegal pieceID: " + pieceID);
		}
	}
}
