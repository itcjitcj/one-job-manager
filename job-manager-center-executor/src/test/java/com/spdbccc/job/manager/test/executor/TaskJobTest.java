package com.spdbccc.job.manager.test.executor;

import com.spdbccc.job.manager.core.common.Constant;
import com.spdbccc.job.manager.core.common.entity.job.LogEntity;
import com.spdbccc.job.manager.core.common.entity.job.ParamsEntity;
import com.spdbccc.job.manager.core.common.entity.job.TaskEntity;
import com.spdbccc.job.manager.core.common.pojo.HttpYarnApp;
import com.spdbccc.job.manager.core.common.pojo.YarnResourceInfo;
import com.spdbccc.job.manager.core.service.impl.ResourceQueryServiceImpl;
import com.spdbccc.job.manager.core.service.impl.TaskLogServiceImpl;
import com.spdbccc.job.manager.core.service.inter.ResourceQueryService;
import com.spdbccc.job.manager.core.service.job.ShellTaskJob;
import com.spdbccc.job.manager.core.util.SchedulerUtils;
import com.spdbccc.job.manager.core.util.YarnApiUtils;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskJobTest {

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final String yarnurl = "http://192.168.200.133:8088/";

    private final String logPrefix="/applog/airm/shell/";
    private final String node="192.168.200.133";


    @Test
    public void shellJobTest() throws InterruptedException {
        ArrayList<String> taskNames = new ArrayList<>();
        Long jobId = 1L;

        //提交对应的schedule任务
        for (int i = 0; i < 4; i++) {
            long taskId = new Date().getTime();
            String taskName = "root_" + jobId + "_" + taskId;
            taskNames.add(taskName);
            int n = (i + 1) * 100;
            shellRun(taskId, n);
        }

        boolean flag = true;
        long breaktime = 2000;
        LocalDateTime start = LocalDateTime.now();
        while (flag) {
            System.out.println("sleep");
            //判断schedule任务是否完成
            if (taskNames.stream().anyMatch(p -> SchedulerUtils.checkExists(p, Constant.JobGroup.SCRIPT_JOB))
            ) {
                Thread.sleep(5000);
            } else {
                flag = false;
            }

            //如果超过时间阈值，则将任务停止删除
            LocalDateTime end = LocalDateTime.now();
            long seconds = Duration.between(start, end).getSeconds();
            if (seconds > breaktime) {
                taskNames.forEach(p -> {
                    TaskEntity taskEntity = new TaskEntity(Long.parseLong(p.replaceAll("root_" + jobId + "_", "")), jobId, p);
                    ShellTaskJob.stop(taskEntity);
                });
            }
        }
        System.out.println("all task complete ~~~~~~~~~~~~~~~~~");
    }

    public void shellRun(Long taskId, int n) throws InterruptedException {
        Long jobId = 1L;
        String taskName = "root_" + jobId + "_" + taskId;
//        String command = "du -sh /*";
        String command = "spark-submit --master yarn --deploy-mode client --queue default --class org.apache.spark.examples.SparkPi --name " + taskName + " --conf spark.yarn.submit.waitAppCompletion=false hdfs://node01:9000/data/big-whale/storage/admin/spark-examples_2.12-3.0.2.jar " + n;

        TaskEntity taskEntity;
        if (n == 300) {
             taskEntity = new TaskEntity(taskId, jobId, taskName,command, (byte) 10);
        }else{
             taskEntity = new TaskEntity(taskId, jobId, taskName,command, (byte) 5);
        }
        ParamsEntity paramsEntity = new ParamsEntity(logPrefix, node, "root", "root", yarnurl,"default");
        ShellTaskJob.build( taskEntity, paramsEntity);
    }

    @Test
    public void testYarn() {
        String appId = "application_1681974856431_0011";
        HttpYarnApp app = YarnApiUtils.getAppWithAppId(yarnurl, appId);
        System.out.println(123);
    }

    @Test
    public void testYarnResource(){
        ResourceQueryService resourceQueryService = new ResourceQueryServiceImpl();
        YarnResourceInfo yarnResource = resourceQueryService.getYarnResource(new ParamsEntity("root",yarnurl, 10, 1024 * 8));
        System.out.println(yarnResource);
    }

    @Test
    public void testReadLog(){
        TaskLogServiceImpl taskLogService = new TaskLogServiceImpl();
        LogEntity logEntity = new LogEntity(405, logPrefix + "1/1682388580320.log");
        String s = taskLogService.taskLogRead(logEntity);
        System.out.println(s);
        System.out.println(logEntity.getLogInfo().toString());
        System.out.println(logEntity.getOffset());
    }

    @Test
    public void deleteLog(){
        TaskLogServiceImpl taskLogService = new TaskLogServiceImpl();
        String s = taskLogService.deleteLog(logPrefix + "1/1682388580320.log");
        System.out.println(s);
    }

    @Test
    public void deleteLogTimeRange(){
        TaskLogServiceImpl taskLogService = new TaskLogServiceImpl();
        String s = taskLogService.deleteLogTimeRange(logPrefix,new Date().getTime());
        System.out.println(s);
    }

    @Test
    public void deleteLogDiskRange(){
        TaskLogServiceImpl taskLogService = new TaskLogServiceImpl();
        String s = taskLogService.deleteLogDiskRange(logPrefix, 1024);
        System.out.println(s);
    }

}
