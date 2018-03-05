package com.ywl5320.wlmusic.beans;


import com.ywl5320.wlmusic.base.BaseBean;

/**
 * Created by ywl on 2017/2/17.
 */

public class EventBusBean extends BaseBean {

    private int type; //1：定位信息 2
    private Object object;

    public EventBusBean(int type, Object object) {
        this.type = type;
        this.object = object;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
