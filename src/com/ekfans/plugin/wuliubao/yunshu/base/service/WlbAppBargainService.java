package com.ekfans.plugin.wuliubao.yunshu.base.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.order.dao.IInquiryDao;
import com.ekfans.base.order.model.Bargain;
import com.ekfans.base.order.model.Inquiry;
import com.ekfans.base.store.dao.IWftransportDao;
import com.ekfans.base.store.model.Wftransport;
import com.ekfans.base.user.model.User;
import com.ekfans.plugin.wuliubao.yunshu.base.dao.IWlbAppBargainDao;
import com.ekfans.plugin.wuliubao.yunshu.base.dao.IWlbAppUserDao;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.AddBargainRemarksForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.BargainingForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.CarSource;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.HuoResponse;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.UserBargain;
import com.ekfans.plugin.wuliubao.yunshu.controller.util.BaiduMapUtil;
import com.ekfans.plugin.wuliubao.yunshu.controller.util.JGUtil;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;
@Service
public class WlbAppBargainService implements IWlbAppBargainService{
	// 定义错误日志
	public static Logger log = LoggerFactory.getLogger(WlbAppBargainService.class);
	@Autowired
	private IWftransportDao dao;
	@Autowired
	private IWlbAppBargainDao bargainDao;
	@Autowired
	private IInquiryDao inquiryDao;
	@Autowired
	private IWlbAppUserDao userdao;
	@SuppressWarnings("unused")
	@Override
	public String sendBargain(BargainingForm bar) throws Exception {
		Session session = null;
		Transaction tran = null;
		if(StringUtil.isEmpty(bar.sourceId)){
			return "0";
		}
		if(StringUtil.isEmpty(bar.contactMan)){
			return "2";
		}
		if(StringUtil.isEmpty(bar.contactPhone)){
			return "3";
		}
        if(StringUtil.isEmpty(bar.sellerId)){
			return "8";
		}
		if(StringUtil.isEmpty(bar.buyerId)){
			return "9";
		}
		//获取车货源
		Wftransport w = (Wftransport) dao.get(bar.sourceId);
        if(w==null){
			return "10";
		}
		if(bar.buyerId.equals(bar.sellerId)||w.getStoreId().equals(bar.buyerId)){
			return "12";
		}
		if(w.getBarCount()==null){
			w.setBarCount(1);
		}else{
			w.setBarCount(w.getBarCount()+1);
		}
		try {
		 //如果议价商品是车源
		 if(w.getType()==0){
			 if(StringUtil.isEmpty(bar.productName)){
					return "6";
			}
			//根据议价人id以及商品ID获取议价信息
			List<Bargain> list = bargainDao.getBargain(bar.buyerId,null,bar.sourceId,null);
			if(list!=null&&list.size()>0){
				return "11";
			}
			
			//保存议价信息
			Bargain b = new Bargain();
			b.setId(UUID.randomUUID().toString().toUpperCase().replaceAll("-", ""));
			b.setSourceId(bar.sourceId);
			b.setBuyerId(bar.buyerId);
			b.setSellerId(bar.sellerId);
			b.setProductName(bar.productName);
			b.setContactMan(bar.contactMan);
		    b.setContactPhone(bar.contactPhone);
			b.setType("1");
			b.setStatus("0");
			b.setStartFullPath(StringUtil.arrayToString(bar.startFullPaths, ","));
			b.setEndFullPath(StringUtil.arrayToString(bar.endFullPaths, ","));
			b.setCreateTime(DateUtil.getSysDateTimeString());
			b.setUpdateTime(DateUtil.getSysDateTimeString());
			bargainDao.addBean(b);
			dao.updateBean(w);
		   }
		//如果议价商品是货源
	    if(w.getType()==1){
		    if(StringUtil.isEmpty(bar.price)){
				return "7";
			}
		    if(StringUtil.isEmpty(bar.priceUnit)){
				return "14";
			}
			List<Inquiry> iList = inquiryDao.getBargain(bar.buyerId, null, bar.sourceId, null);
			if(iList!=null&&iList.size()>0){
				return "11";
			}
			Inquiry in = new Inquiry();
			in.setSellersId(bar.sellerId);
			in.setBuyersId(bar.buyerId);
			in.setProductId(bar.sourceId);
			in.setFinalPrice(new BigDecimal(bar.price));
			in.setUnit(bar.priceUnit);
			in.setType(0);
			in.setStatus("0");
			in.setSourceType(2);
			in.setLinkPerson(bar.contactMan);
			in.setLinkTel(bar.contactPhone);
			in.setCreateTime(DateUtil.getSysDateTimeString());
			in.setUpdateTime(DateUtil.getSysDateTimeString());
			inquiryDao.addBean(in);
			dao.updateBean(w);
		 }
	    User user = (User) userdao.get(bar.sellerId);
	    if(user==null){
	    	return "13";
	    }
		//极光推送议价消息
        if(!StringUtil.isEmpty(user.getRegistrationID())){
        	Map<String,String> dataNode = new HashMap<String,String>();
    		//messageType:"1",为新的对我议价信息
    		dataNode.put("messageType", "1");
    		dataNode.put("sourceId", bar.sourceId);
    		JGUtil.wlbAPPsendMessages(user.getRegistrationID(),dataNode,"您有一条新的议价消息!");
    		return "1";
		}
		return "1";
		}catch (NumberFormatException e) {
			throw new NumberFormatException();
		} catch (Exception e) {
			// 回滚
		    if (tran != null) {
		      tran.rollback();
			}
			if (session != null && session.isOpen()) {
				session.close();
			}
			// 记日志
			log.error(e.getMessage());
			throw new Exception();
		   } finally {}
        
	}
	@Override
	public void deleteBargain(String bargainID) throws Exception {
		bargainDao.delete(bargainID);
		
	}
	@Override
	public List<UserBargain> getUser(HttpServletRequest request, String userType, String bargainType,String sellerContactState,Pager page,String latitude,String longitude) throws Exception {
		if(StringUtil.isEmpty(userType)||StringUtil.isEmpty(bargainType)||StringUtil.isEmpty(sellerContactState)){
			return null;
		}
		//获取用户id
		String token = request.getHeader("WLB-Token");
		String userid = token.split("_")[0];
		List<UserBargain> userBargainlist = new ArrayList<UserBargain>();
		//运输端
		if(userType.equals("1")){
			//我的议价
			if(bargainType.equals("1")){
				//获取用户议价信息
				List<Inquiry> ilist = inquiryDao.getBargain(userid,null,null,page);
				if(ilist==null){
					return userBargainlist;
				}
				for(Inquiry i:ilist){
					//获取用户议价商品
					Wftransport w = (Wftransport) dao.get(i.getProductId());
					if(w==null){
						continue;
					}
					//包装货源信息
					HuoResponse huo = new HuoResponse(w);
					//包装用户议价信息
					UserBargain userBargain = new UserBargain(i,huo,bargainType);
					try {
						userBargain.setCommodityDistance(BaiduMapUtil.getCommodityDistance(w.getStartFullPath(), w.getEndFullPath())+"");
						if(!StringUtil.isEmpty(latitude)&&!StringUtil.isEmpty(longitude)){
							userBargain.setDistanceMe(BaiduMapUtil.getDistanceMe(w.getStartFullPath(),latitude, longitude)+"");
						}
					} catch (Exception e) {}
							if(sellerContactState.equals("0")){
								if(userBargain.getSellerContactState().equals("0")){
								  userBargainlist.add(userBargain);
								}
							}else if(sellerContactState.equals("1")){
								   if(userBargain.getSellerContactState().equals("1")){
								     userBargainlist.add(userBargain);
								   }
							}else{
								  userBargainlist.add(userBargain);
							}
						
				}
				return userBargainlist;
			}
			//对我议价
            if(bargainType.equals("2")){
            	//获取用户议价信息
				List<Bargain> blist = bargainDao.getBargain(null,userid,null,page);
				if(blist==null){
					return userBargainlist;
				}
				for(Bargain b:blist){
				    //获取用户议价商品
					Wftransport w = (Wftransport) dao.get(b.getSourceId());
					if(w==null){
						continue;
					}
					//包装车源信息
					CarSource car = new CarSource(w);
					//包装用户议价信息
					UserBargain userBargain = new UserBargain(b,car,bargainType);
							if(sellerContactState.equals("0")){
								if(userBargain.getSellerContactState().equals("0")){
								  userBargainlist.add(userBargain);
								  }
							}else if(sellerContactState.equals("1")){
								   if(userBargain.getSellerContactState().equals("1")){
								     userBargainlist.add(userBargain);
								   }
							}else{
								   userBargainlist.add(userBargain);
							}
					
				}
				return userBargainlist;
			}
		}
		//产生端
		if(userType.equals("2")){
			//我的议价
			if(bargainType.equals("1")){
				//获取用户议价信息
				List<Bargain> blist = bargainDao.getBargain(userid,null,null,page);
				if(blist==null){
					return userBargainlist;
				}
				for(Bargain b:blist){
					//获取用户议价商品
					Wftransport w = (Wftransport) dao.get(b.getSourceId());
					if(w==null){
						continue;
					}
					//包装车源信息
					CarSource huo = new CarSource(w);
					//包装用户议价信息
					//如果传来联系状态为0则代表未联系
					UserBargain userBargain = new UserBargain(b,huo,bargainType);
					
							if(sellerContactState.equals("0")){
								if(userBargain.getSellerContactState().equals("0")){
								  userBargainlist.add(userBargain);
								  }
							}else if(sellerContactState.equals("1")){
								   if(userBargain.getSellerContactState().equals("1")){
								     userBargainlist.add(userBargain);
								   }
							}else{
								    userBargainlist.add(userBargain);
							}
					
				}
				return userBargainlist;
			}
			//对我议价
			if(bargainType.equals("2")){
				//获取用户议价信息
				List<Inquiry> ilist = inquiryDao.getBargain(null,userid,null,page);
				if(ilist==null){
					return userBargainlist;
				}
				for(Inquiry i:ilist){
					//获取用户议价商品
					Wftransport w = (Wftransport) dao.get(i.getProductId());
					if(w==null){
						continue;
					}
					//包装货源信息
					HuoResponse huo = new HuoResponse(w);
					//包装用户议价信息
					//如果传来联系状态为0则代表未联系
					UserBargain userBargain = new UserBargain(i,huo,bargainType);
					
							if(sellerContactState.equals("0")){
								   if(userBargain.getSellerContactState().equals("0")){
								     userBargainlist.add(userBargain);
								   }
								}else if(sellerContactState.equals("1")){
									   if(userBargain.getSellerContactState().equals("1")){
									     userBargainlist.add(userBargain);
									   }
								}else{
									   userBargainlist.add(userBargain);
								
							}
					}
				
				return userBargainlist;
			}
		}
		return null;
	}
	@Override
	public String addUserBargainRemarks(AddBargainRemarksForm remarks,String userId) throws Exception {
		if(StringUtil.isEmpty(remarks.bargainId)){
			return "3";
		}
		//获取用户议价信息
	    Bargain bt = (Bargain) bargainDao.get(remarks.bargainId);
	    Inquiry in = (Inquiry) inquiryDao.get(remarks.bargainId);
	    if(bt==null&&in==null){
	    	return "3";
	    }
	    if(bt!=null){
	     if(remarks.bargainType.equals("1")&&bt.getBuyerId().equals(userId)){
	    	bt.setBuyerRemarks(remarks.userRemarks);
	    	bargainDao.updateBean(bt);
	    	return "1";
	     }
         if(remarks.bargainType.equals("2")&&bt.getSellerId().equals(userId)){
        	bt.setSellerRemarks(remarks.userRemarks);
        	bargainDao.updateBean(bt);
        	return "1";
	     }
        }
	    if(in!=null){
		 if(remarks.bargainType.equals("1")&&in.getBuyersId().equals(userId)){
			 in.setBuyerRemarks(remarks.userRemarks);
			 inquiryDao.updateBean(in);
		     return "1";
		 }
	     if(remarks.bargainType.equals("2")&&in.getSellersId().equals(userId)){
	    	 in.setSellerRemarks(remarks.userRemarks);
	    	 inquiryDao.updateBean(in);
	         return "1";
		 }
	    }
		return "2";
	}
	@Override
	public String addSellerContactState(String bargainId, String userId) throws Exception {
		if(StringUtil.isEmpty(bargainId)){
			return "3";
		}
		//获取用户议价信息
	    Bargain bt = (Bargain) bargainDao.get(bargainId);
	    Inquiry in = (Inquiry) inquiryDao.get(bargainId);
	    if(bt==null&&in==null){
	    	return "3";
	    }
	    if(bt!=null){
	    	if(!bt.getSellerId().equals(userId)){
		    	return "2";
		    }
		    bt.setSellerContactState("1");
		    bargainDao.updateBean(bt);
			return "1";
	    }
	    if(in!=null){
	    	if(!in.getSellersId().equals(userId)){
		    	return "2";
		    }
	    	in.setSellerContactState("1");
	    	inquiryDao.updateBean(in);
			return "1";
	    }
		return "0";
	}
	@Override
	public Map<String,Object> getBargain(HttpServletRequest request, String wftransportIds, String bargainType,
			String sellerContactState,Pager page,String latitude,String longitude) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		if(StringUtil.isEmpty(wftransportIds)||StringUtil.isEmpty(bargainType)||StringUtil.isEmpty(sellerContactState)
				||!bargainType.equals("1")&&!bargainType.equals("2")){
			//如果传来的[wftransportIds][bargainType][sellerContactState] 参数不正确 statusCode 返回 0
			map.put("statusCode","0");
			map.put("Pager", page);
			map.put("userBargainList", new ArrayList<>());
			return map;
		}
		//获取用户id
		String token = request.getHeader("WLB-Token");
		String userid = token.split("_")[0];
		List<UserBargain> userBargainlist = new ArrayList<UserBargain>();
		User user = (User) userdao.get(userid);
		if(user==null){
			//如果获取的用户为空 statusCode 返回 2
			map.put("statusCode","2");
			map.put("Pager", page);
			map.put("userBargainList", new ArrayList<>());
			return map;
		}
		//获取用户议价商品
		Wftransport w = (Wftransport) dao.get(wftransportIds);
		if(w==null){
			//如果获取的车货源为空 statusCode 返回 3
			map.put("statusCode","3");
			map.put("Pager", page);
			map.put("userBargainList", new ArrayList<>());
			return map;
		}
		//我的议价
		if(bargainType.equals("1")){
		  //如果议价的商品是车源
		  if(w.getType()==0){
		     //获取用户议价信息
		     List<Bargain> blist = bargainDao.getBargain(userid,null,wftransportIds,page);
		     if(blist==null){
		     	map.put("statusCode","1");
			    map.put("Pager", page);
			    map.put("userBargainList", new ArrayList<>());
			    return map;
		     }
		  for(Bargain b:blist){
			//包装车源信息
			CarSource car = new CarSource(w);
			//包装用户议价信息
			UserBargain userBargain = new UserBargain(b,car,bargainType);
			if(sellerContactState.equals("0")){
				if(userBargain.getSellerContactState().equals("0")){
					 userBargainlist.add(userBargain);
				}
			}
			if(sellerContactState.equals("1")){
				if(userBargain.getSellerContactState().equals("1")){
					userBargainlist.add(userBargain);
				}
			}
			    userBargainlist.add(userBargain);
		  }
			map.put("statusCode","1");
		    map.put("Pager", page);
			map.put("userBargainList", userBargainlist);
			return map;
		  }
		   //如果议价的商品是货源
		   if(w.getType()==1){
		      //获取用户议价信息
		      List<Inquiry> ilist = inquiryDao.getBargain(userid,null,wftransportIds,page);
		      if(ilist==null){
		    	  map.put("statusCode","1");
				  map.put("Pager", page);
				  map.put("userBargainList", new ArrayList<>());
				  return map;
		      }
		   for(Inquiry i:ilist){
		    //包装货源信息
		    HuoResponse huo = new HuoResponse(w);
		    //包装用户议价信息
		    UserBargain userBargain = new UserBargain(i,huo,bargainType);
            try {
				userBargain.setCommodityDistance(BaiduMapUtil.getCommodityDistance(w.getStartFullPath(), w.getEndFullPath())+"");
				if(!StringUtil.isEmpty(latitude)&&!StringUtil.isEmpty(longitude)){
					userBargain.setDistanceMe(BaiduMapUtil.getDistanceMe(w.getStartFullPath(),latitude, longitude)+"");
				}
			} catch (Exception e) {}
		    if(sellerContactState.equals("0")){
			  if(userBargain.getSellerContactState().equals("0")){
				userBargainlist.add(userBargain);
			  }
		    } 
		    if(sellerContactState.equals("1")){
			     if(userBargain.getSellerContactState().equals("1")){
			    	userBargainlist.add(userBargain);
			     }
		    }
		    userBargainlist.add(userBargain);
		  }
			map.put("statusCode","1");
			map.put("Pager", page);
			map.put("userBargainList", userBargainlist);
		    return map;
		 }
	  }
			//对我议价
			if(bargainType.equals("2")){
				//如果议价的商品是车源
				if(w.getType()==0){
					//获取用户议价信息
					List<Bargain> blist = bargainDao.getBargain(null,userid,wftransportIds,page);
					if(blist==null){
						map.put("statusCode","1");
						map.put("Pager", page);
						map.put("userBargainList", new ArrayList<>());
						return map;
					}
					for(Bargain b:blist){
						//包装车源信息
						CarSource car = new CarSource(w);
						//包装用户议价信息
						UserBargain userBargain = new UserBargain(b,car,bargainType);
					    if(sellerContactState.equals("0")){
						     if(userBargain.getSellerContactState().equals("0")){
									 userBargainlist.add(userBargain);
							 }
						} 
					    if(sellerContactState.equals("1")){
							 if(userBargain.getSellerContactState().equals("1")){
									 userBargainlist.add(userBargain);
							 }
						}
					    userBargainlist.add(userBargain);
					}
					map.put("statusCode","1");
					map.put("Pager", page);
					map.put("userBargainList", userBargainlist);
					return map;
				}
				//如果议价的商品是货源
				if(w.getType()==1){
					//获取用户议价信息
					List<Inquiry> ilist = inquiryDao.getBargain(null,userid,wftransportIds,page);
					if(ilist==null){
						map.put("statusCode","1");
						map.put("Pager", page);
						map.put("userBargainList", new ArrayList<>());
						return map;
					}
					for(Inquiry i:ilist){
						//包装货源信息
						HuoResponse huo = new HuoResponse(w);
						//包装用户议价信息
						UserBargain userBargain = new UserBargain(i,huo,bargainType);
					    if(sellerContactState.equals("0")){
						     if(userBargain.getSellerContactState().equals("0")){
									 userBargainlist.add(userBargain);
							 }
						} 
					    if(sellerContactState.equals("1")){
							 if(userBargain.getSellerContactState().equals("1")){
									 userBargainlist.add(userBargain);
							 }
						}
					    userBargainlist.add(userBargain);
					}
					map.put("statusCode","1");
					map.put("Pager", page);
					map.put("userBargainList", userBargainlist);
					return map;
				}
			}
		   //成功 statusCode 返回 1 
		   map.put("statusCode","1");
		   map.put("Pager", page);
		   map.put("userBargainList", new ArrayList<>());
           return map;
	}
	@Override
	public String ifAccept(String states, String bargainId,String type, HttpServletRequest request) throws Exception {
		if(!states.equals("1")&&!states.equals("2")) {
			return "0";
		}
		if(!type.equals("0")&&!type.equals("1")) {
			return "2";
		}
		//获取用户ID
		String userId = request.getHeader("WLB-Token").split("_")[0];
		//买家ID
		String sellerId = "";
		//如果是车源
		if(type.equals("0")) {
		  //获取车源的议价信息
		  Bargain bargain = (Bargain) bargainDao.get(bargainId);
		  if(bargain==null) {
			  return "3";
		  }
		  if(!bargain.getSellerId().equals(userId)) {
			  return "4";
		  }
		  if(bargain.getIfAccept().equals("1")) {
			  return "5";
		  }
		  sellerId=bargain.getSellerId();
		  bargain.setIfAccept(states);
		  bargainDao.updateBean(bargain);
		}
		//如果是货源
		if(type.equals("1")) {
	      //获取货源的询价信息
		  Inquiry inquiry = (Inquiry) inquiryDao.get(bargainId);
		  if(inquiry==null) {
			  return "3";
		  }
		  if(!inquiry.getSellersId().equals(userId)) {
			  return "4";
		  }
		  if(inquiry.getIfAccept().equals("1")) {
			  return "5";
		  }
		  sellerId=inquiry.getSellersId();
		  inquiry.setIfAccept(states);
		  inquiryDao.updateBean(inquiry);
		}
		//获取买家用户信息
		User user = (User) userdao.get(sellerId);
		try {
			//发送极光通知
			Map<String,String> dataNode = new HashMap<String,String>();
			dataNode.put("bargainId", bargainId);
			if(type.equals("0")) {
				dataNode.put("notice", "您有一条新的车源预约消息!");
				JGUtil.wlbAPPsendMessages(user.getRegistrationID(),dataNode,"您有一条新的车源预约消息!");
			}else {
				dataNode.put("notice", "您有一条新的货源预约消息!");
				JGUtil.wlbAPPsendMessages(user.getRegistrationID(),dataNode,"您有一条新的货源预约消息!");	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "1";
	}
}
