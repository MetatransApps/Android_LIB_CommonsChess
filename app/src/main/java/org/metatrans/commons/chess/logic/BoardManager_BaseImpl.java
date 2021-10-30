package org.metatrans.commons.chess.logic;


import java.util.List;

import org.metatrans.commons.cfg.difficulty.IConfigurationDifficulty;
import org.metatrans.commons.chess.GlobalConstants;
import org.metatrans.commons.chess.app.Application_Chess_BaseImpl;
import org.metatrans.commons.chess.logic.computer.ComputerPlayer_Random;
import org.metatrans.commons.chess.logic.computer.ComputerPlayer_RandomButCapture;
import org.metatrans.commons.chess.logic.computer.IComputer;

import com.chessartforkids.model.BoardData;
import com.chessartforkids.model.GameData;
import com.chessartforkids.model.IPlayer;
import com.chessartforkids.model.Move;
import com.chessartforkids.model.MovingPiece;


public abstract class BoardManager_BaseImpl implements IBoardManager, BoardConstants {
	
	
	protected int THINK_TIME_FOR_LEVELS_1_TO_4 = 500;
	
	protected MovingPiece movingPiece;
	
	protected int hidden_letter;
	protected int hidden_digit;
	
	
	private int[] captured_w;
	private int[] captured_b;
	private int size_captured_w;
	private int size_captured_b;
	private int[][] piecesForUI;
	
	
	private GameData gamedata;
	
	private IComputer computerWhite;
	private IComputer computerBlack;
	
	protected boolean touchMoveList;
	
	
	public BoardManager_BaseImpl(GameData _gamedata, boolean _touchMoveList) {
		
		gamedata = _gamedata;
		touchMoveList = _touchMoveList;
		
		movingPiece = gamedata.getMovingPiece();
		
		String gameResultText = gamedata.getBoarddata().gameResultText;
		gamedata.setBoarddata(BoardUtils.createBoardDataForNewGame());
		gamedata.getBoarddata().gameResultText = gameResultText;
		
		hidden_letter = -1;
		hidden_digit = -1;
		
		captured_w = new int[16];
		for (int i=0; i<captured_w.length; i++) {
			captured_w[i] = ID_PIECE_NONE;
			//captured_w[i] = data.captured_w[i];
		}
		
		captured_b = new int[16];
		for (int i=0; i<captured_b.length; i++) {
			captured_b[i] = ID_PIECE_NONE;
			//captured_b[i] = data.captured_b[i];
		}
		
		size_captured_w = 0;//data.size_captured_w;
		size_captured_b = 0;//data.size_captured_b;
		
		piecesForUI = new int[8][8];
		
		setComputerWhite(createComputerPlayer(gamedata.getComputerModeID(), COLOUR_PIECE_WHITE));
		setComputerBlack(createComputerPlayer(gamedata.getComputerModeID(), COLOUR_PIECE_BLACK));
	}
	
	
	protected IComputer createComputerPlayer(int modeID, int colour) {
		if (modeID == IConfigurationDifficulty.MODE_COMPUTER_RANDOM) {
			return new ComputerPlayer_Random(colour, this, THINK_TIME_FOR_LEVELS_1_TO_4);	
		} else if (modeID == IConfigurationDifficulty.MODE_COMPUTER_RANDOM_BUT_CAPTURE) {
			return new ComputerPlayer_RandomButCapture(colour, this, THINK_TIME_FOR_LEVELS_1_TO_4);
		} else {
			throw new IllegalStateException("modeID=" + modeID);
		}
	}
	
	
	@Override
	public int getGameStatus() {
		int colourToMove = getColourToMove();
		if (hasMovablePieces(colourToMove)) {
			return GlobalConstants.GAME_STATUS_NONE;
		} else {
			if (colourToMove == BoardConstants.COLOUR_PIECE_WHITE) {
				return GlobalConstants.GAME_STATUS_BLACK_WINS;
			} else {
				return GlobalConstants.GAME_STATUS_WHITE_WINS;
			}
		}
	}
	
	
	@Override
	public IPlayer getPlayerToMove() {
		return getPlayer(getColourToMove());
	}
	
	
	@Override
	public IPlayer getPlayerWhite() {
		GameData data = getGameData();
		return data.getWhite();
	}


