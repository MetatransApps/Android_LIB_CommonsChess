package org.metatrans.commons.chess.engines.search;


import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.chess.main.views.IBoardViewActivity;

import com.chessartforkids.model.GameData;
import com.chessartforkids.model.SearchInfo;

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
			
			
			if (Application_Base.getInstance().getCurrentActivity() instanceof IBoardViewActivity) {
				
				String eval;
				String moves;
				String depth;
				String nps;
				if (info.getDepth() > 0) {
					
					eval = String.format("%,.2f", (info.getEval() / (double) 100));
					if (info.isMateScore()) {
						if (info.getEval() > 0) {
							eval = "+Mate in " + info.getMateScore();	
						} else {
							eval = "-Mate in " + Math.abs(info.getMateScore());
						}
					} else {
						eval = ((info.getEval() > 0) ? "+" : "") + eval;
					}
					moves = BoardUtils.movesToString(cutPV(info.getPV()), board) + " ...";
					depth = "Depth " + info.getDepth() + "/" + info.getSelDepth();
					nps   = "NPS " + (int)(info.getSearchedNodes()/((System.currentTimeMillis()-startTime)/(double)1000));
					
				} else {
					
					eval  = "0";
					moves = "Book Move " + board.getMoveOps().moveToString(info.getBestMove());
					depth = "Depth 0/0";
					nps   = "NPS 0";
				}
				
				GameData gamedata = ((IBoardViewActivity)Application_Base.getInstance().getCurrentActivity()).getBoardManager().getGameData();
				//GameData gamedata = (GameData) Application_Base.getInstance().getGameData();
				gamedata.setSearchinfo(new SearchInfo(eval, moves, depth, nps));
			}
			
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
	
	
	private int[] cutPV(int[] input) {
		int[] result = new int[Math.min(3, input.length)];
		for (int i=0; i<result.length; i++) {
			result[i] = input[i];
		}
		return result;
	}

}
