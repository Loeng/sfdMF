package com.ekfans.controllers.system.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.system.model.Manager;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.dao.IBankBindingDao;
import com.ekfans.base.user.dao.IUserDao;
import com.ekfans.base.user.model.Bank;
import com.ekfans.base.user.model.BankBinding;
import com.ekfans.base.user.model.IntegralLog;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.model.UserLevel;
import com.ekfans.base.user.service.IBankBindingService;
import com.ekfans.base.user.service.IBankService;
import com.ekfans.base.user.service.IIntegralService;
import com.ekfans.base.user.service.IUserLevelService;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.base.user.util.UserConst;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.controllers.tools.util.FileUploadHelper;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
public class UserController extends BasicController {
    
	private Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private IUserService userService;
    @Autowired
    private IUserDao userDao;
    @Autowired
    private IUserLevelService userLevelService;
    
    @Autowired
    private IIntegralService integralService;
    
    @Autowired
    private IBankBindingService bankBindingService;
    @Autowired
    private IBankService bankService;
    @Autowired
    private IBankBindingDao bankBindingDao;
    /**
     * 跳转到新增页面
     * 
     * @return
     */
    @RequestMapping(value = "/system/user/add")
    public String add() {
        return "/system/user/userAdd";
    }

    /**
     * 新增会员
     * 
     * @return
     */
    @RequestMapping(value = "/system/user/save")
    public String save(User user, HttpServletRequest request, HttpSession session, HttpServletResponse response) {
        // 验证会员名和密码是否为空
        if (StringUtil.isEmpty(user.getName()) || user.getName().length() < 6 || user.getName().length() > 20
                || StringUtil.isEmpty(user.getPassword()) || user.getPassword().length() < 6
                || user.getPassword().length() > 32) {
            // 如果为空，返回添加失败
            request.setAttribute("addOk", false);
            return "/system/user/userAdd";
        }

        // 设置图标保存路径
        String currentPath = "/customerfiles/customer/config/";
        // 调用方法获取分类图标，返回图标路径
        String picPath = FileUploadHelper.uploadFile("userPic", currentPath, request, response);
        user.setHeadPortrait(picPath);
        // 关联会员等级以后的保存操作
        IntegralLog inter = new IntegralLog();

        // 得到等级名
        String levelName = request.getParameter("levelName");
        if (!StringUtil.isEmpty(levelName)) {
            UserLevel userLevel = userLevelService.getUserLevelByName(levelName);
            Double loConditions = userLevel.getLoConditions();
            // 获取管理员对象
            Manager manager = (Manager) session.getAttribute(SystemConst.MANAGER);
            // 设置操作者
            inter.setOperation(manager.getId());
            // 设置等级对应的积分
            inter.setIntegral(loConditions);
            // 设置等级id
            user.setLevelId(userLevel.getId());
        }
        // 设置会员类型为用户
        user.setType(UserConst.USER_TYPE_CUSTOMER);

        // 利用Service的方法添加用户 和保存关联的积分日志
        if (userService.addUser(user, inter, request)) {
            request.setAttribute("addOk", true);
            // 添加成功，返回
            return "/system/user/userAdd";
        } else {
            request.setAttribute("addOk", false);
            // 添加成功，返回
            return "/system/user/userAdd";
        }
    }

