package com.ekfans.base.store.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ekfans.base.order.model.Appraise;
import com.ekfans.base.store.dao.IStoreAppraiseDao;
import com.ekfans.base.store.dto.AppraiseContDto;
import com.ekfans.base.store.dto.AppraiseDto;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.user.dao.IUserDao;
import com.ekfans.base.user.model.User;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 
 * @ClassName: StorageAppraiseService
 * @Description: TODO( 商户 评价管理接口的实现 )
 * @author 成都易科远见科技有限公司
 * @date 2014-5-9 下午3:21:30
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
@SuppressWarnings("unchecked")
public class StorageAppraiseService implements IStorageAppraiseService {

    // 定义DAO
    @Resource
    private IStoreAppraiseDao storeAppraiseDao;
    @Resource
    private IUserDao userDao;
    
    // 定义错误处理日志
    private Logger log = LoggerFactory.getLogger(StorageAppraiseService.class);
    
    
    /**
     * 根据评价的类型 返回该时间段的评价数量
    * @param type
    * @param list
    * @return int 返回类型
    * @throws
     */
    public int getAppaiseType(Integer type,List<?> list){
         
         int  returnNumber=0;
         for(int i=0;i<list.size();i++){
             Object[] obj=(Object[]) list.get(i);
             //评价类型(字段为空)转换成数字要抛异常，所以加此判断避免异常发生
             Integer tempType=Integer.parseInt((null==obj[0]||"".equals(obj[0].toString()))?"-4":obj[0].toString());
             if(type==tempType){
                 returnNumber=Integer.parseInt(obj[1].toString());
             }
         }
        return returnNumber;
    }
    
