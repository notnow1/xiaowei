package net.qixiaowei.operate.cloud.mapper.performance;

import net.qixiaowei.operate.cloud.api.domain.performance.PerformanceAppraisalObjects;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalObjectsDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceRankFactorDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


/**
 * PerformanceAppraisalObjectsMapper接口
 *
 * @author Graves
 * @since 2022-12-05
 */
public interface PerformanceAppraisalObjectsMapper {
    /**
     * 查询绩效考核对象表
     *
     * @param performAppraisalObjectsId 绩效考核对象表主键
     * @return 绩效考核对象表
     */
    PerformanceAppraisalObjectsDTO selectPerformanceAppraisalObjectsByPerformAppraisalObjectsId(@Param("performAppraisalObjectsId") Long performAppraisalObjectsId);


    /**
     * 批量查询绩效考核对象表
     *
     * @param performAppraisalObjectsIds 绩效考核对象表主键集合
     * @return 绩效考核对象表
     */
    List<PerformanceAppraisalObjectsDTO> selectPerformanceAppraisalObjectsByPerformAppraisalObjectsIds(@Param("performAppraisalObjectsIds") List<Long> performAppraisalObjectsIds);

    /**
     * 查询绩效考核对象表列表
     *
     * @param performanceAppraisalObjects 绩效考核对象表
     * @return 绩效考核对象表集合
     */
    List<PerformanceAppraisalObjectsDTO> selectPerformanceAppraisalObjectsList(@Param("performanceAppraisalObjects") PerformanceAppraisalObjects performanceAppraisalObjects);

    /**
     * 新增绩效考核对象表
     *
     * @param performanceAppraisalObjects 绩效考核对象表
     * @return 结果
     */
    int insertPerformanceAppraisalObjects(@Param("performanceAppraisalObjects") PerformanceAppraisalObjects performanceAppraisalObjects);

    /**
     * 修改绩效考核对象表
     *
     * @param performanceAppraisalObjects 绩效考核对象表
     * @return 结果
     */
    int updatePerformanceAppraisalObjects(@Param("performanceAppraisalObjects") PerformanceAppraisalObjects performanceAppraisalObjects);

    /**
     * 批量修改绩效考核对象表
     *
     * @param performanceAppraisalObjectsList 绩效考核对象表
     * @return 结果
     */
    int updatePerformanceAppraisalObjectss(@Param("performanceAppraisalObjectsList") List<PerformanceAppraisalObjects> performanceAppraisalObjectsList);

    /**
     * 逻辑删除绩效考核对象表
     *
     * @param performanceAppraisalObjects
     * @return 结果
     */
    int logicDeletePerformanceAppraisalObjectsByPerformAppraisalObjectsId(@Param("performanceAppraisalObjects") PerformanceAppraisalObjects performanceAppraisalObjects);

    /**
     * 逻辑批量删除绩效考核对象表
     *
     * @param performAppraisalObjectsIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeletePerformanceAppraisalObjectsByPerformAppraisalObjectsIds(@Param("performAppraisalObjectsIds") List<Long> performAppraisalObjectsIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除绩效考核对象表
     *
     * @param performAppraisalObjectsId 绩效考核对象表主键
     * @return 结果
     */
    int deletePerformanceAppraisalObjectsByPerformAppraisalObjectsId(@Param("performAppraisalObjectsId") Long performAppraisalObjectsId);

    /**
     * 物理批量删除绩效考核对象表
     *
     * @param performAppraisalObjectsIds 需要删除的数据主键集合
     * @return 结果
     */
    int deletePerformanceAppraisalObjectsByPerformAppraisalObjectsIds(@Param("performAppraisalObjectsIds") List<Long> performAppraisalObjectsIds);

    /**
     * 批量新增绩效考核对象表
     *
     * @param PerformanceAppraisalObjectss 绩效考核对象表列表
     * @return 结果
     */
    int batchPerformanceAppraisalObjects(@Param("performanceAppraisalObjectss") List<PerformanceAppraisalObjects> PerformanceAppraisalObjectss);

    /**
     * 查找根据任务ID对象列表
     *
     * @param performanceAppraisalId 绩效考核ID
     * @return List
     */
    List<PerformanceAppraisalObjectsDTO> selectPerformanceAppraisalObjectsByPerformAppraisalId(@Param("performanceAppraisalId") Long performanceAppraisalId);

    /**
     * 查询组织绩效归档结果排名
     *
     * @param appraisalObjectsIds    对象ID集合
     * @param performanceAppraisalId 绩效考核ID
     * @return List
     */
    List<PerformanceAppraisalObjectsDTO> selectPerformanceAppraisalObjectsByIds(@Param("appraisalObjectsIds") List<Long> appraisalObjectsIds, @Param("performanceAppraisalId") Long performanceAppraisalId);

    /**
     * 根据考核ID查询自定义表
     *
     * @param performanceAppraisalIds 绩效考核ID集合
     * @return List
     */
    List<PerformanceAppraisalObjectsDTO> selectPerformanceAppraisalObjectsByPerformAppraisalIds(@Param("performanceAppraisalIds") List<Long> performanceAppraisalIds);

    /**
     * 查询每个员工的最近三次绩效和奖金系数
     *
     * @param employeeId 员工id
     * @return
     */
    List<PerformanceRankFactorDTO> selectPerformanceRankFactorByEmployeeId(@Param("employeeId") Long employeeId);

    /**
     * 查询部门的最近三次绩效和奖金系数
     *
     * @param departmentId 部门id
     * @return
     */
    List<PerformanceRankFactorDTO> selectPerformanceRankFactorByDeptId(@Param("departmentId") Long departmentId);

    /**
     * 绩效等级id查询绩效等级
     *
     * @param performanceRankId 绩效等级id
     * @return
     */
    List<PerformanceRankFactorDTO> selectPerformanceRankFactorByPerformanceRankId(@Param("performanceRankId") Long performanceRankId);


    /**
     * 分页查询组织绩效制定
     *
     * @param performanceAppraisalDTO 绩效考核DTO
     * @return
     */
    List<PerformanceAppraisalObjectsDTO> selectOrgAppraisalDevelopList(@Param("performanceAppraisalDTO") PerformanceAppraisalDTO performanceAppraisalDTO);

    /**
     * 评议撤回
     *
     * @param performanceAppraisalObjects 考核对象DTO
     * @return int
     */
    int withdrawPerformanceAppraisalObjects(@Param("performanceAppraisalObjects") PerformanceAppraisalObjectsDTO performanceAppraisalObjects);

    /**
     * 员工调薪近三次绩效结果
     *
     * @param performAppraisalObjectsId 绩效对象ID
     * @return List
     */
    List<PerformanceAppraisalObjectsDTO> performanceResult(@Param("performAppraisalObjectsId") Long performAppraisalObjectsId);

    /**
     * 根据员工ID查询绩效考核是否被引用
     *
     * @param employeeId 员工ID
     * @return
     */
    List<PerformanceAppraisalObjectsDTO> queryQuoteEmployeeById(@Param("employeeId") Long employeeId);
}
