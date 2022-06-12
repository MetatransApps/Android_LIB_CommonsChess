package org.metatrans.commons.chess.views_and_controllers;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import java.util.Iterator;
import java.util.Set;

import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.cfg.colours.ConfigurationUtils_Colours;
import org.metatrans.commons.cfg.colours.IConfigurationColours;
import org.metatrans.commons.chess.GlobalConstants;
import org.metatrans.commons.chess.R;
import org.metatrans.commons.chess.cfg.animation.Config_Animation_Base;
import org.metatrans.commons.chess.cfg.pieces.IConfigurationPieces;
import org.metatrans.commons.chess.logic.BoardConstants;
import org.metatrans.commons.chess.logic.board.BoardUtils;
import org.metatrans.commons.chess.logic.computer.ComputerPlayer_Engine;
import org.metatrans.commons.chess.logic.game.GameDataUtils;
import org.metatrans.commons.chess.logic.board.IBoardManager;
import org.metatrans.commons.chess.menu.MenuActivity_Promotion;
import org.metatrans.commons.chess.model.FieldSelection;
import org.metatrans.commons.chess.model.Move;
import org.metatrans.commons.chess.model.MovingPiece;
import org.metatrans.commons.chess.model.SearchInfo;
import org.metatrans.commons.chess.model.UserSettings;
import org.metatrans.commons.chess.utils.CachesBitmap;
import org.metatrans.commons.engagement.leaderboards.View_Achievements_And_Leaderboards_Base;
import org.metatrans.commons.ui.ButtonAreaClick_Image;
import org.metatrans.commons.ui.IButtonArea;
import org.metatrans.commons.ui.TextArea;
import org.metatrans.commons.ui.images.BitmapCacheBase;
import org.metatrans.commons.ui.utils.BitmapUtils;
import org.metatrans.commons.ui.utils.DrawingUtils;


public class BoardView extends BaseView implements BoardConstants, IBoardVisualization {
	
	
	protected Paint blackPaint1;
	protected Paint whitePaint1;
	protected Paint blackPaint2;
	protected Paint whitePaint2;
	
	protected Paint[][] paints_board_fields;
	protected Paint[][] paints_board_pieces;
	protected Paint paint_movingPiece;
	
	protected Paint default_paint;
	
	protected int[][] pieces;
	
	protected Set<FieldSelection>[][] selections;
	
	public RectF rectangleBoard;
	
	private RectF topMessageText_rect;
	private TextArea topMessageText_area;
	private String topMessageText;
	private RectF rect_leaderboards;
	private RectF rect_replay;
	private IButtonArea buttonarea_replay;
	private View_Achievements_And_Leaderboards_Base view_leaderboards;
	
	private MoveAnimation animation;
	private IAnimationHandler animationHandler;
	
	
	private RectF rectf_file_a;
	private RectF rectf_file_b;
	private RectF rectf_file_c;
	private RectF rectf_file_d;
	private RectF rectf_file_e;
	private RectF rectf_file_f;
	private RectF rectf_file_g;
	private RectF rectf_file_h;
	
	private TextArea text_file_a;
	private TextArea text_file_b;
	private TextArea text_file_c;
	private TextArea text_file_d;
	private TextArea text_file_e;
	private TextArea text_file_f;
	private TextArea text_file_g;
	private TextArea text_file_h;
	
	private RectF rectf_rank_1;
	private RectF rectf_rank_2;
	private RectF rectf_rank_3;
	private RectF rectf_rank_4;
	private RectF rectf_rank_5;
	private RectF rectf_rank_6;
	private RectF rectf_rank_7;
	private RectF rectf_rank_8;
	
