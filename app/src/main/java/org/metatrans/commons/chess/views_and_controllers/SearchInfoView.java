package org.metatrans.commons.chess.views_and_controllers;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import org.metatrans.commons.cfg.colours.IConfigurationColours;
import org.metatrans.commons.chess.R;
import org.metatrans.commons.chess.logic.BoardConstants;
import org.metatrans.commons.chess.logic.computer.ComputerPlayer_Engine;
import org.metatrans.commons.chess.logic.computer.IComputer;
import org.metatrans.commons.chess.model.SearchInfo;
import org.metatrans.commons.chess.model.UserSettings;
import org.metatrans.commons.ui.ButtonAreaSwitch_Image;
import org.metatrans.commons.ui.TextArea;
import org.metatrans.commons.ui.utils.BitmapUtils;
import org.metatrans.commons.ui.utils.DrawingUtils;


public abstract class SearchInfoView extends BaseView implements IPanelsVisualization {


	protected static int DELTA_AREAS = 9;

	protected int colour_panel;

	private RectF rectangleSearchPanel;

	protected Paint default_paint;

	protected RectF rectangle_area_info;


	private RectF rectangle_area_bottom2left;
	private RectF rectangle_area_bottom2right1;
	private RectF rectangle_area_bottom2right2;

	private TextArea textarea_bottom2left;
	private TextArea textarea_bottom2right1;
	private TextArea textarea_bottom2right2;

	protected ButtonAreaSwitch_Image textarea_info;


	public SearchInfoView(Context context, View _parent, RectF _rectangleSearchPanel) {
		
		super(context, _parent);

		rectangleSearchPanel = _rectangleSearchPanel;

		default_paint = new Paint();

		rectangle_area_info = new RectF();

		rectangle_area_bottom2left = new RectF();
		rectangle_area_bottom2right1 = new RectF();
		rectangle_area_bottom2right2 = new RectF();

		colour_panel = getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_Black();
	}
	
	
	public void init() {

		IConfigurationColours coloursCfg = getActivity().getUIConfiguration().getColoursConfiguration();

		int delta = DELTA_AREAS;

		float boardSquareSize = (rectangleSearchPanel.right - rectangleSearchPanel.left) / (float) 8;

		rectangle_area_bottom2left.left = rectangleSearchPanel.left + delta;
		rectangle_area_bottom2left.top = rectangleSearchPanel.top + delta;
		rectangle_area_bottom2left.right = rectangleSearchPanel.left + boardSquareSize + boardSquareSize - delta;
		rectangle_area_bottom2left.bottom = rectangleSearchPanel.bottom - delta;

		textarea_bottom2left = new TextArea(rectangle_area_bottom2left, "Messages", getActivity().getUIConfiguration().getColoursConfiguration().getColour_Delimiter(), getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_Black());

		rectangle_area_bottom2right1.left = 2 * boardSquareSize + delta;
		rectangle_area_bottom2right1.top = rectangleSearchPanel.top /*+ ((rectangle_area_bottom2left.bottom - rectangle_area_bottom2left.top) / 2)*/ + delta;
		rectangle_area_bottom2right1.right = rectangleSearchPanel.right - delta;
		rectangle_area_bottom2right1.bottom = rectangleSearchPanel.top + ((rectangleSearchPanel.bottom - rectangleSearchPanel.top) / 2) - delta;

		textarea_bottom2right1 = new TextArea(rectangle_area_bottom2right1, "Thinking: a2a4", getActivity().getUIConfiguration().getColoursConfiguration().getColour_Delimiter(), getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_Black());

		rectangle_area_bottom2right2.left = 2 * boardSquareSize + delta;
		rectangle_area_bottom2right2.top = rectangleSearchPanel.top + ((rectangleSearchPanel.bottom - rectangleSearchPanel.top) / 2) + delta;
		rectangle_area_bottom2right2.right = rectangleSearchPanel.right - delta;
		rectangle_area_bottom2right2.bottom = rectangleSearchPanel.bottom - delta;


		textarea_bottom2right2 = new TextArea(rectangle_area_bottom2right2, "NPS     : 35", getActivity().getUIConfiguration().getColoursConfiguration().getColour_Delimiter(), getActivity().getUIConfiguration().getColoursConfiguration().getColour_Square_Black());


		initInfoRectangle();

		UserSettings settings = getActivity().getUserSettings();

		textarea_info = new ButtonAreaSwitch_Image(rectangle_area_info,
				BitmapUtils.fromResource((Context) getActivity(), R.drawable.ic_action_wizard_white),
				coloursCfg.getColour_Square_ValidSelection(),
				coloursCfg.getColour_Delimiter(),
				coloursCfg.getColour_Square_MarkingSelection(),
				settings.infoEnabled,
				false);
	}


