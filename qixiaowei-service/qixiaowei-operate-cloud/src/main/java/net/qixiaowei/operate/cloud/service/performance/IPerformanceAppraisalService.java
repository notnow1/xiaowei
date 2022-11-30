package net.qixiaowei.operate.cloud.service.performance;

import net.qixiaowei.operate.cloud.api.domain.performance.PerformanceAppraisal;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalObjectsDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceRankFactorDTO;
import net.qixiaowei.operate.cloud.excel.performance.PerformanceAppraisalExcel;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * PerformanceAppraisalService接口
 *
 * @author Graves
 * @since 2022-11-23
 */
public interface IPerformanceAppraisalService {
    /**
     * 查询绩效任务考核表
     *
     * @param performanceAppraisalId 绩效考核表主键
     * @return 绩效考核表
     */
    PerformanceAppraisalDTO selectPerformanceAppraisalByPerformanceAppraisalId(Long performanceAppraisalId);

    /**
     * 查询组织绩效任务考核详情
     *
     * @param performanceAppraisalDTO 绩效考核表
     * @return
     */
    PerformanceAppraisalDTO selectOrgAppraisalArchiveById(Long performanceAppraisalDTO);

    /**
     * 查询组织绩效任务考核详情
     *
     * @param performanceAppraisalId 绩效考核表
     * @return List
     */
    PerformanceAppraisalDTO selectPerAppraisalArchiveById(Long performanceAppraisalId);

    /**
     * 查询绩效考核表列表
     *
     * @param performanceAppraisalDTO 绩效考核表
     * @return 绩效考核表集合
     */
    List<PerformanceAppraisalDTO> selectPerformanceAppraisalList(PerformanceAppraisalDTO performanceAppraisalDTO);

    /**
     * 查询组织绩效归档
     *
     * @param performanceAppraisalDTO 组织绩效
     * @return
     */
    List<PerformanceAppraisalDTO> selectPerAppraisalArchiveList(PerformanceAppraisalDTO performanceAppraisalDTO);

    /**
     * 查询组织绩效归档
     *
     * @param performanceAppraisalDTO 组织绩效
     * @return
     */
    List<PerformanceAppraisalDTO> selectOrgAppraisalArchiveList(PerformanceAppraisalDTO performanceAppraisalDTO);

    /**
     * 查询组织绩效结果排名
     *
     * @param appraisalObjectsIds 绩效考核对象ID集合
     * @return List
     */
    PerformanceAppraisalDTO selectOrgAppraisalRankByDTO(List<Long> appraisalObjectsIds, Long performanceAppraisalId);

    /**
     * @param appraisalObjectsIds 绩效考核对象ID集合
     * @return List
     */
    PerformanceAppraisalDTO selectPerAppraisalRankByDTO(Map<String, List<String>> appraisalObjectsIds);


    /**
     * 新增绩效考核表
     *
     * @param performanceAppraisalDTO 绩效考核表
     * @return 结果
     */
    int insertPerformanceAppraisal(PerformanceAppraisalDTO performanceAppraisalDTO);

    /**
     * 修改绩效考核表
     *
     * @param performanceAppraisalDTO 绩效考核表
     * @return 结果
     */
    int updatePerformanceAppraisal(PerformanceAppraisalDTO performanceAppraisalDTO);

    /**
     * 批量修改绩效考核表
     *
     * @param performanceAppraisalDtos 绩效考核表
     * @return 结果
     */
    int updatePerformanceAppraisals(List<PerformanceAppraisalDTO> performanceAppraisalDtos);

    /**
     * 批量新增绩效考核表
     *
     * @param performanceAppraisalDtos 绩效考核表
     * @return 结果
     */
    int insertPerformanceAppraisals(List<PerformanceAppraisal> performanceAppraisalDtos);

    /**
     * 逻辑批量删除绩效考核表
     *
     * @param performanceAppraisalIds 需要删除的绩效考核表集合
     * @return 结果
     */
    int logicDeletePerformanceAppraisalByPerformanceAppraisalIds(List<Long> performanceAppraisalIds);

    /**
     * 逻辑删除绩效考核表信息
     *
     * @param performanceAppraisalDTO 绩效考核
     * @return 结果
     */
    int logicDeletePerformanceAppraisalByPerformanceAppraisalId(PerformanceAppraisalDTO performanceAppraisalDTO);

