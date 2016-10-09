package com.joe.cflapplication.data.event;

import com.joe.cflapplication.data.model.order.Order;

/**
 * @Autor zongdongdong on 2016/10/9.
 */

public class UpdateOrderEvent {
    private Order order;

    public UpdateOrderEvent(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
