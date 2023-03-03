package net.qixiaowei.system.manage.service.impl.basic;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import net.qixiaowei.integration.common.constant.Constants;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.enums.PrefixCodeRule;
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
    private static final Map<Integer, Indicator> INIT_INDICATOR = new HashMap<>();

    static {
        //初始化指标
        INIT_INDICATOR.put(1, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW001").indicatorName("订单（不含税）").sort(1).level(1).indicatorValueType(1).choiceFlag(1).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(2, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW002").indicatorName("销售收入").sort(2).level(1).indicatorValueType(1).choiceFlag(1).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(3, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW003").indicatorName("销售成本").sort(3).level(1).indicatorValueType(1).choiceFlag(0).examineDirection(0).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(4, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW004").indicatorName("材料成本").sort(1).level(2).indicatorValueType(1).choiceFlag(0).examineDirection(0).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(5, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW005").indicatorName("材料成本率").sort(2).level(2).indicatorValueType(2).choiceFlag(0).examineDirection(0).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(6, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW006").indicatorName("直接制造人工").sort(3).level(2).indicatorValueType(1).choiceFlag(0).examineDirection(0).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(7, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW007").indicatorName("直接制造人工率").sort(4).level(2).indicatorValueType(2).choiceFlag(0).examineDirection(0).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(8, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW008").indicatorName("间接制造人工").sort(5).level(2).indicatorValueType(1).choiceFlag(0).examineDirection(0).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(9, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW009").indicatorName("间接制造人工率").sort(6).level(2).indicatorValueType(2).choiceFlag(0).examineDirection(0).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(10, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW010").indicatorName("销售毛利").sort(4).level(1).indicatorValueType(1).choiceFlag(1).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(11, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW011").indicatorName("销售毛利率").sort(5).level(1).indicatorValueType(2).choiceFlag(0).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(12, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW012").indicatorName("研发费用").sort(6).level(1).indicatorValueType(1).choiceFlag(0).examineDirection(0).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(13, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW013").indicatorName("销售费用").sort(7).level(1).indicatorValueType(1).choiceFlag(0).examineDirection(0).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(14, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW014").indicatorName("管理费用").sort(8).level(1).indicatorValueType(1).choiceFlag(0).examineDirection(0).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(15, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW015").indicatorName("其他业务收支").sort(9).level(1).indicatorValueType(1).choiceFlag(0).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(16, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW016").indicatorName("营业利润（EBIT）").sort(10).level(1).indicatorValueType(1).choiceFlag(0).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(17, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW017").indicatorName("财务费用").sort(11).level(1).indicatorValueType(1).choiceFlag(0).examineDirection(0).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(18, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW018").indicatorName("税前利润").sort(12).level(1).indicatorValueType(1).choiceFlag(0).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(19, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW019").indicatorName("企业所得税").sort(13).level(1).indicatorValueType(1).choiceFlag(0).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(20, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW020").indicatorName("净利润").sort(14).level(1).indicatorValueType(1).choiceFlag(1).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(21, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW021").indicatorName("净利润率").sort(15).level(1).indicatorValueType(2).choiceFlag(0).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(22, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW022").indicatorName("回款金额（含税）").sort(16).level(1).indicatorValueType(1).choiceFlag(1).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(23, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW023").indicatorName("经营性现金流").sort(17).level(1).indicatorValueType(1).choiceFlag(0).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(24, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW024").indicatorName("总资产").sort(18).level(1).indicatorValueType(1).choiceFlag(0).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(25, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW025").indicatorName("资产负债率").sort(19).level(1).indicatorValueType(2).choiceFlag(0).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(26, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW026").indicatorName("现金及现金等价物").sort(20).level(1).indicatorValueType(1).choiceFlag(0).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(27, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW027").indicatorName("运营资产（存货+应收）").sort(21).level(1).indicatorValueType(1).choiceFlag(0).examineDirection(1).drivingFactorFlag(0).build());
    }

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

    @Override
    public Boolean initData() {
        Long userId = SecurityUtils.getUserId();
        Date nowDate = DateUtils.getNowDate();
        //先初始化销售成本
        Indicator indicator = INIT_INDICATOR.get(3);
        indicator.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        indicator.setCreateBy(userId);
        indicator.setUpdateBy(userId);
        indicator.setCreateTime(nowDate);
        indicator.setUpdateTime(nowDate);
        boolean indicatorSuccess = indicatorMapper.insertIndicator(indicator) > 0;
        if (!indicatorSuccess) {
            return false;
        }
        Long indicatorId = indicator.getIndicatorId();
        List<Indicator> indicators = new ArrayList<>(26);
        for (Map.Entry<Integer, Indicator> entry : INIT_INDICATOR.entrySet()) {
            Integer key = entry.getKey();
            Indicator value = entry.getValue();
            if (key == 3) {
                continue;
            }
            //销售成本下的二级指标处理
            if (key > 3 && key < 10) {
                value.setParentIndicatorId(indicatorId);
                value.setAncestors(indicatorId.toString());
            }
            value.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            value.setCreateBy(userId);
            value.setUpdateBy(userId);
            value.setCreateTime(nowDate);
            value.setUpdateTime(nowDate);
            indicators.add(value);
        }
        //初始化指标
        return indicatorMapper.batchIndicator(indicators) > 0;
    }

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
        Indicator indicator = new Indicator();
        BeanUtils.copyProperties(indicatorDTO, indicator);
        String indicatorCategoryName = indicatorDTO.getIndicatorCategoryName();
        Map<String, Object> params = indicatorDTO.getParams();
        if (StringUtils.isNotNull(indicatorCategoryName)) {
            params.put("indicatorCategoryName", indicatorCategoryName);
        }
        indicator.setParams(params);
        List<IndicatorDTO> indicatorDTOS = indicatorMapper.selectIndicatorList(indicator);
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
        Indicator indicator = new Indicator();
        BeanUtils.copyProperties(indicatorDTO, indicator);
        List<IndicatorDTO> indicatorDTOS = indicatorMapper.selectIndicatorList(indicator);
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
            tree.putExtra("parentIndicatorName", treeNode.getParentIndicatorName());
            tree.putExtra("isPreset", treeNode.getIsPreset());
            tree.putExtra("createBy", treeNode.getCreateBy());
            tree.putExtra("createTime", DateUtils.parseDateToStr("yyyy/MM/dd HH:mm:ss", treeNode.getCreateTime()));
        });
    }

    /**
     * 生成指标编码
     *
     * @return 指标编码
     */
    @Override
    public String generateIndicatorCode(Integer indicatorType) {
        String prefixCodeRule = PrefixCodeRule.INDICATOR_FINANCE.getCode();
        if (indicatorType.equals(2)) {
            prefixCodeRule = PrefixCodeRule.INDICATOR_BUSINESS.getCode();
        }
        String indicatorCode;
        int number = 1;
        List<String> indicatorCodes = indicatorMapper.getIndicatorCodes(prefixCodeRule);
        if (StringUtils.isNotEmpty(indicatorCodes)) {
            for (String code : indicatorCodes) {
                if (StringUtils.isEmpty(code) || code.length() != 5) {
                    continue;
                }
                code = code.replaceFirst(prefixCodeRule, "");
                try {
                    int codeOfNumber = Integer.parseInt(code);
                    if (number != codeOfNumber) {
                        break;
                    }
                    number++;
                } catch (Exception ignored) {
                }
            }
        }
        if (number > 1000) {
            throw new ServiceException("流水号溢出，请联系管理员");
        }
        indicatorCode = "000" + number;
        indicatorCode = prefixCodeRule + indicatorCode.substring(indicatorCode.length() - 3);
        return indicatorCode;
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
            throw new ServiceException("该指标不存在");
        }
        IndicatorDTO indicatorByCode = indicatorMapper.getIndicatorByCode(indicatorCode);
        if (StringUtils.isNotNull(indicatorByCode)) {
            if (!indicatorByCode.getIndicatorId().equals(indicatorId)) {
                throw new ServiceException("更新指标" + indicatorDTO.getIndicatorName() + "失败,指标编码重复");
            }
        }
        String ancestors = "";//仅在非一级指标时有用
        int parentLevel = 1;
        Long parentIndicatorId = 0L;
        if (StringUtils.isNotNull(indicatorDTO.getParentIndicatorId())) {
            parentIndicatorId = indicatorDTO.getParentIndicatorId();
            IndicatorDTO parentIndicator = indicatorMapper.selectIndicatorByIndicatorId(parentIndicatorId);
            if (parentIndicator == null && !parentIndicatorId.equals(0L)) {
                throw new ServiceException("上级指标不存在");
            }
            if (!indicatorById.getParentIndicatorId().equals(parentIndicatorId) && !parentIndicatorId.equals(0L)) {
                // 路径修改
                ancestors = parentIndicator.getAncestors();
                if (StringUtils.isNotEmpty(ancestors)) {
                    ancestors = ancestors + ",";
                }
                ancestors = ancestors + parentIndicatorId;
                parentLevel = parentIndicator.getLevel() + 1;
            }
        }
        Indicator indicator = new Indicator();
        BeanUtils.copyProperties(indicatorDTO, indicator);
        indicator.setAncestors(ancestors);
        indicator.setLevel(parentLevel);
        indicator.setParentIndicatorId(parentIndicatorId);
        indicator.setUpdateTime(DateUtils.getNowDate());
        indicator.setUpdateBy(SecurityUtils.getUserId());
        int num = indicatorMapper.updateIndicator(indicator);
        List<Indicator> indicatorUpdateList = this.changeSonAncestor(indicatorId, indicator);
        if (StringUtils.isNotEmpty(indicatorUpdateList)) {
            try {
                indicatorMapper.updateIndicators(indicatorUpdateList);
            } catch (Exception e) {
                throw new ServiceException("批量修改指标子级信息失败");
            }
        }
        return num;
    }

    /**
     * 批量修改子级的祖级列表ID
     *
     * @param indicatorId 指标ID
     * @param indicator   指DTO
     */
    private List<Indicator> changeSonAncestor(Long indicatorId, Indicator indicator) {
        List<Indicator> indicatorUpdateList = new ArrayList<>();
        Map<Long, Integer> map = new HashMap<>();
        List<IndicatorDTO> indicatorDTOList = indicatorMapper.selectAncestors(indicatorId);
        for (int i1 = 0; i1 < indicatorDTOList.size(); i1++) {
            map.put(indicatorDTOList.get(i1).getIndicatorId(), i1);
        }
        if (StringUtils.isNotEmpty(indicatorDTOList) && indicatorDTOList.size() > 1) {
            for (int i1 = 1; i1 < indicatorDTOList.size(); i1++) {
                if (i1 == 1) {
                    Indicator indicator2 = new Indicator();
                    if (StringUtils.isBlank(indicator.getAncestors())) {
                        indicatorDTOList.get(i1).setAncestors(indicator.getParentIndicatorId() + "," + indicator.getIndicatorId());
                    } else {
                        indicatorDTOList.get(i1).setAncestors(indicator.getAncestors() + "," + indicator.getIndicatorId());
                    }
                    indicatorDTOList.get(i1).setLevel(indicator.getLevel() + 1);
                    indicatorDTOList.get(i1).setUpdateTime(DateUtils.getNowDate());
                    indicatorDTOList.get(i1).setUpdateBy(SecurityUtils.getUserId());
                    indicatorDTOList.get(i1).setParentIndicatorId(indicator.getIndicatorId());
                    BeanUtils.copyProperties(indicatorDTOList.get(i1), indicator2);
                    indicatorUpdateList.add(indicator2);
                } else {
                    if (indicatorDTOList.get(i1 - 1).getIndicatorId().equals(indicatorDTOList.get(i1).getParentIndicatorId())) {
                        Indicator indicator2 = new Indicator();
                        //父级
                        IndicatorDTO indicatorDTO2 = indicatorDTOList.get(i1 - 1);
                        if (StringUtils.isBlank(indicatorDTO2.getAncestors())) {
                            indicatorDTOList.get(i1).setAncestors(indicatorDTO2.getParentIndicatorId() + "," + indicatorDTO2.getIndicatorId());
                        } else {
                            indicatorDTOList.get(i1).setAncestors(indicatorDTO2.getAncestors() + "," + indicatorDTO2.getIndicatorId());
                        }
                        indicatorDTOList.get(i1).setLevel(indicatorDTO2.getLevel() + 1);
                        indicatorDTOList.get(i1).setUpdateTime(DateUtils.getNowDate());
                        indicatorDTOList.get(i1).setUpdateBy(SecurityUtils.getUserId());
                        indicatorDTOList.get(i1).setParentIndicatorId(indicatorDTO2.getIndicatorId());
                        BeanUtils.copyProperties(indicatorDTOList.get(i1), indicator2);
                        indicatorUpdateList.add(indicator2);
                    } else {
                        Indicator indicator2 = new Indicator();
                        //父级
                        IndicatorDTO indicatorDTO2 = indicatorDTOList.get(map.get(indicatorDTOList.get(i1).getParentIndicatorId()));
                        if (StringUtils.isBlank(indicatorDTO2.getAncestors())) {
                            indicatorDTOList.get(i1).setAncestors(indicatorDTO2.getParentIndicatorId() + "," + indicatorDTO2.getIndicatorId());
                        } else {
                            indicatorDTOList.get(i1).setAncestors(indicatorDTO2.getAncestors() + "," + indicatorDTO2.getIndicatorId());
                        }
                        indicatorDTOList.get(i1).setLevel(indicatorDTO2.getLevel() + 1);
                        indicatorDTOList.get(i1).setUpdateTime(DateUtils.getNowDate());
                        indicatorDTOList.get(i1).setUpdateBy(SecurityUtils.getUserId());
                        indicatorDTOList.get(i1).setParentIndicatorId(indicatorDTO2.getIndicatorId());
                        BeanUtils.copyProperties(indicatorDTOList.get(i1), indicator2);
                        indicatorUpdateList.add(indicator2);
                    }
                }
            }
        }
        return indicatorUpdateList;

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
        if (StringUtils.isNotNull(bonusBudget)) {
            List<BonusBudgetParametersDTO> bonusBudgetParametersDTOS = bonusBudget.getBonusBudgetParametersDTOS();
            if (StringUtils.isNotNull(bonusBudgetParametersDTOS) && StringUtils.isNotEmpty(bonusBudgetParametersDTOS)) {
                StringBuilder indicatorNames = new StringBuilder("");
                for (IndicatorDTO indicatorById : indicatorByIds) {
                    for (BonusBudgetParametersDTO bonusBudgetParametersDTO : bonusBudgetParametersDTOS) {
                        if (indicatorById.getIndicatorId().equals(bonusBudgetParametersDTO.getIndicatorId())) {
                            indicatorNames.append(indicatorById.getIndicatorName()).append(",");
                            break;
                        }
                    }
                }
                StringBuilder bonusBudgetParameterName = new StringBuilder("");
                Set<String> bonusBudgetParametersSet = new HashSet<>();
                for (BonusBudgetParametersDTO bonusBudgetParametersDTO : bonusBudgetParametersDTOS) {
                    for (IndicatorDTO indicatorById : indicatorByIds) {
                        if (indicatorById.getIndicatorId().equals(bonusBudgetParametersDTO.getIndicatorId())) {
                            bonusBudgetParametersSet.add(bonusBudgetParametersDTO.getBudgetYear().toString());
                        }
                    }
                }
                for (String performance : bonusBudgetParametersSet) {
                    bonusBudgetParameterName.append(performance).append(",");
                }
                quoteReminder.append("指标配置【")
                        .append(indicatorNames.deleteCharAt(indicatorNames.length() - 1))
                        .append("】已被总奖金包预算中的【奖金驱动因素】【")
                        .append(bonusBudgetParameterName.deleteCharAt(bonusBudgetParameterName.length() - 1))
                        .append("】引用 无法删除\n");
            }
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
        Indicator indicatorDTO = new Indicator();
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
        Indicator indicator = new Indicator();
        BeanUtils.copyProperties(indicatorDTO, indicator);
        List<IndicatorDTO> indicatorDTOS = indicatorMapper.selectIndicatorList(indicator);
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
     * 获取上级指标
     *
     * @param indicatorId 指标ID
     * @return List
     */
    @Override
    public List<Tree<Long>> getSuperIndicator(Long indicatorId) {
        if (StringUtils.isNull(indicatorId)) {
            throw new ServiceException("请传入指标ID");
        }
        IndicatorDTO indicatorById = indicatorMapper.selectIndicatorByIndicatorId(indicatorId);
        if (StringUtils.isNull(indicatorById)) {
            throw new ServiceException("当前指标已不存在");
        }
        List<IndicatorDTO> indicatorDTOS = indicatorMapper.selectIndicatorList(new Indicator());
        List<IndicatorDTO> sonIndicatorDTOS = indicatorMapper.selectSon(indicatorId);
        ArrayList<IndicatorDTO> removeIndicatorDTOS = new ArrayList<>();
        for (IndicatorDTO indicatorDTO : indicatorDTOS) {
            if (indicatorDTO.getIndicatorId().equals(indicatorId)) {
                removeIndicatorDTOS.add(indicatorDTO);
            }
            for (IndicatorDTO sonIndicatorDTO : sonIndicatorDTOS) {
                if (indicatorDTO.getIndicatorId().equals(sonIndicatorDTO.getIndicatorId())) {
                    removeIndicatorDTOS.add(indicatorDTO);
                }
            }
        }
        if (StringUtils.isNotEmpty(removeIndicatorDTOS)) {
            indicatorDTOS.removeAll(removeIndicatorDTOS);
        }
        //自定义属性名
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
        });
    }

    /**
     * 查询仪表盘列表
     *
     * @return list
     */
    @Override
    public List<Map<String, Object>> selectIndicatorDashboardList(Integer targetYear) {
        if (StringUtils.isNull(targetYear)) {
            targetYear = DateUtils.getYear();
        }
        R<List<TargetSettingDTO>> listR = targetSettingService.queryIndicatorSettingList(new TargetSettingDTO(), SecurityConstants.INNER);
        List<TargetSettingDTO> targetSettingDTOS = listR.getData();
        if (StringUtils.isEmpty(targetSettingDTOS)) {
            return new ArrayList<>();
        }
        List<IndicatorDTO> indicatorDTOS = indicatorMapper.selectIndicatorList(new Indicator());
        List<Map<String, Object>> list = new ArrayList<>();
        for (IndicatorDTO indicatorDTO : indicatorDTOS) {
            for (TargetSettingDTO targetSettingDTO : targetSettingDTOS) {
                if (targetSettingDTO.getIndicatorId().equals(indicatorDTO.getIndicatorId())) {
                    Map<String, Object> map = new HashMap<>();
                    Integer isPreset = IndicatorCode.selectIsPreset(indicatorDTO.getIndicatorCode());
                    map.put("indicatorId", indicatorDTO.getIndicatorId());
                    map.put("indicatorName", indicatorDTO.getIndicatorName());
                    map.put("indicatorCode", indicatorDTO.getIndicatorCode());
                    if (isPreset == 1) {
                        map.put("isPreset", 1);
                        map.put("isNotPreset", 1);
                    } else {
                        map.put("isPreset", 2);
                        map.put("isNotPreset", 0);
                    }
                    list.add(map);
                    break;
                }
            }
        }
        list.sort(Comparator.comparing(m -> String.valueOf(m.get("isPreset"))));
        return list;
    }

    /**
     * 远程查询指标列表平铺
     *
     * @param indicatorDTO
     * @return
     */
    @Override
    public List<IndicatorDTO> getIndicatorAllData(IndicatorDTO indicatorDTO) {
        Indicator indicator = new Indicator();
        BeanUtils.copyProperties(indicatorDTO, indicator);
        return indicatorMapper.selectIndicatorList(indicator);
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
    @Override
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
    @Override
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
