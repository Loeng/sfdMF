package com.ekfans.plugin.number;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ekfans.base.wfOrder.dao.IWfOrderDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ekfans.base.content.dao.IContentCategoryDao;
import com.ekfans.base.content.dao.IContentDao;
import com.ekfans.base.order.dao.IBargainDao;
import com.ekfans.base.order.dao.IOrderDao;
import com.ekfans.base.product.dao.IProductCategoryDao;
import com.ekfans.base.product.dao.IProductDao;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.plugin.wftong.base.easemob.dao.IGroupsDao;
import com.ekfans.pub.util.DateUtil;

/**
 * 
 * @ClassName: NoManager
 * @author 成都易科远见科技有限公司
 * @date 2014-5-20 上午11:12:10
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@SuppressWarnings("rawtypes")
public class NoManager {

	// 定义错误日志
	public static Logger log = LoggerFactory.getLogger(NoManager.class);

	/**
	 * 
	 * @Title: createId
	 * @Description: 自动生成订单id 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @return String 返回类型
	 * @throws
	 */
	public String createOrderId() {
		// 获取当前日期
		String today = DateUtil.getSysDateString().trim();
		today = today.replaceAll("-", "");

		// 定义查询SQL,查询id的最大值
		StringBuffer sql = new StringBuffer(" select max(o.id) from Order as o where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 添加查询条件(今天的订单号)
		sql.append(" and o.id like :today");
		paramMap.put("today", today + "%");

		// 得到IOrderDao
		IOrderDao orderDao = SpringContextHolder.getBean(IOrderDao.class);

		try {
			// 调用DAO方法找到
			List list = orderDao.list(sql.toString(), paramMap);

			// 如果查询到则+1,否则(即为第一个订单)则手动设置id(max没有查询到返回null，所以size为1)
			if (list.get(0) != null) {
				String id = (String) list.get(0);
				DecimalFormat df = new DecimalFormat("0");
				Double intId = Double.parseDouble(id) + 1;
				return df.format(intId).toString();
			} else {
				String id = today + "0000001";
				return id;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * @Title: createTotalOrderId
	 * @Description: TODO(生成总订单号) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @return String 返回类型
	 * @throws
	 */
	public String createTotalOrderId() {
		// 获取当前日期
		String today = DateUtil.getSysDateString().trim();
		today = today.replaceAll("-", "");

		// 定义查询SQL,查询id的最大值
		StringBuffer sql = new StringBuffer(" select max(o.payId) from Order as o where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 添加查询条件(今天的总订单号)
		sql.append(" and o.id like :today");
		paramMap.put("today", today + "%");

		// 得到IOrderDao
		IOrderDao orderDao = SpringContextHolder.getBean(IOrderDao.class);

		try {
			// 调用DAO方法找到
			List list = orderDao.list(sql.toString(), paramMap);

			// 如果查询到则+1,否则(即为第一个订单)则手动设置id(max没有查询到返回null，所以size为1)
			if (list.get(0) != null) {
				String id = (String) list.get(0);
				DecimalFormat df = new DecimalFormat("0");
				Double intId = Double.parseDouble(id) + 1;
				return df.format(intId).toString();
			} else {
				String id = today + "0000001";
				return id;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 生成议价id
	 * @return
	 */
	public String createBargainId() {
		// 获取当前日期
		String today = DateUtil.getSysDateString().trim();
		today = today.replaceAll("-", "");
		
		// 定义查询SQL,查询id的最大值
		StringBuffer sql = new StringBuffer(" select max(b.id) from Bargain as b where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 添加查询条件(今天的总订单号)
		sql.append(" and b.id like :today");
		paramMap.put("today", today + "%");
		
		// 得到IBargainDao
		IBargainDao bargainDao = SpringContextHolder.getBean(IBargainDao.class);
		
		try {
			// 调用DAO方法
			List list = bargainDao.list(sql.toString(), paramMap);
			
			// 如果查询到则+1,否则手动设置id(max没有查询到返回null，所以size为1)
			if (list.get(0) != null) {
				String id = (String) list.get(0);
				DecimalFormat df = new DecimalFormat("0");
				Double intId = Double.parseDouble(id) + 1;
				return df.format(intId).toString();
			} else {
				String id = today + "0000001";
				return id;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * @Title: createProductId
	 * @Description: 自动生成商品id 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @return String 返回类型
	 * @throws
	 */
	public String createProductId() {

		// 定义查询SQL,查询id的最大值
		StringBuffer sql = new StringBuffer(" select max(p.id) from Product as p where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 得到IProductDao
		IProductDao productDao = SpringContextHolder.getBean(IProductDao.class);

		try {
			// 调用DAO方法找到
			List list = productDao.list(sql.toString(), paramMap);

			// 如果查询到则+1,否则(即为第一个商品)则手动设置id
			if (list.get(0) != null) {
				String id = (String) list.get(0);
				Integer idInt = Integer.parseInt(id);
				idInt += 1;
				// 补齐高位的0,并转为String
				String createID = String.format("%08d", idInt);
				return createID;
			} else {
				String id = "00000001";
				return id;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * @Title: createContentId
	 * @Description: 自动生成资讯id 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @return String 返回类型
	 * @throws
	 */
	public String createContentId() {

		// 定义查询SQL,查询id的最大值
		StringBuffer sql = new StringBuffer(" select max(c.id) from Content as c where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 得到IProductDao
		IContentDao contentDao = SpringContextHolder.getBean(IContentDao.class);

		try {
			// 调用DAO方法找到
			List list = contentDao.list(sql.toString(), paramMap);

			// 如果查询到则+1,否则(即为第一条资讯)则手动设置id
			if (list.get(0) != null) {
				String id = (String) list.get(0);
				Integer idInt = Integer.parseInt(id);
				idInt += 1;
				// 补齐高位的0,并转为String
				String createID = String.format("%08d", idInt);
				return createID;
			} else {
				String id = "00000001";
				return id;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * @Title: createContentCategoryId
	 * @Description: 自动生成资讯分类id 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @return String 返回类型
	 * @throws
	 */
	public String createContentCategoryId() {

		// 定义查询SQL,查询id的最大值
		StringBuffer sql = new StringBuffer(" select max(cc.id) from ContentCategory as cc where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 得到IProductDao
		IContentCategoryDao contentCategoryDao = SpringContextHolder.getBean(IContentCategoryDao.class);

		try {
			// 调用DAO方法找到
			List list = contentCategoryDao.list(sql.toString(), paramMap);

			// 如果查询到则+1,否则(即为第一条资讯)则手动设置id
			if (list.get(0) != null) {
				String id = (String) list.get(0);
				Integer idInt = Integer.parseInt(id);
				idInt += 1;
				// 补齐高位的0,并转为String
				String createID = String.format("%08d", idInt);
				return createID;
			} else {
				String id = "00000001";
				return id;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * @Title: createProductCategoryId
	 * @Description: 自动生成商品分类id 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @return String 返回类型
	 * @throws
	 */
	public String createProductCategoryId() {

		// 定义查询SQL,查询id的最大值
		StringBuffer sql = new StringBuffer(" select max(pc.id) from ProductCategory as pc where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 得到IProductCategoryDao
		IProductCategoryDao productCategoryDao = SpringContextHolder.getBean(IProductCategoryDao.class);

		try {
			// 调用DAO方法找到
			List list = productCategoryDao.list(sql.toString(), paramMap);

			// 如果查询到则+1,否则(即为第一个商品分类)则手动设置id
			if (list.get(0) != null) {
				Integer id = (Integer) list.get(0);
				id += 1;
				return id.toString();
			} else {
				String id = "00000001";
				return id;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 
	 * @Title: 
	 * @Description: 自动生成企业id 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @return String 返回类型
	 * @throws
	 */
	public String createGroupsId() {

		// 定义查询SQL,查询id的最大值
		StringBuffer sql = new StringBuffer(
				" select max(c.id) from Groups as c where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 得到IProductDao
		IGroupsDao groupsDao = SpringContextHolder.getBean(IGroupsDao.class);

		try {
			// 调用DAO方法找到
			List list = groupsDao.list(sql.toString(), paramMap);

			// 如果查询到则+1,否则(即为第一条资讯)则手动设置id
			if (list.get(0) != null) {
				String id = (String) list.get(0);
				Integer idInt = Integer.parseInt(id);
				idInt += 1;
				// 补齐高位的0,并转为String
				String createID = String.format("%08d", idInt);
				return createID;
			} else {
				String id = "00000001";
				return id;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 *
	 * @Title: createId
	 * @Description: 自动生成订单id 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @return String 返回类型
	 * @throws
	 */
	public String createWfOrderId() {
		// 获取当前日期
		String today = DateUtil.getSysDateString().trim();
		today = today.replaceAll("-", "");
		today = today.substring(2,today.length());
		today = today.substring(0,6);
		String bankOrderId = "w" + today ;
//		bankOrderId = bankOrderId.substring(0,6) + bankOrderId.substring(bankOrderId.length()-4,bankOrderId.length());
		System.out.println(bankOrderId);

		// 定义查询SQL,查询id的最大值
		StringBuffer sql = new StringBuffer(" select count(*) from WfOrder as o where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 添加查询条件(今天的订单号)
		sql.append(" and o.createTime like :today");
		paramMap.put("today", DateUtil.getSysDateString() + "%");

		// 得到IOrderDao
		IWfOrderDao orderDao = SpringContextHolder.getBean(IWfOrderDao.class);

		try {
			// 调用DAO方法找到
			List list = orderDao.list(sql.toString(), paramMap);
			Long count = (Long) list.get(0);
			// 如果查询到则+1,否则(即为第一个订单)则手动设置id(max没有查询到返回null，所以size为1)
			if(count <= 0 ) {
				return bankOrderId + "001";
			}else if (count >0 && count <= 8) {
				return bankOrderId + "00" + (count + 1);
			} else if(count >= 9 && count <= 98){
				return bankOrderId + "0" + (count + 1);
			} else{
				return bankOrderId + (count + 1);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}
	/**
	 *
	 * @Title: createId
	 * @Description: 自动生成订单id 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @return String 返回类型
	 * @throws
	 */
	public String createWfRelOrderId() {
		// 获取当前日期
		String today = DateUtil.getSysDateString().trim();
		today = today.replaceAll("-", "");
		today = today.substring(2,today.length());
		today = today.substring(0,6);
		String bankOrderId = "wfrel" + today ;
		System.out.println(bankOrderId);

		// 定义查询SQL,查询id的最大值
		StringBuffer sql = new StringBuffer(" select count(*) from WfOrderJiudingPayRel as o where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 得到IOrderDao
		IWfOrderDao orderDao = SpringContextHolder.getBean(IWfOrderDao.class);

		try {
			// 调用DAO方法找到
			List list = orderDao.list(sql.toString(), paramMap);
			Long count = (Long) list.get(0);
			// 如果查询到则+1,否则(即为第一个订单)则手动设置id(max没有查询到返回null，所以size为1)
			if(count <= 0 ) {
				return bankOrderId + "001";
			}else if (count >0 && count <= 8) {
				return bankOrderId + "00" + (count + 1);
			} else if(count >= 9 && count <= 98){
				return bankOrderId + "0" + (count + 1);
			} else{
				return bankOrderId + (count + 1);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}
}
