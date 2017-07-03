package com.beicai.xiaoyuanzhibo.customview;


import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.beicai.xiaoyuanzhibo.R;


/**
 * @author xiaoyuan
 */
public class LoadingDialog extends Dialog {
    private boolean cancelable = false;

    public LoadingDialog(Context context) {
        super(context, R.style.dialog_style);
        init();
    }

    private void init() {
        View contentView = View.inflate(getContext(), R.layout.progress_dialog, null);
        setContentView(contentView);
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelable) {
                    dismiss();
                }
            }
        });

    }



    @Override
    public void dismiss() {
        super.dismiss();
    }


    @Override
    public void setCancelable(boolean flag) {
        cancelable = flag;
        super.setCancelable(flag);
    }

}
