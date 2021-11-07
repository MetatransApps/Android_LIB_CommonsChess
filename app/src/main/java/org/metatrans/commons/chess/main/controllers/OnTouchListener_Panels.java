package org.metatrans.commons.chess.main.controllers;


import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.chess.GlobalConstants;
import org.metatrans.commons.chess.logic.BoardConstants;
import org.metatrans.commons.chess.logic.IBoardManager;
import org.metatrans.commons.chess.main.MainActivity;
import org.metatrans.commons.chess.main.views.IBoardVisualization;
import org.metatrans.commons.chess.main.views.PanelsView;
import org.metatrans.commons.chess.model.GameData;
import org.metatrans.commons.chess.model.Move;
import org.metatrans.commons.chess.model.UserSettings;

import java.util.List;


public class OnTouchListener_Panels implements OnTouchListener, BoardConstants {
	
	
	private PanelsView panelsView;

	private IBoardVisualization boardVisualization;

	private MainActivity activity;
	
	
	public OnTouchListener_Panels(PanelsView _panelsVisualization, IBoardVisualization _boardVisualization, MainActivity _activity) {
		panelsView = _panelsVisualization;
		boardVisualization = _boardVisualization;
		activity = _activity;
	}
	
	
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		
		if (panelsView.isLocked()) {

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
		
		if (panelsView.isOverButtonMenu(x, y)) {

			panelsView.selectButtonMenu();

		} else if (panelsView.isOverButtonUnmove(x, y)) {

			if (activity.getMainView().getBoardView().hasAnimation() != -1) {

				panelsView.deselectButtonUnmove();

				return;
			}

			panelsView.selectButtonUnmove();

		} else if (panelsView.isOverButtonAutoTop(x, y)) {

			if (boardVisualization.hasAnimation() != -1) {

				return;
			}

			panelsView.selectButtonAutoTop();

		} else if (panelsView.isOverButtonInfo(x, y)) {

			panelsView.selectButton_Info();

		} else if (panelsView.isOverButtonHelpTop(x, y)) {

			panelsView.selectButtonHelpTop();

		} else if (panelsView.isOverButtonHelpBottom(x, y)) {

			panelsView.selectButtonHelpBottom();

		} else if (panelsView.isOverTopArea(x, y)) {

			panelsView.selectButtonTopPlayer();

		} else if (panelsView.isOverBottomArea(x, y)) {

			panelsView.selectButtonBottomPlayer();

		} else if (panelsView.rectangle_bottom1 != null) {

			if (panelsView.isMoveNavigationAndAutoPlayerButtonsLocked()) return;

			GameData gamedata = activity.getBoardManager().getGameData();

			if (!gamedata.isOnTheFirstMove() && panelsView.rectf_play_first.contains(x, y)) {

				panelsView.play_first.select();

			} else if (!gamedata.isOnTheFirstMove() && panelsView.rectf_play_prev.contains(x, y)) {

				panelsView.play_prev.select();

			} else if (!gamedata.isOnTheLastMove() && panelsView.rectf_play_next.contains(x, y)) {

				panelsView.play_next.select();

			} else if (!gamedata.isOnTheLastMove() && panelsView.rectf_play_last.contains(x, y)) {

				panelsView.play_last.select();
			}
		}

		panelsView.redraw();
	}


