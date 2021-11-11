package org.metatrans.commons.chess.edit;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.cfg.colours.IConfigurationColours;
import org.metatrans.commons.chess.R;
import org.metatrans.commons.chess.app.Application_Chess_BaseImpl;
import org.metatrans.commons.chess.logic.BoardConstants;
import org.metatrans.commons.chess.views_and_controllers.BaseView;
import org.metatrans.commons.chess.model.EditBoardData;
import org.metatrans.commons.chess.utils.CachesBitmap;
import org.metatrans.commons.ui.ButtonAreaClick_Image;
import org.metatrans.commons.ui.ButtonAreaSwitch;
import org.metatrans.commons.ui.ButtonAreaSwitch_Image;
import org.metatrans.commons.ui.utils.BitmapUtils;
import org.metatrans.commons.ui.utils.DrawingUtils;


public class EditBoardPanelsView extends BaseView {
	
	
	public RectF rectf_panel_top0;
	public RectF rectf_top;
	public RectF rectf_left;
	public RectF rectf_right;
	public RectF rectf_bottom;
	public RectF rectf_bottom2;
	
	public RectF rectf_castling_K;
	public RectF rectf_castling_Q;
	public RectF rectf_castling_k;
	public RectF rectf_castling_q;
	
	public RectF rectf_w_p;
	public RectF rectf_w_n;
	public RectF rectf_w_b;
	public RectF rectf_w_r;
	public RectF rectf_w_q;
	public RectF rectf_w_k;
	
	public RectF rectf_b_p;
	public RectF rectf_b_n;
	public RectF rectf_b_b;
	public RectF rectf_b_r;
	public RectF rectf_b_q;
	public RectF rectf_b_k;
	
	public RectF rectf_white_to_move;
	public RectF rectf_black_to_move;
	
	public RectF rectf_clear;
	public RectF rectf_new;
	public RectF rectf_rotate;
	public RectF rectf_goto_picture;
	public RectF rectf_goto_analyse;
	
	private ButtonAreaSwitch switch_castling_K;
	private ButtonAreaSwitch switch_castling_Q;
	private ButtonAreaSwitch switch_castling_k;
	private ButtonAreaSwitch switch_castling_q;
	
	
	private ButtonAreaSwitch_Image switch_w_p;
	private ButtonAreaSwitch_Image switch_w_n;
	private ButtonAreaSwitch_Image switch_w_b;
	private ButtonAreaSwitch_Image switch_w_r;
	private ButtonAreaSwitch_Image switch_w_q;
	private ButtonAreaSwitch_Image switch_w_k;
	
	
	private ButtonAreaSwitch_Image switch_b_p;
	private ButtonAreaSwitch_Image switch_b_n;
	private ButtonAreaSwitch_Image switch_b_b;
	private ButtonAreaSwitch_Image switch_b_r;
	private ButtonAreaSwitch_Image switch_b_q;
	private ButtonAreaSwitch_Image switch_b_k;
	
	
	private ButtonAreaSwitch switch_white_to_move;
	private ButtonAreaSwitch switch_black_to_move;
	
