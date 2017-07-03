package com.beicai.xiaoyuanzhibo.activity;

import android.support.v7.app.AppCompatActivity;

import com.beicai.xiaoyuanzhibo.customview.LoadingDialog;

/**
 * Created by xiaoyuan on 17/3/8.
 */

public class BaseActivity extends AppCompatActivity {


    private LoadingDialog dialog;

    public void onShowProgressDlg() {
        if (dialog == null) {
            dialog = new LoadingDialog(this);
            if (!dialog.isShowing()) {
                dialog.show();
            }
        }

    }

    public void cancelProgressDlg() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }

    }
}
