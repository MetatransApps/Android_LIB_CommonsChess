package bagaturchess.bitboard.impl1.internal;


import static bagaturchess.bitboard.impl1.internal.ChessConstants.BISHOP;
import static bagaturchess.bitboard.impl1.internal.ChessConstants.EMPTY;
import static bagaturchess.bitboard.impl1.internal.ChessConstants.KING;
import static bagaturchess.bitboard.impl1.internal.ChessConstants.NIGHT;
import static bagaturchess.bitboard.impl1.internal.ChessConstants.PAWN;
import static bagaturchess.bitboard.impl1.internal.ChessConstants.QUEEN;
import static bagaturchess.bitboard.impl1.internal.ChessConstants.ROOK;
import static bagaturchess.bitboard.impl1.internal.ChessConstants.WHITE;

import bagaturchess.bitboard.api.BoardUtils;
import bagaturchess.bitboard.common.Properties;
import bagaturchess.bitboard.impl.datastructs.StackLongInt;


public final class ChessBoard {
	
	ChessBoard() {
	}

	private static ChessBoard[] instances;
	static {
		initInstances(0);
	}

	public static ChessBoard getInstance() {
		return instances[0];
	}

	public static ChessBoard getInstance(int instanceNumber) {
		return instances[instanceNumber];
	}

	public static ChessBoard getTestInstance() {
		return instances[1];
	}

	public static void initInstances(int numberOfInstances) {
		instances = new ChessBoard[numberOfInstances];
		for (int i = 0; i < numberOfInstances; i++) {
			instances[i] = new ChessBoard();
		}
	}
	
	/** color, piece */
	public final long[][] pieces = new long[2][7];
	public final long[] friendlyPieces = new long[2];

	/** 4 bits: white-king,white-queen,black-king,black-queen */
	public int castlingRights;
	public int psqtScore_mg;
	public int psqtScore_eg;
	public int colorToMove, colorToMoveInverse;
	public int epIndex;
	public int materialKey;
	public int material_factor_white;
	public int material_factor_black;
	
	public long allPieces, emptySpaces;
	public long zobristKey, pawnZobristKey;
	public long checkingPieces, pinnedPieces, discoveredPieces;

	/** which piece is on which square */
	public final int[] pieceIndexes = new int[64];
	public final int[] kingIndex = new int[2];
	public final long[] kingArea = new long[2];

	public int moveCounter = 0;
	public final int[] castlingHistory = new int[EngineConstants.MAX_MOVES];
	public final int[] epIndexHistory = new int[EngineConstants.MAX_MOVES];
	public final long[] zobristKeyHistory = new long[EngineConstants.MAX_MOVES];
	public final long[] checkingPiecesHistory = new long[EngineConstants.MAX_MOVES];
	public final long[] pinnedPiecesHistory = new long[EngineConstants.MAX_MOVES];
	public final long[] discoveredPiecesHistory = new long[EngineConstants.MAX_MOVES];
	public final int[] lastCaptureOrPawnMoveBeforeHistory = new int[EngineConstants.MAX_MOVES];
	

	public int[] playedMoves = new int[2048];
	public int playedMovesCount = 0;
	
	public int lastCaptureOrPawnMoveBefore = 0;
	
	public StackLongInt playedBoardStates = new StackLongInt(9631);//MUST BE PRIME NUMBER
	
	public CastlingConfig castlingConfig;
	
	private int[] buff_castling_rook_from_to = new int[2];
	
	
	@Override
	public String toString() {
		return ChessBoardUtil.toString(this, true);
	}
	
	public boolean isDrawishByMaterial(final int color) {
		// no pawns or queens
		//if (MaterialUtil.hasPawnsOrQueens(materialKey, color)) {
			return false;
		//}

		// material difference bigger than bishop + 50
		// TODO do not include pawn score (why...?)
		//return EvalUtil.getImbalances(this) * ChessConstants.COLOR_FACTOR[color] < EvalConstants.MATERIAL[BISHOP]
			//	+ EvalConstants.OTHER_SCORES[EvalConstants.IX_DRAWISH];
	}
	
	public void changeSideToMove() {
		colorToMove = colorToMoveInverse;
		colorToMoveInverse = 1 - colorToMove;
	}
	
	public boolean isDiscoveredMove(final int fromIndex) {
		return (discoveredPieces & (1L << fromIndex)) != 0;
	}
	
	private void pushHistoryValues(int move) {
		castlingHistory[moveCounter] = castlingRights;
		epIndexHistory[moveCounter] = epIndex;
		zobristKeyHistory[moveCounter] = zobristKey;
		pinnedPiecesHistory[moveCounter] = pinnedPieces;
		discoveredPiecesHistory[moveCounter] = discoveredPieces;
		checkingPiecesHistory[moveCounter] = checkingPieces;
		lastCaptureOrPawnMoveBeforeHistory[moveCounter] = lastCaptureOrPawnMoveBefore;
		moveCounter++;
		playedMoves[playedMovesCount] = move;
		playedMovesCount++;
	}
	
	private void popHistoryValues() {
		playedMovesCount--;
		moveCounter--;
		epIndex = epIndexHistory[moveCounter];
		zobristKey = zobristKeyHistory[moveCounter];
		castlingRights = castlingHistory[moveCounter];
		pinnedPieces = pinnedPiecesHistory[moveCounter];
		discoveredPieces = discoveredPiecesHistory[moveCounter];
		checkingPieces = checkingPiecesHistory[moveCounter];
		lastCaptureOrPawnMoveBefore = lastCaptureOrPawnMoveBeforeHistory[moveCounter];
	}
	
