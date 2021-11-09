package org.metatrans.commons.chess.logic.game;


import android.app.AlertDialog;
import android.content.DialogInterface;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.metatrans.commons.chess.Alerts;
import org.metatrans.commons.chess.GlobalConstants;
import org.metatrans.commons.chess.logic.BoardConstants;
import org.metatrans.commons.chess.logic.computer.ComputerMove;
import org.metatrans.commons.chess.logic.computer.IComputer;
import org.metatrans.commons.chess.main.MainActivity;
import org.metatrans.commons.chess.logic.time.ITimeController;
import org.metatrans.commons.chess.model.GameData;
import org.metatrans.commons.chess.model.IPlayer;
import org.metatrans.commons.chess.model.Move;
import org.metatrans.commons.chess.model.SearchInfo;


public class GameManager implements GlobalConstants {


	private MainActivity mainActivity;

	private ITimeController timeController;

	private SetAutoOnUndo last_SetAutoOnUndo;

	private IComputer thinker;

	private ReadWriteLock lock = new ReentrantReadWriteLock();


	public GameManager(MainActivity _mainActivity) {
		mainActivity = _mainActivity;
	}


	ITimeController getTimeController() {

		if (timeController == null) {
			mainActivity.createTimeController();//Will set timeController field as well
		}

		return timeController;
	}


	public void setTimeController(ITimeController _timeController) {
		timeController = _timeController;
	}


	private void lock() {
		lock.writeLock().lock();
	}


	private void unlock() {
		lock.writeLock().unlock();
	}


	public boolean isThinking() {
		return thinker != null;
	}


	public void scheduleComputerPlayerAutoStart() {

		stopThinking();

		getTimeController().pause(mainActivity.getBoardManager().getColourToMove());

		mainActivity.getBoard().unlock();

		recreateAutoPlayCallBack();
	}


	public void recreateAutoPlayCallBack() {

		cancelCurrentAutoPlayCallBack();

		createNewAutoPlayCallBack();
	}


	private void cancelCurrentAutoPlayCallBack() {
		if (last_SetAutoOnUndo != null) {

			last_SetAutoOnUndo.stopped = true;

			last_SetAutoOnUndo = null;
		}
	}


	private void createNewAutoPlayCallBack() {
		//TODO: Must be only once at a time !!!
		//Should work with this null check, but actually doesn't work ...
		//if (last_SetAutoOnUndo == null) {
		last_SetAutoOnUndo = new SetAutoOnUndo();

		mainActivity.executeJob(last_SetAutoOnUndo);
		//}
	}


	public void pauseGame() {

		stopThinking();

		getTimeController().pauseAll();
	}


	public void resumeGame() {

		System.out.println("GameManager resumeGame");

		lock();

		int gameStatus = mainActivity.getBoardManager().getGameStatus();

		if (gameStatus != GlobalConstants.GAME_STATUS_NONE) {

			mainActivity.updateViewWithGameResult(gameStatus);

		} else {

			getTimeController().resume(mainActivity.getBoardManager().getColourToMove());

			if (mainActivity.getBoardManager().getPlayerToMove().getType() == PLAYER_TYPE_COMPUTER) {

				startThinking(mainActivity.getBoardManager().getComputerToMove());

			} else {

				//Enable human player to manually select piece and make move via UI.
				//The UI of the board should be unlocked for this purpose.
				mainActivity.getBoard().unlock();

			}
		}

		unlock();
	}


	public synchronized void stopThinking() {


		cancelCurrentAutoPlayCallBack();

		if (thinker != null) {
			thinker.stopCurrentJob();
			thinker = null;
		}
	}


	public void switchOnAutoPlayer(int playerColour) {

		lock();

		System.out.println("GameManager.switchOnAutoPlayer: playerColour=" + playerColour);

		GameData gamedata = mainActivity.getBoardManager().getGameData();

		if (playerColour == BoardConstants.COLOUR_PIECE_WHITE) {

			mainActivity.switchPlayerWhite(gamedata);

			mainActivity.getUserSettings().auto_player_enabled_white = true;
			mainActivity.getUserSettings().playerTypeWhite = GlobalConstants.PLAYER_TYPE_COMPUTER;
			mainActivity.getUserSettings().save();

			mainActivity.getPanels().setWhitePlayerName(mainActivity.getBoardManager().getGameData().getWhite().getName(mainActivity));

		} else {

			mainActivity.switchPlayerBlack(gamedata);

			mainActivity.getUserSettings().auto_player_enabled_black = true;
			mainActivity.getUserSettings().playerTypeBlack = GlobalConstants.PLAYER_TYPE_COMPUTER;
			mainActivity.getUserSettings().save();

			mainActivity.getPanels().setBlackPlayerName(mainActivity.getBoardManager().getGameData().getBlack().getName(mainActivity));

		}

		int gameStatus = mainActivity.getBoardManager().getGameStatus();

		if (gameStatus == GlobalConstants.GAME_STATUS_NONE) {

			if (playerColour == mainActivity.getBoardManager().getColourToMove()) {

				if (mainActivity.getMainView().getBoardView().hasAnimation() == -1) {

					resumeGame();
				}
			}
		}

		unlock();
	}


