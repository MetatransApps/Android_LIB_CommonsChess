package org.metatrans.commons.chess.main.controllers;


import org.metatrans.commons.chess.GlobalConstants;
import org.metatrans.commons.chess.logic.BoardConstants;
import org.metatrans.commons.chess.logic.BoardUtils;
import org.metatrans.commons.chess.main.views.IBoardViewActivity;
import org.metatrans.commons.chess.main.views.IBoardVisualization;
import org.metatrans.commons.chess.main.views.IPanelsVisualization;
import org.metatrans.commons.chess.menu.MenuActivity_Promotion;

import com.chessartforkids.model.Move;
import com.chessartforkids.model.MovingPiece;

import android.app.Activity;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;


public class OnTouchListener_Board implements OnTouchListener, BoardConstants /*, DialogInterface.OnClickListener, DialogInterface.OnDismissListener*/ {
	
	
	private IBoardVisualization boardVisualization;
	private IPanelsVisualization panelsVisualization;
	private IBoardViewActivity activity;
	
	//private boolean promotionFlag = false;
	
	
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
		
		/*if (boardVisualization == null
				|| panelsVisualization == null) {
			return true;
		}*/
		
		if (boardVisualization.hasAnimation()) {
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
					
					if (!movingPiece.dragging) {
						activity.getBoardManager().startHidingPiece(letter, digit);
						movingPiece.dragging = true;
					}
					
				} else {
					
					//Selection of other piece
					
					int pieceID = activity.getBoardManager().getPiece(letter, digit);
					if (activity.getBoardManager().isReSelectionAllowed(movingPiece.pieceID, pieceID)) {
						
						pieceDeSelected();
						
						pieceSelected(x, y, letter, digit, pieceID);
						movingPiece = activity.getBoardManager().getMovingPiece();
					}
				}

			} else {
				
				int pieceID = activity.getBoardManager().getPiece(letter, digit);
				if (pieceID != ID_PIECE_NONE) {
					
					if (activity.getBoardManager().isSelectionAllowed(pieceID)) {
						
						//Selection of piece
						pieceSelected(x, y, letter, digit, pieceID);
						movingPiece = activity.getBoardManager().getMovingPiece();
						
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
				
				if (letter == movingPiece.initial_letter && digit == movingPiece.initial_digit) {
					
					if (movingPiece.dragging) {
						activity.getBoardManager().stopHidingPiece(movingPiece.initial_letter, movingPiece.initial_digit);
						movingPiece.dragging = false;
					}
					
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
							
							//activity.showDialog(0);
							
							Intent i = new Intent(((Activity)activity).getApplicationContext(), getPromotionActivityClass());
							//i.putExtra("new_variable_name","value");
							((Activity)activity).startActivity(i);
							
						} else {
							boardVisualization.startMoveAnimation(move, activity.getBoardManager().getMovingPiece());
						}
					} else {
						
						//Not valid move
						boardVisualization.invalidSelection_Temp_Square(letter, digit);
						
						if (movingPiece.dragging) {
							activity.getBoardManager().stopHidingPiece(movingPiece.initial_letter, movingPiece.initial_digit);
							movingPiece.dragging = false;
						}
					}
				}
			}
			
		} else {
			if (movingPiece != null) {
				
				//Dropped out of the board
				
				if (movingPiece.dragging) {
					activity.getBoardManager().stopHidingPiece(movingPiece.initial_letter, movingPiece.initial_digit);
					movingPiece.dragging = false;
				}
			}
		}
	}
	
	
	public Class<?> getPromotionActivityClass() {
		return MenuActivity_Promotion.class;
	}
	
	
	/*@Override
	public void onClick(DialogInterface dialog, int which) {
		
		MovingPiece movingPiece = activity.getBoardManager().getMovingPiece();
		int colour = BoardUtils.getColour(movingPiece.pieceID);
		
		int promotedPieceID = -1;
		switch (which) {
			case 0: //queen
				promotedPieceID = (colour == COLOUR_PIECE_WHITE) ? ID_PIECE_W_QUEEN : ID_PIECE_B_QUEEN;
				break;
			case 1: //rook
				promotedPieceID = (colour == COLOUR_PIECE_WHITE) ? ID_PIECE_W_ROOK : ID_PIECE_B_ROOK;
				break;
			case 2: //bishop
				promotedPieceID = (colour == COLOUR_PIECE_WHITE) ? ID_PIECE_W_BISHOP : ID_PIECE_B_BISHOP;
				break;
			case 3: //knight
				promotedPieceID = (colour == COLOUR_PIECE_WHITE) ? ID_PIECE_W_KNIGHT : ID_PIECE_B_KNIGHT;
				break;
			default:
				throw new IllegalStateException("which=" + which);
		}
		
		Move move = null;
		for (Move cur: movingPiece.moves) {
			if (movingPiece.promotion_letter == cur.toLetter
					&& movingPiece.promotion_digit == cur.toDigit
					&& promotedPieceID == cur.promotedPieceID) {
				move = cur;
				break;
			}
		}
		
		//System.out.println("MOVE: " + move);
		
		activity.getBoardManager().move(move);
		activity.getBoardManager().clearMovingPiece();
		
		pieceMoved(move);
		
		boardVisualization.setData(activity.getBoardManager().getBoard_WithoutHided());
		panelsVisualization.setCapturedPieces(activity.getBoardManager().getCaptured_W(), activity.getBoardManager().getCaptured_B(), activity.getBoardManager().getCapturedSize_W(), activity.getBoardManager().getCapturedSize_B());
		boardVisualization.redraw();
		panelsVisualization.redraw();
		
		promotionFlag = true;
	}*/
	
	
	/*
	@Override
	public void onDismiss(DialogInterface dialog) {
		
		if (promotionFlag) {
			//Promoting piece type is selected and reflected on the board
			promotionFlag = false;
			
			//System.out.println("PROM: " + false);
		} else {
			
			//Nothing selected: the changes must be reverted
			MovingPiece movingPiece = activity.getBoardManager().getMovingPiece();
			
			if (movingPiece != null) {
				
				if (movingPiece.dragging) {
					activity.getBoardManager().stopHidingPiece(movingPiece.initial_letter, movingPiece.initial_digit);
					movingPiece.dragging = false;
				}
			}
			
			boardVisualization.setData(activity.getBoardManager().getBoard_WithoutHided());
			panelsVisualization.setCapturedPieces(activity.getBoardManager().getCaptured_W(), activity.getBoardManager().getCaptured_B(), activity.getBoardManager().getCapturedSize_W(), activity.getBoardManager().getCapturedSize_B());
			panelsVisualization.redraw();
			
			//System.out.println("PROM: " + true);
		}
		
		boardVisualization.redraw();
	}*/
	
	
	private void pieceSelected(float x, float y, int letter, int digit, int pieceID) {
		
		//System.out.println("ACTION SELECTED: " + pieceID); 
		
		
		activity.getBoardManager().createMovingPiece(x, y, letter, digit, pieceID);
		MovingPiece movingPiece = activity.getBoardManager().getMovingPiece();
		
		if (!movingPiece.dragging) {
			activity.getBoardManager().startHidingPiece(letter, digit);
			movingPiece.dragging = true;
		}
		
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