    /**
     * 
    * @Title: appraiseCount
    * @Description: TODO(获取评价的类型的统计)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程) void 返回类型
    * @throws
     */
public List<AppraiseContDto> appraiseCount(Store store){
        
        List<AppraiseContDto>lacd=new ArrayList<AppraiseContDto>();
        try {
                //时间格式化
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
              
                //时间函数
                Calendar cal = Calendar.getInstance();
                //当前的时间
                Date date=new Date();
                
                //设置当前时间
                cal.setTime(date);
                cal.add(Calendar.WEDNESDAY, -1);
                String oneWeekAgo=sdf.format(cal.getTime());
//                System.out.println("方法111111111111Week"+oneWeekAgo);
                cal.setTime(date);
                cal.add(Calendar.MONTH, -1);
                String oneMonthAgo=sdf.format(cal.getTime());
//                System.out.println("方法111111111111M"+oneMonthAgo);
                cal.setTime(new Date());
                cal.add(Calendar.MONTH, -6);
                String sixMonthAgo=sdf.format(cal.getTime());
//                System.out.println("方法111111111116M"+sixMonthAgo);
                
                String newTime=sdf.format(date);
                //一周之内的数据
                StringBuffer oneWeekAgoHql=new StringBuffer(" select  type,count(type) as number from Appraise where 1=1 ");
                oneWeekAgoHql.append(" and createTime>='").append(oneWeekAgo).append("' ");
                oneWeekAgoHql.append(" and createTime<='").append(newTime).append("' ");
                oneWeekAgoHql.append(" and target ='").append(store.getId()).append("' ");
                oneWeekAgoHql.append(" group by type ");
                List<?> loneWeek=storeAppraiseDao.list(oneWeekAgoHql.toString(),null);
//                System.out.println("方法111111111111Week"+loneWeek);
                
                //一个月之内的数据
                StringBuffer oneMonthAgoHql=new StringBuffer(" select  type,count(type) as number from Appraise where 1=1 ");
                oneMonthAgoHql.append(" and createTime>='").append(oneMonthAgo).append("' ");
                oneMonthAgoHql.append(" and createTime<='").append(newTime).append("' ");
                oneMonthAgoHql.append(" and target ='").append(store.getId()).append("' ");
                oneMonthAgoHql.append(" group by type ");
                
                List<?> loneMonth=storeAppraiseDao.list(oneMonthAgoHql.toString(),null);
//                System.out.println("方法111111111111M"+loneMonth);
                //六个月以内的数据
                StringBuffer sixMonthHql=new StringBuffer(" select  type,count(type) as number from Appraise where 1=1 ");
                sixMonthHql.append(" and createTime>='").append(sixMonthAgo).append("' ");
                sixMonthHql.append(" and createTime<='").append(newTime).append("' ");
                sixMonthHql.append(" and target ='").append(store.getId()).append("' ");
                sixMonthHql.append(" group by type ");
                List<?> lsixMonth=storeAppraiseDao.list(sixMonthHql.toString(),null);
//                System.out.println("方法111111111116M"+lsixMonth);
                
                //六个月以前的数据
                StringBuffer sixMonthAgoHql=new StringBuffer(" select  type,count(type) as number from Appraise where 1=1 ");
                sixMonthAgoHql.append(" and createTime<'").append(sixMonthAgo).append("' ");
                sixMonthAgoHql.append(" and target ='").append(store.getId()).append("' ");
                sixMonthAgoHql.append(" group by type ");
                List<?> lsixMonthAgo=storeAppraiseDao.list(sixMonthAgoHql.toString(),null);
                
                //一周的总数量
                int weekAllNumber=0;
                //一月的总数量
                int monthAllNumber=0;
                //六个月的总数量
                int sixMonthAllNumber=0;
                //六个月前的总数量
                int sixMonthAgoAllNumber=0;
                for(int i=1;i<4;i++){
                    AppraiseContDto acd=new AppraiseContDto();
                    
                    
                    //getAppaiseType 根据评价的类型 返回该时间段的评价数量
                    int oneWeekNumber=getAppaiseType(i,loneWeek);
                    int oneMonthNumber=getAppaiseType(i,loneMonth);
                    int sixMonthNumber=getAppaiseType(i,lsixMonth);
                    int sixMonthAgoNumber=getAppaiseType(i,lsixMonthAgo);
                    int allNumber=oneWeekNumber+oneMonthNumber+sixMonthNumber+sixMonthAgoNumber;
                    //设置显示的数据
                    acd.setType(String.valueOf(i));
                    acd.setWeekNumber(oneWeekNumber);
                    acd.setMonthNumber(oneMonthNumber);
                    acd.setSixNumber(sixMonthNumber);
                    acd.setSixAgoNumber(sixMonthAgoNumber);
                    acd.setAllNumber(allNumber);
                    lacd.add(acd);
                    
                    weekAllNumber+=oneWeekNumber;
                    monthAllNumber+=oneMonthNumber;
                    sixMonthAllNumber+=sixMonthNumber;
                    sixMonthAgoAllNumber+=sixMonthAgoAllNumber;
                }
                
              //记录一周以内，一月以内，六个月以内，六个月以外(好中差评)总量
                AppraiseContDto tempAcd=new AppraiseContDto();
                tempAcd.setType("");//好中差评总计
                tempAcd.setWeekNumber(weekAllNumber);
                tempAcd.setMonthNumber(monthAllNumber);
                tempAcd.setSixNumber(sixMonthAllNumber);
                tempAcd.setSixAgoNumber(sixMonthAgoAllNumber);
                tempAcd.setAllNumber(weekAllNumber+monthAllNumber+sixMonthAllNumber+sixMonthAgoAllNumber);
                lacd.add(tempAcd);
                
        } catch (Exception e) {
           log.error(e.getMessage());
        }
        
        return lacd;
        
    }
    
    
    /**
     * 
     * @Title: appraiseCount
     * @Description: TODO(获取评价的类型的统计)
     * 详细业务流程:---------------------------------------------------------------------
     * (详细描述此方法相关的业务处理流程) void 返回类型
     * @throws
     */
    public List<AppraiseContDto> appraiseCount(User user){
        List<AppraiseContDto> lacd = new ArrayList<AppraiseContDto>();
        try {
            //当前的时间
            Date date=new Date();
            //时间格式化
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //时间函数
            Calendar cal = Calendar.getInstance();
            
            //设置当前时间
            cal.setTime(date);
            cal.add(Calendar.WEDNESDAY, -1);
            String oneWeekAgo=sdf.format(cal.getTime());
            
            cal.setTime(date);
            cal.add(Calendar.MONTH, -1);
            String oneMonthAgo=sdf.format(cal.getTime());

            cal.setTime(new Date());
            cal.add(Calendar.MONTH, -6);
            String sixMonthAgo=sdf.format(cal.getTime());
            
            String newTime=sdf.format(date);
            //一周之内的数据
            StringBuffer oneWeekAgoHql = new StringBuffer("select  type,count(type) as number from Appraise where 1=1 ");
            oneWeekAgoHql.append(" and createTime>='").append(oneWeekAgo).append("' ");
            oneWeekAgoHql.append(" and createTime<'").append(newTime).append("' ");
            oneWeekAgoHql.append(" and target='").append(user.getId()).append("' ");
            oneWeekAgoHql.append(" group by type ");
            List<?> loneWeek = userDao.list(oneWeekAgoHql.toString(), null);
            
            //一个月之内的数据
            StringBuffer oneMonthAgoHql=new StringBuffer(" select  type,count(type) as number from Appraise where 1=1 ");
            oneMonthAgoHql.append(" and createTime>='").append(oneMonthAgo).append("' ");
            oneMonthAgoHql.append(" and createTime<'").append(newTime).append("' ");
            oneMonthAgoHql.append(" and target ='").append(user.getId()).append("' ");
            oneMonthAgoHql.append(" group by type ");
            List<?> loneMonth = userDao.list(oneMonthAgoHql.toString(),null);
            
            //六个月以内的数据
            StringBuffer sixMonthHql=new StringBuffer(" select  type,count(type) as number from Appraise where 1=1 ");
            sixMonthHql.append(" and createTime>='").append(sixMonthAgo).append("' ");
            sixMonthHql.append(" and createTime<'").append(newTime).append("' ");
            sixMonthHql.append(" and target ='").append(user.getId()).append("' ");
            sixMonthHql.append(" group by type ");
            List<?> lsixMonth = userDao.list(sixMonthHql.toString(),null);
            
            //六个月以前的数据
            StringBuffer sixMonthAgoHql=new StringBuffer(" select  type,count(type) as number from Appraise where 1=1 ");
            sixMonthAgoHql.append(" and createTime<'").append(sixMonthAgo).append("' ");
            sixMonthAgoHql.append(" and target ='").append(user.getId()).append("' ");
            sixMonthAgoHql.append(" group by type ");
            List<?> lsixMonthAgo = userDao.list(sixMonthAgoHql.toString(),null);
            
            //一周的总数量
            int weekAllNumber=0;
            //一月的总数量
            int monthAllNumber=0;
            //六个月的总数量
            int sixMonthAllNumber=0;
            //六个月前的总数量
            int sixMonthAgoAllNumber=0;
            for(int i=1;i<4;i++){
                AppraiseContDto acd=new AppraiseContDto();
                
                
                //getAppaiseType 根据评价的类型 返回该时间段的评价数量
                int oneWeekNumber=getAppaiseType(i,loneWeek);
                int oneMonthNumber=getAppaiseType(i,loneMonth);
                int sixMonthNumber=getAppaiseType(i,lsixMonth);
                int sixMonthAgoNumber=getAppaiseType(i,lsixMonthAgo);
                int allNumber=oneWeekNumber+oneMonthNumber+sixMonthNumber+sixMonthAgoNumber;
                //设置显示的数据
                acd.setType(String.valueOf(i));
                acd.setWeekNumber(oneWeekNumber);
                acd.setMonthNumber(oneMonthNumber);
                acd.setSixNumber(sixMonthNumber);
                acd.setSixAgoNumber(sixMonthAgoNumber);
                acd.setAllNumber(allNumber);
                lacd.add(acd);
                
                weekAllNumber+=oneWeekNumber;
                monthAllNumber+=oneMonthNumber;
                sixMonthAllNumber+=sixMonthNumber;
                sixMonthAgoAllNumber+=sixMonthAgoAllNumber;
            }
           
            AppraiseContDto acd=new AppraiseContDto();
            acd.setType(String.valueOf(5));
            acd.setWeekNumber(weekAllNumber);
            acd.setMonthNumber(monthAllNumber);
            acd.setSixNumber(sixMonthAllNumber);
            acd.setSixAgoNumber(sixMonthAgoAllNumber);
            acd.setAllNumber(weekAllNumber+monthAllNumber+sixMonthAllNumber+sixMonthAgoAllNumber);
            lacd.add(acd);
             
    } catch (Exception e) {
       log.error(e.getMessage());
    }
    
    return lacd;
    
}
    
