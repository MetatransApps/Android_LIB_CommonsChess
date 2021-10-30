package com.chessartforkids.model;


import java.io.Serializable;



public class BoardData implements Serializable {
	
	
	private static final long serialVersionUID = 5924297685343370427L;
	
	
	public int[][] board;
	public int[] captured_w;
	public int[] captured_b;
	public int size_captured_w = -1;
	public int size_captured_b;
	
	public int colourToMove;
	public boolean w_kingSideAvailable;
	public boolean w_queenSideAvailable;
	public boolean b_kingSideAvailable;
	public boolean b_queenSideAvailable;
	public String enpassantSquare;
	public int halfMoves;
	public int fullMoves;
	
	public String gameResultText;
	
	
	public BoardData() {
		
	}
	
	
	@Override
	public String toString() {
		String result = "";
		result += "colourToMove=" + colourToMove;
		result += ", w_kingSideAvailable=" + w_kingSideAvailable;
		result += ", w_queenSideAvailable=" + w_queenSideAvailable;
		result += ", b_kingSideAvailable=" + b_kingSideAvailable;
		result += ", b_queenSideAvailable=" + b_queenSideAvailable;
		result += ", enpassantSquare=" + enpassantSquare;
		result += ", halfMoves=" + halfMoves;
		result += ", fullMoves=" + fullMoves;
		return result;
	}
}
