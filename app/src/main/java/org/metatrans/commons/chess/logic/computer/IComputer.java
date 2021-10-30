package org.metatrans.commons.chess.logic.computer;


import com.chessartforkids.model.Move;


public interface IComputer {
	
	//public IPlayer getPlayer();
	public int getColour();
	
	public Move think();
	public void stopCurrentJob();
	public void setCurrentJob(ComputerMoveResult currentJob);
	public boolean isThinking();
}