	public ButtonAreaClick_Image clear_board;
	public ButtonAreaClick_Image new_board;
	public ButtonAreaClick_Image rotate_board;
	public ButtonAreaClick_Image goto_picture;
	public ButtonAreaClick_Image goto_analyse;
	
	
	private Paint paint;
	
	
	public EditBoardPanelsView(Context context, View _parent, RectF _rectf_panel_top0, RectF _rectf_top, RectF _rectf_left, RectF _rectf_right, RectF _rectf_bottom, RectF _rectf_bottom2) {
		
		super(context, _parent);
		
		rectf_panel_top0 = _rectf_panel_top0;
		rectf_top = _rectf_top;
		rectf_left = _rectf_left;
		rectf_right = _rectf_right;
		rectf_bottom = _rectf_bottom;
		rectf_bottom2 = _rectf_bottom2;
		
		rectf_castling_K = new RectF();
		rectf_castling_Q = new RectF();
		rectf_castling_k = new RectF();
		rectf_castling_q = new RectF();
		
		rectf_w_p = new RectF();
		rectf_w_n = new RectF();
		rectf_w_b = new RectF();
		rectf_w_r = new RectF();
		rectf_w_q = new RectF();
		rectf_w_k = new RectF();
		
		rectf_b_p = new RectF();
		rectf_b_n = new RectF();
		rectf_b_b = new RectF();
		rectf_b_r = new RectF();
		rectf_b_q = new RectF();
		rectf_b_k = new RectF();
		
		rectf_white_to_move = new RectF();
		rectf_black_to_move = new RectF();
		
		rectf_clear = new RectF();
		rectf_new = new RectF();
		rectf_rotate = new RectF();
		rectf_goto_picture = new RectF();
		rectf_goto_analyse = new RectF();
		
		paint = new Paint();
	}
	
	
	public void init() {
		
		initRectangles();
		
		initButtons();
	}
	
	
	private void initButtons() {
		
		IConfigurationColours coloursCfg = getActivity().getUIConfiguration().getColoursConfiguration();
		
		switch_castling_K = new ButtonAreaSwitch(rectf_castling_K, "K",
				coloursCfg.getColour_Square_ValidSelection(),
				coloursCfg.getColour_Delimiter(),
				coloursCfg.getColour_Square_White(),
				coloursCfg.getColour_Square_MarkingSelection(),
				false);

		switch_castling_Q = new ButtonAreaSwitch(rectf_castling_Q, "Q",
				coloursCfg.getColour_Square_ValidSelection(),
				coloursCfg.getColour_Delimiter(),
				coloursCfg.getColour_Square_White(),
				coloursCfg.getColour_Square_MarkingSelection(),
				false);

		switch_castling_k = new ButtonAreaSwitch(rectf_castling_k, "k",
				coloursCfg.getColour_Square_ValidSelection(),
				coloursCfg.getColour_Delimiter(),
				coloursCfg.getColour_Square_White(),
				coloursCfg.getColour_Square_MarkingSelection(),
				false);

		switch_castling_q = new ButtonAreaSwitch(rectf_castling_q, "q",
				coloursCfg.getColour_Square_ValidSelection(),
				coloursCfg.getColour_Delimiter(),
				coloursCfg.getColour_Square_White(),
				coloursCfg.getColour_Square_MarkingSelection(),
				false);
		
		
		switch_w_p = new ButtonAreaSwitch_Image(rectf_w_p, getPieceBitmap((int)(rectf_w_p.right - rectf_w_p.left),BoardConstants.ID_PIECE_W_PAWN),
				coloursCfg.getColour_Square_ValidSelection(),
				coloursCfg.getColour_Delimiter(),
				coloursCfg.getColour_Square_ValidSelection(),
				false);

		switch_w_n = new ButtonAreaSwitch_Image(rectf_w_n, getPieceBitmap((int)(rectf_w_n.right - rectf_w_n.left), BoardConstants.ID_PIECE_W_KNIGHT),
				coloursCfg.getColour_Square_ValidSelection(),
				coloursCfg.getColour_Delimiter(),
				coloursCfg.getColour_Square_ValidSelection(),
				false);

		switch_w_b = new ButtonAreaSwitch_Image(rectf_w_b, getPieceBitmap((int)(rectf_w_b.right - rectf_w_b.left), BoardConstants.ID_PIECE_W_BISHOP),
				coloursCfg.getColour_Square_ValidSelection(),
				coloursCfg.getColour_Delimiter(),
				coloursCfg.getColour_Square_ValidSelection(),
				false);

		switch_w_r = new ButtonAreaSwitch_Image(rectf_w_r, getPieceBitmap((int)(rectf_w_r.right - rectf_w_r.left), BoardConstants.ID_PIECE_W_ROOK),
				coloursCfg.getColour_Square_ValidSelection(),
				coloursCfg.getColour_Delimiter(),
				coloursCfg.getColour_Square_ValidSelection(),
				false);

		switch_w_q = new ButtonAreaSwitch_Image(rectf_w_q, getPieceBitmap((int)(rectf_w_q.right - rectf_w_q.left), BoardConstants.ID_PIECE_W_QUEEN),
				coloursCfg.getColour_Square_ValidSelection(),
				coloursCfg.getColour_Delimiter(),
				coloursCfg.getColour_Square_ValidSelection(),
				false);

		switch_w_k = new ButtonAreaSwitch_Image(rectf_w_k, getPieceBitmap((int)(rectf_w_k.right - rectf_w_k.left), BoardConstants.ID_PIECE_W_KING),
				coloursCfg.getColour_Square_ValidSelection(),
				coloursCfg.getColour_Delimiter(),
				coloursCfg.getColour_Square_ValidSelection(),
				false);


		switch_b_p = new ButtonAreaSwitch_Image(rectf_b_p, getPieceBitmap((int)(rectf_b_p.right - rectf_b_p.left), BoardConstants.ID_PIECE_B_PAWN),
				coloursCfg.getColour_Square_ValidSelection(),
				coloursCfg.getColour_Square_White(),
				coloursCfg.getColour_Square_ValidSelection(),
				false);

		switch_b_n = new ButtonAreaSwitch_Image(rectf_b_n, getPieceBitmap((int)(rectf_b_n.right - rectf_b_n.left), BoardConstants.ID_PIECE_B_KNIGHT),
				coloursCfg.getColour_Square_ValidSelection(),
				coloursCfg.getColour_Square_White(),
				coloursCfg.getColour_Square_ValidSelection(),
				false);

		switch_b_b = new ButtonAreaSwitch_Image(rectf_b_b, getPieceBitmap((int)(rectf_b_b.right - rectf_b_b.left), BoardConstants.ID_PIECE_B_BISHOP),
				coloursCfg.getColour_Square_ValidSelection(),
				coloursCfg.getColour_Square_White(),
				coloursCfg.getColour_Square_ValidSelection(),
				false);

		switch_b_r = new ButtonAreaSwitch_Image(rectf_b_r, getPieceBitmap((int)(rectf_b_r.right - rectf_b_r.left), BoardConstants.ID_PIECE_B_ROOK),
				coloursCfg.getColour_Square_ValidSelection(),
				coloursCfg.getColour_Square_White(),
				coloursCfg.getColour_Square_ValidSelection(),
				false);

		switch_b_q = new ButtonAreaSwitch_Image(rectf_b_q, getPieceBitmap((int)(rectf_b_q.right - rectf_b_q.left), BoardConstants.ID_PIECE_B_QUEEN),
				coloursCfg.getColour_Square_ValidSelection(),
				coloursCfg.getColour_Square_White(),
				coloursCfg.getColour_Square_ValidSelection(),
				false);

		switch_b_k = new ButtonAreaSwitch_Image(rectf_b_k, getPieceBitmap((int)(rectf_b_k.right - rectf_b_k.left), BoardConstants.ID_PIECE_B_KING),
				coloursCfg.getColour_Square_ValidSelection(),
				coloursCfg.getColour_Square_White(),
				coloursCfg.getColour_Square_ValidSelection(),
				false);
		
		
		switch_white_to_move = new ButtonAreaSwitch(rectf_white_to_move,
				"W",
				coloursCfg.getColour_Square_ValidSelection(),
				coloursCfg.getColour_Delimiter(),
				coloursCfg.getColour_Square_White(),
				coloursCfg.getColour_Square_MarkingSelection(),
				false);

		switch_black_to_move = new ButtonAreaSwitch(rectf_black_to_move,
				"B",
				coloursCfg.getColour_Square_ValidSelection(),
				coloursCfg.getColour_Delimiter(),
				coloursCfg.getColour_Square_White(),
				coloursCfg.getColour_Square_MarkingSelection(),
				false);
		
		clear_board = new ButtonAreaClick_Image(rectf_clear, BitmapUtils.fromResource((Context) getActivity(),
				R.drawable.ic_action_cancel_white),
				coloursCfg.getColour_Square_ValidSelection(), coloursCfg.getColour_Square_MarkingSelection());

		new_board = new ButtonAreaClick_Image(rectf_new, BitmapUtils.fromResource((Context) getActivity(),
				R.drawable.ic_action_add_white),
				coloursCfg.getColour_Square_ValidSelection(), coloursCfg.getColour_Square_MarkingSelection());

		rotate_board = new ButtonAreaClick_Image(rectf_rotate, BitmapUtils.fromResource((Context) getActivity(),
				R.drawable.ic_action_reload_white),
				coloursCfg.getColour_Square_ValidSelection(), coloursCfg.getColour_Square_MarkingSelection());

		goto_picture = new ButtonAreaClick_Image(rectf_goto_picture, BitmapUtils.fromResource((Context) getActivity(),
				((EditBoardActivity) getActivity()).button1ImageID()),
				coloursCfg.getColour_Square_ValidSelection(), coloursCfg.getColour_Square_MarkingSelection());

		goto_analyse = new ButtonAreaClick_Image(rectf_goto_analyse, BitmapUtils.fromResource((Context) getActivity(),
				((EditBoardActivity) getActivity()).button2ImageID()),
				coloursCfg.getColour_Square_ValidSelection(), coloursCfg.getColour_Square_MarkingSelection());
	}


