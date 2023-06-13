package com.spdbccc.job.manager.db.dao;

import com.spdbccc.job.manager.db.entity.JmcTaskInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface JmcTaskInfoMapper {
    JmcTaskInfo selectByPrimaryKey(Long id);

    @Select("select * from jmc_task_info where is_cron='${isCron}' and is_deleted='0' ")
    List<JmcTaskInfo> selectAllJmcTaskInfoByIsCron(String isCron);
}