	public void doNullMove() {
		pushHistoryValues(0);

		zobristKey ^= Zobrist.sideToMove;
		if (epIndex != 0) {
			zobristKey ^= Zobrist.epIndex[epIndex];
			epIndex = 0;
		}
		changeSideToMove();

		if (EngineConstants.ASSERT) {
			ChessBoardTestUtil.testValues(this);
		}
		
		playedBoardStates.inc(zobristKey);
	}
	
	public void undoNullMove() {
		
		playedBoardStates.dec(zobristKey);
		
		popHistoryValues();
		changeSideToMove();

		if (EngineConstants.ASSERT) {
			ChessBoardTestUtil.testValues(this);
		}
	}
	
	public void doMove(int move) {

		if (MoveUtil.isCastlingMove(move)) {
			
			doCastling960(move);
			
			return;
		}
		
		final int fromIndex = MoveUtil.getFromIndex(move);
		int toIndex = MoveUtil.getToIndex(move);
		long toMask = 1L << toIndex;
		final long fromToMask = (1L << fromIndex) ^ toMask;
		final int sourcePieceIndex = MoveUtil.getSourcePieceIndex(move);
		final int attackedPieceIndex = MoveUtil.getAttackedPieceIndex(move);

		if (fromIndex == toIndex) {
			
			throw new IllegalStateException("doMove: fromIndex == toIndex");
		}
		
		if (EngineConstants.ASSERT) {
			Assert.isTrue(pieceIndexes[fromIndex] == sourcePieceIndex, "pieceIndexes[fromIndex] == sourcePieceIndex"
					+ ", sourcePieceIndex=" + sourcePieceIndex + ", pieceIndexes[fromIndex]=" + pieceIndexes[fromIndex] + ", fromIndex=" + fromIndex
					+ ", move=" + (new MoveWrapper(move, BoardUtils.isFRC, castlingConfig)).toString() + ", board=" + this);
			Assert.isTrue(pieceIndexes[toIndex] == attackedPieceIndex || MoveUtil.isEPMove(move), "pieceIndexes[toIndex] == attackedPieceIndex || MoveUtil.isEPMove(move)"
						+ ", attackedPieceIndex="	+ attackedPieceIndex + ", pieceIndexes[toIndex]=" + pieceIndexes[toIndex] + ", toIndex=" + toIndex
						+ ", move=" + (new MoveWrapper(move, BoardUtils.isFRC, castlingConfig)).toString() + ", board=" + this);
			Assert.isTrue(attackedPieceIndex == 0 || (Util.POWER_LOOKUP[toIndex] & friendlyPieces[colorToMove]) == 0, "attackedPieceIndex == 0 || (Util.POWER_LOOKUP[toIndex] & friendlyPieces[colorToMove]) == 0");
		}
		
		pushHistoryValues(move);
		
		if (attackedPieceIndex != 0 || sourcePieceIndex == ChessConstants.PAWN) {
			lastCaptureOrPawnMoveBefore = 0;
		} else {
			lastCaptureOrPawnMoveBefore++;
		}
		
		zobristKey ^= Zobrist.piece[fromIndex][colorToMove][sourcePieceIndex] ^ Zobrist.piece[toIndex][colorToMove][sourcePieceIndex] ^ Zobrist.sideToMove;
		if (epIndex != 0) {
			zobristKey ^= Zobrist.epIndex[epIndex];
			epIndex = 0;
		}

		friendlyPieces[colorToMove] ^= fromToMask;
		pieceIndexes[fromIndex] = EMPTY;
		pieceIndexes[toIndex] = sourcePieceIndex;
		pieces[colorToMove][sourcePieceIndex] ^= fromToMask;
		psqtScore_mg += EvalConstants.PSQT_MG[sourcePieceIndex][colorToMove][toIndex] - EvalConstants.PSQT_MG[sourcePieceIndex][colorToMove][fromIndex];
		psqtScore_eg += EvalConstants.PSQT_EG[sourcePieceIndex][colorToMove][toIndex] - EvalConstants.PSQT_EG[sourcePieceIndex][colorToMove][fromIndex];
		
		switch (sourcePieceIndex) {
		case PAWN:
			pawnZobristKey ^= Zobrist.piece[fromIndex][colorToMove][PAWN];
			if (MoveUtil.isPromotion(move)) {
				
				if (colorToMove == WHITE) {
					material_factor_white += EvalConstants.PHASE[MoveUtil.getMoveType(move)];
				} else {
					material_factor_black += EvalConstants.PHASE[MoveUtil.getMoveType(move)];
				}
				
				materialKey += MaterialUtil.VALUES[colorToMove][MoveUtil.getMoveType(move)] - MaterialUtil.VALUES[colorToMove][PAWN];
				pieces[colorToMove][PAWN] ^= toMask;
				pieces[colorToMove][MoveUtil.getMoveType(move)] |= toMask;
				pieceIndexes[toIndex] = MoveUtil.getMoveType(move);
				zobristKey ^= Zobrist.piece[toIndex][colorToMove][PAWN] ^ Zobrist.piece[toIndex][colorToMove][MoveUtil.getMoveType(move)];
				psqtScore_mg += EvalConstants.PSQT_MG[MoveUtil.getMoveType(move)][colorToMove][toIndex] - EvalConstants.PSQT_MG[PAWN][colorToMove][toIndex];
				psqtScore_eg += EvalConstants.PSQT_EG[MoveUtil.getMoveType(move)][colorToMove][toIndex] - EvalConstants.PSQT_EG[PAWN][colorToMove][toIndex];
			} else {
				pawnZobristKey ^= Zobrist.piece[toIndex][colorToMove][PAWN];
				// 2-move
				if (ChessConstants.IN_BETWEEN[fromIndex][toIndex] != 0) {
					if ((StaticMoves.PAWN_ATTACKS[colorToMove][Long.numberOfTrailingZeros(ChessConstants.IN_BETWEEN[fromIndex][toIndex])]
							& pieces[colorToMoveInverse][PAWN]) != 0) {
						epIndex = Long.numberOfTrailingZeros(ChessConstants.IN_BETWEEN[fromIndex][toIndex]);
						zobristKey ^= Zobrist.epIndex[epIndex];
					}
				}
			}
			break;

		case ROOK:
			
			if (castlingRights != 0) {
				
				zobristKey ^= Zobrist.castling[castlingRights];
				
				if (Properties.DUMP_CASTLING) System.out.println("ChessBoard.doMove/ROOK: castlingRights=" + castlingRights);
				
				castlingRights = CastlingUtil.getRookMovedOrAttackedCastlingRights(castlingRights, fromIndex, castlingConfig);
				
				if (Properties.DUMP_CASTLING) System.out.println("ChessBoard.doMove/ROOK: NEW castlingRights=" + castlingRights);
				
				zobristKey ^= Zobrist.castling[castlingRights];
			}
			
			break;

		case KING:
			
			updateKingValues(colorToMove, toIndex);
			
			if (castlingRights != 0) {
				
				if (MoveUtil.isCastlingMove(move)) {
					
					throw new IllegalStateException("Castling");
				}
				
				zobristKey ^= Zobrist.castling[castlingRights];
				
				if (Properties.DUMP_CASTLING) System.out.println("ChessBoard.doMove/KING: castlingRights=" + castlingRights);
				
				castlingRights = CastlingUtil.getKingMovedCastlingRights(castlingRights, colorToMove, castlingConfig);
				
				if (Properties.DUMP_CASTLING) System.out.println("ChessBoard.doMove/KING: NEW castlingRights=" + castlingRights);
				
				zobristKey ^= Zobrist.castling[castlingRights];
			}
		}

		// piece hit?
		switch (attackedPieceIndex) {
		case EMPTY:
			break;
		case PAWN:
			if (MoveUtil.isEPMove(move)) {
				toIndex += ChessConstants.COLOR_FACTOR_8[colorToMoveInverse];
				toMask = Util.POWER_LOOKUP[toIndex];
				pieceIndexes[toIndex] = EMPTY;
			}
			pawnZobristKey ^= Zobrist.piece[toIndex][colorToMoveInverse][PAWN];
			psqtScore_mg -= EvalConstants.PSQT_MG[PAWN][colorToMoveInverse][toIndex];
			psqtScore_eg -= EvalConstants.PSQT_EG[PAWN][colorToMoveInverse][toIndex];
			friendlyPieces[colorToMoveInverse] ^= toMask;
			pieces[colorToMoveInverse][PAWN] ^= toMask;
			zobristKey ^= Zobrist.piece[toIndex][colorToMoveInverse][PAWN];
			materialKey -= MaterialUtil.VALUES[colorToMoveInverse][PAWN];
			break;
		case ROOK:
			
			if (castlingRights != 0) {
				
				zobristKey ^= Zobrist.castling[castlingRights];
				
				if (Properties.DUMP_CASTLING) System.out.println("ChessBoard.doMove/ROOK/attackedPieceIndex: castlingRights=" + castlingRights);
				
				castlingRights = CastlingUtil.getRookMovedOrAttackedCastlingRights(castlingRights, toIndex, castlingConfig);
				
				if (Properties.DUMP_CASTLING) System.out.println("ChessBoard.doMove/ROOK/attackedPieceIndex: NEW castlingRights=" + castlingRights);
				
				zobristKey ^= Zobrist.castling[castlingRights];
			}
			// fall-through
		default:
			
			if (colorToMoveInverse == WHITE) {
				material_factor_white -= EvalConstants.PHASE[attackedPieceIndex];
			} else {
				material_factor_black -= EvalConstants.PHASE[attackedPieceIndex];
			}
			
			psqtScore_mg -= EvalConstants.PSQT_MG[attackedPieceIndex][colorToMoveInverse][toIndex];
			psqtScore_eg -= EvalConstants.PSQT_EG[attackedPieceIndex][colorToMoveInverse][toIndex];
			friendlyPieces[colorToMoveInverse] ^= toMask;
			pieces[colorToMoveInverse][attackedPieceIndex] ^= toMask;
			zobristKey ^= Zobrist.piece[toIndex][colorToMoveInverse][attackedPieceIndex];
			materialKey -= MaterialUtil.VALUES[colorToMoveInverse][attackedPieceIndex];
		}

		allPieces = friendlyPieces[colorToMove] | friendlyPieces[colorToMoveInverse];
		emptySpaces = ~allPieces;
		changeSideToMove();

		// update checking pieces
		if (isDiscoveredMove(fromIndex)) {
			checkingPieces = CheckUtil.getCheckingPieces(this);
		} else {
			if (MoveUtil.isNormalMove(move)) {
				checkingPieces = CheckUtil.getCheckingPieces(this, sourcePieceIndex);
			} else {
				checkingPieces = CheckUtil.getCheckingPieces(this);
			}
		}

		// TODO can this be done incrementally?
		setPinnedAndDiscoPieces();

		if (EngineConstants.ASSERT) {
			ChessBoardTestUtil.testValues(this);
		}
		
		playedBoardStates.inc(zobristKey);
	}

	
	private void doCastling960(int move) {
		
		
		pushHistoryValues(move);
		
		
		final int fromIndex_king 	= MoveUtil.getFromIndex(move);
		final int toIndex_king 		= MoveUtil.getToIndex(move);
		final int sourcePieceIndex 	= MoveUtil.getSourcePieceIndex(move);
		
		if (sourcePieceIndex != KING || !MoveUtil.isCastlingMove(move)) {
			
			throw new IllegalStateException("sourcePieceIndex != KING || !MoveUtil.isCastlingMove(move)");
		}
		
		CastlingUtil.getRookFromToSquareIDs(this, toIndex_king, buff_castling_rook_from_to);
		final int fromIndex_rook 	= buff_castling_rook_from_to[0];
		final int toIndex_rook 		= buff_castling_rook_from_to[1];
		
		
		if (fromIndex_king == toIndex_king) {
			
			long bb 						= Util.POWER_LOOKUP[fromIndex_rook] | Util.POWER_LOOKUP[toIndex_rook];
			
			pieces[colorToMove][ROOK] 		^= bb;
			friendlyPieces[colorToMove] 	^= bb;
			
			pieceIndexes[fromIndex_rook] 	= EMPTY;
			pieceIndexes[toIndex_rook] 		= ROOK;
			
		} else if (fromIndex_rook == toIndex_rook) {
			
			long bb 						= Util.POWER_LOOKUP[fromIndex_king] | Util.POWER_LOOKUP[toIndex_king];
			
			pieces[colorToMove][KING] 		^= bb;
			friendlyPieces[colorToMove] 	^= bb;
			
			pieceIndexes[fromIndex_king] 	= EMPTY;
			pieceIndexes[toIndex_king] 		= KING;
			
		} else if (fromIndex_rook == toIndex_king && toIndex_rook == fromIndex_king) {
		
			long bb_king					= Util.POWER_LOOKUP[fromIndex_king] | Util.POWER_LOOKUP[toIndex_king];
			pieces[colorToMove][KING] 		^= bb_king;
			
			long bb_rook					= Util.POWER_LOOKUP[fromIndex_rook] | Util.POWER_LOOKUP[toIndex_rook];
			pieces[colorToMove][ROOK] 		^= bb_rook;
			
			pieceIndexes[toIndex_rook] 		= ROOK;
			pieceIndexes[toIndex_king] 		= KING;
			
		} else if (fromIndex_rook == toIndex_king) {
			
			long bb_king					= Util.POWER_LOOKUP[fromIndex_king] | Util.POWER_LOOKUP[toIndex_king];
			pieces[colorToMove][KING] 		^= bb_king;
			
			long bb_rook					= Util.POWER_LOOKUP[fromIndex_rook] | Util.POWER_LOOKUP[toIndex_rook];
			pieces[colorToMove][ROOK] 		^= bb_rook;
			
			friendlyPieces[colorToMove] 	^= (Util.POWER_LOOKUP[fromIndex_king] | Util.POWER_LOOKUP[toIndex_rook]);
			
			pieceIndexes[toIndex_rook] 		= ROOK;
			pieceIndexes[toIndex_king] 		= KING;
			pieceIndexes[fromIndex_king] 	= EMPTY;

			
		} else if (toIndex_rook == fromIndex_king) {
			
			long bb_king					= Util.POWER_LOOKUP[fromIndex_king] | Util.POWER_LOOKUP[toIndex_king];
			pieces[colorToMove][KING] 		^= bb_king;
			
			long bb_rook					= Util.POWER_LOOKUP[fromIndex_rook] | Util.POWER_LOOKUP[toIndex_rook];
			pieces[colorToMove][ROOK] 		^= bb_rook;
			
			friendlyPieces[colorToMove] 	^= (Util.POWER_LOOKUP[toIndex_king] | Util.POWER_LOOKUP[fromIndex_rook]);
			
			pieceIndexes[toIndex_rook] 		= ROOK;
			pieceIndexes[toIndex_king] 		= KING;
			pieceIndexes[fromIndex_rook] 	= EMPTY;
			
		} else {
			
			long bb_king					= Util.POWER_LOOKUP[fromIndex_king] | Util.POWER_LOOKUP[toIndex_king];
			pieces[colorToMove][KING] 		^= bb_king;
			friendlyPieces[colorToMove] 	^= bb_king;
			
			long bb_rook					= Util.POWER_LOOKUP[fromIndex_rook] | Util.POWER_LOOKUP[toIndex_rook];
			pieces[colorToMove][ROOK] 		^= bb_rook;
			friendlyPieces[colorToMove] 	^= bb_rook;
			
			pieceIndexes[fromIndex_rook] 	= EMPTY;
			pieceIndexes[fromIndex_king] 	= EMPTY;
			pieceIndexes[toIndex_rook] 		= ROOK;
			pieceIndexes[toIndex_king] 		= KING;
			
		}
		
		
		updateKingValues(colorToMove, toIndex_king);
		
		zobristKey 						^= Zobrist.piece[fromIndex_king][colorToMove][KING] ^ Zobrist.piece[toIndex_king][colorToMove][KING];
		zobristKey 						^= Zobrist.piece[fromIndex_rook][colorToMove][ROOK] ^ Zobrist.piece[toIndex_rook][colorToMove][ROOK];
		
		if (epIndex != 0) {
			zobristKey ^= Zobrist.epIndex[epIndex];
			epIndex = 0;
		}
		
		zobristKey ^= Zobrist.sideToMove;
		
		
		if (castlingRights != 0) {
			
			zobristKey ^= Zobrist.castling[castlingRights];
			
			if (Properties.DUMP_CASTLING) System.out.println("ChessBoard.doMove/KING: castlingRights=" + castlingRights);
			
			castlingRights = CastlingUtil.getKingMovedCastlingRights(castlingRights, colorToMove, castlingConfig);
			
			if (Properties.DUMP_CASTLING) System.out.println("ChessBoard.doMove/KING: NEW castlingRights=" + castlingRights);
			
			zobristKey ^= Zobrist.castling[castlingRights];
		}
		
		
		psqtScore_mg					+= EvalConstants.PSQT_MG[KING][colorToMove][toIndex_king] - EvalConstants.PSQT_MG[KING][colorToMove][fromIndex_king];
		psqtScore_eg 					+= EvalConstants.PSQT_EG[KING][colorToMove][toIndex_king] - EvalConstants.PSQT_EG[KING][colorToMove][fromIndex_king];
		
		psqtScore_mg					+= EvalConstants.PSQT_MG[ROOK][colorToMove][toIndex_rook] - EvalConstants.PSQT_MG[ROOK][colorToMove][fromIndex_rook];
		psqtScore_eg 					+= EvalConstants.PSQT_EG[ROOK][colorToMove][toIndex_rook] - EvalConstants.PSQT_EG[ROOK][colorToMove][fromIndex_rook];
		
		
		allPieces = friendlyPieces[colorToMove] | friendlyPieces[colorToMoveInverse];
		
		emptySpaces = ~allPieces;
		
		changeSideToMove();

		// update checking pieces
		checkingPieces = CheckUtil.getCheckingPieces(this);
		/*if (isDiscoveredMove(fromIndex_king)) {
			checkingPieces = CheckUtil.getCheckingPieces(this);
		} else {
			if (MoveUtil.isNormalMove(move)) {
				checkingPieces = CheckUtil.getCheckingPieces(this, sourcePieceIndex);
			} else {
				checkingPieces = CheckUtil.getCheckingPieces(this);
			}
		}
		*/
		
		// TODO can this be done incrementally?
		setPinnedAndDiscoPieces();

		if (EngineConstants.ASSERT) {
			
			ChessBoardTestUtil.testValues(this);
		}
		
		
		playedBoardStates.inc(zobristKey);
	}
	

