package org.metatrans.commons.chess.main.views;


import org.metatrans.commons.ui.TextArea;

import android.graphics.RectF;


public class ClockArea extends TextArea {
	
	public ClockArea(RectF _rect, String _text, int _colour_area, int _colour_text) {
		super(_rect, _text, false, _colour_area, _colour_text, false);
	}
	
}
