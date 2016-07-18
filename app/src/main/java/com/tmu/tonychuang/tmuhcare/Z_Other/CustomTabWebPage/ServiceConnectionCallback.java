package com.tmu.tonychuang.tmuhcare.Z_Other.CustomTabWebPage;

import android.support.customtabs.CustomTabsClient;

/**
 * Created by TonyChuang on 2016/6/20.
 */
public interface ServiceConnectionCallback {
    /**
     * Called when the service is connected.
     * @param client a CustomTabsClient
     */
    void onServiceConnected(CustomTabsClient client);

    /**
     * Called when the service is disconnected.
     */
    void onServiceDisconnected();
}
