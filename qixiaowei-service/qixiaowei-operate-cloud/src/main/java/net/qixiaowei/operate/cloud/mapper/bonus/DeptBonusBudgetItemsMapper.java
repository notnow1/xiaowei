package net.qixiaowei.operate.cloud.mapper.bonus;

import java.util.List;
import net.qixiaowei.operate.cloud.api.domain.bonus.DeptBonusBudgetItems;
import net.qixiaowei.operate.cloud.api.dto.bonus.DeptBonusBudgetDetailsDTO;
import net.qixiaowei.operate.cloud.api.dto.bonus.DeptBonusBudgetItemsDTO;
import net.qixiaowei.operate.cloud.api.dto.bonus.DeptBonusCompanyDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* DeptBonusBudgetItemsMapper接口
* @author TANGMICHI
* @since 2022-12-01
*/
public interface DeptBonusBudgetItemsMapper{
    /**
    * 查询部门奖金预算项目表
    *
    * @param deptBonusBudgetItemsId 部门奖金预算项目表主键
    * @return 部门奖金预算项目表
    */
    DeptBonusBudgetItemsDTO selectDeptBonusBudgetItemsByDeptBonusBudgetItemsId(@Param("deptBonusBudgetItemsId")Long deptBonusBudgetItemsId);


    /**
    * 批量查询部门奖金预算项目表
    *
    * @param deptBonusBudgetItemsIds 部门奖金预算项目表主键集合
    * @return 部门奖金预算项目表
    */
    List<DeptBonusBudgetItemsDTO> selectDeptBonusBudgetItemsByDeptBonusBudgetItemsIds(@Param("deptBonusBudgetItemsIds") List<Long> deptBonusBudgetItemsIds);

    /**
     * 根据部门奖金预算主表id查询公司奖金预算明细表
     *
     * @param deptBonusBudgetId 部门奖金预算主表主键
     * @return
     */
    List<DeptBonusCompanyDTO> selectCompanyBonusBudgetDetailsByCompanyBonusBudgetId(@Param("deptBonusBudgetId")Long deptBonusBudgetId);

    /**
     * 根据部门奖金预算主表id集合查询公司奖金预算明细表
     *
     * @param deptBonusBudgetIds 部门奖金预算主表主键集合
     * @return
     */
    List<DeptBonusCompanyDTO> selectCompanyBonusBudgetDetailsByCompanyBonusBudgetIds(@Param("deptBonusBudgetIds")List<Long> deptBonusBudgetIds);
    /**
     * 根据部门奖金预算明细表id集合批量查询部门奖金预算项目表
     *
     * @param deptBonusBudgetDetailsIds 部门奖金预算明细表主键集合
     * @return 部门奖金预算项目表
     */
    List<DeptBonusBudgetItemsDTO> selectDeptBonusBudgetItemsByDeptBonusBudgetDetailsIds(@Param("deptBonusBudgetDetailsIds") List<Long> deptBonusBudgetDetailsIds);

    /**
    * 查询部门奖金预算项目表列表
    *
    * @param deptBonusBudgetItems 部门奖金预算项目表
    * @return 部门奖金预算项目表集合
    */
    List<DeptBonusBudgetItemsDTO> selectDeptBonusBudgetItemsList(@Param("deptBonusBudgetItems")DeptBonusBudgetItems deptBonusBudgetItems);

    /**
    * 新增部门奖金预算项目表
    *
    * @param deptBonusBudgetItems 部门奖金预算项目表
    * @return 结果
    */
    int insertDeptBonusBudgetItems(@Param("deptBonusBudgetItems")DeptBonusBudgetItems deptBonusBudgetItems);

    /**
    * 修改部门奖金预算项目表
    *
    * @param deptBonusBudgetItems 部门奖金预算项目表
    * @return 结果
    */
    int updateDeptBonusBudgetItems(@Param("deptBonusBudgetItems")DeptBonusBudgetItems deptBonusBudgetItems);

    /**
    * 批量修改部门奖金预算项目表
    *
    * @param deptBonusBudgetItemsList 部门奖金预算项目表
    * @return 结果
    */
    int updateDeptBonusBudgetItemss(@Param("deptBonusBudgetItemsList")List<DeptBonusBudgetItems> deptBonusBudgetItemsList);
    /**
    * 逻辑删除部门奖金预算项目表
    *
    * @param deptBonusBudgetItems
    * @return 结果
    */
    int logicDeleteDeptBonusBudgetItemsByDeptBonusBudgetItemsId(@Param("deptBonusBudgetItems")DeptBonusBudgetItems deptBonusBudgetItems);

    /**
    * 逻辑批量删除部门奖金预算项目表
    *
    * @param deptBonusBudgetItemsIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteDeptBonusBudgetItemsByDeptBonusBudgetItemsIds(@Param("deptBonusBudgetItemsIds")List<Long> deptBonusBudgetItemsIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除部门奖金预算项目表
    *
    * @param deptBonusBudgetItemsId 部门奖金预算项目表主键
    * @return 结果
    */
    int deleteDeptBonusBudgetItemsByDeptBonusBudgetItemsId(@Param("deptBonusBudgetItemsId")Long deptBonusBudgetItemsId);

    /**
    * 物理批量删除部门奖金预算项目表
    *
    * @param deptBonusBudgetItemsIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteDeptBonusBudgetItemsByDeptBonusBudgetItemsIds(@Param("deptBonusBudgetItemsIds")List<Long> deptBonusBudgetItemsIds);

    /**
    * 批量新增部门奖金预算项目表
    *
    * @param DeptBonusBudgetItemss 部门奖金预算项目表列表
    * @return 结果
    */
    int batchDeptBonusBudgetItems(@Param("deptBonusBudgetItemss")List<DeptBonusBudgetItems> DeptBonusBudgetItemss);
}
