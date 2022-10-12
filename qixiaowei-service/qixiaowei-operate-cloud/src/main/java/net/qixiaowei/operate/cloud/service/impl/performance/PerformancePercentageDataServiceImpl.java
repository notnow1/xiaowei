package net.qixiaowei.operate.cloud.service.impl.performance;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.performance.PerformancePercentage;
import net.qixiaowei.operate.cloud.api.domain.performance.PerformancePercentageData;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformancePercentageDataDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceRankFactorDTO;
import net.qixiaowei.operate.cloud.mapper.performance.PerformancePercentageDataMapper;
import net.qixiaowei.operate.cloud.service.performance.IPerformancePercentageDataService;
import net.qixiaowei.operate.cloud.service.performance.IPerformanceRankFactorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * PerformancePercentageDataService业务层处理
 *
 * @author Graves
 * @since 2022-10-10
 */
@Service
public class PerformancePercentageDataServiceImpl implements IPerformancePercentageDataService {
    @Autowired
    private PerformancePercentageDataMapper performancePercentageDataMapper;

    @Autowired
    private IPerformanceRankFactorService performanceRankFactorService;

    /**
     * 查询绩效比例数据表
     *
     * @param performancePercentageDataId 绩效比例数据表主键
     * @return 绩效比例数据表
     */
    @Override
    public PerformancePercentageDataDTO selectPerformancePercentageDataByPerformancePercentageDataId(Long performancePercentageDataId) {
        return performancePercentageDataMapper.selectPerformancePercentageDataByPerformancePercentageDataId(performancePercentageDataId);
    }

    /**
     * 查询绩效比例数据表列表
     *
     * @param performancePercentageDataDTO 绩效比例数据表
     * @return 绩效比例数据表
     */
    @Override
    public List<PerformancePercentageDataDTO> selectPerformancePercentageDataList(PerformancePercentageDataDTO performancePercentageDataDTO) {
        PerformancePercentageData performancePercentageData = new PerformancePercentageData();
        BeanUtils.copyProperties(performancePercentageDataDTO, performancePercentageData);
        return performancePercentageDataMapper.selectPerformancePercentageDataList(performancePercentageData);
    }

    /**
     * 新增绩效比例数据表
     *
     * @param performancePercentageDataDTO 绩效比例数据表
     * @return 结果
     */
    @Override
    @Transactional
    public int insertPerformancePercentageData(PerformancePercentageDataDTO performancePercentageDataDTO) {
        PerformancePercentageData performancePercentageData = new PerformancePercentageData();
        BeanUtils.copyProperties(performancePercentageDataDTO, performancePercentageData);
        performancePercentageData.setCreateBy(SecurityUtils.getUserId());
        performancePercentageData.setCreateTime(DateUtils.getNowDate());
        performancePercentageData.setUpdateTime(DateUtils.getNowDate());
        performancePercentageData.setUpdateBy(SecurityUtils.getUserId());
        performancePercentageData.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        return performancePercentageDataMapper.insertPerformancePercentageData(performancePercentageData);
    }

    /**
     * 修改绩效比例数据表
     *
     * @param performancePercentageDataDTO 绩效比例数据表
     * @return 结果
     */
    @Override
    @Transactional
    public int updatePerformancePercentageData(PerformancePercentageDataDTO performancePercentageDataDTO) {
        PerformancePercentageData performancePercentageData = new PerformancePercentageData();
        BeanUtils.copyProperties(performancePercentageDataDTO, performancePercentageData);
        performancePercentageData.setUpdateTime(DateUtils.getNowDate());
        performancePercentageData.setUpdateBy(SecurityUtils.getUserId());
        return performancePercentageDataMapper.updatePerformancePercentageData(performancePercentageData);
    }

    /**
     * 逻辑批量删除绩效比例数据表
     *
     * @param performancePercentageDataDtos 需要删除的绩效比例数据表主键
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeletePerformancePercentageDataByPerformancePercentageDataIds(List<Long> performancePercentageDataDtos) {
        return performancePercentageDataMapper.logicDeletePerformancePercentageDataByPerformancePercentageDataIds(performancePercentageDataDtos, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除绩效比例数据表信息
     *
     * @param performancePercentageDataId 绩效比例数据表主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deletePerformancePercentageDataByPerformancePercentageDataId(Long performancePercentageDataId) {
        return performancePercentageDataMapper.deletePerformancePercentageDataByPerformancePercentageDataId(performancePercentageDataId);
    }

    /**
     * 通过performancePercentageId查找绩效比例信息列表
     *
     * @param performancePercentageId
     * @return
     */
    @Override
    public List<PerformancePercentageDataDTO> selectPerformancePercentageDataByPerformancePercentageId(Long performancePercentageId) {
        return performancePercentageDataMapper.selectPerformancePercentageDataByPerformancePercentageId(performancePercentageId);
    }

    /**
     * 通过performancePercentageIds查找绩效比例信息列表
     *
     * @param performancePercentageIds
     * @return
     */
    @Override
    public List<Long> selectPerformancePercentageDataByPerformancePercentageIds(List<Long> performancePercentageIds) {
        return performancePercentageDataMapper.selectPerformancePercentageDataByPerformancePercentageIds(performancePercentageIds);
    }

