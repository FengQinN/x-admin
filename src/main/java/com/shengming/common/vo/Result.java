package com.shengming.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*此类的作用是统一前后端数据交互所创建*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    /*状态码*/
    private Integer code;
    /*Code说明*/
    private String message;
    /*数据*/
    private T data;
    /*方法封装，若状态表示成功则调用此方法*/
    public static <T>  Result<T> success(){
        return new Result<>(20000,"success",null);
    }

    public static <T>  Result<T> success(T data){
        return new Result<>(20000,"success",data);
    }

    public static <T>  Result<T> success(T data,String message){
        return new Result<>(20000,message,data);
    }

    public static <T>  Result<T> success(String message){
        return new Result<>(20000,message,null);
    }

    /*方法封装，若状态表示失败则调用此方法*/
    public static<T>  Result<T> fail(){
        return new Result<>(20001,"fail",null);
    }

    public static<T>  Result<T> fail(Integer code){
        return new Result<>(code,"fail",null);
    }

    public static<T>  Result<T> fail(Integer code, String message){
        return new Result<>(code,message,null);
    }

    public static<T>  Result<T> fail( String message){
        return new Result<>(20001,message,null);
    }
}
