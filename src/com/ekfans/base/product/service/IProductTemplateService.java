package com.ekfans.base.product.service;

import java.util.List;

import com.ekfans.base.product.model.ProductTemplate;
import com.ekfans.pub.util.Pager;

/**
 * 商品模版实现Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author wsj
 * @date 2014-1-6
 * @version 1.0
 */
public interface IProductTemplateService {

	/**
	 * 
	 * @Title: listTemplate
	 * @Description: 查询模板列表 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param pager
	 * @param name
	 * @return List<ProductTemplate> 返回类型
	 * @throws
	 */
	public List<ProductTemplate> listTemplate(Pager pager, String name);

	/**
	 * 
	 * @Title: saveTempalte
	 * @Description: 保存模板 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param productTemplate
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean saveTempalte(ProductTemplate productTemplate);

	/**
	 * 
	 * @Title: deleteTemplate
	 * @Description: 删除模板 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param id
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean deleteTemplate(String id);

	/**
	 * 
	 * @Title: modifyTemplate
	 * @Description: 修改模板 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param productTemplate
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean modifyTemplate(ProductTemplate productTemplate);

	/**
	 * 
	 * @Title: getProductTemplateById
	 * @Description: 根据id查找模板 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param id
	 * @return ProductTemplate 返回类型
	 * @throws
	 */
	public ProductTemplate getProductTemplateById(String id);

	/**
	 * 
	 * @Title: getTemplateByName
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param name
	 * @return ProductTemplate 返回类型
	 * @throws
	 */
	public ProductTemplate getTemplateByName(String name);

	/**
	 * 
	 * @Title: getProductTemplateByTempLateId
	 * @Description: TODO 根据templateId取得productTempLate对象 详细业务流程:
	 *               (详细描述此方法相关的业务处理流程)
	 * @param @param templateId
	 * @param @return 设定文件
	 * @return ProductTemplate 返回类型
	 * @throws
	 */
	public ProductTemplate getProductTemplateByTempLateId(String templateId);
	/**
	* @Title: getProductTemplate
	* @Description: TODO(查询所有模板)
	* 详细业务流程:
	* (详细描述此方法相关的业务处理流程)
	* @return List<ProductTemplate> 返回类型
	* @throws
	 */
	public List<ProductTemplate> getProductTemplate();
}