package net.qixiaowei.operate.cloud.service.performance;

import java.util.List;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformancePercentageDataDTO;


/**
* PerformancePercentageDataService接口
* @author Graves
* @since 2022-10-10
*/
public interface IPerformancePercentageDataService{
    /**
    * 查询绩效比例数据表
    *
    * @param performancePercentageDataId 绩效比例数据表主键
    * @return 绩效比例数据表
    */
    PerformancePercentageDataDTO selectPerformancePercentageDataByPerformancePercentageDataId(Long performancePercentageDataId);

    /**
    * 查询绩效比例数据表列表
    *
    * @param performancePercentageDataDTO 绩效比例数据表
    * @return 绩效比例数据表集合
    */
    List<PerformancePercentageDataDTO> selectPerformancePercentageDataList(PerformancePercentageDataDTO performancePercentageDataDTO);

    /**
    * 新增绩效比例数据表
    *
    * @param performancePercentageDataDTO 绩效比例数据表
    * @return 结果
    */
    int insertPerformancePercentageData(PerformancePercentageDataDTO performancePercentageDataDTO);

    /**
    * 修改绩效比例数据表
    *
    * @param performancePercentageDataDTO 绩效比例数据表
    * @return 结果
    */
    int updatePerformancePercentageData(PerformancePercentageDataDTO performancePercentageDataDTO);

    /**
    * 批量修改绩效比例数据表
    *
    * @param performancePercentageDataDtos 绩效比例数据表
    * @return 结果
    */
    int updatePerformancePercentageDatas(List<PerformancePercentageDataDTO> performancePercentageDataDtos);

    /**
    * 批量新增绩效比例数据表
    *
    * @param performancePercentageDataDtos 绩效比例数据表
    * @return 结果
    */
    int insertPerformancePercentageDatas(List<PerformancePercentageDataDTO> performancePercentageDataDtos);

    /**
    * 逻辑批量删除绩效比例数据表
    *
    * @param PerformancePercentageDataDtos 需要删除的绩效比例数据表集合
    * @return 结果
    */
    int logicDeletePerformancePercentageDataByPerformancePercentageDataIds(List<PerformancePercentageDataDTO> PerformancePercentageDataDtos);

    /**
    * 逻辑删除绩效比例数据表信息
    *
    * @param performancePercentageDataDTO
    * @return 结果
    */
    int logicDeletePerformancePercentageDataByPerformancePercentageDataId(PerformancePercentageDataDTO performancePercentageDataDTO);
    /**
    * 逻辑批量删除绩效比例数据表
    *
    * @param PerformancePercentageDataDtos 需要删除的绩效比例数据表集合
    * @return 结果
    */
    int deletePerformancePercentageDataByPerformancePercentageDataIds(List<PerformancePercentageDataDTO> PerformancePercentageDataDtos);

    /**
    * 逻辑删除绩效比例数据表信息
    *
    * @param performancePercentageDataDTO
    * @return 结果
    */
    int deletePerformancePercentageDataByPerformancePercentageDataId(PerformancePercentageDataDTO performancePercentageDataDTO);


    /**
    * 删除绩效比例数据表信息
    *
    * @param performancePercentageDataId 绩效比例数据表主键
    * @return 结果
    */
    int deletePerformancePercentageDataByPerformancePercentageDataId(Long performancePercentageDataId);
}
