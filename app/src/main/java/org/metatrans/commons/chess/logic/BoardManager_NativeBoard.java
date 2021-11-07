package org.metatrans.commons.chess.logic;


import java.util.ArrayList;
import java.util.List;

import org.metatrans.commons.cfg.difficulty.IConfigurationDifficulty;
import org.metatrans.commons.chess.GlobalConstants;
import org.metatrans.commons.chess.logic.computer.ComputerPlayer_RandomButCaptureAndDefense;
import org.metatrans.commons.chess.logic.computer.IComputer;
import org.metatrans.commons.chess.model.GameData;
import org.metatrans.commons.chess.model.Move;

import bagaturchess.bitboard.api.IBitBoard;
import bagaturchess.bitboard.impl.Constants;
import bagaturchess.bitboard.impl.Fields;
import bagaturchess.bitboard.impl.movelist.BaseMoveList;


public abstract class BoardManager_NativeBoard extends BoardManager_BaseImpl {
	
	
	protected IBitBoard board;
	
	private BaseMoveList buff_moves = new BaseMoveList(333);
	
	
	public BoardManager_NativeBoard(GameData gamedata, IBitBoard _board) {
		
		super(gamedata);
		
		board = _board;
		
		int currentMoveIndex = gamedata.getCurrentMoveIndex();
		List<Move> moves = gamedata.getMoves();
		for (int i = 0; i <= currentMoveIndex; i++) {
			move(moves.get(i));
		}
		
		//System.out.println("MOVE INDEX (NativeBoard constructor) " + gamedata.getCurrentMoveIndex());
		
		handleMovingPiece(gamedata);
	}
	
	
	private void handleMovingPiece(GameData gamedata) {

		if (gamedata.getMovingPiece() != null) {

			//stopHidingPiece(gamedata.getMovingPiece().initial_letter, gamedata.getMovingPiece().initial_digit);

			/*if (gamedata.getMovingPiece().dragging) {

				movingPiece.dragging = false;
				//throw new IllegalStateException("Piece is dragging");
			}*/
			
			//FIXME: DURTY FIX: the piece should exists on that square
			/*if (getPiece(movingPiece.initial_letter, movingPiece.initial_digit) == ID_PIECE_NONE) {

				clearMovingPiece();
				gamedata.setMovingPiece(null);
				
			} else {*/
				movingPiece.moves = selectToFields(movingPiece.initial_letter, movingPiece.initial_digit);
			//}
		}
	}
	
	
	public abstract IBitBoard createBoard();
	
	
	@Override
	protected IComputer createComputerPlayer(int modeID, int colour) {
		if (modeID == IConfigurationDifficulty.MODE_COMPUTER_RANDOM_BUT_CAPTURE_AND_DEFENSE) {
			return new ComputerPlayer_RandomButCaptureAndDefense(colour, this, THINK_TIME_FOR_LEVELS_1_TO_4);
		} else if (modeID == IConfigurationDifficulty.MODE_COMPUTER_POSITIONAL_EVALUATION) {
			throw new IllegalStateException("not implemented");
		} else if (modeID == IConfigurationDifficulty.MODE_COMPUTER_ENGINE_1SEC) {
			throw new IllegalStateException("not implemented");
		} else if (modeID == IConfigurationDifficulty.MODE_COMPUTER_ENGINE_3SEC) {
			throw new IllegalStateException("not implemented");
		} else if (modeID == IConfigurationDifficulty.MODE_COMPUTER_ENGINE_7SEC) {
			throw new IllegalStateException("not implemented");
		} else if (modeID == IConfigurationDifficulty.MODE_COMPUTER_ENGINE_15SEC) {
			throw new IllegalStateException("not implemented");
		} else {
			return super.createComputerPlayer(modeID, colour);
		}
	}
	
	
	public int[][] getBoard_Full() {

		int[][] pieces = getPiecesArray();
		
		for (int boardLetter=0; boardLetter < 8; boardLetter++) {

			for (int boardDigit=0; boardDigit < 8; boardDigit++) {
				
				//!!! SWITCH digit and letter in native method signature
				int fieldID = getBoardFieldID(boardLetter, boardDigit);
				
				int type = board.getFigureType(fieldID);
				int colour = type == Constants.TYPE_NONE ? -1 : board.getFigureColour(fieldID);
				
				pieces[convertBoardLetterToUI(boardLetter)][7 - boardDigit] = convertPID_b2ui(type, colour);
			}
		}
		
		return pieces;
	}
	
	
	protected int convertBoardLetterToUI(int boardLetter) {
		return 7 - boardLetter;
	}
	
	
	public IBitBoard getBoard_Native() {
		return board;
	}
	
	
	@Override
	public boolean isInCheck(int color) {
		return color == COLOUR_PIECE_WHITE ? board.isInCheck(Constants.COLOUR_WHITE) : board.isInCheck(Constants.COLOUR_BLACK);
	}
	
	
	@Override
	public boolean isOpponentInCheck() {
		return getColourToMove() == COLOUR_PIECE_WHITE ? board.isInCheck(Constants.COLOUR_BLACK) : board.isInCheck(Constants.COLOUR_WHITE);
	}
	
	
	@Override
	public int getColourToMove() {
		return (board.getColourToMove() == Constants.COLOUR_WHITE) ? COLOUR_PIECE_WHITE : COLOUR_PIECE_BLACK;
	}


