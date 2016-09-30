package com.joecorelibrary.mvvm.bindingadapter.banner;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.joecorelibrary.mvvm.command.ReplyCommand;
import com.joecorelibrary.mvvm.widget.banner.BGABanner;
import com.joecorelibrary.mvvm.widget.banner.BGABaseModel;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;


/**
 * Created by zongdongdong on 2016/7/21.
 */
public final class ViewBindingAdapter {
    @BindingAdapter(value = {"bannerData"}, requireAll = false)
    public static <T extends BGABaseModel> void setImagesUrl(BGABanner banner, List<T> baseModels) {
        if (baseModels != null && baseModels.size() > 0) {
            banner.setAdapter((bgaBanner, view, model, positon) -> {
                ImageView imageView = (ImageView) view;
                String url = ((BGABaseModel)model).getUrl();
                Picasso.with(bgaBanner.getContext()).load(url).placeholder(bgaBanner.getPlaceHolderImage()).error(bgaBanner.getPlaceHolderImage()).into(imageView);
            });
            banner.setData(baseModels, null);
        }
    }

    @BindingAdapter({"bannerClickCommand"})
    public static void clickCommand(BGABanner banner, final ReplyCommand<Integer> onBannerClickCommand) {
        banner.setOnItemClickListener((banner1, view, model, position) -> {
            if (onBannerClickCommand != null) {
                onBannerClickCommand.execute(position);
            }
        });
    }
}
