package net.qixiaowei.operate.cloud.api.vo.salary;

import lombok.Data;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryItemDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryPayDTO;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * @description 工资项导入临时数据VO
 * @Author hzk
 * @Date 2022-12-20 18:54
 **/
@Data
public class SalaryPayImportTempDataVO {
    /**
     * excel请求头的headMap
     */
    private Map<Integer, String> headMap;
    /**
     * thirdLevelItem为key，SalaryItemDTO为值的map
     */
    private Map<String, SalaryItemDTO> salaryItemOfThirdLevelItemMap;
    /**
     * salaryItemId为key，SalaryItemDTO为值的map
     */
    private Map<Long, SalaryItemDTO> salaryItemMap;
    /**
     * employeeCode的set集合
     */
    private Set<String> employeeCodes = new HashSet<>();
    /**
     * employeeCode为key，SalaryItemDTO为值的map
     */
    private Map<String, SalaryPayImportOfEmpDataVO> employeeTempDataOfCode = new HashMap<>();
    /**
     * 检查员工发薪年月重复项的辅助Set集合
     */
    private Set<String> salarySet = new HashSet<>();
    /**
     * 员工code+发薪年的辅助Set集合
     */
    private Set<String> employeeCodeAndPayYearSet = new HashSet<>();
    /**
     * employeeId、Year和Month为key，SalaryPayDTO为值的map
     */
    private Map<String, SalaryPayDTO> employeeIdAndYearAndMonthOfSalaryPayMap = new HashMap<>();

}