	@Override
	public boolean getWKingSideAvailable() {
		return board.hasRightsToKingCastle(Constants.COLOUR_WHITE);
	}


	@Override
	public boolean getWQueenSideAvailable() {
		return board.hasRightsToQueenCastle(Constants.COLOUR_WHITE);
	}


	@Override
	public boolean getBKingSideAvailable() {
		return board.hasRightsToKingCastle(Constants.COLOUR_BLACK);
	}


	@Override
	public boolean getBQueenSideAvailable() {
		return board.hasRightsToQueenCastle(Constants.COLOUR_BLACK);
	}


	@Override
	public String getEnpassantSquare() {
		// TODO: Implement
		return null;
	}
	

	@Override
	public int getMoveScores(Move move) {
		return board.getSEEScore(move.nativemove);
	}
	
	
	protected abstract boolean isKingCaptureEnabled();
	
	
	public synchronized List<Move> selectToFields(int uiLetter, int uiDigit) {
		
		List<Move> result = new ArrayList<Move>(8);
		
		if (getPiece(uiLetter, uiDigit) == ID_PIECE_NONE) {
			//throw new IllegalStateException("getPiece(uiLetter, uiDigit) == ID_PIECE_NONE");
			return result;
		}
		
		if (isKingCaptureEnabled()) {
			buff_moves.clear();
			board.genAllMoves(buff_moves);
		} else {
			buff_moves.clear();
			if (board.isInCheck()) {
				board.genKingEscapes(buff_moves);
			} else {
				board.genAllMoves(buff_moves);
			}
		}
		
		int movesSize = buff_moves.reserved_getCurrentSize();
		int[] moves = buff_moves.reserved_getMovesBuffer();
		
		for (int i=0; i<movesSize; i++) {
			
			int moveInt = moves[i];
			int fromFieldID = board.getMoveOps().getFromFieldID(moveInt);
			int boardLetter = board.getMoveOps().getFromField_File(moveInt);//Fields.LETTERS[fromFieldID];
			int boardDigit = board.getMoveOps().getFromField_Rank(moveInt);//Fields.DIGITS[fromFieldID];
			int moveColour = board.getFigureColour(fromFieldID);
					
			if (boardLetter == uiLetter && boardDigit == 7 - uiDigit) {
				
				//System.out.println("MOVE: " + MoveInt.moveToStringUCI(moveInt));
				
				//int toFieldID = board.getMoveOps().getToFieldID(moveInt);
				int toBoardLetter = board.getMoveOps().getToField_File(moveInt);//Fields.LETTERS[toFieldID];
				int toBoardDigit = board.getMoveOps().getToField_Rank(moveInt);//Fields.DIGITS[toFieldID];
				
				int toUILetter = toBoardLetter;
				int toUIDigit = 7 - toBoardDigit;
				
				Move move = new Move();
				
				move.fromLetter = uiLetter;
				move.fromDigit = uiDigit;
				move.toLetter = toUILetter;
				move.toDigit = toUIDigit;
				move.nativemove = moveInt;
				move.pieceID = convertPID_b2ui(board.getMoveOps().getFigureType(moveInt), moveColour);
				move.isPromotion = board.getMoveOps().isPromotion(moveInt);
				if (move.isPromotion) {
					move.promotedPieceID = convertPID_b2ui(board.getMoveOps().getPromotionFigureType(moveInt), moveColour);
				}
				move.isCapture = board.getMoveOps().isCapture(moveInt);
				if (move.isCapture) {
					move.capturedPieceID = convertPID_b2ui(board.getMoveOps().getCapturedFigureType(moveInt), moveColour == Constants.COLOUR_WHITE ? Constants.COLOUR_BLACK : Constants.COLOUR_WHITE);
				}
				
				if (isKingCaptureEnabled()) {
					result.add(move);
				} else {
					if (move.capturedPieceID != ID_PIECE_W_KING && move.capturedPieceID != ID_PIECE_B_KING) {
						result.add(move);
					}
				}
			}
		}
		
		return result;
	}
	
	
	@Override
	public synchronized int getGameStatus() {
		
		if (board.getStateRepetition() >= 3) {
			return GlobalConstants.GAME_STATUS_DRAW;
		}
		
		if (board.isDraw50movesRule()) {
			return GlobalConstants.GAME_STATUS_DRAW;
		}
		
		return super.getGameStatus();
	}
	
	
	@Override
	public void move(Move move) {

		board.makeMoveForward(move.nativemove);
		
		super.move(move);
	}
	
	
	@Override
	public void unmove(Move move) {

		board.makeMoveBackward(move.nativemove);

		super.unmove(move);
	}
	
	
	@Override
	public int getPiece(int uiLetter, int uiDigit) {
		int boardLetter = convertBoardLetterToUI(uiLetter);
		int boardDigit = 7 - uiDigit;
		int boardFieldID = getBoardFieldID(boardLetter, boardDigit);
		int boardPieceType = board.getFigureType(boardFieldID);
		int boardPieceColour = boardPieceType == Constants.TYPE_NONE ? -1 : board.getFigureColour(boardFieldID);
		return convertPID_b2ui(boardPieceType, boardPieceColour);
	}
	
