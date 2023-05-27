package org.metatrans.commons.chess.edit;


import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout.LayoutParams;

import org.metatrans.commons.Activity_Base_Ads_Banner;
import org.metatrans.commons.ads.api.IAdsConfiguration;
import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.cfg.colours.ConfigurationUtils_Colours;
import org.metatrans.commons.cfg.colours.IConfigurationColours;
import org.metatrans.commons.chess.app.Application_Chess_BaseImpl;
import org.metatrans.commons.chess.cfg.Configuration_BaseImpl;
import org.metatrans.commons.chess.cfg.IConfiguration;
import org.metatrans.commons.chess.cfg.pieces.ConfigurationUtils_Pieces;
import org.metatrans.commons.chess.cfg.pieces.IConfigurationPieces;
import org.metatrans.commons.chess.logic.board.BoardManager_AllRules;
import org.metatrans.commons.chess.logic.game.GameDataUtils;
import org.metatrans.commons.chess.logic.board.IBoardManager;
import org.metatrans.commons.chess.logic.game.GameManager;
import org.metatrans.commons.chess.views_and_controllers.IBoardViewActivity;
import org.metatrans.commons.chess.views_and_controllers.IMainView;
import org.metatrans.commons.chess.model.EditBoardData;
import org.metatrans.commons.chess.model.GameData;
import org.metatrans.commons.chess.model.UserSettings;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;


public abstract class EditBoardActivity extends Activity_Base_Ads_Banner implements IBoardViewActivity {
	
	
    private FrameLayout mLayout;

    private EditBoardView editBoardView;

    private IBoardManager boardManager;
    
    private GameData gamedata_original;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
        
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        mLayout = new FrameLayout(this);

        setContentView(mLayout);
    }
    
    
    @Override
    protected void onResume() {
    	
        super.onResume();

		String caller = "analyse";

    	String fen = ((Application_Chess_BaseImpl) Application_Base.getInstance()).getEditBoardData().fen;

        String caller_extra = getIntent().getStringExtra("caller");

        if (caller_extra != null) {

        	caller = caller_extra;
        }
    	
		createBoardManager(fen, caller);
		
        editBoardView = createView();

        editBoardView.setOnTouchListener(new EditBoard_OnTouchListener(editBoardView));

        editBoardView.getBoardView().setData(boardManager.getBoard_WithoutHided());
        
        LayoutParams previewLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        
        mLayout.addView(editBoardView, previewLayoutParams);

		try {

			ByteArrayOutputStream os_array 	= new ByteArrayOutputStream();
			OutputStream os_buffer 			= new BufferedOutputStream(os_array);

			ObjectOutputStream output 		= new ObjectOutputStream(os_buffer);

			output.writeObject(Application_Base.getInstance().getGameData());

			output.flush();

			InputStream is_array 			= new ByteArrayInputStream(os_array.toByteArray());
			InputStream is_buffer 			= new BufferedInputStream(is_array);
			ObjectInputStream input 		= new ObjectInputStream(is_buffer);

			gamedata_original = (GameData) input.readObject();

		} catch(Exception e) {

			e.printStackTrace();
		}
    }
    
    
    @Override
    protected void onPause() {
    	
        super.onPause();
        
        mLayout.removeView(editBoardView);

        editBoardView = null;
    }


	@Override
	public void onBackPressed() {

		//The GameData is already overrided with the last validated board data in the EditBoardHandler.
		//This is why we need copy of the original gamedata object in order to use it for restore on back pressed.

		if (gamedata_original != null) {

			Application_Base.getInstance().storeGameData(gamedata_original);
		}

		finish();
	}


	protected abstract EditBoardView createView();


	protected abstract int button1ImageID();


	protected abstract int button2ImageID();


	protected abstract void processButton1();


	protected abstract void processButton2();


	@Override
	protected String getBannerName() {
		return IAdsConfiguration.AD_ID_BANNER1;
	}


	@Override
	protected int getGravity() {
		return Gravity.TOP | Gravity.CENTER_HORIZONTAL;
	}


	public void createBoardManager(String fen, String caller) {
		
		System.out.println("EditBoardActivity CALLER is " + caller);
		
		GameData gameData;
		
		if ("analyse".equals(caller)) {

			gameData = (GameData) Application_Base.getInstance().getGameData();
			
		} else if ("processimage".equals(caller)) {
			
			GameData emptyGameData = (GameData) ((Application_Chess_BaseImpl) Application_Base.getInstance()).createGameDataObject();
			
			gameData = GameDataUtils.createGameDataForNewGame(emptyGameData, getUserSettings().playerTypeWhite, getUserSettings().playerTypeBlack,
					getUserSettings().boardManagerID, getUserSettings().computerModeID,
					fen);
			
		} else {
			
			throw new IllegalStateException(caller);
		}
		
		boardManager = new BoardManager_AllRules(gameData);
		
		EditBoardData editBoardData = ((Application_Chess_BaseImpl)Application_Base.getInstance()).getEditBoardData();
		
		editBoardData.fen = boardManager.getFEN();
		
		String[] fenArray = editBoardData.fen.split(" ");
		if (fenArray[1].equals("b")) {
			editBoardData.move_B = true;
			editBoardData.move_W = false;
		} else {
			editBoardData.move_B = false;
			editBoardData.move_W = true;
		}
		if (fenArray[2].contains("K")) {
			editBoardData.castling_K = true;
		} else {
			editBoardData.castling_K = false;
		}
		if (fenArray[2].contains("Q")) {
			editBoardData.castling_Q = true;
		} else {
			editBoardData.castling_Q = false;
		}
		if (fenArray[2].contains("k")) {
			editBoardData.castling_k = true;
		} else {
			editBoardData.castling_k = false;
		}
		if (fenArray[2].contains("q")) {
			editBoardData.castling_q = true;
		} else {
			editBoardData.castling_q = false;
		}
		
		((Application_Chess_BaseImpl)Application_Base.getInstance()).storeEditBoardData(editBoardData);
	}
	
    
	protected GameData getGameData() {
		return getBoardManager().getGameData();
	}
	
	
	@Override
	public IBoardManager getBoardManager() {
		return boardManager;
	}
	
	
	@Override
	public IConfiguration getUIConfiguration() {
		
		IConfigurationColours uiColoursCfg = ConfigurationUtils_Colours.getConfigByID(getUserSettings().uiColoursID);
		IConfigurationPieces uiPiecesCfg = ConfigurationUtils_Pieces.getConfigByID(getUserSettings().uiPiecesID);
		
		return new Configuration_BaseImpl(uiColoursCfg, uiPiecesCfg);
	}
	
	
	@Override
	public UserSettings getUserSettings() {
		return (UserSettings) Application_Chess_BaseImpl.getInstance().getUserSettings();
	}
	
	
	@Override
	public GameManager getGameController() {
		throw new UnsupportedOperationException();
	}
	
	
	@Override
	public IMainView getMainView() {
		throw new UnsupportedOperationException();
	}
	
	
	@Override
	protected FrameLayout getFrame() {
		return mLayout;
	}
}
