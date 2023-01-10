package net.qixiaowei.operate.cloud.service.impl.performance;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.performance.PerformanceRank;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformancePercentageDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceRankDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceRankFactorDTO;
import net.qixiaowei.operate.cloud.mapper.performance.PerformanceAppraisalMapper;
import net.qixiaowei.operate.cloud.mapper.performance.PerformancePercentageMapper;
import net.qixiaowei.operate.cloud.mapper.performance.PerformanceRankMapper;
import net.qixiaowei.operate.cloud.service.performance.IPerformancePercentageService;
import net.qixiaowei.operate.cloud.service.performance.IPerformanceRankFactorService;
import net.qixiaowei.operate.cloud.service.performance.IPerformanceRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * PerformanceRankService业务层处理
 *
 * @author Graves
 * @since 2022-10-06
 */
@Service
public class PerformanceRankServiceImpl implements IPerformanceRankService {
    @Autowired
    private PerformanceRankMapper performanceRankMapper;

    @Autowired
    IPerformanceRankFactorService performanceRankFactorService;

    @Autowired
    PerformancePercentageMapper performancePercentageMapper;

    @Autowired
    IPerformancePercentageService performancePercentageService;

    @Autowired
    PerformanceAppraisalMapper performanceAppraisalMapper;

    /**
     * 查询绩效等级表
     *
     * @param performanceRankId 绩效等级表主键
     * @return 绩效等级表
     */
    @Override
    public PerformanceRankDTO selectPerformanceRankByPerformanceRankId(Long performanceRankId) {
        return performanceRankMapper.selectPerformanceRankByPerformanceRankId(performanceRankId);
    }

    /**
     * 查询绩效等级表列表
     *
     * @param performanceRankDTO 绩效等级表
     * @return 绩效等级表
     */
    @Override
    public List<PerformanceRankDTO> selectPerformanceRankList(PerformanceRankDTO performanceRankDTO) {

        PerformanceRank performanceRank = new PerformanceRank();
        BeanUtils.copyProperties(performanceRankDTO, performanceRank);
        return performanceRankMapper.selectPerformanceRankList(performanceRank);
    }

    /**
     * 新增绩效等级表
     *
     * @param performanceRankDTO 绩效等级表
     * @return 结果
     */
    @Transactional
    @Override
    public int insertPerformanceRank(PerformanceRankDTO performanceRankDTO) {
        String performanceRankName = performanceRankDTO.getPerformanceRankName();
        Integer performanceRankCategory = performanceRankDTO.getPerformanceRankCategory();
        if (StringUtils.isEmpty(performanceRankName)) {
            throw new ServiceException("绩效等级名称不能为空");
        }
        if (StringUtils.isNull(performanceRankCategory)) {
            throw new ServiceException("请选择绩效类别");
        }
        int count = performanceRankMapper.checkUniqueName(performanceRankName, performanceRankCategory);
        if (count > 0) {
            throw new ServiceException("绩效等级名称不能重复");
        }
        PerformanceRank performanceRank = new PerformanceRank();
        BeanUtils.copyProperties(performanceRankDTO, performanceRank);
        performanceRank.setCreateBy(SecurityUtils.getUserId());
        performanceRank.setCreateTime(DateUtils.getNowDate());
        performanceRank.setUpdateTime(DateUtils.getNowDate());
        performanceRank.setUpdateBy(SecurityUtils.getUserId());
        performanceRank.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        return performanceRankMapper.insertPerformanceRank(performanceRank);
    }

