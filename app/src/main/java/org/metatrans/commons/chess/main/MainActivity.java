package org.metatrans.commons.chess.main;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.Menu;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.chessartforkids.model.FieldSelection;
import com.chessartforkids.model.GameData;
import com.chessartforkids.model.IPlayer;
import com.chessartforkids.model.Move;
import com.chessartforkids.model.UserSettings;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.metatrans.commons.Activity_Base_Ads_Banner;
import org.metatrans.commons.ads.api.IAdsConfiguration;
import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.app.Application_Base_Ads;
import org.metatrans.commons.chess.GlobalConstants;
import org.metatrans.commons.chess.R;
import org.metatrans.commons.chess.app.Application_Chess_BaseImpl;
import org.metatrans.commons.chess.cfg.IConfiguration;
import org.metatrans.commons.chess.cfg.rules.IConfigurationRule;
import org.metatrans.commons.chess.events.Events;
import org.metatrans.commons.chess.events.IEvent;
import org.metatrans.commons.chess.logic.BoardConstants;
import org.metatrans.commons.chess.logic.BoardManager_AllRules;
import org.metatrans.commons.chess.logic.GameDataUtils;
import org.metatrans.commons.chess.logic.IBoardManager;
import org.metatrans.commons.chess.main.controllers.GameController;
import org.metatrans.commons.chess.main.controllers.OnTouchListener_Main;
import org.metatrans.commons.chess.main.controllers.time.ITimeController;
import org.metatrans.commons.chess.main.controllers.time.TimeController_Increasing;
import org.metatrans.commons.chess.main.views.IBoardViewActivity;
import org.metatrans.commons.chess.main.views.IBoardVisualization;
import org.metatrans.commons.chess.main.views.IPanelsVisualization;
import org.metatrans.commons.chess.main.views.MainView;
import org.metatrans.commons.chess.utils.CachesBitmap;
import org.metatrans.commons.chess.utils.StorageUtils_BoardSelections;
import org.metatrans.commons.ui.images.IBitmapCache;

import bagaturchess.bitboard.impl.Constants;


public abstract class MainActivity extends Activity_Base_Ads_Banner implements BoardConstants, GlobalConstants, IBoardViewActivity {


	protected static final int MAIN_VIEW_ID = 1234567890;


	protected IBoardManager manager;

	protected GameController gameController;


	private ITimeController timeController;
	
	private OnTouchListener_Main boardEventProcessor;
	
	private ExecutorService executor;

	private Handler uiHandler;
	
	private long timestamp_resume = 0;	
	
	private static Toast toast;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		System.out.println("MainActivity.onCreate: savedInstanceState=" + savedInstanceState);

		super.onCreate(savedInstanceState);
		
