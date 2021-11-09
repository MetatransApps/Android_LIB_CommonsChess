package org.metatrans.commons.chess.views_and_controllers;


import android.view.View;

import java.util.Set;

import org.metatrans.commons.chess.model.FieldSelection;
import org.metatrans.commons.chess.model.Move;


public interface IBoardVisualization {
	
	public int getLetter(float x);
	public int getDigit(float y);
	
	public void setData(int[][] pieces);
	
	public void redraw();

	public void addSelection(int letter, int digit, FieldSelection selection);
	
	public void clearSelections();
	public void clearSelections(int letter, int digit);
	
	public void invalidSelection_Temp_Square(int letter, int digit);
	public void invalidSelection_Permanent_Square(int letter, int digit);
	public void invalidSelection_Permanent_Border(int letter, int digit);
	
	public void validSelection_Temp(int letter, int digit);
	public void validSelection_Temp_Square(int letter, int digit);
	public void validSelection_Permanent(int letter, int digit);
	public void validSelection_Permanent_Square(int letter, int digit);
	public void validSelection_Permanent_Border(int letter, int digit);
	public void markingSelection_Permanent_Square(int letter, int digit);
	public void markingSelection_Permanent_Border(int letter, int digit);

	public void makeMovingPiece_OnInvalidSquare();
	public void makeMovingPieceSelections();
	public void makeMovablePiecesSelections();
	public Set<FieldSelection>[][] getSelections();
	public void setSelections(Set<FieldSelection>[][] selections);
	
	public void whiteWinsSelection();
	public void blackWinsSelection();
	
	public void setTopMessageText(String text);
	
	public void lock();
	public void unlock();
	public boolean isLocked();


	void startMoveAnimation(final Move move);

	void endMoveAnimation();

	int hasAnimation();

	View.OnTouchListener createOnTouchListener(IBoardVisualization boardVisualization, IBoardViewActivity activity);

    void setOnTouchListener(View.OnTouchListener listener);

	float getSquareSize();

	boolean isOverBoard(float x, float y);

	boolean isOverBottomReplay(float x, float y);

	boolean isOverLeaderBoards(float x, float y);

	View.OnTouchListener getLeaderboard();

	void selectButtonReplay();

	void deselectButtonReplay();
}
