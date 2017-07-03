package com.beicai.xiaoyuanzhibo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beicai.xiaoyuanzhibo.R;
import com.beicai.xiaoyuanzhibo.utils.PreferenceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xiaoyuan on 17/3/8.
 */

public class MineFragment extends BaseFragment {

    @BindView(R.id.quit)
    TextView quit;

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_mine, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.quit)
    public void onClick() {
        PreferenceUtils.putsessionId("");
    }
}
