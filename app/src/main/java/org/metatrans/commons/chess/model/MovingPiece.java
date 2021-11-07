package org.metatrans.commons.chess.model;


import java.io.Serializable;
import java.util.Collections;
import java.util.List;



public class MovingPiece implements Serializable {
	
	
	private static final long serialVersionUID = -6668839937985265726L;

	public volatile boolean dragging;
	public int initial_letter;
	public int initial_digit;
	public int pieceID;
	public int capturedPID;
	public int promotion_letter = -1;
	public int promotion_digit = -1;
	public float x;
	public float y;
	public FieldSelection over_invalid_square_selection;

	public List<Move> moves = Collections.emptyList();
}
