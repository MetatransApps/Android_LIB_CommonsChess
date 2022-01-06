package org.metatrans.commons.chess.edit;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;


public class BoardView extends org.metatrans.commons.chess.views_and_controllers.BoardView {
	
	
	public BoardView(Context context, View _parent, RectF _rectangle) {
		super(context, _parent, _rectangle);
		
	}
	
	
	@Override
	protected IAnimationHandler createAnimationHandler() {
		return new IAnimationHandler() {
			
			@Override
			public void animationIsDone(MoveAnimation animation) {
				//Do nothing
			}
		};
	}
	
	
	@Override
	protected boolean showReplayAndLeaderboards() {
		return false;
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		super.onDraw(canvas);
		
		invalidate();
	}
}
