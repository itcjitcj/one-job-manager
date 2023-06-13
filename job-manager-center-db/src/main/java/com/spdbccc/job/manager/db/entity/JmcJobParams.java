package com.spdbccc.job.manager.db.entity;

import lombok.Data;

import java.util.Date;
@Data
public class JmcJobParams {
    private Long id;

    private String logPrefix;

    private String cluster;

    private String node;

    private String username;

    private String password;

    private String yarnUrl;

    private String queue;

    private String webhdfsUrl;

    private String hdfsPathPrefix;

    private String cpuThreshold;

    private String memoryThreshold;

    private String createUser;

    private Date createTime;

    private String updateUser;

    private Date updateTime;

    private String isDeleted;

    }