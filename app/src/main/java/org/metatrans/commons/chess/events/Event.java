package org.metatrans.commons.chess.events;


public class Event implements IEvent {
	
	
	private static final long serialVersionUID = 7765202390359612844L;
	
	
	private int id;
	private int subid;
	private int subsubid;
	
	private String name;
	private String subname;
	private String subsubname;
	
	private long value;
	
	
	/*public Event(int _id, String _name) {
		this(_id, _id, _name, _name);
	}*/

	
	/*public Event(int _id, int _subid, String _name, String _subname) {
		this(_id, _subid, _name, _subname, 0);
	}*/
	
	
	public Event(int _id, int _subid, int _subsubid, String _name, String _subname, String _subsubname, long _value) {
		
		id = _id;
		subid = _subid;
		subsubid = _subsubid;
		
		name = _name;
		subname = _subname;
		subsubname = _subsubname;
		
		value = _value;
	}
	
	
	@Override
	public int getID() {
		return id;
	}
	
	
	@Override
	public int getSubID() {
		return subid;
	}
	

	@Override
	public int getSubSubID() {
		return subsubid;
	}
	
	
	@Override
	public String getName() {
		return name;
	}

	
	@Override
	public String getSubName() {
		return subname;
	}
	

	@Override
	public String getSubSubName() {
		return subsubname;
	}

	
	@Override
	public long getValue() {
		return value;
	}
	
	
	@Override
	public String toString() {
		return "id=" + id + ", subid=" + subid + ", subsubid=" + subsubid + ", name=" + name + ", subname=" + subname + ", subsubname=" + subsubname + ", value=" + value;
	}
}
