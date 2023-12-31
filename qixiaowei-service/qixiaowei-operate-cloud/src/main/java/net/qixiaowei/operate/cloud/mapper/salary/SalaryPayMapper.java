package net.qixiaowei.operate.cloud.mapper.salary;

import net.qixiaowei.operate.cloud.api.domain.salary.SalaryPay;
import net.qixiaowei.operate.cloud.api.dto.bonus.EmpAnnualBonusSnapshotDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryPayDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryPayDetailsDTO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;


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
    List<SalaryPayDTO> selectSalaryPayByEmployeeIdsAndPayYear(@Param("employeeIds") List<Long> employeeIds, @Param("payYear") Integer payYear);

    /**
     * 根据人员ID和年份查找工资发薪信息
     *
     * @param employeeId
     * @return
     */
    List<SalaryPayDTO> selectSalaryPayByEmployeeIdAndPayYear(@Param("employeeId") Long employeeId, @Param("payYear") Integer payYear);

    /**
     * 根据人员ID集合和年份查找工资发薪信息
     *
     * @param employeeId
     * @return
     */
    List<SalaryPayDTO> selectSalaryPayByBudggetEmployeeId(@Param("employeeId") Long employeeId, @Param("payYear") Integer payYear);

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
     *
     * @param budgetYear
     * @return
     */
    List<SalaryPayDTO> selectBonusActualNum(@Param("budgetYear") int budgetYear);

    /**
     * 当前月份倒推12个月的工资总计
     *
     * @param budgetYear  年份
     * @param employeeIds 员工Id集合
     * @return BigDecimal
     */
    List<SalaryPayDetailsDTO> selectSalaryAmountNum(@Param("budgetYear") int budgetYear, @Param("employeeIds") List<Long> employeeIds);

    /**
     * 返回上年总工资包实际数：从月度工资数据管理取值（总计值）
     *
     * @param budgetYear
     * @return
     */
    BigDecimal selectSalaryBounsAmount(@Param("budgetYear") int budgetYear);


    /**
     * 返回上年总工资包实际数：从月度工资数据管理取值（总计值）
     *
     * @param budgetYear
     * @return
     */
    BigDecimal selectSalaryPayAmoutNum(@Param("budgetYear") int budgetYear);

    /**
     * 部门奖金预算 某职级的平均薪酬：从月度工资管理取数，取数范围为倒推12个月的数据（年工资）
     *
     * @param employeeId 员工ID
     * @param payYear    发薪年份
     * @param tenantId   租户id
     * @return
     */
    List<SalaryPayDTO> selectDeptAnnualBonusBudgetPay(@Param("employeeId") Long employeeId, @Param("payYear") Integer payYear, @Param("tenantId") Long tenantId);

    /**
     * 部门奖金预算 某职级的平均薪酬：从月度工资管理取数，对于当前及未来年份取数范围为倒推12个月的数据（年工资）  对于历史年份取一整年数据
     *
     * @param employeeId 员工ID
     * @param payYear    发薪年份
     * @param tenantId   租户id
     * @return
     */
    List<SalaryPayDTO> selectDeptBonusBudgetPay(@Param("employeeId") Long employeeId, @Param("payYear") Integer payYear, @Param("nowYear") Integer nowYear, @Param("tenantId") Long tenantId);

    /**
     * 根据员工ID和发薪年份查找员工发薪记录
     *
     * @param employeeId 员工ID
     * @param payYears   发薪年份集合
     * @return
     */
    List<SalaryPayDTO> selectSalaryPayOfEmployeeIdAndPayYears(@Param("employeeId") Long employeeId, @Param("payYears") Set<Integer> payYears);


    /**
     * 根据员工ID和发薪年份查找员工发薪记录
     *
     * @param employeeIds 员工ID集合
     * @param payYears    发薪年份集合
     * @return
     */
    List<SalaryPayDTO> selectSalaryPayOfEmployeeIdsAndPayYears(@Param("employeeIds") Set<Long> employeeIds, @Param("payYears") Set<Integer> payYears);

    /**
     * 根据年份 月份 员工ID查询发薪表
     *
     * @param employeeId 员工id
     * @param year       年
     * @param month      月
     * @return
     */
    SalaryPayDTO selectSalaryPayByYearAndMonth(@Param("employeeId") Long employeeId, @Param("year") int year, @Param("month") int month);

    /**
     * 个人年终奖最近三年的薪酬情况
     *
     * @param employeeIds
     * @param payYear
     * @return
     */
    List<EmpAnnualBonusSnapshotDTO> selectSalaryPayCondition(@Param("employeeIds") List<Long> employeeIds, @Param("payYear") int payYear);

    /**
     * 员工倒推12个月的薪酬合计和奖金合计
     *
     * @param employeeId 员工ID
     * @param payYear    年份
     * @return
     */
    SalaryPayDTO selectSalaryPaySumAndBonusSum(@Param("employeeId") Long employeeId, @Param("payYear") Integer payYear);

    /**
     * 根据员工ID查询工资条
     *
     * @param employeeId 员工ID
     * @return List
     */
    List<SalaryPayDTO> selectByEmployeeId(@Param("employeeId") Long employeeId);

    /**
     * 对于历史年份，取历史年份对应整年的奖金数据
     * @param year
     * @return
     */
    BigDecimal selectAfterYearBonusActualNum(@Param("year")int year);
}
