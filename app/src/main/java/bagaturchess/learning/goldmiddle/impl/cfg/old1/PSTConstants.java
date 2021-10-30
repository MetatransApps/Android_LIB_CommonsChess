/*
 * Created on Sep 28, 2008
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package bagaturchess.learning.goldmiddle.impl.cfg.old1;

import bagaturchess.bitboard.common.Utils;


public class PSTConstants {
	
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
	
	public static final double[] createArray(int size, double value) {
		double[] result = new double[size];
		for (int i=0; i<result.length; i++) {
			result[i] = value; 
		}
		return result;
	}
	
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
	
	public static final double[] KING_O_MIN = Utils.reverseSpecial ( new double[]{
			-128,   -128,   -128,   -128,   -128,   -128,   -128,   -128,   
			-128,   -128,   -128,   -128,   -128,   -128,   -128,   -128,   
			-128,   -128,   -128,   -128,   -128,   -128,   -128,   -128,   
			-128,   -128,   -128,   -128,   -128,   -128,   -128,   -128,   
			-128,   -128,   -128,   -128,   -128,   -128,   -128,   -128,    
			-128,   -128,   -128,   -128,   -128,   -128,   -128,   -128,   
			-128,   -128,   -128,   -128,   -128,   -128,   -128,   -128,   
			-128,   -128,   -128,   -128,   -128,   -128,   -128,   -128,   
	});	
	
	public static final double[] KING_O_MAX = Utils.reverseSpecial ( new double[]{
			-128,   -128,   -128,   -128,   -128,   -128,   -128,   -128,   
			-128,   -128,   -128,   -128,   -128,   -128,   -128,   -128,   
			-128,   -128,   -128,   -128,   -128,   -128,   -128,   -128,   
			-128,   -128,   -128,   -128,   -128,   -128,   -128,   -128,   
			128,   128,   128,   128,   128,   128,   128,   128,    
			128,   128,   128,   128,   128,   128,   128,   128,  
			128,   128,   128,   128,   128,   128,   128,   128,  
			128,   128,   128,   128,   128,   128,   128,   128,
	});
	
	
	/**
	 * KNIGHTS
	 */
	public static final double[] KNIGHT_O = Utils.reverseSpecial ( new double[]{
			-128,   -1,   21,   4,   125,   3,   -6,   -128,   
			-2,   26,   25,   58,   82,   56,   66,   0,   
			1,   58,   67,   78,   101,   86,   64,   27,   
			9,   27,   48,   46,   44,   59,   29,   30,   
			-6,   9,   27,   30,   32,   30,   14,   -4,   
			-23,   0,   16,   27,   22,   20,   9,   -14,   
			-33,   -24,   0,   0,   -3,   0,   -29,   -30,   
			-70,   -29,   -29,   -26,   -34,   -24,   -24,   -89,   

	});

	public static final double[] KNIGHT_E = Utils.reverseSpecial ( new double[]{
			-92,   -40,   -58,   -24,   -116,   -34,   -56,   -118,   
			-43,   -22,   -1,   -20,   -26,   -24,   -69,   -33,   
			-17,   0,   11,   16,   1,   4,   5,   -6,   
			-12,   17,   9,   24,   31,   4,   20,   -33,   
			-31,   0,   17,   21,   19,   11,   1,   -46,   
			-42,   -22,   -2,   3,   0,   -2,   -29,   -47,   
			-57,   -22,   -10,   -3,   -1,   -16,   -21,   -56,   
			-69,   -54,   -21,   -26,   -7,   -39,   -57,   -62,   
   
	});
	
	
	/**
	 * BISHOPS
	 */
	
	public static final double[] BISHOP_O = Utils.reverseSpecial ( new double[]{
			-16,   -8,   1,   -12,   15,   -1,   0,   -14,   
			-13,   15,   1,   13,   46,   23,   27,   -22,   
			-11,   21,   44,   53,   62,   57,   30,   2,   
			-10,   -4,   28,   38,   26,   32,   -6,   -2,   
			-10,   2,   8,   27,   31,   7,   -1,   -9,   
			-3,   -8,   6,   3,   1,   8,   -4,   -2,   
			6,   2,   -3,   -8,   -6,   -4,   4,   0,   
			-9,   -15,   -17,   -26,   -28,   -10,   -1,   -9,   

	});
	
	public static final double[] BISHOP_E = Utils.reverseSpecial ( new double[]{
			-33,   -30,   -34,   -12,   -41,   -39,   -39,   -38,   
			-25,   -1,   0,   -18,   -39,   -7,   -33,   -24,   
			-10,   0,   1,   0,   0,   4,   -8,   -24,   
			-2,   1,   12,   20,   22,   3,   1,   -22,   
			-7,   0,   17,   13,   14,   16,   0,   -16,   
			-18,   0,   7,   6,   12,   2,   -6,   -25,   
			-4,   -2,   -1,   -1,   0,   0,   -7,   -4,   
			-14,   -1,   -12,   0,   0,   -14,   -14,   -10,   

	});

	
	/**
	 * ROOKS
	 */
	
	public static final double[] ROOK_O = Utils.reverseSpecial ( new double[]{	
			10,   15,   69,   62,   63,   106,   56,   80,   
			17,   28,   56,   59,   38,   82,   23,   48,   
			6,   9,   35,   33,   50,   51,   17,   15,   
			0,   2,   24,   30,   31,   29,   6,   18,   
			0,   -10,   25,   15,   11,   1,   1,   4,   
			-11,   -6,   -8,   3,   0,   -2,   0,   -7,   
			-15,   -14,   -4,   -12,   -5,   -3,   -6, -10,
			-5,   -1,   2,   6,   7,   1,   1,   0,   
 
	
	});
	
	public static final double[] ROOK_E = Utils.reverseSpecial ( new double[]{	
			20,   25,   1,   -1,   1,   -5,   -3,   -13,   
			52,   41,   34,   27,   43,   23,   39,   29,   
			22,   23,   9,   10,   7,   6,   14,   16,   
			-1,   -1,   0,   0,   -3,   -4,   -1,   -7,   
			-6,   1,   -4,   0,   -1,   -1,   -6,   -7,   
			0,   -1,   3,   -1,   6,   -2,   0,   -8,   
			-2,   1,   7,   3,   6,   -1,   -4,   -7,   
			-1,   0,   3,   4,   0,   -2,   3,   -1,   
  
		});
	
	
	/**
	 * QUEENS
	 */
	
	public static final double[] QUEEN_O = Utils.reverseSpecial ( new double[]{	
			0,   0,   12,   45,   36,   66,   3,   1,   
			-17,   0,   9,   6,   6,   13,   8,   2,   
			-12,   -23,   1,   9,   13,   20,   6,   17,   
			0,   -3,   6,   20,   12,   14,   1,   12,   
			4,   0,   3,   9,   6,   5,   17,   0,   
			0,   2,   0,   2,   0,   4,   0,   0,   
			-1,   0,   0,   -10,   -12,   0,   -2,   0,   
			0,   -9,   -10,   -6,   -7,   -1,   -3,   -5,   
	});
	
	public static final double[] QUEEN_E = Utils.reverseSpecial ( new double[]{	
			-44,   -52,   -68,   -94,   -88,   -128,   -5,   -13,   
			4,   5,   0,   0,   0,   16,   1,   1,   
			-59,   0,   0,   0,   0,   0,   0,   -76,   
			-90,   0,   0,   0,   0,   0,   -30,   -61,   
			-101,   -1,   0,   0,   0,   -1,   -77,   -98,   
			-103,   -85,   0,   0,   0,   0,   -95,   -110,   
			-112,   -100,   -6,   -9,   -1,   -80,   -96,   -125,   
			-128,   -128,   -121,   -107,   -118,   -118,   -128,   -128,   
	});
	
	/**
	 * PAWNS 
	 */
	
	public static final double[] PAWN_O = Utils.reverseSpecial ( new double[]{	
			0,   0,   0,   0,   0,   0,   0,   0,   
			43,   49,   32,   46,   4,   44,   -15,   26,   
			10,   21,   63,   64,   83,   68,   44,   23,   
			0,   23,   36,   44,   44,   40,   19,   0,   
			0,   21,   22,   33,   30,   22,   11,   -3,   
			1,   13,   22,   25,   21,   14,   6,   0,   
			-1,   0,   22,   5,   0,   15,   -1,   -1,   
			0,   0,   0,   0,   0,   0,   0,   0,   
 
	});
	
	public static final double[] PAWN_E = Utils.reverseSpecial ( new double[]{	  
			0,   0,   0,   0,   0,   0,   0,   0,   
			85,   49,   29,   0,   1,   19,   55,   60,   
			17,   6,   0,   -1,   0,   -14,   -20,   9,   
			7,   10,   0,   1,   2,   0,   6,   5,   
			3,   1,   1,   1,   1,   1,   -1,   1,   
			-1,   0,   -2,   3,   4,   0,   -4,   -3,   
			1,   2,   0,   1,   19,   -7,   1,   -1,   
			0,   0,   0,   0,   0,   0,   0,   0,   

	});
	
	/**
	 * KINGS
	 */
	
	public static final double[] KING_O = Utils.reverseSpecial ( new double[]{
			-128,   -128,   -128,   -128,   -128,   -128,   -128,   -128,   
			-128,   -128,   -128,   -128,   -128,   -128,   -128,   -128,   
			-128,   -128,   -128,   -128,   -128,   -128,   -128,   -128,   
			-128,   -128,   -128,   -128,   -128,   -128,   -128,   -128,   
			128,   -54,   -128,   -128,   -78,   -128,   -87,   -128,   
			-70,   0,   -89,   -71,   -121,   -63,   -43,   -79,   
			-17,   2,   -15,   -43,   -42,   -25,   9,   -9,   
			-7,   25,   -19,   -35,   -1,   -7,   26,   0,   

 
	});	
	
	public static final double[] KING_E = Utils.reverseSpecial ( new double[]{
			0,   -2,   30,   -23,   28,   -85,   -1,   2,   
			-23,   24,   37,   2,   42,   46,   58,   -18,   
			-1,   15,   51,   9,   46,   68,   91,   -4,   
			0,   8,   53,   57,   62,   66,   54,   24,   
			-58,   24,   58,   71,   63,   66,   47,   0,   
			-8,   24,   58,   52,   69,   47,   31,   -5,   
			-40,   2,   21,   38,   36,   21,   0,   -42,   
			-88,   -65,   -30,   -14,   -32,   -33,   -60,   -100,   
 
	});	
	
	public static final double[] CONTROL_O = Utils.reverseSpecial ( new double[]{
			   0,   0,   0,   0,   0,   0,   0,   0,
			   0,   0,   0,   0,   0,   0,   0,   0,
			   0,   0,   0,   0,   0,   0,   0,   0,
			   0,   0,   0,   0,   0,   0,   0,   0,
			   0,   0,   0,   0,   0,   0,   0,   0,
			   0,   0,   0,   0,   0,   0,   0,   0,
			   0,   0,   0,   0,   0,   0,   0,   0,
			   0,   0,   0,   0,   0,   0,   0,   0,
	});	
	
	public static final double[] CONTROL_E = Utils.reverseSpecial ( new double[]{
			   0,   0,   0,   0,   0,   0,   0,   0,
			   0,   0,   0,   0,   0,   0,   0,   0,
			   0,   0,   0,   0,   0,   0,   0,   0,
			   0,   0,   0,   0,   0,   0,   0,   0,
			   0,   0,   0,   0,   0,   0,   0,   0,
			   0,   0,   0,   0,   0,   0,   0,   0,
			   0,   0,   0,   0,   0,   0,   0,   0,
			   0,   0,   0,   0,   0,   0,   0,   0,
	});
	
	public static final double[] OUTPOST_KNIGHT_O = Utils.reverseSpecial ( new double[]{
			0,   0,   0,   0,   0,   0,   0,   0,   
			0,   0,   9,   11,   17,   11,   0,   0,   
			0,   0,   11,   15,   21,   15,   0,   0,   
			0,   0,   14,   13,   16,   22,   0,   0,   
			0,   0,   0,   0,   0,   0,   0,   0,   
			0,   0,   0,   0,   0,   0,   0,   0,   
			0,   0,   0,   0,   0,   0,   0,   0,   
			0,   0,   0,   0,   0,   0,   0,   0,   
	});
	
	public static final double[] OUTPOST_KNIGHT_E = Utils.reverseSpecial ( new double[]{
			0,   0,   0,   0,   0,   0,   0,   0,   
			0,   0,   10,   13,   19,   7,   0,   0,   
			0,   0,   21,   25,   21,   11,   0,   0,   
			0,   0,   15,   20,   19,   10,   0,   0,   
			0,   0,   0,   0,   0,   0,   0,   0,   
			0,   0,   0,   0,   0,   0,   0,   0,   
			0,   0,   0,   0,   0,   0,   0,   0,   
			0,   0,   0,   0,   0,   0,   0,   0,   
	});
	
	public static final double[] OUTPOST_BISHOP_O = Utils.reverseSpecial ( new double[]{
			0,   0,   0,   0,   0,   0,   0,   0,   
			0,   0,   2,   3,   3,   2,   0,   0,   
			0,   0,   5,   7,   11,   9,   0,   0,   
			0,   0,   4,   6,   9,   6,   0,   0,   
			0,   0,   0,   0,   0,   0,   0,   0,   
			0,   0,   0,   0,   0,   0,   0,   0,   
			0,   0,   0,   0,   0,   0,   0,   0,   
			0,   0,   0,   0,   0,   0,   0,   0,  
	});
	
	public static final double[] OUTPOST_BISHOP_E = Utils.reverseSpecial ( new double[]{
			0,   0,   0,   0,   0,   0,   0,   0,   
			0,   0,   4,   6,   9,   3,   0,   0,   
			0,   0,   12,   8,   8,   10,   0,   0,   
			0,   0,   7,   13,   9,   5,   0,   0,   
			0,   0,   0,   0,   0,   0,   0,   0,   
			0,   0,   0,   0,   0,   0,   0,   0,   
			0,   0,   0,   0,   0,   0,   0,   0,   
			0,   0,   0,   0,   0,   0,   0,   0,  
	});
}
