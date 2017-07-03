package com.beicai.xiaoyuanzhibo.fragment;

import android.support.v4.app.Fragment;

import com.beicai.xiaoyuanzhibo.customview.LoadingDialog;

/**
 * Created by xiaoyuan on 17/3/8.
 */

public class BaseFragment extends Fragment {

    private LoadingDialog dialog;

    public void onShowProgressDlg() {
        if (dialog == null) {
            dialog = new LoadingDialog(getActivity());
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
