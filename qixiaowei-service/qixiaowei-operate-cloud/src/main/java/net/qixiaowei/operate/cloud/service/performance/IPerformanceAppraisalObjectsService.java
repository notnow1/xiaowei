package net.qixiaowei.operate.cloud.service.performance;

import java.util.List;

import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalObjectsDTO;
import net.qixiaowei.operate.cloud.excel.performance.PerformanceAppraisalObjectsExcel;


/**
 * PerformanceAppraisalObjectsService接口
 *
 * @author Graves
 * @since 2022-11-23
 */
public interface IPerformanceAppraisalObjectsService {
    /**
     * 查询绩效考核对象表
     *
     * @param performAppraisalObjectsId 绩效考核对象表主键
     * @return 绩效考核对象表
     */
    PerformanceAppraisalObjectsDTO selectPerformanceAppraisalObjectsByPerformAppraisalObjectsId(Long performAppraisalObjectsId);

    /**
     * 查询绩效考核对象表列表
     *
     * @param performanceAppraisalObjectsDTO 绩效考核对象表
     * @return 绩效考核对象表集合
     */
    List<PerformanceAppraisalObjectsDTO> selectPerformanceAppraisalObjectsList(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO);

    /**
     * 新增绩效考核对象表
     *
     * @param performanceAppraisalObjectsDTO 绩效考核对象表
     * @return 结果
     */
    PerformanceAppraisalObjectsDTO insertPerformanceAppraisalObjects(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO);

    /**
     * 修改绩效考核对象表
     *
     * @param performanceAppraisalObjectsDTO 绩效考核对象表
     * @return 结果
     */
    int updatePerformanceAppraisalObjects(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO);

    /**
     * 批量修改绩效考核对象表
     *
     * @param performanceAppraisalObjectsDtos 绩效考核对象表
     * @return 结果
     */
    int updatePerformanceAppraisalObjectss(List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDtos);

    /**
     * 批量新增绩效考核对象表
     *
     * @param performanceAppraisalObjectsDtos 绩效考核对象表
     * @return 结果
     */
    int insertPerformanceAppraisalObjectss(List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDtos);

    /**
     * 逻辑批量删除绩效考核对象表
     *
     * @param performAppraisalObjectsIds 需要删除的绩效考核对象表集合
     * @return 结果
     */
    int logicDeletePerformanceAppraisalObjectsByPerformAppraisalObjectsIds(List<Long> performAppraisalObjectsIds);

    /**
     * 逻辑删除绩效考核对象表信息
     *
     * @param performanceAppraisalObjectsDTO
     * @return 结果
     */
    int logicDeletePerformanceAppraisalObjectsByPerformAppraisalObjectsId(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO);

    /**
     * 批量删除绩效考核对象表
     *
     * @param PerformanceAppraisalObjectsDtos
     * @return 结果
     */
    int deletePerformanceAppraisalObjectsByPerformAppraisalObjectsIds(List<PerformanceAppraisalObjectsDTO> PerformanceAppraisalObjectsDtos);

    /**
     * 逻辑删除绩效考核对象表信息
     *
     * @param performanceAppraisalObjectsDTO
     * @return 结果
     */
    int deletePerformanceAppraisalObjectsByPerformAppraisalObjectsId(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO);


    /**
     * 删除绩效考核对象表信息
     *
     * @param performAppraisalObjectsId 绩效考核对象表主键
     * @return 结果
     */
    int deletePerformanceAppraisalObjectsByPerformAppraisalObjectsId(Long performAppraisalObjectsId);

    /**
     * 导入Excel
     *
     * @param list
     */
    void importPerformanceAppraisalObjects(List<PerformanceAppraisalObjectsExcel> list);

    /**
     * 导出Excel
     *
     * @param performanceAppraisalObjectsDTO
     * @return
     */
    List<PerformanceAppraisalObjectsExcel> exportPerformanceAppraisalObjects(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO);

    /**
     * 查找根据任务ID对象列表
     *
     * @param performanceAppraisalId
     * @return
     */
    List<PerformanceAppraisalObjectsDTO> selectPerformanceAppraisalObjectsByPerformAppraisalId(Long performanceAppraisalId);

    /**
     * 查询组织绩效归档结果排名
     *
     * @param appraisalObjectsIds
     * @param performanceAppraisalId
     * @return
     */
    List<PerformanceAppraisalObjectsDTO> selectPerformanceAppraisalObjectsByIds(List<Long> appraisalObjectsIds, Long performanceAppraisalId);

    /**
     * 根据考核任务ID集合查找考核对象表
     *
     * @param performanceAppraisalIds
     * @return
     */
    List<PerformanceAppraisalObjectsDTO> selectPerformanceAppraisalObjectsByPerformAppraisalIds(List<Long> performanceAppraisalIds);

    /**
     * 分页查询组织绩效制定
     *
     * @param performanceAppraisalDTO 绩效考核DTO
     * @return
     */
    List<PerformanceAppraisalObjectsDTO> selectOrgAppraisalDevelopList(PerformanceAppraisalDTO performanceAppraisalDTO);

    /**
     * 评议撤回
     *
     * @param performanceAppraisalObjects 考核对象DTO
     * @return int
     */
    int withdrawPerformanceAppraisalObjects(PerformanceAppraisalObjectsDTO performanceAppraisalObjects);

    /**
     * 员工调薪近三次绩效结果
     *
     * @param performAppraisalObjectsId 绩效对象ID
     * @return List
     */
    List<PerformanceAppraisalObjectsDTO> performanceResult(Long performAppraisalObjectsId);

    /**
     * 根据员工ID查询绩效考核是否被引用
     *
     * @param employeeId 员工ID
     * @return List
     */
    List<PerformanceAppraisalObjectsDTO> queryQuoteEmployeeById(Long employeeId);

    /**
     * 通过部门ID集合查询绩效考核对象集合
     *
     * @param departmentIds 部门ID集合
     * @return List
     */
    List<PerformanceAppraisalObjectsDTO> selectByDepartmentIds(List<Long> departmentIds);
}
