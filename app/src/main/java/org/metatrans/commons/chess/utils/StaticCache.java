package org.metatrans.commons.chess.utils;


import static org.metatrans.commons.chess.GlobalConstants.PLAYER_TYPE_COMPUTER;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.cfg.difficulty.IConfigurationDifficulty;
import org.metatrans.commons.chess.cfg.rules.IConfigurationRule;
import org.metatrans.commons.chess.logic.BoardConstants;
import org.metatrans.commons.chess.logic.BoardManager_AllRules;
import org.metatrans.commons.chess.logic.GameDataUtils;
import org.metatrans.commons.chess.logic.IBoardManager;
import org.metatrans.commons.chess.logic.computer.ComputerPlayer_BaseImpl;
import org.metatrans.commons.chess.model.GameData;
import org.metatrans.commons.chess.model.Move;
import org.metatrans.commons.chess.model.UserSettings;
import org.metatrans.commons.storage.ObjectUtils;


public class StaticCache {
	

	private static IBoardManager cached_manager_allrules;


	private static boolean initialized;
	
	
	public static void initBoardManagersClasses_AllRulesOnly() {
		
		synchronized (StaticCache.class) {
			
			if (initialized) {
				return;
			}
			
			try {
				
				System.out.println("Creating board manager: ...");
	
				initClasses_AllRules();
				
				System.out.println("Creating board manager: done");
				
				initialized = true;

				cached_manager_allrules = null;
				
				System.gc();
				
			} catch (Exception e) {

				e.printStackTrace();

			} finally {

				//Do nothing
			}
		}
	}
	
	
	public static void initBoardManagersClasses() {

		GameData gamedata = (GameData) Application_Base.getInstance().getGameData();
		UserSettings userSettings = (UserSettings) Application_Base.getInstance().getUserSettings();

		synchronized (StaticCache.class) {
			
			if (initialized) {

				return;
			}
			
			try {
				
				System.out.println("Creating all board managers: ...");
	
				initClasses_AllRules();
				
				System.out.println("Creating all board managers: done");
				
				initialized = true;

				cached_manager_allrules = null;
				
				System.gc();
				
			} catch (Exception e) {

				e.printStackTrace();

			} finally {

				//Do nothing
			}
		}
	}


	private static void initClasses_AllRules() throws IOException, ClassNotFoundException {

		GameData newdata = createGameDataForNewGame(IConfigurationRule.BOARD_MANAGER_ID_ALL_RULES, IConfigurationDifficulty.MODE_COMPUTER_POSITIONAL_EVALUATION);
		newdata.setBoardManagerID(IConfigurationRule.BOARD_MANAGER_ID_ALL_RULES);
		cached_manager_allrules = new BoardManager_AllRules(newdata);
		waitInitialization(cached_manager_allrules);
		System.out.println("AllRules manager: done");
	}
	
	
	private static void waitInitialization(IBoardManager manager) {
		
		//Wait init
		List<Move> all = new ArrayList<Move>();
		int[][] pieces = manager.getBoard_Full();
		for (int letter=0; letter<8; letter++) {
			for (int digit=0; digit<8; digit++) {
				if (pieces[letter][digit] != BoardConstants.ID_PIECE_NONE) {
					all.addAll(manager.selectToFields(letter, digit));
				}
			}
		}
		
		for (Move move: all) {
			((ComputerPlayer_BaseImpl)manager.getComputerWhite()).getMoveScores(move);	
		}
	}
	
	
	private static GameData createGameDataForNewGame(int boardManagerID, int computerModeID) {
		GameData data = GameDataUtils.createGameDataForNewGame(PLAYER_TYPE_COMPUTER, PLAYER_TYPE_COMPUTER, boardManagerID, computerModeID);
		return data;
	}
}
