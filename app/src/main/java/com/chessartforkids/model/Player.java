package com.chessartforkids.model;


import org.metatrans.commons.chess.R;

import android.content.Context;


public class Player implements IPlayer {
	
	
	private static final long serialVersionUID = -2280718989075956258L;
	
	
	private int type;
	private int colour;
	
	
	public Player(int _type, int _colour) {
		type = _type;
		colour = _colour;
	}


	public int getType() {
		return type;
	}


	public int getColour() {
		return colour;
	}


	public String getName(Context ctx) {
		String name = "";
		if (type == PLAYER_TYPE_HUMAN) {
			name = ctx.getString(R.string.player_type_human);
		} else if (type == PLAYER_TYPE_COMPUTER) {
			name = ctx.getString(R.string.player_type_computer);
		} else {
			throw new IllegalStateException("type=" + type);
		}
		
		return name;
	}
}
