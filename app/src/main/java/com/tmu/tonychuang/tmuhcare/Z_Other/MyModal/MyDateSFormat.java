package com.tmu.tonychuang.tmuhcare.Z_Other.MyModal;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by TonyChuang on 2016/6/20.
 */
public class MyDateSFormat {
    public MyDateSFormat() {
    }

    public static SimpleDateFormat getFrmt_yMd() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    }

    public static SimpleDateFormat getFrmt_yyyyMMdd() {
        return new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
    }

    public static SimpleDateFormat getFrmt_yMdHm() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
    }

    public static SimpleDateFormat getFrmt_Mdahm() {
        return new SimpleDateFormat("M月d日 a h:mm", Locale.getDefault());
    }

    public static SimpleDateFormat getFrmt_E() {
        return new SimpleDateFormat("E", Locale.getDefault());
    }

    public static SimpleDateFormat getFrmt_ahm() {
        return new SimpleDateFormat("a h:mm", Locale.getDefault());
    }

    public static SimpleDateFormat getFrmt_Md() {
        return new SimpleDateFormat("M/d", Locale.getDefault());
    }

    public static SimpleDateFormat getFrmt_GPS_yMd_Clr() {
        return new SimpleDateFormat("yyyy-M-d", Locale.getDefault());
    }

    public static SimpleDateFormat getFrmt_GPS_yMdHms(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    }
}
