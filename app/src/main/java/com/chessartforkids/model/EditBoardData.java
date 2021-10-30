package com.chessartforkids.model;


import java.io.Serializable;

import org.metatrans.commons.chess.logic.BoardConstants;


public class EditBoardData implements Serializable {

	
	private static final long serialVersionUID = -2353389684794991915L;
	
	
	public String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
	
	public boolean castling_K = true;
	public boolean castling_Q = true;
	public boolean castling_k = true;
	public boolean castling_q = true;
	
	public boolean move_W = true;
	public boolean move_B = false;
	
	public int selectedPID = BoardConstants.ID_PIECE_NONE;
	
	
	public EditBoardData() {
		
	}
}
