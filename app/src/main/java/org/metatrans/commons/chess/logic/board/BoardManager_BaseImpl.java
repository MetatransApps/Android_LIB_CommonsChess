package org.metatrans.commons.chess.logic.board;


import java.util.List;

import org.metatrans.commons.cfg.difficulty.IConfigurationDifficulty;
import org.metatrans.commons.chess.GlobalConstants;
import org.metatrans.commons.chess.app.Application_Chess_BaseImpl;
import org.metatrans.commons.chess.logic.BoardConstants;
import org.metatrans.commons.chess.logic.computer.ComputerPlayer_Random;
import org.metatrans.commons.chess.logic.computer.ComputerPlayer_RandomButCapture;
import org.metatrans.commons.chess.logic.computer.IComputer;
import org.metatrans.commons.chess.model.BoardData;
import org.metatrans.commons.chess.model.GameData;
import org.metatrans.commons.chess.model.IPlayer;
import org.metatrans.commons.chess.model.Move;
import org.metatrans.commons.chess.model.MovingPiece;


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
	
	
	public BoardManager_BaseImpl(GameData _gamedata) {
		
		gamedata = _gamedata;

		movingPiece = gamedata.getMovingPiece();
		
		hidden_letter = -1;
		hidden_digit = -1;
		
		captured_w = new int[16];
		for (int i=0; i<captured_w.length; i++) {
			captured_w[i] = ID_PIECE_NONE;
		}
		
		captured_b = new int[16];
		for (int i=0; i<captured_b.length; i++) {
			captured_b[i] = ID_PIECE_NONE;
		}
		
		size_captured_w = 0;
		size_captured_b = 0;
		
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
		int colourToMove = getColorToMove();
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
		return getPlayer(getColorToMove());
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
			
			throw new IllegalStateException("colour=" + getColorToMove());

		} else {
			throw new IllegalStateException("colour=" + getColorToMove());
		}
	}
	
	
	protected void setComputerWhite(IComputer computer) {
		computerWhite = computer;
	}

	protected void setComputerBlack(IComputer computer) {
		computerBlack = computer;
	}
	
	
	public IComputer getComputerToMove() {
		return getComputer(getColorToMove());
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
			throw new IllegalStateException("colour=" + getColorToMove());
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


	@Override
	public int getHalfMoves() {
		return gamedata.getMoves().size();
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

		if (move.isCapture) {
			if (BoardUtils.getColour(move.capturedPieceID) == COLOUR_PIECE_WHITE) {
				captured_w[size_captured_w] = move.capturedPieceID;
				size_captured_w++;
			} else {
				captured_b[size_captured_b] = move.capturedPieceID;
				size_captured_b++;					
			}
		}
	}


	@Override
	public void unmove(Move move) {
			
		if (move.isCapture) {

			if (BoardUtils.getColour(move.capturedPieceID) == COLOUR_PIECE_WHITE) {

				captured_w[size_captured_w - 1] = ID_PIECE_NONE;
				size_captured_w--;

			} else {

				captured_b[size_captured_b - 1] = ID_PIECE_NONE;
				size_captured_b--;
			}
		}
	}
	
	
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


	private void fillBoardData(BoardData data) {
		data.board = getBoard_Full();
		data.captured_w = getCaptured_W();
		data.captured_b = getCaptured_B();
		data.size_captured_w = getCapturedSize_W();
		data.size_captured_b = getCapturedSize_B();

		if (getColorToMove() == BoardConstants.COLOUR_PIECE_ALL) {

			GameData gamedata = getGameData();
			List<Move> moves = gamedata.getMoves();
			int moveIndex = gamedata.getCurrentMoveIndex();

			if (moveIndex >= 0) {

				Move move = moves.get(moveIndex);
				int moveColour = BoardUtils.getColour(move.pieceID);
				data.colourToMove = BoardUtils.switchColour(moveColour);

			} else {

				data.colourToMove = COLOUR_PIECE_WHITE;
			}

		} else {
			data.colourToMove = getColorToMove();
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
	public MovingPiece getMovingPiece() {
		return movingPiece;
	}


	@Override
	public void createMovingPiece(float x, float y, int initial_letter, int initial_digit, int pieceID) {
		
		if (movingPiece != null) {
			throw new IllegalStateException("movingPiece=" + movingPiece);
		}
		
		movingPiece = new MovingPiece();
		
		movingPiece.initial_letter = initial_letter;
		movingPiece.initial_digit = initial_digit;
		movingPiece.x = x;
		movingPiece.y = y;
		movingPiece.pieceID = pieceID;
		movingPiece.moves = selectToFields(initial_letter, initial_digit);

		startHidingPiece(initial_letter, initial_digit, false);

	}


	@Override
	public void clearMovingPiece() {

		stopHidingPiece(movingPiece.initial_letter, movingPiece.initial_digit, true);

		movingPiece = null;

		hidden_letter = -1;
		hidden_digit = -1;
	}

	@Override
	public void startHidingPiece(int letter, int digit, boolean enforce) {

		if (!enforce) {

			if (Application_Chess_BaseImpl.getInstance() != null && Application_Chess_BaseImpl.getInstance().isTestMode()) {

				if (hidden_letter != -1) {

					throw new IllegalStateException("letter=" + letter + ", digit=" + digit + ", hidden_letter=" + hidden_letter + ", hidden_digit=" + hidden_digit);
				}
			}
		}


		hidden_letter = letter;
		hidden_digit = digit;


		if (!movingPiece.dragging) {

			movingPiece.dragging = true;

		} else {

			if (!enforce) {

				throw new IllegalStateException();
			}
		}
	}


	@Override
	public void stopHidingPiece(int letter, int digit, boolean enforce) {

		if (!enforce) {

			if (Application_Chess_BaseImpl.getInstance() != null && Application_Chess_BaseImpl.getInstance().isTestMode()) {

				if (letter != hidden_letter) {

					throw new IllegalStateException("letter=" + letter + ", digit=" + digit + ", hidden_letter=" + hidden_letter + ", hidden_digit=" + hidden_digit);
				}

				if (digit != hidden_digit) {

					throw new IllegalStateException("letter=" + letter + ", digit=" + digit + ", hidden_letter=" + hidden_letter + ", hidden_digit=" + hidden_digit);
				}
			}
		}

		hidden_letter = -1;

		hidden_digit = -1;


		if (movingPiece.dragging) {

			movingPiece.dragging = false;

		} else {

			if (!enforce) {
				throw new IllegalStateException();
			}
		}
	}
}