	private TextArea text_rank_1;
	private TextArea text_rank_2;
	private TextArea text_rank_3;
	private TextArea text_rank_4;
	private TextArea text_rank_5;
	private TextArea text_rank_6;
	private TextArea text_rank_7;
	private TextArea text_rank_8;
	
	
	public BoardView(Context context, View _parent, RectF _rectangle) {
		
		super(context, _parent);
		
		rectangleBoard = _rectangle;
		
		animationHandler = createAnimationHandler();
		
		selections = GameDataUtils.createEmptySelections();
		
		
		blackPaint1 = new Paint();
		blackPaint1.setColor(getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_Black());
		
		whitePaint1 = new Paint();
		whitePaint1.setColor(getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_White());
		
		blackPaint2 = new Paint();
		whitePaint2 = new Paint();
		
		default_paint = new Paint();
		
		paints_board_fields = new Paint[][] {
				{ whitePaint1, blackPaint1, whitePaint1, blackPaint1,
						whitePaint1, blackPaint1, whitePaint1, blackPaint1 },
				{ blackPaint1, whitePaint1, blackPaint1, whitePaint1,
						blackPaint1, whitePaint1, blackPaint1, whitePaint1 },
				{ whitePaint1, blackPaint1, whitePaint1, blackPaint1,
						whitePaint1, blackPaint1, whitePaint1, blackPaint1 },
				{ blackPaint1, whitePaint1, blackPaint1, whitePaint1,
						blackPaint1, whitePaint1, blackPaint1, whitePaint1 },
				{ whitePaint1, blackPaint1, whitePaint1, blackPaint1,
						whitePaint1, blackPaint1, whitePaint1, blackPaint1 },
				{ blackPaint1, whitePaint1, blackPaint1, whitePaint1,
						blackPaint1, whitePaint1, blackPaint1, whitePaint1 },
				{ whitePaint1, blackPaint1, whitePaint1, blackPaint1,
						whitePaint1, blackPaint1, whitePaint1, blackPaint1 },
				{ blackPaint1, whitePaint1, blackPaint1, whitePaint1,
						blackPaint1, whitePaint1, blackPaint1, whitePaint1 }, };
		
		paints_board_pieces = new Paint[][] {
				{ whitePaint2, blackPaint2, whitePaint2, blackPaint2,
						whitePaint2, blackPaint2, whitePaint2, blackPaint2 },
				{ blackPaint2, whitePaint2, blackPaint2, whitePaint2,
						blackPaint2, whitePaint2, blackPaint2, whitePaint2 },
				{ whitePaint2, blackPaint2, whitePaint2, blackPaint2,
						whitePaint2, blackPaint2, whitePaint2, blackPaint2 },
				{ blackPaint2, whitePaint2, blackPaint2, whitePaint2,
						blackPaint2, whitePaint2, blackPaint2, whitePaint2 },
				{ whitePaint2, blackPaint2, whitePaint2, blackPaint2,
						whitePaint2, blackPaint2, whitePaint2, blackPaint2 },
				{ blackPaint2, whitePaint2, blackPaint2, whitePaint2,
						blackPaint2, whitePaint2, blackPaint2, whitePaint2 },
				{ whitePaint2, blackPaint2, whitePaint2, blackPaint2,
						whitePaint2, blackPaint2, whitePaint2, blackPaint2 },
				{ blackPaint2, whitePaint2, blackPaint2, whitePaint2,
						blackPaint2, whitePaint2, blackPaint2, whitePaint2 }, };
		
		paint_movingPiece = new Paint();
		
		pieces = new int[8][8];
		
		
		topMessageText_rect = new RectF();
		rect_leaderboards 	= new RectF();
		rect_replay			= new RectF();
		
		rectf_file_a = new RectF();
		rectf_file_b = new RectF();
		rectf_file_c = new RectF();
		rectf_file_d = new RectF();
		rectf_file_e = new RectF();
		rectf_file_f = new RectF();
		rectf_file_g = new RectF();
		rectf_file_h = new RectF();
		
		rectf_rank_1 = new RectF();
		rectf_rank_2 = new RectF();
		rectf_rank_3 = new RectF();
		rectf_rank_4 = new RectF();
		rectf_rank_5 = new RectF();
		rectf_rank_6 = new RectF();
		rectf_rank_7 = new RectF();
		rectf_rank_8 = new RectF();
		
		//System.out.println("New BoardView created: " + this);
	}
	
	
	protected IAnimationHandler createAnimationHandler() {
		return new AnimationHandlerImpl();
	}
	
	
	public void init() {

		System.out.println("OPA=" + rectangleBoard.left + " " + rectangleBoard.right);

		//(new Exception()).printStackTrace();

		float MARGIN = 9;
		
		float board_size_x = rectangleBoard.right - rectangleBoard.left;
		float board_size_y = rectangleBoard.bottom - rectangleBoard.top;
		
		topMessageText_rect.left = rectangleBoard.left;
		topMessageText_rect.right = rectangleBoard.right;
		topMessageText_rect.top = rectangleBoard.top + board_size_y / 2 - getSquareSize() / 2;
		topMessageText_rect.bottom = rectangleBoard.top + board_size_y / 2 + getSquareSize() / 2;
		
		rect_leaderboards.left = rectangleBoard.left + 1 * (board_size_x) / 5 + MARGIN;
		rect_leaderboards.right = rectangleBoard.left + 4 * (board_size_x) / 5 + MARGIN;
		rect_leaderboards.bottom = topMessageText_rect.top - MARGIN;
		rect_leaderboards.top = rect_leaderboards.bottom - (rect_leaderboards.right - rect_leaderboards.left) / 3;
		
		rect_replay.left = rectangleBoard.left + 2 * (board_size_x) / 5;
		rect_replay.right = rectangleBoard.left + 3 * (board_size_x) / 5;
		rect_replay.top = topMessageText_rect.bottom + MARGIN;//rect_replay.bottom - (rect_replay.right - rect_replay.left);
		rect_replay.bottom = rect_replay.top + (rect_replay.right - rect_replay.left);
		
		
		buttonarea_replay = new ButtonAreaClick_Image(rect_replay,
				BitmapUtils.fromResource(getContext(), R.drawable.ic_action_turn_right_white),
				getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_ValidSelection(),
				getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_MarkingSelection(),
				false
			);
		
		
		int colourRankFile = getActivity().getUIConfiguration().getColoursConfiguration().getColour_Delimiter();
		
		rectf_file_a.top = getY(isRotatedBoard() ? 0 : 7) + 2 * getSquareSize() / 3;
		rectf_file_a.left = getX(0);
		rectf_file_a.right = rectf_file_a.left + getSquareSize() / 3;
		rectf_file_a.bottom = rectf_file_a.top + getSquareSize() / 3;
		
		text_file_a = new TextArea(rectf_file_a, "a", colourRankFile);
		
		rectf_file_b.top = getY(isRotatedBoard() ? 0 : 7) + 2 * getSquareSize() / 3;
		rectf_file_b.left = getX(1);
		rectf_file_b.right = rectf_file_b.left + getSquareSize() / 3;
		rectf_file_b.bottom = rectf_file_b.top + getSquareSize() / 3;
		
		text_file_b = new TextArea(rectf_file_b, "b", colourRankFile);
		
		rectf_file_c.top = getY(isRotatedBoard() ? 0 : 7) + 2 * getSquareSize() / 3;
		rectf_file_c.left = getX(2);
		rectf_file_c.right = rectf_file_c.left + getSquareSize() / 3;
		rectf_file_c.bottom = rectf_file_c.top + getSquareSize() / 3;
		
		text_file_c = new TextArea(rectf_file_c, "c", colourRankFile);
		
		rectf_file_d.top = getY(isRotatedBoard() ? 0 : 7) + 2 * getSquareSize() / 3;
		rectf_file_d.left = getX(3);
		rectf_file_d.right = rectf_file_d.left + getSquareSize() / 3;
		rectf_file_d.bottom = rectf_file_d.top + getSquareSize() / 3;
		
		text_file_d = new TextArea(rectf_file_d, "d", colourRankFile);
		
		rectf_file_e.top = getY(isRotatedBoard() ? 0 : 7) + 2 * getSquareSize() / 3;
		rectf_file_e.left = getX(4);
		rectf_file_e.right = rectf_file_e.left + getSquareSize() / 3;
		rectf_file_e.bottom = rectf_file_e.top + getSquareSize() / 3;
		
		text_file_e = new TextArea(rectf_file_e, "e", colourRankFile);
		
		rectf_file_f.top = getY(isRotatedBoard() ? 0 : 7) + 2 * getSquareSize() / 3;
		rectf_file_f.left = getX(5);
		rectf_file_f.right = rectf_file_f.left + getSquareSize() / 3;
		rectf_file_f.bottom = rectf_file_f.top + getSquareSize() / 3;
		
		text_file_f = new TextArea(rectf_file_f, "f", colourRankFile);
		
		rectf_file_g.top = getY(isRotatedBoard() ? 0 : 7) + 2 * getSquareSize() / 3;
		rectf_file_g.left = getX(6);
		rectf_file_g.right = rectf_file_g.left + getSquareSize() / 3;
		rectf_file_g.bottom = rectf_file_g.top + getSquareSize() / 3;
		
		text_file_g = new TextArea(rectf_file_g, "g", colourRankFile);
		
		rectf_file_h.top = getY(isRotatedBoard() ? 0 : 7) + 2 * getSquareSize() / 3;
		rectf_file_h.left = getX(7);
		rectf_file_h.right = rectf_file_h.left + getSquareSize() / 3;
		rectf_file_h.bottom = rectf_file_h.top + getSquareSize() / 3;
		
		text_file_h = new TextArea(rectf_file_h, "h", colourRankFile);
		
		
		rectf_rank_1.top = getY(7);
		rectf_rank_1.left = getX(isRotatedBoard() ? 0 : 7) + 2 * getSquareSize() / 3;
		rectf_rank_1.right = rectf_rank_1.left + getSquareSize() / 3;
		rectf_rank_1.bottom = rectf_rank_1.top + getSquareSize() / 3;
		
		text_rank_1 = new TextArea(rectf_rank_1, "1", colourRankFile);
		
		rectf_rank_2.top = getY(6);
		rectf_rank_2.left = getX(isRotatedBoard() ? 0 : 7) + 2 * getSquareSize() / 3;
		rectf_rank_2.right = rectf_rank_2.left + getSquareSize() / 3;
		rectf_rank_2.bottom = rectf_rank_2.top + getSquareSize() / 3;
		
		text_rank_2 = new TextArea(rectf_rank_2, "2", colourRankFile);
		
		rectf_rank_3.top = getY(5);
		rectf_rank_3.left = getX(isRotatedBoard() ? 0 : 7) + 2 * getSquareSize() / 3;
		rectf_rank_3.right = rectf_rank_3.left + getSquareSize() / 3;
		rectf_rank_3.bottom = rectf_rank_3.top + getSquareSize() / 3;
		
		text_rank_3 = new TextArea(rectf_rank_3, "3", colourRankFile);
		
		rectf_rank_4.top = getY(4);
		rectf_rank_4.left = getX(isRotatedBoard() ? 0 : 7) + 2 * getSquareSize() / 3;
		rectf_rank_4.right = rectf_rank_4.left + getSquareSize() / 3;
		rectf_rank_4.bottom = rectf_rank_4.top + getSquareSize() / 3;
		
		text_rank_4 = new TextArea(rectf_rank_4, "4", colourRankFile);
		
		rectf_rank_5.top = getY(3);
		rectf_rank_5.left = getX(isRotatedBoard() ? 0 : 7) + 2 * getSquareSize() / 3;
		rectf_rank_5.right = rectf_rank_5.left + getSquareSize() / 3;
		rectf_rank_5.bottom = rectf_rank_5.top + getSquareSize() / 3;
		
		text_rank_5 = new TextArea(rectf_rank_5, "5", colourRankFile);
		
		rectf_rank_6.top = getY(2);
		rectf_rank_6.left = getX(isRotatedBoard() ? 0 : 7) + 2 * getSquareSize() / 3;
		rectf_rank_6.right = rectf_rank_6.left + getSquareSize() / 3;
		rectf_rank_6.bottom = rectf_rank_6.top + getSquareSize() / 3;
		
		text_rank_6 = new TextArea(rectf_rank_6, "6", colourRankFile);
		
		rectf_rank_7.top = getY(1);
		rectf_rank_7.left = getX(isRotatedBoard() ? 0 : 7) + 2 * getSquareSize() / 3;
		rectf_rank_7.right = rectf_rank_7.left + getSquareSize() / 3;
		rectf_rank_7.bottom = rectf_rank_7.top + getSquareSize() / 3;
		
		text_rank_7 = new TextArea(rectf_rank_7, "7", colourRankFile);
		
		rectf_rank_8.top = getY(0);
		rectf_rank_8.left = getX(isRotatedBoard() ? 0 : 7) + 2 * getSquareSize() / 3;
		rectf_rank_8.right = rectf_rank_8.left + getSquareSize() / 3;
		rectf_rank_8.bottom = rectf_rank_8.top + getSquareSize() / 3;
		
		text_rank_8 = new TextArea(rectf_rank_8, "8", colourRankFile);
	}
	
	
	private void createLeaderBoardsView() {

		if (Application_Base.getInstance().getEngagementProvider().getLeaderboardsProvider() != null) {

			Application_Base.getInstance().getEngagementProvider().getLeaderboardsProvider().detachLeaderboardView(null);

			IConfigurationColours coloursCfg = ConfigurationUtils_Colours.getConfigByID(Application_Base.getInstance().getUserSettings().uiColoursID);

			final View _view_leaderboards = Application_Base.getInstance().getEngagementProvider().getLeaderboardsProvider().getLeaderboardView(coloursCfg, getRect_leaderboards());

			View _view_achievements = Application_Base.getInstance().getEngagementProvider().getAchievementsProvider().getAchievementsView(coloursCfg, getRect_leaderboards());

			if (_view_leaderboards != null && _view_achievements != null) {
				if (_view_leaderboards != _view_achievements) {
					throw new IllegalStateException("_view_leaderboards != _view_achievements");
				}
			}

			((View_Achievements_And_Leaderboards_Base)_view_leaderboards).measure(0, 0);

			view_leaderboards = (View_Achievements_And_Leaderboards_Base) _view_leaderboards;
		}
	}
	
	
	public View_Achievements_And_Leaderboards_Base getLeaderboard() {
		return view_leaderboards;
	}
	
	
	public boolean isOverLeaderBoards(float x, float y) {
		return rect_leaderboards.contains(x, y);
	}
	
	
	public boolean isOverBottomReplay(float x, float y) {
		return rect_replay.contains(x, y);
	}
	
	
	public void selectButtonReplay() {
		buttonarea_replay.select();
	}
	
	
	public void deselectButtonReplay() {
		buttonarea_replay.deselect();
	}
	
	
	public float getSquareSize() {
		return (rectangleBoard.right - rectangleBoard.left) / (float) 8;
	}
	
	
	protected float getX(int letter) {
		if (isRotatedBoard()) {
			return rectangleBoard.left + getSquareSize() * (float) (7 - letter);
		} else {
			return rectangleBoard.left + getSquareSize() * (float) letter;
		}
	}
	
	
	protected float getY(int digit) {
		if (isRotatedBoard()) {
			return rectangleBoard.top + getSquareSize() * (float) (7 - digit);
		} else {
			return rectangleBoard.top + getSquareSize() * (float) digit;
		}
	}
	
	
	public boolean isOverBoard(float x, float y) {
		boolean in = rectangleBoard.contains(x, y);
		//System.out.println("BOARD: " + x + ", " + y + ", " + rectangleBoard + "	in=" + in);
		return in;
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		super.onDraw(canvas);


		for (int letter = 0; letter < 8; letter++) {
			for (int digit = 0; digit < 8; digit++) {

				float x = getX(letter);
				float y = getY(digit);
				
				// Draw field
				Paint paint_field = paints_board_fields[letter][digit];
				canvas.drawRect((int)x, (int)y, (int)(x + getSquareSize()), (int)(y + getSquareSize()),
						paint_field);
				
				// Draw field selections
				boolean tempSelection = false;
				Set<FieldSelection> selectionsList = selections[letter][digit];
				
				synchronized (selectionsList) {
					Iterator<FieldSelection> i = selectionsList.iterator();
					while (i.hasNext()) {
						FieldSelection selection = i.next();
						// System.out.println("selection priority: " +
						// selection.priority);
						int colour = selection.colour;
						if (selection.shape == FieldSelection.SHAPE_BORDER) {
							drawBorderSelection1(canvas, (int)x, (int)y, getSquareSize(), colour);
						} else if (selection.shape == FieldSelection.SHAPE_SQUARE) {
							drawSquareSelection(canvas, x, y, colour);
						} else {
							throw new IllegalStateException("selection.shape="
									+ selection.shape);
						}
	
						if (selection.appearace == FieldSelection.APPEARANCE_TEMP) {
							i.remove();
							tempSelection = true;
						}
					}
				}
				
				if (tempSelection)
					invalidate();
				
				// Draw piece, if any
				int pieceID = pieces[letter][digit];
				if (pieceID != ID_PIECE_NONE) {
					Paint paint_piece = paints_board_pieces[letter][digit];
					drawPiece(canvas, paint_piece, getX(letter), getY(digit), pieceID, true);
				}
			}
		}
		
		
		text_file_a.draw(canvas);
		text_file_b.draw(canvas);
		text_file_c.draw(canvas);
		text_file_d.draw(canvas);
		text_file_e.draw(canvas);
		text_file_f.draw(canvas);
		text_file_g.draw(canvas);
		text_file_h.draw(canvas);
		
		text_rank_1.draw(canvas);
		text_rank_2.draw(canvas);
		text_rank_3.draw(canvas);
		text_rank_4.draw(canvas);
		text_rank_5.draw(canvas);
		text_rank_6.draw(canvas);
		text_rank_7.draw(canvas);
		text_rank_8.draw(canvas);


		//If there is an animation than draw it.
		if (animation != null) {
			
			drawPiece(canvas, paint_movingPiece, animation.movingPiece.x, animation.movingPiece.y, animation.movingPiece.pieceID, true);

			animation.updateCoordinates();
			
			if (animation.isDone()) {

				animationHandler.animationIsDone(animation);

				endMoveAnimation();
			}
			
			invalidate();

		} else {

			// Draw moving piece, if any
			MovingPiece movingPiece = getMovingPiece();

			if (movingPiece != null) {

				if (movingPiece.dragging) {

					if (movingPiece.over_invalid_square_selection != null) {

						drawSquareSelection(canvas, getX(getLetter(movingPiece.x)), getY(getDigit(movingPiece.y)), movingPiece.over_invalid_square_selection.colour);
					}

					drawPiece(canvas, paint_movingPiece, movingPiece.x, movingPiece.y, movingPiece.pieceID, false);
				}
			}

			invalidate();
		}
		
		String text = topMessageText;
		if (text == null) {
			text = getActivity().getBoardManager().getGameData().getBoarddata().gameResultText;
		}
		
		if (text != null) {
			if (topMessageText_area == null || !topMessageText_area.getText().equals(text)) {
				topMessageText_area = new TextArea(topMessageText_rect, text, 0,
						//getMainActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_MarkingSelection()
						Color.WHITE
						);
			}
			topMessageText_area.draw(canvas);
		}
		
		if (getActivity().getBoardManager().getGameStatus() != GlobalConstants.GAME_STATUS_NONE) {
			
			if (showReplayAndLeaderboards()) {
				
				buttonarea_replay.draw(canvas);
				
				if (view_leaderboards == null) {
					createLeaderBoardsView();
				}

				if (view_leaderboards != null) {
					view_leaderboards.draw(canvas);
				}
			}
			
		} else {
			
			if (view_leaderboards != null) {
				Application_Base.getInstance().getEngagementProvider().getLeaderboardsProvider().detachLeaderboardView(null);
				view_leaderboards = null;
			}
		}
	}
	
	
	private void drawPiece(Canvas canvas, Paint paint_piece, float x, float y, int pieceID, boolean infield) {

		IConfigurationPieces piecesCfg = getActivity().getUIConfiguration().getPiecesConfiguration();

		int imageResID = piecesCfg.getBitmapResID(pieceID);

		Bitmap bitmap = ((BitmapCacheBase) CachesBitmap.getSingletonPiecesBoard((int) getSquareSize())).getBitmap(
				(Context) getActivity(),
				imageResID,
				piecesCfg.getPieceHeightScaleFactor(pieceID),
				piecesCfg.getPieceWidthScaleFactor(pieceID)
			);

		if (bitmap == null) {

			throw new IllegalStateException("bitmap == null");

			//bitmap = BitmapUtils.fromResource(getContext(), imageResID);

			//bitmap = BitmapUtils.cropTransparantPart(bitmap);

			//CachesBitmap.getSingletonPiecesBoard((int) getSquareSize()).addBitmap(imageResID, bitmap);
		}

		//Bitmap bitmap = getActivity().getUIConfiguration().getPiecesConfiguration().getPiece(pieceID);

		//Draw piece bitmap
		if (infield) {
			DrawingUtils.drawInField(canvas, paint_piece, getSquareSize(), x, y, bitmap);
		} else {
			DrawingUtils.drawInCenter(canvas, paint_piece, getSquareSize(), x, y, bitmap);
		}
	}
	
	
	protected boolean showReplayAndLeaderboards() {
		return true;
	}
	
	
	@Override
	public void setTopMessageText(String text) {
		if (text == null) {
			topMessageText = null;
			topMessageText_area = null;
		} else {
			topMessageText = text;
		}
	}
	
		
	private void drawSquareSelection(Canvas canvas, float x, float y, int colour) {

		Paint paint = default_paint;//new Paint();
		paint.setColor(colour);
		canvas.drawRect((int)x, (int)y, (int)(x + getSquareSize()), (int)(y + getSquareSize()), paint);
	}

	
	private void drawBorderSelection1(Canvas canvas, int left, int top, float squaresize, int colour) {

		Paint frame = default_paint;//new Paint();
		frame.setColor(colour);

		//canvas.drawLine(left, top, left + squareSize, top, frame);
		canvas.drawLine(left + 1, top + 1, left + (int) squaresize - 1, top + 1, frame);
		canvas.drawLine(left + 1, top + 2, left + (int) squaresize - 1, top + 2, frame);
		canvas.drawLine(left + 1, top + 3, left + (int) squaresize - 1, top + 3, frame);
		//canvas.drawLine(left + 1, top + 4, left + (int) squareSize - 1, top + 4, frame);
		
		//canvas.drawLine(left + (int) squareSize, top + 1, left + (int) squareSize, top
		//		+ (int) squareSize - 1, frame);
		//canvas.drawLine(left + (int) squareSize - 1, top + 1, left + (int) squareSize - 1, top
		//		+ (int) squareSize - 1, frame);
		canvas.drawLine(left + (int) squaresize - 2, top + 1, left + (int) squaresize - 2, top
				+ (int) squaresize - 1, frame);
		canvas.drawLine(left + (int) squaresize - 3, top + 1, left + (int) squaresize - 3, top
				+ (int) squaresize - 1, frame);
		canvas.drawLine(left + (int) squaresize - 4, top + 1, left + (int) squaresize - 4, top
				+ (int) squaresize - 1, frame);
		
		
		//canvas.drawLine(left + (int) squareSize - 1, top + (int) squareSize, left + 1, top
		//		+ (int) squareSize, frame);
		//canvas.drawLine(left + (int) squareSize - 1, top + (int) squareSize - 1, left + 1, top
		//		+ (int) squareSize - 1, frame);
		canvas.drawLine(left + (int) squaresize - 1, top + (int) squaresize - 2, left + 1, top
				+ (int) squaresize - 2, frame);
		canvas.drawLine(left + (int) squaresize - 1, top + (int) squaresize - 3, left + 1, top
				+ (int) squaresize - 3, frame);
		canvas.drawLine(left + (int) squaresize - 1, top + (int) squaresize - 4, left + 1, top
				+ (int) squaresize - 4, frame);		
		
		//canvas.drawLine(left, top + squareSize, left, top, frame);
		canvas.drawLine(left + 1, top + (int) squaresize - 1, left + 1, top + 1, frame);
		canvas.drawLine(left + 2, top + (int) squaresize - 1, left + 2, top + 1, frame);
		canvas.drawLine(left + 3, top + (int) squaresize - 1, left + 3, top + 1, frame);
		//canvas.drawLine(left + 4, top + (int)squareSize - 1, left + 4, top + 1, frame);
		
	}


