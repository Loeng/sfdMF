package com.ekfans.base.ccwcc.service;

import com.ekfans.base.ccwcc.model.CcwccPush;
import com.ekfans.pub.util.Pager;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by liuguoyu on 2017/4/19.
 */
public interface ICcwccPushService {

    /**
     * 新增或修改推送
     *
     * @param ccwccPush
     * @return
     */
    public boolean saveOrUpdate(CcwccPush ccwccPush, Boolean pushNow, HttpServletRequest request);

    /**
     * 物流宝新增或修改推送
     *@param ccwccPush
     * @return
     */
    public boolean wlbSaveOrUpdate(CcwccPush ccwccPush, Boolean pushNow, HttpServletRequest request);
    /**
     * 删除推送
     * @param ids
     * @return
     */
    public boolean remove(String[] ids);

    /**
     * 查询推送
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
     * 推送
     *
     * @param ccwccPush
     * @return
     */
    public boolean push(CcwccPush ccwccPush, HttpServletRequest request);

    /**
     * 根据ID获取对象
     * @param pushId
     * @return
     */
    public CcwccPush getById(String pushId);
}
