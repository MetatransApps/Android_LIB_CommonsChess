package org.metatrans.commons.chess.logic.time;


import android.os.Handler;

import org.metatrans.commons.chess.logic.BoardConstants;
import org.metatrans.commons.chess.logic.board.IBoardManager;
import org.metatrans.commons.chess.main.MainActivity;
import org.metatrans.commons.chess.model.GameData;
import org.metatrans.commons.chess.views_and_controllers.IBoardViewActivity;
import org.metatrans.commons.chess.views_and_controllers.IMainView;
import org.metatrans.commons.chess.views_and_controllers.IPanelsVisualization;


public class TimeController_Increasing implements ITimeController, BoardConstants {
	
	
	private IBoardViewActivity mainActivity;
	private Heartbeat heartbeat;
	
	private boolean whiteClock;
	private boolean blackClock;
	
	
	public TimeController_Increasing(IBoardViewActivity _mainActivity) {
		
		mainActivity = _mainActivity;
		
		pauseAll();
		
		heartbeat = new Heartbeat();
		
		mainActivity.executeJob(heartbeat);
	}
	
	
	@Override
	public void resume(int colour) {
		if (colour == BoardConstants.COLOUR_PIECE_WHITE) {
			whiteClock = true;
		} else if (colour == BoardConstants.COLOUR_PIECE_BLACK) {
			blackClock = true;
		} else if (colour == BoardConstants.COLOUR_PIECE_ALL) {
			whiteClock = true;
			blackClock = true;
		} else {
			throw new IllegalStateException();
		}
	}
	
	
	@Override
	public void pause(int colour) {
		if (colour == BoardConstants.COLOUR_PIECE_WHITE) {
			whiteClock = false;
		} else if (colour == BoardConstants.COLOUR_PIECE_BLACK) {
			blackClock = false;
		} else if (colour == BoardConstants.COLOUR_PIECE_ALL) {
			whiteClock = false;
			blackClock = false;
		} else {
			throw new IllegalStateException();
		}
	}
	
	
	@Override
	public void pauseAll() {
		pause(COLOUR_PIECE_WHITE);
		pause(COLOUR_PIECE_BLACK);
		
		//mainActivity.getMainView().getPanlesView().switchOffPlayersClocks();
	}


	@Override
	public void destroy() {
		
		if (heartbeat != null) heartbeat.stopped = true;
		
		mainActivity = null;
		heartbeat = null;
	}
	
	
	@Override
	public long getData_white() {
		return heartbeat.accumulated_time_white;
	}

	
	@Override
	public long getData_black() {
		return heartbeat.accumulated_time_black;
	}
	

	@Override
	public void setData(long white, long black) {
		
		heartbeat.accumulated_time_white = white; 
		heartbeat.accumulated_time_black = black;
		
		heartbeat.init();
	}
	
	
	private class Heartbeat implements Runnable {
		
		boolean stopped = false;
		
		long accumulated_time_white_old = 0;
		long accumulated_time_black_old = 0;
		
		long accumulated_time_white = 0;
		long accumulated_time_black = 0;
		
		int check_interval = 50;
		int update_interval = 1000;

		
		Heartbeat() {
		}
		
		
		@Override
		public void run() {
			
			try {
				
				while (!stopped) {

					try {
						Thread.sleep(check_interval);

					} catch (InterruptedException e) {}


					if (whiteClock) {

						accumulated_time_white += check_interval;
						
						if (accumulated_time_white > accumulated_time_white_old + update_interval) {
							
							accumulated_time_white_old = accumulated_time_white;
							
							setWhiteTime(mainActivity);
						}
					}


					if (blackClock) {

						accumulated_time_black += check_interval;
						
						if (accumulated_time_black > accumulated_time_black_old + update_interval) {
							
							accumulated_time_black_old = accumulated_time_black;
							
							setBlackTime(mainActivity);
						}
					}
				}

			} catch(Exception e) {

				e.printStackTrace();
			}
		}
		
		
		private void init() {

			setWhiteTime(mainActivity);

			setBlackTime(mainActivity);
		}
		
		
		private void setBlackTime(IBoardViewActivity mainActivity) {
			
			if (mainActivity != null) {

				IBoardManager boardManager = mainActivity.getBoardManager();

				if (boardManager != null) {

					GameData gamedata = boardManager.getGameData();

					if (gamedata != null) {

						gamedata.setAccumulated_time_black(accumulated_time_black);
					}

					Handler handler = mainActivity.getUIHandler();

					if (handler != null) {

						handler.post(new Runnable() {

							@Override
							public void run() {

								try {

									IMainView mainView = mainActivity.getMainView();

									if (mainView != null) {

										IPanelsVisualization panelsView = mainView.getPanelsView();

										if (panelsView != null) {

											mainView.getPanelsView().redraw();
										}
									}

								} catch (Exception e) {

								}
							}
						});
					}
				}
			}
		}
		
		
		private void setWhiteTime(IBoardViewActivity mainActivity) {

			if (mainActivity != null) {

				IBoardManager boardManager = mainActivity.getBoardManager();

				if (boardManager != null) {

					GameData gamedata = boardManager.getGameData();

					if (gamedata != null) {

						gamedata.setAccumulated_time_white(accumulated_time_white);
					}

					Handler handler = mainActivity.getUIHandler();

					if (handler != null) {

						handler.post(new Runnable() {

							@Override
							public void run() {

								try {

									IMainView mainView = mainActivity.getMainView();

									if (mainView != null) {

										IPanelsVisualization panelsView = mainView.getPanelsView();

										if (panelsView != null) {

											mainView.getPanelsView().redraw();
										}
									}

								} catch (Exception e) {

								}
							}
						});
					}
				}
			}
		}
	}
}
