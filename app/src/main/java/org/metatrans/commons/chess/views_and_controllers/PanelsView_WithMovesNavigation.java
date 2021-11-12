package org.metatrans.commons.chess.views_and_controllers;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.cfg.colours.IConfigurationColours;
import org.metatrans.commons.chess.GlobalConstants;
import org.metatrans.commons.chess.R;
import org.metatrans.commons.chess.logic.board.IBoardManager;
import org.metatrans.commons.chess.model.GameData;
import org.metatrans.commons.chess.model.Move;
import org.metatrans.commons.ui.ButtonAreaSwitch_Image;
import org.metatrans.commons.ui.utils.BitmapUtils;
import org.metatrans.commons.ui.utils.DrawingUtils;

import java.util.List;


public class PanelsView_WithMovesNavigation extends PanelsView {


	public RectF rectangle_bottom1;

	public RectF rectf_play_first;
	public RectF rectf_play_prev;
	public RectF rectf_empty;
	public RectF rectf_play_next;
	public RectF rectf_play_last;


	public ButtonAreaSwitch_Image play_first;
	public ButtonAreaSwitch_Image play_prev;
	public ButtonAreaSwitch_Image play_next;
	public ButtonAreaSwitch_Image play_last;


	public PanelsView_WithMovesNavigation(Context context, View _parent, RectF _rectangleTop, RectF _rectangleBottom, RectF _rectangleBottom1, RectF _rectangleBottom2) {
		
		super(context, _parent, _rectangleTop, _rectangleBottom, _rectangleBottom1, _rectangleBottom2);

		rectangle_bottom1 = _rectangleBottom1;

		rectf_play_first = new RectF();
		rectf_play_prev = new RectF();
		rectf_empty = new RectF();
		rectf_play_next = new RectF();
		rectf_play_last = new RectF();
	}
	
	
	public void init(String playerName_white, String playerName_black, boolean autotop, boolean autobottom, boolean info_enabled) {


		IConfigurationColours coloursCfg = getActivity().getUIConfiguration().getColoursConfiguration();

		int delta = DELTA_AREAS;

		float play_and_goto_buttons_border = 30;
		float play_and_goto_buttons_size_y = 8 * (rectangle_bottom1.bottom - rectangle_bottom1.top) / 10;
		float play_and_goto_buttons_size_x = 1.3f * play_and_goto_buttons_size_y;

		rectf_play_first.top = rectangle_bottom1.top + 1 * (rectangle_bottom1.bottom - rectangle_bottom1.top) / 10;
		rectf_play_first.left = rectangle_bottom1.left + delta;
		rectf_play_first.right = rectf_play_first.left + play_and_goto_buttons_size_x;
		rectf_play_first.bottom = rectf_play_first.top + play_and_goto_buttons_size_y;

		rectf_play_prev.top = rectangle_bottom1.top + 1 * (rectangle_bottom1.bottom - rectangle_bottom1.top) / 10;
		rectf_play_prev.left = rectf_play_first.right + play_and_goto_buttons_border;
		rectf_play_prev.right = rectf_play_prev.left + play_and_goto_buttons_size_x;
		rectf_play_prev.bottom = rectf_play_prev.top + play_and_goto_buttons_size_y;

		rectf_play_last.top = rectangle_bottom1.top + 1 * (rectangle_bottom1.bottom - rectangle_bottom1.top) / 10;
		rectf_play_last.bottom = rectf_play_last.top + play_and_goto_buttons_size_y;
		rectf_play_last.right = rectangle_bottom1.right - delta;
		rectf_play_last.left = rectf_play_last.right - play_and_goto_buttons_size_x;

		rectf_play_next.top = rectangle_bottom1.top + 1 * (rectangle_bottom1.bottom - rectangle_bottom1.top) / 10;
		rectf_play_next.bottom = rectf_play_next.top + play_and_goto_buttons_size_y;
		rectf_play_next.left = rectf_play_last.left - play_and_goto_buttons_size_x - play_and_goto_buttons_border;
		rectf_play_next.right = rectf_play_next.left + play_and_goto_buttons_size_x;

		rectangle_area_info.top = rectangle_bottom1.top + 1 * (rectangle_bottom1.bottom - rectangle_bottom1.top) / 10;
		rectangle_area_info.left = rectf_play_prev.right + play_and_goto_buttons_border;
		rectangle_area_info.right = rectf_play_next.left - play_and_goto_buttons_border;
		rectangle_area_info.bottom = rectangle_area_info.top + play_and_goto_buttons_size_y;


		GameData gamedata = getActivity().getBoardManager().getGameData();

		play_first = new ButtonAreaSwitch_Image(rectf_play_first,
				BitmapUtils.fromResource((Context) getActivity(),R.drawable.ic_action_playback_first_white),
				coloursCfg.getColour_Square_ValidSelection(),
				coloursCfg.getColour_Delimiter(),
				coloursCfg.getColour_Square_MarkingSelection(),
				!gamedata.isOnTheFirstMove(),
				false
		);

		play_prev = new ButtonAreaSwitch_Image(rectf_play_prev,
				BitmapUtils.fromResource((Context) getActivity(), R.drawable.ic_action_playback_prev_white),
				coloursCfg.getColour_Square_ValidSelection(),
				coloursCfg.getColour_Delimiter(),
				coloursCfg.getColour_Square_MarkingSelection(),
				!gamedata.isOnTheFirstMove(),
				false
		);

		play_next = new ButtonAreaSwitch_Image(rectf_play_next,
				BitmapUtils.fromResource((Context) getActivity(), R.drawable.ic_action_playback_next_white),
				coloursCfg.getColour_Square_ValidSelection(),
				coloursCfg.getColour_Delimiter(),
				coloursCfg.getColour_Square_MarkingSelection(),
				!gamedata.isOnTheLastMove(),
				false
		);

		play_last = new ButtonAreaSwitch_Image(rectf_play_last,
				BitmapUtils.fromResource((Context) getActivity(), R.drawable.ic_action_playback_last_white),
				coloursCfg.getColour_Square_ValidSelection(),
				coloursCfg.getColour_Delimiter(),
				coloursCfg.getColour_Square_MarkingSelection(),
				!gamedata.isOnTheLastMove(),
				false
		);


		super.init(playerName_white, playerName_black, autotop, autobottom, info_enabled);
	}


