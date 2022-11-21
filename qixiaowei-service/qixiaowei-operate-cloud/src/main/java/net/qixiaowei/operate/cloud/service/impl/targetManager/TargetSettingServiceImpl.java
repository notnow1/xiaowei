package net.qixiaowei.operate.cloud.service.impl.targetManager;

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
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetSetting;
import net.qixiaowei.operate.cloud.api.dto.targetManager.*;
import net.qixiaowei.operate.cloud.api.vo.TargetSettingIncomeVO;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetSettingExcel;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetSettingIncomeExcel;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetSettingOrderExcel;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetSettingRecoveriesExcel;
import net.qixiaowei.operate.cloud.mapper.targetManager.TargetSettingMapper;
import net.qixiaowei.operate.cloud.service.targetManager.*;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;


/**
 * TargetSettingService业务层处理
 *
 * @author Graves
 * @since 2022-10-27
 */
@Service
public class TargetSettingServiceImpl implements ITargetSettingService {
    @Autowired
    private TargetSettingMapper targetSettingMapper;

    @Autowired
    private ITargetSettingOrderService targetSettingOrderService;

    @Autowired
    private ITargetSettingIncomeService targetSettingIncomeService;

    @Autowired
    private ITargetSettingRecoveryService targetSettingRecoveryService;

    @Autowired
    private ITargetSettingRecoveriesService targetSettingRecoveriesServices;

    @Autowired
    private ITargetOutcomeService targetOutcomeService;

    @Autowired
    private ITargetOutcomeDetailsService targetOutcomeDetailsService;

    @Autowired
    private RemoteIndicatorService indicatorService;


    /**
     * 查询目标制定
     *
     * @param targetSettingId 目标制定主键
     * @return 目标制定
     */
    @Override
    public TargetSettingDTO selectTargetSettingByTargetSettingId(Long targetSettingId) {
        return targetSettingMapper.selectTargetSettingByTargetSettingId(targetSettingId);
    }

    /**
     * 查询目标制定列表
     *
     * @param targetSettingDTO 目标制定
     * @return 目标制定
     */
    @Override
    public List<TargetSettingDTO> selectTargetSettingList(TargetSettingDTO targetSettingDTO) {
        Integer targetYear = targetSettingDTO.getTargetYear();
        BigDecimal zero = new BigDecimal(0);
        if (StringUtils.isNull(targetYear)) {
            throw new ServiceException("请输入目标年度");
        }
        TargetSetting targetSetting = new TargetSetting();
        BeanUtils.copyProperties(targetSettingDTO, targetSetting);
        List<TargetSettingDTO> targetSettingDTOS = targetSettingMapper.selectTargetSettingList(targetSetting);
        List<String> indicatorCodes = new ArrayList<>(IndicatorCode.getAllCodes());
        R<List<IndicatorDTO>> indicatorByCodeR = indicatorService.selectIndicatorByCodeList(indicatorCodes, SecurityConstants.INNER);
        List<IndicatorDTO> indicatorByCode = indicatorByCodeR.getData();
        if (StringUtils.isEmpty(indicatorByCode) || indicatorByCode.size() != indicatorCodes.size()) {
            throw new ServiceException("指标预置数据异常 请联系管理员");
        }
        if (StringUtils.isEmpty(targetSettingDTOS)) {
            // 无数据也要返回五个预置数据
            int i = 0;
            for (String indicatorCode : indicatorCodes) {
                TargetSettingDTO zeroDto = setTargetSettingZero(indicatorCode, zero, indicatorByCode);
                if (StringUtils.isNotNull(zeroDto)) {
                    i += 1;
                    zeroDto.setSort(i);
                    targetSettingDTOS.add(zeroDto);
                }
            }
            return targetSettingDTOS;
        }
        List<TargetSettingDTO> targetSettingList = new ArrayList<>();
        int sort = 0;
        // 提取没有预置的数据
        for (TargetSettingDTO settingDTO : targetSettingDTOS) {
            for (int i = indicatorByCode.size() - 1; i >= 0; i--) {
                IndicatorDTO indicatorDTO = indicatorByCode.get(i);
                if (indicatorDTO.getIndicatorId().equals(settingDTO.getIndicatorId())) {
                    indicatorByCode.remove(indicatorDTO);
                    break;
                }
            }
        }
        //如果那三个需要赋值 0
        for (String indicatorCode : indicatorCodes) {
            TargetSettingDTO zeroDto = setTargetSettingZero(indicatorCode, zero, indicatorByCode);
            if (StringUtils.isNotNull(zeroDto)) {
                sort += 1;
                zeroDto.setSort(sort);
                targetSettingList.add(zeroDto);
            }
        }
        setIndicatorValue(targetSettingDTOS, targetSettingList, sort);
        return targetSettingList;
    }

    /**
     * 获取指标列表
     *
     * @return indicatorList
     */
    @Override
    public List<IndicatorDTO> selectIndicatorList(TargetSettingDTO targetSettingDTO) {
        TargetSetting targetSetting = new TargetSetting();
        BeanUtils.copyProperties(targetSettingDTO, targetSetting);
        List<TargetSettingDTO> targetSettingDTOList = targetSettingMapper.selectTargetSettingList(targetSetting);
        IndicatorDTO indicatorDTO = new IndicatorDTO();
        R<List<IndicatorDTO>> listR = indicatorService.selectIndicatorList(indicatorDTO, SecurityConstants.INNER);
        List<IndicatorDTO> indicatorList = listR.getData();
        if (StringUtils.isEmpty(indicatorList)) {
            throw new ServiceException("指标不存在 请联系管理员！");
        } else {
            for (IndicatorDTO dto : indicatorList) {
                Integer isPreset = IndicatorCode.selectIsPreset(dto.getIndicatorCode());
                if (isPreset == 1) {
                    dto.setIsPreset(1);
                } else if (isPreset == 2) {
                    dto.setIsPreset(2);
                } else if (isPreset == 3) {
                    dto.setIsPreset(3);
                } else {
                    dto.setIsPreset(0);
                }
                for (TargetSettingDTO settingDTO : targetSettingDTOList) {
                    String indicatorCode = dto.getIndicatorCode();
                    if (IndicatorCode.selectIsPreset(indicatorCode) == 1) {
                        dto.setIsTarget(1);
                        break;
                    }
                    Long indicatorId = settingDTO.getIndicatorId();
                    if (dto.getIndicatorId().equals(indicatorId)) {
                        dto.setIsTarget(1);
                    } else {
                        dto.setIsTarget(0);
                    }
                }
            }
            return indicatorList;
        }
    }

    @Override
    public List<Tree<Long>> selectIndicatorTree(TargetSettingDTO targetSettingDTO) {
        TargetSetting targetSetting = new TargetSetting();
        BeanUtils.copyProperties(targetSettingDTO, targetSetting);
        List<TargetSettingDTO> targetSettingDTOList = targetSettingMapper.selectTargetSettingList(targetSetting);
        IndicatorDTO indicatorDTO = new IndicatorDTO();
        R<List<IndicatorDTO>> listR = indicatorService.selectIndicatorList(indicatorDTO, SecurityConstants.INNER);
        List<IndicatorDTO> indicatorList = listR.getData();
        if (StringUtils.isEmpty(indicatorList)) {
            throw new ServiceException("指标不存在 请联系管理员！");
        } else {
            for (IndicatorDTO dto : indicatorList) {
                Integer isPreset = IndicatorCode.selectIsPreset(dto.getIndicatorCode());
                if (isPreset == 1) {
                    dto.setIsPreset(1);
                } else if (isPreset == 2) {
                    dto.setIsPreset(2);
                } else if (isPreset == 3) {
                    dto.setIsPreset(3);
                } else {
                    dto.setIsPreset(0);
                }
                for (TargetSettingDTO settingDTO : targetSettingDTOList) {
                    String indicatorCode = dto.getIndicatorCode();
                    if (IndicatorCode.selectIsPreset(indicatorCode) == 1) {
                        dto.setIsTarget(1);
                        break;
                    }
                    if (dto.getChoiceFlag().equals(1)) {
                        dto.setIsTarget(1);
                        break;
                    }
                    Long indicatorId = settingDTO.getIndicatorId();
                    if (dto.getIndicatorId().equals(indicatorId)) {
                        dto.setIsTarget(1);
                        break;
                    }
                }
            }
            TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
            treeNodeConfig.setIdKey("indicatorId");
            treeNodeConfig.setNameKey("indicatorName");
            treeNodeConfig.setParentIdKey("parentIndicatorId");
            return TreeUtil.build(indicatorList, Constants.TOP_PARENT_ID, treeNodeConfig, (treeNode, tree) -> {
                tree.setId(treeNode.getIndicatorId());
                tree.setParentId(treeNode.getParentIndicatorId());
                tree.setName(treeNode.getIndicatorName());
                tree.putExtra("indicatorCategoryName", treeNode.getIndicatorCategoryName());
                tree.putExtra("isPreset", treeNode.getIsPreset());
                tree.putExtra("indicatorCode", treeNode.getIndicatorCode());
                tree.putExtra("choiceFlag", treeNode.getChoiceFlag());
                tree.putExtra("isTarget", treeNode.getIsTarget());
            });
        }
    }


    /**
     * 查询目标制定树结构列表-树结构
     *
     * @param targetSettingDTO 目标制定
     * @return 目标制定
     */
    @Override
    public List<Tree<Long>> selectTargetSettingTreeList(TargetSettingDTO targetSettingDTO) {
        Integer targetYear = targetSettingDTO.getTargetYear();
        BigDecimal zero = new BigDecimal(0);
        if (StringUtils.isNull(targetYear)) {
            throw new ServiceException("请输入目标年度");
        }
        TargetSetting targetSetting = new TargetSetting();
        BeanUtils.copyProperties(targetSettingDTO, targetSetting);
        List<TargetSettingDTO> targetSettingDTOS = targetSettingMapper.selectTargetSettingList(targetSetting);
        List<String> indicatorCodes = new ArrayList<>(IndicatorCode.getAllCodes());
        R<List<IndicatorDTO>> indicatorByCodeR = indicatorService.selectIndicatorByCodeList(indicatorCodes, SecurityConstants.INNER);
        List<IndicatorDTO> indicatorByCode = indicatorByCodeR.getData();
        if (StringUtils.isEmpty(indicatorByCode) || indicatorByCode.size() != indicatorCodes.size()) {
            throw new ServiceException("指标预置数据异常 请联系管理员");
        }
        if (StringUtils.isEmpty(targetSettingDTOS)) {
            // 无数据也要返回五个预置数据
            int i = 0;
            for (String indicatorCode : indicatorCodes) {
                TargetSettingDTO zeroDto = setTargetSettingZero(indicatorCode, zero, indicatorByCode);
                if (StringUtils.isNotNull(zeroDto)) {
                    i += 1;
                    zeroDto.setSort(i);
                    targetSettingDTOS.add(zeroDto);
                }
            }
            return listToTree(targetSettingDTOS);
        }
        List<TargetSettingDTO> targetSettingList = new ArrayList<>();
        int sort = 0;
        // 提取没有预置的数据
        for (TargetSettingDTO settingDTO : targetSettingDTOS) {
            for (int i = indicatorByCode.size() - 1; i >= 0; i--) {
                IndicatorDTO indicatorDTO = indicatorByCode.get(i);
                if (indicatorDTO.getIndicatorId().equals(settingDTO.getIndicatorId())) {
                    indicatorByCode.remove(indicatorDTO);
                    break;
                }
            }
        }
        //如果那三个需要赋值 0
        for (String indicatorCode : indicatorCodes) {
            TargetSettingDTO zeroDto = setTargetSettingZero(indicatorCode, zero, indicatorByCode);
            if (StringUtils.isNotNull(zeroDto)) {
                sort += 1;
                zeroDto.setSort(sort);
                targetSettingList.add(zeroDto);
            }
        }
        setIndicatorValue(targetSettingDTOS, targetSettingList, sort);
        return listToTree(targetSettingList);
    }

    /**
     * 处理数据 给数据赋上一些指标的值
     *
     * @param targetSettingDTOS
     * @param targetSettingList
     * @param sort
     */
    private void setIndicatorValue(List<TargetSettingDTO> targetSettingDTOS, List<TargetSettingDTO> targetSettingList, int sort) {
        //给targetSettingDTOS排序 ,然后放进targetSettingList
        for (TargetSettingDTO settingDTO : targetSettingDTOS) {
            sort += 1;
            settingDTO.setSort(sort);
        }
        targetSettingList.addAll(targetSettingDTOS);
        IndicatorDTO indicatorDTO = new IndicatorDTO();
        R<List<IndicatorDTO>> listR = indicatorService.selectIndicatorList(indicatorDTO, SecurityConstants.INNER);
        List<IndicatorDTO> indicatorList = listR.getData();
        if (StringUtils.isEmpty(indicatorList)) {
            throw new ServiceException("指标不存在 请联系管理员！");
        }
        //处理数据 给数据赋上一些指标的值
        for (TargetSettingDTO settingDTO : targetSettingList) {
            for (IndicatorDTO dto : indicatorList) {
                if (dto.getIndicatorId().equals(settingDTO.getIndicatorId())) {
                    settingDTO.setParentIndicatorId(dto.getParentIndicatorId());
                    settingDTO.setIndicatorName(dto.getIndicatorName());
                    settingDTO.setIndicatorCode(dto.getIndicatorCode());
                    settingDTO.setChoiceFlag(dto.getChoiceFlag());
                    if (dto.getIndicatorValueType() == 2) {
                        dto.setIndicatorName(dto.getIndicatorName() + "(%)");
                    }
                    break;
                }
            }
            String indicatorCode = settingDTO.getIndicatorCode();
            if (IndicatorCode.contains(indicatorCode)) {
                Integer isPreset = IndicatorCode.selectIsPreset(indicatorCode);
                if (isPreset.equals(1)) {
                    settingDTO.setIsPreset(1);
                } else if (isPreset.equals(2)) {
                    settingDTO.setIsPreset(2);
                } else if (isPreset.equals(3)) {
                    settingDTO.setIsPreset(3);
                }
            } else {
                settingDTO.setIsPreset(0);
            }
        }
    }