	private MovingPiece getMovingPiece() {
		return getBoardManager().getMovingPiece();
	}


	private IBoardManager getBoardManager() {
		return ((IBoardViewActivity)getContext()).getBoardManager();
	}


	@Override
	public int getLetter(float x) {
		if (isRotatedBoard()) {
			float result = (rectangleBoard.right - x) / getSquareSize();
			return (int) Math.floor(result);
		} else {
			float result = (x - rectangleBoard.left) / getSquareSize();
			return (int) Math.floor(result);
		}
	}


	@Override
	public int getDigit(float y) {
		if (isRotatedBoard()) {
			float result = (rectangleBoard.bottom - y) / getSquareSize();
			return (int) Math.floor(result);
		} else {
			float result = (y - rectangleBoard.top) / getSquareSize();
			return (int) Math.floor(result);
		}
	}


	public void clearSelections() {
		for (int i = 0; i < selections.length; i++) {
			Set<FieldSelection>[] cur = selections[i];
			for (int j = 0; j < cur.length; j++) {
				cur[j].clear();
			}
		}
	}


	public void makeMovingPiece_OnInvalidSquare() {

		MovingPiece movingPiece = getMovingPiece();

		if (movingPiece != null) {

			boolean over_correct_square = false;

			int current_letter = getLetter(movingPiece.x);

			int current_digit =  getDigit(movingPiece.y);

			if (current_letter == movingPiece.initial_letter && current_digit == movingPiece.initial_digit) {

				over_correct_square = true;
			}

			if (!over_correct_square) {

				if (movingPiece.moves.size() > 0) {

					if (getBoardManager().selectPossibleFields()) {

						for (Move move : movingPiece.moves) {

							if (current_letter == move.toLetter && current_digit == move.toDigit) {

								over_correct_square = true;

								break;
							}
						}
					}

				} else {

					over_correct_square = false;
				}
			}

			if (!over_correct_square) {

				FieldSelection selection = new FieldSelection();
				selection.priority = 1;
				selection.colour = getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_InvalidSelection();
				selection.shape = FieldSelection.SHAPE_SQUARE;
				selection.appearace = FieldSelection.APPEARANCE_PERMANENT;

				movingPiece.over_invalid_square_selection = selection;
			}

			redraw();
		}
	}


