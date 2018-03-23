package com.actec.bsms.vo;

/**
 * 故障处理记录VO类
 *
 * @author zhangst
 * @create 2017-12-14 3:04 PM
 */

public class TroubleShootVo {

    private static final long serialVersionUID = 1L;

    public TroubleShootVo(){}

    private int moduleId; //故障模块ID

    private int number; //序号

    private String description; //故障现象描述

    private String process;  //故障处理过程描述

    private int isResolve; //故障是否解决

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public int getIsResolve() {
        return isResolve;
    }

    public void setIsResolve(int isResolve) {
        this.isResolve = isResolve;
    }

}
