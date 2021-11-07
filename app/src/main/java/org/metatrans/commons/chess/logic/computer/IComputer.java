package org.metatrans.commons.chess.logic.computer;


import org.metatrans.commons.chess.model.Move;


public interface IComputer {

	public int getColour();
	
	public Move think();
	public void stopCurrentJob();
	public void setCurrentJob(ComputerMoveResult currentJob);
	public boolean isThinking();
}
