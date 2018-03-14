package com.ekfans.controllers.system.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.user.model.UserLevel;
import com.ekfans.base.user.service.IUserLevelService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.controllers.tools.util.FileUploadHelper;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
public class UserLevelController extends BasicController {
	// 定义SERVICE
    @Autowired
	private IUserLevelService userLevelService;

    /**
     * 跳转到新增页面
     * 
     * @return
     */
    @RequestMapping(value = "/system/user/level/add")
    public String add() {
        return "/system/user/level/userLevelAdd";
    }
	
	/**
	 * 新增会员等级
	 * @return
	 */
    @RequestMapping(value = "/system/user/level/save")
    public String save(UserLevel userLevel,HttpServletRequest request,HttpServletResponse response) {
        try {
            String currentPath = "/customerfiles/icon/" + DateUtil.getNoSpSysDateString() + "/userLevel/";
            // 调用方法获取分类图标，返回图标路径
            String picturePath = FileUploadHelper.uploadFile("icon", currentPath, request, response);
            //设置图片
            userLevel.setIcon(picturePath);
            if(StringUtil.isEmpty(userLevel.getName()) || userLevel.getName().length()<3 || userLevel.getName().length()>20){
        	request.setAttribute("addOk", false);
        	return "/system/user/level/userLevelAdd";
            }
            // 利用Service的方法添加会员等级
            if (userLevelService.addLevel(userLevel, null)) {
                request.setAttribute("addOk", true);
                // 添加成功，返回
                return "/system/user/level/userLevelAdd";
            }
        } catch (Exception e) {
            // 添加失败，返回false
            request.setAttribute("addOk", false);
        }
        return "error";
    }

	
	/**
	 * 删除会员等级
	 * @return
	 */
    @RequestMapping(value = "/system/user/level/delete/{id}")
    @ResponseBody
    public Object delete(@PathVariable String id) {
        try {
            // 利用Service的方法根据id删除会员等级
            if (userLevelService.deleteLevel(id)) {
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
	 * 修改会员等级
	 * @return
	 */
    @RequestMapping(value = "/system/user/level/modify")
    public String modify(UserLevel userLevel, HttpServletRequest request,HttpServletResponse response) {
        try {
            
            String currentPath = "/customerfiles/icon/" + DateUtil.getNoSpSysDateString() + "/userLevel/";
            // 调用方法获取分类图标，返回图标路径
            String picturePath = FileUploadHelper.uploadFile("icon", currentPath, request, response);
            //设置图片
            userLevel.setIcon(picturePath);
            if(StringUtil.isEmpty(userLevel.getName()) || userLevel.getName().length()<3 || userLevel.getName().length()>20){
        	request.setAttribute("modifyOk", false);
        	return "/system/user/level/userLevelModify";
            }
            // 利用Service的方法修改频道
            if(userLevelService.modifyLevel(userLevel,null)){
                // 修改成功，返回true
                request.setAttribute("modifyOk", true);
                request.setAttribute("userLevel", userLevel);
                return "system/user/level/userLevelModify";
            }
        } catch (Exception e) {
            // 修改失败，返回false
            request.setAttribute("modifyOk", false);
        }
        return "error";
    }
	
	/**
	 * 查询会员等级信息
	 * @return
	 */
    @RequestMapping(value = "/system/user/level/detail/{id}")
    public String detail(@PathVariable String id, HttpServletRequest request) {
        try {
            // 利用Service的方法根据id查找会员
            UserLevel userLevel = userLevelService.getLevel(id);
            request.setAttribute("userLevel", userLevel);
            return "system/user/level/userLevelModify";
        } catch (Exception e) {
            // TODO: handle exception
        }
        return "error";
    }
	
	/**
	 * 查找等级列表
	 * @return
	 */
    @RequestMapping(value = "/system/user/level/list")
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
            // 利用Service的方法查找频道
            List<UserLevel> userLevels = userLevelService.getLevels(pager);
            request.setAttribute("userLevels", userLevels);
            request.setAttribute("pager", pager);
            return "/system/user/level/userLevelList";
        } catch (Exception e) {
            // TODO: handle exception
        }
        return "error";
    }
    /**
     * 
    * @Title: detail
    * @Description: TODO(根据id查询出详细页面)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param id
    * @param request
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value = "/system/user/level/query/{id}")
    public String Query(@PathVariable String id, HttpServletRequest request) {
        try {
            // 利用Service的方法根据id查找会员
            UserLevel userLevel = userLevelService.getLevel(id);
            request.setAttribute("userLevel", userLevel);
            return "system/user/level/userLevelQuery";
        } catch (Exception e) {
           e.printStackTrace();
        }
        return "error";
    }
    
    /**
     * 
    * @Title: plistSreach
    * @Description: TODO 会员等级弹出层的搜索
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param name
    * @param request
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value = "/system/userLevel/plistSearch")
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
            List<UserLevel> userLevels = userLevelService.getLevels(pager);
            request.setAttribute("userLevels", userLevels);
            request.setAttribute("pager", pager);
            
            
            return "/system/user/userAndUserLevel";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }
}
