package org.metatrans.commons.chess.menu;


import android.view.ViewGroup;

import org.metatrans.commons.Activity_Base;
import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.chess.utils.CachesBitmap;
import org.metatrans.commons.ui.images.IBitmapCache;

import com.chessartforkids.model.UserSettings;


public abstract class MenuActivity_Base extends Activity_Base {


	private ViewGroup frame;


	@Override
	public void onStart(){
		
		super.onStart();
		
	}
	
	
	@Override
	public void onStop(){

		super.onStop();
	}


	@Override
	protected void onDestroy() {

		frame = null;

		super.onDestroy();

		System.gc();
	}
	
	
	@Override
	protected IBitmapCache getBitmapCache() {
		return CachesBitmap.getSingletonFullSized();
	}

	
	public UserSettings getUserSettings() {
		return (UserSettings) Application_Base.getInstance().getUserSettings();
	}


	protected ViewGroup getFrame() {

		return frame;
	}


	protected void setFrame(ViewGroup frame) {
		this.frame = frame;
	}
}
