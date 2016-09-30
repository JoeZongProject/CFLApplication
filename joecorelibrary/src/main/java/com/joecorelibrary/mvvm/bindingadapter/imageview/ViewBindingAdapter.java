package com.joecorelibrary.mvvm.bindingadapter.imageview;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.widget.ImageView;

import com.joecorelibrary.util.StringUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import static android.graphics.Bitmap.Config.ARGB_4444;
import static android.graphics.Bitmap.Config.ARGB_8888;
import static android.graphics.Bitmap.Config.RGB_565;

/**
 * Created by kelin on 16-3-24.
 */
public final class ViewBindingAdapter {

    @BindingAdapter(value = {"uri", "placeholderImageRes"}, requireAll = false)
    public static void loadImage(final ImageView imageView, String uri, @DrawableRes int placeholderImageRes) {
        if (imageView.getTag() == null || String.valueOf(imageView.getTag()).equals(uri)) {
            if (TextUtils.isEmpty(uri) && placeholderImageRes != 0) {
                imageView.setImageResource(placeholderImageRes);
            } else {
                String newUri = utf8Togb2312(uri);
                if(placeholderImageRes == 0){
                    Picasso.with(imageView.getContext()).load(newUri).into(imageView);
                }else{
                    Picasso.with(imageView.getContext()).load(newUri).error(placeholderImageRes).placeholder(placeholderImageRes).into(imageView);
                }

//                Picasso.with(imageView.getContext()).load(newUri).transform(new ImageTrans(newUri)).error(placeholderImageRes).placeholder(placeholderImageRes).into(imageView);
            }
        }
    }

    public static class ImageTrans implements Transformation {
        public String key;

        public ImageTrans(String key) {
            this.key = key;
        }

        @Override
        public Bitmap transform(Bitmap source) {
            int oriWidth = source.getWidth();
            int oriHeight = source.getHeight();
            int newWidth, newHeight;
            if (oriWidth >= 960 || oriHeight >= 1280) {
                newWidth = oriWidth * 1 / 3;
                newHeight = oriHeight * 1 / 3;
            } else if (oriWidth >= 500 || oriHeight >= 500) {
                newWidth = oriWidth * 3 / 5;
                newHeight = oriHeight * 3 / 5;
            } else if (oriWidth > 200 || oriHeight > 200) {
                newWidth = oriWidth * 7 / 10;
                newHeight = oriHeight * 7 / 10;
            } else if (oriWidth > 100 || oriHeight > 100) {
                newWidth = oriWidth * 9 / 10;
                newHeight = oriHeight * 9 / 10;
            } else {
                return source;
            }
            Bitmap bmp = createNewBitmap(source, newWidth, newHeight);
            if (bmp != source) {
                source.recycle();
            }
            return bmp;
        }

        @Override
        public String key() {//唯一的key
            return key;
        }
    }

    /**
     * 转换url中的中文
     *
     * @param str
     * @return
     */
    public static String utf8Togb2312(String str) {
//        return str;
        if (!StringUtils.isContainChinese(str)) {
            return str;
        }
        String data = "";
        try {
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (c + "".getBytes().length > 1 && c != ':' && c != '/') {
                    data = data + java.net.URLEncoder.encode(c + "", "utf-8");
                } else {
                    data = data + c;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static Bitmap createNewBitmap(Bitmap bitMap, int newWidth, int newHeight) {
        int width = bitMap.getWidth();
        int height = bitMap.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
//        Bitmap newBitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix, true);

        Bitmap croppedImage = Bitmap.createBitmap(newWidth, newHeight, ARGB_8888);
//
        Canvas canvas = new Canvas(croppedImage);
////        Rect dr = new Rect(0, 0, width, height);
        canvas.drawBitmap(bitMap, matrix, null);
        return croppedImage;
    }

    /**
     * 图片尺寸压缩
     *
     * @param image
     * @param pixelW
     * @param pixelH
     * @return
     */
    public static Bitmap ratio(Bitmap image, float pixelW, float pixelH) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, os);
        if (os.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            os.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.PNG, 80, os);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = ARGB_4444;
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = pixelH;
        float ww = pixelW;
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        is = new ByteArrayInputStream(os.toByteArray());
        bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        //压缩好比例大小后再进行质量压缩
//      return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
        return bitmap;
    }

}

