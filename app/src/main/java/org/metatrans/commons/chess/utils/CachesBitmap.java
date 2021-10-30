package org.metatrans.commons.chess.utils;


import org.metatrans.commons.chess.R;
import org.metatrans.commons.chess.cfg.pieces.ConfigurationUtils_Pieces;
import org.metatrans.commons.chess.cfg.pieces.IConfigurationPieces;
import org.metatrans.commons.ui.images.BitmapCacheBase;
import org.metatrans.commons.ui.images.BitmapCacheSized;
import org.metatrans.commons.ui.images.IBitmapCache;


public class CachesBitmap {
	
	
	private static BitmapCacheBase singleton_fullsized;
	private static BitmapCacheSized singleton_icons;
	private static BitmapCacheSized singleton_pieces_board;
	private static BitmapCacheSized singleton_pieces_panel1;
	private static BitmapCacheSized singleton_pieces_panel2;
	
	
	public static IBitmapCache getSingletonFullSized() {
		synchronized (CachesBitmap.class) {
			if (singleton_fullsized == null) {
				singleton_fullsized = new BitmapCacheBase(1);
				System.out.println("DrawableCaches: full sized cache created");
			}
			//System.out.println("DrawableCaches: full sized cache size is " + singleton_fullsized.size());
			return singleton_fullsized;
		}
	}
	
	
	public static IBitmapCache getSingletonIcons(int size) {
		synchronized (CachesBitmap.class) {
			if (singleton_icons == null) {
				singleton_icons = new BitmapCacheSized(size, getSingletonFullSized(), 100);
			} else if (singleton_icons.getBitmapsSize() != size) {
				System.out.println("DrawableCaches: re-create icons cache");
				singleton_icons = new BitmapCacheSized(size, getSingletonFullSized(), 100);
			}
			//System.out.println("DrawableCaches: icons cache size is " + singleton_icons.size());
			return singleton_icons;
		}
	}
	
	
	public static IBitmapCache getSingletonPiecesBoard(int size) {
		synchronized (CachesBitmap.class) {
			if (singleton_pieces_board == null) {
				singleton_pieces_board = new BitmapCacheSized(size, getSingletonFullSized(), 100);
			} else if (singleton_pieces_board.getBitmapsSize() != size) {
				System.out.println("DrawableCaches: re-create pieces (board) cache -> old_size=" + size + ", new_size=" + singleton_pieces_board.getBitmapsSize());
				singleton_pieces_board = new BitmapCacheSized(size, getSingletonFullSized(), 100);
			}
			//System.out.println("DrawableCaches: icons cache size is " + singleton_icons.size());
			return singleton_pieces_board;
		}
	}
	
	
	public static IBitmapCache getSingletonPiecesPanel1(int size) {
		synchronized (CachesBitmap.class) {
			if (singleton_pieces_panel1 == null) {
				singleton_pieces_panel1 = new BitmapCacheSized(size, getSingletonFullSized(), 100);
			} else if (singleton_pieces_panel1.getBitmapsSize() != size) {
				System.out.println("DrawableCaches: re-create pieces (panel1) cache");
				singleton_pieces_panel1 = new BitmapCacheSized(size, getSingletonFullSized(), 100);
			}
			//System.out.println("DrawableCaches: icons cache size is " + singleton_icons.size());
			return singleton_pieces_panel1;
		}
	}
	

	public static IBitmapCache getSingletonPiecesPanel2(int size) {
		synchronized (CachesBitmap.class) {
			if (singleton_pieces_panel2 == null) {
				singleton_pieces_panel2 = new BitmapCacheSized(size, getSingletonFullSized(), 100);
			} else if (singleton_pieces_panel2.getBitmapsSize() != size) {
				System.out.println("DrawableCaches: re-create pieces (panel2) cache");
				singleton_pieces_panel2 = new BitmapCacheSized(size, getSingletonFullSized(), 100);
			}
			//System.out.println("DrawableCaches: icons cache size is " + singleton_icons.size());
			return singleton_pieces_panel2;
		}
	}
	
	
	public static void clearAll() {
		clearFullSizedCache();
		clearIconsCache_Pieces();
		clearIconsCache_Promotion();
		clearPiecesCache();
	}
	
	
	public static void clearPiecesCache() {
		if (singleton_pieces_board != null) singleton_pieces_board.clear(); 
		if (singleton_pieces_panel1 != null) singleton_pieces_panel1.clear();
		if (singleton_pieces_panel1 != null) singleton_pieces_panel2.clear();
	}
	
	
	public static void clearFullSizedCache() {
		if (singleton_fullsized != null) singleton_fullsized.clear(); 
	}
	
	public static void clearIconsCache_Promotion() {
		if (singleton_icons != null) {
			singleton_icons.remove(R.drawable.ic_promotion_queen_w);
			singleton_icons.remove(R.drawable.ic_promotion_rook_w);
			singleton_icons.remove(R.drawable.ic_promotion_bishop_w);
			singleton_icons.remove(R.drawable.ic_promotion_knight_w);
			
			singleton_icons.remove(R.drawable.ic_promotion_queen_b);
			singleton_icons.remove(R.drawable.ic_promotion_rook_b);
			singleton_icons.remove(R.drawable.ic_promotion_bishop_b);
			singleton_icons.remove(R.drawable.ic_promotion_knight_b);
		}
	}
	
	
	public static void clearIconsCache_Pieces() {
		if (singleton_icons != null) { 
			IConfigurationPieces[] piecesCfg = ConfigurationUtils_Pieces.getAll();
			for (int i = 0; i < piecesCfg.length; i++) {
				IConfigurationPieces pieceCfg = piecesCfg[i];
				singleton_icons.remove(pieceCfg.getIconResID());
			}
		}
	}	
}
