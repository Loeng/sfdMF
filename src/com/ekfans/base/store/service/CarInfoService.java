package com.ekfans.base.store.service;

import com.ekfans.base.store.dao.ICarInfoDao;
import com.ekfans.base.store.model.CarInfo;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.controllers.tools.util.FileUploadHelper;
import com.ekfans.plugin.webService.monitor.MonitorDataConvert;
import com.ekfans.plugin.webService.monitor.MonitorSyncMain;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 运输企业车辆信息Service接口实现
 *
 * @author ek_lq
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 * @ClassName: CreditEstimatesService
 * @Description:
 * @date 2014-9-20 上午11:55:14
 */
@Service
public class CarInfoService implements ICarInfoService {

    private Logger log = LoggerFactory.getLogger(CarInfoService.class);
    @Resource
    private ICarInfoDao carInfoDao;

    /**
     * 新增或编辑车辆信息
     *
     * @param info
     * @param request
     * @param response
     * @return
     */
    public boolean saveOrUpdate(CarInfo info, HttpServletRequest request, HttpServletResponse response) {
        Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
        if (info == null || store == null) {
            return false;
        }
        boolean flag = false;

        try {
            // 设置图标保存路径
            String currentPath = "/customerfiles/store/" + store.getId() + "/" + DateUtil.getNoSpSysDateString();
            // 调用方法获取分类图标，返回图标路径
            String xszFile = FileUploadHelper.uploadFile("xszFile", currentPath, request, response);
            String yszFile = FileUploadHelper.uploadFile("yszFile", currentPath, request, response);
            info.setXszFile(xszFile);
            info.setYszFile(yszFile);

            if (StringUtil.isEmpty(info.getCreateTime())) {
                info.setCreateTime(DateUtil.getSysDateString());
            }
            info.setUpdateTime(DateUtil.getSysDateString());
            info.setCheckStatus("0");
            info.setStoreId(store.getId());
            carInfoDao.saveOrUpdateBean(info);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public String addCarInfo(CarInfo carInfo) {
        try {
            carInfo.setCreateTime(DateUtil.getSysDateString());
            carInfoDao.addBean(carInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return carInfo.getId();
    }

    /**
     * 批量删除车辆信息
     *
     * @param carInfoIds
     * @return
     */
    public boolean delete(String[] carInfoIds, HttpServletRequest request) {
        // 如果传过来的数组为空或者长度小于等于0，则返回失败
        if (carInfoIds == null || carInfoIds.length <= 0) {
            return false;
        }
        try {
            // 调用DAO方法批量删除
            carInfoDao.deleteByIds(carInfoIds);

            // 数组转换字符串
            StringBuilder ids = new StringBuilder();
            for (String str : carInfoIds) {
                ids.append(str + ",");
            }
            // 删除成功将车辆信息同步到监控平台
            new MonitorSyncMain(MonitorDataConvert.initDelCommon(ids.toString()), "102").start();

            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * 根据车辆信息ID删除车辆信息
     *
     * @param carInfoId
     * @return
     */
    public boolean deleteById(String carInfoId, HttpServletRequest request) {
        // 如果传过来的ID为空，则返回失败
        if (StringUtil.isEmpty(carInfoId)) {
            return false;
        }
        try {
            // 调用DAO删除对象
            carInfoDao.deleteById(carInfoId);

            // 删除成功将车辆信息同步到监控平台
            new MonitorSyncMain(MonitorDataConvert.initDelCommon(carInfoId), "102").start();

            // 返回删除成功状态
            return true;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return false;
    }

    /**
     * 直接更新车辆信息表
     *
     * @param carInfo
     * @return
     */
    public boolean updateBean(CarInfo carInfo, HttpServletRequest request) {
        try {
            if (carInfo != null) {
                carInfo.setUpdateTime(DateUtil.getSysDateString());
                carInfoDao.updateBean(carInfo);
                return true;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * 根据车辆信息ID获取车辆信息对象
     *
     * @param infoId
     * @return
     */
    public CarInfo getCarInfoById(String infoId) {
        try {
            if (!StringUtil.isEmpty(infoId)) {
                return (CarInfo) carInfoDao.get(infoId);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 根据搜索条件查询车辆信息集合
     *
     * @param carNo
     * @param checkStatus
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<CarInfo> getCarInfosByStoreId(Pager page, String carNo, String checkStatus, HttpServletRequest request, HttpServletResponse response) {
        Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
        if (store == null) {
            return null;
        }
        StringBuffer hql = new StringBuffer(" from CarInfo c where c.storeId=:storeId ");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("storeId", store.getId());
        if (!StringUtil.isEmpty(carNo)) {
            hql.append(" and c.carNo like :carNo");
            map.put("carNo", "%" + carNo + "%");
        }
        if ("0".equals(checkStatus)) {
            hql.append(" and c.checkStatus='0'");
        } else if ("1".equals(checkStatus)) {
            hql.append(" and c.checkStatus='1'");
        }
        hql.append(" order by c.createTime desc");
        try {
            if (page != null) {
                return carInfoDao.list(page, hql.toString(), map);
            } else {
                return carInfoDao.list(hql.toString(), map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<CarInfo> getCheckCarsByStoreId(String storeId) {
        StringBuffer hql = new StringBuffer(" from CarInfo c where c.storeId=:storeId ");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("storeId", storeId);
        hql.append(" and c.checkStatus='1'");
        hql.append(" order by c.createTime desc");
        try {
            return carInfoDao.list(hql.toString(), map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<CarInfo> getAllCarInfo() {
        StringBuffer hql = new StringBuffer(" from CarInfo c");
        try {
            List<CarInfo> list = carInfoDao.list(hql.toString(), null);
            if (list.size() > 0 && list != null) {
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // -----------------以下是后台-----------------------
    @Override
    public List<CarInfo> getCarInfoSystem(Pager page, String storeName, HttpServletRequest request, HttpServletResponse response) {
        StringBuffer hql = new StringBuffer("select c,s.storeName from CarInfo c,Store s where c.storeId=s.id and c.checkStatus='0'");
        Map<String, Object> map = new HashMap<String, Object>();
        if (!StringUtil.isEmpty(storeName)) {
            hql.append(" and s.storeName like :storeName");
            map.put("storeName", "%" + storeName + "%");
        }
        hql.append(" order by c.updateTime desc");
        try {
            List<Object[]> list = carInfoDao.list(hql.toString(), map);
            List<CarInfo> carInfos = new ArrayList<CarInfo>();
            for (Object[] object : list) {
                CarInfo carInfo = (CarInfo) object[0];
                carInfo.setStoreName((String) object[1]);
                carInfos.add(carInfo);
            }
            return carInfos;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings({"unused", "unchecked"})
    @Override
    public List<Map<String, Object>> getCarInfoByInterface(boolean status) {
        // TODO Auto-generated method stub
        String hql = "select c.carType,c.storeId,c.id from CarInfo as c,Store as s" + " where 1=1" + " and c.storeId = s.id" + " and c.checkStatus = 1" + " and c.useData = 0";
        Map<String, Object> rMap = new HashMap<String, Object>();
        List<Map<String, Object>> ListMap = new ArrayList<Map<String, Object>>();
        try {
            List<Object[]> list = carInfoDao.list(hql, rMap);
            if (list != null && list.size() >= 0) {
                for (Object[] objects : list) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    // 回调成功修改状态
                    if (status) {
                        CarInfo carInfo = (CarInfo) carInfoDao.get(objects[2].toString());
                        if (carInfo != null) {
                            carInfo.setUseData(true);
                            carInfoDao.updateBean(carInfo);
                        }
                    } else {

                        map.put("carTypeName", objects[0]);
                        map.put("userID", objects[1]);
                        map.put("id", objects[2]);
                        ListMap.add(map);
                    }
                }
                return ListMap;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<CarInfo> getCarInfosByStoreId(Pager page, String storeId, String carNo, String checkStatus) {
        if (storeId == null) {
            return null;
        }
        StringBuffer hql = new StringBuffer(" from CarInfo c where c.storeId = :storeId ");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("storeId", storeId);
        if (!StringUtil.isEmpty(carNo)) {
            hql.append(" and c.carNo like :carNo");
            map.put("carNo", "%" + carNo + "%");
        }
        if ("0".equals(checkStatus)) {
            hql.append(" and c.checkStatus='0'");
        } else if ("1".equals(checkStatus)) {
            hql.append(" and c.checkStatus='1'");
        }
        hql.append(" order by c.updateTime desc");
        try {
            if (page != null) {
                return carInfoDao.list(page, hql.toString(), map);
            } else {
                return carInfoDao.list(hql.toString(), map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<CarInfo> getAllCheckedList() {
        String sql = "SELECT c FROM CarInfo c WHERE 1 = 1";
        sql += " and c.checkStatus = '1'";
        List<CarInfo> carInfos = new ArrayList<CarInfo>();
        try {
            // 调用DAO方法查找商品
            carInfos = carInfoDao.list(sql.toString(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return carInfos;
    }
}