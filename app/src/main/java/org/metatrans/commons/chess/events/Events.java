package org.metatrans.commons.chess.events;


import android.app.Activity;
import android.content.Context;

import java.util.List;

import org.metatrans.commons.chess.model.GameData;
import org.metatrans.commons.chess.model.UserSettings;
import org.metatrans.commons.events.Event_Base;
import org.metatrans.commons.events.EventsData_Base;
import org.metatrans.commons.events.api.IEvent_Base;
import org.metatrans.commons.events.api.IEventsManager;


public class Events {
	
	
	private static IEventsManager eventsmanager;
	
	
	public static void init(IEventsManager _eventsmanager) {
		
		eventsmanager = _eventsmanager;

	}
	
	public static long getLastGameChange(Context context) {
		return eventsmanager.getLastGameChange(context);
	}
	
	
	public static EventsData_Base getEventsData(Context context) {
		return eventsmanager.getEventsData(context);
	}
	

	public static long getLastMainScreenInteraction(Context context) {
		return eventsmanager.getLastMainScreenInteraction(context);
	}
	
	
	public static IEvent_Base create(int id, String name) {
		return eventsmanager.create(id, name);
	}
	
	
	public static IEvent_Base create(int id, int subid, String name, String subname) {
		return eventsmanager.create(id, subid, name, subname);
	}
	

	public static IEvent_Base create(int id, int subid, String name, String subname, long value) {
		return create(id, subid, subid, name, subname, subname, value);
	}
	

	public static IEvent_Base create(int id, int subid, int subsubid, String name, String subname, String subsubname) {
		return create(id, subid, subsubid, name, subname, subsubname, 0);
	}
	
	
	public static IEvent_Base create(int id, int subid, int subsubid, String name, String subname, String subsubname, long value) {
		return new Event_Base(id, subid, subsubid, name, subname, subsubname, value);
	}
	

	public static void register(Context context, List<IEvent_Base> events) {
		eventsmanager.register(context, events);
	}
	
	
	public static void register(Context context, final IEvent_Base event) {
		eventsmanager.register(context, event);
	}
	
	
	public static void handleGameEvents_OnStart(Activity activity, GameData data) {
		
		eventsmanager.handleGameEvents_OnStart(activity, data);
	}
	
	
	public static void handleGameEvents_OnExit(Activity activity, GameData data, UserSettings settings) {
		eventsmanager.handleGameEvents_OnExit(activity, data, settings);
	}


	public static void handleGameEvents_OnFinish(Activity activity, GameData data, UserSettings settings, int gameStatus) {
		eventsmanager.handleGameEvents_OnFinish(activity, data, settings, gameStatus);
	}
	
	
	public static void updateLastMainScreenInteraction(Context context, long timestamp) {
		
		eventsmanager.updateLastMainScreenInteraction(context, timestamp);
	}
}
