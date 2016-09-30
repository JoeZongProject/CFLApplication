package com.joecorelibrary.mvvm.bindingadapter.circleimageview;

import android.databinding.BindingAdapter;
import android.text.TextUtils;

import com.joecorelibrary.mvvm.widget.CircleImageView;
import com.squareup.picasso.Picasso;

/**
 * Created by zongdongdong on 2016/7/30.
 */

public class ViewBindingAdapter {
    @BindingAdapter(value = {"circleImageUri"}, requireAll = false)
    public static void loadImage(final CircleImageView circleImageView, String uri) {
        if(TextUtils.isEmpty(uri)){
            circleImageView.setImageResource(circleImageView.getPlaceHolderImageId());
        }else {
            Picasso.with(circleImageView.getContext()).load(uri).error(circleImageView.getPlaceHolderImageId()).placeholder(circleImageView.getPlaceHolderImageId()).into(circleImageView);
        }
    }
}
