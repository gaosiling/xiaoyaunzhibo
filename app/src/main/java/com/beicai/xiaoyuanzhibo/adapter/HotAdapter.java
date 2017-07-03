package com.beicai.xiaoyuanzhibo.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.beicai.xiaoyuanzhibo.R;
import com.beicai.xiaoyuanzhibo.bean.LiveBean;
import com.beicai.xiaoyuanzhibo.utils.ImageOption;
import com.beicai.xiaoyuanzhibo.widget.MultiBaseAdapter;
import com.beicai.xiaoyuanzhibo.widget.ViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * @author xiaoyuan
 * 这个是精选跟热门的适配器
 */

public class HotAdapter extends MultiBaseAdapter<LiveBean.ResultBean.ListBean> {
    private Context context;
    public HotAdapter(Context context, List<LiveBean.ResultBean.ListBean> listBean, boolean isOpenLoadMore) {
        super(context, listBean, isOpenLoadMore);
        this.context = context;
        setHaed(false);//false是无Banner true是有Banner
    }

    @Override
    protected void convert(ViewHolder holder, LiveBean.ResultBean.ListBean data, int viewType) {
        if (viewType == 0) {
            //如果返回0需要加载banner的布局
        } else {

            TextView name = holder.getView(R.id.name);
            TextView location = holder.getView(R.id.location);
            ImageView photo = holder.getView(R.id.photo);
            ImageView avatar = holder.getView(R.id.mine_avatar);
            name.setText(data.getUser().getUser_data().getUser_name());
            location.setText("北京");
            ImageLoader.getInstance().displayImage(data.getData().getPic(),photo, ImageOption.defaultOptions);
            ImageLoader.getInstance().displayImage(data.getUser().getUser_data().getAvatar(),avatar, ImageOption.defaultOptions);


        }
    }


    @Override
    protected int getViewType(int position, LiveBean.ResultBean.ListBean data) {
        if (data == null) {
            return 1;
            //这里是有Banner 如果 setHaed(true) 需要返回0
        } else {
            return 1;
        }

    }




    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.hot_item;
    }



}
