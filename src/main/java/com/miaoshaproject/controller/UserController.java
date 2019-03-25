package com.miaoshaproject.controller;

import com.miaoshaproject.dataObject.UserDo;
import com.miaoshaproject.dataObject.UserPasswordDo;
import org.apache.commons.lang3.StringUtils;
import com.miaoshaproject.controller.viewobject.UserVo;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.UserModel;
import com.miaoshaproject.utils.CommonReturnType;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@RestController
@RequestMapping("/user")
//@CrossOrigin(allowCredentials = "true",origins = {"*"})   //ajax 跨域
public class UserController extends BaseController {

    private static final String consumes = "application/x-www-form-urlencoded";

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/getUser")
//    @ResponseBody  使用了RestController 可以不用使用  ResponseBody
    public CommonReturnType getUser(@RequestParam(name = "id") Integer id) throws BusinessException {
        //调用service 服务获取对应id的用户对象并返回给前端
        UserModel userModel = userService.getUserById(id);
        if (userModel == null) {
//                userModel.setEncrptPassword("123456");
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        UserVo userVo = convertFromModel(userModel);
        return CommonReturnType.createCommonReturnType(userVo);

    }

    @PostMapping(value = "/getotp")
    public CommonReturnType getOtp(HttpServletRequest request,@RequestParam(name = "telphone") String telphone) {
        //需要按照一定的规则生成OTP验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt += 10000;  //randomInt的值为[10000,109999)
        String otpCode = String.valueOf(randomInt);

        //将OTP验证码同对应用户的手机号关联，使用httpsession的方式绑定他的手机号于OTPCODE
        request.getSession().setAttribute(telphone, otpCode);
        System.out.println(request.getSession());
        //将OTP验证码通过短信通道发送给用户
        System.out.println("telphone = " + telphone + " otpCode = " + otpCode);
        return CommonReturnType.createCommonReturnType(null);
    }

    @PostMapping("/login")
    public CommonReturnType login(@RequestParam(name = "telphone") String telphone, @RequestParam(name = "password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        if (StringUtils.isEmpty(telphone) || StringUtils.isEmpty(password)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        //用户登录服务，用来校验用户登录是否合法
        UserModel userModel = userService.validateLogin(telphone, encodingByMd5(password));
        //登录成功!将登陆凭证加到用户登录成功的session内
        request.getSession().setAttribute("IS_LOGIN", true);
        request.getSession().setAttribute("LOGIN_USER", userModel);
        return CommonReturnType.createCommonReturnType(userModel);
    }

    @PostMapping("/register")
    public CommonReturnType register(HttpServletRequest request,@RequestParam(name = "telphone") String telphone,
                                     @RequestParam(name = "otpCode") String otpCode,
                                     @RequestParam(name = "name") String name,
                                     @RequestParam(name = "age") Integer age,
                                     @RequestParam(name = "gender") Integer gender,
                                     @RequestParam(name = "password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {

        //验证用户手机号与对应otpCode是否相符合
        String inSessionOtpCode = (String) request.getSession().getAttribute(telphone);
        if (!StringUtils.equals(otpCode, inSessionOtpCode)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "短信验证码不符合");
        }
        //用户注册流程
        UserModel userModel = new UserModel();
        userModel.setAge(age);
        userModel.setGender(gender.byteValue());
        userModel.setEncrptPassword(encodingByMd5(password));
        userModel.setName(name);
        userModel.setTelphone(telphone);
        userModel.setRegisterMode("by phone");
        userService.register(userModel);
        return CommonReturnType.createCommonReturnType(null);
    }

    //进行MD5加密
    public String encodingByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        //加密字符串
        String newStr = base64Encoder.encode(md5.digest(str.getBytes("UTF-8")));
        return newStr;
    }

}
