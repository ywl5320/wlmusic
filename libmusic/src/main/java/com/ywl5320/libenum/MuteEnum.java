package com.ywl5320.libenum;

/**
 *
 * mutex enum
 * Created by ywl on 2018-3-16.
 */

public enum MuteEnum {

    MUTE_RIGHT("RIGHT", 0),
    MUTE_LEFT("LEFT", 1),
    MUTE_CENTER("CENTER", 2);

    private String mute;
    private int value;

    MuteEnum(String mute, int value)
    {
        this.mute = mute;
        this.value = value;
    }

    public String getMute() {
        return mute;
    }

    public void setMute(String mute) {
        this.mute = mute;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
