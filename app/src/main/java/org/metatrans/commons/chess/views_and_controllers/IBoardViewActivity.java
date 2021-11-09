package org.metatrans.commons.chess.views_and_controllers;


import android.app.Activity;
import android.content.Intent;

import org.metatrans.commons.chess.cfg.IConfiguration;
import org.metatrans.commons.chess.logic.board.IBoardManager;
import org.metatrans.commons.chess.logic.game.GameManager;
import org.metatrans.commons.chess.model.UserSettings;


public interface IBoardViewActivity {

	public IBoardManager getBoardManager();
	public IConfiguration getUIConfiguration();
	public UserSettings getUserSettings();
	public GameManager getGameController();
	public IMainView getMainView();
	public void updateViewWithGameResult(int gameStatus);

    void startActivity(Intent i);

	Class<? extends Activity> getMainMenuClass();

    void createNewGame(String initialBoard);
}
