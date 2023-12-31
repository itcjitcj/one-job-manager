package com.spdbccc.job.manager.service.handle;

import com.spdbccc.job.manager.core.common.pojo.Msg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author progr1mmer
 */
@ControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Msg errorHandler(Exception e) {
        logger.error(e.getMessage(), e);
        return Msg.create(-999, "请求出错", e.getMessage());
    }

}
