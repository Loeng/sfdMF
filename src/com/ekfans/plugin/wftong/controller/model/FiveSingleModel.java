package com.ekfans.plugin.wftong.controller.model;

/**
 * 上传五联单model
 * @author 成都易科远见有限公司
 *
 */
public class FiveSingleModel {
	// 运输id
	private String transId;
	// 载重量
	private double carWeight;
	// base64图片
	private String pic;

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public double getCarWeight() {
		return carWeight;
	}

	public void setCarWeight(double carWeight) {
		this.carWeight = carWeight;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

}
