package com.ekfans.controllers.web;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.metal.model.PreciousMetal;
import com.ekfans.base.metal.model.PreciousMetalCategory;
import com.ekfans.base.metal.service.IPreciousMetalCategoryService;
import com.ekfans.base.metal.service.IPreciousMetalDetailService;
import com.ekfans.base.metal.service.IPreciousMetalService;
import com.ekfans.base.product.model.Product;
import com.ekfans.base.product.model.ProductPriceChange;
import com.ekfans.base.product.service.IProductPriceChangeService;
import com.ekfans.base.product.service.IProductService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.JsonUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 商品价格走势图查看
 * 
 * @ClassName: WebPriceChangeController
 * @Description: TODO
 * @author cgm
 * @date 2014-6-11 上午10:23:04
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
public class WebPriceChangeController extends BasicController {
	@Autowired
	private IProductPriceChangeService changeService;
	@Autowired
	private IProductService productService;

	@RequestMapping(value = "/web/priceChange/asImg")
	@ResponseBody
	public String hostPricesJsonLoad(HttpServletRequest request,
			HttpSession session) {
		Pager pager = new Pager(1);
		pager.setRowsPerPage(7);
		String productId = request.getParameter("productId");
		Product product = productService.getProductById(productId);
		BigDecimal price = product.getUnitPrice();
		List<ProductPriceChange> changes = changeService.list(pager, productId);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String jsonStr = "";
		Map<String, String> map = null;
		SimpleDateFormat format = new SimpleDateFormat("MM/dd");
		SimpleDateFormat sformat = new SimpleDateFormat("yyyy-MM-dd");
		String endDate = DateUtil.getSysDateString();
		int length = 7;
		try {
			if (null != changes && changes.size() > 0) {
				length = 7 - changes.size();
				for (int a = changes.size() - 1; a >= 0; a--) {
					map = new HashMap<String, String>();
					ProductPriceChange change = changes.get(a);
					if (change == null || StringUtil.isEmpty(change.getCreateTime())) {
						continue;
					}
					map.put("date", format.format(sformat.parse(change.getCreateTime())));
					map.put("value", change.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "");
					map.put("date", format.format(sformat.parse(changes.get(a)
							.getCreateTime())));
					map.put("value",
							changes.get(a).getPrice()
									.setScale(2, BigDecimal.ROUND_HALF_UP)
									.doubleValue()
									+ "");
					list.add(map);
				}
			}

			if (null == changes
					|| (null != changes && changes.size() > 0 && changes.size() < 7)) {
				if (null != changes && changes.size() > 0 && changes.size() < 7) {
					endDate = DateUtil.toString(sformat.parse(changes.get(changes.size() - 1).getCreateTime()));
				}
				for (int i = length; i > 0; i--) {
					map = new HashMap<String, String>();
					String putDate = DateUtil.toString(DateUtil.addDays(
							DateUtil.toDate(endDate), -i));
					map.put("date", format.format(sformat.parse(putDate)));
					map.put("value", price
							.setScale(2, BigDecimal.ROUND_HALF_UP)
							.doubleValue()
							+ "");
					list.add(map);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		//处理list集合根据日期排序
		Collections.sort(list, new Comparator<Map>() {
            public int compare(Map o1, Map o2) {
                Integer a = Integer.parseInt(o1.get("date").toString().replace("/",""));
                Integer b = Integer.parseInt(o2.get("date").toString().replace("/",""));
                // 升序
                return a.compareTo(b);
                // 降序
                // return b.compareTo(a);
            }
        });
		jsonStr = JsonUtil.listToJson(list);
		return jsonStr;
	}
}
