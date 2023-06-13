package com.spdbccc.job.manager.executor.controller;

import com.spdbccc.job.manager.core.common.pojo.Result;
import com.spdbccc.job.manager.core.common.pojo.ResultGenerator;
import com.spdbccc.job.manager.executor.service.job.impl.JmcSparkServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/jmc/job/")
public class JmcJobController {
    @Autowired
    JmcSparkServiceImpl jmcSparkService;

    @PostMapping("upload/spark")
    public Result uploadSparkJar(MultipartFile file, Long jobId){
        return ResultGenerator.genSuccessResult(jmcSparkService.uploadSparkJar(file,jobId));
    }

}
