package com.joecorelibrary.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by huhao on 2016/7/25.
 */
public class PickPicUtil {
    private static final File PHOTO_DIR = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera");
    /* 用来标识请求照相功能的activity */
    public static final int CAMERA_WITH_DATA = 0;
    /* 用来标识请求gallery的activity */
    public static final int PHOTO_PICKED_WITH_DATA = 1;
    public static final int PHOTO_CROP_DATA = 2;

    // private File mCurrentPhotoFile;// 照相机拍照得到的图片
    private static File mCurrentPhotoFile;// 照相机拍照得到的图片

    /**
     * 从相册获取图片
     */
    public static void doPickPhotoFromGallery(Context context, String fileName) {
        try {
            final Intent intent = getPhotoPickIntent(fileName);
            ((Activity)context).startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
        } catch (ActivityNotFoundException e) {
//            Toast.makeText(activity, "---ActivityNotFoundException---", Toast.LENGTH_SHORT).show();
        }
    }

    // 封装请求Gallery的intent
    public static Intent getPhotoPickIntent(String fileName) {

        PHOTO_DIR.mkdirs();// 创建照片的存储目录
        mCurrentPhotoFile = new File(PHOTO_DIR, fileName);// 给新照的照片文件命名
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent intent = new Intent(Intent.ACTION_PICK, null);
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
//        intent.putExtra("crop", "true");
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        intent.putExtra("outputX", 150);
//        intent.putExtra("outputY", 150);
//        intent.putExtra("scale", true);
//        intent.putExtra("return-data", false);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCurrentPhotoFile));
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        return intent;
    }

    /**
     * 从照相机获取图片
     */
    public static void doTakePhotoFromCamera(Context context, String fileName) {
        try {
            PHOTO_DIR.mkdirs();// 创建照片的存储目录
            mCurrentPhotoFile = new File(PHOTO_DIR, fileName);// 给新照的照片文件命名
            final Intent intent = getTakePickIntent(mCurrentPhotoFile);
            ((Activity)context).startActivityForResult(intent, CAMERA_WITH_DATA);
        } catch (ActivityNotFoundException e) {
//            Toast.makeText(activity, "---ActivityNotFoundException---", Toast.LENGTH_SHORT).show();
        }
    }

    public static Intent getTakePickIntent(File file) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        return intent;
    }

    /**
     * 用当前时间给取得的图片命名
     */
    private static String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date) + ".jpg";
    }


    public static void doCropPhoto(Context context) {
        try {
            // 启动gallery去剪辑这个照片
            final Intent intent = getCropImageIntent(Uri.fromFile(mCurrentPhotoFile));
            ((Activity)context).startActivityForResult(intent, PHOTO_CROP_DATA);
        } catch (Exception e) {
//            Toast.makeText(activity, "---photoPickerNotFoundText---", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Constructs an intent for image cropping. 调用图片剪辑程序
     */
    public static Intent getCropImageIntent(Uri photoUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCurrentPhotoFile));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        return intent;
    }

    public static void startCropPhoto(Context context, Uri photoUri){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCurrentPhotoFile));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        ((Activity)context).startActivityForResult(intent, PHOTO_CROP_DATA);
    }


    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }

    public static String getPicPath() {
        return mCurrentPhotoFile == null ? "" : mCurrentPhotoFile.getAbsolutePath();
    }
}
