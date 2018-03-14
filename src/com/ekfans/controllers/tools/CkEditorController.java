package com.ekfans.controllers.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ekfans.basic.controller.BasicController;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.pub.util.StringUtil;

/**
 * CKEditor图片上传实现Controller
 * 
 * @ClassName: CkEditorController
 * @author liuguoyu
 * @date 2014-5-5 下午10:39:22
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
@RequestMapping("/ckeditor")
public class CkEditorController extends BasicController {

	/**
	 * 图片上传方法
	 * 
	 * @Title: upload
	 * @param @param request
	 * @param @param response
	 * @param @param session
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/upload")
	public String upload(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		// 获得第1张图片（根据前台的name名称得到上传的文件）
		MultipartFile upload = multipartRequest.getFile("upload");

		// 对文件进行校验
		if (upload == null) {
			out.print("<font color=\"red\" size=\"2\">*请选择上传文件</font>");
			return null;
		}

		String uploadContentType = upload.getContentType();
		String uploadFileName = upload.getOriginalFilename();

		if ((uploadContentType.equals("image/jpeg") || uploadContentType.equals("image/jpeg")) && uploadFileName.substring(uploadFileName.length() - 4).toLowerCase().equals(".jpg")) {
			// IE6上传jpg图片的headimageContentType是image/pjpeg，而IE9以及火狐上传的jpg图片是image/jpeg
		} else if (uploadContentType.equals("image/png") && uploadFileName.substring(uploadFileName.length() - 4).toLowerCase().equals(".png")) {

		} else if (uploadContentType.equals("image/gif") && uploadFileName.substring(uploadFileName.length() - 4).toLowerCase().equals(".gif")) {

		} else if (uploadContentType.equals("image/bmp") && uploadFileName.substring(uploadFileName.length() - 4).toLowerCase().equals(".bmp")) {

		} else {
			out.print("<font color=\"red\" size=\"2\">*文件格式不正确（必须为.jpg/.gif/.bmp/.png文件）</font>");
			return null;
		}

		if (upload.getSize() > 600 * 1024) {
			out.print("<font color=\"red\" size=\"2\">*文件大小不得大于600k</font>");
			return null;
		}

		String imgPath = Cache.getResource("ckeditor.img.path");
		// 将文件保存到项目目录下
		InputStream is = upload.getInputStream();
		ServletContext servletContext = request.getSession().getServletContext();
		String uploadPath = servletContext.getRealPath(imgPath); // 设置保存目录
		File upFile = new File(uploadPath);
		if (!upFile.exists()) {
			upFile.mkdirs();
		}

		String fileName = java.util.UUID.randomUUID().toString(); // 采用UUID的方式随机命名
		fileName += uploadFileName.substring(uploadFileName.length() - 4);
		File toFile = new File(uploadPath, fileName);
		OutputStream os = new FileOutputStream(toFile);
		byte[] buffer = new byte[1024];
		int length = 0;
		while ((length = is.read(buffer)) > 0) {
			os.write(buffer, 0, length);
		}
		is.close();
		os.close();

		// 设置返回“图像”选项卡
		String callback = request.getParameter("CKEditorFuncNum");
		out.println("<script type=\"text/javascript\">");
		String fullPath = (!StringUtil.isEmpty(request.getContextPath()) ? request.getContextPath() : "") + (imgPath.startsWith("/") ? imgPath : ("/" + imgPath)) + "/" + fileName;
		out.println("window.parent.CKEDITOR.tools.callFunction(" + callback + ",'" + fullPath + "','')");
		out.println("</script>");

		return null;
	}
}
