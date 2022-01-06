package org.metatrans.commons.chess.utils;


import org.metatrans.commons.chess.logic.board.IBoardManager;

import bagaturchess.bitboard.impl.Constants;


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


	private static void initClasses_AllRules() {

		bagaturchess.bitboard.api.BoardUtils.createBoard_WithPawnsCache(Constants.INITIAL_BOARD,
				bagaturchess.learning.goldmiddle.impl.cfg.bagatur.eval.BagaturPawnsEvalFactory.class.getName(),
				new bagaturchess.learning.goldmiddle.impl3.cfg.BoardConfigImpl_V18(), 10);

		System.out.println("AllRules manager: done");
	}
}
