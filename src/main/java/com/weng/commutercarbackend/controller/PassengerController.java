package com.weng.commutercarbackend.controller;


import com.weng.commutercarbackend.common.Result;
import com.weng.commutercarbackend.model.entity.Passenger;
import com.weng.commutercarbackend.model.vo.PassengerVO;
import com.weng.commutercarbackend.model.vo.StopVO;
import com.weng.commutercarbackend.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/passenger")
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;

    /**
     * 获取当前登录乘客信息
     * @param passenger
     * @return
     */
    @GetMapping("/current")
    public Result<PassengerVO> getCurrentPassenger(@AuthenticationPrincipal Passenger passenger) {
        PassengerVO passengerVO = PassengerVO.builder()
                .id(passenger.getId())
                .username(passenger.getUsername())
                .name(passenger.getName())
                .phone(passenger.getPhone())
                .stationName(passenger.getStationName())
                .driverId(passenger.getDriverId())
                .money(passenger.getMoney())
                .build();
        return Result.success(passengerVO);
    }

}