    /**
     * 删除会员
     * 
     * @return
     */
    @RequestMapping(value = "/system/user/delete/{id}")
    @ResponseBody
    public Object delete(@PathVariable
    String id) {
        try {
            // 利用Service的方法根据id删除用户和积分日志
            if (userService.deleteUser(id) && integralService.deleteAllIngegralByUserId(id)) {
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
     * 修改会员
     * 
     * @return
     */
    @RequestMapping(value = "/system/user/modify")
    public String modify(User user, HttpServletRequest request, 
            HttpServletResponse response, HttpSession session) {
        // 如果必填字段未填写 则修改失败
        if (StringUtil.isEmpty(user.getName()) || user.getName().length() < 6 || user.getName().length() > 20
                || StringUtil.isEmpty(user.getPassword()) || user.getPassword().length() < 6
                || user.getPassword().length() > 32) {
            request.setAttribute("modifyOk", false);
            return "system/user/userModify";
        }

        // 得到等级名
        String levelName = request.getParameter("levelName");
        
        // 创建等级对象
        IntegralLog inter = null;
        // 定义旧的积分日志
        IntegralLog oldInter = null;
       
        // 判断是否关联等级
        if (!StringUtil.isEmpty(levelName)) {
            // 根据等级名查找等级对象
            UserLevel userLevel = userLevelService.getUserLevelByName(levelName);
            
            inter = new IntegralLog();
            // 是否修改等级
            if (!userLevel.getId().equals(user.getLevelId())) {
                // 得到等级对应的积分
                Double loConditions = userLevel.getLoConditions();
                // 获取管理员对象
                Manager manager = (Manager) session.getAttribute(SystemConst.MANAGER);
                // 设置操作者
                inter.setOperation(manager.getId());
                // 设置等级对应的积分
                inter.setIntegral(loConditions);
                // 设置等级id
                user.setLevelId(userLevel.getId());
                
                // 得到旧积分日志
                oldInter = integralService.getLevelIntegral(user.getId());
            }
           
        }
        // 设置图标保存路径
        String currentPath = "/customerfiles/customer/config/";
        // 调用方法获取会员头像，返回路径
        String picPath = FileUploadHelper.uploadFile("userPic", currentPath, request, response);

        // 利用Service的方法修改频道
        if (userService.modifyUser(user, inter,oldInter, picPath)) {
            // 修改成功，返回true
            request.setAttribute("modifyOk", true);
            request.setAttribute("user", user);
            if (!StringUtil.isEmpty(user.getLevelId())) {
                levelName = userLevelService.getLevel(user.getLevelId()).getName();
                request.setAttribute("levelName", levelName);
            }
            return "system/user/userModify";
        } else {
            request.setAttribute("modifyOk", false);
            return "system/user/userModify";
        }
    }

    /**
     * 查询会员信息
     * 
     * @return
     */
    @RequestMapping(value = "/system/user/detail/{id}")
    public String detail(@PathVariable
    String id, HttpServletRequest request) {
        try {
            // 利用Service的方法根据id查找会员
            User user = userService.getUser(id);
            // 得到对应的会员等级
            if (!StringUtil.isEmpty(user.getLevelId())) {
                UserLevel userLevel = userLevelService.getLevel(user.getLevelId());
                String levelName = userLevel.getName();
                request.setAttribute("levelName", levelName);
            }
            request.setAttribute("user", user);
            return "system/user/userModify";
        } catch (Exception e) {
            // TODO: handle exception
        }
        return "error";
    }

    /**
     * 查找会员列表
     * 
     * @return
     */
    @RequestMapping(value = "/system/user/list")
    public String list(HttpServletRequest request) {
        String name = request.getParameter("name"); // 频道名称 
        String status = request.getParameter("status"); // 状态
        String mobile = request.getParameter("mobile"); // 手机号
        String email = request.getParameter("email"); // email
        String cardNumber = request.getParameter("cardNumber"); // 身份证号
        String nickName = request.getParameter("nickName"); // 昵称
        String currentpageStr = request.getParameter("pageNum"); // 页码
        
        // 定义分页
        Pager pager = new Pager();
        int currentPage = 1;
        if (!StringUtil.isEmpty(currentpageStr)) {
            try {
                currentPage = Integer.parseInt(currentpageStr);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        pager.setCurrentPage(currentPage);
        
        // 利用Service的方法查找频道
        List<User> users = userService.listUser(pager, name, status, mobile, email, cardNumber, nickName);
        
        request.setAttribute("users", users);
        request.setAttribute("pager", pager);
        request.setAttribute("name", name);
        request.setAttribute("mobile", mobile);
        request.setAttribute("email", email);
        request.setAttribute("cardNumber", cardNumber);
        request.setAttribute("status", status);
        request.setAttribute("nickName", nickName);
        
        return "/system/user/userList";
    }

    /**
     * 
     * @Title: detail
     * @Description: TODO(跳转到详情页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
     * @param id
     * @param request
     * @return String 返回类型
     * @throws
     */
    @RequestMapping(value = "/system/user/query/{id}")
    public String Query(@PathVariable String id, HttpServletRequest request) {
        try {
            // 利用Service的方法根据id查找会员
            User user = userService.getUser(id);
            // 若等级部位空则查询出等级名
            if (!StringUtil.isEmpty(user.getLevelId())) {
                UserLevel userLevel = userLevelService.getLevel(user.getLevelId());
                String levelName = userLevel.getName();
                request.setAttribute("levelName", levelName);
            }
            request.setAttribute("user", user);
            return "system/user/userQuery";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    /**
     * 
     * @Title: checkUserName
     * @Description: TODO 后台添加会员时检查用户是否存在 详细业务流程: (详细描述此方法相关的业务处理流程)
     * @param @param userName
     * @param @return 设定文件
     * @return Object 返回类型
     * @throws
     */
    @RequestMapping(value = "/system/user/checkName")
    @ResponseBody
    public Object checkUserName(HttpServletRequest request) {
        try {
            String name = request.getParameter("name");
            if (!userService.checkUserName(name)) {
                // 如果id不存在，返回true
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 检查用户名称是否重复
     * 
     * @param name 用户名
     * @return true：有该用户，false：没有
     */
    @RequestMapping(value = "/system/user/newCheckUserName", method = RequestMethod.POST)
    @ResponseBody
    public Boolean newCheckUserName(HttpServletRequest req){
    	String name = req.getParameter("name");
    	
    	return this.userService.checkUserName(name);
    }
    
    /**
     * 修改时验证用户名时候重复
     * 
     * @param req
     * @return true：有该用户，false：没有
     */
    @RequestMapping(value = "/system/user/checkUserNameUpdate", method = RequestMethod.POST)
    @ResponseBody
    public Boolean checkUserNameUpdate(HttpServletRequest req){
    	String oldName = req.getParameter("oldName");
    	String newName = req.getParameter("newName");
    	return this.userService.checkUserNameUpdate(oldName, newName);
    }
    /**
    * @Title: systemBankCardList
    * @Description: TODO(根据配置路径及角色ID查询该ID所有的银行卡列表)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param id
    * @param request
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value="/system/user/bankCardList/{id}")
    public String systemBankCardList(@PathVariable String id,HttpServletRequest request){
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
        //根据传入的ID去匹配 1：供应商，2：采购商，3：核心企业
        //获取页面的查询参数
        String name=request.getParameter("name");
        String banknum =request.getParameter("area");
        //获取所有银行卡列表
        List<BankBinding> banklist = bankBindingService.allBank(pager,id,name,banknum);
        List<Bank> bank = bankService.getBank();
        request.setAttribute("banklist", banklist);
        request.setAttribute("bank", bank);
        request.setAttribute("pager", pager);
        request.setAttribute("name", name);
        request.setAttribute("id", id);
        request.setAttribute("banknum", banknum);
        return "/system/user/bankList";
    }
    /**
    * @Title: bankModify
    * @Description: TODO(根据传入的银行卡ID查询详情)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param id
    * @param request
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value="/system/usesr/bankModify/{id}/{userid}")
    public String bankModify(@PathVariable String id,@PathVariable String userid,HttpServletRequest request){
        try {
            BankBinding bankbing = (BankBinding) bankBindingDao.get(id);
            List<Bank> bank = bankService.getBank();
            request.setAttribute("bankbing", bankbing);
            request.setAttribute("bank", bank);
            request.setAttribute("userid", userid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/system/user/bankModify";
    }
    /**
    * @Title: updateBankCard
    * @Description: TODO(根据银行卡ID查询该银行卡,设置user)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value="/system/user/updateBankCard/{id}")
    @ResponseBody
    public Object updateBankCard(@PathVariable String id ,HttpSession session){
    try {
           //根据ID查询银行卡信息
           BankBinding bankbing = (BankBinding) bankBindingDao.get(id);
           //根据银行卡的storeId查询user表
           User user = userService.getUser(bankbing.getStoreId());
           if(!user.getIsAssociatedBank()){
               user.setIsAssociatedBank(true);
               //修改user表
               userDao.updateBean(user);
           }
           bankbing.setStatus(true);
           bankBindingDao.updateBean(bankbing);
        } catch (Exception e) {
           e.printStackTrace();
           return "0";
        }
        return "1";
    }
}
