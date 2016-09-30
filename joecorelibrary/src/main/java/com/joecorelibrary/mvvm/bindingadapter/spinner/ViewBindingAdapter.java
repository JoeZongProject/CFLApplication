package com.joecorelibrary.mvvm.bindingadapter.spinner;

import android.databinding.BindingAdapter;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.joecorelibrary.mvvm.command.ReplyCommand;

/**
 * @Autor zongdongdong on 2016/9/27.
 */

public class ViewBindingAdapter {
    @BindingAdapter({"onSpinnerSelectCommand"})
    public static void onScrollChangeCommand(final Spinner spinner, final ReplyCommand<Integer> onSpinnerSelectCommand) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(onSpinnerSelectCommand != null){
                    onSpinnerSelectCommand.execute(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
