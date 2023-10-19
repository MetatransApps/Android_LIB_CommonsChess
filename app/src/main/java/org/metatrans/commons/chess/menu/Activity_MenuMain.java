package org.metatrans.commons.chess.menu;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import org.metatrans.commons.ads.api.IAdsConfiguration;
import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.cfg.menu.Config_MenuMain_Base;
import org.metatrans.commons.cfg.menu.IConfigurationMenu_Main;
import org.metatrans.commons.chess.Alerts;
import org.metatrans.commons.chess.R;
import org.metatrans.commons.chess.app.Application_Chess_BaseImpl;
import org.metatrans.commons.chess.cfg.animation.ConfigurationUtils_Animation;
import org.metatrans.commons.chess.cfg.pieces.ConfigurationUtils_Pieces;
import org.metatrans.commons.chess.cfg.rules.IConfigurationRule;
import org.metatrans.commons.chess.events.Events;
import org.metatrans.commons.chess.logic.board.BoardManager_AllRules;
import org.metatrans.commons.chess.logic.game.GameDataUtils;
import org.metatrans.commons.chess.model.GameData;
import org.metatrans.commons.chess.model.UserSettings;
import org.metatrans.commons.chess.utils.BoardUtils;
import org.metatrans.commons.chess.utils.MessageUtils;
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
	public static int CFG_MENU_SHARE_FEN 				= 32;
	public static int CFG_MENU_COPY_FEN 				= 33;
	public static int CFG_MENU_PASTE_FEN 				= 34;

	public static int CFG_MENU_STOP_ADS		 			= 35;


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

										Application_Base.getInstance().recreateGameDataObject();

										Application_Base.getInstance().storeGameData();

										Events.handleGameEvents_OnStart(Activity_MenuMain.this, (GameData) Application_Base.getInstance().getGameData());

										Activity_MenuMain.this.finish();

										Intent intent = new Intent(getApplicationContext(), getMainActivityClass());

										intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

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
				return R.drawable.ic_action_edit_white;
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

		
		if (getRewardedVideoName() != null) {
			result.add(new Config_MenuMain_Base() {

				@Override
				public int getName() {
					return R.string.new_stopads_title;
				}

				@Override
				public int getIconResID() {
					return R.drawable.ic_action_tv;
				}

				@Override
				public int getID() {
					return CFG_MENU_STOP_ADS;
				}

				@Override
				public String getDescription_String() {
					return getString(R.string.new_stopads_desc);
				}

				@Override
				public Runnable getAction() {

					return new Runnable() {

						@Override
						public void run() {

							Activity_MenuMain.this.openRewardedVideo();
						}
					};
				}
			});
		}


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
				return R.string.fen_share;
			}

			@Override
			public int getIconResID() {
				return R.drawable.ic_share_white;
			}

			@Override
			public int getID() {
				return CFG_MENU_SHARE_FEN;
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
						Intent intent = new Intent(
								android.content.Intent.ACTION_SEND);
						intent.setType("text/plain");
						intent.putExtra(android.content.Intent.EXTRA_TEXT, getFEN_CurrentGame());
						startActivity(Intent.createChooser(intent, "Share via"));

						finish();
					}
				};
			}
		});

		result.add(new Config_MenuMain_Base() {

			@Override
			public int getName() {
				return R.string.fen_copy;
			}

			@Override
			public int getIconResID() {
				return R.drawable.ic_copy_white;
			}

			@Override
			public int getID() {
				return CFG_MENU_COPY_FEN;
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
						if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
							android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
							clipboard.setText(getFEN_CurrentGame());
						} else {
							android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
							android.content.ClipData clip = android.content.ClipData
									.newPlainText("Copied FEN", getFEN_CurrentGame());
							clipboard.setPrimaryClip(clip);
						}

						finish();
					}
				};
			}
		});

		result.add(new Config_MenuMain_Base() {

			@Override
			public int getName() {
				return R.string.fen_paste;
			}

			@Override
			public int getIconResID() {
				return R.drawable.ic_paste_white;
			}

			@Override
			public int getID() {
				return CFG_MENU_PASTE_FEN;
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

						String fen = null;
						ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
						if (clipboard != null/* && clipboard.getPrimaryClipDescription().hasMimeType("text/plain")*/) {
							if (clipboard.getPrimaryClip() != null) {
								ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
								if (item != null) {
									if (item.getText() != null) {
										fen = item.getText().toString().trim();
									}
								}
							}
						}

						System.out.println("PASTED FEN " + fen);

						if (fen != null) {

							try {
								String validationMessage = BoardUtils.validateBoard(fen, 1);

								System.out.println("validationMessage is " + validationMessage);

								if (validationMessage != null) {

									MessageUtils.showOkDialog("FEN is not valid.", Activity_MenuMain.this);

								} else {

									UserSettings userSettings = ((UserSettings) Application_Base.getInstance().getUserSettings());
									GameData gameData = (GameData) GameDataUtils.createGameDataForNewGame((GameData) Application_Chess_BaseImpl.getInstance().createGameDataObject(), userSettings.playerTypeWhite, userSettings.playerTypeBlack, userSettings.boardManagerID, userSettings.computerModeID, fen);
									BoardManager_AllRules manager = new BoardManager_AllRules(gameData);
									Application_Base.getInstance().storeGameData(gameData);

									finish();
								}

							} catch (Exception e) {

								MessageUtils.showOkDialog("FEN is not valid.", Activity_MenuMain.this);

								e.printStackTrace();

							}

						} else {

							MessageUtils.showOkDialog("Clipboard is empty.", Activity_MenuMain.this);
						}
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


	private String getFEN_CurrentGame() {

		GameData gamedata = ((GameData) Application_Base.getInstance().getGameData());

		if (gamedata.getBoardManagerID() == IConfigurationRule.BOARD_MANAGER_ID_ALL_RULES) {

			BoardManager_AllRules manager = new BoardManager_AllRules(gamedata);

			String fen = manager.getFEN();

			return fen;

		} else {

			throw new IllegalStateException("boardManagerID=" + gamedata.getBoardManagerID());
		}
	}
}
