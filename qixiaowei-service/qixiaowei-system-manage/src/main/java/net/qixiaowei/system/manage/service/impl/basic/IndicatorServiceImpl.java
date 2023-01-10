package net.qixiaowei.system.manage.service.impl.basic;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import net.qixiaowei.integration.common.constant.Constants;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.enums.basic.IndicatorCode;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusBudgetDTO;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusBudgetParametersDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalItemsDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingDTO;
import net.qixiaowei.operate.cloud.api.remote.bonus.RemoteBonusBudgetService;
import net.qixiaowei.operate.cloud.api.remote.performance.RemotePerformanceAppraisalService;
import net.qixiaowei.operate.cloud.api.remote.targetManager.RemoteDecomposeService;
import net.qixiaowei.operate.cloud.api.remote.targetManager.RemoteSettingService;
import net.qixiaowei.system.manage.api.domain.basic.Indicator;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorCategoryDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import net.qixiaowei.system.manage.mapper.basic.IndicatorCategoryMapper;
import net.qixiaowei.system.manage.mapper.basic.IndicatorMapper;
import net.qixiaowei.system.manage.service.basic.IIndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * IndicatorService业务层处理
 *
 * @author Graves
 * @since 2022-09-28
 */
@Service
public class IndicatorServiceImpl implements IIndicatorService {
    @Autowired
    private IndicatorMapper indicatorMapper;

    @Autowired
    private IndicatorCategoryMapper indicatorCategoryMapper;

    @Autowired
    private RemoteSettingService targetSettingService;

    @Autowired
    private RemoteDecomposeService targetDecomposeService;

    @Autowired
    private RemotePerformanceAppraisalService performanceAppraisalService;

    @Autowired
    private RemoteBonusBudgetService bonusBudgetService;

    /**
     * 查询指标表
     *
     * @param indicatorId 指标表主键
     * @return 指标表
     */
    @Override
    public IndicatorDTO selectIndicatorByIndicatorId(Long indicatorId) {
        IndicatorDTO indicatorDTO = indicatorMapper.selectIndicatorByIndicatorId(indicatorId);
        Long indicatorCategoryId = indicatorDTO.getIndicatorCategoryId();
        IndicatorCategoryDTO indicatorCategoryDTO = indicatorCategoryMapper.selectIndicatorCategoryByIndicatorCategoryId(indicatorCategoryId);
        indicatorDTO.setIndicatorCategoryName(indicatorCategoryDTO.getIndicatorCategoryName());
        return indicatorDTO;
    }

    /**
     * 查询指标表列表
     *
     * @param indicatorDTO 指标表
     * @return 指标表
     */
    @Override
    public List<IndicatorDTO> selectIndicatorList(IndicatorDTO indicatorDTO) {
        List<IndicatorDTO> indicatorDTOS = indicatorMapper.selectIndicatorList(indicatorDTO);
        for (IndicatorDTO dto : indicatorDTOS) {
            Integer isPreset = IndicatorCode.selectIsPreset(dto.getIndicatorCode());
            if (isPreset == 1 || isPreset == 2) {
                dto.setIsPreset(1);
            } else {
                dto.setIsPreset(0);
            }
        }
        return indicatorDTOS;
    }

    /**
     * 关键经营结果获取Indicator
     *
     * @param indicatorDTO 指标表
     * @return
     */
    @Override
    public List<IndicatorDTO> selectTargetIndicatorList(IndicatorDTO indicatorDTO) {
        return indicatorMapper.selectTargetIndicatorList(indicatorDTO);
    }

    /**
     * 查询指标表列表通过指标编码
     *
     * @param indicatorCode 指标编码
     * @return
     */
    @Override
    public IndicatorDTO selectIndicatorByCode(String indicatorCode) {
        if (StringUtils.isNotEmpty(indicatorCode)) {
            return indicatorMapper.getIndicatorByCode(indicatorCode);
        }
        return null;
    }

