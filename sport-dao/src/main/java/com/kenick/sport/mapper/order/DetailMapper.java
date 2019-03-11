package com.kenick.sport.mapper.order;

import com.kenick.sport.pojo.order.Detail;
import com.kenick.sport.pojo.order.DetailQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DetailMapper {
    int countByExample(DetailQuery example);

    int deleteByExample(DetailQuery example);

    int deleteByPrimaryKey(Long id);

    int insert(Detail record);

    int insertSelective(Detail record);

    List<Detail> selectByExample(DetailQuery example);

    Detail selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Detail record, @Param("example") DetailQuery example);

    int updateByExample(@Param("record") Detail record, @Param("example") DetailQuery example);

    int updateByPrimaryKeySelective(Detail record);

    int updateByPrimaryKey(Detail record);
}