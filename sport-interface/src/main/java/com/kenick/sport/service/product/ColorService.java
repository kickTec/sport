package com.kenick.sport.service.product;

import com.kenick.sport.pojo.product.Color;

import java.util.List;

public interface ColorService {
    /**
     *  根据parent id 查询所有颜色信息
     * @param parentId 上一级id
     * @return 符合条件的颜色信息集合
     */
    List<Color> selectColorByParentIdNot(Long parentId);

    /**
     *  通过颜色id获取对象
     * @param colorId 颜色id
     * @return 颜色对象
     */
    Color selectColorById(Long colorId);
}
