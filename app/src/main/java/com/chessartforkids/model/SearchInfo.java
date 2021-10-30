package com.chessartforkids.model;


import java.io.Serializable;


public class SearchInfo implements Serializable {
	
	
	private static final long serialVersionUID = -5008886093643467458L;
	
	
	public String infoEval;
	public String infoMoves;
	public String infoDepth;
	public String infoNPS;
	
	public SearchInfo(String infoEval, String infoMoves, String infoDepth, String infoNPS) {
		super();
		this.infoEval = infoEval;
		this.infoMoves = infoMoves;
		this.infoDepth = infoDepth;
		this.infoNPS = infoNPS;
	}
}
