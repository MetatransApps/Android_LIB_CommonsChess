package org.metatrans.commons.chess.views_and_controllers;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import org.metatrans.commons.TimeUtils;
import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.cfg.colours.IConfigurationColours;
import org.metatrans.commons.chess.R;
import org.metatrans.commons.chess.app.Application_Chess_BaseImpl;
import org.metatrans.commons.chess.cfg.pieces.ConfigurationUtils_Pieces;
import org.metatrans.commons.chess.cfg.pieces.IConfigurationPieces;
import org.metatrans.commons.chess.logic.BoardConstants;
import org.metatrans.commons.chess.logic.game.GameManager;
import org.metatrans.commons.chess.model.UserSettings;
import org.metatrans.commons.chess.utils.CachesBitmap;
import org.metatrans.commons.ui.ButtonAreaClick_Image;
import org.metatrans.commons.ui.ButtonAreaSwitch;
import org.metatrans.commons.ui.ButtonAreaSwitch_Image;
import org.metatrans.commons.ui.images.BitmapCacheBase;
import org.metatrans.commons.ui.images.IBitmapCache;
import org.metatrans.commons.ui.utils.BitmapUtils;
import org.metatrans.commons.ui.utils.DrawingUtils;


/**
 * TODO: This class have to be further reworked.
 * It has a lot of redundant methods.
 * They have to be removed and directly rectangles and buttons have to be used
 * in this class as well as in the inner class OnTouchListener_Panels
 */
public abstract class PanelsView extends SearchInfoView implements IPanelsVisualization, BoardConstants {
	
	
	protected static int DELTA_AREAS = 9;

	private static boolean AUTO_BUTTON_BOTTOM_ENABLED = true;

	protected RectF rectangle_top;
	protected RectF rectangle_bottom;
	protected float boardSquareSize;
	
	protected int[] captured_w;
	protected int[] captured_b;
	private int captured_w_size;
	private int captured_b_size;
	
	private RectF rectangle_area_topleft;
	private RectF rectangle_area_topplayer;
	protected RectF rectangle_area_topright;
	protected RectF rectangle_area_switch_colors;

	private RectF rectangle_area_bottomleft;
	private RectF rectangle_area_bottomplayer;


	private ClockArea textarea_topleft;
	private ClockArea textarea_bottomleft;
	protected ButtonAreaClick_Image textarea_menu;
	protected ButtonAreaSwitch_Image textarea_switch_colors;

	private ButtonAreaSwitch textarea_white_player;
	private ButtonAreaSwitch textarea_black_player;

	protected Bitmap thinkingBitmap;


