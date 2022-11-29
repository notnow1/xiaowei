package net.qixiaowei.operate.cloud.mapper.salary;

import java.util.List;

import net.qixiaowei.operate.cloud.api.domain.salary.BonusBudget;
import net.qixiaowei.operate.cloud.api.domain.salary.BonusBudgetParameters;
import net.qixiaowei.operate.cloud.api.dto.salary.BonusBudgetParametersDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* BonusBudgetParametersMapper接口
* @author TANGMICHI
* @since 2022-11-26
*/
public interface BonusBudgetParametersMapper{
    /**
    * 查询奖金预算参数表
    *
    * @param bonusBudgetParametersId 奖金预算参数表主键
    * @return 奖金预算参数表
    */
    BonusBudgetParametersDTO selectBonusBudgetParametersByBonusBudgetParametersId(@Param("bonusBudgetParametersId")Long bonusBudgetParametersId);


    /**
     * 根据总奖金id查询奖金预算参数表
     *
     * @param bonusBudgetId 奖金预算表主表主键
     * @return 奖金预算参数表
     */
    List<BonusBudgetParametersDTO> selectBonusBudgetParametersByBonusBudgetId(@Param("bonusBudgetId")Long bonusBudgetId);
    /**
     * 根据总奖金id集合查询奖金预算参数表
     *
     * @param bonusBudgetIds 奖金预算表主表主键集合
     * @return 奖金预算参数表
     */
    List<BonusBudgetParametersDTO> selectBonusBudgetParametersByBonusBudgetIds(@Param("bonusBudgetIds") List<Long> bonusBudgetIds);
    /**
    * 批量查询奖金预算参数表
    *
    * @param bonusBudgetParametersIds 奖金预算参数表主键集合
    * @return 奖金预算参数表
    */
    List<BonusBudgetParametersDTO> selectBonusBudgetParametersByBonusBudgetParametersIds(@Param("bonusBudgetParametersIds") List<Long> bonusBudgetParametersIds);

    /**
    * 查询奖金预算参数表列表
    *
    * @param bonusBudgetParameters 奖金预算参数表
    * @return 奖金预算参数表集合
    */
    List<BonusBudgetParametersDTO> selectBonusBudgetParametersList(@Param("bonusBudgetParameters")BonusBudgetParameters bonusBudgetParameters);

    /**
    * 新增奖金预算参数表
    *
    * @param bonusBudgetParameters 奖金预算参数表
    * @return 结果
    */
    int insertBonusBudgetParameters(@Param("bonusBudgetParameters")BonusBudgetParameters bonusBudgetParameters);

    /**
    * 修改奖金预算参数表
    *
    * @param bonusBudgetParameters 奖金预算参数表
    * @return 结果
    */
    int updateBonusBudgetParameters(@Param("bonusBudgetParameters")BonusBudgetParameters bonusBudgetParameters);

    /**
    * 批量修改奖金预算参数表
    *
    * @param bonusBudgetParametersList 奖金预算参数表
    * @return 结果
    */
    int updateBonusBudgetParameterss(@Param("bonusBudgetParametersList")List<BonusBudgetParameters> bonusBudgetParametersList);
    /**
    * 逻辑删除奖金预算参数表
    *
    * @param bonusBudgetParameters
    * @return 结果
    */
    int logicDeleteBonusBudgetParametersByBonusBudgetParametersId(@Param("bonusBudgetParameters")BonusBudgetParameters bonusBudgetParameters);

    /**
    * 逻辑批量删除奖金预算参数表
    *
    * @param bonusBudgetParametersIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteBonusBudgetParametersByBonusBudgetParametersIds(@Param("bonusBudgetParametersIds")List<Long> bonusBudgetParametersIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除奖金预算参数表
    *
    * @param bonusBudgetParametersId 奖金预算参数表主键
    * @return 结果
    */
    int deleteBonusBudgetParametersByBonusBudgetParametersId(@Param("bonusBudgetParametersId")Long bonusBudgetParametersId);

    /**
    * 物理批量删除奖金预算参数表
    *
    * @param bonusBudgetParametersIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteBonusBudgetParametersByBonusBudgetParametersIds(@Param("bonusBudgetParametersIds")List<Long> bonusBudgetParametersIds);

    /**
    * 批量新增奖金预算参数表
    *
    * @param BonusBudgetParameterss 奖金预算参数表列表
    * @return 结果
    */
    int batchBonusBudgetParameters(@Param("bonusBudgetParameterss")List<BonusBudgetParameters> BonusBudgetParameterss);
}
