package org.metatrans.commons.chess.views_and_controllers;


import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;


public class MainView_WithMovesNavigation extends MainView {


	private RectF rectf_bottompanel1;


	public MainView_WithMovesNavigation(Context context, AttributeSet attrs) {

		super(context, attrs);

		USAGE_PERCENT_PANEL 				= 0.075F;

		rectf_bottompanel1 = new RectF();

		panelsView = createPanelsView(rectf_toppanel, rectf_bottompanel0, rectf_bottompanel1, rectf_bottompanel2);
	}


	@Override
	protected PanelsView createPanelsView(RectF rectf_toppanel, RectF rectf_bottompanel0, RectF rectf_bottompanel1, RectF rectf_bottompanel2) {

		return new PanelsView_WithMovesNavigation(getContext(), this,
				rectf_toppanel,
				rectf_bottompanel0,
				rectf_bottompanel1,
				rectf_bottompanel2
		);
	}


	@Override
	protected void initInfoAndCustomPanel(float board_dimension, float left) {

		rectf_bottompanel1.left = rectf_main.left;
		rectf_bottompanel1.right = board_dimension;
		rectf_bottompanel1.top = rectf_bottompanel0.bottom + panel_border;
		rectf_bottompanel1.bottom = rectf_bottompanel1.top + panel_height;

		rectf_bottompanel2.left = left;
		rectf_bottompanel2.right = board_dimension;
		rectf_bottompanel2.top = rectf_bottompanel1.bottom + panel_border;
		rectf_bottompanel2.bottom = rectf_bottompanel2.top + panel_height;
	}


	@Override
	protected void adjustPanelHeight() {

		//Do nothing
	}
}
