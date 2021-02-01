package com.telecom.ecloudbpm.goffice.common.response;

/**
 * @author pong
 * @date 2020-08-31
 */
public class SuccessResultData extends ResultData {
    public SuccessResultData() {
        super(true, DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MESSAGE, (Object)null);
    }

    public SuccessResultData(Object object) {
        super(true, DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MESSAGE, object);
    }

    public SuccessResultData(Integer code, String message, Object object) {
        super(true, code, message, object);
    }
}
