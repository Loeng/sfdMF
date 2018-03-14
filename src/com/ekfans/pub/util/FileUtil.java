package com.ekfans.pub.util;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import sun.misc.BASE64Decoder;

import com.ekfans.plugin.cache.base.Cache;

/**
 * 文件工具类
 * 
 * @Title: FileUtil.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author Lgy
 * @date 2013-12-23
 * @version 1.0
 */
public class FileUtil {
	/**
	 * 根据文件详名获取文件内容
	 * 
	 * @param fileName
	 *            文件路径 + 文件内容
	 * @return
	 */
	public static String getFileContent(String fileName) {
		StringBuffer returnStr = new StringBuffer("");
		try {
			File file = new File(fileName);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			// 一次读入一行，直到读入null为文件结束
			// while (reader.readLine() != null) {
			// System.out.println(reader.readLine());
			// tempString += reader.readLine();
			// }
			String tempString = "";
			// int line = 1;
			while ((tempString = reader.readLine()) != null) {
				returnStr.append(tempString);
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnStr.toString();
	}

	/**
	 * 将文件写入硬盘
	 * 
	 * @param basePath
	 *            文件写入的基础路径
	 * @param hierarchyDirPath
	 *            文件存放层级目录路径
	 * @param file
	 *            待写入的文件
	 * @return 上传操作相关信息映射
	 */
	public static Map<String, String> writeToDisk(String basePath, String hierarchyDirPath, MultipartFile file) {
		InputStream in = null;
		OutputStream out = null;
		Map<String, String> messageMap = new HashMap<String, String>();

		// 拼接文件的真实路径
		String realPath = StringUtil.join(basePath, hierarchyDirPath, file.getOriginalFilename());

		// 拼接文件的相对路径(将'\'换为'/'便于浏览器展示)
		String path = StringUtil.join("/", hierarchyDirPath.replaceAll("\\\\", "/"), file.getOriginalFilename());
		// 拼接上传文件存放的真实路径
		File uploadDir = new File(StringUtil.join(basePath, hierarchyDirPath));
		messageMap.put("flagMessage", "success");
		messageMap.put("realPath", realPath);
		messageMap.put("path", path);
		// 如果上传文件的真实文件夹不存在，则创建文件夹
		if (!uploadDir.isDirectory()) {
			uploadDir.mkdir();
		}

		try {
			in = file.getInputStream();
			out = new FileOutputStream(new File(realPath));
			IOUtils.copy(in, out);
		} catch (FileNotFoundException e) {
			messageMap.put("flagMessage", "error");
			e.printStackTrace();
		} catch (IOException e) {
			messageMap.put("flagMessage", "error");
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(in);
		}
		return messageMap;
	}

	/**
	 * 
	 * 根据文件路径删除文件
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean deleteFile(String filePath) {
		File file = new File(filePath);

		if (file.exists() && !file.isDirectory()) {
			file.delete();
			return true;
		}

		return false;
	}

	/**
	 * Copy file from source to destination
	 * 
	 * @param source
	 *            source directory
	 * @param dest
	 *            destination directory
	 * @return true if copy sucessfully; return false when error
	 */
	public static boolean copyFile(String source, String dest) {

		// check if they are the same file
		File fSource = new File(source);
		File fDest = new File(dest);

		String path = "";
		if (dest.indexOf("/") != -1) {
			path = dest.substring(0, dest.lastIndexOf("/"));
		} else {
			path = dest.substring(0, dest.lastIndexOf("\\"));
		}
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}

		if (fSource.equals(fDest)) {
			return true;
		}
		// new
		FileInputStream fis = null;
		FileOutputStream fos = null;

		try {
			fis = new FileInputStream(source);
			fos = new FileOutputStream(dest);

			byte[] buf = new byte[10 * 1024];
			int size = 0;

			while ((size = fis.read(buf)) != -1) {
				fos.write(buf, 0, size);
			}

			fos.flush();

			return true;
		} catch (Exception ex) {
			System.out.println("save file ( " + source + " ) to ( " + dest + " ) failed!");
			ex.printStackTrace();
			return false;
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		}
		// end
	}

	/**
	 * Get file name from absolute path
	 * 
	 * @param fPath
	 *            absolute path
	 * @return file name
	 */
	public static String getFileName(String fPath) {
		int pos1 = fPath.lastIndexOf(File.separator);
		int pos2 = fPath.lastIndexOf("/");
		if (pos1 < pos2) {
			pos1 = pos2;
		}

		if (pos1 < 0) {
			return fPath;
		}

		if (pos1 == fPath.length() - 1) {
			return "";
		}
		return fPath.substring(pos1 + 1, fPath.length());
	}

	/**
	 * Write content to a fileName with the destEncoding 写文件。如果此文件不存在就创建一个。
	 * 
	 * @param content
	 *            String
	 * @param fileName
	 *            String
	 * @param destEncoding
	 *            String
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void writeFile(String content, String fileName, String destEncoding) throws FileNotFoundException, IOException {
		File file = null;
		try {
			file = new File(fileName);
			if (!file.exists()) {
				if (file.createNewFile() == false) {
					throw new IOException("create file '" + fileName + "' failure.");
				}
			}
			if (file.isFile() == false) {
				throw new IOException("'" + fileName + "' is not a file.");
			}
			if (file.canWrite() == false) {
				throw new IOException("'" + fileName + "' is a read-only file.");
			}
		} finally {
			// we dont have to close File here
		}
		BufferedWriter out = null;
		try {
			FileOutputStream fos = new FileOutputStream(fileName);
			out = new BufferedWriter(new OutputStreamWriter(fos, destEncoding));
			out.write(content);
			out.flush();
		} catch (FileNotFoundException fe) {
			throw fe;
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException ex) {
			}
		}
	}

	/**
	 * 读取文件的内容，并将文件内容以字符串的形式返回。
	 * 
	 * @param fileName
	 * @param srcEncoding
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String readFile(String fileName, String srcEncoding) throws FileNotFoundException, IOException {
		File file = null;
		try {
			file = new File(fileName);
			if (file.isFile() == false) {
				throw new IOException("'" + fileName + "' is not a file.");
			}
		} finally {
			// we dont have to close File here
		}
		BufferedReader reader = null;
		try {
			StringBuffer result = new StringBuffer(1024);
			FileInputStream fis = new FileInputStream(fileName);
			reader = new BufferedReader(new InputStreamReader(fis, srcEncoding));
			char[] block = new char[512];
			while (true) {
				int readLength = reader.read(block);
				if (readLength == -1)
					break;// end of file
				result.append(block, 0, readLength);
			}
			return result.toString();
		} catch (FileNotFoundException fe) {
			throw fe;
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (IOException ex) {
			}
		}
	}

	/**
	 * 将文件读成Map<String, Object>返回
	 * 
	 * @param path
	 *            文件全路径
	 * @return
	 */
	public static Map<String, Object> getInputStream(String path) {
		File file = new File(path);
		Map<String, Object> map = null;
		if (file.exists()) {
			map = new HashMap<String, Object>();
			map.put("fileStr", new Base64Util().getImageBase64Str(path));
			map.put("name", file.getName());
		}
		return map;
	}

	/**
	 * 将流写入文件
	 * 
	 * @param path
	 *            待写入文件路径
	 * @param name
	 *            文件名
	 * @param inputStream文件流
	 * @return
	 * @throws IOException
	 */
	public static String writeInputStream(String path, String fileStr, String name) throws IOException {
		File p_file = new File(path);// 目标文件路径
		if (!p_file.exists()) {
			p_file.mkdirs();
		}
		ByteArrayInputStream stream = new ByteArrayInputStream(new Base64Util().getStrToBytes(fileStr));
		File file = new File(path + name);// 目标文件全路径（包含文件名）
		try {
			ByteArrayInputStream fin = stream;
			FileOutputStream fout = new FileOutputStream(file);

			int len = fin.read();

			System.out.println("开始复制.....");
			long startTime = System.currentTimeMillis();// 开始时间
			while (len != -1) {
				fout.write(len);
				len = fin.read();
			}
			long endTime = System.currentTimeMillis();// 结束时间
			System.out.println("文件复制完成，公耗时：" + (endTime - startTime) + "ms");

			// 关闭流
			fin.close();
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file.getPath();
	}

	/**
	 * @Title: baseStringToImage
	 * @Description: TODO(将二进制转换为图片放在指定路径) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 *            请求参数
	 * @param base64String
	 *            二进制字符串
	 * @param imageFormat
	 *            图片格式(jpg,png)
	 * @return boolean 返回类型
	 */
	public static String baseStringToImage(HttpServletRequest request, String base64String, String filePath) {
		// 如果传进来的二进制字符串为空，则返回false
		if (StringUtil.isEmpty(base64String)) {
			return null;
		}
		BASE64Decoder decoder = new BASE64Decoder();
		// 从缓存获取图片存储路径
		String path = Cache.getResource("file.temp.directory");
		String date = DateUtil.getSysDateString();
		// 获取图片唯一名称
		String fileName = java.util.UUID.randomUUID().toString();
		// 获取项目路径
		if (StringUtil.isEmpty(path)) {
			ServletContext servletContext = request.getSession().getServletContext();
			path = servletContext.getRealPath("/");
		}
		if (StringUtil.isEmpty(filePath)) {
			filePath = "customerfiles/appFile/";
		}
		if (!filePath.startsWith("/")) {
			filePath = "/" + filePath;
		}
		if (filePath.endsWith("/")) {
			filePath = filePath + date;
		} else {
			filePath = filePath + "/" + date;
		}
		String imageFormat = "jpg";
		if (base64String.indexOf("****") != -1) {
			String[] str = base64String.split("\\*\\*\\*\\*");
			if (str != null && str.length > 0) {
				base64String = str[0];
				imageFormat = str[1];
			}
		}
		fileName = fileName + "." + imageFormat;
		filePath = filePath + "/" + fileName;
		try {
			// 将二进制转换为字节
			byte[] bytes = decoder.decodeBuffer(base64String);
			// 用字节流写图片
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			BufferedImage reader = ImageIO.read(bais);
			path = path + filePath;
			// 设置图片存放位置
			File file = new File(path);
			// 如果文件路径不存在,创建文件夹
			if (!file.exists()) {
				file.mkdirs();
			}
			ImageIO.write(reader, imageFormat, file);
			return filePath;
		} catch (Exception e) {
			return "";
		}

	}

	// ->
	// // 如果传进来的二进制字符串为空，则返回false
	// if (StringUtil.isEmpty(base64String)) {
	// return null;
	// }
	// // 从缓存获取图片存储路径
	// String path = Cache.getResource("file.temp.directory");
	// String date = DateUtil.getSysDateString();
	// // 获取图片唯一名称
	// String fileName = java.util.UUID.randomUUID().toString();
	// // 获取项目路径
	// if (StringUtil.isEmpty(path)) {
	// ServletContext servletContext =
	// request.getSession().getServletContext();
	// path = servletContext.getRealPath("/");
	// }
	// if (StringUtil.isEmpty(filePath)) {
	// filePath = "customerfiles/appFile/";
	// }
	// if (!filePath.startsWith("/")) {
	// filePath = "/" + filePath;
	// }
	// if (filePath.endsWith("/")) {
	// filePath = filePath + date;
	// } else {
	// filePath = filePath + "/" + date;
	// }
	// String imageFormat = "jpg";
	// fileName = fileName + "." + imageFormat;
	// filePath = filePath + "/" + fileName;
	//
	// path = path + filePath;
	//
	// if (generateImage(base64String, path)) {
	// return filePath;
	// } else {
	// return null;
	// }
	// <-

	// public static boolean generateImage(String imgStr, String imgFilePath)
	// {// 对字节数组字符串进行Base64解码并生成图片
	// if (imgStr == null) // 图像数据为空
	// return false;
	// BASE64Decoder decoder = new BASE64Decoder();
	// try {
	// // Base64解码
	// byte[] bytes = decoder.decodeBuffer(imgStr);
	// for (int i = 0; i < bytes.length; ++i) {
	// if (bytes[i] < 0) {// 调整异常数据
	// bytes[i] += 256;
	// }
	// }
	// // 生成jpeg图片
	// OutputStream out = new FileOutputStream(imgFilePath);
	// out.write(bytes);
	// out.flush();
	// out.close();
	// return true;
	// } catch (Exception e) {
	// return false;
	// }
	// }
}
