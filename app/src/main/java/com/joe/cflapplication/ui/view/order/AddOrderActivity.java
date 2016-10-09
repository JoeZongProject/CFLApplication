package com.joe.cflapplication.ui.view.order;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.joe.cflapplication.BR;
import com.joe.cflapplication.MyApplication;
import com.joe.cflapplication.R;
import com.joe.cflapplication.data.event.UpdateOrderEvent;
import com.joe.cflapplication.data.model.foodmenu.FoodMenu;
import com.joe.cflapplication.data.model.order.Order;
import com.joe.cflapplication.data.model.order.OrderFoodMenu;
import com.joe.cflapplication.data.service.NetCallbackBase;
import com.joe.cflapplication.data.service.NetService;
import com.joe.cflapplication.databinding.ActivityAddOrderBinding;
import com.joe.cflapplication.databinding.ItemSelectedFoodMenuBinding;
import com.joe.cflapplication.ui.base.BaseActivity;
import com.joe.cflapplication.ui.base.annotation.Layout;
import com.joe.cflapplication.ui.dialog.DialogInput;
import com.joe.cflapplication.ui.dialog.DialogInputUserInfo;
import com.joe.cflapplication.ui.view.foodmenu.item.ItemFoodMenuViewModel;
import com.joecorelibrary.mvvm.base.IViewModel;
import com.joecorelibrary.mvvm.command.ReplyCommand;
import com.joecorelibrary.util.UIHelper;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import me.tatarka.bindingcollectionadapter.ItemView;

@Layout(id = R.layout.activity_add_order, title = "创建订单")
public class AddOrderActivity extends BaseActivity<ActivityAddOrderBinding> implements IViewModel {
    private Context mContext;
    public ObservableList<ItemFoodMenuViewModel> itemFoodMenuViewModels = new ObservableArrayList<>();
    public ItemView foodMenuItemView = ItemView.of(BR.viewModel, R.layout.item_select_food_menu);

    public ObservableField<String> finalPrice = new ObservableField<>("0");
    public String couponPrice = "0";
    public String totalPrice = "0";

    /**
     * 已经点好的菜单
     */
    public ObservableList<ItemFoodMenuViewModel> itemSelectedFoodMenuViewModels = new ObservableArrayList<>();
//    public ItemView selectedFoodMenuItemView = ItemView.of(BR.viewModel, R.layout.item_selected_food_menu);

