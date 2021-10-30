package org.metatrans.commons.chess.main.views;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
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

import com.chessartforkids.model.UserSettings;


public class MainView extends View {
	
	
	private static final float USAGE_PERCENT_BOARD 			= 0.7F;
	private static float USAGE_PERCENT_PANEL 				= 0.1F;

	
	private RectF rectf_main;

	private RectF rectf_banner;
	private RectF rectf_toppanel;
	private RectF rectf_board;
	private RectF rectf_bottompanel;
	private RectF rectf_bottompanel2;
	
	private BoardView boardView;
	private PanelsView panelsView;

	private Paint paint;


	public MainView(Context context, AttributeSet attrs) {
		
		super(context, attrs);
		
		rectf_main 			= new RectF();
		rectf_banner 		= new RectF();
		rectf_toppanel 		= new RectF();
		rectf_board 		= new RectF();
		rectf_bottompanel 	= new RectF();
		rectf_bottompanel2	= new RectF();
		
		boardView = createBoardView(rectf_board);
		
		panelsView = new PanelsView(getContext(), this,
				rectf_toppanel,
				rectf_bottompanel,
				rectf_bottompanel2
				);

		paint = new Paint();
	}
	
	
	protected BoardView createBoardView(RectF rectangle) {
		return new BoardView(getContext(), this, rectangle);
	}
	
	
	protected MainActivity getMainActivity() {
		return (MainActivity) getContext();
	}
	
	
	public BoardView getBoardView() {
		return boardView;
	}
	
	
	public PanelsView getPanelsView() {
		return panelsView;
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			
		initializeDimensions();

		boardView.init();

		panelsView.init(getMainActivity().getBoardManager().getPlayerWhite().getName(getMainActivity()), getMainActivity().getBoardManager().getPlayerBlack().getName(getMainActivity()),
				getMainActivity().getUserSettings().playerTypeBlack == GlobalConstants.PLAYER_TYPE_COMPUTER,
				getMainActivity().getUserSettings().playerTypeWhite == GlobalConstants.PLAYER_TYPE_COMPUTER,
				getMainActivity().getUserSettings().infoEnabled
				);
		
		this.setMeasuredDimension( getMeasuredWidth(), (int) (rectf_main.bottom - rectf_main.top) );
	}
	
	
	private void initializeDimensions() {
		
		//int[] screen_size = ScreenUtils.getScreenSize(getMainActivity());
		int main_width = getMeasuredWidth();
		int main_height = getMeasuredHeight();
		
		float dimension = Math.min(main_width, main_height);
		
		float panelBoarder = 15;
		
		if (main_width > main_height) {

			panelBoarder = panelBoarder / 2;

			dimension = main_height;

			float board_dimension = dimension * USAGE_PERCENT_BOARD;
			float panel_height = dimension * USAGE_PERCENT_PANEL - panelBoarder;

			rectf_main.left = 0;
			rectf_main.right = board_dimension;
			rectf_main.top = 0;
			rectf_main.bottom = dimension;

			rectf_toppanel.left = rectf_main.left;
			rectf_toppanel.right = board_dimension;
			rectf_toppanel.top = rectf_main.top;
			rectf_toppanel.bottom = rectf_toppanel.top + panel_height;
			
			rectf_board.left = rectf_main.left;
			rectf_board.right = board_dimension;
			rectf_board.top = rectf_toppanel.bottom + panelBoarder;
			rectf_board.bottom = rectf_board.top + board_dimension;
			
			rectf_bottompanel.left = rectf_main.left;
			rectf_bottompanel.right = board_dimension;
			rectf_bottompanel.top = rectf_board.bottom + panelBoarder;
			rectf_bottompanel.bottom = rectf_bottompanel.top + panel_height;
			
			if (((UserSettings)Application_Base.getInstance().getUserSettings()).infoEnabled) {
				rectf_bottompanel2.left = rectf_main.left;
				rectf_bottompanel2.right = board_dimension;
				rectf_bottompanel2.top = rectf_bottompanel.bottom + panelBoarder;
				rectf_bottompanel2.bottom = rectf_bottompanel2.top + panel_height;
			}
		} else {
			
			dimension = main_width;
			
			float board_dimension = main_width * 1;
			float panel_height = (main_height - main_width) / 4 - panelBoarder;

			float sections_count = 8.9f;
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
			rectf_board.top = rectf_toppanel.bottom + panelBoarder;
			rectf_board.bottom = rectf_board.top + board_dimension;
			
			rectf_bottompanel.left = 0;
			rectf_bottompanel.right = board_dimension - 0;
			rectf_bottompanel.top = rectf_board.bottom + panelBoarder;
			rectf_bottompanel.bottom = rectf_bottompanel.top + panel_height;

			if (((UserSettings)Application_Base.getInstance().getUserSettings()).infoEnabled) {

				rectf_bottompanel2.left = 0;
				rectf_bottompanel2.right = board_dimension - 0;
				rectf_bottompanel2.top = rectf_bottompanel.bottom + panelBoarder;
				rectf_bottompanel2.bottom = rectf_bottompanel2.top + panel_height;

			}
		}
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
		
		panelsView.draw(canvas);
		
		boardView.draw(canvas);
	}
}