    /**
     * list转化树结构
     *
     * @param targetSettingDTOS
     * @return
     */
    private static List<Tree<Long>> listToTree(List<TargetSettingDTO> targetSettingDTOS) {
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setIdKey("indicatorId");
        treeNodeConfig.setNameKey("indicatorName");
        treeNodeConfig.setParentIdKey("parentIndicatorId");
        return TreeUtil.build(targetSettingDTOS, Constants.TOP_PARENT_ID, treeNodeConfig, (treeNode, tree) -> {
            tree.setId(treeNode.getIndicatorId());
            tree.setParentId(treeNode.getParentIndicatorId());
            tree.setName(treeNode.getIndicatorName());
            tree.setWeight(treeNode.getSort());
            tree.putExtra("targetSettingId", treeNode.getTargetSettingId());
            tree.putExtra("isPreset", treeNode.getIsPreset());
            tree.putExtra("indicatorCode", treeNode.getIndicatorCode());
            tree.putExtra("choiceFlag", treeNode.getChoiceFlag());
            tree.putExtra("targetValue", treeNode.getTargetValue());
            tree.putExtra("challengeValue", treeNode.getChallengeValue());
            tree.putExtra("guaranteedValue", treeNode.getGuaranteedValue());
        });
    }

    /**
     * 创建目标制定对象
     *
     * @param indicatorCode
     * @param zero
     * @param indicatorByCode
     * @return
     */
    private TargetSettingDTO setTargetSettingZero(String indicatorCode, BigDecimal zero, List<IndicatorDTO> indicatorByCode) {
        TargetSettingDTO dto = new TargetSettingDTO();
        dto.setTargetValue(zero);
        dto.setGuaranteedValue(zero);
        dto.setChallengeValue(zero);
        boolean t = true;//若为ture，则表明整个循环没有进入执行过方法
        for (IndicatorDTO indicatorDTO : indicatorByCode) {
            if (indicatorDTO.getIndicatorCode().equals(indicatorCode)) {
                dto.setIndicatorName(indicatorDTO.getIndicatorName());
                dto.setIndicatorId(indicatorDTO.getIndicatorId());
                dto.setParentIndicatorId(indicatorDTO.getParentIndicatorId());
                dto.setIndicatorCode(indicatorDTO.getIndicatorCode());
                dto.setChoiceFlag(indicatorDTO.getChoiceFlag());
                t = false;
            }
        }
        if (t) {
            return null;
        }
        Integer isPreset = IndicatorCode.selectIsPreset(indicatorCode);
        if (isPreset == 1) {
            dto.setIsPreset(isPreset);
        } else if (isPreset == 2) {
            dto.setIsPreset(isPreset);
        } else if (isPreset == 3) {
            dto.setIsPreset(isPreset);
        }
        return dto;
    }

    /**
     * 新增目标制定
     *
     * @param targetSettingDTO 目标制定
     * @return 结果
     */
    @Override
    public TargetSetting insertTargetSetting(TargetSettingDTO targetSettingDTO) {
        TargetSetting targetSetting = new TargetSetting();
        BeanUtils.copyProperties(targetSettingDTO, targetSetting);
        targetSetting.setCreateBy(SecurityUtils.getUserId());
        targetSetting.setCreateTime(DateUtils.getNowDate());
        targetSetting.setUpdateTime(DateUtils.getNowDate());
        targetSetting.setUpdateBy(SecurityUtils.getUserId());
        targetSetting.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        targetSettingMapper.insertTargetSetting(targetSetting);
        return targetSetting;
    }

    /**
     * 修改目标制定
     *
     * @param targetSettingDTOS 目标制定
     * @return 结果
     */
    @Override
    @Transactional
    public TargetSettingDTO saveTargetSettings(List<TargetSettingDTO> targetSettingDTOS) {
        Integer targetYear = targetSettingDTOS.get(0).getTargetYear();
        List<TargetSettingDTO> TargetSettingRespList = new LinkedList<>();
        List<Long> indicators = new ArrayList<>();
        int sort = 0;
        List<TargetSettingDTO> targetSettingDTOAfter = treeToList(targetSettingDTOS, TargetSettingRespList, targetYear, indicators, sort);
        R<List<IndicatorDTO>> indicatorR = indicatorService.selectIndicatorByIds(indicators, SecurityConstants.INNER);
        if (indicatorR.getCode() != 200) {
            throw new ServiceException(indicatorR.getMsg());
        }
        List<Long> noEdit = new ArrayList<>();
        List<Long> noDelete = new ArrayList<>();
        List<IndicatorDTO> indicator = indicatorR.getData();
        for (IndicatorDTO indicatorDTO : indicator) {
            String indicatorCode = indicatorDTO.getIndicatorCode();
            if (IndicatorCode.selectIsPreset(indicatorCode).equals(1)) {
                noEdit.add(indicatorDTO.getIndicatorId());
                noDelete.add(indicatorDTO.getIndicatorId());
            }
            if (IndicatorCode.selectIsPreset(indicatorCode).equals(2)) {
                noDelete.add(indicatorDTO.getIndicatorId());
            }
        }
        for (TargetSettingDTO targetSettingDTO : targetSettingDTOAfter) {
            if (!noEdit.contains(targetSettingDTO.getIndicatorId())) {
                targetSettingDTO.setTargetSettingType(0);
            }
        }
        if (StringUtils.isEmpty(targetSettingDTOAfter)) {
            throw new ServiceException("列表为空");
        }
        TargetSetting targetSetting = new TargetSetting();
        targetSetting.setTargetYear(targetYear);
        List<TargetSettingDTO> targetSettingDTOBefore = targetSettingMapper.selectTargetSettingList(targetSetting);
        //更新操作
        List<TargetSettingDTO> updateTargetSetting = updateOperate(targetSettingDTOAfter, noEdit, targetSettingDTOBefore);
        //删除操作
        List<TargetSettingDTO> delTargetSetting = delOperate(targetSettingDTOAfter, noDelete, targetSettingDTOBefore, updateTargetSetting);
        //新增操作
        List<TargetSettingDTO> addTargetSetting = addOperate(targetYear, targetSettingDTOAfter, targetSettingDTOBefore);
        //存值
        storageValue(targetYear, noEdit, updateTargetSetting, delTargetSetting, addTargetSetting);
        TargetSettingDTO targetSettingDTO = new TargetSettingDTO();
        targetSettingDTO.setTargetYear(targetYear);
        return targetSettingDTO;
    }


    /**
     * 更新操作
     *
     * @param targetSettingDTOAfter  参数
     * @param noEdit                 没有编辑的ID
     * @param targetSettingDTOBefore 库里的数据
     * @return updateTargetSetting
     */
    private static List<TargetSettingDTO> updateOperate(List<TargetSettingDTO> targetSettingDTOAfter, List<Long> noEdit, List<TargetSettingDTO> targetSettingDTOBefore) {
        // Before里After的交集
        List<TargetSettingDTO> updateTargetSetting =
                targetSettingDTOAfter.stream().filter(targetSettingDTO ->
                        targetSettingDTOBefore.stream().map(TargetSettingDTO::getIndicatorId)
                                .collect(Collectors.toList()).contains(targetSettingDTO.getIndicatorId())
                ).collect(Collectors.toList());
        for (TargetSettingDTO targetSettingDTO : updateTargetSetting) {
            for (TargetSettingDTO settingDTO : targetSettingDTOBefore) {
                if (settingDTO.getIndicatorId().equals(targetSettingDTO.getIndicatorId())) {
                    targetSettingDTO.setTargetSettingId(settingDTO.getTargetSettingId());
                }
            }
        }
        // 更新筛选校验
        for (int i = updateTargetSetting.size() - 1; i >= 0; i--) {
            TargetSettingDTO targetSettingDTO = updateTargetSetting.get(i);
            if (noEdit.contains(targetSettingDTO.getIndicatorId())) {
                updateTargetSetting.remove(i);
            }
            if (StringUtils.isEmpty(updateTargetSetting)) {
                break;
            }
        }
        return updateTargetSetting;
    }

    /**
     * 删除操作
     *
     * @param targetSettingDTOAfter
     * @param noDelete
     * @param targetSettingDTOBefore
     * @param updateTargetSetting
     * @return
     */
    private static List<TargetSettingDTO> delOperate(List<TargetSettingDTO> targetSettingDTOAfter, List<Long> noDelete, List<TargetSettingDTO> targetSettingDTOBefore, List<TargetSettingDTO> updateTargetSetting) {
        // After里Before的交集
        List<TargetSettingDTO> delTargetSetting =
                targetSettingDTOBefore.stream().filter(targetSettingDTO ->
                        !targetSettingDTOAfter.stream().map(TargetSettingDTO::getIndicatorId)
                                .collect(Collectors.toList()).contains(targetSettingDTO.getIndicatorId())
                ).collect(Collectors.toList());
        // 删除筛选校验
        for (int i = delTargetSetting.size() - 1; i >= 0; i--) {
            TargetSettingDTO targetSettingDTO = delTargetSetting.get(i);
            if (noDelete.contains(targetSettingDTO.getIndicatorId())) {
                updateTargetSetting.remove(i);
            }
            if (StringUtils.isEmpty(delTargetSetting)) {
                break;
            }
        }
        return delTargetSetting;
    }

