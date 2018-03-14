package com.ekfans.controllers.tools.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.ekfans.base.content.model.AdDetail;
import com.ekfans.base.content.model.ShopAd;
import com.ekfans.base.system.util.AdvertConst;
import com.ekfans.plugin.page.BasicRequest;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.StringUtil;

/**
 * 
 * @ClassName: AdvertXmlUtil
 * @Description: TODO(广告xml文件读取工具类)
 * @author ekfans
 * @date 2014-6-10 上午9:19:40
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class AdvertXmlUtil {
	public static final String xmlPath = "/WEB-INF/xmls/advert.xml";

	/**
	 * 
	 * @Title: writeXmlToJetx
	 * @Description: TODO(读取广告对应节点，写入jetx文件) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @param showType
	 *            广告显示风格
	 * @param addTit
	 *            是否添加tit节点
	 * @return boolean 返回类型
	 * @throws
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void getAdHtmlWithSystem(HttpServletRequest request, ShopAd shopAd) {
		// 如果传入的显示类型为空，返回false
		if (shopAd == null) {
			return;
		}

		BasicRequest brequest = new BasicRequest(request);

		String urlPrex = brequest.getWebFullUrlPrex();

		// 定义解析器
		SAXReader reader = new SAXReader();
		try {
			ServletContext servletContext = request.getSession().getServletContext();
			// 得到xml路径
			String path = servletContext.getRealPath("/") + xmlPath;

			// 得到xml文件
			File file = new File(path);
			// 获取根节点
			Document document = reader.read(file);
			// 获取对应的广告节点
			Element element = document.elementByID(shopAd.getShowType());

			// 如果需要添加tit节点，则添加
			// 获取广告tit节点
			Element tit = document.elementByID("tit");
			// 获取广告类型名节点
			Element adName = tit.elementByID("adName");
			// 设置广告样式名
			adName.setText(AdvertConst.typeNameMap.get(shopAd.getShowType()));
			// 添加tit节点
			List list = element.content();
			list.add(0, tit);

			// 获取正文的节点
			Element contentElement = element.elementByID("CONTENTS");

			// 获取正文节点的Style字段
			Attribute attribute = contentElement.attribute("style");
			if (attribute != null) {
				// 如果广告的宽高都大于0，则设置Style的宽高
				if (shopAd.getWidth() > 0 && shopAd.getHigh() > 0) {
					attribute.setValue("width:" + shopAd.getWidth() + "px;height:" + shopAd.getHigh() + "px;");
				} else if (shopAd.getWidth() > 0 && shopAd.getHigh() <= 0) {
					// 如果广告的宽大于0，而高小于或等于0，则只设置宽
					attribute.setValue("width:" + shopAd.getWidth() + "px;");
				} else if (shopAd.getWidth() <= 0 && shopAd.getHigh() > 0) {
					// 如果广告的宽小于或等于0，而高大于0，则只设置高
					attribute.setValue("height:" + shopAd.getHigh() + "px;");
				} else {
					// 否则，宽高都不设置
					attribute.setValue("");
				}
			}

			// 获取需要设置宽高的节点
			Element childContent = contentElement.elementByID("content");
			;
			if (childContent != null) {
				// 获取节点的Style字段
				Attribute attribute1 = childContent.attribute("style");
				if (attribute1 != null) {
					// 如果广告的宽高都大于0，则设置Style的宽高
					if (shopAd.getWidth() > 0 && shopAd.getHigh() > 0) {
						attribute1.setValue("width:" + shopAd.getWidth() + "px;height:" + shopAd.getHigh() + "px;");
					} else if (shopAd.getWidth() > 0 && shopAd.getHigh() <= 0) {
						// 如果广告的宽大于0，而高小于或等于0，则只设置宽
						attribute1.setValue("width:" + shopAd.getWidth() + "px;");
					} else if (shopAd.getWidth() <= 0 && shopAd.getHigh() > 0) {
						// 如果广告的宽小于或等于0，而高大于0，则只设置高
						attribute1.setValue("height:" + shopAd.getHigh() + "px;");
					} else {
						// 否则，宽高都不设置
						attribute1.setValue("");
					}
				}
			}

			// 获取广告需要替换的第一个节点
			Element childElement = contentElement.elementByID("details");
			// 如果需要替换的第一个节点不为空，则将需要替换的节点替换掉，循环增加
			if (childElement != null) {
				// 获取广告的明细集合
				List<AdDetail> detailList = shopAd.getDetailist();

				// 再一次获取details子节点
				Element childElement2 = contentElement.elementByID("details");
				// 获取该子节点的父节点
				Element pEle = childElement2.getParent();
				// 便利广告的明细
				if (detailList != null && detailList.size() > 0) {
					for (int i = 0; i < detailList.size(); i++) {

						// 获取class属性
						Attribute classAttribute = childElement.attribute("class");
						if (classAttribute != null && !StringUtil.isEmpty(classAttribute.getValue())) {
							if (classAttribute.getValue().indexOf(AdvertConst.STR_IF_START) != -1 || classAttribute.getValue().equals("current")) {
								if (i == 0) {
									classAttribute.setValue("current");
								} else {
									classAttribute.setValue("");
								}
							}
						}

						// 将子节点转成字符串
						String elementStr = childElement.asXML();

						// 获取明细
						AdDetail detail = detailList.get(i);
						elementStr = covertStr(urlPrex, elementStr, detail, shopAd, shopAd.getWidth(), shopAd.getHigh());

						if (elementStr.indexOf(AdvertConst.STR_FOR_INDEX) != -1) {
							elementStr = elementStr.replaceAll(AdvertConst.STR_FOR_INDEX_REPLACE, i + 1 + "");
						}
						Document d = DocumentHelper.parseText(elementStr);

						if (i == 0) {
							pEle.remove(childElement2);
						}
						pEle.add(d.getRootElement());
					}
				}

			}

			// 获取广告需要替换的第二个节点
			Element childElement2 = contentElement.elementByID("details2");
			// 如果需要替换的第一个节点不为空，则将需要替换的节点替换掉，循环增加
			if (childElement2 != null) {
				// 再一次获取details2子节点
				Element childElements2 = contentElement.elementByID("details2");
				// 获取该子节点的父节点
				Element pEle2 = childElements2.getParent();
				// 获取广告的明细集合
				List<AdDetail> detailList = shopAd.getDetailist();
				// 便利广告的明细
				if (detailList != null && detailList.size() > 0) {
					for (int i = 0; i < detailList.size(); i++) {

						// 获取class属性
						Attribute classAttribute = childElement2.attribute("class");
						if (classAttribute != null && !StringUtil.isEmpty(classAttribute.getValue())) {
							if (classAttribute.getValue().indexOf(AdvertConst.STR_IF_START) != -1 || classAttribute.getValue().equals("current")) {
								if (i == 0) {
									classAttribute.setValue("current");
								} else {
									classAttribute.setValue("");
								}
							}
						}

						// 获取需要设置宽高的节点
						// Element imgElements =
						// childElement.elementByID("IMGS");

						Element imgElements = null;

						if (childElement.element("a") != null) {
							imgElements = childElement.element("a").element("img");
						} else if (childElement.element("img") != null) {
							imgElements = childElement.element("img");
						}

						if (imgElements != null) {
							// 获取节点的Style字段
							Attribute attribute1 = imgElements.attribute("style");
							if (attribute1 != null) {
								// 如果广告的宽高都大于0，则设置Style的宽高
								if (shopAd.getWidth() > 0 && shopAd.getHigh() > 0) {
									attribute1.setValue("width:" + shopAd.getWidth() + "px;height:" + shopAd.getHigh() + "px;");
								} else if (shopAd.getWidth() > 0 && shopAd.getHigh() <= 0) {
									// 如果广告的宽大于0，而高小于或等于0，则只设置宽
									attribute1.setValue("width:" + shopAd.getWidth() + "px;");
								} else if (shopAd.getWidth() <= 0 && shopAd.getHigh() > 0) {
									// 如果广告的宽小于或等于0，而高大于0，则只设置高
									attribute1.setValue("height:" + shopAd.getHigh() + "px;");
								} else {
									// 否则，宽高都不设置
									attribute1.setValue("");
								}
							}
						}

						// 将子节点转成字符串
						String elementStr = childElement2.asXML();

						// 获取明细
						AdDetail detail = detailList.get(i);
						elementStr = covertStr(urlPrex, elementStr, detail, shopAd, shopAd.getWidth(), shopAd.getHigh());

						if (elementStr.indexOf(AdvertConst.STR_FOR_INDEX) != -1) {
							elementStr = elementStr.replaceAll(AdvertConst.STR_FOR_INDEX_REPLACE, i + 1 + "");
						}

						Document d = DocumentHelper.parseText(elementStr);
						if (i == 0) {
							pEle2.remove(childElements2);
						}
						pEle2.add(d.getRootElement());
					}
				}
			}
			String elementStr = covertStr(urlPrex, element.asXML(), null, shopAd, shopAd.getWidth(), shopAd.getHigh());
			shopAd.setHtml(elementStr);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Title: getAdHtmlWithWeb
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param shopAd 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public static void getAdHtmlWithWeb(HttpServletRequest request, ShopAd shopAd) {
		// 如果传入的显示类型为空，返回false
		if (shopAd == null) {
			return;
		}

		if (shopAd.getType().equals("2")) {
			// 获取广告的明细集合
			List<AdDetail> detailList = shopAd.getDetailist();
			if (detailList != null && detailList.size() > 0) {
				AdDetail detail = detailList.get(0);
				if (detail != null) {
					shopAd.setHtml(detail.getAdContent());
				}
			}
			return;

		}

		BasicRequest brequest = new BasicRequest(request);
		String urlPrex = brequest.getWebFullUrlPrex();

		// 定义解析器
		SAXReader reader = new SAXReader();
		try {

			ServletContext servletContext = request.getSession().getServletContext();
			// 得到xml路径
			String path = servletContext.getRealPath("/") + xmlPath;
			// 得到xml文件
			File file = new File(path);
			// 获取根节点
			Document document = reader.read(file);
			// 获取对应的广告节点
			Element element = document.elementByID(shopAd.getShowType());

			// 获取正文的节点
			Element contentElement = element.elementByID("CONTENTS");

			// 获取正文节点的Style字段
			Attribute attribute = contentElement.attribute("style");
			if (attribute != null) {
				// 如果广告的宽高都大于0，则设置Style的宽高
				if (shopAd.getWidth() > 0 && shopAd.getHigh() > 0) {
					attribute.setValue("width:" + shopAd.getWidth() + "px;height:" + shopAd.getHigh() + "px;");
				} else if (shopAd.getWidth() > 0 && shopAd.getHigh() <= 0) {
					// 如果广告的宽大于0，而高小于或等于0，则只设置宽
					attribute.setValue("width:" + shopAd.getWidth() + "px;");
				} else if (shopAd.getWidth() <= 0 && shopAd.getHigh() > 0) {
					// 如果广告的宽小于或等于0，而高大于0，则只设置高
					attribute.setValue("height:" + shopAd.getHigh() + "px;");
				} else {
					// 否则，宽高都不设置
					attribute.setValue("");
				}
			}

			// 获取需要设置宽高的节点
			Element childContent = contentElement.elementByID("content");
			if (childContent != null) {
				// 获取节点的Style字段
				Attribute attribute1 = childContent.attribute("style");
				if (attribute1 != null) {
					// 如果广告的宽高都大于0，则设置Style的宽高
					if (shopAd.getWidth() > 0 && shopAd.getHigh() > 0) {
						attribute1.setValue("width:" + shopAd.getWidth() + "px;height:" + shopAd.getHigh() + "px;");
					} else if (shopAd.getWidth() > 0 && shopAd.getHigh() <= 0) {
						// 如果广告的宽大于0，而高小于或等于0，则只设置宽
						attribute1.setValue("width:" + shopAd.getWidth() + "px;");
					} else if (shopAd.getWidth() <= 0 && shopAd.getHigh() > 0) {
						// 如果广告的宽小于或等于0，而高大于0，则只设置高
						attribute1.setValue("height:" + shopAd.getHigh() + "px;");
					} else {
						// 否则，宽高都不设置
						attribute1.setValue("");
					}
				}
			}

			// 获取广告需要替换的第一个节点
			Element childElement = contentElement.elementByID("details");
			// 如果需要替换的第一个节点不为空，则将需要替换的节点替换掉，循环增加
			if (childElement != null) {
				// 获取广告的明细集合
				List<AdDetail> detailList = shopAd.getDetailist();

				// 再一次获取details子节点
				Element childElement2 = contentElement.elementByID("details");
				// 获取该子节点的父节点
				Element pEle = childElement2.getParent();
				// 便利广告的明细
				if (detailList != null && detailList.size() > 0) {
					for (int i = 0; i < detailList.size(); i++) {
						// 获取class属性
						Attribute classAttribute = childElement.attribute("class");
						if (classAttribute != null && !StringUtil.isEmpty(classAttribute.getValue())) {
							if (classAttribute.getValue().indexOf(AdvertConst.STR_IF_START) != -1 || classAttribute.getValue().equals("current")) {
								if (i == 0) {
									classAttribute.setValue("current");
								} else {
									classAttribute.setValue("");
								}
							}
						}

						// 获取需要设置宽高的节点
						// Element imgElements =
						// childElement.elementByID("IMGS");

						Element imgElements = null;

						if (childElement.element("a") != null) {
							imgElements = childElement.element("a").element("img");
						} else if (childElement.element("img") != null) {
							imgElements = childElement.element("img");
						}

						if (imgElements != null) {
							// 获取节点的Style字段
							Attribute attribute1 = imgElements.attribute("style");
							if (attribute1 != null) {
								// 如果广告的宽高都大于0，则设置Style的宽高
								if (shopAd.getWidth() > 0 && shopAd.getHigh() > 0) {
									attribute1.setValue("width:" + shopAd.getWidth() + "px;height:" + shopAd.getHigh() + "px;");
								} else if (shopAd.getWidth() > 0 && shopAd.getHigh() <= 0) {
									// 如果广告的宽大于0，而高小于或等于0，则只设置宽
									attribute1.setValue("width:" + shopAd.getWidth() + "px;");
								} else if (shopAd.getWidth() <= 0 && shopAd.getHigh() > 0) {
									// 如果广告的宽小于或等于0，而高大于0，则只设置高
									attribute1.setValue("height:" + shopAd.getHigh() + "px;");
								} else {
									// 否则，宽高都不设置
									attribute1.setValue("");
								}
							}
						}

						// 将子节点转成字符串
						String elementStr = childElement.asXML();

						// 获取明细
						AdDetail detail = detailList.get(i);
						elementStr = covertStr(urlPrex, elementStr, detail, shopAd, shopAd.getWidth(), shopAd.getHigh());

						if (elementStr.indexOf(AdvertConst.STR_FOR_INDEX) != -1) {
							elementStr = elementStr.replaceAll(AdvertConst.STR_FOR_INDEX_REPLACE, i + 1 + "");
						}
						Document d = DocumentHelper.parseText(elementStr);

						if (i == 0) {
							pEle.remove(childElement2);
						}
						pEle.add(d.getRootElement());
					}
				}

			}

			// 获取广告需要替换的第二个节点
			Element childElement2 = contentElement.elementByID("details2");
			// 如果需要替换的第一个节点不为空，则将需要替换的节点替换掉，循环增加
			if (childElement2 != null) {
				// 再一次获取details2子节点
				Element childElements2 = contentElement.elementByID("details2");
				// 获取该子节点的父节点
				Element pEle2 = childElements2.getParent();
				// 获取广告的明细集合
				List<AdDetail> detailList = shopAd.getDetailist();
				// 便利广告的明细
				if (detailList != null && detailList.size() > 0) {
					for (int i = 0; i < detailList.size(); i++) {

						// 获取class属性
						Attribute classAttribute = childElement2.attribute("class");
						if (classAttribute != null && !StringUtil.isEmpty(classAttribute.getValue())) {
							if (classAttribute.getValue().indexOf(AdvertConst.STR_IF_START) != -1 || classAttribute.getValue().equals("current")) {
								if (i == 0) {
									classAttribute.setValue("current");
								} else {
									classAttribute.setValue("");
								}
							}
						}

						// 将子节点转成字符串
						String elementStr = childElement2.asXML();

						// 获取明细
						AdDetail detail = detailList.get(i);
						elementStr = covertStr(urlPrex, elementStr, detail, shopAd, shopAd.getWidth(), shopAd.getHigh());

						if (elementStr.indexOf(AdvertConst.STR_FOR_INDEX) != -1) {
							elementStr = elementStr.replaceAll(AdvertConst.STR_FOR_INDEX_REPLACE, i + 1 + "");
						}

						Document d = DocumentHelper.parseText(elementStr);
						if (i == 0) {
							pEle2.remove(childElements2);
						}
						pEle2.add(d.getRootElement());
					}
				}
			}
			String elementStr = covertStr(urlPrex, contentElement.asXML(), null, shopAd, shopAd.getWidth(), shopAd.getHigh());
			shopAd.setHtml(elementStr);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 转换XML的字符串
	 * 
	 * @Title: covertStr
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param xmlStr
	 * @param @param detil 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public static String covertStr(String contextPath, String xmlStr, AdDetail detail, ShopAd shopAd, double width, double height) {
		// 如果传进来的字符串为空，或者明细为空，则直接返回
		if (StringUtil.isEmpty(xmlStr)) {
			return null;
		}

		// 替换广告标题
		if (xmlStr.indexOf(AdvertConst.STR_AD_NAME) != -1 && detail != null) {
			xmlStr = xmlStr.replaceAll(AdvertConst.STR_AD_NAME_REPLACE, shopAd.getName());
		}

		// 替换广告文字
		if (xmlStr.indexOf(AdvertConst.STR_AD_CONTENT) != -1 && detail != null) {
			xmlStr = xmlStr.replaceAll(AdvertConst.STR_AD_CONTENT_REPLACE, detail.getAdContent());
		}

		// 替换广告宽度
		if (xmlStr.indexOf(AdvertConst.STR_PIC_WIDTH) != -1 && detail != null) {
			if (width != 0) {
				xmlStr = xmlStr.replaceAll(AdvertConst.STR_PIC_WIDTH_REPLACE, width + "");
			} else {

			}
		}

		// 替换广告高度
		if (xmlStr.indexOf(AdvertConst.STR_PIC_HEIGHT) != -1 && detail != null) {
			xmlStr = xmlStr.replaceAll(AdvertConst.STR_PIC_HEIGHT_REPLACE, height + "");
		}

		// 替换上下文路径
		if (xmlStr.indexOf(AdvertConst.STR_CONTEXT_PATH) != -1) {
			xmlStr = xmlStr.replaceAll(AdvertConst.STR_CONTEXT_PATH_REPLACE, contextPath);
		}

		// 替换广告图片
		if (xmlStr.indexOf(AdvertConst.STR_IMG_URL) != -1 && detail != null) {
			xmlStr = xmlStr.replaceAll(AdvertConst.STR_IMG_URL_REPLACE, contextPath + detail.getAdPicture());
		}

		// 替换广告链接路径
		if (xmlStr.indexOf(AdvertConst.STR_LINK_URL) != -1 && detail != null) {
			xmlStr = xmlStr.replaceAll(AdvertConst.STR_LINK_URL_REPLACE, detail.getLinkAddress());
		}

		// 替换广告链接路径
		if (xmlStr.indexOf(AdvertConst.STR_AD_COLOR) != -1 && detail != null) {
			xmlStr = xmlStr.replaceAll(AdvertConst.STR_AD_COLOR_REPLACE, detail.getContentColor());
		}
		return xmlStr;
	}

	/**
	 * for test
	 * 
	 * @Title: main
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param args 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public static void main(String[] args) {
		ShopAd shopAd = new ShopAd();
		shopAd.setWidth(800);
		shopAd.setHigh(800);
		shopAd.setCreateTime(DateUtil.getSysDateTimeString());
		shopAd.setName("aaaa");
		shopAd.setShowType(AdvertConst.SHOW_TYPE_BIGSMALL);
		shopAd.setType("1");
		List<AdDetail> list = new ArrayList<AdDetail>();
		AdDetail detail = new AdDetail();
		detail.setAdContent("test Ad Content");
		detail.setAdId("test Ad Id");
		detail.setAdPicture("test Ad Picture");
		detail.setContentColor("test Ad Color");
		detail.setLinkAddress("www.baidu.com");
		detail.setPosition(1);
		list.add(detail);

		AdDetail detail2 = new AdDetail();
		detail2.setAdContent("test Ad Content222");
		detail2.setAdId("test Ad Id222");
		detail2.setAdPicture("test Ad Picture222");
		detail2.setContentColor("test Ad Color222");
		detail2.setLinkAddress("www.baidu.com222");
		detail2.setPosition(2);
		list.add(detail2);
		shopAd.setDetailist(list);
		// AdvertXmlUtil.getAdHtmlWithWeb(shopAd);
	}
}
