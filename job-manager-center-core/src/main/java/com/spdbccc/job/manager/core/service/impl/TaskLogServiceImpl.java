package com.spdbccc.job.manager.core.service.impl;

import cn.hutool.core.io.FileUtil;
import com.spdbccc.job.manager.core.common.entity.job.LogEntity;
import com.spdbccc.job.manager.core.common.Constant;
import com.spdbccc.job.manager.core.service.inter.TaskLogService;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

public class TaskLogServiceImpl implements TaskLogService {
    @Override
    public String taskLogRead(LogEntity logEntity) {
        long fileSize = FileUtil.size(new File(logEntity.getLogLocation()));
        if (logEntity.getOffset() > fileSize) return Constant.Message.RETURN_NOINFO; // 校验偏移量是否小于文件大小
        String content = FileUtil.readString(new File(logEntity.getLogLocation()), Charset.defaultCharset());
        String[] lines = content.split("\\r?\\n"); // 按行拆分文件内容
        StringBuilder sb = logEntity.getLogInfo();
        for (int i = (int) logEntity.getOffset(); i < lines.length; i++) {
            sb.append(lines[i]).append("\n"); // 构建查询结果
        }
        logEntity.setOffset(fileSize); // 更新偏移量
        return Constant.Message.RETURN_SUCCESS;
    }

    @Override
    public String deleteLog(String logLocation) {
        if(!FileUtil.isFile(logLocation)) return  Constant.Message.RETURN_NOFILE;
        FileUtil.del(logLocation);
        return Constant.Message.RETURN_SUCCESS;
    }

    @Override
    public String deleteLogTimeRange(String logLocation, long time) {
        File dir = new File(logLocation);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File jobfile : files) {
                if (jobfile.isFile()) {
                    long lastModified = jobfile.lastModified();
                    if (lastModified < time) {
                        FileUtil.del(jobfile); // 删除文件
                        System.out.println("Deleted file: " + jobfile.getAbsolutePath());
                    }
                }else if(jobfile.isDirectory()){
                    for (File file : Objects.requireNonNull(jobfile.listFiles())) {
                        long lastModified = file.lastModified();
                        if (lastModified < time) {
                            FileUtil.del(file); // 删除文件
                            System.out.println("Deleted file: " + file.getAbsolutePath());
                        }
                    }
                    if(jobfile.listFiles() == null || Objects.requireNonNull(jobfile.listFiles()).length==0){
                        FileUtil.del(jobfile);
                        System.out.println("Deleted file: " + jobfile.getAbsolutePath());
                    }
                }
            }
        }
        return Constant.Message.RETURN_SUCCESS;
    }

    @Override
    public String deleteLogDiskRange(String logLocation, long diskRange) {
        long totalSize = FileUtil.size(new File(logLocation));
        while (totalSize > diskRange) {
            List<File> files = FileUtil.loopFiles(logLocation);
            File oldestFile = null;
            long oldestTimestamp = Long.MAX_VALUE;
            for (File file : files) {
                if (file.isFile() && file.lastModified() < oldestTimestamp) {
                    oldestFile = file;
                    oldestTimestamp = file.lastModified();
                }
            }
            if (oldestFile != null) {
                totalSize -= oldestFile.length();
                FileUtil.del(oldestFile);
            } else {
                break; // 目录下没有文件了，跳出循环
            }
        }
        return Constant.Message.RETURN_SUCCESS;
    }
}







