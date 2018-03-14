package com.ekfans.controllers.tools;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.system.model.SystemArea;
import com.ekfans.base.system.service.ISystemAreaService;
import com.ekfans.base.system.service.SystemAreaService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.pub.util.StringUtil;

/**
 * 地区选择的实现类
 * 
 * @author liuguoyu
 * 
 */
@Controller
public class AreaSelController extends BasicController {
	// 定义SystemAreaService
	@Autowired
	ISystemAreaService systemAreaService = new SystemAreaService();

	/**
	 * 地区选择Action方法
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/area/sel/{areaId}/{property}")
	@ResponseBody
	public void sel(@PathVariable("areaId") String areaId, @PathVariable String property,HttpServletResponse resp) throws Exception {
		// 如果地区ID不为空，并且地区ID总包含"|"，则分割ID，并且取最后一个做为地区ID
		if (!StringUtil.isEmpty(areaId) && areaId.indexOf("|") != -1) {
			String[] areaIds = areaId.split("\\|");
			if (!StringUtil.isEmpty(areaId)) {
				areaId = areaIds[areaIds.length - 1];
			}
		}

		Map<String, SystemArea> areas = Cache.getSystemAreas();

		if (areas == null || areas.size() <= 0) {
			this.backWriteStr(resp, "");
			return;
		}

		SystemArea area = areas.get(areaId);

		StringBuffer newHtm = new StringBuffer("");
		List<SystemArea> childList = null;
		if (area != null) {
			childList = area.getChildList();
		}
		if (area != null && childList != null && childList.size() > 0) {
			newHtm.append("<select class='").append(property).append("Area'><option value=\"\">请选择:</option>");

			for (int i = 0; i < childList.size(); i++) {
				SystemArea a = childList.get(i);
				if (a != null) {
					newHtm.append("<option value=\"").append(a.getId()).append("\">").append(a.getAreaName()).append("</option>");
				}
			}
			newHtm.append("</select>");
		}
		// response.setCharacterEncoding("UTF-8");
		this.backWriteStr(resp, newHtm.toString());
		// return newHtm.toString();
	}
}