    /**
     * 修改绩效等级表
     *
     * @param performanceRankDTO 绩效等级表
     * @return 结果
     */
    @Transactional
    @Override
    public int updatePerformanceRank(PerformanceRankDTO performanceRankDTO) {
        Long performanceRankId = performanceRankDTO.getPerformanceRankId();
        List<PerformanceRankFactorDTO> performanceRankFactorDTOS
                = performanceRankDTO.getPerformanceRankFactorDTOS();
        if (StringUtils.isNull(performanceRankId)) {
            throw new ServiceException("绩效等级配置id不能为空");
        }
        if (StringUtils.isEmpty(performanceRankFactorDTOS)) {
            throw new ServiceException("等级系数信息不可以为空");
        }
        PerformanceRank performanceRank = new PerformanceRank();
        BeanUtils.copyProperties(performanceRankDTO, performanceRank);
        performanceRank.setUpdateTime(DateUtils.getNowDate());
        performanceRank.setUpdateBy(SecurityUtils.getUserId());
        performanceRankMapper.updatePerformanceRank(performanceRank);
        return performanceRankFactorService.operatePerformanceRankFactor(performanceRankFactorDTOS, performanceRankId);
    }

    /**
     * 逻辑批量删除绩效等级表
     *
     * @param performanceRankIds 需要删除的绩效等级表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeletePerformanceRankByPerformanceRankIds(List<Long> performanceRankIds) {
        List<PerformanceRankDTO> performanceRankDTOByIds = performanceRankMapper.isExist(performanceRankIds);
        if (performanceRankDTOByIds.size() < performanceRankIds.size()) {
            throw new ServiceException("绩效等级已不存在");
        }
        isQuote(performanceRankIds, performanceRankDTOByIds);
        List<Long> performanceRankFactorIds = performanceRankMapper.selectPerformanceRankFactorIds(performanceRankIds);
        if (StringUtils.isNotEmpty(performanceRankFactorIds)) {
            performanceRankFactorService.logicDeletePerformanceRankFactorByPerformanceRankFactorIds(performanceRankFactorIds);
        }

        return performanceRankMapper.logicDeletePerformanceRankByPerformanceRankIds(performanceRankIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * @param performanceRankIds      绩效等级ID集合
     * @param performanceRankDTOByIds 绩效等级DTO集合
     */
    private void isQuote(List<Long> performanceRankIds, List<PerformanceRankDTO> performanceRankDTOByIds) {
        StringBuilder quoteReminder = new StringBuilder("");
        List<PerformanceAppraisalDTO> performanceAppraisalDTOS = performanceAppraisalMapper.selectPerformanceAppraisalByRankIds(performanceRankIds);
        if (StringUtils.isNotEmpty(performanceAppraisalDTOS)) {
            StringBuilder performanceRankNames = new StringBuilder("");
            for (PerformanceRankDTO performanceRankDTO : performanceRankDTOByIds) {
                for (PerformanceAppraisalDTO performanceAppraisalDTO : performanceAppraisalDTOS) {
                    if (performanceAppraisalDTO.getPerformanceRankId().equals(performanceRankDTO.getPerformanceRankId())) {
                        performanceRankNames.append(performanceRankDTO.getPerformanceRankName()).append(",");
                        break;
                    }
                }
            }
            StringBuilder performanceAppraisalName = new StringBuilder("");
            Set<String> performanceAppraisalSet = new HashSet<>();
            for (PerformanceRankDTO performanceRankDTO : performanceRankDTOByIds) {
                for (PerformanceAppraisalDTO performanceAppraisalDTO : performanceAppraisalDTOS) {
                    if (performanceAppraisalDTO.getPerformanceRankId().equals(performanceRankDTO.getPerformanceRankId())) {
                        performanceAppraisalSet.add(performanceAppraisalDTO.getAppraisalName());
                    }
                }
            }
            for (String performanceAppraisal : performanceAppraisalSet) {
                performanceAppraisalName.append(performanceAppraisal).append(",");
            }
            quoteReminder.append("绩效等级配置【")
                    .append(performanceRankNames.deleteCharAt(performanceRankNames.length() - 1))
                    .append("】已被绩效考核任务中的【考核任务名称】为【")
                    .append(performanceAppraisalName.deleteCharAt(performanceAppraisalName.length() - 1))
                    .append("】引用 无法删除\n");
        }
        // 个人绩效等级
        List<PerformancePercentageDTO> perPerformancePercentageDTOS = performancePercentageMapper.selectPerformancePercentageByRankIdAndCategory(performanceRankIds, null);
        if (StringUtils.isEmpty(perPerformancePercentageDTOS)) {
            StringBuilder performanceRankNames = new StringBuilder("");
            for (PerformanceRankDTO performanceRankDTO : performanceRankDTOByIds) {
                for (PerformancePercentageDTO performancePercentageDTO : perPerformancePercentageDTOS) {
                    if (performancePercentageDTO.getPersonPerformanceRankId().equals(performanceRankDTO.getPerformanceRankId())) {
                        performanceRankNames.append(performanceRankDTO.getPerformanceRankName()).append(",");
                        break;
                    }
                }
            }
            quoteReminder.append("绩效等级配置【")
                    .append(performanceRankNames.deleteCharAt(performanceRankNames.length() - 1))
                    .append("】已被绩效考核任务中的【个人绩效等级】引用 无法删除\n");
        }
        // 组织绩效等级
        List<PerformancePercentageDTO> orgPerformancePercentageDTOS = performancePercentageMapper.selectPerformancePercentageByRankIdAndCategory(null, performanceRankIds);
        if (StringUtils.isEmpty(orgPerformancePercentageDTOS)) {
            StringBuilder performanceRankNames = new StringBuilder("");
            for (PerformanceRankDTO performanceRankDTO : performanceRankDTOByIds) {
                for (PerformancePercentageDTO performancePercentageDTO : orgPerformancePercentageDTOS) {
                    if (performancePercentageDTO.getPersonPerformanceRankId().equals(performanceRankDTO.getPerformanceRankId())) {
                        performanceRankNames.append(performanceRankDTO.getPerformanceRankName()).append(",");
                        break;
                    }
                }
            }
            quoteReminder.append("绩效等级配置【")
                    .append(performanceRankNames.deleteCharAt(performanceRankNames.length() - 1))
                    .append("】已被绩效考核任务中的【组织绩效等级】引用 无法删除\n");
        }
        if (quoteReminder.length() != 0) {
            throw new ServiceException(quoteReminder.toString());
        }
    }

