package com.joe.cflapplication.ui.view.foodmenu;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.view.View;

import com.joe.cflapplication.BR;
import com.joe.cflapplication.R;
import com.joe.cflapplication.data.model.foodmenu.FoodMenu;
import com.joe.cflapplication.data.service.NetCallbackBase;
import com.joe.cflapplication.data.service.NetService;
import com.joe.cflapplication.ui.base.BaseActivity;
import com.joe.cflapplication.ui.base.annotation.Layout;
import com.joe.cflapplication.ui.dialog.DialogFoodMenu;
import com.joe.cflapplication.ui.dialog.DialogOperation;
import com.joe.cflapplication.ui.view.foodmenu.item.ItemFoodMenuViewModel;
import com.joecorelibrary.mvvm.base.IViewModel;
import com.joecorelibrary.mvvm.command.ReplyCommand;
import com.joecorelibrary.util.UIHelper;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import me.tatarka.bindingcollectionadapter.ItemView;

@Layout(id = R.layout.activity_food_menu, title = "菜品", rightButtonRes = R.drawable.icon_add)
public class FoodMenuActivity extends BaseActivity implements IViewModel {

    public ObservableList<ItemFoodMenuViewModel> itemFoodMenuViewModels = new ObservableArrayList<>();
    public ItemView foodMenuItemView = ItemView.of(BR.viewModel, R.layout.item_food_menu);
    public Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        loadFoodMenuListData();
    }

    /**
     * 添加菜品
     * @param view
     */
    @Override
    public void onRightButtonBeClick(View view) {
        super.onRightButtonBeClick(view);
        new DialogFoodMenu(this, "添加菜品", foodMenu -> {
            NetService.create(this)
                    .addDefaultProgressing()
                    .addParams(foodMenu)
                    .save(new NetCallbackBase<String>(){
                        @Override
                        public void onResult(boolean isSuccess, String response, String errorMsg) {
                            if(isSuccess){
                                foodMenu.setObjectId(response);
                                itemFoodMenuViewModels.add(0,new ItemFoodMenuViewModel(foodMenu));
                            }else{
                                UIHelper.showShortToast(FoodMenuActivity.this, "菜品添加失败" + errorMsg);
                            }
                        }
                    });
        }).show();
    }

    /**
     * 加载数据
     */
    private void loadFoodMenuListData() {
        UIHelper.showDefaultProgress(this);
        BmobQuery<FoodMenu> foodMenuBmobQuery = new BmobQuery<>();
        foodMenuBmobQuery.order("-createdAt");
        foodMenuBmobQuery.findObjects(new FindListener<FoodMenu>() {
            @Override
            public void done(List<FoodMenu> list, BmobException e) {
                UIHelper.hideProgress();
                if (e != null) {
                    UIHelper.showShortToast(FoodMenuActivity.this, "数据加载失败" + e.getMessage());
                } else {
                    itemFoodMenuViewModels.clear();
                    if (list != null) {
                        for (FoodMenu foodMenu : list) {
                            itemFoodMenuViewModels.add(new ItemFoodMenuViewModel(foodMenu));
                        }
                    }
                }
            }
        });
    }

    /**
     * listview长按操作
     */
    public ReplyCommand<Integer> onItemLongClickCommand = new ReplyCommand<>(position -> {
        new DialogOperation(this, new DialogOperation.OnOperationListener() {
            @Override
            public void update() {
                FoodMenu itemObj = itemFoodMenuViewModels.get(position).foodMenu;
                new DialogFoodMenu(mContext, itemObj, foodMenu -> {
                    NetService.create(mContext)
                            .addProgressing("正在更新...")
                            .addParams(foodMenu)
                            .update(new NetCallbackBase() {
                                @Override
                                public void onResult(boolean isSuccess, Object response, String errorMsg) {
                                    if (isSuccess) {
                                        UIHelper.showShortToast(mContext, "傻逼,已更新成功了");
                                        itemFoodMenuViewModels.set(position, new ItemFoodMenuViewModel(foodMenu));
                                    }else{
                                        UIHelper.showShortToast(FoodMenuActivity.this, "傻逼,别看了，更新失败了" + errorMsg);
                                    }
                                }
                            });
                }).show();
            }

            @Override
            public void delete() {
                FoodMenu itemObj = itemFoodMenuViewModels.get(position).foodMenu;
                FoodMenu foodMenu = new FoodMenu();
                foodMenu.setObjectId(itemObj.getObjectId());
                NetService.create(mContext)
                        .addProgressing("正在删除...")
                        .addParams(foodMenu)
                        .delete(new NetCallbackBase() {
                            @Override
                            public void onResult(boolean isSuccess, Object response, String errorMsg) {
                                if (isSuccess) {
                                    UIHelper.showShortToast(mContext, "傻逼,已删除成功");
                                    itemFoodMenuViewModels.remove(itemFoodMenuViewModels.get(position));
                                }else{
                                    UIHelper.showShortToast(FoodMenuActivity.this, "傻逼,删除失败了" + errorMsg);
                                }
                            }
                        });
            }
        }).show();
    });
}
