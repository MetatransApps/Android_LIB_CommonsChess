package org.metatrans.commons.chess.main.controllers;


import org.metatrans.commons.chess.GlobalConstants;
import org.metatrans.commons.chess.logic.BoardConstants;
import org.metatrans.commons.chess.logic.BoardUtils;
import org.metatrans.commons.chess.main.views.IBoardViewActivity;
import org.metatrans.commons.chess.main.views.IBoardVisualization;
import org.metatrans.commons.chess.main.views.IPanelsVisualization;
import org.metatrans.commons.chess.menu.MenuActivity_Promotion;
import org.metatrans.commons.chess.model.Move;
import org.metatrans.commons.chess.model.MovingPiece;

import android.app.Activity;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;


public class OnTouchListener_Board implements OnTouchListener, BoardConstants /*, DialogInterface.OnClickListener, DialogInterface.OnDismissListener*/ {
	
	
	private IBoardVisualization boardVisualization;

	private IPanelsVisualization panelsVisualization;

	private IBoardViewActivity activity;

	
	public OnTouchListener_Board(IBoardVisualization _boardVisualization, IBoardViewActivity _activity) {
		this(_boardVisualization, null, _activity);
	}
	
	
	public OnTouchListener_Board(IBoardVisualization _boardVisualization, IPanelsVisualization _panelsVisualization, IBoardViewActivity _activity) {
		boardVisualization = _boardVisualization;
		panelsVisualization = _panelsVisualization;
		activity = _activity;
	}
	
	
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		
		//System.out.println("BOARD: onTouch");
		
		if (boardVisualization.hasAnimation() != -1) {
			return true;
		}
		
		if (activity.getBoardManager().getGameStatus() != GlobalConstants.GAME_STATUS_NONE) {
			return true;
		}
		
		if (activity.getGameController().isThinking()) {
			return true;
		}
		
		if (boardVisualization.isLocked()) {
			return true;
		}
		
		if (activity.getBoardManager().getPlayerWhite().getType() == GlobalConstants.PLAYER_TYPE_COMPUTER
			&& activity.getBoardManager().getPlayerBlack().getType() == GlobalConstants.PLAYER_TYPE_COMPUTER) {
			return true;
		}
		
		float x = event.getX();
		float y = event.getY();
		int letter = boardVisualization.getLetter(x);
		int digit =  boardVisualization.getDigit(y);
		if (letter >= 0 && letter < 8 && digit >= 0 && digit < 8) {
			int pieceID = activity.getBoardManager().getPiece(letter, digit);
			if (pieceID != ID_PIECE_NONE) {
				int colour = BoardUtils.getColour(pieceID);
				if (activity.getBoardManager().getColourToMove() == COLOUR_PIECE_WHITE
						&& colour == COLOUR_PIECE_WHITE
						&& activity.getBoardManager().getPlayerWhite().getType() == GlobalConstants.PLAYER_TYPE_COMPUTER) {
					return true;
				}
				if (activity.getBoardManager().getColourToMove() == COLOUR_PIECE_BLACK
						&& colour == COLOUR_PIECE_BLACK
						&& activity.getBoardManager().getPlayerBlack().getType() == GlobalConstants.PLAYER_TYPE_COMPUTER) {
					return true;
				}
			}
		}
		
		synchronized (activity) {
			int action = event.getAction();

			if (action == MotionEvent.ACTION_DOWN) {
				
				processEvent_DOWN(event);
				
			} else if (action == MotionEvent.ACTION_MOVE) {
				
				processEvent_MOVE(event);
				
			} else if (action == MotionEvent.ACTION_UP
					|| action == MotionEvent.ACTION_CANCEL) {
				
				processEvent_UP(event);
				
			}
			
			boardVisualization.setData(activity.getBoardManager().getBoard_WithoutHided());
			if (panelsVisualization != null) panelsVisualization.setCapturedPieces(activity.getBoardManager().getCaptured_W(), activity.getBoardManager().getCaptured_B(), activity.getBoardManager().getCapturedSize_W(), activity.getBoardManager().getCapturedSize_B());
			
			boardVisualization.redraw();
			if (panelsVisualization != null) panelsVisualization.redraw();
		}
		
