package com.ekfans.base.product.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.product.dao.IProductCategoryDao;
import com.ekfans.base.product.dao.web.ProductDetail.ProductDetailDao;
import com.ekfans.base.product.model.ProductCategory;
import com.ekfans.base.product.util.ProductCategoryHelper;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 商品分类业务实现Service
 *
 * @author Lgy
 * @version 1.0
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @date 2014-1-6
 */
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class ProductCategoryService implements IProductCategoryService {

    public static Logger log = LoggerFactory.getLogger(ProductCategoryService.class);
    @Autowired
    private IProductCategoryDao productCategoryDao;

    /**
     * 获取菜单
     *
     * @return
     */
    public List<ProductCategory> getCategories() {

        // 定义查询SQL
        StringBuffer sql = new StringBuffer(" select pc from ProductCategory as pc where 1=1");
        // 定义参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();

        // 权限的状态是放开
        sql.append(" and pc.status = '1'");

        // 查询出一级菜单
        sql.append(" and (pc.parentId is null or pc.parentId = '' or pc.parentId = ' ')");

        // 根据位置进行排序
        sql.append(" order by pc.position asc");

        try {
            // 调用DAO执行SQL获取返回LIST
            List<ProductCategory> productCategoryList = productCategoryDao.list(sql.toString(), paramMap);

            // 如果数据库查出的数据为空，则返回空
            if (productCategoryList == null || productCategoryList.size() <= 0) {
                return null;
            }

            return productCategoryList;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @param @param categories 一级分类
     * @return void
     * @throws
     * @Title: setChilds
     * @Description: TODO 递归设置子列表 详细业务流程: 传入一级分类，递归设置子列表
     */
    public void setChilds(List<ProductCategory> categories) {
        if (categories == null || categories.size() <= 0) {
            return;
        }

        // 遍历分类列表
        for (int i = 0; i < categories.size(); i++) {
            // 获取分类对象
            ProductCategory category = (ProductCategory) categories.get(i);

            // 如果获取的对象为空，则跳过
            if (category == null) {
                continue;
            }
            // 设置子列表
            category.setChildList(getChildCategories(category.getId()));

            // 递归调用设置子列表的子列表
            setChilds(category.getChildList());
        }
    }

    /**
     * 根据上级菜单获取下级菜单
     *
     * @param parentId
     * @return
     */
    public List<ProductCategory> getChildCategories(String parentId) {
        // 定义查询SQL
        StringBuffer sql = new StringBuffer(" select pc from ProductCategory as pc where 1=1");
        // 定义参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();

        // 如果传进来的父ID不为空，则加上父ID条件;若为空，则返回null
        if (!StringUtil.isEmpty(parentId)) {
            sql.append(" and pc.parentId = :parentId");
            paramMap.put("parentId", parentId);
        } else {
            return null;
        }

        // 权限的状态是放开
        sql.append(" and pc.status = '1'");

        // 根据位置进行排序
        sql.append(" order by pc.position asc");

        try {
            // 调用DAO执行SQL获取返回LIST
            List productCategoryList = productCategoryDao.list(sql.toString(), paramMap);

            return productCategoryList;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 根据上级菜单获取下级菜单
     *
     * @param parentId
     * @return
     */
    public List<String> getChildCategorieIds(String parentId) {
        // 定义查询SQL
        StringBuffer sql = new StringBuffer(" select pc.id from ProductCategory as pc where 1=1");
        // 定义参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();

        // 如果传进来的父ID不为空，则加上父ID条件;若为空，则返回null
        if (!StringUtil.isEmpty(parentId)) {
            sql.append(" and pc.parentId = :parentId");
            paramMap.put("parentId", parentId);
        } else {
            return null;
        }

        // 权限的状态是放开
        sql.append(" and pc.status = '1'");

        // 根据位置进行排序
        sql.append(" order by pc.position asc");

        try {
            // 调用DAO执行SQL获取返回LIST
            List productCategoryList = productCategoryDao.list(sql.toString(), paramMap);

            return productCategoryList;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 根据上级菜单获取下级菜单
     *
     * @param parentId
     * @return
     */
    public List<ProductCategory> getChildCategories(String parentId, boolean canParentNull, int showNumber) {
        // 定义查询SQL
        StringBuffer sql = new StringBuffer(" select pc from ProductCategory as pc where 1=1");
        // 定义参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();
        // 权限的状态是放开
        sql.append(" and pc.status = '1'");
        sql.append(" and pc.fullPath like :categoryId");
        paramMap.put("categoryId", "%" + parentId + "%");
        // 根据位置进行排序
        sql.append(" order by pc.position asc");
        // 定义查询SQL
        StringBuffer sql2 = new StringBuffer(" select pc from ProductCategory as pc where 1=1");
        // 定义参数MAP
        Map<String, Object> paramMap2 = new HashMap<String, Object>();
        // 如果传进来的父ID不为空，则加上父ID条件;若为空，则返回null
        if (!StringUtil.isEmpty(parentId)) {
            sql2.append(" and pc.parentId = :parentId");
            paramMap2.put("parentId", parentId);
        } else {
            if (canParentNull) {
                sql2.append(" and (pc.parentId = null or pc.parentId = '' or pc.parentId = ' ')");
            } else {
                return null;
            }
        }
        // 权限的状态是放开
        sql2.append(" and pc.status = '1'");
        // 根据位置进行排序
        sql2.append(" order by pc.position asc");
        try {
            // 调用DAO查询SQL
            List<ProductCategory> productCategoryList = productCategoryDao.list(sql.toString(), paramMap);
            // 调用DAO查询SQL
            List<ProductCategory> categoryList = null;
            if (showNumber > 0) {
                Pager page = new Pager(1);
                page.setRowsPerPage(showNumber);
                categoryList = productCategoryDao.list(page, sql2.toString(), paramMap2);
            } else {
                categoryList = productCategoryDao.list(sql2.toString(), paramMap2);
            }
            ProductCategoryHelper.recoverCategoryList(productCategoryList, categoryList);
            return categoryList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 添加商品分类
     */
    public boolean addCategory(ProductCategory category, File uploadFile, String uploadFileContentType) {
        // 如果分类为空，返回false
        if (category == null) {
            return false;
        }
        try {

            // 调用DAO方法添加分类
            productCategoryDao.addBean(category);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除商品分类
     */
    public boolean deleteCategory(String id) {
        // 如果id为空，返回false
        if (StringUtil.isEmpty(id)) {
            return false;
        }
        try {
            // 删除旧图标
            ProductCategory category = (ProductCategory) productCategoryDao.get(id);
            if (!StringUtil.isEmpty(category.getIcon())) {
                File file = new File(category.getIcon());
                if (file.exists()) {
                    file.delete();
                }
            }
            // 调用DAO方法删除分类
            productCategoryDao.deleteById(id);
            // TODO 删除子分类
            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 修改商品分类
     */
    public boolean modifyCategory(ProductCategory category, File uploadFile, String uploadFileContentType) {
        // 如果分类为空，返回false
        if (category == null) {
            return false;
        }
        try {

            // 删除旧图标
            if (!StringUtil.isEmpty(category.getIcon())) {
                File file = new File(category.getIcon());
                if (file.exists()) {
                    file.delete();
                }
            }

            // 调用DAO方法修改分类
            productCategoryDao.updateBean(category);
            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据id查询商品分类---------------------wsj-----------------------
     */
    public ProductCategory getCategoryById(String id) {
        // 如果id为空，返回null
        if (StringUtil.isEmpty(id)) {
            return null;
        }
        try {
            // 调用DAO方法查找分类
            ProductCategory c = (ProductCategory) productCategoryDao.get(id);
            // setParents(c);
            return c;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据条件查询
     */
    public List<ProductCategory> getCategories(String name, String parentId) {
        // 定义查询SQL
        StringBuffer sql = new StringBuffer(" select pc from ProductCategory as pc where 1=1");
        // 定义参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();

        // 权限的状态是放开
        sql.append(" and pc.status = '1'");

        // 查询出一级菜单
        if (!StringUtil.isEmpty(parentId)) {
            sql.append(" and pc.parentId = :parentId");
            paramMap.put("parentId", parentId);
        } else {
            sql.append(" and pc.parentId = 'common'");
        }
        if (!StringUtil.isEmpty(name)) {
            sql.append(" and pc.name like :name");
            paramMap.put("name", "%" + name + "%");
        }
        // 根据位置进行排序
        sql.append(" order by pc.position asc");

        try {
            // 调用DAO执行SQL获取返回LIST
            List<ProductCategory> productCategoryList = productCategoryDao.list(sql.toString(), paramMap);

            // 如果数据库查出的数据为空，则返回空
            if (productCategoryList == null || productCategoryList.size() <= 0) {
                return null;
            }
            // 递归设置子分类
            setChilds(productCategoryList);

            return productCategoryList;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Map<String, String> searchCategoriesByName(String name, String fullPath) {
        // 定义查询SQL
        StringBuffer sql = new StringBuffer(" select pc.id from ProductCategory as pc where 1=1");
        // 定义参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();

        // 权限的状态是放开
        sql.append(" and pc.status = '1'");

        // 在通用分类下查询
        sql.append(" and pc.fullPath like 'COMMON%'");

        if (!StringUtil.isEmpty(name)) {
            sql.append(" and pc.name like :name");
            paramMap.put("name", "%" + name + "%");
        }
        if (!StringUtil.isEmpty(fullPath)) {
            sql.append(" and pc.fullPath like :fullPath");
            paramMap.put("fullPath", "%" + fullPath + "%");
        }
        // 根据位置进行排序
        sql.append(" order by pc.fullPath asc");

        try {
            // 调用DAO执行SQL获取返回LIST
            List<String> productCategoryList = productCategoryDao.list(sql.toString(), paramMap);

            // 如果数据库查出的数据为空，则返回空
            if (productCategoryList == null || productCategoryList.size() <= 0) {
                return null;
            }
            return setAll(productCategoryList);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public Map<String, String> setAll(List<String> productCategoryList) {
        if (productCategoryList == null || productCategoryList.size() <= 0) {
            return null;
        }

        Map<String, String> returnMap = new HashMap<String, String>();

        for (int i = 0; i < productCategoryList.size(); i++) {
            String categoryId = productCategoryList.get(i);
            Map<String, Object> param = new HashMap<String, Object>();
            StringBuffer sql = new StringBuffer("select pc.id from ProductCategory as pc where pc.fullPath like :fullPath");
            param.put("fullPath", "%" + categoryId + "%");
            try {
                List<String> pcList = productCategoryDao.list(sql.toString(), param);
                if (pcList != null && pcList.size() > 0) {
                    for (int j = 0; j < pcList.size(); j++) {
                        String pcId = pcList.get(j);
                        String fullName = getCategoryFullNameByCategoryId(pcId, " > ");
                        returnMap.put(pcId, fullName);
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return returnMap;
    }

    /**
     * 添加子分类
     */
    public boolean addCategory(ProductCategory category, String picturePath) {
        // 如果分类为空，返回false
        if (category == null) {
            return false;
        }
        try {
            // 如果上传了图标，设置属性
            if (!StringUtil.isEmpty(picturePath)) {
                category.setIcon(picturePath);
            }
            // 设置创建时间

            category.setCreateTime(DateUtil.getSysDateTimeString());
            // category.setId(new NoManager().createProductCategoryId());
            ProductCategory parentCategory = (ProductCategory) productCategoryDao.get(category.getParentId());
            // 设置分类全路径(父分类全路径+分类id)
            productCategoryDao.addBean(category);
            category.setFullPath(parentCategory.getFullPath() + "_" + category.getId());
            // 调用DAO方法添加分类
            productCategoryDao.updateBean(category);
            return true;
        } catch (Exception e) {
            // 将报错的信息打印到日志表
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 修改子分类
     */
    public boolean modifyCategory(ProductCategory category, String picturePath) {
        // 如果分类为空，返回false
        if (category == null) {
            return false;
        }
        try {
            // 如果上传了图标，替换旧图标
            if (!StringUtil.isEmpty(picturePath)) {
                category.setIcon(picturePath);
            }
            // 调用DAO方法修改分类
            productCategoryDao.updateBean(category);
            return true;
        } catch (Exception e) {
            // 将报错的信息打印到日志表
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据name得到分类对象
     */
    public ProductCategory getCategoryByName(String name) {
        StringBuffer sql = new StringBuffer("select pc from ProductCategory as pc where 1=1");
        Map<String, Object> map = new HashMap<String, Object>();
        if (!StringUtil.isEmpty(name)) {
            sql.append(" and pc.name = :name");
            map.put("name", name);
        }
        try {
            List<ProductCategory> list = productCategoryDao.list(sql.toString(), map);
            return list.get(0);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 根据id查到productCategory集合
     */
    public List<ProductCategory> listProductCategory(String id) {
        StringBuffer sql = new StringBuffer("select distinct pc from ProductCategory as pc,Product as p where 1=1");
        Map<String, Object> map = new HashMap<String, Object>();
        sql.append(" and p.mainCategory = pc.id");
        try {
            List<ProductCategory> list = productCategoryDao.list(sql.toString(), map);
            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 根据店铺id得到分类的集合
     */
    public List<ProductCategory> getProductCategoryByStoreId(String storeId) {
        try {
            if (StringUtil.isEmpty(storeId)) {
                return null;
            }
            StringBuffer hql = new StringBuffer("select pc from ProductCategory as pc,Product as p,ProductCategoryRel as pcr where 1=1");
            Map<String, Object> params = new HashMap<String, Object>();
            hql.append(" and p.id = pcr.productId");
            hql.append(" and pcr.categoryId = pc.id");
            hql.append(" and p.storeId = :storeId");
            params.put("storeId", storeId);

            // 调用DAO方法查找商品
            List<ProductCategory> list = productCategoryDao.list(hql.toString(), params);
            return list;
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * 根据商品分类ID获取分类所关联的商品模板，如果该分类没有关联分类，则往上一层找，直到找到根分类
     *
     * @param @param  categoryId
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     * @Title: getTemplateByCategoryId
     * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
     */
    public String getTemplateByCategoryId(String categoryId) {
        // 如果传进来的分类ID为空，则返回空
        if (StringUtil.isEmpty(categoryId)) {
            return null;
        }

        // 定义SQL的参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();
        // 定义SQL，从商品分类表中查询该分类所关联的模板ID以及父分类ID
        StringBuffer sql = new StringBuffer("select category.templateId,category.parentId from ProductCategory as category where 1=1");
        // 关联商品分类主键
        sql.append(" and category.id = :categoryId");
        paramMap.put("categoryId", categoryId);

        try {
            // 调用DAO查询SQL
            List list = productCategoryDao.list(sql.toString(), paramMap);

            // 如果查询出来的List集合不为空，则取第一条数据
            if (list != null && list.size() > 0) {
                // 取出的第一条数据结构为 String[templateId,parentId];
                Object[] strObj = (Object[]) list.get(0);
                if (strObj != null && strObj.length > 0) {
                    // 获取模板ID
                    String templateId = (String) strObj[0];
                    // 获取父分类ID
                    String parentId = (String) strObj[1];
                    // 如果模板ID为空，则使用递归的方式继续获取上一级分类所关联的模板
                    if (StringUtil.isEmpty(templateId)) {
                        return getTemplateByCategoryId(parentId);
                    } else {
                        // 如果模板ID不为空，则返回该模板ID
                        return templateId;
                    }
                }
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取分类的全称
     *
     * @param @param  categoryId
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     * @Title: getCategoryFullName
     * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
     */
    public String getCategoryFullNameByCategoryId(String categoryId, String splitStr) {
        // 如果传进来的商品分类ID为空，则返回null
        if (StringUtil.isEmpty(categoryId)) {
            return null;
        }

        try {
            // 先获取该商品分类的实体类
            ProductCategory category = (ProductCategory) productCategoryDao.get(categoryId);
            // 如果查出的实体为空，则返回null
            if (category == null) {
                return null;
            }

            // 如果该分类没有父分类，则表示该分类是跟分类，直接返回该分类的名称
            if (StringUtil.isEmpty(category.getParentId()) || StringUtil.isEmpty(category.getFullPath())) {
                return category.getName();
            }

            String[] categoryIds = category.getFullPath().split("_");
            // 如果截取的fullPath的集合为空或者长度等于0，则返回该分类的名称
            if (categoryIds == null || categoryIds.length <= 0) {
                return category.getName();
            }

            // 拼写SQL语句，使用in的方法，(100万条数据以下不存在性能问题)
            StringBuffer sql = new StringBuffer("select category.id,category.name from ProductCategory as category where 1=1");
            sql.append(" and category.id in (");
            for (int i = 0; i < categoryIds.length; i++) {
                sql.append("'").append(categoryIds[i]).append("'");
                if (i < categoryIds.length - 1) {
                    sql.append(",");
                }
            }
            sql.append(")");

            // 调用DAO方法查询SQL
            List cList = productCategoryDao.list(sql.toString(), null);

            // 如果查出的集合为空，或者长度小于或等于0，则返回该分类的名称
            if (cList == null || cList.size() <= 0) {
                return category.getName();
            }

            Map<String, String> nameMap = new HashMap<String, String>();
            // 便利查出的集合
            for (int i = 0; i < cList.size(); i++) {
                Object[] obj = (Object[]) cList.get(i);
                if (obj != null && obj.length > 0) {
                    nameMap.put((String) obj[0], (String) obj[1]);
                }
            }

            StringBuffer returnName = new StringBuffer("");
            String rootId = Cache.getResource("product.category.rootcategory");
            if (StringUtil.isEmpty(rootId)) {
                rootId = "COMMON";
            }

            for (int i = 0; i < categoryIds.length; i++) {
                if (StringUtil.isEmpty(categoryIds[i]) || categoryIds[i].equals(rootId)) {
                    continue;
                }
                returnName.append(nameMap.get(categoryIds[i]));
                if (i < categoryIds.length - 1) {
                    returnName.append(splitStr);
                }
            }

            return returnName.toString();

        } catch (Exception e) {
            // TODO: handle exception
        }

        return null;
    }

    @Override
    public List<String> getAllCategoryIdsById(String id) {
        // 如果传进来的ID为空，则返回null
        if (StringUtil.isEmpty(id)) {
            return null;
        }
        // 定义查询SQL
        StringBuffer sql = new StringBuffer(" select pc.id from ProductCategory as pc where 1=1");
        // 定义参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();

        // 权限的状态是放开
        sql.append(" and pc.status = '1'");

        sql.append(" and pc.fullPath like :categoryId");
        paramMap.put("categoryId", "%" + id + "%");

        // 根据位置进行排序
        sql.append(" order by pc.position asc");
        try {
            // 调用DAO查询SQL
            List<String> ids = productCategoryDao.list(sql.toString(), paramMap);

            return ids;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<ProductCategory> getStoreCategories(String storeId, String parentId, boolean canParentNull, int showNumber) {
        // 定义查询SQL
        StringBuffer sql = new StringBuffer(" select pc from ProductCategory as pc where 1=1");
        // 定义参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();

        // 权限的状态是放开
        sql.append(" and pc.status = '1'");
        sql.append(" and pc.checkStatus = '1'");
        // 关联店铺
        sql.append(" and pc.storeId = :storeId");
        paramMap.put("storeId", storeId);

        if (!StringUtil.isEmpty(parentId)) {
            sql.append(" and pc.fullPath like :categoryId");
            paramMap.put("categoryId", "%" + parentId + "%");
        } else {
            sql.append(" and pc.fullPath like '%STORE%'");
        }

        // 根据位置进行排序
        sql.append(" order by pc.position asc");

        // 定义查询SQL
        StringBuffer sql2 = new StringBuffer(" select pc from ProductCategory as pc where 1=1");
        // 定义参数MAP
        Map<String, Object> paramMap2 = new HashMap<String, Object>();

        // 如果传进来的父ID不为空，则加上父ID条件;若为空，则返回null
        if (!StringUtil.isEmpty(parentId)) {
            sql2.append(" and pc.parentId = :parentId");
            paramMap2.put("parentId", parentId);
            // 关联店铺
            sql2.append(" and pc.storeId = :storeId");
            paramMap2.put("storeId", storeId);
        } else {
            if (canParentNull) {
                sql2.append(" and pc.id = 'STORE'");
            } else {
                return null;
            }
        }

        // 权限的状态是放开
        sql2.append(" and pc.status = '1'");
        sql2.append(" and pc.checkStatus = '1'");

        // 根据位置进行排序
        sql2.append(" order by pc.position asc");

        try {
            // 调用DAO查询SQL
            List<ProductCategory> productCategoryList = productCategoryDao.list(sql.toString(), paramMap);
            // 调用DAO查询SQL
            List<ProductCategory> categoryList = null;

            if (showNumber > 0) {
                Pager page = new Pager(1);
                page.setRowsPerPage(showNumber);

                categoryList = productCategoryDao.list(page, sql2.toString(), paramMap2);
            } else {
                categoryList = productCategoryDao.list(sql2.toString(), paramMap2);
            }

            ProductCategoryHelper.recoverCategoryList(productCategoryList, categoryList);

            return categoryList;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;

    }

    @Override
    public boolean saveStoreCategory(List<ProductCategory> pList, List<ProductCategory> cList) {
        // 创建Session
        Session session = productCategoryDao.createSession();
        // 定义事务
        Transaction transaction = session.beginTransaction();

        // 将子分类放入父分类的childList
        for (int i = 0; i < pList.size(); i++) {
            ProductCategory parent = pList.get(i);
            // 设置父分类创建时间
            parent.setCreateTime(DateUtil.getSysDateTimeString());
            for (int j = 0; j < cList.size(); j++) {
                ProductCategory child = cList.get(j);
                // 设置子分类创建时间
                child.setCreateTime(DateUtil.getSysDateTimeString());
                // 找到对应的子分类
                if (parent.getId().equals(child.getParentId())) {
                    // 将子分类放入父分类的childList
                    parent.getChildList().add(child);
                }
            }
        }
        try {
            for (int i = 0; i < pList.size(); i++) {
                ProductCategory parent = pList.get(i);

                if (parent.getId().length() == 32) {
                    // 设置排序位置
                    parent.setPosition(i + 1);
                    // 设置一级分类的父分类
                    parent.setParentId("STORE");
                    // 设置全路径
                    parent.setFullPath(parent.getParentId() + "_" + parent.getId());
                    // 数据库存在该分类，用修改
                    productCategoryDao.updateBean(parent, session);
                    // 保存子分类
                    for (int j = 0; j < parent.getChildList().size(); j++) {
                        ProductCategory child = parent.getChildList().get(j);
                        // 设置排序位置
                        child.setPosition(i + 1);
                        if (child.getId().length() == 32) {
                            // 数据库存在该分类，用修改
                            productCategoryDao.updateBean(child, session);
                            // 设置全路径
                            child.setFullPath(parent.getFullPath() + "_" + child.getId());
                        } else {
                            // 数据库不存在该分类，用新增
                            productCategoryDao.addBean(child, session);
                            // 重新获取子分类
                            child = (ProductCategory) productCategoryDao.get(child.getId(), session);
                            // 设置全路径
                            child.setFullPath(parent.getFullPath() + "_" + child.getId());
                            // 更新
                            productCategoryDao.updateBean(child, session);
                        }
                    }
                } else {
                    // 设置排序位置
                    parent.setPosition(i + 1);
                    // 设置一级分类的父分类
                    parent.setParentId("STORE");
                    // 数据库不存在该分类，用新增
                    productCategoryDao.addBean(parent, session);
                    // 重新获取父分类（便于设置全路径）
                    parent = (ProductCategory) productCategoryDao.get(parent.getId(), session);
                    // 设置全路径
                    parent.setFullPath(parent.getParentId() + "_" + parent.getId());
                    // 更新
                    productCategoryDao.updateBean(parent, session);
                    // 保存子分类
                    for (int j = 0; j < parent.getChildList().size(); j++) {
                        ProductCategory child = parent.getChildList().get(j);

                        // 设置排序位置
                        child.setPosition(i + 1);
                        // 重新设置父id
                        child.setParentId(parent.getId());
                        // 保存子分类
                        productCategoryDao.addBean(child, session);
                        child = (ProductCategory) productCategoryDao.get(child.getId(), session);
                        // 设置全路径
                        child.setFullPath(parent.getFullPath() + "_" + child.getId());
                        // 更新
                        productCategoryDao.updateBean(child, session);
                    }
                }
            }
            session.flush();
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return false;
    }

    /**
     * @param channeId
     * @return Map<String,List<ProductDetailDao>> 返回类型
     * @throws
     * @Title: getProductDetailsForChannelId
     * @Description: TODO(根据频道ID查询优选商城首页商品) 详细业务流程: (详细描述此方法相关的业务处理流程)
     */
    public Map<String, List<ProductDetailDao>> getProductDetailsForChannelId(String channeId) {
        Map<String, List<ProductDetailDao>> map = new HashMap<String, List<ProductDetailDao>>();
        // StringBuffer buffer = new StringBuffer("from Product p");

        return map;
    }

    @Override
    public List<ProductCategory> getCompleteByPc() {
        // hql
        String hql = "from ProductCategory";
        try {
            List<ProductCategory> list = this.productCategoryDao.list(hql, null);
            if (list != null && list.size() > 0) {
                return list;
            }
            return null;
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public List<String> getCategoryNames() {
        StringBuffer hql = new StringBuffer("select pc.name from ProductCategory as pc where 1=1");
        hql.append(" and pc.status = '1'");
        //查询成品和原料分类的Id
        ProductCategory pc1 = this.getCategoryByName("成品");
        String cpId = pc1.getId();
        ProductCategory pc2 = this.getCategoryByName("原料");
        String ylId = pc2.getId();
        hql.append(" and (pc.fullPath like" + " '%" + cpId + "%'" + " or" + " pc.fullPath like" + " '%" + ylId + "%')");
        hql.append(" and pc.name not in('成品','原料')");
        try {
            List<String> list = this.productCategoryDao.list(hql.toString(), null);
            if (list != null && list.size() > 0) {
                return list;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    public List<ProductCategory> getCategoriesByType(String productType) {
        StringBuffer hql = new StringBuffer("select pc from ProductCategory as pc where 1=1");
        Map<String, Object> params = new HashMap<>();
        String parentId = "";
        if (StringUtil.isEmpty(productType)) {
            return null;
        }
        if ("0".equals(productType)) {//现货
            parentId = this.getCategoryByName("现货").getId();
            hql.append(" and pc.parentId =:parentId");
            params.put("parentId", parentId);
        } else if ("1".equals(productType)) {//原料
            parentId = this.getCategoryByName("原料").getId();
            hql.append(" and pc.parentId =:parentId");
            params.put("parentId", parentId);
        } else if ("2".equals(productType)) {//危废品
            parentId = this.getCategoryByName("危废品").getId();
            hql.append(" and pc.parentId =:parentId");
            params.put("parentId", parentId);
        }


        try {
            List<ProductCategory> categories = this.productCategoryDao.list(hql.toString(), params);
            return categories;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ProductCategory> getMallCatg() {
        // 获取“商城”子分类
        return getChildCategories(Cache.getResource("lvseshangcheng"));
    }

    /**
     * @param @return 设定文件
     * @return List<ProductCategory> 商品分类的集合
     * @throws
     * @Title: getCategories
     * @Description: TODO 根据商品类型获取分类列表 详细业务流程: (详细描述此方法相关的业务处理流程)
     */
    public List<ProductCategory> getCcwccCategorys(String productType) {
        StringBuffer hql = new StringBuffer("select pc from ProductCategory as pc where 1=1");
        Map<String, Object> params = new HashMap<>();

        if (!StringUtil.isEmpty(productType)) {
            String parentId;
            if ("0".equals(productType)) {//现货
                parentId = this.getCategoryByName("现货").getId();
                hql.append(" and pc.parentId =:parentId");
                params.put("parentId", parentId);
            } else if ("1".equals(productType)) {//原料
                parentId = this.getCategoryByName("原料").getId();
                hql.append(" and pc.parentId =:parentId");
                params.put("parentId", parentId);
            } else if ("2".equals(productType)) {//危废品
                parentId = this.getCategoryByName("危废品").getId();
                hql.append(" and pc.parentId =:parentId");
                params.put("parentId", parentId);
            }
        } else {
            String xhParentId = "";
            ProductCategory xhCategory = this.getCategoryByName("现货");
            if(xhCategory != null){
                xhParentId = xhCategory.getId();
            }
            String ylParentId = "";
            ProductCategory ylCategory = this.getCategoryByName("原料");
            if(ylCategory != null){
                ylParentId = ylCategory.getId();
            }
            String wfParentId = "";
            ProductCategory wfCategory = this.getCategoryByName("危废品");
            if(wfCategory != null){
                wfParentId = wfCategory.getId();
            }

            hql.append(" and (");
            int a = 0;
            if(!StringUtil.isEmpty(xhParentId)){
                hql.append(" pc.parentId = :xhParentId");
                params.put("xhParentId", xhParentId);
                a++;
            }

            if(!StringUtil.isEmpty(ylParentId)){
                if(a != 0){
                    hql.append(" or ");
                }
                hql.append(" pc.parentId = :ylParentId");
                params.put("ylParentId", ylParentId);
                a++;
            }

            if(!StringUtil.isEmpty(wfParentId)){
                if(a != 0){
                    hql.append(" or ");
                }
                hql.append(" pc.parentId = :wfParentId");
                params.put("wfParentId", wfParentId);
                a++;
            }

            hql.append(" )");

            params.put("ylParentId", ylParentId);
            params.put("wfParentId", wfParentId);
        }

        try {
            List<ProductCategory> categories = this.productCategoryDao.list(hql.toString(), params);
            return categories;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}