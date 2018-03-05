package com.ywl5320.bean;

/**
 * Created by hlwky001 on 2018/1/9.
 */

public class TimeBean {

    private int currSecs;
    private int totalSecs;

    public int getCurrSecs() {
        return currSecs;
    }

    public void setCurrSecs(int currSecs) {
        this.currSecs = currSecs;
    }

    public int getTotalSecs() {
        return totalSecs;
    }

    public void setTotalSecs(int totalSecs) {
        this.totalSecs = totalSecs;
    }

    @Override
    public String toString() {
        return "TimeBean{" +
                "currSecs=" + currSecs +
                ", totalSecs=" + totalSecs +
                '}';
    }
}
