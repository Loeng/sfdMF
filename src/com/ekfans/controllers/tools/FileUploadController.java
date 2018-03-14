package com.ekfans.controllers.tools;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.guice.RequestParameters;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.controllers.tools.util.FileUploadUtil;
import com.ekfans.pub.util.FileUtil;
import com.ekfans.pub.util.JsonUtil;
import com.ekfans.pub.util.StringUtil;

/**
 * 文件上传的Controller
 * 
 * @ClassName: FileUploadController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author liuguoyu
 * @date 2014-5-7 下午08:33:38
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@Controller
@RequestMapping(value = "/upload")
public class FileUploadController {

	public FileUploadController() {

	}

	/**
	 * 图片上传
	 * 
	 * @Title: fileUpload
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param property
	 * @param @param request
	 * @param @param response 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/picUpload/{property}", method = RequestMethod.POST)
	public void picUpload(@PathVariable("property") String property, HttpServletRequest request, HttpServletResponse response) {
		try {
			String jsonStr = FileUploadUtil.picUpload(request, property, "pic");
			OutputStream output = response.getOutputStream();
			output.write(jsonStr.getBytes());
			output.flush();
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 文件上传
	 * 
	 * @Title: fileUpload
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param property
	 * @param @param request
	 * @param @param response 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/fileUpload/{property}", method = RequestMethod.POST)
	@ResponseBody
	public void fileUpload(@PathVariable("property") String property, HttpServletRequest request, HttpServletResponse response) {
		try {
			String jsonStr = FileUploadUtil.picUpload(request, property, "file");
			OutputStream output = response.getOutputStream();
			output.write(jsonStr.getBytes("UTF-8"));
			output.flush();
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/fileRemove", method = RequestMethod.GET)
	public void fileRemove(@RequestParameters String filePath, HttpServletRequest request, HttpServletResponse response) {
		HashMap localHashMap1 = new HashMap();

		try {
			ServletContext servletContext = request.getSession().getServletContext();
			if (!StringUtil.isEmpty(filePath)) {
				String realPath = "";
				if (filePath.startsWith("/customerfiles") || filePath.startsWith("\\customerfiles") || filePath.startsWith("customerfiles")) {
					realPath = servletContext.getRealPath(filePath);
				} else {
					realPath = filePath;
				}
				// 调用方法移除文件
				boolean status = FileUtil.deleteFile(realPath);
				localHashMap1.put("remove", status);
			} else {
				localHashMap1.put("remove", false);
			}

			OutputStream output = response.getOutputStream();
			output.write(JsonUtil.convertToJsonString(localHashMap1).getBytes());
			output.flush();
			output.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
