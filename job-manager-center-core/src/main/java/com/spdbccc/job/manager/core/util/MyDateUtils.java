package com.spdbccc.job.manager.core.util;

import lombok.Synchronized;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDateUtils {
    public final static SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd_HHmmssSSS");

    @Synchronized
    public static String getNowTime(){
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return sdf.format(new Date());
    }
}
