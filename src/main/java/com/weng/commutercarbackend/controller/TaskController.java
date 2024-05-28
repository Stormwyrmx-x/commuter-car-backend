package com.weng.commutercarbackend.controller;


import com.weng.commutercarbackend.common.Result;
import com.weng.commutercarbackend.mapper.BusMapper;
import com.weng.commutercarbackend.mapper.RouteMapper;
import com.weng.commutercarbackend.model.dto.TaskAddRequest;
import com.weng.commutercarbackend.model.dto.TaskPageRequest;
import com.weng.commutercarbackend.model.dto.TaskUpdateRequest;
import com.weng.commutercarbackend.model.entity.Task;
import com.weng.commutercarbackend.model.vo.TaskVO;
import com.weng.commutercarbackend.service.TaskService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final RouteMapper routeMapper;
    private final BusMapper busMapper;

    /**
     * 给定路线号、工单状态、司机id，分页查询所有的工单
     */
    @GetMapping("/page")
    public Result<Page<TaskVO>> listTaskByPage(@Validated TaskPageRequest taskPageRequest){
        Page<TaskVO> taskVOPage = taskService.listTaskByPage(taskPageRequest);
        return Result.success(taskVOPage);
    }

    @PostMapping
    public Result<Boolean> addTask(@RequestBody @Validated TaskAddRequest taskAddRequest){
        taskService.addTask(taskAddRequest);
        return Result.success(true);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteTask(@Min(value = 1,message = "id必须大于0") @PathVariable Long id){
        taskService.deleteTaskById(id);
        return Result.success(true);
    }

    @GetMapping("/{id}")
    public Result<TaskVO> getTaskById(@Min(value = 1,message = "id必须大于0") @PathVariable Long id){
        Task task = taskService.getById(id);
        TaskVO taskVO = TaskVO.builder()
                .id(task.getId())
                .time(task.getTime())
                .driverId(task.getDriverId())
                .routeId(task.getRouteId())
                .number(routeMapper.selectById(task.getRouteId()).getNumber())
                .busId(task.getBusId())
                .status(task.getStatus())
                .build();
        return Result.success(taskVO);
    }


    @PutMapping
    public Result<Boolean> updateTask(@RequestBody @Validated TaskUpdateRequest taskUpdateRequest){
        taskService.updateTaskById(taskUpdateRequest);
        return Result.success(true);
    }




}
