package com.chessartforkids.model;

import java.io.Serializable;


public class FieldSelection implements Comparable<FieldSelection>, Serializable {
	
	
	private static final long serialVersionUID = -865458605670214969L;
	
	
	public static final int SHAPE_BORDER = 1;
	public static final int SHAPE_SQUARE = 2;
	
	public static final int APPEARANCE_TEMP = 1;
	public static final int APPEARANCE_PERMANENT = 2;
	
	
	public int priority; //bigger is highest
	public int colour;
	public int shape;
	public int appearace;
	
	
	@Override
	public String toString() {
		
		String str = "SELECTION [";
		
		str += colour + ", ";
		str += shape + ", ";
		str += appearace + ", ";
		str += "]";
		
		return str;
	}
	
	@Override
	public int hashCode() {
		return priority + colour + shape + appearace;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FieldSelection) {
			FieldSelection other = (FieldSelection) obj;
			if (other.priority == priority && other.colour == colour && other.appearace == appearace && other.shape == shape) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int compareTo(FieldSelection other) {
		if (this.equals(other)) {
			
		}
		
		int delta = priority - other.priority;
		if (delta == 0) {
			return 1;
		}
		return delta;
	}	
}
