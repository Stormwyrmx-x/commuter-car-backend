package com.weng.commutercarbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.weng.commutercarbackend.common.ResultCodeEnum;
import com.weng.commutercarbackend.exception.BusinessException;
import com.weng.commutercarbackend.mapper.UserMapper;
import com.weng.commutercarbackend.model.dto.auth.UserLoginRequest;
import com.weng.commutercarbackend.model.dto.auth.UserRegisterRequest;
import com.weng.commutercarbackend.model.entity.User;
import com.weng.commutercarbackend.model.vo.UserLoginVO;
import com.weng.commutercarbackend.service.UserService;
import com.weng.commutercarbackend.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author weng
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2024-01-01 16:53:33
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final Gson gson;
//    @Value("${application.wechat.appid}")
//    private String appid;
//    @Value("${application.wechat.secret}")
//    private String secret;

    /**
     * 登录
     *
     * @param userLoginRequest
     * @return
     */
    @Override
    public UserLoginVO login(UserLoginRequest userLoginRequest) {
        //1.根据用户名密码进行登录
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(userLoginRequest.username(), userLoginRequest.password());
        Authentication authenticationResponse = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        User user = (User) authenticationResponse.getPrincipal();
        //2.生成jwt令牌
        String token = jwtUtil.generateToken(user);
        //3.返回用户信息
        return UserLoginVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .name(user.getName())
                .phone(user.getPhone())
                .status(user.getStatus())
                .money(user.getMoney())
                .token(token)
                .build();
    }

    @Override
    public Long register(UserRegisterRequest registerRequest) {
        //1.密码和校验密码相同
        if (!Objects.equals(registerRequest.password(), registerRequest.checkPassword())) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR, "两次密码输入不一致");
        }
        //2.账号不能重复(查数据库)
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUsername, registerRequest.username());
        Long count = userMapper.selectCount(userLambdaQueryWrapper);
        if (count > 0) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR, "账号重复");
        }
        //3.存储到数据库
        User user = User.builder()
                .username(registerRequest.username())
                .password(passwordEncoder.encode(registerRequest.password()))
                .name(registerRequest.name())
                .phone(registerRequest.phone())
                .role(registerRequest.role())//这里的role是枚举类型，name()方法返回枚举常量的名称
                .build();
        userMapper.insert(user);//如果插入失败，它会抛出异常.而不是返回一个负数

        return user.getId();
    }

}