	@Override
	protected void initInfoRectangle(float play_and_goto_buttons_border, float play_and_goto_buttons_size_y) {

		rectangle_area_info.top = rectangle_bottom1.top + 1 * (rectangle_bottom1.bottom - rectangle_bottom1.top) / 10;
		rectangle_area_info.left = rectf_play_prev.right + play_and_goto_buttons_border;
		rectangle_area_info.right = rectf_play_next.left - play_and_goto_buttons_border;
		rectangle_area_info.bottom = rectangle_area_info.top + play_and_goto_buttons_size_y;
	}


	@Override
	protected void onDraw(Canvas canvas) {

		default_paint.setColor(colour_panel);
		DrawingUtils.drawRoundRectangle(canvas, default_paint, rectangle_bottom1);

		super.onDraw(canvas);

		disableAndEnableMoveNavigationButtons();

		//Move navigation buttons
		play_first.draw(canvas);
		play_prev.draw(canvas);
		play_next.draw(canvas);
		play_last.draw(canvas);
	}


	public void disableAndEnableMoveNavigationButtons() {

		if (areMovesNavigationButtonsLocked()) {

			play_first.deactivate();

			play_prev.deactivate();

			play_next.deactivate();

			play_last.deactivate();

		} else {

			GameData gamedata = getActivity().getBoardManager().getGameData();

			if (gamedata.isOnTheFirstMove()) {

				play_first.deactivate();

				play_prev.deactivate();

			} else {

				play_first.activate();

				play_prev.activate();

			}

			if (gamedata.isOnTheLastMove()) {

				play_next.deactivate();

				play_last.deactivate();

			} else {

				play_next.activate();

				play_last.activate();
			}
		}
	}


	@Override
	public boolean areMovesNavigationButtonsLocked() {

		if (getActivity().getMainView().getBoardView().hasAnimation() != -1) {

			return true;
		}

		if (getActivity().getGameController().isThinking()) {

			return true;
		}

		return false;
	}


	@Override
	public OnTouchListener createOnTouchListener(IBoardVisualization boardVisualization, IBoardViewActivity activity) {

		return new OnTouchListener_Panels(boardVisualization, activity);
	}


	public class OnTouchListener_Panels extends PanelsView.OnTouchListener_Panels {


