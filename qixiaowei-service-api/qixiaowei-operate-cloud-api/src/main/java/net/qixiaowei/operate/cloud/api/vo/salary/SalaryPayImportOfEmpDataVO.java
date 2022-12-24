package net.qixiaowei.operate.cloud.api.vo.salary;

import lombok.Data;


/**
 * @description 工资项导入员工临时数据VO
 * @Author hzk
 * @Date 2022-12-23 15:54
 **/
@Data
public class SalaryPayImportOfEmpDataVO {
    /**
     * 员工ID
     */
    private Long employeeId;
    /**
     * 员工姓名
     */
    private String employeeName;

}

