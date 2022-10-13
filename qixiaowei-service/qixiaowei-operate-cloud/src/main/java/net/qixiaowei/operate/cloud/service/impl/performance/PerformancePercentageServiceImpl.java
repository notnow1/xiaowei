package net.qixiaowei.operate.cloud.service.impl.performance;

import java.math.BigDecimal;
import java.util.*;

import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.operate.cloud.api.domain.performance.PerformanceRank;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformancePercentageDataDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceRankDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceRankFactorDTO;
import net.qixiaowei.operate.cloud.service.performance.IPerformancePercentageDataService;
import net.qixiaowei.operate.cloud.service.performance.IPerformanceRankFactorService;
import net.qixiaowei.operate.cloud.service.performance.IPerformanceRankService;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentPostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.performance.PerformancePercentage;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformancePercentageDTO;
import net.qixiaowei.operate.cloud.mapper.performance.PerformancePercentageMapper;
import net.qixiaowei.operate.cloud.service.performance.IPerformancePercentageService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


/**
 * PerformancePercentageService业务层处理
 *
 * @author Graves
 * @since 2022-10-10
 */
@Service
public class PerformancePercentageServiceImpl implements IPerformancePercentageService {
    @Autowired
    private PerformancePercentageMapper performancePercentageMapper;

    @Autowired
    private IPerformancePercentageDataService performancePercentageDataService;

    @Lazy
    @Autowired
    private IPerformanceRankService performanceRankService;

    @Autowired
    private IPerformanceRankFactorService performanceRankFactorService;

    /**
     * 查询绩效比例表
     *
     * @param performancePercentageId 绩效比例表主键
     * @return 绩效比例表
     */
    @Override
    public PerformancePercentageDTO selectPerformancePercentageByPerformancePercentageId(Long performancePercentageId) {
        if (StringUtils.isNull(performancePercentageId)) {
            throw new ServiceException("绩效比例id不能为空");
        }
        PerformancePercentageDTO performancePercentageDTO = performancePercentageMapper.selectPerformancePercentageByPerformancePercentageId(performancePercentageId);
        if (StringUtils.isNull(performancePercentageDTO)) {
            throw new ServiceException("该绩效比例已经不存在");
        }
        Long orgPerformanceRankId = performancePercentageDTO.getOrgPerformanceRankId();
        List<PerformanceRankFactorDTO> orgPerformanceRankFactorDTOS = performanceRankFactorService.selectPerformanceRankFactorByPerformanceRankId(orgPerformanceRankId);
        Long personPerformanceRankId = performancePercentageDTO.getPersonPerformanceRankId();
        List<PerformanceRankFactorDTO> personPerformanceRankFactorDTOS = performanceRankFactorService.selectPerformanceRankFactorByPerformanceRankId(personPerformanceRankId);
        // 菜单栏
        ArrayList<String> orgMenu = new ArrayList<>();
        ArrayList<String> personMenu = new ArrayList<>();
        for (PerformanceRankFactorDTO orgPerformanceRankFactorDTO : orgPerformanceRankFactorDTOS) {
            String performanceRankName = orgPerformanceRankFactorDTO.getPerformanceRankName();
            orgMenu.add(performanceRankName);
        }
        for (PerformanceRankFactorDTO personPerformanceRankFactorDTO : personPerformanceRankFactorDTOS) {
            String performanceRankName = personPerformanceRankFactorDTO.getPerformanceRankName();
            personMenu.add(performanceRankName);
        }
        performancePercentageDTO.setOrgMenu(orgMenu);
        performancePercentageDTO.setPersonMenu(personMenu);
        // 绩效等级名称
        PerformanceRankDTO orgPerformanceRankDTO = performanceRankService.selectPerformanceRankByPerformanceRankId(orgPerformanceRankId);
        PerformanceRankDTO personPerformanceRankDTO = performanceRankService.selectPerformanceRankByPerformanceRankId(personPerformanceRankId);
        performancePercentageDTO.setOrgPerformanceRankName(orgPerformanceRankDTO.getPerformanceRankName());
        performancePercentageDTO.setPersonPerformanceRankName(personPerformanceRankDTO.getPerformanceRankName());
        // 数据
        List<PerformancePercentageDataDTO> performancePercentageDataDTOS = performancePercentageDataService.selectPerformancePercentageDataByPerformancePercentageId(performancePercentageId);
        List<Map<String, BigDecimal>> informationList = new ArrayList<>();
        // todo 可优化
        for (PerformanceRankFactorDTO orgPerformanceRankFactorDTO : orgPerformanceRankFactorDTOS) {
            //绩效等级系数Id
            Long orgPerformanceRankFactorId = orgPerformanceRankFactorDTO.getPerformanceRankFactorId();
            Map<String, BigDecimal> orgMap = new TreeMap<>();
            orgMap.put("-1", BigDecimal.valueOf(orgPerformanceRankFactorId));
            //排序
            for (PerformanceRankFactorDTO personPerformanceRankFactorDTO : personPerformanceRankFactorDTOS) {
                Long personPerformanceRankFactorId = personPerformanceRankFactorDTO.getPerformanceRankFactorId();
                for (PerformancePercentageDataDTO performancePercentageDataDTO : performancePercentageDataDTOS) {
                    if (Objects.equals(orgPerformanceRankFactorId, performancePercentageDataDTO.getOrgRankFactorId())
                            && Objects.equals(personPerformanceRankFactorId, performancePercentageDataDTO.getPersonRankFactorId())) {
                        //key 绩效等级ID
                        orgMap.put(String.valueOf(personPerformanceRankFactorId), performancePercentageDataDTO.getValue());
                        break;
                    }
                }
            }
            informationList.add(orgMap);
        }
        performancePercentageDTO.setInformationList(informationList);
        return performancePercentageDTO;
    }