	public void switchOffAutoPlayer(int playerColour) {


		GameData gamedata = mainActivity.getBoardManager().getGameData();


		IPlayer player = (playerColour == BoardConstants.COLOUR_PIECE_WHITE) ? gamedata.getWhite() : gamedata.getBlack();

		if (player.getType() == PLAYER_TYPE_COMPUTER) {

			IComputer autoPlayer = (playerColour == BoardConstants.COLOUR_PIECE_WHITE) ? mainActivity.getBoardManager().getComputerWhite() : mainActivity.getBoardManager().getComputerBlack();

			if (thinker == autoPlayer) {

				stopThinking();
			}
		}


		if (player.getColour() == BoardConstants.COLOUR_PIECE_WHITE) {

			mainActivity.switchPlayerWhite(gamedata);

			mainActivity.getUserSettings().auto_player_enabled_white = false;
			mainActivity.getUserSettings().playerTypeWhite = GlobalConstants.PLAYER_TYPE_HUMAN;
			mainActivity.getUserSettings().save();

			mainActivity.getPanels().setWhitePlayerName(mainActivity.getBoardManager().getGameData().getWhite().getName(mainActivity));

		} else {

			mainActivity.switchPlayerBlack(gamedata);

			mainActivity.getUserSettings().auto_player_enabled_black = false;
			mainActivity.getUserSettings().playerTypeBlack = GlobalConstants.PLAYER_TYPE_HUMAN;
			mainActivity.getUserSettings().save();

			mainActivity.getPanels().setBlackPlayerName(mainActivity.getBoardManager().getGameData().getBlack().getName(mainActivity));
		}
	}


	public void acceptNewMove(final Move move, final SearchInfo last_search_info) {

		pauseGame();

		GameData gamedata = mainActivity.getBoardManager().getGameData();

		int current_move_index = gamedata.getCurrentMoveIndex();

		//If current move index is not at the end of the list we may need to remove moves and ask for user acceptance for this change.
		if (current_move_index < gamedata.getMoves().size() - 1) {

			//If the new move is the same as the old one - just move the old one.
			if (move.nativemove == gamedata.getMoves().get(current_move_index + 1).nativemove) {

				lock();

				//Do not add move here, because we use the existing move from the GameData object
				Move existing_move = gamedata.getMoves().get(current_move_index + 1);

				moveAccepted(gamedata, existing_move, current_move_index);

				unlock();

			} else {

				//Ask for user acceptance for move sequence change.

				//ALERT FOR GAME CHANGE
				AlertDialog.Builder adb = Alerts.createAlertDialog_OverrideMoveSequence(mainActivity,

						new DialogInterface.OnClickListener() {

							//User accepted move sequence change
							@Override
							public void onClick(DialogInterface dialog, int which) {

								lock();

								//TODO: send event for game change:
								//Events.handleGameEvents_OnExit(mainActivity, (GameData) Application_Base.getInstance().getGameData(),

								//If the user clicked YES than continue by removing moves from the game data:
								while (current_move_index < gamedata.getMoves().size() - 1) {
									int remove_index = gamedata.getMoves().size() - 1;
									gamedata.getMoves().remove(remove_index);
									gamedata.getSearchInfos().remove(remove_index);
								}

								gamedata.getMoves().add(move);
								gamedata.getSearchInfos().add((SearchInfo) last_search_info);

								moveAccepted(gamedata, move, current_move_index);

								unlock();
							}
						},


						new DialogInterface.OnClickListener() {

							//User rejected move sequence change
							@Override
							public void onClick(DialogInterface dialog, int which) {

								lock();

								moveRejected();

								unlock();
							}
						},

						new DialogInterface.OnCancelListener() {

							//User canceled the dialog
							@Override
							public void onCancel(DialogInterface dialog) {

								lock();

								moveRejected();

								unlock();
							}
						}
				);

				adb.show();
			}

		} else if (current_move_index == gamedata.getMoves().size() - 1) {

			lock();

			gamedata.getMoves().add(move);
			gamedata.getSearchInfos().add(last_search_info);

			moveAccepted(gamedata, move, current_move_index);

			unlock();

		} else {

			throw new IllegalStateException("current_move_index=" + current_move_index);
		}
	}


