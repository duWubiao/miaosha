package com.miaoshaproject.service;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.model.UserModel;

public interface UserService {

    //通过用户ID获取对象
    UserModel getUserById(Integer id);

    void register(UserModel userModel) throws BusinessException;

    /**
     * telphone:用户注册手机
     * password:用户加密后的密码
     * @param telphone
     * @param encrptPassword
     * @throws BusinessException
     */
    UserModel validateLogin(String telphone, String encrptPassword) throws BusinessException;
}
