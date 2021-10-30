package org.metatrans.commons.chess.main.controllers;


import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.chess.GlobalConstants;
import org.metatrans.commons.chess.logic.BoardConstants;
import org.metatrans.commons.chess.main.MainActivity;
import org.metatrans.commons.chess.main.views.IBoardVisualization;
import org.metatrans.commons.chess.main.views.IPanelsVisualization;

import com.chessartforkids.model.Move;
import com.chessartforkids.model.UserSettings;


public class OnTouchListener_Panels implements OnTouchListener, BoardConstants {
	
	
	private IPanelsVisualization panelsVisualization;
	private IBoardVisualization boardVisualization;
	private MainActivity activity;
	
	
	public OnTouchListener_Panels(IPanelsVisualization _panelsVisualization, IBoardVisualization _boardVisualization, MainActivity _activity) {
		panelsVisualization = _panelsVisualization;
		boardVisualization = _boardVisualization;
		activity = _activity;
	}
	
	
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		
		if (panelsVisualization.isLocked()) {
			return true;
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
		}
		
		return true;
	}
	
	
	private void processEvent_DOWN(MotionEvent event) {
		
		float x = event.getX();
		float y = event.getY();
		
		if (panelsVisualization.isOverButtonMenu(x, y)) {
			panelsVisualization.selectButtonMenu();
		} else if (panelsVisualization.isOverButtonUnmove(x, y)) {
			if (activity.getMainView().getBoardView().hasAnimation()) {
				panelsVisualization.deselectButtonUnmove();
				return;
			}
			panelsVisualization.selectButtonUnmove();
		} else if (panelsVisualization.isOverButtonAutoTop(x, y)) {
			panelsVisualization.selectButtonAutoTop();
			//System.out.println("NEW GAME: DOWN");
		} else if (panelsVisualization.isOverButtonInfo(x, y)) {
			panelsVisualization.selectButton_Info();
		} else if (panelsVisualization.isOverButtonHelpTop(x, y)) {
			panelsVisualization.selectButtonHelpTop();
		} else if (panelsVisualization.isOverButtonHelpBottom(x, y)) {
			panelsVisualization.selectButtonHelpBottom();
		} else if (panelsVisualization.isOverTopArea(x, y)) {
			panelsVisualization.selectButtonTopPlayer();
		} else if (panelsVisualization.isOverBottomArea(x, y)) {
			panelsVisualization.selectButtonBottomPlayer();
		}
		panelsVisualization.redraw();
	}
	
	
	private void processEvent_MOVE(MotionEvent event) {
		
		float x = event.getX();
		float y = event.getY();
		
		if (panelsVisualization.isOverButtonMenu(x, y)) {
			panelsVisualization.selectButtonMenu();
		} else {
			panelsVisualization.deselectButtonMenu();
		}
		
		if (panelsVisualization.isOverButtonUnmove(x, y)) {
			if (activity.getMainView().getBoardView().hasAnimation()) {
				panelsVisualization.deselectButtonUnmove();
				return;
			}
			panelsVisualization.selectButtonUnmove();
		} else {
			panelsVisualization.deselectButtonUnmove();
		}
		
		if (panelsVisualization.isOverButtonAutoTop(x, y)) {
			panelsVisualization.selectButtonAutoTop();
		} else {
			panelsVisualization.deselectButtonAutoTop();
		}
		
		if (panelsVisualization.isOverButtonInfo(x, y)) {
			panelsVisualization.selectButton_Info();
		} else {
			panelsVisualization.deselectButton_Info();
		}
		
		if (panelsVisualization.isOverButtonHelpTop(x, y)) {
			panelsVisualization.selectButtonHelpTop();
		} else {
			panelsVisualization.deselectButtonHelpTop();
		}
		
		if (panelsVisualization.isOverButtonHelpBottom(x, y)) {
			panelsVisualization.selectButtonHelpBottom();
		} else {
			panelsVisualization.deselectButtonHelpBottom();
		}
		
		if (panelsVisualization.isOverTopArea(x, y)) {
			panelsVisualization.selectButtonTopPlayer();
		} else {
			panelsVisualization.deselectButtonTopPlayer();
		}

		if (panelsVisualization.isOverBottomArea(x, y)) {
			panelsVisualization.selectButtonBottomPlayer();
		} else {
			panelsVisualization.deselectButtonBottomPlayer();
		}
		
		panelsVisualization.redraw();
	}
	
	
	private void processEvent_UP(MotionEvent event) {
		
		float x = event.getX();
		float y = event.getY();
		
		if (panelsVisualization.isOverButtonMenu(x, y)) {
			
			panelsVisualization.deselectButtonMenu();
			
			Intent i = new Intent(activity.getApplicationContext(), activity.getMainMenuClass());
			activity.startActivity(i);
			
			//KeyEvent kd = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_SOFT_LEFT);
			//activity.getWindow().openPanel(Window.FEATURE_OPTIONS_PANEL, kd);
			
		} else if (panelsVisualization.isOverButtonUnmove(x, y)) {
			
			panelsVisualization.deselectButtonUnmove();
			
			if (boardVisualization.hasAnimation()) {
				return;
			}
			
			if (activity.getGameController().isThinking()) {
				return;
			}
			
			//Unmove by board manager
			Move move = activity.getGameController().unmove();
			activity.getBoardManager().getGameData().getBoarddata().gameResultText = null;
			
			
			//Visualize needed stuffs
			
			boardVisualization.setData(activity.getBoardManager().getBoard_WithoutHided());
			panelsVisualization.setCapturedPieces(activity.getBoardManager().getCaptured_W(), activity.getBoardManager().getCaptured_B(), activity.getBoardManager().getCapturedSize_W(), activity.getBoardManager().getCapturedSize_B());
			
			activity.getBoardManager().clearMovingPiece();
			boardVisualization.clearSelections();
			
			if (move != null) {
				boardVisualization.markingSelection_Permanent_Border(move.fromLetter, move.fromDigit);
				boardVisualization.markingSelection_Permanent_Border(move.toLetter, move.toDigit);
			}
			
			boardVisualization.redraw();
			
		} else if (panelsVisualization.isOverButtonAutoTop(x, y)) {
			
			panelsVisualization.deselectButtonAutoTop();
			
			/*
			panelsVisualization.finishButtonAutoTop();
			
			if (panelsVisualization.isActiveButtonAutoTop()) {
				
				//Auto player switched on				
				activity.getUserSettings().playerTypeBlack = GlobalConstants.PLAYER_TYPE_COMPUTER;
				activity.getUserSettings().save();
				
				if (activity.getBoardManager().getColourToMove() == COLOUR_PIECE_BLACK) {
					activity.getBoardManager().clearMovingPiece();
					boardVisualization.clearSelections();
					boardVisualization.redraw();
				}
				
				activity.getGameController().lock();
				activity.getGameController().switchOnAutoPlayer(COLOUR_PIECE_BLACK);
				activity.getGameController().unlock();
				
				//System.out.println("SET NAME BLACK: " + activity.getBoardManager().getGameData().black.getName());
				panelsVisualization.setBlackPlayerName(activity.getBoardManager().getGameData().getBlack().getName(activity));

				
			} else {
				
				//Auto player switched off
				activity.getUserSettings().playerTypeBlack = GlobalConstants.PLAYER_TYPE_HUMAN;
				activity.getUserSettings().save();

				activity.getGameController().switchOffAutoPlayer(COLOUR_PIECE_BLACK);

				//System.out.println("SET NAME BLACK: " + activity.getBoardManager().getGameData().black.getName());
				panelsVisualization.setBlackPlayerName(activity.getBoardManager().getGameData().getBlack().getName(activity));
			}*/
			
			if (activity.getMainView().getBoardView().hasAnimation()) {
				return;
			}
			
			((UserSettings)Application_Base.getInstance().getUserSettings()).rotatedboard = !((UserSettings)Application_Base.getInstance().getUserSettings()).rotatedboard;
			Application_Base.getInstance().getUserSettings().save();
			
			activity.getMainView().requestLayout();
			
			boardVisualization.redraw();
			
			
		} else if (panelsVisualization.isOverButtonInfo(x, y)) {
			
			if (panelsVisualization.isActiveButtonInfo()) {
				activity.getUserSettings().infoEnabled = false;
			} else {
				activity.getUserSettings().infoEnabled = true;
			}

			Application_Base.getInstance().getUserSettings().save();
			
			panelsVisualization.finishButtonInfo();
			
		    //force re-calculating the layout dimension and the redraw of the view
			activity.getMainView().requestLayout();
			//activity.getMainView().invalidate();
			
			
		} else if (panelsVisualization.isOverButtonHelpTop(x, y)) {
			panelsVisualization.deselectButtonHelpTop();
		} else if (panelsVisualization.isOverButtonHelpBottom(x, y)) {
			panelsVisualization.deselectButtonHelpBottom();
		} else if (panelsVisualization.isOverTopArea(x, y)) {
			panelsVisualization.deselectButtonTopPlayer();
			panelsVisualization.finishButtonTopPlayer();
			
			if (panelsVisualization.isActiveButtonTopPlayer()) {
				
				//Auto player switched on				
				activity.getUserSettings().playerTypeBlack = GlobalConstants.PLAYER_TYPE_COMPUTER;
				activity.getUserSettings().save();
				
				if (activity.getBoardManager().getColourToMove() == COLOUR_PIECE_BLACK) {
					activity.getBoardManager().clearMovingPiece();
					boardVisualization.clearSelections();
					boardVisualization.redraw();
				}
				
				activity.getGameController().lock();
				activity.getGameController().switchOnAutoPlayer(COLOUR_PIECE_BLACK);
				activity.getGameController().unlock();
				
				//System.out.println("SET NAME BLACK: " + activity.getBoardManager().getGameData().black.getName());
				panelsVisualization.setBlackPlayerName(activity.getBoardManager().getGameData().getBlack().getName(activity));
				
			} else {
				
				//Auto player switched off
				activity.getUserSettings().playerTypeBlack = GlobalConstants.PLAYER_TYPE_HUMAN;
				activity.getUserSettings().save();

				activity.getGameController().switchOffAutoPlayer(COLOUR_PIECE_BLACK);

				//System.out.println("SET NAME BLACK: " + activity.getBoardManager().getGameData().black.getName());
				panelsVisualization.setBlackPlayerName(activity.getBoardManager().getGameData().getBlack().getName(activity));
			}
		} else if (panelsVisualization.isOverBottomArea(x, y)) {
			panelsVisualization.deselectButtonBottomPlayer();
			panelsVisualization.finishButtonBottomPlayer();
			
			if (panelsVisualization.isActiveButtonBottomPlayer()) {
				
				//Auto player switched on
				activity.getUserSettings().playerTypeWhite = GlobalConstants.PLAYER_TYPE_COMPUTER;
				activity.getUserSettings().save();
				
				if (activity.getBoardManager().getColourToMove() == COLOUR_PIECE_WHITE) {
					activity.getBoardManager().clearMovingPiece();
					boardVisualization.clearSelections();
					boardVisualization.redraw();
				}
				
				activity.getGameController().lock();
				activity.getGameController().switchOnAutoPlayer(COLOUR_PIECE_WHITE);
				activity.getGameController().unlock();
				
				//System.out.println("SET NAME WHITE: " + activity.getBoardManager().getGameData().white.getName());
				panelsVisualization.setWhitePlayerName(activity.getBoardManager().getGameData().getWhite().getName(activity));
				
			} else {
				
				//Auto player switched off		
				activity.getUserSettings().playerTypeWhite = GlobalConstants.PLAYER_TYPE_HUMAN;
				activity.getUserSettings().save();
				
				activity.getGameController().switchOffAutoPlayer(COLOUR_PIECE_WHITE);
				
				//System.out.println("SET NAME WHITE: " + activity.getBoardManager().getGameData().white.getName());
				panelsVisualization.setWhitePlayerName(activity.getBoardManager().getGameData().getWhite().getName(activity));
			}
		}
		
		panelsVisualization.redraw();
	}
	
/*
How to open the menu ....
Two ways to do it: 
Activity.getWindow().openPanel(Window.FEATURE_OPTIONS_PANEL, event);
The event argument is a KeyEvent describing the key used to open the menu, which can modify how the menu is displayed based on the type of keyboard it came from. 

Or... by simulating that the user has pressed the button:
IWindowManager wManager = IWindowManager.Stub.asInterface(ServiceManager.getService("window"));
KeyEvent kd = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_SOFT_LEFT);
KeyEvent ku = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_SOFT_LEFT);
wManager.injectKeyEvent(kd.isDown(), kd.getKeyCode(), kd.getRepeatCount(), kd.getDownTime(), kd.getEventTime(), true);
*/

}