	protected abstract void initInfoRectangle();


	@Override
	protected void onDraw(Canvas canvas) {

		default_paint.setColor(colour_panel);

		if (rectangleSearchPanel.height() > 0
				&& getActivity().getUserSettings().infoEnabled) {


			DrawingUtils.drawRoundRectangle(canvas, default_paint, rectangleSearchPanel);


			String value_eval = "  ...  ";
			String value_moves = "  ...  ";
			String value_depth = "  ...  ";

			SearchInfo last_info;

			IComputer thinker = getActivity().getBoardManager().getComputerToMove();

			if (thinker.isThinking()) {

				ComputerPlayer_Engine engine = (ComputerPlayer_Engine) thinker;

				last_info = engine.getLastSearchInfo();

			} else {

				last_info = getActivity().getBoardManager().getGameData().getCurrentMoveIndex() == -1 ? null : getActivity().getBoardManager().getGameData().getSearchInfos().get(getActivity().getBoardManager().getGameData().getCurrentMoveIndex());
			}

			if (last_info != null) {

				value_eval = last_info.infoEval;
				value_moves = last_info.infoMoves + "  ";
				value_depth = last_info.infoDepth + ", " + last_info.infoNPS + "  ";
			}

			textarea_bottom2left.setText(value_eval);
			textarea_bottom2left.draw(canvas);

			textarea_bottom2right1.setText(value_moves);
			textarea_bottom2right1.draw(canvas);

			textarea_bottom2right2.setText(value_depth);
			textarea_bottom2right2.draw(canvas);
		}


		textarea_info.draw(canvas);


		invalidate();
	}


	@Override
	public OnTouchListener createOnTouchListener(IBoardVisualization boardVisualization, IBoardViewActivity activity) {

		return new OnTouchListener_Panels(activity);
	}


	public class OnTouchListener_Panels implements OnTouchListener, BoardConstants {


		private IBoardViewActivity activity;


		public OnTouchListener_Panels(IBoardViewActivity _activity) {

			activity = _activity;
		}


		@Override
		public boolean onTouch(View view, MotionEvent event) {

			if (isLocked()) {

				return true;
			}

			synchronized (activity) {

				int action = event.getAction();

				if (action == MotionEvent.ACTION_DOWN) {

					processEvent_DOWN(event);

				} else if (action == MotionEvent.ACTION_MOVE) {

					processEvent_MOVE(event);

				} else if (action == MotionEvent.ACTION_UP
						|| action == MotionEvent.ACTION_CANCEL) {

					processEvent_UP(event);

				}
			}

			return true;
		}


		private void processEvent_DOWN(MotionEvent event) {

			float x = event.getX();
			float y = event.getY();

			if (rectangleSearchPanel.contains(x, y)) {



			}

			redraw();
		}


		private void processEvent_MOVE(MotionEvent event) {

			float x = event.getX();
			float y = event.getY();

			if (rectangleSearchPanel.contains(x, y)) {

			} else {

			}


			redraw();
		}


		private void processEvent_UP(MotionEvent event) {

			float x = event.getX();
			float y = event.getY();

			if (rectangleSearchPanel.contains(x, y)) {

			} else {

			}

			invalidate();

			redraw();
		}
	}
}
