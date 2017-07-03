package com.beicai.xiaoyuanzhibo.widget;



public interface OnMultiItemClickListeners<T> {
    void onItemClick(ViewHolder viewHolder, T data, int position, int viewType);
}
