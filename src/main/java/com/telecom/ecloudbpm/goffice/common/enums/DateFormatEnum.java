package com.telecom.ecloudbpm.goffice.common.enums;

public enum DateFormatEnum {

    YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),
    YYYY_MM_DD("yyyy-MM-dd");

    private String name;

    DateFormatEnum(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}

