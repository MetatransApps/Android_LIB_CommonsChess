package org.metatrans.commons.chess.logic.time;


import org.metatrans.commons.chess.logic.BoardConstants;
import org.metatrans.commons.chess.main.MainActivity;


public class TimeController_Increasing implements ITimeController, BoardConstants {
	
	
	private MainActivity mainActivity;
	private Heartbeat heartbeat;
	
	private boolean whiteClock;
	private boolean blackClock;
	
	
	public TimeController_Increasing(MainActivity _mainActivity) {
		
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
							
							setWhiteTime();	
						}
					}
					
					if (blackClock) {
						accumulated_time_black += check_interval;
						
						if (accumulated_time_black > accumulated_time_black_old + update_interval) {
							
							accumulated_time_black_old = accumulated_time_black;
							
							setBlackTime();
						}
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		
		private void init() {
			setWhiteTime();
			setBlackTime();
		}
		
		
		private void setBlackTime() {
			
			if (mainActivity != null) {
				
				mainActivity.getBoardManager().getGameData().setAccumulated_time_black(accumulated_time_black);
				
				if (mainActivity.getBoardManager() != null) {
					
					mainActivity.getUIHandler().post(new Runnable() {
						@Override
						public void run() {
							try {
								mainActivity.getMainView().getPanelsView().redraw();
							} catch(Exception e) {
							
							}
						}
					});
				}
			}
		}
		
		
		private void setWhiteTime() {
			
			if (mainActivity != null) {
				
				if (mainActivity.getBoardManager() != null && mainActivity.getBoardManager().getGameData() != null) {
					mainActivity.getBoardManager().getGameData().setAccumulated_time_white(accumulated_time_white);
				}
				
				if (mainActivity.getUIHandler() != null) mainActivity.getUIHandler().post(new Runnable() {
					@Override
					public void run() {
						try {
							mainActivity.getMainView().getPanelsView().redraw();
						} catch(Exception e) {
							
						}
					}
				});
			}
		}
	}
}
