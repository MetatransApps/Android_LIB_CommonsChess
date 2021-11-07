package org.metatrans.commons.chess.loading;


import android.os.Bundle;
import android.view.View;

import org.metatrans.commons.Activity_Base;
import org.metatrans.commons.ads.api.IAdsConfiguration;
import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.cfg.colours.ConfigurationUtils_Colours;
import org.metatrans.commons.cfg.colours.IConfigurationColours;
import org.metatrans.commons.chess.R;
import org.metatrans.commons.chess.main.MainActivity;
import org.metatrans.commons.chess.menu.MenuActivity_Pieces;
import org.metatrans.commons.chess.model.UserSettings;
import org.metatrans.commons.chess.utils.StaticCache;


public class Activity_Loading extends org.metatrans.commons.loading.Activity_Loading_Base_Ads {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	}

	
	@Override
	protected void load() {

		if (!loaded) {

			StaticCache.initBoardManagersClasses();

		} else {

			super.load();
		}
	}
	
	
	@Override
	protected void onResume() {
		
		System.out.println("Activity_Loading:  onResume");

		super.onResume();
	}
	
	
	@Override
	protected Class<? extends Activity_Base> getNextActivityClass() {
		return MainActivity.class;
	}
	
	
	@Override
	protected Class<? extends Activity_Base> getActivityClass_Menu1() {
		return MenuActivity_Pieces.class;
	}


	@Override
	protected int getText_Menu1() {
		return R.string.menu_pieces_scheme;
	}


	@Override
	protected Class<? extends Activity_Base> getActivityClass_Menu2() {
		return null;
	}


	@Override
	protected int getText_Menu2() {
		return R.string.menu_difficulty;
	}
	
	
	@Override
	protected String getBannerName() {
		return IAdsConfiguration.AD_ID_BANNER1;
	}
	
	
	@Override
	protected String getInterstitialName() {
		return IAdsConfiguration.AD_ID_INTERSTITIAL1;
	}


	@Override
	protected IConfigurationColours getColoursCfg() {

		UserSettings settings = null;

		try {

			settings = (UserSettings) Application_Base.getInstance().getUserSettings();

		} catch (Exception e) {

			if (Application_Base.getInstance().isTestMode()) {
				throw e;
			}

			e.printStackTrace();

			//In case of incompatible change of UserSettings class and failed deserialization, we lose the current settings and create new one
			Application_Base.getInstance().recreateUserSettings();
			
			settings = (UserSettings) Application_Base.getInstance().getUserSettings();
		}

		IConfigurationColours colors_cfg = ConfigurationUtils_Colours.getConfigByID(settings.uiColoursID);

		return colors_cfg;
	}


	@Override
	protected View getLoadingView() {
		View_Loading view = new View_Loading(this /*, getUserSettings()*/);
		return view;
	}
}
