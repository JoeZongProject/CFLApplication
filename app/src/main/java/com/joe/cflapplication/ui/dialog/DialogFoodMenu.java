package com.joe.cflapplication.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.joe.cflapplication.R;
import com.joe.cflapplication.BR;
import com.joe.cflapplication.data.model.foodmenu.FoodMenu;
import com.joe.cflapplication.databinding.DialogFoodMenuBinding;
import com.joecorelibrary.mvvm.command.ReplyCommand;
import com.joecorelibrary.util.UIHelper;

/**
 * @Autor zongdongdong on 2016/9/30.
 */

public class DialogFoodMenu extends Dialog {
    private DialogFoodMenu(Context context) {
        super(context);
        initView();
    }

    private DialogFoodMenu(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    private DialogFoodMenu(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public DialogFoodMenu(Context context, String title, OnDoListener listener) {
        super(context);
        initView();
        dialogTitle.set(title);
        isUpdateFoodMenu = false;
        this.listener = listener;
    }

    public DialogFoodMenu(Context context, FoodMenu foodMenu, OnDoListener listener) {
        super(context);
        initView();
        dialogTitle.set("更新菜品");
        foodName.set(foodMenu.getFoodName());
        foodPrice.set(foodMenu.getFoodPrice());
        isUpdateFoodMenu = true;
        this.currentFoodMenu = foodMenu;
        this.listener = listener;
    }

    private void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogFoodMenuBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_food_menu, null, false);
//        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_food_menu, null);
        binding.setVariable(BR.viewModel, this);
        setContentView(binding.getRoot());
    }

    public boolean isUpdateFoodMenu;
    public OnDoListener listener;
    public FoodMenu currentFoodMenu;
    public ObservableField<String> dialogTitle = new ObservableField<>();
    public ObservableField<String> foodName = new ObservableField<>("");
    public ObservableField<String> foodPrice = new ObservableField<>("");

    public ReplyCommand onClickCancelBtnCommand = new ReplyCommand(() -> {
        dismiss();
    });

    public ReplyCommand onClickConfirmBtnCommand = new ReplyCommand(() -> {
        String foodNameStr = foodName.get().trim();
        String foodPriceStr = foodPrice.get().trim();
        if (TextUtils.isEmpty(foodNameStr) || TextUtils.isEmpty(foodPriceStr)) {
            UIHelper.showShortToast(getContext(), "草，你没输入东西啊");
            return;
        }
        dismiss();
        FoodMenu foodMenu = new FoodMenu();
        foodMenu.setFoodName(foodNameStr.trim());
        foodMenu.setFoodPrice(foodPriceStr.trim());
        if (isUpdateFoodMenu) {
            if (!foodNameStr.equals(currentFoodMenu.getFoodName()) || !foodPriceStr.equals(currentFoodMenu.getFoodPrice())){
                foodMenu.setObjectId(currentFoodMenu.getObjectId());
                if (listener != null) {
                    listener.onDo(foodMenu);
                }
            }
        }else{
            if (listener != null) {
                listener.onDo(foodMenu);
            }
        }

    });

    public interface OnDoListener {
        void onDo(FoodMenu foodMenu);
    }
}
