package com.example;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Demo {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        MessageDigest digest=MessageDigest.getInstance("md5");
        String password="123456";
        byte[] result=digest.digest(password.getBytes());
        StringBuffer buffer=new StringBuffer();
        for (byte b:result){
            int number=b&0xff;
            String str=Integer.toHexString(number);
            System.out.println(str);
            if(str.length()==1){
                buffer.append("0");
            }
            buffer.append(str);
        }
        System.out.println(buffer.toString());
    }

}
