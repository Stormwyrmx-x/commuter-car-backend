package com.weng.commutercarbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weng.commutercarbackend.common.ResultCodeEnum;
import com.weng.commutercarbackend.exception.BusinessException;
import com.weng.commutercarbackend.mapper.*;
import com.weng.commutercarbackend.model.dto.TaskAddRequest;
import com.weng.commutercarbackend.model.dto.TaskPageRequest;
import com.weng.commutercarbackend.model.dto.TaskUpdateRequest;
import com.weng.commutercarbackend.model.entity.Route;
import com.weng.commutercarbackend.model.entity.Task;
import com.weng.commutercarbackend.model.vo.TaskVO;
import com.weng.commutercarbackend.service.TaskService;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
* @author weng
* @description 针对表【task(工单表)】的数据库操作Service实现
* @createDate 2024-04-19 20:29:28
*/
@Service
@RequiredArgsConstructor
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task>
    implements TaskService {

    private final TaskMapper taskMapper;
    private final BusMapper busMapper;
    private final RouteMapper routeMapper;
    private final DriverMapper driverMapper;
    private final PassengerMapper passengerMapper;


    @Override
    public List<TaskVO> getTaskByDriverId(Long id) {
        LambdaQueryWrapper<Task>taskLambdaQueryWrapper=new LambdaQueryWrapper<>();
        taskLambdaQueryWrapper.eq(Task::getDriverId,id);
        //查询状态为1(未执行)的工单
        taskLambdaQueryWrapper.eq(Task::getStatus,1);
        List<Task> taskList = taskMapper.selectList(taskLambdaQueryWrapper);
        return taskList.stream().map(task -> TaskVO.builder()
                .id(task.getId())
                .time(task.getTime())
                .driverId(task.getDriverId())
                .routeId(task.getRouteId())
                .number(routeMapper.selectById(task.getRouteId()).getNumber())
                .busId(task.getBusId())
                .licensePlate(busMapper.selectById(task.getBusId()).getLicensePlate())
                .status(task.getStatus())
                .build()).toList();
    }

    @Override
    public List<TaskVO> getTaskByTimeAndNumber(LocalDateTime time, Integer number) {
        LambdaQueryWrapper<Task>taskLambdaQueryWrapper=new LambdaQueryWrapper<>();
        taskLambdaQueryWrapper.eq(time!=null,Task::getTime,time);
        //查询所有路线号和时间匹配且上车站点人数加起来不超过40人的路线
        LambdaQueryWrapper<Route>routeLambdaQueryWrapper=new LambdaQueryWrapper<>();
        routeLambdaQueryWrapper.eq(number!=null,Route::getNumber,number);
        List<Route> routeList = routeMapper.selectList(routeLambdaQueryWrapper);
        if (number!=null){
            routeList = routeList.stream().filter(route -> {
                //根据路线不同，要求所有的上车站点人数加起来不超过40人
                int total = switch (number) {
                    case 1, 2 -> route.getChangan() + route.getDongmen();
                    case 3 -> route.getYouyi() + route.getGaoxin() + route.getZiwei();
                    case 4 -> route.getYouyi() + route.getGuojiyi();
                    default -> 0;
                };
                return total <= 40;
            }).toList();
        }
        List<Long> routeIdList = routeList.stream().map(Route::getId).toList();
        //如果没有符合条件的路线，直接返回空List
        if (routeIdList.isEmpty()){
            return List.of();
        }
        taskLambdaQueryWrapper.in(Task::getRouteId,routeIdList);
        List<Task> taskList = taskMapper.selectList(taskLambdaQueryWrapper);
        return taskList.stream().map(task -> TaskVO.builder()
                .id(task.getId())
                .time(task.getTime())
                .driverId(task.getDriverId())
                .routeId(task.getRouteId())
                .number(routeMapper.selectById(task.getRouteId()).getNumber())
                .busId(task.getBusId())
                .licensePlate(busMapper.selectById(task.getBusId()).getLicensePlate())
                .status(task.getStatus())
                .build()).toList();
    }

    @Override
    public Task updateTaskStatus(Long taskId) {
        Task task = taskMapper.selectById(taskId);
        task.setStatus(2);
        task.setUpdateTime(LocalDateTime.now());
        taskMapper.updateById(task);
        return task;
    }

    @Override
    public Page<TaskVO> listTaskByPage(TaskPageRequest taskPageRequest) {
        Long driverId = taskPageRequest.driverId();
        Integer status = taskPageRequest.status();
        Integer number = taskPageRequest.number();

        LambdaQueryWrapper<Task>taskLambdaQueryWrapper=new LambdaQueryWrapper<>();
        taskLambdaQueryWrapper.eq(driverId!=null,Task::getDriverId,driverId);
        taskLambdaQueryWrapper.eq(status!=null,Task::getStatus,status);

        LambdaQueryWrapper<Route>routeLambdaQueryWrapper=new LambdaQueryWrapper<>();
        routeLambdaQueryWrapper.eq(number!=null,Route::getNumber,number);
        List<Route> routeList = routeMapper.selectList(routeLambdaQueryWrapper);
        List<Long> routeIdList = routeList.stream().map(Route::getId).toList();
        //如果没有符合条件的路线，直接返回空Page
        if (routeIdList.isEmpty()){
            return new Page<>();
        }
        taskLambdaQueryWrapper.in(Task::getRouteId,routeIdList);

        //分页查询，先Task分页查询后，再构造Page<TaskVO>
        Page<Task>taskPage=new Page<>(taskPageRequest.current(),taskPageRequest.size());
        taskMapper.selectPage(taskPage,taskLambdaQueryWrapper);
        List<TaskVO> taskVOList = taskPage.getRecords().stream().map(task -> TaskVO.builder()
                .id(task.getId())
                .time(task.getTime())
                .driverId(task.getDriverId())
                .username(driverMapper.selectById(task.getDriverId()).getUsername())
                .name(driverMapper.selectById(task.getDriverId()).getName())
                .routeId(task.getRouteId())
                .number(routeMapper.selectById(task.getRouteId()).getNumber())
                .busId(task.getBusId())
                .licensePlate(busMapper.selectById(task.getBusId()).getLicensePlate())
                .status(task.getStatus())
                .build()).toList();
        Page<TaskVO>taskVOPage=new Page<>();
        taskVOPage.setRecords(taskVOList);
        taskVOPage.setCurrent(taskPage.getCurrent());
        taskVOPage.setSize(taskPage.getSize());
        taskVOPage.setTotal(taskPage.getTotal());
        return taskVOPage;
    }

    @Override
    public void addTask(TaskAddRequest taskAddRequest) {
        //添加工单时，同时添加对应的路线
        Route route = Route.builder()
                .number(taskAddRequest.number())
                .build();
        routeMapper.insert(route);
        Task task = Task.builder()
                .time(taskAddRequest.time())
                .driverId(taskAddRequest.driverId())
                .routeId(route.getId())
                .busId(taskAddRequest.busId())
                .status(taskAddRequest.status())
                .build();
        taskMapper.insert(task);
    }

    @Override
    public void deleteTaskById(Long id) {
        //如果有人在执行工单的路线或者乘客在乘坐工单上的路线，不允许删除
        Long routeId = taskMapper.selectById(id).getRouteId();
        driverMapper.selectList(new LambdaQueryWrapper<>()).forEach(driver -> {
            if (driver.getRouteId().equals(routeId)){
                throw new BusinessException(ResultCodeEnum.SYSTEM_ERROR,"有司机在执行工单上的路线，不允许删除");
            }
        });
        passengerMapper.selectList(new LambdaQueryWrapper<>()).forEach(passenger -> {
            if (passenger.getRouteId().equals(routeId)){
                throw new BusinessException(ResultCodeEnum.SYSTEM_ERROR,"有乘客在乘坐工单上的路线，不允许删除");
            }
        });
        Task task = taskMapper.selectById(id);
        //删除工单时，同时删除对应的路线
        routeMapper.deleteById(task.getRouteId());
        taskMapper.deleteById(id);
    }

    @Override
    public void updateTaskById(TaskUpdateRequest taskUpdateRequest) {
        //更新工单时，同时更新对应的路线中的路线号（routeId不变，但router表中对应routeId的number要变）
        LambdaUpdateWrapper<Route>routeLambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        routeLambdaUpdateWrapper.set(Route::getNumber,taskUpdateRequest.number())
                .set(Route::getUpdateTime,LocalDateTime.now())
                .eq(Route::getId,taskUpdateRequest.routeId());
        routeMapper.update(routeLambdaUpdateWrapper);


        LambdaUpdateWrapper<Task>taskLambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        taskLambdaUpdateWrapper.eq(Task::getId,taskUpdateRequest.id())
                .set(Task::getTime,taskUpdateRequest.time())
                .set(Task::getDriverId,taskUpdateRequest.driverId())
                .set(Task::getBusId,taskUpdateRequest.busId())
                .set(Task::getStatus,taskUpdateRequest.status())
                .set(Task::getUpdateTime,LocalDateTime.now());
        taskMapper.update(taskLambdaUpdateWrapper);

    }


}




