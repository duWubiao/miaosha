package com.miaoshaproject.Base64;

import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

public class Base64Test {

    private static String str = "imooc security";

    public static void jdkBase64() throws IOException {
        BASE64Encoder encoder = new BASE64Encoder();
        String encode = encoder.encode(str.getBytes());
        System.out.println("encode: " + encode);
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] bytes = decoder.decodeBuffer(encode);
        System.out.println("decoder: " + new String(bytes));
    }

    public static void commonsCodesBase64() {
        byte[] byteEncode = Base64.encodeBase64(str.getBytes());
        System.out.println("encode: " + new String(byteEncode));

        byte[] bytesDecode = Base64.decodeBase64(byteEncode);
        System.out.println("decode: " + new String(bytesDecode));
    }

    public static void bouncyCastleBase() {

    }

    public static void main(String[] args) {
        try {
//            jdkBase64();
            commonsCodesBase64();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
