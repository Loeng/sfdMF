package com.ekfans.pub.util.datacrawling;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.axis2.databinding.types.soapencoding.Array;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.ekfans.base.metal.dao.IPreciousMetalCategoryDao;
import com.ekfans.base.metal.dao.IPreciousMetalDao;
import com.ekfans.base.metal.dao.IPreciousMetalDetailDao;
import com.ekfans.base.metal.model.PreciousMetal;
import com.ekfans.base.metal.model.PreciousMetalCategory;
import com.ekfans.base.metal.model.PreciousMetalDetail;
import com.ekfans.base.metal.service.IPreciousMetalDetailService;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.StringUtil;

/**
 * 用于上海有色金属(https://hq.smm.cn)的数据抓取工具类
 * @author pp
 * @date 2017年10月26日14:20:24
 *
 */
public class SmmDataUtil {
     /**
      * 从网页上抓取数据
      * @return
      */
	 public static String grabbingData() {
		 //记录抓取过程中的信息(包括错误信息)
		 StringBuffer log = new StringBuffer(""); 
		 //循环需要抓取的网址
		 for(Smm smm : Smm.values()) {
	        try {
	        	// 通过Jsoup把url转化成document格式
				Document doc = Jsoup.connect(smm.getUrl()).get();
				// 通过元素ID获取需要抓取得具体品类
				IPreciousMetalDetailService  detailService= SpringContextHolder.getBean(IPreciousMetalDetailService.class);
				Map<String, PreciousMetalDetail> map=detailService.getDetailsByDateAndCategory(smm.getId(), "2017-10-27");
				for(PreciousMetalDetail p : map.values()) {
					//如果上海有色网的金属品类ID为空
					if(StringUtil.isEmpty(p.getSmmId())) {
						IPreciousMetalCategoryDao categoryDao = SpringContextHolder.getBean(IPreciousMetalCategoryDao.class);
						PreciousMetalCategory category  = (PreciousMetalCategory) categoryDao.get(smm.getId());
						log.append("有色金属元素品类:["+category.getName()+"]的数据获取失败;");
						continue;
					}
					  Elements pngs = doc.select("div[product_id="+p.getSmmId()+"]").select(".first");
			          String[] price = pngs.select(".value2").html().split("-");
			          String startPrice =  price[0];
			          String endPrice =  price[1];
			          String riseAndDrop = pngs.select(".value4").html();	
			          PreciousMetalDetail preciousMetalDetail = new PreciousMetalDetail();
			          preciousMetalDetail.setMetalId(p.getSmmId());
			          preciousMetalDetail.setStartPrice(new BigDecimal(startPrice));
			          preciousMetalDetail.setEndPrice(new BigDecimal(endPrice));
			          preciousMetalDetail.setRiseAndDrop(Double.parseDouble(riseAndDrop));
			          preciousMetalDetail.setPriceType(true);
			          preciousMetalDetail.setCreateTime(DateUtil.getSysDateTimeString());
			          if(!detailService.saveOrUpdate(preciousMetalDetail)) {
			        	  IPreciousMetalCategoryDao categoryDao = SpringContextHolder.getBean(IPreciousMetalCategoryDao.class);
						  PreciousMetalCategory category  = (PreciousMetalCategory) categoryDao.get(smm.getId());
			        	  log.append("有色金属元素品类:["+category.getName()+"]的数据获取失败;");
			          }
			    }
			} catch (Exception e) {
				log.append("有色金属元素:["+smm.getName()+"]"+"的数据获取失败;");
				e.printStackTrace();
			} 
		 }
		 return log.toString();
	 }
  
