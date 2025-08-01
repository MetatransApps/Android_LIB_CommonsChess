package bagaturchess.search.impl.env;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import bagaturchess.egtb.syzygy.SyzygyTBProbing;
import bagaturchess.opening.api.OpeningBook;
import bagaturchess.opening.api.OpeningBookFactory;
import bagaturchess.search.api.IRootSearchConfig;
import bagaturchess.search.impl.eval.cache.EvalCache_Impl2;
import bagaturchess.search.impl.eval.cache.IEvalCache;
import bagaturchess.search.impl.tpt.ITTable;
import bagaturchess.search.impl.tpt.TTable_Impl2;
import bagaturchess.uci.api.ChannelManager;
import bagaturchess.uci.api.IChannel;


public class MemoryConsumers {
	
	
	private static final double DEFAULT_MEMORY_USAGE_PERCENT 			= 0.5;
	
	private static double MEMORY_USAGE_PERCENT 							= DEFAULT_MEMORY_USAGE_PERCENT;
	
	//The static memory is between 128MB and 384MB for desktop computers.
	//Under Android it should be less even close or equal to 0.
	//set_STATIC_JVM_MEMORY method can be used to adjust this value.
	//Allocate static arrays and structures created up to now. For example, opening books and egtb.
	private static int STATIC_JVM_MEMORY_IN_MEGABYTES					= 384;
	
	
	private static final int SIZE_MIN_ENTRIES_MULTIPLIER				= 111;
	private static final int SIZE_MIN_ENTRIES_TPT						= 8;
	private static final int SIZE_MIN_ENTRIES_EC						= 4;
	private static final int SIZE_MIN_ENTRIES_PEC						= 1 * SIZE_MIN_ENTRIES_MULTIPLIER;
	
	private static double MEM_USAGE_SYZYGY_DTZ_CACHE 					= 0.05;
	
	
	public static void set_MEMORY_USAGE_PERCENT(double val) {
		
		MEMORY_USAGE_PERCENT = val;	
	}
	
	
	public static void set_STATIC_JVM_MEMORY(int static_jvm_memory_in_megabytes) {
		
		STATIC_JVM_MEMORY_IN_MEGABYTES = static_jvm_memory_in_megabytes;	
	}
	
	
	static {
		
		try {
			
			if (OpeningBookFactory.getBook() == null) {
				
				InputStream is_w_openning_book = new FileInputStream("./data/w.ob");
				InputStream is_b_openning_book = new FileInputStream("./data/b.ob");
				
				OpeningBookFactory.initBook(is_w_openning_book, is_b_openning_book);				
			}
			
		} catch(Throwable t) {
			
			if (ChannelManager.getChannel() != null) ChannelManager.getChannel().dump("Unable to load Openning Book. Error while openning file streams: " + t.getMessage());
		}
	}
	
	
	private IRootSearchConfig engineConfiguration;
	
	private OpeningBook openingBook;
	
	private List<ITTable> ttable_provider;
	private List<IEvalCache> evalCache;
	private List<IEvalCache> syzygyDTZCache;
	
