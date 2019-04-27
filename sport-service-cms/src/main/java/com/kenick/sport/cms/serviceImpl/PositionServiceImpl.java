package com.kenick.sport.cms.serviceImpl;

import com.kenick.sport.mapper.ad.PositionMapper;
import com.kenick.sport.pojo.ad.Position;
import com.kenick.sport.service.product.PositionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("positionService")
public class PositionServiceImpl implements PositionService {
    @Resource
    private PositionMapper positionMapper;

    @Override
    public Position selectPositonById(Long positionId) {
        return positionMapper.selectByPrimaryKey(positionId);
    }
}
