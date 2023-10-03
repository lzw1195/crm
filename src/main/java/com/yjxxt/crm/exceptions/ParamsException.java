package com.yjxxt.crm.exceptions;

/**
 * 自定义参数异常
 */
public class ParamsException extends RuntimeException {
    private Integer code=300;
    private String message="参数异常!";


    public ParamsException() {
        super("参数异常!");
    }

    public ParamsException(String message) {
        super(message);
        this.message = message;
    }

    public ParamsException(Integer code) {
        super("参数异常!");
        this.code = code;
    }

    public ParamsException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return message;
    }

    public void setMsg(String message) {
        this.message = message;
    }
}
