package com.weng.commutercarbackend.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weng.commutercarbackend.model.entity.Stop;
import org.apache.ibatis.annotations.Mapper;

/**
* @author weng
* @description 针对表【stop(站点表)】的数据库操作Mapper
* @createDate 2024-03-30 20:54:48
* @Entity generator.domain.Stop
*/
@Mapper
public interface StopMapper extends BaseMapper<Stop> {

}




