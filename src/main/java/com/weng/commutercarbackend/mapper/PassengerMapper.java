package com.weng.commutercarbackend.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weng.commutercarbackend.model.entity.Passenger;
import org.apache.ibatis.annotations.Mapper;

/**
* @author weng
* @description 针对表【passenger(乘客表)】的数据库操作Mapper
* @createDate 2024-03-30 18:30:30
* @Entity generator.domain.Passenger
*/
@Mapper
public interface PassengerMapper extends BaseMapper<Passenger> {

}




