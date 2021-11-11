package org.metatrans.commons.chess.model;


import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.chess.R;


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


	public String getName() {

		String name = "";

		if (type == PLAYER_TYPE_HUMAN) {

			name = Application_Base.getInstance().getString(R.string.player_type_human);

		} else if (type == PLAYER_TYPE_COMPUTER) {

			name = Application_Base.getInstance().getString(R.string.player_type_computer);

		} else {

			throw new IllegalStateException("type=" + type);
		}
		
		return name;
	}
}
