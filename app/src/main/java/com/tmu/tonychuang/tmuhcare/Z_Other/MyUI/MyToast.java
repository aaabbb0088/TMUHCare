package com.tmu.tonychuang.tmuhcare.Z_Other.MyUI;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by TonyChuang on 2016/6/30.
 */
public class MyToast {
    public MyToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
