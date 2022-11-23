package net.qixiaowei.operate.cloud.mapper.employee;

import java.util.List;
import net.qixiaowei.operate.cloud.api.domain.employee.EmployeeBudgetDetails;
import net.qixiaowei.operate.cloud.api.dto.employee.EmployeeBudgetDTO;
import net.qixiaowei.operate.cloud.api.dto.employee.EmployeeBudgetDetailsDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* EmployeeBudgetDetailsMapper接口
* @author TANGMICHI
* @since 2022-11-22
*/
public interface EmployeeBudgetDetailsMapper{
    /**
    * 查询人力预算明细表
    *
    * @param employeeBudgetDetailsId 人力预算明细表主键
    * @return 人力预算明细表
    */
    EmployeeBudgetDetailsDTO selectEmployeeBudgetDetailsByEmployeeBudgetDetailsId(@Param("employeeBudgetDetailsId")Long employeeBudgetDetailsId);

    /**
     * 根据人力预算主表主键查询人力预算明细表
     *
     * @param employeeBudgetId 人力预算主表主键
     * @return 人力预算明细表
     */
    List<EmployeeBudgetDetailsDTO> selectEmployeeBudgetDetailsByEmployeeBudgetId(@Param("employeeBudgetId")Long employeeBudgetId);

    /**
    * 批量查询人力预算明细表
    *
    * @param employeeBudgetDetailsIds 人力预算明细表主键集合
    * @return 人力预算明细表
    */
    List<EmployeeBudgetDetailsDTO> selectEmployeeBudgetDetailsByEmployeeBudgetDetailsIds(@Param("employeeBudgetDetailsIds") List<Long> employeeBudgetDetailsIds);

    /**
     * 根据人力预算主表主键集合批量查询人力预算明细表
     *
     * @param employeeBudgetIds 人力预算主表主键集合
     * @return 人力预算明细表
     */
    List<EmployeeBudgetDetailsDTO> selectEmployeeBudgetDetailsByEmployeeBudgetIds(@Param("employeeBudgetIds") List<Long> employeeBudgetIds);

    /**
    * 查询人力预算明细表列表
    *
    * @param employeeBudgetDetails 人力预算明细表
    * @return 人力预算明细表集合
    */
    List<EmployeeBudgetDetailsDTO> selectEmployeeBudgetDetailsList(@Param("employeeBudgetDetails")EmployeeBudgetDetails employeeBudgetDetails);

    /**
    * 新增人力预算明细表
    *
    * @param employeeBudgetDetails 人力预算明细表
    * @return 结果
    */
    int insertEmployeeBudgetDetails(@Param("employeeBudgetDetails")EmployeeBudgetDetails employeeBudgetDetails);

    /**
    * 修改人力预算明细表
    *
    * @param employeeBudgetDetails 人力预算明细表
    * @return 结果
    */
    int updateEmployeeBudgetDetails(@Param("employeeBudgetDetails")EmployeeBudgetDetails employeeBudgetDetails);

    /**
    * 批量修改人力预算明细表
    *
    * @param employeeBudgetDetailsList 人力预算明细表
    * @return 结果
    */
    int updateEmployeeBudgetDetailss(@Param("employeeBudgetDetailsList")List<EmployeeBudgetDetails> employeeBudgetDetailsList);
    /**
    * 逻辑删除人力预算明细表
    *
    * @param employeeBudgetDetails
    * @return 结果
    */
    int logicDeleteEmployeeBudgetDetailsByEmployeeBudgetDetailsId(@Param("employeeBudgetDetails")EmployeeBudgetDetails employeeBudgetDetails);

    /**
    * 逻辑批量删除人力预算明细表
    *
    * @param employeeBudgetDetailsIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteEmployeeBudgetDetailsByEmployeeBudgetDetailsIds(@Param("employeeBudgetDetailsIds")List<Long> employeeBudgetDetailsIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除人力预算明细表
    *
    * @param employeeBudgetDetailsId 人力预算明细表主键
    * @return 结果
    */
    int deleteEmployeeBudgetDetailsByEmployeeBudgetDetailsId(@Param("employeeBudgetDetailsId")Long employeeBudgetDetailsId);

    /**
    * 物理批量删除人力预算明细表
    *
    * @param employeeBudgetDetailsIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteEmployeeBudgetDetailsByEmployeeBudgetDetailsIds(@Param("employeeBudgetDetailsIds")List<Long> employeeBudgetDetailsIds);

    /**
    * 批量新增人力预算明细表
    *
    * @param EmployeeBudgetDetailss 人力预算明细表列表
    * @return 结果
    */
    int batchEmployeeBudgetDetails(@Param("employeeBudgetDetailss")List<EmployeeBudgetDetails> EmployeeBudgetDetailss);

    /**
     * 查询增人/减人工资包列表
     * @param employeeBudgetDTO
     * @return
     */
    List<EmployeeBudgetDetailsDTO> salaryPackageList(EmployeeBudgetDTO employeeBudgetDTO);
}
