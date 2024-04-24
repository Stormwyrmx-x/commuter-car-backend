package com.weng.commutercarbackend.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weng.commutercarbackend.model.entity.Bus;
import org.apache.ibatis.annotations.Mapper;

/**
* @author weng
* @description 针对表【bus(车辆表)】的数据库操作Mapper
* @createDate 2024-04-19 20:29:28
* @Entity generator.domain.Bus
*/
@Mapper
public interface BusMapper extends BaseMapper<Bus> {

}




