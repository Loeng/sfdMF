package com.ekfans.controllers.system.content;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.content.model.AdDetail;
import com.ekfans.base.content.model.ShopAd;
import com.ekfans.base.content.service.IAdDetailService;
import com.ekfans.base.content.service.IShopAdService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.controllers.tools.util.AdvertXmlUtil;
import com.ekfans.controllers.tools.util.FileUploadHelper;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
public class SystemAdvertController extends BasicController {
	// 定义Service
	@Autowired
	private IShopAdService shopAdService;

	@Autowired
	private IAdDetailService adDetailService;

	/**
	 * 跳转到新增页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/advert/add")
	public String add() {
		return "/system/ad/adAdd";
	}
    /**
     * 跳转到物流宝广告新增页面
     * @return
     */
	@RequestMapping(value = "/system/wlbad/add")
    public String wlbAdAdd(){
    	return "/system/ad/wlbAdAdd";
    }
	
	/**
	 * 
	 * @Title: typeLoad
	 * @Description: TODO(根据选择的广告类型load对应的页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/advert/typeLoad/{type}")
	public String typeLoad(@PathVariable String type) {
		if ("0".equals(type)) {
			// 普通广告
			return "/system/ad/norAd";
		} else if ("1".equals(type)) {
			// 切换广告
			return "/system/ad/changeAd";
		} else if ("2".equals(type)) {
			// 自定义广告
			return "/system/ad/diyAd";
		} else {
			// 文字广告
			return "/system/ad/textAd";
		}
	}

	/**
	 * 
	 * @Title: numLoad
	 * @Description: TODO(根据输入的数量load循环页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param type
	 * @param num
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/advert/numLoad/{type}/{num}")
	public String numLoad(@PathVariable String type, @PathVariable int num, HttpServletRequest request) {
		request.setAttribute("number", num);
		if ("3".equals(type)) {
			// 文字广告
			return "/system/ad/textNum";
		} else {
			// 非文字广告
			return "/system/ad/norNum";
		}
	}

	/**
	 * 新增广告
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/advert/save")
	public String save(HttpServletRequest request, HttpServletResponse response) {
		// 得到广告类型
		String type = request.getParameter("type");
		// 若为自定义广告
		if ("2".equals(type)) {
			// 得到广告名
			String name = request.getParameter("name");
			// 得到广告内容
			String content = request.getParameter("adContent");
			// 得到广告链接地址
			String linkUrl = request.getParameter("linkUrl");

			ShopAd ad = new ShopAd();
			// 设置广告名
			ad.setName(name);
			// 设置广告创建时间
			ad.setCreateTime(DateUtil.getSysDateTimeString());
			// 设置广告类型
			ad.setType(type);
			// 设置广告风格
			ad.setShowType("zidingyi");
			// 保存广告
			shopAdService.addAdvert(ad);
			// 得到广告id
			String id = shopAdService.getAdvertIdByName(name);
			AdDetail detail = new AdDetail();
			// 设置广告内容
			detail.setAdContent(content);
			// 设置广告id
			detail.setAdId(id);
			// 设置广告链接地址
			detail.setLinkAddress(linkUrl);

			adDetailService.addAdvertDetail(detail);
			request.setAttribute("addOk", true);

			// 若为文字广告
		} else if ("3".equals(type)) {
			// 得到广告名
			String name = request.getParameter("name");
			// 得到广告内容数组
			String[] contents = request.getParameterValues("adContent");

			// 得到文字颜色数组
			String[] colors = request.getParameterValues("textColor");

			// 得到广告链接地址
			String[] linkUrls = request.getParameterValues("linkUrl");

			ShopAd ad = new ShopAd();
			// 设置广告名
			ad.setName(name);
			// 设置广告创建时间
			ad.setCreateTime(DateUtil.getSysDateTimeString());
			// 设置广告类型
			ad.setType(type);
			// 设置广告风格
			ad.setShowType("wenzi");
			// 保存广告
			shopAdService.addAdvert(ad);
			// 得到广告id
			String id = shopAdService.getAdvertIdByName(name);
			for (int i = 0; i < linkUrls.length; i++) {
				AdDetail detail = new AdDetail();
				// 设置广告内容
				detail.setAdContent(contents[i]);
				// 设置广告id
				detail.setAdId(id);
				// 设置广告链接地址
				detail.setLinkAddress(linkUrls[i]);
				// 设置排序位置
				detail.setPosition(i);

				detail.setContentColor(colors[i]);

				adDetailService.addAdvertDetail(detail);
			}
			request.setAttribute("addOk", true);

		} else {
			// 若为普通广告或切换广告
			// 得到广告名
			String name = request.getParameter("name");

			// 得到广告风格
			String showType = request.getParameter("showType");

			// 得到广告宽度
			String width = request.getParameter("wid");

			// 得到广告高度
			String height = request.getParameter("height");

			// 得到广告内容数组
			String[] contents = request.getParameterValues("adContent");

			// 得到广告链接地址数组
			String[] linkUrls = request.getParameterValues("linkUrl");

			ShopAd ad = new ShopAd();
			// 设置广告名
			ad.setName(name);
			// 设置广告创建时间
			ad.setCreateTime(DateUtil.getSysDateTimeString());
			// 设置广告类型
			ad.setType(type);
			// 设置广告风格
			ad.setShowType(showType);
			// 设置宽高
			if(!StringUtil.isEmpty(width)){
			    ad.setWidth(Double.parseDouble(width));
			}
			if(!StringUtil.isEmpty(height)){
			    ad.setHigh(Double.parseDouble(height));
            }
			
			// 保存广告
			shopAdService.addAdvert(ad);
			// 得到广告id
			String id = shopAdService.getAdvertIdByName(name);
			for (int i = 0; i < linkUrls.length; i++) {
				AdDetail detail = new AdDetail();
				// 设置广告内容
				detail.setAdContent(contents[i]);
				// 设置广告id
				detail.setAdId(id);
				// 设置广告链接地址
				detail.setLinkAddress(linkUrls[i]);
				// 设置排序位置
				detail.setPosition(i);

				// 设置图标保存路径
				String currentPath = "/customerfiles/content/" + DateUtil.getNoSpSysDateString() + "/ad/";
				// 调用方法获取分类图标，返回图标路径
				int j = i + 1;
				String picPath = FileUploadHelper.uploadFile("adPic" + j, currentPath, request, response);
				// 设置图片路径
				detail.setAdPicture(picPath);

				adDetailService.addAdvertDetail(detail);
			}
			request.setAttribute("addOk", true);
		}
		return "/system/ad/adAdd";
	}

	/**
	 * 删除广告
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/advert/delete/{id}")
	@ResponseBody
	public Object delete(@PathVariable String id) {
		// 利用Service的方法根据id删除广告
		if (shopAdService.deleteAdvert(id) && adDetailService.deleteDetailByAdvertId(id)) {
			// 删除成功，返回true
			return true;
		}
		return false;
	}

	/**
	 * 修改广告
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/advert/modify")
	public String modify(ShopAd shopAd, HttpServletRequest request, HttpServletResponse response) {
		// 得到广告类型
		String type = request.getParameter("type");
		// 得到广告id
		String id = request.getParameter("id");
		// 得到广告创建时间
		String createTime = request.getParameter("createTime");
		// 若为自定义广告
		if ("2".equals(type)) {
			// 得到广告名
			String name = request.getParameter("name");
			// 得到广告内容
			String content = request.getParameter("adContent");
			// 得到广告链接地址
			String linkUrl = request.getParameter("linkUrl");
			// 设置广告id
			shopAd.setId(id);
			// 设置广告名
			shopAd.setName(name);
			// 设置广告创建时间
			shopAd.setCreateTime(createTime);
			// 设置广告类型
			shopAd.setType(type);
			// 设置广告风格
			shopAd.setShowType("zidingyi");
			// 修改广告
			shopAdService.modifyAdvert(shopAd);

			AdDetail detail = new AdDetail();
			// 得到广告明细id
			String detailId = request.getParameter("detailId");
			// 设置id
			detail.setId(detailId);
			// 设置广告内容
			detail.setAdContent(content);
			// 设置广告id
			detail.setAdId(id);
			// 设置广告链接地址
			detail.setLinkAddress(linkUrl);
			// 修改广告明细
			adDetailService.modifyAdvertDetail(detail);
			request.setAttribute("modifyOk", true);

			// 若为文字广告
		} else if ("3".equals(type)) {
			// 得到广告名
			String name = request.getParameter("name");
			// 得到广告明细id数组
			String[] ids = request.getParameterValues("detailId");

			// 得到广告内容数组
			String[] contents = request.getParameterValues("adContent");

			// 得到文字颜色数组
			String[] colors = request.getParameterValues("textColor");

			// 得到广告链接地址
			String[] linkUrls = request.getParameterValues("linkUrl");
			// 设置广告id
			shopAd.setId(id);
			// 设置广告名
			shopAd.setName(name);
			// 设置广告创建时间
			shopAd.setCreateTime(createTime);
			// 设置广告类型
			shopAd.setType(type);
			// 设置广告风格
			shopAd.setShowType("wenzi");
			// 保存广告
			shopAdService.modifyAdvert(shopAd);
			for (int i = 0; i < linkUrls.length; i++) {
				AdDetail detail = new AdDetail();
				// 若有id时(即未修改广告明细)
				if (ids != null && ids.length > 0) {
					// 设置id
					detail.setId(ids[i]);

					// 设置广告内容
					detail.setAdContent(contents[i]);
					// 设置广告id
					detail.setAdId(id);
					// 设置广告链接地址
					detail.setLinkAddress(linkUrls[i]);
					// 设置排序位置
					detail.setPosition(i);

					detail.setContentColor(colors[i]);

					// 修改广告明细
					adDetailService.modifyAdvertDetail(detail);
				} else {
					// 没有id(即修改了明细)删除对应的明细
					adDetailService.deleteDetailByAdvertId(id);

					// 设置广告内容
					detail.setAdContent(contents[i]);
					// 设置广告id
					detail.setAdId(id);
					// 设置广告链接地址
					detail.setLinkAddress(linkUrls[i]);
					// 设置排序位置
					detail.setPosition(i);

					detail.setContentColor(colors[i]);
					// 新增广告明细
					adDetailService.addAdvertDetail(detail);
				}
			}
			request.setAttribute("modifyOk", true);

		} else {
			// 若为普通广告或切换广告

			// 得到广告名
			String name = request.getParameter("name");

			// 得到广告风格
			String showType = request.getParameter("showType");

			// 得到广告宽度
			String width = request.getParameter("wid");

			// 得到广告高度
			String height = request.getParameter("height");

			// 得到广告内容数组
			String[] contents = request.getParameterValues("adContent");

			// 得到广告图片数组
			String[] pictures = request.getParameterValues("picture");

			// 得到广告链接地址数组
			String[] linkUrls = request.getParameterValues("linkUrl");
			// 设置id
			shopAd.setId(id);
			// 设置广告名
			shopAd.setName(name);
			// 设置广告创建时间
			shopAd.setCreateTime(createTime);
			// 设置广告类型
			shopAd.setType(type);
			// 设置广告风格
			shopAd.setShowType(showType);
			
			// 设置宽高
            if(!StringUtil.isEmpty(width)){
                shopAd.setWidth(Double.parseDouble(width));
            }else{
                shopAd.setWidth(0.0);
            }
            if(!StringUtil.isEmpty(height)){
                shopAd.setHigh(Double.parseDouble(height));
            }else{
                shopAd.setHigh(0.0);
            }
			// 修改广告
			shopAdService.modifyAdvert(shopAd);

			// 删除广告明细
			adDetailService.deleteDetailByAdvertId(id);
			
			for (int i = 0; i < linkUrls.length; i++) {
				AdDetail detail = new AdDetail();
				// 设置广告内容
				detail.setAdContent(contents[i]);
				// 设置广告id
				detail.setAdId(id);
				// 设置广告链接地址
				detail.setLinkAddress(linkUrls[i]);
				// 设置排序位置
				detail.setPosition(i);

				// 设置图标保存路径
				String currentPath = "/customerfiles/content/" + DateUtil.getNoSpSysDateString() + "/ad/";
				// 调用方法获取分类图标，返回图标路径
				int j = i + 1;
				String picPath = FileUploadHelper.uploadFile("adPic" + j, currentPath, request, response);
				// 若修改了图片路径
				if (!StringUtil.isEmpty(picPath)) {
					detail.setAdPicture(picPath);
				} else {
				    if(pictures!=null&&pictures.length>0){
				        // 没有修改则保存原图
	                    detail.setAdPicture(pictures[i]);
				    }
				}
				// 修改广告明细
				adDetailService.addAdvertDetail(detail);
				
			}
			request.setAttribute("modifyOk", true);
		}

		// 得到广告明细列表
		List<AdDetail> details = adDetailService.getDeailsByAdvertId(id);
		shopAd.setDetailist(details);
		request.setAttribute("shopAd", shopAd);
		return "/system/ad/adModify";
	}

	/**
	 * 查询广告信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/advert/detail/{id}")
	public String detail(@PathVariable String id, HttpServletRequest request) {
		// 取得广告
		ShopAd shopAd = shopAdService.getAdvertById(id);

		// 得到广告明细列表
		List<AdDetail> details = adDetailService.getDeailsByAdvertId(id);
		shopAd.setDetailist(details);
		request.setAttribute("shopAd", shopAd);
		return "/system/ad/adModify";
	}

	/**
	 * 
	 * @Title: query
	 * @Description: 广告预览 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param id
	 * @param request
	 * @param response
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/advert/query/{id}")
	public String query(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) {

		// 取得广告
		ShopAd shopAd = shopAdService.getAdvertById(id);
		if (shopAd != null) {
			// 得到广告明细列表
			List<AdDetail> details = adDetailService.getDeailsByAdvertId(id);
			shopAd.setDetailist(details);
			String showType = shopAd.getShowType();
			AdvertXmlUtil.getAdHtmlWithSystem(request, shopAd);
			request.setAttribute("shopAd", shopAd);
			request.setAttribute("showType", showType);
			return "system/ad/adQuery";
		}
		return null;
	}

	/**
	 * 查找广告列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/advert/list")
	public String list(HttpServletRequest request) {
	    // 从页面获取广告名称
        String name = request.getParameter("name");
        // 从页面获取页码
        String currentpageStr = request.getParameter("pageNum");
        // 获取广告类型
        String type = request.getParameter("type");
	    
		// 定义分页
		Pager pager = new Pager();
		// 将从页面获取的分页数据转化成int型
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			try {
				currentPage = Integer.parseInt(currentpageStr);
			} catch (Exception e) {
				
			}
		}
		// 设置要查询的页码
		pager.setCurrentPage(currentPage);
		// 利用Service的方法查找广告
		List<ShopAd> shopAds = shopAdService.listAdvert(pager, name, type);
		request.setAttribute("shopAds", shopAds);
		request.setAttribute("pager", pager);
		request.setAttribute("name", name);
		request.setAttribute("type", type);
		return "/system/ad/adList";
	}
    
	/**
	 * 物流宝获取广告列表
	 * @return
	 */
	@RequestMapping(value = "/system/wlbad/list")
	public String wlbAdList(HttpServletRequest request){
	    // 从页面获取广告名称
        String name = request.getParameter("name");
        // 从页面获取页码
        String currentpageStr = request.getParameter("pageNum");
        // 获取广告类型
        String type = request.getParameter("type");
	    // 定义分页
		Pager pager = new Pager();
		// 将从页面获取的分页数据转化成int型
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			try {
				currentPage = Integer.parseInt(currentpageStr);
			} catch (Exception e) {
				
			}
		}
		// 设置要查询的页码
		pager.setCurrentPage(currentPage);
		// 利用Service的方法查找广告
		List<ShopAd> shopAds = shopAdService.listAdvert(pager, name, type);
		request.setAttribute("shopAds", shopAds);
		request.setAttribute("pager", pager);
		request.setAttribute("name", name);
		request.setAttribute("type", type);
		return "/system/ad/wlbAdList";
	}
	/**
	 * 物流宝配置广告
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/system/wlbad/wlbConfigureAd/{shopAdid}")
	public String wlbConfigureAd(@PathVariable String shopAdid){
		String a = ""; 
		try {
			 a = shopAdService.wlbConfigureAd(shopAdid);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "0";
		}
		return a;
	}
	/**
	 * 物流宝取消广告配置
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/system/wlbad/wlbCancelConfigureAd/{shopAdid}")
	public String wlbCancelConfigureAd(@PathVariable String shopAdid){
		String a = ""; 
		try {
			 a =  shopAdService.wlbCancelConfigureAd(shopAdid);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "0";
		}
		return a;
	}
	
	
	/**
	 * 频道配置选择广告
	 * 
	 * @Title: channelConfigChoseAd
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param request
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/channel/config/adChose/{adName}/{type}/{showType}")
	public String channelConfigChoseAd(@PathVariable String adName, @PathVariable String type, @PathVariable String showType, HttpServletRequest request) {
		request.setAttribute("adName", adName);
		request.setAttribute("type", type);
		request.setAttribute("showType", showType);

		List<ShopAd> adList = shopAdService.getShopAds(adName, type, showType, null);
		request.setAttribute("adList", adList);
		return "/web/channel/commons/config/channelConfigAdChose";
	}
}
