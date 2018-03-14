package com.ekfans.controllers.store.auth.VO;

import java.util.List;

import com.ekfans.base.store.model.StoreLegal;
import com.ekfans.base.store.model.StoreLegalResume;

public class AttributeVO {

	private StoreLegal sl; // 法人信息
	private List<StoreLegalResume> slrlist; // 法人简历
	
	public StoreLegal getSl() {
		return sl;
	}

	public void setSl(StoreLegal sl) {
		this.sl = sl;
	}

	public List<StoreLegalResume> getSlrlist() {
		return slrlist;
	}

	public void setSlrlist(List<StoreLegalResume> slrlist) {
		this.slrlist = slrlist;
	}

}