    /**
     * 获取评价管理的信息
    * @Title: getMyStoreAppraises
    * @Description: TODO(这里用一句话描述这个方法的作用)
    * @param store
    * @param pager
    * @param productName
    * @param appraiser
    * @param beginDate
    * @param endDate
    * @param status
    * @return List<Appraise> 返回类型
    * @throws
     */
    public List<AppraiseDto> getMyStoreAppraises(String target, Pager pager, String productName, String appraiser,
            String beginDate, String endDate, String replyStatus) {
        
        try {
            StringBuffer hql = new StringBuffer();
                hql.append("select ap.id,u.name,ap.note,ap.replyStatus, p.smallPicture,p.description,p.sortName,p.name,ap.createTime " );
                hql.append(" from Appraise as ap,User as u,Product as p  where 1=1");
                //关联产品
                hql.append(" and ap.productId = p.id");
                //关联用户
                hql.append(" and ap.source = u.id");
               
                
                hql.append(" and ap.target ='").append(target).append("' ");
           
                //不是回复的该字段为""
                //hql.append(" and length(ap.parentId)= 0 ");
            
            //添加查询条件
            Map<String,Object> params = new HashMap<String,Object>();
            if(!StringUtil.isEmpty(productName)){
                hql.append(" and p.name like :productName");
                params.put("productName","%"+productName+"%");
            }
            if(!StringUtil.isEmpty(appraiser)){
                hql.append(" and u.name like :userName");
                params.put("userName","%"+appraiser+"%");
            }
            if(!StringUtil.isEmpty(beginDate)){
                hql.append(" and ap.createTime >= :beginDate");
                params.put("beginDate",beginDate);
            }
            if(!StringUtil.isEmpty(endDate)){
                hql.append(" and ap.createTime <= :endDate");
                params.put("endDate",endDate);
            }
            if(!StringUtil.isEmpty(replyStatus)){
//              boolean booleanReplyStatus=Boolean.parseBoolean(replyStatus);
              hql.append(" and ap.replyStatus = :replyStatus");
              params.put("replyStatus",Boolean.parseBoolean(replyStatus));
            }
            
            //添加排序
            hql.append(" order by ap.createTime DESC");
            
            List<?> list = storeAppraiseDao.list(pager,hql.toString(),params);
            //定义返回的集合
            List<AppraiseDto> lappraiseDto = new ArrayList<AppraiseDto>();
            if(list!=null && list.size()>0){
                for(int i=0;i<list.size();i++){
                    // 定义存储记录的实体
                    AppraiseDto appDto = new AppraiseDto();
                    // 取出当前所遍历的记录
                    Object[] objects = (Object [])list.get(i);
                    //将当前记录存入当前实体
                    appDto.setId((String) objects[0]);
                    //这里将创建者的名字存入了creator
                    appDto.setAppraiseName((String) objects[1]);
                    appDto.setAppraiseContent((String) objects[2]);
                    
                    appDto.setReplyStatus((Boolean) objects[3]);
                    
                    appDto.setProductImgUrl((String) objects[4]);
                    appDto.setProductDescription((String) objects[5]);
                    appDto.setProductSortName((String)objects[6]);
                    appDto.setProductName((String)objects[7]);
                    appDto.setCreateTime((String)objects[8]);
                    //获取回复的信息
                    if(appDto.isReplyStatus()){
                        //根据咨询的id查出回复的id
                        String getReplyHql=" from Appraise where parentId='"+appDto.getId()+"' ";
                        List<?> Reply =storeAppraiseDao.list(getReplyHql, null);
                        if(Reply.size()>0){
                           Appraise reply=(Appraise)Reply.get(0);
                           appDto.setReplyContent(reply.getNote());
                        }
                    }
                    //将当前实体放入返回集合
                    lappraiseDto.add(appDto);
                    System.out.println(lappraiseDto.size());
                    System.out.println("__________________________________________");
                }
            }
            //将结果集返回
            return lappraiseDto;
        }
        catch(Exception e) {
            log.error(e.getMessage());
        }

        return null;
    }
    
