package net.qixiaowei.operate.cloud.mapper.bonus;

import java.util.List;
import net.qixiaowei.operate.cloud.api.domain.bonus.BonusPayBudgetDept;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusPayBudgetDeptDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* BonusPayBudgetDeptMapper接口
* @author TANGMICHI
* @since 2022-12-08
*/
public interface BonusPayBudgetDeptMapper{
    /**
    * 查询奖金发放预算部门表
    *
    * @param bonusPayBudgetDeptId 奖金发放预算部门表主键
    * @return 奖金发放预算部门表
    */
    BonusPayBudgetDeptDTO selectBonusPayBudgetDeptByBonusPayBudgetDeptId(@Param("bonusPayBudgetDeptId")Long bonusPayBudgetDeptId);

    /**
     * 根据奖金发放主表主键查询奖金发放预算部门表
     *
     * @param bonusPayApplicationId 奖金发放主表主键
     * @return 奖金发放预算部门表
     */
    List<BonusPayBudgetDeptDTO> selectBonusPayBudgetDeptByBonusPayApplicationId(@Param("bonusPayApplicationId")Long bonusPayApplicationId);

    /**
    * 批量查询奖金发放预算部门表
    *
    * @param bonusPayBudgetDeptIds 奖金发放预算部门表主键集合
    * @return 奖金发放预算部门表
    */
    List<BonusPayBudgetDeptDTO> selectBonusPayBudgetDeptByBonusPayBudgetDeptIds(@Param("bonusPayBudgetDeptIds") List<Long> bonusPayBudgetDeptIds);

    /**
     * 批量查询奖金发放预算部门表
     *
     * @param bonusPayApplicationIds 奖金发放预算部门表主键集合
     * @return 奖金发放预算部门表
     */
    List<BonusPayBudgetDeptDTO> selectBonusPayBudgetDeptByBonusPayApplicationIds(@Param("bonusPayApplicationIds") List<Long> bonusPayApplicationIds);
    /**
    * 查询奖金发放预算部门表列表
    *
    * @param bonusPayBudgetDept 奖金发放预算部门表
    * @return 奖金发放预算部门表集合
    */
    List<BonusPayBudgetDeptDTO> selectBonusPayBudgetDeptList(@Param("bonusPayBudgetDept")BonusPayBudgetDept bonusPayBudgetDept);

    /**
    * 新增奖金发放预算部门表
    *
    * @param bonusPayBudgetDept 奖金发放预算部门表
    * @return 结果
    */
    int insertBonusPayBudgetDept(@Param("bonusPayBudgetDept")BonusPayBudgetDept bonusPayBudgetDept);

    /**
    * 修改奖金发放预算部门表
    *
    * @param bonusPayBudgetDept 奖金发放预算部门表
    * @return 结果
    */
    int updateBonusPayBudgetDept(@Param("bonusPayBudgetDept")BonusPayBudgetDept bonusPayBudgetDept);

    /**
    * 批量修改奖金发放预算部门表
    *
    * @param bonusPayBudgetDeptList 奖金发放预算部门表
    * @return 结果
    */
    int updateBonusPayBudgetDepts(@Param("bonusPayBudgetDeptList")List<BonusPayBudgetDept> bonusPayBudgetDeptList);
    /**
    * 逻辑删除奖金发放预算部门表
    *
    * @param bonusPayBudgetDept
    * @return 结果
    */
    int logicDeleteBonusPayBudgetDeptByBonusPayBudgetDeptId(@Param("bonusPayBudgetDept")BonusPayBudgetDept bonusPayBudgetDept);

    /**
    * 逻辑批量删除奖金发放预算部门表
    *
    * @param bonusPayBudgetDeptIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteBonusPayBudgetDeptByBonusPayBudgetDeptIds(@Param("bonusPayBudgetDeptIds")List<Long> bonusPayBudgetDeptIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除奖金发放预算部门表
    *
    * @param bonusPayBudgetDeptId 奖金发放预算部门表主键
    * @return 结果
    */
    int deleteBonusPayBudgetDeptByBonusPayBudgetDeptId(@Param("bonusPayBudgetDeptId")Long bonusPayBudgetDeptId);

    /**
    * 物理批量删除奖金发放预算部门表
    *
    * @param bonusPayBudgetDeptIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteBonusPayBudgetDeptByBonusPayBudgetDeptIds(@Param("bonusPayBudgetDeptIds")List<Long> bonusPayBudgetDeptIds);

    /**
    * 批量新增奖金发放预算部门表
    *
    * @param BonusPayBudgetDepts 奖金发放预算部门表列表
    * @return 结果
    */
    int batchBonusPayBudgetDept(@Param("bonusPayBudgetDepts")List<BonusPayBudgetDept> BonusPayBudgetDepts);

    /**
     * 对应一级部门以及其下属部门作为预算部门的奖金发放申请单据中的奖项总金额和奖金比例
     * @return
     */
    List<BonusPayBudgetDeptDTO> selectBonusPayBudgetDeptByBonusAnnual(@Param("departmentIds") List<Long> departmentIds);

}
