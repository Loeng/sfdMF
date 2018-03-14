package com.ekfans.base.order.model;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import com.ekfans.base.store.model.Consult;
import com.ekfans.basic.hibernate.model.BasicBean;
/**
 * 
* @ClassName: Appraise
* @Description: TODO(评价表)
* @author 成都易科远见科技有限公司 
* @date 2014-5-13 下午03:51:32
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Entity
public class Appraise extends BasicBean {
       // 序列化
       private static final long serialVersionUID = 1L;
       
       // 订单id
       private String orderId = "";
       
       // 商品id
       private String productId = "";
       
       // 评价人
       private String source= "";
       
       // 被评价
       private String target = "";
       
       // 类型：1，好评，2，中评，3差评
       private String type = "";
       
       //是否　回复 
       private boolean replyStatus = false;
       
       // 评价时间
       private String createTime = "";
       
       // 备注 具体评价的内容
       private String note = "";
       
       // 父级id
       private String parentId = "";
       
       // 审核人
       private String checkMan = "";
       
       // 审核时间
       private String checkTime = "";
       
       // 审核状态
       private int checkStatus = 0;
              
       // 审核说明
       private String checkNote = "";
       
       //订单详情id
       private String orderDetailId = "";
       
       //=====虚拟数据======
       // 商品小图
       private String smallPicture = "";
       //评价人的图片
       private String headPhoto = "";
       //评价人的姓名
       private String sourceName = "";
       // 商品描述
       private String name = "";
       
       //虚拟数据
       private List<Consult> childList = new ArrayList<Consult>(); 
       
       
        public String getHeadPhoto() {
        return headPhoto;
    }

    public void setHeadPhoto(String headPhoto) {
        this.headPhoto = headPhoto;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

        public List<Consult> getChildList()
        {
            return childList;
        }
    
        public void setChildList(List<Consult> childList)
        {
            this.childList = childList;
        }

	public String getOrderId() {
            return orderId;
        }
        
        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }
        
        public String getProductId() {
            return productId;
        }
        
        public void setProductId(String productId) {
            this.productId = productId;
        }
        
        public String getSource() {
            return source;
        }
        
        public void setSource(String source) {
            this.source = source;
        }
        
        public String getTarget() {
            return target;
        }
        
        public void setTarget(String target) {
            this.target = target;
        }
        
        public String getType() {
            return type;
        }
        
        public void setType(String type) {
            this.type = type;
        }
        
        public String getCreateTime() {
            return createTime;
        }
        
        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
        
        public String getNote() {
            return note;
        }
        
        public void setNote(String note) {
            this.note = note;
        }
        
        public String getParentId() {
            return parentId;
        }
        
        public void setParentId(String parentId) {
            this.parentId = parentId;
        }
        
        public static long getSerialversionuid() {
            return serialVersionUID;
        }
        
        public boolean isReplyStatus() {
            return replyStatus;
        }
        
        public void setReplyStatus(boolean replyStatus) {
            this.replyStatus = replyStatus;
        }

	public String getSmallPicture()
	{
	    return smallPicture;
	}

	public void setSmallPicture(String smallPicture)
	{
	    this.smallPicture = smallPicture;
	}

	public String getName()
	{
	    return name;
	}

	public void setName(String name)
	{
	    this.name = name;
	}

	public String getCheckMan() {
	    return checkMan;
	}

	public void setCheckMan(String checkMan) {
	    this.checkMan = checkMan;
	}

	public String getCheckTime() {
	    return checkTime;
	}

	public void setCheckTime(String checkTime) {
	    this.checkTime = checkTime;
	}

	public int getCheckStatus() {
	    return checkStatus;
	}

	public void setCheckStatus(int checkStatus) {
	    this.checkStatus = checkStatus;
	}

	public String getCheckNote() {
	    return checkNote;
	}

	public void setCheckNote(String checkNote) {
	    this.checkNote = checkNote;
	}

    public String getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(String orderDetailId) {
        this.orderDetailId = orderDetailId;
    }
	
}
