package com.beicai.xiaoyuanzhibo.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import com.beicai.xiaoyuanzhibo.R;
import com.beicai.xiaoyuanzhibo.activity.BaseActivity;
import com.beicai.xiaoyuanzhibo.api.HomeApi;
import com.beicai.xiaoyuanzhibo.bean.RigisterBean;
import com.beicai.xiaoyuanzhibo.network.RetrofitManager;
import com.beicai.xiaoyuanzhibo.utils.CommonUtils;
import com.beicai.xiaoyuanzhibo.utils.ToastUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.FormBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xiaoyuan on 17/3/15.
 */

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.mine_avatar)
        CircleImageView mineAvatar;
    @BindView(R.id.nick_name)
    MaterialEditText nickName;
    @BindView(R.id.phone)
    MaterialEditText phone;
    @BindView(R.id.password)
    MaterialEditText password;
    @BindView(R.id.password_repeat)
    MaterialEditText passwordRepeat;
    @BindView(R.id.sign)
    MaterialEditText sign;
    @BindView(R.id.tv_rigister)
    TextView tvRigister;


    public static void start(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);

    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_register);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_rigister)
    public void onClick() {
        String sNickname = nickName.getText().toString();
        String sPhone = phone.getText().toString();
        String sPassword = password.getText().toString();
        String sPasswordRepeat = passwordRepeat.getText().toString();
        String sSign = sign.getText().toString();

        if(TextUtils.isEmpty(sNickname)){
            ToastUtils.showLong("请输入昵称");
            return;
        }
        if(TextUtils.isEmpty(sPhone)){
            ToastUtils.showLong("请输入手机号");
            return;
        }
        if(!CommonUtils.checkPhoneNumber(sPhone)){
            ToastUtils.showLong("请输入正确手机号");
            return;
        }
        if(TextUtils.isEmpty(sPassword)){
            ToastUtils.showLong("请输入密码");
            return;
        }
        if(!sPassword.equals(sPasswordRepeat)){
            ToastUtils.showLong("两次密码输入的不一样");
            return;
        }
        if(TextUtils.isEmpty(sSign)){
            ToastUtils.showLong("请输入密码");
            return;
        }

        rigister(sPhone,sNickname,sSign,sPassword);


    }
    private void rigister(String phone,String user_name,String sign,String password){
        onShowProgressDlg();
        FormBody formBody = new FormBody.Builder()
                .add("phone", phone)
                .add("user_name", user_name)
                .add("avatar", "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2608404801,4206400884&f=23&gp=0.jpg")
                .add("sign", sign)
                .add("password", password)
                .build();
        HomeApi homeApi = RetrofitManager.getTestRetrofit().create(HomeApi.class);
        Call<RigisterBean> checkUserCall = homeApi.live_rigister(formBody);
        checkUserCall.enqueue(new Callback<RigisterBean>() {
            @Override
            public void onResponse(Call<RigisterBean> call, Response<RigisterBean> response) {
                cancelProgressDlg();
                if (response.body().getError_code() == 0) {
                    LoginActivity.start(RegisterActivity.this);
                } else {
                    ToastUtils.showLong("注册失败");
                }
            }

            @Override
            public void onFailure(Call<RigisterBean> call, Throwable t) {
                ToastUtils.showLong(getResources().getString(R.string.net_error));
                cancelProgressDlg();
            }
        });
    }

}
