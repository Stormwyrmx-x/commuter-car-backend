package com.weng.commutercarbackend.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weng.commutercarbackend.model.entity.Driver;
import org.apache.ibatis.annotations.Mapper;

/**
* @author weng
* @description 针对表【driver(乘客表)】的数据库操作Mapper
* @createDate 2024-03-30 18:30:23
* @Entity generator.domain.Driver
*/
@Mapper
public interface DriverMapper extends BaseMapper<Driver> {

}




