package com.beicai.xiaoyuanzhibo.player;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.beicai.xiaoyuanzhibo.R;
import com.beicai.xiaoyuanzhibo.bean.MessageBean;

import java.util.List;


public class MessageAdapter extends BaseAdapter {

    private Context mContext;
    private ViewHolder holder;
    private List<MessageBean> data;

    public MessageAdapter(Context context, List<MessageBean> data) {
        this.mContext = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void NotifyAdapter(List<MessageBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_message, null);
            holder.tvcontent = (TextView) convertView.findViewById(R.id.content);
            holder.tvName = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        holder.tvcontent.setText(data.get(position).content);
        holder.tvName.setText("  "+data.get(position).name+"：");
        return convertView;
    }

    private final class ViewHolder {
        TextView tvcontent;
        TextView tvName;
    }
}