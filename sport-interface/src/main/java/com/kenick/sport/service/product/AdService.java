package com.kenick.sport.service.product;

import com.kenick.sport.pojo.ad.Advertising;
import com.kenick.sport.pojo.ad.Position;

import java.util.List;

public interface AdService {
    /**
     *  根据parentId查询广告位列表
     * @param parentId 广告parentId
     * @return 广告位列表
     */
    List<Position> selectPositionListByParentId(Long parentId);

    /**
     *  查询广告位下的所有广告
     * @param positionId 广告位id
     * @return 所有符合条件的广告
     */
    List<Advertising> selectAdListByPositionId(Long positionId);

    /**
     *  插入广告信息
     * @param advertising 广告信息
     */
    void insertAd(Advertising advertising);

    /**
     *  根据位置id获取大广告数据
     * @param positionId 位置id
     * @return 大广告json数据
     */
    String selectAdListByPositionIdForPortal(Long positionId);
}
