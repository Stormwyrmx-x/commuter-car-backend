package com.weng.commutercarbackend.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.weng.commutercarbackend.model.dto.TaskAddRequest;
import com.weng.commutercarbackend.model.dto.TaskPageRequest;
import com.weng.commutercarbackend.model.dto.TaskUpdateRequest;
import com.weng.commutercarbackend.model.entity.Task;
import com.weng.commutercarbackend.model.vo.TaskVO;

import java.time.LocalDateTime;
import java.util.List;

/**
* @author weng
* @description 针对表【task(工单表)】的数据库操作Service
* @createDate 2024-04-19 20:29:28
*/
public interface TaskService extends IService<Task> {

    List<TaskVO> getTaskByDriverId(Long id);

    List<TaskVO> getTaskByTimeAndNumber(LocalDateTime time, Integer number);

    Task updateTaskStatus(Long taskId);

    void cancelTask(Long taskId);

    Page<TaskVO> listTaskByPage(TaskPageRequest taskPageRequest);

    void addTask(TaskAddRequest taskAddRequest);

    void deleteTaskById(Long id);

    void updateTaskById(TaskUpdateRequest taskUpdateRequest);
}
