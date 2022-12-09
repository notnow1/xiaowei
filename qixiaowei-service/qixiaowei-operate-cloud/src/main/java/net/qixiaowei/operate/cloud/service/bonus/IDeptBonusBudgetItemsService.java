package net.qixiaowei.operate.cloud.service.bonus;

import java.util.List;
import net.qixiaowei.operate.cloud.api.dto.bonus.DeptBonusBudgetItemsDTO;



/**
* DeptBonusBudgetItemsService接口
* @author TANGMICHI
* @since 2022-11-29
*/
public interface IDeptBonusBudgetItemsService{
    /**
    * 查询部门奖金预算项目表
    *
    * @param deptBonusBudgetItemsId 部门奖金预算项目表主键
    * @return 部门奖金预算项目表
    */
    DeptBonusBudgetItemsDTO selectDeptBonusBudgetItemsByDeptBonusBudgetItemsId(Long deptBonusBudgetItemsId);

    /**
    * 查询部门奖金预算项目表列表
    *
    * @param deptBonusBudgetItemsDTO 部门奖金预算项目表
    * @return 部门奖金预算项目表集合
    */
    List<DeptBonusBudgetItemsDTO> selectDeptBonusBudgetItemsList(DeptBonusBudgetItemsDTO deptBonusBudgetItemsDTO);

    /**
    * 新增部门奖金预算项目表
    *
    * @param deptBonusBudgetItemsDTO 部门奖金预算项目表
    * @return 结果
    */
    DeptBonusBudgetItemsDTO insertDeptBonusBudgetItems(DeptBonusBudgetItemsDTO deptBonusBudgetItemsDTO);

    /**
    * 修改部门奖金预算项目表
    *
    * @param deptBonusBudgetItemsDTO 部门奖金预算项目表
    * @return 结果
    */
    int updateDeptBonusBudgetItems(DeptBonusBudgetItemsDTO deptBonusBudgetItemsDTO);

    /**
    * 批量修改部门奖金预算项目表
    *
    * @param deptBonusBudgetItemsDtos 部门奖金预算项目表
    * @return 结果
    */
    int updateDeptBonusBudgetItemss(List<DeptBonusBudgetItemsDTO> deptBonusBudgetItemsDtos);

    /**
    * 批量新增部门奖金预算项目表
    *
    * @param deptBonusBudgetItemsDtos 部门奖金预算项目表
    * @return 结果
    */
    int insertDeptBonusBudgetItemss(List<DeptBonusBudgetItemsDTO> deptBonusBudgetItemsDtos);

    /**
    * 逻辑批量删除部门奖金预算项目表
    *
    * @param deptBonusBudgetItemsIds 需要删除的部门奖金预算项目表集合
    * @return 结果
    */
    int logicDeleteDeptBonusBudgetItemsByDeptBonusBudgetItemsIds(List<Long> deptBonusBudgetItemsIds);

    /**
    * 逻辑删除部门奖金预算项目表信息
    *
    * @param deptBonusBudgetItemsDTO
    * @return 结果
    */
    int logicDeleteDeptBonusBudgetItemsByDeptBonusBudgetItemsId(DeptBonusBudgetItemsDTO deptBonusBudgetItemsDTO);
    /**
    * 批量删除部门奖金预算项目表
    *
    * @param DeptBonusBudgetItemsDtos
    * @return 结果
    */
    int deleteDeptBonusBudgetItemsByDeptBonusBudgetItemsIds(List<DeptBonusBudgetItemsDTO> DeptBonusBudgetItemsDtos);

    /**
    * 逻辑删除部门奖金预算项目表信息
    *
    * @param deptBonusBudgetItemsDTO
    * @return 结果
    */
    int deleteDeptBonusBudgetItemsByDeptBonusBudgetItemsId(DeptBonusBudgetItemsDTO deptBonusBudgetItemsDTO);


    /**
    * 删除部门奖金预算项目表信息
    *
    * @param deptBonusBudgetItemsId 部门奖金预算项目表主键
    * @return 结果
    */
    int deleteDeptBonusBudgetItemsByDeptBonusBudgetItemsId(Long deptBonusBudgetItemsId);
}