    private SelectedListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        List<FoodMenu> foodMenuList = MyApplication.getInstance().getFoodMenuList();
        if (foodMenuList == null || foodMenuList.size() == 0) {
            loadFoodMenuList();
        } else {
            setFoodMenu(foodMenuList);
        }
        adapter = new SelectedListViewAdapter();
        getDataBinding().listView.setAdapter(adapter);
    }

    public ReplyCommand<Integer> onItemSelectFoodMenuCommand = new ReplyCommand<Integer>(position -> {
        FoodMenu foodMenu = itemFoodMenuViewModels.get(position).foodMenu;
        addFoodMenu(foodMenu);
    });

    /**
     * 优惠
     */
    public ReplyCommand onClickCouponBtnCommand = new ReplyCommand(() -> {
        DialogInput dialogInput = new DialogInput(mContext, "优惠多少呢", new DialogInput.OnInputListener() {
            @Override
            public void onInput(String content) {
                if(TextUtils.isEmpty(content)){
                    couponPrice = "0";
                    finalPrice.set(totalPrice);
                    return;
                }
                if (finalPrice.get().equals("0") || content.startsWith(".")) {
                    UIHelper.showShortToast(mContext, "你可以回家睡觉了");
                    return;
                }
                if (new BigDecimal(totalPrice).compareTo(new BigDecimal(content)) > 0) {
                    couponPrice = content;
                    finalPrice.set(new BigDecimal(totalPrice).subtract(new BigDecimal(content)).toString());
                } else {
                    UIHelper.showShortToast(mContext, "畜生，你够想赚钱啦");
                }
            }
        });
        if(!couponPrice.equals("0")){
            dialogInput.setInputContent(couponPrice);
        }
        dialogInput.show();
    });

    /**
     * 确认下单
     */
    public ReplyCommand onClickCreateOrderBtnCommand = new ReplyCommand(() -> {
        if(itemSelectedFoodMenuViewModels.size()>0){
            new DialogInputUserInfo(this, new DialogInputUserInfo.OnInputInfoListener() {
                @Override
                public void onInput(String... content) {
                    Order order = new Order();
                    order.setState(0);
                    order.setTotalPrice(totalPrice);
                    order.setCouponPrice(couponPrice);
                    order.setFinalPrice(finalPrice.get());
                    order.setUserAddress(content[0]);
                    order.setUserMobile(content[1]);
                    List<OrderFoodMenu> foodMenus = new ArrayList<>();
                    for (ItemFoodMenuViewModel viewModel : itemSelectedFoodMenuViewModels) {
                        foodMenus.add(new OrderFoodMenu(viewModel.foodMenu.getFoodName(), viewModel.foodMenu.getFoodPrice(), viewModel.foodMenu.getCount()));
                    }
                    order.setFoodMenuList(foodMenus);
                    NetService.create(mContext)
                            .addProgressing("正在下单...")
                            .addParams(order)
                            .save(new NetCallbackBase<String>() {
                                @Override
                                public void onResult(boolean isSuccess, String response, String errorMsg) {
                                    super.onResult(isSuccess, response, errorMsg);
                                    if(isSuccess){
                                        order.setObjectId(response);
                                        EventBus.getDefault().post(new UpdateOrderEvent(order));
                                        UIHelper.showShortToast(mContext, "下单成功");
                                        finish();
                                    }else{
                                        UIHelper.showShortToast(mContext, "下单失败了"+errorMsg);
                                    }
                                }
                            });
                }
            }).show();
        }
    });

    /**
     * 添加菜品
     *
     * @param foodMenu
     */
    private synchronized void addFoodMenu(FoodMenu foodMenu) {
        boolean isExist = false;
        for (ItemFoodMenuViewModel itemFoodMenuViewModel : itemSelectedFoodMenuViewModels) {
            if (itemFoodMenuViewModel.foodMenu.getFoodName().equals(foodMenu.getFoodName())) {
                itemFoodMenuViewModel.add();
                isExist = true;
                break;
            }
        }
        if (!isExist) {
            foodMenu.setCount(1 + "");
            itemSelectedFoodMenuViewModels.add(new ItemFoodMenuViewModel(foodMenu));
        }
        adapter.notifyDataSetChanged();
    }


    /**
     * 从网络加载菜单
     */
    private void loadFoodMenuList() {
        UIHelper.showDefaultProgress(this);
        BmobQuery<FoodMenu> foodMenuBmobQuery = new BmobQuery<>();
        foodMenuBmobQuery.order("-createdAt");
        foodMenuBmobQuery.findObjects(new FindListener<FoodMenu>() {
            @Override
            public void done(List<FoodMenu> list, BmobException e) {
                UIHelper.hideProgress();
                if (e != null) {
                    UIHelper.showShortToast(mContext, "数据加载失败" + e.getMessage());
                } else {
                    MyApplication.getInstance().setFoodMenuList(list);
                    setFoodMenu(list);
                }
            }
        });
    }

    /**
     * 菜品数据
     *
     * @param list
     */
    private void setFoodMenu(List<FoodMenu> list) {
        if (list != null) {
            itemFoodMenuViewModels.clear();
            for (FoodMenu foodMenu : list) {
                itemFoodMenuViewModels.add(new ItemFoodMenuViewModel(foodMenu));
            }
        }
    }

    class SelectedListViewAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return itemSelectedFoodMenuViewModels.size();
        }

        @Override
        public Object getItem(int position) {
            return itemSelectedFoodMenuViewModels.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ItemSelectedFoodMenuBinding binding;
            if (convertView == null) {
                binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_selected_food_menu, parent, false);
                convertView = binding.getRoot();
                convertView.setTag(binding);
            } else {
                binding = (ItemSelectedFoodMenuBinding) convertView.getTag();
            }
            binding.setViewModel(itemSelectedFoodMenuViewModels.get(position));
            binding.imgvRemove.setOnClickListener(v -> {
                removeSingleFoodMenu(position);
            });

            binding.imgvAdd.setOnClickListener(v -> {
                addSingleFoodMenu(position);
            });
            return binding.getRoot();
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            String price = "0";
            for (ItemFoodMenuViewModel viewModel : itemSelectedFoodMenuViewModels) {
                BigDecimal bigDecimal = new BigDecimal(viewModel.foodMenu.getFoodPrice()).multiply(new BigDecimal(viewModel.foodMenu.getCount()));
                price = new BigDecimal(price).add(bigDecimal).toString();
            }
            totalPrice = price;
            if(new BigDecimal(price).compareTo(new BigDecimal(couponPrice))>0){
                finalPrice.set(new BigDecimal(totalPrice).subtract(new BigDecimal(couponPrice)).toString());
            }else{
                couponPrice = "0";
                UIHelper.showShortToast(mContext, "优惠价格已清空");
                finalPrice.set(totalPrice);
            }
        }
    }


    /**
     * 加减菜品
     *
     * @param position
     */
    public synchronized void addSingleFoodMenu(int position) {
        itemSelectedFoodMenuViewModels.get(position).add();
        adapter.notifyDataSetChanged();
    }

    public synchronized void removeSingleFoodMenu(int position) {
        int count = itemSelectedFoodMenuViewModels.get(position).remove();
        if (count <= 0) {
            itemSelectedFoodMenuViewModels.remove(position);
        }
        adapter.notifyDataSetChanged();
    }

}
