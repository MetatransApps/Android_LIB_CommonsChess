package org.metatrans.commons.chess;


import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import org.metatrans.commons.Alerts_Base;
import org.metatrans.commons.chess.R;
import org.metatrans.commons.chess.utils.CachesBitmap;
import org.metatrans.commons.ui.utils.BitmapUtils;


public class Alerts extends Alerts_Base {
	
	
	public static AlertDialog createAlertDialog_EULA(Context context, int icon_size) {
		
		AlertDialog.Builder adb = new AlertDialog.Builder(context);
		
		Drawable drawable = BitmapUtils.createDrawable(context, CachesBitmap.getSingletonIcons(icon_size).getBitmap(context, R.drawable.ic_logo_cafk));
		adb.setIcon(drawable);
		adb.setTitle(R.string.alert_title);
		adb.setMessage(R.string.alert_message_eula);
		
		adb.setNeutralButton(R.string.eula_open, null);
		adb.setPositiveButton(R.string.agree, null);
		adb.setNegativeButton(R.string.disagree, null);
		
		adb.setCancelable(false);
		
		return adb.create();
	}
	
	
	public static AlertDialog createAlertDialog_CongratsForAchievements(Context context, int icon_size) {
		
		AlertDialog.Builder adb = new AlertDialog.Builder(context);
		
		Drawable drawable = BitmapUtils.createDrawable(context, CachesBitmap.getSingletonIcons(icon_size).getBitmap(context, R.drawable.ic_achievements));
		adb.setIcon(drawable);
		
		adb.setTitle(R.string.achievements_alert_title);
		adb.setMessage(R.string.achievements_alert_message);
		
		adb.setNeutralButton(R.string.achievements_button_close, null);
		adb.setPositiveButton(R.string.achievements_button_showme, null);
		
		adb.setCancelable(false);
		
		return adb.create();
	}
}
