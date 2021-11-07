package org.metatrans.commons.chess.logic.computer;


import java.util.List;

import org.metatrans.commons.chess.engines.EngineClient_LocalImpl;
import org.metatrans.commons.chess.engines.search.IEngineClient;
import org.metatrans.commons.chess.logic.BoardManager_NativeBoard;
import org.metatrans.commons.chess.model.Move;
import org.metatrans.commons.chess.model.SearchInfo;

import bagaturchess.bitboard.api.BoardUtils;
import bagaturchess.bitboard.api.IBitBoard;
import bagaturchess.search.api.internal.ISearchInfo;


public class ComputerPlayer_Engine extends ComputerPlayer_BaseImpl {
	
	
	private IBitBoard board_computer;
	
	private IEngineClient engine;

	private SearchInfo last_search_info;


	public ComputerPlayer_Engine(int colour, BoardManager_NativeBoard boardManager, int thinkTime) {

		super(colour, boardManager, thinkTime);

		System.out.println("ComputerPlayer_Engine: constructor");

		board_computer = boardManager.createBoard();
		
		engine = EngineClient_LocalImpl.getSingleton(board_computer);
	}


	public SearchInfo getLastSearchInfo() {
		return last_search_info;
	}


	@Override
	public Move think() {

		System.out.println("ComputerPlayer_Engine: think: Moves count is " + getBoardManager().getGameData().getMoves().size());

		//Revert local board and play all moves
		//Start think process
		//Iterate to check stop condition
		//If result is available - get it and return
		
		board_computer.revert();
		
		for (int i = 0; i <= getBoardManager().getGameData().getCurrentMoveIndex(); i++) {
			board_computer.makeMoveForward(getBoardManager().getGameData().getMoves().get(i).nativemove);
		}

		long start_time = System.currentTimeMillis();

		last_search_info = null;

		ISearchInfo native_last_search_info = null;
		
		try {
			
			if (checkStopCondition()) {
				return null;
			}
			
			engine.startThinking(board_computer);
			
			while (true) {
				
				try {
					Thread.sleep(3);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if (checkStopCondition()) {
					engine.stopThinking();
					return null;
				}
				
				if (engine.isDone()
						|| ((System.currentTimeMillis() - start_time) > getThinkTime() && engine.hasAtLeastOneMove())
					) {

					native_last_search_info = engine.stopThinkingWithResult();

					if (native_last_search_info == null) {

						return null;
					}
					
					break;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		List<Move> moves = getOrderedMovesList();
		
		Move result = null;
		for (Move moveObj: moves) {
			if (moveObj.nativemove == native_last_search_info.getBestMove()) {
				result = moveObj;
				break;
			}
		}

		if (native_last_search_info != null) {

			String eval;
			String pv;
			String depth;
			String nps;

			if (native_last_search_info.getDepth() > 0) {

				eval = String.format("%,.2f", (native_last_search_info.getEval() / (double) 100));
				if (native_last_search_info.isMateScore()) {
					if (native_last_search_info.getEval() > 0) {
						eval = "+Mate in " + native_last_search_info.getMateScore();
					} else {
						eval = "-Mate in " + Math.abs(native_last_search_info.getMateScore());
					}
				} else {
					eval = ((native_last_search_info.getEval() > 0) ? "+" : "") + eval;
				}
				pv = BoardUtils.movesToString(cutPV(native_last_search_info.getPV()), board_computer) + " ...";
				depth = "Depth " + native_last_search_info.getDepth() + "/" + native_last_search_info.getSelDepth();
				nps   = "NPS " + (int)(native_last_search_info.getSearchedNodes()/((System.currentTimeMillis()-start_time)/(double)1000));

			} else {

				eval  = "0";
				pv = "Book Move " + board_computer.getMoveOps().moveToString(native_last_search_info.getBestMove());
				depth = "Depth 0/0";
				nps   = "NPS 0";
			}

			last_search_info = new SearchInfo(eval, pv, depth, nps);
		}


		if (result == null) {
			//throw new IllegalStateException("No move");
		}
		
		return result;
	}


	private int[] cutPV(int[] input) {
		int[] result = new int[Math.min(3, input.length)];
		for (int i=0; i<result.length; i++) {
			result[i] = input[i];
		}
		return result;
	}

	
	@Override
	public int getMoveScores(Move move) {

		return 0;
	}
}
