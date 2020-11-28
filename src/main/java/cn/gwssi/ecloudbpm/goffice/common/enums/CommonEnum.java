package cn.gwssi.ecloudbpm.goffice.common.enums;


/**
 * @author Pong weipengxiang@gwssi.com.cn
 * @date 2020/11/28 15:22
 **/
public enum CommonEnum {

    YES("1","是"),
    NO("0","否");

    private String code;
    private String name;

    CommonEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