	private void moveAccepted(GameData gamedata, Move move, int current_move_index) {

		gamedata.setCurrentMoveIndex(current_move_index + 1);
		gamedata.save();

		updateUI_AfterAcceptedMove(move);

		mainActivity.getMainView().getBoardView().startMoveAnimation(move);
	}


	private void moveRejected() {

		updateUI_AfterRejectedMove();

		int gameStatus = mainActivity.getBoardManager().getGameStatus();

		if (gameStatus == GlobalConstants.GAME_STATUS_NONE) {

			if (mainActivity.getBoardManager().getPlayerToMove().getType() == GlobalConstants.PLAYER_TYPE_COMPUTER) {

				scheduleComputerPlayerAutoStart();

			} else {

				resumeGame();
			}

		} else {

			mainActivity.updateViewWithGameResult(gameStatus);
		}
	}


	public void updateUI_AfterAcceptedMove(Move move) {

		mainActivity.getMainView().getBoardView().clearSelections();

		mainActivity.getMainView().getBoardView().markingSelection_Permanent_Border(move.fromLetter, move.fromDigit);
		mainActivity.getMainView().getBoardView().markingSelection_Permanent_Border(move.toLetter, move.toDigit);

		updateUI_AfterRejectedMove();

	}

	private void updateUI_AfterRejectedMove() {

		mainActivity.getMainView().getPanelsView().setCapturedPieces(mainActivity.getBoardManager().getCaptured_W(),
				mainActivity.getBoardManager().getCaptured_B(),
				mainActivity.getBoardManager().getCapturedSize_W(),
				mainActivity.getBoardManager().getCapturedSize_B());

		mainActivity.getMainView().getBoardView().setData(mainActivity.getBoardManager().getBoard_WithoutHided());

		mainActivity.getMainView().invalidate();

		mainActivity.getMainView().getBoardView().redraw();
		mainActivity.getMainView().getPanelsView().redraw();
	}


	private synchronized void startThinking(IComputer computerPlayerToMove) {

		System.out.println("Start thinking of " + computerPlayerToMove.getColour() + " computerPlayerToMove=" + computerPlayerToMove);

		if (thinker != null) {

			thinker.stopCurrentJob();
		}

		thinker = computerPlayerToMove;

		if (computerPlayerToMove.isThinking()) {

			throw new IllegalStateException("thinker is already thinking");
		}

		Runnable computerThinking = new ComputerMove(computerPlayerToMove, mainActivity, mainActivity.getUIHandler());

		mainActivity.executeJob(computerThinking);
	}


	public boolean isBlackComputerThinking() {

		return thinker != null && thinker.getColour() == BoardConstants.COLOUR_PIECE_BLACK;
	}


	public boolean isWhiteComputerThinking() {

		return thinker != null && thinker.getColour() == BoardConstants.COLOUR_PIECE_WHITE;
	}


	public boolean isWhitePlayerClockOn() {

		boolean result = mainActivity.getBoardManager().getColourToMove() == BoardConstants.COLOUR_PIECE_WHITE

				&& mainActivity.getBoardManager().getGameStatus() == GlobalConstants.GAME_STATUS_NONE;

		return result;
	}


	public boolean isBlackPlayerClockOn() {

		boolean result = mainActivity.getBoardManager().getColourToMove() == BoardConstants.COLOUR_PIECE_BLACK

				&& mainActivity.getBoardManager().getGameStatus() == GlobalConstants.GAME_STATUS_NONE;

		return result;
	}


	private class SetAutoOnUndo implements Runnable {

		boolean stopped;

		@Override
		public void run() {

			try {

				Thread.sleep(2000);

				if (!stopped) {

					last_SetAutoOnUndo = null;

					int gameStatus = mainActivity.getBoardManager().getGameStatus();

					if (gameStatus == GlobalConstants.GAME_STATUS_NONE) {

						resumeGame();
					}
				}

			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	}
}

