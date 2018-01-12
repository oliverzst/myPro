package com.actec.bsms.entity;

/**
 * 目录类
 *
 * @author zhangst
 * @create 2017-11-28 10:29 AM
 */

public class Menu extends DataEntity<Menu> {

    private String name;	// 名称
    private String description;	// 描述
    private String inputType;	// 输入类型

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }
}
