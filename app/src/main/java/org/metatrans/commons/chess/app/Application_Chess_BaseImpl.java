package org.metatrans.commons.chess.app;


import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.app.Application_Base_Ads;
import org.metatrans.commons.cfg.colours.ConfigurationUtils_Colours;
import org.metatrans.commons.cfg.colours.IConfigurationColours;
import org.metatrans.commons.cfg.menu.ConfigurationUtils_Base_MenuMain;
import org.metatrans.commons.chess.BuildConfig;
import org.metatrans.commons.chess.R;
import org.metatrans.commons.chess.cfg.Configuration_BaseImpl;
import org.metatrans.commons.chess.cfg.IConfiguration;
import org.metatrans.commons.chess.cfg.animation.ConfigurationUtils_Animation;
import org.metatrans.commons.chess.cfg.pieces.ConfigurationUtils_Pieces;
import org.metatrans.commons.chess.cfg.pieces.IConfigurationPieces;
import org.metatrans.commons.chess.cfg.rules.ConfigurationUtils_Bagatur_AllRules;
import org.metatrans.commons.chess.cfg.rules.IConfigurationRule;
import org.metatrans.commons.chess.engines.EngineClient_LocalImpl;
import org.metatrans.commons.chess.events.Events;
import org.metatrans.commons.chess.logic.board.BoardManager_AllRules;
import org.metatrans.commons.chess.logic.board.IBoardManager;
import org.metatrans.commons.chess.logic.game.GameDataUtils;
import org.metatrans.commons.chess.model.EditBoardData;
import org.metatrans.commons.chess.model.GameData;
import org.metatrans.commons.chess.model.UserSettings;
import org.metatrans.commons.chess.utils.CachesBitmap;
import org.metatrans.commons.events.EventsManager_Base;
import org.metatrans.commons.events.api.IEventsManager;
import org.metatrans.commons.model.GameData_Base;
import org.metatrans.commons.model.UserSettings_Base;
import org.metatrans.commons.ui.utils.DebugUtils;

import bagaturchess.bitboard.impl1.internal.MoveGenerator;
import bagaturchess.uci.api.ChannelManager;
import bagaturchess.uci.impl.Channel_Console;


public abstract class Application_Chess_BaseImpl extends Application_Base_Ads {


	private static final String EDIT_BOARD_DATA_FILE_NAME = "EditBoardData";


	private static final String[] KEYWORDS = new String[] {"chess", "kids", "art"};


	//"Called when the application is starting, before any other application objects have been created."
	@Override
	public void onCreate() {

		System.out.println("Application_Bagatur: onCreate called " + System.currentTimeMillis());

		super.onCreate();

		ConfigurationUtils_Pieces.init(this);
		ConfigurationUtils_Colours.getAll();
		ConfigurationUtils_Animation.createInstance();

		loadCustomConfigurations();

		Events.init(getEventsManager());
		
		ConfigurationUtils_Base_MenuMain.createInstance();

		//Do not use dedicated memory under Android as it works different than desktop computers.
		EngineClient_LocalImpl.STATIC_JVM_MEMORY 	= 0;
		//Meaning: use 50% of the available memory
		//EngineClient_LocalImpl.MEMORY_USAGE_PERCENT = 0.50;
		MoveGenerator.USE_ContinuationHistory = false;

		ChannelManager.setChannel(new Channel_Console(System.in, System.out, System.out));

		System.out.println("Application_Bagatur: onCreate finished " + System.currentTimeMillis());
	}


	public IBoardManager createBoardManager(GameData gamedata) {

		if (gamedata.getBoardManagerID() == IConfigurationRule.BOARD_MANAGER_ID_ALL_RULES) {

			return new BoardManager_AllRules(gamedata);

		} else {

			throw new IllegalStateException("boardManagerID=" + gamedata.getBoardManagerID());
		}
	}


	protected void loadCustomConfigurations() {
		ConfigurationUtils_Bagatur_AllRules.init(this);
	}


	public EditBoardData getEditBoardData() {

		Object data = org.metatrans.commons.storage.StorageUtils.readStorage(this, EDIT_BOARD_DATA_FILE_NAME);

		if (data != null) {

			if (!EditBoardData.class.equals(data.getClass())) {

				EditBoardData edit_data = new EditBoardData();

				storeEditBoardData(edit_data);

				return edit_data;

			} else {

				return (EditBoardData) data;
			}

		} else {

			EditBoardData edit_data = new EditBoardData();

			storeEditBoardData(edit_data);

			return edit_data;
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


	public int getMovingComputerIconID() {

		return R.drawable.ic_computer_moving_bagatur;
	}


	public String[] getKeywords() {
		return KEYWORDS;
	}

	
	@Override
	protected IEventsManager createEventsManager() {

		return new EventsManager_Base(getExecutor());
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

		return GameDataUtils.createGameDataForNewGame(settings.playerTypeWhite, settings.playerTypeBlack, settings.boardManagerID, settings.computerModeID);
	}


	@Override
	public UserSettings_Base createUserSettingsObject() {
		return new UserSettings();
	}
}
