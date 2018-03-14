package com.ekfans.base.order.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

@Entity
@SuppressWarnings("serial")
public class Wfpml extends BasicBean {
	private String name = "";// 名录名称
	private String no = "";// 名录代码
	private String parentId;// 父ID
	private String note = "";// 说明
	private int position;
	private String fullPath = "";
	private String status = "1";// 状态
	private String show_sub = "0";// 显示状态
	private String identity = "";

	// -------临时数据，用于展示
	List<Wfpml> childs = new ArrayList<Wfpml>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getShow_sub() {
		return show_sub;
	}

	public void setShow_sub(String show_sub) {
		this.show_sub = show_sub;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public List<Wfpml> getChilds() {
		return childs;
	}

	public void setChilds(List<Wfpml> childs) {
		this.childs = childs;
	}
}
