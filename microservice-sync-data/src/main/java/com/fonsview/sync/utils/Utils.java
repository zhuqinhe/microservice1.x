package com.fonsview.sync.utils;

import java.util.Set;
import java.util.TreeSet;

import com.fonsview.sync.common.ConfigKey;


public class Utils {

	public static String arrayToString(String[] strArr) {

		if (strArr == null || strArr.length == 0) {
			return "";
		}
		Set<String> set = new TreeSet<>();
		for (String str : strArr) {
			set.add(str);
		}
		StringBuffer sb = new StringBuffer("");
		for (String str : set) {
			sb.append(str).append(",");
		}
		return sb.toString();
	}
}
