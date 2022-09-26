package net.qixiaowei.operate.cloud.service.targetManager;

import java.util.List;

import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetDecomposeDimension;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDimensionDTO;


/**
 * TargetDecomposeDimensionService接口
 *
 * @author Graves
 * @since 2022-09-26
 */
public interface ITargetDecomposeDimensionService {
    /**
     * 查询目标分解维度配置
     *
     * @param targetDecomposeDimensionId 目标分解维度配置主键
     * @return 目标分解维度配置
     */
    TargetDecomposeDimensionDTO selectTargetDecomposeDimensionByTargetDecomposeDimensionId(Long targetDecomposeDimensionId);

    /**
     * 查询目标分解维度配置列表
     *
     * @param targetDecomposeDimensionDTO 目标分解维度配置
     * @return 目标分解维度配置集合
     */
    List<TargetDecomposeDimensionDTO> selectTargetDecomposeDimensionList(TargetDecomposeDimensionDTO targetDecomposeDimensionDTO);

    /**
     * 新增目标分解维度配置
     *
     * @param targetDecomposeDimensionDTO 目标分解维度配置
     * @return 结果
     */
    int insertTargetDecomposeDimension(TargetDecomposeDimensionDTO targetDecomposeDimensionDTO);

    /**
     * 修改目标分解维度配置
     *
     * @param targetDecomposeDimensionDTO 目标分解维度配置
     * @return 结果
     */
    int updateTargetDecomposeDimension(TargetDecomposeDimensionDTO targetDecomposeDimensionDTO);

    /**
     * 批量修改目标分解维度配置
     *
     * @param targetDecomposeDimensionDtos 目标分解维度配置
     * @return 结果
     */
    int updateTargetDecomposeDimensions(List<TargetDecomposeDimensionDTO> targetDecomposeDimensionDtos);

    /**
     * 批量新增目标分解维度配置
     *
     * @param targetDecomposeDimensionDtos 目标分解维度配置
     * @return 结果
     */
    int insertTargetDecomposeDimensions(List<TargetDecomposeDimensionDTO> targetDecomposeDimensionDtos);

    /**
     * 逻辑批量删除目标分解维度配置
     *
     * @param TargetDecomposeDimensionDtos 需要删除的目标分解维度配置集合
     * @return 结果
     */
    int logicDeleteTargetDecomposeDimensionByTargetDecomposeDimensionIds(List<TargetDecomposeDimensionDTO> TargetDecomposeDimensionDtos);

    /**
     * 逻辑删除目标分解维度配置信息
     *
     * @param targetDecomposeDimensionDTO
     * @return 结果
     */
    int logicDeleteTargetDecomposeDimensionByTargetDecomposeDimensionId(TargetDecomposeDimensionDTO targetDecomposeDimensionDTO);

    /**
     * 逻辑批量删除目标分解维度配置
     *
     * @param TargetDecomposeDimensionDtos 需要删除的目标分解维度配置集合
     * @return 结果
     */
    int deleteTargetDecomposeDimensionByTargetDecomposeDimensionIds(List<TargetDecomposeDimensionDTO> TargetDecomposeDimensionDtos);

    /**
     * 逻辑删除目标分解维度配置信息
     *
     * @param targetDecomposeDimensionDTO
     * @return 结果
     */
    int deleteTargetDecomposeDimensionByTargetDecomposeDimensionId(TargetDecomposeDimensionDTO targetDecomposeDimensionDTO);


    /**
     * 删除目标分解维度配置信息
     *
     * @param targetDecomposeDimensionId 目标分解维度配置主键
     * @return 结果
     */
    int deleteTargetDecomposeDimensionByTargetDecomposeDimensionId(Long targetDecomposeDimensionId);

    /**
     * 校验分解维度的唯一性
     *
     * @param dimension
     * @return
     */
    boolean checkUnique(String dimension, TargetDecomposeDimension targetDecomposeDimension);

    /**
     * todo 引用校验
     *
     * @return
     */
    boolean isQuote(Long targetDecomposeDimensionId);
}
