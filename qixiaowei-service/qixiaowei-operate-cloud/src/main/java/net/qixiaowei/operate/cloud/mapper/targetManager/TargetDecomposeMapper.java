package net.qixiaowei.operate.cloud.mapper.targetManager;

import java.util.List;

import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetDecompose;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * TargetDecomposeMapper接口
 *
 * @author TANGMICHI
 * @since 2022-10-27
 */
public interface TargetDecomposeMapper {
    /**
     * 查询目标分解表
     *
     * @param targetDecomposeId 目标分解(销售订单)表主键
     * @return 目标分解表
     */
    TargetDecomposeDTO selectTargetDecomposeByTargetDecomposeId(@Param("targetDecomposeId") Long targetDecomposeId);

    /**
     * 联合主键查询目标分解表
     *
     * @param targetDecompose
     * @return
     */
    TargetDecomposeDTO selectTargetDecomposeUniteId(@Param("targetDecompose") TargetDecompose targetDecompose);


    /**
     * 批量查询目标分解表
     *
     * @param targetDecomposeIds 目标分解(销售订单)表主键集合
     * @return 目标分解表
     */
    List<TargetDecomposeDTO> selectTargetDecomposeByTargetDecomposeIds(@Param("targetDecomposeIds") List<Long> targetDecomposeIds);

    /**
     * 查询目标分解表列表
     *
     * @param targetDecompose 目标分解表
     * @return 目标分解表集合
     */
    List<TargetDecomposeDTO> selectTargetDecomposeList(@Param("targetDecompose") TargetDecompose targetDecompose);


    /**
     * 查询滚动预测列表
     *
     * @param targetDecompose 目标分解表
     * @return 滚动预测列表集合
     */
    List<TargetDecomposeDTO> selectRollPageList(@Param("targetDecompose") TargetDecompose targetDecompose);
    /**
     * 新增目标分解(销售订单)表
     *
     * @param targetDecompose 目标分解(销售订单)表
     * @return 结果
     */
    int insertTargetDecompose(@Param("targetDecompose") TargetDecompose targetDecompose);

    /**
     * 修改目标分解(销售订单)表
     *
     * @param targetDecompose 目标分解(销售订单)表
     * @return 结果
     */
    int updateTargetDecompose(@Param("targetDecompose") TargetDecompose targetDecompose);

    /**
     * 批量修改目标分解(销售订单)表
     *
     * @param targetDecomposeList 目标分解(销售订单)表
     * @return 结果
     */
    int updateTargetDecomposes(@Param("targetDecomposeList") List<TargetDecompose> targetDecomposeList);

    /**
     * 逻辑删除目标分解(销售订单)表
     *
     * @param targetDecompose
     * @return 结果
     */
    int logicDeleteTargetDecomposeByTargetDecomposeId(@Param("targetDecompose") TargetDecompose targetDecompose);

    /**
     * 逻辑批量删除目标分解(销售订单)表
     *
     * @param targetDecomposeIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteTargetDecomposeByTargetDecomposeIds(@Param("targetDecomposeIds") List<Long> targetDecomposeIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 批量新增目标分解(销售订单)表
     *
     * @param TargetDecomposes 目标分解(销售订单)表列表
     * @return 结果
     */
    int batchTargetDecompose(@Param("targetDecomposes") List<TargetDecompose> TargetDecomposes);
}