	public void makeMovingPieceSelections() {
		
		MovingPiece movingPiece = getMovingPiece();
		
		if (movingPiece != null) {

			if (movingPiece.moves.size() > 0) {

				validSelection_Permanent(movingPiece.initial_letter, movingPiece.initial_digit);

				if (getBoardManager().selectPossibleFields()) {

					for (Move move: movingPiece.moves) {

						validSelection_Permanent_Border(move.toLetter, move.toDigit);
					}
				}

			} else {

				invalidSelection_Permanent_Square(movingPiece.initial_letter, movingPiece.initial_digit);

				makeMovablePiecesSelections();
			}

			redraw();
		}
	}


	@Override
	public void makeMovablePiecesSelections() {

		int[][] result = getBoardManager().getMovablePieces();
		
		for (int letter = 0; letter < 8; letter++) {
			for (int digit = 0; digit < 8; digit++) {
				if (result[letter][digit] != 0) {
					validSelection_Temp_Square(letter, digit);
				}
			}
		}
	}


	@Override
	public void invalidSelection_Temp_Square(int letter, int digit) {

		FieldSelection selection = new FieldSelection();
		selection.colour = getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_InvalidSelection();
		selection.shape = FieldSelection.SHAPE_SQUARE;
		selection.appearace = FieldSelection.APPEARANCE_TEMP;
		addSelection(letter, digit, selection);
	}


