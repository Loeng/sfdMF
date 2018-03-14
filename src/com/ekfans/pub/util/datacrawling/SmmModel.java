package com.ekfans.pub.util.datacrawling;

/**
 * 上海有色网有色金属信息模型
 * @author pp
 * @date 2017年11月8日09:16:50
 */
public class SmmModel {
    
	// 上海有色金属品类ID
	private String smmId = "";
	// 上海有色金属品类名
	private String smmName = "";
	// 均价
    private String price = "--";
	// 价格区间：起始价
	private String startPrice = "--";
	// 价格区间：结束价
	private String endPrice = "--";
	// 涨跌金额
	private String riseAndDrop = "--";
	// 地区
	private String area = "";
	// 单位
	private String unit = "";
	// 日期
	private String date = "";
    // 规格
	private String spec = "";
	
	public String getSpec() {
		return spec;
	}


	public void setSpec(String spec) {
		this.spec = spec;
	}


	public String getSmmId() {
		return smmId;
	}


	public void setSmmId(String smmId) {
		this.smmId = smmId;
	}


	public String getSmmName() {
		return smmName;
	}


	public void setSmmName(String smmName) {
		this.smmName = smmName;
	}


	public String getPrice() {
		return price;
	}


	public void setPrice(String price) {
		this.price = price;
	}


	public String getStartPrice() {
		return startPrice;
	}


	public void setStartPrice(String startPrice) {
		this.startPrice = startPrice;
	}


	public String getEndPrice() {
		return endPrice;
	}


	public void setEndPrice(String endPrice) {
		this.endPrice = endPrice;
	}

    
    public String getRiseAndDrop() {
		return riseAndDrop;
	}


	public void setRiseAndDrop(String riseAndDrop) {
		this.riseAndDrop = riseAndDrop;
	}


	public String getArea() {
		return area;
	}


	public void setArea(String area) {
		this.area = area;
	}


	public String getUnit() {
		return unit;
	}


	public void setUnit(String unit) {
		this.unit = unit;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	@Override
	public String toString() {
		return "SmmModel [smmId=" + smmId + ", smmName=" + smmName + ", price=" + price + ", startPrice=" + startPrice
				+ ", endPrice=" + endPrice + ", riseAndDrop=" + riseAndDrop + ", area=" + area + ", unit=" + unit
				+ ", date=" + date + ", spec=" + spec + "]";
	}
    
	
}
