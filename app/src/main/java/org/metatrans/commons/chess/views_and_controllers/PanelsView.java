package org.metatrans.commons.chess.views_and_controllers;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import org.metatrans.commons.TimeUtils;
import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.cfg.colours.IConfigurationColours;
import org.metatrans.commons.chess.GlobalConstants;
import org.metatrans.commons.chess.R;
import org.metatrans.commons.chess.app.Application_Chess_BaseImpl;
import org.metatrans.commons.chess.logic.BoardConstants;
import org.metatrans.commons.chess.logic.game.GameManager;
import org.metatrans.commons.chess.logic.board.IBoardManager;
import org.metatrans.commons.chess.main.MainActivity;
import org.metatrans.commons.chess.model.GameData;
import org.metatrans.commons.chess.model.Move;
import org.metatrans.commons.chess.model.SearchInfo;
import org.metatrans.commons.chess.model.UserSettings;
import org.metatrans.commons.chess.utils.CachesBitmap;
import org.metatrans.commons.ui.ButtonAreaClick;
import org.metatrans.commons.ui.ButtonAreaClick_Image;
import org.metatrans.commons.ui.ButtonAreaSwitch;
import org.metatrans.commons.ui.TextArea;
import org.metatrans.commons.ui.images.IBitmapCache;
import org.metatrans.commons.ui.utils.BitmapUtils;
import org.metatrans.commons.ui.utils.DrawingUtils;

import java.util.List;


/**
 * TODO: This class have to be reworked.
 * It has a lot of redundant methods, which are also exposed in the IPanelsVisualization interface
 * They have to be removed and directly rectangles and buttons have to be used
 * in this class as well as in the inner class OnTouchListener_Panels
 */
public class PanelsView extends BaseView implements IPanelsVisualization, BoardConstants {
	
	
	private static int DELTA_AREAS = 9;
	
	private static boolean AUTO_BUTTON_TOP_ENABLED = true;
	private static boolean AUTO_BUTTON_BOTTOM_ENABLED = true;
	
	private static boolean AREA_BOTTOM2_ENABLED = true;
	
	private int colour_panel;
	
	private RectF rectangle_top;
	private RectF rectangle_bottom;
	public RectF rectangle_bottom1;
	private RectF rectangle_bottom2;
	private float boardSquareSize;
	
	protected int[] captured_w;
	protected int[] captured_b;
	private int captured_w_size;
	private int captured_b_size;
	
	private Paint default_paint;
	
	private RectF rectangle_area_topleft;
	private RectF rectangle_area_topplayer;
	private RectF rectangle_area_topright;
	private RectF rectangle_area_topright2;

	private RectF rectangle_area_bottomleft;
	private RectF rectangle_area_bottomplayer;
	private RectF rectangle_area_bottomright;
	private RectF rectangle_area_bottomright2;

	private RectF rectangle_area_bottom2left;
	private RectF rectangle_area_bottom2right1;
	private RectF rectangle_area_bottom2right2;

	public RectF rectf_play_first;
	public RectF rectf_play_prev;
	public RectF rectf_empty;
	public RectF rectf_play_next;
	public RectF rectf_play_last;


	private ClockArea textarea_topleft;
	private ClockArea textarea_bottomleft;
	private ButtonAreaClick textarea_topleft2;
	private ButtonAreaClick textarea_topright;
	//private ButtonAreaClick textarea_topright2_newgame;
	private ButtonAreaClick_Image textarea_topright2_newgame;
	private ButtonAreaClick textarea_bottomleft2;
	private ButtonAreaClick textarea_bottomright;
	private ButtonAreaSwitch textarea_bottomright2_info;
	
	private ButtonAreaSwitch textarea_topplayername;
	private ButtonAreaSwitch textarea_bottomplayername;
	
	private TextArea textarea_bottom2left;
	private TextArea textarea_bottom2right1;
	private TextArea textarea_bottom2right2;

	public ButtonAreaClick_Image play_first;
	public ButtonAreaClick_Image play_prev;
	//public ButtonAreaSwitch_Image empty_space_for_now;
	public ButtonAreaClick_Image play_next;
	public ButtonAreaClick_Image play_last;

