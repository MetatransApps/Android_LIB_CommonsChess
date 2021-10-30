package org.metatrans.commons.chess.menu;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.chessartforkids.model.GameData;
import com.chessartforkids.model.UserSettings;

import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.cfg.menu.Config_MenuMain_Base;
import org.metatrans.commons.cfg.menu.IConfigurationMenu_Main;
import org.metatrans.commons.chess.Alerts;
import org.metatrans.commons.chess.R;
import org.metatrans.commons.chess.cfg.animation.ConfigurationUtils_Animation;
import org.metatrans.commons.chess.cfg.pieces.ConfigurationUtils_Pieces;
import org.metatrans.commons.chess.events.Events;
import org.metatrans.commons.menu.Activity_Menu_Main_Base;
import org.metatrans.commons.web.Activity_WebView_StatePreservingImpl_With_VideoPlayer;

import java.util.ArrayList;
import java.util.List;


public abstract class Activity_MenuMain extends Activity_Menu_Main_Base {


	public static int CFG_MENU_PIECES			 		= 16;
	public static int CFG_MENU_NEW			 			= 17;
	public static int CFG_MENU_ANIMATION		 		= 18;

	public static int CFG_MENU_STAR_ON_GITHUB 			= 30;
	public static int CFG_MENU_EDIT_BOARD 				= 31;


	protected abstract Class<?> getMainActivityClass();


	protected abstract Class<?> getEditBoardActivityClass();

	
	@Override
	protected List<IConfigurationMenu_Main> getEntries() {


		List<IConfigurationMenu_Main> result = new ArrayList<IConfigurationMenu_Main>();


		result.add(new Config_MenuMain_Base() {

			@Override
			public int getName() {
				return R.string.new_game_fulltext;
			}

			@Override
			public int getIconResID() {
				return R.drawable.ic_new;
			}

			@Override
			public int getID() {
				return CFG_MENU_NEW;
			}

			@Override
			public String getDescription_String() {
				return "";
			}

			@Override
			public Runnable getAction() {

				return new Runnable() {

					@Override
					public void run() {

						AlertDialog.Builder adb = Alerts.createAlertDialog_LoseGame(Activity_MenuMain.this,

								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog, int which) {

										Events.handleGameEvents_OnExit(Activity_MenuMain.this, (GameData) Application_Base.getInstance().getGameData(),
												(UserSettings) Application_Base.getInstance().getUserSettings());

										//New
										Activity_MenuMain.this.finish();

										Intent intent = new Intent(getApplicationContext(), getMainActivityClass());

										intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

										intent.putExtra("myaction","new");

										startActivity(intent);

									}
								});

						adb.show();

					}
				};
			}
		});


		result.add(new Config_MenuMain_Base() {
			
			@Override
			public int getName() {
				return R.string.menu_edit_board;
			}
			
			@Override
			public int getIconResID() {
				return R.drawable.ic_edit_white;
			}
			
			@Override
			public int getID() {
				return CFG_MENU_EDIT_BOARD;
			}
			
			@Override
			public Runnable getAction() {
				
				return new Runnable() {
					
					@Override
					public void run() {
						
						Intent i = new Intent(getApplicationContext(), getEditBoardActivityClass());

						startActivity(i);

						finish();
						
					}
				};
			}
		});


		result.add(new Config_MenuMain_Base() {

			@Override
			public int getName() {
				return R.string.menu_pieces_scheme;
			}

			@Override
			public int getIconResID() {
				return R.drawable.ic_pieces;
			}

			@Override
			public int getID() {
				return CFG_MENU_PIECES;
			}

			@Override
			public String getDescription_String() {
				return getString(R.string.label_current) + ": " + getString(ConfigurationUtils_Pieces.getConfigByID(((UserSettings)Application_Base.getInstance().getUserSettings()).uiPiecesID).getName());
			}

			@Override
			public Runnable getAction() {

				return new Runnable() {

					@Override
					public void run() {

						//Pieces
						Intent i = new Intent(getApplicationContext(), MenuActivity_Pieces.class);
						startActivity(i);
						finish();

					}
				};
			}
		});


		result.add(new Config_MenuMain_Base() {

			@Override
			public int getName() {
				return R.string.menu_animation_mode;
			}

			@Override
			public int getIconResID() {
				return R.drawable.ic_action_compass_white;
			}

			@Override
			public int getID() {
				return CFG_MENU_ANIMATION;
			}

			@Override
			public String getDescription_String() {
				return getString(R.string.label_current) + ": " + getString(ConfigurationUtils_Animation.getInstance().getConfigByID(((UserSettings)Application_Base.getInstance().getUserSettings()).moveAnimationID).getName());
			}

			@Override
			public Runnable getAction() {

				return new Runnable() {

					@Override
					public void run() {

						//Difficulty
						Intent i = new Intent(getApplicationContext(), MenuActivity_Animation.class);
						startActivity(i);
						finish();

					}
				};
			}
		});


		result.add(new Config_MenuMain_Base() {
			
			
			@Override
			public int getName() {
				return R.string.menu_about_bagatur;
			}
			
			@Override
			public int getIconResID() {
				return R.drawable.ic_star_gold;
			}
			
			@Override
			public int getID() {
				return CFG_MENU_STAR_ON_GITHUB;
			}
			
			@Override
			public Runnable getAction() {
				
				return new Runnable() {
					
					@Override
					public void run() {
						Activity currentActivity = Application_Base.getInstance().getCurrentActivity();
						if (currentActivity != null) {
				           	Intent intent = new Intent(currentActivity, Activity_WebView_StatePreservingImpl_With_VideoPlayer.class);
			            	intent.putExtra("URL", "https://github.com/bagaturchess/Bagatur");
			            	intent.putExtra("titleID", getName_String());
			            	currentActivity.startActivity(intent);
						}
					}
				};
			}
		});


		List<IConfigurationMenu_Main> parent_cfg = super.getEntries();

		result.addAll(parent_cfg);


		return result;
	}
}
