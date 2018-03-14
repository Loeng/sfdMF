package com.ekfans.base.order.service;

import java.util.List;

import com.ekfans.base.order.model.ValuationCategory;
import com.ekfans.pub.util.Pager;


public interface IValuationCategoryService {
    /**
    * @Title: getValuationCategory
    * @Description: TODO(获取所有的分类数据)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @return List<ValuationCategory> 返回类型
    * @throws
     */
    public List<ValuationCategory> getValuationCategory();
    /**
    * @Title: getValuationCatrgoryById
    * @Description: TODO(这里用一句话描述这个方法的作用)
    * 详细业务流程:根据ID查询含量
    * (详细描述此方法相关的业务处理流程)
    * @param id
    * @return List<ValuationCategory> 返回类型
    * @throws
     */
    
    
    /**
     * 添加分类
     */
    public boolean add(ValuationCategory vc);
    
    /**
     * 删除
     * @param id
     */
    public boolean del(String id);
    
    /**
     * 修改
     * @param vc
     */
    public boolean update(ValuationCategory vc);
    
    /**
     * 根据id得到vc对象
     * * @param id
     * @return
     */
    public ValuationCategory getVCById(String id);
    
    /**
     * 分页查询分类
     * @param page 分页对象
     * @return
     */
    public List<ValuationCategory> getVCByPage(Pager page,String contentName);
    
}
