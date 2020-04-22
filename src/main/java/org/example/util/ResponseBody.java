package org.example.util;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName ResponseBody
 * @Description  返回体定义
 * @Date 2020/4/22 17:19
 * @Author wangyong
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBody<T> {


    private Integer code;

    private String message;

    private T data;

    public static <T> ResponseBody<T> ok(T t){
        return new ResponseBody<>(ResponsStatus.SUCCESS.getCode(),ResponsStatus.SUCCESS.getMessage(),t);
    }
}
