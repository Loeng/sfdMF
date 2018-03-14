package com.ekfans.controllers.web.vo;

import java.util.List;

/**
 * 
* @ClassName: ProListTemplateVO
* @Description: TODO(前台-商品列表-商品所属模板的显示实体)
* @author 成都易科远见科技有限公司
* @date 2014-5-20 上午09:38:08
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class ProListTemplateVO {
    // 扩展字段名
    private String fieldName = "";
    // 模板显示位置
    private String position= "";
    // 拆分后的模板值
    private List<String> fieldValueList;
    
    public String getFieldName() {
        return fieldName;
    }
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    public List<String> getFieldValueList() {
        return fieldValueList;
    }
    public void setFieldValueList(List<String> fieldValueList) {
        this.fieldValueList = fieldValueList;
    }
    public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }
	@Override
	public String toString() {
		return "ProListTemplateVO [fieldName=" + fieldName + ", position="
				+ position + ", fieldValueList=" + fieldValueList + "]";
	}
    
    
}
