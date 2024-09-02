/**
 *  BagaturChess (UCI chess engine and tools)
 *  Copyright (C) 2005 Krasimir I. Topchiyski (k_topchiyski@yahoo.com)
 *  
 *  This file is part of BagaturChess program.
 * 
 *  BagaturChess is open software: you can redistribute it and/or modify
 *  it under the terms of the Eclipse Public License version 1.0 as published by
 *  the Eclipse Foundation.
 *
 *  BagaturChess is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  Eclipse Public License for more details.
 *
 *  You should have received a copy of the Eclipse Public License version 1.0
 *  along with BagaturChess. If not, see http://www.eclipse.org/legal/epl-v10.html
 *
 */
package bagaturchess.learning.goldmiddle.impl4.filler;


import bagaturchess.bitboard.api.IBitBoard;
import bagaturchess.bitboard.impl1.BoardImpl;
import bagaturchess.bitboard.impl1.internal.ChessBoard;
import bagaturchess.learning.api.IFeatureComplexity;
import bagaturchess.learning.api.ISignalFiller;
import bagaturchess.learning.api.ISignals;
import bagaturchess.learning.goldmiddle.api.IEvalComponentsProcessor;
import bagaturchess.learning.goldmiddle.impl4.base.EvalInfo;
import bagaturchess.learning.goldmiddle.impl4.base.Evaluator;
import bagaturchess.search.api.IEvalConfig;


public class Bagatur_V20_SignalFiller implements ISignalFiller {
	
	
	public static final IEvalConfig eval_config = new bagaturchess.learning.goldmiddle.impl4.cfg.EvaluationConfig_V20_GOLDENMIDDLE_Train();
	
	
	private final IBitBoard bitboard;
	
	private final ChessBoard board;
	
	private final EvalInfo evalInfo;
	
	
	public Bagatur_V20_SignalFiller(IBitBoard _bitboard) {
		
		bitboard = _bitboard;
		
		board = ((BoardImpl)bitboard).getChessBoard();
		
		evalInfo = new EvalInfo();
	}
	
	
	@Override
	public void fill(ISignals signals) {
		
		evalInfo.clearEvals();
		
		evalInfo.fillBoardInfo(board);
		
		IEvalComponentsProcessor evalComponentsProcessor = new EvalComponentsProcessor(signals);
		
		Evaluator.eval1(bitboard.getBoardConfig(), board, evalInfo, evalComponentsProcessor);
		Evaluator.eval2(board, evalInfo, evalComponentsProcessor);
		Evaluator.eval3(board, evalInfo, evalComponentsProcessor);
		Evaluator.eval4(board, evalInfo, evalComponentsProcessor);
		Evaluator.eval5(board, evalInfo, evalComponentsProcessor);
	}
	
	
	@Override
	public void fillByComplexity(int complexity, ISignals signals) {
		switch(complexity) {
			case IFeatureComplexity.GROUP1:
				fill(signals);
				return;
			case IFeatureComplexity.GROUP2:
				//fillPawnSignals(signals);
				return;
			case IFeatureComplexity.GROUP3:
				//fillPiecesIterationSignals(signals);
				return;
			case IFeatureComplexity.GROUP4:
				//fillMovesIterationSignals(signals);
				return;
			case IFeatureComplexity.GROUP5:
				//throw new UnsupportedOperationException("FIELDS_STATES_ITERATION");
				return;
			default:
				throw new IllegalStateException("complexity=" + complexity);
		}
	}
	
	
	private final class EvalComponentsProcessor implements IEvalComponentsProcessor {
		
		
		private final ISignals signals;
		
		
		private EvalComponentsProcessor(final ISignals _signals) {
			signals = _signals;
		}
		
		
		@Override
		public void addEvalComponent(int evalPhaseID, int componentID, int value_o, int value_e, double weight_o, double weight_e) {
			
			int total_factor = Math.min(63, bitboard.getMaterialFactor().getTotalFactor());
			
			int features_index = total_factor / 32;
			
			if (features_index == 0) {
				
				signals.getSignal(componentID).addStrength(value_e, -1);
				
			} else if (features_index == 1) {
				
				signals.getSignal(componentID).addStrength(value_o, -1);
				
			} else {
				
				throw new IllegalStateException("features_index=" + features_index);
			}
			
			//signals.getSignal(componentID + 1000).addStrength(value_e, -1);
		}


		@Override
		public void addEvalComponent(int evalPhaseID, int componentID,
				int fieldID, int value_o, int value_e, double weight_o,
				double weight_e) {

			throw new UnsupportedOperationException();
		}
		
		
		@Override
		public void setEvalInfo(Object evalinfo) {
			
			throw new UnsupportedOperationException();
		}
	}
}
