package org.metatrans.commons.chess.main.views;


import java.util.Set;

import org.metatrans.commons.chess.model.FieldSelection;
import org.metatrans.commons.chess.model.Move;
import org.metatrans.commons.chess.model.MovingPiece;




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
	public void startMoveAnimation(final Move move);
	public void endMoveAnimation();

	public int hasAnimation();

	public void invalidate();
}