	public PanelsView(Context context, View _parent,  RectF _rectangleTop, RectF _rectangleBottom, RectF _rectangleBottom1,  RectF _rectangleBottom2) {
		
		super(context, _parent, _rectangleBottom2);
		
		rectangle_top = _rectangleTop;
		rectangle_bottom = _rectangleBottom;
		
		captured_w = new int[16];
		captured_b = new int[16];

		default_paint = new Paint();
		
		rectangle_area_topleft = new RectF();
		rectangle_area_bottomleft = new RectF();
		
		rectangle_area_topright = new RectF();
		
		rectangle_area_topplayer = new RectF();
		rectangle_area_bottomplayer = new RectF();
		rectangle_area_switch_colors = new RectF();

		colour_panel = getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_Black();
	}
	
	
	public void init(String playerName_white, String playerName_black, boolean autotop, boolean autobottom, boolean info_enabled) {

		super.init();

		IConfigurationColours coloursCfg = getActivity().getUIConfiguration().getColoursConfiguration();

		int delta = DELTA_AREAS;
		
		boardSquareSize = (rectangle_top.right - rectangle_top.left) / (float) 8;

		rectangle_area_topleft.left = rectangle_top.left + delta;
		rectangle_area_topleft.top = rectangle_top.top + delta;
		rectangle_area_topleft.right = rectangle_top.left + boardSquareSize + boardSquareSize - delta;
		rectangle_area_topleft.bottom = rectangle_top.bottom - delta;
		textarea_topleft = new ClockArea(rectangle_area_topleft, "00:00:00", getActivity().getUIConfiguration().getColoursConfiguration().getColour_Delimiter(), getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_Black());

		rectangle_area_topright.left = rectangle_top.right - 2 * boardSquareSize + delta;
		rectangle_area_topright.top = rectangle_top.top + delta;
		rectangle_area_topright.right = rectangle_top.right - delta;
		rectangle_area_topright.bottom = rectangle_top.bottom - delta;

		textarea_menu = new ButtonAreaClick_Image(rectangle_area_topright,
				BitmapUtils.fromResource((Context) getActivity(),R.drawable.ic_action_settings_white),
				coloursCfg.getColour_Square_ValidSelection(),
				coloursCfg.getColour_Square_MarkingSelection(),
				false
			);


		rectangle_area_bottomleft.left = rectangle_bottom.left + delta;
		rectangle_area_bottomleft.top = rectangle_bottom.top + delta;
		rectangle_area_bottomleft.right = rectangle_bottom.left + boardSquareSize + boardSquareSize - delta;
		rectangle_area_bottomleft.bottom = rectangle_bottom.bottom - delta;

		textarea_bottomleft = new ClockArea(rectangle_area_bottomleft, "00:00:00", getActivity().getUIConfiguration().getColoursConfiguration().getColour_Delimiter(), getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_Black());

		rectangle_area_switch_colors.left = rectangle_bottom.right - 2 * boardSquareSize + delta;
		rectangle_area_switch_colors.top = rectangle_bottom.top + delta;
		rectangle_area_switch_colors.right = rectangle_bottom.right - delta;
		rectangle_area_switch_colors.bottom = rectangle_bottom.bottom - delta;

		textarea_switch_colors = new ButtonAreaSwitch_Image(rectangle_area_switch_colors,
				BitmapUtils.fromResource((Context) getActivity(), R.drawable.ic_action_reload_white),
				coloursCfg.getColour_Square_ValidSelection(),
				coloursCfg.getColour_Delimiter(),
				coloursCfg.getColour_Square_MarkingSelection(),
				true,
				false
			);

		
		rectangle_area_topplayer.left = 2 * boardSquareSize + delta;
		rectangle_area_topplayer.top = rectangle_top.top + delta;
		rectangle_area_topplayer.right = 6 * boardSquareSize - delta;
		rectangle_area_topplayer.bottom = rectangle_area_topleft.bottom - ((rectangle_area_topleft.bottom - rectangle_area_topleft.top) / 2) /*- delta*/;
		
		rectangle_area_bottomplayer.left = 2 * boardSquareSize + delta;
		rectangle_area_bottomplayer.top = rectangle_bottom.top + ((rectangle_area_bottomleft.bottom - rectangle_area_bottomleft.top) / 2) + delta;
		rectangle_area_bottomplayer.right = 6 * boardSquareSize - delta;
		rectangle_area_bottomplayer.bottom = rectangle_area_bottomleft.bottom;


		UserSettings settings = getActivity().getUserSettings();

		textarea_white_player = new ButtonAreaSwitch(isRotatedBoard() ? rectangle_area_topplayer : rectangle_area_bottomplayer,
				getActivity().getBoardManager().getGameData().getWhite().getName(),
				coloursCfg.getColour_Square_ValidSelection(),
				coloursCfg.getColour_Delimiter(),
				coloursCfg.getColour_Square_Black(),
				coloursCfg.getColour_Square_MarkingSelection(),
				settings.auto_player_enabled_white);

		textarea_black_player = new ButtonAreaSwitch(isRotatedBoard() ? rectangle_area_bottomplayer : rectangle_area_topplayer,
				getActivity().getBoardManager().getGameData().getBlack().getName(),
				coloursCfg.getColour_Square_ValidSelection(),
				coloursCfg.getColour_Delimiter(),
				coloursCfg.getColour_Square_Black(),
				coloursCfg.getColour_Square_MarkingSelection(),
				settings.auto_player_enabled_black);


		initInfoRectangle();


		int moving_computer_icon_id = ((Application_Chess_BaseImpl) Application_Base.getInstance()).getMovingComputerIconID();

		int size_thinking_image = Math.min((int) (rectangle_area_info.right - rectangle_area_info.left), (int) (rectangle_area_info.bottom - rectangle_area_info.top));
		size_thinking_image = (int) Math.min(size_thinking_image, boardSquareSize);

		Bitmap ic_thinking = CachesBitmap.getSingletonFullSized().getBitmap((Context) getActivity(), moving_computer_icon_id);

		thinkingBitmap = BitmapUtils.createScaledBitmap(ic_thinking, size_thinking_image, size_thinking_image);
	}


