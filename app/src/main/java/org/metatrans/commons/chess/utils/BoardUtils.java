package org.metatrans.commons.chess.utils;


import bagaturchess.bitboard.impl.Constants;


public class BoardUtils {
	
	
	private static final String FEN_WHITE_PIECES[] = { "1", "P", "N", "B", "R", "Q", "K" };
	private static final String FEN_BLACK_PIECES[] = { "1", "p", "n", "b", "r", "q", "k" };
	
	
	public static String getCorrectCastlingAfterScan(String fen) {
		
		String castling = "";
		
		String[] fenArray = fen.split(" ");
		int[][] piecesData = buildCountsMap(fenArray[0]);
		int[] piecesSquares = piecesData[1];
		
		if (piecesSquares[3] == Constants.PID_W_KING
				&& piecesSquares[0] == Constants.PID_W_ROOK) {
			castling += "K";
		}
		if (piecesSquares[3] == Constants.PID_W_KING
				&& piecesSquares[7] == Constants.PID_W_ROOK) {
			castling += "Q";
		}
		if (piecesSquares[59] == Constants.PID_B_KING
				&& piecesSquares[56] == Constants.PID_B_ROOK) {
			castling += "k";
		}
		if (piecesSquares[59] == Constants.PID_B_KING
				&& piecesSquares[63] == Constants.PID_B_ROOK) {
			castling += "q";
		}
		
		if (castling.equals("")) {
			castling = "-";
		}
		
		return castling;
	}
	
	
	public static String validateBoard(String fen, int max_kings_count) {
		
		String[] fenArray = fen.split(" ");
		int[][] piecesData = buildCountsMap(fenArray[0]);
		int[] piecesCounts = piecesData[0];
		int[] piecesSquares = piecesData[1];
		
		String message = null;
		if (piecesCounts[Constants.PID_W_KING] == 0) {
			message = "There is no white king";
		} else if (piecesCounts[Constants.PID_W_KING] > max_kings_count) {
			message = "Too much white kings";
		} else if (piecesCounts[Constants.PID_B_KING] == 0) {
			message = "There is no black king";
		} else if (piecesCounts[Constants.PID_B_KING] > max_kings_count) {
			message = "Too much black kings";
		} else if (piecesCounts[Constants.PID_W_PAWN] > 12 || piecesCounts[Constants.PID_B_PAWN] > 12) {
			message = "There are more than 12 pawns";
		} else if (piecesCounts[Constants.PID_W_KNIGHT] > 10 || piecesCounts[Constants.PID_B_KNIGHT] > 10) {
			message = "There are more than 10 knights";
		} else if (piecesCounts[Constants.PID_W_BISHOP] > 10 || piecesCounts[Constants.PID_B_BISHOP] > 10) {
			message = "There are more than 10 bishops";
		} else if (piecesCounts[Constants.PID_W_ROOK] > 10 || piecesCounts[Constants.PID_B_ROOK] > 10) {
			message = "There are more than 10 rooks";
		} else if (piecesCounts[Constants.PID_W_QUEEN] > 9 || piecesCounts[Constants.PID_B_QUEEN] > 9) {
			message = "There are more than 9 queens";
		} else {
			int countWhite = 0;
			int countBlack = 0;
			for (int i = 0; i < piecesSquares.length; i++) {
				int pid = piecesSquares[i];
				if (pid >= Constants.PID_W_PAWN && pid <= Constants.PID_W_KING) {
					countWhite++;
				} else if (pid >= Constants.PID_B_PAWN && pid <= Constants.PID_B_KING) {
					countBlack++;
				}
			}
			if (countWhite > 32) {
				message = "There are more than 32 white pieces";
			} else if (countBlack > 32) {
				message = "There are more than 32 black pieces";
			} else {
				for (int i = 0; i < piecesSquares.length; i++) {
					int pid = piecesSquares[i];
					if (pid == Constants.PID_W_PAWN || pid == Constants.PID_B_PAWN) {
						if (i >= 8 && i <= 56) {
							//Ok
						} else {
							message = "There are pawns on the last rank";
							break;
						}
					}
				}
			}
		}
		
		return message;
	}
	
	
	private static int[][] buildCountsMap(final String fenPieces) {
		
		int[] piecesCounts = new int[13];
		int[] piecesSquares = new int[64];
		
		int squareID = 63;
		for (int i = 0; i < fenPieces.length(); i++) {

			final char character = fenPieces.charAt(i);
			switch (character) {
				case '/':
					continue;
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
					squareID -= Character.digit(character, 10);
					break;
				case 'P':
					piecesCounts[Constants.PID_W_PAWN]++;
					piecesSquares[squareID] = Constants.PID_W_PAWN;
					squareID--;
					break;
				case 'N':
					piecesCounts[Constants.PID_W_KNIGHT]++;
					piecesSquares[squareID] = Constants.PID_W_KNIGHT;
					squareID--;
					break;
				case 'B':
					piecesCounts[Constants.PID_W_BISHOP]++;
					piecesSquares[squareID] = Constants.PID_W_BISHOP;
					squareID--;
					break;
				case 'R':
					piecesCounts[Constants.PID_W_ROOK]++;
					piecesSquares[squareID] = Constants.PID_W_ROOK;
					squareID--;
					break;
				case 'Q':
					piecesCounts[Constants.PID_W_QUEEN]++;
					piecesSquares[squareID] = Constants.PID_W_QUEEN;
					squareID--;
					break;
				case 'K':
					piecesCounts[Constants.PID_W_KING]++;
					piecesSquares[squareID] = Constants.PID_W_KING;
					squareID--;
					break;
				case 'p':
					piecesCounts[Constants.PID_B_PAWN]++;
					piecesSquares[squareID] = Constants.PID_B_PAWN;
					squareID--;
					break;
				case 'n':
					piecesCounts[Constants.PID_B_KNIGHT]++;
					piecesSquares[squareID] = Constants.PID_B_KNIGHT;
					squareID--;
					break;
				case 'b':
					piecesCounts[Constants.PID_B_BISHOP]++;
					piecesSquares[squareID] = Constants.PID_B_BISHOP;
					squareID--;
					break;
				case 'r':
					piecesCounts[Constants.PID_B_ROOK]++;
					piecesSquares[squareID] = Constants.PID_B_ROOK;
					squareID--;
					break;
				case 'q':
					piecesCounts[Constants.PID_B_QUEEN]++;
					piecesSquares[squareID] = Constants.PID_B_QUEEN;
					squareID--;
					break;
				case 'k':
					piecesCounts[Constants.PID_B_KING]++;
					piecesSquares[squareID] = Constants.PID_B_KING;
					squareID--;
					break;
				default:
					throw new IllegalStateException("" + character);
			}
		}
		
		return new int[][] {piecesCounts, piecesSquares};
	}


	public static String rotateBoard(String fen) {
		
		int[][] piecesData = buildCountsMap(fen);
		int[] piecesSquares = piecesData[1];
		
		int[] rotatedPIDs = new int[64];
		for (int i = 0; i < piecesSquares.length; i++) {
			rotatedPIDs[63 - i] = piecesSquares[i];
		}
		
		String rotatedFen = createFENFromPIDs(rotatedPIDs);
		
		return rotatedFen;
	}
	
	
	private static String createFENFromPIDs(int[] pids) {
		
		StringBuilder sb = new StringBuilder();
		for (int i = 63; i >= 0; i--) {
			if (pids[i] >= 1 && pids[i] <= 6) {
				sb.append(FEN_WHITE_PIECES[Constants.PIECE_IDENTITY_2_TYPE[pids[i]]]);
			} else {
				sb.append(FEN_BLACK_PIECES[Constants.PIECE_IDENTITY_2_TYPE[pids[i]]]);
			}
			
			if (i % 8 == 0 && i != 0) {
				sb.append("/");
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
		return fen;
	}
}
