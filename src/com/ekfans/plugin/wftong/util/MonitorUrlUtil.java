package com.ekfans.plugin.wftong.util;

import com.ekfans.plugin.cache.base.Cache;

import java.net.URL;

/**
 * 车辆监控地址接口
 */
public class MonitorUrlUtil {
    /**
     * 车辆监控地址
     */
    private static final String MONITOR_URL = "/sources/jumpmonitors";
    private static final String ROOT_URL = Cache.getResource("monitor.url.prefx");
    private static final String ORG_ID = Cache.getResource("orgId");

    /**
     * 获得车辆监控
     *
     * @param carId
     * @return
     */
    public static String getUrl(MonitorViewTypeEnum typeEnum, String carId) {
        return ROOT_URL + MONITOR_URL + "/" + typeEnum.getViewType() + "/" + carId + "/" + ORG_ID;
    }

    /**
     * 查看车辆地图方式（order订单，store企业，car车辆）
     */
    public enum MonitorViewTypeEnum {
        VIEW_ORDER("order"), VIEW_STORE("store"), VIEW_CAR("car");
        private final String viewType;

        MonitorViewTypeEnum(String viewType) {
            this.viewType = viewType;
        }

        public static MonitorViewTypeEnum parse(String viewType) {
            MonitorViewTypeEnum[] enumList = MonitorViewTypeEnum.values();
            for (MonitorViewTypeEnum enumInstance : enumList) {
                if (enumInstance.getViewType().equalsIgnoreCase(viewType)) {
                    return (MonitorViewTypeEnum) enumInstance;
                }
            }
            return null;
        }

        public String getViewType() {
            return viewType;
        }
    }

}