	protected abstract void initInfoRectangle();


	@Override
	protected void onDraw(Canvas canvas) {

		disableAndEnableRotateBoardButton();

		drawPlayersPanels(canvas);

		super.onDraw(canvas);

		invalidate();
	}

	
	private void drawPlayersPanels(Canvas canvas) {
		
		drawTopPlayerPanel(canvas, default_paint);
		
		drawBottomPlayerPanel(canvas, default_paint);
		
		//Draw player names
		textarea_white_player.draw(canvas);
		
		//Draw players' names
		textarea_black_player.draw(canvas);


		GameManager gameController = getActivity().getGameController();


		//Draw white thinking
		if (gameController != null) {

			if (gameController.isWhiteComputerThinking()) {

				drawComputerThinking(canvas, rectangle_area_topplayer, rectangle_area_bottomplayer, true);

			}
		}


		//Draw black thinking
		if (gameController != null && gameController.isBlackComputerThinking()) {

			drawComputerThinking(canvas, rectangle_area_bottomplayer, rectangle_area_topplayer, true);
		}


		int colour = getActivity().getMainView().getBoardView().hasAnimation();

		if (colour != -1) {

			if (colour == COLOUR_PIECE_WHITE) {

				drawComputerThinking(canvas, rectangle_area_topplayer, rectangle_area_bottomplayer, false);

			} else if (colour == COLOUR_PIECE_BLACK) {

				drawComputerThinking(canvas, rectangle_area_bottomplayer, rectangle_area_topplayer, false);

			} else {

				throw new IllegalStateException();
			}
		}
	}

	private void drawComputerThinking(Canvas canvas, RectF rectangle_area_bottomplayer, RectF rectangle_area_topplayer, boolean moving) {

		float x;
		float y;

		if (isRotatedBoard()) {

			x = rectangle_area_bottomplayer.left - thinkingBitmap.getWidth() / 2 + (int) (rectangle_area_bottomplayer.right - rectangle_area_bottomplayer.left) / 2;
			y = rectangle_area_bottomplayer.top - thinkingBitmap.getHeight() / 2 + (int) (rectangle_area_bottomplayer.bottom - rectangle_area_bottomplayer.top) / 2;

		} else {

			x = rectangle_area_topplayer.left - thinkingBitmap.getWidth() / 2 + (int) (rectangle_area_topplayer.right - rectangle_area_topplayer.left) / 2;
			y = rectangle_area_topplayer.top - thinkingBitmap.getHeight() / 2 + (int) (rectangle_area_topplayer.bottom - rectangle_area_topplayer.top) / 2;
		}

		if (moving) {

			x += (thinkingBitmap.getWidth() / 5) * (Math.random() - 0.5);
			y += (thinkingBitmap.getHeight() / 5) * (Math.random() - 0.5);
		}

		canvas.drawBitmap(thinkingBitmap, x, y, default_paint);
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

		textarea_menu.draw(canvas);
	}
	
	
	private void drawBottomPlayerPanel(Canvas canvas, Paint paint) {
		
		
		//Draw player panel
		paint.setColor(colour_panel);
		DrawingUtils.drawRoundRectangle(canvas, paint, rectangle_bottom);
		
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

		if (getActivity().getMainView().getBoardView().hasAnimation() != -1) {

			textarea_switch_colors.deactivate();

		} else {

			textarea_switch_colors.activate();
		}

		textarea_switch_colors.draw(canvas);

		textarea_info.draw(canvas);
	}


