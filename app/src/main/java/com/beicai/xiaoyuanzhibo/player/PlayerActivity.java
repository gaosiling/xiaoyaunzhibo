package com.beicai.xiaoyuanzhibo.player;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

import com.beicai.xiaoyuanzhibo.R;


public class PlayerActivity extends FragmentActivity {


    public static void start(Context context) {
        Intent intent = new Intent(context, PlayerActivity.class);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        setContentView(R.layout.act_player);
        PlayerFragment liveViewFragment = new PlayerFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fra_player, liveViewFragment).commit();
        FunFragment funFragment = FunFragment.newInstance();
        funFragment.show(getSupportFragmentManager(), "fun");

    }


}