package com.ekfans.base.store.dao;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ekfans.base.store.model.Wftransport;
import com.ekfans.basic.hibernate.dao.GenericDao;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * @ClassName: WftransportDao  
 * @Description: TODO(WftransportDao接口实现) 
 * @Copyright: Copyright (c) 2016年3月15日
 * @Company: 成都易科远见科技有限公司
 * @author 成都易科远见科技有限公司
 * @date 2016年3月15日上午11:58:25
 * @version 1.0
 */
@Repository
public class WftransportDao extends GenericDao implements IWftransportDao{

	 public WftransportDao() throws HibernateException {
        super();
        this.beanClass = "com.ekfans.base.store.model.Wftransport";
    }

	@SuppressWarnings("unchecked")
	@Override
	public List<Wftransport> getUserWftransport(String userid,int type,Pager page,String status) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("select w from Wftransport as w where 1=1");
		List<Wftransport> wftransportsTemp=null;
		// 用户id
		if(!StringUtil.isEmpty(userid)){
			sql.append(" and w.storeId =:storeId");
			map.put("storeId",userid);
			sql.append(" and w.type = "+type+"");
			//全部(0) 已上架(1) 已下架(2) 正在审核(3) 审核未通过(4) 已完成(5) 已审核(6)
			if(status.equals("1")){
				sql.append(" and w.status =:status");
				map.put("status",1);
				sql.append(" and w.checkStatus =:checkStatus");
				map.put("checkStatus",1);
				// 有效期判断
				String time = DateUtil.getSysDateTimeString();
				sql.append(" and (w.endTime > :time or w.endTime is null or w.endTime = '')");
				map.put("time", time);
			}
			
			if(status.equals("2")){
				sql.append(" and w.status =:status");
				map.put("status",0);
				sql.append(" and w.checkStatus =:checkStatus");
				map.put("checkStatus",1);
				// 有效期判断
				String time = DateUtil.getSysDateTimeString();
				sql.append(" and (w.endTime > :time or w.endTime is null or w.endTime = '')");
				map.put("time", time);
			}
			
			if(status.equals("3")){
				sql.append(" and w.checkStatus =:checkStatus");
				map.put("checkStatus",0);
				// 有效期判断
				String time = DateUtil.getSysDateTimeString();
				sql.append(" and (w.endTime > :time or w.endTime is null or w.endTime = '')");
				map.put("time", time);
			}
			
			if(status.equals("6")){
				sql.append(" and w.checkStatus =:checkStatus");
				map.put("checkStatus",1);
				// 有效期判断
				String time = DateUtil.getSysDateTimeString();
				sql.append(" and (w.endTime > :time or w.endTime is null or w.endTime = '')");
				map.put("time", time);
			}
			
			if(status.equals("4")){
				sql.append(" and w.checkStatus =:checkStatus");
				map.put("checkStatus",2);
				// 有效期判断
				String time = DateUtil.getSysDateTimeString();
				sql.append(" and (w.endTime > :time or w.endTime is null or w.endTime = '')");
				map.put("time", time);
			}
			
			if(status.equals("5")){
				// 有效期判断
				String time = DateUtil.getSysDateTimeString();
				sql.append(" and (w.endTime < :time or w.endTime is null or w.endTime = '')");
				map.put("time", time);
			}
			// 根据需求排序
			sql.append(" order by updateTime desc");
		    wftransportsTemp= super.list(page,sql.toString(), map);
		}
		
		
		return wftransportsTemp;
	}

	@Override
	@SuppressWarnings("unchecked")
	public int getWftransportCheckNum(int type) {
		  String time = DateUtil.getSysDateTimeString();
		String sql= "select count(1) from Wf_transport  where 1=1"
				+ " and type ="+type+" and check_Status = 0"
				+ " and (end_Time > "+time+" or end_Time is null or end_Time = '')";
		List<BigInteger> num = super.createSession().createSQLQuery(sql).list();
		return num.get(0).intValue();
	}
}
