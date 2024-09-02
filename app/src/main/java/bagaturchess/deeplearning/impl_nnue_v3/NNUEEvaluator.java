package bagaturchess.deeplearning.impl_nnue_v3;


import static bagaturchess.bitboard.impl1.internal.ChessConstants.BLACK;

import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.chess.R;

import java.io.IOException;
import java.io.InputStream;

import bagaturchess.bitboard.api.IBitBoard;
import bagaturchess.nnue_v2.NNUE;
import bagaturchess.search.api.IEvalConfig;
import bagaturchess.search.impl.eval.BaseEvaluator;
import bagaturchess.search.impl.eval.cache.IEvalCache;


public class NNUEEvaluator extends BaseEvaluator {
		
	
	private IBitBoard bitboard;
	
	private NNUE nnue;
	
	
	NNUEEvaluator(IBitBoard _bitboard, IEvalCache _evalCache, IEvalConfig _evalConfig) {
		
		super(_bitboard, _evalCache, _evalConfig);
		
		bitboard = _bitboard;
		
		try {
		
			InputStream is = Application_Base.getInstance().getResources().openRawResource(R.raw.network_bagatur);
			
			nnue = new NNUE(is, bitboard);
		
		} catch (IOException e) {
			
			throw new RuntimeException(e);
		}
	}
	
	
	@Override
	public boolean useEvalCache_Reads() {
		
		return true;
	}
	
	
	@Override
	protected int phase1() {
		
		return 0;
	}
	
	
	@Override
	protected int phase2() {
		
		int actualWhitePlayerEval = nnue.evaluate();
		
		if (bitboard.getColourToMove() == BLACK) {
			
			actualWhitePlayerEval = -actualWhitePlayerEval;
		}
		
		return actualWhitePlayerEval;
	}
	
	
	@Override
	protected int phase3() {
		
		int eval = 0;
				
		return eval;
	}
	
	
	@Override
	protected int phase4() {
		
		int eval = 0;
		
		return eval;
	}
	
	
	@Override
	protected int phase5() {
		
		int eval = 0;
		
		return eval;
	}
}
