package com.joe.cflapplication.ui.view.about;

import android.content.Intent;
import android.databinding.ObservableField;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.joe.cflapplication.BuildConfig;
import com.joe.cflapplication.R;
import com.joe.cflapplication.data.model.common.AppVersion;
import com.joe.cflapplication.ui.base.BaseActivity;
import com.joe.cflapplication.ui.base.annotation.Layout;
import com.joe.cflapplication.ui.dialog.DialogTip;
import com.joecorelibrary.mvvm.base.IViewModel;
import com.joecorelibrary.mvvm.command.ReplyCommand;
import com.joecorelibrary.util.UIHelper;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

@Layout(id = R.layout.activity_about, title = "关于")
public class AboutActivity extends BaseActivity implements IViewModel {
    public ObservableField<String> version = new ObservableField<>();
    DialogTip dialogTip = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        version.set("Version" + BuildConfig.VERSION_NAME + "  Build" + BuildConfig.VERSION_CODE);
    }

    public ReplyCommand onClickUpdateAppCommand = new ReplyCommand(() -> {
        UIHelper.showDefaultProgress(this);
        BmobQuery<AppVersion> query = new BmobQuery<>();
        query.addWhereGreaterThan("versionCode", BuildConfig.VERSION_CODE);
        query.findObjects(new FindListener<AppVersion>() {
            @Override
            public void done(List<AppVersion> list, BmobException e) {
                UIHelper.hideProgress();
                if (e == null) {
                    if (list == null || list.size() == 0) {
                        UIHelper.showShortToast(AboutActivity.this, "当前已是最新版本");
                    }else{
                        dialogTip = new DialogTip(AboutActivity.this, "是否需要更新", v -> {
                            Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(list.get(0).getAppUrl()));
                            startActivity(it);
                            dialogTip.dismiss();
                        });
                        dialogTip.show();
                    }
                } else {
                    UIHelper.showShortToast(AboutActivity.this, "检查出错" + e.getMessage());
                }
            }
        });
    });
}
