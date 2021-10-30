package org.metatrans.commons.chess.edit;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.chess.Alerts;
import org.metatrans.commons.chess.app.Application_Chess_BaseImpl;
import org.metatrans.commons.chess.logic.BoardConstants;
import org.metatrans.commons.chess.utils.BoardUtils;
import org.metatrans.commons.chess.utils.MessageUtils;

import com.chessartforkids.model.EditBoardData;
import com.chessartforkids.model.GameData;
import com.chessartforkids.model.UserSettings;

import bagaturchess.bitboard.impl.Constants;


public class EditBoard_Panels_OnTouchListener implements OnTouchListener {
	
	
	private EditBoardView mainView;

	private EditBoardPanelsView panelsView;

	private EditBoardChangeHandler handler;
	
	
	public EditBoard_Panels_OnTouchListener(EditBoardView _mainView, EditBoardPanelsView _panelsView, EditBoardChangeHandler _handler) {
		mainView = _mainView;
		panelsView = _panelsView;
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
		
		if (panelsView.rectf_top.contains(x, y)) {
			
			String castling = BoardUtils.getCorrectCastlingAfterScan(editBoardData.fen);
			
			if (panelsView.rectf_castling_K.contains(x, y)){
				
				if (!castling.contains("K") && !editBoardData.castling_K) {
					MessageUtils.showOkDialog("White O-O castling is not possible. King or rook is moved.", (Activity) panelsView.getContext());
				} else {
					editBoardData.castling_K = !editBoardData.castling_K;
					handler.handleBoardChange(editBoardData);
				}
				
			} else if (panelsView.rectf_castling_Q.contains(x, y)) {
				
				if (!castling.contains("Q") && !editBoardData.castling_Q) {
					MessageUtils.showOkDialog("White O-O-O castling is not possible. King or rook is moved.", (Activity) panelsView.getContext());
				} else {
					editBoardData.castling_Q = !editBoardData.castling_Q;
					handler.handleBoardChange(editBoardData);
				}
				
			} else if (panelsView.rectf_castling_k.contains(x, y)) {
				
				if (!castling.contains("k") && !editBoardData.castling_k) {
					MessageUtils.showOkDialog("Black O-O castling is not possible. King or rook is moved.", (Activity) panelsView.getContext());
				} else {
					editBoardData.castling_k = !editBoardData.castling_k;
					handler.handleBoardChange(editBoardData);
				}
				
			} else if (panelsView.rectf_castling_q.contains(x, y)) {
				
				if (!castling.contains("q") && !editBoardData.castling_q) {
					MessageUtils.showOkDialog("Black O-O-O castling is not possible. King or rook is moved.", (Activity) panelsView.getContext());
				} else {
					editBoardData.castling_q = !editBoardData.castling_q;
					handler.handleBoardChange(editBoardData);
				}
				
			}
			
		} else if (panelsView.rectf_left.contains(x, y)) {
			
			if (panelsView.rectf_w_p.contains(x, y)){
				editBoardData.selectedPID = (editBoardData.selectedPID == BoardConstants.ID_PIECE_W_PAWN) ? BoardConstants.ID_PIECE_NONE : BoardConstants.ID_PIECE_W_PAWN;
			} else if (panelsView.rectf_w_n.contains(x, y)) {
				editBoardData.selectedPID = (editBoardData.selectedPID == BoardConstants.ID_PIECE_W_KNIGHT) ? BoardConstants.ID_PIECE_NONE : BoardConstants.ID_PIECE_W_KNIGHT;
			} else if (panelsView.rectf_w_b.contains(x, y)) {
				editBoardData.selectedPID = (editBoardData.selectedPID == BoardConstants.ID_PIECE_W_BISHOP) ? BoardConstants.ID_PIECE_NONE : BoardConstants.ID_PIECE_W_BISHOP;
			} else if (panelsView.rectf_w_r.contains(x, y)) {
				editBoardData.selectedPID = (editBoardData.selectedPID == BoardConstants.ID_PIECE_W_ROOK) ? BoardConstants.ID_PIECE_NONE : BoardConstants.ID_PIECE_W_ROOK;
			} else if (panelsView.rectf_w_q.contains(x, y)) {
				editBoardData.selectedPID = (editBoardData.selectedPID == BoardConstants.ID_PIECE_W_QUEEN) ? BoardConstants.ID_PIECE_NONE : BoardConstants.ID_PIECE_W_QUEEN;
			} else if (panelsView.rectf_w_k.contains(x, y)) {
				editBoardData.selectedPID = (editBoardData.selectedPID == BoardConstants.ID_PIECE_W_KING) ? BoardConstants.ID_PIECE_NONE : BoardConstants.ID_PIECE_W_KING;
			}
			
		} else if (panelsView.rectf_right.contains(x, y)) {
			
			if (panelsView.rectf_b_p.contains(x, y)){
				editBoardData.selectedPID = (editBoardData.selectedPID == BoardConstants.ID_PIECE_B_PAWN) ? BoardConstants.ID_PIECE_NONE : BoardConstants.ID_PIECE_B_PAWN;
			} else if (panelsView.rectf_b_n.contains(x, y)) {
				editBoardData.selectedPID = (editBoardData.selectedPID == BoardConstants.ID_PIECE_B_KNIGHT) ? BoardConstants.ID_PIECE_NONE : BoardConstants.ID_PIECE_B_KNIGHT;
			} else if (panelsView.rectf_b_b.contains(x, y)) {
				editBoardData.selectedPID = (editBoardData.selectedPID == BoardConstants.ID_PIECE_B_BISHOP) ? BoardConstants.ID_PIECE_NONE : BoardConstants.ID_PIECE_B_BISHOP;
			} else if (panelsView.rectf_b_r.contains(x, y)) {
				editBoardData.selectedPID = (editBoardData.selectedPID == BoardConstants.ID_PIECE_B_ROOK) ? BoardConstants.ID_PIECE_NONE : BoardConstants.ID_PIECE_B_ROOK;
			} else if (panelsView.rectf_b_q.contains(x, y)) {
				editBoardData.selectedPID = (editBoardData.selectedPID == BoardConstants.ID_PIECE_B_QUEEN) ? BoardConstants.ID_PIECE_NONE : BoardConstants.ID_PIECE_B_QUEEN;
			} else if (panelsView.rectf_b_k.contains(x, y)) {
				editBoardData.selectedPID = (editBoardData.selectedPID == BoardConstants.ID_PIECE_B_KING) ? BoardConstants.ID_PIECE_NONE : BoardConstants.ID_PIECE_B_KING;
			}
			
		} else if (panelsView.rectf_bottom.contains(x, y)) {
			
			if (panelsView.rectf_white_to_move.contains(x, y)){
				
				if (!editBoardData.move_W) {
					editBoardData.move_W = !editBoardData.move_W;
					editBoardData.move_B = !editBoardData.move_B;
					handler.handleBoardChange(editBoardData);
				}
				
			} else if (panelsView.rectf_black_to_move.contains(x, y)) {
				
				if (!editBoardData.move_B) {
					editBoardData.move_W = !editBoardData.move_W;
					editBoardData.move_B = !editBoardData.move_B;
					handler.handleBoardChange(editBoardData);
				}	
			}
		} else if (panelsView.rectf_bottom2.contains(x, y)) {
			
			if (panelsView.rectf_clear.contains(x, y)) {
				
				panelsView.clear_board.select();
				
				editBoardData.castling_K = false;
				editBoardData.castling_Q = false;
				editBoardData.castling_k = false;
				editBoardData.castling_q = false;
				editBoardData.move_W = true;
				editBoardData.move_B = false;
				editBoardData.fen = "4k3/8/8/8/8/8/8/4K3 w - - 0 1";
				
				handler.handleBoardChange(editBoardData, editBoardData.fen);
				
			} else if (panelsView.rectf_new.contains(x, y)) {
				
				panelsView.new_board.select();
				
				editBoardData.castling_K = true;
				editBoardData.castling_Q = true;
				editBoardData.castling_k = true;
				editBoardData.castling_q = true;
				editBoardData.move_W = true;
				editBoardData.move_B = false;
				editBoardData.fen = Constants.INITIAL_BOARD;
				
				handler.handleBoardChange(editBoardData, editBoardData.fen);
				
			} else if (panelsView.rectf_rotate.contains(x, y)) {
				
				panelsView.rotate_board.select();
				
				((UserSettings)Application_Base.getInstance().getUserSettings()).rotatedboard = !((UserSettings)Application_Base.getInstance().getUserSettings()).rotatedboard;
				Application_Base.getInstance().storeUserSettings();
				mainView.requestLayout();
				
			} else if (panelsView.rectf_goto_picture.contains(x, y)) {
				
				panelsView.goto_picture.select();
				
				((Activity) panelsView.getContext()).finish();

				((EditBoardActivity)panelsView.getContext()).processButton1();
				
			} else if (panelsView.rectf_goto_analyse.contains(x, y)){
				
				panelsView.goto_analyse.select();

				AlertDialog.Builder adb = Alerts.createAlertDialog_LoseGame(panelsView.getContext(),

						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int which) {

								Application_Base.getInstance().storeGameData(((EditBoardActivity) panelsView.getContext()).getBoardManager().getGameData());

								((EditBoardActivity) panelsView.getContext()).finish();

								((EditBoardActivity) panelsView.getContext()).processButton2();
							}
						});

				adb.show();
			}
		}
		
		((Application_Chess_BaseImpl)Application_Base.getInstance()).storeEditBoardData(editBoardData);
	}
	
	
	void processEvent_MOVE(MotionEvent event) {

		float x = event.getX();
		float y = event.getY();
		
		if (panelsView.rectf_bottom2.contains(x, y)) {
			
			if (panelsView.rectf_clear.contains(x, y)) {
				panelsView.clear_board.select();
			} else {
				panelsView.clear_board.deselect();
			}
			if (panelsView.rectf_new.contains(x, y)) {
				panelsView.new_board.select();
			} else {
				panelsView.new_board.deselect();
			}
			if (panelsView.rectf_rotate.contains(x, y)) {
				panelsView.rotate_board.select();
			} else {
				panelsView.rotate_board.deselect();
			}
			if (panelsView.rectf_goto_picture.contains(x, y)) {
				panelsView.goto_picture.select();
			} else {
				panelsView.goto_picture.deselect();
			}
			if (panelsView.rectf_goto_analyse.contains(x, y)){
				panelsView.goto_analyse.select();
			} else {
				panelsView.goto_analyse.deselect();
			}
		}
	}
	
	
	void processEvent_UP(MotionEvent event) {
		
		float x = event.getX();
		float y = event.getY();
		
		if (panelsView.rectf_bottom2.contains(x, y)) {
			
			if (panelsView.rectf_clear.contains(x, y)) {
				
				panelsView.clear_board.deselect();
				
			} else if (panelsView.rectf_new.contains(x, y)) {
				
				panelsView.new_board.deselect();
				
			} else if (panelsView.rectf_rotate.contains(x, y)) {
				
				panelsView.rotate_board.deselect();
				
			} else if (panelsView.rectf_goto_picture.contains(x, y)) {
				
				panelsView.goto_picture.deselect();
				
			} else if (panelsView.rectf_goto_analyse.contains(x, y)){
				
				panelsView.goto_analyse.deselect();
				
			}
		}
	}
}
