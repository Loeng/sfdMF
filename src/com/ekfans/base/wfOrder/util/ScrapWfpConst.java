package com.ekfans.base.wfOrder.util;

import java.util.HashMap;
import java.util.Map;

public class ScrapWfpConst {

	// 危险特性 0毒性
	public static final String WFP_CHARACT_DU = "0";
	// 危险特性 1易燃性
	public static final String WFP_CHARACT_YR = "1";
	// 危险特性 2爆炸性
	public static final String WFP_CHARACT_BZ = "2";
	// 危险特性 3腐蚀性
	public static final String WFP_CHARACT_FS = "3";
	// 危险特性 4传染性
	public static final String WFP_CHARACT_CR = "4";
	// 危险特性 5其他
	public static final String WFP_CHARACT_OTHER = "5";

	public static Map<String, String> characterMap = new HashMap<String, String>();
	static {
		characterMap.put(WFP_CHARACT_DU, "毒性");
		characterMap.put(WFP_CHARACT_YR, "易燃性");
		characterMap.put(WFP_CHARACT_BZ, "爆炸性");
		characterMap.put(WFP_CHARACT_FS, "腐蚀性");
		characterMap.put(WFP_CHARACT_CR, "传染性");
		characterMap.put(WFP_CHARACT_OTHER, "其他");
	}

	// 形态 0固态
	public static final String WFP_SHAPE_GU = "0";
	// 形态 1液态
	public static final String WFP_SHAPE_YE = "1";
	// 形态 2半固态
	public static final String WFP_SHAPE_BG = "2";
	// 形态 3其他
	public static final String WFP_SHAPE_OTHER = "3";

	public static Map<String, String> shapeMap = new HashMap<String, String>();
	static {
		shapeMap.put(WFP_SHAPE_GU, "固态");
		shapeMap.put(WFP_SHAPE_YE, "液态");
		shapeMap.put(WFP_SHAPE_BG, "半固态");
		shapeMap.put(WFP_SHAPE_OTHER, "其他");
	}

	// 废物处置方式 0利用
	public static final String WFP_HANDLE_USE = "0";
	// 废物处置方式 1贮存
	public static final String WFP_HANDLE_ZC = "1";
	// 废物处置方式 2焚烧
	public static final String WFP_HANDLE_FS = "2";
	// 废物处置方式 3安全填埋
	public static final String WFP_HANDLE_TM = "3";
	public static Map<String, String> handleMap = new HashMap<String, String>();
	static {
		handleMap.put(WFP_HANDLE_USE, "利用");
		handleMap.put(WFP_HANDLE_ZC, "贮存");
		handleMap.put(WFP_HANDLE_FS, "焚烧");
		handleMap.put(WFP_HANDLE_TM, "安全填埋");
	}
}
