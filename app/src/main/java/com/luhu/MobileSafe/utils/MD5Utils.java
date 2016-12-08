package com.luhu.MobileSafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by LuHu on 2016/9/27.
 */
public class MD5Utils {
    /**
     * MD5加密方法
     * @param password
     * @return
     */
    public static String md5Password(String password){
        MessageDigest digest= null;
        try {
            digest = MessageDigest.getInstance("md5");
            byte[] result=digest.digest(password.getBytes());
            StringBuffer buffer=new StringBuffer();
            //把每一个byte做一个与运算0xff
            for (byte b:result){
                //与运算
                int number=b&0xff;
                String str=Integer.toHexString(number);
                System.out.println(str);
                if(str.length()==1){
                    buffer.append("0");
                }
                buffer.append(str);
            }
            //标识的md5加密后的结果
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }

    }

}

