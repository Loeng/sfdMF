package com.ekfans.controllers.tools.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ekfans.plugin.page.BasicRequest;
import com.ekfans.pub.util.FileUtil;
import com.ekfans.pub.util.StringUtil;

/**
 * 文件上传帮助类
 * 
 * @ClassName: FileUploadHelper
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author liuguoyu
 * @date 2014-5-7 上午11:56:06
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class FileUploadHelper {
	/**
	 * 文件上传
	 * 
	 * @param methodName
	 *            页面property名称
	 * @param filePath
	 *            文件目标存放路径
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return 文件最终全路径
	 */
	public static String uploadFile(String methodName, String filePath,
			HttpServletRequest request, HttpServletResponse response) {
		// 如果传进来的方法名和文件路径为空，则返回空串
		if (StringUtil.isEmpty(methodName) || StringUtil.isEmpty(filePath)) {
			return null;
		}
		// 获取原文件路径
		String oldUrlPath = request.getParameter(methodName + "OldUrlPath");
		String returnPath = "";
		try {
			// 获取用户是否操作文件删除了
			boolean removeStatus = FileUploadHelper.getIsDeleteOldPic(
					methodName, request);
			ServletContext servletContext = request.getSession()
					.getServletContext();
			// 如果是需要删除，则执行删除操作
			if (removeStatus) {
				try {
					// 如果路径不为空，则删除
					if (!StringUtil.isEmpty(oldUrlPath)) {
						FileUtil.deleteFile(servletContext
								.getRealPath(oldUrlPath));
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			String url = request.getParameter(methodName + "FileUrl");

			// url = url.replaceAll("\\/","\\\\");
			// url = url.replaceAll("\\\\\\\\","\\\\");
			// 如果文件路径存在，则复制图片到目标路径
			if (!StringUtil.isEmpty(url)) {
				// 加上盘符路径
				if (url.startsWith("/customerfiles")) {
					url = servletContext.getRealPath(url);
				}

				// 取文件名，放到目标文件上
				String fileName = FileUtil.getFileName(url);
				if (filePath.endsWith("/")) {
					returnPath = filePath + fileName;
				} else {
					returnPath = filePath + "/" + fileName;
				}

				boolean copyStatus = FileUtil.copyFile(url,
						servletContext.getRealPath(returnPath));

				if (copyStatus) {
					FileUtil.deleteFile(url); // 删除临时文件
					FileUtil.deleteFile(servletContext.getRealPath(oldUrlPath)); // 删除原文件
				}
			} else if (!removeStatus) {
				return oldUrlPath;
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return returnPath;
	}

	/**
	 * 根据页面property名称获取文件上传的流
	 * 
	 * @param methodName
	 *            页面property名称
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return
	 */
	public static InputStream getUploadFileInputStream(String methodName,
			HttpServletRequest request, HttpServletResponse response) {
		// 如果传进来的方法名为空，则返回空串
		if (StringUtil.isEmpty(methodName)) {
			return null;
		}

		// 获取原文件路径
		String oldUrlPath = request.getParameter(methodName + "OldUrlPath");

		ServletContext servletContext = request.getSession()
				.getServletContext();
		try {
			// 获取用户是否操作文件删除了
			boolean removeStatus = FileUploadHelper.getIsDeleteOldPic(
					methodName, request);
			// 如果是需要删除，则执行删除操作
			if (removeStatus) {
				try {
					// 如果路径不为空，则删除
					if (!StringUtil.isEmpty(oldUrlPath)) {
						FileUtil.deleteFile(servletContext
								.getRealPath(oldUrlPath));
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			String url = request.getParameter(methodName + "FileUrl");

			// 如果文件路径存在，则复制图片到目标路径
			if (!StringUtil.isEmpty(url)) {
				// 根据文件路径获取File
				File file = new File(url);

				// 如果文件存在，则转化成流并返回
				if (file.exists()) {
					InputStream is = new FileInputStream(url);
					return is;
				}

			} else if (!removeStatus && !StringUtil.isEmpty(oldUrlPath)) {
				// 根据原文件路径获取流
				File file = new File(oldUrlPath);
				// 如果文件存在，则转化成流并返回
				if (file.exists()) {
					InputStream is = new FileInputStream(url);
					return is;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	/**
	 * 根据页面property名称获取文件上传的流
	 * 
	 * @param methodName
	 *            页面property名称
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return
	 */
	public static InputStream getUploadFileInputStream(String methodName,
			HttpServletRequest request) {
		// 如果传进来的方法名为空，则返回空串
		if (StringUtil.isEmpty(methodName)) {
			return null;
		}

		// 获取原文件路径
		String oldUrlPath = request.getParameter(methodName + "OldUrlPath");

		try {
			// 获取用户是否操作文件删除了
			boolean removeStatus = FileUploadHelper.getIsDeleteOldPic(
					methodName, request);
			ServletContext servletContext = request.getSession()
					.getServletContext();
			// 如果是需要删除，则执行删除操作
			if (removeStatus) {
				try {
					// 如果路径不为空，则删除
					if (!StringUtil.isEmpty(oldUrlPath)) {
						FileUtil.deleteFile(servletContext
								.getRealPath(oldUrlPath));
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			String url = request.getParameter(methodName + "FileUrl");

			// 如果文件路径存在，则复制图片到目标路径
			if (!StringUtil.isEmpty(url)) {
				// 根据文件路径获取File
				File file = new File(url);

				// 如果文件存在，则转化成流并返回
				if (file.exists()) {
					InputStream is = new FileInputStream(url);
					return is;
				}

			} else if (!removeStatus && !StringUtil.isEmpty(oldUrlPath)) {
				// 根据原文件路径获取流
				File file = new File(oldUrlPath);
				// 如果文件存在，则转化成流并返回
				if (file.exists()) {
					InputStream is = new FileInputStream(url);
					return is;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	/**
	 * 根据页面property名称获取文件上传的流
	 * 
	 * @param methodName
	 *            页面property名称
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return
	 */
	public static File getUploadFile(String methodName,
			HttpServletRequest request) {
		// 如果传进来的方法名为空，则返回空串
		if (StringUtil.isEmpty(methodName)) {
			return null;
		}

		// 获取原文件路径
		String oldUrlPath = request.getParameter(methodName + "OldUrlPath");

		try {
			// 获取用户是否操作文件删除了
			boolean removeStatus = FileUploadHelper.getIsDeleteOldPic(
					methodName, request);
			ServletContext servletContext = request.getSession()
					.getServletContext();
			// 如果是需要删除，则执行删除操作
			if (removeStatus) {
				try {
					// 如果路径不为空，则删除
					if (!StringUtil.isEmpty(oldUrlPath)) {
						FileUtil.deleteFile(servletContext
								.getRealPath(oldUrlPath));
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			String url = request.getParameter(methodName + "FileUrl");

			// 如果文件路径存在，则复制图片到目标路径
			if (!StringUtil.isEmpty(url)) {
				// 根据文件路径获取File
				File file = new File(url);

				// 如果文件存在，则转化成流并返回
				if (file.exists()) {
					return file;
				}

			} else if (!removeStatus && !StringUtil.isEmpty(oldUrlPath)) {
				// 根据原文件路径获取流
				File file = new File(oldUrlPath);
				// 如果文件存在，则转化成流并返回
				if (file.exists()) {
					return file;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	/**
	 * 根据页面property名称获取文件上传的临时路径
	 * 
	 * @param methodName
	 *            页面property名称
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return
	 */
	public static String getUploadFileUrl(String methodName,
			HttpServletRequest request) {
		// 如果传进来的方法名为空，则返回空串
		if (StringUtil.isEmpty(methodName)) {
			return null;
		}

		// 获取原文件路径
		String oldUrlPath = request.getParameter(methodName + "OldUrlPath");

		try {
			// 获取用户是否操作文件删除了
			boolean removeStatus = FileUploadHelper.getIsDeleteOldPic(
					methodName, request);
			ServletContext servletContext = request.getSession()
					.getServletContext();
			// 如果是需要删除，则执行删除操作
			if (removeStatus) {
				try {
					// 如果路径不为空，则删除
					if (!StringUtil.isEmpty(oldUrlPath)) {
						FileUtil.deleteFile(servletContext
								.getRealPath(oldUrlPath));
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			String url = request.getParameter(methodName + "FileUrl");

			// 如果文件路径存在，则复制图片到目标路径
			if (!StringUtil.isEmpty(url)) {
				return url;
			} else if (!removeStatus && !StringUtil.isEmpty(oldUrlPath)) {
				return oldUrlPath;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	/**
	 * 获取用户是否删除文件
	 * 
	 * @param methodName
	 *            页面property名称
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return
	 */
	public static boolean getIsDeleteOldPic(String methodName,
			HttpServletRequest request) {
		// 如果传进来的名称为空，则返回不删除
		if (StringUtil.isEmpty(methodName)) {
			return false;
		}
		try {
			// 获取控件的删除状态
			String status = request.getParameter(methodName + "remove");

			// 如果删除状态不为空，则返回
			if (!StringUtil.isEmpty(status)) {
				return Boolean.valueOf(status);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return false;
	}

	/**
	 * 删除文件夹
	 * 
	 * @param sPath
	 */
	public static void deleteDirectory(File dirFile) {
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return;
		}
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				files[i].delete();
			} else {
				// 删除子目录
				deleteDirectory(files[i]);
			}
		}
		// 删除文件夹
		dirFile.delete();
	}

	/**
	 * 获取文件预览路径
	 * 
	 * @param request
	 * @param fileUrl
	 * @return
	 */
	public static String getFilePurviewUrl(HttpServletRequest request,
			String fileUrl) {
		if (StringUtil.isEmpty(fileUrl)) {
			return null;
		}
		// if (fileUrl.indexOf("/") != -1) {
		// fileUrl = fileUrl.replaceAll("/", "\\$b\\$");
		// }
		// if (fileUrl.indexOf("\\") != -1) {
		// fileUrl = fileUrl.replaceAll("\\\\", "\\$b\\$");
		// }
		// if (fileUrl.indexOf(".") != -1) {
		// fileUrl = fileUrl.replaceAll("\\.", "\\$d\\$");
		// }
		BasicRequest br = new BasicRequest(request);

		String link = br.getWebFullUrlPrex() + fileUrl;
		return link;
	}
}
