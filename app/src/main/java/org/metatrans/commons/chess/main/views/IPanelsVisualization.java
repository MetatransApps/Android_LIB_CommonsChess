package org.metatrans.commons.chess.main.views;


public interface IPanelsVisualization {
	
	public void redraw();
	
	//public ClockArea getWhiteClockArea();
	//public ClockArea getBlackClockArea();
	
	public void setCapturedPieces(int[] _captured_w, int[] _captured_b, int _captured_w_size, int _captured_b_size);
	
	public boolean isOverButtonMenu(float x, float y);
	public boolean isOverButtonUnmove(float x, float y);
	public boolean isOverButtonAutoTop(float x, float y);
	public boolean isOverButtonInfo(float x, float y);
	public boolean isOverButtonHelpTop(float x, float y);
	public boolean isOverButtonHelpBottom(float x, float y);
	public boolean isOverTopArea(float x, float y);
	public boolean isOverBottomArea(float x, float y);
	
	public void selectButtonUnmove();
	public void deselectButtonUnmove();
	public boolean isActiveButtonUnmove();
	
	public void selectButtonMenu();
	public void deselectButtonMenu();
	public boolean isActiveButtonMenu();
	
	public void selectButtonHelpTop();
	public void deselectButtonHelpTop();

	public void selectButtonHelpBottom();
	public void deselectButtonHelpBottom();

	public void selectButtonAutoTop();
	public void deselectButtonAutoTop();
	//public void finishButtonAutoTop();
	//public boolean isActiveButtonAutoTop();
	
	public void selectButton_Info();
	public void deselectButton_Info();
	public boolean isActiveButtonInfo();
	public void finishButtonInfo();
	//public boolean isActiveButtonAutoBottom();
	
	public void selectButtonTopPlayer();
	public void deselectButtonTopPlayer();
	public void finishButtonTopPlayer();
	public boolean isActiveButtonTopPlayer();

	public void selectButtonBottomPlayer();
	public void deselectButtonBottomPlayer();
	public void finishButtonBottomPlayer();
	public boolean isActiveButtonBottomPlayer();
	
	public void setWhitePlayerName(String playerNameWhite);
	public void setBlackPlayerName(String playerNameBlack);
	
	public void lock();
	public void unlock();
	public boolean isLocked();
}