	@Override
	public IPlayer getPlayerBlack() {
		GameData data = getGameData();
		return data.getBlack();
	}
	
	
	@Override
	public IPlayer getPlayer(int colour) {
		GameData data = getGameData();
		if (colour == COLOUR_PIECE_WHITE) {
			return data.getWhite();
		} else if (colour == COLOUR_PIECE_BLACK) {
			return data.getBlack();
		} else if (colour == COLOUR_PIECE_ALL) {
			
			//if (data.getMoves().size() == 0) {
			//	return data.getWhite();
			//} else {
			//	if (COLOUR_PIECE_WHITE == BoardUtils.getColour(data.getMoves().get(data.getMoves().size() - 1).pieceID)) {
			//		return data.getBlack();
			//	} else {
			//		return data.getWhite();
			//	}
			//}
			
			/*double rand = Math.random();
			if (rand <= 0.5) {
				return data.getWhite();
			} else {
				return data.getBlack();
			}*/
			
			throw new IllegalStateException("colour=" + getColourToMove());
		} else {
			throw new IllegalStateException("colour=" + getColourToMove());
		}
	}


	@Override
	public void switchComputerMode(int modeID) {
		
		if (computerWhite != null) computerWhite.stopCurrentJob();
		if (computerBlack != null) computerBlack.stopCurrentJob();
		
		setComputerWhite(createComputerPlayer(modeID, COLOUR_PIECE_WHITE));
		setComputerBlack(createComputerPlayer(modeID, COLOUR_PIECE_BLACK));
		
		if (modeID != getGameData().getComputerModeID()) {
			throw new IllegalStateException();
		}
	}
	
	
	protected void setComputerWhite(IComputer computer) {
		computerWhite = computer;
	}

	protected void setComputerBlack(IComputer computer) {
		computerBlack = computer;
	}
	
	
	public IComputer getComputerToMove() {
		return getComputer(getColourToMove());
	}
	
	
	public IComputer getComputerWhite() {
		return computerWhite;
	}
	
	
	public IComputer getComputerBlack() {
		return computerBlack;
	}
	
	
	public IComputer getComputer(int colour) {
		if (colour == COLOUR_PIECE_WHITE) {
			return computerWhite;
		} else if (colour == COLOUR_PIECE_BLACK) {
			return computerBlack;
		} else if (colour == COLOUR_PIECE_ALL) {
			double rand = Math.random();
			if (rand <= 0.5) {
				return computerWhite;
			} else {
				return computerBlack;
			}
			//throw new IllegalStateException("colour=" + getColourToMove());
		} else {
			throw new IllegalStateException("colour=" + getColourToMove());
		}	
	}
	
	
	@Override
	public GameData getGameData() {
		
		fillBoardData(gamedata.getBoarddata());
		
		gamedata.setMovingPiece(getMovingPiece()); 
		
		return gamedata;
	}
	
	
	@Override
	public GameData getGameData_Raw() {
		return gamedata;
	}
	
	
	protected int[][] getPiecesArray() {
		int[][] pieces = piecesForUI;//new int[8][8];
		return pieces;
	}
	
	
	@Override
	public int[] getCaptured_W() {
		return captured_w;
	}


	@Override
	public int[] getCaptured_B() {
		return captured_b;
	}
	
	@Override
	public int getCapturedSize_W() {
		return size_captured_w;
	}


	@Override
	public int getCapturedSize_B() {
		return size_captured_b;
	}

	public List<Move> getMoves() {
		return gamedata.getMoves();
	}
	
