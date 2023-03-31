package net.qixiaowei.operate.cloud.service.performance;

import net.qixiaowei.operate.cloud.api.domain.performance.PerformanceAppraisalItems;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalItemsDTO;
import net.qixiaowei.operate.cloud.excel.performance.PerformanceAppraisalItemsExcel;

import java.util.List;
import java.util.Map;


/**
 * PerformanceAppraisalItemsService接口
 *
 * @author Graves
 * @since 2022-12-06
 */
public interface IPerformanceAppraisalItemsService {
    /**
     * 查询绩效考核项目表
     *
     * @param performAppraisalItemsId 绩效考核项目表主键
     * @return 绩效考核项目表
     */
    PerformanceAppraisalItemsDTO selectPerformanceAppraisalItemsByPerformAppraisalItemsId(Long performAppraisalItemsId);

    /**
     * 查询绩效考核项目表列表
     *
     * @param performanceAppraisalItemsDTO 绩效考核项目表
     * @return 绩效考核项目表集合
     */
    List<PerformanceAppraisalItemsDTO> selectPerformanceAppraisalItemsList(PerformanceAppraisalItemsDTO performanceAppraisalItemsDTO);

    /**
     * 新增绩效考核项目表
     *
     * @param performanceAppraisalItemsDTO 绩效考核项目表
     * @return 结果
     */
    PerformanceAppraisalItemsDTO insertPerformanceAppraisalItems(PerformanceAppraisalItemsDTO performanceAppraisalItemsDTO);

    /**
     * 修改绩效考核项目表
     *
     * @param performanceAppraisalItemsDTO 绩效考核项目表
     * @return 结果
     */
    int updatePerformanceAppraisalItems(PerformanceAppraisalItemsDTO performanceAppraisalItemsDTO);

    /**
     * 批量修改绩效考核项目表
     *
     * @param performanceAppraisalItemsDtoS 绩效考核项目表
     */
    void updatePerformanceAppraisalItemsS(List<PerformanceAppraisalItemsDTO> performanceAppraisalItemsDtoS);

    /**
     * 批量新增绩效考核项目表
     *
     * @param performanceAppraisalItemsDtos 绩效考核项目表
     * @return 结果
     */
    List<PerformanceAppraisalItems> insertPerformanceAppraisalItemss(List<PerformanceAppraisalItemsDTO> performanceAppraisalItemsDtos);

    /**
     * 逻辑批量删除绩效考核项目表
     *
     * @param performAppraisalItemsIds 需要删除的绩效考核项目表集合
     */
    void logicDeletePerformanceAppraisalItemsByPerformAppraisalItemsIds(List<Long> performAppraisalItemsIds);

    /**
     * 逻辑删除绩效考核项目表信息
     *
     * @param performanceAppraisalItemsDTO
     * @return 结果
     */
    int logicDeletePerformanceAppraisalItemsByPerformAppraisalItemsId(PerformanceAppraisalItemsDTO performanceAppraisalItemsDTO);

    /**
     * 批量删除绩效考核项目表
     *
     * @param PerformanceAppraisalItemsDtos
     * @return 结果
     */
    int deletePerformanceAppraisalItemsByPerformAppraisalItemsIds(List<PerformanceAppraisalItemsDTO> PerformanceAppraisalItemsDtos);

    /**
     * 逻辑删除绩效考核项目表信息
     *
     * @param performanceAppraisalItemsDTO
     * @return 结果
     */
    int deletePerformanceAppraisalItemsByPerformAppraisalItemsId(PerformanceAppraisalItemsDTO performanceAppraisalItemsDTO);


    /**
     * 删除绩效考核项目表信息
     *
     * @param performAppraisalItemsId 绩效考核项目表主键
     * @return 结果
     */
    int deletePerformanceAppraisalItemsByPerformAppraisalItemsId(Long performAppraisalItemsId);

    /**
     * 导入Excel
     *
     * @param list
     */
    void importPerformanceAppraisalItems(List<PerformanceAppraisalItemsExcel> list);

    /**
     * 导出Excel
     *
     * @param performanceAppraisalItemsDTO
     * @return
     */
    List<PerformanceAppraisalItemsExcel> exportPerformanceAppraisalItems(PerformanceAppraisalItemsDTO performanceAppraisalItemsDTO);

    /**
     * 根据对象ID查找指标列表
     *
     * @param performAppraisalObjectsId 对象ID
     * @return
     */
    List<PerformanceAppraisalItemsDTO> selectPerformanceAppraisalItemsByPerformAppraisalObjectId(Long performAppraisalObjectsId);

    /**
     * 评议撤回
     *
     * @param itemsDTOList 评议指标DTO
     * @return int
     */
    int withdrawPerformanceAppraisalItems(List<PerformanceAppraisalItemsDTO> itemsDTOList);

    /**
     * 查询匹配的对象指标列表
     *
     * @param performanceAppraisalObjectsIds 对象ID集合
     * @return List
     */
    List<PerformanceAppraisalItemsDTO> selectPerformanceAppraisalItemsByPerformAppraisalObjectIds(List<Long> performanceAppraisalObjectsIds);

    /**
     * 根据指标ID集合查询绩效
     *
     * @param map 指标ID集合
     * @return List
     */
    List<PerformanceAppraisalItemsDTO> selectByIndicatorIds(Map<Integer, List<Long>> map);

}
