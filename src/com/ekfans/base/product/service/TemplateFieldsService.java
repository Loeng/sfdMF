package com.ekfans.base.product.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.product.dao.ITemplateFieldsDao;
import com.ekfans.base.product.model.TemplateFields;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 商品模板扩展字段业务实现Service
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
@Service
public class TemplateFieldsService implements ITemplateFieldsService {
	// 定义DAO
	@Autowired
	private ITemplateFieldsDao templateFieldsDao;

	// 根据Id查找
	public boolean checkId(String tempId) {
		try {
			if (templateFieldsDao.get(tempId) != null) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// 根据Id删除对象
	public boolean deleteTempalteFields(String tempId) {
		if (StringUtil.isEmpty(tempId)) {
			return false;
		}
		// 定义sql语句
		StringBuffer sql = new StringBuffer(" select tf from TemplateFields as tf where 1=1 ");
		// 定义Map函数
		Map<String, Object> hashMap = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(tempId)) {
			sql.append(" and tf.tempId = :tempId");
			hashMap.put("tempId", tempId);
		}
		sql.append(" order by tf.position asc");
		try {
			templateFieldsDao.delete(sql.toString(), hashMap);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// -------------------------------wsj--------------------------
	// 根据ID查询
	@SuppressWarnings("unchecked")
	public List<TemplateFields> getProductTempalteFieldsByTemplateId(String templateId) {
		if (StringUtil.isEmpty(templateId)) {
			return null;
		}
		// 定义sql语句
		StringBuffer sql = new StringBuffer(" select tf.id,tf.tempId,tf.search,tf.fieldValue,tf.fieldName,tf.commons,tf.position,tf.fieldCategory,tf.fieldType from TemplateFields as tf where 1=1 ");

		// 定义Map函数
		Map<String, Object> hashMap = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(templateId)) {
			sql.append(" and tf.tempId = :templateId");
			hashMap.put("templateId", templateId);
		}
		sql.append(" order by tf.position asc");
		try {
			List<Object[]> list = templateFieldsDao.list(sql.toString(), hashMap);
			List<TemplateFields> templateFields = new ArrayList<TemplateFields>();

			for (Object[] objects : list) {
				TemplateFields template = new TemplateFields();
				template.setId((String) objects[0]);
				template.setTempId((String) objects[1]);
				template.setSearch((Boolean) objects[2]);

				// template.setFieldValue((String)objects[3]);
				String fieldValue = (String) objects[3];
				template.setFieldValue(fieldValue);

				List<String> fieldValueList = new ArrayList<String>();
				if (!StringUtil.isEmpty(fieldValue)) {
					// 将模板详细字段值拆分成一个字符串数组 并存入集合
					String[] fieldValueArray = fieldValue.split(";");
					for (int h = 0; h < fieldValueArray.length; h++) {
						fieldValueList.add(fieldValueArray[h]);
					}
				}

				template.setFieldValueList(fieldValueList);
				template.setFieldName((String) objects[4]);
				template.setCommons((Boolean) objects[5]);
				template.setPosition((Integer) objects[6]);
				template.setFieldCategory((String) objects[7]);
				template.setFieldType((String) objects[8]);
				templateFields.add(template);
			}
			return templateFields;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// //定义修改时的查询出已选择的模板扩展字段
	// public List<TemplateFields>
	// getProductTempalteFieldsByTemplateIdSetChecked(String templateId) {
	// List<TemplateFields> temp = new ArrayList<TemplateFields>();
	// //定义sql语句
	// StringBuffer sql = new
	// StringBuffer(" select tf.fieldName,tf.fieldValue from TemplateFields as tf where 1=1 ");
	// //定义Map函数
	// Map<String, Object> map = new HashMap<String, Object>();
	// if (!StringUtil.isEmpty(templateId)) {
	// sql.append(" and tf.tempId = :templateId");
	// map.put("templateId", templateId);
	// }
	// sql.append(" order by tf.position asc");
	//
	// // StringBuffer sql2 = new
	// StringBuffer("select pi.infoValue,pId.infoPic1,pId.infoPic2,pId.infoPic3,");
	// //
	// sql2.append("pId.infoPic4 from ProductInfo as pi,ProductInfoDetail as pId,Product as p where 1=1 ");
	// // sql2.append(" and pId.productId = p.id");
	// // sql2.append(" and pi.productId = p.id");
	// StringBuffer sql2 = new
	// StringBuffer("select pi.infoValue,pi.infoName from ProductInfo as pi,TemplateFields as tf where 1=1");
	// sql2.append(" and pi.templateFieldId = tf.id");
	// try{
	// List<Object[]> templateFields =
	// templateFieldsDao.list(sql.toString(),map);
	// List<Object[]> productInfos = productInfoDao.list(sql2.toString(),map);
	// for(int i = 0;i < templateFields.size();i++){
	// /* String configNameBig = templateFields.get(i)[0].toString();
	// String configValueBig = templateFields.get(i)[1].toString();
	//
	// String configNameMin = productInfos.get(i)[0].toString();
	// String configValueMin = productInfos.get(i)[1].toString();*/
	//
	// if(templateFields.size()==productInfos.size()){
	// String[] templateArray = templateFields.get(i)[1].toString().split(";");
	// String[] pInfo = productInfos.get(i)[1].toString().split(";");
	// for(int j = 0;j < templateArray.length;j++){
	// TemplateFields tempEntityFields = new TemplateFields();
	// tempEntityFields.setFieldName(templateFields.get(i)[0].toString());
	// tempEntityFields.setFieldValue(templateArray[j]);
	//
	// if(ArrayUtils.contains(pInfo,templateArray[i])){
	// tempEntityFields.setChecked(true);
	// }else{
	// tempEntityFields.setChecked(false);
	// }
	// temp.add(tempEntityFields);
	// }
	// }else{
	//
	// }
	//
	//
	//
	//
	//
	// /* for(Object[] tInfo : productInfos){
	// String configNameMin = tInfo[0].toString();
	// String configValueMin = tInfo[1].toString();
	// if(configNameBig.equals(configNameMin)){
	// for(String str : configValueMin.split(";")){
	//
	// }
	//
	// configValueBig.split(";");
	// }
	// }*/
	// }
	//
	//
	//
	// /*
	// for(int i = 0;i < templateFields.size();i++){
	// for(String template : templateFields.get(i)[0].toString().split(";")){
	// TemplateFields tempEntityFields = new TemplateFields();
	// for(String pInfo : productInfos.get(i)[0].toString().split(";")){
	// if(template.equals(pInfo)){
	// tempEntityFields.setChecked(true);
	// break;
	//
	// }
	// }
	// tempEntityFields.setChecked(false);
	//
	// }
	// }*/
	// return temp;
	// }catch(Exception e){
	// e.printStackTrace();
	// }
	// return null;
	// }

	@SuppressWarnings("unchecked")
	public List<TemplateFields> getProductTempalteFieldsByTemplateIdSetChecked(String productId) {
		List<TemplateFields> temp = new ArrayList<TemplateFields>();

		StringBuffer sql = new StringBuffer("select tf.fieldName,tf.fieldValue,pi.infoValue from TemplateFields as tf,ProductInfo as pi,");
		sql.append("Product as p where pi.templateFieldId=tf.id and tf.tempId=p.templateId and pi.productId=p.id and p.id=:productId");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("productId", productId);
		sql.append(" order by tf.position asc");
		try {
			List<Object[]> objList = templateFieldsDao.list(sql.toString(), map);
			// List<TemplateFields> tempList = new ArrayList<TemplateFields>();
			int judgment = 1;
			for (Object[] objects : objList) {
				String fieldName = objects[0].toString();
				String[] all = objects[1].toString().split(";");
				String[] checkStr = objects[2].toString().split(";");
				for (String str : all) {
					TemplateFields tempEntityFields = new TemplateFields();
					tempEntityFields.setFieldName(fieldName);
					tempEntityFields.setFieldValue(str);
					tempEntityFields.setIftype(judgment);
					if (ArrayUtils.contains(checkStr, str)) {
						tempEntityFields.setChecked(true);
					} else {
						tempEntityFields.setChecked(false);
					}
					temp.add(tempEntityFields);
				}
				++judgment;
			}
			return temp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public List<TemplateFields> listTemplate(Pager pager, String fieldsName) {
		// TODO Auto-generated method stub
		return null;
	}

	// 修改模板扩张类
	public boolean modifyTempalteFields(TemplateFields templateFields) {
		try {
			templateFieldsDao.updateBean(templateFields);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// 保存模板
	public boolean saveTempalteFields(TemplateFields templateFields) {
		if (templateFields == null) {
			return false;
		}
		try {
			templateFieldsDao.addBean(templateFields);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 得到根据模板id和分类id扩展字段
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TemplateFields> listFieldsByTidAndCid(String tId, String cId) {
		if (StringUtil.isEmpty(tId) || StringUtil.isEmpty(cId)) {
			return null;
		}
		// 定义sql语句
		StringBuffer sql = new StringBuffer(" select tf from TemplateFields as tf where 1=1 ");
		// 定义Map函数
		Map<String, Object> hashMap = new HashMap<String, Object>();
		// 关联模板
		sql.append(" and tf.tempId = :tempId");
		hashMap.put("tempId", tId);
		// 关联分类
		sql.append(" and tf.fieldCategory = :cId");
		hashMap.put("cId", cId);

		sql.append(" order by tf.position asc");
		try {
			List<TemplateFields> list = templateFieldsDao.list(sql.toString(), hashMap);
			return list;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public boolean deleteField(String id) {
		// 如果id为空返回false
		if (StringUtil.isEmpty(id)) {
			return false;
		}
		try {
			// 根据id删除扩展字段
			templateFieldsDao.deleteById(id);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}