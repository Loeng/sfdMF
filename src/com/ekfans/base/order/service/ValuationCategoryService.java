package com.ekfans.base.order.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.order.dao.IValuationCategoryDao;
import com.ekfans.base.order.model.ValuationCategory;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;
@Service
public class ValuationCategoryService implements IValuationCategoryService{
    
    @Autowired
    private IValuationCategoryDao valuationCategoryDao;
    /**
     * 获取所有的分类
     */
    @Override
    public List<ValuationCategory> getValuationCategory() {
        StringBuffer buffer = new StringBuffer("from ValuationCategory");
        try {
            return valuationCategoryDao.list(buffer.toString(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	@Override
	public boolean add(ValuationCategory vc) {
		try {
			if(vc!=null){
				vc.setCreateTime(DateUtil.getSysDateTimeString());
				valuationCategoryDao.addBean(vc);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	@Override
	public boolean del(String id) {
		try {
			if(!StringUtil.isEmpty(id)){
				valuationCategoryDao.deleteById(id);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	@Override
	public boolean update(ValuationCategory vc) {
		try {
			if(vc!=null){
				valuationCategoryDao.updateBean(vc);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	@Override
	public List<ValuationCategory> getVCByPage(Pager page,String contentName) {
		StringBuffer hql=new StringBuffer(" from ValuationCategory vc where 1=1 ");
		Map<String, Object> map=new HashMap<String, Object>();
		if(!StringUtil.isEmpty(contentName)){
			hql.append("and vc.specName like :contentName");
			map.put("contentName", "%"+contentName+"%");
		}
//		if(!StringUtil.isEmpty(startTime)){
//			hql.append("and vc.createTime >= :startTime");
//			map.put("startTime", startTime);
//		}
//		if(!StringUtil.isEmpty(endTime)){
//			hql.append("and vc.createTime <= :endTime");
//			map.put("endTime", endTime);
//		}
		
		hql.append(" order by vc.createTime desc");
		try {
			return valuationCategoryDao.list(page, hql.toString(), map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public ValuationCategory getVCById(String id) {
		try{
			if(!StringUtil.isEmpty(id)){
				return (ValuationCategory) valuationCategoryDao.get(id);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
