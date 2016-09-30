package com.joecorelibrary.mvvm.bindingadapter.swiperefresh;

import android.databinding.BindingAdapter;
import android.support.v4.widget.SwipeRefreshLayout;

import com.joecorelibrary.mvvm.command.ReplyCommand;


/**
 * Created by kelin on 16-4-26.
 */
public class ViewBindingAdapter {
    @BindingAdapter({"onRefreshCommand"})
    public static void onRefreshCommand(SwipeRefreshLayout swipeRefreshLayout, final ReplyCommand onRefreshCommand) {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (onRefreshCommand != null) {
                onRefreshCommand.execute();
            }
        });
    }

    @BindingAdapter({"swipeLayoutRefreshing"})
    public static void setRefreshing(SwipeRefreshLayout swipeRefreshLayout, boolean isRefreshing){
        swipeRefreshLayout.setRefreshing(isRefreshing);
    }

}
