package net.qixiaowei.operate.cloud.mapper.targetManager;

import java.util.List;

import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetSetting;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * TargetSettingMapper接口
 *
 * @author Graves
 * @since 2022-10-27
 */
public interface TargetSettingMapper {
    /**
     * 查询目标制定
     *
     * @param targetSettingId 目标制定主键
     * @return 目标制定
     */
    TargetSettingDTO selectTargetSettingByTargetSettingId(@Param("targetSettingId") Long targetSettingId);


    /**
     * 批量查询目标制定
     *
     * @param targetSettingIds 目标制定主键集合
     * @return 目标制定
     */
    List<TargetSettingDTO> selectTargetSettingByTargetSettingIds(@Param("targetSettingIds") List<Long> targetSettingIds);

    /**
     * 查询目标制定列表
     *
     * @param targetSetting 目标制定
     * @return 目标制定集合
     */
    List<TargetSettingDTO> selectTargetSettingList(@Param("targetSetting") TargetSetting targetSetting);

    /**
     * 新增目标制定
     *
     * @param targetSetting 目标制定
     * @return 结果
     */
    int insertTargetSetting(@Param("targetSetting") TargetSetting targetSetting);

    /**
     * 修改目标制定
     *
     * @param targetSetting 目标制定
     * @return 结果
     */
    int updateTargetSetting(@Param("targetSetting") TargetSetting targetSetting);

    /**
     * 批量修改目标制定
     *
     * @param targetSettingList 目标制定
     * @return 结果
     */
    int updateTargetSettings(@Param("targetSettingList") List<TargetSetting> targetSettingList);

    /**
     * 逻辑删除目标制定
     *
     * @param targetSetting
     * @return 结果
     */
    int logicDeleteTargetSettingByTargetSettingId(@Param("targetSetting") TargetSetting targetSetting);

    /**
     * 逻辑批量删除目标制定
     *
     * @param targetSettingIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteTargetSettingByTargetSettingIds(@Param("targetSettingIds") List<Long> targetSettingIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除目标制定
     *
     * @param targetSettingId 目标制定主键
     * @return 结果
     */
    int deleteTargetSettingByTargetSettingId(@Param("targetSettingId") Long targetSettingId);

    /**
     * 物理批量删除目标制定
     *
     * @param targetSettingIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteTargetSettingByTargetSettingIds(@Param("targetSettingIds") List<Long> targetSettingIds);

    /**
     * 批量新增目标制定
     *
     * @param TargetSettings 目标制定列表
     * @return 结果
     */
    int batchTargetSetting(@Param("targetSettings") List<TargetSetting> TargetSettings);

    /**
     * 通过目标年份与指标获取目标制定
     *
     * @return
     */
    TargetSettingDTO selectTargetSettingByTargetYearAndIndicator(@Param("targetYear") Integer targetYear, @Param("indicatorId") Long indicatorId);

}
