package com.longqin.business.util;

public class MyStringUtil {

	// 判断是否是数字
	public static boolean isNumeric(String str) {
	    return str.matches("-?\\d+(\\.\\d+)?");
	}
}
