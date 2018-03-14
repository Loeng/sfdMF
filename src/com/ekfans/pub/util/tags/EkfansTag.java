package com.ekfans.pub.util.tags;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import jetbrick.template.runtime.JetTagContext;
import jetbrick.template.runtime.JetWriter;
import jetbrick.template.web.JetWebContext;

import com.ekfans.base.product.model.ProductCategory;
import com.ekfans.base.system.util.SystemAreaHelper;
import com.ekfans.controllers.tools.util.FileUploadHelper;
import com.ekfans.plugin.page.BasicRequest;
import com.ekfans.pub.util.StringUtil;
import com.ekfans.pub.util.tags.util.FileConst;

/**
 * 易科自定义标签
 * 
 * @ClassName: EkfansTag
 * @Description: 用来存放易科自定义标签以及其实现
 * @author 成都易科远见科技有限公司
 * @date 2014-5-6 下午05:42:15
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class EkfansTag {

	/**
	 * 图片上传的自定义标签
	 * 
	 * @Title: file 文件上传
	 * @param @param ctx JetBirck标准必带
	 * @param @param property 名称/ID
	 * @param @param fileType 上传文件类型(具体见FileConst)
	 * @param @param maxSize 上传文件的最大尺寸(不填则默认为1M，以M为单位，可以是小数)
	 * @param @param width 上传图片时使用，用来限制图片展示框的宽度
	 * @param @param height 上传图片时使用，用来限制图片展示框的高度
	 * @param @param value 原图片路径
	 * @param @throws IOException 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public static void picUpload(JetTagContext ctx, String property, String maxSize, String width, String height, String value) throws IOException {
		// 获取HttpServletRequest
		HttpServletRequest request = (HttpServletRequest) ctx.getContext().get(JetWebContext.REQUEST);

		// 解析图片宽度，将"px"去掉，只去数字部分
		if (!StringUtil.isEmpty(width)) {
			width = FileConst.getNumbers(width);
		}
		if (StringUtil.isEmpty(width) || (!StringUtil.isEmpty(width) && Integer.parseInt(width) <= 150)) {
			width = "150";
		}

		// 解析图片高度，将"px"去掉，只去数字部分
		if (!StringUtil.isEmpty(height)) {
			height = FileConst.getNumbers(height);
		}
		if (StringUtil.isEmpty(height) || (!StringUtil.isEmpty(height) && Integer.parseInt(height) <= 150)) {
			height = "150";
		}

		String defaultUrl = "/resources/commons/images/upload/imgdefault.jpg";
		String urlPath = "";
		// 拼写文件全路径
		if (!StringUtil.isEmpty(value)) {
			BasicRequest brequest = new BasicRequest(request);
			if (brequest != null) {
				urlPath = brequest.getImage(value);
				defaultUrl = brequest.getImage(defaultUrl);
			}
		} else {
			BasicRequest br = new BasicRequest(request);
			// http://www.ekfans.com:8080/
			StringBuffer urlStr = new StringBuffer(br.getWebFullUrlPrex());
			defaultUrl = urlStr + "/resources/commons/images/upload/imgdefault.jpg";
		}

		// 设置最大尺寸，如果没有传过来，则默认为1T
		if (StringUtil.isEmpty(maxSize)) {
			maxSize = (1024 * 1024) + "";
		} else {
			// 新增不能大于500kb的需求，这里不再适用整数
			// maxSize = FileConst.getNumbers(maxSize);
		}

		// HttpSession session = this.pageContext.getSession();
		StringBuffer str = new StringBuffer("");

		// 拼写div字符串
		str.append("<div class=\"imgUpload\" style=\"width:" + width + "px;height:" + height + "px;\">");
		str.append("<div class=\"imgWindow\" id=\"" + property + "Purview\" style=\"height:" + ((int) (Integer.parseInt(height) * 0.86) - 2) + "px;width:" + (int) (Integer.parseInt(width) - 2) + "px;\"\" >");

		str.append("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr><td>");
		str.append("<img src=\"" + defaultUrl + "\" height=\"" + ((int) (Integer.parseInt(height) * 0.86) - 3) + "px\" id=\"" + property + "Img\"></td></tr></table>");

		str.append("</div>");

		int aHeight = (int) (Integer.parseInt(height) * 0.14);
		int fontSize = (int) (aHeight * 0.7);

		str.append("<a class=\"btnUpload\" href=\"#\" id=\"" + property + "A\" style=\"height:" + aHeight + "px;line-height:" + aHeight + "px;font-size:" + fontSize + "px;\"><input name=\"" + property + "InputFile\" id=\"" + property + "InputFile\" type=\"file\">上传图片</a>");
		str.append("</div>");

		// 拼写隐藏域，用来传递值
		str.append("<input type='hidden' name='").append(property).append("remove' id='").append(property).append("remove' value='false'/> \r\n");
		str.append("<input type='hidden' name='").append(property).append("OldUrlPath' id='").append(property).append("OldUrlPath' value='").append(value).append("'/> \r\n");
		str.append("<input type='hidden' name='").append(property).append("OldRealPath' id='").append(property).append("OldRealPath' value='").append(urlPath).append("'/> \r\n");
		str.append("<input type='hidden' name='").append(property).append("FileUrl' id='").append(property).append("FileUrl' value=''/> \r\n");
		if (!StringUtil.isEmpty(value)) {
			str.append("<input type='hidden' name='").append(property).append("editUrl' id='").append(property).append("editUrl' value='true'/> \r\n");
		} else {
			str.append("<input type='hidden' name='").append(property).append("editUrl' id='").append(property).append("editUrl' value='false'/> \r\n");
		}

		if (!StringUtil.isEmpty(urlPath)) {
			try {
				// URL url = new URL(this.urlPath);
				// BufferedImage image = ImageIO.read(url);
				BufferedImage image = ImageIO.read(new File(urlPath));
				int imgWidth = image.getWidth();
				int imgHeight = image.getHeight();

				str.append("<input type='hidden' name='").append(property).append("wh' id='").append(property).append("wh' value='").append(imgWidth).append(",").append(imgHeight).append("'/> \r\n");
			} catch (IOException e) {
				str.append("<input type='hidden' name='").append(property).append("wh' id='").append(property).append("wh' value=''/> \r\n");
			}
		} else {
			str.append("<input type='hidden' name='").append(property).append("wh' id='").append(property).append("wh' value=''/> \r\n");
		}

		// 调用各个类型的JS
		str.append("<script> ajaxChosePic('").append(property).append("','").append(Integer.parseInt(width) - 4).append("','").append((int) (Integer.parseInt(height) * 0.86) - 3).append("','").append(maxSize).append("'); </script> \r\n");
		ctx.getWriter().print(str.toString());
	}

	/**
	 * 文件上传的自定义标签
	 * 
	 * @Title: file 文件上传
	 * @param @param ctx JetBirck标准必带
	 * @param @param property 名称/ID
	 * @param @param fileType 上传文件类型(具体见FileConst)
	 * @param @param maxSize 上传文件的最大尺寸(不填则默认为1M，以M为单位，可以是小数)
	 * @param @param width 上传图片时使用，用来限制图片展示框的宽度
	 * @param @param height 上传图片时使用，用来限制图片展示框的高度
	 * @param @param value 原图片路径
	 * @param @throws IOException 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	/*
	public static void fileUpload(JetTagContext ctx, String property, String fileType) throws IOException {
		StringBuffer str = new StringBuffer("");

		// 上传的文件类型为除图片之外的所有类型
		str.append("<div class=\"fileUpload\">");
		str.append("<div class=\"i_bg fileNameBox\" id=\"" + property + "NameSpan\"></div>");
		str.append("<a href=\"javascript:void(0)\" class=\"btnUpload\" id=\"" + property + "A\"><input name=\"" + property + "InputFile\" id=\"" + property + "InputFile\" type=\"file\">上传文件</a>");
		str.append("<a href=\"javascript:void(0)\" class=\"fileDelete\" id=\"" + property + "Del\"></a>");
		str.append("</div>");

		// str.append("<div class=\"fileUpload\">");
		// str.append("<a class=\"btnUpload\" href=\"#\" id=\"" + property +
		// "A\"><input name=\"" + property + "InputFile\" id=\"" + property +
		// "InputFile\" type=\"file\">上传文件</a>");
		// str.append("<span class=\"fileLoading\" id=\""+property+"Loading\" style=\"display:none;\"></span>");
		// str.append("<span class=\"fileName\" id=\"" + property +
		// "NameSpan\">请选择您需要上传的文件</span></div>");

		// 拼写隐藏域，用来传递值
		str.append("<input type='hidden' name='").append(property).append("remove' id='").append(property).append("remove' value='false'/> \r\n");
		str.append("<input type='hidden' name='").append(property).append("FileUrl' id='").append(property).append("FileUrl' value=''/> \r\n");

		// 调用各个类型的JS
		if (FileConst.FILE_TYPE_EXCEL.equals(fileType)) {
			str.append("<script> ajaxChoseExcel('").append(property).append("'); </script> \r\n");
		} else if (FileConst.FILE_TYPE_WORD.equals(fileType)) {
			str.append("<script> ajaxChoseWord('").append(property).append("'); </script> \r\n");
		} else if (FileConst.FILE_TYPE_ZIP.equals(fileType)) {
			str.append("<script> ajaxChoseZip('").append(property).append("'); </script> \r\n");
		}
		ctx.getWriter().print(str.toString());
	}
	*/
	/**
     * 文件上传自定义控件
     * 
     * @param ctx
     *            jetbrick自带标签
     * @param property
     *            名称/ID
     * @param fileType
     *            上传文件的类型
     * @param value
     *            原路径
     * @throws IOException
     */
    public static void fileUpload(JetTagContext ctx, String property, String fileType, String value) throws IOException {
        StringBuffer str = new StringBuffer("");

        // 获取HttpServletRequest
        HttpServletRequest request = (HttpServletRequest) ctx.getContext().get(JetWebContext.REQUEST);

        String urlPath = "";
        String fileName = "";

        if (!StringUtil.isEmpty(value)) {
            BasicRequest brequest = new BasicRequest(request);
            if (brequest != null) {
                urlPath = brequest.getFileFullUrl(value);
            }
            if (value.indexOf("/") != -1) {
                fileName = value.substring(value.lastIndexOf("/") + 1, value.length());
            }

        }
        if (!StringUtil.isEmpty(urlPath) && !StringUtil.isEmpty(fileName)) {
            // 上传的文件类型为除图片之外的所有类型
            str.append("<div class=\"fileUpload\">");
            str.append("<a href=\"javascript:void(0);\" id=\"").append(property).append("A\" class=\"btnUploadNo\">");
            // str.append("<input name=\"").append(property).append("InputFile\" id=\"").append(property).append("InputFile\" disabled type=\"file\">");
            str.append("上传文件</a>");
            str.append("<span class=\"fileLoading\" style=\"display:none;\"></span>");
            str.append("<span class=\"fileName\">");
            // 调用方法获取链接的路径
            String link = FileUploadHelper.getFilePurviewUrl(request, value);
            str.append("<a href=\"").append(link).append("\" id=\"").append(property).append("PurviewA\" target=\"_black\">").append(fileName).append("</a>");
            str.append("<a href=\"javascript:removeFile('").append(property).append("','").append(fileType).append("');\" id=\"").append(property).append("PurviewDel\" class=\"fileDelete\"></a>");
        } else {
            str.append("<div class=\"fileUpload\">");
            str.append("<a href=\"javascript:void(0);\" id=\"").append(property).append("A\" class=\"btnUpload\">");
            str.append("<input name=\"").append(property).append("InputFile\" id=\"").append(property).append("InputFile\" type=\"file\">");
            str.append("上传文件</a>");
            str.append("<span class=\"fileLoading\"  id=\"").append(property).append("Loading\" style=\"display:none;\"></span>");
            str.append("<span class=\"fileName\">");

            String typeName = "";
            if (!StringUtil.isEmpty(fileType)) {
                typeName = FileConst.fileTypeNames.get(fileType);
            }
            if (StringUtil.isEmpty(typeName)) {
                typeName = "文件";
            }
            str.append("<a href=\"javascript:void(0);\"  id=\"").append(property).append("PurviewA\" target=\"_black\">").append("请选择需要上传的").append(typeName).append("!").append("</a>");
            str.append("<a href=\"javascript:removeFile('").append(property).append("','").append(fileType).append("');\"  id=\"").append(property).append("PurviewDel\" style=\"display:none;\" class=\"fileDelete\"></a>");
        }

        str.append("</span>");
        str.append("</div>");

        // 拼写隐藏域，用来传递值
        str.append("<input type='hidden' name='").append(property).append("remove' id='").append(property).append("remove' value='false'/> \r\n");
        str.append("<input type='hidden' name='").append(property).append("FileUrl' id='").append(property).append("FileUrl' value=''/> \r\n");
        str.append("<input type='hidden' name='").append(property).append("OldUrlPath' id='").append(property).append("OldUrlPath' value='").append(value).append("'/> \r\n");

        if (StringUtil.isEmpty(urlPath) || StringUtil.isEmpty(fileName)) {
            // 调用各个类型的JS
            str.append("<script> ajaxChoseFile('").append(property).append("','").append(fileType).append("'); </script> \r\n");
        }

        JetWriter out = ctx.getWriter();
        out.print(str.toString());
    }

	/**
	 * 选择地区的自定义标签
	 * 
	 * @param ctx
	 * @param property
	 * @param areaId
	 * @throws IOException
	 */
	public static void systemAreaSel(JetTagContext ctx, String property, String areaId) throws IOException {
		String html = SystemAreaHelper.getAreaSelecs(property,areaId, true, "");
		html = html + "<input type=\"hidden\" name=\"" + property + "\" id=\"" + property + "\" value=\"" + areaId + "\">";

		// 获取HttpServletRequest
		HttpServletRequest request = (HttpServletRequest) ctx.getContext().get(JetWebContext.REQUEST);
		html = html + "<script>$(\"."+property+"Area\").live(\"change\",function(){";
		html = html + "var val=$(this).find(\"option:selected\").attr(\"value\");";
		html = html + "$(\"#" + property + "\").val(val);";
		html = html + "var obj = $(this);";
		html = html + "obj.nextAll(\"."+property+"Area\").remove();";
		html = html + "if(val!=\"\"){";
		html = html + "$.post(";
		html = html + "\"" + request.getContextPath() + "/area/sel/\"+val+\"/"+property+"\",";
		html = html + "function(data){";
		html = html + "obj.parent().append(data+\"\");";
		html = html + "});";
		html = html + "}";
		html = html + "});</script>";

		ctx.getWriter().print(html);

	}

	/**
	 * 短路径转换
	 * 
	 * @param ctx
	 *            模板容器
	 * @param type
	 *            路劲类型
	 * @param uuid
	 *            数据编号
	 * @throws IOException
	 */
	public static void shortLink(JetTagContext ctx, String type, Object uuid) throws IOException {
		HttpServletRequest request = (HttpServletRequest) ctx.getContext().get(JetWebContext.REQUEST);
		BasicRequest br = new BasicRequest(request);
		String shortLink = br.getWebUrl(type, uuid.toString());
		ctx.getWriter().print(shortLink);
	}

	/**
	 * 
	 * @Title: printProductCategory
	 * @Description: 打印商品子分类数标签
	 * @param ctx
	 *            JetTagContext
	 * @param categorys
	 *            商品子分类(二级以后)
	 * @throws IOException
	 */
	public static void printProductCategory(JetTagContext ctx, List<ProductCategory> categorys) throws IOException {
		String html = buildProductCategoryChildHtml(categorys);
		ctx.getWriter().print(html);
	}

	/**
	 * @Title: buildProductCategoryChildHtml
	 * @Description: 构建商品子分类树html
	 * @param categorys
	 *            商品子分类(二级以后)
	 * @return String 返回类型
	 * @throws
	 */
	private static String buildProductCategoryChildHtml(List<ProductCategory> categorys) {
		if (null == categorys || 0 == categorys.size()) {
			return "";
		}
		StringBuffer html = new StringBuffer();
		// 遍历二级分类
		for (ProductCategory pc : categorys) {
			html.append("<div class=\"fl_in\">");
			html.append("<span class=\"fl_title\" id=\"");
			html.append(pc.getId());
			html.append("\">");
			html.append(pc.getName());
			html.append("<label>选择</label><em class=\"borderLine\"></em></span>");
			// 二级分类下的子分类
			List<ProductCategory> childList = pc.getChildList();
			if (null != childList && 0 != childList.size()) {// 如果存在下级分类则递归
				html.append(buildProductCategoryChildHtml(childList));
			}
			html.append("</div>");
		}
		return html.toString();
	}
}