		private IBoardVisualization boardVisualization;

		private IBoardViewActivity activity;


		public OnTouchListener_Panels(IBoardVisualization _boardVisualization, IBoardViewActivity _activity) {

			super(_boardVisualization, _activity);

			boardVisualization = _boardVisualization;

			activity = _activity;
		}


		@Override
		public boolean onTouch(View view, MotionEvent event) {


			float x = event.getX();
			float y = event.getY();


			if (rectangle_bottom1.contains(x, y)) {

				synchronized (activity) {

					int action = event.getAction();

					if (action == MotionEvent.ACTION_DOWN) {

						return processEvent_DOWN(event) ? true : super.onTouch(view, event);

					} else if (action == MotionEvent.ACTION_MOVE) {

						return processEvent_MOVE(event) ? true : super.onTouch(view, event);

					} else if (action == MotionEvent.ACTION_UP
							|| action == MotionEvent.ACTION_CANCEL) {

						return processEvent_UP(event) ? true : super.onTouch(view, event);

					}
				}

				view.invalidate();

				return true;

			} else {

				return super.onTouch(view, event);
			}
		}


		private boolean processEvent_DOWN(MotionEvent event) {


			float x = event.getX();
			float y = event.getY();


			if (rectf_play_first.contains(x, y)) {

				play_first.select();

				return true;

			} else if (rectf_play_prev.contains(x, y)) {

				play_prev.select();

				return true;

			} else if (rectf_play_next.contains(x, y)) {

				play_next.select();

				return true;

			} else if (rectf_play_last.contains(x, y)) {

				play_last.select();

				return true;
			}

			return false;
		}


		private boolean processEvent_MOVE(MotionEvent event) {

			float x = event.getX();
			float y = event.getY();


			play_first.deselect();

			play_prev.deselect();

			play_next.deselect();

			play_last.deselect();


			if (rectf_play_first.contains(x, y)) {

				play_first.select();

				return true;

			} else if (rectf_play_prev.contains(x, y)) {

				play_prev.select();

				return true;

			} else if (rectf_play_next.contains(x, y)) {

				play_next.select();

				return true;

			} else if (rectf_play_last.contains(x, y)) {

				play_last.select();

				return true;

			}

			return false;
		}


