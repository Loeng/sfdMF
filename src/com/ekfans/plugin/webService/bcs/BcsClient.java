package com.ekfans.plugin.webService.bcs;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.InputSource;

import com.ekfans.base.store.model.AccountBankLog;
import com.ekfans.plugin.cache.base.Cache;

@SuppressWarnings("unchecked")
public class BcsClient {
	public static String client(String methdName, String xmlStr) {

		try {
			String url = Cache.getBCSResource("BCS.WSDL.URL");
			System.out.println("--------------------------------");
			System.out.println("--------------------------------");
			System.out.println("--------------------------------");
			System.out.println(xmlStr);
			System.out.println("--------------------------------");
			System.out.println("--------------------------------");
			System.out.println("--------------------------------");
			// 使用RPC方式调用WebService
			RPCServiceClient serviceClient = new RPCServiceClient();
			// 指定调用WebService的URL
			EndpointReference targetEPR = new EndpointReference(url);
			Options options = serviceClient.getOptions();
			// 确定目标服务地址
			options.setTo(targetEPR);
			// 确定调用方法
			options.setAction("urn:request");

			/**
			 * 指定要调用的方法及WSDL文件的命名空间 如果 webservice 服务端由axis2编写 命名空间 不一致导致的问题
			 * org.apache.axis2.AxisFault: java.lang.RuntimeException:
			 * Unexpected subelement arg0
			 */
			QName qname = new QName(Cache.getBCSResource("BCS.WSDL.QNAME"), "request");
			// 指定getPrice方法的参数值
			Object[] parameters = new Object[] { xmlStr };

			// 指定getPrice方法返回值的数据类型的Class对象
			// Class[] returnTypes = new Class[] { String.class };

			// 调用方法一 传递参数，调用服务，获取服务返回结果集
			OMElement element = serviceClient.invokeBlocking(qname, parameters);
			// 值得注意的是，返回结果就是一段由OMElement对象封装的xml字符串。
			// 我们可以对之灵活应用,下面我取第一个元素值，并打印之。因为调用的方法返回一个结果
			String result = element.getFirstElement().getText();
			System.out.println("==================================");
			System.out.println("==================================");
			System.out.println("==================================");
			System.out.println(result);
			System.out.println("==================================");
			System.out.println("==================================");
			System.out.println("==================================");

			// // 调用方法二 getPrice方法并输出该方法的返回值
			// Object[] response = serviceClient.invokeBlocking(qname,
			// parameters, returnTypes);
			// // String r = (String) response[0];
			// String r = (String) response[0];
			// System.out.println(r);
			return result;
		} catch (AxisFault e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Map<String, Object> getMapFromBCS(String methdName, String mId, Map<String, Object> map, AccountBankLog log) {

		String xmlStr = BcsXmlHelper.mapToXml(methdName, mId, map, log);
		log.setType(methdName);
		Map<String, Object> reMap = new HashMap<String, Object>();
		try {
			String reXmlStr = client(methdName, xmlStr);
			log.setReMessage(reXmlStr);
			// 创建一个新的字符串
			StringReader read = new StringReader(reXmlStr);
			// 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
			InputSource source = new InputSource(read);
			SAXBuilder builder = new SAXBuilder();
			Document docum = builder.build(source);
			Element serviceElement = docum.getRootElement();
			Element headerElement = serviceElement.getChild("Header");
			if (headerElement != null) {
				Element responseElement = headerElement.getChild("Response");
				List<Element> elements = responseElement.getChildren();
				if (elements != null && elements.size() > 0) {
					for (int i = 0; i < elements.size(); i++) {
						Element el = elements.get(i);
						String name = el.getName();
						if ("List".equals(name)) {
							List<Map<String, Object>> childs = (List<Map<String, Object>>) reMap.get("List");
							if (childs == null) {
								childs = new ArrayList<Map<String, Object>>();
							}
							List<Element> lists = el.getChildren();
							Map<String, Object> cMap = new HashMap<String, Object>();
							if (lists != null && lists.size() > 0) {
								for (int j = 0; j < lists.size(); j++) {
									Element el2 = lists.get(j);
									String name2 = el2.getName();
									String value2 = el2.getValue();
									cMap.put(name2, value2);
								}
							}
							childs.add(cMap);
							reMap.put(name, childs);
						} else {
							String value = el.getValue();
							reMap.put(name, value);
						}
					}
				}

			}
			Element bodyElement = serviceElement.getChild("Body");

			if ("1".equals(Cache.getBCSResource("Encrypt"))) {
				// /验签----

				String bodyStr = reXmlStr.substring(reXmlStr.indexOf("<Body>"), reXmlStr.indexOf("</Body>") + 7);
				System.out.println(bodyElement.toString());
				Boolean checkStatus = BcsSignHelper.checkSign(headerElement.getChildText("SignData"), bodyStr);
				if (!checkStatus) {
					reMap.put("ReturnCode", "9999");
					reMap.put("ReturnMessage", "签名验证错误");
					return reMap;
				}
			}

			if (bodyElement != null) {
				Element responseElement = bodyElement.getChild("Response");
				if (responseElement != null) {
					List<Element> elements = responseElement.getChildren();
					if (elements != null && elements.size() > 0) {
						for (int i = 0; i < elements.size(); i++) {
							Element el = elements.get(i);
							String name = el.getName();
							if ("List".equals(name)) {
								List<Map<String, Object>> childs = (List<Map<String, Object>>) reMap.get("List");
								if (childs == null) {
									childs = new ArrayList<Map<String, Object>>();
								}
								List<Element> lists = el.getChildren();
								Map<String, Object> cMap = new HashMap<String, Object>();
								if (lists != null && lists.size() > 0) {
									for (int j = 0; j < lists.size(); j++) {
										Element el2 = lists.get(j);
										String name2 = el2.getName();
										String value2 = el2.getValue();
										cMap.put(name2, value2);
									}
								}
								childs.add(cMap);
								reMap.put(name, childs);
							} else {
								String value = el.getValue();
								reMap.put(name, value);
							}
						}
					}
				}
			}
			return reMap;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static void main(String[] args) {

		try {
			String url = "http://localhost:8080/services/bcsService?wsdl";

			// 使用RPC方式调用WebService
			RPCServiceClient serviceClient = new RPCServiceClient();
			// 指定调用WebService的URL
			EndpointReference targetEPR = new EndpointReference(url);
			Options options = serviceClient.getOptions();
			// 确定目标服务地址
			options.setTo(targetEPR);
			// 确定调用方法
			options.setAction("urn:request");

			/**
			 * 指定要调用的方法及WSDL文件的命名空间 如果 webservice 服务端由axis2编写 命名空间 不一致导致的问题
			 * org.apache.axis2.AxisFault: java.lang.RuntimeException:
			 * Unexpected subelement arg0
			 */
			QName qname = new QName("http://service.bcs.webService.plugin.ekfans.com", "request");
			// 指定getPrice方法的参数值
			Object[] parameters = new Object[] { "123123123" };

			// 指定getPrice方法返回值的数据类型的Class对象
			// Class[] returnTypes = new Class[] { String.class };

			// 调用方法一 传递参数，调用服务，获取服务返回结果集
			OMElement element = serviceClient.invokeBlocking(qname, parameters);
			// 值得注意的是，返回结果就是一段由OMElement对象封装的xml字符串。
			// 我们可以对之灵活应用,下面我取第一个元素值，并打印之。因为调用的方法返回一个结果
			String result = element.getFirstElement().getText();
			System.out.println(result);

			// // 调用方法二 getPrice方法并输出该方法的返回值
			// Object[] response = serviceClient.invokeBlocking(qname,
			// parameters, returnTypes);
			// // String r = (String) response[0];
			// String r = (String) response[0];
			// System.out.println(r);
		} catch (AxisFault e) {
			e.printStackTrace();
		}
	}

	public static String utfToGBK(String t) {
		String utf8 = "";
		try {
			utf8 = new String(t.getBytes("UTF-8"));
			String unicode = new String(utf8.getBytes(), "UTF-8");
			String gbk = new String(unicode.getBytes("GBK"));
			return gbk;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return t;
	}
}
