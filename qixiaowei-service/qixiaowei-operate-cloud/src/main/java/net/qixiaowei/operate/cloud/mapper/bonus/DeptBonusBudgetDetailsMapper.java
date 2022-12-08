package net.qixiaowei.operate.cloud.mapper.bonus;

import java.util.List;
import net.qixiaowei.operate.cloud.api.domain.salary.DeptBonusBudgetDetails;
import net.qixiaowei.operate.cloud.api.dto.salary.DeptBonusBudgetDetailsDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* DeptBonusBudgetDetailsMapper接口
* @author TANGMICHI
* @since 2022-11-30
*/
public interface DeptBonusBudgetDetailsMapper{
    /**
    * 查询部门奖金预算明细表
    *
    * @param deptBonusBudgetDetailsId 部门奖金预算明细表主键
    * @return 部门奖金预算明细表
    */
    DeptBonusBudgetDetailsDTO selectDeptBonusBudgetDetailsByDeptBonusBudgetDetailsId(@Param("deptBonusBudgetDetailsId")Long deptBonusBudgetDetailsId);

    /**
     * 根据部门奖金预算主表id查询部门奖金预算明细表
     *
     * @param deptBonusBudgetId 部门奖金预算主表主键
     * @return 部门奖金预算明细表
     */
    List<DeptBonusBudgetDetailsDTO> selectDeptBonusBudgetDetailsByDeptBonusBudgetId(@Param("deptBonusBudgetId")Long deptBonusBudgetId);

    /**
    * 批量查询部门奖金预算明细表
    *
    * @param deptBonusBudgetDetailsIds 部门奖金预算明细表主键集合
    * @return 部门奖金预算明细表
    */
    List<DeptBonusBudgetDetailsDTO> selectDeptBonusBudgetDetailsByDeptBonusBudgetDetailsIds(@Param("deptBonusBudgetDetailsIds") List<Long> deptBonusBudgetDetailsIds);

    /**
     * 根据部门奖金预算主表id集合批量查询部门奖金预算明细表
     *
     * @param deptBonusBudgetIds 部门奖金预算主表主键集合
     * @return 部门奖金预算明细表
     */
    List<DeptBonusBudgetDetailsDTO> selectDeptBonusBudgetDetailsByDeptBonusBudgetIds(@Param("deptBonusBudgetIds") List<Long> deptBonusBudgetIds);

    /**
    * 查询部门奖金预算明细表列表
    *
    * @param deptBonusBudgetDetails 部门奖金预算明细表
    * @return 部门奖金预算明细表集合
    */
    List<DeptBonusBudgetDetailsDTO> selectDeptBonusBudgetDetailsList(@Param("deptBonusBudgetDetails")DeptBonusBudgetDetails deptBonusBudgetDetails);

    /**
    * 新增部门奖金预算明细表
    *
    * @param deptBonusBudgetDetails 部门奖金预算明细表
    * @return 结果
    */
    int insertDeptBonusBudgetDetails(@Param("deptBonusBudgetDetails")DeptBonusBudgetDetails deptBonusBudgetDetails);

    /**
    * 修改部门奖金预算明细表
    *
    * @param deptBonusBudgetDetails 部门奖金预算明细表
    * @return 结果
    */
    int updateDeptBonusBudgetDetails(@Param("deptBonusBudgetDetails")DeptBonusBudgetDetails deptBonusBudgetDetails);

    /**
    * 批量修改部门奖金预算明细表
    *
    * @param deptBonusBudgetDetailsList 部门奖金预算明细表
    * @return 结果
    */
    int updateDeptBonusBudgetDetailss(@Param("deptBonusBudgetDetailsList")List<DeptBonusBudgetDetails> deptBonusBudgetDetailsList);
    /**
    * 逻辑删除部门奖金预算明细表
    *
    * @param deptBonusBudgetDetails
    * @return 结果
    */
    int logicDeleteDeptBonusBudgetDetailsByDeptBonusBudgetDetailsId(@Param("deptBonusBudgetDetails")DeptBonusBudgetDetails deptBonusBudgetDetails);

    /**
    * 逻辑批量删除部门奖金预算明细表
    *
    * @param deptBonusBudgetDetailsIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteDeptBonusBudgetDetailsByDeptBonusBudgetDetailsIds(@Param("deptBonusBudgetDetailsIds")List<Long> deptBonusBudgetDetailsIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除部门奖金预算明细表
    *
    * @param deptBonusBudgetDetailsId 部门奖金预算明细表主键
    * @return 结果
    */
    int deleteDeptBonusBudgetDetailsByDeptBonusBudgetDetailsId(@Param("deptBonusBudgetDetailsId")Long deptBonusBudgetDetailsId);

    /**
    * 物理批量删除部门奖金预算明细表
    *
    * @param deptBonusBudgetDetailsIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteDeptBonusBudgetDetailsByDeptBonusBudgetDetailsIds(@Param("deptBonusBudgetDetailsIds")List<Long> deptBonusBudgetDetailsIds);

    /**
    * 批量新增部门奖金预算明细表
    *
    * @param DeptBonusBudgetDetailss 部门奖金预算明细表列表
    * @return 结果
    */
    int batchDeptBonusBudgetDetails(@Param("deptBonusBudgetDetailss")List<DeptBonusBudgetDetails> DeptBonusBudgetDetailss);

    /**
     * 根据年份和部门id查询部门预算数据
     * @param annualBonusYear
     * @param departmentIds
     * @return
     */
    List<DeptBonusBudgetDetailsDTO> selectDeptBonusBudgetBybudgetYearAnnua(@Param("annualBonusYear") int annualBonusYear, @Param("departmentIds")List<Long> departmentIds);
}
