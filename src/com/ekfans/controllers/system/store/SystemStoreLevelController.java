package com.ekfans.controllers.system.store;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.store.model.StoreLevel;
import com.ekfans.base.store.service.IStoreLevelService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
public class SystemStoreLevelController extends BasicController {
	// 定义SERVICE
	@Autowired
	private IStoreLevelService storeLevelService;
	
	/**
	 * 跳转到新增页面
	 * @return
	 */
	@RequestMapping(value = "/system/store/storeLevel/add")
	public String add() {
		return "/system/store/level/storeLevelAdd";
	}
	
	/**
	 * 新增店铺等级
	 * @return
	 */
	@RequestMapping(value = "/system/store/storeLevel/save")
	public String save(StoreLevel storeLevel,HttpServletRequest request) {
		try {
		    //如果店铺等级名为空 则新增失败
		    if(StringUtil.isEmpty(storeLevel.getName())
		                          || storeLevel.getName().length()<3 || storeLevel.getName().length()>20){
			request.setAttribute("addOk", false);
			return "/system/store/level/storeLevelAdd";
		    }
			// 利用Service的方法添加等级
			if (storeLevelService.addLevel(storeLevel,null,null)) {
				request.setAttribute("addOk", true);
				// 添加成功，返回
				return "/system/store/level/storeLevelAdd";
			}
		} catch (Exception e) {
			// 添加失败，返回false
			request.setAttribute("addOk", false);
		}
		return "error";
	}
	
	/**
	 * 删除店铺等级
	 * @return
	 */
	@RequestMapping(value = "/system/store/storeLevel/delete/{id}")
	@ResponseBody
	public Object delete(@PathVariable String id) {
		try {
			// 利用Service的方法根据id删除等级
			if (storeLevelService.deleteLevel(id)) {
				// 删除成功，返回true
				return true;
			}
		} catch (Exception e) {
			// 删除失败，返回false
			return false;
		}
		return "error";
	}
	
	/**
	 * 修改店铺等级
	 * @return
	 */
	@RequestMapping(value = "/system/store/storeLevel/modify")
	public String modify(StoreLevel storeLevel,HttpServletRequest request) {
		try {
		    //如果店铺等级名为空 则修改失败
		    if(StringUtil.isEmpty(storeLevel.getName())
		                          || storeLevel.getName().length()<3 || storeLevel.getName().length()>20){
			request.setAttribute("modifyOk", false);
			return "/system/store/level/storeLevelModify";
		    }
			// 利用Service的方法修改等级
			if(storeLevelService.modifyLevel(storeLevel,null,null)){
				// 修改成功，返回true
				request.setAttribute("modifyOk", true);
				return "/system/store/level/storeLevelModify";
			}
		} catch (Exception e) {
			// 修改失败，返回false
			request.setAttribute("modifyOk", false);
		}
		return "error";
	}
	
	/**
	 * 查询店铺等级信息
	 * @return
	 */
	@RequestMapping(value = "/system/store/storeLevel/detail/{id}")
	public String detail(@PathVariable String id, HttpServletRequest request) {
		try {
			// 利用Service的方法根据id查找等级
			StoreLevel storeLevel = storeLevelService.getLevel(id);
			request.setAttribute("storeLevel", storeLevel);
			return "/system/store/level/storeLevelModify";
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "error";
	}
	
	/**
	 * 查找等级列表
	 * @return
	 */
	@RequestMapping(value = "/system/store/storeLevel/list")
	public String list(HttpServletRequest request) {
		try {
		 // 定义分页
            Pager pager = new Pager();
            
            // 从页面获取页码
            String currentpageStr = request.getParameter("pageNum");
            // 将从页面获取的分页数据转化成int型
            int currentPage = 1;
            if (!StringUtil.isEmpty(currentpageStr)) {
                try {
                    currentPage = Integer.parseInt(currentpageStr);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            // 设置要查询的页码
            pager.setCurrentPage(currentPage);
			// 利用Service的方法查找等级
			List<StoreLevel> storeLevels = storeLevelService.getLevels(pager);
			request.setAttribute("storeLevels", storeLevels);
			request.setAttribute("pager", pager);
			return "/system/store/level/storeLevelList";
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "error";
	}
	/**
     * 根据id得到详细页面
     * @return
     */
    @RequestMapping(value = "/system/store/storeLevel/query/{id}")
    public String query(@PathVariable String id, HttpServletRequest request) {
        try {
            // 利用Service的方法根据id查找等级
            StoreLevel storeLevel = storeLevelService.getLevel(id);
            request.setAttribute("storeLevel", storeLevel);
            return "/system/store/level/storeLevelQuery";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }
    /**
     * 
    * @Title: plistSreach
    * @Description: TODO 店铺等级弹出层的搜索
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param name
    * @param request
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value = "/system/storeLevel/plistSearch")
    public String plistSearch(HttpServletRequest request) {
        try {
            // 定义分页
            Pager pager = new Pager();
            // 从页面获取页码
            String currentpageStr = request.getParameter("pageNum");
            // 将从页面获取的分页数据转化成int型
            int currentPage = 1;
            if (!StringUtil.isEmpty(currentpageStr)) {
                try {
                    currentPage = Integer.parseInt(currentpageStr);
                } catch (Exception e) {
                   e.printStackTrace();
                }
            }
            // 设置要查询的页码
            pager.setCurrentPage(currentPage);
            // 利用Service的方法查找店铺
            List<StoreLevel> storeLevels = storeLevelService.getLevels(pager);
            request.setAttribute("storeLevels", storeLevels);
            request.setAttribute("pager", pager);
            
            
            return "/system/store/level/storeAndStoreLevel";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }
}