    /**
     * 物理删除绩效等级表信息
     *
     * @param performanceRankId 绩效等级表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deletePerformanceRankByPerformanceRankId(Long performanceRankId) {
        return performanceRankMapper.deletePerformanceRankByPerformanceRankId(performanceRankId);
    }

    /**
     * 逻辑删除绩效等级表信息
     *
     * @param performanceRankDTO 绩效等级表
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeletePerformanceRankByPerformanceRankId(PerformanceRankDTO performanceRankDTO) {
        List<Long> performanceIds = new ArrayList<>();
        performanceIds.add(performanceRankDTO.getPerformanceRankId());
        return logicDeletePerformanceRankByPerformanceRankIds(performanceIds);
    }

    /**
     * 物理删除绩效等级表信息
     *
     * @param performanceRankDTO 绩效等级表
     * @return 结果
     */
    @Transactional
    @Override
    public int deletePerformanceRankByPerformanceRankId(PerformanceRankDTO performanceRankDTO) {
        PerformanceRank performanceRank = new PerformanceRank();
        BeanUtils.copyProperties(performanceRankDTO, performanceRank);
        return performanceRankMapper.deletePerformanceRankByPerformanceRankId(performanceRank.getPerformanceRankId());
    }

    /**
     * 物理批量删除绩效等级表
     *
     * @param performanceRankDtos 需要删除的绩效等级表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deletePerformanceRankByPerformanceRankIds(List<PerformanceRankDTO> performanceRankDtos) {
        List<Long> stringList = new ArrayList<>();
        for (PerformanceRankDTO performanceRankDTO : performanceRankDtos) {
            stringList.add(performanceRankDTO.getPerformanceRankId());
        }
        return performanceRankMapper.deletePerformanceRankByPerformanceRankIds(stringList);
    }

    /**
     * 批量新增绩效等级表信息
     *
     * @param performanceRankDtos 绩效等级表对象
     */
    @Override
    @Transactional
    public int insertPerformanceRanks(List<PerformanceRankDTO> performanceRankDtos) {
        List<PerformanceRank> performanceRankList = new ArrayList<>();
        for (PerformanceRankDTO performanceRankDTO : performanceRankDtos) {
            PerformanceRank performanceRank = new PerformanceRank();
            BeanUtils.copyProperties(performanceRankDTO, performanceRank);
            performanceRank.setCreateBy(SecurityUtils.getUserId());
            performanceRank.setCreateTime(DateUtils.getNowDate());
            performanceRank.setUpdateTime(DateUtils.getNowDate());
            performanceRank.setUpdateBy(SecurityUtils.getUserId());
            performanceRank.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            performanceRankList.add(performanceRank);
        }
        return performanceRankMapper.batchPerformanceRank(performanceRankList);
    }

