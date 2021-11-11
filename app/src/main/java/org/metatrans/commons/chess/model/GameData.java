package org.metatrans.commons.chess.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.metatrans.commons.chess.GlobalConstants;
import org.metatrans.commons.model.GameData_Base;

import bagaturchess.bitboard.impl.Constants;


public class GameData extends GameData_Base implements Serializable, GlobalConstants {
	
	
	private static final long serialVersionUID = -4605611735160698191L;
	
	
	private int boardManagerID;
	private int computerModeID;
	
	private IPlayer white;
	private IPlayer black;
	
	private IPlayer white_backup;
	private IPlayer black_backup;
	
	private String initialFEN;
	private BoardData boarddata;
	
	protected int currentMoveIndex;
	private List<Move> moves;
	protected List<SearchInfo> searchinfos;
	
	private MovingPiece movingPiece;
	
	private long accumulated_time_white;
	private long accumulated_time_black;
	
	
	public GameData() {

		moves = new ArrayList<Move>();

		searchinfos = new ArrayList<SearchInfo>();
		
		currentMoveIndex = -1;
				
		timestamp_created = System.currentTimeMillis();
	}
	
	
	@Override
	public String toString() {
		String result = "";
		result += "BOARD[" + boarddata + "], MOVES[" + moves + "]";
		return result;
	}


	public int getBoardManagerID() {
		return boardManagerID;
	}


	public void setBoardManagerID(int boardManagerID) {
		this.boardManagerID = boardManagerID;
	}


	public int getComputerModeID() {
		return computerModeID;
	}


	public void setComputerModeID(int computerModeID) {
		this.computerModeID = computerModeID;
	}


	public IPlayer getWhite() {
		return white;
	}


	public void setWhite(IPlayer white) {
		this.white = white;
	}


	public IPlayer getBlack() {
		return black;
	}


	public void setBlack(IPlayer black) {
		this.black = black;
	}


	public BoardData getBoarddata() {
		return boarddata;
	}


	public void setBoarddata(BoardData boarddata) {
		this.boarddata = boarddata;
	}
	
	
	public List<Move> getMoves() {
		return moves;
	}


	public int getCurrentMoveIndex() {
		return currentMoveIndex;
	}


	public boolean isOnTheFirstMove() {

		return currentMoveIndex == -1 || moves.size() == 0 || moves.size() == 1;
	}


	public boolean isOnTheLastMove() {

		return currentMoveIndex == moves.size() - 1 || moves.size() == 0 || moves.size() == 1;
	}


	public void setCurrentMoveIndex(int currentMoveIndex) {
		this.currentMoveIndex = currentMoveIndex;
	}
	
	
	public long getAccumulated_time_white() {
		return accumulated_time_white;
	}
	
	
	public void setAccumulated_time_white(long accumulated_time_white) {
		this.accumulated_time_white = accumulated_time_white;
	}
	
	
	public long getAccumulated_time_black() {
		return accumulated_time_black;
	}
	
	
	public void setAccumulated_time_black(long accumulated_time_black) {
		this.accumulated_time_black = accumulated_time_black;
	}

	public void setMoves(List<Move> moves) {
		this.moves = moves;
	}
	
	
	public MovingPiece getMovingPiece() {
		return movingPiece;
	}
	
	
	public void setMovingPiece(MovingPiece movingPiece) {
		this.movingPiece = movingPiece;
	}
	
	
	public List<SearchInfo> getSearchInfos() {
		
		return searchinfos;
	}
	
	
	public String getInitialFEN() {

		if (initialFEN == null) {

			initialFEN = Constants.INITIAL_BOARD;
		}

		return initialFEN;
	}
	
	
	public void setInitialFEN(String initialFEN) {
		this.initialFEN = initialFEN;
	}
}
