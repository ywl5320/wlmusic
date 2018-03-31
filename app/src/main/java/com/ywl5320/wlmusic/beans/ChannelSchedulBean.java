package com.ywl5320.wlmusic.beans;

import com.ywl5320.wlmusic.base.BaseBean;

import java.util.List;

/**
 * Created by ywl on 2018-1-13.
 */

public class ChannelSchedulBean extends BaseBean {

    private String id;
    private String ondemandId;
    private int timing;
    private String name;
    private String anchor;
    private String start;
    private String duration;
    private String end;
    private List<StreamsBean> streams;
    private String downLoadUrl;
    private String programName;
    private int programCount;
    private String orderNum;
    private String shareUrl;
    private String img;

    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOndemandId() {
        return ondemandId;
    }

    public void setOndemandId(String ondemandId) {
        this.ondemandId = ondemandId;
    }

    public int getTiming() {
        return timing;
    }

    public void setTiming(int timing) {
        this.timing = timing;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public List<StreamsBean> getStreams() {
        return streams;
    }

    public void setStreams(List<StreamsBean> streams) {
        this.streams = streams;
    }

    public String getDownLoadUrl() {
        return downLoadUrl;
    }

    public void setDownLoadUrl(String downLoadUrl) {
        this.downLoadUrl = downLoadUrl;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public int getProgramCount() {
        return programCount;
    }

    public void setProgramCount(int programCount) {
        this.programCount = programCount;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