    /**
     * 查询指标表树状图
     *
     * @param indicatorDTO
     * @return
     */
    @Override
    public List<Tree<Long>> selectTreeList(IndicatorDTO indicatorDTO) {
        List<IndicatorDTO> indicatorDTOS = indicatorMapper.selectIndicatorList(indicatorDTO);
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setIdKey("indicatorId");
        treeNodeConfig.setNameKey("indicatorName");
        treeNodeConfig.setParentIdKey("parentIndicatorId");
        return TreeUtil.build(indicatorDTOS, Constants.TOP_PARENT_ID, treeNodeConfig, (treeNode, tree) -> {
            tree.setId(treeNode.getIndicatorId());
            tree.setParentId(treeNode.getParentIndicatorId());
            tree.setName(treeNode.getIndicatorName());
            tree.setWeight(treeNode.getSort());
            tree.putExtra("level", treeNode.getLevel());
            tree.putExtra("indicatorCode", treeNode.getIndicatorCode());
            tree.putExtra("sort", treeNode.getSort());
            tree.putExtra("indicatorType", treeNode.getIndicatorType());
            tree.putExtra("indicatorValueType", treeNode.getIndicatorValueType());
            tree.putExtra("choiceFlag", treeNode.getChoiceFlag());
            tree.putExtra("examineDirection", treeNode.getExamineDirection());
            tree.putExtra("drivingFactorFlag", treeNode.getDrivingFactorFlag());
            tree.putExtra("indicatorCategoryId", treeNode.getIndicatorCategoryId());
            tree.putExtra("indicatorCategoryName", treeNode.getIndicatorCategoryName());
            tree.putExtra("isPreset", treeNode.getIsPreset());
        });
    }

    /**
     * 新增指标表
     *
     * @param indicatorDTO 指标表
     * @return 结果
     */
    @Transactional
    @Override
    public IndicatorDTO insertIndicator(IndicatorDTO indicatorDTO) {
        String indicatorCode = indicatorDTO.getIndicatorCode();
        Integer indicatorValueType = indicatorDTO.getIndicatorValueType();
        Integer examineDirection = indicatorDTO.getExamineDirection();
        Integer choiceFlag = indicatorDTO.getChoiceFlag();
        if (indicatorValueType != 1 && indicatorValueType != 2) {
            throw new ServiceException("指标值类型输入不正确");
        }
        if (choiceFlag != 0 && choiceFlag != 1) {
            throw new ServiceException("必选标记输入不正确");
        }
        if (examineDirection != 0 && examineDirection != 1) {
            throw new ServiceException("考核方向输入不正确");
        }
        if (StringUtils.isEmpty(indicatorCode)) {
            throw new ServiceException("指标编码不能为空");
        }
        IndicatorDTO indicatorByCode = indicatorMapper.getIndicatorByCode(indicatorCode);
        if (StringUtils.isNotNull(indicatorByCode)) {
            throw new ServiceException("指标编码重复");
        }
        Long parentIndicatorId = indicatorDTO.getParentIndicatorId();
        String parentAncestors = "";//仅在非一级行业时有用
        Integer parentLevel = 1;
        if (StringUtils.isNotNull(parentIndicatorId)) {// 一级行业
            IndicatorDTO parentIndicator = indicatorMapper.selectIndicatorByIndicatorId(parentIndicatorId);
            if (parentIndicator == null) {
                throw new ServiceException("上级指标不存在");
            }
            parentAncestors = parentIndicator.getAncestors();
            parentLevel = parentIndicator.getLevel();
        }
        Indicator indicator = new Indicator();
        BeanUtils.copyProperties(indicatorDTO, indicator);
        if (StringUtils.isNotNull(parentIndicatorId)) {
            String ancestors = parentAncestors;
            if (StringUtils.isNotEmpty(ancestors)) {
                ancestors = ancestors + ",";
            }
            ancestors = ancestors + parentIndicatorId;
            indicator.setAncestors(ancestors);
            indicator.setLevel(parentLevel + 1);
        } else {
            indicator.setParentIndicatorId(0L);
            indicator.setAncestors(parentAncestors);
            indicator.setLevel(parentLevel);
        }
        Integer maxSort = indicatorMapper.selectSortByIndicatorId(parentIndicatorId);
        indicator.setSort(maxSort);
        indicator.setCreateBy(SecurityUtils.getUserId());
        indicator.setCreateTime(DateUtils.getNowDate());
        indicator.setUpdateTime(DateUtils.getNowDate());
        indicator.setUpdateBy(SecurityUtils.getUserId());
        indicator.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        indicatorMapper.insertIndicator(indicator);
        indicatorDTO.setIndicatorId(indicator.getIndicatorId());
        return indicatorDTO;
    }

