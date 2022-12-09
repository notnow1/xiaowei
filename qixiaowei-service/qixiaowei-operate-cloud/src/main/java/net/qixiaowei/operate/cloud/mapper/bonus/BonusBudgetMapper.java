package net.qixiaowei.operate.cloud.mapper.bonus;

import java.util.List;
import net.qixiaowei.operate.cloud.api.domain.bonus.BonusBudget;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusBudgetDTO;
import net.qixiaowei.operate.cloud.api.dto.bonus.DeptAnnualBonusOperateDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* BonusBudgetMapper接口
* @author TANGMICHI
* @since 2022-11-26
*/
public interface BonusBudgetMapper{

    /**
     * 根据年份查询奖金预算表
     *
     * @param budgetYear 奖金预算表主键
     * @return 奖金预算表
     */
    BonusBudgetDTO selectBonusBudgetByBudgetYear(@Param("budgetYear")int budgetYear);
    /**
    * 查询奖金预算表
    *
    * @param bonusBudgetId 奖金预算表主键
    * @return 奖金预算表
    */
    BonusBudgetDTO selectBonusBudgetByBonusBudgetId(@Param("bonusBudgetId")Long bonusBudgetId);


    /**
    * 批量查询奖金预算表
    *
    * @param bonusBudgetIds 奖金预算表主键集合
    * @return 奖金预算表
    */
    List<BonusBudgetDTO> selectBonusBudgetByBonusBudgetIds(@Param("bonusBudgetIds") List<Long> bonusBudgetIds);

    /**
    * 查询奖金预算表列表
    *
    * @param bonusBudget 奖金预算表
    * @return 奖金预算表集合
    */
    List<BonusBudgetDTO> selectBonusBudgetList(@Param("bonusBudget")BonusBudget bonusBudget);

    /**
    * 新增奖金预算表
    *
    * @param bonusBudget 奖金预算表
    * @return 结果
    */
    int insertBonusBudget(@Param("bonusBudget")BonusBudget bonusBudget);

    /**
    * 修改奖金预算表
    *
    * @param bonusBudget 奖金预算表
    * @return 结果
    */
    int updateBonusBudget(@Param("bonusBudget")BonusBudget bonusBudget);

    /**
    * 批量修改奖金预算表
    *
    * @param bonusBudgetList 奖金预算表
    * @return 结果
    */
    int updateBonusBudgets(@Param("bonusBudgetList")List<BonusBudget> bonusBudgetList);
    /**
    * 逻辑删除奖金预算表
    *
    * @param bonusBudget
    * @return 结果
    */
    int logicDeleteBonusBudgetByBonusBudgetId(@Param("bonusBudget")BonusBudget bonusBudget);

    /**
    * 逻辑批量删除奖金预算表
    *
    * @param bonusBudgetIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteBonusBudgetByBonusBudgetIds(@Param("bonusBudgetIds")List<Long> bonusBudgetIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除奖金预算表
    *
    * @param bonusBudgetId 奖金预算表主键
    * @return 结果
    */
    int deleteBonusBudgetByBonusBudgetId(@Param("bonusBudgetId")Long bonusBudgetId);

    /**
    * 物理批量删除奖金预算表
    *
    * @param bonusBudgetIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteBonusBudgetByBonusBudgetIds(@Param("bonusBudgetIds")List<Long> bonusBudgetIds);

    /**
    * 批量新增奖金预算表
    *
    * @param BonusBudgets 奖金预算表列表
    * @return 结果
    */
    int batchBonusBudget(@Param("bonusBudgets")List<BonusBudget> BonusBudgets);

    /**
     * 返回最大年份
     * @return
     */
    int queryBonusBudgetYear();

    /**
     * 查询总奖金预算赋值部门年终奖经营绩效结果
     * @param annualBonusYear
     * @return
     */
    List<DeptAnnualBonusOperateDTO> selectDeptAnnualBonusOperate(@Param("annualBonusYear")int annualBonusYear);


}
