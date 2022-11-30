package net.qixiaowei.operate.cloud.mapper.salary;

import net.qixiaowei.operate.cloud.api.domain.salary.SalaryPay;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryPayDTO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * SalaryPayMapper接口
 *
 * @author Graves
 * @since 2022-11-17
 */
public interface SalaryPayMapper {
    /**
     * 查询工资发薪表
     *
     * @param salaryPayId 工资发薪表主键
     * @return 工资发薪表
     */
    SalaryPayDTO selectSalaryPayBySalaryPayId(@Param("salaryPayId") Long salaryPayId);


    /**
     * 批量查询工资发薪表
     *
     * @param salaryPayIds 工资发薪表主键集合
     * @return 工资发薪表
     */
    List<SalaryPayDTO> selectSalaryPayBySalaryPayIds(@Param("salaryPayIds") List<Long> salaryPayIds);

    /**
     * 查询工资发薪表列表
     *
     * @param salaryPay 工资发薪表
     * @return 工资发薪表集合
     */
    List<SalaryPayDTO> selectSalaryPayList(@Param("salaryPay") SalaryPay salaryPay);

    /**
     * 新增工资发薪表
     *
     * @param salaryPay 工资发薪表
     * @return 结果
     */
    int insertSalaryPay(@Param("salaryPay") SalaryPay salaryPay);

    /**
     * 修改工资发薪表
     *
     * @param salaryPay 工资发薪表
     * @return 结果
     */
    int updateSalaryPay(@Param("salaryPay") SalaryPay salaryPay);

    /**
     * 批量修改工资发薪表
     *
     * @param salaryPayList 工资发薪表
     * @return 结果
     */
    int updateSalaryPays(@Param("salaryPayList") List<SalaryPay> salaryPayList);

    /**
     * 逻辑删除工资发薪表
     *
     * @param salaryPay
     * @return 结果
     */
    int logicDeleteSalaryPayBySalaryPayId(@Param("salaryPay") SalaryPay salaryPay);

    /**
     * 逻辑批量删除工资发薪表
     *
     * @param salaryPayIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteSalaryPayBySalaryPayIds(@Param("salaryPayIds") List<Long> salaryPayIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除工资发薪表
     *
     * @param salaryPayId 工资发薪表主键
     * @return 结果
     */
    int deleteSalaryPayBySalaryPayId(@Param("salaryPayId") Long salaryPayId);

    /**
     * 物理批量删除工资发薪表
     *
     * @param salaryPayIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteSalaryPayBySalaryPayIds(@Param("salaryPayIds") List<Long> salaryPayIds);

    /**
     * 批量新增工资发薪表
     *
     * @param SalaryPays 工资发薪表列表
     * @return 结果
     */
    int batchSalaryPay(@Param("salaryPays") List<SalaryPay> SalaryPays);

    /**
     * 根据人员ID集合查找工资发薪信息
     *
     * @param employeeIds
     * @return
     */
    List<SalaryPayDTO> selectSalaryPayByEmployeeIds(@Param("employeeIds") List<Long> employeeIds);

    /**
     * 根据人员ID集合和年份查找工资发薪信息
     *
     * @param employeeIds
     * @return
     */
    List<SalaryPayDTO> selectSalaryPayByEmployeeIdsAndPayYear(@Param("employeeIds") List<Long> employeeIds,@Param("payYear")Integer payYear);

    /**
     * 根据人员ID和年份查找工资发薪信息
     *
     * @param employeeId
     * @return
     */
    List<SalaryPayDTO> selectSalaryPayByEmployeeIdAndPayYear(@Param("employeeId") Long employeeId,@Param("payYear")Integer payYear);

    /**
     * 根据人员ID集合和年份查找工资发薪信息
     *
     * @param employeeIds
     * @return
     */
    List<SalaryPayDTO> selectSalaryPayByBudggetEmployeeIds(@Param("employeeIds") List<Long> employeeIds,@Param("payYear") Integer payYear);

    /**
     * 同一年的多个月
     *
     * @param payYear
     * @param startMonth
     * @param endMonth
     * @return
     */
    List<SalaryPayDTO> selectSalaryPayBySomeMonth(@Param("payYear") Integer payYear, @Param("startMonth") Integer startMonth, @Param("endMonth") Integer endMonth);

    /**
     * 多个年份的所有
     *
     * @param startYear
     * @param endYear
     * @return
     */
    List<SalaryPayDTO> selectSalaryPayBySomeYear(@Param("startYear") Integer startYear, @Param("endYear") Integer endYear);

    /**
     * 当前月份倒推12个月的“奖金”部分合计
     * @param budgetYear
     * @param month
     * @return
     */
    BigDecimal selectBonusActualNum(@Param("budgetYear") int budgetYear, @Param("month")int month);

    /**
     * 返回上年总工资包实际数：从月度工资数据管理取值（总计值）
     * @return
     * @param budgetYear
     */
    BigDecimal selectSalaryPayAmoutNum(@Param("budgetYear") int budgetYear);

    /**
     * 部门奖金预算 某职级的平均薪酬：从月度工资管理取数，取数范围为倒推12个月的数据（年工资）
     *
     * @param employeeId
     * @param payYear
     * @return
     */
    List<SalaryPayDTO> selectDeptBonusBudgetPay(@Param("employeeIds") Long employeeId,@Param("payYear") Integer payYear);
}
