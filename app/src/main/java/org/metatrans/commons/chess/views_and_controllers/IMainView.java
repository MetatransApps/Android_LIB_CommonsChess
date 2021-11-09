package org.metatrans.commons.chess.views_and_controllers;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.MotionEvent;
import android.view.View;

import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.chess.Alerts;
import org.metatrans.commons.chess.GlobalConstants;
import org.metatrans.commons.chess.events.Events;
import org.metatrans.commons.chess.logic.BoardConstants;
import org.metatrans.commons.chess.model.GameData;


import bagaturchess.bitboard.impl.Constants;


public interface IMainView {

	IBoardVisualization getBoardView();

	IPanelsVisualization getPanelsView();

	void requestLayout();

	void invalidate();


	public class OnTouchListener_Main implements View.OnTouchListener, BoardConstants {


		private IBoardViewActivity activity;

		private View.OnTouchListener boardListener;

		private View.OnTouchListener panelsListener;


		public OnTouchListener_Main(IBoardViewActivity _activity, IBoardVisualization boardVisualization, IPanelsVisualization panelsVisualization) {

			activity = _activity;

			boardListener = boardVisualization.createOnTouchListener(boardVisualization, activity);
			boardVisualization.setOnTouchListener(boardListener);

			panelsListener = panelsVisualization.createOnTouchListener(boardVisualization, activity);
			panelsVisualization.setOnTouchListener(panelsListener);
		}


		@Override
		public boolean onTouch(View view, MotionEvent event) {

			//System.out.println("MAIN: onTouch");

			if (activity == null
					|| activity.getBoardManager() == null
					|| activity.getGameController() == null
					|| activity.getMainView() == null
					|| activity.getMainView().getBoardView() == null
					|| activity.getMainView().getPanelsView() == null
			) {
				return true;
			}

			synchronized (activity) {

				if (event != null) {

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
			}

			return true;
		}


		private void processEvent_DOWN(MotionEvent event) {

			float x = event.getX();
			float y = event.getY();

			if (activity.getMainView().getBoardView().isOverBoard(x, y)) {

				if (activity.getBoardManager().getGameStatus() != GlobalConstants.GAME_STATUS_NONE) {

					if (activity.getMainView().getBoardView().isOverBottomReplay(x, y)) {
						activity.getMainView().getBoardView().selectButtonReplay();
						return;
					} else if (activity.getMainView().getBoardView().isOverLeaderBoards(x, y)) {
						if (activity.getMainView().getBoardView().getLeaderboard() != null) {
							activity.getMainView().getBoardView().getLeaderboard().onTouch((View) activity.getMainView(), event);
							return;
						}
					}

				} else {

					boardListener.onTouch((View) activity.getMainView().getBoardView(), event);

				}
			}

			panelsListener.onTouch((View) activity.getMainView().getPanelsView(), event);

		}


		private void processEvent_MOVE(MotionEvent event) {

			float x = event.getX();
			float y = event.getY();

			if (activity.getBoardManager().getGameStatus() != GlobalConstants.GAME_STATUS_NONE) {

				if (activity.getMainView().getBoardView().isOverBottomReplay(x, y)) {
					activity.getMainView().getBoardView().selectButtonReplay();
					return;

				} else {

					activity.getMainView().getBoardView().deselectButtonReplay();

					if (activity.getMainView().getBoardView().isOverLeaderBoards(x, y)) {

						if (activity.getMainView().getBoardView().getLeaderboard() != null) {
							activity.getMainView().getBoardView().getLeaderboard().onTouch((View) activity.getMainView(), event);
							return;
						}
					}
				}

			} else {
				activity.getMainView().getBoardView().deselectButtonReplay();
			}

			boardListener.onTouch((View) activity.getMainView().getBoardView(), event);

			panelsListener.onTouch((View) activity.getMainView().getPanelsView(), event);
		}


		private void processEvent_UP(MotionEvent event) {

			float x = event.getX();
			float y = event.getY();

			activity.getMainView().getBoardView().deselectButtonReplay();

			if (activity.getBoardManager().getGameStatus() != GlobalConstants.GAME_STATUS_NONE) {

				if (activity.getMainView().getBoardView().isOverBottomReplay(x, y)) {

					AlertDialog.Builder adb = Alerts.createAlertDialog_LoseGame((Context) activity,

							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog, int which) {

									Events.handleGameEvents_OnExit((Activity) activity, (GameData) Application_Base.getInstance().getGameData(), activity.getUserSettings());

									activity.createNewGame(Constants.INITIAL_BOARD);

								}
							});

					adb.show();

					return;

				} else if (activity.getMainView().getBoardView().isOverLeaderBoards(x, y)) {
					if (activity.getMainView().getBoardView().getLeaderboard() != null) {
						activity.getMainView().getBoardView().getLeaderboard().onTouch((View) activity.getMainView(), event);
						return;
					}
				}

			}

			boardListener.onTouch((View) activity.getMainView().getBoardView(), event);

			panelsListener.onTouch((View) activity.getMainView().getPanelsView(), event);
		}
	}
}
