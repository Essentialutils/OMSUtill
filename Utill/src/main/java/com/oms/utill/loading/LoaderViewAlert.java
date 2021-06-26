package com.oms.utill.loading;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.oms.utill.R;
import com.oms.utill.loading.sprite.Sprite;

public class LoaderViewAlert {
    public Dialog loading;
    View view;
    LoaderView loaderView;

    public LoaderViewAlert(Activity activity) {
        view = LayoutInflater.from(activity).inflate(R.layout.alert_loading, null);
        loaderView = view.findViewById(R.id.loader);
        loading = new Dialog(activity);
        loading.setContentView(view);
        loading.setCancelable(false);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public LoaderViewAlert setCancellable(boolean flag) {
        loading.setCancelable(flag);
        return this;
    }

    public LoaderViewAlert setLoaderStyle(Sprite style) {
        loaderView.setIndeterminateDrawable(style);
        return this;
    }

    public LoaderViewAlert setLoaderColor(int color) {
        loaderView.setColor(color);
        return this;
    }

    public LoaderViewAlert removeShadow() {
        loading.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        return this;
    }

    public void start() {
        loading.show();
    }

    public void stop() {
        loading.dismiss();
    }
}
