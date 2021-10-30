package com.chessartforkids.model;


import java.io.Serializable;

import org.metatrans.commons.chess.logic.BoardConstants;


public class Move implements Serializable, BoardConstants {
	
	
	private static final long serialVersionUID = -158488720204616051L;
	
	
	public Move() {
		promotedPieceID = ID_PIECE_NONE;
		capturedPieceID = ID_PIECE_NONE;
		isPromotion = false;
		isCapture = false;
		nativemove = 0;
	}
	
	
	public int fromLetter;
	public int fromDigit;
	public int toLetter;
	public int toDigit;
	public int nativemove;
	
	public boolean isPromotion;
	public boolean isCapture;
	
	public int pieceID;
	public int promotedPieceID;
	public int capturedPieceID;
	
	
	@Override
	public String toString() {
		String result = "";
		
		result += fromLetter + "," + fromDigit;
		result += " -> ";
		result += toLetter + "," + toDigit;
		
		return result;
	}
}
