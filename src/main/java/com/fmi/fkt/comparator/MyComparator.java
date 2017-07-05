package com.fmi.fkt.comparator;

import java.util.Comparator;
import java.util.Map;

public class MyComparator implements Comparator<Object> {

	Map<Integer, Double> map;

	public MyComparator(Map<Integer, Double> map) {
		this.map = map;
	}

	public int compare(Object o1, Object o2) {

		if (map.get(o2) == map.get(o1))
			return 1;
		else
			return ((Double) map.get(o2)).compareTo((Double) map.get(o1));

	}
}