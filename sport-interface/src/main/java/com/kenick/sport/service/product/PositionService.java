package com.kenick.sport.service.product;

import com.kenick.sport.pojo.ad.Position;

public interface PositionService {

    /**
     *  根据position id获取对象
     * @param positionId 位置id
     * @return 位置对象
     */
    Position selectPositonById(Long positionId);
}
