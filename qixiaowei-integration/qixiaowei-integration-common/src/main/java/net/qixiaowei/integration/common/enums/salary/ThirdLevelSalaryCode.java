package net.qixiaowei.integration.common.enums.salary;

public enum ThirdLevelSalaryCode {

    BASIC(1L, "基本工资"),
    STRATEGIC(2L, "战略奖"),
    PROJECT(3L, "项目奖"),
    PERFORMANCE(4L, "绩效奖金");

    private final Long salaryId;
    private final String thirdLevelItem;

    ThirdLevelSalaryCode(Long salaryId, String thirdLevelItem) {
        this.thirdLevelItem = thirdLevelItem;
        this.salaryId = salaryId;
    }

    public String getThirdLevelItem() {
        return thirdLevelItem;
    }

    public Long getSalaryId() {
        return salaryId;
    }

    public static Boolean containThirdItems(String thirdLevelItem) {
        for (ThirdLevelSalaryCode salaryCode : ThirdLevelSalaryCode.values()) {
            if (salaryCode.getThirdLevelItem().equals(thirdLevelItem)) {
                return true;
            }
        }
        return false;
    }

    public static String containIds(Long salaryId) {
        for (ThirdLevelSalaryCode salaryCode : ThirdLevelSalaryCode.values()) {
            if (salaryCode.getSalaryId().equals(salaryId)) {
                return salaryCode.getThirdLevelItem();
            }
        }
        return null;
    }
}
