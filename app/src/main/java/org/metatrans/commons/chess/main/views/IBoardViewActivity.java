package org.metatrans.commons.chess.main.views;


import org.metatrans.commons.chess.cfg.IConfiguration;
import org.metatrans.commons.chess.logic.IBoardManager;
import org.metatrans.commons.chess.main.controllers.GameController;

import com.chessartforkids.model.UserSettings;


public interface IBoardViewActivity {
	public IBoardManager getBoardManager();
	public IConfiguration getUIConfiguration();
	public UserSettings getUserSettings();
	public GameController getGameController();
	public MainView getMainView();
}
