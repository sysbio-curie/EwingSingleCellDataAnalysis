package vdaoengine.utils;

/**
 * Integer vector class to handle object wrapping
 * so we dont have to think about it
 *
 */

import java.util.*;

public class IntegerVector extends Vector {

	public void setInt(int i, int n) {
		setElementAt(new Integer(i), n);
	}
	public void appendInt(int i) {
		addElement(new Integer(i));
	}
	public int getInt(int n) {
		return ((Integer)elementAt(n)).intValue();
	}
}