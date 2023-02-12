package org.metatrans.commons.chess.menu;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.cfg.colours.ConfigurationUtils_Colours;
import org.metatrans.commons.cfg.colours.IConfigurationColours;
import org.metatrans.commons.chess.R;
import org.metatrans.commons.chess.cfg.pieces.ConfigurationUtils_Pieces;
import org.metatrans.commons.chess.cfg.pieces.IConfigurationPieces;
import org.metatrans.commons.chess.events.Events;
import org.metatrans.commons.chess.events.IEvent;
import org.metatrans.commons.chess.logic.BoardConstants;
import org.metatrans.commons.chess.utils.CachesBitmap;
import org.metatrans.commons.events.Event_Base;
import org.metatrans.commons.events.api.IEvent_Base;
import org.metatrans.commons.ui.list.ListViewFactory;
import org.metatrans.commons.ui.list.RowItem_CIdTD;
import org.metatrans.commons.ui.utils.BitmapUtils;


public class MenuActivity_Pieces extends MenuActivity_Base {
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		System.out.println("MenuPieces: onCreate()");

		super.onCreate(savedInstanceState);

		int currOrderNumber = ConfigurationUtils_Pieces.getOrderNumber(getUserSettings().uiPiecesID);

		LayoutInflater inflater = LayoutInflater.from(this);

		IConfigurationColours coloursCfg = ConfigurationUtils_Colours.getConfigByID(((Application_Base) getApplication()).getUserSettings().uiColoursID);

		int color_background = coloursCfg.getColour_Background();

		ViewGroup frame = ListViewFactory.create_CITD_ByXML(this, inflater, buildRows(currOrderNumber), currOrderNumber, color_background, new OnItemClickListener_Menu());

		frame.setBackgroundColor(color_background);

		setContentView(frame);
		setFrame(frame);

		setBackgroundPoster(R.id.commons_listview_frame, 77);
	}
	
	
	public List<RowItem_CIdTD> buildRows(int initialSelection) {
		
		List<RowItem_CIdTD> rowItems = new ArrayList<RowItem_CIdTD>();

		IConfigurationPieces[] piecesCfg = ConfigurationUtils_Pieces.getAll();



		for (int i = 0; i < piecesCfg.length; i++) {


			IConfigurationPieces pieceCfg = piecesCfg[i];

			//int pieceID = i % 2 == 0 ? BoardConstants.ID_PIECE_W_KING : BoardConstants.ID_PIECE_B_KING;
			//int pieceID = BoardConstants.ID_PIECE_W_KNIGHT;
			int pieceID = BoardConstants.ID_PIECE_W_KING;


			//int bcolour = ConfigurationUtils_Colours.getConfigByID(getUserSettings().uiColoursID).getColour_Square_Black();
			//ensureBitmapExists(pieceCfg.getIconResID(), pieceCfg, BoardConstants.ID_PIECE_B_KING, bcolour);

			Drawable drawable = BitmapUtils.createDrawable(this,
					CachesBitmap.getSingletonIcons(getIconSize()).getBitmap(this, pieceCfg.getBitmapResID(pieceID)));
			
			RowItem_CIdTD item = new RowItem_CIdTD(i == initialSelection, drawable, getString(pieceCfg.getName()), "");
			
			rowItems.add(item);
		}

		return rowItems;
	}
	
	
	private class OnItemClickListener_Menu implements
			AdapterView.OnItemClickListener {
		
		
		private OnItemClickListener_Menu() {
		}
		
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			
			//System.out.println("PiecesSelection POS=" + position + ", id=" + id);
			
			int currOrderNumber = ConfigurationUtils_Pieces.getOrderNumber(getUserSettings().uiPiecesID);
			if (position != currOrderNumber) {
				int newCfgID = ConfigurationUtils_Pieces.getID(position);
				changePieces(newCfgID);
			}
			
			finish();
		}
	}
	
	
	public void changePieces(int uiPiecesCfgID) {

		getUserSettings().uiPiecesID = uiPiecesCfgID;
		getUserSettings().save();
		
		CachesBitmap.clearPiecesCache();
		CachesBitmap.clearIconsCache_Promotion();

		IConfigurationPieces cfg_pieces = ConfigurationUtils_Pieces.getConfigByID(uiPiecesCfgID);

		Events.register(this,
				IEvent.EVENT_MENU_OPERATION_CHANGE_PIECES.createByVarianceInCategory3(
						cfg_pieces.getID(), getString(cfg_pieces.getName())
				)
		);
	}
}
