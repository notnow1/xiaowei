package net.qixiaowei.operate.cloud.service.performance;

import net.qixiaowei.operate.cloud.api.domain.performance.PerformanceAppraisal;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalObjectsDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformancePercentageDTO;
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
     * 查询绩效考核详情-归档-个人
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
     * @param queryType           选择内容
     * @return List
     */
    PerformanceAppraisalDTO selectOrgAppraisalRankByDTO(List<Long> appraisalObjectsIds, Long performanceAppraisalId, Integer queryType);

    /**
     * @param performanceAppraisalDTO 绩效考核对象ID集合
     * @return List
     */
    PerformanceAppraisalDTO selectPerAppraisalRankByDTO(Map<String, List<Object>> performanceAppraisalDTO);

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

    /**
     * 根据绩效考核ID查找比例
     *
     * @param performanceAppraisalId 绩效考核id
     * @return
     */
    List<PerformancePercentageDTO> selectPerformancePercentageByPerformanceAppraisalId(Long performanceAppraisalId);

    /**
     * 查询绩效考核表列表-组织-制定
     *
     * @param performanceAppraisalObjectsDTO 绩效考核DTO
     * @return List
     */
    List<PerformanceAppraisalObjectsDTO> selectOrgAppraisalDevelopList(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO);

    /**
     * 查询绩效考核表列表-组织-制定
     *
     * @param performanceAppraisalObjectsDTO 绩效考核DTO
     * @return List
     */
    List<PerformanceAppraisalObjectsDTO> selectPerAppraisalDevelopList(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO);

    /**
     * 查询绩效考核表列表-组织-制定-详情
     *
     * @param performAppraisalObjectsId 绩效考核对象ID
     * @return DTO
     */
    PerformanceAppraisalObjectsDTO selectOrgAppraisalDevelopById(Long performAppraisalObjectsId);

    /**
     * 查询绩效考核表列表-个人-制定-详情
     *
     * @param performAppraisalObjectsId 绩效考核对象ID
     * @return DTO
     */
    PerformanceAppraisalObjectsDTO selectPerAppraisalDevelopById(Long performAppraisalObjectsId);

    /**
     * 保存/提交绩效考核-制定-组织
     *
     * @param performanceAppraisalObjectsDTO 绩效考核DTO
     * @return DTO
     */
    PerformanceAppraisalObjectsDTO updateOrgDevelopPerformanceAppraisal(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO);

    /**
     * 保存/提交绩效考核-制定-个人
     *
     * @param performanceAppraisalObjectsDTO 绩效考核DTO
     * @return DTO
     */
    PerformanceAppraisalObjectsDTO updatePerDevelopPerformanceAppraisal(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO);

    /**
     * 查询绩效考核表列表-组织-评议
     *
     * @param performanceAppraisalObjectsDTO 绩效考核DTO
     * @return List
     */
    List<PerformanceAppraisalObjectsDTO> selectOrgAppraisalReviewList(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO);

    /**
     * 查询绩效考核表列表-个人-评议
     *
     * @param performanceAppraisalObjectsDTO 绩效考核DTO
     * @return List
     */
    List<PerformanceAppraisalObjectsDTO> selectPerAppraisalReviewList(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO);

    /**
     * 查询组织绩效考核表详情-评议-组织
     *
     * @param performAppraisalObjectsId 绩效考核对象ID
     * @return 绩效考核表
     */
    PerformanceAppraisalObjectsDTO selectOrgAppraisalReviewById(Long performAppraisalObjectsId);

    /**
     * 查询组织绩效考核表详情-评议-个人
     *
     * @param performAppraisalObjectsId 绩效考核对象ID
     * @return 绩效考核表
     */
    PerformanceAppraisalObjectsDTO selectPerAppraisalReviewById(Long performAppraisalObjectsId);

    /**
     * 编辑组织绩效考核评议表
     *
     * @param performanceAppraisalObjectsDTO 考核对象
     * @return 考核对象DTO
     */
    PerformanceAppraisalObjectsDTO updateOrgReviewPerformanceAppraisal(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO);

    /**
     * 查询绩效考核表列表-组织-排名
     *
     * @param performanceAppraisalDTO 考核对象
     * @return List
     */
    List<PerformanceAppraisalDTO> selectOrgAppraisalRankingList(PerformanceAppraisalDTO performanceAppraisalDTO);

    /**
     * 查询绩效考核详情--排名
     *
     * @param performanceAppraisalId 考核ID
     * @return 考核任务DTO
     */
    PerformanceAppraisalDTO selectOrgAppraisalRankingById(Long performanceAppraisalId);

    /**
     * 编辑组织绩效考核排名表
     *
     * @param performanceAppraisalDTO 考核对象DTO
     * @return 考核任务DTO
     */
    int updateOrgRankingPerformanceAppraisal(PerformanceAppraisalDTO performanceAppraisalDTO);

    /**
     * 评议撤回
     *
     * @param performAppraisalObjectsId 考核对象ID
     * @return
     */
    int withdraw(Long performAppraisalObjectsId);

}
