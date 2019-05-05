package com.kenick.sport.service.buyer;

import com.kenick.sport.pojo.order.Order;

public interface OrderService {

    /**
     *  保存订单信息
     * @param order 订单对象
     * @return 成功状态
     */
    Boolean saveOrder(Order order);
}
