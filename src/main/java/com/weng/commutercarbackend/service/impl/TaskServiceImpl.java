package com.weng.commutercarbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weng.commutercarbackend.mapper.BusMapper;
import com.weng.commutercarbackend.mapper.RouteMapper;
import com.weng.commutercarbackend.mapper.TaskMapper;
import com.weng.commutercarbackend.model.entity.Route;
import com.weng.commutercarbackend.model.entity.Task;
import com.weng.commutercarbackend.model.vo.TaskVO;
import com.weng.commutercarbackend.service.TaskService;
import lombok.RequiredArgsConstructor;
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
                .LicensePlate(busMapper.selectById(task.getBusId()).getLicensePlate())
                .status(task.getStatus())
                .build()).toList();
    }

    @Override
    public List<TaskVO> getTaskByTimeAndNumber(LocalDateTime time, Integer number) {
        LambdaQueryWrapper<Task>taskLambdaQueryWrapper=new LambdaQueryWrapper<>();
        taskLambdaQueryWrapper.eq(Task::getTime,time);
        //查询所有路线号和时间匹配且上车站点人数加起来不超过40人的路线
        LambdaQueryWrapper<Route>routeLambdaQueryWrapper=new LambdaQueryWrapper<>();
        routeLambdaQueryWrapper.eq(Route::getNumber,number);
        List<Route> routeList1 = routeMapper.selectList(routeLambdaQueryWrapper);
        List<Route> routeList2 = routeList1.stream().filter(route -> {
            //根据路线不同，要求所有的上车站点人数加起来不超过40人
            int total = switch (number) {
                case 1, 2 -> route.getChangan() + route.getDongmen();
                case 3 -> route.getYouyi() + route.getGaoxin() + route.getZiwei();
                case 4 -> route.getYouyi() + route.getGuojiyi();
                default -> 0;
            };
            return total <= 40;
        }).toList();
        List<Long> routeIdList = routeList2.stream().map(Route::getId).toList();

        taskLambdaQueryWrapper.in(Task::getRouteId,routeIdList);
        List<Task> taskList = taskMapper.selectList(taskLambdaQueryWrapper);
        return taskList.stream().map(task -> TaskVO.builder()
                .id(task.getId())
                .time(task.getTime())
                .driverId(task.getDriverId())
                .routeId(task.getRouteId())
                .number(routeMapper.selectById(task.getRouteId()).getNumber())
                .busId(task.getBusId())
                .LicensePlate(busMapper.selectById(task.getBusId()).getLicensePlate())
                .status(task.getStatus())
                .build()).toList();
    }

    @Override
    public Task updateTaskStatus(Long taskId) {
        Task task = taskMapper.selectById(taskId);
        task.setStatus(2);
        taskMapper.updateById(task);
        return task;
    }




}




