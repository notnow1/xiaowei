package net.qixiaowei.system.manage.service.impl.basic;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import net.qixiaowei.integration.common.constant.BusinessConstants;
import net.qixiaowei.integration.common.constant.Constants;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.enums.PrefixCodeRule;
import net.qixiaowei.integration.common.enums.system.ConfigCode;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.datascope.annotation.DataScope;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.integration.security.utils.UserUtils;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDetailsDTO;
import net.qixiaowei.operate.cloud.api.remote.targetManager.RemoteDecomposeService;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignDTO;
import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.*;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.AnnualKeyWorkDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMeasureDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMetricsDTO;
import net.qixiaowei.strategy.cloud.api.remote.businessDesign.RemoteBusinessDesignService;
import net.qixiaowei.strategy.cloud.api.remote.gap.RemoteGapAnalysisService;
import net.qixiaowei.strategy.cloud.api.remote.marketInsight.*;
import net.qixiaowei.strategy.cloud.api.remote.strategyDecode.RemoteAnnualKeyWorkService;
import net.qixiaowei.strategy.cloud.api.remote.strategyDecode.RemoteStrategyMeasureService;
import net.qixiaowei.strategy.cloud.api.remote.strategyDecode.RemoteStrategyMetricsService;
import net.qixiaowei.system.manage.api.domain.basic.Industry;
import net.qixiaowei.system.manage.api.domain.basic.IndustryDefault;
import net.qixiaowei.system.manage.api.dto.basic.ConfigDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDefaultDTO;
import net.qixiaowei.system.manage.mapper.basic.IndustryDefaultMapper;
import net.qixiaowei.system.manage.mapper.basic.IndustryMapper;
import net.qixiaowei.system.manage.service.basic.IConfigService;
import net.qixiaowei.system.manage.service.basic.IIndustryDefaultService;
import net.qixiaowei.system.manage.service.basic.IIndustryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


/**
 * IndustryService业务层处理
 *
 * @author Graves
 * @since 2022-09-26
 */
@Service
public class IndustryServiceImpl implements IIndustryService {
    @Autowired
    private IndustryMapper industryMapper;

    @Autowired
    private IConfigService configService;

    @Autowired
    private IIndustryDefaultService industryDefaultService;

    @Autowired
    private IndustryDefaultMapper industryDefaultMapper;

    @Autowired
    private RemoteDecomposeService targetDecomposeService;
    @Autowired
    private RemoteAnnualKeyWorkService remoteAnnualKeyWorkService;

    @Autowired
    private RemoteStrategyMeasureService remoteStrategyMeasureService;

    @Autowired
    private RemoteStrategyMetricsService remoteStrategyMetricsService;

    @Autowired
    private RemoteGapAnalysisService remoteGapAnalysisService;

    @Autowired
    private RemoteBusinessDesignService remoteBusinessDesignService;
    @Autowired
    private RemoteMarketInsightCustomerService remoteMarketInsightCustomerService;
    @Autowired
    private RemoteMarketInsightIndustryService remoteMarketInsightIndustryService;
    @Autowired
    private RemoteMarketInsightMacroService remoteMarketInsightMacroService;
    @Autowired
    private RemoteMarketInsightOpponentService remoteMarketInsightOpponentService;
    @Autowired
    private RemoteMarketInsightSelfService remoteMarketInsightSelfService;

    /**
     * 查询行业 行业启用：1系统；2自定义
     *
     * @param industryId 行业主键
     * @return 行业
     */
    @Override
    public IndustryDTO selectIndustryByIndustryId(Long industryId) {
        if (StringUtils.isNull(industryId)) {
            return null;
        }
        if (industryId < 100000) {
            IndustryDefaultDTO industryDefaultDTO = industryDefaultService.selectIndustryDefaultByIndustryId(industryId);
            IndustryDTO industryDTO = new IndustryDTO();
            BeanUtils.copyProperties(industryDefaultDTO, industryDTO);
            return industryDTO;
        } else {
            return industryMapper.selectIndustryByIndustryId(industryId);
        }
    }

    /**
     * 查询行业分页列表
     *
     * @param industryDTO 行业
     * @return 行业
     */
    @DataScope(businessAlias = "i1")
    @Override
    public List<IndustryDTO> selectIndustryPageList(IndustryDTO industryDTO) {
        Industry industry = new Industry();
        BeanUtils.copyProperties(industryDTO, industry);
        Map<String, Object> params = industryDTO.getParams();
        industry.setParams(params);
        return industryMapper.selectIndustryList(industry);
    }

    @Override
    public void handleResult(List<IndustryDTO> result) {
        if (StringUtils.isNotEmpty(result)) {
            Set<Long> userIds = result.stream().map(IndustryDTO::getCreateBy).collect(Collectors.toSet());
            Map<Long, String> employeeNameMap = UserUtils.getEmployeeNameMap(userIds);
            result.forEach(entity -> {
                Long userId = entity.getCreateBy();
                entity.setCreateByName(employeeNameMap.get(userId));
            });
        }
    }

    /**
     * 查询行业列表
     * 行业启用：1系统；2自定义
     *
     * @param industryDTO 行业
     * @return 行业
     */
    @Override
    public List<IndustryDTO> selectIndustryList(IndustryDTO industryDTO) {
        Integer enableType = getInteger();
        if (enableType == 2) {
            Industry industry = new Industry();
            BeanUtils.copyProperties(industryDTO, industry);
            Map<String, Object> params = industryDTO.getParams();
            industry.setParams(params);
            return industryMapper.selectIndustryList(industry);
        } else {
            IndustryDefaultDTO industryDefaultDTO = new IndustryDefaultDTO();
            BeanUtils.copyProperties(industryDTO, industryDefaultDTO);
            List<IndustryDefaultDTO> industryDefaultDTOS = industryDefaultService.selectIndustryDefaultList(industryDefaultDTO);
            if (StringUtils.isEmpty(industryDefaultDTOS)) {
                return new ArrayList<>();
            }
            List<IndustryDTO> industryDTOList = new ArrayList<>();
            for (IndustryDefaultDTO defaultDTO : industryDefaultDTOS) {
                IndustryDTO industryDTO1 = new IndustryDTO();
                BeanUtils.copyProperties(defaultDTO, industryDTO1);
                industryDTOList.add(industryDTO1);
            }
            if (StringUtils.isNotEmpty(industryDTOList)){
                List<IndustryDTO> tree = new ArrayList<>();
                tree.addAll(this.createTree(industryDTOList, 0));
                industryDTOList.clear();
                industryDTOList.addAll(treeToList(tree));
            }
            return industryDTOList;
        }
    }

