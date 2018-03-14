package com.ekfans.base.product.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.ekfans.base.product.model.Product;
import com.ekfans.base.product.model.ProductPicture;
import com.ekfans.base.product.model.ProductPrice;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.image.Im4Java;
import com.ekfans.plugin.image.LocalImgCovert;
import com.ekfans.pub.util.StringUtil;

/**
 * 商品相关帮助类
 * 
 * @ClassName: ProductHelper
 * @author 成都易科远见科技有限公司
 * @date 2014-6-18 上午05:47:35
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class ProductHelper {
	// 商品图片处理类型：商品图
	public static String PIC_TYPE_PIC = "product_pic";

	// 商品图片处理类型：多角多视图
	public static String PIC_TYPE_POP = "pic_pop";

	/**
	 * 压缩商品图
	 * 
	 * @Title: img
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param type 压缩类型
	 * @param @param obj 对象 -商品传商品对象，多角多视图传多角多视图对象
	 * @return void 返回类型
	 * @throws
	 */
	public static void img(String type, Object obj, HttpServletRequest request) {
		// 如果传进来的类型或者对象为空，则直接返回；
		if (StringUtil.isEmpty(type) || obj == null) {
			return;
		}
		ServletContext servletContext = request.getSession().getServletContext();
		try {
			// 获取系统配置的大图尺寸
			int bigPicWidth = 0;
			try {
				bigPicWidth = Integer.parseInt(Cache.getSystemParamConfig("商品大图尺寸(px)"));
			} catch (Exception e) {
				// TODO: handle exception
				bigPicWidth = 800;
			}
			// 获取系统配置的中图尺寸
			int midPicWidth = 0;
			try {
				midPicWidth = Integer.parseInt(Cache.getSystemParamConfig("商品中图尺寸(px)"));
			} catch (Exception e) {
				// TODO: handle exception
				midPicWidth = 500;
			}
			// 获取系统配置的小图尺寸
			int smallPicWidth = 0;
			try {
				smallPicWidth = Integer.parseInt(Cache.getSystemParamConfig("商品小图尺寸(px)"));
			} catch (Exception e) {
				// TODO: handle exception
				smallPicWidth = 100;
			}

			// 如果传的类型是商品图
			if (PIC_TYPE_PIC.equals(type)) {
				// 将对象强转成商品对象
				Product product = (Product) obj;

				// 获取原图路径
				String picturePath = product.getPicture();

				// 得到后缀名
				String suffix = picturePath.substring(picturePath.lastIndexOf("."), picturePath.length());

				// 得到文件路径(无后缀名)
				String prefix = picturePath.substring(0, picturePath.lastIndexOf("."));

				// 大图路径
				String bigPicturePath = prefix + "big" + suffix;
				try {
					Im4Java.cutImage(bigPicWidth, bigPicWidth, servletContext.getRealPath(picturePath), servletContext.getRealPath(bigPicturePath));
				} catch (Exception e) {
					LocalImgCovert localImg = new LocalImgCovert();
					localImg.compressPic(servletContext.getRealPath(picturePath), servletContext.getRealPath(bigPicturePath), bigPicWidth, bigPicWidth, true);
				}

				product.setBigPicture(bigPicturePath);

				// 中图路径
				String midPicturePath = prefix + "middle" + suffix;
				// 压缩为中图保存

				try {
					Im4Java.cutImage(midPicWidth, midPicWidth, servletContext.getRealPath(picturePath), servletContext.getRealPath(midPicturePath));
				} catch (Exception e) {
					LocalImgCovert localImg = new LocalImgCovert();
					localImg.compressPic(servletContext.getRealPath(picturePath), servletContext.getRealPath(midPicturePath), midPicWidth, midPicWidth, true);
				}

				product.setCenterPicture(midPicturePath);

				// 小图路径
				String smallPicturePath = prefix + "small" + suffix;
				// 压缩为小图保存

				try {
					Im4Java.cutImage(smallPicWidth, smallPicWidth, servletContext.getRealPath(picturePath), servletContext.getRealPath(smallPicturePath));
				} catch (Exception e) {
					LocalImgCovert localImg = new LocalImgCovert();
					localImg.compressPic(servletContext.getRealPath(picturePath), servletContext.getRealPath(smallPicturePath), smallPicWidth, smallPicWidth, true);
				}
				product.setSmallPicture(smallPicturePath);

				// 推荐图尺寸1(px)
				String popPic1Path = prefix + "pop1" + suffix;
				// 压缩为推荐图1保存
				int picpop1Width = 0;
				try {
					picpop1Width = Integer.parseInt(Cache.getSystemParamConfig("推荐图尺寸1(px)"));
				} catch (Exception e) {
					// TODO: handle exception
					picpop1Width = 50;
				}

				try {
					Im4Java.cutImage(picpop1Width, picpop1Width, servletContext.getRealPath(picturePath), servletContext.getRealPath(popPic1Path));
				} catch (Exception e) {
					LocalImgCovert localImg = new LocalImgCovert();
					localImg.compressPic(servletContext.getRealPath(picturePath), servletContext.getRealPath(popPic1Path), picpop1Width, picpop1Width, true);
				}
				product.setRecommendPicture1(popPic1Path);

				// 推荐图尺寸2(px)
				String popPic2Path = prefix + "pop2" + suffix;
				// 压缩为推荐图1保存
				int picpop2Width = 0;
				try {
					picpop2Width = Integer.parseInt(Cache.getSystemParamConfig("推荐图尺寸2(px)"));
				} catch (Exception e) {
					// TODO: handle exception
					picpop2Width = 100;
				}

				try {
					Im4Java.cutImage(picpop2Width, picpop2Width, servletContext.getRealPath(picturePath), servletContext.getRealPath(popPic2Path));
				} catch (Exception e) {
					LocalImgCovert localImg = new LocalImgCovert();
					localImg.compressPic(servletContext.getRealPath(picturePath), servletContext.getRealPath(popPic2Path), picpop2Width, picpop2Width, true);
				}
				product.setRecommendPicture2(popPic2Path);

				// 推荐图尺寸3(px)
				String popPic3Path = prefix + "pop3" + suffix;
				// 压缩为推荐图3保存
				int picpop3Width = 0;
				try {
					picpop3Width = Integer.parseInt(Cache.getSystemParamConfig("推荐图尺寸3(px)"));
				} catch (Exception e) {
					// TODO: handle exception
					picpop3Width = 150;
				}

				try {
					Im4Java.cutImage(picpop3Width, picpop3Width, servletContext.getRealPath(picturePath), servletContext.getRealPath(popPic3Path));
				} catch (Exception e) {
					LocalImgCovert localImg = new LocalImgCovert();
					localImg.compressPic(servletContext.getRealPath(picturePath), servletContext.getRealPath(popPic3Path), picpop3Width, picpop3Width, true);
				}
				product.setRecommendPicture3(popPic3Path);

				// 推荐图尺寸4(px)
				String popPic4Path = prefix + "pop4" + suffix;
				// 压缩为推荐图4保存
				int picpop4Width = 0;
				try {
					picpop4Width = Integer.parseInt(Cache.getSystemParamConfig("推荐图尺寸4(px)"));
				} catch (Exception e) {
					// TODO: handle exception
					picpop4Width = 200;
				}

				try {
					Im4Java.cutImage(picpop4Width, picpop4Width, servletContext.getRealPath(picturePath), servletContext.getRealPath(popPic4Path));
				} catch (Exception e) {
					LocalImgCovert localImg = new LocalImgCovert();
					localImg.compressPic(servletContext.getRealPath(picturePath), servletContext.getRealPath(popPic4Path), picpop4Width, picpop4Width, true);
				}
				product.setRecommendPicture4(popPic4Path);

				// 推荐图尺寸5(px)
				String popPic5Path = prefix + "pop5" + suffix;
				// 压缩为推荐图5保存
				int picpop5Width = 0;
				try {
					picpop5Width = Integer.parseInt(Cache.getSystemParamConfig("推荐图尺寸5(px)"));
				} catch (Exception e) {
					// TODO: handle exception
					picpop5Width = 250;
				}

				try {
					Im4Java.cutImage(picpop5Width, picpop5Width, servletContext.getRealPath(picturePath), servletContext.getRealPath(popPic5Path));
				} catch (Exception e) {
					LocalImgCovert localImg = new LocalImgCovert();
					localImg.compressPic(servletContext.getRealPath(picturePath), servletContext.getRealPath(popPic5Path), picpop5Width, picpop5Width, true);
				}
				product.setRecommendPicture5(popPic5Path);
			} else if (PIC_TYPE_POP.equals(type)) {
				// 如果传进来的类型是推荐图，则将对象强转成推荐图
				ProductPicture pPicture = (ProductPicture) obj;
				// 获取原图片路径
				String oldPath = pPicture.getPicture();
				// 得到后缀名
				String suffix = oldPath.substring(oldPath.lastIndexOf("."), oldPath.length());

				// 得到文件路径(无后缀名)
				String prefix = oldPath.substring(0, oldPath.lastIndexOf("."));

				// 大图路径
				String bigPicturePath = prefix + "big" + suffix;
				// 压缩为大图保存

				try {
					Im4Java.cutImage(bigPicWidth, bigPicWidth, servletContext.getRealPath(oldPath), servletContext.getRealPath(bigPicturePath));
				} catch (Exception e) {
					LocalImgCovert localImg = new LocalImgCovert();
					localImg.compressPic(servletContext.getRealPath(oldPath), servletContext.getRealPath(bigPicturePath), bigPicWidth, bigPicWidth, true);
				}
				pPicture.setBigPicture(bigPicturePath);

				// 中图路径
				String midPicturePath = prefix + "middle" + suffix;
				// 压缩为中图保存

				try {
					Im4Java.cutImage(midPicWidth, midPicWidth, servletContext.getRealPath(oldPath), servletContext.getRealPath(midPicturePath));
				} catch (Exception e) {
					LocalImgCovert localImg = new LocalImgCovert();
					localImg.compressPic(servletContext.getRealPath(oldPath), servletContext.getRealPath(midPicturePath), midPicWidth, midPicWidth, true);
				}
				pPicture.setMidPicture(midPicturePath);

				// 小图路径
				String smallPicturePath = prefix + "small" + suffix;
				// 压缩为小图保存
				try {
					Im4Java.cutImage(smallPicWidth, smallPicWidth, servletContext.getRealPath(oldPath), servletContext.getRealPath(smallPicturePath));
				} catch (Exception e) {
					LocalImgCovert localImg = new LocalImgCovert();
					localImg.compressPic(servletContext.getRealPath(oldPath), servletContext.getRealPath(smallPicturePath), smallPicWidth, smallPicWidth, true);
				}
				pPicture.setSmallPicture(smallPicturePath);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 设置商品模板的Map，用于商品编辑展示模板关联等
	 * 
	 * @Title: configTempDetailInfoMap
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param detailMap
	 * @param @param tempId
	 * @param @param tempValue
	 * @param @param tempPic 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public static void configTempDetailInfoMap(Map<String, Map<String, String>> detailMap, String tempId, String tempValue, String tempPic) {
		Map<String, String> valueMap = null;
		// 如果Map中存在该Key值的，则将值放入Map
		if (detailMap.containsKey(tempId)) {
			valueMap = detailMap.get(tempId);
		}
		// 如果值Map为空，则定义一个新Map
		if (valueMap == null) {
			valueMap = new HashMap<String, String>();
		}
		// 以模板字段值作为主键，将值、图片放入Map
		if (!valueMap.containsKey(tempValue)) {
			valueMap.put(tempValue, tempPic);
		}
		// 将值Map放入明细Map
		detailMap.put(tempId, valueMap);
	}

	/**
	 * 判断两个字符串是否有变动
	 * 
	 * @Title: checkDeffrens
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param str1
	 * @param @param str2
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public static boolean checkDeffrens(String str1, String str2) {
		// 如果两者都为空，则返回false
		if (StringUtil.isEmpty(str1) && StringUtil.isEmpty(str2)) {
			return false;
		}

		if (!StringUtil.isEmpty(str1) && StringUtil.isEmpty(str2)) {
			return true;
		}

		if (StringUtil.isEmpty(str1) && !StringUtil.isEmpty(str2)) {
			return true;
		}

		if (str1.equals(str2)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 根据商品购买数量获取商品的价格
	 * 
	 * @param priceList
	 * @param quantity
	 * @return
	 */
	public static ProductPrice getPriceByQuantity(List<ProductPrice> priceList, int quantity) {
		if (priceList == null || priceList.size() <= 0) {
			return null;
		}
		for (int i = 0; i < priceList.size(); i++) {
			ProductPrice price = priceList.get(i);
			if (price != null) {
				if (price.getEndNum() != 0) {
					if (quantity >= price.getStartNum() && quantity < price.getEndNum()) {
						return price;
					}
				} else {
					if (quantity >= price.getStartNum()) {
						return price;
					}
				}
			}
		}
		return null;
	}
}
