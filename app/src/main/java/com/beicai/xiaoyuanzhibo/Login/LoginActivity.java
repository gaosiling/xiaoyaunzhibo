package com.beicai.xiaoyuanzhibo.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.beicai.xiaoyuanzhibo.R;
import com.beicai.xiaoyuanzhibo.activity.BaseActivity;
import com.beicai.xiaoyuanzhibo.activity.MainActivity;
import com.beicai.xiaoyuanzhibo.api.HomeApi;
import com.beicai.xiaoyuanzhibo.bean.UserBean;
import com.beicai.xiaoyuanzhibo.network.RetrofitManager;
import com.beicai.xiaoyuanzhibo.utils.CommonUtils;
import com.beicai.xiaoyuanzhibo.utils.PreferenceUtils;
import com.beicai.xiaoyuanzhibo.utils.ToastUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xiaoyuan on 17/3/15.
 */

public class LoginActivity extends BaseActivity {


    @BindView(R.id.login_phone)
    MaterialEditText loginPhone;
    @BindView(R.id.login_password)
    MaterialEditText loginPassword;
    @BindView(R.id.tv_login)
    TextView tvLogin;

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        ButterKnife.bind(this);
    }


    private void login(String phone, String password) {
        onShowProgressDlg();
        FormBody formBody = new FormBody.Builder()
                .add("phone", phone)
                .add("password", password)
                .build();
        HomeApi homeApi = RetrofitManager.getTestRetrofit().create(HomeApi.class);
        Call<UserBean> checkUserCall = homeApi.live_login(formBody);
        checkUserCall.enqueue(new Callback<UserBean>() {
            @Override
            public void onResponse(Call<UserBean> call, Response<UserBean> response) {
                cancelProgressDlg();
                if (response.body().getError_code() == 0) {

                    MainActivity.start(LoginActivity.this);
                    PreferenceUtils.putsessionId(response.body().getResult().getId() + "");
                } else {
                    ToastUtils.showLong("登录失败");
                }
            }

            @Override
            public void onFailure(Call<UserBean> call, Throwable t) {
                ToastUtils.showLong(getResources().getString(R.string.net_error));
                cancelProgressDlg();
            }
        });
    }

    @OnClick({R.id.tv_login, R.id.new_rigister})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                String phone = loginPhone.getText().toString();
                String password = loginPassword.getText().toString();

                if(TextUtils.isEmpty(phone)){
                    ToastUtils.showLong("请输入手机号");
                    return;
                }

                if(!CommonUtils.checkPhoneNumber(phone)){
                    ToastUtils.showLong("请输入正确手机号");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    ToastUtils.showLong("请输入密码");
                    return;
                }

                login(phone,password);
                break;
            case R.id.new_rigister:
                RegisterActivity.start(LoginActivity.this);
                break;
        }
    }
}
