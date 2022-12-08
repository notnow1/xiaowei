package net.qixiaowei.operate.cloud.service.bonus;

import java.util.List;
import net.qixiaowei.operate.cloud.api.dto.salary.DeptBonusBudgetDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.DeptBonusBudgetDetailsDTO;


/**
* DeptBonusBudgetService接口
* @author TANGMICHI
* @since 2022-11-29
*/
public interface IDeptBonusBudgetService{
    /**
    * 查询部门奖金包预算表
    *
    * @param deptBonusBudgetId 部门奖金包预算表主键
    * @return 部门奖金包预算表
    */
    DeptBonusBudgetDTO selectDeptBonusBudgetByDeptBonusBudgetId(Long deptBonusBudgetId);

    /**
    * 查询部门奖金包预算表列表
    *
    * @param deptBonusBudgetDTO 部门奖金包预算表
    * @return 部门奖金包预算表集合
    */
    List<DeptBonusBudgetDTO> selectDeptBonusBudgetList(DeptBonusBudgetDTO deptBonusBudgetDTO);

    /**
    * 新增部门奖金包预算表
    *
    * @param deptBonusBudgetDTO 部门奖金包预算表
    * @return 结果
    */
    DeptBonusBudgetDTO insertDeptBonusBudget(DeptBonusBudgetDTO deptBonusBudgetDTO);

    /**
    * 修改部门奖金包预算表
    *
    * @param deptBonusBudgetDTO 部门奖金包预算表
    * @return 结果
    */
    int updateDeptBonusBudget(DeptBonusBudgetDTO deptBonusBudgetDTO);

    /**
    * 批量修改部门奖金包预算表
    *
    * @param deptBonusBudgetDtos 部门奖金包预算表
    * @return 结果
    */
    int updateDeptBonusBudgets(List<DeptBonusBudgetDTO> deptBonusBudgetDtos);

    /**
    * 批量新增部门奖金包预算表
    *
    * @param deptBonusBudgetDtos 部门奖金包预算表
    * @return 结果
    */
    int insertDeptBonusBudgets(List<DeptBonusBudgetDTO> deptBonusBudgetDtos);

    /**
    * 逻辑批量删除部门奖金包预算表
    *
    * @param deptBonusBudgetIds 需要删除的部门奖金包预算表集合
    * @return 结果
    */
    int logicDeleteDeptBonusBudgetByDeptBonusBudgetIds(List<Long> deptBonusBudgetIds);

    /**
    * 逻辑删除部门奖金包预算表信息
    *
    * @param deptBonusBudgetDTO
    * @return 结果
    */
    int logicDeleteDeptBonusBudgetByDeptBonusBudgetId(DeptBonusBudgetDTO deptBonusBudgetDTO);
    /**
    * 批量删除部门奖金包预算表
    *
    * @param DeptBonusBudgetDtos
    * @return 结果
    */
    int deleteDeptBonusBudgetByDeptBonusBudgetIds(List<DeptBonusBudgetDTO> DeptBonusBudgetDtos);

    /**
    * 逻辑删除部门奖金包预算表信息
    *
    * @param deptBonusBudgetDTO
    * @return 结果
    */
    int deleteDeptBonusBudgetByDeptBonusBudgetId(DeptBonusBudgetDTO deptBonusBudgetDTO);


    /**
    * 删除部门奖金包预算表信息
    *
    * @param deptBonusBudgetId 部门奖金包预算表主键
    * @return 结果
    */
    int deleteDeptBonusBudgetByDeptBonusBudgetId(Long deptBonusBudgetId);

    /**
     * 新增部门奖金包预算预制数据
     * @param budgetYear
     * @return
     */
    DeptBonusBudgetDTO addDeptBonusBudgetTamount(int budgetYear);

    /**
     * 返回部门奖金预算最大年份
     * @return
     * @param deptBonusBudgetDTO
     */
    DeptBonusBudgetDTO queryDeptBonusBudgetYear(DeptBonusBudgetDTO deptBonusBudgetDTO);

    /**
     * 实时查询部门奖金包预算明细参考值数据
     * @param deptBonusBudgetDTO
     * @return
     */
    List<DeptBonusBudgetDetailsDTO> realTimeQueryDeptBonusBudget(DeptBonusBudgetDTO deptBonusBudgetDTO);
}
