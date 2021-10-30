package org.metatrans.commons.chess.logic.computer;


import org.metatrans.commons.chess.logic.BoardUtils;
import org.metatrans.commons.chess.logic.IBoardManager;

import com.chessartforkids.model.Move;


public class ComputerPlayer_RandomButCapture extends ComputerPlayer_BaseImpl {
	

	public ComputerPlayer_RandomButCapture(int colour, IBoardManager boardManager, int thinkTime) {
		super(colour, boardManager, thinkTime);
	}
	
	
	@Override
	public int getMoveScores(Move move) {
		
		int score = 0;
		
		if (move.isCapture) {
			int type = BoardUtils.getType(move.capturedPieceID);
			score += getPieceScore(type);
		}
		
		if (move.isPromotion) {
			int type = BoardUtils.getType(move.promotedPieceID);
			score -= getPieceScore(TYPE_PAWN);
			score += getPieceScore(type);
		}
		
		return score;
	}

	
	private static final int getPieceScore(int type) {
		switch (type) {
			case TYPE_KING:
				return 3000;
			case TYPE_QUEEN:
				return 900;
			case TYPE_ROOK:
				return 500;
			case TYPE_BISHOP:
				return 300;
			case TYPE_KNIGHT:
				return 300;
			case TYPE_PAWN:
				return 100;
			default:
				throw new IllegalStateException("type=" + type);
		}
	}
}
