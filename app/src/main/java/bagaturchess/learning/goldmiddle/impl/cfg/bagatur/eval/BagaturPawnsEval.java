package bagaturchess.learning.goldmiddle.impl.cfg.bagatur.eval;


import bagaturchess.bitboard.impl.Fields;
import bagaturchess.bitboard.impl.eval.pawns.model.Pawn;
import bagaturchess.bitboard.impl.eval.pawns.model.PawnsModel;
import bagaturchess.bitboard.impl.eval.pawns.model.PawnsModelEval;
import bagaturchess.learning.goldmiddle.impl.cfg.bagatur.filler.BagaturEval_SignalFillerConstants;


public class BagaturPawnsEval extends PawnsModelEval implements FeatureWeights {
	
	
	private int standardEval_o;
	private int standardEval_e;
	private int passersEval_o;
	private int passersEval_e;
	private int passersKingEval_o;
	private int passersKingEval_e;
	/*private int passersKingEval_of;
	private int passersKingEval_ef;
	private int passersKingEval_off;
	private int passersKingEval_eff;
	private int passersKingEval_oof;
	private int passersKingEval_eof;*/
	private int w_gardsScores;
	private int b_gardsScores;
	
	
	public int getWGardsScores() {
		return w_gardsScores;
	}
	
	
	public int getBGardsScores() {
		return b_gardsScores;
	}
	
	
	public int getPassersEval_e() {
		return passersEval_e;
	}
	
	
	public int getPassersEval_o() {
		return passersEval_o;
	}
	
	public int getPassersKingEval_e() {
		return passersKingEval_e;
	}

	public int getPassersKingEval_o() {
		return passersKingEval_o;
	}
	
	
	public int getStandardEval_e() {
		//return 0;
		return standardEval_e;
	}
	
	
	public int getStandardEval_o() {
		return standardEval_o;
	}
	
