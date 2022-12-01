package net.qixiaowei.operate.cloud.mapper.salary;

import java.util.List;
import net.qixiaowei.operate.cloud.api.domain.salary.DeptBonusBudget;
import net.qixiaowei.operate.cloud.api.dto.salary.DeptBonusBudgetDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* DeptBonusBudgetMapper接口
* @author TANGMICHI
* @since 2022-11-29
*/
public interface DeptBonusBudgetMapper{
    /**
    * 查询部门奖金包预算表
    *
    * @param deptBonusBudgetId 部门奖金包预算表主键
    * @return 部门奖金包预算表
    */
    DeptBonusBudgetDTO selectDeptBonusBudgetByDeptBonusBudgetId(@Param("deptBonusBudgetId")Long deptBonusBudgetId);


    /**
    * 批量查询部门奖金包预算表
    *
    * @param deptBonusBudgetIds 部门奖金包预算表主键集合
    * @return 部门奖金包预算表
    */
    List<DeptBonusBudgetDTO> selectDeptBonusBudgetByDeptBonusBudgetIds(@Param("deptBonusBudgetIds") List<Long> deptBonusBudgetIds);

    /**
    * 查询部门奖金包预算表列表
    *
    * @param deptBonusBudget 部门奖金包预算表
    * @return 部门奖金包预算表集合
    */
    List<DeptBonusBudgetDTO> selectDeptBonusBudgetList(@Param("deptBonusBudget")DeptBonusBudget deptBonusBudget);

    /**
    * 新增部门奖金包预算表
    *
    * @param deptBonusBudget 部门奖金包预算表
    * @return 结果
    */
    int insertDeptBonusBudget(@Param("deptBonusBudget")DeptBonusBudget deptBonusBudget);

    /**
    * 修改部门奖金包预算表
    *
    * @param deptBonusBudget 部门奖金包预算表
    * @return 结果
    */
    int updateDeptBonusBudget(@Param("deptBonusBudget")DeptBonusBudget deptBonusBudget);

    /**
    * 批量修改部门奖金包预算表
    *
    * @param deptBonusBudgetList 部门奖金包预算表
    * @return 结果
    */
    int updateDeptBonusBudgets(@Param("deptBonusBudgetList")List<DeptBonusBudget> deptBonusBudgetList);
    /**
    * 逻辑删除部门奖金包预算表
    *
    * @param deptBonusBudget
    * @return 结果
    */
    int logicDeleteDeptBonusBudgetByDeptBonusBudgetId(@Param("deptBonusBudget")DeptBonusBudget deptBonusBudget);

    /**
    * 逻辑批量删除部门奖金包预算表
    *
    * @param deptBonusBudgetIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteDeptBonusBudgetByDeptBonusBudgetIds(@Param("deptBonusBudgetIds")List<Long> deptBonusBudgetIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除部门奖金包预算表
    *
    * @param deptBonusBudgetId 部门奖金包预算表主键
    * @return 结果
    */
    int deleteDeptBonusBudgetByDeptBonusBudgetId(@Param("deptBonusBudgetId")Long deptBonusBudgetId);

    /**
    * 物理批量删除部门奖金包预算表
    *
    * @param deptBonusBudgetIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteDeptBonusBudgetByDeptBonusBudgetIds(@Param("deptBonusBudgetIds")List<Long> deptBonusBudgetIds);

    /**
    * 批量新增部门奖金包预算表
    *
    * @param DeptBonusBudgets 部门奖金包预算表列表
    * @return 结果
    */
    int batchDeptBonusBudget(@Param("deptBonusBudgets")List<DeptBonusBudget> DeptBonusBudgets);

    /**
     * 返回部门奖金预算最大年份
     * @return
     */
    int queryDeptBonusBudgetYear();
}
