package com.ekfans.base.store.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.store.dao.IStoreConsultDao;
import com.ekfans.base.store.model.Consult;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.user.dao.IUserDao;
import com.ekfans.base.user.model.User;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Service
@SuppressWarnings("unchecked")
public class StoreConsultService implements IStoreConsultService {
    // 定义DAO
    @Autowired private IStoreConsultDao storeConsultDao;
    @Autowired private IUserDao userDao;
    private Logger log = LoggerFactory.getLogger(StoreConsultService.class);
    
    /**
     * 根据条件查询出咨询结果
     */
    @Override
    public List<Consult> getConsultByCondition(Store store,String userName,String beginDate,
						   String endDate, String replyStatus, Pager pager) {
	try {
	    StringBuffer hql = new StringBuffer();
        hql.append("select c,p.smallPicture,p.name,u.nickName from Consult as c,User as u,Product as p  where 1=1");
        //关联产品
        hql.append(" and c.productId = p.id");
	    
	    //关联用户
	    hql.append(" and c.creator = u.id");
	    //默认获取 已经审核了的 
	    hql.append(" and c.checkStatus = 1 ");
	    //给哪个商店 的留言或咨询
	    hql.append(" and c.storeId='").append(store.getId()).append("' ");
	    //添加查询条件
	    Map<String,Object> params = new HashMap<String,Object>();
	    if(!StringUtil.isEmpty(userName)){
		hql.append(" and u.name like :userName");
		params.put("userName","%"+userName+"%");
	    }
	    if(!StringUtil.isEmpty(beginDate)){
		hql.append(" and c.createTime >= :beginDate");
		params.put("beginDate",beginDate);
	    }
	    if(!StringUtil.isEmpty(endDate)){
		hql.append(" and c.createTime <= :endDate");
		params.put("endDate",endDate);
	    }
	    if(!StringUtil.isEmpty(replyStatus)){
	      boolean booleanReplyStatus=Boolean.parseBoolean(replyStatus);
		  hql.append(" and c.replyStatus = :replyStatus");
		  params.put("replyStatus",booleanReplyStatus);
	    }
	    
	    //添加排序
	    hql.append(" order by c.createTime DESC");
	    
	    List<?>list = storeConsultDao.list(pager,hql.toString(),params);
	    //定义返回的集合
            List<Consult> storeConsults = new ArrayList<Consult>();
	    if(list!=null && list.size()>0){
    		for(int i=0;i<list.size();i++){
    		    // 取出当前所遍历的记录
    		    Object[] objects = (Object [])list.get(i);
    		    // 定义存储记录的实体
                Consult consult = (Consult) objects[0];
                // 设置商品名
                consult.setProductName((String) objects[2]);
                // 设置商品图
                consult.setProductPicture((String) objects[1]);
    		    // 设置咨询人
    		    consult.setCreatorName((String) objects[3]);
    		    //获取回复的信息
    		    if(consult.isReplyStatus()){
    		        //根据咨询的id查出回复的id
    		        String getReplyConsultHql=" from Consult where parentId='"+consult.getId()+"' ";
    		        List<?>ReplyConsult=storeConsultDao.list(getReplyConsultHql, null);
    		        if(ReplyConsult.size()>0){
    		           Consult reply=(Consult)ReplyConsult.get(0);
    		           // 设置回复内容
    		           consult.setReplyContent(reply.getQuestionAnswer());
    		        }
    		    }
    		    
    		    consult.setCreateTime(consult.getCreateTime().substring(0, 11));
    		    
    		    //将当前实体放入返回集合
    		    storeConsults.add(consult);
    		}
	    }
	    //将结果集返回
        return storeConsults;
	}
	catch(Exception e) {
	    log.error(e.getMessage());
	}
	return null;
    }

