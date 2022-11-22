package net.qixiaowei.operate.cloud.mapper.targetManager;

import java.util.List;

import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetSettingIncome;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingIncomeDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * TargetSettingIncomeMapper接口
 *
 * @author Graves
 * @since 2022-10-31
 */
public interface TargetSettingIncomeMapper {
    /**
     * 查询目标制定收入表
     *
     * @param targetSettingIncomeId 目标制定收入表主键
     * @return 目标制定收入表
     */
    TargetSettingIncomeDTO selectTargetSettingIncomeByTargetSettingIncomeId(@Param("targetSettingIncomeId") Long targetSettingIncomeId);


    /**
     * 批量查询目标制定收入表
     *
     * @param targetSettingIncomeIds 目标制定收入表主键集合
     * @return 目标制定收入表
     */
    List<TargetSettingIncomeDTO> selectTargetSettingIncomeByTargetSettingIncomeIds(@Param("targetSettingIncomeIds") List<Long> targetSettingIncomeIds);

    /**
     * 查询目标制定收入表列表
     *
     * @param targetSettingIncome 目标制定收入表
     * @return 目标制定收入表集合
     */
    List<TargetSettingIncomeDTO> selectTargetSettingIncomeList(@Param("targetSettingIncome") TargetSettingIncome targetSettingIncome);

    /**
     * 新增目标制定收入表
     *
     * @param targetSettingIncome 目标制定收入表
     * @return 结果
     */
    int insertTargetSettingIncome(@Param("targetSettingIncome") TargetSettingIncome targetSettingIncome);

    /**
     * 修改目标制定收入表
     *
     * @param targetSettingIncome 目标制定收入表
     * @return 结果
     */
    int updateTargetSettingIncome(@Param("targetSettingIncome") TargetSettingIncome targetSettingIncome);

    /**
     * 批量修改目标制定收入表
     *
     * @param targetSettingIncomeList 目标制定收入表
     * @return 结果
     */
    int updateTargetSettingIncomes(@Param("targetSettingIncomeList") List<TargetSettingIncome> targetSettingIncomeList);

    /**
     * 逻辑删除目标制定收入表
     *
     * @param targetSettingIncome
     * @return 结果
     */
    int logicDeleteTargetSettingIncomeByTargetSettingIncomeId(@Param("targetSettingIncome") TargetSettingIncome targetSettingIncome);

    /**
     * 逻辑批量删除目标制定收入表
     *
     * @param targetSettingIncomeIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteTargetSettingIncomeByTargetSettingIncomeIds(@Param("targetSettingIncomeIds") List<Long> targetSettingIncomeIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除目标制定收入表
     *
     * @param targetSettingIncomeId 目标制定收入表主键
     * @return 结果
     */
    int deleteTargetSettingIncomeByTargetSettingIncomeId(@Param("targetSettingIncomeId") Long targetSettingIncomeId);

    /**
     * 物理批量删除目标制定收入表
     *
     * @param targetSettingIncomeIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteTargetSettingIncomeByTargetSettingIncomeIds(@Param("targetSettingIncomeIds") List<Long> targetSettingIncomeIds);

    /**
     * 批量新增目标制定收入表
     *
     * @param TargetSettingIncomes 目标制定收入表列表
     * @return 结果
     */
    int batchTargetSettingIncome(@Param("targetSettingIncomes") List<TargetSettingIncome> TargetSettingIncomes);

    /**
     * 通过年限List获取目标收入列表
     *
     * @param targetSettingId 目标制定ID
     */
    List<TargetSettingIncomeDTO> selectTargetSettingIncomeByTargetSettingId(@Param("targetSettingId") Long targetSettingId);

}
