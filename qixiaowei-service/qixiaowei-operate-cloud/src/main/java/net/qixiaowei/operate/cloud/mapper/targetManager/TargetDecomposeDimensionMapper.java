package net.qixiaowei.operate.cloud.mapper.targetManager;

import java.util.List;

import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetDecomposeDimension;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDimensionDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * TargetDecomposeDimensionMapper接口
 *
 * @author Graves
 * @since 2022-09-26
 */
public interface TargetDecomposeDimensionMapper {
    /**
     * 查询目标分解维度配置
     *
     * @param targetDecomposeDimensionId 目标分解维度配置主键
     * @return 目标分解维度配置
     */
    TargetDecomposeDimensionDTO selectTargetDecomposeDimensionByTargetDecomposeDimensionId(@Param("targetDecomposeDimensionId") Long targetDecomposeDimensionId);

    /**
     * 查询目标分解维度配置列表
     *
     * @param targetDecomposeDimension 目标分解维度配置
     * @return 目标分解维度配置集合
     */
    List<TargetDecomposeDimensionDTO> selectTargetDecomposeDimensionList(@Param("targetDecomposeDimension") TargetDecomposeDimension targetDecomposeDimension);

    /**
     * 分解维度重复性校验
     *
     * @param dimension
     * @return
     */
    Integer checkDimension(@Param("dimension") String dimension);

    /**
     * 新增目标分解维度配置
     *
     * @param targetDecomposeDimension 目标分解维度配置
     * @return 结果
     */
    int insertTargetDecomposeDimension(@Param("targetDecomposeDimension") TargetDecomposeDimension targetDecomposeDimension);

    /**
     * 修改目标分解维度配置
     *
     * @param targetDecomposeDimension 目标分解维度配置
     * @return 结果
     */
    int updateTargetDecomposeDimension(@Param("targetDecomposeDimension") TargetDecomposeDimension targetDecomposeDimension);

    /**
     * 批量修改目标分解维度配置
     *
     * @param targetDecomposeDimensionList 目标分解维度配置
     * @return 结果
     */
    int updateTargetDecomposeDimensions(@Param("targetDecomposeDimensionList") List<TargetDecomposeDimension> targetDecomposeDimensionList);

    /**
     * 逻辑删除目标分解维度配置
     *
     * @param targetDecomposeDimension
     * @return 结果
     */
    int logicDeleteTargetDecomposeDimensionByTargetDecomposeDimensionId(@Param("targetDecomposeDimension") TargetDecomposeDimension targetDecomposeDimension);

    /**
     * 逻辑批量删除目标分解维度配置
     *
     * @param targetDecomposeDimensionIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteTargetDecomposeDimensionByTargetDecomposeDimensionIds(@Param("targetDecomposeDimensionIds") List<Long> targetDecomposeDimensionIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除目标分解维度配置
     *
     * @param targetDecomposeDimensionId 目标分解维度配置主键
     * @return 结果
     */
    int deleteTargetDecomposeDimensionByTargetDecomposeDimensionId(@Param("targetDecomposeDimensionId") Long targetDecomposeDimensionId);

    /**
     * 物理批量删除目标分解维度配置
     *
     * @param targetDecomposeDimensionIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteTargetDecomposeDimensionByTargetDecomposeDimensionIds(@Param("targetDecomposeDimensionIds") List<Long> targetDecomposeDimensionIds);

    /**
     * 批量新增目标分解维度配置
     *
     * @param TargetDecomposeDimensions 目标分解维度配置列表
     * @return 结果
     */
    int batchTargetDecomposeDimension(@Param("targetDecomposeDimensions") List<TargetDecomposeDimension> TargetDecomposeDimensions);

    /**
     * 获取当前最大sort值
     *
     * @return 结果
     */
    int getMaxTargetDimensionConfigSort();

    /**
     * 判断该分解为度是否存在
     *
     * @param targetDecomposeDimensionIds
     * @return
     */
    List<TargetDecomposeDimensionDTO> isExist(@Param("targetDecomposeDimensionIds") List<Long> targetDecomposeDimensionIds);
}
