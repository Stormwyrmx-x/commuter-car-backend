package com.weng.commutercarbackend.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.weng.commutercarbackend.model.dto.auth.LoginRequest;
import com.weng.commutercarbackend.model.dto.auth.RegisterRequest;
import com.weng.commutercarbackend.model.entity.Passenger;
import com.weng.commutercarbackend.model.vo.LoginVO;

/**
* @author weng
* @description 针对表【passenger(乘客表)】的数据库操作Service
* @createDate 2024-03-30 18:30:30
*/
public interface PassengerService extends IService<Passenger> {
    LoginVO login(LoginRequest loginRequest);

    Long register(RegisterRequest registerRequest);
}
