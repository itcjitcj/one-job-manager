package com.spdbccc.job.manager.executor.service.schedule;

public interface JmcScheduleService {

    public String stopTask(String taskName);
    public String deleteSchedule(String taskName);
    public String flushSchedule();
    public String submitTask(Long taskId);

}
