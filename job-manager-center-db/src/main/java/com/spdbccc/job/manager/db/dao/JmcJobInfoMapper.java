package com.spdbccc.job.manager.db.dao;

import com.spdbccc.job.manager.db.entity.JmcJobInfo;
import com.spdbccc.job.manager.db.entity.JmcJobInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface JmcJobInfoMapper {
    int insert(JmcJobInfo record);

    int insertSelective(JmcJobInfo record);

    List<JmcJobInfo> selectByExample(JmcJobInfoExample example);

    JmcJobInfo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") JmcJobInfo record, @Param("example") JmcJobInfoExample example);

    int updateByExample(@Param("record") JmcJobInfo record, @Param("example") JmcJobInfoExample example);

    int updateByPrimaryKeySelective(JmcJobInfo record);

    int updateByPrimaryKey(JmcJobInfo record);
}