package net.qixiaowei.operate.cloud.mapper.salary;

import java.util.List;

import net.qixiaowei.operate.cloud.api.domain.salary.SalaryPay;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryPayDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


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

}