	private void drawCapturedPieces(Canvas canvas, Paint paint, int[] pieceIDs,
			float left, float top, float imageSize, IBitmapCache cache) {
		
		int distanceBetweenImages = 0;
		
		for (int i=0; i<pieceIDs.length; i++) {

			int pieceID = pieceIDs[i];

			if (pieceID != ID_PIECE_NONE) {

				int imageResID = getActivity().getUIConfiguration().getPiecesConfiguration().getBitmapResID(pieceID);

				IConfigurationPieces piecesCfg = getActivity().getUIConfiguration().getPiecesConfiguration();

				Bitmap bitmap = ((BitmapCacheBase) cache).getBitmap(
						(Context) getActivity(),
						imageResID,
						piecesCfg.getPieceHeightScaleFactor(pieceID),
						piecesCfg.getPieceWidthScaleFactor(pieceID)
				);

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


	private void selectButtonMenu() {
		textarea_menu.select();
	}


	private void deselectButtonMenu() {
		textarea_menu.deselect();
	}


	private void selectButtonHelpTop() {

	}


	private void deselectButtonHelpTop() {
	}


	private void selectButtonHelpBottom() {

	}


	private void deselectButtonHelpBottom() {
	}
	

	private boolean isOverButtonInfo(float x, float y) {
		if (AUTO_BUTTON_BOTTOM_ENABLED) { 
			return rectangle_area_info.contains(x, y);
		} else {
			return false;
		}
	}
	

	private boolean isOverBlackPlayerButton(float x, float y) {

		if (isRotatedBoard()) {

			return rectangle_area_bottomplayer.contains(x, y);

		} else {

			return rectangle_area_topplayer.contains(x, y);

		}
	}


	private boolean isOverWhitePlayerButton(float x, float y) {

		if (isRotatedBoard()) {

			return rectangle_area_topplayer.contains(x, y);

		} else {

			return rectangle_area_bottomplayer.contains(x, y);

		}
	}


	private boolean isOverButtonMenu(float x, float y) {
		return rectangle_area_topright.contains(x, y);
	}


	private boolean isOverButtonAutoTop(float x, float y) {
		return rectangle_area_switch_colors.contains(x, y);
	}
	

	private boolean isOverButtonHelpTop(float x, float y) {
		return false;
	}


	private boolean isOverButtonHelpBottom(float x, float y) {
		return false;
	}


	private boolean isRotatedBoard() {
		return ((UserSettings)Application_Base.getInstance().getUserSettings()).rotatedboard;
	}


	private void disableAndEnableRotateBoardButton() {

		if (getActivity().getMainView().getBoardView().hasAnimation() != -1) {

			textarea_switch_colors.deactivate();

		} else {

			textarea_switch_colors.activate();
		}
	}


	@Override
	public boolean areMovesNavigationButtonsLocked() {

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

			} else if (isOverButtonAutoTop(x, y)) {

				textarea_switch_colors.select();

				if (boardVisualization.hasAnimation() != -1) {

					return;
				}

			} else if (isOverButtonInfo(x, y)) {

				textarea_info.select();

			} else if (isOverButtonHelpTop(x, y)) {

				selectButtonHelpTop();

			} else if (isOverButtonHelpBottom(x, y)) {

				selectButtonHelpBottom();

			} else if (isOverBlackPlayerButton(x, y)) {

				textarea_black_player.select();

			} else if (isOverWhitePlayerButton(x, y)) {

				textarea_white_player.select();

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


			if (isOverButtonAutoTop(x, y)) {

				textarea_switch_colors.select();

			} else {

				textarea_switch_colors.deselect();
			}

			if (isOverButtonInfo(x, y)) {

				textarea_info.select();

			} else {

				textarea_info.deselect();
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

			if (isOverBlackPlayerButton(x, y)) {

				textarea_black_player.select();

			} else {

				textarea_black_player.deselect();
			}

			if (isOverWhitePlayerButton(x, y)) {

				textarea_white_player.select();

			} else {

				textarea_white_player.deselect();
			}


			redraw();
		}


		private void processEvent_UP(MotionEvent event) {

			float x = event.getX();
			float y = event.getY();

			if (isOverButtonMenu(x, y)) {

				Application_Base.getInstance().getSFXManager().playSound(R.raw.sfx_button_pressed_1);

				deselectButtonMenu();

				Intent i = new Intent((Context) activity, activity.getMainMenuClass());
				activity.startActivity(i);

			} else if (isOverButtonAutoTop(x, y)) {

				Application_Base.getInstance().getSFXManager().playSound(R.raw.sfx_button_pressed_1);

				textarea_switch_colors.deselect();

				if (boardVisualization.hasAnimation() != -1) {

					return;
				}


				((UserSettings) Application_Base.getInstance().getUserSettings()).rotatedboard = !((UserSettings) Application_Base.getInstance().getUserSettings()).rotatedboard;
				Application_Base.getInstance().getUserSettings().save();


				activity.getMainView().requestLayout();


			} else if (isOverButtonInfo(x, y)) {

				Application_Base.getInstance().getSFXManager().playSound(R.raw.sfx_button_pressed_1);

				textarea_info.deselect();

				activity.getUserSettings().infoEnabled = !activity.getUserSettings().infoEnabled;

				if (activity.getUserSettings().infoEnabled) {

					textarea_info.activate();

				} else {

					textarea_info.deactivate();
				}

				Application_Base.getInstance().getUserSettings().save();


				activity.getMainView().requestLayout();


			} else if (isOverButtonHelpTop(x, y)) {

				//System.out.println("PanelsView.isOverButtonHelpTop");

				Application_Base.getInstance().getSFXManager().playSound(R.raw.sfx_button_pressed_1);

				deselectButtonHelpTop();

			} else if (isOverButtonHelpBottom(x, y)) {

				//System.out.println("PanelsView.isOverButtonHelpBottom");

				Application_Base.getInstance().getSFXManager().playSound(R.raw.sfx_button_pressed_1);

				deselectButtonHelpBottom();

			} else if (isOverBlackPlayerButton(x, y)) {

				Application_Base.getInstance().getSFXManager().playSound(R.raw.sfx_button_pressed_1);

				textarea_black_player.deselect();

				UserSettings settings = activity.getUserSettings();

				//System.out.println("PanelsView.isOverBlackPlayerButton: clicked settings.auto_player_enabled_black=" + settings.auto_player_enabled_black);

				if (!settings.auto_player_enabled_black) {

					//Auto player switched on
					activity.getGameController().switchOnAutoPlayer(COLOUR_PIECE_BLACK);

					textarea_black_player.activate();

				} else {

					//Auto player switched off
					activity.getGameController().switchOffAutoPlayer(COLOUR_PIECE_BLACK);

					//Enable human player to manually select piece and make move via UI.
					//The UI of the board should be unlocked for this purpose.
					activity.getMainView().getBoardView().unlock();

					textarea_black_player.deactivate();
				}

				textarea_black_player.setText(getActivity().getBoardManager().getGameData().getBlack().getName());

				activity.getMainView().requestLayout();

			} else if (isOverWhitePlayerButton(x, y)) {

				Application_Base.getInstance().getSFXManager().playSound(R.raw.sfx_button_pressed_1);

				textarea_white_player.deselect();

				UserSettings settings = activity.getUserSettings();

				//System.out.println("PanelsView.isOverWhitePlayerButton: clicked settings.auto_player_enabled_white=" + settings.auto_player_enabled_white);

				if (!settings.auto_player_enabled_white) {

					//Auto player switched on
					activity.getGameController().switchOnAutoPlayer(COLOUR_PIECE_WHITE);

					if (textarea_white_player.isActive()) {

						throw new IllegalStateException();
					}

					textarea_white_player.activate();

				} else {

					//Auto player switched off
					activity.getGameController().switchOffAutoPlayer(COLOUR_PIECE_WHITE);

					//Enable human player to manually select piece and make move via UI.
					//The UI of the board should be unlocked for this purpose.
					activity.getMainView().getBoardView().unlock();

					if (!textarea_white_player.isActive()) {

						throw new IllegalStateException();
					}

					textarea_white_player.deactivate();
				}

				textarea_white_player.setText(getActivity().getBoardManager().getGameData().getWhite().getName());

				activity.getMainView().requestLayout();

			}


			invalidate();

			redraw();
		}
	}
}
