package org.metatrans.commons.chess.model;


import java.io.Serializable;

import org.metatrans.commons.chess.GlobalConstants;


public interface IPlayer extends Serializable, GlobalConstants {
	public int getType();
	public int getColour();
	public String getName();
}
