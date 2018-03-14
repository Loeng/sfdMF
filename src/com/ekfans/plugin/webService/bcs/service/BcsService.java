package com.ekfans.plugin.webService.bcs.service;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService(serviceName = "bcsService", wsdlLocation = "http://182.131.21.111:59/services/bcsService.wsdl", targetNamespace = "http://www.sfdhb.com")
public class BcsService implements IBcsService {

	@WebMethod
	public String request(String xml) {

		System.out.println("+++++++++++++++++++++++++++++++");
		System.out.println("+++++++++++++++++++++++++++++++");
		System.out.println("+++++++++++++++++++++++++++++++");
		System.out.println(xml);
		System.out.println("+++++++++++++++++++++++++++++++");
		System.out.println("+++++++++++++++++++++++++++++++");
		System.out.println("+++++++++++++++++++++++++++++++");
		String returnXml = BcsServiceHelper.getReturnStr(xml);

		System.out.println("********************************");
		System.out.println("********************************");
		System.out.println("********************************");
		System.out.println(returnXml);
		System.out.println("********************************");
		System.out.println("********************************");
		System.out.println("********************************");

		return returnXml;
	}

}
