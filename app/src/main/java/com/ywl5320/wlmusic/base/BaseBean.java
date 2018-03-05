package com.ywl5320.wlmusic.base;




import com.ywl5320.wlmusic.util.BeanUtil;

import java.io.Serializable;

/**
 * Created by ywl on 2017/9/4.
 */

public class BaseBean implements Serializable{

    public static final long serialVersionUID = -316172390920775219L;

    @Override
    public String toString() {
        return BeanUtil.bean2string(this);
    }

}
