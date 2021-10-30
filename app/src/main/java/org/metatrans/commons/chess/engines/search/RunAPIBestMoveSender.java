package org.metatrans.commons.chess.engines.search;


import bagaturchess.uci.api.BestMoveSender;


public class RunAPIBestMoveSender implements BestMoveSender {

	
	private boolean interrupted = false;

	
	public boolean isInterrupted() {
		return interrupted;
	}
	
	@Override
	public void sendBestMove() {
		interrupted = true;
		System.out.println("RunAPIBestMoveSender.sendBestMove: INTERRUPTED");
	}
}
