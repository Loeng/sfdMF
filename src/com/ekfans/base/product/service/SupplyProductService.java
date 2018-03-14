package com.ekfans.base.product.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.order.dao.IInquiryDao;
import com.ekfans.base.product.dao.IProductCategoryDao;
import com.ekfans.base.product.dao.ISupplyProductDao;
import com.ekfans.base.product.model.ProductCategory;
import com.ekfans.base.product.model.SupplyProduct;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.system.service.SystemAreaService;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Service
public class SupplyProductService implements ISupplyProductService {

	@Autowired
	private ISupplyProductDao supplyProductdao;

	@Autowired
	private SystemAreaService systemAreaService;

	@Autowired
	private IInquiryDao inquiryDao;

	@Autowired
	private IProductCategoryDao productCategoryDao;
	
	@Autowired
	private IStoreService storeService;
	
	@Autowired
	private IUserService userService;
	
	@Override
	public boolean saveSupplyProduct(SupplyProduct product) {
		if (product != null) {
			try {
				supplyProductdao.addBean(product);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	/**
	 * 核心企业查询供应商的议价信息
	 * supplyName 供应商商品名称
	 * storeName 核心企业名称
	 * productNum 商品编号
	 * pager 分页
	 */
	public List<SupplyProduct> getList(String supplyName, String storeName, String productNum, Pager pager) {
		// 拼接查询语句,查询所有供应商
		StringBuffer buffer = new StringBuffer();
		// 查询供应商发布商品列表 商品名称、商品数量(库存量)、商品批发价、商品产地、商品分类、联系人、联系电话 type=3为供应商发布的商品
		buffer.append("select p.supplyProductName,p.quantity,p.pfPrice,p.habitat,s.storeName,p.linkTel,p.mainCategory,p.userId,p.id,p.unit from SupplyProduct as p,Store s  where p.userId=s.id ");
		// 定义参数Map
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(supplyName)) {
			buffer.append(" and p.supplyProductName like :supplyProductName");
			paramMap.put("supplyProductName", "%" + supplyName + "%");
		}
		if (!StringUtil.isEmpty(storeName)) {
			buffer.append(" and s.storeName like :storeName");
			paramMap.put("storeName", "%" + storeName + "%");
		}
		if (!StringUtil.isEmpty(productNum)) {
            buffer.append(" and s.storeName like :productNum");
            paramMap.put("productNum", "%" + productNum + "%");
        }
		buffer.append(" order by p.createTime desc");
		try {
		  List<SupplyProduct> supply = new ArrayList<SupplyProduct>();
			List<Object[]> sup = supplyProductdao.list(pager, buffer.toString(), paramMap);
			if (sup != null && sup.size() > 0) {
			    for (Object[] obj:sup) {
                    SupplyProduct s = new SupplyProduct();
                    s.setSupplyProductName((String)obj[0]);
                    s.setQuantity((Integer)obj[1]);
                    s.setPfPrice((BigDecimal)obj[2]);
                    s.setHabitat((String)obj[3]);
                    String area = systemAreaService.getAreaFullNameByAreaIdAndDelimiter((String)obj[3], "");
                    ProductCategory p = (ProductCategory) productCategoryDao.get((String)obj[6]);
                    s.setMainCategory(p==null?"":p.getName());
                    s.setHabitat(area);
                    s.setLinkPerson((String)obj[4]);
                    if(obj[4]==null){
                        //获取用户custmer 表中的用户名
                        User user =  userService.getUser((String)obj[7]);
                        if("".equals(user.getNickName())|| user.getNickName() == null){
                            s.setLinkPerson(user.getName());
                        }else{
                            s.setLinkPerson(user.getNickName());
                        }
                    }
                    s.setLinkTel((String)obj[5]);
                    s.setId((String)obj[8]);
                    s.setUnit((String)obj[9]);
                    supply.add(s);
                }
			}
			return supply;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
     * 供应商查询发布的商品列表
     */
    public List<SupplyProduct> getListByGys(String storeName, String productNum, Pager pager) {
        // 拼接查询语句,查询所有供应商
        StringBuffer buffer = new StringBuffer();
        // 查询供应商发布商品列表 商品名称、商品数量(库存量)、商品批发价、商品产地、商品分类、联系人、联系电话 type=3为供应商发布的商品
        buffer.append("select p.supplyProductName,p.quantity,p.pfPrice,p.habitat,s.storeName,p.linkTel,p.mainCategory,p.userId,p.id,p.unit,p.productNum from SupplyProduct as p,Store s  where p.userId=s.id ");
        // 定义参数Map
        Map<String, Object> paramMap = new HashMap<String, Object>();
        if (!StringUtil.isEmpty(storeName) && storeName != null) {
            buffer.append(" and p.supplyProductName like:supplyProductName");
            paramMap.put("supplyProductName", "%" + storeName + "%");
        }
        if (!StringUtil.isEmpty(productNum) && productNum != null) {
            buffer.append(" and p.productNum like :productNum");
            paramMap.put("productNum", "%"+productNum+"%");
        }
        buffer.append(" order by p.createTime desc");
        try {
          List<SupplyProduct> supply = new ArrayList<SupplyProduct>();
            List<Object[]> sup = supplyProductdao.list(pager, buffer.toString(), paramMap);
            if (sup != null && sup.size() > 0) {
                for (Object[] obj:sup) {
                    SupplyProduct s = new SupplyProduct();
                    s.setSupplyProductName((String)obj[0]);
                    s.setQuantity((Integer)obj[1]);
                    s.setPfPrice((BigDecimal)obj[2]);
                    s.setHabitat((String)obj[3]);
                    String area = systemAreaService.getAreaFullNameByAreaIdAndDelimiter((String)obj[3], "");
                    ProductCategory p = (ProductCategory) productCategoryDao.get((String)obj[6]);
                    s.setMainCategory(p==null?"":p.getName());
                    s.setHabitat(area);
                    s.setLinkPerson((String)obj[4]);
                    if(obj[4]==null){
                        //获取用户custmer 表中的用户名
                        User user =  userService.getUser((String)obj[7]);
                        if("".equals(user.getNickName())|| user.getNickName() == null){
                            s.setLinkPerson(user.getName());
                        }else{
                            s.setLinkPerson(user.getNickName());
                        }
                    }
                    s.setLinkTel((String)obj[5]);
                    s.setId((String)obj[8]);
                    s.setUnit((String)obj[9]);
                    s.setProductNum((String)obj[10]);
                    supply.add(s);
                }
            }
            return supply;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	/**
	 * 根据id查找商品
	 * 
	 * @param id
	 * @return
	 */
	public SupplyProduct getSupplyProductById(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		try {
		    SupplyProduct sp = (SupplyProduct) supplyProductdao.get(id);
		  //根据企业ID查询企业名称
            Store store = storeService.getStore(sp.getUserId());
            sp.setLinkPerson(store.getStoreName());
			// 调用DAO方法查找角色
			return sp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int getCheckProductNum(String num) {
		StringBuffer buffer = new StringBuffer("select sp.id from SupplyProduct as sp where sp.productNum=:productNum");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("productNum", num);

		try {
			List<SupplyProduct> product = supplyProductdao.list(buffer.toString(), paramMap);
			return product.size();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

    @Override
    public String getProductSum(String storeId) {
        StringBuffer hql = new StringBuffer("select sp.id from SupplyProduct as sp where 1 = 1 ");      
        // 定义存储查询条件的Map
        Map<String, Object> params = new HashMap<String, Object>();
        if (!StringUtil.isEmpty(storeId)) {
            hql.append(" and sp.userId =:userId");
            params.put("userId", storeId);
        }
      
        try {
            List list = supplyProductdao.list( hql.toString(), params);
            return list.size()+"";
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public SupplyProduct getSupplyProductById(String id, Session session) {
        if (StringUtil.isEmpty(id)) {
            return null;
        }
        try {
            // 调用DAO方法查找角色
            return (SupplyProduct) supplyProductdao.get(id,session);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

	@Override
	public Boolean update(SupplyProduct sp) {
		if(sp == null){
			return false;
		}
		
		try {
			this.supplyProductdao.updateBean(sp);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
