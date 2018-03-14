package com.ekfans.pub.util;

import com.ekfans.base.content.model.Content;
import com.ekfans.base.product.model.Product;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.image.Im4Java;
import com.ekfans.plugin.image.LocalImgCovert;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * imageMagick图片处理工具类
 * 
 * @ClassName: ImageUtil
 * @Description: 用来实现对图片的各种处理
 * @author liuguoyu
 * @date 2014-4-8 下午10:35:32
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class ImageUtil {
    /**
     * 
    * @Title: productPictureManager
    * @Description: TODO(给商品图加水印，压缩成3种大小保存到product对应的属性)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param product
    * @param picturePath void 返回类型
    * @throws
     */
	public void productPictureManager(Product product,String picturePath) {
	    
	}

	/**
	 *
	 * @Title: appContentPictureManager @Description: TODO(给资讯图加水印，压缩大小，截取图片)
	 *         详细业务流程: (详细描述此方法相关的业务处理流程) @param product @param picturePath void
	 *         返回类型 @throws
	 */
	public static void appContentPictureManager(Content content, String picturePath, HttpServletRequest request) {
		// 如果传过来的资讯对象为空，则直接返回
		if (content == null) {
			return;
		}
		// 获取第一张图
		String pic1 = content.getAppPic1();
		// 获取第二张图
		String pic2 = content.getAppPic2();
		// 获取第三张图
		String pic3 = content.getAppPic3();
		// 如果所有的图均为空，则直接返回
		if (StringUtil.isEmpty(pic1) && StringUtil.isEmpty(pic2) && StringUtil.isEmpty(pic3)) {
			return;
		}

		int width = 0;
		int height = 0;
		int a = 0;
		if (!StringUtil.isEmpty(pic1)) {
			a++;
		}
		if (!StringUtil.isEmpty(pic2)) {
			a++;
		}
		if (!StringUtil.isEmpty(pic3)) {
			a++;
		}

		if (a > 1) {
			try {
				width = Integer.parseInt(Cache.getSystemParamConfig("资讯多图宽"));
				height = Integer.parseInt(Cache.getSystemParamConfig("资讯多图高"));
			} catch (Exception e) {
				width = 310;
				height = 220;
			}
		} else {
			try {
				width = Integer.parseInt(Cache.getSystemParamConfig("资讯单图宽"));
				height = Integer.parseInt(Cache.getSystemParamConfig("资讯单图高"));
			} catch (Exception e) {
				width = 273;
				height = 192;
			}
		}

		ServletContext servletContext = request.getSession().getServletContext();

		File file = new File(servletContext.getRealPath(picturePath));
		if (!file.exists()) {
			file.mkdirs();
		}

		if (!StringUtil.isEmpty(pic1)) {
			// 得到后缀名
			String suffix = pic1.substring(pic1.lastIndexOf("."), pic1.length());
			String picPath = picturePath + "/" + java.util.UUID.randomUUID().toString() + suffix;
			try {
				Im4Java.zipAndCutImg(width, height, servletContext.getRealPath(pic1), servletContext.getRealPath(picPath));
			} catch (Exception e) {
				e.printStackTrace();
				LocalImgCovert localImg = new LocalImgCovert();
				localImg.compressPic(servletContext.getRealPath(pic1), servletContext.getRealPath(picPath), width, height, true);
			}
			content.setAppPic1(picPath);
		}

		if (!StringUtil.isEmpty(pic2)) {
			// 得到后缀名
			String suffix = pic1.substring(pic2.lastIndexOf("."), pic1.length());
			String picPath = picturePath + "/" + java.util.UUID.randomUUID().toString() + suffix;
			try {
				Im4Java.zipAndCutImg(width, height, servletContext.getRealPath(pic2), servletContext.getRealPath(picPath));
			} catch (Exception e) {
				e.printStackTrace();
				LocalImgCovert localImg = new LocalImgCovert();
				localImg.compressPic(servletContext.getRealPath(pic2), servletContext.getRealPath(picPath), width, height, true);
			}
			content.setAppPic2(picPath);
		}

		if (!StringUtil.isEmpty(pic3)) {
			// 得到后缀名
			String suffix = pic1.substring(pic3.lastIndexOf("."), pic1.length());
			String picPath = picturePath + "/" + java.util.UUID.randomUUID().toString() + suffix;
			try {
				Im4Java.zipAndCutImg(width, height, servletContext.getRealPath(pic3), servletContext.getRealPath(picPath));
			} catch (Exception e) {
				LocalImgCovert localImg = new LocalImgCovert();
				localImg.compressPic(servletContext.getRealPath(pic3), servletContext.getRealPath(picPath), width, height, true);
			}
			content.setAppPic3(picPath);
		}
	}
}
