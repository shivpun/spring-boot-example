package org.dovetail.util;

import java.util.List;

public abstract class CollectionUtil {
	
	public static <T> T add(List<T> lt, T t) {
		T ts = t;
		if(lt!=null && ts!=null) {
			int index = lt.indexOf(ts);
			if(index<0) {
				lt.add(ts);
			} else {
				ts = lt.get(index);
			}
		}
		return ts;
	}
}