	@Override
	public void validSelection_Temp(int letter, int digit) {

		FieldSelection selection = new FieldSelection();
		selection.colour = getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_ValidSelection();
		selection.shape = FieldSelection.SHAPE_BORDER;
		selection.appearace = FieldSelection.APPEARANCE_TEMP;
		addSelection(letter, digit, selection);
	}


	@Override
	public void validSelection_Temp_Square(int letter, int digit) {

		FieldSelection selection = new FieldSelection();
		selection.colour = getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_ValidSelection();
		selection.shape = FieldSelection.SHAPE_SQUARE;
		selection.appearace = FieldSelection.APPEARANCE_TEMP;
		addSelection(letter, digit, selection);
	}


	@Override
	public void validSelection_Permanent_Square(int letter, int digit) {

		FieldSelection selection = new FieldSelection();
		selection.colour = getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_ValidSelection();
		selection.shape = FieldSelection.SHAPE_SQUARE;
		selection.appearace = FieldSelection.APPEARANCE_PERMANENT;
		addSelection(letter, digit, selection);
	}


	@Override
	public void validSelection_Permanent_Border(int letter, int digit) {

		FieldSelection selection = new FieldSelection();
		selection.colour = getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_ValidSelection();
		selection.shape = FieldSelection.SHAPE_BORDER;
		selection.appearace = FieldSelection.APPEARANCE_PERMANENT;
		addSelection(letter, digit, selection);
	}


