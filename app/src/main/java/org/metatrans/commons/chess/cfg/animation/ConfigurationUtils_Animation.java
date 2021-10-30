package org.metatrans.commons.chess.cfg.animation;


import org.metatrans.commons.cfg.ConfigurationUtils_Base;
import org.metatrans.commons.cfg.IConfigurationEntry;


public class ConfigurationUtils_Animation extends ConfigurationUtils_Base {	
	
	
	private static final String TAG_NAME = ConfigurationUtils_Animation.class.getName();
	
	
	public static ConfigurationUtils_Animation getInstance() {
		return (ConfigurationUtils_Animation) getInstance(TAG_NAME);
	}
	
	
	public static void createInstance() {
		
		IConfigurationEntry[] cfgs_entries = new IConfigurationEntry[] {
				new Config_Animation_None(),
				new Config_Animation_SuperSlow(),
				new Config_Animation_Slow(),
				new Config_Animation_Normal(),
				new Config_Animation_Fast(),
				new Config_Animation_SuperFast(),
			};
		
		createInstance(TAG_NAME, new ConfigurationUtils_Animation(), cfgs_entries);
	}
}
