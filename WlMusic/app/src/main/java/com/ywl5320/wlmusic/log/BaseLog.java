package com.ywl5320.wlmusic.log;

import android.util.Log;

/**
 */
public class BaseLog {

    public static void printDefault(int type, String tag, String msg) {

        int index = 0;
        int maxLength = 4000;
        int countOfSub = msg.length() / maxLength;

        if (countOfSub > 0) {
            for (int i = 0; i < countOfSub; i++) {
                String sub = msg.substring(index, index + maxLength);
                printSub(type, tag, sub);
                index += maxLength;
            }
            printSub(type, tag, msg.substring(index, msg.length()));
        } else {
            printSub(type, tag, msg);
        }
    }

    private static void printSub(int type, String tag, String sub) {
        switch (type) {
            case MyLog.V:
                Log.v(tag, sub);
                break;
            case MyLog.D:
                Log.d(tag, sub);
                break;
            case MyLog.I:
                Log.i(tag, sub);
                break;
            case MyLog.W:
                Log.w(tag, sub);
                break;
            case MyLog.E:
                Log.e(tag, sub);
                break;
            case MyLog.A:
                Log.wtf(tag, sub);
                break;
        }
    }

}
