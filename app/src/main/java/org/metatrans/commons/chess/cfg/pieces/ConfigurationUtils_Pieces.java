package org.metatrans.commons.chess.cfg.pieces;


import android.annotation.SuppressLint;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;


public class ConfigurationUtils_Pieces {
	
	
	private static IConfigurationPieces[] ALL_CFGs;
	private static String[] ALL_Names;
	
	@SuppressLint("UseSparseArrays")
	private static final Map<Integer, IConfigurationPieces> mapping_id = new HashMap<Integer, IConfigurationPieces>();
	private static final Map<String, IConfigurationPieces> mapping_name = new HashMap<String, IConfigurationPieces>();
	
	private static Context context;
	private static boolean initialized = false;
	
	
	public static void init(Context _context) {
		
		context = _context;
		
		if (!initialized) {
			
			 ALL_CFGs = new IConfigurationPieces[] {
					 	new Config_Pieces_Bagaturs_v1(context),
					 	new Config_Pieces_Bagaturs_v2(context),
					 	new Config_Pieces_ASCII_droid_sans_fallback_1(context),
						new Config_Pieces_ASCII_arial_unicode_ms(context),
						new Config_Pieces_PaintClassic(context),
						new Config_Pieces_ASCII_code2000(context),
						new Config_Pieces_ASCII_segoe_ui_symbol(context),
						new Config_Pieces_ClayDark(context),
						new Config_Pieces_ClayLight(context),
						new Config_Pieces_Blocks(context),
						new Config_Pieces_ASCII_dejavu_sans(context),
						new Config_Pieces_PaintGosts(context),
						new Config_Pieces_ClayLightBW(context),
						new Config_Pieces_ClayDarkBW(context),
						new Config_Pieces_ASCII_freeserif(context),
						new Config_Pieces_BlocksBW(context),
						new Config_Pieces_ASCII_yozfont(context),
						//new Config_Pieces_ASCII_droid_sans_fallback_2(context),
					};
			 
			 ALL_Names = new String[ALL_CFGs.length];
			 
			 
			for (int i=0; i<ALL_CFGs.length; i++) {
				
				Integer id = ALL_CFGs[i].getID();
				String name = context.getString(ALL_CFGs[i].getName());
				ALL_Names[i] = name;
				
				IConfigurationPieces cfg = ALL_CFGs[i];
				
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
	
	
	public static IConfigurationPieces[] getAll() {
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
		
		//Campatibility for IConfigurationPieces.CFG_PIECES_ASCII_droid_sans_fallback_2
		if (cfgID == IConfigurationPieces.CFG_PIECES_ASCII_droid_sans_fallback_2) {
			return 0;//IConfigurationPieces.CFG_PIECES_ASCII_droid_sans_fallback_1;
		}
		
		throw new IllegalStateException("CFG identifier " + cfgID + " not found.");
	}
	
	
	public static int getID(int orderNumber) {
		return ALL_CFGs[orderNumber].getID();
	}
	
	
	public static IConfigurationPieces getConfigByID(int id) {
		
		IConfigurationPieces result = mapping_id.get(id);
		
		if (result == null) {
			
			//Compatibility
			if (id == IConfigurationPieces.CFG_PIECES_ASCII_droid_sans_fallback_2) {
				return new Config_Pieces_ASCII_droid_sans_fallback_2(context);
			}
			
			throw new IllegalStateException("Config with id = " + id + " not found.");
		}
		
		return result;
	}
}
