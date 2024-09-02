/*
 *  BagaturChess (UCI chess engine and tools)
 *  Copyright (C) 2005 Krasimir I. Topchiyski (k_topchiyski@yahoo.com)
 *  
 *  Open Source project location: http://sourceforge.net/projects/bagaturchess/develop
 *  SVN repository https://bagaturchess.svn.sourceforge.net/svnroot/bagaturchess
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
 *  along with BagaturChess. If not, see <http://www.eclipse.org/legal/epl-v10.html/>.
 *
 */
package bagaturchess.search.impl.tpt;


import bagaturchess.bitboard.impl1.internal.Assert;
import bagaturchess.bitboard.impl1.internal.EngineConstants;
import bagaturchess.bitboard.impl1.internal.Util;
import bagaturchess.search.impl.utils.DEBUGSearch;
import bagaturchess.uci.api.ChannelManager;


public class TTable_Impl2 implements ITTable {
	
	
	private static final int FLAG = 9; //12
	private static final int MOVE = 11; //14
	private static final int SCORE = 45; //48
	
	
	private long[] keys;
	
	private long[] values;
	
	private long counter_usage;
	
	private long counter_tries;
	
	private long counter_hits;
	
	
	public TTable_Impl2(long size_in_bytes) {
		
		if (ChannelManager.getChannel() != null) ChannelManager.getChannel().dump("TTable_Impl2: bytes_count=" + size_in_bytes);
		
		long maxEntries = size_in_bytes / 16; //two longs per entry and one long has 8 bytes: 2 * 8 = 16
		
		if (maxEntries > 1073741823) { //1073741823 = 2^30 - 1 (should work on 32 and 64 bits), 2147483647 = 2^31 - 1 (should work on 64 bits only)
			maxEntries = 1073741823;
			if (ChannelManager.getChannel() != null) ChannelManager.getChannel().dump("TTable_Impl2: limited to " + 1073741823 + " entries.");
		}
		
		maxEntries = maxEntries + maxEntries % 4;
		
		if (ChannelManager.getChannel() != null) ChannelManager.getChannel().dump("TTable_Impl2: maxEntries=" + maxEntries);
		
		keys = new long[(int) maxEntries];
		
		values = new long[(int) maxEntries];
	}