    /**
     * 修改指标表
     *
     * @param indicatorDTO 指标表
     * @return 结果
     */
    @Transactional
    @Override
    public int updateIndicator(IndicatorDTO indicatorDTO) {
        String indicatorCode = indicatorDTO.getIndicatorCode();
        Long indicatorId = indicatorDTO.getIndicatorId();
        Integer indicatorValueType = indicatorDTO.getIndicatorValueType();
        if (StringUtils.isEmpty(indicatorCode)) {
            throw new ServiceException("指标编码不能为空");
        }
        if (indicatorValueType != 1 && indicatorValueType != 2) {
            throw new ServiceException("指标值类型输入不正常");
        }
        IndicatorDTO indicatorById = indicatorMapper.selectIndicatorByIndicatorId(indicatorId);
        if (StringUtils.isNull(indicatorById)) {
            throw new ServiceException("该行业不存在");
        }
//        if (IndicatorCode.contains(indicatorById.getIndicatorCode())) {
//            throw new ServiceException(indicatorById.getIndicatorName() + "指标属于系统内置指标，不允许修改");
//        }
        IndicatorDTO indicatorByCode = indicatorMapper.getIndicatorByCode(indicatorCode);
        if (StringUtils.isNotNull(indicatorByCode)) {
            if (!indicatorByCode.getIndicatorId().equals(indicatorId)) {
                throw new ServiceException("更新指标" + indicatorDTO.getIndicatorName() + "失败,指标编码重复");
            }
        }
        Indicator indicator = new Indicator();
        BeanUtils.copyProperties(indicatorDTO, indicator);
        indicator.setUpdateTime(DateUtils.getNowDate());
        indicator.setUpdateBy(SecurityUtils.getUserId());
        return indicatorMapper.updateIndicator(indicator);
    }

    /**
     * 逻辑批量删除指标表
     *
     * @param indicatorIds 需要删除的指标表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteIndicatorByIndicatorIds(List<Long> indicatorIds) {
        if (StringUtils.isEmpty(indicatorIds)) {
            throw new ServiceException("请选择指标");
        }
        List<IndicatorDTO> indicatorByIds = indicatorMapper.selectIndicatorByIds(indicatorIds);
        if (StringUtils.isEmpty(indicatorByIds)) {
            throw new ServiceException("指标不存在");
        }
//        for (IndicatorDTO indicatorById : indicatorByIds) {
//            if (IndicatorCode.contains(indicatorById.getIndicatorCode())) {
//                throw new ServiceException(indicatorById.getIndicatorName() + "指标属于系统内置指标，不允许修改");
//            }
//        }
        addSons(indicatorIds);
        isQuote(indicatorIds, indicatorByIds);
        return indicatorMapper.logicDeleteIndicatorByIndicatorIds(indicatorIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 添加子级
     *
     * @param indicatorIds 指标ID集合
     */
    private void addSons(List<Long> indicatorIds) {
        List<Long> sons = indicatorMapper.selectSons(indicatorIds);//获取子级
        if (StringUtils.isNotEmpty(sons)) {
            indicatorIds.addAll(sons);
        }
    }

