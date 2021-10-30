package org.metatrans.commons.chess.cfg.rules;

import org.metatrans.commons.chess.cfg.IConfigurationEntry;


public interface IConfigurationRule extends IConfigurationEntry {
	
	//public static final int BOARD_MANAGER_ID_FREESTYLE 				= 1;
	public static final int BOARD_MANAGER_ID_FREESTYLE_IN_SERIES 	= 2;
	public static final int BOARD_MANAGER_ID_PIECES_AWARE 			= 3;
	public static final int BOARD_MANAGER_ID_ALL_RULES	 			= 4;
	public static final int BOARD_MANAGER_ID_MAX		 			= BOARD_MANAGER_ID_ALL_RULES;
	
	public int getDescription();
}
