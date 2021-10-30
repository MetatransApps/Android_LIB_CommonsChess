package org.metatrans.commons.chess.main.controllers;


import com.chessartforkids.model.GameData;
import com.chessartforkids.model.IPlayer;
import com.chessartforkids.model.Move;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.metatrans.commons.chess.GlobalConstants;
import org.metatrans.commons.chess.R;
import org.metatrans.commons.chess.events.Events;
import org.metatrans.commons.chess.logic.BoardConstants;
import org.metatrans.commons.chess.logic.BoardUtils;
import org.metatrans.commons.chess.logic.IBoardManager;
import org.metatrans.commons.chess.logic.computer.ComputerMove;
import org.metatrans.commons.chess.logic.computer.IComputer;
import org.metatrans.commons.chess.main.MainActivity;
import org.metatrans.commons.chess.main.controllers.time.ITimeController;
import org.metatrans.commons.chess.main.views.MainView;


public class GameController implements GlobalConstants {
	
	
	private MainActivity mainActivity;
	private ITimeController timeController;
	
	private SetAutoOnUndo last_SetAutoOnUndo;
	
	private IComputer thinker;
	
	private ReadWriteLock lock = new ReentrantReadWriteLock();
	
	
	public GameController(MainActivity _mainActivity, IBoardManager _boardManager, MainView _mainView) {
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
	
	
	public void lock() {
		lock.writeLock().lock();
	}
	

	public void unlock() {
		lock.writeLock().unlock();
	}

	
	public boolean isThinking() {
		return thinker != null;
	}
	
	
	public Move unmove() {
		
		stopThinking();
		
		getTimeController().pause(mainActivity.getBoardManager().getColourToMove());
		
		Move move = mainActivity.getBoardManager().unmove();
		
		mainActivity.getBoardManager().clearMovingPiece();
				
		last_SetAutoOnUndo = new SetAutoOnUndo();
		mainActivity.executeJob(last_SetAutoOnUndo);
		
		return move;
	}
	
	
	public void stopGame() {
		pauseGame();
		getTimeController().destroy();
	}
	
	
	public void pauseGame() {
		stopThinking();
		getTimeController().pauseAll();
	}
	
	
	public void startNewGame() {
		startCurrentComputerPlayer();
		getTimeController().resume(mainActivity.getBoardManager().getColourToMove());
	}
	
	
	public void resumeGame() {
		
		System.out.println("GameController resumeGame");
		
		lock();
		
		getTimeController().resume(mainActivity.getBoardManager().getColourToMove());
		
		if (mainActivity.getBoardManager().getPlayerToMove().getType() == PLAYER_TYPE_COMPUTER) {
			startCurrentComputerPlayer();
		}
		
		unlock();
	}
	
	
	public void startCurrentComputerPlayer() {
		System.out.println("START THINKING: startCurrentComputerPlayer");
		
		IPlayer playerToMove = mainActivity.getBoardManager().getPlayerToMove();
		if (playerToMove.getType() == GlobalConstants.PLAYER_TYPE_COMPUTER) {
			startThinking(mainActivity.getBoardManager().getComputerToMove());
		}
	}
	
	
	public synchronized void stopThinking() {
		
		if (last_SetAutoOnUndo != null) {
			last_SetAutoOnUndo.stopped = true;
		}
		
		if (thinker != null) {
			thinker.stopCurrentJob();
			thinker = null;
		}
		
		/*GameData gamedata = mainActivity.getBoardManager().getGameData();
		
		if (gamedata.white.getType() == PLAYER_TYPE_COMPUTER) {
			IComputer autoPlayer = mainActivity.getBoardManager().getComputerWhite();
			autoPlayer.stopCurrentJob();
		}
		
		if (gamedata.black.getType() == PLAYER_TYPE_COMPUTER) {
			IComputer autoPlayer = mainActivity.getBoardManager().getComputerBlack();
			autoPlayer.stopCurrentJob();
		}*/
	}
	
	
	public void switchOnAutoPlayer(int playerColour) {
		
		//System.out.println("START THINKING: switchOnAutoPlayer");
		
		GameData gamedata = mainActivity.getBoardManager().getGameData();
		
		if (playerColour == BoardConstants.COLOUR_PIECE_WHITE) {
			mainActivity.switchPlayerWhite(gamedata);
		} else {
			mainActivity.switchPlayerBlack(gamedata);
		}
		
		int gameStatus = mainActivity.getBoardManager().getGameStatus();
		if (gameStatus == GlobalConstants.GAME_STATUS_NONE) {
			if (playerColour == mainActivity.getBoardManager().getColourToMove()) {
				IComputer autoPlayer = mainActivity.getBoardManager().getComputerToMove();
				startThinking(autoPlayer);
			}
		}
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
		} else {
			mainActivity.switchPlayerBlack(gamedata);
		}
	}
	
