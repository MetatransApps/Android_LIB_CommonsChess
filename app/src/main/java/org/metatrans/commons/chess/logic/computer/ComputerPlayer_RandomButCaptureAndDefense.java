package org.metatrans.commons.chess.logic.computer;


import org.metatrans.commons.chess.logic.BoardManager_NativeBoard;

import com.chessartforkids.model.Move;

import bagaturchess.bitboard.api.IBitBoard;


public class ComputerPlayer_RandomButCaptureAndDefense extends ComputerPlayer_BaseImpl {
	
	
	private IBitBoard board;
	
	
	public ComputerPlayer_RandomButCaptureAndDefense(int colour, BoardManager_NativeBoard boardManager, int thinkTime) {
		super(colour, boardManager, thinkTime);
		
		board = boardManager.createBoard();
	}
	
	
	@Override
	public int getMoveScores(Move move) {
		
		int native_move = move.nativemove;
		
		//String move_str = MoveInt.moveToString(native_move);
		
		//System.out.println("move_str=" + move_str);
		
		//int native_colour = MoveInt.getColour(native_move);
		//int native_type = MoveInt.getFigureType(native_move);
		int native_fromfield = board.getMoveOps().getFromFieldID(native_move);
		//int native_tofield = MoveInt.getToFieldID(native_move);
		
		//System.out.println("native_colour=" + native_colour);
		//System.out.println("native_type=" + native_type);
		//System.out.println("native_fromfield=" + native_fromfield);
		//System.out.println("native_tofield=" + native_tofield);
		
		// >= 0 is good, otherwise is losing capture or move to attacked square by the enemy
		//int see_move = getBoard().getSee().seeMove(native_colour, native_type, native_tofield);
		int see_move = getBoard().getSEEScore(native_move);
		
		//= 0 is good, otherwise (<0) is hanging
		int see_field = getBoard().getSEEFieldScore(native_fromfield);
		
		return see_move + (-see_field / 2);
	}

	
	private IBitBoard getBoard() {
		return getBoardManager_impl().getBoard_Native();
	}

	
	private BoardManager_NativeBoard getBoardManager_impl() {
		return (BoardManager_NativeBoard)getBoardManager();
	}
}
