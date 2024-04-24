package com.weng.commutercarbackend.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.weng.commutercarbackend.model.dto.LocationAddRequest;
import com.weng.commutercarbackend.model.dto.LoginRequest;
import com.weng.commutercarbackend.model.dto.PasswordChangeRequest;
import com.weng.commutercarbackend.model.dto.RegisterRequest;
import com.weng.commutercarbackend.model.entity.Passenger;
import com.weng.commutercarbackend.model.vo.LoginVO;

import java.io.IOException;
import java.math.BigDecimal;

/**
* @author weng
* @description 针对表【passenger(乘客表)】的数据库操作Service
* @createDate 2024-03-30 18:30:30
*/
public interface PassengerService extends IService<Passenger> {
    LoginVO login(LoginRequest loginRequest);

    Long register(RegisterRequest registerRequest);

    void compareLocation(Long id) throws IOException, InterruptedException;

//    void updateStationName(Passenger passenger, String stationName) throws IOException;

    void payment(BigDecimal money, Passenger passenger);

    void changePassword(PasswordChangeRequest passwordChangeRequest);

    void updateRouteId(Long routeId);

    void updateGetonStationName(Passenger passenger, String getonStationName);

    void updateGetoffStationName(Passenger passenger, String getoffStationName);

    void checkStop(Long id, LocationAddRequest locationAddRequest) throws IOException;
}
