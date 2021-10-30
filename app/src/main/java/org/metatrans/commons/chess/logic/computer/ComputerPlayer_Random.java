package org.metatrans.commons.chess.logic.computer;


import org.metatrans.commons.chess.logic.IBoardManager;

import com.chessartforkids.model.Move;


public class ComputerPlayer_Random extends ComputerPlayer_BaseImpl {
	
	
	public ComputerPlayer_Random(int colour, IBoardManager boardManager, int thinkTime) {
		super(colour, boardManager, thinkTime);
	}

	
	@Override
	public int getMoveScores(Move move) {
		return 0;
	}
}
