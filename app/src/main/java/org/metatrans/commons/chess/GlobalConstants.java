package org.metatrans.commons.chess;


public interface GlobalConstants {
	
	public static final String PERSISTENCY_KEY_GAMEDATA 			= "gamedata";
	public static final String PERSISTENCY_KEY_UI_SELECTIONS 		= "boardview.selections";
	public static final String PERSISTENCY_KEY_USER_DATA 			= "user.data";
	
	public static final int PLAYER_TYPE_HUMAN			 			= 1;
	public static final int PLAYER_TYPE_COMPUTER		 			= 2;
	
	public static final int GAME_STATUS_NONE			 			= 1;
	public static final int GAME_STATUS_WHITE_WINS		 			= 2;
	public static final int GAME_STATUS_BLACK_WINS		 			= 3;
	public static final int GAME_STATUS_DRAW			 			= 4;
	
	public static final int ICON_FULL_SIZE							= 150;
}
