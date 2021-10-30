/*
 * Created on Sep 28, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package bagaturchess.learning.impl.utils;

import bagaturchess.bitboard.common.Utils;


public class PSTConstants {
	
	
	public static final double[] createArray(int size, double value) {
		double[] result = new double[size];
		for (int i=0; i<result.length; i++) {
			result[i] = value; 
		}
		return result;
	}
	
	
	public static final double[] ZEROS = Utils.reverseSpecial ( new double[]{	
			   0,   0,   0,   0,   0,   0,   0,   0,
			   0,   0,   0,   0,   0,   0,   0,   0,
			   0,   0,   0,   0,   0,   0,   0,   0,
			   0,   0,   0,   0,   0,   0,   0,   0,
			   0,   0,   0,   0,   0,   0,   0,   0,
			   0,   0,   0,   0,   0,   0,   0,   0,
			   0,   0,   0,   0,   0,   0,   0,   0,
			   0,   0,   0,   0,   0,   0,   0,   0,

	});
	
	
	public static final double[] MIN_2000 = Utils.reverseSpecial ( new double[]{	
			   -2000,   -2000,   -2000,   -2000,   -2000,   -2000,   -2000,  -2000,
			   -2000,   -2000,   -2000,   -2000,   -2000,   -2000,   -2000,  -2000,
			   -2000,   -2000,   -2000,   -2000,   -2000,   -2000,   -2000,  -2000,
			   -2000,   -2000,   -2000,   -2000,   -2000,   -2000,   -2000,  -2000,
			   -2000,   -2000,   -2000,   -2000,   -2000,   -2000,   -2000,  -2000,
			   -2000,   -2000,   -2000,   -2000,   -2000,   -2000,   -2000,  -2000,
			   -2000,   -2000,   -2000,   -2000,   -2000,   -2000,   -2000,  -2000,
			   -2000,   -2000,   -2000,   -2000,   -2000,   -2000,   -2000,  -2000,
	});
	
	public static final double[] MAX_2000 = Utils.reverseSpecial ( new double[]{	
			   2000,   2000,   2000,   2000,   2000,   2000,   2000,  2000,
			   2000,   2000,   2000,   2000,   2000,   2000,   2000,  2000,
			   2000,   2000,   2000,   2000,   2000,   2000,   2000,  2000,
			   2000,   2000,   2000,   2000,   2000,   2000,   2000,  2000,
			   2000,   2000,   2000,   2000,   2000,   2000,   2000,  2000,
			   2000,   2000,   2000,   2000,   2000,   2000,   2000,  2000,
			   2000,   2000,   2000,   2000,   2000,   2000,   2000,  2000,
			   2000,   2000,   2000,   2000,   2000,   2000,   2000,  2000,
	});
	
	public static final double[] MIN_1000 = Utils.reverseSpecial ( new double[]{	
			   -1000,   -1000,   -1000,   -1000,   -1000,   -1000,   -1000,  -1000,
			   -1000,   -1000,   -1000,   -1000,   -1000,   -1000,   -1000,  -1000,
			   -1000,   -1000,   -1000,   -1000,   -1000,   -1000,   -1000,  -1000,
			   -1000,   -1000,   -1000,   -1000,   -1000,   -1000,   -1000,  -1000,
			   -1000,   -1000,   -1000,   -1000,   -1000,   -1000,   -1000,  -1000,
			   -1000,   -1000,   -1000,   -1000,   -1000,   -1000,   -1000,  -1000,
			   -1000,   -1000,   -1000,   -1000,   -1000,   -1000,   -1000,  -1000,
			   -1000,   -1000,   -1000,   -1000,   -1000,   -1000,   -1000,  -1000,
	});
	
	public static final double[] MAX_1000 = Utils.reverseSpecial ( new double[]{	
			   1000,   1000,   1000,   1000,   1000,   1000,   1000,  1000,
			   1000,   1000,   1000,   1000,   1000,   1000,   1000,  1000,
			   1000,   1000,   1000,   1000,   1000,   1000,   1000,  1000,
			   1000,   1000,   1000,   1000,   1000,   1000,   1000,  1000,
			   1000,   1000,   1000,   1000,   1000,   1000,   1000,  1000,
			   1000,   1000,   1000,   1000,   1000,   1000,   1000,  1000,
			   1000,   1000,   1000,   1000,   1000,   1000,   1000,  1000,
			   1000,   1000,   1000,   1000,   1000,   1000,   1000,  1000,
	});
	
	public static final double[] MIN_500 = Utils.reverseSpecial ( new double[]{	
			   -500,   -500,   -500,   -500,   -500,   -500,   -500,  -500,
			   -500,   -500,   -500,   -500,   -500,   -500,   -500,  -500,
			   -500,   -500,   -500,   -500,   -500,   -500,   -500,  -500,
			   -500,   -500,   -500,   -500,   -500,   -500,   -500,  -500,
			   -500,   -500,   -500,   -500,   -500,   -500,   -500,  -500,
			   -500,   -500,   -500,   -500,   -500,   -500,   -500,  -500,
			   -500,   -500,   -500,   -500,   -500,   -500,   -500,  -500,
			   -500,   -500,   -500,   -500,   -500,   -500,   -500,  -500,
	});
	
	public static final double[] MAX_500 = Utils.reverseSpecial ( new double[]{	
			   500,   500,   500,   500,   500,   500,   500,  500,
			   500,   500,   500,   500,   500,   500,   500,  500,
			   500,   500,   500,   500,   500,   500,   500,  500,
			   500,   500,   500,   500,   500,   500,   500,  500,
			   500,   500,   500,   500,   500,   500,   500,  500,
			   500,   500,   500,   500,   500,   500,   500,  500,
			   500,   500,   500,   500,   500,   500,   500,  500,
			   500,   500,   500,   500,   500,   500,   500,  500,
	});
	
	public static final double[] MIN_250 = Utils.reverseSpecial ( new double[]{	
			   -250,   -250,   -250,   -250,   -250,   -250,   -250,  -250,
			   -250,   -250,   -250,   -250,   -250,   -250,   -250,  -250,
			   -250,   -250,   -250,   -250,   -250,   -250,   -250,  -250,
			   -250,   -250,   -250,   -250,   -250,   -250,   -250,  -250,
			   -250,   -250,   -250,   -250,   -250,   -250,   -250,  -250,
			   -250,   -250,   -250,   -250,   -250,   -250,   -250,  -250,
			   -250,   -250,   -250,   -250,   -250,   -250,   -250,  -250,
			   -250,   -250,   -250,   -250,   -250,   -250,   -250,  -250,
	});
	
	public static final double[] MAX_250 = Utils.reverseSpecial ( new double[]{	
			   250,   250,   250,   250,   250,   250,   250,  250,
			   250,   250,   250,   250,   250,   250,   250,  250,
			   250,   250,   250,   250,   250,   250,   250,  250,
			   250,   250,   250,   250,   250,   250,   250,  250,
			   250,   250,   250,   250,   250,   250,   250,  250,
			   250,   250,   250,   250,   250,   250,   250,  250,
			   250,   250,   250,   250,   250,   250,   250,  250,
			   250,   250,   250,   250,   250,   250,   250,  250,
	});
	
	public static final double[] MIN_125 = Utils.reverseSpecial ( new double[]{	
			   -125,   -125,   -125,   -125,   -125,   -125,   -125,  -125,
			   -125,   -125,   -125,   -125,   -125,   -125,   -125,  -125,
			   -125,   -125,   -125,   -125,   -125,   -125,   -125,  -125,
			   -125,   -125,   -125,   -125,   -125,   -125,   -125,  -125,
			   -125,   -125,   -125,   -125,   -125,   -125,   -125,  -125,
			   -125,   -125,   -125,   -125,   -125,   -125,   -125,  -125,
			   -125,   -125,   -125,   -125,   -125,   -125,   -125,  -125,
			   -125,   -125,   -125,   -125,   -125,   -125,   -125,  -125,
	});
	
	public static final double[] MAX_125 = Utils.reverseSpecial ( new double[]{	
			   125,   125,   125,   125,   125,   125,   125,  125,
			   125,   125,   125,   125,   125,   125,   125,  125,
			   125,   125,   125,   125,   125,   125,   125,  125,
			   125,   125,   125,   125,   125,   125,   125,  125,
			   125,   125,   125,   125,   125,   125,   125,  125,
			   125,   125,   125,   125,   125,   125,   125,  125,
			   125,   125,   125,   125,   125,   125,   125,  125,
			   125,   125,   125,   125,   125,   125,   125,  125,
	});
	
	public static final double[] MIN_64 = Utils.reverseSpecial ( new double[]{	
			   -64,   -64,   -64,   -64,   -64,   -64,   -64,  -64,
			   -64,   -64,   -64,   -64,   -64,   -64,   -64,  -64,
			   -64,   -64,   -64,   -64,   -64,   -64,   -64,  -64,
			   -64,   -64,   -64,   -64,   -64,   -64,   -64,  -64,
			   -64,   -64,   -64,   -64,   -64,   -64,   -64,  -64,
			   -64,   -64,   -64,   -64,   -64,   -64,   -64,  -64,
			   -64,   -64,   -64,   -64,   -64,   -64,   -64,  -64,
			   -64,   -64,   -64,   -64,   -64,   -64,   -64,  -64,
	});
	
	public static final double[] MAX_64 = Utils.reverseSpecial ( new double[]{	
			   64,   64,   64,   64,   64,   64,   64,  64,
			   64,   64,   64,   64,   64,   64,   64,  64,
			   64,   64,   64,   64,   64,   64,   64,  64,
			   64,   64,   64,   64,   64,   64,   64,  64,
			   64,   64,   64,   64,   64,   64,   64,  64,
			   64,   64,   64,   64,   64,   64,   64,  64,
			   64,   64,   64,   64,   64,   64,   64,  64,
			   64,   64,   64,   64,   64,   64,   64,  64,
	});
}
