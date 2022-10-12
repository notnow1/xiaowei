package net.qixiaowei.operate.cloud.mapper.performance;

import net.qixiaowei.operate.cloud.api.domain.performance.PerformancePercentageData;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformancePercentageDataDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


/**
 * PerformancePercentageDataMapper接口
 *
 * @author Graves
 * @since 2022-10-10
 */
public interface PerformancePercentageDataMapper {
    /**
     * 查询绩效比例数据表
     *
     * @param performancePercentageDataId 绩效比例数据表主键
     * @return 绩效比例数据表
     */
    PerformancePercentageDataDTO selectPerformancePercentageDataByPerformancePercentageDataId(@Param("performancePercentageDataId") Long performancePercentageDataId);

    /**
     * 查询绩效比例数据表列表
     *
     * @param performancePercentageData 绩效比例数据表
     * @return 绩效比例数据表集合
     */
    List<PerformancePercentageDataDTO> selectPerformancePercentageDataList(@Param("performancePercentageData") PerformancePercentageData performancePercentageData);

    /**
     * 新增绩效比例数据表
     *
     * @param performancePercentageData 绩效比例数据表
     * @return 结果
     */
    int insertPerformancePercentageData(@Param("performancePercentageData") PerformancePercentageData performancePercentageData);

    /**
     * 修改绩效比例数据表
     *
     * @param performancePercentageData 绩效比例数据表
     * @return 结果
     */
    int updatePerformancePercentageData(@Param("performancePercentageData") PerformancePercentageData performancePercentageData);

    /**
     * 批量修改绩效比例数据表
     *
     * @param performancePercentageDataList 绩效比例数据表
     * @return 结果
     */
    int updatePerformancePercentageDatas(@Param("performancePercentageDataList") List<PerformancePercentageData> performancePercentageDataList);

    /**
     * 逻辑删除绩效比例数据表
     *
     * @param performancePercentageData
     * @return 结果
     */
    int logicDeletePerformancePercentageDataByPerformancePercentageDataId(@Param("performancePercentageData") PerformancePercentageData performancePercentageData);

    /**
     * 逻辑批量删除绩效比例数据表
     *
     * @param performancePercentageDataIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeletePerformancePercentageDataByPerformancePercentageDataIds(@Param("performancePercentageDataIds") List<Long> performancePercentageDataIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除绩效比例数据表
     *
     * @param performancePercentageDataId 绩效比例数据表主键
     * @return 结果
     */
    int deletePerformancePercentageDataByPerformancePercentageDataId(@Param("performancePercentageDataId") Long performancePercentageDataId);

    /**
     * 物理批量删除绩效比例数据表
     *
     * @param performancePercentageDataIds 需要删除的数据主键集合
     * @return 结果
     */
    int deletePerformancePercentageDataByPerformancePercentageDataIds(@Param("performancePercentageDataIds") List<Long> performancePercentageDataIds);

    /**
     * 批量新增绩效比例数据表
     *
     * @param PerformancePercentageDatas 绩效比例数据表列表
     * @return 结果
     */
    int batchPerformancePercentageData(@Param("performancePercentageDatas") List<PerformancePercentageData> PerformancePercentageDatas);

    /**
     * 通过performancePercentageId查找绩效比例信息列表
     *
     * @param performancePercentageId
     * @return
     */
    List<PerformancePercentageDataDTO> selectPerformancePercentageDataByPerformancePercentageId(@Param("performancePercentageId") Long performancePercentageId);

    /**
     * 通过performancePercentageIds查找绩效比例信息列表
     *
     * @param performancePercentageIds
     * @return
     */
    List<Long> selectPerformancePercentageDataByPerformancePercentageIds(@Param("performancePercentageIds") List<Long> performancePercentageIds);
}
