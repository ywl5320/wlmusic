package com.ywl5320.wlmusic.http.service;


/**
 * Created by ywl on 2016/5/19.
 */
public class ExceptionApi extends RuntimeException{

    private int code;
    private String msg;

    public ExceptionApi(int resultCode, String msg) {
        this.code = resultCode;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
