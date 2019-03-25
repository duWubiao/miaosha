package com.miaoshaproject.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.utils.CommonReturnType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.Buffer;
import java.util.Random;


@RestController
@RequestMapping("/code")
public class VerificationCode {

    //由于是spring进行管理，所以是全局共享的，也叫做单例的
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private Producer defaultKaptcha;

    @GetMapping("/getCode")
    public void getCode() throws IOException {
        BufferedImage bi = new BufferedImage(68, 22, BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.getGraphics();
        Color c = new Color(199, 237, 204);
        g.setColor(c);
        g.fillRect(0, 0, 68, 22);

        char[] ch = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        int len = ch.length, index;
        for (int i = 0; i < 4; i++) {
            index = random.nextInt(len);
            g.setColor(new Color(random.nextInt(88), random.nextInt(255), random.nextInt(255)));
            g.drawString(ch[index] + "", (i * 15) + 3, 18);
            builder.append(ch[index]);
        }
        request.getSession().setAttribute("picCode", builder.toString());
        ImageIO.write(bi, "JPG", response.getOutputStream());
    }

    @GetMapping("/defaultKaptcha")
    public void defaultKaptcha() throws IOException {
        byte[] captcha = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try{
            //生成验证码字符串保存到session中
            String createText = defaultKaptcha.createText();
            String s1 = createText.substring(0,1);
            String s2 = createText.substring(1,2);
            int value = Integer.parseInt(s1) + Integer.parseInt(s2);
            request.getSession().setAttribute("vrifyCode",value);
            //使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage bi = defaultKaptcha.createImage(s1 + "+" + s2 + "=?");
            ImageIO.write(bi,"jpg",out);
        }catch (Exception e) {
            e.printStackTrace();
        }
        //定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
        captcha = out.toByteArray();
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream =
                response.getOutputStream();
        responseOutputStream.write(captcha);
        responseOutputStream.flush();
        responseOutputStream.close();
//        response.setDateHeader("Expires", 0);
//
//        // Set standard HTTP/1.1 no-cache headers.
//        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
//        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
//        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
//        // Set standard HTTP/1.0 no-cache header.
//        response.setHeader("Pragma", "no-cache");
//        // return a jpeg
//        response.setContentType("image/jpeg");
//        // create the text for the image
//        String capText = defaultKaptcha.createText();
//        // store the text in the session
//        //request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
//        //将验证码存到session
//        request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
////        log.info(capText);
//        // create the image with the text
//        BufferedImage bi = defaultKaptcha.createImage(capText);
//        ServletOutputStream out = response.getOutputStream();
//        // write the data out
//        ImageIO.write(bi, "jpg", out);
//        try {
//            out.flush();
//        } finally {
//            out.close();
//        }
    }

    @PostMapping("/loginCode")
    public CommonReturnType loginCode(@RequestParam(name = "checkCode") String checkCode) throws IOException {
        if (StringUtils.isNoneBlank(checkCode)) {
            Integer picCode = (Integer) request.getSession().getAttribute("vrifyCode");
            if (picCode.equals(Integer.parseInt(checkCode))) {
                return CommonReturnType.createCommonReturnType("验证码输入正确");
            } else {
                return CommonReturnType.createCommonReturnType("验证码输入错误");
            }
        } else {
            return CommonReturnType.createCommonReturnType("验证码不能为空");
        }
    }
}
