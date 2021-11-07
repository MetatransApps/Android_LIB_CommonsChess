package org.metatrans.commons.chess.edit;


import org.metatrans.commons.chess.model.EditBoardData;
import org.metatrans.commons.chess.utils.BoardUtils;
import org.metatrans.commons.chess.utils.MessageUtils;

import android.app.Activity;


public class EditBoardChangeHandler {
	
	
	private static final String FEN_ALL_PIECES[] = { "1", "K", "Q", "R", "B", "N", "P", "k", "q", "r", "b", "n", "p" };
	
	
	private BoardView boardView;
	private EditBoardPanelsView panelsView;
	
	
	EditBoardChangeHandler(BoardView _boardView, EditBoardPanelsView _panelsView) {
		boardView = _boardView;
		panelsView = _panelsView;
	}
	
	
	public void handleBoardChange(EditBoardData editBoardData, int file, int rank) {
		
		int[][] board = boardView.getData();
		
		int pid_backup = board[file][rank];
		if (file != -1 && rank != -1) {
			board[file][rank] = editBoardData.selectedPID;
		}
		
		String fen = toFENAndCorrectCastling(board, editBoardData);
		
		System.out.println("EditBoardChangeHandler FEN is " + fen);
		
		String message = BoardUtils.validateBoard(fen);
		if (message == null) {
					
			editBoardData.fen = fen;
			((EditBoardActivity)boardView.getContext()).createBoardManager(editBoardData.fen, "processimage");
			
			board = ((EditBoardActivity)boardView.getContext()).getBoardManager().getBoard_WithoutHided();
			boardView.setData(board);
			
			boardView.invalidate();
			panelsView.invalidate();
			
		} else {
			
			board[file][rank] = pid_backup;
			
			MessageUtils.showOkDialog("Not valid board: " + message, (Activity) panelsView.getContext());
		}
	}
	
	
	public void handleBoardChange(EditBoardData editBoardData) {
		
		int[][] board = boardView.getData();
		
		String fen = toFENAndCorrectCastling(board, editBoardData);
		
		System.out.println("EditBoardChangeHandler FEN is " + fen);
		
		String message = BoardUtils.validateBoard(fen);
		if (message == null) {
			
			editBoardData.fen = fen;
			((EditBoardActivity)boardView.getContext()).createBoardManager(editBoardData.fen, "processimage");
			
			board = ((EditBoardActivity)boardView.getContext()).getBoardManager().getBoard_WithoutHided();
			boardView.setData(board);
			
			boardView.invalidate();
			panelsView.invalidate();
			
		} else {
			
			MessageUtils.showOkDialog("Not valid board: " + message, (Activity) panelsView.getContext());
			
		}
	}
	
	
	public void handleBoardChange(EditBoardData editBoardData, String fen) {
		
		int[][] board = boardView.getData();
		
		System.out.println("EditBoardChangeHandler FEN is " + fen);
		
		editBoardData.fen = fen;
		((EditBoardActivity)boardView.getContext()).createBoardManager(editBoardData.fen, "processimage");
		
		board = ((EditBoardActivity)boardView.getContext()).getBoardManager().getBoard_WithoutHided();
		boardView.setData(board);
		
		boardView.invalidate();
		panelsView.invalidate();
	}
	
	
	private static String toFENAndCorrectCastling(int[][] board, EditBoardData editBoardData) {
		
		StringBuilder sb = new StringBuilder(32);
		
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				int pid = board[j][i];
				sb.append(FEN_ALL_PIECES[pid]);
			}
			if (i != 7) {
				sb.append("/");
			}
		}
		
		String possibleCastling = BoardUtils.getCorrectCastlingAfterScan(sb.toString());
		
		// color to move
		String colorToMove = editBoardData.move_W ? "w" : "b";
		sb.append(" ").append(colorToMove).append(" ");
		
		// castling rights
		if (!editBoardData.castling_K && !editBoardData.castling_Q && !editBoardData.castling_k && !editBoardData.castling_q) {
			sb.append("-");
		} else {
			if (editBoardData.castling_K && possibleCastling.contains("K")) { // 1000
				sb.append("K");
			} else {
				editBoardData.castling_K = false;
			}
			if (editBoardData.castling_Q && possibleCastling.contains("Q")) { // 0100
				sb.append("Q");
			} else {
				editBoardData.castling_Q = false;
			}
			if (editBoardData.castling_k && possibleCastling.contains("k")) { // 0010
				sb.append("k");
			} else {
				editBoardData.castling_k = false;
			}
			if (editBoardData.castling_q && possibleCastling.contains("q")) { // 0001
				sb.append("q");
			} else {
				editBoardData.castling_q = false;
			}
		}

		String fen = sb.toString();
		fen = fen.replaceAll("11111111", "8");
		fen = fen.replaceAll("1111111", "7");
		fen = fen.replaceAll("111111", "6");
		fen = fen.replaceAll("11111", "5");
		fen = fen.replaceAll("1111", "4");
		fen = fen.replaceAll("111", "3");
		fen = fen.replaceAll("11", "2");
		
		fen += " -";//Enpassant string
		
		fen += " ";
		fen += "0";
		
		fen += " ";
		fen += "1";

		return fen;
	}
}
