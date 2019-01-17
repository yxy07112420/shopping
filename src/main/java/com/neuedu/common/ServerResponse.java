package com.neuedu.common;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 创建高复用的对象
 */

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> {

    //状态码0---->成功
    private Integer status;
    //status=0时
    private T date;
    //提示信息
    private String msg;

    //无参构造方法
    private ServerResponse(){};
    //只含有状态码的构造方法
    private ServerResponse(Integer status){
        this.status = status;
    }
    //含有状态码和提示信息的构造方法
    private ServerResponse(Integer status,String msg){
        this.status = status;
        this.msg = msg;
    }
    //含有状态码,提示信息和date的构造方法
    private ServerResponse(Integer status,String msg,T date){
        this.status = status;
        this.msg = msg;
        this.date = date;
    }
    //判断接口是否访问成功
    @JsonIgnore
    public boolean isSuccess(){
        return this.status == ResponseCord.SUCCESS;
    }
    //当status=0时创建的方法
    public static ServerResponse responseIsSuccess(){
        return new ServerResponse(ResponseCord.SUCCESS);
    }
    //{"status":0,"msg":""}
    public static ServerResponse responseIsSuccess(String msg){
        return new ServerResponse(ResponseCord.SUCCESS,msg);
    }
    //{"status":0,"msg":"","date":}
    public static  <T> ServerResponse responseIsSuccess(String msg,T date){
        return new ServerResponse(ResponseCord.SUCCESS,msg,date);
    }
    //当status=1
    //当status=0时创建的方法
    public static ServerResponse responseIsError(){
        return new ServerResponse(ResponseCord.ERROR);
    }
    //{"status": }
    public static ServerResponse responseIsError(Integer status){
        return new ServerResponse(status);
    }
    //{"status":1,"msg":""}
    public static ServerResponse responseIsError(String msg){
        return new ServerResponse(ResponseCord.ERROR,msg);
    }
    //{"status": ,"msg":""}
    public static ServerResponse responseIsError(Integer status,String msg){
        return new ServerResponse(status,msg);
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public T getDate() {
        return date;
    }

    public void setDate(T date) {
        this.date = date;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
