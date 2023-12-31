package net.qixiaowei.operate.cloud.mapper.targetManager;

import java.util.List;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetDecompose;
import net.qixiaowei.operate.cloud.api.dto.dashboard.TargetAchieveAnalysisDTO;
import net.qixiaowei.operate.cloud.api.dto.dashboard.TargetLeaderboardDTO;
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
     * @param targetDecomposeId 目标分解表主键
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
     * 根据时间维度查询目标分解所有数据
     *
     * @param timeDimension 目标分解表
     * @return 目标分解表集合
     */
    List<TargetDecomposeDTO> selectCronCreateHistoryList(@Param("timeDimension") int timeDimension);

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

    /**
     * 查询经营结果分析报表列表
     *
     * @param targetDecompose
     * @return
     */
    List<TargetDecomposeDTO> selectResultList(@Param("targetDecompose") TargetDecompose targetDecompose);

    /**
     * 目标分解是否被引用
     *
     * @param departmentIds 部门ID
     * @return
     */
    List<TargetDecompose> queryDeptDecompose(@Param("departmentIds") List<Long> departmentIds);

    /**
     * 查询目标分解预制数据年份
     *
     * @param targetDecompose 目标分解
     * @return TargetDecomposeDTO
     */
    TargetDecomposeDTO selectMaxYear(@Param("targetDecompose") TargetDecompose targetDecompose);

    /**
     * 根据指标ID查询目标分解
     *
     * @param indicatorIds 指标ID集合
     * @return List
     */
    List<TargetDecomposeDTO> selectByIndicatorIds(@Param("indicatorIds") List<Long> indicatorIds);

    /**
     * 获取目标分解的指标数据
     *
     * @return List
     */
    List<TargetDecomposeDTO> selectTargetDecomposeIndicator();

    /**
     * 取最近一次分解维度的数据
     *
     * @return List
     */
    List<TargetAchieveAnalysisDTO> selectRecentDecompose();

    /**
     * 获取分解维度数据
     *
     * @param targetDecomposeDTO 分解DTO
     * @return List
     */
    List<TargetAchieveAnalysisDTO> selectAchieveAnalysisDecompose(@Param("targetDecomposeDTO") TargetDecomposeDTO targetDecomposeDTO);

    /**
     * 获取仪表盘分解维度列表
     *
     * @param targetDecomposeDTO 分解维度DTO
     * @return List
     */
    List<TargetLeaderboardDTO> selectLeaderboardDecompose(@Param("targetDecomposeDTO") TargetDecomposeDTO targetDecomposeDTO);

    /**
     * 取最近一次分解维度的数据
     *
     * @return List
     */
    List<TargetLeaderboardDTO> selectRecentDecompose2();

    /**
     * 引用校验
     *
     * @param targetDecomposeDimensionIds 分结维度ID集合
     * @return 结果
     */
    List<TargetDecomposeDTO> selectListByTargetDecomposeDimensionIds(@Param("targetDecomposeDimensionIds") List<Long> targetDecomposeDimensionIds);
}
