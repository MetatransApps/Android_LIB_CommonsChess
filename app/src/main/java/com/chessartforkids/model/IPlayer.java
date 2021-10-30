package com.chessartforkids.model;


import android.content.Context;

import java.io.Serializable;

import org.metatrans.commons.chess.GlobalConstants;


public interface IPlayer extends Serializable, GlobalConstants {
	public int getType();
	public int getColour();
	public String getName(Context ctx);
}
