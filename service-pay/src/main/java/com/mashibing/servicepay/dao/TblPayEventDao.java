package com.mashibing.servicepay.dao;

import com.mashibing.servicepay.entity.TblPayEvent;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TblPayEventDao {
    int deleteByPrimaryKey(Integer id);

    int insert(TblPayEvent record);

    int insertSelective(TblPayEvent record);

    TblPayEvent selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TblPayEvent record);

    int updateByPrimaryKey(TblPayEvent record);
}