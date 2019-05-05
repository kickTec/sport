package com.kenick.sport.buyer.serviceImpl;

import com.kenick.sport.mapper.order.DetailMapper;
import com.kenick.sport.pojo.order.Detail;
import com.kenick.sport.service.buyer.DetailService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("detailService")
public class DetailServiceImpl implements DetailService {

    @Resource
    private DetailMapper detailMapper;

    @Override
    public Boolean saveDetail(Detail detail) {
        int insertLines = detailMapper.insert(detail);
        return insertLines>0;
    }

    @Override
    public Boolean saveDetailList(List<Detail> list) {
        for(Detail detail:list){
            detailMapper.insert(detail);
        }
        return true;
    }
}