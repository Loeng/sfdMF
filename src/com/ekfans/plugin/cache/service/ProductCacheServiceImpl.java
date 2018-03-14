package com.ekfans.plugin.cache.service;

import java.util.List;
import java.util.Map;

import com.ekfans.base.product.model.Product;
import com.ekfans.base.product.model.ProductCategory;
import com.ekfans.base.product.service.IProductCategoryService;
import com.ekfans.base.product.service.IProductService;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.cache.utils.ProductCacheKeyUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@SuppressWarnings("unchecked")
public class ProductCacheServiceImpl implements IProductCacheService {
    /**
     * 根据商品分类ID和商品类型获取商品集合
     * 
     * @Title: getProductsByCategory
     * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
     * @param categoryId
     * @param productType
     * @return List<Product> 返回类型
     * @throws
     */
    public List<String> getProductsByCategory(String categoryId, String productType) {
        // 如果傳過來的商品分類ID為空，或者商品分類為空，則返回NULL
        if (StringUtil.isEmpty(categoryId) || StringUtil.isEmpty(productType)) {
            return null;
        }
        // 获取在缓存中的KEY
        String cacheKey = ProductCacheKeyUtil.getCategoryProductsCacheKey(categoryId, productType);
        // 從緩存中獲取商品集合
        List<String> productList = (List<String>) Cache.engine.get(cacheKey);
        // 如果获取到的集合为空则调用Service查询
        if (productList == null || productList.size() <= 0) {
            IProductService productService = SpringContextHolder.getBean(IProductService.class);
            productList = productService.getProductIdListByCategory(categoryId, productType, 0);
            Cache.engine.add(cacheKey, productList);
        }
        return productList;
    }

    @Override
    public Product getProductById(String productId) {
        // 如果傳過來的商品分類ID為空，或者商品分類為空，則返回NULL
        if (StringUtil.isEmpty(productId)) {
            return null;
        }
        // 获取在缓存中的KEY
        String cacheKey = ProductCacheKeyUtil.getProductCacheKey(productId);
        // 從緩存中獲取商品对象
        Product product = (Product) Cache.engine.get(cacheKey);
        // 如果获取到的集合为空则调用Service查询
        if (product == null) {
            IProductService productService = SpringContextHolder.getBean(IProductService.class);
            product = productService.getProductById(productId);
            Cache.engine.add(cacheKey, product);
        }
        return product;
    }

    @Override
    public void refreshProduct(String productId) {
        // 如果傳過來的商品分類ID為空，或者商品分類為空，則返回NULL
        if (StringUtil.isEmpty(productId)) {
            return;
        }
        // 获取在缓存中的KEY
        String cacheKey = ProductCacheKeyUtil.getProductCacheKey(productId);
        Cache.engine.remove(cacheKey);
        this.getProductById(productId);
    }

    public void refrefshProductsByCategory(String categoryId, String productType) {
        // 如果傳過來的商品分類ID為空，或者商品分類為空，則返回NULL
        if (StringUtil.isEmpty(categoryId) || StringUtil.isEmpty(productType)) {
            return;
        }
        IProductCategoryService categoryService = SpringContextHolder.getBean(IProductCategoryService.class);
        ProductCategory category = categoryService.getCategoryById(categoryId);
        if (category == null) {
            // 获取在缓存中的KEY
            String cacheKey = ProductCacheKeyUtil.getCategoryProductsCacheKey(categoryId, productType);
            Cache.engine.remove(cacheKey);
            // this.getProductsByCategory(categoryId, productType);
        } else {
            String fullPath = category.getFullPath();
            if (StringUtil.isEmpty(fullPath)) {
                return;
            }
            String[] ids = fullPath.split("_");
            if (ids == null || ids.length <= 0) {
                return;
            }
            for (int i = 0; i < ids.length; i++) {
                String parentId = ids[i];
                if (!StringUtil.isEmpty(parentId)) {
                    String cacheKey = ProductCacheKeyUtil.getCategoryProductsCacheKey(parentId, productType);
                    Cache.engine.remove(cacheKey);
                    this.getProductsByCategory(parentId, productType);
                }
            }

        }

    }

    @Override
    public List<List<Product>> getWebIndexProductXsgp() {
        // 获取在缓存中的KEY
        String cacheKey = ProductCacheKeyUtil.getProductWebIndexKey();
        // 從緩存中獲取商品对象
        List<List<Product>> productsIndex = (List<List<Product>>) Cache.engine.get(cacheKey);
        // 如果获取到的集合为空则调用Service查询
        if (productsIndex == null) {
            IProductService productService = SpringContextHolder.getBean(IProductService.class);
            productsIndex = productService.getIndexProduct();
            Cache.engine.add(cacheKey, productsIndex);
        }
        return productsIndex;
    }

    @Override
    public void refrefshWebIndexProductXsgp() {
        // 获取在缓存中的KEY
        String cacheKey = ProductCacheKeyUtil.getProductWebIndexKey();
        IProductService productService = SpringContextHolder.getBean(IProductService.class);
        List<List<Product>> productsIndex = productService.getIndexProduct();
        if (productsIndex != null && productsIndex.size() > 0) {
            Cache.engine.remove(cacheKey);
            Cache.engine.add(cacheKey, productsIndex);
        }
    }
}
