package org.metatrans.commons.chess.cfg.animation;


import org.metatrans.commons.chess.R;


public class Config_Animation_None extends Config_Animation_Base {
	
	
	@Override
	public int getID() {
		return ID_NONE;
	}
	
	
	@Override
	public int getName() {
		return R.string.menu_animation_none;
	}
	
	
	@Override
	public int getIconResID() {
		return R.drawable.ic_animation_none_white;
	}
}