	private void processEvent_MOVE(MotionEvent event) {
		
		float x = event.getX();
		float y = event.getY();
		
		if (panelsView.isOverButtonMenu(x, y)) {

			panelsView.selectButtonMenu();

		} else {

			panelsView.deselectButtonMenu();
		}
		
		if (panelsView.isOverButtonUnmove(x, y)) {
			if (activity.getMainView().getBoardView().hasAnimation() != -1) {
				panelsView.deselectButtonUnmove();
				return;
			}
			panelsView.selectButtonUnmove();
		} else {
			panelsView.deselectButtonUnmove();
		}

		if (boardVisualization.hasAnimation() == -1) {

			if (panelsView.isOverButtonAutoTop(x, y)) {

				panelsView.selectButtonAutoTop();

			} else {

				panelsView.deselectButtonAutoTop();
			}
		}
		
		if (panelsView.isOverButtonInfo(x, y)) {
			panelsView.selectButton_Info();
		} else {
			panelsView.deselectButton_Info();
		}
		
		if (panelsView.isOverButtonHelpTop(x, y)) {
			panelsView.selectButtonHelpTop();
		} else {
			panelsView.deselectButtonHelpTop();
		}
		
		if (panelsView.isOverButtonHelpBottom(x, y)) {
			panelsView.selectButtonHelpBottom();
		} else {
			panelsView.deselectButtonHelpBottom();
		}
		
		if (panelsView.isOverTopArea(x, y)) {
			panelsView.selectButtonTopPlayer();
		} else {
			panelsView.deselectButtonTopPlayer();
		}

		if (panelsView.isOverBottomArea(x, y)) {
			panelsView.selectButtonBottomPlayer();
		} else {
			panelsView.deselectButtonBottomPlayer();
		}

		if (panelsView.rectangle_bottom1 != null) {

			if (panelsView.isMoveNavigationAndAutoPlayerButtonsLocked()) return;

			GameData gamedata = activity.getBoardManager().getGameData();

			if (!gamedata.isOnTheFirstMove() && panelsView.rectf_play_first.contains(x, y)) {
				panelsView.play_first.select();
			} else {
				panelsView.play_first.deselect();
			}

			if (!gamedata.isOnTheFirstMove() && panelsView.rectf_play_prev.contains(x, y)) {
				panelsView.play_prev.select();
			} else {
				panelsView.play_prev.deselect();
			}

			if (!gamedata.isOnTheLastMove() &&panelsView.rectf_play_next.contains(x, y)) {
				panelsView.play_next.select();
			} else {
				panelsView.play_next.deselect();
			}

			if (!gamedata.isOnTheLastMove() && panelsView.rectf_play_last.contains(x, y)) {
				panelsView.play_last.select();
			} else {
				panelsView.play_last.deselect();
			}
		}

		panelsView.redraw();
	}
	
	
	private void processEvent_UP(MotionEvent event) {
		
		float x = event.getX();
		float y = event.getY();
		
		if (panelsView.isOverButtonMenu(x, y)) {
			
			panelsView.deselectButtonMenu();
			
			Intent i = new Intent(activity.getApplicationContext(), activity.getMainMenuClass());
			activity.startActivity(i);
			
		} else if (panelsView.isOverButtonUnmove(x, y)) {
			
			panelsView.deselectButtonUnmove();

			if (panelsView.isMoveNavigationAndAutoPlayerButtonsLocked()) return;

			GameData gamedata = activity.getBoardManager().getGameData();

			List<Move> moves = gamedata.getMoves();
			int moveIndex = gamedata.getCurrentMoveIndex();
			Move lastmove = null;
			if (moveIndex >= 0) {
				lastmove = moves.get(moveIndex);
				activity.getGameController().scheduleComputerPlayerAutoStart();
				activity.getBoardManager().unmove(lastmove);
				gamedata.setCurrentMoveIndex(moveIndex - 1);
				gamedata.save();
			}

			boardVisualization.setTopMessageText(null);
			
			
			//Visualize needed stuffs
			
			boardVisualization.setData(activity.getBoardManager().getBoard_WithoutHided());
			panelsView.setCapturedPieces(activity.getBoardManager().getCaptured_W(), activity.getBoardManager().getCaptured_B(), activity.getBoardManager().getCapturedSize_W(), activity.getBoardManager().getCapturedSize_B());

			if (activity.getBoardManager().getMovingPiece() != null) activity.getBoardManager().clearMovingPiece();

			boardVisualization.clearSelections();
			
			if (lastmove != null) {
				boardVisualization.markingSelection_Permanent_Border(lastmove.fromLetter, lastmove.fromDigit);
				boardVisualization.markingSelection_Permanent_Border(lastmove.toLetter, lastmove.toDigit);
			}
			
			boardVisualization.redraw();
			
		} else if (panelsView.isOverButtonAutoTop(x, y)) {

			panelsView.deselectButtonAutoTop();

			if (boardVisualization.hasAnimation() != -1) {

				return;
			}

			((UserSettings)Application_Base.getInstance().getUserSettings()).rotatedboard = !((UserSettings)Application_Base.getInstance().getUserSettings()).rotatedboard;
			Application_Base.getInstance().getUserSettings().save();
			
			activity.getMainView().requestLayout();
			
			boardVisualization.redraw();
			
			
		} else if (panelsView.isOverButtonInfo(x, y)) {
			
			if (panelsView.isActiveButtonInfo()) {
				activity.getUserSettings().infoEnabled = false;
			} else {
				activity.getUserSettings().infoEnabled = true;
			}

			Application_Base.getInstance().getUserSettings().save();
			
			panelsView.finishButtonInfo();
			
		    //force re-calculating the layout dimension and the redraw of the view
			activity.getMainView().requestLayout();
			
			
		} else if (panelsView.isOverButtonHelpTop(x, y)) {

			panelsView.deselectButtonHelpTop();

		} else if (panelsView.isOverButtonHelpBottom(x, y)) {

			panelsView.deselectButtonHelpBottom();

		} else if (panelsView.isOverTopArea(x, y)) {

			panelsView.deselectButtonTopPlayer();

			panelsView.finishButtonTopPlayer();

			UserSettings settings = activity.getUserSettings();

			if (!settings.auto_player_enabled_black) {

				//Auto player switched on
				activity.getGameController().switchOnAutoPlayer(COLOUR_PIECE_BLACK);
				
			} else {

				//Auto player switched off
				activity.getGameController().switchOffAutoPlayer(COLOUR_PIECE_BLACK);

				//Enable human player to manually select piece and make move via UI.
				//The UI of the board should be unlocked for this purpose.
				activity.getBoard().unlock();
			}

		} else if (panelsView.isOverBottomArea(x, y)) {

			panelsView.deselectButtonBottomPlayer();

			panelsView.finishButtonBottomPlayer();

			UserSettings settings = activity.getUserSettings();

			if (!settings.auto_player_enabled_white) {

				//Auto player switched on
				activity.getGameController().switchOnAutoPlayer(COLOUR_PIECE_WHITE);

			} else {

				//Auto player switched off
				activity.getGameController().switchOffAutoPlayer(COLOUR_PIECE_WHITE);

				//Enable human player to manually select piece and make move via UI.
				//The UI of the board should be unlocked for this purpose.
				activity.getBoard().unlock();
			}

		} else if (panelsView.rectangle_bottom1 != null) {

			panelsView.play_first.deselect();
			panelsView.play_prev.deselect();
			panelsView.play_next.deselect();
			panelsView.play_last.deselect();

			if (panelsView.isMoveNavigationAndAutoPlayerButtonsLocked()) {

				return;
			}

			if (panelsView.rectf_play_first.contains(x, y)) {

				GameData gamedata = activity.getBoardManager().getGameData();

				if (gamedata.isOnTheFirstMove()) {

					return;
				}

				IBoardManager boardManager = activity.getBoardManager();

				List<Move> moves = gamedata.getMoves();
				int moveIndex = gamedata.getCurrentMoveIndex();
				Move lastmove = null;
				for (int i = moveIndex; i >= 0; i--) {
					lastmove = moves.get(i);
					activity.getGameController().scheduleComputerPlayerAutoStart();
					boardManager.unmove(lastmove);
				}
				gamedata.setCurrentMoveIndex(-1);

				Application_Base.getInstance().storeGameData(gamedata);

				if (activity.getBoardManager().getMovingPiece() != null) activity.getBoardManager().clearMovingPiece();


				boardVisualization.clearSelections();
				if (lastmove != null) boardVisualization.markingSelection_Permanent_Border(lastmove.fromLetter, lastmove.fromDigit);
				if (lastmove != null) boardVisualization.markingSelection_Permanent_Border(lastmove.toLetter, lastmove.toDigit);

				boardVisualization.setData(boardManager.getBoard_WithoutHided());
				boardVisualization.setTopMessageText(null);
				panelsView.setCapturedPieces(activity.getBoardManager().getCaptured_W(), activity.getBoardManager().getCaptured_B(), activity.getBoardManager().getCapturedSize_W(), activity.getBoardManager().getCapturedSize_B());
				panelsView.redraw();
				boardVisualization.redraw();

			} else if (panelsView.rectf_play_prev.contains(x, y)) {

				GameData gamedata = activity.getBoardManager().getGameData();

				if (gamedata.isOnTheFirstMove()) {
					return;
				}

				IBoardManager boardManager = activity.getBoardManager();

				List<Move> moves = gamedata.getMoves();
				int moveIndex = gamedata.getCurrentMoveIndex();
				Move lastmove = null;
				if (moveIndex >= 0) {
					lastmove = moves.get(moveIndex);
					activity.getGameController().scheduleComputerPlayerAutoStart();
					boardManager.unmove(lastmove);
					gamedata.setCurrentMoveIndex(moveIndex - 1);
				}

				lastmove = null;

				if (gamedata.getCurrentMoveIndex() >= 0) {
					lastmove = moves.get(gamedata.getCurrentMoveIndex());
				}

				gamedata.getBoarddata().gameResultText = null;

				Application_Base.getInstance().storeGameData(gamedata);


				if (activity.getBoardManager().getMovingPiece() != null) activity.getBoardManager().clearMovingPiece();


				boardVisualization.clearSelections();
				if (lastmove != null) boardVisualization.markingSelection_Permanent_Border(lastmove.fromLetter, lastmove.fromDigit);
				if (lastmove != null) boardVisualization.markingSelection_Permanent_Border(lastmove.toLetter, lastmove.toDigit);

				boardVisualization.setData(boardManager.getBoard_WithoutHided());
				boardVisualization.setTopMessageText(null);
				panelsView.setCapturedPieces(activity.getBoardManager().getCaptured_W(), activity.getBoardManager().getCaptured_B(), activity.getBoardManager().getCapturedSize_W(), activity.getBoardManager().getCapturedSize_B());
				panelsView.redraw();
				boardVisualization.redraw();

			} else if (panelsView.rectf_play_next.contains(x, y)) {

				GameData gamedata = activity.getBoardManager().getGameData();

				if (gamedata.isOnTheLastMove()) {
					return;
				}

				IBoardManager boardManager = activity.getBoardManager();

				List<Move> moves = gamedata.getMoves();
				int moveIndex = gamedata.getCurrentMoveIndex() + 1;
				if (moveIndex < 0) {
					moveIndex = 0;
				}
				Move lastmove = null;
				if (moveIndex <= moves.size() - 1) {
					lastmove = moves.get(moveIndex);
					boardManager.move(lastmove);
					gamedata.setCurrentMoveIndex(moveIndex);
				}

				if (lastmove == null && moves.size() > 0) {
					lastmove = moves.get(moves.size() - 1);
				}

				Application_Base.getInstance().storeGameData(gamedata);


				if (activity.getBoardManager().getMovingPiece() != null) activity.getBoardManager().clearMovingPiece();


				boardVisualization.clearSelections();
				if (lastmove != null) boardVisualization.markingSelection_Permanent_Border(lastmove.fromLetter, lastmove.fromDigit);
				if (lastmove != null) boardVisualization.markingSelection_Permanent_Border(lastmove.toLetter, lastmove.toDigit);


				boardVisualization.setData(boardManager.getBoard_WithoutHided());
				panelsView.setCapturedPieces(activity.getBoardManager().getCaptured_W(), activity.getBoardManager().getCaptured_B(), activity.getBoardManager().getCapturedSize_W(), activity.getBoardManager().getCapturedSize_B());

				int gameStatus = activity.getBoardManager().getGameStatus();
				if (gameStatus != GlobalConstants.GAME_STATUS_NONE) {

					activity.updateViewWithGameResult(gameStatus);

				} else {

					//Do not resume game, because the player most probably will click next move button more than once.
					//activity.getGameController().resumeGame();
					//So cancel the auto run as well
					activity.getGameController().recreateAutoPlayCallBack();

				}

				panelsView.redraw();
				boardVisualization.redraw();

			} else if (panelsView.rectf_play_last.contains(x, y)) {

				GameData gamedata = activity.getBoardManager().getGameData();

				if (gamedata.isOnTheLastMove()) {
					return;
				}

				IBoardManager boardManager = activity.getBoardManager();

				List<Move> moves = gamedata.getMoves();
				int moveIndex = gamedata.getCurrentMoveIndex() + 1;
				if (moveIndex < 0) {
					moveIndex = 0;
				}
				Move lastmove = null;
				for (int i = moveIndex; i <= moves.size() - 1; i++) {
					lastmove = moves.get(i);
					boardManager.move(lastmove);
				}
				gamedata.setCurrentMoveIndex(moves.size() - 1);

				if (lastmove == null && moves.size() > 0) {
					lastmove = moves.get(moves.size() - 1);
				}

				Application_Base.getInstance().storeGameData(gamedata);


				if (activity.getBoardManager().getMovingPiece() != null) activity.getBoardManager().clearMovingPiece();


				boardVisualization.clearSelections();
				if (lastmove != null) boardVisualization.markingSelection_Permanent_Border(lastmove.fromLetter, lastmove.fromDigit);
				if (lastmove != null) boardVisualization.markingSelection_Permanent_Border(lastmove.toLetter, lastmove.toDigit);


				boardVisualization.setData(boardManager.getBoard_WithoutHided());
				panelsView.setCapturedPieces(activity.getBoardManager().getCaptured_W(), activity.getBoardManager().getCaptured_B(), activity.getBoardManager().getCapturedSize_W(), activity.getBoardManager().getCapturedSize_B());

				int gameStatus = activity.getBoardManager().getGameStatus();
				if (gameStatus != GlobalConstants.GAME_STATUS_NONE) {
					activity.updateViewWithGameResult(gameStatus);
				} else {
					activity.getGameController().resumeGame();
				}

				panelsView.redraw();
				boardVisualization.redraw();
			}
		}
		
		panelsView.redraw();
	}
}
