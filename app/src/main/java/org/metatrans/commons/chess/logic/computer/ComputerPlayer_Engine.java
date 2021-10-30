package org.metatrans.commons.chess.logic.computer;


import java.util.List;

import org.metatrans.commons.chess.engines.EngineClient_LocalImpl;
import org.metatrans.commons.chess.engines.search.IEngineClient;
import org.metatrans.commons.chess.logic.BoardManager_NativeBoard;

import com.chessartforkids.model.Move;

import bagaturchess.bitboard.api.IBitBoard;
import bagaturchess.search.api.internal.ISearchInfo;


public class ComputerPlayer_Engine extends ComputerPlayer_BaseImpl {
	
	
	private IBitBoard board_computer;
	
	private IEngineClient engine;
	
	
	public ComputerPlayer_Engine(int colour, BoardManager_NativeBoard boardManager, int thinkTime) {
		
		super(colour, boardManager, thinkTime);
		
		board_computer = boardManager.createBoard();
		
		engine = EngineClient_LocalImpl.getSingleton(board_computer);
	}
	
	
	@Override
	public Move think() {
		
		//Revert local board and play all moves
		//Start think process
		//Iterate to check stop condition
		//If result is available - get it and return
		
		board_computer.revert();
		
		for (int i = 0; i <= getBoardManager().getGameData().getCurrentMoveIndex(); i++) {
			board_computer.makeMoveForward(getBoardManager().getGameData().getMoves().get(i).nativemove);
		}
		/*for (Move history: getBoardManager().getGameData().getMoves()) {
			board_computer.makeMoveForward(history.nativemove);
		}*/

		long start_time = System.currentTimeMillis();
		
		ISearchInfo moveInfo = null;
		
		try {
			
			if (checkStopCondition()) {
				return null;
			}
			
			engine.startThinking(board_computer);
			
			while (true) {
				
				try {
					Thread.sleep(33);
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
					
					moveInfo = engine.stopThinkingWithResult();
					if (moveInfo == null) {
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
			if (moveObj.nativemove == moveInfo.getBestMove()) {
				result = moveObj;
				break;
			}
		}
		
		if (result == null) {
			//throw new IllegalStateException("No move");
		}
		
		return result;
	}
	
	
	@Override
	public int getMoveScores(Move move) {
		//throw new IllegalStateException();
		return 0;// For waitInitialization method
	}
}
