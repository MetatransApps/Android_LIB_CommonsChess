package org.metatrans.commons.chess.engines.search;


import bagaturchess.bitboard.api.IBitBoard;
import bagaturchess.search.api.internal.ISearchInfo;


public interface IEngineClient {
	
	public void startThinking(IBitBoard boardForSetup) throws Exception;
	
	public void stopThinking() throws Exception;
	
	public ISearchInfo stopThinkingWithResult() throws Exception;
	
	public boolean hasAtLeastOneMove() throws Exception;
	
	public boolean isDone() throws Exception;
	
	public String getInfoLine() throws Exception;
	
}
