package org.metatrans.commons.chess.cfg.rules;


import org.metatrans.commons.chess.R;


public class Config_Rules_All implements IConfigurationRule {
	
	
	@Override
	public int getID() {
		return IConfigurationRule.BOARD_MANAGER_ID_ALL_RULES;
	}
	
	
	@Override
	public int getName() {
		return R.string.menu_rules_allrules;
	}
	
	
	@Override
	public int getIconResID() {
		return R.drawable.ic_rules_v2_o3;
	}
	
	
	@Override
	public int getDescription() {
		return R.string.menu_rules_allrules_desc;
	}
}
