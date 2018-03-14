package com.ekfans.base.loan.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LoanTypeRoleUtil {
	// 融资类型满足条件所对应的 信用等级
	public final static String LOAN_ROLE_TYPE_CREDIT_LEVEL = "0";

	// 融资类型满足条件所对应的 注册资金
	public final static String LOAN_ROLE_TYPE_REGEDIT_FUND = "1";

	// 融资类型满足条件所对应的 企业总资产
	public final static String LOAN_ROLE_TYPE_TOTAL_PROPERTY = "2";

	// 融资类型满足条件所对应的 融资金额
	public final static String LOAN_ROLE_TYPE_PRICE = "3";

	public static Map<String, String> roleTypeNamesMap = new LinkedHashMap<String, String>();
	static {
		roleTypeNamesMap.put(LOAN_ROLE_TYPE_CREDIT_LEVEL, "信用等级");
		roleTypeNamesMap.put(LOAN_ROLE_TYPE_REGEDIT_FUND, "注册资金");
		roleTypeNamesMap.put(LOAN_ROLE_TYPE_TOTAL_PROPERTY, "总资产");
		roleTypeNamesMap.put(LOAN_ROLE_TYPE_PRICE, "融资金额");
	}

	// 信用等级 C
	public final static String LOAN_ROLE_C = "C";
	// 信用等级 CC
	public final static String LOAN_ROLE_CC = "CC";
	// 信用等级 CCC
	public final static String LOAN_ROLE_CCC = "CCC";
	// 信用等级 B-
	public final static String LOAN_ROLE_B_ = "B-";
	// 信用等级 B
	public final static String LOAN_ROLE_B = "B";
	// 信用等级 B+
	public final static String LOAN_ROLE_B__ = "B+";
	// 信用等级 BB-
	public final static String LOAN_ROLE_BB_ = "BB-";
	// 信用等级 BB
	public final static String LOAN_ROLE_BB = "BB";
	// 信用等级 BB+
	public final static String LOAN_ROLE_BB__ = "BB+";
	// 信用等级 BBB-
	public final static String LOAN_ROLE_BBB_ = "BBB-";
	// 信用等级 BBB
	public final static String LOAN_ROLE_BBB = "BBB";
	// 信用等级 BBB+
	public final static String LOAN_ROLE_BBB__ = "BBB+";
	// 信用等级 A-
	public final static String LOAN_ROLE_A_ = "A-";
	// 信用等级 A
	public final static String LOAN_ROLE_A = "A";
	// 信用等级 A+
	public final static String LOAN_ROLE_A__ = "A+";
	// 信用等级 AA-
	public final static String LOAN_ROLE_AA_ = "AA-";
	// 信用等级 AA
	public final static String LOAN_ROLE_AA = "AA";
	// 信用等级 AA+
	public final static String LOAN_ROLE_AA__ = "AA+";
	// 信用等级 AAA-
	public final static String LOAN_ROLE_AAA_ = "AAA-";
	// 信用等级 AAA
	public final static String LOAN_ROLE_AAA = "AAA";
	// 信用等级 AAA+
	public final static String LOAN_ROLE_AAA__ = "AAA+";

	public static Map<String, Integer> loanRoleCreditMap = new LinkedHashMap<String, Integer>();
	static {
		loanRoleCreditMap.put(LOAN_ROLE_C, 1);
		loanRoleCreditMap.put(LOAN_ROLE_CC, 2);
		loanRoleCreditMap.put(LOAN_ROLE_CCC, 3);
		loanRoleCreditMap.put(LOAN_ROLE_B_, 4);
		loanRoleCreditMap.put(LOAN_ROLE_B, 5);
		loanRoleCreditMap.put(LOAN_ROLE_B__, 6);
		loanRoleCreditMap.put(LOAN_ROLE_BB_, 7);
		loanRoleCreditMap.put(LOAN_ROLE_BB, 8);
		loanRoleCreditMap.put(LOAN_ROLE_BB__, 9);
		loanRoleCreditMap.put(LOAN_ROLE_BBB_, 10);
		loanRoleCreditMap.put(LOAN_ROLE_BBB, 11);
		loanRoleCreditMap.put(LOAN_ROLE_BBB__, 12);
		loanRoleCreditMap.put(LOAN_ROLE_A_, 13);
		loanRoleCreditMap.put(LOAN_ROLE_A, 14);
		loanRoleCreditMap.put(LOAN_ROLE_A__, 15);
		loanRoleCreditMap.put(LOAN_ROLE_AA_, 16);
		loanRoleCreditMap.put(LOAN_ROLE_AA, 17);
		loanRoleCreditMap.put(LOAN_ROLE_AA__, 18);
		loanRoleCreditMap.put(LOAN_ROLE_AAA_, 19);
		loanRoleCreditMap.put(LOAN_ROLE_AAA, 20);
		loanRoleCreditMap.put(LOAN_ROLE_AAA__, 21);
	}
	
	// 满足筛选条件的评级List
	public static List<String> qualifiedCreditList = new ArrayList<String>();
	static {
		qualifiedCreditList.add(LOAN_ROLE_BBB);
		qualifiedCreditList.add(LOAN_ROLE_BBB__);
		qualifiedCreditList.add(LOAN_ROLE_A_);
		qualifiedCreditList.add(LOAN_ROLE_A);
		qualifiedCreditList.add(LOAN_ROLE_A__);
		qualifiedCreditList.add(LOAN_ROLE_AA_);
		qualifiedCreditList.add(LOAN_ROLE_AA);
		qualifiedCreditList.add(LOAN_ROLE_AA__);
		qualifiedCreditList.add(LOAN_ROLE_AAA_);
		qualifiedCreditList.add(LOAN_ROLE_AAA);
		qualifiedCreditList.add(LOAN_ROLE_AAA__);
	}

	// 计算规则 大于
	public final static String LOAN_ROLE_ALG_BIGGER = ">";
	// 计算规则 大于或等于
	public final static String LOAN_ROLE_ALG_BIGEQUGER = ">=";
	// 计算规则 等于
	public final static String LOAN_ROLE_ALG_EQU = "=";
	// 计算规则 小于
	public final static String LOAN_ROLE_ALG_SMALLER = "<";
	// 计算规则 小于或等于
	public final static String LOAN_ROLE_ALG_SMALLEQUER = "<=";

	// 计算规则 加
	public final static String LOAN_ROLE_ALG_ADD = "+";
	// 计算规则 减
	public final static String LOAN_ROLE_ALG_REDU = "-";
	// 计算规则 乘
	public final static String LOAN_ROLE_ALG_CHEN = "*";
	// 计算规则 除
	public final static String LOAN_ROLE_ALG_CHU = "/";

	// 计算规则 并且
	public final static String LOAN_ROLE_ALG_AND = "&&";
	// 计算规则 或者
	public final static String LOAN_ROLE_ALG_OR = "||";

	public static Map<String, String> loanRoleAlgNamesMap = new LinkedHashMap<String, String>();
	static {
		loanRoleAlgNamesMap.put(LOAN_ROLE_ALG_BIGGER, "大于");
		loanRoleAlgNamesMap.put(LOAN_ROLE_ALG_BIGEQUGER, "大于或等于");
		loanRoleAlgNamesMap.put(LOAN_ROLE_ALG_EQU, "等于");
		loanRoleAlgNamesMap.put(LOAN_ROLE_ALG_SMALLER, "小于");
		loanRoleAlgNamesMap.put(LOAN_ROLE_ALG_SMALLEQUER, "小于或等于");

		loanRoleAlgNamesMap.put(LOAN_ROLE_ALG_ADD, "加");
		loanRoleAlgNamesMap.put(LOAN_ROLE_ALG_REDU, "减");
		loanRoleAlgNamesMap.put(LOAN_ROLE_ALG_CHEN, "乘");
		loanRoleAlgNamesMap.put(LOAN_ROLE_ALG_CHU, "除");

		loanRoleAlgNamesMap.put(LOAN_ROLE_ALG_AND, "并且");
		loanRoleAlgNamesMap.put(LOAN_ROLE_ALG_OR, "或者");
	}

	public static void main(String[] args) {
		System.out.println("aa-".toUpperCase());
	}

}
