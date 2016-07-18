package com.tmu.tonychuang.tmuhcare.Z_Other.CustomTabWebPage;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * Created by TonyChuang on 2016/6/20.
 */
public class KeepAliveService extends Service {
    private static final Binder sBinder = new Binder();

    @Override
    public IBinder onBind(Intent intent) {
        return sBinder;
    }
}
