package com.weng.commutercarbackend.websocket;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.weng.commutercarbackend.mapper.DriverMapper;
import com.weng.commutercarbackend.mapper.PassengerMapper;
import com.weng.commutercarbackend.mapper.RouteMapper;
import com.weng.commutercarbackend.model.entity.Driver;
import com.weng.commutercarbackend.model.entity.Passenger;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@ServerEndpoint("/ws/{sid}")
@Slf4j
public class WebSocketServer {
    private static Map<String, Session> sessionMap = new HashMap<>();
    private static PassengerMapper passengerMapper;
    private static RouteMapper routeMapper;
    private static DriverMapper driverMapper;
    private static StringRedisTemplate stringRedisTemplate;

    @Autowired
    public void setPassengerMapper(PassengerMapper passengerMapper) {
        WebSocketServer.passengerMapper = passengerMapper;
    }
    @Autowired
    public void setStopMapper(RouteMapper routeMapper) {
        WebSocketServer.routeMapper = routeMapper;
    }
    @Autowired
    public void setDriverMapper(DriverMapper driverMapper) {
        WebSocketServer.driverMapper = driverMapper;
    }
    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        WebSocketServer.stringRedisTemplate = stringRedisTemplate;
    }

    @OnOpen
    public void open(Session session, @PathParam(value = "sid") String sid) {
        log.info("建立连接，{}", sid);
        sessionMap.put(sid, session);
    }

    @OnMessage
    public void message(@PathParam(value = "sid") String sid, String message) {
        log.info("收到了消息，{}", message);
    }

    /**
     * 司机/乘客退出登录
     * @param sid
     */
    @OnClose
    public void close(@PathParam(value = "sid") String sid) {
        // 如果是司机关闭连接，那么更改司机的routeId。同时清空redis表
        if (sid.startsWith("driver_")) {
            LambdaUpdateWrapper<Driver>driverLambdaUpdateWrapper=new LambdaUpdateWrapper<>();
            driverLambdaUpdateWrapper.eq(Driver::getId,Integer.parseInt(sid.substring(7)));
            driverLambdaUpdateWrapper.set(Driver::getRouteId,0);
            driverLambdaUpdateWrapper.set(Driver::getUpdateTime, LocalDateTime.now());
            driverMapper.update(driverLambdaUpdateWrapper);

            stringRedisTemplate.delete(sid);
        }
        // 如果是乘客关闭连接，那么更改乘客对应的driverId、routeId，清除上下车站点。同时清空redis表
        else if (sid.startsWith("passenger_")) {
            LambdaUpdateWrapper<Passenger> passengerLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            passengerLambdaUpdateWrapper.eq(Passenger::getId, Integer.parseInt(sid.substring(10)));
            passengerLambdaUpdateWrapper.set(Passenger::getDriverId, 0);
            passengerLambdaUpdateWrapper.set(Passenger::getRouteId, 0);
            passengerLambdaUpdateWrapper.set(Passenger::getGetonStationName, null);
            passengerLambdaUpdateWrapper.set(Passenger::getGetoffStationName, null);
            passengerLambdaUpdateWrapper.set(Passenger::getUpdateTime, LocalDateTime.now());
            passengerMapper.update(passengerLambdaUpdateWrapper);

            stringRedisTemplate.delete(sid);
        }
        else {
            log.warn("sid格式错误，{}", sid);
        }
        log.info("关闭连接，{}", sid);
        sessionMap.remove(sid);
    }

    public void sendToAll(String message) throws IOException {
        Collection<Session> values = sessionMap.values();
        if (!CollectionUtils.isEmpty(values)) {
            for (Session value : values) {
                value.getBasicRemote().sendText(message);
            }
        }
    }

    public void sendToUser(String sid, String message) throws IOException {
        Session session = sessionMap.get(sid);
        if (session != null) {
            session.getBasicRemote().sendText(message);
        }
        else {
            log.warn("No session found for sid: {}", sid);
        }
    }

}
