package com.joecorelibrary.mvvm.bindingadapter.gridview;

import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.joecorelibrary.mvvm.command.ReplyCommand;


/**
 * Created by zongdongdong on 2016/7/28.
 */

public final class ViewBindingAdapter {
    @BindingAdapter(value = {"onGridViewItemClickCommand"}, requireAll = false)
    public static void onItemClickCommand(final GridView gridView, final ReplyCommand<Integer> onGridViewItemClickCommand) {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onGridViewItemClickCommand != null) {
                    onGridViewItemClickCommand.execute(position);
                }
            }
        });
    }
}