	@Override
	public final int getUsage() {
		
		return (int) (counter_usage * 100 / keys.length);
	}
	
	
	@Override
	public final int getHitRate() {
		
		if (counter_tries == 0) return 0;
		
		return (int) (counter_hits * 100 / counter_tries);
	}
	
	
	@Override
	public final void correctAllDepths(final int reduction) {
		
		//Do nothing
	}
	
	
	@Override
	public final void get(long key, ITTEntry entry) {
		
		counter_tries++;
		
		if (counter_tries % 100000000 == 0) {
			
			if (ChannelManager.getChannel() != null) {
				
				if (DEBUGSearch.DEBUG_MODE) ChannelManager.getChannel().dump(
						"TTable_Impl2.get: TableID=" + this.hashCode() +
						", HitRate=" + getHitRate() +
						"%, Usage=" + getUsage() + "%");
			}
		}
		
		
		entry.setIsEmpty(true);
		
		
		long value = getTTValue(key);
		
		if (value != 0) {
			
			entry.setIsEmpty(false);
			
			entry.setDepth(getDepth(value));
			entry.setFlag(getFlag(value));
			entry.setEval(getScore(value));
			entry.setBestMove(getMove(value));	
		}
	}
	
	
	@Override
	public final void put(long hashkey, int depth, int eval, int alpha, int beta, int bestmove) {
		
		int flag = ITTEntry.FLAG_EXACT;
		
		if (eval >= beta) {
			
			flag = ITTEntry.FLAG_LOWER;
			
		} else if (eval <= alpha) {
			
			flag = ITTEntry.FLAG_UPPER;
		}
		
		addValue(hashkey, eval, depth, flag, bestmove);
	}
	
	
	private final long getTTValue(final long key) {

		final int index = getIndex(key);

		for (int i = 0; i < 4; i++) {
			
			long stored_key 	= keys[index + i];
			
			long value 			= values[index + i];
			
			if ((stored_key ^ value) == key) {
				
				counter_hits++;
				
				return value;
			}
		}
		
		return 0;
	}
	
	
	private final int getIndex(final long key) {
		
		long index = (int) (key ^ (key >>> 32));
		
		if (index < 0) {
			
			index = -index;
		}
		
		index = index % (keys.length - 3);
		
		index = 4 * (index / 4);
		
		return (int) index;
	}
	
	
	private final void addValue(final long new_key, int score, final int new_depth, final int flag, final int new_move) {

		if (EngineConstants.ASSERT) {
			Assert.isTrue(new_depth >= 0);
			//Assert.isTrue(move != 0);
			//Assert.isTrue(score >= Util.SHORT_MIN && score <= Util.SHORT_MAX);
			//Assert.isTrue(MoveUtil.getSourcePieceIndex(move) != 0);
		}

		final long new_value 		= createValue(score, new_move, flag, new_depth);
		
		final int start_index_entry = getIndex(new_key);
		
		int replaced_min_depth 		= Integer.MAX_VALUE;
		int replaced_index 			= -1;

		for (int i = start_index_entry; i < start_index_entry + 4; i++) {

			long stored_key = keys[i];

			if (stored_key == 0) {
				
				replaced_min_depth 	= 0;
				replaced_index 		= i;
				counter_usage++;
				
				break;
			}
			
			long stored_value = values[i];
			
			int stored_depth = getDepth(stored_value);
			
			if ((stored_key ^ stored_value) == new_key) {
				
				//Minimize writes in the shared static arrays, because in multi-threaded case it impacts performance.
				if (new_value == stored_value) {
					
					return;
				}
				
				if (new_depth >= stored_depth) {
					
					replaced_min_depth = stored_depth;
					
					replaced_index = i;
					
					break;
					
				} else {
					
					return;
				}
			}
			
			// keep the lowest depth and its index 
			if (stored_depth < replaced_min_depth) {
				
				replaced_min_depth = stored_depth;
				
				replaced_index = i;
			}
		}
		
		if (EngineConstants.ASSERT) {
			
			//Assert.isTrue(score >= Util.SHORT_MIN && score <= Util.SHORT_MAX);
		}

		if (replaced_index == -1) {
			
			throw new IllegalStateException();
		}
		
		keys[replaced_index] = new_key ^ new_value;
		values[replaced_index] = new_value;
	}
	
	
	private static int getScore(final long value) {
		
		int score = (int) (value >> SCORE);

		if (EngineConstants.ASSERT) {
			
			//Assert.isTrue(score >= Util.SHORT_MIN && score <= Util.SHORT_MAX);
		}

		return score;
	}
	
	
	private static int getDepth(final long value) {
		return (int) (value & 0xff);
	}
	
	
	private static int getFlag(final long value) {
		return (int) (value >>> FLAG & 3); //...00000011 - last 2 right bits after the shift
	}
	
	//21 bits - 0x3fffff, 24 bits - 0x3ffffff
	private static int getMove(final long value) {
		return (int) (value >>> MOVE & 0x3fffff); //1111111111111111111111 binary or 4194303 decimal
	}
	
	
	// SCORE,HALF_MOVE_COUNTER,MOVE,FLAG,DEPTH
	private static long createValue(final long score, final long move, final long flag, final long depth) {
		if (EngineConstants.ASSERT) {
			//Assert.isTrue(score >= Util.SHORT_MIN && score <= Util.SHORT_MAX);
			Assert.isTrue(depth <= 255);
		}
		return score << SCORE | move << MOVE | flag << FLAG | depth;
	}
}