	public void pieceMoved(Move move) {
		lock();
		
		mainActivity.getBoardManager().move(move);
		
		moveMade(mainActivity.getBoardManager().getPlayer(BoardUtils.getColour(move.pieceID)),
				move,
				mainActivity.getBoardManager().getPlayer(BoardUtils.switchColour(BoardUtils.getColour(move.pieceID))),
				mainActivity.getBoardManager().getComputer(BoardUtils.switchColour(BoardUtils.getColour(move.pieceID))));
		unlock();
	}
	
	
	public void moveMade(IPlayer playerMoved, Move move, IPlayer playerToMove, IComputer computerToMove) {
		
		synchronized (mainActivity) {
			//System.out.println("START THINKING: moveMade");
			
			thinker = null;
			
			int gameStatus = mainActivity.getBoardManager().getGameStatus();
			
			mainActivity.getMainView().getBoardView().clearSelections();
			
			if (gameStatus != GlobalConstants.GAME_STATUS_NONE) {
				
				String gameStatusText = null;
				switch(gameStatus) {
					case GlobalConstants.GAME_STATUS_WHITE_WINS:
						gameStatusText = mainActivity.getString(R.string.game_status_white_wins);
						mainActivity.getMainView().getBoardView().whiteWinsSelection();
						break;
					case GlobalConstants.GAME_STATUS_BLACK_WINS:
						gameStatusText = mainActivity.getString(R.string.game_status_black_wins);
						mainActivity.getMainView().getBoardView().blackWinsSelection();
						break;
					case GlobalConstants.GAME_STATUS_DRAW:
						gameStatusText = mainActivity.getString(R.string.game_status_draw);
						break;
					default:
						throw new IllegalStateException("game states = " + gameStatus);
				}
				
				gameStatusText += " ";//"\r\n";
				gameStatusText += mainActivity.getString(R.string.game_status_inmoves1);
				gameStatusText += " " + ((mainActivity.getBoardManager().getGameData().getMoves().size() / 2) + 1);
				gameStatusText += " " + mainActivity.getString(R.string.game_status_inmoves2);
				
				mainActivity.getBoardManager().getGameData().getBoarddata().gameResultText = gameStatusText;
				
				pauseGame();
				
				//Update UI
				updateUI(move);
				
				//Handle Achievements
				Events.handleGameEvents_OnFinish(mainActivity, mainActivity.getBoardManager().getGameData(), mainActivity.getUserSettings(), mainActivity.getBoardManager().getGameStatus());
				
			} else {
				
				int colour = mainActivity.getBoardManager().getColourToMove();
	
				getTimeController().pause(playerMoved.getColour());
				getTimeController().resume(colour);
				
				updateUI(move);
				
				//System.out.println("PLAYER TYPE = " + playerToMove.getType());
				
				if (playerToMove.getType() == GlobalConstants.PLAYER_TYPE_HUMAN) {
					//Do nothing
				} else if (playerToMove.getType() == GlobalConstants.PLAYER_TYPE_COMPUTER) {
					
					startThinking(computerToMove);
					
				} else {
					throw new IllegalStateException();
				}
			}
		}
	}
	
	
	private synchronized void startThinking(IComputer computerPlayerToMove) {
		
		//mainActivity.waitInitialization();
		
		System.out.println("Start thinking of " + computerPlayerToMove.getColour() + " computerPlayerToMove=" + computerPlayerToMove);
		//Exception e = new Exception();
		//e.printStackTrace(System.out);
		
		if (thinker != null) {
			//throw new IllegalStateException("thinker is not null");
			thinker.stopCurrentJob();
		}
		
		thinker = computerPlayerToMove;
		
		//System.out.println("START THINKING");
		//(new Exception()).printStackTrace();
		
		//if (!computerPlayer.isThinking()) {
		//if (mainActivity.getBoardManager().getColourToMove() != BoardConstants.COLOUR_PIECE_ALL) {
			
			if (computerPlayerToMove.isThinking()) {
				throw new IllegalStateException("thinker is already thinking");
			}
			
			Runnable computerThinking = new ComputerMove(computerPlayerToMove, mainActivity, mainActivity.getBoardManager(), this, mainActivity.getUIHandler());
			
			mainActivity.executeJob(computerThinking);
		//}
		//}
	}
	
	
	public void updateUI(Move move) {
		
		mainActivity.getMainView().getBoardView().markingSelection_Permanent_Border(move.fromLetter, move.fromDigit);
		mainActivity.getMainView().getBoardView().markingSelection_Permanent_Border(move.toLetter, move.toDigit);
		mainActivity.getMainView().getBoardView().redraw();		
		mainActivity.getMainView().getPanelsView().redraw();
	}
	
	
	public boolean isBlackComputerThinking() {
		return thinker != null && thinker.getColour() == BoardConstants.COLOUR_PIECE_BLACK;
	}
	
	
	public boolean isWhiteComputerThinking() {
		return thinker != null && thinker.getColour() == BoardConstants.COLOUR_PIECE_WHITE;
		//return whiteComputerThinking > 0;
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
			
			/*boolean boardViewIsLocked = mainActivity.getMainView().getBoardView().isLocked();
			if (!boardViewIsLocked) {
				mainActivity.getMainView().getBoardView().lock();
			}
			System.out.println("SetAutoOnUndo: boardViewIsLocked=" + boardViewIsLocked);
			*/
			
			try {
								
				Thread.sleep(2000);
				
				if (!stopped) {
					
					resumeGame();

					/*if (!boardViewIsLocked) {
						mainActivity.getMainView().getBoardView().unlock();
					}
					 */
				}
			} catch (InterruptedException ie) {
				//Do nothing
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
	}
}
