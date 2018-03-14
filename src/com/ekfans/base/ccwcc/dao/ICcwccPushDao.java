package com.ekfans.base.ccwcc.dao;

import com.ekfans.base.ccwcc.model.CcwccPush;
import com.ekfans.basic.hibernate.dao.IGenericDao;
import com.ekfans.pub.util.Pager;

import java.util.List;

/**
 * Created by liuguoyu on 2017/4/19.
 */
public interface ICcwccPushDao extends IGenericDao {

    /**
     * 查询推送
     *
     * @param pager
     * @param content
     * @param type
     * @param startTime
     * @param endTime
     * @param pushStartTime
     * @param pushEndTime
     * @return
     */
    public List<CcwccPush> getCcwccPushList(Pager pager, String content,String status, String type, String startTime, String endTime, String pushStartTime, String pushEndTime);


    /**
     * 根据ID删除
     *
     * @param ids
     * @return
     */
    public Boolean removeByIds(String[] ids);
}
