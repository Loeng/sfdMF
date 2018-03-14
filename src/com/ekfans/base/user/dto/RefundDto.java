package com.ekfans.base.user.dto;

public class RefundDto {
        
       private String id;
       // 0:换货；1：退货
       private String type;
       // 0：等待卖家确认；1：退货/换货中；:2：退货/换货完成
       private String status;
       // 订单号
       private String orderId;
       // 产品名称
       private String productSortName;
       
       private String createTime;
       // 物流单号
       private String logisticsNo;
       
       private String productId;
       
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }
        public String getStatus() {
            return status;
        }
        public void setStatus(String status) {
            this.status = status;
        }
        public String getOrderId() {
            return orderId;
        }
        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }
        public String getProductSortName() {
            return productSortName;
        }
        public void setProductSortName(String productSortName) {
            this.productSortName = productSortName;
        }
        public String getCreateTime() {
            return createTime;
        }
        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
        public String getLogisticsNo() {
            return logisticsNo;
        }
        public void setLogisticsNo(String logisticsNo) {
            this.logisticsNo = logisticsNo;
        }
        public String getProductId() {
            return productId;
        }
        public void setProductId(String productId) {
            this.productId = productId;
        }
}
