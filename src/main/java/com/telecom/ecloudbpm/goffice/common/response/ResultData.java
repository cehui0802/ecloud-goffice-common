package com.telecom.ecloudbpm.goffice.common.response;

/**
 * @author pong
 * @date 2020-08-31
 */
public class ResultData {
    public static final String DEFAULT_SUCCESS_MESSAGE = "请求成功";
    public static final String DEFAULT_ERROR_MESSAGE = "系统异常";
    public static final Integer DEFAULT_SUCCESS_CODE = 200;
    public static final Integer DEFAULT_ERROR_CODE = 500;
    private Boolean success;
    private Integer code;
    private String message;
    private Object data;

    public ResultData() {
    }

    public ResultData(Boolean success, Integer code, String message, Object data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static SuccessResultData success() {
        return new SuccessResultData();
    }

    public static SuccessResultData success(Object object) {
        return new SuccessResultData(object);
    }

    public static SuccessResultData success(Integer code, String message, Object object) {
        return new SuccessResultData(code, message, object);
    }

    public static ErrorResultData error(String message) {
        return new ErrorResultData(message);
    }

    public static ErrorResultData error(Integer code, String message) {
        return new ErrorResultData(code, message);
    }

    public static ErrorResultData error(Integer code, String message, Object object) {
        return new ErrorResultData(code, message, object);
    }

    public Boolean getSuccess() {
        return this.success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
