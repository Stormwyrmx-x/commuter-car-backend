package com.weng.commutercarbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.weng.commutercarbackend.model.dto.auth.UserLoginRequest;
import com.weng.commutercarbackend.model.dto.auth.UserRegisterRequest;
import com.weng.commutercarbackend.model.entity.User;
import com.weng.commutercarbackend.model.vo.UserLoginVO;

/**
* @author weng
* @description 针对表【user】的数据库操作Service
* @createDate 2024-01-01 16:53:33
*/
public interface UserService extends IService<User> {
//    User login(LoginRequest loginRequest, HttpServletRequest request);
    UserLoginVO login(UserLoginRequest userLoginRequest);

    Long register(UserRegisterRequest userRegisterRequest);
}
