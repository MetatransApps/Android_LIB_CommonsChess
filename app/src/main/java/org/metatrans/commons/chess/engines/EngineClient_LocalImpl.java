package org.metatrans.commons.chess.engines;


import android.os.RemoteException;

import java.io.InputStream;

import org.metatrans.commons.DeviceUtils;
import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.chess.R;
import org.metatrans.commons.chess.engines.search.IEngineClient;
import org.metatrans.commons.chess.engines.search.IRunAPIStatus;
import org.metatrans.commons.chess.engines.search.RunAPIBestMoveSender;
import org.metatrans.commons.chess.engines.search.RunAPIMediator;
import org.metatrans.commons.chess.engines.search.RunAPIStatusImpl1;

import bagaturchess.bitboard.api.BoardUtils;
import bagaturchess.bitboard.api.IBitBoard;
import bagaturchess.engines.cfg.base.RootSearchConfig_BaseImpl_SMP_Threads;
import bagaturchess.engines.cfg.base.TimeConfigImpl;
import bagaturchess.opening.api.OpeningBook;
import bagaturchess.opening.api.OpeningBookFactory;
import bagaturchess.search.api.IRootSearch;
import bagaturchess.search.api.IRootSearchConfig;
import bagaturchess.search.api.internal.ISearchInfo;
import bagaturchess.search.api.internal.ISearchMediator;
import bagaturchess.search.impl.env.MemoryConsumers;
import bagaturchess.search.impl.env.SharedData;
import bagaturchess.search.impl.rootsearch.parallel.MTDParallelSearch_ThreadsImpl;
import bagaturchess.search.impl.uci_adaptor.TimeSaver;
import bagaturchess.search.impl.uci_adaptor.timemanagement.ITimeController;
import bagaturchess.search.impl.uci_adaptor.timemanagement.TimeControllerFactory;
import bagaturchess.uci.api.ChannelManager;
import bagaturchess.uci.impl.Channel_Console;
import bagaturchess.uci.impl.commands.Go;


public class EngineClient_LocalImpl implements IEngineClient {
	
	
	public static boolean USE_OPENNING = true;
	
	public static double MEMORY_USAGE_PERCENT = 0.25;
	
	
	private static EngineClient_LocalImpl singleton;
	
	
	private IRootSearchConfig cfg;
	private IRootSearch search;
	private static IBitBoard bitboardForSetup;
	private TimeSaver saver;
	
