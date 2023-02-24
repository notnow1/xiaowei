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
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDetailsDTO;
import net.qixiaowei.operate.cloud.api.remote.targetManager.RemoteDecomposeService;
import net.qixiaowei.system.manage.api.domain.basic.Industry;
import net.qixiaowei.system.manage.api.dto.basic.ConfigDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDefaultDTO;
import net.qixiaowei.system.manage.mapper.basic.IndustryMapper;
import net.qixiaowei.system.manage.service.basic.IConfigService;
import net.qixiaowei.system.manage.service.basic.IIndustryDefaultService;
import net.qixiaowei.system.manage.service.basic.IIndustryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private RemoteDecomposeService targetDecomposeService;


    /**
     * 查询行业 行业启用：1系统；2自定义
     *
     * @param industryId 行业主键
     * @return 行业
     */
    @Override
    public IndustryDTO selectIndustryByIndustryId(Long industryId) {
        Integer enableType = configService.getValueByCode(ConfigCode.INDUSTRY_ENABLE.getCode());
        if (enableType == 1) {
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
    @Override
    public List<IndustryDTO> selectIndustryPageList(IndustryDTO industryDTO) {
        Long parentIndustryId = industryDTO.getParentIndustryId();
        if (StringUtils.isNull(parentIndustryId)) {
            industryDTO.setParentIndustryId(0L);
        }
        Industry industry = new Industry();
        BeanUtils.copyProperties(industryDTO, industry);
        return industryMapper.selectIndustryList(industry);
    }

    /**
     * 查询行业列表
     *
     * @param industryDTO 行业
     * @return 行业
     */
    @Override
    public List<IndustryDTO> selectIndustryList(IndustryDTO industryDTO) {
        Industry industry = new Industry();
        BeanUtils.copyProperties(industryDTO, industry);
        Map<String, Object> params = industryDTO.getParams();
        industry.setParams(params);
        return industryMapper.selectIndustryList(industry);
    }

    /**
     * 树结构
     *
     * @param industryDTO 行业DTO
     * @return List
     */
    @Override
    public List<Tree<Long>> selectIndustryTreeList(IndustryDTO industryDTO) {
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
            tree.putExtra("createBy", treeNode.getCreateBy());
            tree.putExtra("createTime", treeNode.getCreateTime());
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
        if (StringUtils.isEmpty(industryCode)) {
            throw new ServiceException("行业编码不能为空");
        }
        String parentAncestors = "";//仅在非一级行业时有用
        Integer parentLevel = 1;
        Long parentIndustryId = industryDTO.getParentIndustryId();
        if (StringUtils.isNotNull(parentIndustryId)) {
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
        IndustryDTO industryByCode = industryMapper.checkUnique(industryCode);
        if (StringUtils.isNotNull(industryByCode)) {
            throw new ServiceException("新增行业" + industryDTO.getIndustryName() + "失败,行业编码重复");
        }
        Industry industry = new Industry();
        BeanUtils.copyProperties(industryDTO, industry);
        if (StringUtils.isNotNull(parentIndustryId)) {
            String ancestors = parentAncestors;
            if (StringUtils.isNotEmpty(ancestors)) {
                ancestors = ancestors + ",";
            }
            ancestors = ancestors + parentIndustryId;
            industry.setAncestors(ancestors);
            industry.setLevel(parentLevel + 1);
        } else {
            industry.setParentIndustryId(0L);
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
        Long industryId = industryDTO.getIndustryId();
        IndustryDTO industryById = industryMapper.selectIndustryByIndustryId(industryId);
        if (StringUtils.isEmpty(industryCode)) {
            throw new ServiceException("行业编码不能为空");
        }
        if (StringUtils.isNull(industryById)) {
            throw new ServiceException("该行业不存在");
        }
        IndustryDTO industryByCode = industryMapper.checkUnique(industryCode);
        if (StringUtils.isNotNull(industryByCode)) {
            if (!industryByCode.getIndustryId().equals(industryId)) {
                throw new ServiceException("更新行业" + industryDTO.getIndustryName() + "失败,行业编码重复");
            }
        }
        Long parentIndustryId = 0L;
        String ancestors = "";//仅在非一级指标时有用
        int parentLevel = 1;
        if (StringUtils.isNotNull(industryDTO.getParentIndustryId())) {
            parentIndustryId = industryDTO.getParentIndustryId();
            IndustryDTO parentIndustry = industryMapper.selectIndustryByIndustryId(parentIndustryId);
            if (StringUtils.isNull(parentIndustry) && !parentIndustryId.equals(0L)) {
                throw new ServiceException("该上级行业不存在");
            }
            Integer status = industryDTO.getStatus();
            // 如果父节点不为正常状态,则不允许新增子节点
            if (StringUtils.isNotNull(parentIndustry) && BusinessConstants.DISABLE.equals(parentIndustry.getStatus())) {
                throw new ServiceException("上级行业失效，不允许编辑子节点");
            }
            // 路径修改
            if (!industryById.getParentIndustryId().equals(parentIndustryId) && !parentIndustryId.equals(0L)) {
                ancestors = parentIndustry.getAncestors();
                if (StringUtils.isNotEmpty(ancestors)) {
                    ancestors = ancestors + ",";
                }
                ancestors = ancestors + parentIndustryId;
                parentLevel = parentIndustry.getLevel() + 1;
            }
            this.changeSonStatus(industryId, status);
        }
        Industry industry = new Industry();
        BeanUtils.copyProperties(industryDTO, industry);
        industry.setAncestors(ancestors);
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
                        industryDTOList.get(i1).setAncestors(industry.getParentIndustryId() + "," + industry.getIndustryId());
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
                    if (industryDTOList.get(i1 - 1).getIndustryId().equals(industryDTOList.get(i1).getParentIndustryId())) {
                        Industry industry2 = new Industry();
                        //父级
                        IndustryDTO industryDTO2 = industryDTOList.get(i1 - 1);
                        if (StringUtils.isBlank(industryDTO2.getAncestors())) {
                            industryDTOList.get(i1).setAncestors(industryDTO2.getParentIndustryId() + "," + industryDTO2.getIndustryId());
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
                    } else {
                        Industry industry2 = new Industry();
                        //父级
                        IndustryDTO industryDTO2 = industryDTOList.get(map.get(industryDTOList.get(i1).getParentIndustryId()));
                        if (StringUtils.isBlank(industryDTO2.getAncestors())) {
                            industryDTOList.get(i1).setAncestors(industryDTO2.getParentIndustryId() + "," + industryDTO2.getIndustryId());
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
        isQuote(industryIds, industryByIds);
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
            StringBuilder industryNames = new StringBuilder("");
            for (IndustryDTO industryById : industryByIds) {
                for (TargetDecomposeDetailsDTO targetDecomposeDetailsDTO : targetDecomposeDetailsDTOS) {
                    if (targetDecomposeDetailsDTO.getIndustryId().equals(industryById.getIndustryId())) {
                        industryNames.append(industryById.getIndustryName()).append(",");
                        break;
                    }
                }
            }
            quoteReminder.append("行业配置【").append(industryNames.deleteCharAt(industryNames.length() - 1)).append("】正在被目标分解中的【分解维度-区域】引用 无法删除\n");
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
        ArrayList<Long> industryIds = new ArrayList<>();
        industryIds.add(industryId);
        return logicDeleteIndustryByIndustryIds(industryIds);
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
            return selectIndustryTreeList(industryDTO);
        } else {
            IndustryDefaultDTO industryDefaultDTO = new IndustryDefaultDTO();
            BeanUtils.copyProperties(industryDTO, industryDefaultDTO);
            return industryDefaultService.selectIndustryDefaultTreeList(industryDefaultDTO);
        }
    }

    /**
     * 获取启用行业类型
     *
     * @return 行业
     */
    @Override
    public IndustryDTO getEnableType(IndustryDTO industryDTO) {
        Integer enableType = configService.getValueByCode(ConfigCode.INDUSTRY_ENABLE.getCode());
        if (StringUtils.isNull(enableType)) {
            throw new ServiceException("系统配置数据异常");
        }
        industryDTO.setConfigValue(enableType);
        return industryDTO;
    }

    /**
     * 修改启用行业类型
     *
     * @return
     */
    @Override
    public int updateEnableType(Integer configValue) {
        if (StringUtils.isNull(configValue)) {
            throw new ServiceException("configValue传值为空,无法进行操作");
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
        List<IndustryDTO> industryDTOS = new ArrayList<>();
        if (enableType == 1) {
            List<IndustryDefaultDTO> industryDefaultDTOS = industryDefaultService.selectDefaultByCodes(industryCodes);
            if (StringUtils.isEmpty(industryDefaultDTOS)) {
                return new ArrayList<>();
            }
            for (IndustryDefaultDTO industryDefaultDTO : industryDefaultDTOS) {
                IndustryDTO industryDTO = new IndustryDTO();
                BeanUtils.copyProperties(industryDefaultDTO, industryDTO);
                industryDTOS.add(industryDTO);
            }
        } else {
            industryDTOS = industryMapper.selectCodeList(industryCodes);
        }
        return industryDTOS;
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
        Integer enableType = configService.getValueByCode(ConfigCode.INDUSTRY_ENABLE.getCode());
        List<Long> defaultIndustryIds;
        if (enableType == 1) {// 系统
            defaultIndustryIds = industryIds.stream().filter(industryId -> industryId < 100000).collect(Collectors.toList());// 默认
            if (StringUtils.isEmpty(defaultIndustryIds)) {
                return new ArrayList<>();
            }
            List<IndustryDefaultDTO> industryDTOS = industryDefaultService.selectIndustryDefaultByIndustryIds(defaultIndustryIds);
            List<IndustryDTO> industryDTOArrayList = new ArrayList<>();
            for (IndustryDefaultDTO industryDefaultDTO : industryDTOS) {
                IndustryDTO industryDTO = new IndustryDTO();
                BeanUtils.copyProperties(industryDefaultDTO, industryDTO);
                industryDTOArrayList.add(industryDTO);
            }
            return industryDTOArrayList;
        } else {
            defaultIndustryIds = industryIds.stream().filter(industryId -> industryId >= 100000).collect(Collectors.toList());// 自定义
            if (StringUtils.isEmpty(defaultIndustryIds)) {
                return new ArrayList<>();
            }
            return industryMapper.selectIndustryByIndustryIds(defaultIndustryIds);
        }
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
        });
    }
}

