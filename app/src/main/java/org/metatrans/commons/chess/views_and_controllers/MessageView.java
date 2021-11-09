package org.metatrans.commons.chess.views_and_controllers;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import org.metatrans.commons.chess.main.MainActivity;


public class MessageView extends View {
	
	
	private boolean initialized = false;
	private RectF rectf_main;
	
	
	public MessageView(Context context, AttributeSet attrs) {
		
		super(context, attrs);
		
		rectf_main 			= new RectF();
		
	}
	
	
	protected MainActivity getMainActivity() {
		return (MainActivity) getContext();
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		if (!initialized) {
			initializeDimensions();
		}
		
		this.setMeasuredDimension( (int) (rectf_main.right - rectf_main.left), (int) (rectf_main.bottom - rectf_main.top) );
		
		/*int main_width = getMeasuredWidth();
		int main_height = getMeasuredHeight();
		System.out.println("main_width = " + main_width + ", main_height=" + main_height);
		
		this.setMeasuredDimension(main_width, 100);*/
		
	}
	
	
	private void initializeDimensions() {

		IMainView main = getMainActivity().getMainView();
		//System.out.println("Main view = " + main);
		
		if (main != null) {
			
			int squareSize = (int) main.getBoardView().getSquareSize();
			
			int main_width = getMeasuredWidth();
			int main_height = getMeasuredHeight();
			
			//System.out.println("message_width = " + main_width + ", message_height=" + main_height);
			
			rectf_main.left = 0;
			rectf_main.top = 0;
			rectf_main.right = rectf_main.left + main_width;
			rectf_main.bottom = rectf_main.top + squareSize;
			
			//System.out.println("rectf_main = " + rectf_main);
			
			initialized = true;
		}
	}
	
	
	private Paint paint = new Paint();
	
	
	/*@Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        
		int[] topleft = new int[2];
		getLocationOnScreen(topleft);
		System.out.println("x = " + topleft[0] + ", y = " + topleft[1]);
		
        //rectf_main = new RectF(topleft[0], topleft[1], topleft[0] + getWidth(), topleft[1] + getHeight());
        //rectf_main = new RectF(topleft[0], topleft[1], topleft[0] + getWidth(), 1000);
		//RectF f = new RectF(topleft[0], 0, topleft[0] + getWidth(), 1000);
		//RectF f = new RectF(0, topleft[1], 1000, topleft[1] + getHeight());
		RectF f = new RectF(0, 0, 100, 100);
        
        System.out.println("rectf_main = " + rectf_main);
    }*/
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		super.onDraw(canvas);

		if (true) throw new IllegalStateException();

		/*int colour_panel = getMainActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_Black();
		paint.setColor(colour_panel);
		
		RectF f = new RectF(rectf_main.left + 3, rectf_main.top + 3, rectf_main.right, rectf_main.bottom + 2);
		DrawingUtils.drawRoundRectangle(canvas, paint, f);*/
	}
}

