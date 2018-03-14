package com.ekfans.base.system.util;

import java.util.List;
import java.util.Map;

import com.ekfans.base.system.model.SystemArea;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.pub.util.StringUtil;

/**
 * 地址选择的工具类
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-29
 * @version 1.0
 */
public class SystemAreaHelper {

	public static String getAreaSelecs(String property, String areaId,
			boolean addChild, String html) {
		Map<String, SystemArea> areas = Cache.getSystemAreas();
		// 如果地区ID不为空，并且地区ID总包含"|"，则分割ID，并且取最后一个做为地区ID
		if (!StringUtil.isEmpty(areaId) && areaId.indexOf("|") != -1) {
			String[] areaIds = areaId.split("\\|");
			if (!StringUtil.isEmpty(areaId)) {
				areaId = areaIds[areaIds.length - 1];
			}
		}
		if (StringUtil.isEmpty(html)) {
			html = "";
		}

		String rootId = Cache.getResource("area.parent.code");
		if (StringUtil.isEmpty(areaId)) {
			// 调用缓存方法获取跟地区的Id
			areaId = rootId;
		}

		SystemArea thisArea = areas.get(areaId);

		if (thisArea == null) {
			return html;
		}

		SystemArea parentArea = areas.get(thisArea.getParentCode());
		if (parentArea != null && !StringUtil.isEmpty(parentArea.getId())
				&& parentArea.getChildList() != null
				&& parentArea.getChildList().size() > 0) {
			StringBuffer newHtm = new StringBuffer("");
			newHtm.append("<select class=\"" + property
					+ "Area\"><option value=\"\">请选择:</option>");

			List<SystemArea> pChildList = parentArea.getChildList();
			for (int i = 0; i < pChildList.size(); i++) {
				SystemArea a = pChildList.get(i);
				if (a != null) {
					newHtm.append("<option value=\"").append(a.getId())
							.append("\"");
					if (areaId.equals(a.getId())) {
						newHtm.append(" selected ");
					}
					newHtm.append(">").append(a.getAreaName())
							.append("</option>");
				}
			}
			newHtm.append("</select>");
			html = newHtm.toString() + html;
		}

		if (addChild && thisArea.getChildList() != null
				&& thisArea.getChildList().size() > 0) {
			StringBuffer newHtm = new StringBuffer("");
			newHtm.append("<select class=\"" + property
					+ "Area\"><option value=\"\">请选择:</option>");

			List<SystemArea> childList = thisArea.getChildList();
			for (int i = 0; i < childList.size(); i++) {
				SystemArea a = childList.get(i);
				if (a != null) {
					newHtm.append("<option value=\"").append(a.getId())
							.append("\">").append(a.getAreaName())
							.append("</option>");
				}
			}
			newHtm.append("</select>");
			html = html + newHtm.toString();
		}

		if (!StringUtil.isEmpty(parentArea.getId())
				&& !rootId.equals(parentArea.getId())) {
			return getAreaSelecs(property, thisArea.getParentCode(), false,
					html);
		}

		return html;

	}
}
