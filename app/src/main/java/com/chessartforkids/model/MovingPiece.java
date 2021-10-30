package com.chessartforkids.model;


import java.io.Serializable;
import java.util.Collections;
import java.util.List;



public class MovingPiece implements Serializable {
	
	
	private static final long serialVersionUID = -6668839937985265726L;
	
	
	public int initial_letter;
	public int initial_digit;
	public float x;
	public float y;
	public int pieceID;
	volatile public boolean dragging;
	public int promotion_letter = -1;
	public int promotion_digit = -1;
	public int capturedPID;
	public List<Move> moves = Collections.emptyList();
}
