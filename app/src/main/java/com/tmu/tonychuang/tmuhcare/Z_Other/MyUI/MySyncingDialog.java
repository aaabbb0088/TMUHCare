package com.tmu.tonychuang.tmuhcare.Z_Other.MyUI;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.tmu.tonychuang.tmuhcare.R;

/**
 * Created by TonyChuang on 2016/6/30.
 */
public class MySyncingDialog {

    private MaterialDialog materialDialog;

    public MySyncingDialog(boolean horizontal, Context context, String content) {
        materialDialog = new MaterialDialog.Builder(context)
                .content(content)
                .progress(true, 0)
                .progressIndeterminateStyle(horizontal)
                .cancelable(false)
                .widgetColorRes(R.color.colorPrimaryDark)
                .build();
    }

    public void show() {
        materialDialog.show();
    }

    public void dismiss() {
        materialDialog.dismiss();
    }

    public boolean isShowing() {
        return materialDialog.isShowing();
    }

}