    /**
     * 批量修改绩效等级表信息
     *
     * @param performanceRankDtos 绩效等级表对象
     */
    @Override
    @Transactional
    public int updatePerformanceRanks(List<PerformanceRankDTO> performanceRankDtos) {
        List<PerformanceRank> performanceRankList = new ArrayList<>();
        for (PerformanceRankDTO performanceRankDTO : performanceRankDtos) {
            PerformanceRank performanceRank = new PerformanceRank();
            BeanUtils.copyProperties(performanceRankDTO, performanceRank);
            performanceRank.setCreateBy(SecurityUtils.getUserId());
            performanceRank.setCreateTime(DateUtils.getNowDate());
            performanceRank.setUpdateTime(DateUtils.getNowDate());
            performanceRank.setUpdateBy(SecurityUtils.getUserId());
            performanceRankList.add(performanceRank);
        }
        return performanceRankMapper.updatePerformanceRanks(performanceRankList);
    }

    /***
     * 绩效等级详情
     *
     * @param performanceRankId
     * @return
     */
    @Override
    public PerformanceRankDTO detailPerformanceRank(Long performanceRankId) {
        PerformanceRankDTO performanceRankDTO = performanceRankMapper.selectPerformanceRankByPerformanceRankId(performanceRankId);
        Integer performanceRankCategory = performanceRankDTO.getPerformanceRankCategory();
        int quote = performancePercentageService.isQuote(performanceRankId, performanceRankCategory);
        int isEdit = 1;
        if (quote > 0) {
            isEdit = 0;
        }
        performanceRankDTO.setIsEdit(isEdit);
        List<PerformanceRankFactorDTO> performanceRankFactorDTOS
                = performanceRankFactorService.selectPerformanceRankFactorByPerformanceRankId(performanceRankId);
        performanceRankDTO.setPerformanceRankFactorDTOS(performanceRankFactorDTOS);
        return performanceRankDTO;
    }

    /**
     * 返回不同performance_rank_category的PerformanceRankDTO
     *
     * @return
     */
    @Override
    public Map<String, List<PerformanceRank>> detailLevelInfo() {
        Map<String, List<PerformanceRank>> PerformanceRankDTOS = new HashMap<>();
        List<PerformanceRank> organizeDto = performanceRankMapper.selectOrganizeDto();
        List<PerformanceRank> personDto = performanceRankMapper.selectPersonDto();
        PerformanceRankDTOS.put("organize", organizeDto);
        PerformanceRankDTOS.put("person", personDto);
        return PerformanceRankDTOS;
    }

    /**
     * 通过ids查找绩效等级
     *
     * @param orgPerformanceRankIds
     * @return
     */
    @Override
    public List<PerformanceRank> selectPerformanceRank(List<Long> orgPerformanceRankIds) {
        return performanceRankMapper.selectPerformanceRank(orgPerformanceRankIds);
    }

}

