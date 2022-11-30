package net.qixiaowei.operate.cloud.mapper.salary;

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
    * 批量查询部门奖金预算明细表
    *
    * @param deptBonusBudgetDetailsIds 部门奖金预算明细表主键集合
    * @return 部门奖金预算明细表
    */
    List<DeptBonusBudgetDetailsDTO> selectDeptBonusBudgetDetailsByDeptBonusBudgetDetailsIds(@Param("deptBonusBudgetDetailsIds") List<Long> deptBonusBudgetDetailsIds);

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
}
