package org.metatrans.commons.chess.views_and_controllers;


import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

import org.metatrans.commons.chess.cfg.IConfiguration;
import org.metatrans.commons.chess.logic.board.IBoardManager;
import org.metatrans.commons.chess.logic.game.GameManager;
import org.metatrans.commons.chess.model.UserSettings;

import java.util.concurrent.ExecutorService;


public interface IBoardViewActivity {

	IBoardManager getBoardManager();
	IConfiguration getUIConfiguration();
	UserSettings getUserSettings();
	GameManager getGameController();
	IMainView getMainView();
	void updateViewWithGameResult(int gameStatus);

    void startActivity(Intent i);

	Class<? extends Activity> getMainMenuClass();

    void createNewGame(String initialBoard);

	Handler getUIHandler();

	void executeJob(Runnable runnable);

	IBoardVisualization getBoard();

	ExecutorService getExecutor();
}
