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
package bagaturchess.learning.goldmiddle.impl.cfg.old0;


import bagaturchess.bitboard.api.IBitBoard;
import bagaturchess.bitboard.api.IBoardConfig;
import bagaturchess.learning.api.ISignalFiller;
import bagaturchess.learning.goldmiddle.api.ILearningInput;
import bagaturchess.learning.goldmiddle.impl.cfg.bagatur.eval.BagaturPawnsEvalFactory;


public class LearningInputImpl implements ILearningInput {
	
	
	public String getPawnsEvalFactoryClassName() {
		return BagaturPawnsEvalFactory.class.getName();
	}
	
	
	public IBoardConfig createBoardConfig() {
		return new BoardConfigImpl();
	}
	
	
	public ISignalFiller createFiller(IBitBoard bitboard) {
		return new SignalFiller(bitboard);
	}
	
	
	public String getFeaturesConfigurationClassName() {
		return FeaturesConfigurationBagaturImpl.class.getName();
	}
}
