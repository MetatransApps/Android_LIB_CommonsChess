package org.metatrans.commons.chess.logic.computer;


import org.metatrans.commons.chess.main.MainActivity;
import org.metatrans.commons.chess.model.Move;
import org.metatrans.commons.chess.model.SearchInfo;

import android.os.Handler;


public class ComputerMove implements Runnable {


	private MainActivity mainActivity;
	private IComputer computerPlayer;
	private Handler uiHandler;
	private ComputerMoveResult thinkJob;
	
	
	public ComputerMove(IComputer _computerPlayer, MainActivity _mainActivity, Handler _uiHandler) {
		mainActivity = _mainActivity;
		computerPlayer = _computerPlayer;
		uiHandler = _uiHandler;
		
		init();
	}
	
	
	@Override
	public void run() {
		mainActivity.executeJob(new CheckerJob(thinkJob));
	}


	private void init() {
		
		mainActivity.getMainView().getBoardView().lock();
		
		uiHandler.post(new Runnable() {
			@Override
			public void run() {
				try {
					mainActivity.getMainView().getPanelsView().redraw();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		thinkJob = new ComputerMoveResult(computerPlayer);
		
		computerPlayer.setCurrentJob(thinkJob);
	}
	
	
	private final class CheckerJob implements Runnable {
		
		
		private final ComputerMoveResult thinkJob;
		boolean started = false;
		
		
		private CheckerJob(ComputerMoveResult thinkJob) {
			this.thinkJob = thinkJob;
		}

		@Override
		public void run() {
			
			if (!started) {

				mainActivity.executeJob(thinkJob);

				started = true;
			}
			
			try {
				
				if (thinkJob.isCancelled()) {
					
					computerPlayer.stopCurrentJob();
					
					uiHandler.post(new Runnable() {

						@Override
						public void run() {

							try {

								mainActivity.getMainView().getPanelsView().redraw();

								mainActivity.getMainView().getBoardView().unlock();
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					
				} else if (thinkJob.isDone()) {
					
					computerPlayer.stopCurrentJob();

					try {

						final Move computerMove = thinkJob.get();

						uiHandler.post( new Runnable() {

							@Override
							public void run() {
								
								try {

									SearchInfo last_search_info = null;

									if (computerPlayer instanceof ComputerPlayer_Engine) {

										last_search_info = ((ComputerPlayer_Engine) computerPlayer).getLastSearchInfo();
									}

									mainActivity.getGameController().acceptNewMove(computerMove, last_search_info);

								} catch (Exception e) {

									e.printStackTrace();
								}
							}
						});

					} catch (Exception e) {

						e.printStackTrace();
					}

				} else {
					
					try {
						Thread.sleep(17);

					} catch (InterruptedException e) {}
					
					mainActivity.executeJob(this);
				}
			
			
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	}
}
