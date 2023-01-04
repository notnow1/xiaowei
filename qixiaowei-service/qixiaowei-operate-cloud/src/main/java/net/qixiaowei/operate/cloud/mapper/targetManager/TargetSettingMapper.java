package net.qixiaowei.operate.cloud.mapper.targetManager;

import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetSetting;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


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
    TargetSettingDTO selectTargetSettingByTargetYearAndIndicator(@Param("targetYear") Integer targetYear, @Param("targetSettingType") Integer targetSettingType);

    /**
     * @param historyNumS 历史年度list
     * @param indicatorId 指标id
     * @return
     */
    List<TargetSettingDTO> selectTargetSettingByIndicator(@Param("historyNumS") List<Integer> historyNumS, @Param("indicatorId") Long indicatorId);

    /**
     * 查询经营分析报表列表
     *
     * @param targetSetting
     * @return
     */
    List<TargetSettingDTO> selectAnalyseList(@Param("targetSetting") TargetSetting targetSetting);

    /**
     * 通过Types-List查询
     *
     * @return
     */
    List<TargetSettingDTO> selectTargetSettingByTypes(@Param("targetSettingType") List<Integer> targetSettingType, @Param("targetYear") Integer targetYear);

    /**
     * 根据年份区间查找
     *
     * @param targetSetting
     * @param historyYears
     * @return
     */
    List<TargetSettingDTO> selectTargetSettingByYears(@Param("targetSetting") TargetSettingDTO targetSetting, @Param("historyYears") List<Integer> historyYears);

    /**
     * 通过指标ID集合和年份查找
     * @param indicatorIds
     * @param targetYear
     * @return
     */
    List<TargetSettingDTO> selectTargetSettingByIndicators(@Param("indicatorIds") List<Long> indicatorIds, @Param("targetYear") Integer targetYear);

    /**
     * 根据年份指标id查询数据
     * @param targetSetting
     * @return
     */
    List<TargetSettingDTO> selectSetDrivingFactor(@Param("targetSetting") TargetSetting targetSetting);
}
