package com.blue.corelib.utils

import com.blue.corelib.bean.LoginUser
import com.newcw.component.utils.ConfigManager

object CacheDemo {
    /**
     * 获取当前登录的用户
     */
    private var user: LoginUser? = null

    /**
     * 获取登录用户
     */
    fun getLoginUser(): LoginUser? {
        if (user == null) {
            var res: String = ConfigManager.getStringValue(ConstantInfoUtils.LOGIN_USER, "")
            if (res.isNotEmpty()) {
                user = JsonUtil.fromJson<LoginUser>(res, LoginUser::class.java) as LoginUser
            }
        }
        return user
    }

    fun setLoginUser(user: LoginUser?) {
        CacheDemo.user = user
        if (user != null) {
            ConfigManager.setAnyValue(ConstantInfoUtils.LOGIN_USER, user)
        } else {
            ConfigManager.setAnyValue(ConstantInfoUtils.LOGIN_USER, "")
        }
    }

    /**
     * 获取当前登录的用户
     */
  /*  private var positionMoveInfoBean: PositionMoveInfoBean? = null

    *//**
     * 获取位置移动数据
     *//*
    fun getLPositionMoveInfoBean(): PositionMoveInfoBean? {
        if (positionMoveInfoBean == null) {
            var res: String = ConfigManager.getStringValue(ConstantUtils.POSITION_MOVE_INFO, "")
            if (res.isNotEmpty()) {
                positionMoveInfoBean = JsonUtil.fromJson<PositionMoveInfoBean>(res, PositionMoveInfoBean::class.java) as PositionMoveInfoBean
            }
        }
        return positionMoveInfoBean
    }

    fun setPositionMoveInfoBean(positionMoveInfoBean: PositionMoveInfoBean?) {
        Cache.positionMoveInfoBean = positionMoveInfoBean
        if (positionMoveInfoBean != null) {
            ConfigManager.setAnyValue(ConstantUtils.POSITION_MOVE_INFO, positionMoveInfoBean)
        } else {
            ConfigManager.setAnyValue(ConstantUtils.POSITION_MOVE_INFO, "")
        }
    }
*/
    /**
     * 获取登录Token
     */
    fun getToken(): String? {
        return getLoginUser()?.token
    }

    /**
     * 退出登录 清除user
     */
    fun clearLoginUser() {
        user = null
        ConfigManager.setAnyValue(ConstantInfoUtils.LOGIN_USER, "")
    }


}