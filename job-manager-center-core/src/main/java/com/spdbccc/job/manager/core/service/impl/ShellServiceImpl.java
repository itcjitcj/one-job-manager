package com.spdbccc.job.manager.core.service.impl;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.spdbccc.job.manager.core.common.entity.job.ParamsEntity;
import com.spdbccc.job.manager.core.common.entity.job.TaskEntity;
import com.spdbccc.job.manager.core.common.pojo.HttpYarnApp;
import com.spdbccc.job.manager.core.service.inter.ShellService;
import com.spdbccc.job.manager.core.util.MyDateUtils;
import com.spdbccc.job.manager.core.util.YarnApiUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Collections;


public class ShellServiceImpl implements ShellService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShellServiceImpl.class);
    private TaskEntity taskEntity;
    private ParamsEntity paramsEntity;
    private String logLocation;
    private String statusLocation;

    private Connection conn = null;
    private Session session = null;
    private Process process = null;

    /**
     * 执行远程服务器shell命令
     *
     * @param taskEntity
     * @param paramsEntity
     */
    @Override
    public void runShell(TaskEntity taskEntity, ParamsEntity paramsEntity) {
        LOGGER.info("task start:::"+taskEntity.getCommand());
        this.taskEntity = taskEntity;
        this.paramsEntity = paramsEntity;
        JSONObject statusJo=new JSONObject();
        //此处需要新增两个文件，一个是log 记录文件，一个是任务执行状态的文件，可以用它进行任务的状态管理和日志查看
        String filePrefix = paramsEntity.getLogPrefix() + taskEntity.getJobId() + FileUtil.FILE_SEPARATOR +
                taskEntity.getTaskId() + FileUtil.FILE_SEPARATOR + MyDateUtils.getNowTime();
        logLocation = filePrefix + ".log";
        statusLocation = filePrefix + ".status";
        if (!FileUtil.isDirectory(paramsEntity.getLogPrefix())) {
            FileUtil.mkdir(paramsEntity.getLogPrefix());
            if (!FileUtil.isFile(logLocation)) {
                FileUtil.touch(logLocation);
            }
            if (!FileUtil.isFile(statusLocation)) {
                FileUtil.touch(statusLocation);
            }
        }
        try {
            statusJo.put("taskId",taskEntity.getTaskId());
            statusJo.put("command",taskEntity.getCommand());
            statusJo.put("taskStatus","0");
            FileUtil.writeUtf8String(statusJo.toString(), statusLocation);
            command(taskEntity.getCommand(),statusJo);
            if (taskEntity.isGetYarnLog()) {
//                getYarnApp(paramsEntity.getYarnUrl());
                Assert.isTrue(StringUtils.isNotBlank(taskEntity.getApplicationId()), "applicationId 为空,无法查询yarnlogs");
                command("yarn logs -applicationId " + taskEntity.getApplicationId(),statusJo);
            }
            statusJo.put("taskStatus","1");
            FileUtil.writeUtf8String(statusJo.toString(), statusLocation);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.warn(e.toString());
            LOGGER.warn(taskEntity.toString());
            FileUtil.appendUtf8Lines(Collections.singletonList(MyDateUtils.getNowTime()+": "+e), logLocation);
            statusJo.put("taskStatus","2");
            FileUtil.writeUtf8String(statusJo.toString(), statusLocation);
        } finally {
            if (session != null) {
                session.close();
            }
            if (conn != null) {
                conn.close();
            }
            killYarnAppShell(statusJo);
            if(statusJo.getString("taskStatus").equals("0")){
                statusJo.put("taskStatus","2");
                FileUtil.writeUtf8String(statusJo.toString(), statusLocation);
            }
        }
        LOGGER.info("task end:::"+taskEntity.getCommand());
    }

    @Override
    public boolean stopShell(TaskEntity taskEntity, ParamsEntity paramsEntity) {
        this.taskEntity = taskEntity;
        this.paramsEntity = paramsEntity;
        return killYarnAppShell(new JSONObject());
    }

    public void command(String command,JSONObject statusJo) {
        try {
            if (StringUtils.isNotBlank(paramsEntity.getNode())) {
                conn = new Connection(paramsEntity.getNode());
                conn.connect(null, 5000, 30000);
                conn.authenticateWithPassword(paramsEntity.getUser(), paramsEntity.getPassword());
                session = conn.openSession();
            }
            FileUtil.appendUtf8Lines(Collections.singletonList(MyDateUtils.getNowTime()+": "), logLocation);
            FileUtil.appendUtf8Lines(Collections.singletonList(MyDateUtils.getNowTime()+": "+"============================================"), logLocation);
            FileUtil.appendUtf8Lines(Collections.singletonList(MyDateUtils.getNowTime()+": "+"以下为" + command + "执行情况："), logLocation);
            FileUtil.appendUtf8Lines(Collections.singletonList(MyDateUtils.getNowTime()+": "+"============================================"), logLocation);
            BufferedReader reader;
            if (StringUtils.isNotBlank(paramsEntity.getNode())) {
                session.execCommand(command);
                reader = new BufferedReader(new InputStreamReader(new SequenceInputStream(session.getStderr(),session.getStdout())));
            } else {
                process = Runtime.getRuntime().exec(command);
                reader = new BufferedReader(new InputStreamReader(new SequenceInputStream(process.getErrorStream(),process.getInputStream())));
            }
            String line;
            boolean flag=true;
            while ((line = reader.readLine()) != null) {
                if(flag&&line.contains("application_")&&line.contains("(state:")){
                    String appId = line.substring(line.indexOf("application_"), line.indexOf("(state:")).trim();
                    statusJo.put("applicationId",appId);
                    taskEntity.setApplicationId(appId);
                    FileUtil.writeUtf8String(statusJo.toString(), statusLocation);
                    flag=false;
                }
                FileUtil.appendUtf8Lines(Collections.singletonList(MyDateUtils.getNowTime()+": "+line), logLocation);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.warn(e.toString());
            FileUtil.appendUtf8Lines(Collections.singletonList(MyDateUtils.getNowTime()+": "+e), logLocation);
        } finally {
            if (session != null) {
                session.close();
            }
            if (conn != null) {
                conn.close();
            }
            if (process != null) {
                process.destroy();
            }
        }
    }


    private void getYarnApp(String yarnUrl) {
        HttpYarnApp httpYarnApp = YarnApiUtils.getApp(yarnUrl, paramsEntity.getUser(), taskEntity.getTaskName(), 2, paramsEntity.getQueue(), false);
        if (httpYarnApp != null) {
            String applicationId = httpYarnApp.getId();
            taskEntity.setApplicationId(applicationId);
        }
    }

    private boolean killYarnAppShell(JSONObject statusJo) {
        try {
            String applicationId = null;
            if(StringUtils.isNotBlank(taskEntity.getApplicationId())){
                applicationId=taskEntity.getApplicationId();
            }else if(statusJo.containsKey("applicationId")){
                applicationId=statusJo.getString("applicationId");
            }else{
//                HttpYarnApp httpYarnApp = YarnApiUtils.getApp(paramsEntity.getYarnUrl(), paramsEntity.getUser(), taskEntity.getTaskName(), 2, paramsEntity.getQueue(), true);
//                if (httpYarnApp != null) {
//                    applicationId = httpYarnApp.getId();
//                }
                statusJo=JSON.parseObject(FileUtil.readUtf8String(statusLocation));
                applicationId=statusJo.getString("applicationId");
            }
            Assert.isTrue(StringUtils.isNotBlank(applicationId), "applicationId 为空,无法查询yarnlogs");
            command("yarn application --kill " + applicationId,statusJo);
            return true;
        } catch (Exception e) {
            LOGGER.warn("killYarnAppShell:"+e.getMessage());
            return false;
        }
    }

}
