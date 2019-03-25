package com.miaoshaproject.aspect;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.utils.CommonReturnType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
//@CrossOrigin(
//        origins = "*",
//        allowedHeaders = "*",
//        allowCredentials = "true",
//        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.HEAD}
//)
public class HttpAspect {

    @Autowired
    private HttpServletRequest request;

    private Logger logger = LoggerFactory.getLogger(HttpAspect.class);

    @Pointcut("execution(public * com.miaoshaproject.controller.ItemController.*(..))")
    public void log() {}


    @Before("log()")
    public void before(JoinPoint joinPoint) throws BusinessException {
        //url
        logger.info("url={}", request.getRequestURL());
        //method
        logger.info("method={}", request.getMethod());
        //ip
        logger.info("ip={}",request.getRemoteAddr());
        //类方法
        logger.info("class_method={}", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        //参数
        logger.info("args={}", joinPoint.getArgs());

    }

//    @After("execution(public Object com.miaoshaproject.controller.BaseController.*(..))")
//    public CommonReturnType exceptionAfter(JoinPoint joinPoint) {
//        Object[] objs = joinPoint.getArgs();
//        Object o = null;
//        if(objs.length == 2) {
//            o = objs[1];
//        }
//        BusinessException b = null;
//        if(o instanceof BusinessException) {
//            b = (BusinessException) o;
//        }
//        System.out.println("异常");
//        return CommonReturnType.createCommonReturnType(b.getErrorMsg(),"fail");
//    }


    @After("log()")
    public void after() {
        logger.info("之后");
    }

    @AfterReturning(returning ="object", pointcut = "log()")
    public void afterReturning(Object object){
        logger.info("response={}", object);
    }
}