    /**
     * 批量删除绩效考核表
     *
     * @param PerformanceAppraisalDtos 绩效考核
     * @return 结果
     */
    int deletePerformanceAppraisalByPerformanceAppraisalIds(List<PerformanceAppraisalDTO> PerformanceAppraisalDtos);

    /**
     * 逻辑删除绩效考核表信息
     *
     * @param performanceAppraisalDTO 绩效考核
     * @return 结果
     */
    int deletePerformanceAppraisalByPerformanceAppraisalId(PerformanceAppraisalDTO performanceAppraisalDTO);


    /**
     * 删除绩效考核表信息
     *
     * @param performanceAppraisalId 绩效考核表主键
     * @return 结果
     */
    int deletePerformanceAppraisalByPerformanceAppraisalId(Long performanceAppraisalId);

    /**
     * 导入系统的组织绩效考核Excel
     *
     * @param performanceAppraisalDTO 绩效考核
     * @param file                    文件
     */
    void importSysOrgPerformanceAppraisal(PerformanceAppraisalDTO performanceAppraisalDTO, MultipartFile file);

    /**
     * 导入系统的个人绩效考核Excel
     *
     * @param performanceAppraisalDTO 绩效考核
     * @param file                    文件
     */
    void importSysPerPerformanceAppraisal(PerformanceAppraisalDTO performanceAppraisalDTO, MultipartFile file);

    /**
     * 导入自定义的组织绩效考核Excel
     *
     * @param performanceAppraisalDTO 绩效考核
     * @param file                    文件
     */
    void importCustomOrgPerformanceAppraisal(PerformanceAppraisalDTO performanceAppraisalDTO, MultipartFile file);

    /**
     * 导入自定义的个人绩效考核Excel
     *
     * @param performanceAppraisalDTO 绩效考核
     * @param file                    文件
     */
    void importCustomPerPerformanceAppraisal(PerformanceAppraisalDTO performanceAppraisalDTO, MultipartFile file);


    /**
     * 导出Excel
     *
     * @param performanceAppraisalDTO 绩效考核
     * @return
     */
    List<PerformanceAppraisalExcel> exportPerformanceAppraisal(PerformanceAppraisalDTO performanceAppraisalDTO);

    /**
     * @param performanceAppraisalId    考核对象ID
     * @param performanceRankFactorDTOS 绩效考核等级
     * @return Collection
     */
    Collection<List<Object>> dataOrgCustomList(Long performanceAppraisalId, List<PerformanceRankFactorDTO> performanceRankFactorDTOS);

    /**
     * @param performanceAppraisalId    考核对象ID
     * @param performanceRankFactorDTOS 绩效考核等级
     * @return Collection
     */
    Collection<List<Object>> dataOrgSysList(Long performanceAppraisalId, List<PerformanceRankFactorDTO> performanceRankFactorDTOS);

    /**
     * @param performanceAppraisalId    考核对象ID
     * @param performanceRankFactorDTOS 绩效考核等级
     * @return Collection
     */
    Collection<List<Object>> dataPerCustomList(Long performanceAppraisalId, List<PerformanceRankFactorDTO> performanceRankFactorDTOS);

    /**
     * @param performanceAppraisalId    考核对象ID
     * @param performanceRankFactorDTOS 绩效考核等级
     * @return Collection
     */
    Collection<List<Object>> dataPerSysList(Long performanceAppraisalId, List<PerformanceRankFactorDTO> performanceRankFactorDTOS);

    /**
     * 根据appraisalId查询对象列表
     *
     * @param appraisalId 绩效任务ID
     * @return List
     */
    List<PerformanceAppraisalObjectsDTO> selectOrgAppraisalObjectList(Long appraisalId);

    /**
     * 根据appraisalId查询对象列表
     *
     * @param appraisalId 绩效任务ID
     * @return List
     */
    List<PerformanceAppraisalObjectsDTO> selectPerAppraisalObjectList(Long appraisalId);

    /**
     * 根据绩效考核ID获取绩效下拉列表
     *
     * @param appraisalId 绩效任务ID
     * @return List
     */
    List<PerformanceRankFactorDTO> selectPerformanceRankFactor(Long appraisalId);

    /**
     * 归档组
     *
     * @param performanceAppraisalId 绩效任务ID
     * @return int
     */
    int archive(Long performanceAppraisalId);
}
