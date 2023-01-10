package net.qixiaowei.operate.cloud.mapper.performance;

import net.qixiaowei.operate.cloud.api.domain.performance.PerformanceAppraisal;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalObjectsDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceRankFactorDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


/**
 * PerformanceAppraisalMapper接口
 *
 * @author Graves
 * @since 2022-12-05
 */
public interface PerformanceAppraisalMapper {
    /**
     * 查询绩效考核表
     *
     * @param performanceAppraisalId 绩效考核表主键
     * @return 绩效考核表
     */
    PerformanceAppraisalDTO selectPerformanceAppraisalByPerformanceAppraisalId(@Param("performanceAppraisalId") Long performanceAppraisalId);


    /**
     * 批量查询绩效考核表
     *
     * @param performanceAppraisalIds 绩效考核表主键集合
     * @return 绩效考核表
     */
    List<PerformanceAppraisalDTO> selectPerformanceAppraisalByPerformanceAppraisalIds(@Param("performanceAppraisalIds") List<Long> performanceAppraisalIds);

    /**
     * 查询绩效考核表列表
     *
     * @param performanceAppraisal 绩效考核表
     * @return 绩效考核表集合
     */
    List<PerformanceAppraisalDTO> selectPerformanceAppraisalList(@Param("performanceAppraisal") PerformanceAppraisal performanceAppraisal);

    /**
     * 新增绩效考核表
     *
     * @param performanceAppraisal 绩效考核表
     * @return 结果
     */
    int insertPerformanceAppraisal(@Param("performanceAppraisal") PerformanceAppraisal performanceAppraisal);

    /**
     * 修改绩效考核表
     *
     * @param performanceAppraisal 绩效考核表
     * @return 结果
     */
    int updatePerformanceAppraisal(@Param("performanceAppraisal") PerformanceAppraisal performanceAppraisal);

    /**
     * 批量修改绩效考核表
     *
     * @param performanceAppraisalList 绩效考核表
     * @return 结果
     */
    int updatePerformanceAppraisals(@Param("performanceAppraisalList") List<PerformanceAppraisal> performanceAppraisalList);

    /**
     * 逻辑删除绩效考核表
     *
     * @param performanceAppraisal
     * @return 结果
     */
    int logicDeletePerformanceAppraisalByPerformanceAppraisalId(@Param("performanceAppraisal") PerformanceAppraisal performanceAppraisal);

    /**
     * 逻辑批量删除绩效考核表
     *
     * @param performanceAppraisalIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeletePerformanceAppraisalByPerformanceAppraisalIds(@Param("performanceAppraisalIds") List<Long> performanceAppraisalIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除绩效考核表
     *
     * @param performanceAppraisalId 绩效考核表主键
     * @return 结果
     */
    int deletePerformanceAppraisalByPerformanceAppraisalId(@Param("performanceAppraisalId") Long performanceAppraisalId);

    /**
     * 物理批量删除绩效考核表
     *
     * @param performanceAppraisalIds 需要删除的数据主键集合
     * @return 结果
     */
    int deletePerformanceAppraisalByPerformanceAppraisalIds(@Param("performanceAppraisalIds") List<Long> performanceAppraisalIds);

    /**
     * 批量新增绩效考核表
     *
     * @param PerformanceAppraisals 绩效考核表列表
     * @return 结果
     */
    int batchPerformanceAppraisal(@Param("performanceAppraisals") List<PerformanceAppraisal> PerformanceAppraisals);

    /**
     * 根据绩效考核ID获取绩效下拉列表
     *
     * @param appraisalId 绩效考核表ID
     * @return List
     */
    List<PerformanceRankFactorDTO> selectRankFactorByAppraisalId(@Param("appraisalId") Long appraisalId);

    /**
     * 通过考核名称获取考核列表
     *
     * @param appraisalName 考核名称
     * @return
     */
    List<PerformanceAppraisalDTO> selectPerformanceAppraisalListByName(@Param("appraisalName") String appraisalName);

    /**
     * 查询绩效考核表列表-组织-制定
     *
     * @param performanceAppraisalObjectsDTO 绩效考核DTO
     * @return
     */
    List<PerformanceAppraisalObjectsDTO> selectOrgAppraisalObjectList(@Param("performanceAppraisalObjectsDTO") PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO);

    /**
     * 查询绩效考核表列表-组织-制定-详情
     *
     * @param performAppraisalObjectsId 绩效考核ID
     * @return 绩效考核对象
     */
    PerformanceAppraisalObjectsDTO selectOrgAppraisalObjectByObjectId(@Param("performAppraisalObjectsId") Long performAppraisalObjectsId);

    /**
     * 根据绩效等级ID集合查询绩效考核
     *
     * @param performanceRankIds 绩效等级ID集合
     * @return List
     */
    List<PerformanceAppraisalDTO> selectPerformanceAppraisalByRankIds(@Param("performanceRankIds") List<Long> performanceRankIds);
}
