package org.metatrans.commons.chess.app;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Set;

import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.app.Application_Base_Ads;
import org.metatrans.commons.cfg.colours.ConfigurationUtils_Colours;
import org.metatrans.commons.cfg.colours.IConfigurationColours;
import org.metatrans.commons.cfg.menu.ConfigurationUtils_Base_MenuMain;
import org.metatrans.commons.chess.BuildConfig;
import org.metatrans.commons.chess.cfg.Configuration_BaseImpl;
import org.metatrans.commons.chess.cfg.IConfiguration;
import org.metatrans.commons.chess.cfg.animation.ConfigurationUtils_Animation;
import org.metatrans.commons.chess.cfg.pieces.ConfigurationUtils_Pieces;
import org.metatrans.commons.chess.cfg.pieces.IConfigurationPieces;
import org.metatrans.commons.chess.cfg.rules.ConfigurationUtils_Bagatur_AllRules;
import org.metatrans.commons.chess.engines.EngineClient_LocalImpl;
import org.metatrans.commons.chess.events.Events;
import org.metatrans.commons.chess.logic.GameDataUtils;
import org.metatrans.commons.chess.model.EditBoardData;
import org.metatrans.commons.chess.model.FieldSelection;
import org.metatrans.commons.chess.model.GameData;
import org.metatrans.commons.chess.model.UserSettings;
import org.metatrans.commons.chess.utils.CachesBitmap;
import org.metatrans.commons.chess.utils.StorageUtils_BoardSelections;
import org.metatrans.commons.events.EventsManager_Base;
import org.metatrans.commons.events.api.IEventsManager;
import org.metatrans.commons.model.GameData_Base;
import org.metatrans.commons.model.UserSettings_Base;
import org.metatrans.commons.ui.utils.DebugUtils;

import bagaturchess.bitboard.impl1.internal.MoveGenerator;


public abstract class Application_Chess_BaseImpl extends Application_Base_Ads {


	private static final String EDIT_BOARD_DATA_FILE_NAME = "EditBoardData";


	private static final String[] KEYWORDS = new String[] {"chess", "kids", "art"};


	//"Called when the application is starting, before any other application objects have been created."
	@Override
	public void onCreate() {

		System.out.println("Application_Bagatur: onCreate called " + System.currentTimeMillis());

		super.onCreate();

		migrateData();

		ConfigurationUtils_Pieces.init(this);
		ConfigurationUtils_Colours.getAll();
		ConfigurationUtils_Animation.createInstance();

		loadCustomConfigurations();

		Events.init(getEventsManager());
		
		ConfigurationUtils_Base_MenuMain.createInstance();
		
		MoveGenerator.USE_ContinuationHistory 		= false;
		EngineClient_LocalImpl.MEMORY_USAGE_PERCENT = 0.75;

		System.out.println("Application_Bagatur: onCreate finished " + System.currentTimeMillis());
	}


	/**
	 * This code is for backward compatibility purposes and will be removed in 2022.
	 * It reads the data from the old file structure (single file) and saves it to the new file structure (1 file per each data category).
	 */
	private void migrateData() {

		ObjectInputStream input = null;

		try {

			File file = new File(getFilesDir(), "storage");

			if (file.exists()) {

				//Load from the old structure
				InputStream is = openFileInput("storage");
				InputStream buffer = new BufferedInputStream(is);
				input = new ObjectInputStream(buffer);

				GameData gamedata = (GameData) input.readObject();
				Set<FieldSelection>[][] selections = (Set<FieldSelection>[][])  input.readObject();
				UserSettings user_settings = (UserSettings)  input.readObject();

				//Save in the new structure
				if (user_settings != null) Application_Base.getInstance().storeUserSettings(user_settings);

				if (gamedata != null) Application_Base.getInstance().storeGameData(gamedata);

				if (selections != null) StorageUtils_BoardSelections.writeStore(this, selections);

				file.delete();
			}

		} catch (Throwable t) {

			t.printStackTrace();

		} finally {

			if (input != null) {

				try {
					input.close();

				} catch (IOException e) {}
			}
		}
	}


	protected void loadCustomConfigurations() {
		ConfigurationUtils_Bagatur_AllRules.init(this);
	}


	public EditBoardData getEditBoardData() {
		if (org.metatrans.commons.storage.StorageUtils.storageFileExists(this, EDIT_BOARD_DATA_FILE_NAME)) {
			return (EditBoardData) org.metatrans.commons.storage.StorageUtils.readStorage(this, EDIT_BOARD_DATA_FILE_NAME);
		} else {
			return new EditBoardData();
		}
	}


	public void storeEditBoardData(EditBoardData data) {
		org.metatrans.commons.storage.StorageUtils.writeStore(this, EDIT_BOARD_DATA_FILE_NAME, data);
	}


	public IConfiguration getUIConfiguration() {

		UserSettings settings = (UserSettings) Application_Base.getInstance().getUserSettings();

		IConfigurationColours cfg_colors = ConfigurationUtils_Colours.getConfigByID(settings.uiColoursID);

		IConfigurationPieces cfg_pieces = ConfigurationUtils_Pieces.getConfigByID(settings.uiPiecesID);

		return new Configuration_BaseImpl(cfg_colors, cfg_pieces);
	}


	public String[] getKeywords() {
		return KEYWORDS;
	}


	/*@Override
	protected ILeaderboardsProvider createLeaderboardsProvider() {
		return null;
	}
	

	@Override
	protected IAchievementsManager createAchievementsManager() {
		return null;
	}*/

	
	@Override
	protected IEventsManager createEventsManager() {
		return new EventsManager_Base(getExecutor(), getAnalytics());
	}

	
	@Override
	public boolean isTestMode() {
		boolean productiveMode = !BuildConfig.DEBUG || !DebugUtils.isDebuggable(this);
		return !productiveMode;
	}
	
	
	@Override
	public void onLowMemory() {
		
		System.out.println("Application_Bagatur: LOW MEMORY");
		
		CachesBitmap.clearAll();
		
		super.onLowMemory();
		
		//This is called when the overall system is running low on memory, and would like actively running processes to tighten their belts.
	}


	@Override
	public GameData_Base createGameDataObject() {

		//String fen = Constants.INITIAL_BOARD;

		UserSettings settings = (UserSettings) getUserSettings();

		return GameDataUtils.createGameDataForNewGame(settings.playerTypeWhite, settings.playerTypeBlack, settings.boardManagerID, settings.computerModeID/*, fen*/);
	}


	@Override
	public UserSettings_Base createUserSettingsObject() {
		return new UserSettings();
	}
}