		private boolean processEvent_UP(MotionEvent event) {


			float x = event.getX();
			float y = event.getY();


			play_first.deselect();

			play_prev.deselect();

			play_next.deselect();

			play_last.deselect();


			if (areMovesNavigationButtonsLocked()) {

				return false;
			}

			if (rectf_play_first.contains(x, y)) {

				GameData gamedata = activity.getBoardManager().getGameData();

				if (gamedata.isOnTheFirstMove()) {

					return false;
				}

				IBoardManager boardManager = activity.getBoardManager();

				List<Move> moves = gamedata.getMoves();
				int moveIndex = gamedata.getCurrentMoveIndex();
				Move lastmove = null;
				for (int i = moveIndex; i >= 0; i--) {
					lastmove = moves.get(i);
					boardManager.unmove(lastmove);
				}
				gamedata.setCurrentMoveIndex(-1);

				Application_Base.getInstance().storeGameData(gamedata);

				if (activity.getBoardManager().getMovingPiece() != null)
					activity.getBoardManager().clearMovingPiece();


				boardVisualization.clearSelections();
				if (lastmove != null)
					boardVisualization.markingSelection_Permanent_Border(lastmove.fromLetter, lastmove.fromDigit);
				if (lastmove != null)
					boardVisualization.markingSelection_Permanent_Border(lastmove.toLetter, lastmove.toDigit);

				boardVisualization.setData(boardManager.getBoard_WithoutHided());
				boardVisualization.setTopMessageText(null);
				setCapturedPieces(activity.getBoardManager().getCaptured_W(), activity.getBoardManager().getCaptured_B(), activity.getBoardManager().getCapturedSize_W(), activity.getBoardManager().getCapturedSize_B());
				redraw();
				boardVisualization.redraw();

				activity.getGameController().scheduleComputerPlayerAutoStart();

				return true;

			} else if (rectf_play_prev.contains(x, y)) {

				GameData gamedata = activity.getBoardManager().getGameData();

				if (gamedata.isOnTheFirstMove()) {
					return false;
				}

				IBoardManager boardManager = activity.getBoardManager();

				List<Move> moves = gamedata.getMoves();
				int moveIndex = gamedata.getCurrentMoveIndex();
				Move lastmove = null;
				if (moveIndex >= 0) {
					lastmove = moves.get(moveIndex);
					boardManager.unmove(lastmove);
					gamedata.setCurrentMoveIndex(moveIndex - 1);
				}

				lastmove = null;

				if (gamedata.getCurrentMoveIndex() >= 0) {
					lastmove = moves.get(gamedata.getCurrentMoveIndex());
				}

				gamedata.getBoarddata().gameResultText = null;

				Application_Base.getInstance().storeGameData(gamedata);


				if (activity.getBoardManager().getMovingPiece() != null)
					activity.getBoardManager().clearMovingPiece();


				boardVisualization.clearSelections();
				if (lastmove != null)
					boardVisualization.markingSelection_Permanent_Border(lastmove.fromLetter, lastmove.fromDigit);
				if (lastmove != null)
					boardVisualization.markingSelection_Permanent_Border(lastmove.toLetter, lastmove.toDigit);

				boardVisualization.setData(boardManager.getBoard_WithoutHided());
				boardVisualization.setTopMessageText(null);
				setCapturedPieces(activity.getBoardManager().getCaptured_W(), activity.getBoardManager().getCaptured_B(), activity.getBoardManager().getCapturedSize_W(), activity.getBoardManager().getCapturedSize_B());
				redraw();
				boardVisualization.redraw();

				activity.getGameController().scheduleComputerPlayerAutoStart();

				return true;

			} else if (rectf_play_next.contains(x, y)) {

				GameData gamedata = activity.getBoardManager().getGameData();

				if (gamedata.isOnTheLastMove()) {
					return false;
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


				if (activity.getBoardManager().getMovingPiece() != null)
					activity.getBoardManager().clearMovingPiece();


				boardVisualization.clearSelections();
				if (lastmove != null)
					boardVisualization.markingSelection_Permanent_Border(lastmove.fromLetter, lastmove.fromDigit);
				if (lastmove != null)
					boardVisualization.markingSelection_Permanent_Border(lastmove.toLetter, lastmove.toDigit);


				boardVisualization.setData(boardManager.getBoard_WithoutHided());
				setCapturedPieces(activity.getBoardManager().getCaptured_W(), activity.getBoardManager().getCaptured_B(), activity.getBoardManager().getCapturedSize_W(), activity.getBoardManager().getCapturedSize_B());

				int gameStatus = activity.getBoardManager().getGameStatus();
				if (gameStatus != GlobalConstants.GAME_STATUS_NONE) {

					activity.updateViewWithGameResult(gameStatus);

				} else {

					//Do not resume game, because the player most probably will click next move button more than once.
					//activity.getGameController().resumeGame();
					//So cancel the auto run as well
					activity.getGameController().recreateAutoPlayCallBack();

				}

				redraw();
				boardVisualization.redraw();

				return true;

			} else if (rectf_play_last.contains(x, y)) {

				GameData gamedata = activity.getBoardManager().getGameData();

				if (gamedata.isOnTheLastMove()) {
					return false;
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


				if (activity.getBoardManager().getMovingPiece() != null)
					activity.getBoardManager().clearMovingPiece();


				boardVisualization.clearSelections();
				if (lastmove != null)
					boardVisualization.markingSelection_Permanent_Border(lastmove.fromLetter, lastmove.fromDigit);
				if (lastmove != null)
					boardVisualization.markingSelection_Permanent_Border(lastmove.toLetter, lastmove.toDigit);


				boardVisualization.setData(boardManager.getBoard_WithoutHided());
				setCapturedPieces(activity.getBoardManager().getCaptured_W(), activity.getBoardManager().getCaptured_B(), activity.getBoardManager().getCapturedSize_W(), activity.getBoardManager().getCapturedSize_B());

				int gameStatus = activity.getBoardManager().getGameStatus();
				if (gameStatus != GlobalConstants.GAME_STATUS_NONE) {
					activity.updateViewWithGameResult(gameStatus);
				} else {
					activity.getGameController().resumeGame();
				}

				redraw();
				boardVisualization.redraw();

				return true;
			}


			return false;
		}
	}
}
