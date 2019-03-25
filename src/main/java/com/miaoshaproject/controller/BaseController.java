package com.miaoshaproject.controller;

import com.miaoshaproject.controller.viewobject.UserVo;
import com.miaoshaproject.dataObject.UserPasswordDo;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.service.model.UserModel;
import com.miaoshaproject.utils.CommonReturnType;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
//@CrossOrigin(allowCredentials = "true",origins = {"*"})
public class BaseController {

    //定义ExceptionHandler解决未被controller层吸收的Exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletResponse response,Exception ex) {
        Map<String,Object> responseData = new HashMap<>();
        if(ex instanceof BusinessException) {
            BusinessException businessException = (BusinessException) ex;
            responseData.put("errorCode",businessException.getErrorCode());
            responseData.put("errorMsg",businessException.getErrorMsg());
        }else {
            responseData.put("errorCode", EmBusinessError.UNKNOWN_ERROR.getErrorCode());
            responseData.put("errorMsg",EmBusinessError.UNKNOWN_ERROR.getErrorMsg());
        }
//        Throwable throwable = ex.getCause();
//        if(throwable != null) {
//            if(throwable instanceof BusinessException) {
//                BusinessException businessException = (BusinessException) throwable;
//                responseData.put("errorCode",businessException.getErrorCode());
//                responseData.put("errorMsg",businessException.getErrorMsg());
//            }
//        }
        return CommonReturnType.createCommonReturnType(responseData,"fail");
    }

    public UserVo convertFromModel(UserModel model) {
        if (model == null) {
            return null;
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(model, userVo); //第一个参数拷贝的模型，第二个是需要拷贝到的模型
        return userVo;
    }
}
