package com.weng.commutercarbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.weng.commutercarbackend.common.Result;
import com.weng.commutercarbackend.mapper.BusMapper;
import com.weng.commutercarbackend.model.dto.LoginRequest;
import com.weng.commutercarbackend.model.dto.RegisterRequest;
import com.weng.commutercarbackend.model.entity.Bus;
import com.weng.commutercarbackend.model.entity.Driver;
import com.weng.commutercarbackend.model.vo.BusVO;
import com.weng.commutercarbackend.model.vo.DriverVO;
import com.weng.commutercarbackend.model.vo.LoginVO;
import com.weng.commutercarbackend.service.DriverService;
import com.weng.commutercarbackend.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bus")
@RequiredArgsConstructor
public class BusController {

    private final BusMapper busMapper;

    @GetMapping("/all")
    public Result<List<BusVO>> getAll(){
        LambdaQueryWrapper<Bus>busLambdaQueryWrapper=new LambdaQueryWrapper<>();
        List<Bus> busList = busMapper.selectList(busLambdaQueryWrapper);
        List<BusVO> busVOList = busList.stream().map(
                bus -> BusVO.builder()
                .id(bus.getId())
                .licensePlate(bus.getLicensePlate())
                .build()).toList();
        return Result.success(busVOList);
    }



}
