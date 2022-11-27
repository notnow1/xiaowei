package net.qixiaowei.operate.cloud.service.salary;

import java.util.List;
import net.qixiaowei.operate.cloud.api.dto.salary.BonusBudgetDTO;



/**
* BonusBudgetService接口
* @author TANGMICHI
* @since 2022-11-26
*/
public interface IBonusBudgetService{
    /**
    * 查询奖金预算表
    *
    * @param bonusBudgetId 奖金预算表主键
    * @return 奖金预算表
    */
    BonusBudgetDTO selectBonusBudgetByBonusBudgetId(Long bonusBudgetId);

    /**
    * 查询奖金预算表列表
    *
    * @param bonusBudgetDTO 奖金预算表
    * @return 奖金预算表集合
    */
    List<BonusBudgetDTO> selectBonusBudgetList(BonusBudgetDTO bonusBudgetDTO);

    /**
    * 新增奖金预算表
    *
    * @param bonusBudgetDTO 奖金预算表
    * @return 结果
    */
    BonusBudgetDTO insertBonusBudget(BonusBudgetDTO bonusBudgetDTO);

    /**
    * 修改奖金预算表
    *
    * @param bonusBudgetDTO 奖金预算表
    * @return 结果
    */
    int updateBonusBudget(BonusBudgetDTO bonusBudgetDTO);

    /**
    * 批量修改奖金预算表
    *
    * @param bonusBudgetDtos 奖金预算表
    * @return 结果
    */
    int updateBonusBudgets(List<BonusBudgetDTO> bonusBudgetDtos);

    /**
    * 批量新增奖金预算表
    *
    * @param bonusBudgetDtos 奖金预算表
    * @return 结果
    */
    int insertBonusBudgets(List<BonusBudgetDTO> bonusBudgetDtos);

    /**
    * 逻辑批量删除奖金预算表
    *
    * @param bonusBudgetIds 需要删除的奖金预算表集合
    * @return 结果
    */
    int logicDeleteBonusBudgetByBonusBudgetIds(List<Long> bonusBudgetIds);

    /**
    * 逻辑删除奖金预算表信息
    *
    * @param bonusBudgetDTO
    * @return 结果
    */
    int logicDeleteBonusBudgetByBonusBudgetId(BonusBudgetDTO bonusBudgetDTO);
    /**
    * 批量删除奖金预算表
    *
    * @param BonusBudgetDtos
    * @return 结果
    */
    int deleteBonusBudgetByBonusBudgetIds(List<BonusBudgetDTO> BonusBudgetDtos);

    /**
    * 逻辑删除奖金预算表信息
    *
    * @param bonusBudgetDTO
    * @return 结果
    */
    int deleteBonusBudgetByBonusBudgetId(BonusBudgetDTO bonusBudgetDTO);


    /**
    * 删除奖金预算表信息
    *
    * @param bonusBudgetId 奖金预算表主键
    * @return 结果
    */
    int deleteBonusBudgetByBonusBudgetId(Long bonusBudgetId);

    /**
     * 新增奖金预算预制数据
     * @param budgetYear
     * @return
     */
    BonusBudgetDTO addBonusBudgetTamount(int budgetYear);
}
