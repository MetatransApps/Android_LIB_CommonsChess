package org.metatrans.commons.chess.cfg.animation;


import org.metatrans.commons.chess.R;


public class Config_Animation_Normal extends Config_Animation_Base {
	
	
	@Override
	public int getID() {
		return ID_NORMAL;
	}
	
	
	@Override
	public int getName() {
		return R.string.menu_animation_normal;
	}
	
	
	@Override
	public int getIconResID() {
		return R.drawable.ic_animation_normal_white;
	}
}
