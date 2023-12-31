package net.qixiaowei.operate.cloud.mapper.targetManager;

import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetOutcomeDetails;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetOutcomeDetailsDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


/**
 * TargetOutcomeDetailsMapper接口
 *
 * @author TANGMICHI
 * @since 2022-11-07
 */
public interface TargetOutcomeDetailsMapper {
    /**
     * 查询目标结果详情表
     *
     * @param targetOutcomeDetailsId 目标结果详情表主键
     * @return 目标结果详情表
     */
    TargetOutcomeDetailsDTO selectTargetOutcomeDetailsByTargetOutcomeDetailsId(@Param("targetOutcomeDetailsId") Long targetOutcomeDetailsId);


    /**
     * 批量查询目标结果详情表
     *
     * @param targetOutcomeDetailsIds 目标结果详情表主键集合
     * @return 目标结果详情表
     */
    List<TargetOutcomeDetailsDTO> selectTargetOutcomeDetailsByTargetOutcomeDetailsIds(@Param("targetOutcomeDetailsIds") List<Long> targetOutcomeDetailsIds);

    /**
     * 查询目标结果详情表列表
     *
     * @param targetOutcomeDetails 目标结果详情表
     * @return 目标结果详情表集合
     */
    List<TargetOutcomeDetailsDTO> selectTargetOutcomeDetailsList(@Param("targetOutcomeDetails") TargetOutcomeDetails targetOutcomeDetails);

    /**
     * 新增目标结果详情表
     *
     * @param targetOutcomeDetails 目标结果详情表
     * @return 结果
     */
    int insertTargetOutcomeDetails(@Param("targetOutcomeDetails") TargetOutcomeDetails targetOutcomeDetails);

    /**
     * 修改目标结果详情表
     *
     * @param targetOutcomeDetails 目标结果详情表
     * @return 结果
     */
    int updateTargetOutcomeDetails(@Param("targetOutcomeDetails") TargetOutcomeDetails targetOutcomeDetails);

    /**
     * 批量修改目标结果详情表
     *
     * @param targetOutcomeDetailsList 目标结果详情表
     * @return 结果
     */
    int updateTargetOutcomeDetailss(@Param("targetOutcomeDetailsList") List<TargetOutcomeDetails> targetOutcomeDetailsList);

    /**
     * 逻辑删除目标结果详情表
     *
     * @param targetOutcomeDetails
     * @return 结果
     */
    int logicDeleteTargetOutcomeDetailsByTargetOutcomeDetailsId(@Param("targetOutcomeDetails") TargetOutcomeDetails targetOutcomeDetails);

    /**
     * 逻辑批量删除目标结果详情表
     *
     * @param targetOutcomeDetailsIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteTargetOutcomeDetailsByTargetOutcomeDetailsIds(@Param("targetOutcomeDetailsIds") List<Long> targetOutcomeDetailsIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除目标结果详情表
     *
     * @param targetOutcomeDetailsId 目标结果详情表主键
     * @return 结果
     */
    int deleteTargetOutcomeDetailsByTargetOutcomeDetailsId(@Param("targetOutcomeDetailsId") Long targetOutcomeDetailsId);

    /**
     * 物理批量删除目标结果详情表
     *
     * @param targetOutcomeDetailsIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteTargetOutcomeDetailsByTargetOutcomeDetailsIds(@Param("targetOutcomeDetailsIds") List<Long> targetOutcomeDetailsIds);

    /**
     * 批量新增目标结果详情表
     *
     * @param TargetOutcomeDetailss 目标结果详情表列表
     * @return 结果
     */
    int batchTargetOutcomeDetails(@Param("targetOutcomeDetailss") List<TargetOutcomeDetails> TargetOutcomeDetailss);

    /**
     * 逻辑批量删除目标结果详情表根据指标ID和OutComeID
     *
     * @param updateBy
     * @param updateTime
     * @param indicators
     * @param targetOutcomeId
     * @return
     */
    int logicDeleteTargetOutcomeDetailsByOutcomeIdAndIndicator(@Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime, @Param("indicators") List<Long> indicators, @Param("targetOutcomeId") Long targetOutcomeId);

    /**
     * 根据outId查找目标结果详情表
     *
     * @param targetOutcomeId
     * @return
     */
    List<TargetOutcomeDetailsDTO> selectTargetOutcomeDetailsByOutcomeId(@Param("targetOutcomeId") Long targetOutcomeId);

    /**
     * 通过指标id集合查找结果详情表
     *
     * @param indicatorIds
     * @param targetOutcomeId
     * @return
     */
    List<TargetOutcomeDetailsDTO> selectTargetOutcomeDetailsByIndicatorIds(@Param("indicatorIds") List<Long> indicatorIds, @Param("targetOutcomeId") Long targetOutcomeId);

    /**
     * 通过目标年度查询目标结果详情
     *
     * @param targetYear
     * @param indicatorId
     * @return
     */
    TargetOutcomeDetailsDTO selectTargetOutcomeDetailByTargetYear(@Param("targetYear") Integer targetYear, @Param("indicatorId") Long indicatorId);
}
