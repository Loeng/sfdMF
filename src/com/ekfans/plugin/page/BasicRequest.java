package com.ekfans.plugin.page;

import javax.servlet.http.HttpServletRequest;

import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.page.util.BasicConst;
import com.ekfans.pub.util.StringUtil;

public class BasicRequest {
	private HttpServletRequest request;

	// 初始化 ActionRequest
	public BasicRequest(HttpServletRequest request) {
		this.request = request;

	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * 转换图片路径
	 * 
	 * @Title: getImage
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param imagePath
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getImage(String imagePath) {
		if (StringUtil.isEmpty(imagePath)) {
			return "";
		}

		if (imagePath.indexOf("http:") != -1 || imagePath.indexOf("HTTP:") != -1 || imagePath.indexOf("https:") != -1 && imagePath.indexOf("HTTPS:") != -1) {
			return imagePath;
		}

		String returnImage = Cache.getResource("img.webRoot");

		if (StringUtil.isEmpty(returnImage)) {
			returnImage = getWebFullUrlPrex();
		}

		if (!StringUtil.isEmpty(imagePath)) {
			if (!imagePath.startsWith("/")) {
				if (returnImage.endsWith("/")) {
					returnImage = returnImage + imagePath;
				} else {
					returnImage = returnImage + "/" + imagePath;
				}
			} else {
				returnImage = returnImage + imagePath;
			}
		}

		return returnImage;
	}

	/**
	 * 获取路径前缀，如： http://www.ekfans.com:8080/ekfans/
	 * 
	 * @Title: getWebFullUrlPrex
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getWebFullUrlPrex() {
		// http://www.ekfans.com:8080/
		StringBuffer urlStr = new StringBuffer("http://");
		urlStr.append(request.getServerName());
		if (request.getServerPort() != 80) {
			urlStr.append(":").append(request.getServerPort());
		}

		// http://www.ekfans.com/01-id.htm
		if (!StringUtil.isEmpty(request.getContextPath())) {
			if (request.getContextPath().startsWith("/")) {
				urlStr.append(request.getContextPath());
			} else {
				urlStr.append("/").append(request.getContextPath());
			}
		}
		return urlStr.toString();
	}

	/**
	 * 根据类型获取全路径
	 * 
	 * @Title: getWebUrl
	 * @param @param type
	 * @param @param uuid
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getWebUrl(String type, String uuid) {
		if (StringUtil.isEmpty(type) || StringUtil.isEmpty(uuid)) {
			return null;
		}

		// http://www.ekfans.com:8080
		StringBuffer urlStr = new StringBuffer(getWebFullUrlPrex());

		// 商品
		if (BasicConst.WEB_URL_PRODUCT.equals(type)) {
			urlStr.append("/").append(BasicConst.WEB_URL_PRODUCT).append("-").append(uuid).append(".html");

			// 商品分类
		} else if (BasicConst.WEB_URL_PRODUCT_CATEGORY.equals(type)) {
			urlStr.append("/").append(BasicConst.WEB_URL_PRODUCT_CATEGORY).append("-").append(uuid).append(".html");

			// 资讯
		} else if (BasicConst.WEB_URL_CONTENT.equals(type)) {
			urlStr.append("/").append(BasicConst.WEB_URL_CONTENT).append("-").append(uuid).append("-1").append(".html");

			// 资讯分类
		} else if (BasicConst.WEB_URL_CONTENT_CATEGORY.equals(type)) {
			urlStr.append("/").append(BasicConst.WEB_URL_CONTENT_CATEGORY).append("-").append(uuid).append(".html");

			// 店铺
		} else if (BasicConst.WEB_URL_STORE.equals(type)) {
			urlStr.append("/").append(BasicConst.WEB_URL_STORE).append("-").append(uuid).append(".html");

			// 频道
		} else if (BasicConst.WEB_URL_CHANNEL.equals(type)) {
			urlStr.append("/").append(uuid).append(".html");

		}

		return urlStr.toString();
	}

	/**
	 * 根据类型获取全路径
	 * 
	 * @Title: getWebUrl
	 * @param @param type
	 * @param @param uuid
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getContentWebUrl(String uuid, String pageNo) {
		if (StringUtil.isEmpty(uuid)) {
			return null;
		}

		if (StringUtil.isEmpty(pageNo)) {
			pageNo = "1";
		}
		// http://www.ekfans.com:8080/
		StringBuffer urlStr = new StringBuffer(getWebFullUrlPrex());

		urlStr.append("/").append(BasicConst.WEB_URL_CONTENT).append("-").append(uuid).append("-").append(pageNo).append(".html");

		return urlStr.toString();
	}

	/**
	 * 转换文件路径
	 * 
	 * @Title: getFileFullUrl
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param imagePath
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getFileFullUrl(String filePath) {
		if (StringUtil.isEmpty(filePath)) {
			return "";
		}

		if (filePath.indexOf("http:") != -1 || filePath.indexOf("HTTP:") != -1 || filePath.indexOf("https:") != -1 || filePath.indexOf("HTTPS:") != -1) {
			return filePath;
		}

		String returnUrl = Cache.getResource("img.webRoot");

		if (StringUtil.isEmpty(returnUrl)) {
			returnUrl = getWebFullUrlPrex();
		}

		if (!StringUtil.isEmpty(filePath)) {
			if (!filePath.startsWith("/")) {
				if (returnUrl.endsWith("/")) {
					returnUrl = returnUrl + filePath;
				} else {
					returnUrl = returnUrl + "/" + filePath;
				}
			} else {
				returnUrl = returnUrl + filePath;
			}
		}

		return returnUrl;
	}
}
