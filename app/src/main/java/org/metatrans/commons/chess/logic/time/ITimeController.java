package org.metatrans.commons.chess.logic.time;


public interface ITimeController {
	public void resume(int colour);
	public void pause(int colour);
	public void pauseAll();
	public void destroy();
	public long getData_white();
	public long getData_black();
	public void setData(long white, long black);
}