    /**
     * 引用校验
     *
     * @param indicatorIds   指标ID集合
     * @param indicatorByIds 指标DTO集合
     */
    private void isQuote(List<Long> indicatorIds, List<IndicatorDTO> indicatorByIds) {
        StringBuilder quoteReminder = new StringBuilder("");
        // 目标制定
        R<List<TargetSettingDTO>> listR = targetSettingService.queryIndicatorSetting(indicatorIds, SecurityConstants.INNER);
        if (listR.getCode() != 200) {
            throw new ServiceException("远程调用失败");
        }
        List<TargetSettingDTO> targetSettingDTOS = listR.getData();
        if (StringUtils.isNotEmpty(targetSettingDTOS)) {
            StringBuilder indicatorNames = new StringBuilder("");
            for (TargetSettingDTO targetSettingDTO : targetSettingDTOS) {
                for (IndicatorDTO indicatorById : indicatorByIds) {
                    if (indicatorById.getIndicatorId().equals(targetSettingDTO.getIndicatorId())) {
                        indicatorNames.append(indicatorById.getIndicatorName()).append(",");
                        break;
                    }
                }
            }
            quoteReminder.append("指标配置【").append(indicatorNames.deleteCharAt(indicatorNames.length() - 1)).append("】已被关键经营目标制定中的【主要指标】引用 无法删除\n");
        }
        // 目标分解
        R<List<TargetDecomposeDTO>> decomposeR = targetDecomposeService.selectByIndicatorIds(indicatorIds, SecurityConstants.INNER);
        if (decomposeR.getCode() != 200) {
            throw new ServiceException("远程调用失败");
        }
        List<TargetDecomposeDTO> targetDecomposeDTOS = decomposeR.getData();
        if (StringUtils.isNotEmpty(targetDecomposeDTOS)) {
            StringBuilder indicatorNames = new StringBuilder("");
            for (IndicatorDTO indicatorById : indicatorByIds) {
                for (TargetDecomposeDTO targetDecomposeDTO : targetDecomposeDTOS) {
                    if (indicatorById.getIndicatorId().equals(targetDecomposeDTO.getIndicatorId())) {
                        indicatorNames.append(indicatorById.getIndicatorName()).append(",");
                        break;
                    }
                }
            }
            quoteReminder.append("指标配置【").append(indicatorNames.deleteCharAt(indicatorNames.length() - 1)).append("】已被自定义目标分解中的【指标名称】引用 无法删除\n");
        }
        //组织绩效制定
        Map<Integer, List<Long>> orgMap = new HashMap<>();
        orgMap.put(1, indicatorIds);
        R<List<PerformanceAppraisalItemsDTO>> orgPerformanceR = performanceAppraisalService.selectByIndicatorIds(orgMap, SecurityConstants.INNER);
        List<PerformanceAppraisalItemsDTO> orgPerformanceDTOS = orgPerformanceR.getData();
        if (orgPerformanceR.getCode() != 200) {
            throw new ServiceException("远程调用失败");
        }
        if (StringUtils.isNotEmpty(orgPerformanceDTOS)) {
            StringBuilder indicatorNames = new StringBuilder("");
            for (IndicatorDTO indicatorById : indicatorByIds) {
                for (PerformanceAppraisalItemsDTO performanceAppraisalItemsDTO : orgPerformanceDTOS) {
                    if (indicatorById.getIndicatorId().equals(performanceAppraisalItemsDTO.getIndicatorId())) {
                        indicatorNames.append(indicatorById.getIndicatorName()).append(",");
                        break;
                    }
                }
            }
            StringBuilder performanceName = new StringBuilder("");
            Set<String> performanceSet = new HashSet<>();
            for (IndicatorDTO indicatorById : indicatorByIds) {
                for (PerformanceAppraisalItemsDTO performanceAppraisalItemsDTO : orgPerformanceDTOS) {
                    if (indicatorById.getIndicatorId().equals(performanceAppraisalItemsDTO.getIndicatorId())) {
                        performanceSet.add(performanceAppraisalItemsDTO.getPerformanceRankName());
                    }
                }
            }
            for (String performance : performanceSet) {
                performanceName.append(performance).append(",");
            }
            quoteReminder.append("指标配置【")
                    .append(indicatorNames.deleteCharAt(indicatorNames.length() - 1))
                    .append("】已被组织绩效中的【任务名称】【")
                    .append(performanceName.deleteCharAt(performanceName.length() - 1))
                    .append("】引用 无法删除\n");
        }
        //个人绩效制定
        Map<Integer, List<Long>> perMap = new HashMap<>();
        perMap.put(2, indicatorIds);
        R<List<PerformanceAppraisalItemsDTO>> perPerformanceR = performanceAppraisalService.selectByIndicatorIds(perMap, SecurityConstants.INNER);
        List<PerformanceAppraisalItemsDTO> perPerformanceDTOS = perPerformanceR.getData();
        if (perPerformanceR.getCode() != 200) {
            throw new ServiceException("远程调用失败");
        }
        if (StringUtils.isNotEmpty(perPerformanceDTOS)) {
            StringBuilder indicatorNames = new StringBuilder("");
            for (IndicatorDTO indicatorById : indicatorByIds) {
                for (PerformanceAppraisalItemsDTO performanceAppraisalItemsDTO : perPerformanceDTOS) {
                    if (indicatorById.getIndicatorId().equals(performanceAppraisalItemsDTO.getIndicatorId())) {
                        indicatorNames.append(indicatorById.getIndicatorName()).append(",");
                        break;
                    }
                }
            }
            StringBuilder performanceName = new StringBuilder("");
            Set<String> performanceSet = new HashSet<>();
            for (PerformanceAppraisalItemsDTO performanceAppraisalItemsDTO : orgPerformanceDTOS) {
                for (IndicatorDTO indicatorById : indicatorByIds) {
                    if (indicatorById.getIndicatorId().equals(performanceAppraisalItemsDTO.getIndicatorId())) {
                        performanceSet.add(performanceAppraisalItemsDTO.getPerformanceRankName());
                    }
                }
            }
            for (String performance : performanceSet) {
                performanceName.append(performance).append(",");
            }
            quoteReminder.append("指标配置【")
                    .append(indicatorNames.deleteCharAt(indicatorNames.length() - 1))
                    .append("】已被个人绩效中的【任务名称】【")
                    .append(performanceName.deleteCharAt(performanceName.length() - 1))
                    .append("】引用 无法删除\n");
        }
        // 总奖金包预算
        BonusBudgetDTO bonusBudgetDTO = new BonusBudgetDTO();
        bonusBudgetDTO.setIndicatorIds(indicatorIds);
        R<BonusBudgetDTO> bonusBudgetDTOR = bonusBudgetService.selectBonusBudgetByIndicatorId(bonusBudgetDTO, SecurityConstants.INNER);
        if (bonusBudgetDTOR.getCode() != 200) {
            throw new ServiceException("远程调用总奖金包预算失败");
        }
        BonusBudgetDTO bonusBudget = bonusBudgetDTOR.getData();
        List<BonusBudgetParametersDTO> bonusBudgetParametersDTOS = bonusBudget.getBonusBudgetParametersDTOS();
        // todo 总奖金包预算-奖金驱动因素，引用校验
        if (StringUtils.isNotEmpty(bonusBudgetParametersDTOS)){

        }
        if (quoteReminder.length() != 0) {
            throw new ServiceException(quoteReminder.toString());
        }
    }

