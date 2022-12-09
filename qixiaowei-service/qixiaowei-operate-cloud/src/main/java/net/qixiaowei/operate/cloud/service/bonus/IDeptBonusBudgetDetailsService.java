package net.qixiaowei.operate.cloud.service.bonus;

import net.qixiaowei.operate.cloud.api.dto.bonus.DeptBonusBudgetDetailsDTO;

import java.util.List;


/**
* DeptBonusBudgetDetailsService接口
* @author TANGMICHI
* @since 2022-11-29
*/
public interface IDeptBonusBudgetDetailsService {
    /**
     * 查询部门奖金预算明细表
     *
     * @param deptBonusBudgetDetailsId 部门奖金预算明细表主键
     * @return 部门奖金预算明细表
     */
    DeptBonusBudgetDetailsDTO selectDeptBonusBudgetDetailsByDeptBonusBudgetDetailsId(Long deptBonusBudgetDetailsId);

    /**
     * 查询部门奖金预算明细表列表
     *
     * @param deptBonusBudgetDetailsDTO 部门奖金预算明细表
     * @return 部门奖金预算明细表集合
     */
    List<DeptBonusBudgetDetailsDTO> selectDeptBonusBudgetDetailsList(DeptBonusBudgetDetailsDTO deptBonusBudgetDetailsDTO);

    /**
     * 新增部门奖金预算明细表
     *
     * @param deptBonusBudgetDetailsDTO 部门奖金预算明细表
     * @return 结果
     */
    DeptBonusBudgetDetailsDTO insertDeptBonusBudgetDetails(DeptBonusBudgetDetailsDTO deptBonusBudgetDetailsDTO);

    /**
     * 修改部门奖金预算明细表
     *
     * @param deptBonusBudgetDetailsDTO 部门奖金预算明细表
     * @return 结果
     */
    int updateDeptBonusBudgetDetails(DeptBonusBudgetDetailsDTO deptBonusBudgetDetailsDTO);

    /**
     * 批量修改部门奖金预算明细表
     *
     * @param deptBonusBudgetDetailsDtos 部门奖金预算明细表
     * @return 结果
     */
    int updateDeptBonusBudgetDetailss(List<DeptBonusBudgetDetailsDTO> deptBonusBudgetDetailsDtos);

    /**
     * 批量新增部门奖金预算明细表
     *
     * @param deptBonusBudgetDetailsDtos 部门奖金预算明细表
     * @return 结果
     */
    int insertDeptBonusBudgetDetailss(List<DeptBonusBudgetDetailsDTO> deptBonusBudgetDetailsDtos);

    /**
     * 逻辑批量删除部门奖金预算明细表
     *
     * @param deptBonusBudgetDetailsIds 需要删除的部门奖金预算明细表集合
     * @return 结果
     */
    int logicDeleteDeptBonusBudgetDetailsByDeptBonusBudgetDetailsIds(List<Long> deptBonusBudgetDetailsIds);

    /**
     * 逻辑删除部门奖金预算明细表信息
     *
     * @param deptBonusBudgetDetailsDTO
     * @return 结果
     */
    int logicDeleteDeptBonusBudgetDetailsByDeptBonusBudgetDetailsId(DeptBonusBudgetDetailsDTO deptBonusBudgetDetailsDTO);

    /**
     * 批量删除部门奖金预算明细表
     *
     * @param DeptBonusBudgetDetailsDtos
     * @return 结果
     */
    int deleteDeptBonusBudgetDetailsByDeptBonusBudgetDetailsIds(List<DeptBonusBudgetDetailsDTO> DeptBonusBudgetDetailsDtos);

    /**
     * 逻辑删除部门奖金预算明细表信息
     *
     * @param deptBonusBudgetDetailsDTO
     * @return 结果
     */
    int deleteDeptBonusBudgetDetailsByDeptBonusBudgetDetailsId(DeptBonusBudgetDetailsDTO deptBonusBudgetDetailsDTO);


    /**
     * 删除部门奖金预算明细表信息
     *
     * @param deptBonusBudgetDetailsId 部门奖金预算明细表主键
     * @return 结果
     */
    int deleteDeptBonusBudgetDetailsByDeptBonusBudgetDetailsId(Long deptBonusBudgetDetailsId);
}
