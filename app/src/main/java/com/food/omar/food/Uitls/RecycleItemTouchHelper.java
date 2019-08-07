package com.food.omar.food.Uitls;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.food.omar.food.Adapter.CartAdapter;
import com.food.omar.food.Adapter.FavoritAdapter;
import com.food.omar.food.DataBase.ModelDB.Favorite;
import com.food.omar.food.FavoriteActivity;

public class RecycleItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    RecycleItemTouchHelperListener listener;

    public RecycleItemTouchHelper(int dragDirs, int swipeDirs,RecycleItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener=listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
   listener.onSwiped(viewHolder,direction,viewHolder.getAdapterPosition());
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof FavoritAdapter.FavoritViewHolder)
        {
        View foregroundView =((FavoritAdapter.FavoritViewHolder)viewHolder).id_foreground;
        getDefaultUIUtil().clearView(foregroundView);
    }
    else  if (viewHolder instanceof CartAdapter.CartViewHolder)
        {
            View foregroundView =((CartAdapter.CartViewHolder)viewHolder).view_foreground;
            getDefaultUIUtil().clearView(foregroundView);
        }
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder!=null)
        {
            if (viewHolder instanceof FavoritAdapter.FavoritViewHolder)
            {
                View foregroundView =((FavoritAdapter.FavoritViewHolder)viewHolder).id_foreground;
                getDefaultUIUtil().onSelected(foregroundView);
            }
            else  if (viewHolder instanceof CartAdapter.CartViewHolder)
            {
                View foregroundView =((CartAdapter.CartViewHolder)viewHolder).view_foreground;
                getDefaultUIUtil().onSelected(foregroundView);
            }
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (viewHolder instanceof FavoritAdapter.FavoritViewHolder)
        {
            View foregroundView =((FavoritAdapter.FavoritViewHolder)viewHolder).id_foreground;
            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
        }
        else  if (viewHolder instanceof CartAdapter.CartViewHolder)
        {
            View foregroundView =((CartAdapter.CartViewHolder)viewHolder).view_foreground;
            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        if (viewHolder instanceof FavoritAdapter.FavoritViewHolder)
        {
            View foregroundView =((FavoritAdapter.FavoritViewHolder)viewHolder).id_foreground;
            getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
        }
        else  if (viewHolder instanceof CartAdapter.CartViewHolder)
        {
            View foregroundView =((CartAdapter.CartViewHolder)viewHolder).view_foreground;
            getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
        }
    }
}
