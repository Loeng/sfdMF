package com.ekfans.base.user.model;

import com.ekfans.basic.hibernate.model.BasicBean;

import java.math.BigDecimal;

/**
 * 会员基础信息--实体类
 *
 * @author ek_lq
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 * Company:成都易科远见科技有限公司 www.ekfans.com
 * @ClassName: Store
 * @Description:
 * @date 2014-9-20 上午11:55:14
 */
public class User extends BasicBean {
    // ID头：用于多表唯一性
    public static final String SINGLE_MARK = "MU";

    private static final long serialVersionUID = 1L;
    // 昵称
    private String nickName = "";
    // 用户名
    private String name = "";
    // 密码
    private String password = "";
    // 密码强度(1:弱,2:中,3:强)
    private String passwordStrong = "";
    // 会员类型（1：产生企业，2：采购商，3：处置企业,4:运输企业,5:供应商）
    private String type = "0";
    // 用户等级ID -- 对应会员等级表(UserLevel)主键
    private String levelId = "";
    // 会员状态（0：禁用，1：启用，2：删除）
    private Integer status = 0;
    // 激活状态（true：以激活，false：未激活）
    private Boolean verificationStatus = false;
    // 激活码
    private String verificationCode = "";
    // 存放临时邮箱,未激活的
    private String emailValiDate = "";
    // 用户头像
    private String headPortrait = "";
    // 会员积分
    private Double integration = 0.00;
    // 手机号（找回账号密码）
    private String mobile = "";
    //物流宝用户token
    private String wlbToken = "";
    // 邮箱（找回账号密码）
    private String email = "";
    // 安全手机（找资金安全账户密码）
    private String safeMobile = "";
    // 安全密码
    private String safePassword = "";
    // 账户余额
    private BigDecimal viratual = new BigDecimal(0.00);
    // 上次登陆时间
    private String lastLoginTime = "";
    // 总登陆次数
    private Integer loginNum = 0;
    // 创建时间
    private String createTime = "";
    // 更新时间
    private String updateTime = "";
    // 身份证号(个人会员)
    private String cardNumber = "";
    // 是否关联银行卡（true:关联，false:没有关联）
    private Boolean isAssociatedBank = false;
    // 数据是否被第三方使用
    private boolean useData = false;
    // 环信ID
    private String hxUserName = "";
    // 环信PWD
    private String hxPassWord = "";
    //极光推送的设备唯一性标识
    private String registrationID = "";

    

    // 向所有人显示电话开关(0 不显示 1显示)
    private boolean friendPhoneSwitch;
    // 向好友显示电话开关(0 不显示 1显示)
    private boolean allPhoneSwitch;
    // 添加好友验证开关(0 不验证 1验证)
    private boolean friendValidSwitch;
    // 0为正式用户，1为待完善基本信息的游客
    private String touristType;
    //用于给建行支付的用户ID
    private String ccbUserId;

    // ======================== get set ========================

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmailValiDate() {
        return emailValiDate;
    }

    public void setEmailValiDate(String emailValiDate) {
        this.emailValiDate = emailValiDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordStrong() {
        return passwordStrong;
    }

    public void setPasswordStrong(String passwordStrong) {
        this.passwordStrong = passwordStrong;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(Boolean verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public Double getIntegration() {
        return integration;
    }

    public void setIntegration(Double integration) {
        this.integration = integration;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSafeMobile() {
        return safeMobile;
    }

    public void setSafeMobile(String safeMobile) {
        this.safeMobile = safeMobile;
    }

    public String getSafePassword() {
        return safePassword;
    }

    public void setSafePassword(String safePassword) {
        this.safePassword = safePassword;
    }

    public BigDecimal getViratual() {
        return viratual;
    }

    public void setViratual(BigDecimal viratual) {
        this.viratual = viratual;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getLoginNum() {
        return loginNum;
    }

    public void setLoginNum(Integer loginNum) {
        this.loginNum = loginNum;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Boolean getIsAssociatedBank() {
        return isAssociatedBank;
    }

    public void setIsAssociatedBank(Boolean isAssociatedBank) {
        this.isAssociatedBank = isAssociatedBank;
    }

    public boolean getUseData() {
        return useData;
    }

    public void setUseData(boolean useData) {
        this.useData = useData;
    }

    public String getHxUserName() {
        return hxUserName;
    }

    public void setHxUserName(String hxUserName) {
        this.hxUserName = hxUserName;
    }

    public String getHxPassWord() {
        return hxPassWord;
    }

    public void setHxPassWord(String hxPassWord) {
        this.hxPassWord = hxPassWord;
    }

    public boolean getFriendPhoneSwitch() {
        return friendPhoneSwitch;
    }

    public void setFriendPhoneSwitch(boolean friendPhoneSwitch) {
        this.friendPhoneSwitch = friendPhoneSwitch;
    }

    public boolean getAllPhoneSwitch() {
        return allPhoneSwitch;
    }

    public void setAllPhoneSwitch(boolean allPhoneSwitch) {
        this.allPhoneSwitch = allPhoneSwitch;
    }

    public boolean getFriendValidSwitch() {
        return friendValidSwitch;
    }

    public void setFriendValidSwitch(boolean friendValidSwitch) {
        this.friendValidSwitch = friendValidSwitch;
    }

    public String getTouristType() {
        return touristType;
    }

    public void setTouristType(String touristType) {
        this.touristType = touristType;
    }

    public String getWlbToken() {
        return wlbToken;
    }

    public void setWlbToken(String wlbToken) {
        this.wlbToken = wlbToken;
    }

    public String getRegistrationID() {
        return registrationID;
    }

    public void setRegistrationID(String registrationID) {
        this.registrationID = registrationID;
    }

    public String getCcbUserId() {
        return ccbUserId;
    }

    public void setCcbUserId(String ccbUserId) {
        this.ccbUserId = ccbUserId;
    }


    
}