  /**
   * 通过金属品目ID获取有色网金属品类列表
   * @param categoryId
   * @return
   */
  public static List<SmmModel> getListSmmModel(String categoryId){
	  List<SmmModel> smmModelList= new ArrayList<SmmModel>();
	  //循环需要抓取的网址
	  for(Smm smm : Smm.values()) {
		  if(smm.getId().equals(categoryId)) {
			// 通过Jsoup把url转化成document格式
			try {
				Document doc = Jsoup.connect(smm.getUrl()).get();
				Elements pngs = doc.select("div[class=market_content_body_body]").select("li");
				for(int i = 0;i<pngs.size();i++) {
				 try {
					SmmModel smmModel = new SmmModel();
					String smmId = pngs.get(i).select(".market_content_body_body_wrap").attr("product_id");
					Elements p = pngs.get(i).select(".first");
					smmModel.setSmmId(smmId);
					smmModel.setSmmName(p.select(".value1").text());
					smmModel.setArea(p.select(".value5").text());
					smmModel.setDate(pngs.get(i).select("div[class=itemDateTime]").text());
					if(!p.select(".value2").text().equals("--")&&!p.select(".value2").text().equals("缺货")) {
						String[] price = p.select(".value2").text().split("-");
						smmModel.setStartPrice(price[0]);
						smmModel.setEndPrice(price[1]);
						smmModel.setPrice(p.select(".value3").text());
						smmModel.setRiseAndDrop(p.select(".value4").text());
					}
					Elements four = pngs.get(i).select(".four");
					if(!four.isEmpty()) {
						smmModel.setSpec(four.select(".value1").text());
						SmmModel smmModel2 = new SmmModel();
						smmModel2.setSmmId(smmId+"hhh");
						smmModel2.setSmmName(smmModel.getSmmName());
						smmModel2.setArea(smmModel.getArea());
						smmModel2.setDate(smmModel.getDate());
						if(!four.select(".value2").text().equals("--")&&!four.select(".value2").text().equals("缺货")) {
							String[] price = four.select(".value2").text().split("-");
							smmModel2.setStartPrice(price[0]);
							smmModel2.setEndPrice(price[1]);
							smmModel2.setPrice(four.select(".value3").text());
							smmModel2.setRiseAndDrop(four.select(".value4").text());
							smmModel2.setUnit(four.select(".value6").text());
							smmModelList.add(smmModel2);
						}
						
					}else {
						Elements third = pngs.get(i).select(".third");
						if(!third.isEmpty()) {
							smmModel.setSpec(third.select("span").text());
						}
					}
					smmModel.setUnit(p.select(".value2").attr("dataunit"));
					smmModelList.add(smmModel);
				  }catch (Exception e) {
					 e.printStackTrace();
				  }
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		  }
	  }
	  return smmModelList;
  }
  
    /**
     * 更新所有的金属品类
     */
    public static String updateAll() {
    	//记录抓取过程中的信息(包括错误信息)
		StringBuffer log = new StringBuffer(""); 
		IPreciousMetalDao iPreciousMetalDao= SpringContextHolder.getBean(IPreciousMetalDao.class);
		IPreciousMetalDetailService detailService = SpringContextHolder.getBean(IPreciousMetalDetailService.class);
    	for(Smm smm : Smm.values()) {
    		List<SmmModel> smmModelList  =  SmmDataUtil.getListSmmModel(smm.getId());
    		String hql = "from PreciousMetal where categoryId ='"+smm.getId()+"'";
    		try {
    			@SuppressWarnings("unchecked")
				List<PreciousMetal> preciousMetalList = iPreciousMetalDao.list(hql, null);
    			
    			for(PreciousMetal p : preciousMetalList) {
    				int i = 0;
    				for(SmmModel s : smmModelList) {
    					if(p.getSmmId().equals(s.getSmmId())) {
    					 try {
    						PreciousMetalDetail preciousMetalDetail = new PreciousMetalDetail();
    						preciousMetalDetail.setCreateTime(DateUtil.getSysDateTimeString());
    						preciousMetalDetail.setDateTime(DateUtil.getSysDateString());
    						if(!s.getEndPrice().equals("--")) {
    							preciousMetalDetail.setEndPrice(new BigDecimal(s.getEndPrice()));
    						}
    						if(!s.getStartPrice().equals("--")) {
    							preciousMetalDetail.setStartPrice(new BigDecimal(s.getStartPrice()));
    						}
    						preciousMetalDetail.setMetalId(p.getId());
    						preciousMetalDetail.setPrice(new BigDecimal(s.getPrice()));
    						preciousMetalDetail.setPriceType(true);
    						try {
    						preciousMetalDetail.setRiseAndDrop(Double.parseDouble(s.getRiseAndDrop()));
							} catch (Exception e) {
								e.printStackTrace();
							}
    						detailService.saveOrUpdate(preciousMetalDetail);
    						i=1;
    						break;
    					  } catch (Exception e) {
    							e.printStackTrace();
    							log.append("<p>有色金属品类["+smm.getName()+"]元素["+p.getName()+"]规格["+p.getSpec()+"]更新失败</p>");
    							break;
    					  }
    				   }
    				}
    				if(i==0) {
    					log.append("<p>有色金属品类["+smm.getName()+"]元素["+p.getName()+"]规格["+p.getSpec()+"]更新失败</p>");
    				}
    			}
			} catch (Exception e) {
				e.printStackTrace();
				log.append("<p>有色金属品类["+smm.getName()+"]更新失败</p>");
			}
    	}
    	return log.toString();
    }
}