    /**
     * 查询绩效比例表列表
     *
     * @param performancePercentageDTO 绩效比例表
     * @return 绩效比例表
     */
    @Override
    public List<PerformancePercentageDTO> selectPerformancePercentageList(PerformancePercentageDTO performancePercentageDTO) {
        PerformancePercentage performancePercentage = new PerformancePercentage();
        BeanUtils.copyProperties(performancePercentageDTO, performancePercentage);
        List<PerformancePercentageDTO> performancePercentageDTOS = performancePercentageMapper.selectPerformancePercentageList(performancePercentage);
        ArrayList<Long> orgPerformanceRankIds = new ArrayList<>();
        ArrayList<Long> personPerformanceRankIds = new ArrayList<>();
        for (PerformancePercentageDTO percentageDTO : performancePercentageDTOS) {
            orgPerformanceRankIds.add(percentageDTO.getOrgPerformanceRankId());
            personPerformanceRankIds.add(percentageDTO.getPersonPerformanceRankId());
        }
        List<PerformanceRank> orgPerformanceRanks = performanceRankService.selectPerformanceRank(orgPerformanceRankIds);
        Map<Long, String> orgPerformanceRankMap = new HashMap<>();
        for (PerformanceRank orgPerformanceRank : orgPerformanceRanks) {
            orgPerformanceRankMap.put(orgPerformanceRank.getPerformanceRankId(), orgPerformanceRank.getPerformanceRankName());
        }
        List<PerformanceRank> personPerformanceRanks = performanceRankService.selectPerformanceRank(personPerformanceRankIds);
        Map<Long, String> personPerformanceRankMap = new HashMap<>();
        for (PerformanceRank personPerformanceRank : personPerformanceRanks) {
            personPerformanceRankMap.put(personPerformanceRank.getPerformanceRankId(), personPerformanceRank.getPerformanceRankName());
        }
        for (PerformancePercentageDTO percentageDTO : performancePercentageDTOS) {
            percentageDTO.setOrgPerformanceRankName(orgPerformanceRankMap.get(percentageDTO.getOrgPerformanceRankId()));
            percentageDTO.setPersonPerformanceRankName(personPerformanceRankMap.get(percentageDTO.getPersonPerformanceRankId()));
        }
        return performancePercentageDTOS;
    }

    /**
     * 新增绩效比例表
     *
     * @param performancePercentageDTO 绩效比例表
     * @return 结果
     */
    @Override
    @Transactional
    public int insertPerformancePercentage(PerformancePercentageDTO performancePercentageDTO) {
        String performancePercentageName = performancePercentageDTO.getPerformancePercentageName();
        List<Map<String, BigDecimal>> informationList = performancePercentageDTO.getInformationList();
        int count = performancePercentageMapper.isUnique(performancePercentageName);
        if (count > 0) {
            throw new ServiceException("该绩效比例名称重复");
        }
        // todo 校验组织和个人绩效等级是否存在且对应
        PerformancePercentage performancePercentage = new PerformancePercentage();
        BeanUtils.copyProperties(performancePercentageDTO, performancePercentage);
        performancePercentage.setCreateBy(SecurityUtils.getUserId());
        performancePercentage.setCreateTime(DateUtils.getNowDate());
        performancePercentage.setUpdateTime(DateUtils.getNowDate());
        performancePercentage.setUpdateBy(SecurityUtils.getUserId());
        performancePercentage.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        int i = performancePercentageMapper.insertPerformancePercentage(performancePercentage);
        if (StringUtils.isNotEmpty(informationList)) {
            return performancePercentageDataService.insertPerformancePercentageDatas(informationList, performancePercentage);
        }
        return i;
    }