	@Override
	protected void eval() {
		
		w_gardsScores = 0;
		b_gardsScores = 0;
		standardEval_o = 0;
		standardEval_e = 0;		
		passersEval_o = 0;
		passersEval_e = 0;
		passersKingEval_o = 0;
		passersKingEval_e = 0;
		/*passersKingEval_of = 0;
		passersKingEval_ef = 0;
		passersKingEval_off = 0;
		passersKingEval_eff = 0;
		passersKingEval_oof = 0;
		passersKingEval_eof = 0;*/
		
		PawnsModel pmodel = getModel();
		
		int w_kingID = pmodel.getWKingFieldID();
		int b_kingID = pmodel.getBKingFieldID();
		
		int w_pawns_count = pmodel.getWCount();
		if (w_pawns_count > 0) {
			
			Pawn[] w_pawns = pmodel.getWPawns();
			
			for (int i=0; i<w_pawns_count; i++) {
				
				Pawn p = w_pawns[i];
				
				if (p.isGuard() && p.getRank() <= 3) {
					int scores = 0;
					switch(p.getRank()) {
						case 1:
							scores = 4;
							break;
						case 2:
							scores = 2;
							break;
						case 3:
							scores = 1;
							break;
						default:
							throw new IllegalStateException();
					}
					w_gardsScores += scores;
					standardEval_o += BagaturEval_SignalFillerConstants.PAWNS_KING_GUARDS * scores;
				}
				
				
				int rank = p.getRank();
				int file = Fields.LETTERS[p.getFieldID()];
				int file_symmetry = Fields.FILE_SYMMETRY[file];
				
				
				if (p.isDoubled()) {
					standardEval_o += BagaturEval_SignalFillerConstants.PAWNS_DOUBLED_O[file_symmetry];
					standardEval_e += BagaturEval_SignalFillerConstants.PAWNS_DOUBLED_E[file_symmetry];
				}
				
				if (p.isIsolated()) {
					standardEval_o += BagaturEval_SignalFillerConstants.PAWNS_ISOLATED_O[file_symmetry];
					standardEval_e += BagaturEval_SignalFillerConstants.PAWNS_ISOLATED_E[file_symmetry];
				}
				
				if (p.isBackward()) {
					standardEval_o += BagaturEval_SignalFillerConstants.PAWNS_BACKWARD_O[file_symmetry];
					standardEval_e += BagaturEval_SignalFillerConstants.PAWNS_BACKWARD_E[file_symmetry];
				}
				
				if (p.isSupported() && !p.isPassed()) {
					standardEval_o += BagaturEval_SignalFillerConstants.PAWNS_SUPPORTED_O[file_symmetry];
					standardEval_e += BagaturEval_SignalFillerConstants.PAWNS_SUPPORTED_E[file_symmetry];
				}
				
				if (p.isCandidate()) {
					passersEval_o += BagaturEval_SignalFillerConstants.PAWNS_CANDIDATE_O[rank];
					passersEval_e += BagaturEval_SignalFillerConstants.PAWNS_CANDIDATE_E[rank];
				} else if (p.isPassed() && !p.isDoubled()) {
					if (p.isSupported()) {
						passersEval_o += BagaturEval_SignalFillerConstants.PAWNS_PASSED_SUPPORTED_O[rank];
						passersEval_e += BagaturEval_SignalFillerConstants.PAWNS_PASSED_SUPPORTED_E[rank];
					} else {
						passersEval_o += BagaturEval_SignalFillerConstants.PAWNS_PASSED_O[rank];
						passersEval_e += BagaturEval_SignalFillerConstants.PAWNS_PASSED_E[rank];
					}
					
			        // Adjust bonus based on king proximity:
			        int frontFieldID = p.getFieldID() + 8;
			        int frontFrontFieldID = frontFieldID + 8;
			        if (frontFrontFieldID >= 64) {
			        	frontFrontFieldID = frontFieldID;
			        }
			        
			        int dist_f = Fields.getDistancePoints_reversed(w_kingID, frontFieldID);
			        passersKingEval_o += (BagaturEval_SignalFillerConstants.PAWNS_PASSED_O[rank] * dist_f * 25) / (7 * 100);
			        passersKingEval_e += (BagaturEval_SignalFillerConstants.PAWNS_PASSED_E[rank] * dist_f * 38) / (7 * 100);
			        
			        int dist_ff = Fields.getDistancePoints_reversed(w_kingID, frontFrontFieldID);
			        passersKingEval_o += (BagaturEval_SignalFillerConstants.PAWNS_PASSED_O[rank] * dist_ff * 25) / (7 * 100);
			        passersKingEval_e += (BagaturEval_SignalFillerConstants.PAWNS_PASSED_E[rank] * dist_ff * 38) / (7 * 100);
			        
			        int dist_op_f = Fields.getDistancePoints_reversed(b_kingID, frontFieldID);
			        passersKingEval_o -= (BagaturEval_SignalFillerConstants.PAWNS_PASSED_O[rank] * dist_op_f * 0) / (7 * 100);
			        passersKingEval_e -= (BagaturEval_SignalFillerConstants.PAWNS_PASSED_E[rank] * dist_op_f * 95) / (7 * 100); //* 160) / (7 * 100);
				}
			}
		}
		
		
		int b_pawns_count = pmodel.getBCount();
		if (b_pawns_count > 0) {
			
			Pawn[] b_pawns = pmodel.getBPawns();
			
			for (int i=0; i<b_pawns_count; i++) {
				
				Pawn p = b_pawns[i];
				
				if (p.isGuard() && p.getRank() <= 3) {
					int scores = 0;
					switch(p.getRank()) {
						case 1:
							scores = 4;
							break;
						case 2:
							scores = 2;
							break;
						case 3:
							scores = 1;
							break;
						default:
							throw new IllegalStateException();
					}
					b_gardsScores += scores;
					standardEval_o -= BagaturEval_SignalFillerConstants.PAWNS_KING_GUARDS * scores;
				}
				
				
				int rank = p.getRank();
				int file = Fields.LETTERS[p.getFieldID()];
				int file_symmetry = Fields.FILE_SYMMETRY[file];
				
				if (p.isDoubled()) {
					standardEval_o -= BagaturEval_SignalFillerConstants.PAWNS_DOUBLED_O[file_symmetry];
					standardEval_e -= BagaturEval_SignalFillerConstants.PAWNS_DOUBLED_E[file_symmetry];
				}
				
				if (p.isIsolated()) {
					standardEval_o -= BagaturEval_SignalFillerConstants.PAWNS_ISOLATED_O[file_symmetry];
					standardEval_e -= BagaturEval_SignalFillerConstants.PAWNS_ISOLATED_E[file_symmetry];
				}
				
				if (p.isBackward()) {
					standardEval_o -= BagaturEval_SignalFillerConstants.PAWNS_BACKWARD_O[file_symmetry];
					standardEval_e -= BagaturEval_SignalFillerConstants.PAWNS_BACKWARD_E[file_symmetry];
				}
				
				if (p.isSupported() && !p.isPassed()) {
					standardEval_o -= BagaturEval_SignalFillerConstants.PAWNS_SUPPORTED_O[file_symmetry];
					standardEval_e -= BagaturEval_SignalFillerConstants.PAWNS_SUPPORTED_E[file_symmetry];
				}
				
				if (p.isCandidate()) {
					passersEval_o -= BagaturEval_SignalFillerConstants.PAWNS_CANDIDATE_O[rank];
					passersEval_e -= BagaturEval_SignalFillerConstants.PAWNS_CANDIDATE_E[rank];
				} else if (p.isPassed() && !p.isDoubled()) {
					if (p.isSupported()) {
						passersEval_o -= BagaturEval_SignalFillerConstants.PAWNS_PASSED_SUPPORTED_O[rank];
						passersEval_e -= BagaturEval_SignalFillerConstants.PAWNS_PASSED_SUPPORTED_E[rank];
					} else {
						passersEval_o -= BagaturEval_SignalFillerConstants.PAWNS_PASSED_O[rank];
						passersEval_e -= BagaturEval_SignalFillerConstants.PAWNS_PASSED_E[rank];
					}
					
			        // Adjust bonus based on king proximity:
			        int frontFieldID = p.getFieldID() - 8;
			        int frontFrontFieldID = frontFieldID - 8;
			        if (frontFrontFieldID < 0) {
			        	frontFrontFieldID = frontFieldID;
			        }
			        
			        int dist_f = Fields.getDistancePoints_reversed(b_kingID, frontFieldID);
			        passersKingEval_o -= (BagaturEval_SignalFillerConstants.PAWNS_PASSED_O[rank] * dist_f * 25) / (7 * 100);
			        passersKingEval_e -= (BagaturEval_SignalFillerConstants.PAWNS_PASSED_E[rank] * dist_f * 38) / (7 * 100);
			        
			        int dist_ff = Fields.getDistancePoints_reversed(b_kingID, frontFrontFieldID);
			        passersKingEval_o -= (BagaturEval_SignalFillerConstants.PAWNS_PASSED_O[rank] * dist_ff * 25) / (7 * 100);
			        passersKingEval_e -= (BagaturEval_SignalFillerConstants.PAWNS_PASSED_E[rank] * dist_ff * 38) / (7 * 100);
			        
			        int dist_op_f = Fields.getDistancePoints_reversed(w_kingID, frontFieldID);
			        passersKingEval_o += (BagaturEval_SignalFillerConstants.PAWNS_PASSED_O[rank] * dist_op_f * 0) / (7 * 100);
			        passersKingEval_e += (BagaturEval_SignalFillerConstants.PAWNS_PASSED_E[rank] * dist_op_f * 95) / (7 * 100); //* 160) / (7 * 100);
			     }
			}
		}
	}
}
