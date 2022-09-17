package com.bjpowernode.setttings.test;

import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.MD5Util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test1 {
    public static void main(String[] args) {

        //验证失效时间
        //失效时间
        String expireTime = "2022-09-05 13:30:00";

        //当前系统时间
        String currentTime = DateTimeUtil.getSysTime();

        int count = expireTime.compareTo(currentTime);
        System.out.println(count);


        System.out.println(MD5Util.getMD5("jyh.231815"));
    }
}