	public void undoMove(int move) {
		
		
		if (MoveUtil.isCastlingMove(move)) {
			
			undoCastling960(move);
			
			return;
		}
		
		
		playedBoardStates.dec(zobristKey);
		
		final int fromIndex = MoveUtil.getFromIndex(move);
		int toIndex = MoveUtil.getToIndex(move);
		long toMask = 1L << toIndex;
		final long fromToMask = (1L << fromIndex) ^ toMask;
		final int sourcePieceIndex = MoveUtil.getSourcePieceIndex(move);
		final int attackedPieceIndex = MoveUtil.getAttackedPieceIndex(move);

		if (fromIndex == toIndex) {
			
			throw new IllegalStateException("undoMove: fromIndex == toIndex");
		}
		
		if (EngineConstants.ASSERT) {
			Assert.isTrue(attackedPieceIndex != KING, "attackedPieceIndex != KING");
			Assert.isTrue(attackedPieceIndex == 0 || (Util.POWER_LOOKUP[fromIndex] & friendlyPieces[colorToMove]) == 0, "attackedPieceIndex == 0 || (Util.POWER_LOOKUP[fromIndex] & friendlyPieces[colorToMove]) == 0");
		}
		
		popHistoryValues();

		// undo move
		friendlyPieces[colorToMoveInverse] ^= fromToMask;
		pieceIndexes[fromIndex] = sourcePieceIndex;
		pieces[colorToMoveInverse][sourcePieceIndex] ^= fromToMask;
		psqtScore_mg += EvalConstants.PSQT_MG[sourcePieceIndex][colorToMoveInverse][fromIndex] - EvalConstants.PSQT_MG[sourcePieceIndex][colorToMoveInverse][toIndex];
		psqtScore_eg += EvalConstants.PSQT_EG[sourcePieceIndex][colorToMoveInverse][fromIndex] - EvalConstants.PSQT_EG[sourcePieceIndex][colorToMoveInverse][toIndex];

		switch (sourcePieceIndex) {
		case EMPTY:
			// not necessary but provides a table-index
			break;
		case PAWN:
			pawnZobristKey ^= Zobrist.piece[fromIndex][colorToMoveInverse][PAWN];
			if (MoveUtil.isPromotion(move)) {
				
				if (colorToMoveInverse== WHITE) {
					material_factor_white -= EvalConstants.PHASE[MoveUtil.getMoveType(move)];
				} else {
					material_factor_black -= EvalConstants.PHASE[MoveUtil.getMoveType(move)];
				}
				
				materialKey -= MaterialUtil.VALUES[colorToMoveInverse][MoveUtil.getMoveType(move)] - MaterialUtil.VALUES[colorToMoveInverse][PAWN];
				pieces[colorToMoveInverse][PAWN] ^= toMask;
				pieces[colorToMoveInverse][MoveUtil.getMoveType(move)] ^= toMask;
				psqtScore_mg += EvalConstants.PSQT_MG[PAWN][colorToMoveInverse][toIndex] - EvalConstants.PSQT_MG[MoveUtil.getMoveType(move)][colorToMoveInverse][toIndex];
				psqtScore_eg += EvalConstants.PSQT_EG[PAWN][colorToMoveInverse][toIndex] - EvalConstants.PSQT_EG[MoveUtil.getMoveType(move)][colorToMoveInverse][toIndex];
			} else {
				pawnZobristKey ^= Zobrist.piece[toIndex][colorToMoveInverse][PAWN];
			}
			break;
			
		case KING:
			
			if (MoveUtil.isCastlingMove(move)) {
				
				throw new IllegalStateException("Castling");
			}
			
			updateKingValues(colorToMoveInverse, fromIndex);
		}

		// undo hit
		switch (attackedPieceIndex) {
		case EMPTY:
			break;
		case PAWN:
			if (MoveUtil.isEPMove(move)) {
				pieceIndexes[toIndex] = EMPTY;
				toIndex += ChessConstants.COLOR_FACTOR_8[colorToMove];
				toMask = Util.POWER_LOOKUP[toIndex];
			}
			psqtScore_mg += EvalConstants.PSQT_MG[PAWN][colorToMove][toIndex];
			psqtScore_eg += EvalConstants.PSQT_EG[PAWN][colorToMove][toIndex];
			pawnZobristKey ^= Zobrist.piece[toIndex][colorToMove][PAWN];
			pieces[colorToMove][attackedPieceIndex] |= toMask;
			friendlyPieces[colorToMove] |= toMask;
			materialKey += MaterialUtil.VALUES[colorToMove][PAWN];
			break;
		default:
			psqtScore_mg += EvalConstants.PSQT_MG[attackedPieceIndex][colorToMove][toIndex];
			psqtScore_eg += EvalConstants.PSQT_EG[attackedPieceIndex][colorToMove][toIndex];
			
			if (colorToMove == WHITE) {
				material_factor_white += EvalConstants.PHASE[attackedPieceIndex];
			} else {
				material_factor_black += EvalConstants.PHASE[attackedPieceIndex];
			}
			
			materialKey += MaterialUtil.VALUES[colorToMove][attackedPieceIndex];
			pieces[colorToMove][attackedPieceIndex] |= toMask;
			friendlyPieces[colorToMove] |= toMask;
		}

		pieceIndexes[toIndex] = attackedPieceIndex;
		allPieces = friendlyPieces[colorToMove] | friendlyPieces[colorToMoveInverse];
		emptySpaces = ~allPieces;
		changeSideToMove();

		if (EngineConstants.ASSERT) {
			ChessBoardTestUtil.testValues(this);
		}
	}
	
	
	public void undoCastling960(int move) {
		
		
		playedBoardStates.dec(zobristKey);
		
		
		popHistoryValues();
		
		
		final int fromIndex_king 	= MoveUtil.getFromIndex(move);
		final int toIndex_king 		= MoveUtil.getToIndex(move);
		final int sourcePieceIndex 	= MoveUtil.getSourcePieceIndex(move);
		
		if (sourcePieceIndex != KING || !MoveUtil.isCastlingMove(move)) {
			
			throw new IllegalStateException("sourcePieceIndex != KING || !MoveUtil.isCastlingMove(move)");
		}
		
		CastlingUtil.getRookFromToSquareIDs(this, toIndex_king, buff_castling_rook_from_to);
		final int fromIndex_rook 	= buff_castling_rook_from_to[0];
		final int toIndex_rook 		= buff_castling_rook_from_to[1];
		
		
		if (fromIndex_king == toIndex_king) {
			
			long bb 							= Util.POWER_LOOKUP[fromIndex_rook] | Util.POWER_LOOKUP[toIndex_rook];
			
			pieces[colorToMoveInverse][ROOK] 	^= bb;
			friendlyPieces[colorToMoveInverse] 	^= bb;
			
			pieceIndexes[fromIndex_rook] 		= ROOK;
			pieceIndexes[toIndex_rook] 			= EMPTY;
			
		} else if (fromIndex_rook == toIndex_rook) {
			
			long bb 							= Util.POWER_LOOKUP[fromIndex_king] | Util.POWER_LOOKUP[toIndex_king];
			
			pieces[colorToMoveInverse][KING] 	^= bb;
			friendlyPieces[colorToMoveInverse] 	^= bb;
			
			pieceIndexes[fromIndex_king] 		= KING;
			pieceIndexes[toIndex_king] 			= EMPTY;
			
		} else if (fromIndex_rook == toIndex_king && toIndex_rook == fromIndex_king) {
		
			long bb_king						= Util.POWER_LOOKUP[fromIndex_king] | Util.POWER_LOOKUP[toIndex_king];
			pieces[colorToMoveInverse][KING] 	^= bb_king;
			
			long bb_rook						= Util.POWER_LOOKUP[fromIndex_rook] | Util.POWER_LOOKUP[toIndex_rook];
			pieces[colorToMoveInverse][ROOK] 	^= bb_rook;
			
			pieceIndexes[toIndex_rook] 			= KING;
			pieceIndexes[toIndex_king] 			= ROOK;
			
		} else if (fromIndex_rook == toIndex_king) {
			
			long bb_king						= Util.POWER_LOOKUP[fromIndex_king] | Util.POWER_LOOKUP[toIndex_king];
			pieces[colorToMoveInverse][KING] 	^= bb_king;
			
			long bb_rook						= Util.POWER_LOOKUP[fromIndex_rook] | Util.POWER_LOOKUP[toIndex_rook];
			pieces[colorToMoveInverse][ROOK] 	^= bb_rook;
			
			friendlyPieces[colorToMoveInverse] 	^= (Util.POWER_LOOKUP[fromIndex_king] | Util.POWER_LOOKUP[toIndex_rook]);
			
			pieceIndexes[toIndex_rook] 			= EMPTY;
			pieceIndexes[toIndex_king] 			= ROOK;
			pieceIndexes[fromIndex_king] 		= KING;

			
		} else if (toIndex_rook == fromIndex_king) {
			
			long bb_king						= Util.POWER_LOOKUP[fromIndex_king] | Util.POWER_LOOKUP[toIndex_king];
			pieces[colorToMoveInverse][KING] 	^= bb_king;
			
			long bb_rook						= Util.POWER_LOOKUP[fromIndex_rook] | Util.POWER_LOOKUP[toIndex_rook];
			pieces[colorToMoveInverse][ROOK] 	^= bb_rook;
			
			friendlyPieces[colorToMoveInverse] 	^= (Util.POWER_LOOKUP[toIndex_king] | Util.POWER_LOOKUP[fromIndex_rook]);
			
			pieceIndexes[toIndex_rook] 			= KING;
			pieceIndexes[toIndex_king] 			= EMPTY;
			pieceIndexes[fromIndex_rook] 		= ROOK;
			
		} else {
			
			long bb_king						= Util.POWER_LOOKUP[fromIndex_king] | Util.POWER_LOOKUP[toIndex_king];
			pieces[colorToMoveInverse][KING] 	^= bb_king;
			friendlyPieces[colorToMoveInverse] 	^= bb_king;
			
			long bb_rook						= Util.POWER_LOOKUP[fromIndex_rook] | Util.POWER_LOOKUP[toIndex_rook];
			pieces[colorToMoveInverse][ROOK] 	^= bb_rook;
			friendlyPieces[colorToMoveInverse] 	^= bb_rook;
			
			pieceIndexes[fromIndex_rook] 		= ROOK;
			pieceIndexes[fromIndex_king] 		= KING;
			pieceIndexes[toIndex_rook] 			= EMPTY;
			pieceIndexes[toIndex_king] 			= EMPTY;
			
		}
		
		
		updateKingValues(colorToMoveInverse, fromIndex_king);
		
				
		psqtScore_mg					+= EvalConstants.PSQT_MG[KING][colorToMoveInverse][fromIndex_king] - EvalConstants.PSQT_MG[KING][colorToMoveInverse][toIndex_king];
		psqtScore_eg 					+= EvalConstants.PSQT_EG[KING][colorToMoveInverse][fromIndex_king] - EvalConstants.PSQT_EG[KING][colorToMoveInverse][toIndex_king];
		
		psqtScore_mg					+= EvalConstants.PSQT_MG[ROOK][colorToMoveInverse][fromIndex_rook] - EvalConstants.PSQT_MG[ROOK][colorToMoveInverse][toIndex_rook];
		psqtScore_eg 					+= EvalConstants.PSQT_EG[ROOK][colorToMoveInverse][fromIndex_rook] - EvalConstants.PSQT_EG[ROOK][colorToMoveInverse][toIndex_rook];
		
		
		allPieces = friendlyPieces[colorToMove] | friendlyPieces[colorToMoveInverse];
		
		emptySpaces = ~allPieces;
		
		changeSideToMove();

		if (EngineConstants.ASSERT) {
			
			ChessBoardTestUtil.testValues(this);
		}
	}
	
	
	public void setPinnedAndDiscoPieces() {

		pinnedPieces = 0;
		discoveredPieces = 0;

		for (int kingColor = ChessConstants.WHITE; kingColor <= ChessConstants.BLACK; kingColor++) {

			int enemyColor = 1 - kingColor;

			if (!MaterialUtil.hasSlidingPieces(materialKey, enemyColor)) {
				continue;
			}

			long enemyPiece = (pieces[enemyColor][BISHOP] | pieces[enemyColor][QUEEN]) & MagicUtil.getBishopMovesEmptyBoard(kingIndex[kingColor])
					| (pieces[enemyColor][ROOK] | pieces[enemyColor][QUEEN]) & MagicUtil.getRookMovesEmptyBoard(kingIndex[kingColor]);
			while (enemyPiece != 0) {
				final long checkedPiece = ChessConstants.IN_BETWEEN[kingIndex[kingColor]][Long.numberOfTrailingZeros(enemyPiece)] & allPieces;
				if (Long.bitCount(checkedPiece) == 1) {
					pinnedPieces |= checkedPiece & friendlyPieces[kingColor];
					discoveredPieces |= checkedPiece & friendlyPieces[enemyColor];
				}
				enemyPiece &= enemyPiece - 1;
			}

		}
	}
	
	
	public void updateKingValues(final int kingColor, final int index) {
		if (index == 64) {//If there is no king return
			return;
		}
		kingIndex[kingColor] = index;
		kingArea[kingColor] = ChessConstants.KING_AREA[kingColor][index];
	}
	
