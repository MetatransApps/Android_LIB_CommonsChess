package org.metatrans.commons.chess.events;


import org.metatrans.commons.events.Event_Base;
import org.metatrans.commons.events.api.IEvent_Base;


public interface IEvent extends IEvent_Base {
	
	
	/*
	 * Categories
	 */	
	public static final int CHANGE_AUTO								= 50;
	
	
	/*
	 * Sub categories
	 */
	public static final int EXIT_GAME_RULES		 					= 2;
	public static final int EXIT_GAME_PIECES						= 5;
	public static final int EXIT_GAME_PLAYERS						= 6;
	
	public static final int MENU_OPERATION_CHANGE_RULES		 		= 5;
	public static final int MENU_OPERATION_CHANGE_PIECES			= 6;
	
	public static final int CHANGE_AUTO_W_HUMAN						= 1;
	public static final int CHANGE_AUTO_W_COMPUTER					= 2;
	public static final int CHANGE_AUTO_B_HUMAN						= 3;
	public static final int CHANGE_AUTO_B_COMPUTER					= 4;
	
	public static final int WIN_GAME_FREESTYLE_L1 					= 1;
	public static final int WIN_GAME_FREESTYLE_L2 					= 2;
	public static final int WIN_GAME_PIECESAWARE_L1 				= 3;
	public static final int WIN_GAME_PIECESAWARE_L2 				= 4;
	public static final int WIN_GAME_PIECESAWARE_L3 				= 5;
	public static final int WIN_GAME_PIECESAWARE_L4 				= 6;
	public static final int WIN_GAME_ALLRULES_L1 					= 7;
	public static final int WIN_GAME_ALLRULES_L2 					= 8;
	public static final int WIN_GAME_ALLRULES_L3 					= 9;
	public static final int WIN_GAME_ALLRULES_L4 					= 10;
	public static final int WIN_GAME_ALLRULES_L5 					= 11;
	public static final int WIN_GAME_ALLRULES_L6 					= 12;
	public static final int WIN_GAME_ALLRULES_L7 					= 13;
	public static final int WIN_GAME_ALLRULES_L8 					= 14;
	public static final int WIN_GAME_ALLRULES_L9 					= 15;
	public static final int WIN_GAME_ALLRULES_L10 					= 16;
	
	/*
	 * Sub-Sub categories
	 */
	public static final int EXIT_GAME_PLAYERS_HH					= 1;
	public static final int EXIT_GAME_PLAYERS_HC					= 2;
	public static final int EXIT_GAME_PLAYERS_CC					= 3;
	public static final int EXIT_GAME_PLAYERS_CH					= 4;


	/*
	 * Static events
	 */
	IEvent_Base EVENT_MENU_OPERATION_CHANGE_PIECES = new Event_Base(
			MENU_OPERATION, MENU_OPERATION_CHANGE_PIECES,
			STR_MENU_OPERATION, "CHANGE_PIECES"
	);

	IEvent_Base EVENT_MENU_OPERATION_CHANGE_RULES = new Event_Base(
			MENU_OPERATION, MENU_OPERATION_CHANGE_RULES,
			STR_MENU_OPERATION, "CHANGE_RULES"
	);

	IEvent_Base EVENT_GAME_WIN_FREESTYLE_L1 = new Event_Base(
			WIN_GAME, WIN_GAME_FREESTYLE_L1,
			STR_WIN_GAME, "FREESTYLE_L1"
	);

	IEvent_Base EVENT_GAME_WIN_FREESTYLE_L2 = new Event_Base(
			WIN_GAME, WIN_GAME_FREESTYLE_L2,
			STR_WIN_GAME, "FREESTYLE_L2"
	);

	IEvent_Base EVENT_GAME_WIN_PIECESAWARE_L1 = new Event_Base(
			WIN_GAME, WIN_GAME_PIECESAWARE_L1,
			STR_WIN_GAME, "PIECESAWARE_L1"
	);

	IEvent_Base EVENT_GAME_WIN_PIECESAWARE_L2 = new Event_Base(
			WIN_GAME, WIN_GAME_PIECESAWARE_L2,
			STR_WIN_GAME, "PIECESAWARE_L2"
	);

	IEvent_Base EVENT_GAME_WIN_PIECESAWARE_L3 = new Event_Base(
			WIN_GAME, WIN_GAME_PIECESAWARE_L3,
			STR_WIN_GAME, "PIECESAWARE_L3"
	);

	IEvent_Base EVENT_GAME_WIN_PIECESAWARE_L4 = new Event_Base(
			WIN_GAME, WIN_GAME_PIECESAWARE_L4,
			STR_WIN_GAME, "PIECESAWARE_L4"
	);

	IEvent_Base EVENT_GAME_WIN_ALLRULES_L1 = new Event_Base(
			WIN_GAME, WIN_GAME_ALLRULES_L1,
			STR_WIN_GAME, "ALLRULES_L1"
	);

	IEvent_Base EVENT_GAME_WIN_ALLRULES_L2 = new Event_Base(
			WIN_GAME, WIN_GAME_ALLRULES_L2,
			STR_WIN_GAME, "ALLRULES_L2"
	);

	IEvent_Base EVENT_GAME_WIN_ALLRULES_L3 = new Event_Base(
			WIN_GAME, WIN_GAME_ALLRULES_L3,
			STR_WIN_GAME, "ALLRULES_L3"
	);

	IEvent_Base EVENT_GAME_WIN_ALLRULES_L4 = new Event_Base(
			WIN_GAME, WIN_GAME_ALLRULES_L4,
			STR_WIN_GAME, "ALLRULES_L4"
	);

	IEvent_Base EVENT_GAME_WIN_ALLRULES_L5 = new Event_Base(
			WIN_GAME, WIN_GAME_ALLRULES_L5,
			STR_WIN_GAME, "ALLRULES_L5"
	);

	IEvent_Base EVENT_GAME_WIN_ALLRULES_L6 = new Event_Base(
			WIN_GAME, WIN_GAME_ALLRULES_L6,
			STR_WIN_GAME, "ALLRULES_L6"
	);

	IEvent_Base EVENT_GAME_WIN_ALLRULES_L7 = new Event_Base(
			WIN_GAME, WIN_GAME_ALLRULES_L7,
			STR_WIN_GAME, "ALLRULES_L7"
	);

	IEvent_Base EVENT_GAME_WIN_ALLRULES_L8 = new Event_Base(
			WIN_GAME, WIN_GAME_ALLRULES_L8,
			STR_WIN_GAME, "ALLRULES_L8"
	);

	IEvent_Base EVENT_GAME_WIN_ALLRULES_L9 = new Event_Base(
			WIN_GAME, WIN_GAME_ALLRULES_L9,
			STR_WIN_GAME, "ALLRULES_L9"
	);

	IEvent_Base EVENT_GAME_WIN_ALLRULES_L10 = new Event_Base(
			WIN_GAME, WIN_GAME_ALLRULES_L10,
			STR_WIN_GAME, "ALLRULES_L10"
	);
}
