package com.spdbccc.job.manager.executor.service.job;

import org.springframework.web.multipart.MultipartFile;

public interface JmcSparkService {

    public String uploadSparkJar(MultipartFile file, Long jobId);

}
