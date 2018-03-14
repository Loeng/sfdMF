package com.ekfans.base.store.service;

import com.ekfans.base.store.dao.ICarUserDao;
import com.ekfans.base.store.model.CarUser;
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
 * 运输企业车辆人员信息Service接口实现
 *
 * @author ek_lq
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 * @ClassName: CreditEstimatesService
 * @Description:
 * @date 2014-9-20 上午11:55:14
 */
@Service
public class CarUserService implements ICarUserService {

    private Logger log = LoggerFactory.getLogger(CarUserService.class);
    @Resource
    private ICarUserDao carUserDao;

    /**
     * 新增或编辑车辆人员信息
     *
     * @param carUser
     * @param request
     * @param response
     * @return
     */
    public boolean saveOrUpdate(CarUser carUser, HttpServletRequest request,
                                HttpServletResponse response) {
        try {
            Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
            if (carUser == null || store == null) {
                return false;
            }
            String currentPath = "/customerfiles/store/" + store.getId() + "/" + DateUtil.getNoSpSysDateString();
            // 调用方法获取分类图标，返回图标路径
            if (carUser.getType().equals("0")) {
                String driverFile = FileUploadHelper.uploadFile("driverFile", currentPath, request, response);
                carUser.setDriverFile(driverFile);

                String licenseFile = FileUploadHelper.uploadFile("licenseFile", currentPath, request, response);
                carUser.setLicenseFile(licenseFile);
            } else {
                String licenseFile = FileUploadHelper.uploadFile("licenseFile", currentPath, request, response);
                carUser.setLicenseFile(licenseFile);
            }
            if (StringUtil.isEmpty(carUser.getCreateTime())) {
                carUser.setCreateTime(DateUtil.getSysDateString());
            }
            carUser.setUpdateTime(DateUtil.getSysDateString());
            carUser.setCheckStatus("0");
            carUser.setStoreId(store.getId());
            carUserDao.saveOrUpdateBean(carUser);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 批量删除车辆人员信息
     *
     * @param carUserIds
     * @return
     */
    public boolean delete(String[] carUserIds) {
        // 如果传过来的数组为空或者长度小于等于0，则返回失败
        if (carUserIds == null || carUserIds.length <= 0) {
            return false;
        }
        try {
            // 如果长度为1，则调用单个删除方法，并返回
            if (carUserIds.length == 1) {
                return deleteById(carUserIds[0]);
            }
            // 调用DAO方法批量删除
            carUserDao.deleteByIds(carUserIds);


            // 数组转换字符串
            String ids = org.apache.commons.lang.StringUtils.join(carUserIds);
            // 删除成功将车辆信息同步到监控平台ids
            new MonitorSyncMain(MonitorDataConvert.initDelCommon(ids), "202").start();
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * 根据车辆人员信息ID删除车辆信息
     *
     * @param carUserId
     * @return
     */
    public boolean deleteById(String carUserId) {
        // 如果传过来的ID为空，则返回失败
        if (StringUtil.isEmpty(carUserId)) {
            return false;
        }
        try {
            // 调用DAO删除对象
            carUserDao.deleteById(carUserId);

            // 删除成功将车辆信息同步到监控平台
            new MonitorSyncMain(MonitorDataConvert.initDelCommon(carUserId), "202").start();
            // 返回删除成功状态
            return true;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return false;
    }

    /**
     * 直接更新车辆人员信息表
     *
     * @param info
     * @return
     */
    public boolean updateBean(CarUser carUser) {
        try {
            if (carUser != null) {
                carUserDao.updateBean(carUser);
                return true;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * 根据车辆人员信息ID获取车辆信息对象
     *
     * @param carUserId
     * @return
     */
    public CarUser getCarUserById(String carUserId) {
        try {
            if (!StringUtil.isEmpty(carUserId)) {
                return (CarUser) carUserDao.get(carUserId);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 根据搜索条件查询车辆信息集合
     *
     * @param userId
     * @param carNo
     * @param checkStatus
     * @return
     */
    public List<CarUser> getCarUsersByStoreId(Pager pager, HttpServletRequest request,
                                              HttpServletResponse response, String userType,
                                              String name, String mobile, String checkStatus) {
        Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
        if (store == null) {
            return null;
        }
        StringBuffer hql = new StringBuffer(" from CarUser c where c.storeId=:storeId ");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("storeId", store.getId());
        if (!StringUtil.isEmpty(name)) {
            hql.append(" and c.name like :name");
            map.put("name", "%" + name + "%");
        }
        if (!StringUtil.isEmpty(mobile)) {
            hql.append(" and c.mobile like :mobile");
            map.put("mobile", "%" + mobile + "%");
        }
        if ("0".equals(userType)) {
            hql.append(" and c.type='0'");
        } else if ("1".equals(userType)) {
            hql.append(" and c.type='1'");
        }
        if ("0".equals(checkStatus)) {
            hql.append(" and c.checkStatus='0'");
        } else if ("1".equals(checkStatus)) {
            hql.append(" and c.checkStatus='1'");
        }
        hql.append(" order by c.createTime desc");
        try {
            return carUserDao.list(pager, hql.toString(), map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //-------------------以下是后台
    @Override
    public List<CarUser> getCarUserByPageSystem(String userType, Pager pager, String storeName, HttpServletRequest request, HttpServletResponse response) {
        StringBuffer hql = new StringBuffer("select c,s.storeName from CarUser c,Store s where c.storeId=s.id and c.checkStatus='0'");
        Map<String, Object> map = new HashMap<String, Object>();
        if (!StringUtil.isEmpty(storeName)) {
            hql.append(" and s.storeName like :storeName");
            map.put("storeName", "%" + storeName + "%");
        }
        if ("0".equals(userType)) {
            hql.append(" and c.type='0'");
        } else if ("1".equals(userType)) {
            hql.append(" and c.type='1'");
        }
        hql.append(" order by c.createTime desc");
        try {
            List<Object[]> list = carUserDao.list(hql.toString(), map);
            List<CarUser> carUsers = new ArrayList<CarUser>();
            for (Object[] object : list) {
                CarUser carUser = (CarUser) object[0];
                carUser.setStoreName((String) object[1]);
                carUsers.add(carUser);
            }
            return carUsers;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean save(CarUser carUser, HttpServletRequest request, HttpServletResponse response) {
        try {
            Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
            if (carUser == null || store == null) {
                return false;
            }
            String currentPath = "/customerfiles/store/" + store.getId() + "/" + DateUtil.getNoSpSysDateString();
            // 调用方法获取分类图标，返回图标路径
            if (carUser.getType().equals("0")) {
                String driverFile = FileUploadHelper.uploadFile("driverFile", currentPath, request, response);
                carUser.setDriverFile(driverFile);

                String licenseFile = FileUploadHelper.uploadFile("licenseFile", currentPath, request, response);
                carUser.setLicenseFile(licenseFile);
            } else {
                String licenseFile = FileUploadHelper.uploadFile("licenseFile", currentPath, request, response);
                carUser.setLicenseFile(licenseFile);
            }
            if (StringUtil.isEmpty(carUser.getCreateTime())) {
                carUser.setCreateTime(DateUtil.getSysDateString());
            }
            carUser.setUpdateTime(DateUtil.getSysDateString());
            carUser.setCheckStatus("0");
            carUser.setStoreId(store.getId());
            carUserDao.addBean(carUser);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<CarUser> getAllCheckedList() {
        String sql = "SELECT c FROM CarUser c WHERE 1 = 1";
        sql += " and c.checkStatus = '1'";
        List<CarUser> carUsers = new ArrayList<CarUser>();
        try {
            // 调用DAO方法查找商品
            carUsers = carUserDao.list(sql.toString(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return carUsers;
    }
}