	private void initRectangles() {
		
		float castling_and_color_border = 20;
		float castling_size = 3 * (rectf_top.bottom - rectf_top.top) / 5;
		
		rectf_castling_K.top = rectf_top.top + 1 * (rectf_top.bottom - rectf_top.top) / 5;
		rectf_castling_K.left = rectf_top.left + (rectf_top.right - rectf_top.left) / 2 - 2 * castling_size - 3 * castling_and_color_border / 2;
		rectf_castling_K.right = rectf_castling_K.left + castling_size;
		rectf_castling_K.bottom = rectf_castling_K.top + castling_size;
		
		rectf_castling_Q.top = rectf_top.top + 1 * (rectf_top.bottom - rectf_top.top) / 5;
		rectf_castling_Q.left = rectf_top.left + (rectf_top.right - rectf_top.left) / 2 - castling_size - 1 * castling_and_color_border / 2;
		rectf_castling_Q.right = rectf_castling_Q.left + castling_size;
		rectf_castling_Q.bottom = rectf_castling_Q.top + castling_size;
		
		rectf_castling_k.top = rectf_top.top + 1 * (rectf_top.bottom - rectf_top.top) / 5;
		rectf_castling_k.left = rectf_top.left + (rectf_top.right - rectf_top.left) / 2 + 1 * castling_and_color_border / 2;
		rectf_castling_k.right = rectf_castling_k.left + castling_size;
		rectf_castling_k.bottom = rectf_castling_k.top + castling_size;
		
		rectf_castling_q.top = rectf_top.top + 1 * (rectf_top.bottom - rectf_top.top) / 5;
		rectf_castling_q.left = rectf_top.left + (rectf_top.right - rectf_top.left) / 2 + castling_size + 3 * castling_and_color_border / 2;
		rectf_castling_q.right = rectf_castling_q.left + castling_size;
		rectf_castling_q.bottom = rectf_castling_q.top + castling_size;
		
		
		initPieces(rectf_left, rectf_w_p, rectf_w_n, rectf_w_b, rectf_w_r, rectf_w_q, rectf_w_k);
		
		initPieces(rectf_right, rectf_b_p, rectf_b_n, rectf_b_b, rectf_b_r, rectf_b_q, rectf_b_k);
		
		
		rectf_white_to_move.top = rectf_bottom.top + 1 * (rectf_bottom.bottom - rectf_bottom.top) / 5;
		rectf_white_to_move.left = rectf_bottom.left + (rectf_bottom.right - rectf_bottom.left) / 2 - castling_size - 1 * castling_and_color_border / 2;
		rectf_white_to_move.right = rectf_white_to_move.left + castling_size;
		rectf_white_to_move.bottom = rectf_white_to_move.top + castling_size;
		
		rectf_black_to_move.top = rectf_bottom.top + 1 * (rectf_bottom.bottom - rectf_bottom.top) / 5;
		rectf_black_to_move.left = rectf_bottom.left + (rectf_bottom.right - rectf_bottom.left) / 2 + 1 * castling_and_color_border / 2;
		rectf_black_to_move.right = rectf_black_to_move.left + castling_size;
		rectf_black_to_move.bottom = rectf_black_to_move.top + castling_size;
		
		
		rectf_clear.top = rectf_bottom2.top + 1 * (rectf_bottom2.bottom - rectf_bottom2.top) / 5;
		rectf_clear.left = rectf_bottom2.left + (rectf_bottom2.right - rectf_bottom2.left) / 2 - 2.5f * castling_size - 2 * castling_and_color_border;
		rectf_clear.right = rectf_clear.left + castling_size;
		rectf_clear.bottom = rectf_clear.top + castling_size;
		
		rectf_new.top = rectf_bottom2.top + 1 * (rectf_bottom2.bottom - rectf_bottom2.top) / 5;
		rectf_new.left = rectf_clear.right + castling_and_color_border;
		rectf_new.right = rectf_new.left + castling_size;
		rectf_new.bottom = rectf_new.top + castling_size;
		
		rectf_rotate.top = rectf_bottom2.top + 1 * (rectf_bottom2.bottom - rectf_bottom2.top) / 5;
		rectf_rotate.left = rectf_new.right + castling_and_color_border;
		rectf_rotate.right = rectf_rotate.left + castling_size;
		rectf_rotate.bottom = rectf_rotate.top + castling_size;
		
		rectf_goto_picture.top = rectf_bottom2.top + 1 * (rectf_bottom2.bottom - rectf_bottom2.top) / 5;
		rectf_goto_picture.left = rectf_rotate.right + castling_and_color_border;
		rectf_goto_picture.right = rectf_goto_picture.left + castling_size;
		rectf_goto_picture.bottom = rectf_goto_picture.top + castling_size;
		
		rectf_goto_analyse.top = rectf_bottom2.top + 1 * (rectf_bottom2.bottom - rectf_bottom2.top) / 5;
		rectf_goto_analyse.left = rectf_goto_picture.right + castling_and_color_border;
		rectf_goto_analyse.right = rectf_goto_analyse.left + castling_size;
		rectf_goto_analyse.bottom = rectf_goto_analyse.top + castling_size;
	}
	
	
	private void initPieces(RectF contour, RectF rectf_p, RectF rectf_n, RectF rectf_b, RectF rectf_r, RectF rectf_q, RectF rectf_k) {
		
		float pieces_border = 3;
		float pieces_size = Math.min((contour.right - contour.left) - 2 * pieces_border, (contour.bottom - contour.top) / 8 - 2 * pieces_border);
		
		rectf_p.top = contour.top + 1.5f * (contour.bottom - contour.top) / 8 - pieces_size / 2;
		rectf_p.left = contour.left + (contour.right - contour.left) / 2 - pieces_size / 2;
		rectf_p.right = rectf_p.left + pieces_size;
		rectf_p.bottom = rectf_p.top + pieces_size;
		
		
		rectf_n.top = contour.top + 2.5f * (contour.bottom - contour.top) / 8 - pieces_size / 2;
		rectf_n.left = contour.left + (contour.right - contour.left) / 2 - pieces_size / 2;
		rectf_n.right = rectf_n.left + pieces_size;
		rectf_n.bottom = rectf_n.top + pieces_size;
		
		rectf_b.top = contour.top + 3.5f * (contour.bottom - contour.top) / 8 - pieces_size / 2;
		rectf_b.left = contour.left + (contour.right - contour.left) / 2 - pieces_size / 2;
		rectf_b.right = rectf_b.left + pieces_size;
		rectf_b.bottom = rectf_b.top + pieces_size;
		
		rectf_r.top = contour.top + 4.5f * (contour.bottom - contour.top) / 8 - pieces_size / 2;
		rectf_r.left = contour.left + (contour.right - contour.left) / 2 - pieces_size / 2;
		rectf_r.right = rectf_r.left + pieces_size;
		rectf_r.bottom = rectf_r.top + pieces_size;
		
		rectf_q.top = contour.top + 5.5f * (contour.bottom - contour.top) / 8 - pieces_size / 2;
		rectf_q.left = contour.left + (contour.right - contour.left) / 2 - pieces_size / 2;
		rectf_q.right = rectf_q.left + pieces_size;
		rectf_q.bottom = rectf_q.top + pieces_size;
		
		rectf_k.top = contour.top + 6.5f * (contour.bottom - contour.top) / 8 - pieces_size / 2;
		rectf_k.left = contour.left + (contour.right - contour.left) / 2 - pieces_size / 2;
		rectf_k.right = rectf_k.left + pieces_size;
		rectf_k.bottom = rectf_k.top + pieces_size;
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		super.onDraw(canvas);
		
		
		EditBoardData editBoardData = ((Application_Chess_BaseImpl)Application_Base.getInstance()).getEditBoardData();
		
		
		paint.setColor(getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_Black());
		DrawingUtils.drawRoundRectangle(canvas, paint, rectf_panel_top0, 10);
		DrawingUtils.drawRoundRectangle(canvas, paint, rectf_top, 10);
		DrawingUtils.drawRoundRectangle(canvas, paint, rectf_left, 10);
		DrawingUtils.drawRoundRectangle(canvas, paint, rectf_right, 10);
		DrawingUtils.drawRoundRectangle(canvas, paint, rectf_bottom, 10);
		DrawingUtils.drawRoundRectangle(canvas, paint, rectf_bottom2, 10);
		
		
		if (editBoardData.castling_K) {
			switch_castling_K.activate();
		} else {
			switch_castling_K.deactivate();
		}
		if (editBoardData.castling_Q) {
			switch_castling_Q.activate();
		} else {
			switch_castling_Q.deactivate();
		}
		if (editBoardData.castling_k) {
			switch_castling_k.activate();
		} else {
			switch_castling_k.deactivate();
		}
		if (editBoardData.castling_q) {
			switch_castling_q.activate();
		} else {
			switch_castling_q.deactivate();
		}
		switch_castling_K.draw(canvas);
		switch_castling_Q.draw(canvas);
		switch_castling_k.draw(canvas);
		switch_castling_q.draw(canvas);
		
		
		if (editBoardData.selectedPID == BoardConstants.ID_PIECE_W_PAWN) {
			switch_w_p.activate();
		} else {
			switch_w_p.deactivate();
		}
		if (editBoardData.selectedPID == BoardConstants.ID_PIECE_W_KNIGHT) {
			switch_w_n.activate();
		} else {
			switch_w_n.deactivate();
		}
		if (editBoardData.selectedPID == BoardConstants.ID_PIECE_W_BISHOP) {
			switch_w_b.activate();
		} else {
			switch_w_b.deactivate();
		}
		if (editBoardData.selectedPID == BoardConstants.ID_PIECE_W_ROOK) {
			switch_w_r.activate();
		} else {
			switch_w_r.deactivate();
		}
		if (editBoardData.selectedPID == BoardConstants.ID_PIECE_W_QUEEN) {
			switch_w_q.activate();
		} else {
			switch_w_q.deactivate();
		}
		if (editBoardData.selectedPID == BoardConstants.ID_PIECE_W_KING) {
			switch_w_k.activate();
		} else {
			switch_w_k.deactivate();
		}
		switch_w_p.draw(canvas);
		switch_w_n.draw(canvas);
		switch_w_b.draw(canvas);
		switch_w_r.draw(canvas);
		switch_w_q.draw(canvas);
		switch_w_k.draw(canvas);
		
		if (editBoardData.selectedPID == BoardConstants.ID_PIECE_B_PAWN) {
			switch_b_p.activate();
		} else {
			switch_b_p.deactivate();
		}
		if (editBoardData.selectedPID == BoardConstants.ID_PIECE_B_KNIGHT) {
			switch_b_n.activate();
		} else {
			switch_b_n.deactivate();
		}
		if (editBoardData.selectedPID == BoardConstants.ID_PIECE_B_BISHOP) {
			switch_b_b.activate();
		} else {
			switch_b_b.deactivate();
		}
		if (editBoardData.selectedPID == BoardConstants.ID_PIECE_B_ROOK) {
			switch_b_r.activate();
		} else {
			switch_b_r.deactivate();
		}
		if (editBoardData.selectedPID == BoardConstants.ID_PIECE_B_QUEEN) {
			switch_b_q.activate();
		} else {
			switch_b_q.deactivate();
		}
		if (editBoardData.selectedPID == BoardConstants.ID_PIECE_B_KING) {
			switch_b_k.activate();
		} else {
			switch_b_k.deactivate();
		}
		switch_b_p.draw(canvas);
		switch_b_n.draw(canvas);
		switch_b_b.draw(canvas);
		switch_b_r.draw(canvas);
		switch_b_q.draw(canvas);
		switch_b_k.draw(canvas);
		
		
		if (editBoardData.move_W) {
			switch_white_to_move.activate();
		} else {
			switch_white_to_move.deactivate();
		}
		if (editBoardData.move_B) {
			switch_black_to_move.activate();
		} else {
			switch_black_to_move.deactivate();
		}
		switch_white_to_move.draw(canvas);
		switch_black_to_move.draw(canvas);
		
		
		clear_board.draw(canvas);
		new_board.draw(canvas);
		rotate_board.draw(canvas);
		goto_picture.draw(canvas);
		goto_analyse.draw(canvas);
		
		
		invalidate();
	}


	private Bitmap getPieceBitmap(int size, int pieceID) {
		//Obtain piece bitmap
		int imageResID = getActivity().getUIConfiguration().getPiecesConfiguration().getBitmapResID(pieceID);
		Bitmap bitmap = CachesBitmap.getSingletonPiecesBoard(size).getBitmap((Context) getActivity(), imageResID);
		if (bitmap == null) {
			getActivity().getUIConfiguration().getPiecesConfiguration().getPiece(pieceID); //This call will be added into the cache
			bitmap = CachesBitmap.getSingletonPiecesBoard(size).getBitmap((Context) getActivity(), imageResID);	
		}
		return bitmap;
	}
}