    /**
     * 物理删除指标表信息
     *
     * @param indicatorId 指标表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteIndicatorByIndicatorId(Long indicatorId) {
        return indicatorMapper.deleteIndicatorByIndicatorId(indicatorId);
    }

    /**
     * 指标详情
     *
     * @param indicatorId 指标ID
     * @return IndicatorDTO
     */
    @Override
    public IndicatorDTO detailIndicator(Long indicatorId) {
        if (StringUtils.isNull(indicatorId)) {
            throw new ServiceException("指标详情id不能为空");
        }
        IndicatorDTO dto = indicatorMapper.selectIndicatorByIndicatorId(indicatorId);
        if (StringUtils.isNull(dto)) {
            throw new ServiceException("该指标已经不存在");
        }
        if (IndicatorCode.contains(dto.getIndicatorCode())) {
            dto.setIsPreset(1);
        } else {
            dto.setIsPreset(0);
        }
        return dto;
    }

    /**
     * 获取指标最大层级
     *
     * @return List
     */
    @Override
    public List<Integer> getLevel() {
        return indicatorMapper.selectLevel();
    }

    /**
     * 通过CodeList查找指标列表
     *
     * @param indicatorCodes 指标编码
     * @return IndicatorDTOS
     */
    @Override
    public List<IndicatorDTO> selectIndicatorByCodeList(List<String> indicatorCodes) {
        return indicatorMapper.selectIndicatorByCodeList(indicatorCodes);
    }

    /**
     * 通过指标IDS查找指标
     *
     * @param indicatorIds
     * @return
     */
    @Override
    public List<IndicatorDTO> selectIndicatorByIds(List<Long> indicatorIds) {
        return indicatorMapper.selectIndicatorByIds(indicatorIds);
    }

    /**
     * 通过指标名称集合获取指标列表
     *
     * @param indicatorNames 指标名称集合
     * @return
     */
    @Override
    public List<IndicatorDTO> selectIndicatorByNames(List<String> indicatorNames) {
        return indicatorMapper.selectIndicatorByNames(indicatorNames);
    }

    /**
     * 通过ID查询指标列表
     *
     * @param indicatorId
     * @return
     */
    @Override
    public IndicatorDTO selectIndicatorById(Long indicatorId) {
        return indicatorMapper.selectIndicatorByIndicatorId(indicatorId);
    }

    @Override
    public List<IndicatorDTO> selectIsDriverList() {
        IndicatorDTO indicatorDTO = new IndicatorDTO();
        indicatorDTO.setDrivingFactorFlag(1);
        return indicatorMapper.selectIndicatorList(indicatorDTO);
    }