		return true;
	}
	
	
	public void processEvent_DOWN(MotionEvent event) {
		
		MovingPiece movingPiece = activity.getBoardManager().getMovingPiece();
		
		float x = event.getX();
		float y = event.getY();
		
		//System.out.println("x=" + x + ", y=" + y + " movingPiece=" + movingPiece);
		
		int letter = boardVisualization.getLetter(x);
		int digit =  boardVisualization.getDigit(y);

		//System.out.println("letter=" + letter + ", digit=" + digit);
		
		
		if (letter >= 0 && letter < 8 && digit >= 0 && digit < 8) {
			
			
			if (movingPiece != null) {

				movingPiece.x = (int) x;
				movingPiece.y = (int) y;

				if (letter == movingPiece.initial_letter
						&& digit == movingPiece.initial_digit) {
					
					//Re-selection of the same moving piece
					movingPiece.dragging = true;

				} else {

					//Target square clicked
					//May be selection of other piece?

					//overField(letter, digit);

					int pieceID = activity.getBoardManager().getPiece(letter, digit);

					if (pieceID != ID_PIECE_NONE) {

						//selection of other piece
						if (activity.getBoardManager().isReSelectionAllowed(movingPiece.pieceID, pieceID)) {

							movingPiece.dragging = false;

							pieceDeSelected();

							pieceSelected(x, y, letter, digit, pieceID);

						} else {

							boardVisualization.makeMovingPiece_OnInvalidSquare();
						}
					} else {

						boardVisualization.makeMovingPiece_OnInvalidSquare();
					}
				}

			} else {
				
				int pieceID = activity.getBoardManager().getPiece(letter, digit);

				if (pieceID != ID_PIECE_NONE) {
					
					if (activity.getBoardManager().isSelectionAllowed(pieceID)) {
						
						//Selection of piece
						pieceSelected(x, y, letter, digit, pieceID);
						
					} else {

						//selection of piece of side, which is not on move
						boardVisualization.invalidSelection_Temp_Square(letter, digit);
					}
				} else {
					
					//Selection of empty square
					
					boardVisualization.invalidSelection_Temp_Square(letter, digit);
				}
			}
			
			overField(letter, digit);
			
		} else {

			//if (true) throw new IllegalStateException();
			System.out.println("processEvent_DOWN is outside the board");
		}
	}
	
	
	public void processEvent_MOVE(MotionEvent event) {

		MovingPiece movingPiece = activity.getBoardManager().getMovingPiece();
		
		float x = event.getX();

		float y = event.getY();

		int letter = boardVisualization.getLetter(x);

		int digit =  boardVisualization.getDigit(y);

		if (movingPiece != null) {

			movingPiece.x = (int) x;

			movingPiece.y = (int) y;
		}
		
		if (letter >= 0 && letter < 8 && digit >= 0 && digit < 8) {

			overField(letter, digit);

			boardVisualization.makeMovingPiece_OnInvalidSquare();
		}
	}
	
	
	public void processEvent_UP(MotionEvent event) {
		
		MovingPiece movingPiece = activity.getBoardManager().getMovingPiece();
		
		float x = event.getX();
		float y = event.getY();
		
		int letter = boardVisualization.getLetter(x);
		int digit =  boardVisualization.getDigit(y);
		
		
		if (letter >= 0 && letter < 8 && digit >= 0 && digit < 8) {
			
			overField(letter, digit);
			
			if (movingPiece != null) {


				//Re-selection of the same piece
				if (letter == movingPiece.initial_letter && digit == movingPiece.initial_digit) {

					//activity.getBoardManager().stopHidingPiece(movingPiece.initial_letter, movingPiece.initial_digit);
					//TODO: re-create moving piece or use the existing one?

				} else {
					
					Move move = null;
					for (Move cur: movingPiece.moves) {
						if (letter == cur.toLetter && digit == cur.toDigit) {
							move = cur;
							break;
						}
					}
					
					if (move != null) {
						
						//Moving piece to the final destination

						movingPiece.capturedPID = activity.getBoardManager().getPiece(letter, digit);
						
						if (activity.getBoardManager().isPromotion(movingPiece.pieceID, digit)) {
							
							movingPiece.promotion_letter = letter;
							movingPiece.promotion_digit = digit;

							Intent i = new Intent( ((Activity) activity).getApplicationContext(), getPromotionActivityClass());

							((Activity)activity).startActivity(i);
							
						} else {

							//Will call start animation code a bit later in the stack
							activity.getGameController().acceptNewMove(move, null);

						}

					} else {
						
						//Not valid move
						boardVisualization.invalidSelection_Temp_Square(letter, digit);

					}
				}
			}
			
		} else {

			if (movingPiece != null) {
				
				//Dropped out of the board

			}
		}
	}
	
	
	public Class<?> getPromotionActivityClass() {
		return MenuActivity_Promotion.class;
	}
	
	
	private void pieceSelected(float x, float y, int letter, int digit, int pieceID) {
		
		activity.getBoardManager().createMovingPiece(x, y, letter, digit, pieceID);
		
		boardVisualization.clearSelections();
		
		boardVisualization.makeMovingPieceSelections();
	}
	
	
	private void pieceDeSelected() {

		//System.out.println("ACTION DESELECTED: " + visualization.getMovingPiece().pieceID);

		activity.getBoardManager().clearMovingPiece();
		
		boardVisualization.clearSelections();
	}
	
	
	private void overField(int letter, int digit) {
		
		if (!(letter >= 0 && letter < 8 && digit >= 0 && digit < 8)) {

			throw new IllegalStateException();
		}
		
		MovingPiece movingPiece = activity.getBoardManager().getMovingPiece();
		
		if (movingPiece != null) {
			
			Move move = null;

			for (Move cur: movingPiece.moves) {

				if (letter == cur.toLetter && digit == cur.toDigit) {

					move = cur;

					break;
				}
			}
			
			if (move != null) {

				boardVisualization.validSelection_Temp_Square(letter, digit);

			} else {

				boardVisualization.invalidSelection_Temp_Square(letter, digit);
				
				if (movingPiece.moves.size() == 0) {

					boardVisualization.makeMovablePiecesSelections();
				}
			}
			
		} else {
		
			boardVisualization.invalidSelection_Temp_Square(letter, digit);
			
			boardVisualization.makeMovablePiecesSelections();
		}
	}
}
