package com.ekfans.base.metal.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.ekfans.controllers.ccwccApp.AppPreciousDetailBean;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.metal.dao.IPreciousMetalDetailDao;
import com.ekfans.base.metal.model.PreciousMetal;
import com.ekfans.base.metal.model.PreciousMetalDetail;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.JsonUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 品目Service接口实现
 *
 * @author ek_lq
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 * @ClassName: InquiryService
 * @Description:
 * @date 2014-9-20 上午11:55:14
 */
@Service
@SuppressWarnings("unchecked")
public class PreciousMetalDetailService implements IPreciousMetalDetailService {

    private Logger log = LoggerFactory.getLogger(PreciousMetalDetailService.class);

    @Autowired
    private IPreciousMetalDetailDao detailDao;
	@Resource
	private IPreciousMetalService metalService;
    @Override
    public Map<String, PreciousMetalDetail> getDetailsByDateAndCategory(String categoryId, String date) {
        if (StringUtil.isEmpty(categoryId)) {
            return null;
        }
        StringBuffer sql = new StringBuffer("select detail from PreciousMetalDetail as detail,PreciousMetal as metal where 1=1");
        Map<String, Object> paramMap = new HashMap<String, Object>();

        sql.append(" and metal.categoryId = :categoryId");
        paramMap.put("categoryId", categoryId);
        sql.append(" and metal.id = detail.metalId");

        if (StringUtil.isEmpty(date)) {
            date = DateUtil.getSysDateString();
        }
        sql.append(" and detail.dateTime = :dateTime");
        paramMap.put("dateTime", date);

        try {
            List<PreciousMetalDetail> details = detailDao.list(sql.toString(), paramMap);
            if (details == null || details.size() <= 0) {
                return null;
            }

            Map<String, PreciousMetalDetail> rMap = new HashMap<String, PreciousMetalDetail>();
            for (PreciousMetalDetail detail : details) {
                if (detail == null) {
                    continue;
                }
                rMap.put(detail.getMetalId(), detail);
            }
            return rMap;
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return null;
    }

    /**
     * 批量新增明细
     *
     * @param metalIds
     * @param request
     * @return
     */
    @Override
    public boolean saveDetails(String[] metalIds, HttpServletRequest request) {
        if (metalIds == null || metalIds.length <= 0) {
            return false;
        }

        Session session = null;
        Transaction transaction = null;
        try {
            session = detailDao.createSession();
            transaction = session.beginTransaction();
            // 先从数据库中删除
            String dateTime = request.getParameter("chosedDate");
            StringBuffer sql = new StringBuffer(" from PreciousMetalDetail as detail where 1=1");
            Map<String, Object> paramMap = new HashMap<String, Object>();
            sql.append(" and detail.metalId in :metalId");
            paramMap.put("metalId", metalIds);
            sql.append(" and detail.dateTime = :dateTime");
            paramMap.put("dateTime", dateTime);
            detailDao.delete(sql.toString(), paramMap, session);

            for (String metalId : metalIds) {
                PreciousMetalDetail detail = new PreciousMetalDetail();
                detail.setCreateTime(DateUtil.getSysDateTimeString());
                detail.setDateTime(dateTime);
                detail.setMetalId(metalId);

                String priceType = request.getParameter(metalId + "priceType");
                String priceStr = request.getParameter(metalId + "price");
                String startPriceStr = request.getParameter(metalId + "startPrice");
                String endPriceStr = request.getParameter(metalId + "endPrice");
                String riseAndDropStr = request.getParameter(metalId + "riseAndDrop");
                String smmId =request.getParameter(metalId + "smmId");
                
                if ("0".equals(priceType)) {
                    detail.setPriceType(false);
                    detail.setPrice(new BigDecimal(priceStr));
                    detail.setStartPrice(detail.getPrice());
                    detail.setEndPrice(detail.getPrice());
                } else {
                    detail.setPriceType(true);
                    detail.setStartPrice(new BigDecimal(startPriceStr));
                    detail.setEndPrice(new BigDecimal(endPriceStr));
                    BigDecimal price = (detail.getStartPrice().add(detail.getEndPrice())).divide(new BigDecimal(2));
                    detail.setPrice(price);
                }
                    
                try {
                	detail.setSmmId(smmId);
                    detail.setRiseAndDrop(Double.parseDouble(riseAndDropStr));
                } catch (Exception e) {
                    // TODO: handle exception
                }
    			PreciousMetal metal = metalService.getMetalById(metalId);
    			metal.setSmmId(smmId);
    			metalService.saveOrUpdate(metal);
                detailDao.addBean(detail, session);
            }
            session.flush();
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return false;
    }

    @Override
    public boolean saveOrUpdate(PreciousMetalDetail detail) {
        if (detail == null) {
            return false;
        }

        Session session = null;
        Transaction transaction = null;
        try {
            session = detailDao.createSession();
            transaction = session.beginTransaction();
            // 先从数据库中删除
            StringBuffer sql = new StringBuffer(" from PreciousMetalDetail as detail where 1=1");
            Map<String, Object> paramMap = new HashMap<String, Object>();
            sql.append(" and detail.metalId = :metalId");
            paramMap.put("metalId", detail.getMetalId());
            sql.append(" and detail.dateTime = :dateTime");
            paramMap.put("dateTime", detail.getDateTime());
            detailDao.delete(sql.toString(), paramMap, session);

            detail.setCreateTime(DateUtil.getSysDateTimeString());
            detailDao.addBean(detail, session);

            session.flush();
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return false;
    }

    /**
     * 获取前台图标的JSON数据
     *
     * @param metaId
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public String getJsonStr(String metaId) {
        if (StringUtil.isEmpty(metaId)) {
            return null;
        }
        List<Map<String, String>> jsonList = new LinkedList<Map<String, String>>();
        StringBuffer sql = new StringBuffer("select detail.price,detail.dateTime from  PreciousMetalDetail as detail where 1=1");
        Map<String, Object> paramMap = new HashMap<String, Object>();
        sql.append(" and detail.metalId = :metalId");
        paramMap.put("metalId", metaId);
        sql.append(" order by detail.dateTime desc");
        try {
            Pager pager = new Pager(1);
            pager.setRowsPerPage(7);
            List<Object[]> details = detailDao.list(pager, sql.toString(), paramMap);
            if (details == null || details.size() <= 0) {
                return JsonUtil.listToJson(jsonList);
            }

            SimpleDateFormat format = new SimpleDateFormat("MM/dd");
            SimpleDateFormat sformat = new SimpleDateFormat("yyyy-MM-dd");

            for (int i = details.size() - 1; i >= 0; i--) {
                Object[] obj = details.get(i);
                BigDecimal price = (BigDecimal) obj[0];
                String priceStr = price.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "";
                String dateTime = obj[1].toString();

                Map<String, String> childMap = new HashMap<String, String>();
                childMap.put("value", priceStr);
                childMap.put("date", format.format(sformat.parse(dateTime)));
                jsonList.add(childMap);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        return JsonUtil.listToJson(jsonList);
    }

    /**
     * 获取前台图标的JSON数据
     *
     * @param metaId
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public List<AppPreciousDetailBean> getPreciousDetail(String metaId, int dateNum) {
        if (StringUtil.isEmpty(metaId)) {
            return null;
        }
        List<AppPreciousDetailBean> jsonList = new LinkedList<>();
        StringBuffer sql = new StringBuffer("select detail.price,detail.dateTime from  PreciousMetalDetail as detail where 1=1");
        Map<String, Object> paramMap = new HashMap<String, Object>();
        sql.append(" and detail.metalId = :metalId");
        paramMap.put("metalId", metaId);
        sql.append(" order by detail.dateTime desc");
        try {
            Pager pager = new Pager(1);
            if (dateNum > 0) {
                pager.setRowsPerPage(dateNum);
            } else {
                pager.setRowsPerPage(7);
            }
            List<Object[]> details = detailDao.list(pager, sql.toString(), paramMap);
            if (details == null || details.size() <= 0) {
                return null;
            }

            SimpleDateFormat format = new SimpleDateFormat("MM/dd");
            SimpleDateFormat sformat = new SimpleDateFormat("yyyy-MM-dd");

            for (int i = details.size() - 1; i >= 0; i--) {
                Object[] obj = details.get(i);
                BigDecimal price = (BigDecimal) obj[0];
//                String priceStr = price.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "";
                String dateTime = obj[1].toString();

                AppPreciousDetailBean detailBean = new AppPreciousDetailBean();
                detailBean.setDateTime(dateTime);
                detailBean.setPrice(price);

                jsonList.add(detailBean);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        return jsonList;
    }
}