    /**
     * 根据条件查询出留言结果
     */
    @Override
    public List<Consult> getLeaveByCondition(String userName,
						  String beginDate,String endDate,
						  String status,Pager pager) {
	try {
	    StringBuffer hql = new StringBuffer("select sc.id,u.name,sc.questionAnswer," +
	    		"sc.status,sc.type,sc.consultType,s.storeName,sc.createTime" +
	    		" from consult as sc,User as u,Store as s" +
	    		" where 1=1");
	    // 添加关联条件
	    hql.append(" and sc.creator = u.id");
	    hql.append(" and sc.storeId = s.id");
	    
	    // 留言是的consultType为:1
	    hql.append(" and sc.consultType = '1'");
	    
	    //添加查询条件
	    Map<String,Object> params = new HashMap<String,Object>();
	    if(!StringUtil.isEmpty(userName)){
		hql.append(" and u.name like :userName");
		params.put("userName","%"+userName+"%");
	    }
	    if(!StringUtil.isEmpty(beginDate)){
		hql.append(" and sc.createTime >= :beginDate");
		params.put("beginDate",beginDate);
	    }
	    if(!StringUtil.isEmpty(endDate)){
		hql.append(" and sc.createTime <= :endDate");
		params.put("endDate",endDate);
	    }
	    if(!StringUtil.isEmpty(status)){
		hql.append(" and sc.status = :status");
		params.put("status",status);
	    }
	    //添加排序
	    hql.append(" order by sc.createTime DESC");
	    
	    List<?> list = storeConsultDao.list(pager,hql.toString(),params);
	    if(list!=null && list.size()>0){
		//定义返回的集合
		List<Consult> storeConsults = new ArrayList<Consult>();
		for(int i=0;i<list.size();i++){
		    // 定义存储记录的实体
		    Consult consult = new Consult();
		    // 取出当前所遍历的记录
		    Object[] objects = (Object [])list.get(i);
		    //将当前记录存入当前实体
		    consult.setId((String) objects[0]);
		    consult.setCreator((String) objects[1]);//这里将创建者的名字存入了creator
		    consult.setQuestionAnswer((String) objects[2]);
		    consult.setStatus((String) objects[3]);
		    consult.setType((String) objects[4]);
		    consult.setConsultType((String) objects[5]);
		    consult.setStoreId((String) objects[6]);//这里讲店铺的名字存入了storeId
		    consult.setCreateTime((String) objects[7]);
		    
		    //将当前实体放入返回集合
		    storeConsults.add(consult);
		}
		//将结果集返回
		return storeConsults;
	    }
	}catch(Exception e) {
	    log.error(e.getMessage());
	}
	return null;
    }
    
    
    /**
     * @Title: saveConsult
     * @Description: TODO(保存 咨询和留言的回复)
     * @param content
     * @param consultID
     * @param store
     * @return String 返回类型
     * @throws
      */
    public String saveConsult(String content,String consultID,Store store){
        try {
            Consult hibernateConsult =(Consult) storeConsultDao.get(consultID);
            hibernateConsult.setReplyStatus(true);
            storeConsultDao.updateBean(hibernateConsult);
            
            Consult consult=new Consult();
            //user.id==store.id
            consult.setCreator(store.getId());
            consult.setQuestionAnswer(content);
            //默认生效
            consult.setStatus("1");
            consult.setStoreId(store.getId());
            consult.setParentId(consultID);
            consult.setCheckStatus(1);
            storeConsultDao.addBean(consult);
            return "true";
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "false";
        
    }
    
    /**
     * @Title: saveConsult
     * @Description: TODO(批量 保存 咨询和留言的回复)
     * @param content
     * @param consultID
     * @param store
     * @return String 返回类型
     * @throws
      */
    public String saveConsults(String content,String consultIDS,Store store){
        try {
            //劈开 批量回复的id值
            String[] _consultIDS=consultIDS.split("@");
            if(_consultIDS.length==0){
                return "false";
            }
            String returnValue="true";
            
            for (String consultID : _consultIDS) {
                //保存回复类容
                returnValue= saveConsult(content,consultID,store);
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
     * 根据创建者取得该创建者的所有咨询以及得到的回复
     */
    public List<Consult> getConsultByCondition(Pager pager,String creator
    		,String productName,String beginDate
    		,String endDate,String replyStatus){
	StringBuffer sql = new StringBuffer("select u.name,cs.checkStatus,cs.questionAnswer,p.name,s.storeName,cs.id,p.picture,cs.checkNote,cs.createTime,cs.productId,cs.storeId");
        sql.append(" from Consult as cs,Store as s,Product as p,User as u where 1=1");
	sql.append(" and cs.creator = u.id");
	sql.append(" and cs.productId = p.id");
	sql.append(" and cs.storeId = s.id");
	Map<String,Object> map = new HashMap<String,Object>();
	if(!StringUtil.isEmpty(creator)){
	    sql.append(" and cs.creator = :creator");
	    map.put("creator",creator);
	}
	if(!StringUtil.isEmpty(productName)){
	    sql.append(" and p.name like :name");
	    map.put("name","%"+productName+"%");
	}
	if(!StringUtil.isEmpty(beginDate)){
		sql.append(" and cs.createTime >= :beginDate");
		map.put("beginDate",beginDate);
	    }
	    if(!StringUtil.isEmpty(endDate)){
		sql.append(" and cs.createTime <= :endDate");
		map.put("endDate",endDate);
	}
    if(!StringUtil.isEmpty(replyStatus)){
		boolean booleanReplyStatus=Boolean.parseBoolean(replyStatus);
		sql.append(" and cs.replyStatus = :replyStatus");
		map.put("replyStatus",booleanReplyStatus);
    }
	sql.append(" order by cs.createTime DESC");
	try {
	    List<Object[]> list = storeConsultDao.list(pager,sql.toString(),map);
	    List<Consult>  consults = new ArrayList<Consult>();
	    for(Object[] object:list){
		Consult consult = new Consult();
		consult.setCreatorName((String)object[0]);
		consult.setCheckStatus((Integer) object[1]);
		consult.setQuestionAnswer((String)object[2]);
		consult.setProductName((String)object[3]);
		consult.setStoreName((String)object[4]);
		consult.setId((String)object[5]);
		consult.setProductPicture((String) object[6]);
		consult.setCheckNote((String) object[7]);
		consult.setCreateTime((String) object[8]);
		consult.setProductId((String) object[9]);
		consult.setStoreId((String) object[10]);
		consults.add(consult);
	    }
	    return consults;
	}
	catch(Exception e)
	{
	    log.error(e.getMessage());
	}
	return null;
    }

    /**
     * 根据userId取得该用户下的留言
     */
    @Override
    public List<Consult> getConsultByUserId(String creator,String storeId,String beginDate,String endDate,String replyStatus,Pager pager){
	StringBuffer sql = new StringBuffer("select c.creator,c.createTime,c.replyStatus,s.storeName,c.questionAnswer,c.id");
	sql.append(" from Consult as c, Store as s,User as u where 1=1");
	sql.append(" and c.creator = u.id");
	sql.append(" and c.storeId = s.id");
	Map<String,Object> map = new HashMap<String,Object>();
	if(!StringUtil.isEmpty(creator)){
	    sql.append(" and c.creator = :creator");
	    map.put("creator",creator);
	}
	if(!StringUtil.isEmpty(storeId)){
	    sql.append(" and s.storeName like :storeName");
	    map.put("storeName","%"+storeId+"%");
	}
	if(!StringUtil.isEmpty(beginDate)){
	    sql.append(" and c.createTime >= :beginDate");
	    map.put("beginDate",beginDate);
	}
	if(!StringUtil.isEmpty(endDate)){
	    sql.append(" and c.createTime <= :endDate");
	    map.put("endDate",endDate);
	}
	if(!StringUtil.isEmpty(replyStatus)){
	    boolean booleanReplyStatus=Boolean.parseBoolean(replyStatus);
	    sql.append(" and c.replyStatus = :replyStatus");
	    map.put("replyStatus",booleanReplyStatus);
	}
	sql.append(" and c.consultType = 1");
	//添加排序
	sql.append(" order by c.createTime DESC");
	try{
	    List<Object[]> list = storeConsultDao.list(pager,sql.toString(),map);
	    List<Consult> consults = new ArrayList<Consult>();
	    for(Object[] objects : list){
		Consult consult = new Consult();
		consult.setCreator((String)objects[0]);
		consult.setCreateTime((String)objects[1]);
		consult.setReplyStatus((Boolean)objects[2]);
		consult.setStoreId((String)objects[3]);
		consult.setQuestionAnswer((String)objects[4]);
		consult.setId((String)objects[5]);
		consults.add(consult);
	    }
	    return consults;
	}catch(Exception e){
	    log.error(e.getMessage());
	}
	return null;
    }
    
    /**
     * 根据父类Id取得留言的回复
     */
    @Override
    public List<Consult> getByParentId(String parentId){
	StringBuffer sql = new StringBuffer("select c from Consult as c where 1=1");	
	Map<String,Object> map = new HashMap<String,Object>();
	if(!StringUtil.isEmpty(parentId)){
	    sql.append(" and c.parentId = :parentId");
	    map.put("parentId",parentId);
	}
	try{
	    List<Consult> list = storeConsultDao.list(sql.toString(),map);
	    return list;
	}catch(Exception e){
	    log.error(e.getMessage());
	}
	return null;
    }

    /**
     * 得到满足条件咨询或留言的列表
     */
    @Override
    public List<Consult> getConsultListByCondition(String content,String person,
						   String beginDate,String endDate,
						   String consultType,Pager pager) {
	try {
	    StringBuffer hql = new StringBuffer("select c.id,c.creator,c.questionAnswer,c.status,c.replyStatus,c.type,c.consultType,");
	    hql.append(" c.createTime,c.parentId,c.checkMan,c.checkTime,c.checkStatus,c.checkNote,s.storeName,p.name,u.name");
	    hql.append(" from Consult as c,User as u,Store as s,Product as p");
	    hql.append(" where 1=1");
	    // 关联关系
	    hql.append(" and c.creator = u.id");
	    hql.append(" and c.storeId = s.id");
	    hql.append(" and c.productId = p.id");
	    
	    Map<String,Object> params = new HashMap<String,Object>();
	    if(!StringUtil.isEmpty(content)){
		hql.append(" and c.questionAnswer like :content");
		params.put("content","%"+content+"%");
	    }
	    if(!StringUtil.isEmpty(person)){
		hql.append(" and u.name = :person");
		params.put("person",person);
	    }
	    if(!StringUtil.isEmpty(beginDate) && !StringUtil.isEmpty(endDate)){
		hql.append(" and c.createTime between :beginDate and :endDate");
		params.put("beginDate",beginDate);
		params.put("endDate",endDate);
	    }
	    if(!StringUtil.isEmpty(consultType)){
		hql.append(" and c.consultType = :consultType");
		params.put("consultType",consultType);
	    }
	    // 只查询出未审核的咨询、留言
	    hql.append(" and c.checkStatus = 0");
	    hql.append(" order by c.createTime DESC");
	    
	    List<Object[]> consults = 
		storeConsultDao.list(pager,hql.toString(),params);
	    if(consults != null && consults.size() >0){
		List<Consult> returnCS = new ArrayList<Consult>();
		for(int i=0;i<consults.size();i++){
		    Consult consult = new Consult();
		    Object[] objArray = consults.get(i);
		    consult.setId((String) objArray[0]);
		    consult.setCreator((String) objArray[1]);
		    consult.setQuestionAnswer((String) objArray[2]);
		    consult.setStatus((String) objArray[3]);
		    consult.setReplyStatus((Boolean) objArray[4]);
		    consult.setType((String) objArray[5]);
		    consult.setConsultType((String) objArray[6]);
		    consult.setCreateTime((String) objArray[7]);
		    consult.setParentId((String) objArray[8]);
		    consult.setCheckMan((String) objArray[9]);
		    consult.setCheckTime((String) objArray[10]);
		    consult.setCheckStatus((Integer) objArray[11]);
		    consult.setCheckNote((String) objArray[12]);
		    consult.setStoreName((String) objArray[13]);
		    consult.setProductName((String) objArray[14]);
		    consult.setCreatorName((String) objArray[15]);
		    
		    User u = StoreConsultService.getUserById((String)objArray[9],userDao,log);
		    if(u != null){
			consult.setCheckManName(u.getName());
		    }
		    
		    returnCS.add(consult);
		}
		return returnCS;
	    }
	    
	}
	catch(Exception e) {
	    log.error(e.getMessage());
	}
	return null;
    }
    private static User getUserById(String userId,IUserDao mydao,Logger mylog){
	try {
	    if(StringUtil.isEmpty(userId)){
		return null;
	    }
	    return (User)mydao.get(userId);
	}
	catch(Exception e) {
	    mylog.error(e.getMessage());
	}
	return null;
    }

    /**
     * 获得咨询详情
     */
    @Override
    public Consult getConsultDetailByConsultId(String consultId) {
	try {
	    if(StringUtil.isEmpty(consultId)){
		return null;
	    }
	    StringBuffer hql = new StringBuffer("select c.id,c.creator,c.questionAnswer,c.status,c.replyStatus,c.type,c.consultType,");
	    hql.append(" c.createTime,c.parentId,c.checkMan,c.checkTime,c.checkStatus,c.checkNote,s.storeName,p.name,u.name");
	    hql.append(" from Consult as c,User as u,Store as s,Product as p");
	    hql.append(" where 1=1");
	    // 关联关系
	    hql.append(" and c.creator = u.id");
	    hql.append(" and c.storeId = s.id");
	    hql.append(" and c.productId = p.id");
	    Map<String,Object> params = new HashMap<String,Object>();
	    hql.append(" and c.id = :consultId");
	    params.put("consultId",consultId);
	    
	    List<Object[]> consults = 
		storeConsultDao.list(hql.toString(),params);
	    if(consults != null && consults.size() == 1){
		Consult consult = new Consult();
		Object[] objArray = consults.get(0);
		consult.setId((String) objArray[0]);
		consult.setCreator((String) objArray[1]);
		consult.setQuestionAnswer((String) objArray[2]);
		consult.setStatus((String) objArray[3]);
		consult.setReplyStatus((Boolean) objArray[4]);
		consult.setType((String) objArray[5]);
		consult.setConsultType((String) objArray[6]);
		consult.setCreateTime((String) objArray[7]);
		consult.setParentId((String) objArray[8]);
		consult.setCheckMan((String) objArray[9]);
		consult.setCheckTime((String) objArray[10]);
		consult.setCheckStatus((Integer) objArray[11]);
		consult.setCheckNote((String) objArray[12]);
		consult.setStoreName((String) objArray[13]);
		consult.setProductName((String) objArray[14]);
		consult.setCreatorName((String) objArray[15]);
		    
		User u = StoreConsultService.getUserById((String)objArray[9],userDao,log);
		if(u != null){
		    consult.setCheckManName(u.getName());
		}
		return consult;
	    }
	}
	catch(Exception e) {
	    log.error(e.getMessage());
	}
	return null;
    }

    /**
     * 内容审核
     */
    @Override
    public void checkConsult(String consultId,String checkMan,Integer checkStatus,String checkNote) {
	try {
	    if(StringUtil.isEmpty(consultId)){
		return;
	    }
	    Consult consult = (Consult)storeConsultDao.get(consultId);
	    consult.setCheckMan(checkMan);
	    consult.setCheckStatus(checkStatus);
	    consult.setCheckNote(checkNote);
	    consult.setCheckTime(DateUtil.getSysDateTimeString());
	    
	    storeConsultDao.updateBean(consult);
	}
	catch(Exception e) {
	    log.error(e.getMessage());
	}
    }

}