    /**
     * 逻辑删除绩效比例数据表信息
     *
     * @param performancePercentageDataDTO 绩效比例数据表
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeletePerformancePercentageDataByPerformancePercentageDataId(PerformancePercentageDataDTO performancePercentageDataDTO) {
        PerformancePercentageData performancePercentageData = new PerformancePercentageData();
        performancePercentageData.setPerformancePercentageDataId(performancePercentageDataDTO.getPerformancePercentageDataId());
        performancePercentageData.setUpdateTime(DateUtils.getNowDate());
        performancePercentageData.setUpdateBy(SecurityUtils.getUserId());
        return performancePercentageDataMapper.logicDeletePerformancePercentageDataByPerformancePercentageDataId(performancePercentageData);
    }

    /**
     * 物理删除绩效比例数据表信息
     *
     * @param performancePercentageDataDTO 绩效比例数据表
     * @return 结果
     */
    @Override
    @Transactional
    public int deletePerformancePercentageDataByPerformancePercentageDataId(PerformancePercentageDataDTO performancePercentageDataDTO) {
        PerformancePercentageData performancePercentageData = new PerformancePercentageData();
        BeanUtils.copyProperties(performancePercentageDataDTO, performancePercentageData);
        return performancePercentageDataMapper.deletePerformancePercentageDataByPerformancePercentageDataId(performancePercentageData.getPerformancePercentageDataId());
    }

    /**
     * 物理批量删除绩效比例数据表
     *
     * @param performancePercentageDataDtos 需要删除的绩效比例数据表主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deletePerformancePercentageDataByPerformancePercentageDataIds(List<PerformancePercentageDataDTO> performancePercentageDataDtos) {
        List<Long> stringList = new ArrayList<>();
        for (PerformancePercentageDataDTO performancePercentageDataDTO : performancePercentageDataDtos) {
            stringList.add(performancePercentageDataDTO.getPerformancePercentageDataId());
        }
        return performancePercentageDataMapper.deletePerformancePercentageDataByPerformancePercentageDataIds(stringList);
    }

    /**
     * 批量新增绩效比例数据表信息
     *
     * @param informationList 绩效比例数据表对象
     */
    @Override
    @Transactional
    public int insertPerformancePercentageDatas(List<Map<String, BigDecimal>> informationList, PerformancePercentage performancePercentage) {
        Long performancePercentageId = performancePercentage.getPerformancePercentageId();
        List<Long> orgIds = new ArrayList<>();
        for (Map<String, BigDecimal> information : informationList) {
            for (String personId : information.keySet()) {
                if (personId.equals("-1")) {
                    orgIds.add(information.get("-1").longValue());
                    information.remove("-1");
                    break;
                }
            }
        }
        List<PerformancePercentageData> performancePercentageDataList = new ArrayList<>();
        for (int i = 0; i < informationList.size(); i++) {
            Map<String, BigDecimal> information = informationList.get(i);
            for (String personId : information.keySet()) {
                BigDecimal value = information.get(personId);//数值
                PerformancePercentageData performancePercentageData = new PerformancePercentageData();
                performancePercentageData.setValue(value);
                performancePercentageData.setOrgRankFactorId(orgIds.get(i));
                try {
                    performancePercentageData.setPersonRankFactorId(Long.valueOf(personId));
                } catch (Exception e) {
                    throw new ServiceException("informationList中key存在字符串");
                }
                performancePercentageData.setPerformancePercentageId(performancePercentageId);
                performancePercentageData.setCreateBy(SecurityUtils.getUserId());
                performancePercentageData.setCreateTime(DateUtils.getNowDate());
                performancePercentageData.setUpdateTime(DateUtils.getNowDate());
                performancePercentageData.setUpdateBy(SecurityUtils.getUserId());
                performancePercentageData.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                performancePercentageDataList.add(performancePercentageData);
            }
        }
        return performancePercentageDataMapper.batchPerformancePercentageData(performancePercentageDataList);
    }

    /**
     * 批量修改绩效比例数据表信息
     *
     * @param informationList
     * @param performancePercentage
     * @return
     */
    @Override
    @Transactional
    public int updatePerformancePercentageDatas(List<Map<String, BigDecimal>> informationList, PerformancePercentage performancePercentage) {
        Long performancePercentageId = performancePercentage.getPerformancePercentageId();
        List<PerformancePercentageDataDTO> performancePercentageDataDTOS =
                performancePercentageDataMapper.selectPerformancePercentageDataByPerformancePercentageId(performancePercentageId);
        List<PerformancePercentageData> performancePercentageDataList = new ArrayList<>();
        List<Long> orgIds = new ArrayList<>();
        for (Map<String, BigDecimal> information : informationList) {
            for (String personId : information.keySet()) {
                if (personId.equals("-1")) {
                    orgIds.add(information.get("-1").longValue());
                    information.remove("-1");
                    break;
                }
            }
        }
        for (int i = 0; i < informationList.size(); i++) {
            Map<String, BigDecimal> information = informationList.get(i);
            for (String personId : information.keySet()) {
                for (PerformancePercentageDataDTO performancePercentageDataDTO : performancePercentageDataDTOS) {
                    if (performancePercentageDataDTO.getOrgRankFactorId().equals(orgIds.get(i))
                            && performancePercentageDataDTO.getPersonRankFactorId().equals(Long.valueOf(personId))) {
                        PerformancePercentageData performancePercentageData = new PerformancePercentageData();
                        BeanUtils.copyProperties(performancePercentageDataDTO, performancePercentageData);
                        try {
                            performancePercentageData.setPersonRankFactorId(Long.valueOf(personId));
                            performancePercentageData.setValue(information.get(personId));
                            performancePercentageData.setOrgRankFactorId(orgIds.get(i));
                        } catch (Exception e) {
                            break;
                        }
                        performancePercentageData.setUpdateTime(DateUtils.getNowDate());
                        performancePercentageData.setUpdateBy(SecurityUtils.getUserId());
                        performancePercentageDataList.add(performancePercentageData);
                    }
                }
            }
        }
        return performancePercentageDataMapper.updatePerformancePercentageDatas(performancePercentageDataList);
    }
}

