package org.metatrans.commons.chess.views_and_controllers;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.cfg.colours.ConfigurationUtils_Colours;
import org.metatrans.commons.cfg.colours.IConfigurationColours;
import org.metatrans.commons.chess.GlobalConstants;
import org.metatrans.commons.chess.main.MainActivity;
import org.metatrans.commons.ui.utils.DrawingUtils;


public abstract class MainView extends View implements IMainView {


	protected static final float USAGE_PERCENT_BOARD 			= 0.7F;


	protected RectF rectf_main;

	protected RectF rectf_toppanel;
	protected RectF rectf_bottompanel0;
	protected RectF rectf_bottompanel2;

	protected float panel_height;
	protected float panel_border;

	protected RectF rectf_banner;
	protected RectF rectf_board;


	protected BoardView boardView;
	protected IPanelsVisualization panelsView;

	protected Paint paint;


	public MainView(Context context, AttributeSet attrs) {
		
		super(context, attrs);

		rectf_main 			= new RectF();
		rectf_banner 		= new RectF();
		rectf_toppanel 		= new RectF();
		rectf_board 		= new RectF();

		rectf_bottompanel0 = new RectF();
		rectf_bottompanel2	= new RectF();
		
		boardView = createBoardView(rectf_board);
		
		panelsView = createPanelsView(rectf_toppanel, null, rectf_bottompanel0, null, rectf_bottompanel2);

		paint = new Paint();
	}
	
	
	protected BoardView createBoardView(RectF rectangle) {
		return new BoardView(getContext(), this, rectangle);
	}


	protected abstract IPanelsVisualization createPanelsView(RectF rectf_toppanel, RectF rectf_toppanel1, RectF rectf_bottompanel0, RectF rectf_bottompanel1, RectF rectf_bottompanel2);


	protected MainActivity getMainActivity() {
		return (MainActivity) getContext();
	}
	
	
	public IBoardVisualization getBoardView() {
		return boardView;
	}
	
	
	public IPanelsVisualization getPanelsView() {
		return panelsView;
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		initializeDimensions();

		boardView.init();

		panelsView.init(getMainActivity().getBoardManager().getPlayerWhite().getName(), getMainActivity().getBoardManager().getPlayerBlack().getName(),
				getMainActivity().getUserSettings().playerTypeBlack == GlobalConstants.PLAYER_TYPE_COMPUTER,
				getMainActivity().getUserSettings().playerTypeWhite == GlobalConstants.PLAYER_TYPE_COMPUTER,
				getMainActivity().getUserSettings().infoEnabled
				);
		
		//this.setMeasuredDimension( getMeasuredWidth(), (int) (rectf_main.bottom - rectf_main.top) );
		this.setMeasuredDimension( getMeasuredWidth(), getMeasuredHeight());

		setOnTouchListener(new OnTouchListener_Main(getMainActivity(), boardView, panelsView));
	}
	
	
	private void initializeDimensions() {
		
		//int[] screen_size = ScreenUtils.getScreenSize(getMainActivity());
		int main_width = getMeasuredWidth();
		int main_height = getMeasuredHeight();

		panel_border = 10;
		
		if (main_width > main_height) {

			panel_border = panel_border / 2;

			float board_dimension = main_height * USAGE_PERCENT_BOARD;
			panel_height = main_height * getUsagePercentPanel() - panel_border;

			rectf_main.left = 0;
			rectf_main.right = board_dimension;
			rectf_main.top = 0;
			rectf_main.bottom = main_height;

			rectf_toppanel.left = rectf_main.left;
			rectf_toppanel.right = board_dimension;
			rectf_toppanel.top = rectf_main.top;
			rectf_toppanel.bottom = rectf_toppanel.top + panel_height;

			rectf_board.left = rectf_main.left;
			rectf_board.right = board_dimension;
			rectf_board.top = rectf_toppanel.bottom + panel_border;
			rectf_board.bottom = rectf_board.top + board_dimension;

			rectf_bottompanel0.left = rectf_main.left;
			rectf_bottompanel0.right = board_dimension;
			rectf_bottompanel0.top = rectf_board.bottom + panel_border;
			rectf_bottompanel0.bottom = rectf_bottompanel0.top + panel_height;

			initInfoAndCustomPanel(board_dimension, rectf_main.left);

		} else {
			
			float board_dimension = main_width * 1;
			panel_height = (main_height - main_width) / 5.28f - panel_border;
			adjustPanelHeight();

			float sections_count = 9.1f;
			int section_height = (int) (main_height / sections_count);

			rectf_main.left = 0;
			rectf_main.top = 0;
			rectf_main.right = board_dimension;
			rectf_main.bottom = main_height;

			rectf_banner.left = 0;
			rectf_banner.right = main_width - 0;
			rectf_banner.top = rectf_main.top;
			rectf_banner.bottom = rectf_banner.top + section_height;

			rectf_toppanel.left = 0;
			rectf_toppanel.right = board_dimension - 0;
			rectf_toppanel.top = rectf_banner.bottom;
			rectf_toppanel.bottom = rectf_toppanel.top + panel_height;

			rectf_board.left = 0;
			rectf_board.right = board_dimension - 0;
			rectf_board.top = rectf_toppanel.bottom + panel_border;
			rectf_board.bottom = rectf_board.top + board_dimension;

			rectf_bottompanel0.left = 0;
			rectf_bottompanel0.right = board_dimension - 0;
			rectf_bottompanel0.top = rectf_board.bottom + panel_border;
			rectf_bottompanel0.bottom = rectf_bottompanel0.top + panel_height;

			initInfoAndCustomPanel(board_dimension, rectf_main.left);
		}
	}


	protected float getUsagePercentPanel() {

		return 0.1F;
	}


	protected void adjustPanelHeight() {

		panel_height = (4f * panel_height * 1.015f) / 3f ;
	}


	protected void initInfoAndCustomPanel(float board_dimension, float left) {

		rectf_bottompanel2.left = left;
		rectf_bottompanel2.right = board_dimension;
		rectf_bottompanel2.top = rectf_bottompanel0.bottom + panel_border;
		rectf_bottompanel2.bottom = rectf_bottompanel2.top + panel_height;
	}


	@Override
	protected void onDraw(Canvas canvas) {
		
		super.onDraw(canvas);

		IConfigurationColours coloursCfg = ConfigurationUtils_Colours.getConfigByID(
				Application_Base.getInstance().getUserSettings().uiColoursID
		);

		paint.setColor(coloursCfg.getColour_Background());
		//paint.setColor(Color.BLUE);
		DrawingUtils.drawRoundRectangle(canvas, paint, rectf_main);

		/*if (rectf_bottompanel1 != null) {
			paint.setColor(Color.RED);
			DrawingUtils.drawRoundRectangle(canvas, paint, rectf_bottompanel1);
		}*/

		panelsView.draw(canvas);
		
		boardView.draw(canvas);
	}
}
