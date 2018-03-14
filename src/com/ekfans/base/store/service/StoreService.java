package com.ekfans.base.store.service;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ekfans.base.product.model.Product;
import com.ekfans.base.product.model.ProductBrand;
import com.ekfans.base.product.model.ProductCategory;
import com.ekfans.base.store.dao.IStoreDao;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.model.StoreFinancialData;
import com.ekfans.base.store.model.StoreInfo;
import com.ekfans.base.store.model.StoreLegalResume;
import com.ekfans.base.store.util.StoreConst;
import com.ekfans.base.user.dao.IUserDao;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.util.UUIDUtil;
import com.ekfans.base.user.util.UserConst;
import com.ekfans.controllers.web.vo.ProListTemplateVO;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.webService.monitor.MonitorDataConvert;
import com.ekfans.plugin.webService.monitor.MonitorSyncMain;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.PasswordStrengthUtil;
import com.ekfans.pub.util.StringUtil;
import com.ekfans.pub.util.EncDec.MD5Util;

/**
 * 企业基础信息Service接口实现
 * 
 * @ClassName: StoreService
 * @author Lgy
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
@SuppressWarnings({ "unchecked", "rawtypes" })
public class StoreService implements IStoreService {

	private Logger log = LoggerFactory.getLogger(StoreService.class);
	@Resource
	private IStoreDao storeDao;
	@Resource
	private IUserDao userDao;

	@Override
	public Store getStore(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		try {
			return (Store) storeDao.get(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int updateStore(Store store) {
		// 1：成功，2：失败，3：上传企业LOGO，4：上传营业执照，5：企业名称不能为空
		if (store == null) {
			return 2;
		}

		try {
			// 验证企业LOGO
			if (StringUtil.isEmpty(store.getStoreLogo())) {
				return 3;
			}
			// 验证营业执照
			if (StringUtil.isEmpty(store.getBusinessLicense())) {
				return 4;
			}
			// 验证企业名称
			if (StringUtil.isEmpty(store.getStoreName())) {
				return 5;
			}
			// 验证信用代码证
			if (StringUtil.isEmpty(store.getCreditCodeCard())) {
				return 6;
			}
			// 验证公司章程
			if (StringUtil.isEmpty(store.getArticles())) {
				return 7;
			}
			// 验证企业简介
			if (StringUtil.isEmpty(store.getSynopsis())) {
				return 8;
			}
			store.setCommonStatus("1");
			store.setUpdateTime(DateUtil.getSysDateTimeString());
			storeDao.updateBean(store);

			return 1;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return 2;
	}

	/**
	 * 更新企业信息
	 * 
	 * @param store
	 * @return
	 */
	public boolean updateStoreOnly(Store store) {
		if (store == null) {
			return false;
		}
		try {
			storeDao.updateBean(store);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	@Override
	public Boolean checkStoreNameUpdate(String oldStoreName, String newStoreName) {
		// param data
		Map<String, Object> params = new HashMap<String, Object>();
		// hql
		String hql = "from Store where storeName!=:oldStoreName and storeName=:newStoreName";
		// setting data
		params.put("oldStoreName", oldStoreName.trim());
		params.put("newStoreName", newStoreName.trim());

		try {
			List<User> list = storeDao.list(hql, params);

			return (list == null || list.size() <= 0) ? false : true;
		} catch (Exception e) {
			log.error(e.getMessage());
			return true;
		}
	}

	/**
	 * 添加店铺
	 * 
	 * @param store
	 * @return
	 */
	public boolean addStore(Store store, File uploadFile, String uploadFileContentType, boolean checkStatus) {
		try {

			// 设置创建时间为当前系统时间
			store.setCreateTime(DateUtil.getSysDateTimeString());
			// 将店铺资料保存到数据库
			storeDao.addBean(store);
			// 返回true
			return true;
		} catch (Exception e) {
			// 若出现异常，返回false
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 根据id删除店铺
	 */
	public boolean deleteStore(String id) {
		// 如果传进来的id为空，则返回false
		if (StringUtil.isEmpty(id)) {
			return false;
		}

		try {
			// 调用SERVICE的方法删除店铺
			storeDao.deleteById(id);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 修改店铺资料
	 */
	public boolean modifyStore(Store store, File uploadFile, String uploadFileContentType) {
		// 如果传进来的店铺对象为空，则返回false
		if (store == null) {
			return false;
		}
		try {

			// 删除旧头像
			if (!StringUtil.isEmpty(store.getStoreLogo())) {
				File file = new File(store.getStoreLogo());
				if (file.exists()) {
					file.delete();
				}
			}

			// 设置更新时间为当前系统时间
			store.setUpdateTime(DateUtil.getSysDateTimeString());
			// 调用DAO的方法修改店铺
			storeDao.updateBean(store);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<Store> listStore(Pager pager, String userType, Boolean status, String name, String mobile, String email, String userName) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select s.id,s.storeName,u.mobile,u.email,s.createTime,s.storeLogo,u.name from Store as s,User as u where 1=1");

		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		sql.append(" and s.id = u.id");
		// 如果查询条件输入了status，添加查询条件
		if (status) {
			sql.append(" and u.status = :status");
			paramMap.put("status", 1);

			sql.append(" and u.verificationStatus = :verificationStatus");
			paramMap.put("verificationStatus", true);
		}

		// 如果查询条件输入了name，添加查询条件
		if (!StringUtil.isEmpty(name)) {
			sql.append(" and s.storeName like :name");
			paramMap.put("name", "%" + name + "%");
		}

		// 如果查询条件输入了status，添加查询条件
		if (!StringUtil.isEmpty(mobile)) {
			// 添加查询条件
			sql.append(" and u.mobile like :mobile");
			paramMap.put("mobile", "%" + mobile + "%");
		}
		if (!StringUtil.isEmpty(email)) {
			// 添加查询条件
			sql.append(" and u.email like :email");
			paramMap.put("email", "%" + email + "%");
		}
		if (!StringUtil.isEmpty(userName)) {
			// 添加查询条件
			sql.append(" and u.name like :userName");
			paramMap.put("userName", "%" + userName + "%");
		}

		if (!StringUtil.isEmpty(userType)) {
			// 添加查询条件
			sql.append(" and u.type = :userType");
			paramMap.put("userType", userType);
		} else {
			sql.append(" and u.type != :userType");
			paramMap.put("userType", UserConst.USER_TYPE_BUYER);
		}

		// sql.append(" and s.checkStatus = 1");
		sql.append(" order by s.createTime desc");
		try {
			// 调用DAO方法查找商品
			List<Object[]> list = storeDao.list(pager, sql.toString(), paramMap);
			List<Store> stores = new ArrayList<Store>();
			for (Object[] object : list) {
				Store store = new Store();
				store.setId((String) object[0]);
				store.setStoreName((String) object[1]);
				// store.setCity((String) object[2]);
				// store.setArea((String) object[3]);
				store.setCreateTime((String) object[4]);
				store.setStoreLogo((String) object[5]);
				// store.setCoordinateX((String) object[6]);
				stores.add(store);
			}

			return stores;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Store> listUncheckStore(Pager pager, String status, String name, String mobile, String email, String userName) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select s.id,s.storeName,s.levelId,u.mobile,u.email,s.checkStatus,s.createTime,s.status,u.name from Store as s,User as u where 1=1");

		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		sql.append(" and s.id = u.id");
		// 如果查询条件输入了status，添加查询条件
		if (!StringUtil.isEmpty(status)) {
			sql.append(" and s.status = :status");
			paramMap.put("status", Boolean.parseBoolean(status));
		}

		// 如果查询条件输入了name，添加查询条件
		if (!StringUtil.isEmpty(name)) {
			sql.append(" and s.storeName like :name");
			paramMap.put("name", "%" + name + "%");
		}

		// 如果查询条件输入了status，添加查询条件
		if (!StringUtil.isEmpty(mobile)) {
			// 添加查询条件
			sql.append(" and u.mobile like :mobile");
			paramMap.put("mobile", "%" + mobile + "%");
		}
		if (!StringUtil.isEmpty(email)) {
			// 添加查询条件
			sql.append(" and u.email like :email");
			paramMap.put("email", "%" + email + "%");
		}
		if (!StringUtil.isEmpty(userName)) {
			// 添加查询条件
			sql.append(" and u.name like :userName");
			paramMap.put("userName", "%" + userName + "%");
		}
		// sql.append(" and s.checkStatus = 0");
		sql.append(" order by s.createTime desc");
		try {
			// 调用DAO方法查找商品
			List<Object[]> list = storeDao.list(pager, sql.toString(), paramMap);
			List<Store> stores = new ArrayList<Store>();
			for (Object[] object : list) {
				Store store = new Store();
				store.setId((String) object[0]);
				store.setStoreName((String) object[1]);
				// store.setLevelId((String) object[2]);
				// store.setCity((String) object[3]);
				// store.setArea((String) object[4]);
				// store.setCheckStatus((Integer) object[5]);
				store.setCreateTime((String) object[6]);
				// store.setStatus((Boolean) object[7]);
				// store.setCoordinateX((String) object[8]);
				stores.add(store);
			}

			return stores;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据店铺登录名获取店铺对象
	 * 
	 * @param storeName
	 * @return
	 */
	public Object[] storeLoginByName(String storeName) {
		// 如果传进来的用户名为空，则返回null
		if (StringUtil.isEmpty(storeName)) {
			return null;
		}
		// 拼写查询店铺的SQL
		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("select user.password,store.id,store.storeName,store.storeLogo,store.domain,store.roleId from Store as store,User as user where 1=1");
		// 将店铺与会员对应起来
		sql.append(" and store.id = user.id");
		// 将店铺用户名作为SQL搜索条件
		sql.append(" and user.name = :name");
		// 将传进来的用户名作为参数放进sql
		paramMap.put("name", storeName);
		// 会员类型为：店铺会员
		sql.append(" and user.type = :userType");
		// 放入参数
		paramMap.put("userType", UserConst.USER_TYPE_CODE);

		try {
			// 调用DAO方法执行SQL
			List list = storeDao.list(sql.toString(), paramMap);
			// 如果返回的List集合不为空，并且长度大于0，就获取第一个对象
			if (list != null && list.size() > 0) {
				Object[] obj = (Object[]) list.get(0);
				if (obj != null && obj.length > 0) {
					// 定义返回的Object数组
					Object[] returnObj = new Object[2];
					// 获取密码
					String password = (String) obj[0];
					returnObj[0] = password;
					// 定义一个Store 并将Store放入Object数组中返回
					Store store = new Store();
					store.setId((String) obj[1]);
					store.setStoreName((String) obj[2]);
					store.setStoreLogo((String) obj[3]);
					store.setDomain((String) obj[4]);
					store.setRoleId((String) obj[5]);
					returnObj[1] = store;
					return returnObj;

				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 新增商品时查询店铺列表
	 */
	public List<Store> listStore() {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select s from Store as s where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		try {
			List<Store> list = storeDao.list(sql.toString(), paramMap);
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 设置域名
	 * 
	 * @throws Exception
	 */
	public boolean updateDomain(Store store) throws Exception {
		if (store == null) {
			return false;
		}
		// 创建Store对象获取传过来的store参数
		storeDao.updateBean(store);
		return true;
	}

	/**
	 * 查看域名是否被使用
	 * 
	 * @throws Exception
	 */
	public boolean existDomain(String domain, String storeId) throws Exception {
		// 创建SQL语句
		StringBuffer sql = new StringBuffer("select store from Store as store where 1=1");
		// 将 用户输入的域名 和 登录用户的ID作为判断条件,查看如果域名相同,但是ID不同的对象是否存在
		// 存在就说明域名重复,不存在说明域名没有重复可以使用
		sql.append(" and store.domain=:domain");
		sql.append(" and store.id !=:storeId ");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("domain", domain);
		map.put("storeId", storeId);
		// 调用Dao方法执行SQL语句
		List<Store> list = storeDao.list(sql.toString(), map);
		if (list != null && list.size() > 0) {
			return false;
		}
		return true;
	}

	/**
	 * 修改商户的信息
	 * 
	 * @throws Exception
	 */
	public boolean updateStoreInfo(Store store) {
		try {
			if (StringUtil.isEmpty(store.getId())) {
				return false;
			}
			// 获取数据的原始store
			Store hibernateStore = (Store) storeDao.get(store.getId());

			// 界面穿过来的格式为（ 四川 成都 德阳）的地址劈开 分别存入 省市区
			String delimiter = Cache.getResource("area.name.delimiter");
			if (delimiter == null || "&nbsp;".equals(delimiter)) {
				delimiter = " ";
			}
			// String[] tempArea = store.getProvince().split(delimiter);
			/*
			 * for (int i = 0; i < tempArea.length; i++) {
			 * 
			 * if (i == 0) { String province = tempArea[i].trim();
			 * hibernateStore.setProvince(province);
			 * hibernateStore.setCity(null); hibernateStore.setArea(null); } if
			 * (i == 1) { String city = tempArea[i].trim();
			 * hibernateStore.setCity(city); hibernateStore.setArea(null); }
			 * 
			 * if (i == 2) { String area = tempArea[i].trim();
			 * hibernateStore.setArea(area); } } // 详细地址
			 * hibernateStore.setAddress(store.getAddress());
			 */
			// 店铺名字
			hibernateStore.setStoreName(store.getStoreName());
			// 店铺介绍
			hibernateStore.setNotes(store.getNotes());
			// 更新时间
			hibernateStore.setUpdateTime(DateUtil.getSysDateTimeString());

			storeDao.updateBean(hibernateStore);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return false;
	}

	/**
	 * 查看店铺名是否被使用 true 重复 false 可以用
	 * 
	 * @throws Exception
	 */
	public boolean existStoreName(String name, String storeId) throws Exception {
		// 创建SQL语句
		StringBuffer sql = new StringBuffer("select store from Store as store where 1=1");
		// 将店铺名与店铺ID作为查询条件,如果店铺名相等,并且店铺ID不相等的对象存在,说明店铺名重复
		// 如果不存在,说明店铺名不重复,可以使用
		sql.append(" and store.storeName= :storeName");
		sql.append(" and store.id != :storeId");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("storeName", name);
		map.put("storeId", storeId);

		// 调用Dao方法执行SQL语句,返回集合的个数用于判断店铺名是否重复,如果个数不为0则说明店铺名有重复,不能使用
		List<Store> list = storeDao.list(sql.toString(), map);
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}

	public List<Store> listStoreMap(String province, String city, String address, String coordinateX, String coordinateY) {
		StringBuffer sql = new StringBuffer("select s from Store as s where 1=1");
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(province)) {
			sql.append("and s.province = :province");
			map.put("province", province);
		}
		if (!StringUtil.isEmpty(city)) {
			sql.append("and s.city = :city");
			map.put("city", city);
		}
		if (!StringUtil.isEmpty(address)) {
			sql.append("and s.address = :address");
			map.put("address", address);
		}
		if (!StringUtil.isEmpty(coordinateX)) {
			sql.append("and s.coordinateX = :coordinateX");
			map.put("coordinateX", coordinateX);
		}
		if (!StringUtil.isEmpty(coordinateY)) {
			sql.append("and s.coordinateY = :coordinateY");
			map.put("coordinateY", coordinateY);
		}
		try {
			List<Store> list = storeDao.list(sql.toString(), map);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean addStoreMap(Store store) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 设置店铺描述
	 */
	public boolean updateNotes(Store store) {
		// 创建Store对象获取传过来的store参数
		try {
			storeDao.updateBean(store);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 通过storeName得到store对象
	 */
	public Store getStoreByStoreName(String storeName) {
		StringBuffer sql = new StringBuffer("select s from Store as s where 1=1");
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(storeName)) {
			sql.append("and s.storeName = :storeName");
			map.put("storeName", storeName);
		}

		try {
			List<Store> list = storeDao.list(sql.toString(), map);

			return list.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获得商品的所属模板的详细字段名和详细字段值
	 */
	@Override
	public List<ProListTemplateVO> findProductTemplateFieldsByStoreId(String storeId) {
		try {
			if (StringUtil.isEmpty(storeId)) {
				return null;
			}
			StringBuffer hql = new StringBuffer("select distinct(tf.fieldName),tf.fieldValue from TemplateFields as tf,Product as p,Store as s where 1=1");
			// 添加关联条件
			hql.append(" and p.templateId = tf.tempId");
			Map<String, Object> params = new HashMap<String, Object>();
			hql.append(" and p.storeId  = :storeId");
			params.put("storeId", storeId);

			List list = storeDao.list(hql.toString(), params);
			if (list != null && list.size() > 0) {
				List<ProListTemplateVO> templateVOs = new ArrayList<ProListTemplateVO>();
				for (int i = 0; i < list.size(); i++) {
					Object[] objects = (Object[]) list.get(i);
					ProListTemplateVO vo = new ProListTemplateVO();
					vo.setFieldName((String) objects[0]);

					String fieldValue = (String) objects[1];
					// 将模板详细字段值拆分成一个字符串数组 并存入集合
					String[] fieldValueArray = fieldValue.split(";");
					List<String> fieldValueList = new ArrayList<String>();
					for (int h = 0; h < fieldValueArray.length; h++) {
						fieldValueList.add(fieldValueArray[h]);
					}
					vo.setFieldValueList(fieldValueList);
					templateVOs.add(vo);
				}
				return templateVOs;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 那条件查询出满足条件的商品
	 */
	@Override
	public Object[] getProductByConditionsByStore(String storeId, String mainCategory, String brand, String templateOne, String templateTwo, String sortNameAndType, Pager pager) {
		try {

			StringBuffer hql = new StringBuffer("select p.id,p.name,p.centerPicture,p.unitPrice,p.listPrice,p.buyCount,s.city");
			hql.append(" from Product as p,ProductInfo as pi,Store as s where 1=1");

			Map<String, Object> params = new HashMap<String, Object>();
			// 添加关联条件
			hql.append(" and pi.productId = p.id");
			hql.append(" and p.storeId = s.id");

			// 先决条件
			if (!StringUtil.isEmpty(storeId)) {
				hql.append(" and p.storeId = :storeId");
				params.put("storeId", storeId);
			}
			// 搜索普通条件
			if (!StringUtil.isEmpty(mainCategory)) {
				hql.append(" and p.mainCategory = :mainCategory");
				params.put("mainCategory", mainCategory);
			}
			if (!StringUtil.isEmpty(brand)) {
				hql.append(" and p.brand = :brand");
				params.put("brand", brand);
			}
			// 搜索模板条件
			if (!StringUtil.isEmpty(templateOne)) {
				String[] tempOne = templateOne.split("/");
				String infoNameOne = tempOne[0];
				String infoValueOne = tempOne[1];
				hql.append(" and (pi.infoName=:infoNameOne and pi.infoValue=:infoValueOne)");
				params.put("infoNameOne", infoNameOne);
				params.put("infoValueOne", infoValueOne);
			}
			if (!StringUtil.isEmpty(templateTwo)) {
				String[] tempTwo = templateTwo.split("/");
				String infoNameTwo = tempTwo[0];
				String infoValueTwo = tempTwo[1];
				hql.append(" and (pi.infoName=:infoNameTwo and pi.infoValue=:infoValueTwo)");
				params.put("infoNameTwo", infoNameTwo);
				params.put("infoValueTwo", infoValueTwo);
			}

			// 添加排序条件
			if (!StringUtil.isEmpty(sortNameAndType)) {
				String[] sortArray = sortNameAndType.split("/");
				String sortName = sortArray[0];
				String sortType = sortArray[1];
				hql.append(StoreService.addSortCondition(sortName, sortType));
			}

			List list = storeDao.list(pager, hql.toString(), params);
			if (list != null && list.size() > 0) {
				// 返回的Object数组
				Object[] objArray = new Object[2];

				List<Product> products = new ArrayList<Product>();
				for (int i = 0; i < list.size(); i++) {
					Product p = new Product();
					Object[] objects = (Object[]) list.get(i);
					p.setId((String) objects[0]);
					p.setName((String) objects[1]);
					p.setCenterPicture((String) objects[2]);
					p.setUnitPrice((BigDecimal) objects[3]);
					p.setListPrice((BigDecimal) objects[4]);
					p.setBuyCount((Integer) objects[5]);

					p.setArea((String) objects[6]);

					products.add(p);
				}
				// 为Object数组赋值
				objArray[0] = products;
				objArray[1] = list.size();
				return objArray;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * @Title: addSortCondition
	 * @Description: TODO(添加排序方法) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param sortName
	 * @param @param sortType
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	private static String addSortCondition(String sortName, String sortType) {
		if (!StringUtil.isEmpty(sortName) && !StringUtil.isEmpty(sortType)) {
			// 按时间
			if ("newest".equals(sortName)) {
				if ("desc".equals(sortType)) {
					return " order by p.checkTime DESC";
				} else if ("asc".equals(sortType)) {
					return " order by p.checkTime ASC";
				}
				// 按人气
			} else if ("popularity".equals(sortName)) {
				if ("desc".equals(sortType)) {
					return " order by p.visitCount DESC";
				} else if ("asc".equals(sortType)) {
					return " order by p.visitCount ASC";
				}
				// 按销量
			} else if ("salesVolume".equals(sortName)) {
				if ("desc".equals(sortType)) {
					return " order by p.buyCount DESC";
				} else if ("asc".equals(sortType)) {
					return " order by p.buyCount ASC";
				}
				// 按价格
			} else if ("price".equals(sortName)) {
				if ("desc".equals(sortType)) {
					return " order by p.unitPrice DESC";
				} else if ("asc".equals(sortType)) {
					return " order by p.unitPrice ASC";
				}
			}
		}

		return "";
	}

	/**
	 * 查询出符合条件商品的所属分类
	 */
	@Override
	public List<ProductCategory> findThisProductCatagoryByStore(String storeId) {
		try {
			if (StringUtil.isEmpty(storeId)) {
				return null;
			}
			StringBuffer hql = new StringBuffer("select distinct(pc.id),pc.name,pc.fullPath" + " from Product as p,ProductCategory as pc" + " where 1=1");
			Map<String, Object> params = new HashMap<String, Object>();
			// 添加关联条件
			hql.append(" and p.mainCategory = pc.id");

			// 添加查询条件
			if (!StringUtil.isEmpty(storeId)) {
				hql.append(" and p.storeId = :storeId");
				params.put("storeId", storeId);
			}

			List list = storeDao.list(hql.toString(), params);
			if (list != null && list.size() > 0) {
				List<ProductCategory> pcs = new ArrayList<ProductCategory>();
				for (int i = 0; i < list.size(); i++) {
					ProductCategory pc = new ProductCategory();
					Object[] objects = (Object[]) list.get(i);
					pc.setId((String) objects[0]);
					pc.setName((String) objects[1]);
					pc.setFullPath((String) objects[2]);
					pcs.add(pc);
				}
				return pcs;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 查询出符合条件商品的所属分类
	 */
	@Override
	public List<ProductCategory> findThisProductCatagoryByFullPath(String fullPath) {
		try {
			if (StringUtil.isEmpty(fullPath)) {
				return null;
			}
			StringBuffer hql = new StringBuffer("select distinct(pc.id),pc.name,pc.fullPath" + " from Product as p,ProductCategory as pc" + " where 1=1");
			Map<String, Object> params = new HashMap<String, Object>();
			// 添加关联条件
			hql.append(" and p.mainCategory = pc.id");
			if (!StringUtil.isEmpty(fullPath)) {
				hql.append(" and pc.fullPath like :fullPath");
				params.put("fullPath", "%" + fullPath + "%");
			}
			List<Object[]> list = storeDao.list(hql.toString(), params);
			List<ProductCategory> pcs = new ArrayList<ProductCategory>();
			for (Object[] objects : list) {
				ProductCategory pc = new ProductCategory();
				pc.setId((String) objects[0]);
				pc.setName((String) objects[1]);
				pc.setFullPath((String) objects[2]);
				pcs.add(pc);
			}
			return pcs;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 查询出匹配商品所属品牌
	 */
	@Override
	public List<ProductBrand> findThisProductBrandByStore(String storeId) {
		try {
			if (StringUtil.isEmpty(storeId)) {
				return null;
			}
			StringBuffer hql = new StringBuffer("select distinct(pb.id),pb.name" + " from Product as p,ProductBrand as pb" + " where 1=1");
			Map<String, Object> params = new HashMap<String, Object>();
			// 添加关联条件
			hql.append(" and p.brand = pb.id");

			// 添加查询条件
			if (!StringUtil.isEmpty(storeId)) {
				hql.append(" and p.storeId = :storeId");
				params.put("storeId", storeId);
			}
			List list = storeDao.list(hql.toString(), params);
			if (list != null && list.size() > 0) {
				List<ProductBrand> pbs = new ArrayList<ProductBrand>();
				for (int i = 0; i < list.size(); i++) {
					ProductBrand pb = new ProductBrand();
					Object[] objects = (Object[]) list.get(i);
					pb.setId((String) objects[0]);
					pb.setName((String) objects[1]);
					pbs.add(pb);
				}
				return pbs;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 搜索商品时匹配出所有商品
	 */
	@Override
	public Object[] getProductsById(String storeId, Pager pager) {
		try {
			if (StringUtil.isEmpty(storeId)) {
				return null;
			}
			StringBuffer hql = new StringBuffer("select p.id,p.name,p.centerPicture,p.unitPrice,p.listPrice,p.buyCount,s.city from Product as p,Store as s where 1=1");
			hql.append(" and p.storeId = s.id");
			Map<String, Object> params = new HashMap<String, Object>();
			hql.append(" and p.storeId = :storeId");
			params.put("storeId", storeId);
			List list = storeDao.list(pager, hql.toString(), params);
			if (list != null && list.size() > 0) {
				// 返回的Object数组
				Object[] objArray = new Object[2];

				List<Product> products = new ArrayList<Product>();
				for (int i = 0; i < list.size(); i++) {
					Product p = new Product();
					Object[] objects = (Object[]) list.get(i);
					p.setId((String) objects[0]);
					p.setName((String) objects[1]);
					p.setCenterPicture((String) objects[2]);
					p.setUnitPrice((BigDecimal) objects[3]);
					p.setListPrice((BigDecimal) objects[4]);
					p.setBuyCount((Integer) objects[5]);

					p.setArea((String) objects[6]);

					products.add(p);
				}
				// 为Object数组赋值
				objArray[0] = products;
				objArray[1] = list.size();
				return objArray;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 验证邮箱格式时候正确
	 * 
	 * @param mail
	 * @return true：正确，false：错误
	 */
	private Boolean checkMail(String mail) {
		Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		Matcher matcher = pattern.matcher(mail);
		return matcher.matches();
	}

	@Override
	public Boolean saveStoreInfo(User user, Store store, HttpServletRequest request) {
		// 获取 Hibernate Session
		Session session = null;
		// 获取 Hibernate Transaction
		Transaction tran = null;

		try {
			session = this.storeDao.createSession();
			tran = session.beginTransaction();

			user.setId(UUIDUtil.getMarkedUUID32(User.SINGLE_MARK));
			user.setNickName(store.getStoreName());
			int passwordStrength = PasswordStrengthUtil.getInstance().strength(user.getPassword());
			if (passwordStrength >= 4) {
				user.setPasswordStrong("3");
			} else {
				user.setPasswordStrong(String.valueOf(passwordStrength));
			}
			user.setStatus(1);
			user.setVerificationStatus(true);
			user.setHeadPortrait(store.getStoreLogo());
			user.setMobile(checkMail(user.getName()) ? "" : user.getName());
			user.setEmail(checkMail(user.getName()) ? user.getName() : "");
			user.setCreateTime(DateUtil.getSysDateTimeString());
			user.setPassword(new MD5Util().getMD5ofStr(user.getPassword()));

			this.userDao.addBean(user, session);

			store.setRoleId(user.getType());
			store.setId(user.getId());
			store.setCommonStatus("1");
			store.setBankStatus("1");
			store.setCreateTime(DateUtil.getSysDateTimeString());

			// 同步需要，对保存无影响
			store.setUserEntity(user);
			this.storeDao.addBean(store, session);
			tran.commit();
			session.flush();
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
			if (null != tran) {
				tran.rollback();
			}

			return false;
		} finally {
			if (null != session && session.isOpen()) {
				session.close();
			}
		}
	}

	@Override
	public List<Store> getStoreList(Pager pager, Integer status, String storeName, String legalMobile, String mail, String name, String type, Integer mark) {
		// params_data
		Map<String, Object> params = new HashMap<String, Object>();
		// hql
		StringBuilder hql = new StringBuilder(
				"select s.id,s.storeName,u.name,u.mobile,u.email,u.status,u.verificationStatus,s.commonStatus,s.bankStatus,s.buyerStatus,s.salerStatus,s.transStatus,s.createTime,s.updateTime from User u,Store s");
		hql.append(" where s.id=u.id");
		if (!StringUtil.isEmpty(type)) {
			hql.append(" and u.type=:type");
			params.put("type", type);
		}
		// if (mark == 2) {
		// hql.append(" and s.checkThreeStatus<>:checkThreeStatus");
		// params.put("checkThreeStatus", 3);
		// }
		// 0：禁用，1：启用，2：删除
		if (status == -1) {
			hql.append(" and u.status<>:status");
			params.put("status", 2);
		} else {
			hql.append(" and u.status=:status");
			params.put("status", status);
		}
		if (!StringUtil.isEmpty(storeName)) {
			hql.append(" and s.storeName like :storeName");
			params.put("storeName", "%" + storeName + "%");
		}
		/*
		 * if (!StringUtil.isEmpty(legalMobile)) {
		 * hql.append(" and si.legalMobile like :legalMobile");
		 * params.put("legalMobile", "%" + legalMobile + "%"); }
		 */
		if (!StringUtil.isEmpty(mail)) {
			hql.append(" and u.email like :mail");
			params.put("mail", "%" + mail + "%");
		}
		if (!StringUtil.isEmpty(name)) {
			hql.append(" and u.name like :name");
			params.put("name", "%" + name + "%");
		}
		hql.append(" order by s.createTime desc");

		try {
			List<Object[]> list = this.storeDao.list(pager,hql.toString(), params);
			if (list == null || list.size() <= 0) {
				return null;
			}

			List<Store> slist = new ArrayList<Store>();
			for (Object[] obj : list) {
				Store st = new Store();
				User u = new User();

				st.setId(obj[0] == null ? "" : obj[0].toString());
				st.setStoreName(obj[1] == null ? "" : obj[1].toString());
				u.setName(obj[2] == null ? "" : obj[2].toString());
				u.setMobile(obj[3] == null ? "" : obj[3].toString());
				u.setEmail(obj[4] == null ? "" : obj[4].toString());
				u.setStatus(Integer.valueOf(obj[5].toString()));
				u.setVerificationStatus(Boolean.valueOf(obj[6].toString()));
				st.setCommonStatus(obj[7].toString());
				st.setBankStatus(obj[8].toString());
				st.setBuyerStatus(obj[9].toString());
				st.setSalerStatus(obj[10].toString());
				st.setTransStatus(obj[11].toString());
				st.setCreateTime(obj[12].toString());
				st.setUpdateTime(obj[13] == null ? "" : obj[13].toString());
				st.setUserEntity(u);
				slist.add(st);
			}
			return slist;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Boolean deleteById(String ids) {
		if (StringUtil.isEmpty(ids)) {
			return false;
		}

		// 获取 HibernateSession
		Session session = null;
		// 获取 HibernateTransaction
		Transaction tran = null;
		try {
			session = this.storeDao.createSession();
			tran = session.beginTransaction();

			StringBuilder hql = new StringBuilder("update User set status=2 where id");

			String[] idA = ids.split(",");
			if (idA.length == 1) {
				hql.append("='" + ids + "'");
			} else {
				hql.append(" in (");
				for (int i = 0; i < idA.length; i++) {
					if (i == 0) {
						hql.append("'" + idA[i] + "'");
					} else {
						hql.append(",'" + idA[i] + "'");
					}
				}
				hql.append(")");
			}

			session.createQuery(hql.toString()).executeUpdate();

			tran.commit();
			session.flush();

			// delete 上面为逻辑删除，但也要同步
			new MonitorSyncMain(MonitorDataConvert.initDelCommon(ids), "002").start();
			
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
			if (tran != null) {
				tran.rollback();
			}

			return false;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	@Override
	public Store getStoreById(String id) {
		// params_data
		Map<String, Object> params = new HashMap<String, Object>();
		// hql
		String hql1 = "select u,s from User u,Store s where s.id=u.id and s.id=:id";

		params.put("id", id);
		try {
			List<Object[]> list = this.storeDao.list(hql1, params);
			if (list == null || list.size() <= 0) {
				return null;
			}
			Object[] obj = list.get(0);
			Store store = (Store) obj[1];
			store.setUserEntity((User) obj[0]);

			return store;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Boolean updateStoreInfo(User user, Store store) {
		// 获取 HibernateSession
		Session session = null;
		// 获取 HibernateTransaction
		Transaction tran = null;

		try {
			session = this.storeDao.createSession();
			tran = session.beginTransaction();
			User nuser = (User) userDao.get(user.getId());
			nuser.setNickName(store.getStoreName());
			nuser.setName(user.getName());
			nuser.setUpdateTime(DateUtil.getSysDateTimeString());
			if (!nuser.getPassword().equals(user.getPassword())) {
				nuser.setPassword(new MD5Util().getMD5ofStr(user.getPassword()));
			}
			nuser.setStatus(user.getStatus());
			userDao.updateBean(nuser, session);
			store.setRoleId(nuser.getType());
			store.setUpdateTime(DateUtil.getSysDateTimeString());
			storeDao.updateBean(store, session);
			tran.commit();
			session.flush();
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
			if (null != tran) {
				tran.rollback();
			}
			return false;
		} finally {
			if (null != session && session.isOpen()) {
				session.close();
			}
		}
	}

	@Override
	public Boolean authStoreInfo(String id, String status, String mark, String checknote, String checkMan) {
		if (StringUtil.isEmpty(id)) {
			return false;
		}

		try {
			Store store = (Store) this.storeDao.get(id);
			if ("1".equals(mark)) {
				store.setCommonStatus(status);
			} else if ("2".equals(mark)) {
				store.setBankStatus(status);
			} else if ("3".equals(mark)) {
				// store.setCheckThreeStatus(Integer.valueOf(status));
			}
			this.storeDao.updateBean(store);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Boolean checkStoreName(String storeName) {
		// param data
		Map<String, Object> params = new HashMap<String, Object>();
		// hql
		String hql = "from Store where storeName=:storeName";
		// setting data
		params.put("storeName", storeName.trim());
		String hql2 = "select user from User as user where user.nickName='" + storeName.trim() + "'";
		try {
			List<User> list = this.userDao.list(hql, params);
			List<User> list1 = this.userDao.list(hql2, null);
			if (list.size() > 0) {
				return true;
			}
			if (list1.size() > 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			log.error(e.getMessage());
			return true;
		}
	}

	@Override
	public List<Store> getAutoCompany() {
		// 企业用户注册数量
		int companyNumber = Integer.valueOf(Cache.getResource("company.reg"));

		// param data
		Map<String, Object> params = new HashMap<String, Object>();
		// hql
		String hql = "select s from Store s,User u where s.id=u.id and s.commonStatus=:commonStatus and u.status=:status";
		// setting data
		params.put("commonStatus", 3);
		params.put("status", 1);
		// pagination
		Pager pager = new Pager();
		pager.setRowsPerPage(12);
		pager.setCurrentPage(0);

		try {
			List<Store> list = this.storeDao.list(pager, hql, params);
			if (list != null && list.size() > 0) {
				String hql1 = "select count(*) from Store s,User u where s.id=u.id and s.commonStatus=:commonStatus and u.status=:status";
				List<Object> num = this.storeDao.list(hql1, params);

				list.get(0).setAutoNumber(Integer.valueOf(num.get(0).toString()) + companyNumber);
			} else {
				list.get(0).setAutoNumber(companyNumber);
			}

			return list;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();

			return null;
		}

	}

	@Override
	public Boolean updateStoreOrStoreInfoOrLgOrCa(Store store, StoreInfo storeInfo, List<StoreLegalResume> lrlist, List<StoreFinancialData> fdlist, int type) {
		Session session = null;
		Transaction tran = null;

		try {
			session = this.storeDao.createSession();
			tran = session.beginTransaction();

			if (type == 1) {
				if (StringUtil.isEmpty(store.getStoreLogo())) {
					return false;
				}
				if (StringUtil.isEmpty(store.getStoreName())) {
					return false;
				}
			} else if (type == 2) {
				if (StringUtil.isEmpty(storeInfo.getLegalName())) {
					return false;
				}
				if (StringUtil.isEmpty(storeInfo.getLegalIdNumber())) {
					return false;
				}
			}

			if (store != null) {
				Store s = (Store) this.storeDao.get(store.getId());
				if (type == 1) {
					s.setStoreLogo(store.getStoreLogo());
					s.setBusinessLicense(store.getBusinessLicense());
					s.setNotes(store.getNotes());
					s.setStoreName(store.getStoreName());
					s.setOrgId(store.getOrgId());
					s.setCommonStatus("1");
				} else if (type == 2) {
					s.setBankStatus("1");
				} else if (type == 3) {
					// s.setCheckThreeStatus(1);
				}

				this.storeDao.updateBean(s, session);
			}
			if (storeInfo != null && type == 1) {
				StoreInfo si = this.storeDao.get(StoreInfo.class, storeInfo.getId());
				si.setRegTime(storeInfo.getRegTime());
				si.setMailingAddress(storeInfo.getMailingAddress());
				si.setUnitType(storeInfo.getUnitType());
				si.setZipCode(storeInfo.getZipCode());
				si.setAreaNumber(storeInfo.getAreaNumber());
				si.setContactName(storeInfo.getContactName());
				si.setContactPhone(storeInfo.getContactPhone());
				si.setContactFax(storeInfo.getContactFax());
				si.setRegCapital(storeInfo.getRegCapital());
				si.setNumberEmployees(storeInfo.getNumberEmployees());
				si.setBureau(storeInfo.getBureau());
				si.setBureauTime(storeInfo.getBureauTime());
				si.setBusinessLicenseNumber(storeInfo.getBusinessLicenseNumber());
				si.setBank(storeInfo.getBank());
				si.setOpeningTime(storeInfo.getOpeningTime());
				si.setReditCard(storeInfo.getReditCard());
				si.setBusinessScope(storeInfo.getBusinessScope());
				si.setPlanning(storeInfo.getPlanning());

				this.storeDao.updateBean(si, session);
			} else if (storeInfo != null && type == 2) {
				StoreInfo si = this.storeDao.get(StoreInfo.class, storeInfo.getId());
				si.setLegalName(storeInfo.getLegalName());
				si.setLegalIdNumber(storeInfo.getLegalIdNumber());
				si.setLegalBirth(storeInfo.getLegalBirth());
				si.setLegalSex(storeInfo.getLegalSex());
				si.setLegalNation(storeInfo.getLegalNation());
				si.setLegalEducation(storeInfo.getLegalEducation());
				si.setLegalBplace(storeInfo.getLegalBplace());
				si.setLegalPan(storeInfo.getLegalPan());
				si.setJobTitle(storeInfo.getJobTitle());
				si.setHisCurrentTime(storeInfo.getHisCurrentTime());

				this.storeDao.updateBean(si, session);
			}
			if (lrlist != null && lrlist.size() > 0) {
				Map<String, Object> params = new HashMap<String, Object>();
				String lrhql = "delete from LegalResume where companyId=:companyId";
				params.put("companyId", storeInfo.getId());

				this.storeDao.delete(lrhql, params, session);

				for (StoreLegalResume lr : lrlist) {
					lr.setStoreId(storeInfo.getId());

					this.storeDao.addBean(lr, session);
				}
			}
			if (fdlist != null && fdlist.size() > 0) {
				for (StoreFinancialData fd : fdlist) {
					if (StringUtil.isEmpty(fd.getId())) {
						this.storeDao.addBean(fd, session);
					} else {
						this.storeDao.updateBean(fd, session);
					}
				}
			}

			tran.commit();
			session.flush();

			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			if (tran != null) {
				tran.rollback();
			}

			return false;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	/**
	 * 根据企业名称查询企业列表
	 */
	@Override
	public List<Store> getListStoreByStoreName(Pager pager, String storeName, String type, String htType) {
		List<Store> storeList = new ArrayList<Store>();
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer("select s.id,s.storeName,s.contactName,s.contactPhone from Store s,User u where s.id=u.id ");
		hql.append(" and u.status=1 and u.verificationStatus=" + true);
		hql.append(" and s.commonStatus = :commonStatus");
		map.put("commonStatus", StoreConst.RZ_TYPE_SUC);
		hql.append(" and s.bankStatus = :bankStatus");
		map.put("bankStatus", StoreConst.RZ_TYPE_SUC);
		if ("0".equals(htType)) {
			// 销售合同 ---- 甲方：有产生资质
			if ("1".equals(type)) {
				hql.append(" and s.salerStatus = :salerStatus");
				map.put("salerStatus", StoreConst.RZ_TYPE_SUC);
			} else {
				// 销售合同 --- 乙方：有处置资质
				hql.append(" and s.buyerStatus = :buyerStatus");
				map.put("buyerStatus", StoreConst.RZ_TYPE_SUC);
			}
		} else {
			// 运输合同 ---- 甲方：有产生或处置资质
			if ("1".equals(type)) {
				hql.append(" and s.salerStatus = :salerStatus");
				map.put("salerStatus", StoreConst.RZ_TYPE_SUC);
			} else {
				// 运输合同 --- 乙方：有运输资质
				hql.append(" and s.transStatus = :transStatus");
				map.put("transStatus", StoreConst.RZ_TYPE_SUC);
			}
		}

		if (!StringUtil.isEmpty(storeName)) {
			hql.append(" and s.storeName like :Name");
			map.put("Name", "%" + storeName + "%");
		}
		try {
			List<Object[]> list = storeDao.list(pager, hql.toString(), map);
			if (list != null && list.size() > 0) {
				for (Object[] obj : list) {
					Store store = new Store();
					store.setId(obj[0].toString());
					store.setStoreName((String) obj[1]);
					store.setContactName((String) obj[2]);
					store.setContactPhone((String) obj[3]);
					// store.setUserType(obj[4] == null ? 0 : (Integer)obj[4]);
					storeList.add(store);
				}
			}
			return storeList;
		} catch (Exception e) {
			log.error("查询企业失败!");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Store> getCheckStore(Pager pager, String name, int mark) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer("select s from Store s,User u where s.id=u.id and u.status=:status and u.verificationStatus=:verificationStatus");
		map.put("status", 1);
		map.put("verificationStatus", true);

		switch (mark) {
		case 1:
			hql.append(" and s.commonStatus = :commonStatus");
			map.put("commonStatus", "1");
			break;
		case 2:
			hql.append(" and s.bankStatus = :bankStatus");
			map.put("bankStatus", "1");
			break;
		}
		if (!StringUtil.isEmpty(name)) {
			hql.append(" and s.storeName like :name");
			map.put("name", name);
		}
		hql.append(" order by s.createTime desc");

		try {
			return storeDao.list(pager, hql.toString(), map);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public boolean checkStoreInfo(String id, String mark, String status) {
		if (StringUtil.isEmpty(id) || StringUtil.isEmpty(mark) || StringUtil.isEmpty(status)) {
			return false;
		}

		try {
			Store store = getStore(id);
			if (store == null) {
				return false;
			}

			if ("1".equals(mark)) {
				store.setCommonStatus(status);
			} else if ("2".equals(mark)) {
				store.setBankStatus(status);
			}
			storeDao.updateBean(store);

			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 获取运输企业的列表,及运输企业的列表数据
	 */
	@Override
	public List<Store> getStoreByType(Pager pager, String areaId, String pageType) {
		List<Store> list = new ArrayList<Store>();
		StringBuffer buffer = new StringBuffer(
				"select s.id,s.storeName,s.storeLogo,s.notes,s.commonStatus from Store s,User u where s.id=u.id and u.type='4' and u.status='1' and u.verificationStatus='1' and s.commonStatus='3'");
		if (!StringUtil.isEmpty(areaId)) {
			buffer.append(" and s.areaId='" + areaId + "'");
		}
		if (!StringUtil.isEmpty(pageType) && "channel".equals(pageType)) {
			buffer.append(" and s.storeName not like '测试%'");
		}
		try {
			List<Object[]> li = storeDao.list(pager, buffer.toString(), null);
			if (li != null && li.size() > 0) {
				for (Object[] obj : li) {
					Store s = new Store();
					s.setId((String) obj[0]);
					s.setStoreName((String) obj[1]);
					s.setStoreLogo((String) obj[2]);
					s.setNotes((String) obj[3]);
					s.setCommonStatus(obj[4].toString());
					list.add(s);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 获取核心企业基础信息
	 */
	@Override
	public List<Store> getStoreByHx(String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buffer = new StringBuffer("select s.id,s.storeName,s.storeLogo,s.notes,s.commonStatus from Store s,User u where s.id=u.id");
		if (!StringUtil.isEmpty(type)) {
			buffer.append(" and u.type = :type");
			map.put("type", type);
		}
		buffer.append(" and u.status = 1");
		buffer.append(" and s.commonStatus = '3'");
		buffer.append(" order by s.updateTime desc");
		try {
			List<Object[]> li = storeDao.list(buffer.toString(), map);
			if (li != null && li.size() > 0) {
				List<Store> returnList = new ArrayList<Store>();
				for (Object[] obj : li) {
					Store s = new Store();
					s.setId((String) obj[0]);
					s.setStoreName((String) obj[1]);
					s.setStoreLogo((String) obj[2]);
					s.setNotes((String) obj[3]);
					s.setCommonStatus(obj[4].toString());
					returnList.add(s);
				}
				return returnList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Store> getWebList(Pager pager, String storeTpye) {
		StringBuffer sql = new StringBuffer("select s from Store as s,User as u ");
		sql.append(" where s.id = u.id ");
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(storeTpye)) {
			if ("1".equals(storeTpye)) {
				sql.append(" and u.type = :type");
				map.put("type", "4");
			} else {
				sql.append(" and u.type != :type");
				map.put("type", "4");
			}

		}
		sql.append(" and s.commonStatus = '3'");
		try {
			List<Store> list = storeDao.list(pager, sql.toString(), map);

			return list;
		} catch (Exception e) {
			log.error("查询企业失败!");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Store> getBasisStore(Pager pager) {
		String hql = "select s from Store s,User u where u.id=s.id and u.type<>'4' and u.status=1 and u.verificationStatus=true and s.commonStatus= '3' order by s.createTime desc";

		try {
			return storeDao.list(pager, hql, null);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 查询集团企业
	 */
	@Override
	public List<Store> getStoreByGroup(Pager pager, String type) {
		String hql = "select s.id,s.storeName from Store s,User u  where u.id=s.id and s.storeGroup=true and u.status='1' and u.verificationStatus=true and u.type='" + type + "'";

		try {
			List<Object[]> list = storeDao.list(pager, hql, null);
			if (list != null && list.size() > 0) {
				List<Store> storeList = new ArrayList<Store>();
				for (Object[] obj : list) {
					Store s = new Store();
					s.setId((String) obj[0]);
					s.setStoreName((String) obj[1]);
					storeList.add(s);
				}
				return storeList;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public boolean jumdmentContractByStore(String mark, String jStoreName, String yStoreName) {
		String hql = "select u.type from Store s,User u where s.id=u.id and s.storeName in ('" + jStoreName + "', '" + yStoreName + "')";

		try {
			Collection<Object> list = this.storeDao.list(hql, null);
			if (list != null && list.size() > 0) {
				boolean markss = true;
				if ("危废处置合同".equals(mark) && list.contains("4")) {
					markss = false;
				}
				if ("危废运输合同".equals(mark) && !list.contains("4")) {
					markss = false;
				}

				return markss;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<Store> getStoreByType(String type) {
		String hql = "select s from Store s,User u where s.id=u.id and u.status=1 and u.verificationStatus=true and u.type='" + type + "'";

		try {
			return storeDao.list(hql, null);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 查询待认证的会员信息
	 * 
	 * @param rzType
	 * @param storeName
	 * @param email
	 * @param page
	 * @return
	 */
	public List<Store> getStoreUnCheckList(String rzType, String storeName, String email, Pager page) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("select store from Store as store");
		sql.append(",User as user ");
		sql.append(" where 1=1");
		sql.append(" and store.id = user.id");
		sql.append(" and user.status!=2");
		if ("cz".equals(rzType)) {
			sql.append(" and store.buyerStatus = :rzType");
		} else if ("cs".equals(rzType)) {
			sql.append(" and store.salerStatus = :rzType");
		} else {
			sql.append(" and store.transStatus = :rzType");
		}
		paramMap.put("rzType", StoreConst.RZ_TYPE_CHECK);

		if (!StringUtil.isEmpty(storeName)) {
			sql.append(" and store.storeName like :storeName");
			paramMap.put("storeName", "%" + storeName + "%");
		}
		if (!StringUtil.isEmpty(email)) {
			sql.append(" and user.name like :email");
			paramMap.put("email", email);
		}

		sql.append(" order by store.updateTime desc");

		try {
			List<Store> storeList = null;

			if (page != null) {
				storeList = storeDao.list(page, sql.toString(), paramMap);
			} else {
				storeList = storeDao.list(sql.toString(), paramMap);
			}

			return storeList;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

	@Override
	public Store getStoreByAccountNo(String accountNo) {
		if (StringUtil.isEmpty(accountNo)) {
			return null;
		}

		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer(" from Store as store where 1=1");
		sql.append(" and store.accountNo = :accountNo");
		paramMap.put("accountNo", accountNo);
		try {
			List<Store> stores = storeDao.list(sql.toString(), paramMap);
			if (stores != null && stores.size() > 0) {
				return stores.get(0);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

	@Override
	public boolean update(Store store) {
		try {
			storeDao.updateBean(store);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	@Override
	public List<Store> getAllNormalStoreList() {
		String sql = "SELECT DISTINCT s, u FROM Store s, User u WHERE 1 = 1";
		sql += " and s.id = u.id";
		sql += " and u.status = '1'";
		List<Store> stores = new ArrayList<Store>();
		try {
			// 调用DAO方法查找商品
			List<Object[]> list = storeDao.list(sql.toString(), null);
			for (Object[] object : list) {
				Store store = (Store) object[0];
				store.setUserEntity((User) object[1]);
				stores.add(store);
			}

			return stores;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stores;
	}

	@Override
	public List<Store> getStoreList(Pager pager, Integer status) {
		// params_data
				Map<String, Object> params = new HashMap<String, Object>();
				// hql
				StringBuilder hql = new StringBuilder(
						"select s from User u,Store s");
				hql.append(" where s.id=u.id");
				hql.append(" and s.isDataSynchro = '0'");
				// 0：禁用，1：启用，2：删除
				if (status == -1) {
					hql.append(" and u.status<>:status");
					params.put("status", 2);
				} else {
					hql.append(" and u.status=:status");
					params.put("status", status);
				}
				hql.append(" order by s.createTime desc");

				try {
					List<Store> list = this.storeDao.list(pager,hql.toString(), params);
					if (list == null || list.size() <= 0) {
						return null;
					}
					return list;
				} catch (Exception e) {
					log.error(e.getMessage());
					e.printStackTrace();
					return null;
				}
	}
}