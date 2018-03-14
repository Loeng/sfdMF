package com.ekfans.base.ccwcc.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by liuguoyu on 2017/4/19.
 */
public class CcwccPushConst {
    //(0:普通推送，1:资讯推送，2:供应推送，3:求购推送)
    public static final String PUSH_TYPE_COMMON = "0";
    public static final String PUSH_TYPE_CONTENT = "1";
    public static final String PUSH_TYPE_SUPPLY_SALE = "2";
    public static final String PUSH_TYPE_SUPPLY_BUY = "3";

    public static Map<String, String> pushTypeMap = new LinkedHashMap<>();

    static {
        pushTypeMap.put(PUSH_TYPE_COMMON, "普通推送");
        pushTypeMap.put(PUSH_TYPE_CONTENT, "资讯推送");
        pushTypeMap.put(PUSH_TYPE_SUPPLY_SALE, "供应推送");
        pushTypeMap.put(PUSH_TYPE_SUPPLY_BUY, "求购推送");
    }

    //(0:取消，1:待推送，2:已推送)
    public static final String PUSH_STATUS_CANCEL = "0";
    public static final String PUSH_STATUS_WAIT = "1";
    public static final String PUSH_STATUS_PUSH = "2";

    public static Map<String, String> pushStatusMap = new LinkedHashMap<>();

    static {
        pushStatusMap.put(PUSH_STATUS_CANCEL, "取消");
        pushStatusMap.put(PUSH_STATUS_WAIT, "待推送");
        pushStatusMap.put(PUSH_STATUS_PUSH, "已推送");
    }
}
