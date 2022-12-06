package net.qixiaowei.operate.cloud.service.performance;

import java.util.List;

import net.qixiaowei.operate.cloud.api.dto.performance.PerformAppraisalObjectSnapDTO;
import net.qixiaowei.operate.cloud.excel.performance.PerformAppraisalObjectSnapExcel;


/**
 * PerformAppraisalObjectSnapService接口
 *
 * @author Graves
 * @since 2022-12-05
 */
public interface IPerformAppraisalObjectSnapService {
    /**
     * 查询绩效考核对象快照表
     *
     * @param appraisalObjectSnapId 绩效考核对象快照表主键
     * @return 绩效考核对象快照表
     */
    PerformAppraisalObjectSnapDTO selectPerformAppraisalObjectSnapByAppraisalObjectSnapId(Long appraisalObjectSnapId);

    /**
     * 查询绩效考核对象快照表列表
     *
     * @param performAppraisalObjectSnapDTO 绩效考核对象快照表
     * @return 绩效考核对象快照表集合
     */
    List<PerformAppraisalObjectSnapDTO> selectPerformAppraisalObjectSnapList(PerformAppraisalObjectSnapDTO performAppraisalObjectSnapDTO);

    /**
     * 新增绩效考核对象快照表
     *
     * @param performAppraisalObjectSnapDTO 绩效考核对象快照表
     * @return 结果
     */
    PerformAppraisalObjectSnapDTO insertPerformAppraisalObjectSnap(PerformAppraisalObjectSnapDTO performAppraisalObjectSnapDTO);

    /**
     * 修改绩效考核对象快照表
     *
     * @param performAppraisalObjectSnapDTO 绩效考核对象快照表
     * @return 结果
     */
    int updatePerformAppraisalObjectSnap(PerformAppraisalObjectSnapDTO performAppraisalObjectSnapDTO);

    /**
     * 批量修改绩效考核对象快照表
     *
     * @param performAppraisalObjectSnapDtos 绩效考核对象快照表
     * @return 结果
     */
    int updatePerformAppraisalObjectSnaps(List<PerformAppraisalObjectSnapDTO> performAppraisalObjectSnapDtos);

    /**
     * 批量新增绩效考核对象快照表
     *
     * @param performAppraisalObjectSnapDtos 绩效考核对象快照表
     * @return 结果
     */
    int insertPerformAppraisalObjectSnaps(List<PerformAppraisalObjectSnapDTO> performAppraisalObjectSnapDtos);

    /**
     * 逻辑批量删除绩效考核对象快照表
     *
     * @param appraisalObjectSnapIds 需要删除的绩效考核对象快照表集合
     * @return 结果
     */
    int logicDeletePerformAppraisalObjectSnapByAppraisalObjectSnapIds(List<Long> appraisalObjectSnapIds);

    /**
     * 逻辑删除绩效考核对象快照表信息
     *
     * @param performAppraisalObjectSnapDTO
     * @return 结果
     */
    int logicDeletePerformAppraisalObjectSnapByAppraisalObjectSnapId(PerformAppraisalObjectSnapDTO performAppraisalObjectSnapDTO);

    /**
     * 批量删除绩效考核对象快照表
     *
     * @param PerformAppraisalObjectSnapDtos
     * @return 结果
     */
    int deletePerformAppraisalObjectSnapByAppraisalObjectSnapIds(List<PerformAppraisalObjectSnapDTO> PerformAppraisalObjectSnapDtos);

    /**
     * 逻辑删除绩效考核对象快照表信息
     *
     * @param performAppraisalObjectSnapDTO
     * @return 结果
     */
    int deletePerformAppraisalObjectSnapByAppraisalObjectSnapId(PerformAppraisalObjectSnapDTO performAppraisalObjectSnapDTO);


    /**
     * 删除绩效考核对象快照表信息
     *
     * @param appraisalObjectSnapId 绩效考核对象快照表主键
     * @return 结果
     */
    int deletePerformAppraisalObjectSnapByAppraisalObjectSnapId(Long appraisalObjectSnapId);

    /**
     * 导入Excel
     *
     * @param list
     */
    void importPerformAppraisalObjectSnap(List<PerformAppraisalObjectSnapExcel> list);

    /**
     * 导出Excel
     *
     * @param performAppraisalObjectSnapDTO 快照DTO
     * @return
     */
    List<PerformAppraisalObjectSnapExcel> exportPerformAppraisalObjectSnap(PerformAppraisalObjectSnapDTO performAppraisalObjectSnapDTO);

    /**
     * 通过对象ID集合查询快招标
     *
     * @param performanceObjectIds 对象ID集合
     * @return List
     */
    List<PerformAppraisalObjectSnapDTO> selectPerformAppraisalObjectSnapByAppraisalObjectsIds(List<Long> performanceObjectIds);
}
