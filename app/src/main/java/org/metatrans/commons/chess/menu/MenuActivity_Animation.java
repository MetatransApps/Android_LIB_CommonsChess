package org.metatrans.commons.chess.menu;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.cfg.IConfigurationEntry;
import org.metatrans.commons.cfg.colours.ConfigurationUtils_Colours;
import org.metatrans.commons.cfg.colours.IConfigurationColours;
import org.metatrans.commons.chess.R;
import org.metatrans.commons.chess.cfg.animation.ConfigurationUtils_Animation;
import org.metatrans.commons.chess.utils.CachesBitmap;
import org.metatrans.commons.ui.list.ListViewFactory;
import org.metatrans.commons.ui.list.RowItem_CIdTD;
import org.metatrans.commons.ui.utils.BitmapUtils;


public class MenuActivity_Animation extends MenuActivity_Base {
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		int currOrderNumber = ConfigurationUtils_Animation.getInstance().getOrderNumber(getUserSettings().moveAnimationID);
		
		LayoutInflater inflater = LayoutInflater.from(this);

		IConfigurationColours coloursCfg = ConfigurationUtils_Colours.getConfigByID(((Application_Base) getApplication()).getUserSettings().uiColoursID);
		int color_background = coloursCfg.getColour_Background();

		ViewGroup frame = ListViewFactory.create_CITD_ByXML(this, inflater, buildRows(currOrderNumber), currOrderNumber, color_background, getItemClickListener());

		frame.setBackgroundColor(color_background);

		setContentView(frame);
		setFrame(frame);
		
		setBackgroundPoster(R.id.commons_listview_frame, 77);
	}
	
	
	public AdapterView.OnItemClickListener getItemClickListener() {
		return new OnItemClickListener_Menu(); 
	}
	
	
	public List<RowItem_CIdTD> buildRows(int initialSelection) {
		
		List<RowItem_CIdTD> rowItems = new ArrayList<RowItem_CIdTD>();
		
		IConfigurationEntry[] cfgs = ConfigurationUtils_Animation.getInstance().getAll();
		
		for (int i = 0; i < cfgs.length; i++) {
			
			IConfigurationEntry cfg = cfgs[i];
			
			Bitmap bitmap = CachesBitmap.getSingletonIcons(getIconSize()).getBitmap(this, cfg.getIconResID());
			Drawable drawable = BitmapUtils.createDrawable(this, bitmap);
			RowItem_CIdTD item = new RowItem_CIdTD(true, i == initialSelection, drawable, getString(cfg.getName()), "");
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
			
			//System.out.println("MenuActivity_Difficulty: selection=" + position);

			Application_Base.getInstance().getSFXManager().playSound(org.metatrans.commons.R.raw.sfx_button_pressed_2);

			int currOrderNumber = ConfigurationUtils_Animation.getInstance().getOrderNumber(getUserSettings().moveAnimationID);
			if (position != currOrderNumber) {
				int newCfgID = ConfigurationUtils_Animation.getInstance().getID(position);
				getUserSettings().moveAnimationID = newCfgID;
				Application_Base.getInstance().storeUserSettings();
			}
			
			finish();
		}
	}
}
