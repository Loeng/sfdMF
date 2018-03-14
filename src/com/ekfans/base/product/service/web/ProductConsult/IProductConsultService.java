package com.ekfans.base.product.service.web.ProductConsult;

import java.util.List;

import com.ekfans.base.store.model.Consult;
import com.ekfans.pub.util.Pager;

/**
 * 
* @ClassName: IProductConsultService
* @Description: TODO(前台-商品咨询)
* @author HJC
* @date 2014-6-11 上午10:29:20
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IProductConsultService {
    /**
     * 
    * @Title: getProConsultByCondition
    * @Description: TODO(根据条件查询出满足条件的咨询)
    * 详细业务流程:
    * (一:每一条咨询涉及的信息有:咨询人(网友),咨询时间,咨询内容,客服回复
    *  二:咨询的条件涉及有,搜索条件(模糊查询匹配咨询的问题);分页条件,商品详细页默认显示6条咨询;
    *     咨询按时间降序排序)
    * @param @return    设定文件
    * @return List<Consult>    返回类型
    * @throws
     */
    public List<Consult> getProConsultByCondition(String proId,String consultContent,String type,Pager pager);
    
    /**
     * 
    * @Title: addProductConsult
    * @Description: TODO(新增一条咨询)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param consultType
    * @param @param consultContent
    * @param @param proId
    * @param @param storeId
    * @param @param userId
    * @param @return    设定文件
    * @return boolean    返回类型
    * @throws
     */
    public boolean addProductConsult(String consultType,String consultContent,String proId,String userId);
}
