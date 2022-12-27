package org.metatrans.commons.chess.logic.game;


import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.metatrans.commons.chess.GlobalConstants;
import org.metatrans.commons.chess.logic.BoardConstants;
import org.metatrans.commons.chess.logic.board.BoardUtils;
import org.metatrans.commons.chess.model.FieldSelection;
import org.metatrans.commons.chess.model.GameData;
import org.metatrans.commons.chess.model.IPlayer;
import org.metatrans.commons.chess.model.Player;

import bagaturchess.bitboard.impl.Constants;


public class GameDataUtils implements BoardConstants, GlobalConstants {


	private static final IPlayer[][] PLAYER_BY_COLOR_AND_TYPE = new IPlayer[3][3];//Indexes 1 and 2 are used in both dimensions. There are 4 possible players.

	static {

		PLAYER_BY_COLOR_AND_TYPE[COLOUR_PIECE_WHITE][PLAYER_TYPE_HUMAN] = GameDataUtils.createPlayer(PLAYER_TYPE_HUMAN, COLOUR_PIECE_WHITE);
		PLAYER_BY_COLOR_AND_TYPE[COLOUR_PIECE_WHITE][PLAYER_TYPE_COMPUTER] = GameDataUtils.createPlayer(PLAYER_TYPE_COMPUTER, COLOUR_PIECE_WHITE);
		PLAYER_BY_COLOR_AND_TYPE[COLOUR_PIECE_BLACK][PLAYER_TYPE_HUMAN] = GameDataUtils.createPlayer(PLAYER_TYPE_HUMAN, COLOUR_PIECE_BLACK);
		PLAYER_BY_COLOR_AND_TYPE[COLOUR_PIECE_BLACK][PLAYER_TYPE_COMPUTER] = GameDataUtils.createPlayer(PLAYER_TYPE_COMPUTER, COLOUR_PIECE_BLACK);
	}

	
	public static GameData createGameDataForNewGame(int playerTypeWhite, int playerTypeBlack, int boardManagerID, int computerModeID) {
		return createGameDataForNewGame(playerTypeWhite, playerTypeBlack, boardManagerID, computerModeID, Constants.INITIAL_BOARD);
	}


	public static GameData createGameDataForNewGame(int playerTypeWhite, int playerTypeBlack, int boardManagerID, int computerModeID, String fen) {
		GameData data = new GameData();
		return createGameDataForNewGame(data, playerTypeWhite, playerTypeBlack, boardManagerID, computerModeID, fen);
	}


	public static GameData createGameDataForNewGame(GameData data, int playerTypeWhite, int playerTypeBlack, int boardManagerID, int computerModeID) {

		return createGameDataForNewGame(data, playerTypeWhite, playerTypeBlack, boardManagerID, computerModeID, Constants.INITIAL_BOARD);
	}


	public static GameData createGameDataForNewGame(GameData data, int playerTypeWhite, int playerTypeBlack, int boardManagerID, int computerModeID, String fen) {

		//System.out.println("GameDataUtils.createGameDataForNewGame: playerTypeWhite=" + playerTypeWhite + ", playerTypeBlack=" + playerTypeBlack);
		//(new Exception()).printStackTrace(System.out);

		data.setWhite(createPlayer(playerTypeWhite, COLOUR_PIECE_WHITE));
		data.setBlack(createPlayer(playerTypeBlack, COLOUR_PIECE_BLACK));
		
		data.setBoarddata(BoardUtils.createBoardDataForNewGame());
		
		data.setBoardManagerID(boardManagerID);
		data.setComputerModeID(computerModeID);
		
		data.setInitialFEN(fen);
		
		return data;
	}


	public static Set<FieldSelection>[][] createEmptySelections() {
		Set<FieldSelection>[][] selections = new Set[8][8];
		for (int i = 0; i < selections.length; i++) {
			Set<FieldSelection>[] cur = selections[i];
			for (int j = 0; j < cur.length; j++) {
				cur[j] = Collections.synchronizedSet(new TreeSet<FieldSelection>());
			}
		}
		return selections;
	}


	public static void switchPlayerType(int color, int type, GameData data) {

		IPlayer new_player = PLAYER_BY_COLOR_AND_TYPE[color][type];

		if (new_player.getColour() == COLOUR_PIECE_WHITE) {

			data.setWhite(new_player);

		} else if (new_player.getColour() == COLOUR_PIECE_BLACK) {

			data.setBlack(new_player);

		} else {

			throw new IllegalStateException();
		}
	}


	private static IPlayer createPlayer(int type, int colour) {

		return new Player(type, colour);
	}
}
