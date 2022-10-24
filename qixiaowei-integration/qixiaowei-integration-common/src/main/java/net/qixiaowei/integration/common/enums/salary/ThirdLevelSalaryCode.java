package net.qixiaowei.integration.common.enums.salary;

public enum ThirdLevelSalaryCode {

    BASIC("基本工资", "基本工资"),
    STRATEGIC("战略奖", "战略奖"),
    PROJECT("项目奖", "项目奖"),
    PERFORMANCE("绩效奖金", "绩效奖金");

    private final String code;
    private final String info;

    ThirdLevelSalaryCode(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public static Boolean containThirdItems(String thirdLevelItem) {
        for (ThirdLevelSalaryCode salaryCode : ThirdLevelSalaryCode.values()) {
            if (salaryCode.getInfo().equals(thirdLevelItem)) {
                return true;
            }
        }
        return false;
    }
}
