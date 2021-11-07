package org.metatrans.commons.chess.engines.search;


import bagaturchess.search.api.internal.ISearchInfo;
import bagaturchess.search.api.internal.ISearchMediator;
import bagaturchess.search.api.internal.ISearchStopper;
import bagaturchess.uci.api.BestMoveSender;


public class RunAPIMediator implements ISearchMediator {
	
	
	private long startTime;
	private IRunAPIStatus status;
	private ISearchStopper stopper;
	private volatile ISearchInfo lastinfo;
	private RunAPIBestMoveSender sender;


	public RunAPIMediator(IRunAPIStatus _status, int millis) {
		status = _status;
		startTime = System.currentTimeMillis();
		stopper = new RunAPISearchStopper(startTime + millis);
		sender = new RunAPIBestMoveSender();
	}
	
	
	@Override
	public ISearchStopper getStopper() {
		return stopper;
	}
	
	
	@Override
	public void changedMajor(ISearchInfo info) {
		
		if (!info.isUpperBound()) {	
			lastinfo = info;
		}
		
		status.sendInfoLine(info);
	}


	@Override
	public void dump(String msg) {
		System.out.println(msg);
	}


	@Override
	public void dump(Throwable t) {
		t.printStackTrace();
	}


	@Override
	public BestMoveSender getBestMoveSender() {
		return sender;
	}


	@Override
	public void changedMinor(ISearchInfo info) {
		//Do nothing
	}


	@Override
	public ISearchInfo getLastInfo() {
		return lastinfo;
	}


	@Override
	public void registerInfoObject(ISearchInfo info) {
		//Do nothing
	}


	@Override
	public void startIteration(int iteration) {
		//Do nothing
	}


	@Override
	public void send(String msg) {
		throw new UnsupportedOperationException();
	}


	@Override
	public int getTrustWindow_AlphaAspiration() {
		throw new IllegalStateException();
	}


	@Override
	public int getTrustWindow_BestMove() {
		throw new IllegalStateException();
	}


	@Override
	public int getTrustWindow_MTD_Step() {
		throw new IllegalStateException();
	}


	@Override
	public void setStopper(ISearchStopper _stopper) {
		stopper = _stopper;
	}
}