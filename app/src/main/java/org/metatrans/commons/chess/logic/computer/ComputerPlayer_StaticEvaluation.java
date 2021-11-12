package org.metatrans.commons.chess.logic.computer;


import org.metatrans.commons.chess.logic.board.BoardManager_NativeBoard;
import org.metatrans.commons.chess.model.Move;

import bagaturchess.bitboard.api.IBitBoard;
import bagaturchess.bitboard.impl.Constants;
import bagaturchess.search.api.IEvaluator;
import bagaturchess.search.impl.eval.cache.EvalCache_Impl2;


public class ComputerPlayer_StaticEvaluation extends ComputerPlayer_BaseImpl {
	
	
	private IBitBoard board_computer;
	private IEvaluator eval;
	
	
	public ComputerPlayer_StaticEvaluation(int colour, BoardManager_NativeBoard boardManager, int thinkTime) {
		super(colour, boardManager, thinkTime);
		
		board_computer = boardManager.createBoard();
		
		bagaturchess.learning.goldmiddle.impl1.eval.BagaturEvaluatorFactory ef = new bagaturchess.learning.goldmiddle.impl1.eval.BagaturEvaluatorFactory();

		eval = ef.create(board_computer, new EvalCache_Impl2(8 * 2));
	}
	
	
	@Override
	public int getMoveScores(Move move) {
		
		board_computer.revert();

		for (int i = 0; i <= getBoardManager().getGameData().getCurrentMoveIndex(); i++) {
			board_computer.makeMoveForward(getBoardManager().getGameData().getMoves().get(i).nativemove);
		}
		
		int native_move = move.nativemove;
		
		board_computer.makeMoveForward(native_move);
		if (board_computer.isInCheck()) {
			if (!board_computer.hasMoveInCheck()) {
				board_computer.makeMoveBackward(native_move);
				return 10000;
			}
		} else {
			if (!board_computer.hasMoveInNonCheck()) {
				board_computer.makeMoveBackward(native_move);
				return 0;
			}
		}
		board_computer.makeMoveBackward(native_move);
		
		//String move_str = MoveInt.moveToString(native_move);
		
		//System.out.println("move_str=" + move_str);
		
		//int native_colour = MoveInt.getColour(native_move);
		//int native_type = MoveInt.getFigureType(native_move);
		int native_fromfield = board_computer.getMoveOps().getFromFieldID(native_move);
		//int native_tofield = MoveInt.getToFieldID(native_move);
		
		//System.out.println("native_colour=" + native_colour);
		//System.out.println("native_type=" + native_type);
		//System.out.println("native_fromfield=" + native_fromfield);
		//System.out.println("native_tofield=" + native_tofield);
		
		// >= 0 is good, otherwise is losing capture or move to attacked square by the enemy
		//int see_move = getBoard().getSee().seeMove(native_colour, native_type, native_tofield);
		int see_move = board_computer.getSEEScore(native_move);
		
		//= 0 is good, otherwise (<0) is hanging
		int see_field = board_computer.getSEEFieldScore(native_fromfield);
		
		int score = 0;
		
		if (!move.isCapture) {
			
			double eval_scores_before = eval.fullEval(0, IEvaluator.MIN_EVAL, IEvaluator.MAX_EVAL, board_computer.getColourToMove());
			board_computer.makeMoveForward(native_move);
			double eval_scores_after = -eval.fullEval(0, IEvaluator.MIN_EVAL, IEvaluator.MAX_EVAL, board_computer.getColourToMove());
			board_computer.makeMoveBackward(native_move);
			
			score += (eval_scores_after - eval_scores_before);
		}
		
		if (see_field != 0) {
			int type = board_computer.getMoveOps().getFigureType(native_move);
			if (type == Constants.TYPE_KING) {
				see_field /= 10;
			}
		}
		
		score += see_move + (-see_field / 2);
		
		return score;
	}
}
