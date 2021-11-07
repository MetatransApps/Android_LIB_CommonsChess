package org.metatrans.commons.chess.logic;


import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.metatrans.commons.chess.GlobalConstants;
import org.metatrans.commons.chess.model.FieldSelection;
import org.metatrans.commons.chess.model.GameData;
import org.metatrans.commons.chess.model.IPlayer;
import org.metatrans.commons.chess.model.Player;

import bagaturchess.bitboard.impl.Constants;


public class GameDataUtils implements BoardConstants, GlobalConstants {

	
	public static GameData createGameDataForNewGame(int playerTypeWhite, int playerTypeBlack, int boardManagerID, int computerModeID) {
		return createGameDataForNewGame(playerTypeWhite, playerTypeBlack, boardManagerID, computerModeID, Constants.INITIAL_BOARD);
	}
	
	
	public static GameData createGameDataForNewGame(int playerTypeWhite, int playerTypeBlack, int boardManagerID, int computerModeID, String fen) {
		GameData data = new GameData();
		return createGameDataForNewGame(data, playerTypeWhite, playerTypeBlack, boardManagerID, computerModeID, fen);
	}
	
	
	public static GameData createGameDataForNewGame(GameData data, int playerTypeWhite, int playerTypeBlack, int boardManagerID, int computerModeID, String fen) {
		
		data.setWhite(createPlayer(playerTypeWhite, COLOUR_PIECE_WHITE));
		data.setBlack(createPlayer(playerTypeBlack, COLOUR_PIECE_BLACK));
		
		data.setBoarddata(BoardUtils.createBoardDataForNewGame());
		
		data.setBoardManagerID(boardManagerID);
		data.setComputerModeID(computerModeID);
		
		data.setInitialFEN(fen);
		
		return data;
	}
	
	
	public static IPlayer createPlayer(int type, int colour) {
		
		if (type == PLAYER_TYPE_HUMAN) {
			
			return new Player(type, colour);
			
		} else if (type == PLAYER_TYPE_COMPUTER) {
			
			return new Player(type, colour);
			
		} else {
			
			throw new IllegalStateException("type=" + type);
		}
	}
	
	
	public static Set<FieldSelection>[][] createEmptySelections() {
		@SuppressWarnings("unchecked")
		Set<FieldSelection>[][] selections = new Set[8][8];
		for (int i = 0; i < selections.length; i++) {
			Set<FieldSelection>[] cur = selections[i];
			for (int j = 0; j < cur.length; j++) {
				cur[j] = Collections.synchronizedSet(new TreeSet<FieldSelection>());
			}
		}
		return selections;
	}
}