	@Override
	public int getHalfMoves() {
		return getMoves().size();
	}


	@Override
	public int getFullMoves() {
		return (getHalfMoves() / 2) + 1;
	}
	
	@Override
	public boolean isPromotion(int pieceID, int digit) {
		int colour = BoardUtils.getColour(pieceID);
		if (colour == COLOUR_PIECE_WHITE) {
			return pieceID == ID_PIECE_W_PAWN && digit == 0;
		} else {
			return pieceID == ID_PIECE_B_PAWN && digit == 7;
		}
	}
	
	@Override
	public void move(Move move) {
		
		if (touchMoveList) gamedata.getMoves().add(move);
		gamedata.getSearchInfos().add(gamedata.getLastSearchInfo());//Add the last search info in order to make it visible on the panel after the move
		
		if (move.isCapture) {
			if (BoardUtils.getColour(move.capturedPieceID) == COLOUR_PIECE_WHITE) {
				captured_w[size_captured_w] = move.capturedPieceID;
				size_captured_w++;				
			} else {
				captured_b[size_captured_b] = move.capturedPieceID;
				size_captured_b++;					
			}
		}
		
		hidden_letter = -1;
		hidden_digit = -1;
	}
	
	@Override
	public Move unmove() {
		
		/*if (Application_Base.getInstance().isTestMode()) {
			if (hidden_letter != -1) {
				throw new IllegalStateException("hidden_letter != -1");
			}
		}*/
		
		Move move = null;
		if (gamedata.getMoves().size() > 0) {
			
			move = gamedata.getMoves().remove(gamedata.getMoves().size() - 1);
			if (gamedata.getSearchInfos().size() > 0) {//Temporal work around for preventing de-serialization issues. Could be removed later. 
				gamedata.getSearchInfos().remove(gamedata.getSearchInfos().size() - 1);
			}
			
			unmove(move);
			
			if (move.isCapture) {
				
				//if (true) throw new IllegalStateException("TODO: Implement capture pieces");
				
				if (BoardUtils.getColour(move.capturedPieceID) == COLOUR_PIECE_WHITE) {
					captured_w[size_captured_w - 1] = ID_PIECE_NONE;
					size_captured_w--;				
				} else {
					captured_b[size_captured_b - 1] = ID_PIECE_NONE;
					size_captured_b--;		
				}
			}
		}
		
		return move;
	}
	
	@Override
	public abstract void unmove(Move move);
	
	
	@Override
	public int[][] getBoard_WithoutHided() {
		int[][] pieces = getBoard_Full();
		
		if (hidden_letter != -1) {
			pieces[hidden_letter][hidden_digit] = ID_PIECE_NONE;
		}
		
		return pieces;
	}
	
	
	@Override
	public int[][] getMovablePieces() {
		
		int[][] result = new int[8][8];
		
		for (int letter = 0; letter < 8; letter++) {
			for (int digit = 0; digit < 8; digit++) {
				int pieceID = getPiece(letter, digit);
				if (pieceID != ID_PIECE_NONE && isSelectionAllowed(pieceID)) {
					int availableMoves = selectToFields(letter, digit).size();
					if (availableMoves > 0) {
						result[letter][digit] = pieceID;
					}
				}
			}
		}
		
		return result;
	}
	
	
	@Override
	public boolean hasMovablePieces(int color) {
		
		boolean hasMove = false;
		int[][] pieces = piecesForUI;
		for (int letter=0; letter<8; letter++) {
			for (int digit=0; digit<8; digit++) {
				int pid = pieces[letter][digit];
				if (pid != ID_PIECE_NONE
						&& BoardUtils.getColour(pid) == color) {
					if (selectToFields(letter, digit).size() > 0) {
						hasMove = true;
						break;
					}
				}
			}
		}
		
		return hasMove;
	}
	
	
	@Override
	public void startHidingPiece(int letter, int digit) {
		
		if (Application_Chess_BaseImpl.getInstance() != null && Application_Chess_BaseImpl.getInstance().isTestMode()) {
			if (hidden_letter != -1) {
				throw new IllegalStateException("letter=" + letter + ", digit=" + digit + ", hidden_letter=" + hidden_letter + ", hidden_digit=" + hidden_digit);
			}
		}
		
		hidden_letter = letter;
		hidden_digit = digit;
	}


