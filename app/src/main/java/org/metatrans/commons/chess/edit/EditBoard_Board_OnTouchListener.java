package org.metatrans.commons.chess.edit;


import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.chess.R;
import org.metatrans.commons.chess.app.Application_Chess_BaseImpl;
import org.metatrans.commons.chess.model.EditBoardData;


public class EditBoard_Board_OnTouchListener implements OnTouchListener {
	
	
	private BoardView boardView;
	private EditBoardChangeHandler handler;
	
	
	public EditBoard_Board_OnTouchListener(BoardView _boardView, EditBoardChangeHandler _handler) {
		boardView = _boardView;
		handler = _handler;
	}
	
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		throw new UnsupportedOperationException();
	}
	
	
	void processEvent_DOWN(MotionEvent event) {
		
		float x = event.getX();
		float y = event.getY();
		
		EditBoardData editBoardData = ((Application_Chess_BaseImpl)Application_Base.getInstance()).getEditBoardData();
		
		if (boardView.rectangleBoard.contains(x, y)) {

			handler.handleBoardChange(editBoardData, boardView.getLetter(x), boardView.getDigit(y));
			
		}
		
		((Application_Chess_BaseImpl)Application_Base.getInstance()).storeEditBoardData(editBoardData);
	}


	void processEvent_MOVE(MotionEvent event) {
		//Do nothing
	}


	void processEvent_UP(MotionEvent event) {
		//Do nothing
	}
}
