package com.weng.commutercarbackend.config;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.weng.commutercarbackend.filter.JwtAuthenticationFilter;
import com.weng.commutercarbackend.mapper.UserMapper;
import com.weng.commutercarbackend.model.entity.User;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Lazy
    @Resource
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Resource
    private UserMapper userMapper;

    @Bean
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder,
                                                       UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .cors(httpSecurityCorsConfigurer ->
                        httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                //当一个HTTP请求到达应用程序时，Spring Security的过滤器链会按照它们在配置中定义的顺序开始处理这个请求。
                //在你的SecurityConfig.java文件中，authorizeHttpRequests方法定义的安全规则是在addFilterBefore方法之前调用的，所以这些规则会先被应用到请求上。
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests.requestMatchers("/**",
                                        //无需加上api前缀.The pattern must not contain the context path
                                        "/doc.html", "/webjars/**", "/v3/api-docs/**").permitAll()
                                .anyRequest().authenticated())
                .sessionManagement(sessionManagement -> sessionManagement
                        //默认是IF_REQUIRED,即如果需要就创建一个session。jwt信息包含了用户信息
                        //这里改为STATELESS，不创建session。因为每次请求携带的
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            LambdaQueryWrapper<User> enumUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
            enumUserLambdaQueryWrapper.eq(User::getUsername, username);
            User user = userMapper.selectOne(enumUserLambdaQueryWrapper);
            if (user == null) {
                throw new UsernameNotFoundException("用户名不存在");
            }
            return user;
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedMethods(List.of("*"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowedOrigins(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
