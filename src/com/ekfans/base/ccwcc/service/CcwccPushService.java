package com.ekfans.base.ccwcc.service;

import com.ekfans.base.ccwcc.dao.ICcwccPushDao;
import com.ekfans.base.ccwcc.model.CcwccPush;
import com.ekfans.base.ccwcc.util.CcwccPushConst;
import com.ekfans.base.ccwcc.util.CcwccPushHelp;
import com.ekfans.plugin.wuliubao.yunshu.controller.util.JGUtil;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuguoyu on 2017/4/19.
 */
@Service
public class CcwccPushService implements ICcwccPushService {
    @Autowired
    ICcwccPushDao ccwccPushDao;


    @Override
    public boolean saveOrUpdate(CcwccPush ccwccPush, Boolean pushNow, HttpServletRequest request) {
        try {
            if (pushNow) {
                CcwccPushHelp.push(ccwccPush,request);
                ccwccPush.setStatus(CcwccPushConst.PUSH_STATUS_PUSH);
                ccwccPush.setPushTime(DateUtil.getSysDateTimeString());
            }

            if (StringUtil.isEmpty(ccwccPush.getId())) {
                ccwccPush.setCreateTime(DateUtil.getSysDateTimeString());
                ccwccPushDao.addBean(ccwccPush);
            } else {
                ccwccPushDao.updateBean(ccwccPush);
            }
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    @Override
    public boolean remove(String[] ids) {
        return ccwccPushDao.removeByIds(ids);
    }

    @Override
    public List<CcwccPush> getCcwccPushList(Pager pager, String content, String status, String type, String startTime, String endTime, String pushStartTime, String pushEndTime) {
        return ccwccPushDao.getCcwccPushList(pager, content, status, type, startTime, endTime, pushStartTime, pushEndTime);
    }

    /**
     * 推送
     *
     * @param ccwccPush
     * @return
     */
    public boolean push(CcwccPush ccwccPush, HttpServletRequest request) {
        CcwccPushHelp.push(ccwccPush,request);
        ccwccPush.setStatus(CcwccPushConst.PUSH_STATUS_PUSH);
        ccwccPush.setPushTime(DateUtil.getSysDateTimeString());
        try {
            ccwccPushDao.updateBean(ccwccPush);
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    /**
     * 根据ID获取对象
     *
     * @param pushId
     * @return
     */
    @Override
    public CcwccPush getById(String pushId) {
        if (StringUtil.isEmpty(pushId)) {
            return null;
        }
        try {
            return (CcwccPush) ccwccPushDao.get(pushId);
        } catch (Exception e) {

        }

        return null;
    }

	@Override
	public boolean wlbSaveOrUpdate(CcwccPush ccwccPush, Boolean pushNow, HttpServletRequest request) {
		try {

            if (StringUtil.isEmpty(ccwccPush.getId())) {
                ccwccPush.setCreateTime(DateUtil.getSysDateTimeString());
                ccwccPushDao.addBean(ccwccPush);
            } else {
                ccwccPushDao.updateBean(ccwccPush);
            }
            if (pushNow) {
            	//通过极光推送议价消息
            	Map<String,String> dataNode = new HashMap<String,String>();
            	//messageType:"4",为系统推送信息
            	dataNode.put("messageType", "4");
            	dataNode.put("id", ccwccPush.getId());
                dataNode.put("content", ccwccPush.getContent());
            	dataNode.put("pushTime", DateUtil.getSysDateTimeString());
            	//使用极光推送系统消息 
            	JGUtil.wlbSendMessages(dataNode,"您有一条新的系统消息!");
                ccwccPush.setStatus(CcwccPushConst.PUSH_STATUS_PUSH);
                ccwccPush.setPushTime(DateUtil.getSysDateTimeString());
                ccwccPushDao.updateBean(ccwccPush);
            }
            return true;
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return false;
	}
}
