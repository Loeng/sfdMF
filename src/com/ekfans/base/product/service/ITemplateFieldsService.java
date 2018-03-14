package com.ekfans.base.product.service;

import java.util.List;

import com.ekfans.base.product.model.TemplateFields;
import com.ekfans.pub.util.Pager;

/**
 * 商品模板扩展字段实现Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
public interface ITemplateFieldsService {

    /**
     * 
    * @Title: listTemplate
    * @Description: 获取模板扩展字段列表
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param pager
    * @param fieldsName
    * @return List<TemplateFields> 返回类型
    * @throws
     */
    public List<TemplateFields> listTemplate(Pager pager,String fieldsName);
    
    /**
     * 
    * @Title: saveTempalteFields
    * @Description: 保存扩展字段
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param templateFields
    * @return boolean 返回类型
    * @throws
     */
    public boolean saveTempalteFields(TemplateFields templateFields);
    
    /**
     * 
    * @Title: deleteTempalteFields
    * @Description: 根据模板id删除扩展字段
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param tempId
    * @return boolean 返回类型
    * @throws
     */
    public boolean deleteTempalteFields(String tempId);
    
    /**
     * 根据id删除扩展字段
     * @param id
     * @return
     */
    public boolean deleteField(String id);
    
    /**
     * 
    * @Title: modifyTempalteFields
    * @Description: 修改扩展字段
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param templateFields
    * @return boolean 返回类型
    * @throws
     */
    public boolean modifyTempalteFields(TemplateFields templateFields);
    
    /**
     * 
    * @Title: getProductTempalteFieldsById
    * @Description: 根据模板id查找扩展字段列表
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param tempId
    * @return List<TemplateFields> 返回类型
    * @throws
     */
    public List<TemplateFields> getProductTempalteFieldsByTemplateId(String templateId);
    
    /**
     * 
    * @Title: listFieldsByTidAndCid
    * @Description:根据模板id和模板分类id查找扩展字段
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param tId
    * @param cId
    * @return List<TemplateFields> 返回类型
    * @throws
     */
    public List<TemplateFields> listFieldsByTidAndCid(String tId, String cId);
    /**
     * 
    * @Title: getProductTempalteFieldsByTemplateIdSetChecked
    * @Description: TODO 修改的时候显示出已经选择的模板
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param templateId
    * @param @return    设定文件
    * @return List<TemplateFields>    返回类型
    * @throws
     */
    public List<TemplateFields> getProductTempalteFieldsByTemplateIdSetChecked(String templateId);
}