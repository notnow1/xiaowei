package net.qixiaowei.integration.common.enums.message;

/**
 * @description 业务类型枚举
 * @Author hzk
 * @Date 2022-12-09 19:15
 **/
public enum BusinessType {

    TENANT(1, "租户管理"),

    TARGET_SETTING(2, "目标制定"),

    PROCESS_MANAGEMENT(3, "过程管理"),

    PERFORMANCE_MANAGEMENT(4, "绩效管理"),

    USAGE_OF_RESULTS(5, "结果应用");


    private Integer code;
    private String info;

    BusinessType(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }


}
