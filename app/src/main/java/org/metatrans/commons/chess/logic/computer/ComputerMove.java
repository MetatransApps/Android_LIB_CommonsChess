package org.metatrans.commons.chess.logic.computer;


import org.metatrans.commons.chess.logic.IBoardManager;
import org.metatrans.commons.chess.main.MainActivity;
import org.metatrans.commons.chess.main.controllers.GameController;

import com.chessartforkids.model.Move;

import android.os.Handler;


public class ComputerMove implements Runnable {


	private MainActivity mainActivity;
	private IComputer computerPlayer;
	private IBoardManager boardManager;
	private GameController gameController;
	private Handler uiHandler;
	private ComputerMoveResult thinkJob;
	
	
	public ComputerMove(IComputer _computerPlayer, MainActivity _mainActivity, IBoardManager _boardManager, GameController _gameController, Handler _uiHandler) {
		mainActivity = _mainActivity;
		computerPlayer = _computerPlayer;
		boardManager = _boardManager;
		gameController = _gameController;
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
					mainActivity.keepScreenOn();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
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
								
								mainActivity.keepScreenOff();
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					
				} else if (thinkJob.isDone()) {
					
					computerPlayer.stopCurrentJob();
					
					mainActivity.getMainView().getBoardView().clearSelections();
					
					try {
						final Move computerMove = thinkJob.get();
						
						uiHandler.post(new Runnable() {
							@Override
							public void run() {
								
								try {
									
									gameController.updateUI(computerMove);								
		
									mainActivity.getMainView().getBoardView().unlock();
									
									mainActivity.keepScreenOff();
									
									mainActivity.getBoardManager().createMovingPiece(computerMove.fromLetter, computerMove.fromDigit, computerMove.fromLetter, computerMove.fromDigit, computerMove.pieceID);
									
									mainActivity.getMainView().getBoardView().startMoveAnimation(computerMove, mainActivity.getBoardManager().getMovingPiece());
									
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
						
					} catch (InterruptedException e) {
						e.printStackTrace();
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
