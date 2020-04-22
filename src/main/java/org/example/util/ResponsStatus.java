package org.example.util;

/**
 * @ClassName ResponsStatus
 * @Description 状态结果枚举
 * @Date 2020/4/22 17:21
 * @Author wangyong
 * @Version 1.0
 **/
public enum ResponsStatus {

    SUCCESS(200,"成功"),
    FAIL(500,"失败");

    private String message;

    private Integer code;

    ResponsStatus(Integer code,String message){
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
