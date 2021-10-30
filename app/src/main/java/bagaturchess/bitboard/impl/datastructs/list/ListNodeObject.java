/*
 *  BagaturChess (UCI chess engine and tools)
 *  Copyright (C) 2005 Krasimir I. Topchiyski (k_topchiyski@yahoo.com)
 *  
 *  Open Source project location: http://sourceforge.net/projects/bagaturchess/develop
 *  SVN repository https://bagaturchess.svn.sourceforge.net/svnroot/bagaturchess
 *
 *  This file is part of BagaturChess program.
 * 
 *  BagaturChess is open software: you can redistribute it and/or modify
 *  it under the terms of the Eclipse Public License version 1.0 as published by
 *  the Eclipse Foundation.
 *
 *  BagaturChess is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  Eclipse Public License for more details.
 *
 *  You should have received a copy of the Eclipse Public License version 1.0
 *  along with BagaturChess. If not, see <http://www.eclipse.org/legal/epl-v10.html/>.
 *
 */
package bagaturchess.bitboard.impl.datastructs.list;

public class ListNodeObject<V> {
	
	ListNodeObject<V> prev;
	ListNodeObject<V> next;
	
	long key;
	V value;
	
	public ListNodeObject(long _key, V _value) {
		key = _key;
		value = _value;
	}
	
	public void bypassNeighbours() {
		ListNodeObject<V> prevNode = prev;
		ListNodeObject<V> nextNode = next;
		prev = null;
		next = null;
		
		if (prevNode != null) {
			prevNode.next = nextNode;
		}
		
		if (nextNode != null) {
			nextNode.prev = prevNode;
		}		
	}
	
	public V getValue() {
		return value;
	}
	
	public void setValue(V newValue) {
		value = newValue;
	}
	
	public long getKey() {
		return key;
	}

	public void setKey(long _key) {
		key = _key;
	}
	
	public void clearNeighbours() {
		next = null;
		prev = null;
	}
}
