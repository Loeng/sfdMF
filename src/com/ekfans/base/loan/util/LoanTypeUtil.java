package com.ekfans.base.loan.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class LoanTypeUtil {
	// 融资类型字段明细 类型 - 平台数据
	public final static String LOAN_DETAIL_TYPE_SHOP = "shop";

	// 融资类型字段明细 类型 - 单图
	public final static String LOAN_DETAIL_TYPE_PIC = "pic";

	// 融资类型字段明细 类型 - 单图
	// public final static String LOAN_DETAIL_TYPE_PICS = "pics";

	// 融资类型字段明细 类型 - 单个EXCEL文档
	public final static String LOAN_DETAIL_TYPE_EXCEL = "excel";

	// 融资类型字段明细 类型 - 多个EXCEL文档
	// public final static String LOAN_DETAIL_TYPE_EXCELS = "excels";

	// 融资类型字段明细 类型 - 单个Word文档
	public final static String LOAN_DETAIL_TYPE_WORD = "word";

	// 融资类型字段明细 类型 - 多个Word文档
	// public final static String LOAN_DETAIL_TYPE_WORDS = "words";

	// 融资类型字段明细 类型 - 单个PDF文档
	public final static String LOAN_DETAIL_TYPE_PDF = "pfd";

	// 融资类型字段明细 类型 - 多个PDF文档
	// public final static String LOAN_DETAIL_TYPE_PDFS = "pdfs";

	// 融资类型字段明细 类型 - 文本输入框
	public final static String LOAN_DETAIL_TYPE_INPUT = "input";

	// 融资类型字段明细 类型 - 大文本输入框
	public final static String LOAN_DETAIL_TYPE_TEXTAREA = "textarea";

	// 融资类型字段明细 类型 - 多选框
	public final static String LOAN_DETAIL_TYPE_CHECKBOX = "checkbox";

	// 融资类型字段明细 类型 - 单选框
	public final static String LOAN_DETAIL_TYPE_RADIO = "radio";

	// 融资类型字段明细 类型 - 下拉框
	public final static String LOAN_DETAIL_TYPE_SELECT = "select";

	// 融资类型字段明细 类型 - 日期
	public final static String LOAN_DETAIL_TYPE_DATE = "date";

	// 融资类型字段明细 类型 - 带时间的日期
	public final static String LOAN_DETAIL_TYPE_DATETIME = "datetime";

	// 融资类型字段明细 类型 - 网页编辑器
	public final static String LOAN_DETAIL_TYPE_CKEDITOR = "ckeditor";

	public static Map<String, String> typeNameMap = new LinkedHashMap<String, String>();
	static {
		typeNameMap.put(LOAN_DETAIL_TYPE_SHOP, "平台数据");
		typeNameMap.put(LOAN_DETAIL_TYPE_PIC, "图片(扫描件)");
		// typeNameMap.put(LOAN_DETAIL_TYPE_PICS, "多张图片(多个扫描件)");
		typeNameMap.put(LOAN_DETAIL_TYPE_EXCEL, "EXCEL文档");
		// typeNameMap.put(LOAN_DETAIL_TYPE_EXCELS, "多个EXCEL文档");
		typeNameMap.put(LOAN_DETAIL_TYPE_WORD, "WORD文档");
		// typeNameMap.put(LOAN_DETAIL_TYPE_WORDS, "多个WORD文档");
		typeNameMap.put(LOAN_DETAIL_TYPE_PDF, "PDF文档");
		// typeNameMap.put(LOAN_DETAIL_TYPE_PDFS, "多个PDF文档");
		typeNameMap.put(LOAN_DETAIL_TYPE_INPUT, "文本输入框");
		typeNameMap.put(LOAN_DETAIL_TYPE_TEXTAREA, "大文本输入框");
		typeNameMap.put(LOAN_DETAIL_TYPE_CHECKBOX, "多选框");
		typeNameMap.put(LOAN_DETAIL_TYPE_RADIO, "单选框");
		typeNameMap.put(LOAN_DETAIL_TYPE_SELECT, "下拉框");
		typeNameMap.put(LOAN_DETAIL_TYPE_DATE, "日期选择器");
		typeNameMap.put(LOAN_DETAIL_TYPE_DATETIME, "带时间的日期选择器");
		typeNameMap.put(LOAN_DETAIL_TYPE_CKEDITOR, "网页编辑器");
	}

	// 融资类型字段编号:订单
	public final static String LOAN_DETAIL_CODE_ORDER = "order";
	// 融资类型字段编号:往来流水
	public final static String LOAN_DETAIL_CODE_WL = "wl";
	// 融资类型字段编号:历史业务数据
	public final static String LOAN_DETAIL_CODE_YW = "yw";
	// 融资类型字段编号:营业执照
	public final static String LOAN_DETAIL_CODE_YYZZ = "yyzz";
	// 融资类型字段编号:业务合同
	public final static String LOAN_DETAIL_CODE_CONTRACT = "contract";
	// 融资类型字段编号:业务发票
	public final static String LOAN_DETAIL_CODE_RECEIPT = "receipt";
	// 融资类型字段编号:近三年的财务报表
	public final static String LOAN_DETAIL_CODE_CWBB_YEAR = "yearCwbb";
	// 融资类型字段编号:近三个月的财务报表
	public final static String LOAN_DETAIL_CODE_CWBB_MONTH = "monthCwbb";
	// 融资类型字段编号:公司章程
	public final static String LOAN_DETAIL_CODE_ZC = "zc";
	// 融资类型字段编号:机构信用代码证
	public final static String LOAN_DETAIL_CODE_DMZ = "dmz";
	// 融资类型字段编号:公司简介
	public final static String LOAN_DETAIL_CODE_JJ = "jj";
	// 跟下游企业之间的往来发票扫描件
	public final static String LOAN_DETAIL_CODE_WLFP = "wlfp";

	public static Map<String, String> typeCodeMap = new HashMap<String, String>();
	static {
		typeCodeMap.put(LOAN_DETAIL_CODE_ORDER, "业务订单");
		typeCodeMap.put(LOAN_DETAIL_CODE_WL, "往来流水");
		typeCodeMap.put(LOAN_DETAIL_CODE_YW, "历史业务数据");
		typeCodeMap.put(LOAN_DETAIL_CODE_YYZZ, "营业执照");
		typeCodeMap.put(LOAN_DETAIL_CODE_CONTRACT, "业务合同");
		typeCodeMap.put(LOAN_DETAIL_CODE_RECEIPT, "业务发票");
		typeCodeMap.put(LOAN_DETAIL_CODE_CWBB_YEAR, "近三年经审计的财务报表");
		typeCodeMap.put(LOAN_DETAIL_CODE_CWBB_MONTH, "近三个月财务报表");
		typeCodeMap.put(LOAN_DETAIL_CODE_ZC, "公司章程");
		typeCodeMap.put(LOAN_DETAIL_CODE_DMZ, "机构信用代码证");
		typeCodeMap.put(LOAN_DETAIL_CODE_JJ, "公司简介");
		typeCodeMap.put(LOAN_DETAIL_CODE_WLFP, "跟下游企业之间的往来发票扫描件");
	}

	public static Map<String, String> typeCodeNameMap = new HashMap<String, String>();
	static {
		typeCodeNameMap.put(LOAN_DETAIL_CODE_ORDER, LOAN_DETAIL_TYPE_PDF);
		typeCodeNameMap.put(LOAN_DETAIL_CODE_WL, LOAN_DETAIL_TYPE_PDF);
		typeCodeNameMap.put(LOAN_DETAIL_CODE_YW, LOAN_DETAIL_TYPE_SHOP);
		typeCodeNameMap.put(LOAN_DETAIL_CODE_YYZZ, LOAN_DETAIL_TYPE_PIC);
		typeCodeNameMap.put(LOAN_DETAIL_CODE_CONTRACT, LOAN_DETAIL_TYPE_PDF);
		typeCodeNameMap.put(LOAN_DETAIL_CODE_RECEIPT, LOAN_DETAIL_TYPE_PDF);
		typeCodeNameMap.put(LOAN_DETAIL_CODE_CWBB_YEAR, LOAN_DETAIL_TYPE_PDF);
		typeCodeNameMap.put(LOAN_DETAIL_CODE_CWBB_MONTH, LOAN_DETAIL_TYPE_EXCEL);
		typeCodeNameMap.put(LOAN_DETAIL_CODE_ZC, LOAN_DETAIL_TYPE_PDF);
		typeCodeNameMap.put(LOAN_DETAIL_CODE_DMZ, LOAN_DETAIL_TYPE_PIC);
		typeCodeNameMap.put(LOAN_DETAIL_CODE_JJ, LOAN_DETAIL_TYPE_WORD);
		typeCodeNameMap.put(LOAN_DETAIL_CODE_WLFP, LOAN_DETAIL_TYPE_PDF);
	}

}
