package com.kenick.sport.product.serviceImpl;

import com.kenick.sport.mapper.product.ColorMapper;
import com.kenick.sport.pojo.product.Color;
import com.kenick.sport.pojo.product.ColorQuery;
import com.kenick.sport.service.product.ColorService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("colorService")
public class ColorServiceImpl implements ColorService {
    @Resource
    private ColorMapper colorMapper;

    @Override
    public List<Color> selectColorByParentIdNot(Long parentId) {
        ColorQuery colorQuery = new ColorQuery();
        ColorQuery.Criteria criteria = colorQuery.createCriteria();
        if(parentId != null){
            criteria.andParentIdNotEqualTo(parentId);
        }
        return colorMapper.selectByExample(colorQuery);
    }

    @Override
    public Color selectColorById(Long colorId) {
        return colorMapper.selectByPrimaryKey(colorId);
    }
}