	public boolean isLegal(final int move) {
		
		if (MoveUtil.getSourcePieceIndex(move) == KING) {
			
			return !CheckUtil.isInCheckIncludingKing(MoveUtil.getToIndex(move), colorToMove, pieces[colorToMoveInverse],
					allPieces ^ Util.POWER_LOOKUP[MoveUtil.getFromIndex(move)], MaterialUtil.getMajorPieces(materialKey, colorToMoveInverse));
		}

		if (MoveUtil.getAttackedPieceIndex(move) != 0) {
			if (MoveUtil.isEPMove(move)) {
				return isLegalEPMove(MoveUtil.getFromIndex(move));
			}
			return true;
		}

		if (checkingPieces != 0) {
			return !CheckUtil.isInCheck(kingIndex[colorToMove], colorToMove, pieces[colorToMoveInverse],
					allPieces ^ Util.POWER_LOOKUP[MoveUtil.getFromIndex(move)] ^ Util.POWER_LOOKUP[MoveUtil.getToIndex(move)]);
		}
		return true;
	}

	private boolean isLegalEPMove(final int fromIndex) {

		// do move, check if in check, undo move. slow but also not called very often

		final long fromToMask = Util.POWER_LOOKUP[fromIndex] ^ Util.POWER_LOOKUP[epIndex];

		// do-move and hit
		friendlyPieces[colorToMove] ^= fromToMask;
		pieces[colorToMoveInverse][PAWN] ^= Util.POWER_LOOKUP[epIndex + ChessConstants.COLOR_FACTOR_8[colorToMoveInverse]];
		allPieces = friendlyPieces[colorToMove]
				| friendlyPieces[colorToMoveInverse] ^ Util.POWER_LOOKUP[epIndex + ChessConstants.COLOR_FACTOR_8[colorToMoveInverse]];

		/* Check if is in check */
		final boolean isInCheck = CheckUtil.getCheckingPieces(this) != 0;

		// undo-move and hit
		friendlyPieces[colorToMove] ^= fromToMask;
		pieces[colorToMoveInverse][PAWN] |= Util.POWER_LOOKUP[epIndex + ChessConstants.COLOR_FACTOR_8[colorToMoveInverse]];
		allPieces = friendlyPieces[colorToMove] | friendlyPieces[colorToMoveInverse];

		return !isInCheck;

	}
	
	
	public boolean isValidMove(final int move) {
		
		// check piece at from square
		final int fromIndex = MoveUtil.getFromIndex(move);
		final long fromSquare = Util.POWER_LOOKUP[fromIndex];
		if ((pieces[colorToMove][MoveUtil.getSourcePieceIndex(move)] & fromSquare) == 0) {
			return false;
		}

		// check piece at to square
		final int toIndex = MoveUtil.getToIndex(move);
		final long toSquare = Util.POWER_LOOKUP[toIndex];
		final int attackedPieceIndex = MoveUtil.getAttackedPieceIndex(move);
		if (attackedPieceIndex == 0) {
			if (MoveUtil.isCastlingMove(move)) {
				//The rook or king can be on the to_square
				if (pieceIndexes[toIndex] != EMPTY && pieceIndexes[toIndex] != ROOK && pieceIndexes[toIndex] != KING) {
					return false;
				}
			} else {
				if (pieceIndexes[toIndex] != EMPTY) {
					return false;
				}
			}
		} else {
			if ((pieces[colorToMoveInverse][attackedPieceIndex] & toSquare) == 0 && !MoveUtil.isEPMove(move)) {
				return false;
			}
		}


		// check if move is possible
		switch (MoveUtil.getSourcePieceIndex(move)) {
		case PAWN:
			if (MoveUtil.isEPMove(move)) {
				if (toIndex != epIndex) {
					return false;
				}
				return isLegalEPMove(fromIndex);
			} else {
				if (colorToMove == WHITE) {
					if (fromIndex > toIndex) {
						return false;
					}
					// 2-move
					if (toIndex - fromIndex == 16 && (allPieces & Util.POWER_LOOKUP[fromIndex + 8]) != 0) {
						return false;
					}
				} else {
					if (fromIndex < toIndex) {
						return false;
					}
					// 2-move
					if (fromIndex - toIndex == 16 && (allPieces & Util.POWER_LOOKUP[fromIndex - 8]) != 0) {
						return false;
					}
				}
			}
			break;
		case NIGHT:
			break;
		case BISHOP:
			// fall-through
		case ROOK:
			// fall-through
		case QUEEN:
			if ((ChessConstants.IN_BETWEEN[fromIndex][toIndex] & allPieces) != 0) {
				return false;
			}
			break;
			
		case KING:
			
			if (MoveUtil.isCastlingMove(move)) {
				
				long castlingIndexes = CastlingUtil.getCastlingIndexes(colorToMove, castlingRights, castlingConfig);
				
				if (Properties.DUMP_CASTLING) System.out.println("ChessBoard.isValidMove.Castling: castlingIndexes=" + castlingIndexes);
				
				while (castlingIndexes != 0) {
					
					if (toIndex == Long.numberOfTrailingZeros(castlingIndexes)) {
						
						return CastlingUtil.isValidCastlingMove(this, fromIndex, toIndex);
					}
					
					castlingIndexes &= castlingIndexes - 1;
				}
				
				return false;
			}
			
			return isLegalKingMove(move);
		}

		if ((fromSquare & pinnedPieces) != 0) {
			if ((ChessConstants.PINNED_MOVEMENT[fromIndex][kingIndex[colorToMove]] & toSquare) == 0) {
				return false;
			}
		}

		if (checkingPieces != 0) {
			if (attackedPieceIndex == 0) {
				return isLegalNonKingMove(move);
			} else {
				if (Long.bitCount(checkingPieces) >= 2) {
					return false;
				}
				return (toSquare & checkingPieces) != 0;
			}
		}

		return true;
	}
	
	
	private boolean isLegalKingMove(final int move) {
		return !CheckUtil.isInCheckIncludingKing(MoveUtil.getToIndex(move), colorToMove, pieces[colorToMoveInverse],
				allPieces ^ Util.POWER_LOOKUP[MoveUtil.getFromIndex(move)]);
	}
	
	
	private boolean isLegalNonKingMove(final int move) {
		return !CheckUtil.isInCheck(kingIndex[colorToMove], colorToMove, pieces[colorToMoveInverse],
				allPieces ^ Util.POWER_LOOKUP[MoveUtil.getFromIndex(move)] ^ Util.POWER_LOOKUP[MoveUtil.getToIndex(move)]);
	}
	
	
	public int getRepetition() {
		int count = playedBoardStates.get(zobristKey);
		if (count == StackLongInt.NO_VALUE) {
			return 0;
		} else return count;
	}
}