    /**
     * 修改绩效比例表
     *
     * @param performancePercentageDTO 绩效比例表
     * @return 结果
     */
    @Override
    @Transactional
    public int updatePerformancePercentage(PerformancePercentageDTO performancePercentageDTO) {
        String performancePercentageName = performancePercentageDTO.getPerformancePercentageName();
        List<Map<String, BigDecimal>> informationList = performancePercentageDTO.getInformationList();
        int count = performancePercentageMapper.isUnique(performancePercentageName);
        if (count > 0) {
            throw new ServiceException("该绩效比例名称重复");
        }
        // todo 校验组织和个人绩效等级是否存在且对应
        PerformancePercentage performancePercentage = new PerformancePercentage();
        BeanUtils.copyProperties(performancePercentageDTO, performancePercentage);
        performancePercentage.setUpdateTime(DateUtils.getNowDate());
        performancePercentage.setUpdateBy(SecurityUtils.getUserId());
        performancePercentageMapper.updatePerformancePercentage(performancePercentage);
        return performancePercentageDataService.updatePerformancePercentageDatas(informationList, performancePercentage);
    }

    /**
     * 逻辑批量删除绩效比例表
     *
     * @param performancePercentageIds 需要删除的绩效比例表主键
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeletePerformancePercentageByPerformancePercentageIds(List<Long> performancePercentageIds) {
        List<PerformancePercentageDTO> performancePercentageDTOS = performancePercentageMapper.selectPerformancePercentageByPerformancePercentageIds(performancePercentageIds);
        if (performancePercentageDTOS.size() < performancePercentageIds.size()) {
            throw new ServiceException("当前数据已不存在");
        }
        int i = performancePercentageMapper.logicDeletePerformancePercentageByPerformancePercentageIds(performancePercentageIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
        List<Long> performancePercentageDataIds = performancePercentageDataService.selectPerformancePercentageDataByPerformancePercentageIds(performancePercentageIds);
        if (StringUtils.isNotEmpty(performancePercentageDataIds)) {
            return performancePercentageDataService.logicDeletePerformancePercentageDataByPerformancePercentageDataIds(performancePercentageDataIds);
        }
        return i;
    }

    /**
     * 物理删除绩效比例表信息
     *
     * @param performancePercentageId 绩效比例表主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deletePerformancePercentageByPerformancePercentageId(Long performancePercentageId) {
        return performancePercentageMapper.deletePerformancePercentageByPerformancePercentageId(performancePercentageId);
    }

    /**
     * 引用校验
     *
     * @param performanceRankId
     * @param performanceRankCategory
     * @return
     */
    @Override
    @Transactional
    public int isQuote(Long performanceRankId, Integer performanceRankCategory) {
        performancePercentageMapper.isQuote(performanceRankId, performanceRankCategory);
        return 0;
    }

    /**
     * 逻辑删除绩效比例表信息
     *
     * @param performancePercentageDTO 绩效比例表
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeletePerformancePercentageByPerformancePercentageId(PerformancePercentageDTO performancePercentageDTO) {
        ArrayList<Long> performancePercentageIds = new ArrayList<>();
        performancePercentageIds.add(performancePercentageDTO.getPerformancePercentageId());
        return logicDeletePerformancePercentageByPerformancePercentageIds(performancePercentageIds);
    }

    /**
     * 物理删除绩效比例表信息
     *
     * @param performancePercentageDTO 绩效比例表
     * @return 结果
     */
    @Override
    @Transactional
    public int deletePerformancePercentageByPerformancePercentageId(PerformancePercentageDTO performancePercentageDTO) {
        PerformancePercentage performancePercentage = new PerformancePercentage();
        BeanUtils.copyProperties(performancePercentageDTO, performancePercentage);
        return performancePercentageMapper.deletePerformancePercentageByPerformancePercentageId(performancePercentage.getPerformancePercentageId());
    }

    /**
     * 物理批量删除绩效比例表
     *
     * @param performancePercentageDtos 需要删除的绩效比例表主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deletePerformancePercentageByPerformancePercentageIds(List<PerformancePercentageDTO> performancePercentageDtos) {
        List<Long> stringList = new ArrayList<>();
        for (PerformancePercentageDTO performancePercentageDTO : performancePercentageDtos) {
            stringList.add(performancePercentageDTO.getPerformancePercentageId());
        }
        return performancePercentageMapper.deletePerformancePercentageByPerformancePercentageIds(stringList);
    }

    /**
     * 批量修改绩效比例表信息
     *
     * @param performancePercentageDtos 绩效比例表对象
     */
    @Transactional
    public int updatePerformancePercentages(List<PerformancePercentageDTO> performancePercentageDtos) {
        List<PerformancePercentage> performancePercentageList = new ArrayList<>();

        for (PerformancePercentageDTO performancePercentageDTO : performancePercentageDtos) {
            PerformancePercentage performancePercentage = new PerformancePercentage();
            BeanUtils.copyProperties(performancePercentageDTO, performancePercentage);
            performancePercentage.setCreateBy(SecurityUtils.getUserId());
            performancePercentage.setCreateTime(DateUtils.getNowDate());
            performancePercentage.setUpdateTime(DateUtils.getNowDate());
            performancePercentage.setUpdateBy(SecurityUtils.getUserId());
            performancePercentageList.add(performancePercentage);
        }
        return performancePercentageMapper.updatePerformancePercentages(performancePercentageList);
    }
}

