package com.telecom.ecloudbpm.goffice.common.enums;

import com.telecom.ecloudframework.base.api.constant.IStatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * @title: BaseExceptionEnum
 * @author: 辛元方
 * @date: 2020/7/13 09:54
 */
@AllArgsConstructor
@Getter
public enum BaseExceptionEnum implements IStatusCode {

    FILE_EXPORT_FAILED(BaseExceptionEnum.BASE_ERROR_CODE + 1,"导出文件失败");

    private static final int RESERVED_BITS = 100;
    //预留1000个错误码
    private static final int CUSTOM_ERROR_CODE = 1000;
    private static final int MODULE_NUM = 25;
    //任务管理
    private static final int BASE_ERROR_CODE = MODULE_NUM * RESERVED_BITS * CUSTOM_ERROR_CODE;
    private String code;
    private String desc;
    private String system;

    private BaseExceptionEnum(int code, String desc) {
        this.code = String.valueOf(code);
        this.desc = desc;
        this.system = "BASE";
    }
}