    /**
     * 新增操作
     *
     * @param targetYear
     * @param targetSettingDTOAfter
     * @param targetSettingDTOBefore
     * @return
     */
    private List<TargetSettingDTO> addOperate(Integer targetYear, List<TargetSettingDTO> targetSettingDTOAfter, List<TargetSettingDTO> targetSettingDTOBefore) {
        // 差集 After中Before的补集
        List<TargetSettingDTO> addTargetSetting =
                targetSettingDTOAfter.stream().filter(targetSettingDTO ->
                        !targetSettingDTOBefore.stream().map(TargetSettingDTO::getIndicatorId)
                                .collect(Collectors.toList()).contains(targetSettingDTO.getIndicatorId())
                ).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(addTargetSetting)) {
            // 如果查不到结果表，就新增
            TargetOutcomeDTO targetOutcomeDTO = targetOutcomeService.selectTargetOutcomeByTargetYear(targetYear);
            if (StringUtils.isNull(targetOutcomeDTO)) {
                TargetOutcomeDTO dto = new TargetOutcomeDTO();
                dto.setTargetYear(targetYear);
                targetOutcomeDTO = targetOutcomeService.insertTargetOutcome(dto);
            }
            //通过年份查找ID，然后通过outComeID和指标ID集合批量新增outDetail
            List<Long> indicatorIds = new ArrayList<>();
            for (TargetSettingDTO targetSettingDTO : addTargetSetting) {
                indicatorIds.add(targetSettingDTO.getIndicatorId());
            }
            targetOutcomeDetailsService.addTargetOutcomeDetailsS(indicatorIds, targetOutcomeDTO.getTargetOutcomeId());
        }
        return addTargetSetting;
    }

    /**
     * 向库存值
     *
     * @param targetYear
     * @param noEdit
     * @param updateTargetSetting
     * @param delTargetSetting
     * @param addTargetSetting
     */
    private void storageValue(Integer targetYear, List<Long> noEdit, List<TargetSettingDTO> updateTargetSetting, List<TargetSettingDTO> delTargetSetting, List<TargetSettingDTO> addTargetSetting) {
        // 对新增进行筛选，讲新增的预置数据数据删除
        for (int i = addTargetSetting.size() - 1; i >= 0; i--) {
            TargetSettingDTO targetSettingDTO = addTargetSetting.get(i);
            if (noEdit.contains(targetSettingDTO.getIndicatorId())) {
                addTargetSetting.remove(i);
            }
            if (StringUtils.isEmpty(addTargetSetting)) {
                break;
            }
        }
        if (StringUtils.isNotEmpty(delTargetSetting)) {
            logicDeleteTargetSettingByTargetSettingIds(delTargetSetting);
            //通过年份查找ID，然后通过outComeID和指标ID集合逻辑删除outDetail
            TargetOutcomeDTO targetOutcomeDTO = targetOutcomeService.selectTargetOutcomeByTargetYear(targetYear);
            List<Long> indicatorIds = new ArrayList<>();
            for (TargetSettingDTO targetSettingDTO : delTargetSetting) {
                indicatorIds.add(targetSettingDTO.getIndicatorId());
            }
            targetOutcomeDetailsService.logicDeleteTargetOutcomeDetailsByOutcomeIdAndIndicator(indicatorIds, targetOutcomeDTO.getTargetOutcomeId());
        }
        if (StringUtils.isNotEmpty(updateTargetSetting)) {
            updateTargetSettings(updateTargetSetting);
            //通过年份来更新创建日期和创建人，然后更新detail表
        }
        if (StringUtils.isNotEmpty(addTargetSetting)) {
            insertTargetSettings(addTargetSetting);
        }
    }

    /**
     * Tree → List
     *
     * @param targetSettingDTOList
     * @param targetSettingRespList
     * @param targetYear
     * @param indicators
     * @param sort
     * @return
     */
    public List<TargetSettingDTO> treeToList(List<TargetSettingDTO> targetSettingDTOList, List<TargetSettingDTO> targetSettingRespList, Integer targetYear, List<Long> indicators, int sort) {
        for (TargetSettingDTO targetSettingDTO : targetSettingDTOList) {
            sort += 1;
            targetSettingDTO.setSort(sort);
            targetSettingDTO.setTargetYear(targetYear);
            targetSettingRespList.add(targetSettingDTO);
            indicators.add(targetSettingDTO.getIndicatorId());
            if (StringUtils.isNotEmpty(targetSettingDTO.getChildren())) {
                treeToList(targetSettingDTO.getChildren(), targetSettingRespList, targetYear, indicators, sort);
            }
        }
        return targetSettingRespList;
    }


    /**
     * 修改目标制定
     *
     * @param targetSettingDTO 目标制定
     * @return 结果
     */
    @Override
    public int saveTargetSetting(TargetSettingDTO targetSettingDTO) {
        TargetSetting targetSetting = new TargetSetting();
        BeanUtils.copyProperties(targetSettingDTO, targetSetting);
        targetSetting.setUpdateTime(DateUtils.getNowDate());
        targetSetting.setUpdateBy(SecurityUtils.getUserId());
        return targetSettingMapper.updateTargetSetting(targetSetting);
    }

    /**
     * 逻辑删除目标制定信息
     *
     * @param targetSettingDTO 目标制定
     * @return 结果
     */
    @Override
    public int logicDeleteTargetSettingByTargetSettingId(TargetSettingDTO targetSettingDTO) {
        TargetSetting targetSetting = new TargetSetting();
        targetSetting.setTargetSettingId(targetSettingDTO.getTargetSettingId());
        targetSetting.setUpdateTime(DateUtils.getNowDate());
        targetSetting.setUpdateBy(SecurityUtils.getUserId());
        return targetSettingMapper.logicDeleteTargetSettingByTargetSettingId(targetSetting);
    }

    /**
     * 逻辑批量删除目标制定
     *
     * @param targetSettingDTOS 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteTargetSettingByTargetSettingIds(List<TargetSettingDTO> targetSettingDTOS) {
        List<Long> targetSettingIds = new ArrayList<>();
        for (TargetSettingDTO targetSettingDTO : targetSettingDTOS) {
            targetSettingIds.add(targetSettingDTO.getTargetSettingId());
        }
        return targetSettingMapper.logicDeleteTargetSettingByTargetSettingIds(targetSettingIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 查询经营分析报表列表
     *
     * @param targetSettingDTO 目标制定
     * @return
     */
    @Override
    public List<TargetSettingDTO> analyseList(TargetSettingDTO targetSettingDTO) {
        TargetSetting targetSetting = new TargetSetting();
        targetSetting.setTargetYear(targetSettingDTO.getTargetYear());
        //指标code集合
        List<String> list = new ArrayList<>();
        //订单（不含税）
        list.add(IndicatorCode.ORDER.getCode());
        //销售收入
        list.add(IndicatorCode.INCOME.getCode());
        //回款金额（含税）
        list.add(IndicatorCode.RECEIVABLE.getCode());
        //销售毛利
        list.add(IndicatorCode.GROSS.getCode());
        //PROFITS
        list.add(IndicatorCode.RECEIVABLE.getCode());
        R<List<IndicatorDTO>> listR = indicatorService.selectIndicatorByCodeList(list, SecurityConstants.INNER);
        if (StringUtils.isEmpty(listR.getData())) {
            throw new ServiceException("指标不存在 请联系管理员！");
        } else {
            List<Long> collect = listR.getData().stream().map(IndicatorDTO::getIndicatorId).collect(Collectors.toList());
            targetSetting.setIndicatorIds(collect);
        }
        List<TargetSettingDTO> targetSettingDTOS = targetSettingMapper.selectAnalyseList(targetSetting);
        if (StringUtils.isNotEmpty(targetSettingDTOS)) {
            List<Long> indicatorIds = targetSettingDTOS.stream().map(TargetSettingDTO::getIndicatorId).collect(Collectors.toList());
            R<List<IndicatorDTO>> listR1 = indicatorService.selectIndicatorByIds(indicatorIds, SecurityConstants.INNER);
            List<IndicatorDTO> data = listR1.getData();
            for (TargetSettingDTO settingDTO : targetSettingDTOS) {
                if (StringUtils.isNotEmpty(data)) {
                    for (IndicatorDTO datum : data) {
                        if (settingDTO.getIndicatorId() == datum.getIndicatorId()) {
                            settingDTO.setIndicatorName(datum.getIndicatorName());
                        }
                    }
                }
            }

        }
        if (StringUtils.isNotEmpty(targetSettingDTOS)) {
            for (TargetSettingDTO settingDTO : targetSettingDTOS) {
                //年度目标值
                BigDecimal targetValue = settingDTO.getTargetValue();
                //年度实际值
                BigDecimal actualTotal = settingDTO.getActualTotal();
                //上年年度实际值
                BigDecimal lastActualTotal = new BigDecimal("0");
                //目标完成率
                BigDecimal targetPercentageComplete = new BigDecimal("0");
                //同比
                BigDecimal onBasis = settingDTO.getOnBasis();
                if (actualTotal != null && actualTotal.compareTo(new BigDecimal("0")) != 0) {
                    if (targetValue != null && targetValue.compareTo(new BigDecimal("0")) != 0) {
                        targetPercentageComplete = actualTotal.divide(targetValue).setScale(2);
                    }
                }
                settingDTO.setTargetPercentageComplete(targetPercentageComplete);
                //同比 公式=（目标年度年度实际/上年年度实际）-1
                if (lastActualTotal != null && lastActualTotal.compareTo(new BigDecimal("0")) != 0) {
                    if (actualTotal != null && actualTotal.compareTo(new BigDecimal("0")) != 0) {
                        onBasis = actualTotal.divide(lastActualTotal).subtract(new BigDecimal("1")).setScale(2);
                    }
                }
                settingDTO.setOnBasis(onBasis);
            }
        }
        return targetSettingDTOS;
    }

    /**
     * todo 通过目标结果详情获取历史年度实际值，若没有则返回空
     *
     * @param historyNumS 历史年度list
     * @param indicatorId 指标ID
     * @return
     */
    private List<TargetSettingOrderDTO> selectOutcomeDetailByTargetYear(List<Integer> historyNumS, Long indicatorId) {
        List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOList = targetOutcomeService.selectTargetOutcomeByTargetYears(historyNumS, indicatorId);
        List<TargetSettingOrderDTO> targetSettingOrderDTOS = new ArrayList<>();
        for (TargetOutcomeDetailsDTO targetOutcomeDetailsDTO : targetOutcomeDetailsDTOList) {
            Integer targetYear = targetOutcomeDetailsDTO.getTargetYear();
            BigDecimal historyActual = targetOutcomeDetailsDTO.getActualTotal();
            TargetSettingOrderDTO targetSettingOrderDTO = new TargetSettingOrderDTO();
            targetSettingOrderDTO.setHistoryActual(historyActual);
            targetSettingOrderDTO.setHistoryYear(targetYear);
            targetSettingOrderDTOS.add(targetSettingOrderDTO);
            historyNumS.remove(targetYear);
        }
        insertOrderRow(historyNumS, targetSettingOrderDTOS);
        return targetSettingOrderDTOS;
    }

    /**
     * 批量新增目标制定信息
     *
     * @param targetSettingDtos 目标制定对象
     */
    public int insertTargetSettings(List<TargetSettingDTO> targetSettingDtos) {
        List<TargetSetting> targetSettingList = new ArrayList<>();
        for (TargetSettingDTO targetSettingDTO : targetSettingDtos) {
            TargetSetting targetSetting = new TargetSetting();
            BeanUtils.copyProperties(targetSettingDTO, targetSetting);
            targetSetting.setCreateBy(SecurityUtils.getUserId());
            targetSetting.setCreateTime(DateUtils.getNowDate());
            targetSetting.setUpdateTime(DateUtils.getNowDate());
            targetSetting.setUpdateBy(SecurityUtils.getUserId());
            targetSetting.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            targetSettingList.add(targetSetting);
        }
        return targetSettingMapper.batchTargetSetting(targetSettingList);
    }

    /**
     * 批量修改目标制定信息
     *
     * @param targetSettingDtos 目标制定对象
     */
    public int updateTargetSettings(List<TargetSettingDTO> targetSettingDtos) {
        List<TargetSetting> targetSettingList = new ArrayList<>();

        for (TargetSettingDTO targetSettingDTO : targetSettingDtos) {
            TargetSetting targetSetting = new TargetSetting();
            BeanUtils.copyProperties(targetSettingDTO, targetSetting);
            targetSetting.setCreateBy(SecurityUtils.getUserId());
            targetSetting.setCreateTime(DateUtils.getNowDate());
            targetSetting.setUpdateTime(DateUtils.getNowDate());
            targetSetting.setUpdateBy(SecurityUtils.getUserId());
            targetSettingList.add(targetSetting);
        }
        return targetSettingMapper.updateTargetSettings(targetSettingList);
    }

    /**
     * 导入Excel
     *
     * @param targetSettingExcelMaps EXCEL MAP
     */
    @Override
    public List<TargetSettingDTO> importTargetSetting(Map<String, List<TargetSettingExcel>> targetSettingExcelMaps) {
        List<TargetSettingExcel> targetSettingExcelList = targetSettingExcelMaps.get("关键经营目标制定");
        List<TargetSettingDTO> targetSettingDTOS = new ArrayList<>();
        List<String> indicatorCodes = new ArrayList<>();
        List<IndicatorDTO> indicatorDTOS = getIndicatorByCodes(targetSettingExcelList, indicatorCodes);
        TargetSetting targetSetting = new TargetSetting();
        targetSetting.setTargetYear(2023);
        List<TargetSettingDTO> targetSettingDTOBefore = targetSettingMapper.selectTargetSettingList(targetSetting);
        for (TargetSettingExcel targetSettingExcel : targetSettingExcelList) {
            String indicatorCode = targetSettingExcel.getIndicatorCode();
            TargetSettingDTO targetSettingDTO = new TargetSettingDTO();
            BeanUtils.copyProperties(targetSettingExcel, targetSettingDTO);
            Long indicatorId = null;
            for (IndicatorDTO indicatorDTO : indicatorDTOS) {
                if (indicatorDTO.getIndicatorCode().equals(indicatorCode)) {
                    indicatorId = indicatorDTO.getIndicatorId();
                    targetSettingDTO.setIndicatorId(indicatorDTO.getIndicatorId());
                    targetSettingDTO.setIndicatorName(indicatorDTO.getIndicatorName());
                    break;
                }
            }
            targetSettingDTOS.add(targetSettingDTO);
        }
        for (TargetSettingDTO targetSettingDTO : targetSettingDTOS) {
            Long indicatorId = targetSettingDTO.getIndicatorId();
            for (TargetSettingDTO settingDTO : targetSettingDTOBefore) {
                if (StringUtils.isNotNull(indicatorId) && settingDTO.getIndicatorId().equals(indicatorId)) {
                    targetSettingDTO.setTargetSettingId(settingDTO.getTargetSettingId());
                }
            }
        }
        return targetSettingDTOS;
    }

    /**
     * 根据CodeList获取IndicatorList
     *
     * @param targetSettingExcelList EXCEL列表
     * @param indicatorCodes         指标编码列表
     * @return indicatorDTOS
     */
    private List<IndicatorDTO> getIndicatorByCodes(List<TargetSettingExcel> targetSettingExcelList, List<String> indicatorCodes) {
        for (TargetSettingExcel targetSettingExcel : targetSettingExcelList) {
            String indicatorCode = targetSettingExcel.getIndicatorCode();
            if (indicatorCodes.contains(indicatorCode)) {
                throw new ServiceException("指标编码重复，请检查");
            }
            if (StringUtils.isNull(indicatorCode)) {
                throw new ServiceException("指标编码不能为空");
            }
            indicatorCodes.add(indicatorCode);
        }
        R<List<IndicatorDTO>> indicatorR = indicatorService.selectIndicatorByCodeList(indicatorCodes, SecurityConstants.INNER);
        List<IndicatorDTO> indicatorDTOS = indicatorR.getData();
        if (indicatorR.getCode() != 200 || StringUtils.isEmpty(indicatorDTOS)) {
            throw new ServiceException("远程获取指标编码失败");
        }
        if (indicatorDTOS.size() != indicatorCodes.size()) {
            throw new ServiceException("请输入正确的指标编码");
        }
        return indicatorDTOS;
    }

    /**
     * 导出Excel
     *
     * @param targetSettingDTO 目标制定列表
     * @return
     */
    @Override
    public List<List<TargetSettingExcel>> exportTargetSetting(TargetSettingDTO targetSettingDTO) {
        List<Integer> historyYears = getHistoryYears(targetSettingDTO);
        List<List<TargetSettingDTO>> targetSettingDTOLists = new ArrayList<>();
        List<List<TargetSettingExcel>> targetSettingExcelLists = new ArrayList<>();
        historyYears.forEach(targetYear -> {
            targetSettingDTO.setTargetYear(targetYear);
            List<TargetSettingDTO> targetSettingDTOList = selectTargetSettingList(targetSettingDTO);
            targetSettingDTOLists.add(targetSettingDTOList);
        });
        for (int i = 0; i < targetSettingDTOLists.size(); i++) {
            List<TargetSettingExcel> targetSettingExcels = new ArrayList<>();
            List<TargetSettingDTO> targetSettingDTOList = targetSettingDTOLists.get(i);
            Integer targetYear = historyYears.get(i);
            targetSettingDTOList.forEach(settingDTO -> {
                TargetSettingExcel targetSettingExcel = new TargetSettingExcel();
                targetSettingExcel.setTargetYear(targetYear);
                targetSettingExcel.setIndicatorName(settingDTO.getIndicatorName());
                targetSettingExcel.setIndicatorCode(settingDTO.getIndicatorCode());
                targetSettingExcel.setChallengeValue(settingDTO.getChallengeValue());
                targetSettingExcel.setTargetValue(settingDTO.getTargetValue());
                targetSettingExcel.setGuaranteedValue(settingDTO.getGuaranteedValue());
                targetSettingExcels.add(targetSettingExcel);
            });
            targetSettingExcelLists.add(targetSettingExcels);
        }
        return targetSettingExcelLists;
    }

    /**
     * 根据年份区间获得年份集合
     *
     * @param targetSettingDTO
     * @return
     */
    private static List<Integer> getHistoryYears(TargetSettingDTO targetSettingDTO) {
        Integer startYear = targetSettingDTO.getStartYear();
        Integer endYear = targetSettingDTO.getEndYear();
        List<Integer> historyYears = new ArrayList<>();
        if (startYear.equals(endYear)) {
            historyYears.add(startYear);
        } else if (startYear < endYear) {
            for (int i = startYear; i <= endYear; i++) {
                historyYears.add(i);
            }
        } else {
            throw new ServiceException("开始年份不能小于结束年份");
        }
        return historyYears;
    }

    /**
     * 保存销售订单目标制定
     *
     * @param targetSettingDTO 目标制定
     * @return
     */
    @Override
    @Transactional
    public TargetSettingDTO saveOrderTargetSetting(TargetSettingDTO targetSettingDTO) {
        Integer targetYear = targetSettingDTO.getTargetYear();
        List<TargetSettingOrderDTO> targetSettingOrderAfter = targetSettingDTO.getTargetSettingOrderDTOS();
        TargetSettingDTO targetSetting = targetSettingMapper.selectTargetSettingByTargetYearAndIndicator(targetYear, 1);
        targetSettingDTO.setTargetSettingType(1);
        targetSettingDTO.setSort(0);
        if (StringUtils.isNull(targetSetting)) {//新增
            TargetSetting setting = insertTargetSetting(targetSettingDTO);
            targetSetting = new TargetSettingDTO();
            targetSetting.setTargetSettingId(setting.getTargetSettingId());
            operate(targetSettingOrderAfter, targetSetting);
            // 新增目标结果
            TargetOutcomeDTO targetOutcomeDTO = targetOutcomeService.selectTargetOutcomeByTargetYear(targetYear);
            if (StringUtils.isNull(targetOutcomeDTO)) {
                TargetOutcomeDTO outcomeDTO = new TargetOutcomeDTO();
                outcomeDTO.setTargetYear(targetYear);
                targetOutcomeService.insertTargetOutcome(outcomeDTO, 1);
            } else {
                R<IndicatorDTO> indicatorDTOR = indicatorService.selectIndicatorByCode("CW001", SecurityConstants.INNER);
                if (indicatorDTOR == null) {
                    throw new ServiceException("添加目标结果 指标ID获取失败");
                }
                IndicatorDTO indicatorDTO = indicatorDTOR.getData();
                if (indicatorDTOR.getCode() != 200 || StringUtils.isNull(indicatorDTO)) {
                    throw new ServiceException("添加目标结果 指标ID获取失败");
                }
                TargetOutcomeDetailsDTO targetOutcomeDetailsDTO = new TargetOutcomeDetailsDTO();
                targetOutcomeDetailsDTO.setIndicatorId(indicatorDTO.getIndicatorId());
                targetOutcomeDetailsDTO.setTargetOutcomeId(targetOutcomeDTO.getTargetOutcomeId());
                targetOutcomeDetailsService.insertTargetOutcomeDetails(targetOutcomeDetailsDTO);
            }
            return targetSetting;
        }
        Long targetSettingId = targetSetting.getTargetSettingId();
        targetSettingDTO.setTargetSettingId(targetSettingId);
        // 保存
        saveTargetSetting(targetSettingDTO);
        if (StringUtils.isEmpty(targetSettingOrderAfter)) {
            return targetSettingDTO;
        }
        operate(targetSettingOrderAfter, targetSetting);
        targetSettingDTO.setTargetSettingId(targetSettingId);
        return targetSettingDTO;
    }

    /**
     * 销售订单目标制定竖表编辑
     *
     * @param targetSettingOrderAfter 订单目标制定后增
     * @param targetSetting           目标制定
     */
    private void operate(List<TargetSettingOrderDTO> targetSettingOrderAfter, TargetSettingDTO targetSetting) {
        List<TargetSettingOrderDTO> targetSettingOrderBefore = targetSettingOrderService.selectTargetSettingOrderByTargetSettingId(targetSetting.getTargetSettingId());
        // system-manage  更新
        List<TargetSettingOrderDTO> updateTargetSettingOrder =
                targetSettingOrderAfter.stream().filter(targetSettingOrderDTO ->
                        targetSettingOrderBefore.stream().map(TargetSettingOrderDTO::getHistoryYear)
                                .collect(Collectors.toList()).contains(targetSettingOrderDTO.getHistoryYear())
                ).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(updateTargetSettingOrder)) {
            targetSettingOrderService.updateTargetSettingOrders(updateTargetSettingOrder, targetSetting);
        }
        // 差集 After中Before的补集
        List<TargetSettingOrderDTO> addTargetSettingOrder =
                targetSettingOrderAfter.stream().filter(targetSettingOrderDTO ->
                        !targetSettingOrderBefore.stream().map(TargetSettingOrderDTO::getHistoryYear)
                                .collect(Collectors.toList()).contains(targetSettingOrderDTO.getHistoryYear())
                ).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(addTargetSettingOrder)) {
            targetSettingOrderService.insertTargetSettingOrders(addTargetSettingOrder, targetSetting);
        }
    }

    /**
     * todo 查询销售订单目标制定
     *
     * @param targetSettingDTO 目标定制
     * @return settingDTO
     */
    @Override
    public TargetSettingDTO selectOrderTargetSettingList(TargetSettingDTO targetSettingDTO) {
        Integer targetYear = targetSettingDTO.getTargetYear();
        Integer historyNum = targetSettingDTO.getHistoryNum();
        targetSettingDTO.setTargetSettingId(null);
        IndicatorDTO indicatorDTO = getIndicator(IndicatorCode.ORDER.getCode());
        TargetSetting targetSetting = new TargetSetting();
        BeanUtils.copyProperties(targetSettingDTO, targetSetting);
        targetSetting.setTargetSettingType(1);
        targetSetting.setTargetYear(targetYear);
        List<TargetSettingDTO> targetSettingDTOS = targetSettingMapper.selectTargetSettingList(targetSetting);
        List<Integer> historyNumS = null;
        if (StringUtils.isNotNull(historyNum)) {
            historyNumS = getHistoryYearList(targetYear, historyNum);
        }
        if (StringUtils.isEmpty(targetSettingDTOS)) {
            BigDecimal zero = new BigDecimal(0);
            targetSettingDTO.setTargetValue(zero);
            targetSettingDTO.setChallengeValue(zero);
            targetSettingDTO.setGuaranteedValue(zero);
            targetSettingDTO.setPercentage(zero);
            targetSettingDTO.setTargetSettingId(null);
            List<TargetSettingOrderDTO> targetSettingOrderDTOS = new ArrayList<>();
            if (StringUtils.isNotNull(historyNum) && historyNum != 0) {
                return getTargetSettingDTO(historyNum, indicatorDTO, historyNumS, targetSettingOrderDTOS, targetSettingDTO);
            }
        }
        TargetSettingDTO settingDTO = targetSettingDTOS.get(0);
        if (StringUtils.isEmpty(historyNumS)) {
            return settingDTO;
        }
        List<TargetSettingOrderDTO> targetSettingOrderDTOS = targetSettingOrderService.selectTargetSettingOrderList(settingDTO.getTargetSettingId(), historyNumS);
        if (StringUtils.isEmpty(targetSettingDTOS)) {
            return getTargetSettingDTO(historyNum, indicatorDTO, historyNumS, targetSettingOrderDTOS, targetSettingDTO);
        }
        Long targetSettingId = settingDTO.getTargetSettingId();
        return getTargetSettingDTO(historyNum, indicatorDTO, historyNumS, targetSettingOrderDTOS, settingDTO);
    }

    /**
     * 查询销售订单目标制定-不带主表玩
     *
     * @param targetSettingDTO 目标制定DTO
     * @return targetSettingOrderDTOS
     */
    @Override
    public List<TargetSettingOrderDTO> selectOrderDropTargetSettingList(TargetSettingDTO targetSettingDTO) {
        Integer targetYear = targetSettingDTO.getTargetYear();
        Integer historyNum = targetSettingDTO.getHistoryNum();
        IndicatorDTO indicatorDTO = getIndicator(IndicatorCode.ORDER.getCode());
        List<Integer> historyNumS = getHistoryYearList(targetYear, historyNum);
        List<TargetSettingOrderDTO> targetSettingOrderDTOS = targetSettingOrderService.selectTargetSettingOrderList(targetSettingDTO.getTargetSettingId(), historyNumS);
        if (StringUtils.isEmpty(targetSettingOrderDTOS)) {
            List<TargetSettingOrderDTO> maps = selectOutcomeDetailByTargetYear(historyNumS, indicatorDTO.getIndicatorId());
            if (StringUtils.isNotEmpty(maps)) {
                maps.sort((TargetSettingOrderDTO o1, TargetSettingOrderDTO o2) -> {//排序
                    return o2.getHistoryYear() - o1.getHistoryYear();
                });
                calculateGrowthRate(maps);
                return maps;
            }
            //此处已经对空的历史年度实际值做了处理，所以还是空的话就是报错了
            throw new ServiceException("获取失败");
        }
        if (targetSettingOrderDTOS.size() == historyNum) {
            return targetSettingOrderDTOS;
        }
        for (TargetSettingOrderDTO targetSettingOrderDTO : targetSettingOrderDTOS) {
            historyNumS.remove(targetSettingOrderDTO.getHistoryYear());
        }
        return getDropTargetSettingDTO(indicatorDTO, historyNumS, targetSettingOrderDTOS);
    }

    /**
     * 处理销售订单列表
     *
     * @param historyNum             历史年份
     * @param indicatorDTO           指标dto
     * @param historyNumS            历史年份list
     * @param targetSettingOrderDTOS 销售订单目标制定列表
     * @param settingDTO             返回目标制定
     * @return 销售订单列表
     */
    private TargetSettingDTO getTargetSettingDTO(Integer historyNum, IndicatorDTO indicatorDTO, List<Integer> historyNumS, List<TargetSettingOrderDTO> targetSettingOrderDTOS, TargetSettingDTO settingDTO) {
        Integer targetYear = settingDTO.getTargetYear();
        if (StringUtils.isEmpty(targetSettingOrderDTOS)) {
            // todo 通过目标结果详情获取历史年度实际值
            List<TargetSettingOrderDTO> maps = selectOutcomeDetailByTargetYear(historyNumS, indicatorDTO.getIndicatorId());
            if (StringUtils.isNotEmpty(maps)) {
                maps.sort((TargetSettingOrderDTO o1, TargetSettingOrderDTO o2) -> {//排序
                    return o2.getHistoryYear() - o1.getHistoryYear();
                });
                calculateGrowthRate(maps);
                settingDTO.setTargetSettingOrderDTOS(maps);
                return settingDTO;
            }
            //此处已经对空的历史年度实际值做了处理，所以还是空的话就是报错了
            throw new ServiceException("获取失败");
        }
        if (targetSettingOrderDTOS.size() == historyNum) {
            settingDTO.setTargetSettingOrderDTOS(targetSettingOrderDTOS);
            calculateGrowthRate(targetSettingOrderDTOS);
            return settingDTO;
        }
        for (TargetSettingOrderDTO targetSettingOrderDTO : targetSettingOrderDTOS) {
            historyNumS.remove(targetSettingOrderDTO.getHistoryYear());
        }
        List<TargetSettingOrderDTO> settingOrderDTOS = selectOutcomeDetailByTargetYear(historyNumS, indicatorDTO.getIndicatorId());
        if (StringUtils.isNotEmpty(settingOrderDTOS)) {
            targetSettingOrderDTOS.addAll(settingOrderDTOS);
            targetSettingOrderDTOS.sort((TargetSettingOrderDTO o1, TargetSettingOrderDTO o2) -> {//排序
                return o2.getHistoryYear() - o1.getHistoryYear();
            });
            calculateGrowthRate(targetSettingOrderDTOS);
        } else {
            //此处已经对空的历史年度实际值做了处理，所以还是空的话就是报错了
            throw new ServiceException("获取失败");
        }
        settingDTO.setTargetSettingOrderDTOS(targetSettingOrderDTOS);
        return settingDTO;
    }

    /**
     * 处理销售订单列表
     *
     * @param indicatorDTO           指标dto
     * @param historyNumS            历史年份list
     * @param targetSettingOrderDTOS 销售订单目标制定列表
     */
    private List<TargetSettingOrderDTO> getDropTargetSettingDTO(IndicatorDTO indicatorDTO, List<Integer> historyNumS, List<TargetSettingOrderDTO> targetSettingOrderDTOS) {
        // todo 通过目标年度直接去找滚动越策管理,获取历史年度实际值
        List<TargetSettingOrderDTO> settingOrderDTOS = null;
        targetSettingOrderDTOS.addAll(selectOutcomeDetailByTargetYear(historyNumS, indicatorDTO.getIndicatorId()));
        // 排序，然后赋值年度长率
        targetSettingOrderDTOS.sort((TargetSettingOrderDTO o1, TargetSettingOrderDTO o2) -> {
            return o2.getHistoryYear() - o1.getHistoryYear();
        });
        calculateGrowthRate(targetSettingOrderDTOS);
        return targetSettingOrderDTOS;
    }

    /**
     * 计算年度增长率
     *
     * @param targetSettingOrderDTOS
     */
    private static void calculateGrowthRate(List<TargetSettingOrderDTO> targetSettingOrderDTOS) {
        BigDecimal beforeRate = new BigDecimal(0);
        if (StringUtils.isNotEmpty(targetSettingOrderDTOS)) {
            for (TargetSettingOrderDTO targetSettingOrderDTO : targetSettingOrderDTOS) {
                BigDecimal historyActual = targetSettingOrderDTO.getHistoryActual();
                if (StringUtils.isNotNull(historyActual)) {
                    BigDecimal subtract = historyActual.subtract(beforeRate);
                    BigDecimal divide;
                    if (beforeRate.compareTo(BigDecimal.ZERO) != 0) {
                        divide = subtract.divide(beforeRate, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
                    } else {
                        divide = BigDecimal.ZERO;
                    }
                    targetSettingOrderDTO.setGrowthRate(divide);
                    beforeRate = targetSettingOrderDTO.getHistoryActual();
                } else {
                    beforeRate = new BigDecimal(0);
                }
            }
        }
    }

    /**
     * 获取指标
     */
    private IndicatorDTO getIndicator(String code) {
        R<IndicatorDTO> indicatorDTOR = indicatorService.selectIndicatorByCode(code, SecurityConstants.INNER);
        if (indicatorDTOR.getCode() != 200) {
            throw new ServiceException("远程获取指标信息失败");
        }
        if (StringUtils.isNull(indicatorDTOR.getData())) {
            throw new ServiceException("当前销售订单指标无法获取");
        }
        //指标ID
        return indicatorDTOR.getData();
    }

    /**
     * 获取历史年份列表-倒顺
     *
     * @param targetYear 目标年度
     * @param historyNum 历史年份数
     * @return
     */
    private static List<Integer> getHistoryYearList(Integer targetYear, Integer historyNum) {
        List<Integer> historyNumS = new ArrayList<>();
        for (int i = 1; i <= historyNum; i++) {
            historyNumS.add(targetYear - i);
        }
        return historyNumS;
    }

    /**
     * 获取历史年份列表-顺序
     *
     * @param targetYear 目标年度
     * @param historyNum 历史年份数
     * @return
     */
    private static List<Integer> getHistoryYearOppositeList(Integer targetYear, Integer historyNum) {
        List<Integer> historyNumS = new ArrayList<>();
        for (int i = historyNum; i >= 1; i--) {
            historyNumS.add(targetYear - i);
        }
        return historyNumS;
    }

    /**
     * 给没有数据的年份创建新的对象
     *
     * @param historyNumS            给没有数据的历史年份
     * @param targetSettingOrderDTOS 目标订单列表
     */
    private static void insertOrderRow(List<Integer> historyNumS, List<TargetSettingOrderDTO> targetSettingOrderDTOS) {
        BigDecimal zero = new BigDecimal(0);
        for (Integer history : historyNumS) {
            TargetSettingOrderDTO targetSettingOrderDTO = new TargetSettingOrderDTO();
            targetSettingOrderDTO.setHistoryYear(history);
            targetSettingOrderDTO.setHistoryActual(zero);
            targetSettingOrderDTOS.add(targetSettingOrderDTO);
        }
    }

    /**
     * 导出销售订单目标制定
     *
     * @param targetSettingDTO
     * @return
     */
    @Override
    public List<TargetSettingOrderExcel> exportOrderTargetSetting(TargetSettingDTO targetSettingDTO) {
        List<Integer> historyYears = getHistoryYears(targetSettingDTO);
        TargetSettingDTO settingDTO = new TargetSettingDTO();
        settingDTO.setTargetSettingType(1);
        List<TargetSettingDTO> targetSettingDTOList = targetSettingMapper.selectTargetSettingByYears(settingDTO, historyYears);
        if (StringUtils.isEmpty(targetSettingDTOList)) {
            throw new ServiceException("当前目标制定不存在");
        }
        ArrayList<TargetSettingOrderExcel> targetSettingOrderExcels = new ArrayList<>();
        for (TargetSettingDTO dto : targetSettingDTOList) {
            TargetSettingOrderExcel targetSettingOrderExcel = new TargetSettingOrderExcel();
            targetSettingOrderExcel.setHistoryYear(dto.getTargetYear());
            targetSettingOrderExcel.setPercentage(dto.getPercentage());
            targetSettingOrderExcel.setTargetValue(dto.getTargetValue());
            targetSettingOrderExcel.setGuaranteedValue(dto.getGuaranteedValue());
            targetSettingOrderExcel.setChallengeValue(dto.getChallengeValue());
            targetSettingOrderExcels.add(targetSettingOrderExcel);
        }
        return targetSettingOrderExcels;
    }

    /**
     * 保存销售收入目标制定
     *
     * @param targetSettingDTO 目标制定
     * @return
     */
    @Override
    @Transactional
    public TargetSettingDTO saveIncomeTargetSetting(TargetSettingDTO targetSettingDTO) {
        Integer targetYear = targetSettingDTO.getTargetYear();
        Long indicatorId = getIndicator(IndicatorCode.INCOME.getCode()).getIndicatorId();
        List<TargetSettingIncomeVO> targetSettingIncomeVOS = targetSettingDTO.getTargetSettingIncomeVOS();
        TargetSettingDTO targetSetting = targetSettingMapper.selectTargetSettingByTargetYearAndIndicator(targetYear, 2);
        targetSettingDTO.setTargetSettingType(2);
//        targetSettingDTO.setIndicatorId(indicatorId);
        targetSettingDTO.setSort(0);
        TargetSetting setting;
        if (StringUtils.isNull(targetSetting)) {//新增
            TargetSettingIncomeDTO targetSettingIncomeDTO;
            if (StringUtils.isNotEmpty(targetSettingIncomeVOS)) {
                targetSettingIncomeDTO = incomeVoToDto(targetSettingIncomeVOS, targetSettingDTO);
                setting = insertTargetSetting(targetSettingDTO);
                Long targetSettingId = setting.getTargetSettingId();
                targetSettingIncomeDTO.setTargetSettingId(targetSettingId);
                targetSettingIncomeService.insertTargetSettingIncome(targetSettingIncomeDTO);
            } else {
                setting = insertTargetSetting(targetSettingDTO);
            }
            // 新增目标结果
            TargetOutcomeDTO targetOutcomeDTO = targetOutcomeService.selectTargetOutcomeByTargetYear(targetYear);
            if (StringUtils.isNull(targetOutcomeDTO)) {
                TargetOutcomeDTO outcomeDTO = new TargetOutcomeDTO();
                outcomeDTO.setTargetYear(targetYear);
                targetOutcomeService.insertTargetOutcome(outcomeDTO, 2);
            } else {
                R<IndicatorDTO> indicatorDTOR = indicatorService.selectIndicatorByCode("CW002", SecurityConstants.INNER);
                if (indicatorDTOR == null) {
                    throw new ServiceException("添加目标结果 指标ID获取失败");
                }
                IndicatorDTO indicatorDTO = indicatorDTOR.getData();
                if (indicatorDTOR.getCode() != 200 || StringUtils.isNull(indicatorDTO)) {
                    throw new ServiceException("添加目标结果 指标ID获取失败");
                }
                TargetOutcomeDetailsDTO targetOutcomeDetailsDTO = new TargetOutcomeDetailsDTO();
                targetOutcomeDetailsDTO.setIndicatorId(indicatorDTO.getIndicatorId());
                targetOutcomeDetailsDTO.setTargetOutcomeId(targetOutcomeDTO.getTargetOutcomeId());
                targetOutcomeDetailsService.insertTargetOutcomeDetails(targetOutcomeDetailsDTO);
            }
            targetSetting = new TargetSettingDTO();
            targetSetting.setTargetSettingId(setting.getTargetSettingId());
        } else {
            Long targetSettingId = targetSetting.getTargetSettingId();
            if (StringUtils.isNotEmpty(targetSettingIncomeVOS)) {
                TargetSettingIncomeDTO targetSettingIncomeDTO = incomeVoToDto(targetSettingIncomeVOS, targetSettingDTO);
                targetSettingDTO.setTargetSettingId(targetSettingId);
                saveTargetSetting(targetSettingDTO);
                targetSettingIncomeDTO.setTargetSettingId(targetSettingId);
                targetSettingIncomeService.updateTargetSettingIncome(targetSettingIncomeDTO);
            }
            saveTargetSetting(targetSettingDTO);
        }
        return targetSetting;
    }

    /**
     * VO → DTO
     *
     * @param targetSettingIncomeVOS
     * @param targetSettingDTO
     * @return
     */
    private TargetSettingIncomeDTO incomeVoToDto(List<TargetSettingIncomeVO> targetSettingIncomeVOS, TargetSettingDTO targetSettingDTO) {
        TargetSettingIncomeDTO targetSettingIncomeDTO = new TargetSettingIncomeDTO();
        for (int i = 0; i < targetSettingIncomeVOS.size(); i++) {
            if (i == 0) {//前三年
                TargetSettingIncomeVO targetSettingIncomeVO = targetSettingIncomeVOS.get(i);
                targetSettingIncomeDTO.setConversionBeforeThree(targetSettingIncomeVO.getConversion());
                targetSettingIncomeDTO.setMoneyBeforeThree(targetSettingIncomeVO.getMoney());
            } else if (i == 1) {//前两年
                TargetSettingIncomeVO targetSettingIncomeVO = targetSettingIncomeVOS.get(i);
                targetSettingIncomeDTO.setConversionBeforeTwo(targetSettingIncomeVO.getConversion());
                targetSettingIncomeDTO.setMoneyBeforeTwo(targetSettingIncomeVO.getMoney());
            } else if (i == 2) {//前一年
                TargetSettingIncomeVO targetSettingIncomeVO = targetSettingIncomeVOS.get(i);
                targetSettingIncomeDTO.setConversionBeforeOne(targetSettingIncomeVO.getConversion());
                targetSettingIncomeDTO.setMoneyBeforeOne(targetSettingIncomeVO.getMoney());
            } else if (i == 3) {// 本年
                TargetSettingIncomeVO targetSettingIncomeVO = targetSettingIncomeVOS.get(i);
                BigDecimal conversion = targetSettingIncomeVO.getConversion();
                targetSettingDTO.setPercentage(conversion);
            }
        }
        return targetSettingIncomeDTO;
    }

    /**
     * 查询销售收入目标制定列表
     *
     * @param targetSettingDTO
     * @return
     */
    @Override
    public TargetSettingDTO selectIncomeTargetSettingList(TargetSettingDTO targetSettingDTO) {
        Integer targetYear = targetSettingDTO.getTargetYear();
        IndicatorDTO indicatorIncomeDTO = getIndicator(IndicatorCode.INCOME.getCode());
        IndicatorDTO indicatorOrderDTO = getIndicator(IndicatorCode.ORDER.getCode());
        List<Integer> historyNumS = getHistoryYearOppositeList(targetYear + 1, 4);
        TargetSetting targetSetting = new TargetSetting();
        BeanUtils.copyProperties(targetSettingDTO, targetSetting);
        targetSetting.setIndicatorId(indicatorIncomeDTO.getIndicatorId());
        targetSetting.setTargetSettingType(2);
        targetSetting.setTargetYear(targetYear);
        List<TargetSettingDTO> targetSettingDTOS = targetSettingMapper.selectTargetSettingList(targetSetting);// 当年份销售收入的目标制定list
        // 当年份销售订单的目标制定-当年
        TargetSettingDTO targetSettingByIndicator = targetSettingMapper.selectTargetSettingByTargetYearAndIndicator(targetYear, 1);
        BigDecimal zero = new BigDecimal(0);
        if (StringUtils.isNull(targetSettingByIndicator)) {
            targetSettingByIndicator = new TargetSettingDTO();
            targetSettingByIndicator.setChallengeValue(zero);
            targetSettingByIndicator.setGuaranteedValue(zero);
            targetSettingByIndicator.setTargetValue(zero);
        }
        if (StringUtils.isEmpty(targetSettingDTOS)) {
            List<TargetSettingIncomeVO> targetSettingIncomeVOS = new ArrayList<>();
            for (int i = 0; i < historyNumS.size(); i++) {
                TargetSettingIncomeVO targetSettingIncomeVO = new TargetSettingIncomeVO();
                if (i == 3) {//当年  // 目标值 挑战值 保底值  //本年增量订单-订单金额:从经营云-目标制定-公司目标生成-销售订单目标制定中获取当年目标值
                    BigDecimal targetValue = targetSettingByIndicator.getTargetValue();
                    targetSettingIncomeVO.setMoney(targetValue);
                    targetSettingDTO.setChallengeValue(zero);
                    targetSettingDTO.setTargetValue(zero);
                    targetSettingDTO.setGuaranteedValue(zero);
                    targetSettingIncomeVO.setConversion(zero);
                    targetSettingIncomeVO.setIncome(zero);
                    targetSettingIncomeVO.setYearName("本年增量订单");
                    targetSettingIncomeVOS.add(targetSettingIncomeVO);
                } else if (i == 2) {//前一年
                    targetSettingIncomeVO.setMoney(zero);
                    targetSettingIncomeVO.setConversion(zero);
                    targetSettingIncomeVO.setIncome(zero);
                    targetSettingIncomeVO.setYearName(targetYear - 1 + "年存量订单");
                    targetSettingIncomeVOS.add(targetSettingIncomeVO);
                } else if (i == 1) {//前两年
                    targetSettingIncomeVO.setMoney(zero);
                    targetSettingIncomeVO.setConversion(zero);
                    targetSettingIncomeVO.setIncome(zero);
                    targetSettingIncomeVO.setYearName(targetYear - 2 + "年存量订单");
                    targetSettingIncomeVOS.add(targetSettingIncomeVO);
                } else if (i == 0) {//前三年
                    targetSettingIncomeVO.setMoney(zero);
                    targetSettingIncomeVO.setConversion(zero);
                    targetSettingIncomeVO.setIncome(zero);
                    targetSettingIncomeVO.setYearName(targetYear - 3 + "年及以前存量订单");
                    targetSettingIncomeVOS.add(targetSettingIncomeVO);
                }
            }
            targetSettingDTO.setOrderTargetSetting(targetSettingByIndicator);
            targetSettingDTO.setTargetSettingIncomeVOS(targetSettingIncomeVOS);
            return targetSettingDTO;
        }
        TargetSettingDTO setting = targetSettingDTOS.get(0);
        Long targetSettingId = setting.getTargetSettingId();
        List<TargetSettingIncomeDTO> targetSettingIncomeDTOS = targetSettingIncomeService.selectTargetSettingIncomeByHistoryNumS(targetSettingId);
        TargetSettingIncomeDTO targetSettingIncomeDTO = targetSettingIncomeDTOS.get(0);
        List<TargetSettingIncomeVO> targetSettingIncomeVOS = new ArrayList<>();
        // dtoToVo
        for (int i = 0; i < historyNumS.size(); i++) {
            TargetSettingIncomeVO targetSettingIncomeVO = new TargetSettingIncomeVO();
            if (i == 3) {//当年
                targetSettingDTO.setChallengeValue(zero);
                targetSettingDTO.setTargetValue(zero);
                targetSettingDTO.setGuaranteedValue(zero);
                BigDecimal targetValue = targetSettingByIndicator.getTargetValue();
                targetSettingIncomeVO.setConversion(targetSettingByIndicator.getPercentage());
                targetSettingIncomeVO.setMoney(targetValue);
                targetSettingIncomeVO.setYearName("本年增量订单");
                targetSettingIncomeVOS.add(targetSettingIncomeVO);
            } else if (i == 2) {//前一年
                BigDecimal income = targetSettingIncomeDTO.getMoneyBeforeOne().multiply(targetSettingIncomeDTO.getConversionBeforeOne()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                targetSettingIncomeVO.setIncome(income);
                targetSettingIncomeVO.setMoney(targetSettingIncomeDTO.getMoneyBeforeOne());
                targetSettingIncomeVO.setConversion(targetSettingIncomeDTO.getConversionBeforeOne());
                targetSettingIncomeVO.setYearName(targetYear - 1 + "年存量订单");
                targetSettingIncomeVOS.add(targetSettingIncomeVO);
            } else if (i == 1) {//前两年
                BigDecimal income = targetSettingIncomeDTO.getMoneyBeforeTwo().multiply(targetSettingIncomeDTO.getConversionBeforeTwo()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                targetSettingIncomeVO.setIncome(income);
                targetSettingIncomeVO.setMoney(targetSettingIncomeDTO.getMoneyBeforeTwo());
                targetSettingIncomeVO.setConversion(targetSettingIncomeDTO.getConversionBeforeTwo());
                targetSettingIncomeVO.setYearName(targetYear - 2 + "年存量订单");
                targetSettingIncomeVOS.add(targetSettingIncomeVO);
            } else if (i == 0) {//前三年
                BigDecimal income = targetSettingIncomeDTO.getMoneyBeforeThree().multiply(targetSettingIncomeDTO.getConversionBeforeThree()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                targetSettingIncomeVO.setIncome(income);
                targetSettingIncomeVO.setMoney(targetSettingIncomeDTO.getMoneyBeforeThree());
                targetSettingIncomeVO.setConversion(targetSettingIncomeDTO.getConversionBeforeThree());
                targetSettingIncomeVO.setYearName(targetYear - 3 + "年及以前存量订单");
                targetSettingIncomeVOS.add(targetSettingIncomeVO);
            }
        }
        setting.setOrderTargetSetting(targetSettingByIndicator);
        setting.setTargetSettingIncomeVOS(targetSettingIncomeVOS);
        return setting;
    }

    /**
     * 导出销售收入目标制定列表
     *
     * @param targetSettingDTO
     * @return
     */
    @Override
    public List<TargetSettingIncomeExcel> exportIncomeTargetSetting(TargetSettingDTO targetSettingDTO) {
        List<Integer> historyYears = getHistoryYears(targetSettingDTO);
        BigDecimal zero = new BigDecimal(0);
        TargetSettingDTO settingDTO = new TargetSettingDTO();
        settingDTO.setTargetSettingType(2);
        List<TargetSettingDTO> targetSettingDTOList = targetSettingMapper.selectTargetSettingByYears(settingDTO, historyYears);
        if (StringUtils.isEmpty(targetSettingDTOList)) {
            throw new ServiceException("当前目标制定不存在");
        }
        // 查询   remove    赋值0     排序    传给excel
        for (TargetSettingDTO dto : targetSettingDTOList) {
            historyYears.remove(dto.getTargetYear());
        }
        if (StringUtils.isNotEmpty(historyYears)) {
            for (Integer targetYear : historyYears) {
                TargetSettingDTO targetSetting = new TargetSettingDTO();
                targetSetting.setTargetValue(zero);
                targetSetting.setChallengeValue(zero);
                targetSetting.setGuaranteedValue(zero);
                targetSetting.setPercentage(zero);
                targetSetting.setTargetYear(targetYear);
                targetSettingDTOList.add(targetSetting);
            }
        }
        targetSettingDTOList.sort((TargetSettingDTO o1, TargetSettingDTO o2) -> {
            return o2.getTargetYear() - o1.getTargetYear();
        });
        List<TargetSettingIncomeExcel> targetSettingIncomeExcels = new ArrayList<>();
        for (TargetSettingDTO dto : targetSettingDTOList) {
            TargetSettingIncomeExcel targetSettingIncomeExcel = new TargetSettingIncomeExcel();
            targetSettingIncomeExcel.setTargetValue(dto.getTargetValue());
            targetSettingIncomeExcel.setChallengeValue(dto.getChallengeValue());
            targetSettingIncomeExcel.setGuaranteedValue(dto.getGuaranteedValue());
            targetSettingIncomeExcel.setPercentage(dto.getPercentage());
            targetSettingIncomeExcel.setTargetYear(dto.getTargetYear());
            targetSettingIncomeExcels.add(targetSettingIncomeExcel);
        }
        return targetSettingIncomeExcels;
    }

    /**
     * 查询销售回款目标制定列表
     *
     * @param targetSettingDTO 目标制定列表s
     * @return
     */
    @Override
    public TargetSettingDTO selectRecoveryTargetSettingList(TargetSettingDTO targetSettingDTO) {
        Integer targetYear = targetSettingDTO.getTargetYear();
        IndicatorDTO indicatorReceivableDTO = getIndicator(IndicatorCode.RECEIVABLE.getCode());
//        IndicatorDTO indicatorIncomeDTO = getIndicator(IndicatorCode.INCOME.getCode());
        TargetSettingDTO targetSettingByIndicator = targetSettingMapper.selectTargetSettingByTargetYearAndIndicator(targetYear, 3);
        TargetSettingDTO targetIncomeByIndicator = targetSettingMapper.selectTargetSettingByTargetYearAndIndicator(targetYear, 2);
        List<TargetSettingRecoveriesDTO> targetSettingTypeDTOS = new ArrayList<>();
        List<TargetSettingRecoveriesDTO> targetSettingIndicatorDTOS = new ArrayList<>();
        BigDecimal zero = new BigDecimal(0);
        if (StringUtils.isNotNull(targetSettingByIndicator)) {
            Long targetSettingId = targetSettingByIndicator.getTargetSettingId();
            BigDecimal percentage = targetSettingByIndicator.getPercentage();
            TargetSettingRecoveryDTO recoveryDTO = targetSettingRecoveryService.selectTargetSettingRecoveryByTargetSettingId(targetSettingId);
            if (StringUtils.isNull(recoveryDTO)) {
                recoveryDTO = new TargetSettingRecoveryDTO();
            }
            recoveryDTO.setAddRate(percentage);
            List<Map<String, Object>> recoveryList = setRecoveryList(zero, recoveryDTO);
            targetSettingByIndicator.setTargetSettingRecoveryList(recoveryList);
            BigDecimal DSOValue = new BigDecimal(recoveryDTO.getBaselineValue() - recoveryDTO.getImproveDays());
//               todo 查询销售回款列表--recoveries
//              【DSO（应收账款周转天数）】：公式=DSO基线-DSO改进天数。
//              【期末应收账款余额】：公式=（销售收入目标*DSO）/180-上年年末应收账款余额。
            List<TargetSettingRecoveriesDTO> targetSettingRecoveriesDTOS = targetSettingRecoveriesServices.selectTargetSettingRecoveriesByTargetSettingId(targetSettingId);
            if (targetSettingRecoveriesDTOS.size() > 4) {
                //销售收入目标
                TargetSettingRecoveriesDTO saleIncomeGoal = new TargetSettingRecoveriesDTO();//销售收入目标
                TargetSettingRecoveriesDTO periodReceivables = new TargetSettingRecoveriesDTO();//期末应收款余额
                BigDecimal targetSum = BigDecimal.ZERO;
                BigDecimal challengeSum = BigDecimal.ZERO;
                BigDecimal guaranteedSum = BigDecimal.ZERO;
                BigDecimal actualLastSum = BigDecimal.ZERO;
                for (TargetSettingRecoveriesDTO targetSettingRecoveriesDTO : targetSettingRecoveriesDTOS) {
                    switch (targetSettingRecoveriesDTO.getType()) {
                        case 1:
                            targetSettingRecoveriesDTO.setPrefixType("1.应回尽回");
                            targetSum = targetSum.add(targetSettingRecoveriesDTO.getTargetValue());
                            challengeSum = challengeSum.add(targetSettingRecoveriesDTO.getChallengeValue());
                            guaranteedSum = guaranteedSum.add(targetSettingRecoveriesDTO.getGuaranteedValue());
                            actualLastSum = actualLastSum.add(targetSettingRecoveriesDTO.getActualLastYear());
                            targetSettingTypeDTOS.add(targetSettingRecoveriesDTO);
                            break;
                        case 2:
                            targetSettingRecoveriesDTO.setPrefixType("2.逾期清理");
                            targetSum = targetSum.add(targetSettingRecoveriesDTO.getTargetValue());
                            challengeSum = challengeSum.add(targetSettingRecoveriesDTO.getChallengeValue());
                            guaranteedSum = guaranteedSum.add(targetSettingRecoveriesDTO.getGuaranteedValue());
                            actualLastSum = actualLastSum.add(targetSettingRecoveriesDTO.getActualLastYear());
                            targetSettingTypeDTOS.add(targetSettingRecoveriesDTO);
                            break;
                        case 3:
                            targetSettingRecoveriesDTO.setPrefixType("3.提前回款");
                            targetSum = targetSum.add(targetSettingRecoveriesDTO.getTargetValue());
                            challengeSum = challengeSum.add(targetSettingRecoveriesDTO.getChallengeValue());
                            guaranteedSum = guaranteedSum.add(targetSettingRecoveriesDTO.getGuaranteedValue());
                            actualLastSum = actualLastSum.add(targetSettingRecoveriesDTO.getActualLastYear());
                            targetSettingTypeDTOS.add(targetSettingRecoveriesDTO);
                            break;
                        case 4:
                            targetSettingRecoveriesDTO.setPrefixType("销售收入目标");
                            saleIncomeGoal = targetSettingRecoveriesDTO;
                            targetSettingIndicatorDTOS.add(targetSettingRecoveriesDTO);
                            break;
                    }
                }
                TargetSettingRecoveriesDTO targetSettingRecoveriesDTO = new TargetSettingRecoveriesDTO();
//                总和
                targetSettingRecoveriesDTO.setPrefixType("合计");
                targetSettingRecoveriesDTO.setChallengeValue(challengeSum);
                targetSettingRecoveriesDTO.setTargetValue(targetSum);
                targetSettingRecoveriesDTO.setGuaranteedValue(guaranteedSum);
                targetSettingRecoveriesDTO.setActualLastYear(actualLastSum);
                targetSettingTypeDTOS.add(targetSettingRecoveriesDTO);
//                DSO
                targetSettingRecoveriesDTO = new TargetSettingRecoveriesDTO();
                targetSettingRecoveriesDTO.setPrefixType("DSO");
                targetSettingRecoveriesDTO.setChallengeValue(DSOValue);
                targetSettingRecoveriesDTO.setTargetValue(DSOValue);
                targetSettingRecoveriesDTO.setGuaranteedValue(DSOValue);
                targetSettingIndicatorDTOS.add(targetSettingRecoveriesDTO);
//                期末应收账款余额】：公式=（销售收入目标*DSO）/180-上年年末应收账款余额
                targetSettingRecoveriesDTO = new TargetSettingRecoveriesDTO();
                targetSettingRecoveriesDTO.setPrefixType("期末应收账款余额");
                BigDecimal targetDivide = saleIncomeGoal.getTargetValue().multiply(DSOValue).divide(BigDecimal.valueOf(180), 2, RoundingMode.HALF_UP);
                targetSettingRecoveriesDTO.setTargetValue(targetDivide.subtract(recoveryDTO.getBalanceReceivables()));
                BigDecimal challengeDivide = saleIncomeGoal.getChallengeValue().multiply(DSOValue).divide(BigDecimal.valueOf(180), 2, RoundingMode.HALF_UP);
                targetSettingRecoveriesDTO.setChallengeValue(challengeDivide.subtract(recoveryDTO.getBalanceReceivables()));
                BigDecimal guaranteedDivide = saleIncomeGoal.getGuaranteedValue().multiply(DSOValue).divide(BigDecimal.valueOf(180), 2, RoundingMode.HALF_UP);
                targetSettingRecoveriesDTO.setGuaranteedValue(guaranteedDivide.subtract(recoveryDTO.getBalanceReceivables()));
                periodReceivables = targetSettingRecoveriesDTO;
                targetSettingIndicatorDTOS.add(targetSettingRecoveriesDTO);
//              【回款总目标】：公式=上年年末应收账款余额+销售收入目标*（1+平均增值税率）-期末应收账款余额。
                //        1,328.125   1000
                //回款总目标-1.挑战值，目标值，保底值
                targetSettingRecoveriesDTO = new TargetSettingRecoveriesDTO();
                targetSettingRecoveriesDTO.setPrefixType("回款总目标");
                if (StringUtils.isNull(targetIncomeByIndicator)) {
                    targetIncomeByIndicator = new TargetSettingDTO();
                    targetIncomeByIndicator.setTargetValue(zero);
                    targetIncomeByIndicator.setGuaranteedValue(zero);
                    targetIncomeByIndicator.setChallengeValue(zero);
                }
//                BigDecimal challengeGoal = recoveryDTO.getBalanceReceivables().add(targetIncomeByIndicator.getChallengeValue().multiply(targetSettingByIndicator.getPercentage().add(new BigDecimal(1))));
//                BigDecimal targetGoal = recoveryDTO.getBalanceReceivables().add(targetIncomeByIndicator.getTargetValue().multiply(targetSettingByIndicator.getPercentage().add(new BigDecimal(1))));
//                BigDecimal guaranteedGoal = recoveryDTO.getBalanceReceivables().add(targetIncomeByIndicator.getGuaranteedValue().multiply(targetSettingByIndicator.getPercentage().add(new BigDecimal(1))));
                BigDecimal challengeGoal = recoveryDTO.getBalanceReceivables()
                        .add((saleIncomeGoal.getChallengeValue().multiply(((targetSettingByIndicator.getPercentage().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)).add(BigDecimal.valueOf(1))))))
                        .subtract(periodReceivables.getChallengeValue());
                BigDecimal targetGoal = recoveryDTO.getBalanceReceivables()
                        .add((saleIncomeGoal.getTargetValue().multiply(((targetSettingByIndicator.getPercentage().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)).add(BigDecimal.valueOf(1))))))
                        .subtract(periodReceivables.getTargetValue());
                BigDecimal guaranteedGoal = recoveryDTO.getBalanceReceivables()
                        .add((saleIncomeGoal.getGuaranteedValue().multiply(((targetSettingByIndicator.getPercentage().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)).add(BigDecimal.valueOf(1))))))
                        .subtract(periodReceivables.getGuaranteedValue());
                targetSettingRecoveriesDTO.setChallengeValue(challengeGoal);
                targetSettingRecoveriesDTO.setTargetValue(targetGoal);
                targetSettingRecoveriesDTO.setGuaranteedValue(guaranteedGoal);
                targetSettingIndicatorDTOS.add(targetSettingRecoveriesDTO);
            } else {
                setRecoveriesZero(targetSettingTypeDTOS, targetSettingIndicatorDTOS, zero, targetIncomeByIndicator);
            }
        } else {
            targetSettingByIndicator = new TargetSettingDTO();
            targetSettingByIndicator.setChallengeValue(zero);
            targetSettingByIndicator.setTargetValue(zero);
            targetSettingByIndicator.setGuaranteedValue(zero);
            targetSettingByIndicator.setTargetYear(targetYear);
            List<Map<String, Object>> recoveryList = setRecoveryList(zero, null);
            targetSettingByIndicator.setTargetSettingRecoveryList(recoveryList);
            setRecoveriesZero(targetSettingTypeDTOS, targetSettingIndicatorDTOS, zero, targetIncomeByIndicator);
        }
        targetSettingByIndicator.setTargetSettingTypeDTOS(targetSettingTypeDTOS);
        targetSettingByIndicator.setTargetSettingIndicatorDTOS(targetSettingIndicatorDTOS);
        return targetSettingByIndicator;
    }

    /**
     * 销售回款赋值
     *
     * @param zero
     * @return
     */
    private static List<Map<String, Object>> setRecoveryList(BigDecimal zero, TargetSettingRecoveryDTO recoveryDTO) {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map;
        boolean recoveryIsNull = StringUtils.isNull(recoveryDTO);
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    map = new HashMap<>();
                    map.put("name", "上年年末应收账款余额");
                    if (recoveryIsNull) {
                        map.put("value", zero);
                    } else {
                        map.put("value", recoveryDTO.getBalanceReceivables());
                    }
                    list.add(map);
                    break;
                case 1:
                    map = new HashMap<>();
                    map.put("name", "DSO基线");
                    if (recoveryIsNull) {
                        map.put("value", zero);
                    } else {
                        map.put("value", recoveryDTO.getBaselineValue());
                    }
                    list.add(map);
                    break;
                case 2:
                    map = new HashMap<>();
                    map.put("name", "DSO改进天数");
                    if (recoveryIsNull) {
                        map.put("value", zero);
                    } else {
                        map.put("value", recoveryDTO.getImproveDays());
                    }
                    list.add(map);
                    break;
                case 3:
                    map = new HashMap<>();
                    map.put("name", "平均增值税率（%）");
                    if (recoveryIsNull) {
                        map.put("value", zero);
                    } else {
                        map.put("value", recoveryDTO.getAddRate());
                    }
                    list.add(map);
                    break;
            }
        }
        return list;
    }

    /**
     * 空值赋0
     *
     * @param targetSettingTypeDTOS
     * @param targetSettingIndicatorDTOS
     */
    private static void setRecoveriesZero(List<TargetSettingRecoveriesDTO> targetSettingTypeDTOS, List<TargetSettingRecoveriesDTO> targetSettingIndicatorDTOS, BigDecimal zero, TargetSettingDTO targetIncomeByIndicator) {
        TargetSettingRecoveriesDTO targetSettingRecoveriesDTO;
        for (int i = 0; i < 5; i++) {
            switch (i) {
                case 0:
                    targetSettingRecoveriesDTO = new TargetSettingRecoveriesDTO();
                    targetSettingRecoveriesDTO.setPrefixType("1.应回尽回");
                    targetSettingRecoveriesDTO.setActualLastYear(zero);
                    setRecoveriesValue(targetSettingRecoveriesDTO, zero);
                    targetSettingTypeDTOS.add(targetSettingRecoveriesDTO);
                    break;
                case 1:
                    targetSettingRecoveriesDTO = new TargetSettingRecoveriesDTO();
                    targetSettingRecoveriesDTO.setPrefixType("2.逾期清理");
                    targetSettingRecoveriesDTO.setActualLastYear(zero);
                    setRecoveriesValue(targetSettingRecoveriesDTO, zero);
                    targetSettingTypeDTOS.add(targetSettingRecoveriesDTO);
                    break;
                case 2:
                    targetSettingRecoveriesDTO = new TargetSettingRecoveriesDTO();
                    targetSettingRecoveriesDTO.setPrefixType("3.提前回款");
                    targetSettingRecoveriesDTO.setActualLastYear(zero);
                    setRecoveriesValue(targetSettingRecoveriesDTO, zero);
                    targetSettingTypeDTOS.add(targetSettingRecoveriesDTO);
                    break;
                case 3:
//                    【销售收入指标】：从经营云-目标制定-公司目标生成-销售收入目标制定中获取当年目标值，可以编辑。
                    targetSettingRecoveriesDTO = new TargetSettingRecoveriesDTO();
                    targetSettingRecoveriesDTO.setPrefixType("销售收入目标");
                    if (StringUtils.isNotNull(targetIncomeByIndicator)) {
                        targetSettingRecoveriesDTO.setTargetValue(targetIncomeByIndicator.getTargetValue());
                        targetSettingRecoveriesDTO.setGuaranteedValue(targetIncomeByIndicator.getGuaranteedValue());
                        targetSettingRecoveriesDTO.setChallengeValue(targetIncomeByIndicator.getChallengeValue());
                    } else {
                        targetSettingRecoveriesDTO.setTargetValue(zero);
                        targetSettingRecoveriesDTO.setGuaranteedValue(zero);
                        targetSettingRecoveriesDTO.setChallengeValue(zero);
                    }
                    targetSettingIndicatorDTOS.add(targetSettingRecoveriesDTO);
                    break;
            }
        }
        targetSettingRecoveriesDTO = new TargetSettingRecoveriesDTO();
        targetSettingRecoveriesDTO.setPrefixType("DSO");
        setRecoveriesValue(targetSettingRecoveriesDTO, zero);
        targetSettingIndicatorDTOS.add(targetSettingRecoveriesDTO);
        targetSettingRecoveriesDTO = new TargetSettingRecoveriesDTO();
        targetSettingRecoveriesDTO.setPrefixType("期末应收账款余额");
        setRecoveriesValue(targetSettingRecoveriesDTO, zero);
        targetSettingIndicatorDTOS.add(targetSettingRecoveriesDTO);
        targetSettingRecoveriesDTO = new TargetSettingRecoveriesDTO();
        targetSettingRecoveriesDTO.setPrefixType("回款总目标");
        setRecoveriesValue(targetSettingRecoveriesDTO, zero);
        targetSettingIndicatorDTOS.add(targetSettingRecoveriesDTO);
        targetSettingRecoveriesDTO = new TargetSettingRecoveriesDTO();
        targetSettingRecoveriesDTO.setPrefixType("合计");
        setRecoveriesValue(targetSettingRecoveriesDTO, zero);
        targetSettingTypeDTOS.add(targetSettingRecoveriesDTO);
    }

    /**
     * 销售回款list的null→0
     *
     * @param targetSettingRecoveriesDTO
     * @param zero
     */
    private static void setRecoveriesValue(TargetSettingRecoveriesDTO targetSettingRecoveriesDTO, BigDecimal zero) {
        targetSettingRecoveriesDTO.setTargetValue(zero);
        targetSettingRecoveriesDTO.setGuaranteedValue(zero);
        targetSettingRecoveriesDTO.setChallengeValue(zero);
    }

    /**
     * 保存销售回款目标制定列表
     *
     * @param targetSettingDTO 目标制定列表
     * @return
     */
    @Override
    @Transactional
    public TargetSettingDTO saveRecoveryTargetSetting(TargetSettingDTO targetSettingDTO) {
        IndicatorDTO indicatorIncomeDTO = getIndicator(IndicatorCode.RECEIVABLE.getCode());
        Integer targetYear = targetSettingDTO.getTargetYear();
        List<TargetSettingRecoveriesDTO> targetSettingIndicatorDTOS = targetSettingDTO.getTargetSettingIndicatorDTOS();
        List<TargetSettingRecoveriesDTO> targetSettingTypeDTOS = targetSettingDTO.getTargetSettingTypeDTOS();
        List<Map<String, Object>> targetSettingRecoveryList = targetSettingDTO.getTargetSettingRecoveryList();
        TargetSettingRecoveryDTO targetSettingRecoveryDTO = recoveryListToDto(targetSettingRecoveryList, targetSettingDTO);
        targetSettingDTO.setSort(0);
        targetSettingDTO.setTargetSettingType(3);
        targetSettingDTO.setIndicatorId(indicatorIncomeDTO.getIndicatorId());
        targetSettingDTO.setPercentage(targetSettingRecoveryDTO.getAddRate());
        TargetSettingDTO targetSettingByIndicator = targetSettingMapper.selectTargetSettingByTargetYearAndIndicator(targetYear, 3);
        Long targetSettingId;
        if (StringUtils.isNull(targetSettingByIndicator)) {
            TargetSettingRecoveriesDTO recoveriesDTO = targetSettingIndicatorDTOS.stream().filter(f -> f.getPrefixType().equals("回款总目标")).collect(Collectors.toList()).get(0);
            targetSettingByIndicator = new TargetSettingDTO();
            targetSettingByIndicator.setTargetValue(recoveriesDTO.getTargetValue());
            targetSettingByIndicator.setGuaranteedValue(recoveriesDTO.getGuaranteedValue());
            targetSettingByIndicator.setChallengeValue(recoveriesDTO.getChallengeValue());
            // 目标结果新增
            TargetOutcomeDTO targetOutcomeDTO = targetOutcomeService.selectTargetOutcomeByTargetYear(targetYear);
            if (StringUtils.isNull(targetOutcomeDTO)) {
                TargetOutcomeDTO outcomeDTO = new TargetOutcomeDTO();
                outcomeDTO.setTargetYear(targetYear);
                targetOutcomeService.insertTargetOutcome(outcomeDTO, 3);
            } else {
                R<IndicatorDTO> indicatorDTOR = indicatorService.selectIndicatorByCode("CW022", SecurityConstants.INNER);
                if (indicatorDTOR == null) {
                    throw new ServiceException("添加目标结果 指标ID获取失败");
                }
                IndicatorDTO indicatorDTO = indicatorDTOR.getData();
                if (indicatorDTOR.getCode() != 200 || StringUtils.isNull(indicatorDTO)) {
                    throw new ServiceException("添加目标结果 指标ID获取失败");
                }
                TargetOutcomeDetailsDTO targetOutcomeDetailsDTO = new TargetOutcomeDetailsDTO();
                targetOutcomeDetailsDTO.setIndicatorId(indicatorDTO.getIndicatorId());
                targetOutcomeDetailsDTO.setTargetOutcomeId(targetOutcomeDTO.getTargetOutcomeId());
                targetOutcomeDetailsService.insertTargetOutcomeDetails(targetOutcomeDetailsDTO);
            }
            TargetSetting targetSetting = insertTargetSetting(targetSettingDTO);
            targetSettingId = targetSetting.getTargetSettingId();
        } else {
            targetSettingId = targetSettingByIndicator.getTargetSettingId();
            targetSettingDTO.setTargetSettingId(targetSettingId);
            saveTargetSetting(targetSettingDTO);
        }
        TargetSettingRecoveryDTO recovery = targetSettingRecoveryService.selectTargetSettingRecoveryByTargetSettingId(targetSettingId);
        targetSettingRecoveryDTO.setTargetSettingId(targetSettingId);
        if (StringUtils.isNull(recovery)) {
            TargetSettingRecoveryDTO targetSettingRecovery = targetSettingRecoveryService.insertTargetSettingRecovery(targetSettingRecoveryDTO);
        } else {
            Long targetSettingRecoveriesId = recovery.getTargetSettingRecoveriesId();
            targetSettingRecoveryDTO.setTargetSettingRecoveriesId(targetSettingRecoveriesId);
            targetSettingRecoveryService.updateTargetSettingRecovery(targetSettingRecoveryDTO);
        }
        List<TargetSettingRecoveriesDTO> recoveries = targetSettingRecoveriesServices.selectTargetSettingRecoveriesByTargetSettingId(targetSettingId);
        List<TargetSettingRecoveriesDTO> recoveryDTO = integration(targetSettingIndicatorDTOS, targetSettingTypeDTOS, targetSettingId);
        if (StringUtils.isNotEmpty(recoveries) && recoveries.size() > 4) {
            targetSettingRecoveriesServices.updateTargetSettingRecoveriess(recoveryDTO);
        } else {// 新增
            targetSettingRecoveriesServices.insertTargetSettingRecoveriess(recoveryDTO);
        }
        targetSettingDTO.setTargetSettingId(targetSettingId);
        return targetSettingDTO;
    }

    /**
     * 导出销售回款目标制定
     *
     * @param targetSettingDTO
     * @return
     */
    @Override
    public List<TargetSettingRecoveriesExcel> exportRecoveryTargetSetting(TargetSettingDTO targetSettingDTO) {
        List<Integer> historyYears = getHistoryYears(targetSettingDTO);
        TargetSettingDTO settingDTO = new TargetSettingDTO();
        settingDTO.setTargetSettingType(3);
        List<TargetSettingDTO> targetSettingDTOList = targetSettingMapper.selectTargetSettingByYears(settingDTO, historyYears);
        //【销售收入指标】：从经营云-目标制定-公司目标生成-销售收入目标制定中获取当年目标值
        settingDTO.setTargetSettingType(2);
        List<TargetSettingDTO> targetSettingIncomeList = targetSettingMapper.selectTargetSettingByYears(settingDTO, historyYears);
        if (StringUtils.isEmpty(targetSettingDTOList)) {
            throw new ServiceException("当前目标制定不存在");
        }
        List<Long> targetSettingIds = new ArrayList<>();
        for (TargetSettingDTO dto : targetSettingDTOList) {
            targetSettingIds.add(dto.getTargetSettingId());
            historyYears.remove(dto.getTargetYear());//剩下的可以用来造假数据
        }
        List<TargetSettingRecoveryDTO> targetSettingRecoveryDTOS = targetSettingRecoveryService.selectTargetSettingRecoveryByTargetSettingIds(targetSettingIds);
        List<TargetSettingRecoveriesDTO> targetSettingRecoveriesDTOS = targetSettingRecoveriesServices.selectTargetSettingRecoveriesByTargetSettingIds(targetSettingIds);
        List<TargetSettingRecoveriesExcel> targetSettingRecoveriesExcels = new ArrayList<TargetSettingRecoveriesExcel>();
        for (int i = 0; i < targetSettingDTOList.size(); i++) {
            TargetSettingRecoveriesExcel targetSettingRecoveriesExcel = new TargetSettingRecoveriesExcel();
            TargetSettingDTO targetSetting = targetSettingDTOList.get(i);
            Long targetSettingId = targetSetting.getTargetSettingId();
            TargetSettingRecoveryDTO targetSettingRecoveryDTO = targetSettingRecoveryDTOS.get(i);
            // 平均增值税率
            BigDecimal percentage = targetSetting.getPercentage();
            // 销售收入目标
            BigDecimal salesRevenueTarget = BigDecimal.ZERO;
            BigDecimal salesRevenueChallenge = BigDecimal.ZERO;
            BigDecimal salesRevenueGuaranteed = BigDecimal.ZERO;
            for (TargetSettingDTO dto : targetSettingIncomeList) {
                if (dto.getIndicatorId().equals(targetSetting.getIndicatorId())) {
                    salesRevenueTarget = dto.getTargetValue();
                    salesRevenueChallenge = dto.getChallengeValue();
                    salesRevenueGuaranteed = dto.getGuaranteedValue();
                    break;
                }
            }
            // 上年年末应收账款余额
            BigDecimal balanceReceivables = targetSettingRecoveryDTO.getBalanceReceivables();
            // DSO
            int DSO = targetSettingRecoveryDTO.getBaselineValue() - targetSettingRecoveryDTO.getImproveDays();
            // 【期末应收账款余额】：公式=（销售收入目标*DSO）/180-上年年末应收账款余额。
            BigDecimal endingBalanceTarget =
                    ((salesRevenueTarget.multiply(BigDecimal.valueOf(DSO))).divide(BigDecimal.valueOf(180), 2, RoundingMode.HALF_UP)).subtract(balanceReceivables);
            BigDecimal endingBalanceChallenge =
                    ((salesRevenueChallenge.multiply(BigDecimal.valueOf(DSO))).divide(BigDecimal.valueOf(180), 2, RoundingMode.HALF_UP)).subtract(balanceReceivables);
            BigDecimal endingBalanceGuaranteed =
                    ((salesRevenueGuaranteed.multiply(BigDecimal.valueOf(DSO))).divide(BigDecimal.valueOf(180), 2, RoundingMode.HALF_UP)).subtract(balanceReceivables);
            // 【回款总目标】：公式=上年年末应收账款余额+销售收入目标*（1+平均增值税率）-期末应收账款余额。
            BigDecimal rate = (percentage.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)).add(BigDecimal.ONE);
            BigDecimal totalTarget = balanceReceivables.add(salesRevenueTarget.multiply(rate)).add(endingBalanceTarget);
            BigDecimal totalChallenge = balanceReceivables.add(salesRevenueChallenge.multiply(rate)).add(endingBalanceChallenge);
            BigDecimal totalGuaranteed = balanceReceivables.add(salesRevenueGuaranteed.multiply(rate)).add(endingBalanceGuaranteed);
            Map<String, BigDecimal> challengeMap = new HashMap<>();
            Map<String, BigDecimal> targetMap = new HashMap<>();
            Map<String, BigDecimal> guaranteedMap = new HashMap<>();
            for (TargetSettingRecoveriesDTO targetSettingRecoveriesDTO : targetSettingRecoveriesDTOS) {
                if (targetSettingRecoveriesDTO.getTargetSettingId().equals(targetSettingId)) {
                    switch (targetSettingRecoveriesDTO.getType()) {
                        case 1:
                            challengeMap.put("1.应回尽回", targetSettingRecoveriesDTO.getChallengeValue());
                            targetMap.put("1.应回尽回", targetSettingRecoveriesDTO.getTargetValue());
                            guaranteedMap.put("1.应回尽回", targetSettingRecoveriesDTO.getGuaranteedValue());
                        case 2:
                            challengeMap.put("2.逾期清理", targetSettingRecoveriesDTO.getChallengeValue());
                            targetMap.put("2.逾期清理", targetSettingRecoveriesDTO.getTargetValue());
                            guaranteedMap.put("2.逾期清理", targetSettingRecoveriesDTO.getGuaranteedValue());
                        case 3:
                            challengeMap.put("3.提前回款", targetSettingRecoveriesDTO.getChallengeValue());
                            targetMap.put("3.提前回款", targetSettingRecoveriesDTO.getTargetValue());
                            guaranteedMap.put("3.提前回款", targetSettingRecoveriesDTO.getGuaranteedValue());
                    }
                }
            }
            challengeMap.put("期末应收账款余额", salesRevenueChallenge);
            targetMap.put("期末应收账款余额", salesRevenueTarget);
            guaranteedMap.put("期末应收账款余额", salesRevenueGuaranteed);
            challengeMap.put("回款总目标", totalChallenge);
            targetMap.put("回款总目标", totalTarget);
            guaranteedMap.put("回款总目标", totalGuaranteed);
            targetSettingRecoveriesExcel.setTargetMap(targetMap);
            targetSettingRecoveriesExcel.setChallengeMap(challengeMap);
            targetSettingRecoveriesExcel.setGuaranteedMap(guaranteedMap);
            targetSettingRecoveriesExcel.setDSO(DSO);
            targetSettingRecoveriesExcel.setTargetYear(targetSetting.getTargetYear());
            targetSettingRecoveriesExcels.add(targetSettingRecoveriesExcel);
        }
        if (StringUtils.isNotEmpty(historyYears)) {
            for (Integer historyYear : historyYears) {
                TargetSettingRecoveriesExcel targetSettingRecoveriesExcel = new TargetSettingRecoveriesExcel();
                Map<String, BigDecimal> challengeMap = new HashMap<>();
                Map<String, BigDecimal> targetMap = new HashMap<>();
                Map<String, BigDecimal> guaranteedMap = new HashMap<>();
                challengeMap.put("1.应回尽回", BigDecimal.ZERO);
                targetMap.put("1.应回尽回", BigDecimal.ZERO);
                guaranteedMap.put("1.应回尽回", BigDecimal.ZERO);
                challengeMap.put("2.逾期清理", BigDecimal.ZERO);
                targetMap.put("2.逾期清理", BigDecimal.ZERO);
                guaranteedMap.put("2.逾期清理", BigDecimal.ZERO);
                challengeMap.put("3.提前回款", BigDecimal.ZERO);
                targetMap.put("3.提前回款", BigDecimal.ZERO);
                guaranteedMap.put("3.提前回款", BigDecimal.ZERO);
                challengeMap.put("期末应收账款余额", BigDecimal.ZERO);
                targetMap.put("期末应收账款余额", BigDecimal.ZERO);
                guaranteedMap.put("期末应收账款余额", BigDecimal.ZERO);
                challengeMap.put("回款总目标", BigDecimal.ZERO);
                targetMap.put("回款总目标", BigDecimal.ZERO);
                guaranteedMap.put("回款总目标", BigDecimal.ZERO);
                targetSettingRecoveriesExcel.setTargetMap(targetMap);
                targetSettingRecoveriesExcel.setChallengeMap(challengeMap);
                targetSettingRecoveriesExcel.setGuaranteedMap(guaranteedMap);
                targetSettingRecoveriesExcel.setDSO(0);
                targetSettingRecoveriesExcel.setTargetYear(historyYear);
                targetSettingRecoveriesExcels.add(targetSettingRecoveriesExcel);
            }
        }
        return targetSettingRecoveriesExcels;
    }

    /**
     * Recovery List → DTO
     *
     * @param targetSettingRecoveryList
     * @param targetSettingDTO
     * @return
     */
    private TargetSettingRecoveryDTO recoveryListToDto(List<Map<String, Object>> targetSettingRecoveryList, TargetSettingDTO targetSettingDTO) {
        TargetSettingRecoveryDTO targetSettingRecoveryDTO = new TargetSettingRecoveryDTO();
        if (StringUtils.isEmpty(targetSettingRecoveryList)) {
            return targetSettingRecoveryDTO;
        } else {
            for (Map<String, Object> stringObjectMap : targetSettingRecoveryList) {
                if (StringUtils.isNotNull(stringObjectMap)) {
                    String s = stringObjectMap.get("name").toString();
                    switch (s) {
                        case "上年年末应收账款余额":
                            targetSettingRecoveryDTO.setBalanceReceivables(new BigDecimal(stringObjectMap.get("value").toString()));
                            break;
                        case "DSO基线":
                            targetSettingRecoveryDTO.setBaselineValue(Integer.valueOf(stringObjectMap.get("value").toString()));
                            break;
                        case "DSO改进天数":
                            targetSettingRecoveryDTO.setImproveDays(Integer.valueOf(stringObjectMap.get("value").toString()));
                            break;
                        case "平均增值税率（%）":
                            targetSettingRecoveryDTO.setAddRate(new BigDecimal(stringObjectMap.get("value").toString()));
                            break;
                    }
                }
            }
        }
        return targetSettingRecoveryDTO;
    }

    /**
     * 整合两个表
     *
     * @param targetSettingIndicatorDTOS 指标表
     * @param targetSettingTypeDTOS      类型表
     * @param targetSettingId            目标制定ID
     * @return
     */
    private List<TargetSettingRecoveriesDTO> integration
    (List<TargetSettingRecoveriesDTO> targetSettingIndicatorDTOS, List<TargetSettingRecoveriesDTO> targetSettingTypeDTOS, Long targetSettingId) {
        List<TargetSettingRecoveriesDTO> targetSettingRecoveriesDTOS = new ArrayList<>();
        if (StringUtils.isNotEmpty(targetSettingIndicatorDTOS)) {
            for (TargetSettingRecoveriesDTO targetSettingIndicatorDTO : targetSettingIndicatorDTOS) {
                if (targetSettingIndicatorDTO.getPrefixType().equals("销售收入目标")) {
                    targetSettingIndicatorDTO.setType(4);
                    targetSettingIndicatorDTO.setTargetSettingId(targetSettingId);
                    targetSettingRecoveriesDTOS.add(targetSettingIndicatorDTO);
                } else if (targetSettingIndicatorDTO.getPrefixType().equals("期末应收账款余额")) {
                    targetSettingIndicatorDTO.setType(5);
                    targetSettingIndicatorDTO.setTargetSettingId(targetSettingId);
                    targetSettingRecoveriesDTOS.add(targetSettingIndicatorDTO);
                }
            }
        }
        if (StringUtils.isNotEmpty(targetSettingTypeDTOS)) {
            for (TargetSettingRecoveriesDTO targetSettingTypeDTO : targetSettingTypeDTOS) {
                TargetSettingRecoveriesDTO recoveriesDTO = new TargetSettingRecoveriesDTO();
                switch (targetSettingTypeDTO.getPrefixType()) {
                    case "1.应回尽回":
                        targetSettingTypeDTO.setType(1);
                        targetSettingTypeDTO.setTargetSettingId(targetSettingId);
                        targetSettingRecoveriesDTOS.add(targetSettingTypeDTO);
                        break;
                    case "2.逾期清理":
                        targetSettingTypeDTO.setType(2);
                        targetSettingTypeDTO.setTargetSettingId(targetSettingId);
                        targetSettingRecoveriesDTOS.add(targetSettingTypeDTO);
                        break;
                    case "3.提前回款":
                        targetSettingTypeDTO.setType(3);
                        targetSettingTypeDTO.setTargetSettingId(targetSettingId);
                        targetSettingRecoveriesDTOS.add(targetSettingTypeDTO);
                        break;
                }
            }
        }
        return targetSettingRecoveriesDTOS;
    }

}

