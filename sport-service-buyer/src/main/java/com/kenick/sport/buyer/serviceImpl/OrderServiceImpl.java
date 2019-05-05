package com.kenick.sport.buyer.serviceImpl;

import com.kenick.sport.mapper.order.OrderMapper;
import com.kenick.sport.pojo.order.Order;
import com.kenick.sport.service.buyer.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Override
    public Boolean saveOrder(Order order) {
        int insertLines = orderMapper.insert(order);
        return insertLines>0;
    }
}
