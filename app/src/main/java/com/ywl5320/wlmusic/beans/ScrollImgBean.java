package com.ywl5320.wlmusic.beans;

import com.ywl5320.wlmusic.base.BaseBean;

/**
 * Created by ywl on 2017/7/21.
 */

public class ScrollImgBean extends BaseBean {

    private int type;
    private String id;
    private String img;
    private String htmlUrl;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }
}