	@Override
	public void stopHidingPiece(int letter, int digit) {
		
		if (Application_Chess_BaseImpl.getInstance() != null && Application_Chess_BaseImpl.getInstance().isTestMode()) {
			if (letter != hidden_letter) {
				throw new IllegalStateException("letter=" + letter + ", digit=" + digit + ", hidden_letter=" + hidden_letter + ", hidden_digit=" + hidden_digit);
			}
			if (digit != hidden_digit) {
				throw new IllegalStateException("letter=" + letter + ", digit=" + digit + ", hidden_letter=" + hidden_letter + ", hidden_digit=" + hidden_digit);
			}
		}
		
		hidden_letter = -1;
		hidden_digit = -1;
	}


	@Override
	public boolean selectPossibleFields() {
		return true;
	}


	@Override
	public boolean isReSelectionAllowed(int currentPieceID, int nextPieceID) {
		return (nextPieceID != ID_PIECE_NONE && BoardUtils.getColour(nextPieceID) == BoardUtils.getColour(currentPieceID));
	}


	@Override
	public boolean isSelectionAllowed(int pieceID) {
		return true;
	}
	
	
	@Override
	public MovingPiece getMovingPiece() {
		return movingPiece;
	}
	
	@Override
	public void clearMovingPiece() {
		movingPiece = null;
		
		hidden_letter = -1;
		hidden_digit = -1;
	}


	@Override
	public void createMovingPiece(float x, float y, int letter, int digit, int pieceID) {
		
		if (movingPiece != null) {
			throw new IllegalStateException("movingPiece=" + movingPiece);
		}
		
		movingPiece = new MovingPiece();
		
		movingPiece.initial_letter = letter;
		movingPiece.initial_digit = digit;
		movingPiece.x = x;
		movingPiece.y = y;
		movingPiece.pieceID = pieceID;
		movingPiece.moves = selectToFields(letter, digit);		
	}
	
	
	private void fillBoardData(BoardData data) {
		data.board = getBoard_Full();
		data.captured_w = getCaptured_W();
		data.captured_b = getCaptured_B();
		data.size_captured_w = getCapturedSize_W();
		data.size_captured_b = getCapturedSize_B();
		if (getColourToMove() == BoardConstants.COLOUR_PIECE_ALL) {
			int movesCount = gamedata.getMoves().size();
			if (movesCount > 0) {
				Move last = gamedata.getMoves().get(movesCount - 1);
				int lastColour = BoardUtils.getColour(last.pieceID);
				data.colourToMove = BoardUtils.switchColour(lastColour);
			} else {
				data.colourToMove = COLOUR_PIECE_WHITE;
			}
		} else {
			data.colourToMove = getColourToMove();	
		}
		data.w_kingSideAvailable = getWKingSideAvailable();
		data.w_queenSideAvailable = getWQueenSideAvailable();
		data.b_kingSideAvailable = getBKingSideAvailable();
		data.b_queenSideAvailable = getBQueenSideAvailable();
		data.enpassantSquare = getEnpassantSquare();
		data.halfMoves = getHalfMoves();
		data.fullMoves = getFullMoves();
	}
	
	
	@Override
	public String getFEN() {
		throw new UnsupportedOperationException();
	}
	
	
	@Override
	public boolean isInCheck(int color) {
		throw new UnsupportedOperationException();
	}
	
	
	@Override
	public boolean isOpponentInCheck() {
		throw new UnsupportedOperationException();
	}
}
