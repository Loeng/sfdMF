package com.ekfans.controllers.system.store.VO;

import java.util.List;

import com.ekfans.base.store.model.LegalResume;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.model.StoreInfo;
import com.ekfans.base.user.model.User;

public class AttributeVO {

	private User user;// 用户
	private Store store;// 企业基础信息
	private StoreInfo si;// 企业详细信息
	private List<LegalResume> lrlist;// 法人简历

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public StoreInfo getSi() {
		return si;
	}

	public void setSi(StoreInfo si) {
		this.si = si;
	}

	public List<LegalResume> getLrlist() {
		return lrlist;
	}

	public void setLrlist(List<LegalResume> lrlist) {
		this.lrlist = lrlist;
	}
}
