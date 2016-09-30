package com.joecorelibrary.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

public class PermissionRequest {

    private Activity activity;
    private String permission;
    private int requestCode;
    private Callback callback;

    public PermissionRequest(Activity activity) {
        this.activity = activity;
    }

    public void startRequest(String permission, int requestCode, Callback callback) {
        this.permission = permission;
        this.requestCode = requestCode;
        this.callback = callback;
        requesting();
    }

    private void requesting() {
        if (Build.VERSION.SDK_INT >= 23) {
            int hasPermission = activity.checkSelfPermission(permission);
            Log.i("PermissionRequest", hasPermission+"");
            if (hasPermission == PackageManager.PERMISSION_GRANTED) {
                callback.onCallback(true);
            } else {
                boolean isShowAlert = activity.shouldShowRequestPermissionRationale(permission);
                if (isShowAlert) {
                    Log.i("PermissionRequest","shouldShowRequestPermissionRationale");
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    if (Build.VERSION.SDK_INT >= 21) {
                        builder = new AlertDialog.Builder(activity, android.R.style.Theme_Material_Light_Dialog_Alert);
                    }
                    builder.setMessage("permission_text");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 请求权限
                            if (Build.VERSION.SDK_INT >= 23) {
                                activity.requestPermissions(new String[]{permission}, requestCode);
                            }
                        }
                    });
                    builder.show();
                } else {
                    // 请求权限
                    Log.i("PermissionRequest","requestPermissions");
                    activity.requestPermissions(new String[]{permission}, requestCode);
                }
            }
        } else {
            int hasPermission = activity.checkPermission(permission, android.os.Process.myPid(), android.os.Process.myUid());
            if (hasPermission == PackageManager.PERMISSION_GRANTED) {
                callback.onCallback(true);
            } else {
                openSettings();
            }
        }
    }

    public void onResponse(int requestCode, String[] permissions, int[] grantResults) {
        if (this.requestCode == requestCode) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(permission)) {
                    boolean isAllowed = grantResults[i] == PackageManager.PERMISSION_GRANTED;
                    if (isAllowed) {
                        callback.onCallback(true);
                    } else {
                        openSettings();
                    }
                }
            }
        }
    }

    private void openSettings() {
        Log.i("PermissionRequest","openSettings");
//        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//        if (Build.VERSION.SDK_INT >= 21) {
//            builder = new AlertDialog.Builder(activity, android.R.style.Theme_Material_Light_Dialog_Alert);
//        }
//        String message = String.format(Res.getString("permission_text2" + getStringName()),getApplicationName());
//        builder.setMessage(message);
//        builder.setPositiveButton(Res.getString("permission_setting"), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                callback.onCallback(false);
//                try {
//                    Intent intent = new Intent();
//                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                    intent.setData(Uri.parse("package:" + activity.getPackageName()));
//                    activity.startActivity(intent);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        builder.setNegativeButton(Res.getString("permission_cancel"), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                callback.onCallback(false);
//            }
//        });
//        builder.show();
    }

    private String getStringName() {
        if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            return "_storage";
        } else if (permission.equals(Manifest.permission.GET_ACCOUNTS)) {
            return "_contacts";
        } else {
            return "";
        }
    }

    public String getApplicationName() {
        try {
            PackageManager packageManager = activity.getApplicationContext().getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(activity.getPackageName(), 0);
            return (String) packageManager.getApplicationLabel(applicationInfo);
        } catch (PackageManager.NameNotFoundException e) {
        }
        return "";
    }

    public interface Callback {
        void onCallback(boolean isAllowed);
    }
}
