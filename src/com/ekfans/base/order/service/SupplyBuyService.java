package com.ekfans.base.order.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.page.BasicRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.order.dao.ISupplyBuyDao;
import com.ekfans.base.order.model.SupplyBuy;
import com.ekfans.base.product.model.Product;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;
import com.ekfans.base.system.service.ISystemAreaService;
import com.ekfans.base.system.service.SystemAreaService;
import com.ekfans.basic.spring.SpringContextHolder;

import javax.servlet.http.HttpServletRequest;

/**
 * 供求关系业务实现Service
 *
 * @author 成都易科远见科技有限公司
 * @version 1.0
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: 成都易科远见科技有限公司
 * @date 2014-1-6
 */
@Service
public class SupplyBuyService implements ISupplyBuyService {
    // 定义错误日志
    private Logger log = LoggerFactory.getLogger(SupplyBuyService.class);
    // 定义DAO
    @Autowired
    private ISupplyBuyDao supplyBuyDao;

    @Override
    public boolean add(SupplyBuy sb) {
        try {
            supplyBuyDao.addBean(sb);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除
     */
    @Override
    public boolean remove(String id) {
        if (StringUtil.isEmpty(id)) {
            return false;
        }
        try {
            supplyBuyDao.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<SupplyBuy> listSupplyBuy(Pager pager, String title, String beginDate, String endDate, String storeId, String type, String status, String productType, String checkStatus,
                                         String checkType, String storeName) {
        // 定义查询SQL
        StringBuffer sql = new StringBuffer(" select sb.id,sb.title,sb.contactName,sb.contactPhone,sb.type,sb.status,sb.productType,sb.createTime,sb.checkStatus,sb.storeName"
                + " from SupplyBuy as sb where 1=1");
        // 定义参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();
        // 当品牌的id和product品跑对应
        if (!StringUtil.isEmpty(title)) {
            sql.append(" and sb.title like :title");
            paramMap.put("title", "%" + title + "%");
        }
        if (!StringUtil.isEmpty(beginDate)) {
            sql.append(" and sb.createTime>=:beginDate");
            paramMap.put("beginDate", beginDate);
        }
        if (!StringUtil.isEmpty(endDate)) {
            sql.append(" and sb.createTime<=:endDate");
            paramMap.put("endDate", endDate);
        }
        if (!StringUtil.isEmpty(storeId)) {
            sql.append(" and sb.storeId = :storeId");
            paramMap.put("storeId", storeId);
        }
        if (!StringUtil.isEmpty(type)) {
            sql.append(" and sb.type = :type");
            paramMap.put("type", type);
        }
        if (!StringUtil.isEmpty(status)) {
            sql.append(" and sb.status = :status");
            paramMap.put("status", status);
        }
        if (!StringUtil.isEmpty(productType)) {
            sql.append(" and sb.productType = :productType");
            paramMap.put("productType", productType);
        }
        if (!StringUtil.isEmpty(checkStatus)) {
            if ("0".equals(checkType)) {
                sql.append(" and sb.checkStatus != :checkStatus");
                paramMap.put("checkStatus", Integer.parseInt(checkStatus));
            } else if ("1".equals(checkType)) {
                sql.append(" and sb.checkStatus = :checkStatus");
                paramMap.put("checkStatus", Integer.parseInt(checkStatus));
            }

        }
        if (!StringUtil.isEmpty(storeName)) {
            sql.append(" and sb.storeName like :storeName");
            paramMap.put("storeName", "%" + storeName + "%");
        }
        sql.append(" order by sb.createTime desc");
        try {
            // 调用DAO方法查找商品
            List<Object[]> list = supplyBuyDao.list(pager, sql.toString(), paramMap);
            List<SupplyBuy> supplyBuys = new ArrayList<SupplyBuy>();
            for (Object[] object : list) {
                SupplyBuy sb = new SupplyBuy();
                sb.setId((String) object[0]);
                sb.setTitle((String) object[1]);
                sb.setContactName((String) object[2]);
                sb.setContactPhone((String) object[3]);
                sb.setType((String) object[4]);
                sb.setStatus((String) object[5]);
                sb.setProductType((String) object[6]);
                sb.setCreateTime((String) object[7]);
                sb.setCheckStatus((Integer) object[8]);
                sb.setStoreName((String) object[9]);
                supplyBuys.add(sb);
            }
            return supplyBuys;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public SupplyBuy getSupplyBuyById(String id) {
        try {
            return (SupplyBuy) supplyBuyDao.get(id);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public boolean update(SupplyBuy sb) {
        try {
            supplyBuyDao.updateBean(sb);
            // 删除时更新缓存
            if ("0".equals(sb.getStatus())) {
                Cache.refrefshIndexSupplyBuyInfo(sb.getType(), sb.getProductType());
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public List<SupplyBuy> webListSupplyBuy(Pager pager, String checkStatus, String productType, String type, String status, String catgId) {
        // 定义查询SQL
        StringBuffer sql = new StringBuffer(" select sb from  SupplyBuy as sb,ProductCategory pc where 1=1");
        // 定义参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();
        // 当品牌的id和product品跑对应
        sql.append(" and pc.id=sb.categoryId");
        if (!StringUtil.isEmpty(type)) {
            sql.append(" and sb.type = :type");
            paramMap.put("type", type);
        }
        if (!StringUtil.isEmpty(status)) {
            sql.append(" and sb.status = :status");
            paramMap.put("status", status);
        }
        if (!StringUtil.isEmpty(productType)) {
            sql.append(" and sb.productType = :productType");
            paramMap.put("productType", productType);
        }
        if (!StringUtil.isEmpty(checkStatus)) {
            sql.append(" and sb.checkStatus = :checkStatus");
            paramMap.put("checkStatus", Integer.parseInt(checkStatus));
        }
        if (!StringUtil.isEmpty(catgId)) {
            sql.append(" and pc.fullPath like :catgId");
            paramMap.put("catgId", "%" + catgId + "%");
        }
        // 前台显示过滤掉已过期的数据
        sql.append(" and sb.endTime>=:endTime");
        paramMap.put("endTime", DateUtil.getSysDateString());

        sql.append(" order by sb.createTime desc");
        try {
            // 调用DAO方法查找商品
            List<SupplyBuy> supplyBuys = supplyBuyDao.list(pager, sql.toString(), paramMap);
            return supplyBuys;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public List<SupplyBuy> webListSupplyShow(Pager pager, String type) {
        // 状态正常
        String status = "1";
        // 通过审核
        Integer checkStatus = 1;
        return supplyBuyDao.listCommon(pager, null, type, status, null, checkStatus);
    }

    @Override
    public List<SupplyBuy> webListSupplyTable(Pager pager, String type, String title) {
        // 状态正常
        String status = "1";
        // 通过审核
        Integer checkStatus = 1;
        return supplyBuyDao.listCommon(pager, title, type, status, null, checkStatus);
    }

    @Override
    public List<SupplyBuy> listSupplyTable(Pager pager, String storeId, String title, String type, String productType, String destination, String categoryId, String lowPrice, String topPrice) {
        // 定义查询SQL
        StringBuffer sql = new StringBuffer(" select sb  from  SupplyBuy as sb,ProductCategory pc where 1=1");
        // 定义参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();
        // 当品牌的id和product品跑对应
        sql.append(" and pc.id=sb.categoryId");
        if (!StringUtil.isEmpty(storeId)) {
            sql.append(" and sb.storeId = :storeId");
            paramMap.put("storeId", "%" + storeId + "%");
        }
        if (!StringUtil.isEmpty(title)) {
            sql.append(" and sb.title like :title");
            paramMap.put("title", "%" + title + "%");
        }
        if (!StringUtil.isEmpty(type)) {
            sql.append(" and sb.type = :type");
            paramMap.put("type", type);
        }
        if (!StringUtil.isEmpty(productType)) {
            sql.append(" and sb.productType = :productType");
            paramMap.put("productType", productType);
        }
        if (!StringUtil.isEmpty(destination)) {
            sql.append(" and sb.destination like :destination");
            paramMap.put("destination", "%" + destination + "%");
        }

        if (!StringUtil.isEmpty(categoryId)) {
            sql.append(" and sb.categoryId = :categoryId");
            paramMap.put("categoryId", categoryId);
        }
        if (!StringUtil.isEmpty(lowPrice)) {
            sql.append(" and sb.futurePrices > :lowPrice");
            paramMap.put("lowPrice", new BigDecimal(lowPrice));
        }
        if (!StringUtil.isEmpty(topPrice)) {
            sql.append(" and sb.futurePrices <:topPrice");
            paramMap.put("topPrice", new BigDecimal(topPrice));
        }

        // 显示没有过期的供求
        String nowTime = DateUtil.getSysDateTimeString();
        sql.append(" and sb.endTime > :nowTime");
        paramMap.put("nowTime", nowTime);

        // 前台显示过滤掉不合条件的数据
        // 审核通过
        sql.append(" and sb.checkStatus = 1");
        // 正常状态
        sql.append(" and sb.status = '1'");
        // 过滤过期数据
        // sql.append(" and sb.endTime>=:endTime");
        // paramMap.put("endTime", DateUtil.getSysDateString());

        sql.append(" order by  sb.createTime desc");
        try {
            // 调用DAO方法查找商品
            List<SupplyBuy> supplyBuys = supplyBuyDao.list(pager, sql.toString(), paramMap);
            //通过 areaId 获取地址，以逗号分隔
            ISystemAreaService systemAreaService = SpringContextHolder.getBean(ISystemAreaService.class);
            for (SupplyBuy sb : supplyBuys) {
                String sbAreaId = sb.getAreaId();
                String sbDestination = systemAreaService.getAreaFullNameByAreaIdAndDelimiter(sbAreaId, ",");
                sb.setDestination(sbDestination);
            }

            return supplyBuys;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public List<SupplyBuy> getNewSupplyList(String productType, String type) {
        StringBuffer sql = new StringBuffer(" select sb.title,sb.futurePrices,sb.unit from SupplyBuy as sb where 1=1");
        Map<String, Object> params = new HashMap<>();
        if (!StringUtil.isEmpty(productType)) {
            sql.append(" and sb.productType =:productType");
            params.put("productType", productType);
        }
        if (!StringUtil.isEmpty(type)) {
            sql.append(" and sb.type =:type");
            params.put("type", type);
        }
        // 查询时间最近6条数据
        sql.append(" order by sb.createTime desc");
        Pager page = new Pager();
        // 查前6条
        page.setRowsPerPage(6);
        try {
            List<Object[]> objs = supplyBuyDao.list(page, sql.toString(), params);
            List<SupplyBuy> sbs = new ArrayList<SupplyBuy>();
            if (objs != null && objs.size() > 0) {
                for (Object[] obj : objs) {
                    SupplyBuy sb = new SupplyBuy();
                    String title = (String) obj[0];
                    BigDecimal futurePrices = (BigDecimal) obj[1];
                    String unit = (String) obj[2];
                    sb.setTitle(title);
                    sb.setFuturePrices(futurePrices);
                    sb.setUnit(unit);
                    sbs.add(sb);
                }
            }
            return sbs;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public List<SupplyBuy> listSupplyApp(Pager pager, String type, String storeId, String title, String categoryId, String destination, String checkStatus, boolean guoqi) {
        // 定义查询SQL
        StringBuffer sql = new StringBuffer(" select sb from  SupplyBuy as sb");
        if (!StringUtil.isEmpty(categoryId)) {
            sql.append(" ,ProductCategory pc");
        }
        sql.append(" where 1=1");
        // 定义参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();
        // 当品牌的id和product品跑对应
        if (!StringUtil.isEmpty(categoryId)) {
            sql.append(" and pc.id=sb.categoryId");
            sql.append(" and sb.categoryId = :categoryId");
            paramMap.put("categoryId", categoryId);
        }
        if (!StringUtil.isEmpty(storeId)) {
            sql.append(" and sb.storeId = :storeId");
            paramMap.put("storeId", storeId);
        }
        if (!StringUtil.isEmpty(title)) {
            sql.append(" and sb.title like :title");
            paramMap.put("title", "%" + title + "%");
        }
        if (!StringUtil.isEmpty(type)) {
            sql.append(" and sb.type = :type");
            paramMap.put("type", type);
        }
        if (!StringUtil.isEmpty(destination)) {
            sql.append(" and sb.destination like :destination");
            paramMap.put("destination", "%" + destination + "%");
        }
        // 显示没有过期的供求
        String nowTime = DateUtil.getSysDateTimeString();

        if (!StringUtil.isEmpty(checkStatus)) {
            sql.append(" and sb.checkStatus = :checkStatus");
            paramMap.put("checkStatus", Integer.parseInt(checkStatus));
        }
        // 正常状态
        sql.append(" and sb.status = '1'");

        // 过滤过期数据
        if (guoqi) {
            sql.append(" and sb.endTime > :nowTime");
            paramMap.put("nowTime", nowTime);
        }

        sql.append(" order by  sb.createTime desc");


        try {
            // 调用DAO方法查找商品
            List<SupplyBuy> supplyBuys = supplyBuyDao.list(pager, sql.toString(), paramMap);
            return supplyBuys;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public List<SupplyBuy> getIndexSupplyBuy(Pager pager, String storeId, String title, String type, String productType, String destination, String categoryId, String lowPrice, String topPrice) {
        // 定义查询SQL
        StringBuffer sql = new StringBuffer(" select sb  from  SupplyBuy as sb,ProductCategory pc where 1=1");
        // 定义参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();
        // 当品牌的id和product品跑对应
        sql.append(" and pc.id=sb.categoryId");
        if (!StringUtil.isEmpty(storeId)) {
            sql.append(" and sb.storeId = :storeId");
            paramMap.put("storeId", "%" + storeId + "%");
        }
        if (!StringUtil.isEmpty(title)) {
            sql.append(" and sb.title like :title");
            paramMap.put("title", "%" + title + "%");
        }
        if (!StringUtil.isEmpty(type)) {
            sql.append(" and sb.type = :type");
            paramMap.put("type", type);
        }
        if (!StringUtil.isEmpty(productType)) {
            sql.append(" and sb.productType = :productType");
            paramMap.put("productType", productType);
        }
        if (!StringUtil.isEmpty(destination)) {
            sql.append(" and sb.destination like :destination");
            paramMap.put("destination", "%" + destination + "%");
        }

        if (!StringUtil.isEmpty(categoryId)) {
            sql.append(" and sb.categoryId = :categoryId");
            paramMap.put("categoryId", categoryId);
        }
        if (!StringUtil.isEmpty(lowPrice)) {
            sql.append(" and sb.futurePrices > :lowPrice");
            paramMap.put("lowPrice", new BigDecimal(lowPrice));
        }
        if (!StringUtil.isEmpty(topPrice)) {
            sql.append(" and sb.futurePrices <:topPrice");
            paramMap.put("topPrice", new BigDecimal(topPrice));
        }

        // 显示没有过期的供求
        String nowTime = DateUtil.getSysDateTimeString();
        sql.append(" and sb.endTime > :nowTime");
        paramMap.put("nowTime", nowTime);

        // 前台显示过滤掉不合条件的数据
        // 审核通过
        sql.append(" and sb.checkStatus = 1");
        // 正常状态
        sql.append(" and sb.status = '1'");
        // 过滤过期数据
        // sql.append(" and sb.endTime>=:endTime");
        // paramMap.put("endTime", DateUtil.getSysDateString());

        sql.append(" order by  sb.createTime desc");
        try {
            // 调用DAO方法查找商品
            List<SupplyBuy> supplyBuys = supplyBuyDao.list(pager, sql.toString(), paramMap);
            //通过 areaId 获取地址，以逗号分隔
            ISystemAreaService systemAreaService = SpringContextHolder.getBean(ISystemAreaService.class);
            for (SupplyBuy sb : supplyBuys) {
                String sbAreaId = sb.getAreaId();
                String sbDestination = systemAreaService.getAreaFullNameByAreaIdAndDelimiter(sbAreaId, ",");
                sb.setDestination(sbDestination);
            }

            return supplyBuys;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return null;
    }


    /**
     * 用于展示首页的供求信息
     *
     * @param pager
     * @param categoryId
     * @return
     */
    public List<SupplyBuy> appSupplyShow(Pager pager, String type, String productType, String destination, String categoryId, HttpServletRequest request) {
        // 定义参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();

        // 定义查询SQL
        StringBuffer sql = new StringBuffer(" select sb,pc.name  from  SupplyBuy as sb,ProductCategory as pc ");

//        if (!StringUtil.isEmpty(categoryId)) {
//            sql.append(",ProductCategory pc");
//        }
        sql.append(" where 1=1");
        sql.append(" and pc.id=sb.categoryId");
//        if (!StringUtil.isEmpty(categoryId)) {
//            // 当品牌的id和product品跑对应
//            sql.append(" and pc.id=sb.categoryId");
//        }
        if (!StringUtil.isEmpty(type)) {
            sql.append(" and sb.type = :type");
            paramMap.put("type", type);
        }
        if (!StringUtil.isEmpty(productType)) {
            sql.append(" and sb.productType = :productType");
            paramMap.put("productType", productType);
        }
        if (!StringUtil.isEmpty(destination)) {
            sql.append(" and sb.destination like :destination");
            paramMap.put("destination", "%" + destination + "%");
        }

        if (!StringUtil.isEmpty(categoryId)) {
//            sql.append(" and pc.fullPath like :categoryId");
//            paramMap.put("categoryId", "%" + categoryId + "%");
            sql.append(" and sb.categoryId = :categoryId");
            paramMap.put("categoryId", categoryId);
        }

        // 显示没有过期的供求
        String nowTime = DateUtil.getSysDateTimeString();
        sql.append(" and sb.endTime > :nowTime");
        paramMap.put("nowTime", nowTime);

        // 前台显示过滤掉不合条件的数据
        // 审核通过
        sql.append(" and sb.checkStatus = 1");
        // 正常状态
        sql.append(" and sb.status = '1'");
        // 过滤过期数据
        // sql.append(" and sb.endTime>=:endTime");
        // paramMap.put("endTime", DateUtil.getSysDateString());

        sql.append(" order by  sb.createTime desc");
        try {
            // 调用DAO方法查找商品
            List<Object[]> supplyBuys = supplyBuyDao.list(pager, sql.toString(), paramMap);
            //通过 areaId 获取地址，以逗号分隔
            ISystemAreaService systemAreaService = SpringContextHolder.getBean(ISystemAreaService.class);
            BasicRequest br = new BasicRequest(request);
            List<SupplyBuy> rList = new ArrayList<>();
            for (Object[] obj : supplyBuys) {
                if (obj == null || obj.length <= 0) {
                    continue;
                }
                SupplyBuy sb = (SupplyBuy) obj[0];
                String categoryName = (String) obj[1];
                String sbAreaId = sb.getAreaId();
                String sbDestination = systemAreaService.getAreaFullNameByAreaIdAndDelimiter(sbAreaId, ",");
                sb.setDestination(sbDestination);
                sb.setCategoryName(categoryName);
                sb.setShareUrl(br.getWebFullUrlPrex() + "/ccwcc/supply/share/" + sb.getId());
                rList.add(sb);
            }

            return rList;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 后台查数量
     * @param type(类型0供应，1求购)
     * @return
     */
	public int getSupplyBuyCheckNum(String productType,int type) {
		StringBuffer sql=  new StringBuffer("select count(1) from SupplyBuy where 1=1");
		Map<String,Object> map = new HashMap<String, Object>();
		sql.append(" and productType ="+productType);
		sql.append(" and type ="+type);
		sql.append(" and checkStatus = 0 ");
		// 有效期判断
		String time = DateUtil.getSysDateTimeString();
		sql.append(" and (s.endTime > :time or s.endTime is null or s.endTime = '')");
		map.put("time", time);
		@SuppressWarnings("unchecked")
		List<BigInteger> num = supplyBuyDao.createSession().createSQLQuery(sql.toString()).list();
		return num.get(0).intValue();
	}
}