	private Bitmap thinkingBitmap;
	
	
	public PanelsView(Context context, View _parent,  RectF _rectangleTop, RectF _rectangleBottom, RectF _rectangleBottom1,  RectF _rectangleBottom2) {
		
		super(context, _parent);
		
		rectangle_top = _rectangleTop;
		rectangle_bottom = _rectangleBottom;
		rectangle_bottom1 = _rectangleBottom1;
		rectangle_bottom2 = _rectangleBottom2;
		
		captured_w = new int[16];
		captured_b = new int[16];

		default_paint = new Paint();
		
		rectangle_area_topleft = new RectF();
		rectangle_area_bottomleft = new RectF();
		
		rectangle_area_topright = new RectF();
		rectangle_area_topright2 = new RectF();
		rectangle_area_bottomright = new RectF();
		rectangle_area_bottomright2 = new RectF();
		
		rectangle_area_topplayer = new RectF();
		rectangle_area_bottomplayer = new RectF();
		
		rectangle_area_bottom2left = new RectF();
		rectangle_area_bottom2right1 = new RectF();
		rectangle_area_bottom2right2 = new RectF();

		if (rectangle_bottom1 != null) {

			rectf_play_first = new RectF();
			rectf_play_prev = new RectF();
			rectf_empty = new RectF();
			rectf_play_next = new RectF();
			rectf_play_last = new RectF();
		}

		colour_panel = getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_Black();
	}
	
	
	public void init(String playerName_white, String playerName_black, boolean autotop, boolean autobottom, boolean info_enabled) {

		IConfigurationColours coloursCfg = getActivity().getUIConfiguration().getColoursConfiguration();

		int delta = DELTA_AREAS;
		
		boardSquareSize = (rectangle_top.right - rectangle_top.left) / (float) 8;

		rectangle_area_topleft.left = rectangle_top.left + delta;
		rectangle_area_topleft.top = rectangle_top.top + delta;
		rectangle_area_topleft.right = rectangle_top.left + boardSquareSize + boardSquareSize - delta;
		rectangle_area_topleft.bottom = rectangle_top.bottom - delta;
		textarea_topleft = new ClockArea(rectangle_area_topleft, "00:00:00", getActivity().getUIConfiguration().getColoursConfiguration().getColour_Delimiter(), getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_Black());
		
		if (AUTO_BUTTON_TOP_ENABLED) {
			rectangle_area_topright.left = rectangle_top.right - boardSquareSize + delta;
			rectangle_area_topright.top = rectangle_top.top + delta;
			rectangle_area_topright.right = rectangle_top.right - delta;
			rectangle_area_topright.bottom = rectangle_top.bottom - delta;
			textarea_topright = new ButtonAreaClick(rectangle_area_topright, ((Context) getActivity()).getString(R.string.button_menu),
					coloursCfg.getColour_Square_ValidSelection(), coloursCfg.getColour_Square_Black(), coloursCfg.getColour_Square_MarkingSelection());
			
			
			rectangle_area_topright2.left = rectangle_top.right - 2 * boardSquareSize + delta;
			rectangle_area_topright2.top = rectangle_top.top + delta;
			rectangle_area_topright2.right = rectangle_top.right - 1 * boardSquareSize - delta;
			rectangle_area_topright2.bottom = rectangle_top.bottom - delta;
			textarea_topright2_newgame = new ButtonAreaClick_Image(rectangle_area_topright2, BitmapUtils.fromResource((Context) getActivity(), R.drawable.ic_arrows_double),
					coloursCfg.getColour_Square_ValidSelection(), coloursCfg.getColour_Square_MarkingSelection());
		} else {
			rectangle_area_topright.left = rectangle_top.right - 2 * boardSquareSize + delta;
			rectangle_area_topright.top = rectangle_top.top + delta;
			rectangle_area_topright.right = rectangle_top.right - delta;
			rectangle_area_topright.bottom = rectangle_top.bottom - delta;
			textarea_topright = new ButtonAreaClick(rectangle_area_topright, "   " + ((Context) getActivity()).getString(R.string.button_menu) + "   ", getActivity().getUIConfiguration().getColoursConfiguration().getColour_Delimiter(), getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_Black(), getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_ValidSelection());
		}

		rectangle_area_bottomleft.left = rectangle_bottom.left + delta;
		rectangle_area_bottomleft.top = rectangle_bottom.top + delta;
		rectangle_area_bottomleft.right = rectangle_bottom.left + boardSquareSize + boardSquareSize - delta;
		rectangle_area_bottomleft.bottom = rectangle_bottom.bottom - delta;
		textarea_bottomleft = new ClockArea(rectangle_area_bottomleft, "00:00:00", getActivity().getUIConfiguration().getColoursConfiguration().getColour_Delimiter(), getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_Black());
		
		if (AUTO_BUTTON_BOTTOM_ENABLED) {
			rectangle_area_bottomright.left = rectangle_bottom.right - boardSquareSize + delta;
			rectangle_area_bottomright.top = rectangle_bottom.top + delta;
			rectangle_area_bottomright.right = rectangle_bottom.right - delta;
			rectangle_area_bottomright.bottom = rectangle_bottom.bottom - delta;
			textarea_bottomright = new ButtonAreaClick(rectangle_area_bottomright, ((Context) getActivity()).getString(R.string.button_undo),
					coloursCfg.getColour_Square_ValidSelection(), coloursCfg.getColour_Square_Black(), coloursCfg.getColour_Square_MarkingSelection());

			rectangle_area_bottomright2.left = rectangle_bottom.right - 2 * boardSquareSize + delta;
			rectangle_area_bottomright2.top = rectangle_bottom.top + delta;
			rectangle_area_bottomright2.right = rectangle_bottom.right - 1 * boardSquareSize - delta;
			rectangle_area_bottomright2.bottom = rectangle_bottom.bottom - delta;
			textarea_bottomright2_info = new ButtonAreaSwitch(rectangle_area_bottomright2, ((Context) getActivity()).getString(R.string.button_info),
					coloursCfg.getColour_Delimiter(), coloursCfg.getColour_Square_Black(), coloursCfg.getColour_Square_ValidSelection(), info_enabled);
		}
		
		
		if (AREA_BOTTOM2_ENABLED) {
			
			rectangle_area_bottom2left.left = rectangle_bottom2.left + delta;
			rectangle_area_bottom2left.top = rectangle_bottom2.top + delta;
			rectangle_area_bottom2left.right = rectangle_bottom2.left + boardSquareSize + boardSquareSize - delta;
			rectangle_area_bottom2left.bottom = rectangle_bottom2.bottom - delta;
			textarea_bottom2left = new TextArea(rectangle_area_bottom2left, "Messages", getActivity().getUIConfiguration().getColoursConfiguration().getColour_Delimiter(), getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_Black());
			
			rectangle_area_bottom2right1.left = 2 * boardSquareSize + delta;
			rectangle_area_bottom2right1.top = rectangle_bottom2.top /*+ ((rectangle_area_bottom2left.bottom - rectangle_area_bottom2left.top) / 2)*/ + delta;
			rectangle_area_bottom2right1.right = rectangle_bottom2.right - delta;
			rectangle_area_bottom2right1.bottom = rectangle_bottom2.top + ((rectangle_bottom2.bottom - rectangle_bottom2.top) / 2) - delta;
			textarea_bottom2right1 = new TextArea(rectangle_area_bottom2right1, "Thinking: a2a4", getActivity().getUIConfiguration().getColoursConfiguration().getColour_Delimiter(), getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_Black());
			
			rectangle_area_bottom2right2.left = 2 * boardSquareSize + delta;
			rectangle_area_bottom2right2.top = rectangle_bottom2.top + ((rectangle_bottom2.bottom - rectangle_bottom2.top) / 2) + delta;
			rectangle_area_bottom2right2.right = rectangle_bottom.right - delta;
			rectangle_area_bottom2right2.bottom = rectangle_bottom2.bottom - delta;
			textarea_bottom2right2 = new TextArea(rectangle_area_bottom2right2, "NPS     : 35", getActivity().getUIConfiguration().getColoursConfiguration().getColour_Delimiter(), getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_Black());
		}
		
		
		rectangle_area_topplayer.left = 2 * boardSquareSize + delta;
		rectangle_area_topplayer.top = rectangle_top.top + delta;
		rectangle_area_topplayer.right = 6 * boardSquareSize - delta;
		rectangle_area_topplayer.bottom = rectangle_area_topleft.bottom - ((rectangle_area_topleft.bottom - rectangle_area_topleft.top) / 2) /*- delta*/;
		
		rectangle_area_bottomplayer.left = 2 * boardSquareSize + delta;
		rectangle_area_bottomplayer.top = rectangle_bottom.top + ((rectangle_area_bottomleft.bottom - rectangle_area_bottomleft.top) / 2) + delta;
		rectangle_area_bottomplayer.right = 6 * boardSquareSize - delta;
		rectangle_area_bottomplayer.bottom = rectangle_area_bottomleft.bottom;
		
		textarea_topplayername = new ButtonAreaSwitch(isRotatedBoard() ? rectangle_area_bottomplayer : rectangle_area_topplayer, playerName_black,
				coloursCfg.getColour_Delimiter(), coloursCfg.getColour_Square_Black(), coloursCfg.getColour_Square_ValidSelection(), autotop);

		textarea_bottomplayername = new ButtonAreaSwitch(isRotatedBoard() ? rectangle_area_topplayer : rectangle_area_bottomplayer, playerName_white,
				coloursCfg.getColour_Delimiter(), coloursCfg.getColour_Square_Black(), coloursCfg.getColour_Square_ValidSelection(), autobottom);
		
		setBlackPlayerName(playerName_black);
		setWhitePlayerName(playerName_white);

		int moving_computer_icon_id = ((Application_Chess_BaseImpl) Application_Base.getInstance()).getMovingComputerIconID();

	    Bitmap ic_thinking = CachesBitmap.getSingletonFullSized().getBitmap((Context) getActivity(), moving_computer_icon_id);
	    int size = Math.min((int) (rectangle_area_bottomright.right - rectangle_area_bottomright.left), (int) (rectangle_area_bottomright.bottom - rectangle_area_bottomright.top));
	    size = (int) Math.min(size, boardSquareSize);
	    thinkingBitmap = BitmapUtils.createScaledBitmap(ic_thinking, size, size, false);


		if (rectangle_bottom1 != null) {

			float play_and_goto_buttons_border = 30;
			float play_and_goto_buttons_size_y = 8 * (rectangle_bottom1.bottom - rectangle_bottom1.top) / 10;
			float play_and_goto_buttons_size_x = 1.3f * play_and_goto_buttons_size_y;

			rectf_play_first.top = rectangle_bottom1.top + 1 * (rectangle_bottom1.bottom - rectangle_bottom1.top) / 10;
			rectf_play_first.left = rectangle_bottom1.left + delta;
			rectf_play_first.right = rectf_play_first.left + play_and_goto_buttons_size_x;
			rectf_play_first.bottom = rectf_play_first.top + play_and_goto_buttons_size_y;

			rectf_play_prev.top = rectangle_bottom1.top + 1 * (rectangle_bottom1.bottom - rectangle_bottom1.top) / 10;
			rectf_play_prev.left = rectf_play_first.right + play_and_goto_buttons_border;
			rectf_play_prev.right = rectf_play_prev.left + play_and_goto_buttons_size_x;
			rectf_play_prev.bottom = rectf_play_prev.top + play_and_goto_buttons_size_y;

			/*rectf_empty.top = rectangle_bottom1.top + 1 * (rectangle_bottom1.bottom - rectangle_bottom1.top) / 10;
			rectf_empty.left = rectf_play_prev.right + play_and_goto_buttons_border;
			rectf_empty.right = rectf_empty.left + play_and_goto_buttons_size_x;
			rectf_empty.bottom = rectf_empty.top + play_and_goto_buttons_size_y;*/

			rectf_play_last.top = rectangle_bottom1.top + 1 * (rectangle_bottom1.bottom - rectangle_bottom1.top) / 10;
			rectf_play_last.bottom = rectf_play_last.top + play_and_goto_buttons_size_y;
			rectf_play_last.right = rectangle_bottom1.right - delta;
			rectf_play_last.left = rectf_play_last.right - play_and_goto_buttons_size_x;

			rectf_play_next.top = rectangle_bottom1.top + 1 * (rectangle_bottom1.bottom - rectangle_bottom1.top) / 10;
			rectf_play_next.bottom = rectf_play_next.top + play_and_goto_buttons_size_y;
			rectf_play_next.left = rectf_play_last.left - play_and_goto_buttons_size_x - play_and_goto_buttons_border;
			rectf_play_next.right = rectf_play_next.left + play_and_goto_buttons_size_x;


			play_first = new ButtonAreaClick_Image(rectf_play_first, BitmapUtils.fromResource((Context) getActivity(), R.drawable.ic_play_first),
					coloursCfg.getColour_Square_ValidSelection(), coloursCfg.getColour_Square_MarkingSelection());
			play_prev = new ButtonAreaClick_Image(rectf_play_prev, BitmapUtils.fromResource((Context) getActivity(), R.drawable.ic_play_back),
					coloursCfg.getColour_Square_ValidSelection(), coloursCfg.getColour_Square_MarkingSelection());
			play_next = new ButtonAreaClick_Image(rectf_play_next, BitmapUtils.fromResource((Context) getActivity(), R.drawable.ic_play_forward),
					coloursCfg.getColour_Square_ValidSelection(), coloursCfg.getColour_Square_MarkingSelection());
			play_last = new ButtonAreaClick_Image(rectf_play_last, BitmapUtils.fromResource((Context) getActivity(), R.drawable.ic_play_last),
					coloursCfg.getColour_Square_ValidSelection(), coloursCfg.getColour_Square_MarkingSelection());
		}
	}
	
	
	public boolean isOverPanels(float x, float y) {
		return rectangle_top.contains(x, y) || rectangle_bottom.contains(x, y);
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {

		disableAndEnableNavigationButtons();

		disableAndEnableRotateBoardButton();

		drawPlayersPanels(canvas);
	}

	
	private void drawPlayersPanels(Canvas canvas) {
		
		drawTopPlayerPanel(canvas, default_paint);
		
		drawBottomPlayerPanel(canvas, default_paint);
		
		//Draw player names
		textarea_topplayername.draw(canvas);
		
		//Draw players' names
		textarea_bottomplayername.draw(canvas);


		GameManager gameController = getActivity().getGameController();


		//Draw white thinking
		if (gameController != null) {

			if (gameController.isWhiteComputerThinking()) {

				float x;
				float y;

				if (isRotatedBoard()) {

					x = rectangle_area_topplayer.left - thinkingBitmap.getWidth() / 2 + (int) (rectangle_area_topplayer.right - rectangle_area_topplayer.left) / 2;
					y = rectangle_area_topplayer.top - thinkingBitmap.getHeight() / 2 + (int) (rectangle_area_topplayer.bottom - rectangle_area_topplayer.top) / 2;

				} else {

					x = rectangle_area_bottomplayer.left - thinkingBitmap.getWidth() / 2 + (int) (rectangle_area_bottomplayer.right - rectangle_area_bottomplayer.left) / 2;
					y = rectangle_area_bottomplayer.top - thinkingBitmap.getHeight() / 2 + (int) (rectangle_area_bottomplayer.bottom - rectangle_area_bottomplayer.top) / 2;
				}

				x += (thinkingBitmap.getWidth() / 5) * (Math.random() - 0.5);
				y += (thinkingBitmap.getHeight() / 5) * (Math.random() - 0.5);

				canvas.drawBitmap(thinkingBitmap, x, y, default_paint);

				async_updateUI();

			}
		}


		//Draw black thinking
		if (gameController != null && gameController.isBlackComputerThinking()) {
			
			float x;
			float y;

			if (isRotatedBoard()) {

			    x = rectangle_area_bottomplayer.left - thinkingBitmap.getWidth() / 2 + (int) (rectangle_area_bottomplayer.right - rectangle_area_bottomplayer.left) / 2;
			    y = rectangle_area_bottomplayer.top - thinkingBitmap.getHeight() / 2 + (int) (rectangle_area_bottomplayer.bottom - rectangle_area_bottomplayer.top) / 2;

			} else {

				x = rectangle_area_topplayer.left - thinkingBitmap.getWidth() / 2 + (int) (rectangle_area_topplayer.right - rectangle_area_topplayer.left) / 2;
		    	y = rectangle_area_topplayer.top - thinkingBitmap.getHeight() / 2 + (int) (rectangle_area_topplayer.bottom - rectangle_area_topplayer.top) / 2;
			}

		    x += (thinkingBitmap.getWidth() / 5) * (Math.random() - 0.5);
		    y += (thinkingBitmap.getHeight() / 5) * (Math.random() - 0.5);
		    
		    canvas.drawBitmap(thinkingBitmap, x, y, default_paint);
		    
		    async_updateUI();
		}


		int colour = getActivity().getMainView().getBoardView().hasAnimation();

		if (colour != -1) {

			if (colour == COLOUR_PIECE_WHITE) {

				float x;
				float y;

				if (isRotatedBoard()) {

					x = rectangle_area_topplayer.left - thinkingBitmap.getWidth() / 2 + (int) (rectangle_area_topplayer.right - rectangle_area_topplayer.left) / 2;
					y = rectangle_area_topplayer.top - thinkingBitmap.getHeight() / 2 + (int) (rectangle_area_topplayer.bottom - rectangle_area_topplayer.top) / 2;

				} else {

					x = rectangle_area_bottomplayer.left - thinkingBitmap.getWidth() / 2 + (int) (rectangle_area_bottomplayer.right - rectangle_area_bottomplayer.left) / 2;
					y = rectangle_area_bottomplayer.top - thinkingBitmap.getHeight() / 2 + (int) (rectangle_area_bottomplayer.bottom - rectangle_area_bottomplayer.top) / 2;
				}

				//x += (thinkingBitmap.getWidth() / 5) * (Math.random() - 0.5);
				//y += (thinkingBitmap.getHeight() / 5) * (Math.random() - 0.5);

				canvas.drawBitmap(thinkingBitmap, x, y, default_paint);

				async_updateUI();

			} else if (colour == COLOUR_PIECE_BLACK) {

				float x;
				float y;

				if (isRotatedBoard()) {

					x = rectangle_area_bottomplayer.left - thinkingBitmap.getWidth() / 2 + (int) (rectangle_area_bottomplayer.right - rectangle_area_bottomplayer.left) / 2;
					y = rectangle_area_bottomplayer.top - thinkingBitmap.getHeight() / 2 + (int) (rectangle_area_bottomplayer.bottom - rectangle_area_bottomplayer.top) / 2;

				} else {

					x = rectangle_area_topplayer.left - thinkingBitmap.getWidth() / 2 + (int) (rectangle_area_topplayer.right - rectangle_area_topplayer.left) / 2;
					y = rectangle_area_topplayer.top - thinkingBitmap.getHeight() / 2 + (int) (rectangle_area_topplayer.bottom - rectangle_area_topplayer.top) / 2;
				}

				//x += (thinkingBitmap.getWidth() / 5) * (Math.random() - 0.5);
				//y += (thinkingBitmap.getHeight() / 5) * (Math.random() - 0.5);

				canvas.drawBitmap(thinkingBitmap, x, y, default_paint);

				async_updateUI();

			} else {

				throw new IllegalStateException();
			}
		}
	}
	
	
	private void drawTopPlayerPanel(Canvas canvas, Paint paint) {
		
		
		//Draw player panel
		paint.setColor(colour_panel);
		DrawingUtils.drawRoundRectangle(canvas, paint, rectangle_top);
		
		
		//Draw pieces
		float max_image_height = (rectangle_top.bottom - rectangle_top.top) / 2;
		
		int w_image_size = (int) Math.min(max_image_height, (int) Math.min(boardSquareSize, 4 * boardSquareSize / (float) captured_w_size));
		int b_image_size = (int) Math.min(max_image_height, (int) Math.min(boardSquareSize, 4 * boardSquareSize / (float) captured_b_size));

		IBitmapCache cache = CachesBitmap.getSingletonPiecesPanel1(isRotatedBoard() ? b_image_size : w_image_size);

		float imageTop = rectangle_top.bottom - (isRotatedBoard() ? b_image_size : w_image_size);

		drawCapturedPieces(canvas, paint, isRotatedBoard() ? captured_b : captured_w,
				2 * boardSquareSize,
				imageTop,
				isRotatedBoard() ? b_image_size : w_image_size, 
				cache);
		
		
		if (isRotatedBoard()) {
			if (getActivity().getGameController().isWhitePlayerClockOn()) {
				textarea_topleft.setColour_Text(getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_Black());
				textarea_topleft.setColour_Area(getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_ValidSelection());
			} else {
				textarea_topleft.setColour_Text(getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_Black());
				textarea_topleft.setColour_Area(getActivity().getUIConfiguration().getColoursConfiguration().getColour_Delimiter());
			}
			//Set clock time
			textarea_topleft.setText(TimeUtils.time2string(getActivity().getBoardManager().getGameData().getAccumulated_time_white()));
		} else {
			if (getActivity().getGameController().isBlackPlayerClockOn()) {
				textarea_topleft.setColour_Text(getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_Black());
				textarea_topleft.setColour_Area(getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_ValidSelection());
			} else {
				textarea_topleft.setColour_Text(getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_Black());
				textarea_topleft.setColour_Area(getActivity().getUIConfiguration().getColoursConfiguration().getColour_Delimiter());
			}
			//Set clock time
			textarea_topleft.setText(TimeUtils.time2string(getActivity().getBoardManager().getGameData().getAccumulated_time_black()));
		}
		
		//Draw buttons and clock
		textarea_topleft.draw(canvas);
		textarea_topright.draw(canvas);

		if (AUTO_BUTTON_TOP_ENABLED) {

			textarea_topright2_newgame.draw(canvas);

		} else {

			//Do nothing
		}
	}
	
	
	private void drawBottomPlayerPanel(Canvas canvas, Paint paint) {
		
		
		//Draw player panel
		paint.setColor(colour_panel);
		DrawingUtils.drawRoundRectangle(canvas, paint, rectangle_bottom);
		
		if (AREA_BOTTOM2_ENABLED
				&& rectangle_bottom2.height() > 0
				//&& getActivity().getBoardManager().getGameData().getLastSearchInfo() != null
				&& getActivity().getUserSettings().infoEnabled) {
			
			DrawingUtils.drawRoundRectangle(canvas, paint, rectangle_bottom2);


			String value_eval = "  ...  ";
			String value_moves = "  ...  ";
			String value_depth = "  ...  ";

			int current_move_index = getActivity().getBoardManager().getGameData().getCurrentMoveIndex();

			if (current_move_index != -1) {

				SearchInfo last_info = getActivity().getBoardManager().getGameData().getSearchInfos().get(current_move_index);

				if (last_info != null) {

					value_eval = last_info.infoEval;
					value_moves = last_info.infoMoves + "  ";
					value_depth = last_info.infoDepth + ", " + last_info.infoNPS + "  ";
				}
			}

			textarea_bottom2left.setText(value_eval);
			textarea_bottom2left.draw(canvas);
			
			textarea_bottom2right1.setText(value_moves);
			textarea_bottom2right1.draw(canvas);
			
			textarea_bottom2right2.setText(value_depth);
			textarea_bottom2right2.draw(canvas);
		}
		
		//Draw pieces
		float max_image_height = (rectangle_bottom.bottom - rectangle_bottom.top) / 2;
		float imageTop = rectangle_bottom.top;
		
		int w_image_size = (int) Math.min(max_image_height, (int) Math.min(boardSquareSize, 4 * boardSquareSize / (float) captured_w_size));
		int b_image_size = (int) Math.min(max_image_height, (int) Math.min(boardSquareSize, (4 * boardSquareSize / (float) captured_b_size)));
		IBitmapCache cache = CachesBitmap.getSingletonPiecesPanel2(isRotatedBoard() ? w_image_size : b_image_size);
		drawCapturedPieces(canvas, paint, isRotatedBoard() ? captured_w : captured_b,
				2 * boardSquareSize,
				imageTop,
				isRotatedBoard() ? w_image_size : b_image_size,
				cache);
		
		
		if (isRotatedBoard()) {
			if (getActivity().getGameController().isBlackPlayerClockOn()) {
				textarea_bottomleft.setColour_Text(getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_Black());
				textarea_bottomleft.setColour_Area(getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_ValidSelection());
			} else {
				textarea_bottomleft.setColour_Text(getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_Black());
				textarea_bottomleft.setColour_Area(getActivity().getUIConfiguration().getColoursConfiguration().getColour_Delimiter());
			}
			//Set clock time
			textarea_bottomleft.setText(TimeUtils.time2string(getActivity().getBoardManager().getGameData().getAccumulated_time_black()));
		} else {
			if (getActivity().getGameController().isWhitePlayerClockOn()) {
				textarea_bottomleft.setColour_Text(getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_Black());
				textarea_bottomleft.setColour_Area(getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_ValidSelection());
			} else {
				textarea_bottomleft.setColour_Text(getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_Black());
				textarea_bottomleft.setColour_Area(getActivity().getUIConfiguration().getColoursConfiguration().getColour_Delimiter());
			}
			//Set clock time
			textarea_bottomleft.setText(TimeUtils.time2string(getActivity().getBoardManager().getGameData().getAccumulated_time_white()));
		}
		
		//Draw buttons and clock
		textarea_bottomleft.draw(canvas);

		textarea_bottomright.draw(canvas);

		if (AUTO_BUTTON_BOTTOM_ENABLED) {

			textarea_bottomright2_info.draw(canvas);

		} else {

			//Do nothing
		}

		if (rectangle_bottom1 != null) {

			paint.setColor(colour_panel);
			DrawingUtils.drawRoundRectangle(canvas, paint, rectangle_bottom1);

			play_first.draw(canvas);
			play_prev.draw(canvas);
			play_next.draw(canvas);
			play_last.draw(canvas);
		}
	}


	private void disableAndEnableNavigationButtons() {

		if (rectangle_bottom1 != null) {

			IConfigurationColours coloursCfg = getActivity().getUIConfiguration().getColoursConfiguration();

			if (isMoveNavigationAndAutoPlayerButtonsLocked()) {

				if (play_first != null) play_first.setColour_Area(coloursCfg.getColour_Delimiter());
				if (play_prev != null) play_prev.setColour_Area(coloursCfg.getColour_Delimiter());
				if (play_next != null) play_next.setColour_Area(coloursCfg.getColour_Delimiter());
				if (play_last != null) play_last.setColour_Area(coloursCfg.getColour_Delimiter());

			} else {

				GameData gamedata = getActivity().getBoardManager().getGameData();

				if (!gamedata.isOnTheFirstMove()) {

					if (play_first != null) play_first.setColour_Area(coloursCfg.getColour_Square_ValidSelection());
					if (play_prev != null) play_prev.setColour_Area(coloursCfg.getColour_Square_ValidSelection());

				} else {

					if (play_first != null) play_first.setColour_Area(coloursCfg.getColour_Delimiter());
					if (play_prev != null) play_prev.setColour_Area(coloursCfg.getColour_Delimiter());
				}

				if (!gamedata.isOnTheLastMove()) {

					if (play_next != null) play_next.setColour_Area(coloursCfg.getColour_Square_ValidSelection());
					if (play_last != null) play_last.setColour_Area(coloursCfg.getColour_Square_ValidSelection());

				} else {

					if (play_next != null) play_next.setColour_Area(coloursCfg.getColour_Delimiter());
					if (play_last != null) play_last.setColour_Area(coloursCfg.getColour_Delimiter());
				}
			}
		}
	}


	private void disableAndEnableRotateBoardButton() {

		IConfigurationColours coloursCfg = getActivity().getUIConfiguration().getColoursConfiguration();

		if (getActivity().getMainView().getBoardView().hasAnimation() != -1) {

			textarea_topright2_newgame.setColour_Area(coloursCfg.getColour_Delimiter());

		} else {

			if (textarea_topright2_newgame.isSelected()) {

				textarea_topright2_newgame.setColour_Area(coloursCfg.getColour_Square_MarkingSelection());

			} else {

				textarea_topright2_newgame.setColour_Area(coloursCfg.getColour_Square_ValidSelection());
			}
		}
	}


	private void async_updateUI() {

		final MainActivity mainActivity = (MainActivity) getActivity();
		
		mainActivity.executeJob(new Runnable() {
			
			@Override
			public void run() {

				try {

					Thread.sleep(42);

				} catch (InterruptedException e) {}
				
				Handler ui = mainActivity.getUIHandler();
				
				if (ui != null) {

					ui.post(new Runnable() {
						
						@Override
						public void run() {
							invalidate();
						}
					});
				}
			}
		});
	}
	
	
	private void drawCapturedPieces(Canvas canvas, Paint paint, int[] pieceIDs,
			float left, float top, float imageSize, IBitmapCache cache) {
		
		int distanceBetweenImages = 0;
		
		for (int i=0; i<pieceIDs.length; i++) {
			int pieceID = pieceIDs[i];
			if (pieceID != ID_PIECE_NONE) {
				
				//Obtain piece bitmap
				int imageResID = getActivity().getUIConfiguration().getPiecesConfiguration().getBitmapResID(pieceID);
				Bitmap bitmap = cache.getBitmap((Context) getActivity(), imageResID);
				if (bitmap == null) {
					getActivity().getUIConfiguration().getPiecesConfiguration().getPiece(pieceID); //This call will be added into the cache
					bitmap = cache.getBitmap((Context) getActivity(), imageResID);	
				}
				
				//Draw piece bitmap
				DrawingUtils.drawInField(canvas, paint, imageSize, left + i * (imageSize + distanceBetweenImages), top, bitmap);
			}
		}
	}
	
	
	@Override
	public void setCapturedPieces(int[] _captured_w, int[] _captured_b, int _captured_w_size, int _captured_b_size) {
		
		captured_w_size = _captured_w_size;
		captured_b_size = _captured_b_size;
		
		for (int i=0; i<captured_w.length; i++) {
			captured_w[i] = _captured_w[i];
		}

		for (int i=0; i<captured_b.length; i++) {
			captured_b[i] = _captured_b[i];
		}
	}
	
	
	@Override
	public void selectButtonUnmove() {
		textarea_bottomright.select();
		
	}


	@Override
	public void deselectButtonUnmove() {
		textarea_bottomright.deselect();
	}


	@Override
	public void selectButtonMenu() {
		textarea_topright.select();
	}


	@Override
	public void deselectButtonMenu() {
		textarea_topright.deselect();
	}


	@Override
	public void selectButtonHelpTop() {
		textarea_topleft2.select();
	}


	@Override
	public void deselectButtonHelpTop() {
	}


	@Override
	public void selectButtonHelpBottom() {
		textarea_bottomleft2.select();
	}


	@Override
	public void deselectButtonHelpBottom() {
	}


	@Override
	public void selectButtonAutoTop() {
		if (AUTO_BUTTON_TOP_ENABLED) {
			textarea_topright2_newgame.select();
		} else {
			//Do nothing
		}
	}


	@Override
	public void deselectButtonAutoTop() {
		if (AUTO_BUTTON_TOP_ENABLED) {
			textarea_topright2_newgame.deselect();
		} else {
			//Do nothing
		}
	}

	
	@Override
	public void selectButton_Info() {
		if (AUTO_BUTTON_BOTTOM_ENABLED) {
			textarea_bottomright2_info.select();
		} else {
			//Do nothing
		}
	}


	@Override
	public void deselectButton_Info() {
		if (AUTO_BUTTON_BOTTOM_ENABLED) {
			textarea_bottomright2_info.deselect();
		} else {
			//Do nothing
		}
	}
	
	@Override
	public void finishButtonInfo() {
		if (AUTO_BUTTON_BOTTOM_ENABLED) {
			textarea_bottomright2_info.finish();
		} else {
			//Do nothing
		}
	}
	
	@Override
	public boolean isOverButtonInfo(float x, float y) {
		if (AUTO_BUTTON_BOTTOM_ENABLED) { 
			return rectangle_area_bottomright2.contains(x, y);
		} else {
			return false;
		}
	}

	
	@Override
	public boolean isActiveButtonInfo() {
		if (AUTO_BUTTON_BOTTOM_ENABLED) { 
			return textarea_bottomright2_info.isActive();
		} else {
			return false;
		}
	}
	
	
	@Override
	public boolean isOverTopArea(float x, float y) {
		if (isRotatedBoard()) {
			return rectangle_area_bottomplayer.contains(x, y);
		} else {
			return rectangle_area_topplayer.contains(x, y);
		}
	}


	@Override
	public void selectButtonTopPlayer() {
		textarea_topplayername.select();
	}


	@Override
	public void deselectButtonTopPlayer() {
		textarea_topplayername.deselect();
	}


	@Override
	public void finishButtonTopPlayer() {
		textarea_topplayername.finish();
	}
	
	
	@Override
	public boolean isActiveButtonTopPlayer() {
		return textarea_topplayername.isActive();
	}
	

	@Override
	public boolean isOverBottomArea(float x, float y) {
		if (isRotatedBoard()) {
			return rectangle_area_topplayer.contains(x, y);
		} else {
			return rectangle_area_bottomplayer.contains(x, y);
		}
	}


	@Override
	public void selectButtonBottomPlayer() {
		textarea_bottomplayername.select();
	}


	@Override
	public void deselectButtonBottomPlayer() {
		textarea_bottomplayername.deselect();
	}


	@Override
	public void finishButtonBottomPlayer() {
		textarea_bottomplayername.finish();
	}
	
	
	@Override
	public boolean isActiveButtonBottomPlayer() {
		return textarea_bottomplayername.isActive();
	}
	
	@Override
	public boolean isOverButtonMenu(float x, float y) {
		return rectangle_area_topright.contains(x, y);
	}
	
	@Override
	public boolean isActiveButtonMenu() {
		return textarea_topright.isActive();
	}
	
	@Override
	public boolean isOverButtonUnmove(float x, float y) {
		return rectangle_area_bottomright.contains(x, y);
	}
	
	@Override
	public boolean isActiveButtonUnmove() {
		return textarea_bottomright.isActive();
	}


	@Override
	public boolean isOverButtonAutoTop(float x, float y) {
		if (AUTO_BUTTON_TOP_ENABLED) {
			return rectangle_area_topright2.contains(x, y);
		} else {
			return false;
		}
	}
	
	
	@Override
	public boolean isOverButtonHelpTop(float x, float y) {
		return false;
	}


	@Override
	public boolean isOverButtonHelpBottom(float x, float y) {
		return false;
	}


	@Override
	public void setWhitePlayerName(String playerNameWhite) {
		textarea_bottomplayername.setText("   " + playerNameWhite + "   ");
	}
	
	@Override
	public void setBlackPlayerName(String playerNameBlack) {
		textarea_topplayername.setText("   " + playerNameBlack + "   ");
	}


	private boolean isRotatedBoard() {
		return ((UserSettings)Application_Base.getInstance().getUserSettings()).rotatedboard;
	}


	@Override
	public boolean isMoveNavigationAndAutoPlayerButtonsLocked() {

		if (getActivity().getMainView().getBoardView().hasAnimation() != -1) {

			return true;
		}

		if (getActivity().getGameController().isThinking()) {

			return true;
		}

		return false;
	}


	@Override
	public OnTouchListener createOnTouchListener(IBoardVisualization boardVisualization, IBoardViewActivity activity) {

		return new OnTouchListener_Panels(boardVisualization, activity);
	}


	public class OnTouchListener_Panels implements OnTouchListener, BoardConstants {


		private IBoardVisualization boardVisualization;

		private IBoardViewActivity activity;


		public OnTouchListener_Panels(IBoardVisualization _boardVisualization, IBoardViewActivity _activity) {

			boardVisualization = _boardVisualization;

			activity = _activity;
		}


		@Override
		public boolean onTouch(View view, MotionEvent event) {

			if (isLocked()) {

				return true;
			}

			synchronized (activity) {

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

			if (isOverButtonMenu(x, y)) {

				selectButtonMenu();

			} else if (isOverButtonUnmove(x, y)) {

				if (activity.getMainView().getBoardView().hasAnimation() != -1) {

					deselectButtonUnmove();

					return;
				}

				selectButtonUnmove();

			} else if (isOverButtonAutoTop(x, y)) {

				if (boardVisualization.hasAnimation() != -1) {

					return;
				}

				selectButtonAutoTop();

			} else if (isOverButtonInfo(x, y)) {

				selectButton_Info();

			} else if (isOverButtonHelpTop(x, y)) {

				selectButtonHelpTop();

			} else if (isOverButtonHelpBottom(x, y)) {

				selectButtonHelpBottom();

			} else if (isOverTopArea(x, y)) {

				selectButtonTopPlayer();

			} else if (isOverBottomArea(x, y)) {

				selectButtonBottomPlayer();

			} else if (rectangle_bottom1 != null) {

				if (isMoveNavigationAndAutoPlayerButtonsLocked()) return;

				GameData gamedata = activity.getBoardManager().getGameData();

				if (!gamedata.isOnTheFirstMove() && rectf_play_first.contains(x, y)) {

					play_first.select();

				} else if (!gamedata.isOnTheFirstMove() && rectf_play_prev.contains(x, y)) {

					play_prev.select();

				} else if (!gamedata.isOnTheLastMove() && rectf_play_next.contains(x, y)) {

					play_next.select();

				} else if (!gamedata.isOnTheLastMove() && rectf_play_last.contains(x, y)) {

					play_last.select();
				}
			}

			redraw();
		}


		private void processEvent_MOVE(MotionEvent event) {

			float x = event.getX();
			float y = event.getY();

			if (isOverButtonMenu(x, y)) {

				selectButtonMenu();

			} else {

				deselectButtonMenu();
			}

			if (isOverButtonUnmove(x, y)) {
				if (activity.getMainView().getBoardView().hasAnimation() != -1) {
					deselectButtonUnmove();
					return;
				}
				selectButtonUnmove();
			} else {
				deselectButtonUnmove();
			}

			if (boardVisualization.hasAnimation() == -1) {

				if (isOverButtonAutoTop(x, y)) {

					selectButtonAutoTop();

				} else {

					deselectButtonAutoTop();
				}
			}

			if (isOverButtonInfo(x, y)) {
				selectButton_Info();
			} else {
				deselectButton_Info();
			}

			if (isOverButtonHelpTop(x, y)) {
				selectButtonHelpTop();
			} else {
				deselectButtonHelpTop();
			}

			if (isOverButtonHelpBottom(x, y)) {
				selectButtonHelpBottom();
			} else {
				deselectButtonHelpBottom();
			}

			if (isOverTopArea(x, y)) {
				selectButtonTopPlayer();
			} else {
				deselectButtonTopPlayer();
			}

			if (isOverBottomArea(x, y)) {
				selectButtonBottomPlayer();
			} else {
				deselectButtonBottomPlayer();
			}

			if (rectangle_bottom1 != null) {

				if (isMoveNavigationAndAutoPlayerButtonsLocked()) return;

				GameData gamedata = activity.getBoardManager().getGameData();

				if (!gamedata.isOnTheFirstMove() && rectf_play_first.contains(x, y)) {
					play_first.select();
				} else {
					play_first.deselect();
				}

				if (!gamedata.isOnTheFirstMove() && rectf_play_prev.contains(x, y)) {
					play_prev.select();
				} else {
					play_prev.deselect();
				}

				if (!gamedata.isOnTheLastMove() && rectf_play_next.contains(x, y)) {
					play_next.select();
				} else {
					play_next.deselect();
				}

				if (!gamedata.isOnTheLastMove() && rectf_play_last.contains(x, y)) {
					play_last.select();
				} else {
					play_last.deselect();
				}
			}

			redraw();
		}


		private void processEvent_UP(MotionEvent event) {

			float x = event.getX();
			float y = event.getY();

			if (isOverButtonMenu(x, y)) {

				deselectButtonMenu();

				Intent i = new Intent((Context) activity, activity.getMainMenuClass());
				activity.startActivity(i);

			} else if (isOverButtonUnmove(x, y)) {

				deselectButtonUnmove();

				if (isMoveNavigationAndAutoPlayerButtonsLocked()) return;

				GameData gamedata = activity.getBoardManager().getGameData();

				List<Move> moves = gamedata.getMoves();
				int moveIndex = gamedata.getCurrentMoveIndex();
				Move lastmove = null;
				if (moveIndex >= 0) {

					lastmove = moves.remove(moveIndex);
					gamedata.getSearchInfos().remove(moveIndex);

					activity.getBoardManager().unmove(lastmove);
					gamedata.setCurrentMoveIndex(moveIndex - 1);
					gamedata.save();
				}

				boardVisualization.setTopMessageText(null);


				//Visualize needed stuffs

				boardVisualization.setData(activity.getBoardManager().getBoard_WithoutHided());
				setCapturedPieces(activity.getBoardManager().getCaptured_W(), activity.getBoardManager().getCaptured_B(), activity.getBoardManager().getCapturedSize_W(), activity.getBoardManager().getCapturedSize_B());

				if (activity.getBoardManager().getMovingPiece() != null)
					activity.getBoardManager().clearMovingPiece();

				boardVisualization.clearSelections();

				if (lastmove != null) {
					boardVisualization.markingSelection_Permanent_Border(lastmove.fromLetter, lastmove.fromDigit);
					boardVisualization.markingSelection_Permanent_Border(lastmove.toLetter, lastmove.toDigit);
				}

				boardVisualization.redraw();

				activity.getGameController().scheduleComputerPlayerAutoStart();

			} else if (isOverButtonAutoTop(x, y)) {

				deselectButtonAutoTop();

				if (boardVisualization.hasAnimation() != -1) {

					return;
				}

				((UserSettings) Application_Base.getInstance().getUserSettings()).rotatedboard = !((UserSettings) Application_Base.getInstance().getUserSettings()).rotatedboard;
				Application_Base.getInstance().getUserSettings().save();

				activity.getMainView().requestLayout();

				boardVisualization.redraw();


			} else if (isOverButtonInfo(x, y)) {

				if (isActiveButtonInfo()) {
					activity.getUserSettings().infoEnabled = false;
				} else {
					activity.getUserSettings().infoEnabled = true;
				}

				Application_Base.getInstance().getUserSettings().save();

				finishButtonInfo();

				//force re-calculating the layout dimension and the redraw of the view
				activity.getMainView().requestLayout();


			} else if (isOverButtonHelpTop(x, y)) {

				deselectButtonHelpTop();

			} else if (isOverButtonHelpBottom(x, y)) {

				deselectButtonHelpBottom();

			} else if (isOverTopArea(x, y)) {

				deselectButtonTopPlayer();

				finishButtonTopPlayer();

				UserSettings settings = activity.getUserSettings();

				if (!settings.auto_player_enabled_black) {

					//Auto player switched on
					activity.getGameController().switchOnAutoPlayer(COLOUR_PIECE_BLACK);

				} else {

					//Auto player switched off
					activity.getGameController().switchOffAutoPlayer(COLOUR_PIECE_BLACK);

					//Enable human player to manually select piece and make move via UI.
					//The UI of the board should be unlocked for this purpose.
					activity.getMainView().getBoardView().unlock();
				}

			} else if (isOverBottomArea(x, y)) {

				deselectButtonBottomPlayer();

				finishButtonBottomPlayer();

				UserSettings settings = activity.getUserSettings();

				if (!settings.auto_player_enabled_white) {

					//Auto player switched on
					activity.getGameController().switchOnAutoPlayer(COLOUR_PIECE_WHITE);

				} else {

					//Auto player switched off
					activity.getGameController().switchOffAutoPlayer(COLOUR_PIECE_WHITE);

					//Enable human player to manually select piece and make move via UI.
					//The UI of the board should be unlocked for this purpose.
					activity.getMainView().getBoardView().unlock();
				}

			} else if (rectangle_bottom1 != null) {

				play_first.deselect();
				play_prev.deselect();
				play_next.deselect();
				play_last.deselect();

				if (isMoveNavigationAndAutoPlayerButtonsLocked()) {

					return;
				}

				if (rectf_play_first.contains(x, y)) {

					GameData gamedata = activity.getBoardManager().getGameData();

					if (gamedata.isOnTheFirstMove()) {

						return;
					}

					IBoardManager boardManager = activity.getBoardManager();

					List<Move> moves = gamedata.getMoves();
					int moveIndex = gamedata.getCurrentMoveIndex();
					Move lastmove = null;
					for (int i = moveIndex; i >= 0; i--) {
						lastmove = moves.get(i);
						boardManager.unmove(lastmove);
					}
					gamedata.setCurrentMoveIndex(-1);

					Application_Base.getInstance().storeGameData(gamedata);

					if (activity.getBoardManager().getMovingPiece() != null)
						activity.getBoardManager().clearMovingPiece();


					boardVisualization.clearSelections();
					if (lastmove != null)
						boardVisualization.markingSelection_Permanent_Border(lastmove.fromLetter, lastmove.fromDigit);
					if (lastmove != null)
						boardVisualization.markingSelection_Permanent_Border(lastmove.toLetter, lastmove.toDigit);

					boardVisualization.setData(boardManager.getBoard_WithoutHided());
					boardVisualization.setTopMessageText(null);
					setCapturedPieces(activity.getBoardManager().getCaptured_W(), activity.getBoardManager().getCaptured_B(), activity.getBoardManager().getCapturedSize_W(), activity.getBoardManager().getCapturedSize_B());
					redraw();
					boardVisualization.redraw();

					activity.getGameController().scheduleComputerPlayerAutoStart();

				} else if (rectf_play_prev.contains(x, y)) {

					GameData gamedata = activity.getBoardManager().getGameData();

					if (gamedata.isOnTheFirstMove()) {
						return;
					}

					IBoardManager boardManager = activity.getBoardManager();

					List<Move> moves = gamedata.getMoves();
					int moveIndex = gamedata.getCurrentMoveIndex();
					Move lastmove = null;
					if (moveIndex >= 0) {
						lastmove = moves.get(moveIndex);
						boardManager.unmove(lastmove);
						gamedata.setCurrentMoveIndex(moveIndex - 1);
					}

					lastmove = null;

					if (gamedata.getCurrentMoveIndex() >= 0) {
						lastmove = moves.get(gamedata.getCurrentMoveIndex());
					}

					gamedata.getBoarddata().gameResultText = null;

					Application_Base.getInstance().storeGameData(gamedata);


					if (activity.getBoardManager().getMovingPiece() != null)
						activity.getBoardManager().clearMovingPiece();


					boardVisualization.clearSelections();
					if (lastmove != null)
						boardVisualization.markingSelection_Permanent_Border(lastmove.fromLetter, lastmove.fromDigit);
					if (lastmove != null)
						boardVisualization.markingSelection_Permanent_Border(lastmove.toLetter, lastmove.toDigit);

					boardVisualization.setData(boardManager.getBoard_WithoutHided());
					boardVisualization.setTopMessageText(null);
					setCapturedPieces(activity.getBoardManager().getCaptured_W(), activity.getBoardManager().getCaptured_B(), activity.getBoardManager().getCapturedSize_W(), activity.getBoardManager().getCapturedSize_B());
					redraw();
					boardVisualization.redraw();

					activity.getGameController().scheduleComputerPlayerAutoStart();

				} else if (rectf_play_next.contains(x, y)) {

					GameData gamedata = activity.getBoardManager().getGameData();

					if (gamedata.isOnTheLastMove()) {
						return;
					}

					IBoardManager boardManager = activity.getBoardManager();

					List<Move> moves = gamedata.getMoves();
					int moveIndex = gamedata.getCurrentMoveIndex() + 1;
					if (moveIndex < 0) {
						moveIndex = 0;
					}
					Move lastmove = null;
					if (moveIndex <= moves.size() - 1) {
						lastmove = moves.get(moveIndex);
						boardManager.move(lastmove);
						gamedata.setCurrentMoveIndex(moveIndex);
					}

					if (lastmove == null && moves.size() > 0) {
						lastmove = moves.get(moves.size() - 1);
					}

					Application_Base.getInstance().storeGameData(gamedata);


					if (activity.getBoardManager().getMovingPiece() != null)
						activity.getBoardManager().clearMovingPiece();


					boardVisualization.clearSelections();
					if (lastmove != null)
						boardVisualization.markingSelection_Permanent_Border(lastmove.fromLetter, lastmove.fromDigit);
					if (lastmove != null)
						boardVisualization.markingSelection_Permanent_Border(lastmove.toLetter, lastmove.toDigit);


					boardVisualization.setData(boardManager.getBoard_WithoutHided());
					setCapturedPieces(activity.getBoardManager().getCaptured_W(), activity.getBoardManager().getCaptured_B(), activity.getBoardManager().getCapturedSize_W(), activity.getBoardManager().getCapturedSize_B());

					int gameStatus = activity.getBoardManager().getGameStatus();
					if (gameStatus != GlobalConstants.GAME_STATUS_NONE) {

						activity.updateViewWithGameResult(gameStatus);

					} else {

						//Do not resume game, because the player most probably will click next move button more than once.
						//activity.getGameController().resumeGame();
						//So cancel the auto run as well
						activity.getGameController().recreateAutoPlayCallBack();

					}

					redraw();
					boardVisualization.redraw();

				} else if (rectf_play_last.contains(x, y)) {

					GameData gamedata = activity.getBoardManager().getGameData();

					if (gamedata.isOnTheLastMove()) {
						return;
					}

					IBoardManager boardManager = activity.getBoardManager();

					List<Move> moves = gamedata.getMoves();
					int moveIndex = gamedata.getCurrentMoveIndex() + 1;
					if (moveIndex < 0) {
						moveIndex = 0;
					}
					Move lastmove = null;
					for (int i = moveIndex; i <= moves.size() - 1; i++) {
						lastmove = moves.get(i);
						boardManager.move(lastmove);
					}
					gamedata.setCurrentMoveIndex(moves.size() - 1);

					if (lastmove == null && moves.size() > 0) {
						lastmove = moves.get(moves.size() - 1);
					}

					Application_Base.getInstance().storeGameData(gamedata);


					if (activity.getBoardManager().getMovingPiece() != null)
						activity.getBoardManager().clearMovingPiece();


					boardVisualization.clearSelections();
					if (lastmove != null)
						boardVisualization.markingSelection_Permanent_Border(lastmove.fromLetter, lastmove.fromDigit);
					if (lastmove != null)
						boardVisualization.markingSelection_Permanent_Border(lastmove.toLetter, lastmove.toDigit);


					boardVisualization.setData(boardManager.getBoard_WithoutHided());
					setCapturedPieces(activity.getBoardManager().getCaptured_W(), activity.getBoardManager().getCaptured_B(), activity.getBoardManager().getCapturedSize_W(), activity.getBoardManager().getCapturedSize_B());

					int gameStatus = activity.getBoardManager().getGameStatus();
					if (gameStatus != GlobalConstants.GAME_STATUS_NONE) {
						activity.updateViewWithGameResult(gameStatus);
					} else {
						activity.getGameController().resumeGame();
					}

					redraw();
					boardVisualization.redraw();
				}
			}

			redraw();
		}
	}
}
