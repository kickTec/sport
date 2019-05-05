package com.kenick.sport.service.buyer;

import com.kenick.sport.pojo.order.Detail;

import java.util.List;

public interface DetailService {

    /**
     *  保存订单明细
     * @param detail 订单明细
     * @return 成功状态
     */
    Boolean saveDetail(Detail detail);

    /**
     * 保存订单明细集合
     * @param detailList 订单明细集合
     * @return 成功状态
     */
    Boolean saveDetailList(List<Detail> detailList);
}