	private IChannel channel;
	
	
	public MemoryConsumers(IChannel _channel, IRootSearchConfig _engineConfiguration, boolean ownBookEnabled) {
		
		channel = _channel;
		
		engineConfiguration = _engineConfiguration;
		
		if (ChannelManager.getChannel() != null) ChannelManager.getChannel().dump("OS arch: " + getJVMBitmode() + " bits");
		
		/**
		 * The memory usage percent is very important because of 2 main reason:
		 * 1. Java VM runs GC (Garbage Collector) for less or more time (including "Stop the world" GC)
		 *   depending on the amount of used memory from the java process.
		 * 2. ELO strength is affected from the size of TPT, because of the used search algorithm MTD(f),
		 *    which visits same positions multiple times.
		 * The general rule is: the size of TPT should be enough for the engine to think one whole move without being filled on 100%
		 * In short time controls (fast games like 1/1) the size could be small and in long controls (long games 40/40) should be bigger.
		 * The selection bellow is optimized for long games.
		 */
		
		//Set only if not set statically
		//if (MEMORY_USAGE_PERCENT == 0) {
			//0.29 for short games (e.g. 1/1), 0.69 for long games (e.g. 40/40)
			double memoryUsagePercent = engineConfiguration.get_MEMORY_USAGE_PERCENT(); //0.90;//(engineConfiguration.getTimeControlOptimizationType() == IRootSearchConfig.TIME_CONTROL_OPTIMIZATION_TYPE_40_40) ? 0.69 : 0.29;
			set_MEMORY_USAGE_PERCENT(memoryUsagePercent);
		//}
		
		
		//ChannelManager.getChannel().dump(new Exception());
		
		//if (getAvailableMemory() / (1024 * 1024) < 63 - (JVMDLL_MEMORY_CONSUMPTION / (1024 * 1024))) {
		//	throw new IllegalStateException("Not enough memory. The engine needs from at least 64MB to run. Please increase the -Xmx option of Java VM");
		//}
		
		
		if (ChannelManager.getChannel() != null) ChannelManager.getChannel().dump("Defined memory usage percent " + (MEMORY_USAGE_PERCENT * 100) + "%");
		if (ChannelManager.getChannel() != null) ChannelManager.getChannel().dump("Memory the Engine will use " + (getAvailableMemoryInBytes() / (1024 * 1024)) + "MB");
		
		if (ChannelManager.getChannel() != null) ChannelManager.getChannel().dump("Initializing Memory Consumers ...");
		
		//ChannelManager.getChannel().dump("SEE Metadata ... ");
		//seeMetadata = SeeMetadata.getSingleton();
		//ChannelManager.getChannel().dump("SEE Metadata OK => " + (lastAvailable_in_MB - ((getAvailableMemory()) / (1024 * 1024))) + "MB");
		
		
		if (ChannelManager.getChannel() != null) ChannelManager.getChannel().dump("Openning Book enabled: " + ownBookEnabled);
		//if (ownBookEnabled) {

			if (ChannelManager.getChannel() != null) ChannelManager.getChannel().dump("Openning Book ... ");
			
			if (OpeningBookFactory.getBook() == null) {
				if (ChannelManager.getChannel() != null) ChannelManager.getChannel().dump("No openning book");
			} else {
				try {
					openingBook = OpeningBookFactory.getBook();
					if (ChannelManager.getChannel() != null) ChannelManager.getChannel().dump("Openning Book OK.");
				} catch(Exception e) {
					if (ChannelManager.getChannel() != null) ChannelManager.getChannel().dump("Unable to load Openning Book. Error is:");
					channel.dump(e);
				}
			}
		//}
		
		
		if (ChannelManager.getChannel() != null) ChannelManager.getChannel().dump("Loading modules for Endgame Tablebases support ... ");
		
		//if (engineConfiguration.getThreadsCount() > 1) {
			
			//If there are many threads than switch off syzygy calls, because of crashes:
			
			/*230018 <Bagatur 3.1(0): # A fatal error has been detected by the Java Runtime Environment:
				230018 <Bagatur 3.1(0): #
				230018 <Bagatur 3.1(0): #  SIGSEGV (0xb) at pc=0x00007fcea1f29513, pid=102795, tid=0x00007fce8b7f7700
				230018 <Bagatur 3.1(0): #
				230018 <Bagatur 3.1(0): # JRE version: OpenJDK Runtime Environment (8.0_312-b07) (build 1.8.0_312-b07)
				230018 <Bagatur 3.1(0): # Java VM: OpenJDK 64-Bit Server VM (25.312-b07 mixed mode linux-amd64 )
				230018 <Bagatur 3.1(0): # Problematic frame:
				230018 <Bagatur 3.1(0): # C  [libJSyzygy.so+0x6513]  probe_dtz+0x403*/
			
			//SyzygyTBProbing.disableSingleton();
		//}
		
		if (SyzygyTBProbing.getSingleton() != null) {
			
			if (engineConfiguration.getTbPath() != null) {
			
				File TB_dir = new File(engineConfiguration.getTbPath());
				
				if (TB_dir.exists()) {
					
					SyzygyTBProbing.getSingleton().load(engineConfiguration.getTbPath());
					
					if (ChannelManager.getChannel() != null) ChannelManager.getChannel().dump("Modules for Endgame Tablebases OK. Will try to load Tablebases from => " + engineConfiguration.getTbPath());
					
				} else {
					
					if (ChannelManager.getChannel() != null) ChannelManager.getChannel().dump("Modules for Endgame Tablebases cannot be loaded. Directory does not exists => " + TB_dir.getAbsolutePath());
				}
			} else {
				
				if (ChannelManager.getChannel() != null) ChannelManager.getChannel().dump("Modules for Endgame Tablebases cannot be loaded. Directory with Syzygy TB files is not set");
			}


		} else {
			
			//TODO: set memory usage percent of TBs to 0 and don't create EGTB/DTZ cache at all.
		}
		
		
		if (engineConfiguration.initCaches()) {
			
			if (ChannelManager.getChannel() != null) ChannelManager.getChannel().dump("Caches (Transposition Table, Eval Cache and Pawns Eval Cache) ...");
			if (ChannelManager.getChannel() != null) ChannelManager.getChannel().dump("Transposition Table usage percent from the free memory " + (100 * engineConfiguration.getTPTUsagePercent()) + "%");
			if (ChannelManager.getChannel() != null) ChannelManager.getChannel().dump("Eval Cache usage percent from the free memory " + (100 * engineConfiguration.getEvalCacheUsagePercent()) + "%");
			if (ChannelManager.getChannel() != null) ChannelManager.getChannel().dump("Syzygy DTZ Cache usage percent from the free memory " + (100 * MEM_USAGE_SYZYGY_DTZ_CACHE) + "%");			
			
			
			double percents_sum = engineConfiguration.getTPTUsagePercent()
								+ engineConfiguration.getEvalCacheUsagePercent()
								//+ engineConfiguration.getSyzygyDTZUsagePercent()
								+ engineConfiguration.getPawnsCacheUsagePercent();
			
			if (percents_sum <= 0 || percents_sum > 1) {
				throw new IllegalStateException("Memory split percents sum is incorrect: " + percents_sum + ". It should be between 0 and 1");
			}
			
			long static_memory_in_bytes = STATIC_JVM_MEMORY_IN_MEGABYTES * 1024 * 1024;
			if (ChannelManager.getChannel() != null) ChannelManager.getChannel().dump("Excluded memory for static structures is " + STATIC_JVM_MEMORY_IN_MEGABYTES + " MB");
			
			long availableMemoryInBytes = getAvailableMemoryInBytes() - static_memory_in_bytes;
			
			initCaches(availableMemoryInBytes);
		}
		
		if (ChannelManager.getChannel() != null) ChannelManager.getChannel().dump("Memory Consumers are initialized.");
	}
	
	
	private void initCaches(long availableMemoryInBytes) {
		
		
		if (ChannelManager.getChannel() != null) ChannelManager.getChannel().dump("Initializing caches inside "
				+ (int) (
							(engineConfiguration.getTPTUsagePercent() + engineConfiguration.getEvalCacheUsagePercent() + engineConfiguration.getPawnsCacheUsagePercent())
							* availableMemoryInBytes
							/ (1024 * 1024)
						) + "MB");
		
		
		int THREADS_COUNT 				= engineConfiguration.getThreadsCount();
		
		if (ChannelManager.getChannel() != null) {
			
			ChannelManager.getChannel().dump("Threads are " + THREADS_COUNT);
			
			ChannelManager.getChannel().dump("engineConfiguration.getTPTsCount()=" + engineConfiguration.getTPTsCount());
			
			ChannelManager.getChannel().dump("engineConfiguration.useTPT()=" + engineConfiguration.useTPT());
			
			ChannelManager.getChannel().dump("engineConfiguration.useEvalCache()=" + engineConfiguration.useEvalCache());
			
			ChannelManager.getChannel().dump("engineConfiguration.useSyzygyDTZCache()=" + engineConfiguration.useSyzygyDTZCache());
		}
		
		
		/**
		 * Initialize caches
		 */
		long size_tpt 			= Math.max(SIZE_MIN_ENTRIES_TPT, (long) ((engineConfiguration.getTPTUsagePercent() * availableMemoryInBytes)));
		
		long size_ec 			= Math.max(SIZE_MIN_ENTRIES_EC, (long) ((engineConfiguration.getEvalCacheUsagePercent() * availableMemoryInBytes) / THREADS_COUNT));
		
		long syzygy_ec 			= Math.max(SIZE_MIN_ENTRIES_EC, (long) ((MEM_USAGE_SYZYGY_DTZ_CACHE * availableMemoryInBytes) / THREADS_COUNT));
		
		
		//Create and use TT carefully as SMP version doesn't scale, because of many reads/writes in the arrays
		//We create more tables in order to minimize reads/writes in the same arrays, as this is an issue for Java and performance of the SMP search goes down up to 10 times.
		List<ITTable> global_ttables 	= new ArrayList<ITTable>();
		
		if (engineConfiguration.useTPT()) {
			
			int count_TTs = engineConfiguration.getTPTsCount();
			
			if (count_TTs < 1) {
				
				throw new IllegalStateException("Transposition Tables count is less than 1");
			}
			
			for (int i = 0; i < count_TTs; i++) {
				
				global_ttables.add(new TTable_Impl2(size_tpt / count_TTs));
			}
		}
		
		ttable_provider 		= new Vector<ITTable>();
		
		evalCache 				= new Vector<IEvalCache>();
		
		syzygyDTZCache  		= new Vector<IEvalCache>();
		
		
		for (int i = 0; i < THREADS_COUNT; i++) {
			
			if (engineConfiguration.useTPT()) {
				
				ttable_provider.add(global_ttables.get(i %  global_ttables.size()));
				
			} else {
				
				ttable_provider.add(null);
			}
			
			evalCache.add(engineConfiguration.useEvalCache() ? new EvalCache_Impl2(size_ec) : null);
			
			syzygyDTZCache.add(engineConfiguration.useSyzygyDTZCache() ? new EvalCache_Impl2(syzygy_ec) : null);
		}		
	}
	
	
	public OpeningBook getOpeningBook() {
		
		return openingBook;
	}
	
	
	public List<ITTable> getTPTProvider() {
		
		return ttable_provider;
	}
	
	
	public List<IEvalCache> getEvalCache() {
		
		return evalCache;
	}

	
	public List<IEvalCache> getSyzygyDTZCache() {
		
		return syzygyDTZCache;
	}
	
	
	private long getAvailableMemoryInBytes() {
		
		System.gc();
		
		return (long) (MEMORY_USAGE_PERCENT * Runtime.getRuntime().maxMemory());
	}
	
	
	private static int getJVMBitmode() {
		
	    String vendorKeys [] = {
		        "sun.arch.data.model",
		        "com.ibm.vm.bitmode",
		        "os.arch",
		};
	    
        for (String key : vendorKeys ) {
            String property = System.getProperty(key);
            if (property != null) {
                int code = (property.indexOf("64") >= 0) ? 64 : 32;
                return code;
            }
        }
        
        return 32;
	}
	
	
	public void clear() {
		
		if (ttable_provider != null) ttable_provider.clear();
		
		if (evalCache != null) evalCache.clear();
		
		if (syzygyDTZCache != null) syzygyDTZCache.clear(); 
	}
}
