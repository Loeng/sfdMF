package com.ekfans.base.store.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

@Entity
@SuppressWarnings("serial")
public class Wfpys extends BasicBean {
	private String name = "";// 名录名称
	private String descr = "";// 名录描述
	private String parentId;// 父ID
	private int position;
	private String fullPath = "";

	// -------临时数据，用于展示
	List<Wfpys> childs = new ArrayList<Wfpys>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	public List<Wfpys> getChilds() {
		return childs;
	}

	public void setChilds(List<Wfpys> childs) {
		this.childs = childs;
	}

	
}
