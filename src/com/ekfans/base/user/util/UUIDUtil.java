package com.ekfans.base.user.util;

import java.util.UUID;

import com.ekfans.pub.util.StringUtil;

public class UUIDUtil {
	/**
	 * 始终返回一个32位长的id；
	 * mark充当头部，如当mark为WU则返回的是WU667beb0c47470ab5f0e05d1fefbc6d
	 * @param mark
	 * @return
	 */
	public static String getMarkedUUID32(String mark) {
		if (StringUtil.isEmpty(mark) || mark.length() > 20) {
			return UUID.randomUUID().toString();
		}
		mark = mark.toUpperCase();
		int markLength = mark.length();
		String returnId = UUID.randomUUID().toString().replaceAll("-", "");
		returnId = mark + returnId.substring(markLength,32);
		return returnId;
	}
}
