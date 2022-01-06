package org.metatrans.commons.chess.model;


import java.io.Serializable;


public class SearchInfo implements Serializable {
	
	
	private static final long serialVersionUID = -5008886093643467458L;
	
	
	public String infoEval;
	public String infoMoves;
	public String infoDepth;
	public String infoNPS;

	public long first_move_native;


	public SearchInfo(String infoEval, String infoMoves, String infoDepth, String infoNPS, long first_move_native) {

		super();

		this.infoEval = infoEval;
		this.infoMoves = infoMoves;
		this.infoDepth = infoDepth;
		this.infoNPS = infoNPS;
		this.first_move_native = first_move_native;
	}
}
