package com.spdbccc.job.manager.executor.service.job.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import com.spdbccc.job.manager.core.common.Constant;
import com.spdbccc.job.manager.core.util.WebHdfsUtils;
import com.spdbccc.job.manager.db.dao.JmcJobInfoMapper;
import com.spdbccc.job.manager.db.dao.JmcJobParamsMapper;
import com.spdbccc.job.manager.db.entity.JmcJobInfo;
import com.spdbccc.job.manager.db.entity.JmcJobParams;
import com.spdbccc.job.manager.executor.service.job.JmcSparkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Service
public class JmcSparkServiceImpl implements JmcSparkService {
    @Autowired
    private JmcJobParamsMapper jmcJobParamsMapper;

    @Autowired
    private JmcJobInfoMapper jmcJobInfoMapper;

    public String uploadSparkJar(MultipartFile file,Long jobId){
        JmcJobParams jmcJobParams = jmcJobParamsMapper.selectByPrimaryKey(1L);
        JmcJobInfo jmcJobInfo = jmcJobInfoMapper.selectByPrimaryKey(jobId);
        String filename = file.getOriginalFilename();
        String hdfsPathDir = jmcJobParams.getHdfsPathPrefix() + jobId;

        int deleteFlag = WebHdfsUtils.delete(jmcJobParams.getWebhdfsUrl(), hdfsPathDir, jmcJobParams.getUsername());
        Assert.isTrue(deleteFlag>0,"删除hdfs原目录失败:"+hdfsPathDir);
        boolean mkdirFlag = WebHdfsUtils.mkdir(jmcJobParams.getWebhdfsUrl(), hdfsPathDir, jmcJobParams.getUsername(), jmcJobParams.getPassword());
        Assert.isTrue(mkdirFlag,"创建hdfs目录失败:"+hdfsPathDir);
        boolean uploadFlag = WebHdfsUtils.upload(file, jmcJobParams.getWebhdfsUrl(), hdfsPathDir, jmcJobParams.getUsername(), jmcJobParams.getPassword());
        Assert.isTrue(uploadFlag,"上传hdfs文件失败:"+hdfsPathDir+"/"+filename);

        jmcJobInfo.setHdfsPath(hdfsPathDir+"/"+filename);
        jmcJobInfo.setUpdateTime(new Date());
        jmcJobInfoMapper.updateByPrimaryKey(jmcJobInfo);
        return "uploadSparkJar "+ Constant.Message.RETURN_SUCCESS;
    }
}