	@Override
	public boolean isSelectionAllowed(int pieceID) {
		int uiColour = BoardUtils.getColour(pieceID);
		int boardColour = board.getColourToMove();
		
		boolean isUIColourWhite = uiColour == BoardConstants.COLOUR_PIECE_WHITE; 
		boolean isBoardColourWhite = boardColour == Constants.COLOUR_WHITE; 
		
		return isUIColourWhite == isBoardColourWhite;
	}
	
	@Override
	public boolean isReSelectionAllowed(int currentPieceID, int nextPieceID) {
		//return (nextPieceID != ID_PIECE_NONE && BoardUtils.getColour(nextPieceID) == BoardUtils.getColour(currentPieceID));
		return (nextPieceID != ID_PIECE_NONE) && isSelectionAllowed(nextPieceID);
	}
	
	//!!! SWITCH digit and letter
	private int getBoardFieldID(int boardLetter, int boardDigit) {
		//!!! SWITCH digit and letter
		return Fields.getFieldIDByFileAndRank(boardDigit, boardLetter);
	}
	
	protected int convertPID_b2ui(int type, int colour) {

		switch (type) {
		
			case Constants.TYPE_NONE:
				return ID_PIECE_NONE;
				
			case Constants.TYPE_KING:
				if (colour == Constants.COLOUR_WHITE) {
					return ID_PIECE_W_KING;
				} else {
					return ID_PIECE_B_KING;
				}
			case Constants.TYPE_QUEEN:
				if (colour == Constants.COLOUR_WHITE) {
					return ID_PIECE_W_QUEEN;
				} else {
					return ID_PIECE_B_QUEEN;
				}
				
			case Constants.TYPE_ROOK:
				if (colour == Constants.COLOUR_WHITE) {
					return ID_PIECE_W_ROOK;
				} else {
					return ID_PIECE_B_ROOK;
				}
				
			case Constants.TYPE_BISHOP:
				if (colour == Constants.COLOUR_WHITE) {
					return ID_PIECE_W_BISHOP;
				} else {
					return ID_PIECE_B_BISHOP;
				}
				
			case Constants.TYPE_KNIGHT:
				if (colour == Constants.COLOUR_WHITE) {
					return ID_PIECE_W_KNIGHT;
				} else {
					return ID_PIECE_B_KNIGHT;
				}
				
			case Constants.TYPE_PAWN:
				if (colour == Constants.COLOUR_WHITE) {
					return ID_PIECE_W_PAWN;
				} else {
					return ID_PIECE_B_PAWN;
				}
			
			default:
				throw new IllegalStateException("type=" + type);
		}
	}
}