    /**
     * 树形结构
     *
     * @param lists
     * @param pid
     * @return
     */
    private List<IndustryDTO> createTree(List<IndustryDTO> lists, int pid) {
        List<IndustryDTO> tree = new ArrayList<>();
        for (IndustryDTO catelog : lists) {
            if (catelog.getParentIndustryId() == pid) {
                if (pid == 0) {
                    catelog.setParentIndustryExcelName(catelog.getIndustryName());
                } else {
                    List<IndustryDTO> industryDTOList = lists.stream().filter(f -> f.getIndustryId() == pid).collect(Collectors.toList());
                    if (StringUtils.isNotEmpty(industryDTOList)) {
                        catelog.setParentIndustryExcelName(industryDTOList.get(0).getParentIndustryExcelName() + "/" + catelog.getIndustryName());

                    }
                }
                catelog.setChildren(createTree(lists, Integer.parseInt(catelog.getIndustryId().toString())));
                tree.add(catelog);
            }
        }
        return tree;
    }
    /**
     * 树形数据转list
     *
     * @param industryDTOList
     * @return
     */
    private List<IndustryDTO> treeToList(List<IndustryDTO> industryDTOList) {
        List<IndustryDTO> allSysMenuDto = new ArrayList<>();
        for (IndustryDTO industryDTO : industryDTOList) {
            List<IndustryDTO> children = industryDTO.getChildren();
            allSysMenuDto.add(industryDTO);
            if (children != null && children.size() > 0) {
                allSysMenuDto.addAll(treeToList(children));
                industryDTO.setChildren(null);
            }
        }
        return allSysMenuDto;
    }
    /**
     * 树结构
     *
     * @param industryDTO 行业DTO
     * @return List
     */
    @DataScope(businessAlias = "i1")
    @Override
    public List<Tree<Long>> selectIndustryTreeList(IndustryDTO industryDTO) {
        Industry industry = new Industry();
        BeanUtils.copyProperties(industryDTO, industry);
        List<IndustryDTO> industryDTOS = industryMapper.selectIndustryList(industry);
        this.handleResult(industryDTOS);
        //自定义属性名
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setIdKey("industryId");
        treeNodeConfig.setNameKey("industryName");
        treeNodeConfig.setParentIdKey("parentIndustryId");
        return TreeUtil.build(industryDTOS, Constants.TOP_PARENT_ID, treeNodeConfig, (treeNode, tree) -> {
            tree.setId(treeNode.getIndustryId());
            tree.setParentId(treeNode.getParentIndustryId());
            tree.setName(treeNode.getIndustryName());
            tree.putExtra("parentIndustryName", treeNode.getParentIndustryName());
            tree.putExtra("level", treeNode.getLevel());
            tree.putExtra("industryCode", treeNode.getIndustryCode());
            tree.putExtra("status", treeNode.getStatus());
            tree.putExtra("statusFlag", treeNode.getStatus() != 1);
            tree.putExtra("createBy", treeNode.getCreateBy());
            tree.putExtra("createByName", treeNode.getCreateByName());
            tree.putExtra("createTime", DateUtils.parseDateToStr("yyyy/MM/dd HH:mm:ss", treeNode.getCreateTime()));
        });
    }