	@Override
	public void validSelection_Permanent(int letter, int digit) {
		
		FieldSelection selection = new FieldSelection();
		selection.priority = 1;
		selection.colour = getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_ValidSelection();
		selection.shape = FieldSelection.SHAPE_SQUARE;
		selection.appearace = FieldSelection.APPEARANCE_PERMANENT;
		addSelection(letter, digit, selection);
	}


	@Override
	public void invalidSelection_Permanent_Square(int letter, int digit) {

		FieldSelection selection = new FieldSelection();
		selection.priority = 1;
		selection.colour = getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_InvalidSelection();
		selection.shape = FieldSelection.SHAPE_SQUARE;
		selection.appearace = FieldSelection.APPEARANCE_PERMANENT;
		addSelection(letter, digit, selection);
	}


	@Override
	public void invalidSelection_Permanent_Border(int letter, int digit) {

		FieldSelection selection = new FieldSelection();
		selection.priority = 1;
		selection.colour = getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_InvalidSelection();
		selection.shape = FieldSelection.SHAPE_BORDER;
		selection.appearace = FieldSelection.APPEARANCE_PERMANENT;
		addSelection(letter, digit, selection);
	}


	@Override
	public void markingSelection_Permanent_Square(int letter, int digit) {
		FieldSelection selection = new FieldSelection();
		selection.priority = 1;
		selection.colour = getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_MarkingSelection();
		selection.shape = FieldSelection.SHAPE_SQUARE;
		selection.appearace = FieldSelection.APPEARANCE_PERMANENT;
		addSelection(letter, digit, selection);
	}


	@Override
	public void markingSelection_Permanent_Border(int letter, int digit) {
		FieldSelection selection = new FieldSelection();
		selection.priority = 1;
		selection.colour = getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_MarkingSelection();
		selection.shape = FieldSelection.SHAPE_BORDER;
		selection.appearace = FieldSelection.APPEARANCE_PERMANENT;
		addSelection(letter, digit, selection);
	}
	
	
	@Override
	public void addSelection(int letter, int digit, FieldSelection selection) {

		selections[letter][digit].add(selection);
	}


	@Override
	public void clearSelections(int letter, int digit) {

		MovingPiece movingPiece = getMovingPiece();

		if (movingPiece != null) {

			movingPiece.over_invalid_square_selection = null;
		}

		selections[letter][digit].clear();
	}


	@Override
	public void setData(int[][] _pieces) {
		for (int i = 0; i < pieces.length; i++) {
			for (int j = 0; j < pieces[0].length; j++) {
				pieces[i][j] = _pieces[i][j];
			}
		}
	}
	
	
	public int[][] getData() {
		return pieces;
	}
	
	
	public Set<FieldSelection>[][] getSelections() {
		return selections;
	}


	public void setSelections(Set<FieldSelection>[][] selections) {
		this.selections = selections;
	}


	/*public void selectMovingPiece() {
		MovingPiece movingPiece = getMovingPiece();
		if (movingPiece != null) {
			validSelection_Permanent(movingPiece.initial_letter, movingPiece.initial_digit);
			if (movingPiece.moves.size() > 0) {
				for (Move move: movingPiece.moves) {
					validSelection_Permanent_Border(move.toLetter, move.toDigit);
				}
			}
		}
	}*/


	@Override
	public void whiteWinsSelection() {
		for (int letter = 0; letter < 8; letter++) {
			for (int digit = 0; digit < 8; digit++) {
				int pieceID = pieces[letter][digit];
				if (pieceID != ID_PIECE_NONE) {
					if (BoardUtils.getColour(pieceID) == COLOUR_PIECE_WHITE) {
						validSelection_Permanent_Border(letter, digit);
					} else {
						invalidSelection_Permanent_Border(letter, digit);
					}
				}
			}
		}
	}


	@Override
	public void blackWinsSelection() {
		for (int letter = 0; letter < 8; letter++) {
			for (int digit = 0; digit < 8; digit++) {
				int pieceID = pieces[letter][digit];
				if (pieceID != ID_PIECE_NONE) {
					if (BoardUtils.getColour(pieceID) == COLOUR_PIECE_WHITE) {
						invalidSelection_Permanent_Border(letter, digit);
					} else {
						validSelection_Permanent_Border(letter, digit);
					}
				}
			}
		}
	}


	public RectF getRect_leaderboards() {
		return rect_leaderboards;
	}


	private boolean isRotatedBoard() {
		return ((UserSettings)Application_Base.getInstance().getUserSettings()).rotatedboard;
	}


	@Override
	public void startMoveAnimation(final Move move) {

		float start_x = getX(move.fromLetter);
		float start_y = getY(move.fromDigit);

		if (getBoardManager().getMovingPiece() == null) {

			//If this is a computer move, we have to create moving piece in order to fit into the current implementation model
			getBoardManager().createMovingPiece(start_x, start_y, move.fromLetter, move.fromDigit, move.pieceID);

		} else {

			//When human player moves there is an exiting MovingPiece object already created
			getBoardManager().startHidingPiece(move.fromLetter, move.fromDigit, true);
		}

		MovingPiece movingPiece = getBoardManager().getMovingPiece();

		animation = new MoveAnimation(move, movingPiece, getAnimationDenominator());

		setData(getBoardManager().getBoard_WithoutHided());
	}


