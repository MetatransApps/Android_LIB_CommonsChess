package bagaturchess.egtb.syzygy;


import bagaturchess.bitboard.api.IBitBoard;


public class SyzygyTBProbing {
	
	
	private static boolean loadingInitiated;
	
	
    private SyzygyTBProbing() {
    	loadingInitiated = false;
    }
	
	
    public static final SyzygyTBProbing getSingleton() {
        return null;
    }


    public final void load(String path) {
        throw new UnsupportedOperationException("Syzygy TB probing is not supported.");
    }


    public boolean isAvailable(int piecesLeft){
        throw new UnsupportedOperationException("Syzygy TB probing is not supported.");
    }
    

    public synchronized int probeDTZ(IBitBoard board){
        throw new UnsupportedOperationException("Syzygy TB probing is not supported.");
    }


    public int toMove(int result){
        throw new UnsupportedOperationException("Syzygy TB probing is not supported.");
    }


    public int getMove(int fromSquare, int toSquare, int promotes) {
        return fromSquare | (toSquare <<6) | (promotes << 12);
    }


    public synchronized int getWDLScore(int wdl, int depth) {
        throw new UnsupportedOperationException("Syzygy TB probing is not supported.");
    }
}