    /**
     * 获取评价管理的信息
    * @Title: getUserAppraises
    * @Description: TODO(这里用一句话描述这个方法的作用)
    * @param user--------------------------------------------------------------------------
    * @param pager
    * @param productName
    * @param appraiser
    * @param beginDate
    * @param endDate
    * @param status
    * @return List<Appraise> 返回类型
    * @throws
     */
    public List<AppraiseDto> getUserAppraises(User user, Pager pager, String productName, String appraiser,
            String beginDate, String endDate, String replyStatus) {
        
        try {
            StringBuffer hql = new StringBuffer();
                hql.append("select ap.id,u.id,ap.note,ap.replyStatus, p.picture,p.description,p.sortName " );
                hql.append(" from Appraise as ap,User as u,Product as p  where 1=1");
                //关联产品
                hql.append(" and ap.productId = p.id");
                //关联用户
                hql.append(" and ap.source = u.id");
               
                
                hql.append(" and ap.target ='").append(user.getId()).append("' ");
           
                //不是回复的该字段为""
                hql.append(" and length(ap.parentId)= 0 ");
            
            //添加查询条件
            Map<String,Object> params = new HashMap<String,Object>();
            if(!StringUtil.isEmpty(productName)){
                hql.append(" and p.name like :productName");
                params.put("productName","%"+productName+"%");
            }
            if(!StringUtil.isEmpty(appraiser)){
                hql.append(" and u.id like :user");
                params.put("id","%"+appraiser+"%");
            }
            if(!StringUtil.isEmpty(beginDate)){
                hql.append(" and ap.createTime >= :beginDate");
                params.put("beginDate",beginDate);
            }
            if(!StringUtil.isEmpty(endDate)){
                hql.append(" and ap.createTime <= :endDate");
                params.put("endDate",endDate);
            }
            if(!StringUtil.isEmpty(replyStatus)){
              boolean booleanReplyStatus=Boolean.parseBoolean(replyStatus);
              hql.append(" and ap.replyStatus = :replyStatus");
              params.put("replyStatus",booleanReplyStatus);
            }
            
            //添加排序
            hql.append(" order by ap.createTime DESC");
            
            List<?> list = userDao.list(pager,hql.toString(),params);
            //定义返回的集合
            List<AppraiseDto> lappraiseDto = new ArrayList<AppraiseDto>();
            if(list!=null && list.size()>0){
                for(int i=0;i<list.size();i++){
                    // 定义存储记录的实体
                    AppraiseDto appDto = new AppraiseDto();
                    // 取出当前所遍历的记录
                    Object[] objects = (Object [])list.get(i);
                    //将当前记录存入当前实体
                    appDto.setId((String) objects[0]);
                    //这里将创建者的名字存入了creator
                    appDto.setAppraiseName((String) objects[1]);
                    appDto.setAppraiseContent((String) objects[2]);
                    
                    appDto.setReplyStatus((Boolean) objects[3]);
                    
                    appDto.setProductImgUrl((String) objects[4]);
                    appDto.setProductDescription((String) objects[5]);
                    appDto.setProductSortName((String)objects[6]);
                    
                    //获取回复的信息
                    if(appDto.isReplyStatus()){
                        //根据咨询的id查出回复的id
                        String getReplyHql=" from Appraise where parentId='"+appDto.getId()+"' ";
                        List<?> Reply =userDao.list(getReplyHql, null);
                        if(Reply.size()>0){
                           Appraise reply=(Appraise)Reply.get(0);
                           appDto.setReplyContent(reply.getNote());
                        }
                    }
                    //将当前实体放入返回集合
                    lappraiseDto.add(appDto);
                    System.out.println(lappraiseDto.size());
                    System.out.println("__________________________________________");
                }
            }
            //将结果集返回
            return lappraiseDto;
        }
        catch(Exception e) {
            log.error(e.getMessage());
        }

        return null;
    }


