package com.kenick.sport.mapper.ad;

import com.kenick.sport.pojo.ad.Position;
import com.kenick.sport.pojo.ad.PositionQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PositionMapper {
    int countByExample(PositionQuery example);

    int deleteByExample(PositionQuery example);

    int deleteByPrimaryKey(Long id);

    int insert(Position record);

    int insertSelective(Position record);

    List<Position> selectByExample(PositionQuery example);

    Position selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Position record, @Param("example") PositionQuery example);

    int updateByExample(@Param("record") Position record, @Param("example") PositionQuery example);

    int updateByPrimaryKeySelective(Position record);

    int updateByPrimaryKey(Position record);
}