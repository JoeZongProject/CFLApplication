package com.joe.cflapplication.ui.view.foodmenu.item;

import android.databinding.ObservableField;

import com.joe.cflapplication.data.model.foodmenu.FoodMenu;

/**
 * @Autor zongdongdong on 2016/9/30.
 */

public class ItemFoodMenuViewModel {
    public FoodMenu foodMenu;
    public ObservableField<String> foodName = new ObservableField<>();
    public ObservableField<String> foodPrice = new ObservableField<>();
    public ObservableField<String> foodCount = new ObservableField<>();

    public ItemFoodMenuViewModel(FoodMenu foodMenu) {
        this.foodMenu = foodMenu;
        foodName.set(foodMenu.getFoodName());
        foodPrice.set(foodMenu.getFoodPrice() + "元");
        foodCount.set(foodMenu.getCount());
    }

    public void add() {
        foodCount.set((Integer.parseInt(foodCount.get()) + 1)+"");
        foodMenu.setCount(foodCount.get() + "");
    }

    public int remove(){
        foodCount.set((Integer.parseInt(foodCount.get()) - 1)+"");
        foodMenu.setCount(foodCount.get() + "");
        return Integer.parseInt(foodCount.get());
    }
}
