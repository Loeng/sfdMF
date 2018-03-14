package com.ekfans.pub.tools.security;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class WebSecurityServlet extends HttpServlet {
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 调用方法获取默认难度和长度的验证码
		String securityCode = SecurityCode.getSecurityCode();
		// 调用方法获取图片流
		BufferedImage imageStream = WebSecurityImage.createImage(securityCode);
		// 获取SESSION
		HttpSession session = request.getSession();
		// 将验证码放入SESSION中
		session.setAttribute("SESSION_SECURITY_CODE", securityCode);
		// 禁止图像缓存。
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		response.setContentType("image/jpeg");
		// 将图像输出到Servlet输出流中。
		ServletOutputStream sos = response.getOutputStream();
		ImageIO.write(imageStream, "jpeg", sos);
		sos.close();
	}
}
