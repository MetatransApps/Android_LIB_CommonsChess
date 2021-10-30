package org.metatrans.commons.chess.cfg.animation;


import org.metatrans.commons.chess.R;


public class Config_Animation_SuperSlow extends Config_Animation_Base {
	
	
	@Override
	public int getID() {
		return ID_SUPER_SLOW;
	}
	
	
	@Override
	public int getName() {
		return R.string.menu_animation_super_slow;
	}
	
	
	@Override
	public int getIconResID() {
		return R.drawable.ic_animation_slow_white;
	}
}