    /**
     * @Title: saveAppraise
     * @Description: TODO(保存 咨询和留言的回复)
     * @param content
     * @param appraiseID
     * @param store
     * @return String 返回类型
     * @throws
      */
    public String saveAppraise(String content,String appraiseID,Store store){
        try {
            //获取 评价人的评价信息 并更改回复 字段
            Appraise hibernateAppraise =(Appraise) storeAppraiseDao.get(appraiseID);
            hibernateAppraise.setReplyStatus(true);
            storeAppraiseDao.updateBean(hibernateAppraise);
            
            Appraise appraise=new Appraise();
            
            
            appraise.setNote(content);
            //默认生效
            appraise.setParentId(appraiseID);
            
            appraise.setCreateTime(DateUtil.getSysDateTimeString());
            storeAppraiseDao.addBean(appraise);
            return "true";
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "false";
        
    }
    
    /**
     * @Title: saveAppraise
     * @Description: TODO(保存 咨询和留言的回复)
     * @param content
     * @param appraiseID-----------------------------------------
     * @param store
     * @return String 返回类型
     * @throws
      */
    public String saveAppraise(String content,String appraiseID,User user){
        try {
            //获取 评价人的评价信息 并更改回复 字段
            Appraise hibernateAppraise =(Appraise) userDao.get(appraiseID);
            hibernateAppraise.setReplyStatus(true);
            userDao.updateBean(hibernateAppraise);
            
            Appraise appraise=new Appraise();
            
            
            appraise.setNote(content);
            //默认生效
            appraise.setParentId(appraiseID);
            
            appraise.setCreateTime(DateUtil.getSysDateTimeString());
            userDao.addBean(appraise);
            return "true";
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "false";
        
    }
    
    
    /**
     * @Title: saveAppraises
     * @Description: TODO(批量 保存 评价的回复)
     * @param content
     * @param appraiseIDS
     * @param store
     * @return String 返回类型
     * @throws
      */
    public String saveAppraises(String content,String appraiseIDS,Store store){
        try {
            //劈开 批量回复的id值
            String[] _appraiseIDS=appraiseIDS.split("@");
            if(_appraiseIDS.length==0){
                return "false";
            }
            String returnValue="true";
            
            for (String appraiseID : _appraiseIDS) {
                //保存回复类容
                returnValue= saveAppraise(content,appraiseID,store);
                //如果有一个保存失败就停止并抛给客户端信息
                if(returnValue=="false"){
                    break;
                }
            }
            return returnValue;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "false";
        
    }
    
    
    /**
     * @Title: saveAppraises
     * @Description: TODO(批量 保存 评价的回复)
     * @param content----------------------------------------
     * @param appraiseIDS
     * @param store
     * @return String 返回类型
     * @throws
      */
    public String saveAppraises(String content,String appraiseIDS,User user){
        try {
            //劈开 批量回复的id值
            String[] _appraiseIDS=appraiseIDS.split("@");
            if(_appraiseIDS.length==0){
                return "false";
            }
            String returnValue="true";
            
            for (String appraiseID : _appraiseIDS) {
                //保存回复类容
                returnValue= saveAppraise(content,appraiseID,user);
                //如果有一个保存失败就停止并抛给客户端信息
                if(returnValue=="false"){
                    break;
                }
            }
            return returnValue;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "false";
        
    }

    /**
     * 根据登录的用户查的属于该用户的评价
     */
    public List<AppraiseDto> getAppraiseBySource(String source, Pager pager){
	StringBuffer sql = new StringBuffer("select a.id,a.note,a.createTime,a.replyStatus,p.smallPicture,p.name");
	sql.append(",u.name from Appraise as a,Product as p,User as u where 1=1");
	sql.append(" and a.productId = p.id");
	sql.append(" and a.source = u.id");
	Map<String,Object> map = new HashMap<String,Object>();
	if(!StringUtil.isEmpty(source)){
	    sql.append(" and a.source = :source");
	    map.put("source",source);
	}
	sql.append(" order by a.createTime desc");
	try{
	   List<Object[]> list = storeAppraiseDao.list(pager,sql.toString(),map);
	   List<AppraiseDto> appraiseDtos = new ArrayList<AppraiseDto>();
	   for(Object[] objects : list){
	       AppraiseDto appDto = new AppraiseDto();
	       appDto.setId((String)objects[0]);
	       appDto.setAppraiseContent((String) objects[1]);
	       appDto.setCreateTime((String)objects[2]);
	       appDto.setReplyStatus((Boolean)objects[3]);
	       appDto.setProductImgUrl((String) objects[4]);
	       appDto.setProductName((String)objects[5]);
	       //这里将创建者的名字存入了creator
	       appDto.setAppraiseName((String) objects[6]);
	       
	     //获取回复的信息
           if(appDto.isReplyStatus()){
               //根据咨询的id查出回复的id
               String getReplyHql=" from Appraise where parentId='"+appDto.getId()+"' ";
               List<?> Reply =userDao.list(getReplyHql, null);
               if(Reply.size()>0){
                  Appraise reply=(Appraise)Reply.get(0);
                  appDto.setReplyContent(reply.getNote());
               }
           }
           //将当前实体放入返回集合
	       appraiseDtos.add(appDto);
	   }
	   return appraiseDtos;
	}
	catch(Exception e){
	    log.error(e.getMessage());
	}
	return null;
    }
    
    
   
    public List<AppraiseDto> getAppraiseByTarget(String target){
        StringBuffer sql = new StringBuffer("select a.id,a.note,a.createTime,a.replyStatus,p.smallPicture,p.name");
        sql.append(",a.target,a.source from Appraise as a,Product as p,User as u where 1=1");
        sql.append(" and a.productId = p.id");
        sql.append(" and a.source = u.id");
        
        Map<String,Object> map = new HashMap<String,Object>();
    	if(!StringUtil.isEmpty(target)){
    		sql.append(" and a.target = :target");
    	    map.put("target",target);
    	}
    	try{
    	    List<Object[]> list = storeAppraiseDao.list(sql.toString(),map);
    	    List<AppraiseDto> appraiseDtos = new ArrayList<AppraiseDto>();
    	   for(Object[] objects : list){
    	       AppraiseDto appDto = new AppraiseDto();
    	       appDto.setId((String)objects[0]);
    	       appDto.setAppraiseContent((String) objects[1]);
    	       appDto.setCreateTime((String)objects[2]);
    	       appDto.setReplyStatus((Boolean)objects[3]);
    	       appDto.setProductImgUrl((String) objects[4]);
    	       appDto.setProductName((String)objects[5]);
    	       //这里将创建者的名字存入了creator
    	       appDto.setAppraiseName((String) objects[6]);
    	       appraiseDtos.add(appDto);
    	   }
    	   return appraiseDtos;
    	}catch(Exception e){
    	    log.error(e.getMessage());
    	}
    	return null;
    }
    
    
    /**
     * 获取评价管理的信息
    * @Title: getMyStoreAppraises
    * @Description: TODO(这里用一句话描述这个方法的作用)
    * @param store
    * @param pager
    * @param productName
    * @param appraiser
    * @param beginDate
    * @param endDate
    * @param status
    * @return List<Appraise> 返回类型
    * @throws
     */
    public List<AppraiseDto> getMyStoreAppraises(Store store, Pager pager, String productName, String appraiser,
            String beginDate, String endDate, String replyStatus) {
        
        try {
            StringBuffer hql = new StringBuffer();
                hql.append("select ap.id,u.name,ap.note,ap.replyStatus, p.picture,p.description,p.sortName,p.name,ap.createTime " );
                hql.append(" from Appraise as ap,User as u,Product as p  where 1=1");
                //关联产品
                hql.append(" and ap.productId = p.id");
                //关联用户
                hql.append(" and ap.source = u.id");
               
                
                hql.append(" and ap.target ='").append(store.getId()).append("' ");
           
                //不是回复的该字段为""
                //hql.append(" and length(ap.parentId)= 0 ");
            
            //添加查询条件
            Map<String,Object> params = new HashMap<String,Object>();
            if(!StringUtil.isEmpty(productName)){
                hql.append(" and p.name like :productName");
                params.put("productName","%"+productName+"%");
            }
            if(!StringUtil.isEmpty(appraiser)){
                hql.append(" and u.name like :userName");
                params.put("userName","%"+appraiser+"%");
            }
            if(!StringUtil.isEmpty(beginDate)){
                hql.append(" and ap.createTime >= :beginDate");
                params.put("beginDate",beginDate);
            }
            if(!StringUtil.isEmpty(endDate)){
                hql.append(" and ap.createTime <= :endDate");
                params.put("endDate",endDate);
            }
            if(!StringUtil.isEmpty(replyStatus)){
//              boolean booleanReplyStatus=Boolean.parseBoolean(replyStatus);
              hql.append(" and ap.replyStatus = :replyStatus");
              params.put("replyStatus",Boolean.parseBoolean(replyStatus));
            }
            
            //添加排序
            hql.append(" order by ap.createTime DESC");
            
            List<?> list = storeAppraiseDao.list(pager,hql.toString(),params);
            //定义返回的集合
            List<AppraiseDto> lappraiseDto = new ArrayList<AppraiseDto>();
            if(list!=null && list.size()>0){
                for(int i=0;i<list.size();i++){
                    // 定义存储记录的实体
                    AppraiseDto appDto = new AppraiseDto();
                    // 取出当前所遍历的记录
                    Object[] objects = (Object [])list.get(i);
                    //将当前记录存入当前实体
                    appDto.setId((String) objects[0]);
                    //这里将创建者的名字存入了creator
                    appDto.setAppraiseName((String) objects[1]);
                    appDto.setAppraiseContent((String) objects[2]);
                    
                    appDto.setReplyStatus((Boolean) objects[3]);
                    
                    appDto.setProductImgUrl((String) objects[4]);
                    appDto.setProductDescription((String) objects[5]);
                    appDto.setProductSortName((String)objects[6]);
                    appDto.setProductName((String)objects[7]);
                    appDto.setCreateTime((String)objects[8]);
                    
                    //获取回复的信息
                    if(appDto.isReplyStatus()){
                        //根据咨询的id查出回复的id
                        String getReplyHql=" from Appraise where parentId='"+appDto.getId()+"' ";
                        List<?> Reply =storeAppraiseDao.list(getReplyHql, null);
                        if(Reply.size()>0){
                           Appraise reply=(Appraise)Reply.get(0);
                           appDto.setReplyContent(reply.getNote());
                        }
                    }
                    //将当前实体放入返回集合
                    lappraiseDto.add(appDto);
                    System.out.println(lappraiseDto.size());
                    System.out.println("__________________________________________");
                }
            }
            //将结果集返回
            return lappraiseDto;
        }
        catch(Exception e) {
            log.error(e.getMessage());
        }

        return null;
    }
    
}
