package com.food.omar.food.Uitls;

import android.support.v7.widget.RecyclerView;

public interface RecycleItemTouchHelperListener {
    void onSwiped(RecyclerView.ViewHolder viewHolder,int direction,int postion);
}
