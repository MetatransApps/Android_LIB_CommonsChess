package org.metatrans.commons.chess.cfg.rules;


import android.content.Context;

import java.util.HashMap;
import java.util.Map;


public class ConfigurationUtils_Bagatur_AllRules {
	
	
	private static IConfigurationRule[] ALL_CFGs;
	private static String[] ALL_Names;

	private static final Map<Integer, IConfigurationRule> mapping_id = new HashMap<Integer, IConfigurationRule>();
	private static final Map<String, IConfigurationRule> mapping_name = new HashMap<String, IConfigurationRule>();
	
	private static Context context;
	private static boolean initialized = false;
	
	
	public static void init(Context _context) {
		
		context = _context;
		
		if (!initialized) { 
			
			 ALL_CFGs = new IConfigurationRule[] {
						new Config_Rules_All(),
					};
			 
			 ALL_Names = new String[ALL_CFGs.length];
		 
		 
			for (int i=0; i<ALL_CFGs.length; i++) {
				
				Integer id = ALL_CFGs[i].getID();
				String name = context.getString(ALL_CFGs[i].getName());
				ALL_Names[i] = name;
				
				IConfigurationRule cfg = ALL_CFGs[i];
				
				if (mapping_id.containsKey(id)) {
					throw new IllegalStateException("Duplicated cfg id: " + id);
				}
				mapping_id.put(id, cfg);
				
				if (mapping_name.containsKey(id)) {
					throw new IllegalStateException("Duplicated cfg name: " + id);
				}
				
				mapping_name.put(name, cfg);
			}
			
			initialized = true;
		}
	}
	
	
	public static IConfigurationRule[] getAll() {
		return ALL_CFGs;
	}

	
	public static String[] getNames() {
		return ALL_Names;
	}
	

	public static int getOrderNumber(int cfgID) {
		for (int i=0; i<ALL_CFGs.length; i++) {
			Integer id = ALL_CFGs[i].getID();
			if (id == cfgID) {
				return i;
			}
		}
		throw new IllegalStateException("CFG identifier " + cfgID + " not found.");
	}
	
	
	public static int getID(int orderNumber) {
		return ALL_CFGs[orderNumber].getID();
	}
	
	
	public static IConfigurationRule getConfigByID(int id) {
		
		IConfigurationRule result = mapping_id.get(id);
		
		if (result == null) {
			throw new IllegalStateException("Config with id = " + id + " not found.");
		}
		
		return result;
	}
}
