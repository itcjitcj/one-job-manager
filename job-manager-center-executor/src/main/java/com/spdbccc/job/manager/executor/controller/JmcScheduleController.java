package com.spdbccc.job.manager.executor.controller;

import com.spdbccc.job.manager.core.common.pojo.Result;
import com.spdbccc.job.manager.core.common.pojo.ResultGenerator;
import com.spdbccc.job.manager.executor.service.schedule.impl.JmcSchduleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jmc/schedule/")
public class JmcScheduleController {

    @Autowired
    private JmcSchduleServiceImpl jmcSchduleService;


    @GetMapping("task/stop")
    public Result stopTask(String taskName){
        return ResultGenerator.genSuccessResult(jmcSchduleService.stopTask(taskName));
    }

    @GetMapping("delete")
    public Result delete(String taskName){
        return ResultGenerator.genSuccessResult(jmcSchduleService.deleteSchedule(taskName));
    }

    @GetMapping("flush")
    public Result flush(){
        return ResultGenerator.genSuccessResult(jmcSchduleService.flushSchedule());
    }

    @GetMapping("submitTask/{taskId}")
    public Result submitTask(@PathVariable Long taskId){
        return ResultGenerator.genSuccessResult(jmcSchduleService.submitTask(taskId));
    }


}