	@Override
	public void endMoveAnimation() {

		//getBoardManager().stopHidingPiece(animation.move.fromLetter, animation.move.fromDigit);

		getActivity().getBoardManager().clearMovingPiece();

		animation = null;

		setData(getBoardManager().getBoard_WithoutHided());

		unlock();
	}


	@Override
	public int hasAnimation() {

		if (animation == null) {

			return -1;
		}

		return BoardUtils.getColour(animation.movingPiece.pieceID);
	}


	private int getAnimationDenominator() {

		int animationID = ((UserSettings)Application_Base.getInstance().getUserSettings()).moveAnimationID;
		int denominator = 0;
		switch(animationID) {
			case Config_Animation_Base.ID_NONE:
				denominator = 1;
				break;
			case Config_Animation_Base.ID_SUPER_SLOW:
				denominator = 200;
				break;
			case Config_Animation_Base.ID_SLOW:
				denominator = 100;
				break;
			case Config_Animation_Base.ID_NORMAL:
				denominator = 45;
				break;
			case Config_Animation_Base.ID_FAST:
				denominator = 25;
				break;
			case Config_Animation_Base.ID_SUPER_FAST:
				denominator = 10;
				break;
			default:
				throw new IllegalStateException("denominator=" + denominator);
		}
		return denominator;
	}


	protected boolean canTouchBoard(MotionEvent event) {


		if (isLocked()) {

			return false;
		}


		if (getActivity().getGameController().isThinking()) {

			return false;
		}

		if (getActivity().getBoardManager().getPlayerWhite().getType() == GlobalConstants.PLAYER_TYPE_COMPUTER
				&& getActivity().getBoardManager().getPlayerBlack().getType() == GlobalConstants.PLAYER_TYPE_COMPUTER) {

			return false;
		}

		float x = event.getX();
		float y = event.getY();

		int letter = getLetter(x);
		int digit =  getDigit(y);

		if (letter >= 0 && letter < 8 && digit >= 0 && digit < 8) {

			int pieceID = getActivity().getBoardManager().getPiece(letter, digit);

			if (pieceID != ID_PIECE_NONE) {

				int colour = BoardUtils.getColour(pieceID);

				if (getActivity().getBoardManager().getColourToMove() == COLOUR_PIECE_WHITE
						&& colour == COLOUR_PIECE_WHITE
						&& getActivity().getBoardManager().getPlayerWhite().getType() == GlobalConstants.PLAYER_TYPE_COMPUTER) {

					return false;
				}

				if (getActivity().getBoardManager().getColourToMove() == COLOUR_PIECE_BLACK
						&& colour == COLOUR_PIECE_BLACK
						&& getActivity().getBoardManager().getPlayerBlack().getType() == GlobalConstants.PLAYER_TYPE_COMPUTER) {

					return false;
				}
			}
		}


		return true;
	}


	public class MoveAnimation {
		
		
		public Move move;
		private MovingPiece movingPiece;
		
		private float stepX;
		private float stepY;
		
		
		MoveAnimation(final Move _move, final MovingPiece _movingPiece, int denominator) {
			
			move = _move;
			movingPiece = _movingPiece;
			
			float deltaX = getX(move.toLetter) - getX(move.fromLetter);
			float deltaY = getY(move.toDigit) - getY(move.fromDigit);
			
			stepX = deltaX / denominator;
			stepY = deltaY / denominator;
			
			movingPiece.x = getX(move.fromLetter);
			movingPiece.y = getY(move.fromDigit);
		}
		
		
		public boolean isDone() {
			return Math.abs(movingPiece.x - getX(move.toLetter)) <= Math.abs(stepX)
					&& Math.abs(movingPiece.y - getY(move.toDigit)) <= Math.abs(stepY);
		}

		
		void updateCoordinates() {
			movingPiece.x += stepX;
			movingPiece.y += stepY;
		}
	}
	
	
	public interface IAnimationHandler {

		void animationIsDone(MoveAnimation animation);
		
	}
	
	
	public class AnimationHandlerImpl implements IAnimationHandler {

		@Override
		public void animationIsDone(MoveAnimation animation) {

			getBoardManager().move(animation.move);

			int gameStatus = getActivity().getBoardManager().getGameStatus();

			if (gameStatus == GlobalConstants.GAME_STATUS_NONE) {

				getActivity().getGameController().updateUI_AfterAcceptedMove(animation.move);

				getActivity().getGameController().resumeGame();

			} else {

				//UPDATE MAIN VIEW WITH THE GAME RESULT ON TOP WHEN THE GAME HAS OVER
				getActivity().updateViewWithGameResult(gameStatus);
			}

			getActivity().getMainView().invalidate();
		}
	}


	@Override
	public OnTouchListener createOnTouchListener(IBoardVisualization boardView, IBoardViewActivity activity) {

		return new OnTouchListener_Board(boardView, activity);
	}


	public class OnTouchListener_Board implements OnTouchListener, BoardConstants /*, DialogInterface.OnClickListener, DialogInterface.OnDismissListener*/ {


		private IBoardVisualization boardVisualization;

		private IPanelsVisualization panelsVisualization;

		private IBoardViewActivity activity;


		public OnTouchListener_Board(IBoardVisualization _boardVisualization, IBoardViewActivity _activity) {
			this(_boardVisualization, null, _activity);
		}


		public OnTouchListener_Board(IBoardVisualization _boardVisualization, IPanelsVisualization _panelsVisualization, IBoardViewActivity _activity) {
			boardVisualization = _boardVisualization;
			panelsVisualization = _panelsVisualization;
			activity = _activity;
		}


		@Override
		public boolean onTouch(View view, MotionEvent event) {

			//System.out.println("BOARD: onTouch");

			if (boardVisualization.hasAnimation() != -1) {

				return true;
			}

			if (activity.getBoardManager().getGameStatus() != GlobalConstants.GAME_STATUS_NONE) {

				return true;
			}


			if (!canTouchBoard(event)) {

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

				boardVisualization.setData(activity.getBoardManager().getBoard_WithoutHided());
				if (panelsVisualization != null) panelsVisualization.setCapturedPieces(activity.getBoardManager().getCaptured_W(), activity.getBoardManager().getCaptured_B(), activity.getBoardManager().getCapturedSize_W(), activity.getBoardManager().getCapturedSize_B());

				boardVisualization.redraw();
				if (panelsVisualization != null) panelsVisualization.redraw();
			}

			return true;
		}


