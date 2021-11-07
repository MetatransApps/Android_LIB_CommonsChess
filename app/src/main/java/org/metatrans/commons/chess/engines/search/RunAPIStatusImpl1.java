package org.metatrans.commons.chess.engines.search;


import bagaturchess.bitboard.api.BoardUtils;
import bagaturchess.bitboard.api.IBitBoard;
import bagaturchess.search.api.internal.ISearchInfo;


public class RunAPIStatusImpl1 implements IRunAPIStatus {
	
	
	private IBitBoard board;

	private long startTime;
	
	
	public RunAPIStatusImpl1(IBitBoard _board) {
		board = _board;
		startTime = System.currentTimeMillis();
	}
	
	
	@Override
	public void sendInfoLine(ISearchInfo info) {
		
		if (!info.isUpperBound()) {
			
			String bestLineString = 
		  			"D: " + info.getDepth() +
		  			" SD: " + info.getSelDepth() +
		  			" Time: " + ((System.currentTimeMillis()-startTime)/(double)1000) + " s" +
		  			//" Mate: " + info.isMateScore() +
		  			" Eval: " + (info.isMateScore() ? (info.getMateScore() + "M") : info.getEval() ) +
		  			" NPS: " + (int)(info.getSearchedNodes()/((System.currentTimeMillis()-startTime)/(double)1000)) +
		  			//" Thread: " + Thread.currentThread().getName() +
		  			" PV: " + BoardUtils.movesToString(info.getPV(), board);
			
			System.out.println(bestLineString);
			
		} else {
			
			String bestLineString = 
		  			"D: " + info.getDepth() +
		  			" SD: " + info.getSelDepth() +
		  			" Time: " + ((System.currentTimeMillis()-startTime)/(double)1000) + " s" +
		  			//" Mate: " + info.isMateScore() +
		  			" Eval: " + (info.isMateScore() ? (info.getMateScore() + "M") : info.getEval() ) +
		  			" NPS: " + (int)(info.getSearchedNodes()/((System.currentTimeMillis()-startTime)/(double)1000)) +
		  			//" Thread: " + Thread.currentThread().getName() +
		  			" UPPERBOUND";
			
			System.out.println(bestLineString);
		}
		
		System.out.println("MEMORY: " + (Runtime.getRuntime().freeMemory() / (1024 * 1024)) + " MB");
	}
}
