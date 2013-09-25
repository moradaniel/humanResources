package org.dpi.util;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtils {

	/**
	 * chops a list into non-view sublists of length L
	 * @param list
	 * @param L
	 * @return
	 */
	public static <T> List<List<T>> chopped(List<T> list, final int L) {
	    List<List<T>> parts = new ArrayList<List<T>>();
	    final int N = list.size();
	    for (int i = 0; i < N; i += L) {
	        parts.add(new ArrayList<T>(
	            list.subList(i, Math.min(N, i + L)))
	        );
	    }
	    return parts;
	}


}
