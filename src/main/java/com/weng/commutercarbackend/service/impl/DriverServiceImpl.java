package com.weng.commutercarbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.weng.commutercarbackend.common.ResultCodeEnum;
import com.weng.commutercarbackend.exception.BusinessException;
import com.weng.commutercarbackend.mapper.DriverMapper;
import com.weng.commutercarbackend.mapper.StopMapper;
import com.weng.commutercarbackend.model.dto.auth.LoginRequest;
import com.weng.commutercarbackend.model.dto.auth.RegisterRequest;
import com.weng.commutercarbackend.model.entity.Driver;
import com.weng.commutercarbackend.model.entity.Stop;
import com.weng.commutercarbackend.model.vo.LoginVO;
import com.weng.commutercarbackend.service.DriverService;
import com.weng.commutercarbackend.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
* @author weng
* @description 针对表【driver(乘客表)】的数据库操作Service实现
* @createDate 2024-03-30 18:30:23
*/
@Service
@RequiredArgsConstructor
@Slf4j
public class DriverServiceImpl extends ServiceImpl<DriverMapper, Driver>
    implements DriverService {
    
    private final DriverMapper driverMapper;
    private final StopMapper stopMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final Gson gson;

    @Override
    public LoginVO login(LoginRequest loginRequest) {
        //1.根据用户名密码进行登录
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());
        Authentication authenticationResponse = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        Driver driver = (Driver) authenticationResponse.getPrincipal();
        //2.生成jwt令牌
        String token = jwtUtil.generateToken(driver);
        //3.返回用户信息
        return LoginVO.builder()
                .id(driver.getId())
                .username(driver.getUsername())
                .name(driver.getName())
                .phone(driver.getPhone())
                .token(token)
                .build();
    }

    @Override
    public Long register(RegisterRequest registerRequest) {
        //1.密码和校验密码相同
        if (!Objects.equals(registerRequest.password(), registerRequest.checkPassword())) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR, "两次密码输入不一致");
        }
        //2.账号不能重复(查数据库)
        LambdaQueryWrapper<Driver> driverQueryWrapper = new LambdaQueryWrapper<>();
        driverQueryWrapper.eq(Driver::getUsername, registerRequest.username());
        Long count = driverMapper.selectCount(driverQueryWrapper);
        if (count > 0) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR, "账号重复");
        }
        //3.存储到数据库
        Stop stop=new Stop();
        stopMapper.insert(stop);
        Driver driver = Driver.builder()
                .username(registerRequest.username())
                .password(passwordEncoder.encode(registerRequest.password()))
                .name(registerRequest.name())
                .phone(registerRequest.phone())
                .stopId(stop.getId())
                .build();
        driverMapper.insert(driver);//如果插入失败，它会抛出异常.而不是返回一个负数

        return driver.getId();
    }
}




