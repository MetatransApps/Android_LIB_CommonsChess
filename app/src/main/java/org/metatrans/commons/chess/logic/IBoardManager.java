package org.metatrans.commons.chess.logic;


import java.util.List;

import org.metatrans.commons.chess.logic.computer.IComputer;

import com.chessartforkids.model.GameData;
import com.chessartforkids.model.IPlayer;
import com.chessartforkids.model.Move;
import com.chessartforkids.model.MovingPiece;


public interface IBoardManager {
	
	//public int getID();
	
	public void switchComputerMode(int modeID);
	
	public GameData getGameData();
	
	public GameData getGameData_Raw();
	
	public int getGameStatus();
	
	public int getPiece(int letter, int digit);
	
	public int getColourToMove();

	public IPlayer getPlayerToMove();
	public IPlayer getPlayerWhite();
	public IPlayer getPlayerBlack();
	public IPlayer getPlayer(int colour);
	
	public IComputer getComputerToMove();
	public IComputer getComputerWhite();
	public IComputer getComputerBlack();
	public IComputer getComputer(int colour);
	
	public int[][] getBoard_WithoutHided();
	public int[][] getBoard_Full();
	
	public int[] getCaptured_W();
	public int[] getCaptured_B();

	public int getCapturedSize_W();
	public int getCapturedSize_B();
	
	public boolean getWKingSideAvailable();
	public boolean getWQueenSideAvailable();
	public boolean getBKingSideAvailable();
	public boolean getBQueenSideAvailable();
	public String getEnpassantSquare();
	public int getHalfMoves();
	public int getFullMoves();
	
	
	public boolean isPromotion(int pieceID, int digit);
	
	public boolean isSelectionAllowed(int pieceID);
	public boolean isReSelectionAllowed(int currentPieceID, int nextPieceID);
	
	public void startHidingPiece(int letter, int digit);
	public void stopHidingPiece(int letter, int digit);
	
	public void move(Move move);
	public void unmove(Move move);
	public Move unmove();
	
	public boolean selectPossibleFields();
	public List<Move> selectToFields(int uiLetter, int uiDigit);
	public int getMoveScores(Move move);
	
	public int[][] getMovablePieces();
	
	public MovingPiece getMovingPiece();
	public void clearMovingPiece();
	public void createMovingPiece(float x, float y, int letter, int digit,
			int pieceID);
	
	public String getFEN();
	
	public boolean hasMovablePieces(int color);
	public boolean isInCheck(int color);
	public boolean isOpponentInCheck();
}
