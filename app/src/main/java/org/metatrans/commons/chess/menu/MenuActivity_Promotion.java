package org.metatrans.commons.chess.menu;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.cfg.colours.ConfigurationUtils_Colours;
import org.metatrans.commons.cfg.colours.IConfigurationColours;
import org.metatrans.commons.chess.GlobalConstants;
import org.metatrans.commons.chess.R;
import org.metatrans.commons.chess.app.Application_Chess_BaseImpl;
import org.metatrans.commons.chess.cfg.pieces.ConfigurationUtils_Pieces;
import org.metatrans.commons.chess.cfg.pieces.IConfigurationPieces;
import org.metatrans.commons.chess.logic.BoardConstants;
import org.metatrans.commons.chess.logic.board.BoardManager_AllRules;
import org.metatrans.commons.chess.logic.board.BoardUtils;
import org.metatrans.commons.chess.logic.board.IBoardManager;
import org.metatrans.commons.chess.logic.game.GameDataUtils;
import org.metatrans.commons.chess.model.FieldSelection;
import org.metatrans.commons.chess.model.GameData;
import org.metatrans.commons.chess.model.Move;
import org.metatrans.commons.chess.model.MovingPiece;
import org.metatrans.commons.chess.utils.CachesBitmap;
import org.metatrans.commons.chess.utils.StorageUtils_BoardSelections;
import org.metatrans.commons.ui.list.ListAdapter_IdT;
import org.metatrans.commons.ui.list.RowItem_IdT;
import org.metatrans.commons.ui.utils.BitmapUtils;


