package org.metatrans.commons.chess.edit;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import org.metatrans.commons.chess.views_and_controllers.IBoardViewActivity;
import org.metatrans.commons.ui.utils.DrawingUtils;


public class EditBoardView extends View {
	
	
	public RectF rectf_main;
	public RectF rectf_board;
	public RectF rectf_panel_top0;
	public RectF rectf_panel_top;
	public RectF rectf_panel_left;
	public RectF rectf_panel_right;
	public RectF rectf_panel_bottom;
	public RectF rectf_panel_bottom2;
	
	private BoardView boardView;
	private EditBoardPanelsView panelsView;
	
	private Paint paint;
	
	
	public EditBoardView(Context context, View parent) {
		
		super(context);
		
		rectf_main 			= new RectF();
		rectf_board 		= new RectF();
		rectf_panel_top0	= new RectF();
		rectf_panel_top		= new RectF();
		rectf_panel_left	= new RectF();
		rectf_panel_right	= new RectF();
		rectf_panel_bottom	= new RectF();
		rectf_panel_bottom2 = new RectF();
		
		boardView = createBoardView();
		
		panelsView = new EditBoardPanelsView(context, this, rectf_panel_top0, rectf_panel_top, rectf_panel_left, rectf_panel_right, rectf_panel_bottom, rectf_panel_bottom2);
		
		paint = new Paint();
	}
	
	
	protected BoardView createBoardView() {
		return new BoardView(getContext(), this, rectf_board);
	}
	
	
	public BoardView getBoardView() {
		return boardView;
	}
	
	public EditBoardPanelsView getPanelsView() {
		return panelsView;
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		initializeDimensions();
			
		boardView.init();
		panelsView.init();
		
		this.setMeasuredDimension( (int) (rectf_main.right - rectf_main.left), (int) (rectf_main.bottom - rectf_main.top) );
	}
	
	
	private void initializeDimensions() {
		
		int main_width = getMeasuredWidth();
		int main_height = getMeasuredHeight();
		
		if (main_width > main_height) {
			
			throw new IllegalStateException();
			
		} else {
			
			float panelsBorder = 10;
			
			float board_dimension = 6 * main_width / 8;
			float topAndBottomPanels_height = (main_height - main_width) / 4 - 2 * panelsBorder;
			float top0_height = main_height - 3 * topAndBottomPanels_height - board_dimension - 6 * panelsBorder;
			
			rectf_main.left = 0;
			rectf_main.top = 0;
			rectf_main.right = main_width;
			rectf_main.bottom = main_height;
			
			rectf_panel_top0.top = 0 + panelsBorder;
			rectf_panel_top0.left = 0 + panelsBorder;
			rectf_panel_top0.right = rectf_main.right - panelsBorder;
			rectf_panel_top0.bottom = rectf_panel_top0.top + top0_height;
			
			rectf_panel_top.top = rectf_panel_top0.bottom + panelsBorder;
			rectf_panel_top.left = 0 + panelsBorder;
			rectf_panel_top.right = rectf_main.right - panelsBorder;
			rectf_panel_top.bottom = rectf_panel_top.top + topAndBottomPanels_height;
			
			rectf_board.left = 1 * main_width / 8;
			rectf_board.top = rectf_panel_top.bottom + panelsBorder;
			rectf_board.right = rectf_board.left + board_dimension;
			rectf_board.bottom = rectf_board.top + board_dimension;
			
			rectf_panel_bottom.top = rectf_board.bottom + panelsBorder;
			rectf_panel_bottom.left = 0 + panelsBorder;
			rectf_panel_bottom.right = rectf_main.right - panelsBorder;
			rectf_panel_bottom.bottom = rectf_panel_bottom.top + topAndBottomPanels_height;
			
			rectf_panel_bottom2.top = rectf_panel_bottom.bottom + panelsBorder;
			rectf_panel_bottom2.left = 0 + panelsBorder;
			rectf_panel_bottom2.right = rectf_main.right - panelsBorder;
			rectf_panel_bottom2.bottom = rectf_panel_bottom2.top + topAndBottomPanels_height;
			
			rectf_panel_left.top = rectf_board.top;
			rectf_panel_left.left = 0 + panelsBorder;
			rectf_panel_left.right = rectf_board.left - panelsBorder;
			rectf_panel_left.bottom = rectf_board.bottom;
			
			rectf_panel_right.top = rectf_board.top;
			rectf_panel_right.left = rectf_board.right + panelsBorder;
			rectf_panel_right.right = rectf_main.right - panelsBorder;
			rectf_panel_right.bottom = rectf_board.bottom;
		}
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		super.onDraw(canvas);
		
		paint.setColor(((IBoardViewActivity) getContext()).getUIConfiguration().getColoursConfiguration().getColour_Background());
		canvas.drawRect(rectf_main, paint);

		paint.setColor(((IBoardViewActivity) getContext()).getUIConfiguration().getColoursConfiguration().getColour_Delimiter());
		DrawingUtils.drawRoundRectangle(canvas, paint, rectf_main, 20);
		
		panelsView.draw(canvas);
		
		boardView.draw(canvas);
	}
}
