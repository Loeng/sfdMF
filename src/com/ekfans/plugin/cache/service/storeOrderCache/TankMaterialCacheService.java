package com.ekfans.plugin.cache.service.storeOrderCache;

import java.util.List;
import com.ekfans.base.store.model.TankMaterial;
import com.ekfans.base.store.service.ITankMaterialService;
import com.ekfans.basic.spring.SpringContextHolder;

/**
 * 
 * @ClassName: VehicleTypeCacheService
 * @Description: TODO(危废运输频道页-列表展示所用缓存)
 * @author hjc
 * @date 2014-5-25 下午04:56:53
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class TankMaterialCacheService implements ITankMaterialCacheService {
	
	@Override
	public List<TankMaterial> getList() {
		ITankMaterialService tankMaterialService=SpringContextHolder.getBean(ITankMaterialService.class);
		List<TankMaterial> tankMaterials = tankMaterialService.getList();
		return tankMaterials;
	}

}