	private volatile ISearchMediator currentMediator;
	
	
	public static IEngineClient getSingleton(IBitBoard initialBoard) {

		synchronized (EngineClient_LocalImpl.class) {

			if (singleton == null) {
				
				singleton = new EngineClient_LocalImpl(initialBoard);
				
			} else if (bitboardForSetup != null && !initialBoard.toEPD().equals(bitboardForSetup.toEPD())) {
				
				System.out.println("RECREATE EngineClient_LocalImpl");
				
				singleton.stopThinking();
				singleton.search.shutDown();
				singleton.search = null;
				singleton.cfg = null;
				singleton.saver = null;
				bitboardForSetup = null;
				singleton = null;
				
				System.gc();
				
				singleton = new EngineClient_LocalImpl(initialBoard);
			}
					
		}
		return singleton;
	}
	
	
	public static void clearSingleton() {
		singleton = null;
	}
	
	
	private EngineClient_LocalImpl(IBitBoard initialBoard) {
				
		init(initialBoard);	
	}
	
	
	private synchronized void init(IBitBoard initialBoard) {
		
		System.out.println("EngineClient_LocalImpl: init");
		
		ChannelManager.setChannel(new Channel_Console(System.in, System.out, System.out));
		
		MemoryConsumers.set_JVMDLL_MEMORY_CONSUMPTION((int) DeviceUtils.getJVM_MemoryUsage());
		
		MemoryConsumers.set_MEMORY_USAGE_PERCENT(MEMORY_USAGE_PERCENT);
		
		MemoryConsumers.set_MIN_MEMORY_BUFFER(5 * 1024 * 1024);
		
		
		/*
		set ARGS=bagaturchess.engines.cfg.base.UCIConfig_BaseImpl
		set ARGS=%ARGS% bagaturchess.search.impl.uci_adaptor.UCISearchAdaptorImpl_PonderingOpponentMove
		set ARGS=%ARGS% bagaturchess.engines.cfg.base.UCISearchAdaptorConfig_BaseImpl
		set ARGS=%ARGS% bagaturchess.search.impl.rootsearch.parallel.MTDParallelSearch_ThreadsImpl
		set ARGS=%ARGS% bagaturchess.engines.cfg.base.RootSearchConfig_BaseImpl_SMP_Threads
		set ARGS=%ARGS% bagaturchess.search.impl.alg.impl1.Search_PVS_NWS
		set ARGS=%ARGS% bagaturchess.engines.cfg.base.SearchConfigImpl_AB
		set ARGS=%ARGS% bagaturchess.learning.goldmiddle.impl4.cfg.BoardConfigImpl_V20
		set ARGS=%ARGS% bagaturchess.learning.goldmiddle.impl4.cfg.EvaluationConfig_V20
		*/
		
		cfg = new RootSearchConfig_BaseImpl_SMP_Threads(
				new String[] {
						bagaturchess.search.impl.alg.impl1.Search_PVS_NWS.class.getName(),
						bagaturchess.engines.cfg.base.SearchConfigImpl_AB.class.getName(),
						bagaturchess.learning.goldmiddle.impl4.cfg.BoardConfigImpl_V20.class.getName(),
						bagaturchess.learning.goldmiddle.impl4.cfg.EvaluationConfig_V20.class.getName() });
		
		bitboardForSetup = BoardUtils.createBoard_WithPawnsCache(initialBoard.toEPD(),
				bagaturchess.learning.goldmiddle.impl.cfg.bagatur.eval.BagaturPawnsEvalFactory.class.getName(), cfg.getBoardConfig(), 10);
		
		try {
			if (OpeningBookFactory.getBook() == null) {
				InputStream is_w_ob = Application_Base.getInstance().getResources().openRawResource(R.raw.w30);
				InputStream is_b_ob = Application_Base.getInstance().getResources().openRawResource(R.raw.b30);
				OpeningBookFactory.initBook(is_w_ob, is_b_ob);				
			}
		} catch(Throwable t) {
			System.out.println("Unable to load Openning Book. Error while openning file streams: " + t.getMessage());
		}
		
		SharedData sharedData = new SharedData(ChannelManager.getChannel(), cfg);
		
		if (USE_OPENNING && OpeningBookFactory.getBook() != null) {
			saver = new TimeSaver(OpeningBookFactory.getBook());
		}
		
		search = new MTDParallelSearch_ThreadsImpl(new Object[] { cfg, sharedData });
		//search = new SequentialSearch_MTD(new Object[] {cfg, sharedData});
		
		search.createBoard(bitboardForSetup);
		
		System.out.println("EngineClient_LocalImpl: init OK");
	}
	
	
	@Override
	public synchronized void startThinking(IBitBoard boardForSetup) throws RemoteException {
		
		System.out.println("EngineClient_LocalImpl: startThinking");
		
		if (getCurrentMediator() != null) {
			//throw new IllegalStateException("currentMediator is not null");
			stopThinking();
		}
		
		
		int[] moves = getMoves(boardForSetup);
		
		bitboardForSetup.revert();
		for (int i=0; i<moves.length; i++) {
			bitboardForSetup.makeMoveForward(moves[i]);
		}
		
		System.out.println(bitboardForSetup);
		
		IRunAPIStatus status = new RunAPIStatusImpl1(bitboardForSetup);
		setCurrentMediator(new RunAPIMediator(status, 24 * 60 * 60 * 1000));
		
		boolean moveSent = (saver == null ? false : saver.beforeMove(boardForSetup, OpeningBook.OPENNING_BOOK_MODE_POWER1, getCurrentMediator(), true));
		
		if (!moveSent) {
			
			Go go = new Go(ChannelManager.getChannel(), "go infinite");
			ITimeController timeController = TimeControllerFactory.createTimeController(new TimeConfigImpl(), bitboardForSetup.getColourToMove(), go);
			search.negamax(bitboardForSetup, currentMediator, timeController, go);

			System.out.println("EngineClient_LocalImpl: startThinking OK");
			
		} else {
			
			getCurrentMediator().getStopper().markStopped();
			
			System.out.println("EngineClient_LocalImpl: Openning book move used");
		}
	}
	
	
	@Override
	public synchronized ISearchInfo stopThinkingWithResult() throws Exception {

		System.out.println("EngineClient_LocalImpl: stopThinkingWithResult");
		
		if (getCurrentMediator() != null) {//TODO: Durty fix
			getCurrentMediator().getStopper().markStopped();
		}
		
		search.stopSearchAndWait();
		
		System.out.println("EngineClient_LocalImpl: stopThinkingWithResult - stopped!");
		
		ISearchInfo moveinfo = getCurrentMediator() == null ? null : getCurrentMediator().getLastInfo();
		
		setCurrentMediator(null);
		
		return moveinfo;
	}
	
	
	@Override
	public synchronized void stopThinking() {
		
		System.out.println("EngineClient_LocalImpl: stopThinking");
		
		if (getCurrentMediator() != null && getCurrentMediator().getStopper() != null)
			getCurrentMediator().getStopper().markStopped();
		
		search.stopSearchAndWait();
		
		System.out.println("EngineClient_LocalImpl: stopThinking - stopped!");
		
		setCurrentMediator(null);
	}
	
	
	@Override
	public synchronized boolean hasAtLeastOneMove() throws RemoteException {
		return currentMediator.getLastInfo() != null;
	}
	
	
	@Override
	public synchronized String getInfoLine() throws RemoteException {
		
		//System.out.println("EngineClient_LocalImpl: getInfoLine");
		
		return "Info Line: " + System.currentTimeMillis();
	}
	
	
	@Override
	public synchronized boolean isDone() throws RemoteException {
		
		//System.out.println("EngineClient_LocalImpl: isDone");
		
		if (getCurrentMediator() == null) {
			return true;
		}
		
		return getCurrentMediator().getStopper().isStopped() || ((RunAPIBestMoveSender) getCurrentMediator().getBestMoveSender()).isInterrupted();
	}
	
	
	private static int[] getMoves(IBitBoard bitboard) {
		
		int count = bitboard.getPlayedMovesCount();
		int[] result = new int[count];
		int[] moves = bitboard.getPlayedMoves();
		for (int i=0; i<count; i++) {
			result[i] = moves[i];
		}
	
		return result;
	}


	private ISearchMediator getCurrentMediator() {
		return currentMediator;
	}
	
	
	private void setCurrentMediator(ISearchMediator _currentMediator) {
		currentMediator = _currentMediator;
	}
}
