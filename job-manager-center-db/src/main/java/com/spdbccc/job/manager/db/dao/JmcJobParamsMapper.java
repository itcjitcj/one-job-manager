package com.spdbccc.job.manager.db.dao;

import com.spdbccc.job.manager.db.entity.JmcJobParams;

public interface JmcJobParamsMapper {
    JmcJobParams selectByPrimaryKey(Long id);
}