    /**
     * 查询绩效的指标表树状图下拉
     *
     * @param indicatorDTO
     * @return
     */
    @Override
    public List<Tree<Long>> performanceTreeList(IndicatorDTO indicatorDTO) {
        List<IndicatorDTO> indicatorDTOS = indicatorMapper.selectIndicatorList(indicatorDTO);
        for (IndicatorDTO dto : indicatorDTOS) {
            if (dto.getIndicatorValueType() == 2) {
                dto.setIndicatorName(dto.getIndicatorName() + "(%)");
            }
        }
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setIdKey("indicatorId");
        treeNodeConfig.setNameKey("indicatorName");
        treeNodeConfig.setParentIdKey("parentIndicatorId");
        return TreeUtil.build(indicatorDTOS, Constants.TOP_PARENT_ID, treeNodeConfig, (treeNode, tree) -> {
            tree.setId(treeNode.getIndicatorId());
            tree.setParentId(treeNode.getParentIndicatorId());
            tree.setName(treeNode.getIndicatorName());
            tree.putExtra("level", treeNode.getLevel());
            tree.putExtra("indicatorCode", treeNode.getIndicatorCode());
            tree.putExtra("sort", treeNode.getSort());
            tree.putExtra("indicatorType", treeNode.getIndicatorType());
            tree.putExtra("indicatorValueType", treeNode.getIndicatorValueType());
            tree.putExtra("choiceFlag", treeNode.getChoiceFlag());
            tree.putExtra("examineDirection", treeNode.getExamineDirection());
            tree.putExtra("drivingFactorFlag", treeNode.getDrivingFactorFlag());
            tree.putExtra("indicatorCategoryId", treeNode.getIndicatorCategoryId());
            tree.putExtra("indicatorCategoryName", treeNode.getIndicatorCategoryName());
            tree.putExtra("isPreset", treeNode.getIsPreset());
        });
    }

    /**
     * 逻辑删除指标表信息
     *
     * @param indicatorId 指标表
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteIndicatorByIndicatorId(Long indicatorId) {
        ArrayList<Long> indicatorIds = new ArrayList<>();
        indicatorIds.add(indicatorId);
        return logicDeleteIndicatorByIndicatorIds(indicatorIds);
    }

    /**
     * 物理删除指标表信息
     *
     * @param indicatorDTO 指标表
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteIndicatorByIndicatorId(IndicatorDTO indicatorDTO) {
        Indicator indicator = new Indicator();
        BeanUtils.copyProperties(indicatorDTO, indicator);
        return indicatorMapper.deleteIndicatorByIndicatorId(indicator.getIndicatorId());
    }

    /**
     * 物理批量删除指标表
     *
     * @param indicatorDtos 需要删除的指标表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteIndicatorByIndicatorIds(List<IndicatorDTO> indicatorDtos) {
        List<Long> stringList = new ArrayList<>();
        for (IndicatorDTO indicatorDTO : indicatorDtos) {
            stringList.add(indicatorDTO.getIndicatorId());
        }
        return indicatorMapper.deleteIndicatorByIndicatorIds(stringList);
    }

    /**
     * 批量新增指标表信息
     *
     * @param indicatorDtos 指标表对象
     */
    @Transactional
    public int insertIndicators(List<IndicatorDTO> indicatorDtos) {
        List<Indicator> indicatorList = new ArrayList<>();
        for (IndicatorDTO indicatorDTO : indicatorDtos) {
            Indicator indicator = new Indicator();
            BeanUtils.copyProperties(indicatorDTO, indicator);
            indicator.setCreateBy(SecurityUtils.getUserId());
            indicator.setCreateTime(DateUtils.getNowDate());
            indicator.setUpdateTime(DateUtils.getNowDate());
            indicator.setUpdateBy(SecurityUtils.getUserId());
            indicator.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            indicatorList.add(indicator);
        }
        return indicatorMapper.batchIndicator(indicatorList);
    }

    /**
     * 批量修改指标表信息
     *
     * @param indicatorDtos 指标表对象
     */
    @Transactional
    public int updateIndicators(List<IndicatorDTO> indicatorDtos) {
        List<Indicator> indicatorList = new ArrayList<>();
        for (IndicatorDTO indicatorDTO : indicatorDtos) {
            Indicator indicator = new Indicator();
            BeanUtils.copyProperties(indicatorDTO, indicator);
            indicator.setCreateBy(SecurityUtils.getUserId());
            indicator.setCreateTime(DateUtils.getNowDate());
            indicator.setUpdateTime(DateUtils.getNowDate());
            indicator.setUpdateBy(SecurityUtils.getUserId());
            indicatorList.add(indicator);
        }
        return indicatorMapper.updateIndicators(indicatorList);
    }
}
