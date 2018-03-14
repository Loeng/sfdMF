package com.ekfans.controllers.tools.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.JsonUtil;
import com.ekfans.pub.util.StringUtil;

/**
 * 类说明
 * 
 * @author lgy
 * @version 创建时间：2012-9-27 下午03:54:37
 */
public class FileUploadUtil {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String picUpload(HttpServletRequest request, String property, String type) throws IOException {
		String path = Cache.getResource("file.temp.directory");

		String date = DateUtil.getSysDateString();
		// 获取文件大小
		String fileSize = request.getParameter("fileSize");
		// 存相对路径
		String filePath = "";
		if (!StringUtil.isEmpty(path)) {
			if (path.endsWith("/")) {
				filePath = path + "customerfiles/fileTemp/" + date;
			} else {
				filePath = path + "/customerfiles/fileTemp/" + date;
			}
		} else {
			ServletContext servletContext = request.getSession().getServletContext();
			filePath = servletContext.getRealPath("/") + "/customerfiles/fileTemp/" + date;
		}

		File uploadPath = new File(filePath);
		// 检查文件夹是否存在 不存在 创建一个
		if (!uploadPath.exists()) {
			uploadPath.mkdirs();
		}

		// 文件最大容量，默认1M
		double size = 1.0;
		if ("file".equals(type)) {
			size = 50.0;
		}
		if (!StringUtil.isEmpty(fileSize)) {
			try {
				size = Double.parseDouble(fileSize);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		// 文件名
		String fileName = "";

		// 上传文件
		MultipartFile file = null;
		try {

			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			// 获得文件：
			file = multipartRequest.getFile(property + "InputFile");
			if (file != null) {
				
				// 判断文件大小1024 * 1024 = 1M
				if (file.getSize() > size * 1024 * 1024) {
					throw new RuntimeException("文件不能大于"+size+"M!");
				}
				
				
				File toFile;
				InputStream is = file.getInputStream();
				String uploadFileName = file.getOriginalFilename();
				// fileName = uploadFileName.substring(0,
				// uploadFileName.lastIndexOf("."));
				// fileName += "-" + DateUtil.getSysDateTimeString();
				fileName += java.util.UUID.randomUUID().toString();
				fileName += uploadFileName.substring(uploadFileName.lastIndexOf("."));

				toFile = new File(uploadPath, fileName);
				if (toFile.exists()) {
					fileName = uploadFileName.substring(0, uploadFileName.lastIndexOf("."));
					fileName += DateUtil.getSysDateLong();
					fileName += uploadFileName.substring(uploadFileName.lastIndexOf("."));
					toFile = new File(uploadPath, fileName);
				}
				OutputStream os = new FileOutputStream(toFile);
				byte[] buffer = new byte[(int) (1024 * size)];
				int length = 0;
				while ((length = is.read(buffer)) > 0) {
					os.write(buffer, 0, length);
				}
				is.close();
				os.close();
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			HashMap localHashMap1 = new HashMap();
			localHashMap1.put("errorMessage", e.getMessage());
			return JsonUtil.convertToJsonString(localHashMap1);
		}

		HashMap localHashMap1 = new HashMap();

		if ("pic".equals(type)) {
			String fileUrl = Cache.getResource("file.temp.weburl");

			if (StringUtil.isEmpty(fileUrl)) {
				fileUrl = "http://" + request.getServerName();
				if (!"80".equals(request.getServerPort())) {
					fileUrl = fileUrl + ":" + request.getServerPort();
				}
				fileUrl = fileUrl + request.getContextPath();
			}

			if (fileUrl.endsWith("/")) {
				fileUrl = fileUrl + "customerfiles/fileTemp/" + date + "/" + fileName;
			} else {
				fileUrl = fileUrl + "/customerfiles/fileTemp/" + date + "/" + fileName;
			}

			localHashMap1.put("fileUrl", fileUrl);
			localHashMap1.put("realPath", filePath + "/" + fileName);
		} else {
			String fileUrl = "/customerfiles/fileTemp/" + date + "/" + fileName;
			localHashMap1.put("fileName", fileName);
			localHashMap1.put("fileUrl", fileUrl);
			String link = FileUploadHelper.getFilePurviewUrl(request, fileUrl);
			localHashMap1.put("purviewPath", link);
		}
		localHashMap1.put("errorMessage", "");

		return JsonUtil.convertToJsonString(localHashMap1);
	}
}
