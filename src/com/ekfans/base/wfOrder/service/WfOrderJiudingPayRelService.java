package com.ekfans.base.wfOrder.service;

import com.ekfans.base.wfOrder.dao.IWfOrderJiudingPayRelDao;
import com.ekfans.base.wfOrder.model.WfOrderJiudingPayRel;
import com.ekfans.plugin.number.NoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WfOrderJiudingPayRelService implements IWfOrderJiudingPayRelService {
    @Autowired
    private IWfOrderJiudingPayRelDao wfOrderJiudingPayRelDao;


    @Override
    public WfOrderJiudingPayRel create(String wfOrderId) {
        try {
            WfOrderJiudingPayRel wfOrderJiudingPayRel = new WfOrderJiudingPayRel();
            wfOrderJiudingPayRel.setWfOrderId(wfOrderId);
            wfOrderJiudingPayRel.setId(new NoManager().createWfRelOrderId());

            wfOrderJiudingPayRelDao.addBean(wfOrderJiudingPayRel);
            return wfOrderJiudingPayRel;
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public String getWfOrderIdByRelId(String relId) {
        try {
            WfOrderJiudingPayRel wfOrderJiudingPayRel = (WfOrderJiudingPayRel) wfOrderJiudingPayRelDao.get(relId);
            return wfOrderJiudingPayRel == null ? null : wfOrderJiudingPayRel.getWfOrderId();
        } catch (Exception e) {
        }
        return null;
    }
}
