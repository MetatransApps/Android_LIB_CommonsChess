package org.metatrans.commons.chess.main.views;


import android.content.Context;
import android.view.View;


public class BaseView extends View {
	
	
	private View parent;
	
	private boolean locked;
	
	
	public BaseView(Context context, View _parent) {
		
		super(context);
		
		parent = _parent;
	}
	
	
	protected IBoardViewActivity getActivity() {
		return (IBoardViewActivity) getContext();
	}
	
	
	public void redraw() {
		invalidate();
		parent.invalidate();
	}
	
	
	@Override
	public void invalidate() {
		super.invalidate();
		parent.invalidate();
	}
	
	//@Override
	public void lock() {
		locked = true;
	}


	//@Override
	public void unlock() {
		locked = false;
	}


	//@Override
	public boolean isLocked() {
		return locked;
	}
}
