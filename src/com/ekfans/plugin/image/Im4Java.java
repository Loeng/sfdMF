package com.ekfans.plugin.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.im4java.core.CompositeCmd;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.pub.util.StringUtil;

import javax.imageio.ImageIO;

/**
 * 图片处理工具类
 * 
 * @ClassName: ImageTools
 * @Description: 本类使用Jmagick + ImageMagick 对图片做各种处理，如：按照尺寸缩放，加水印等等
 * @author liuguoyu
 * @date 2014-4-8 下午10:45:52
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class Im4Java {
	/**
	 * ImageMagick的路径
	 */
	public static String imageMagickPath = null;

	static {
		/**
		 * 获取ImageMagick的路径
		 */
		imageMagickPath = Cache.getResource("imageMagick.path");
	}

	/**
	 * 根据坐标裁剪图片
	 * 
	 * @Title: cutImage
	 * @param @param srcPath 要裁剪图片的路径
	 * @param @param newPath 裁剪图片后的路径
	 * @param @param x 起始横坐标
	 * @param @param y 起始纵坐标
	 * @param @param x1 结束横坐标
	 * @param @param y1 结束纵坐标
	 * @param @throws Exception 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public static void cutImage(String srcPath, String newPath, int x, int y, int x1, int y1) throws IOException, InterruptedException, IM4JavaException {
		// 计算宽度
		int width = x1 - x;
		// 计算高度
		int height = y1 - y;
		IMOperation op = new IMOperation();
		op.addImage(srcPath);

		/**
		 * width：裁剪的宽度 height：裁剪的高度 x：裁剪的横坐标 y：裁剪的挫坐标
		 */
		op.crop(width, height, x, y);
		op.addImage(newPath);
		ConvertCmd convert = new ConvertCmd();
		convert.setSearchPath(imageMagickPath);
		convert.run(op);
	}

	/**
	 * 根据尺寸缩放图片
	 * 
	 * @param width
	 *            缩放后的图片宽度
	 * @param height
	 *            缩放后的图片高度
	 * @param srcPath
	 *            源图片路径
	 * @param newPath
	 *            缩放后图片的路径
	 */
	public static void cutImage(int width, int height, String srcPath, String newPath) throws IOException, InterruptedException, IM4JavaException {
		IMOperation op = new IMOperation();
		op.addImage(srcPath);

		op.resize(width, height);
		op.addImage(newPath);
		ConvertCmd convert = new ConvertCmd();
		convert.setSearchPath(imageMagickPath);
		convert.run(op);
	}

	/**
	 * 根据宽度等比缩放图片
	 * 
	 * @param width
	 *            缩放后的图片宽度
	 * @param srcPath
	 *            源图片路径
	 * @param newPath
	 *            缩放后图片的路径
	 */
	public static void cutImage(int width, String srcPath, String newPath) throws IOException, InterruptedException, IM4JavaException {
		IMOperation op = new IMOperation();
		op.addImage(srcPath);
		op.resize(width, null);
		op.addImage(newPath);
		ConvertCmd convert = new ConvertCmd();
		convert.setSearchPath(imageMagickPath);
		convert.run(op);
	}

	/**
	 * 旋转图片
	 * 
	 * @Title: rotateImg
	 * @param @param srcPath 原图片路径
	 * @param @param newPath 目标图片路径
	 * @param @param angle 旋转角度
	 * @param @throws IOException
	 * @param @throws InterruptedException
	 * @param @throws IM4JavaException 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void rotateImg(String srcPath, String newPath, double angle) throws IOException, InterruptedException, IM4JavaException {
		ConvertCmd cmd = new ConvertCmd();
		IMOperation operation = new IMOperation();
		operation.addImage(srcPath);
		operation.rotate(angle);
		operation.addImage(newPath);
		cmd.setSearchPath(imageMagickPath);
		cmd.run(operation);
	}

	/**
	 * 将图片编程黑白图片
	 * 
	 * @Title: monochrome
	 * @param @param srcPath 原图片路径
	 * @param @param newPath 目标图片路径
	 * @param @throws IOException
	 * @param @throws InterruptedException
	 * @param @throws IM4JavaException 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void monochrome(String srcPath, String newPath) throws IOException, InterruptedException, IM4JavaException {
		ConvertCmd cmd = new ConvertCmd();
		IMOperation operation = new IMOperation();
		operation.addImage(srcPath);
		operation.monochrome();
		operation.addImage(newPath);
		cmd.setSearchPath(imageMagickPath);
		cmd.run(operation);
	}

	/**
	 * 给图片打算图片水印
	 * 
	 * @Title: waterMark
	 * @param @param watePath 水印图片路径
	 * @param @param srcPath 原图片路径
	 * @param @param newPath 目标图片路径
	 * @param @param gravity 水印图片位置
	 * @param @param dissolve 水印透明度
	 * @param @throws IOException
	 * @param @throws InterruptedException
	 * @param @throws IM4JavaException 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void waterMark(String watePath, String srcPath, String newPath, String gravity, int dissolve) throws IOException, InterruptedException, IM4JavaException {
		IMOperation op = new IMOperation();
		op.gravity(gravity);
		op.dissolve(dissolve);
		op.addImage(watePath);
		op.addImage(srcPath);
		op.addImage(newPath);
		CompositeCmd cmd = new CompositeCmd(true);
		cmd.setSearchPath(imageMagickPath);
		cmd.run(op);
	}

	/**
	 * 给图片加水印文字
	 * 
	 * @Title: addImgText
	 * @param @param srcPath 原图片路径
	 * @param @param newPath 目标图片路径
	 * @param @param content 水印文字
	 * @param @param x 水印文字的横坐标
	 * @param @param y 水印文字的纵坐标
	 * @param @param font 水印文字的字体
	 * @param @param fontSize 水印文字的大小
	 * @param @param gravity 水印文字的位置
	 * @param @param color 水印的颜色
	 * @param @throws Exception 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public static void addImgText(String srcPath, String newPath, String content, int x, int y, String font, int fontSize, String gravity, String color) throws IOException, InterruptedException, IM4JavaException {
		IMOperation op = new IMOperation();
		if (!StringUtil.isEmpty(font)) {
			op.font(font);
		}
		if (!StringUtil.isEmpty(gravity)) {
			// 设置方位 NorthWest, North, NorthEast, West, Center, East, SouthWest,
			// South, SouthEast annotate 设置偏移量
			op.gravity(gravity);
		}
		if (!StringUtil.isEmpty(color)) {
			op.fill(color);
		}
		op.pointsize(fontSize);
		// text:格式， x:X轴距，y:Y轴距，content:文字内容
		op.draw("text " + x + "," + y + " " + content);
		op.addImage();
		op.addImage();
		ConvertCmd convert = new ConvertCmd();

		convert.setSearchPath(imageMagickPath);
		convert.run(op, srcPath, srcPath);
	}

	/**
	 * 将几个图片合成 --- 时间关系，暂未详细考虑，预留
	 * 
	 * @param args
	 * @param maxWidth
	 * @param maxHeight
	 * @param newpath
	 * @param mrg
	 * @param type
	 *            1:横,2:竖
	 */
	public static void montage(String[] args, Integer maxWidth, Integer maxHeight, String newpath, Integer mrg, String type) throws IOException, InterruptedException, IM4JavaException {
		IMOperation op = new IMOperation();
		ConvertCmd cmd = new ConvertCmd(true);
		String thumb_size = maxWidth + "x" + maxHeight + "^";
		String extent = maxWidth + "x" + maxHeight;
		if ("1".equals(type)) {
			op.addRawArgs("+append");
		} else if ("2".equals(type)) {
			op.addRawArgs("-append");
		}

		op.addRawArgs("-thumbnail", thumb_size);
		op.addRawArgs("-gravity", "center");
		op.addRawArgs("-extent", extent);

		Integer border_w = maxWidth / 40;
		op.addRawArgs("-border", border_w + "x" + border_w);
		op.addRawArgs("-bordercolor", "#ccc");

		op.addRawArgs("-border", 1 + "x" + 1);
		op.addRawArgs("-bordercolor", "#fff");

		for (String img : args) {
			op.addImage(img);
		}
		if ("1".equals(type)) {
			Integer whole_width = ((mrg / 2) + 1 + border_w + maxWidth + border_w + (mrg / 2) + 1) * args.length - mrg;
			Integer whole_height = maxHeight + border_w + 1;
			op.addRawArgs("-extent", whole_width + "x" + whole_height);
		} else if ("2".equals(type)) {
			Integer whole_width = maxWidth + border_w + 1;
			Integer whole_height = ((mrg / 2) + 1 + border_w + maxHeight + border_w + (mrg / 2) + 1) * args.length - mrg;
			op.addRawArgs("-extent", whole_width + "x" + whole_height);
		}
		op.addImage(newpath);
		cmd.setSearchPath(imageMagickPath);
		cmd.run(op);
	}

	/**
	 * 根据宽高先压缩后裁剪
	 *
	 * @param width
	 *            最终图片宽
	 * @param height
	 *            最终图片高
	 * @param srcPath
	 *            源图片路径
	 * @param newPath
	 *            最终保存的图片路径
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws IM4JavaException
	 */
	public static void zipAndCutImg(int width, int height, String srcPath, String newPath) throws IOException, InterruptedException, IM4JavaException {
		int picWidth = 0;
		int picHeight = 0;

		BufferedImage bufferedImage = ImageIO.read(new File(srcPath));
		picWidth = bufferedImage.getWidth();
		picHeight = bufferedImage.getHeight();

		int type = 0;// 压缩和裁剪类型 0：无需裁剪 1：宽压缩，高裁剪 2：高压缩，宽裁剪
		if ((picWidth / width > picHeight / height) || ((picWidth / width == picHeight / height) && (picWidth % width > picHeight % height))) {
			type = 2;
		} else if ((picWidth / width < picHeight / height) || ((picWidth / width == picHeight / height) && (picWidth % width < picHeight % height))) {
			type = 1;
		}

		IMOperation op = new IMOperation();
		op.addImage(srcPath);
		if (type == 0) {
			op.resize(width, height);
		} else if (type == 1) {
			op.resize(width, null);
		} else if (type == 2) {
			op.resize(null, height);
		}
		op.addImage(newPath);
		ConvertCmd convert = new ConvertCmd();
		convert.setSearchPath(imageMagickPath);
		convert.run(op);
		cutImage(newPath, newPath, 0, 0, width, height);
	}
}
