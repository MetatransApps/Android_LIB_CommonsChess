package org.metatrans.commons.chess.edit;


import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;


public class EditBoard_OnTouchListener implements OnTouchListener{
	
	
	private EditBoardView editBoard;
	
	private EditBoard_Panels_OnTouchListener panelsListener;
	private EditBoard_Board_OnTouchListener boardListener;
	
	
	public EditBoard_OnTouchListener(EditBoardView _editBoard) {
		
		editBoard = _editBoard;
		
		EditBoardChangeHandler handler = new EditBoardChangeHandler(editBoard.getBoardView(), editBoard.getPanelsView());
		
		panelsListener = new EditBoard_Panels_OnTouchListener(editBoard, editBoard.getPanelsView(), handler);
		
		boardListener = new EditBoard_Board_OnTouchListener(editBoard.getBoardView(), handler);
	}
	
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		if (event != null) {
			
			int action = event.getAction();
			
			if (action == MotionEvent.ACTION_DOWN) {

				processEvent_DOWN(event);

			} else if (action == MotionEvent.ACTION_MOVE) {

				processEvent_MOVE(event);
				
			} else if (action == MotionEvent.ACTION_UP
					|| action == MotionEvent.ACTION_CANCEL) {

				processEvent_UP(event);

			}
		}
		
		return true;
	}


	private void processEvent_DOWN(MotionEvent event) {
		
		float x = event.getX();
		float y = event.getY();
		
		if (editBoard.rectf_panel_top.contains(x, y)
				|| editBoard.rectf_panel_left.contains(x, y)
				|| editBoard.rectf_panel_right.contains(x, y)
				|| editBoard.rectf_panel_bottom.contains(x, y)
				|| editBoard.rectf_panel_bottom2.contains(x, y)) {
			
			panelsListener.processEvent_DOWN(event);
			
		} else if (editBoard.rectf_board.contains(x, y)) {
			
			boardListener.processEvent_DOWN(event);
			
		}
	}


	private void processEvent_MOVE(MotionEvent event) {
		
		float x = event.getX();
		float y = event.getY();
		
		if (editBoard.rectf_panel_top.contains(x, y)
				|| editBoard.rectf_panel_left.contains(x, y)
				|| editBoard.rectf_panel_right.contains(x, y)
				|| editBoard.rectf_panel_bottom.contains(x, y)
				|| editBoard.rectf_panel_bottom2.contains(x, y)) {
			
			panelsListener.processEvent_MOVE(event);
			
		} else if (editBoard.rectf_board.contains(x, y)) {
			
			boardListener.processEvent_MOVE(event);
			
		}
	}


	private void processEvent_UP(MotionEvent event) {
		
		float x = event.getX();
		float y = event.getY();
		
		if (editBoard.rectf_panel_top.contains(x, y)
				|| editBoard.rectf_panel_left.contains(x, y)
				|| editBoard.rectf_panel_right.contains(x, y)
				|| editBoard.rectf_panel_bottom.contains(x, y)
				|| editBoard.rectf_panel_bottom2.contains(x, y)) {
			
			panelsListener.processEvent_UP(event);
			
		} else if (editBoard.rectf_board.contains(x, y)) {
			
			boardListener.processEvent_UP(event);
			
		}
	}
}
