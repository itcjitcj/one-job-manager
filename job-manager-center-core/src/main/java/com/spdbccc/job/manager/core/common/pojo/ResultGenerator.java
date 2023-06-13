package com.spdbccc.job.manager.core.common.pojo;

import com.spdbccc.job.manager.core.common.Constant;

public class ResultGenerator {

    public static Result genSuccessResult(){
        return new Result(Constant.Code.SUCCESS_CODE,Constant.Message.RETURN_SUCCESS);
    }

    public static Result genFailResult(){
        return new Result(Constant.Code.FAIL_CODE,Constant.Message.RETURN_FAILED);
    }

    public static  Result genSuccessResult(String message){
        return new Result(Constant.Code.SUCCESS_CODE,message);
    }

    public static Result genFailResult(String message){
        return new Result(Constant.Code.FAIL_CODE,message);
    }

    public static <T> Result<T> genSuccessResult(T data){
        return new Result(Constant.Code.SUCCESS_CODE,Constant.Message.RETURN_SUCCESS,data);
    }

    public static <T> Result<T> genFailResult(T data){
        return new Result(Constant.Code.FAIL_CODE,Constant.Message.RETURN_FAILED,data);
    }

    public static <T> Result<T> genSuccessResult(String message,T data){
        return new Result(Constant.Code.SUCCESS_CODE,message,data);
    }

    public static <T> Result<T> genFailResult(String message,T data){
        return new Result(Constant.Code.FAIL_CODE,message,data);
    }


}
