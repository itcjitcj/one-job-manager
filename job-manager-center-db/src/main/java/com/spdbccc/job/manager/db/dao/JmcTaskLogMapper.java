package com.spdbccc.job.manager.db.dao;

import com.spdbccc.job.manager.db.entity.JmcTaskLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface JmcTaskLogMapper {
    JmcTaskLog selectByPrimaryKey(Long id);

    @Insert("insert into jmc_task_log(task_status, taskid, application_id, command) values(#{task_status}, #{taskid}, #{application_id}, #{command}) ")
    int insertJmcTaskLog(JmcTaskLog jmcTaskLog);

    @Update("UPDATE jmc_task_log SET task_status=#{task_status}, taskid=#{taskid} , application_id=#{application_id}, command=#{command}WHERE id=#{id}")
    int updateJmcTaskLog(JmcTaskLog jmcTaskLog);
}