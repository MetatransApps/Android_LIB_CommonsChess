package org.metatrans.commons.chess.logic.computer;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.chess.R;
import org.metatrans.commons.chess.logic.BoardConstants;
import org.metatrans.commons.chess.logic.board.BoardManager_NativeBoard;
import org.metatrans.commons.chess.logic.board.BoardUtils;
import org.metatrans.commons.chess.logic.board.IBoardManager;
import org.metatrans.commons.chess.model.Move;

import bagaturchess.opening.api.OpeningBook;
import bagaturchess.opening.api.OpeningBookFactory;
import bagaturchess.search.api.internal.ISearchInfo;
import bagaturchess.search.api.internal.ISearchMediator;
import bagaturchess.search.api.internal.ISearchStopper;
import bagaturchess.search.impl.uci_adaptor.TimeSaver;
import bagaturchess.uci.api.BestMoveSender;


public abstract class ComputerPlayer_BaseImpl implements IComputer , BoardConstants {
	
	
	private int colour;
	private IBoardManager boardManager;
	private volatile ComputerMoveResult currentJob;
	private int thinkTime;
	private TimeSaver saver;
	
	
	public ComputerPlayer_BaseImpl(int _colour, IBoardManager _boardManager, int _thinkTime) {
		
		colour = _colour;
		boardManager = _boardManager;
		thinkTime = _thinkTime;
		
		try {
			if (OpeningBookFactory.getBook() == null) {
				InputStream is_w_ob = Application_Base.getInstance().getResources().openRawResource(R.raw.w30);
				InputStream is_b_ob = Application_Base.getInstance().getResources().openRawResource(R.raw.b30);
				OpeningBookFactory.initBook(is_w_ob, is_b_ob);				
			}
		} catch(Throwable t) {
			System.out.println("Unable to load Openning Book. Error while openning file streams: " + t.getMessage());
		}
		
		if (OpeningBookFactory.getBook() != null) {
			saver = new TimeSaver(null, OpeningBookFactory.getBook());
		}
	}
	
	
	@Override
	public int getColour() {
		return colour;
	}
	
	
	@Override
	public synchronized void stopCurrentJob() {
		if (currentJob != null) {
			//System.out.println("Cancel job: " + this);
			currentJob.cancel(true);
			currentJob = null;
		}
	}
	
	
	@Override
	public synchronized void setCurrentJob(ComputerMoveResult _currentJob) {
		
		if (currentJob != null) {

			throw new IllegalStateException();
		}
		
		currentJob = _currentJob;
	}


	@Override
	public synchronized boolean isThinking() {
		return currentJob != null;
	}
	
	
	protected boolean checkStopCondition() {
		return currentJob == null || currentJob.isCancelled();
	}
	
	
	protected IBoardManager getBoardManager() {
		return boardManager;
	}
	
	
	@Override
	public Move think() {
		
		if (!isThinking()) {
			//TODO: after the call should be not null but the check is risky
			//throw new IllegalStateException();
		}
		
		if (checkStopCondition()) {
			return null;
		}
		
		
		ISearchInfo bookinfo = null;
		if (saver != null) {
			if (boardManager instanceof BoardManager_NativeBoard) {
				if (this instanceof ComputerPlayer_StaticEvaluation /*|| this instanceof ComputerPlayer_StaticEvaluation_PiecesAware*/) {
					ISearchMediator mediator = new OpenningBookCaptureMediator();
					boolean moveSent = saver.beforeMove(((BoardManager_NativeBoard) boardManager).getBoard_Native(), OpeningBook.OPENING_BOOK_MODE_POWER1, mediator, true, false, 0);
					if (moveSent) {
						bookinfo = mediator.getLastInfo();
						if (bookinfo == null) {
							throw new IllegalStateException("info from OpenningBookCaptureMediator is null");
						}
					}	
				}
			}
		}
		
		
		List<Move> allmoves = getOrderedMovesList();
		Move result = null;
		
		if (bookinfo != null) {
			for (Move moveObj: allmoves) {
				if (moveObj.nativemove == bookinfo.getBestMove()) {
					result = moveObj;
					break;
				}
			}	
		} else {
			result = allmoves.get(0);
		}
		
		//TODO: notify for best line / info
		
		try {
			Thread.sleep(thinkTime);
		} catch (InterruptedException e) {}
		
		if (checkStopCondition()) {
			return null;
		}
		
		if (result != null) {
			return result;			
		}
		
		throw new IllegalStateException("No move");
	}
	
	
	protected List<Move> getOrderedMovesList() {
		
		List<Pair> pairs = new ArrayList<Pair>();
		
		int[][] board = getBoardManager().getBoard_Full();
		for (int letter = 0; letter < 8; letter++) {
			for (int digit = 0; digit < 8; digit++) {
				int pieceID = board[letter][digit];
				if (pieceID != ID_PIECE_NONE
						//&& BoardUtils.getColour(pieceID) == boardManager.getColourToMove()) {
						&& BoardUtils.getColour(pieceID) == colour) {
					
					List<Move> moves = getBoardManager().selectToFields(letter, digit);
					
					for (Move move: moves) {
						
						Pair pair = new Pair();
						pair.scores = getMoveScores(move);
						pair.move = move;
						
						pairs.add(pair);
					}
				}
			}
		}
		
		Collections.shuffle(pairs);
		
		Collections.sort(pairs);
		
		List<Move> allmoves = new ArrayList<Move>();
		for (Pair pair: pairs) {
			allmoves.add(pair.move);
		}
		
		return allmoves;
	}
	
	
	public abstract int getMoveScores(Move move);
	

	private class Pair implements Comparable<Pair> {
		
		int scores;
		Move move;
		//String bestline;
		
		
		@Override
		public int compareTo(Pair another) {
			
			if (another.equals(this)) {
				return 0;
			}
			
			if (another.scores == scores) {
				//return 1;
			}
			
			return another.scores - scores;
		}
		
		
		@Override
		public int hashCode() {
			return super.hashCode();
		}
		
		
		@Override
		public boolean equals(Object other) {
			return this == ((Pair)other);
		}
	}


	public int getThinkTime() {
		return thinkTime;
	}
	
	
	private class OpenningBookCaptureMediator implements ISearchMediator {
		
		private ISearchInfo last;
		
		@Override
		public void startIteration(int arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void setStopper(ISearchStopper arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void send(String arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void registerInfoObject(ISearchInfo arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public int getTrustWindow_MTD_Step() {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public int getTrustWindow_BestMove() {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public int getTrustWindow_AlphaAspiration() {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public ISearchStopper getStopper() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public ISearchInfo getLastInfo() {
			return last;
		}
		
		@Override
		public BestMoveSender getBestMoveSender() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public void dump(Throwable arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void dump(String arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void changedMinor(ISearchInfo arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void changedMajor(ISearchInfo arg0) {
			last = arg0;
		}
	}
}
