package org.metatrans.commons.chess.views_and_controllers;


import android.graphics.Canvas;
import android.view.View;


public interface IPanelsVisualization {

	void redraw();
	
	void setCapturedPieces(int[] _captured_w, int[] _captured_b, int _captured_w_size, int _captured_b_size);
	
	void lock();

	void unlock();

	boolean isLocked();

	boolean areMovesNavigationButtonsLocked();

	View.OnTouchListener createOnTouchListener(IBoardVisualization boardVisualization, IBoardViewActivity activity);

	void setOnTouchListener(View.OnTouchListener panelsListener);

    void init(String playerName_white, String playerName_black, boolean autotop, boolean autobottom, boolean info_enabled);

	void draw(Canvas canvas);
}