		handleIntent(getIntent());
	}

	
	@Override
	public void onNewIntent(Intent intent){

		System.out.println("MainActivity.onNewIntent: intent=" + intent);

		super.onNewIntent(intent);
		
		setIntent(intent);
		
		handleIntent(intent);
	}


	private void handleIntent(Intent intent) {

		try {
			
			System.out.println("MainActivity.handleIntent: intent=" + intent);
			
			if (executor == null) executor = Executors.newCachedThreadPool();

			if (uiHandler == null) uiHandler = new Handler(Looper.getMainLooper());

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		String action = intent.getStringExtra("myaction");
			
		if (action != null && action.equals("new")) {
			
			System.out.println("MainActivity.handleIntent: with extra 'myaction=new'");
			
			//Prevent executing again the action e.g. on screen rotation
			intent.removeExtra("myaction");

			Events.handleGameEvents_OnExit(this, getGameData(), getUserSettings());

			createNewGame(Constants.INITIAL_BOARD);
		}
		
		CharSequence text = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
		
		if (text != null) {
			
			System.out.println("MainActivity.handleIntent: Imported FEN is " + text);
			
			try {
				
				GameData gameData = GameDataUtils.createGameDataForNewGame(getUserSettings().playerTypeWhite, getUserSettings().playerTypeBlack, getUserSettings().boardManagerID, getUserSettings().computerModeID, text.toString().trim());

				Application_Base.getInstance().storeGameData(gameData);


				setContentView(getMainLayout());

				ViewGroup frame = findViewById(getMainLayoutID());

				MainView mainview = new MainView(this, null);

				mainview.setId(MAIN_VIEW_ID);

				frame.addView(mainview);


				manager = new BoardManager_AllRules((GameData) Application_Base.getInstance().getGameData());

				gameController = new GameController(this, manager, mainview);

				createTimeController();
				
			} catch (Exception e) {
				
				showToast("MainActivity.handleIntent: Not valid FEN format: '" + text + "'");
				
				e.printStackTrace();
			}
		}
	}


	public abstract Class getMainMenuClass();

	
	public void showToast(String info) {
		
		if (toast != null) {
			toast.cancel();
		}
		
        toast = Toast.makeText(this, info, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, 0);
        
        toast.show();
        
	}


	@Override
	protected IBitmapCache getBitmapCache() {
		return CachesBitmap.getSingletonFullSized();
	}
	
	
	@Override
	public void onBackPressed() {

		super.onBackPressed();
	}
	
	
	@Override
	protected void onDestroy() {
		
		System.out.println("MainActivity: onDestroy");

		clear();
		
		super.onDestroy();
		
		System.gc();
	}
	
	
	private void clear() {
		
		manager = null;
		
		gameController = null;
		
		boardEventProcessor = null;
		
		List<Runnable> rejected = executor.shutdownNow();

		System.out.println("MainActivity: shutting down executor -> rejected " + rejected.size() + " jobs.");
		
		executor = null;
		
		uiHandler.removeCallbacksAndMessages(null);
		
		uiHandler = null;
	}
	
	
	private void initMainView() {

		setContentView(getMainLayout());

		ViewGroup frame = findViewById(getMainLayoutID());

		MainView mainview = new MainView(this, null);

		mainview.setId(MAIN_VIEW_ID);

		frame.addView(mainview);

		setBackgroundPoster(getMainLayoutID(), 77);
		
		boardEventProcessor = new OnTouchListener_Main(this);
		
		mainview.setOnTouchListener((OnTouchListener) boardEventProcessor);
	}
	
	
	protected int getMainLayoutID() {
		boolean left_handed = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
		int mainlayoutID = left_handed ? R.id.layout_main_horizontal_left : R.id.layout_main_vertical;
		return mainlayoutID;
	}


	protected int getMainLayout() {
		boolean left_handed = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
		int mainlayout = left_handed ? R.layout.main_horizontal : R.layout.main_vertical;
		return mainlayout;
	}
	
	
	public void createNewGame(String fen) {
		
		try {
			
			if (gameController != null) {
				gameController.stopThinking();
				gameController.setTimeController(null);
			}
			
			
			if (timeController == null) {
				//throw new IllegalStateException();
				//May be null if the game is finished
			} else {
				timeController.pauseAll();
				timeController.destroy();
				timeController = null;	
			}
			
			
			if (toast != null) {
				toast.cancel();
				toast = null;
			}
			
			
			if (uiHandler != null) uiHandler.removeCallbacksAndMessages(null);


			GameData gameData = GameDataUtils.createGameDataForNewGame(getUserSettings().playerTypeWhite, getUserSettings().playerTypeBlack, getUserSettings().boardManagerID, getUserSettings().computerModeID, fen);

			Application_Base.getInstance().storeGameData(gameData);

			Set<FieldSelection>[][] selections = GameDataUtils.createEmptySelections();

			StorageUtils_BoardSelections.writeStore(this, selections);
			
			
			//Reload game
			createBoardManager(gameData);
			
			//Redraw
			if (getMainView() != null) {
				getMainView().getBoardView().clearSelections();
				getMainView().getBoardView().setData(getBoardManager().getBoard_WithoutHided());
				getMainView().getPanelsView().setCapturedPieces(getBoardManager().getCaptured_W(),
						getBoardManager().getCaptured_B(),
						getBoardManager().getCapturedSize_W(),
						getBoardManager().getCapturedSize_B());
				
				getMainView().getBoardView().redraw();		
				getMainView().getPanelsView().redraw();
			}
			
			//Resume controller
			gameController.resumeGame();
			
			
			Events.handleGameEvents_OnStart(this, gameData);
			
			Events.register(this,
					Events.create(IEvent.MENU_OPERATION, IEvent.MENU_OPERATION_NEW_GAME, 
					"MENU_OPERATION", "NEW_GAME" ));

			Application_Base_Ads.getInstance().openInterstitial();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public IBoardManager getBoardManager() {
		return manager;
	}


	@Override
	public IConfiguration getUIConfiguration() {

		return ((Application_Chess_BaseImpl) Application_Base.getInstance()).getUIConfiguration();
	}


	@Override
	protected void onPause() {
		
		System.out.println("MainActivity: onPause");


		getGameData().save();

		getUserSettings().save();

		StorageUtils_BoardSelections.writeStore(this, getBoard().getSelections());


		if (!getGameData().isCountedAsCompleted()) {
			long lastPeriodInsideTheMainScreen = System.currentTimeMillis() - timestamp_resume;
			getGameData().addAccumulated_time_inmainscreen(lastPeriodInsideTheMainScreen);
		}
		
		Events.updateLastMainScreenInteraction(this, System.currentTimeMillis());
		
		//waitInitialization();
		
		//dumpGameData(getGameData());
		
		if (gameController != null) {
			gameController.stopThinking();
			gameController.setTimeController(null);
		}
		
		
		if (timeController == null) {
			//throw new IllegalStateException();
			//May be null if the game is finished
		} else {
			timeController.destroy();
			timeController = null;	
		}
		
		
		if (toast != null) {
			toast.cancel();
			toast = null;
		}
		
		if (uiHandler != null) uiHandler.removeCallbacksAndMessages(null);
		
		super.onPause();
		
		CachesBitmap.clearAll();
		
		System.gc();
	}


	@Override
	protected void onResume() {
		
		System.out.println("MainActivity: onResume");

		System.gc();
		
		timestamp_resume = System.currentTimeMillis();

		createBoardManager(getGameData());

		initMainView();

		initBoardAndPanelsViews(StorageUtils_BoardSelections.readStorage(this));


		super.onResume();


		int gameStatus = getBoardManager().getGameStatus();
		
		if (gameStatus == GlobalConstants.GAME_STATUS_NONE) {
			
			if (timeController != null) {

				createTimeController();
			}

			gameController.resumeGame();

		} else {
			
			if (getGameData().getMoves().size() - 1 >= 0) {
				
				Move lastmove = getGameData().getMoves().get(getGameData().getMoves().size() - 1);
				
				if (lastmove.isPromotion) {
					
					getMainView().getBoardView().clearSelections();					
					
					//Update UI
					getMainView().getBoardView().markingSelection_Permanent_Border(lastmove.fromLetter, lastmove.fromDigit);
					getMainView().getBoardView().markingSelection_Permanent_Border(lastmove.toLetter, lastmove.toDigit);
					getMainView().getBoardView().redraw();		
					getMainView().getPanelsView().redraw();					
				}	
			}
			
			String gameStatusText = null;
			switch(gameStatus) {
				case GlobalConstants.GAME_STATUS_WHITE_WINS:
					gameStatusText = getString(R.string.game_status_white_wins);
					getMainView().getBoardView().whiteWinsSelection();
					break;
				case GlobalConstants.GAME_STATUS_BLACK_WINS:
					gameStatusText = getString(R.string.game_status_black_wins);
					getMainView().getBoardView().blackWinsSelection();
					break;
				case GlobalConstants.GAME_STATUS_DRAW:
					gameStatusText = getString(R.string.game_status_draw);
					break;
				default:
					throw new IllegalStateException("game states = " + gameStatus);
			}
			
			gameStatusText += " ";//"\r\n";
			gameStatusText += getString(R.string.game_status_inmoves1);
			gameStatusText += " " + ((getBoardManager().getGameData().getMoves().size() / 2) + 1);
			gameStatusText += " " + getString(R.string.game_status_inmoves2);
			
			getBoardManager().getGameData().getBoarddata().gameResultText = gameStatusText;
			
			CachesBitmap.clearAll();
			
			//Achievements
			Events.handleGameEvents_OnFinish(this, getBoardManager().getGameData(), getUserSettings(), getBoardManager().getGameStatus());
		}
	}


	@Override
	protected String getBannerName() {
		return IAdsConfiguration.AD_ID_BANNER1;
	}


	@Override
	public String getInterstitialName() {
		return IAdsConfiguration.AD_ID_INTERSTITIAL1;
	}


	public synchronized MainView getMainView() {

		MainView mainview = findViewById(MAIN_VIEW_ID);

		return mainview;
	}


	@Override
	protected FrameLayout getFrame() {

		ViewGroup frame = findViewById(getMainLayoutID());

		return (FrameLayout) frame;
	}


	public void createTimeController() {
		
		if (timeController != null){

			timeController.destroy();

			timeController = null;
		}
		
		timeController = new TimeController_Increasing(this);

		timeController.setData(getBoardManager().getGameData().getAccumulated_time_white(), getBoardManager().getGameData().getAccumulated_time_black());

		gameController.setTimeController(timeController);
	}


	private GameData getGameData() {

		return (GameData) Application_Base.getInstance().getGameData();
	}
	
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);
	}
	
	
	public IBoardVisualization getBoard() {

		return getMainView().getBoardView();	
	}
	
	
	public IPanelsVisualization getPanels() {

		return getMainView().getPanelsView();	
	}
	
	
	private synchronized void initBoardAndPanelsViews(Set<FieldSelection>[][] selections) {
		
		IBoardVisualization boardview = getBoard();
		boardview.setData(getBoardManager().getBoard_Full()); 
		if (selections != null) boardview.setSelections(selections);
		boardview.redraw();
		
		IPanelsVisualization panelsview = getPanels();
		panelsview.setCapturedPieces(getBoardManager().getCaptured_W(), getBoardManager().getCaptured_B(), getBoardManager().getCapturedSize_W(), getBoardManager().getCapturedSize_B());
		panelsview.redraw();
	}
	
	
	public void switchPlayerWhite(GameData data) {
		
		IPlayer white = data.getWhite();
		
		if (white.getType() == PLAYER_TYPE_HUMAN) {
			//if (data.getWhite_backup() == null) {
				data.setWhite_backup(white);
				data.setWhite(GameDataUtils.createPlayer(PLAYER_TYPE_COMPUTER, COLOUR_PIECE_WHITE));
			//} else {
			//	data.setWhite(data.getWhite_backup());
			//	data.setWhite_backup(white);
			//}
			
			Events.register(this, Events.create(IEvent.CHANGE_AUTO, IEvent.CHANGE_AUTO_W_COMPUTER, "CHANGE_AUTO", "CHANGE_AUTO_W_COMPUTER"));
			
		} else if (white.getType() == PLAYER_TYPE_COMPUTER) {
			//if (data.getWhite_backup() == null) {
				data.setWhite_backup(white);
				data.setWhite(GameDataUtils.createPlayer(PLAYER_TYPE_HUMAN, COLOUR_PIECE_WHITE));
			//} else {
			//	data.setWhite(data.getWhite_backup());
			//	data.setWhite_backup(white);
			//}
			
			Events.register(this, Events.create(IEvent.CHANGE_AUTO, IEvent.CHANGE_AUTO_W_HUMAN, "CHANGE_AUTO", "CHANGE_AUTO_W_HUMAN"));
			
		} else {
			throw new IllegalStateException("player type is " + white.getType());
		}
	}
	
	
	public void switchPlayerBlack(GameData data) {
		
		IPlayer black = data.getBlack();
		
		if (black.getType() == PLAYER_TYPE_HUMAN) {
			//if (data.getBlack_backup() == null) {
				data.setBlack_backup(black);
				data.setBlack(GameDataUtils.createPlayer(PLAYER_TYPE_COMPUTER, COLOUR_PIECE_BLACK));
			//} else {
			//	data.setBlack(data.getBlack_backup());
			//	data.setBlack_backup(black);
			//}
			
			Events.register(this, Events.create(IEvent.CHANGE_AUTO, IEvent.CHANGE_AUTO_B_COMPUTER, "CHANGE_AUTO", "CHANGE_AUTO_B_COMPUTER"));
			
		} else if (black.getType() == PLAYER_TYPE_COMPUTER) {
			//if (data.getBlack_backup() == null) {
				data.setBlack_backup(black);
				data.setBlack(GameDataUtils.createPlayer(PLAYER_TYPE_HUMAN, COLOUR_PIECE_BLACK));
			//} else {
			//	data.setBlack(data.getBlack_backup());
			//	data.setBlack_backup(black);
			//}
			
			Events.register(this, Events.create(IEvent.CHANGE_AUTO, IEvent.CHANGE_AUTO_B_HUMAN, "CHANGE_AUTO", "CHANGE_AUTO_B_HUMAN"));
			
		} else {
			throw new IllegalStateException("player type is " + black.getType());
		}
	}
	
	
	protected void createBoardManager(GameData gamedata) {
		
		manager = null;

		if (gamedata.getBoardManagerID() == IConfigurationRule.BOARD_MANAGER_ID_ALL_RULES) {

			manager = new BoardManager_AllRules(gamedata);

		} else {

			throw new IllegalStateException("boardManagerID=" + gamedata.getBoardManagerID());
		}
		
		MainView mainview = findViewById(MAIN_VIEW_ID);

		gameController = new GameController(this, manager, mainview);
	}
	
	
	public GameController getGameController() {

		return gameController;
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		Intent i = new Intent(getApplicationContext(), getMainMenuClass());

		startActivity(i);
		
		return false;
	}
	
	
	public void executeJob(Runnable r) {

		try {

			executor.execute(r);

		} catch(Exception e) {
			System.out.println("Rejected job: " + r);
			e.printStackTrace();
		}
	}
	
	
	public Handler getUIHandler() {
		return uiHandler;
	}


	public UserSettings getUserSettings() {
		return (UserSettings) Application_Chess_BaseImpl.getInstance().getUserSettings();
	}


	public void keepScreenOn() {
		getMainView().getBoardView().setKeepScreenOn(true);
		getMainView().getPanelsView().setKeepScreenOn(true);
		getMainView().setKeepScreenOn(true);
		//getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}
	
	
	public void keepScreenOff() {
		getMainView().getBoardView().setKeepScreenOn(false);
		getMainView().getPanelsView().setKeepScreenOn(false);
		getMainView().setKeepScreenOn(false);
		//getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}
}