		public void processEvent_DOWN(MotionEvent event) {

			MovingPiece movingPiece = activity.getBoardManager().getMovingPiece();

			float x = event.getX();
			float y = event.getY();

			//System.out.println("x=" + x + ", y=" + y + " movingPiece=" + movingPiece);

			int letter = boardVisualization.getLetter(x);
			int digit =  boardVisualization.getDigit(y);

			//System.out.println("letter=" + letter + ", digit=" + digit);


			if (letter >= 0 && letter < 8 && digit >= 0 && digit < 8) {


				if (movingPiece != null) {

					movingPiece.x = (int) x;
					movingPiece.y = (int) y;

					if (letter == movingPiece.initial_letter
							&& digit == movingPiece.initial_digit) {

						//Re-selection of the same moving piece
						movingPiece.dragging = true;

					} else {

						//Target square clicked
						//May be selection of other piece?

						//overField(letter, digit);

						int pieceID = activity.getBoardManager().getPiece(letter, digit);

						if (pieceID != ID_PIECE_NONE) {

							//selection of other piece
							if (activity.getBoardManager().isReSelectionAllowed(movingPiece.pieceID, pieceID)) {

								movingPiece.dragging = false;

								pieceDeSelected();

								pieceSelected(x, y, letter, digit, pieceID);

							} else {

								boardVisualization.makeMovingPiece_OnInvalidSquare();
							}
						} else {

							boardVisualization.makeMovingPiece_OnInvalidSquare();
						}
					}

				} else {

					int pieceID = activity.getBoardManager().getPiece(letter, digit);

					if (pieceID != ID_PIECE_NONE) {

						if (activity.getBoardManager().isSelectionAllowed(pieceID)) {

							//Selection of piece
							pieceSelected(x, y, letter, digit, pieceID);

						} else {

							//selection of piece of side, which is not on move
							boardVisualization.invalidSelection_Temp_Square(letter, digit);
						}
					} else {

						//Selection of empty square

						boardVisualization.invalidSelection_Temp_Square(letter, digit);
					}
				}

				overField(letter, digit);

			} else {

				//if (true) throw new IllegalStateException();
				System.out.println("processEvent_DOWN is outside the board");
			}
		}


		public void processEvent_MOVE(MotionEvent event) {

			MovingPiece movingPiece = activity.getBoardManager().getMovingPiece();

			float x = event.getX();

			float y = event.getY();

			int letter = boardVisualization.getLetter(x);

			int digit =  boardVisualization.getDigit(y);

			if (movingPiece != null) {

				movingPiece.x = (int) x;

				movingPiece.y = (int) y;
			}

			if (letter >= 0 && letter < 8 && digit >= 0 && digit < 8) {

				overField(letter, digit);

				boardVisualization.makeMovingPiece_OnInvalidSquare();
			}
		}


		public void processEvent_UP(MotionEvent event) {

			MovingPiece movingPiece = activity.getBoardManager().getMovingPiece();

			float x = event.getX();
			float y = event.getY();

			int letter = boardVisualization.getLetter(x);
			int digit =  boardVisualization.getDigit(y);


			if (letter >= 0 && letter < 8 && digit >= 0 && digit < 8) {

				overField(letter, digit);

				if (movingPiece != null) {


					//Re-selection of the same piece
					if (letter == movingPiece.initial_letter && digit == movingPiece.initial_digit) {

						//activity.getBoardManager().stopHidingPiece(movingPiece.initial_letter, movingPiece.initial_digit);
						//TODO: re-create moving piece or use the existing one?

					} else {

						Move move = null;
						for (Move cur: movingPiece.moves) {
							if (letter == cur.toLetter && digit == cur.toDigit) {
								move = cur;
								break;
							}
						}

						if (move != null) {

							//Moving piece to the final destination

							movingPiece.capturedPID = activity.getBoardManager().getPiece(letter, digit);

							if (activity.getBoardManager().isPromotion(movingPiece.pieceID, digit)) {

								movingPiece.promotion_letter = letter;
								movingPiece.promotion_digit = digit;

								Intent i = new Intent( ((Activity) activity).getApplicationContext(), getPromotionActivityClass());

								((Activity)activity).startActivity(i);

							} else {

								SearchInfo last_search_info = null;

								if (getBoardManager().getComputerToMove() instanceof ComputerPlayer_Engine) {

									last_search_info = ((ComputerPlayer_Engine) getBoardManager().getComputerToMove()).getLastSearchInfo();

									if (last_search_info != null) {

										if (last_search_info.first_move_native != move.nativemove) {

											last_search_info = null;
										}
									}
								}

								//Will call start animation code a bit later in the stack
								activity.getGameController().acceptNewMove(move, last_search_info);

							}

						} else {

							//Not valid move
							boardVisualization.invalidSelection_Temp_Square(letter, digit);

						}
					}
				}

			} else {

				if (movingPiece != null) {

					//Dropped out of the board

				}
			}
		}


		public Class<?> getPromotionActivityClass() {
			return MenuActivity_Promotion.class;
		}


		private void pieceSelected(float x, float y, int letter, int digit, int pieceID) {

			activity.getBoardManager().createMovingPiece(x, y, letter, digit, pieceID);

			boardVisualization.clearSelections();

			boardVisualization.makeMovingPieceSelections();
		}


		private void pieceDeSelected() {

			//System.out.println("ACTION DESELECTED: " + visualization.getMovingPiece().pieceID);

			activity.getBoardManager().clearMovingPiece();

			boardVisualization.clearSelections();
		}


		private void overField(int letter, int digit) {

			if (!(letter >= 0 && letter < 8 && digit >= 0 && digit < 8)) {

				throw new IllegalStateException();
			}

			MovingPiece movingPiece = activity.getBoardManager().getMovingPiece();

			if (movingPiece != null) {

				Move move = null;

				for (Move cur: movingPiece.moves) {

					if (letter == cur.toLetter && digit == cur.toDigit) {

						move = cur;

						break;
					}
				}

				if (move != null) {

					boardVisualization.validSelection_Temp_Square(letter, digit);

				} else {

					boardVisualization.invalidSelection_Temp_Square(letter, digit);

					if (movingPiece.moves.size() == 0) {

						boardVisualization.makeMovablePiecesSelections();
					}
				}

			} else {

				boardVisualization.invalidSelection_Temp_Square(letter, digit);

				boardVisualization.makeMovablePiecesSelections();
			}
		}
	}
}