    /**
     * 生成行业编码
     *
     * @return 行业编码
     */
    @Override
    public String generateIndustryCode() {
        String industryCode;
        int number = 1;
        String prefixCodeRule = PrefixCodeRule.INDUSTRY.getCode();
        List<String> industryCodes = industryMapper.getIndustryCodes(prefixCodeRule);
        if (StringUtils.isNotEmpty(industryCodes)) {
            for (String code : industryCodes) {
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
        industryCode = "000" + number;
        industryCode = prefixCodeRule + industryCode.substring(industryCode.length() - 3);
        return industryCode;
    }

    /**
     * 新增行业
     *
     * @param industryDTO 行业
     * @return 结果
     */
    @Transactional
    @Override
    public IndustryDTO insertIndustry(IndustryDTO industryDTO) {
        String industryCode = industryDTO.getIndustryCode();
        String industryName = industryDTO.getIndustryName();
        if (StringUtils.isEmpty(industryCode)) {
            throw new ServiceException("请输入行业编码");
        }
        if (StringUtils.isEmpty(industryName)) {
            throw new ServiceException("请输入行业名称");
        }
        String parentAncestors = "";//仅在非一级行业时有用
        Integer parentLevel = 1;
        Long parentIndustryId = industryDTO.getParentIndustryId();
        IndustryDTO industryByCode = industryMapper.checkUnique(industryCode);
        if (StringUtils.isNotNull(industryByCode)) {
            throw new ServiceException("行业编码已存在");
        }
        if (parentIndustryId != 0L) {
            IndustryDTO parentIndustry = industryMapper.selectIndustryByIndustryId(parentIndustryId);
            if (parentIndustry == null) {
                throw new ServiceException("该上级行业不存在");
            }
            parentAncestors = parentIndustry.getAncestors();
            parentLevel = parentIndustry.getLevel();
            Integer status = parentIndustry.getStatus();
            // 如果父节点不为正常状态,则不允许新增子节点
            if (BusinessConstants.DISABLE.equals(status)) {
                throw new ServiceException("上级行业失效，不允许新增子节点");
            }
        }
        Industry industry = new Industry();
        BeanUtils.copyProperties(industryDTO, industry);
        if (parentIndustryId != 0L) {
            industry.setAncestors(StringUtils.isEmpty(parentAncestors) ? parentIndustryId.toString() : parentAncestors + "," + parentIndustryId);
            industry.setLevel(parentLevel + 1);
        } else {
            industry.setParentIndustryId(Constants.TOP_PARENT_ID);
            industry.setAncestors(parentAncestors);
            industry.setLevel(parentLevel);
        }
        industry.setCreateTime(DateUtils.getNowDate());
        industry.setUpdateTime(DateUtils.getNowDate());
        industry.setCreateBy(SecurityUtils.getUserId());
        industry.setUpdateBy(SecurityUtils.getUserId());
        industry.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        industryMapper.insertIndustry(industry);
        industryDTO.setIndustryId(industry.getIndustryId());
        return industryDTO;
    }

    /**
     * 修改行业
     *
     * @param industryDTO 行业
     * @return 结果
     */
    @Transactional
    @Override
    public int updateIndustry(IndustryDTO industryDTO) {
        String industryCode = industryDTO.getIndustryCode();
        String industryName = industryDTO.getIndustryName();
        Long industryId = industryDTO.getIndustryId();
        IndustryDTO industryById = industryMapper.selectIndustryByIndustryId(industryId);
        if (StringUtils.isEmpty(industryCode)) {
            throw new ServiceException("请输入行业编码");
        }
        if (StringUtils.isEmpty(industryName)) {
            throw new ServiceException("请输入行业名称");
        }
        if (StringUtils.isNull(industryById)) {
            throw new ServiceException("该行业不存在");
        }
        IndustryDTO industryByCode = industryMapper.checkUnique(industryCode);
        if (StringUtils.isNotNull(industryByCode)) {
            if (!industryByCode.getIndustryId().equals(industryId)) {
                throw new ServiceException("行业编码已存在");
            }
        }
        Industry industry = new Industry();
        BeanUtils.copyProperties(industryDTO, industry);
        String ancestors ;//仅在非一级指标时有用
        int parentLevel = 1;
        Integer status = industryDTO.getStatus();
        Long parentIndustryId = industryDTO.getParentIndustryId();
        if (parentIndustryId != 0L) {
            IndustryDTO parentIndustry = industryMapper.selectIndustryByIndustryId(parentIndustryId);
            if (StringUtils.isNull(parentIndustry)) {
                throw new ServiceException("该上级行业不存在");
            }
            // 如果父节点不为正常状态,则不允许新增子节点
            if (BusinessConstants.DISABLE.equals(parentIndustry.getStatus())) {
                throw new ServiceException("上级行业失效，不允许编辑子节点");
            }
            // 等级
            parentLevel = parentIndustry.getLevel() + 1;
            // 路径修改
            ancestors = parentIndustry.getAncestors();
            if (StringUtils.isNotEmpty(ancestors)) {
                ancestors = ancestors + ",";
            }
            ancestors = ancestors + parentIndustryId;
        } else {
            ancestors = "";
            industry.setParentIndustryId(Constants.TOP_PARENT_ID);
        }
        this.changeSonStatus(industryId, status);
        industry.setAncestors(ancestors);
        industry.setStatus(industryDTO.getStatus());
        industry.setLevel(parentLevel);
        industry.setParentIndustryId(parentIndustryId);
        industry.setUpdateTime(DateUtils.getNowDate());
        industry.setUpdateBy(SecurityUtils.getUserId());
        int num = industryMapper.updateIndustry(industry);
        List<Industry> industryUpdateList = this.changeSonIndustry(industryId, industry);
        if (StringUtils.isNotEmpty(industryUpdateList)) {
            try {
                industryMapper.updateIndustrys(industryUpdateList);
            } catch (Exception e) {
                throw new ServiceException("批量修改行业子级信息失败");
            }
        }
        return num;
    }

    /**
     * 批量修改子级的祖级列表ID
     *
     * @param industryId 行业ID
     * @param industry   寒夜
     */
    private List<Industry> changeSonIndustry(Long industryId, Industry industry) {
        List<Industry> industryUpdateList = new ArrayList<>();
        Map<Long, Integer> map = new HashMap<>();
        List<IndustryDTO> industryDTOList = industryMapper.selectAncestors(industryId);
        for (int i1 = 0; i1 < industryDTOList.size(); i1++) {
            map.put(industryDTOList.get(i1).getIndustryId(), i1);
        }
        if (StringUtils.isNotEmpty(industryDTOList) && industryDTOList.size() > 1) {
            for (int i1 = 1; i1 < industryDTOList.size(); i1++) {
                if (i1 == 1) {
                    Industry industry2 = new Industry();
                    if (StringUtils.isBlank(industry.getAncestors())) {
                        industryDTOList.get(i1).setAncestors(industry.getIndustryId().toString());
                    } else {
                        industryDTOList.get(i1).setAncestors(industry.getAncestors() + "," + industry.getIndustryId());
                    }
                    industryDTOList.get(i1).setLevel(industry.getLevel() + 1);
                    if (null != industry.getStatus() && industry.getStatus() == 0) {
                        industryDTOList.get(i1).setStatus(industry.getStatus());
                    }
                    industryDTOList.get(i1).setUpdateTime(DateUtils.getNowDate());
                    industryDTOList.get(i1).setUpdateBy(SecurityUtils.getUserId());
                    industryDTOList.get(i1).setParentIndustryId(industry.getIndustryId());
                    BeanUtils.copyProperties(industryDTOList.get(i1), industry2);
                    industryUpdateList.add(industry2);
                } else {
                    Industry industry2 = new Industry();
                    IndustryDTO industryDTO2;
                    if (industryDTOList.get(i1 - 1).getIndustryId().equals(industryDTOList.get(i1).getParentIndustryId())) {
                        //父级
                        industryDTO2 = industryDTOList.get(i1 - 1);
                    } else {
                        //父级
                        industryDTO2 = industryDTOList.get(map.get(industryDTOList.get(i1).getParentIndustryId()));
                    }
                    if (StringUtils.isBlank(industryDTO2.getAncestors())) {
                        industryDTOList.get(i1).setAncestors(industryDTO2.getIndustryId().toString());
                    } else {
                        industryDTOList.get(i1).setAncestors(industryDTO2.getAncestors() + "," + industryDTO2.getIndustryId());
                    }
                    industryDTOList.get(i1).setLevel(industryDTO2.getLevel() + 1);
                    if (null != industry.getStatus() && industry.getStatus() == 0) {
                        industryDTOList.get(i1).setParentIndustryId(industry.getIndustryId());
                    }
                    industryDTOList.get(i1).setUpdateTime(DateUtils.getNowDate());
                    industryDTOList.get(i1).setUpdateBy(SecurityUtils.getUserId());
                    industryDTOList.get(i1).setParentIndustryId(industryDTO2.getIndustryId());
                    BeanUtils.copyProperties(industryDTOList.get(i1), industry2);
                    industryUpdateList.add(industry2);
                }
            }
        }
        return industryUpdateList;
    }

    /**
     * 修改状态
     *
     * @param industryId 行业ID
     * @param status     状态
     */
    private void changeSonStatus(Long industryId, Integer status) {
        if (BusinessConstants.DISABLE.equals(status)) {//失效会影响子级
            //先查再批量更新
            List<IndustryDTO> industryDTOS = industryMapper.selectSon(industryId);
            List<Long> industryIds = industryDTOS.stream().map(IndustryDTO::getIndustryId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(industryIds)) {
                industryIds.add(industryId);
                List<IndustryDTO> industryDTOList = new ArrayList<>();
                for (Long id : industryIds) {
                    IndustryDTO industry = new IndustryDTO();
                    industry.setIndustryId(id);
                    industry.setStatus(status);
                    industryDTOList.add(industry);
                }
                updateIndustrys(industryDTOList);
            }
        }
    }

    /**
     * 战略云引用校验
     *
     * @param industryIds 行业ID集合
     */
    private void isStrategyQuote(List<Long> industryIds) {
        Map<String, Object> params;
        AnnualKeyWorkDTO annualKeyWorkDTO = new AnnualKeyWorkDTO();
        params = new HashMap<>();
        params.put("industryIdEqual", industryIds);
        annualKeyWorkDTO.setParams(params);
        R<List<AnnualKeyWorkDTO>> remoteAnnualKeyWorkR = remoteAnnualKeyWorkService.remoteAnnualKeyWork(annualKeyWorkDTO, SecurityConstants.INNER);
        List<AnnualKeyWorkDTO> annualKeyWorkDTOS = remoteAnnualKeyWorkR.getData();
        if (remoteAnnualKeyWorkR.getCode() != 200) {
            throw new ServiceException("远程调用年度重点工作失败");
        }
        if (StringUtils.isNotEmpty(annualKeyWorkDTOS)) {
            throw new ServiceException("数据被引用!");
        }
        StrategyMeasureDTO strategyMeasureDTO = new StrategyMeasureDTO();
        params = new HashMap<>();
        params.put("industryIdEqual", industryIds);
        strategyMeasureDTO.setParams(params);
        R<List<StrategyMeasureDTO>> remoteStrategyMeasureR = remoteStrategyMeasureService.remoteStrategyMeasure(strategyMeasureDTO, SecurityConstants.INNER);
        List<StrategyMeasureDTO> strategyMeasureDTOS = remoteStrategyMeasureR.getData();
        if (remoteStrategyMeasureR.getCode() != 200) {
            throw new ServiceException("远程调用战略举措清单失败");
        }
        if (StringUtils.isNotEmpty(strategyMeasureDTOS)) {
            throw new ServiceException("数据被引用!");
        }
        StrategyMetricsDTO strategyMetricsDTO = new StrategyMetricsDTO();
        params = new HashMap<>();
        params.put("industryIdEqual", industryIds);
        strategyMetricsDTO.setParams(params);
        R<List<StrategyMetricsDTO>> remoteStrategyMetricsR = remoteStrategyMetricsService.remoteStrategyMetrics(strategyMetricsDTO, SecurityConstants.INNER);
        List<StrategyMetricsDTO> strategyMetricsDTOS = remoteStrategyMetricsR.getData();
        if (remoteStrategyMetricsR.getCode() != 200) {
            throw new ServiceException("远程调用战略衡量指标失败");
        }
        if (StringUtils.isNotEmpty(strategyMetricsDTOS)) {
            throw new ServiceException("数据被引用!");
        }
        GapAnalysisDTO gapAnalysisDTO = new GapAnalysisDTO();
        params = new HashMap<>();
        params.put("industryIdEqual", industryIds);
        gapAnalysisDTO.setParams(params);
        R<List<GapAnalysisDTO>> remoteGapAnalysisR = remoteGapAnalysisService.remoteGapAnalysis(gapAnalysisDTO, SecurityConstants.INNER);
        List<GapAnalysisDTO> gapAnalysisDTOS = remoteGapAnalysisR.getData();
        if (remoteGapAnalysisR.getCode() != 200) {
            throw new ServiceException("远程调用差距分析失败");
        }
        if (StringUtils.isNotEmpty(gapAnalysisDTOS)) {
            throw new ServiceException("数据被引用!");
        }
        BusinessDesignDTO businessDesignDTO = new BusinessDesignDTO();
        params = new HashMap<>();
        params.put("industryIdEqual", industryIds);
        businessDesignDTO.setParams(params);
        R<List<BusinessDesignDTO>> remoteBusinessDesignR = remoteBusinessDesignService.remoteBusinessDesign(businessDesignDTO, SecurityConstants.INNER);
        List<BusinessDesignDTO> businessDesignDTOS = remoteBusinessDesignR.getData();
        if (remoteBusinessDesignR.getCode() != 200) {
            throw new ServiceException("远程调用业务设计失败");
        }
        if (StringUtils.isNotEmpty(businessDesignDTOS)) {
            throw new ServiceException("数据被引用!");
        }


        MarketInsightCustomerDTO marketInsightCustomerDTO = new MarketInsightCustomerDTO();
        params = new HashMap<>();
        params.put("industryIds", industryIds);
        marketInsightCustomerDTO.setParams(params);
        //看客户远程查询是否引用
        R<List<MarketInsightCustomerDTO>> marketInsightCustomerList = remoteMarketInsightCustomerService.remoteMarketInsightCustomerList(marketInsightCustomerDTO, SecurityConstants.INNER);
        List<MarketInsightCustomerDTO> marketInsightCustomerListData = marketInsightCustomerList.getData();
        if (StringUtils.isNotEmpty(marketInsightCustomerListData)) {
            throw new ServiceException("数据被引用！");
        }
        MarketInsightIndustryDTO marketInsightIndustryDTO = new MarketInsightIndustryDTO();
        params = new HashMap<>();
        params.put("industryIds", industryIds);
        marketInsightIndustryDTO.setParams(params);
        //看行业远程查询是否引用
        R<List<MarketInsightIndustryDTO>> marketInsightIndustryList = remoteMarketInsightIndustryService.remoteMarketInsightIndustryList(marketInsightIndustryDTO, SecurityConstants.INNER);
        List<MarketInsightIndustryDTO> marketInsightIndustryListData = marketInsightIndustryList.getData();
        if (StringUtils.isNotEmpty(marketInsightIndustryListData)) {
            throw new ServiceException("数据被引用！");
        }
        MarketInsightMacroDTO marketInsightMacroDTO = new MarketInsightMacroDTO();
        params = new HashMap<>();
        params.put("industryIds", industryIds);
        marketInsightMacroDTO.setParams(params);
        //看宏观远程查询是否引用
        R<List<MarketInsightMacroDTO>> marketInsightMacroList = remoteMarketInsightMacroService.remoteMarketInsightMacroList(marketInsightMacroDTO, SecurityConstants.INNER);
        List<MarketInsightMacroDTO> marketInsightMacroListData = marketInsightMacroList.getData();
        if (StringUtils.isNotEmpty(marketInsightMacroListData)) {
            throw new ServiceException("数据被引用！");
        }
        MarketInsightOpponentDTO marketInsightOpponentDTO = new MarketInsightOpponentDTO();
        params = new HashMap<>();
        params.put("industryIds", industryIds);
        marketInsightOpponentDTO.setParams(params);
        //看对手远程查询是否引用
        R<List<MarketInsightOpponentDTO>> marketInsightOpponentList = remoteMarketInsightOpponentService.remoteMarketInsightOpponentList(marketInsightOpponentDTO, SecurityConstants.INNER);
        List<MarketInsightOpponentDTO> marketInsightOpponentListData = marketInsightOpponentList.getData();
        if (StringUtils.isNotEmpty(marketInsightOpponentListData)) {
            throw new ServiceException("数据被引用！");
        }
        MarketInsightSelfDTO marketInsightSelfDTO = new MarketInsightSelfDTO();
        params = new HashMap<>();
        params.put("industryIds", industryIds);
        marketInsightSelfDTO.setParams(params);
        //看自身远程查询是否引用
        R<List<MarketInsightSelfDTO>> marketInsightSelfList = remoteMarketInsightSelfService.remoteMarketInsightSelfList(marketInsightSelfDTO, SecurityConstants.INNER);
        List<MarketInsightSelfDTO> marketInsightSelfListData = marketInsightSelfList.getData();
        if (StringUtils.isNotEmpty(marketInsightSelfListData)) {
            throw new ServiceException("数据被引用！");
        }

        //远程查询市场洞察行业详情是否被引用
        MiIndustryDetailDTO miIndustryDetailDTO = new MiIndustryDetailDTO();
        params = new HashMap<>();
        params.put("industryIds", industryIds);
        miIndustryDetailDTO.setParams(params);
        //远程查询市场洞察行业详情是否被引用
        R<List<MiIndustryDetailDTO>> miIndustryDetailList = remoteMarketInsightIndustryService.remoteMiIndustryDetailList(miIndustryDetailDTO, SecurityConstants.INNER);
        List<MiIndustryDetailDTO> miIndustryDetailListData = miIndustryDetailList.getData();
        if (StringUtils.isNotEmpty(miIndustryDetailListData)) {
            throw new ServiceException("数据被引用！");
        }

        //远程查询看市场洞察客户选择集合是否被引用(行业)引用
        MiCustomerChoiceDTO miCustomerChoiceDTO = new MiCustomerChoiceDTO();
        params = new HashMap<>();
        params.put("industryIds", industryIds);
        miCustomerChoiceDTO.setParams(params);
        //远程查询看市场洞察客户选择集合是否被引用(行业)引用
        R<List<MiCustomerChoiceDTO>> miCustomerChoiceList = remoteMarketInsightCustomerService.remoteMiCustomerChoiceList(miCustomerChoiceDTO, SecurityConstants.INNER);
        List<MiCustomerChoiceDTO> miCustomerChoiceListData = miCustomerChoiceList.getData();
        if (StringUtils.isNotEmpty(miCustomerChoiceListData)) {
            throw new ServiceException("数据被引用！");
        }

        //远程查询市场洞察客户投资计划集合是否被引用（行业）
        MiCustomerInvestPlanDTO miCustomerInvestPlanDTO = new MiCustomerInvestPlanDTO();
        params = new HashMap<>();
        params.put("industryIds", industryIds);
        miCustomerInvestPlanDTO.setParams(params);
        //远程查询市场洞察客户投资计划集合是否被引用（行业）
        R<List<MiCustomerInvestPlanDTO>> miCustomerInvestPlanList = remoteMarketInsightCustomerService.remoteMiCustomerInvestPlanList(miCustomerInvestPlanDTO, SecurityConstants.INNER);
        List<MiCustomerInvestPlanDTO> miCustomerInvestPlanListData = miCustomerInvestPlanList.getData();
        if (StringUtils.isNotEmpty(miCustomerInvestPlanListData)) {
            throw new ServiceException("数据被引用！");
        }

        //市场洞察对手选择远程查询列表是否被引用(行业)
        MiOpponentChoiceDTO miOpponentChoiceDTO = new MiOpponentChoiceDTO();
        params = new HashMap<>();
        params.put("industryIds", industryIds);
        miOpponentChoiceDTO.setParams(params);
        //市场洞察对手选择远程查询列表是否被引用(行业)
        R<List<MiOpponentChoiceDTO>> miOpponentChoiceList = remoteMarketInsightOpponentService.remoteMiOpponentChoiceList(miOpponentChoiceDTO, SecurityConstants.INNER);
        List<MiOpponentChoiceDTO> miOpponentChoiceListData = miOpponentChoiceList.getData();
        if (StringUtils.isNotEmpty(miOpponentChoiceListData)) {
            throw new ServiceException("数据被引用！");
        }
    }

    /**
     * 逻辑批量删除行业
     *
     * @param industryIds 需要删除的行业主键
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteIndustryByIndustryIds(List<Long> industryIds) {
        List<IndustryDTO> industryByIds = industryMapper.isExist(industryIds);
        if (StringUtils.isEmpty(industryByIds)) {
            throw new ServiceException("该行业已不存在");
        }
        addSons(industryIds);
        //系统经营云引用
        isQuote(industryIds, industryByIds);
        //战略云引用
        isStrategyQuote(industryIds);
        return industryMapper.logicDeleteIndustryByIndustryIds(industryIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 根据ids添加子级
     *
     * @param industryIds
     * @param industryIds
     */
    private void addSons(List<Long> industryIds) {
        List<Long> longs = industryMapper.selectSons(industryIds);
        if (StringUtils.isNotEmpty(longs)) {
            industryIds.addAll(longs);
        }
    }

    /**
     * 行业引用校验
     *
     * @param industryIds   行业ID集合
     * @param industryByIds 行业DTO
     */
    private void isQuote(List<Long> industryIds, List<IndustryDTO> industryByIds) {
        StringBuilder quoteReminder = new StringBuilder("");
        Map<Integer, List<Long>> map = new HashMap<>();
        map.put(6, industryIds);
        R<List<TargetDecomposeDetailsDTO>> decomposeListR = targetDecomposeService.selectByIds(map, SecurityConstants.INNER);
        if (decomposeListR.getCode() != 200) {
            throw new ServiceException("远程调用目标分解失败 请联系管理员");
        }
        List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOS = decomposeListR.getData();
        if (StringUtils.isNotEmpty(targetDecomposeDetailsDTOS)) {
            throw new ServiceException("数据被引用！");
/*            StringBuilder industryNames = new StringBuilder("");
            for (IndustryDTO industryById : industryByIds) {
                for (TargetDecomposeDetailsDTO targetDecomposeDetailsDTO : targetDecomposeDetailsDTOS) {
                    if (targetDecomposeDetailsDTO.getIndustryId().equals(industryById.getIndustryId())) {
                        industryNames.append(industryById.getIndustryName()).append(",");
                        break;
                    }
                }
            }
            quoteReminder.append("行业配置【").append(industryNames.deleteCharAt(industryNames.length() - 1)).append("】正在被目标分解中的【分解维度-区域】引用 无法删除\n");*/
        }
        if (quoteReminder.length() != 0) {
            throw new ServiceException(quoteReminder.toString());
        }
    }

    /**
     * 物理删除行业信息
     *
     * @param industryId 行业主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteIndustryByIndustryId(Long industryId) {
        return industryMapper.deleteIndustryByIndustryId(industryId);
    }

    /**
     * 逻辑删除行业信息
     *
     * @param industryId 行业
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteIndustryByIndustryId(Long industryId) {
        return logicDeleteIndustryByIndustryIds(Collections.singletonList(industryId));
    }

    /**
     * 物理删除行业信息
     *
     * @param industryDTO 行业
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteIndustryByIndustryId(IndustryDTO industryDTO) {
        Industry industry = new Industry();
        BeanUtils.copyProperties(industryDTO, industry);
        return industryMapper.deleteIndustryByIndustryId(industry.getIndustryId());
    }

    /**
     * 物理批量删除行业
     *
     * @param industryDtos 需要删除的行业主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteIndustryByIndustryIds(List<IndustryDTO> industryDtos) {
        List<Long> stringList = new ArrayList<>();
        for (IndustryDTO industryDTO : industryDtos) {
            stringList.add(industryDTO.getIndustryId());
        }
        return industryMapper.deleteIndustryByIndustryIds(stringList);
    }

    /**
     * 批量新增行业信息
     *
     * @param industryDtos 行业对象
     */
    @Override
    @Transactional
    public int insertIndustrys(List<IndustryDTO> industryDtos) {
        List<Industry> industryList = new ArrayList<>();
        for (IndustryDTO industryDTO : industryDtos) {
            Industry industry = new Industry();
            BeanUtils.copyProperties(industryDTO, industry);
            industry.setCreateBy(SecurityUtils.getUserId());
            industry.setCreateTime(DateUtils.getNowDate());
            industry.setUpdateTime(DateUtils.getNowDate());
            industry.setUpdateBy(SecurityUtils.getUserId());
            industry.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            industryList.add(industry);
        }
        return industryMapper.batchIndustry(industryList);
    }

    /**
     * 批量修改行业信息
     *
     * @param industryDtos 行业对象
     */
    @Override
    @Transactional
    public int updateIndustrys(List<IndustryDTO> industryDtos) {
        List<Industry> industryList = new ArrayList<>();

        for (IndustryDTO industryDTO : industryDtos) {
            Industry industry = new Industry();
            BeanUtils.copyProperties(industryDTO, industry);
            industry.setUpdateTime(DateUtils.getNowDate());
            industry.setUpdateBy(SecurityUtils.getUserId());
            industryList.add(industry);
        }
        return industryMapper.updateIndustrys(industryList);
    }

    /**
     * 获取启用行业类型
     *
     * @return
     */
    @Override
    public List<Tree<Long>> getEnableList(IndustryDTO industryDTO) {
        Integer enableType = configService.getValueByCode(ConfigCode.INDUSTRY_ENABLE.getCode());
        if (StringUtils.isNull(enableType)) {
            throw new ServiceException("系统配置数据异常");
        }
        if (enableType == 2) {
            Industry industry = new Industry();
            BeanUtils.copyProperties(industryDTO, industry);
            List<IndustryDTO> industryDTOS = industryMapper.selectIndustryList(industry);
            //自定义属性名
            TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
            treeNodeConfig.setIdKey("industryId");
            treeNodeConfig.setNameKey("industryName");
            treeNodeConfig.setParentIdKey("parentIndustryId");
            return TreeUtil.build(industryDTOS, Constants.TOP_PARENT_ID, treeNodeConfig, (treeNode, tree) -> {
                tree.setId(treeNode.getIndustryId());
                tree.setParentId(treeNode.getParentIndustryId());
                tree.setName(treeNode.getIndustryName());
                tree.putExtra("parentIndustryName", treeNode.getParentIndustryName());
                tree.putExtra("level", treeNode.getLevel());
                tree.putExtra("industryCode", treeNode.getIndustryCode());
                tree.putExtra("status", treeNode.getStatus());
                tree.putExtra("statusFlag", treeNode.getStatus() != 1);
                tree.putExtra("createBy", treeNode.getCreateBy());
                tree.putExtra("statusValue", treeNode.getStatus() != 1);
                tree.putExtra("createTime", DateUtils.parseDateToStr("yyyy/MM/dd HH:mm:ss", treeNode.getCreateTime()));
            });
        } else {
            IndustryDefault industryDefault = new IndustryDefault();
            BeanUtils.copyProperties(industryDTO, industryDefault);
            List<IndustryDefaultDTO> industryDefaultDTOS = industryDefaultMapper.selectIndustryDefaultList(industryDefault);
            //自定义属性名
            TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
            treeNodeConfig.setIdKey("industryId");
            treeNodeConfig.setNameKey("industryName");
            treeNodeConfig.setParentIdKey("parentIndustryId");
            return TreeUtil.build(industryDefaultDTOS, Constants.TOP_PARENT_ID, treeNodeConfig, (treeNode, tree) -> {
                tree.setId(treeNode.getIndustryId());
                tree.setParentId(treeNode.getParentIndustryId());
                tree.setName(treeNode.getIndustryName());
                tree.putExtra("parentIndustryName", treeNode.getParentIndustryName());
                tree.putExtra("level", treeNode.getLevel());
                tree.putExtra("industryCode", treeNode.getIndustryCode());
                tree.putExtra("status", treeNode.getStatus());
                tree.putExtra("statusFlag", treeNode.getStatus() != 1);
                tree.putExtra("createBy", treeNode.getCreateBy());
                tree.putExtra("statusValue", treeNode.getStatus() != 1);
                tree.putExtra("createTime", DateUtils.parseDateToStr("yyyy/MM/dd HH:mm:ss", treeNode.getCreateTime()));
            });
        }
    }

    /**
     * 行业启用：1系统；2自定义
     *
     * @return Integer
     */
    private Integer getInteger() {
        Integer enableType = configService.getValueByCode(ConfigCode.INDUSTRY_ENABLE.getCode());
        if (StringUtils.isNull(enableType)) {
            throw new ServiceException("系统配置数据异常");
        }
        return enableType;
    }

    /**
     * 获取启用行业类型
     *
     * @return 行业
     */
    @Override
    public IndustryDTO getEnableType(IndustryDTO industryDTO) {
        Integer enableType = getInteger();
        industryDTO.setConfigValue(enableType);
        return industryDTO;
    }

    /**
     * 修改启用行业类型
     *
     * @return int
     */
    @Override
    public int updateEnableType(Integer configValue) {
        if (StringUtils.isNull(configValue)) {
            throw new ServiceException("configValue传值为空,无法进行操作");
        }
        if (!Arrays.asList(1, 2).contains(configValue)) {
            throw new ServiceException("请传入规范内的枚举值");
        }
        ConfigDTO configById = configService.selectConfigByConfigCode(ConfigCode.INDUSTRY_ENABLE.getCode());
        if (StringUtils.isNull(configById)) {
            throw new ServiceException("当前无启用行业");
        }
        ConfigDTO configDTO = new ConfigDTO();
        configDTO.setConfigValue(configValue.toString());
        configDTO.setConfigCode(ConfigCode.INDUSTRY_ENABLE.getCode());
        return configService.updateConfig(configDTO);
    }

    /**
     * 行业配置详情
     *
     * @param industryId 行业ID
     * @return 行业DTO
     */
    @Override
    public IndustryDTO detailIndustry(Long industryId) {
        if (StringUtils.isNull(industryId)) {
            throw new ServiceException("行业配置id不能为空");
        }
        IndustryDTO industryDTO = industryMapper.selectIndustryByIndustryId(industryId);
        if (StringUtils.isNull(industryDTO)) {
            throw new ServiceException("该行业配置已不存在");
        }
        Long parentIndustryId = industryDTO.getParentIndustryId();
        if (parentIndustryId != 0) {
            IndustryDTO parentIndustryDTO = industryMapper.selectIndustryByIndustryId(parentIndustryId);
            industryDTO.setParentIndustryName(parentIndustryDTO.getIndustryName());
        }
        return industryDTO;
    }

    /**
     * 获取行业的层级列表
     *
     * @return List
     */
    @Override
    public List<Integer> getLevel() {
        return industryMapper.getLevelList();
    }

    /**
     * 根据code集合查询行业信息
     *
     * @param industryCodes 行业编码
     * @return List
     */
    @Override
    public List<IndustryDTO> selectCodeList(List<String> industryCodes) {
        Integer enableType = configService.getValueByCode(ConfigCode.INDUSTRY_ENABLE.getCode());
        List<IndustryDefaultDTO> industryDefaultDTOS = industryDefaultService.selectDefaultByCodes(industryCodes);
        List<IndustryDTO> industryDTOList = industryMapper.selectCodeList(industryCodes);
        if (StringUtils.isEmpty(industryDefaultDTOS) && StringUtils.isEmpty(industryDTOList)) {
            return new ArrayList<>();
        } else if (StringUtils.isNotEmpty(industryDefaultDTOS) && StringUtils.isEmpty(industryDTOList)) {
            List<IndustryDTO> industryDTOArrayList = new ArrayList<>();
            for (IndustryDefaultDTO industryDefaultDTO : industryDefaultDTOS) {
                IndustryDTO industryDTO = new IndustryDTO();
                BeanUtils.copyProperties(industryDefaultDTO, industryDTO);
                industryDTOArrayList.add(industryDTO);
            }
            industryDTOList.addAll(industryDTOArrayList);
            return industryDTOArrayList;
        } else if (StringUtils.isEmpty(industryDefaultDTOS) && StringUtils.isNotEmpty(industryDTOList)) {
            return industryDTOList;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 通过Id集合查询行业信息
     *
     * @param industryIds 行业ID集合
     * @return List
     */
    @Override
    public List<IndustryDTO> selectIndustryByIndustryIds(List<Long> industryIds) {
        if (StringUtils.isEmpty(industryIds)) {
            return new ArrayList<>();
        }
        List<IndustryDTO> industryDTOS = new ArrayList<>();
        List<Long> customIndustryIds = industryIds.stream().filter(industryId -> industryId >= 100000).collect(Collectors.toList());// 自定义
        if (StringUtils.isNotEmpty(customIndustryIds)) {
            List<IndustryDTO> industryDTOList = industryMapper.selectIndustryByIndustryIds(customIndustryIds);
            if (StringUtils.isNotEmpty(industryDTOList)) {
                industryDTOS.addAll(industryDTOList);
            }
        }
        List<Long> defaultIndustryIds = industryIds.stream().filter(industryId -> industryId < 100000).collect(Collectors.toList());// 默认
        if (StringUtils.isNotEmpty(defaultIndustryIds)) {
            List<IndustryDefaultDTO> industryDefaultDTOS = industryDefaultService.selectIndustryDefaultByIndustryIds(defaultIndustryIds);
            if (StringUtils.isNotEmpty(industryDefaultDTOS)) {
                List<IndustryDTO> industryDTOArrayList = new ArrayList<>();
                for (IndustryDefaultDTO industryDefaultDTO : industryDefaultDTOS) {
                    IndustryDTO industryDTO = new IndustryDTO();
                    BeanUtils.copyProperties(industryDefaultDTO, industryDTO);
                    industryDTOArrayList.add(industryDTO);
                }
                industryDTOS.addAll(industryDTOArrayList);
            }
        }
        return industryDTOS;
    }

    /**
     * 获取上级行业
     *
     * @param industryId 行业DTO
     * @return Tree
     */
    @Override
    public List<Tree<Long>> getSuperIndustry(Long industryId) {
        if (StringUtils.isNull(industryId)) {
            throw new ServiceException("请传入行业ID");
        }
        IndustryDTO industryById = industryMapper.selectIndustryByIndustryId(industryId);
        if (StringUtils.isNull(industryById)) {
            throw new ServiceException("当前行业已不存在");
        }
        List<IndustryDTO> industryDTOS = industryMapper.selectIndustryList(new Industry());
        List<IndustryDTO> sonIndustryDTOS = industryMapper.selectSon(industryId);
        ArrayList<IndustryDTO> removeIndustryDTOS = new ArrayList<>();
        for (IndustryDTO industryDTO : industryDTOS) {
            if (industryDTO.getIndustryId().equals(industryId)) {
                removeIndustryDTOS.add(industryDTO);
            }
            for (IndustryDTO sonIndustryDTO : sonIndustryDTOS) {
                if (industryDTO.getIndustryId().equals(sonIndustryDTO.getIndustryId())) {
                    removeIndustryDTOS.add(industryDTO);
                }
            }
        }
        if (StringUtils.isNotEmpty(removeIndustryDTOS)) {
            industryDTOS.removeAll(removeIndustryDTOS);
        }
        //自定义属性名
        return getTreeList(industryDTOS);
    }

    /**
     * 获取树
     *
     * @param industryDTOS 列表
     * @return 结果
     */
    private static List<Tree<Long>> getTreeList(List<IndustryDTO> industryDTOS) {
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setIdKey("industryId");
        treeNodeConfig.setNameKey("industryName");
        treeNodeConfig.setParentIdKey("parentIndustryId");
        return TreeUtil.build(industryDTOS, Constants.TOP_PARENT_ID, treeNodeConfig, (treeNode, tree) -> {
            tree.setId(treeNode.getIndustryId());
            tree.setParentId(treeNode.getParentIndustryId());
            tree.setName(treeNode.getIndustryName());
            tree.putExtra("parentIndustryName", treeNode.getParentIndustryName());
            tree.putExtra("level", treeNode.getLevel());
            tree.putExtra("industryCode", treeNode.getIndustryCode());
            tree.putExtra("status", treeNode.getStatus());
            tree.putExtra("statusFlag", treeNode.getStatus() != 1);
        });
    }

    /**
     * 查询行业树结构列表
     *
     * @param industryDTO 行业
     * @return 结果
     */
    @Override
    public List<Tree<Long>> selectIndustryEffectiveTreeList(IndustryDTO industryDTO) {
        Integer status = industryDTO.getStatus();
        if (StringUtils.isNull(status)) {
            throw new ServiceException("请传入状态");
        }
        List<IndustryDTO> industryDTOS = industryMapper.selectIndustryList(new Industry().setStatus(status));
        return getTreeList(industryDTOS);
    }
}

