package org.metatrans.commons.chess.logic.board;


import org.metatrans.commons.cfg.difficulty.IConfigurationDifficulty;
import org.metatrans.commons.chess.GlobalConstants;
import org.metatrans.commons.chess.logic.computer.ComputerPlayer_Engine;
import org.metatrans.commons.chess.logic.computer.ComputerPlayer_RandomButCaptureAndDefense;
import org.metatrans.commons.chess.logic.computer.ComputerPlayer_StaticEvaluation;
import org.metatrans.commons.chess.logic.computer.IComputer;
import org.metatrans.commons.chess.model.GameData;

import bagaturchess.bitboard.api.IBitBoard;
import bagaturchess.bitboard.api.IGameStatus;


public class BoardManager_AllRules extends BoardManager_NativeBoard {
	
	
	public BoardManager_AllRules(GameData gamedata) {
		super(gamedata, initBoard(gamedata.getInitialFEN()));
	}
	
	
	@Override
	public IBitBoard createBoard() {
		return initBoard(getGameData_Raw().getInitialFEN());
	}


	private static IBitBoard initBoard(String fen) {
		return bagaturchess.bitboard.api.BoardUtils.createBoard_WithPawnsCache(fen,
				bagaturchess.learning.goldmiddle.impl.cfg.bagatur.eval.BagaturPawnsEvalFactory.class.getName(),
				new bagaturchess.learning.goldmiddle.impl3.cfg.BoardConfigImpl_V18(), 10);
	}
	
	
	@Override
	protected IComputer createComputerPlayer(int modeID, int colour) {
		if (modeID == IConfigurationDifficulty.MODE_COMPUTER_RANDOM_BUT_CAPTURE_AND_DEFENSE) {
			return new ComputerPlayer_RandomButCaptureAndDefense(colour, this, THINK_TIME_FOR_LEVELS_1_TO_4);
		} else if (modeID == IConfigurationDifficulty.MODE_COMPUTER_POSITIONAL_EVALUATION) {
			return new ComputerPlayer_StaticEvaluation(colour, this, THINK_TIME_FOR_LEVELS_1_TO_4);
		} else if (modeID == IConfigurationDifficulty.MODE_COMPUTER_ENGINE_1PLY) {
			return new ComputerPlayer_Engine(colour, this, 0);
		} else if (modeID == IConfigurationDifficulty.MODE_COMPUTER_ENGINE_1SEC) {
			return new ComputerPlayer_Engine(colour, this, 1000);
		} else if (modeID == IConfigurationDifficulty.MODE_COMPUTER_ENGINE_3SEC) {
			return new ComputerPlayer_Engine(colour, this, 3000);
		} else if (modeID == IConfigurationDifficulty.MODE_COMPUTER_ENGINE_7SEC) {
			return new ComputerPlayer_Engine(colour, this, 7000);
		} else if (modeID == IConfigurationDifficulty.MODE_COMPUTER_ENGINE_15SEC) {
			return new ComputerPlayer_Engine(colour, this, 15000);
		} else if (modeID == IConfigurationDifficulty.MODE_COMPUTER_ENGINE_30SEC) {
			return new ComputerPlayer_Engine(colour, this, 30 * 1000);
		} else if (modeID == IConfigurationDifficulty.MODE_COMPUTER_ENGINE_1MIN) {
			return new ComputerPlayer_Engine(colour, this, 1 * 60 * 1000);
		} else if (modeID == IConfigurationDifficulty.MODE_COMPUTER_ENGINE_2MINS) {
			return new ComputerPlayer_Engine(colour, this, 2 * 60 * 1000);
		} else if (modeID == IConfigurationDifficulty.MODE_COMPUTER_ENGINE_4MINS) {
			return new ComputerPlayer_Engine(colour, this, 4 * 60 * 1000);
		} else if (modeID == IConfigurationDifficulty.MODE_COMPUTER_ENGINE_10MINS) {
			return new ComputerPlayer_Engine(colour, this, 10 * 60 * 1000);
		} else if (modeID == IConfigurationDifficulty.MODE_COMPUTER_ENGINE_30MINS) {
			return new ComputerPlayer_Engine(colour, this, 30 * 60 * 1000);
		} else if (modeID == IConfigurationDifficulty.MODE_COMPUTER_ENGINE_1HOUR) {
			return new ComputerPlayer_Engine(colour, this, 60 * 60 * 1000);
		} else {
			return super.createComputerPlayer(modeID, colour);
		}
	}
	
	
	@Override
	public synchronized int getGameStatus() {
		
		//System.out.println("getGameStatus: incheck=" + board.isInCheck());
		if (board.isInCheck()) {
			//hasMoveInCheck throws exception in some cases (checksCount <= 0)
			//if (!board.hasMoveInCheck()) {
				//Do nothing
			//}
		} else {
			if (!board.hasMoveInNonCheck()) {
				//TODO: Check engine behavior
				//System.out.println("getGameStatus: no moves");
				return GlobalConstants.GAME_STATUS_DRAW;
			}
		}

			
		if (board.getStateRepetition() >= 3) {
			//System.out.println("getGameStatus: 3 states rep");
			return GlobalConstants.GAME_STATUS_DRAW;
		}
		
		IGameStatus status = board.getStatus();
		
		int gameStatus = 0; 
		
		switch (status) {
			case MATE_WHITE_WIN:
				gameStatus = GlobalConstants.GAME_STATUS_WHITE_WINS;
				break;
			case MATE_BLACK_WIN:
				gameStatus = GlobalConstants.GAME_STATUS_BLACK_WINS;
				break;
			/*case DRAW_3_STATES_REPETITION:
				System.out.println("getGameStatus: 3 states rep");
				gameStatus = BagaturChessConstants.GAME_STATUS_DRAW;
				break;*/
			case DRAW_50_MOVES_RULE:
				//System.out.println("getGameStatus: 50 move rule");
				gameStatus = GlobalConstants.GAME_STATUS_DRAW;
				break;
			case NO_SUFFICIENT_MATERIAL:
				gameStatus = GlobalConstants.GAME_STATUS_DRAW;
				break;
			case NONE:
				gameStatus = GlobalConstants.GAME_STATUS_NONE;
				break;
			case UNDEFINED:
				gameStatus = GlobalConstants.GAME_STATUS_NONE;
				break;
			default:
				gameStatus = GlobalConstants.GAME_STATUS_NONE;
				break;
		}
		
		return gameStatus;
	}


	@Override
	protected boolean isKingCaptureEnabled() {
		return false;
	}
	
	
	@Override
	public String getFEN() {
		return board.toEPD();
	}
}
