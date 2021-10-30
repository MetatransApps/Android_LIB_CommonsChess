package org.metatrans.commons.chess.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;


public class MessageUtils {

	
	public static void showOkDialog(String message, Activity activity) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage(message)
		       .setCancelable(false)
		       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   //Do nothing
		           }
		       });
		activity.runOnUiThread(new Runnable() {
		    public void run() {
				AlertDialog alert = builder.create();
				alert.show();
		    }
		});
	}
	
}
