package org.metatrans.commons.chess.menu;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.chessartforkids.model.FieldSelection;
import com.chessartforkids.model.GameData;
import com.chessartforkids.model.Move;
import com.chessartforkids.model.MovingPiece;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.cfg.colours.ConfigurationUtils_Colours;
import org.metatrans.commons.cfg.colours.IConfigurationColours;
import org.metatrans.commons.chess.GlobalConstants;
import org.metatrans.commons.chess.R;
import org.metatrans.commons.chess.cfg.pieces.ConfigurationUtils_Pieces;
import org.metatrans.commons.chess.cfg.pieces.IConfigurationPieces;
import org.metatrans.commons.chess.logic.BoardConstants;
import org.metatrans.commons.chess.logic.BoardUtils;
import org.metatrans.commons.chess.logic.GameDataUtils;
import org.metatrans.commons.chess.utils.CachesBitmap;
import org.metatrans.commons.chess.utils.StorageUtils_BoardSelections;
import org.metatrans.commons.ui.list.ListAdapter_IdT;
import org.metatrans.commons.ui.list.RowItem_IdT;
import org.metatrans.commons.ui.utils.BitmapUtils;


public class MenuActivity_Promotion extends MenuActivity_BasePieces implements GlobalConstants, BoardConstants {
	
	
	private int promotion_colour;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.menu_promotion);

		FrameLayout frame = (FrameLayout) findViewById(R.id.layout_promotion);

		IConfigurationColours coloursCfg = ConfigurationUtils_Colours.getConfigByID(((Application_Base) getApplication()).getUserSettings().uiColoursID);
		int color_background = coloursCfg.getColour_Background();

		frame.setBackgroundColor(color_background);

		setFrame(frame);

		buildView();
	}
	
	
	@Override
	public void onResume() {
		
		System.out.println("MenuActivity_Promotion: onResume()");
		
		super.onResume();
		
		if (promotion_colour != ((GameData) Application_Base.getInstance().getGameData()).getBoarddata().colourToMove) {
			buildView();
		}
	}
	
	
	public void buildView() {
		
		List<RowItem_IdT> rowItems = new ArrayList<RowItem_IdT>();
		
		IConfigurationPieces piecesCfg = ConfigurationUtils_Pieces.getConfigByID(getUserSettings().uiPiecesID);
		
		promotion_colour = ((GameData) Application_Base.getInstance().getGameData()).getBoarddata().colourToMove;
		
		int bcolour = ConfigurationUtils_Colours.getConfigByID(getUserSettings().uiColoursID).getColour_Background();

		if (promotion_colour == COLOUR_PIECE_WHITE) { 
			
			ensureBitmapExists(R.drawable.ic_promotion_queen_w, piecesCfg, BoardConstants.ID_PIECE_W_QUEEN, bcolour);
			Drawable drawable = BitmapUtils.createDrawable(this, CachesBitmap.getSingletonIcons(getIconSize()).getBitmap(this, R.drawable.ic_promotion_queen_w));
			rowItems.add(new RowItem_IdT(drawable, getString(R.string.queen)));
			
			ensureBitmapExists(R.drawable.ic_promotion_rook_w, piecesCfg, BoardConstants.ID_PIECE_W_ROOK, bcolour);
			drawable = BitmapUtils.createDrawable(this, CachesBitmap.getSingletonIcons(getIconSize()).getBitmap(this, R.drawable.ic_promotion_rook_w));
			rowItems.add(new RowItem_IdT(drawable, getString(R.string.rook)));
			
			ensureBitmapExists(R.drawable.ic_promotion_bishop_w, piecesCfg, BoardConstants.ID_PIECE_W_BISHOP, bcolour);			
			drawable = BitmapUtils.createDrawable(this, CachesBitmap.getSingletonIcons(getIconSize()).getBitmap(this, R.drawable.ic_promotion_bishop_w));
			rowItems.add(new RowItem_IdT(drawable, getString(R.string.bishop)));
			
			ensureBitmapExists(R.drawable.ic_promotion_knight_w, piecesCfg, BoardConstants.ID_PIECE_W_KNIGHT, bcolour);			
			drawable = BitmapUtils.createDrawable(this, CachesBitmap.getSingletonIcons(getIconSize()).getBitmap(this, R.drawable.ic_promotion_knight_w));
			rowItems.add(new RowItem_IdT(drawable, getString(R.string.knight)));	
		} else {
			
			ensureBitmapExists(R.drawable.ic_promotion_queen_b, piecesCfg, BoardConstants.ID_PIECE_B_QUEEN, bcolour);			
			Drawable drawable = BitmapUtils.createDrawable(this, CachesBitmap.getSingletonIcons(getIconSize()).getBitmap(this, R.drawable.ic_promotion_queen_b));
			rowItems.add(new RowItem_IdT(drawable, getString(R.string.queen)));
			
			ensureBitmapExists(R.drawable.ic_promotion_rook_b, piecesCfg, BoardConstants.ID_PIECE_B_ROOK, bcolour);			
			drawable = BitmapUtils.createDrawable(this, CachesBitmap.getSingletonIcons(getIconSize()).getBitmap(this, R.drawable.ic_promotion_rook_b));
			rowItems.add(new RowItem_IdT(drawable, getString(R.string.rook)));
			
			ensureBitmapExists(R.drawable.ic_promotion_bishop_b, piecesCfg, BoardConstants.ID_PIECE_B_BISHOP, bcolour);			
			drawable = BitmapUtils.createDrawable(this, CachesBitmap.getSingletonIcons(getIconSize()).getBitmap(this, R.drawable.ic_promotion_bishop_b));
			rowItems.add(new RowItem_IdT(drawable, getString(R.string.bishop)));

			ensureBitmapExists(R.drawable.ic_promotion_knight_b, piecesCfg, BoardConstants.ID_PIECE_B_KNIGHT, bcolour);
			drawable = BitmapUtils.createDrawable(this, CachesBitmap.getSingletonIcons(getIconSize()).getBitmap(this, R.drawable.ic_promotion_knight_b));
			rowItems.add(new RowItem_IdT(drawable, getString(R.string.knight)));
		}
		
		ListAdapter_IdT adapter = new ListAdapter_IdT(this, rowItems,
				R.layout.menu_promotion_list_item,
				R.id.menu_promotion_listitem_icon, R.id.menu_promotion_listitem_title);
		
		ListView list = (ListView) findViewById(R.id.menu_promotion_list);
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
			
			MovingPiece movingPiece = ((GameData) Application_Base.getInstance().getGameData()).getMovingPiece();
			
			if (movingPiece == null) {//Bugfixing for rare cases
				finish();
				return;
			}
			
			int colour = BoardUtils.getColour(movingPiece.pieceID);
			
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

				GameData gamedata = ((GameData) Application_Base.getInstance().getGameData());

				gamedata.getMoves().add(move);
				gamedata.getSearchInfos().add(gamedata.getLastSearchInfo());//Add the last search info in order to make it visible on the panel after the move

				gamedata.setMovingPiece(null);

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
	}
}