public class MenuActivity_Promotion extends MenuActivity_Base implements GlobalConstants, BoardConstants {
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.menu_promotion);

		FrameLayout frame = findViewById(R.id.layout_promotion);

		IConfigurationColours coloursCfg = ConfigurationUtils_Colours.getConfigByID(((Application_Base) getApplication()).getUserSettings().uiColoursID);
		int color_background = coloursCfg.getColour_Background();

		frame.setBackgroundColor(color_background);

		setFrame(frame);

		buildView();
	}
	
	
	public void buildView() {
		
		List<RowItem_IdT> rowItems = new ArrayList<RowItem_IdT>();
		
		IConfigurationPieces piecesCfg = ConfigurationUtils_Pieces.getConfigByID(getUserSettings().uiPiecesID);

		GameData gamedata = (GameData) Application_Base.getInstance().getGameData();

		int promotion_colour = gamedata.getBoarddata().colourToMove;

		if (promotion_colour == COLOUR_PIECE_WHITE) { 

			Drawable drawable = BitmapUtils.createDrawable(this, CachesBitmap.getSingletonIcons(getIconSize()).getBitmap(this, piecesCfg.getBitmapResID(BoardConstants.ID_PIECE_W_QUEEN)));
			rowItems.add(new RowItem_IdT(drawable, getString(R.string.queen)));

			drawable = BitmapUtils.createDrawable(this, CachesBitmap.getSingletonIcons(getIconSize()).getBitmap(this, piecesCfg.getBitmapResID(BoardConstants.ID_PIECE_W_ROOK)));
			rowItems.add(new RowItem_IdT(drawable, getString(R.string.rook)));

			drawable = BitmapUtils.createDrawable(this, CachesBitmap.getSingletonIcons(getIconSize()).getBitmap(this, piecesCfg.getBitmapResID(BoardConstants.ID_PIECE_W_BISHOP)));
			rowItems.add(new RowItem_IdT(drawable, getString(R.string.bishop)));

			drawable = BitmapUtils.createDrawable(this, CachesBitmap.getSingletonIcons(getIconSize()).getBitmap(this, piecesCfg.getBitmapResID(BoardConstants.ID_PIECE_W_KNIGHT)));
			rowItems.add(new RowItem_IdT(drawable, getString(R.string.knight)));

		} else {

			Drawable drawable = BitmapUtils.createDrawable(this, CachesBitmap.getSingletonIcons(getIconSize()).getBitmap(this, piecesCfg.getBitmapResID(BoardConstants.ID_PIECE_B_QUEEN)));
			rowItems.add(new RowItem_IdT(drawable, getString(R.string.queen)));

			drawable = BitmapUtils.createDrawable(this, CachesBitmap.getSingletonIcons(getIconSize()).getBitmap(this, piecesCfg.getBitmapResID(BoardConstants.ID_PIECE_B_ROOK)));
			rowItems.add(new RowItem_IdT(drawable, getString(R.string.rook)));

			drawable = BitmapUtils.createDrawable(this, CachesBitmap.getSingletonIcons(getIconSize()).getBitmap(this, piecesCfg.getBitmapResID(BoardConstants.ID_PIECE_B_BISHOP)));
			rowItems.add(new RowItem_IdT(drawable, getString(R.string.bishop)));

			drawable = BitmapUtils.createDrawable(this, CachesBitmap.getSingletonIcons(getIconSize()).getBitmap(this, piecesCfg.getBitmapResID(BoardConstants.ID_PIECE_B_KNIGHT)));
			rowItems.add(new RowItem_IdT(drawable, getString(R.string.knight)));
		}
		
		ListAdapter_IdT adapter = new ListAdapter_IdT(this, rowItems,
				R.layout.menu_promotion_list_item,
				R.id.menu_promotion_listitem_icon, R.id.menu_promotion_listitem_title);
		
		ListView list = findViewById(R.id.menu_promotion_list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener_Menu());
	}
	
	
	private class OnItemClickListener_Menu implements
			AdapterView.OnItemClickListener {
		
		
		private OnItemClickListener_Menu() {
		}
		
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			
			System.out.println("MenuActivity_Promotion: selection=" + position);

			Application_Base.getInstance().getSFXManager().playSound(org.metatrans.commons.R.raw.sfx_button_pressed_2);

			GameData gamedata = (GameData) Application_Base.getInstance().getGameData();

			MovingPiece movingPiece = gamedata.getMovingPiece();

			//Bugfixing for rare cases - no explanation currently ...
			if (movingPiece == null) {

				finish();

				return;
			}

			int colour = gamedata.getBoarddata().colourToMove;
			
			int promotedPieceID = -1;
			switch (position) {
				case 0: //queen
					promotedPieceID = (colour == COLOUR_PIECE_WHITE) ? ID_PIECE_W_QUEEN : ID_PIECE_B_QUEEN;
					break;
				case 1: //rook
					promotedPieceID = (colour == COLOUR_PIECE_WHITE) ? ID_PIECE_W_ROOK : ID_PIECE_B_ROOK;
					break;
				case 2: //bishop
					promotedPieceID = (colour == COLOUR_PIECE_WHITE) ? ID_PIECE_W_BISHOP : ID_PIECE_B_BISHOP;
					break;
				case 3: //knight
					promotedPieceID = (colour == COLOUR_PIECE_WHITE) ? ID_PIECE_W_KNIGHT : ID_PIECE_B_KNIGHT;
					break;
				default:
					throw new IllegalStateException("position=" + position);
			}
			
			Move move = null;
			for (Move cur: movingPiece.moves) {
				if (movingPiece.promotion_letter == cur.toLetter
						&& movingPiece.promotion_digit == cur.toDigit
						&& promotedPieceID == cur.promotedPieceID) {
					move = cur;
					break;
				}
			}


			if (move != null) {

				int current_move_index = gamedata.getCurrentMoveIndex();

				//Continue by removing moves from the game data:
				while (current_move_index < gamedata.getMoves().size() - 1) {
					int remove_index = gamedata.getMoves().size() - 1;
					gamedata.getMoves().remove(remove_index);
					gamedata.getSearchInfos().remove(remove_index);
				}

				updateGameData(gamedata, move);


				Set<FieldSelection>[][] selections = GameDataUtils.createEmptySelections();
				
				FieldSelection selection_from = new FieldSelection();
				selection_from.priority = 1;
				selection_from.colour = ConfigurationUtils_Colours.getConfigByID(getUserSettings().uiColoursID).getColour_Square_MarkingSelection();
				selection_from.shape = FieldSelection.SHAPE_BORDER;
				selection_from.appearace = FieldSelection.APPEARANCE_PERMANENT;
				selections[move.fromLetter][move.fromDigit].add(selection_from);
				
				FieldSelection selection_to = new FieldSelection();
				selection_to.priority = 1;
				selection_to.colour = ConfigurationUtils_Colours.getConfigByID(getUserSettings().uiColoursID).getColour_Square_MarkingSelection();
				selection_to.shape = FieldSelection.SHAPE_BORDER;
				selection_to.appearace = FieldSelection.APPEARANCE_PERMANENT;
				selections[move.toLetter][move.toDigit].add(selection_to);

				StorageUtils_BoardSelections.writeStore(MenuActivity_Promotion.this, selections);
			}
			
			finish();
		}


		private void updateGameData(GameData gamedata, Move move) {

			IBoardManager manager = ((Application_Chess_BaseImpl) Application_Base.getInstance()).createBoardManager(gamedata);

			manager.move(move);

			gamedata = manager.getGameData();

			gamedata.getMoves().add(move);
			gamedata.setCurrentMoveIndex(gamedata.getCurrentMoveIndex() + 1);

			gamedata.getSearchInfos().add(null);
			gamedata.setMovingPiece(null);

			gamedata.getBoarddata().colourToMove = BoardUtils.getColour(move.pieceID) == COLOUR_PIECE_WHITE ? COLOUR_PIECE_BLACK : COLOUR_PIECE_WHITE;

			gamedata.save();
		}
	}
}
