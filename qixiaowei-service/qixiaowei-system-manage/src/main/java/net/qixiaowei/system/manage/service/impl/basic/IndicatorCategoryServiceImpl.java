package net.qixiaowei.system.manage.service.impl.basic;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.enums.PrefixCodeRule;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.datascope.annotation.DataScope;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.integration.security.utils.UserUtils;
import net.qixiaowei.system.manage.api.domain.basic.IndicatorCategory;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorCategoryDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import net.qixiaowei.system.manage.mapper.basic.IndicatorCategoryMapper;
import net.qixiaowei.system.manage.mapper.basic.IndicatorMapper;
import net.qixiaowei.system.manage.service.basic.IIndicatorCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * IndicatorCategoryService业务层处理
 *
 * @author Graves
 * @since 2022-09-28
 */
@Service
public class IndicatorCategoryServiceImpl implements IIndicatorCategoryService {
    @Autowired
    private IndicatorCategoryMapper indicatorCategoryMapper;

    @Autowired
    private IndicatorMapper indicatorMapper;

    /**
     * 查询指标类型表
     *
     * @param indicatorCategoryId 指标类型表主键
     * @return 指标类型表
     */
    @Override
    public IndicatorCategoryDTO selectIndicatorCategoryByIndicatorCategoryId(Long indicatorCategoryId) {
        return indicatorCategoryMapper.selectIndicatorCategoryByIndicatorCategoryId(indicatorCategoryId);
    }

    /**
     * 查询指标类型表列表
     *
     * @param indicatorCategoryDTO 指标类型表
     * @return 指标类型表
     */
    @DataScope(businessAlias = "ic")
    @Override
    public List<IndicatorCategoryDTO> selectIndicatorCategoryList(IndicatorCategoryDTO indicatorCategoryDTO) {
        IndicatorCategory indicatorCategory = new IndicatorCategory();
        BeanUtils.copyProperties(indicatorCategoryDTO, indicatorCategory);
        List<IndicatorCategoryDTO> indicatorCategoryDTOS = indicatorCategoryMapper.selectIndicatorCategoryList(indicatorCategory);
        this.handleResult(indicatorCategoryDTOS);
        return indicatorCategoryDTOS;
    }

    @Override
    public void handleResult(List<IndicatorCategoryDTO> result) {
        if (StringUtils.isNotEmpty(result)) {
            Set<Long> userIds = result.stream().map(IndicatorCategoryDTO::getCreateBy).collect(Collectors.toSet());
            Map<Long, String> employeeNameMap = UserUtils.getEmployeeNameMap(userIds);
            result.forEach(entity -> {
                Long userId = entity.getCreateBy();
                entity.setCreateByName(employeeNameMap.get(userId));
            });
        }
    }

    /**
     * 生成指标类型编码
     *
     * @return 指标类型编码
     */
    @Override
    public String generateIndicatorCategoryCode(Integer indicatorType) {
        String prefixCodeRule = PrefixCodeRule.INDICATOR_CATEGORY_FINANCE.getCode();
        if (indicatorType.equals(2)) {
            prefixCodeRule = PrefixCodeRule.INDICATOR_CATEGORY_BUSINESS.getCode();
        }
        String indicatorCategoryCode;
        int number = 1;
        List<String> indicatorCategoryCodes = indicatorCategoryMapper.getIndicatorCategoryCodes(prefixCodeRule);
        if (StringUtils.isNotEmpty(indicatorCategoryCodes)) {
            for (String code : indicatorCategoryCodes) {
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
        indicatorCategoryCode = "000" + number;
        indicatorCategoryCode = prefixCodeRule + indicatorCategoryCode.substring(indicatorCategoryCode.length() - 3);
        return indicatorCategoryCode;
    }

    /**
     * 新增指标类型表
     *
     * @param indicatorCategoryDTO 指标类型表
     * @return 结果
     */
    @Transactional
    @Override
    public IndicatorCategoryDTO insertIndicatorCategory(IndicatorCategoryDTO indicatorCategoryDTO) {
        String indicatorCategoryCode = indicatorCategoryDTO.getIndicatorCategoryCode();
        String indicatorCategoryName = indicatorCategoryDTO.getIndicatorCategoryName();
        Long indicatorCategoryId = indicatorCategoryDTO.getIndicatorCategoryId();
        Integer indicatorType = indicatorCategoryDTO.getIndicatorType();
        if (StringUtils.isEmpty(indicatorCategoryName)) {
            throw new ServiceException("指标类型名称不能为空");
        }
        if (StringUtils.isEmpty(indicatorCategoryCode)) {
            throw new ServiceException("指标类型编码不能为空");
        }
        IndicatorCategoryDTO indicatorCategoryByCode = indicatorCategoryMapper.checkCodeUnique(indicatorCategoryCode);
        if (StringUtils.isNotNull(indicatorCategoryByCode)) {
            throw new ServiceException("指标类型编码重复");
        }
        IndicatorCategoryDTO indicatorCategoryByName = indicatorCategoryMapper.checkNameUnique(indicatorCategoryName);
        if (StringUtils.isNotNull(indicatorCategoryByName)) {
            throw new ServiceException("指标类型名称重复");
        }
        if (StringUtils.isNull(indicatorType)) {
            throw new ServiceException("指标类型不可以为空");
        }
        IndicatorCategory indicatorCategory = new IndicatorCategory();
        BeanUtils.copyProperties(indicatorCategoryDTO, indicatorCategory);
        indicatorCategory.setCreateBy(SecurityUtils.getUserId());
        indicatorCategory.setCreateTime(DateUtils.getNowDate());
        indicatorCategory.setUpdateTime(DateUtils.getNowDate());
        indicatorCategory.setUpdateBy(SecurityUtils.getUserId());
        indicatorCategory.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        indicatorCategoryMapper.insertIndicatorCategory(indicatorCategory);
        return indicatorCategoryDTO.setIndicatorCategoryId(indicatorCategory.getIndicatorCategoryId());
    }

    /**
     * 修改指标类型表
     *
     * @param indicatorCategoryDTO 指标类型表
     * @return 结果
     */
    @Transactional
    @Override
    public int updateIndicatorCategory(IndicatorCategoryDTO indicatorCategoryDTO) {
        String indicatorCategoryCode = indicatorCategoryDTO.getIndicatorCategoryCode();
        Long indicatorCategoryId = indicatorCategoryDTO.getIndicatorCategoryId();
        String indicatorCategoryName = indicatorCategoryDTO.getIndicatorCategoryName();
        if (StringUtils.isNull(indicatorCategoryId)) {
            throw new ServiceException("指标类型id不能为空");
        }
        if (StringUtils.isEmpty(indicatorCategoryCode)) {
            throw new ServiceException("指标类型编码不能为空");
        }
        IndicatorCategoryDTO indicatorCategoryById = indicatorCategoryMapper.selectIndicatorCategoryByIndicatorCategoryId(indicatorCategoryId);
        if (StringUtils.isNull(indicatorCategoryById)) {
            throw new ServiceException("当前指标类型不存在");
        }
        IndicatorCategoryDTO indicatorCategoryByCode = indicatorCategoryMapper.checkCodeUnique(indicatorCategoryCode);
        if ((StringUtils.isNotNull(indicatorCategoryByCode)) && !indicatorCategoryByCode.getIndicatorCategoryId().equals(indicatorCategoryId)) {
            throw new ServiceException("更新指标" + indicatorCategoryName + "失败,指标编码重复");
        }
        IndicatorCategoryDTO indicatorCategoryByName = indicatorCategoryMapper.checkNameUnique(indicatorCategoryName);
        if (StringUtils.isNotNull(indicatorCategoryByName) && !indicatorCategoryByName.getIndicatorCategoryId().equals(indicatorCategoryId)) {
            throw new ServiceException("更新指标" + indicatorCategoryName + "失败,指标编码重复");
        }

        IndicatorCategory indicatorCategory = new IndicatorCategory();
        BeanUtils.copyProperties(indicatorCategoryDTO, indicatorCategory);
        indicatorCategory.setUpdateTime(DateUtils.getNowDate());
        indicatorCategory.setUpdateBy(SecurityUtils.getUserId());
        return indicatorCategoryMapper.updateIndicatorCategory(indicatorCategory);
    }

    /**
     * 逻辑批量删除指标类型表
     *
     * @param indicatorCategoryIds 需要删除的指标类型表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteIndicatorCategoryByIndicatorCategoryIds(List<Long> indicatorCategoryIds) {
        List<IndicatorCategoryDTO> indicatorCategoryByIds = indicatorCategoryMapper.selectIndicatorCategoryIds(indicatorCategoryIds);
        if (StringUtils.isEmpty(indicatorCategoryByIds)) {
            throw new ServiceException("指标类型不存在");
        }
        isQuote(indicatorCategoryIds, indicatorCategoryByIds);
        return indicatorCategoryMapper.logicDeleteIndicatorCategoryByIndicatorCategoryIds(indicatorCategoryIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 引用校验
     *
     * @param indicatorCategoryIds   需要删除的指标类型表主键
     * @param indicatorCategoryByIds 指标类型DTO集合
     */
    private void isQuote(List<Long> indicatorCategoryIds, List<IndicatorCategoryDTO> indicatorCategoryByIds) {
        StringBuilder quoteReminder = new StringBuilder("");
        List<IndicatorDTO> indicatorDTOS = indicatorMapper.selectIndicatorCountByIndicatorCategoryId(indicatorCategoryIds);
        if (StringUtils.isNotEmpty(indicatorDTOS)) {
            StringBuilder indicatorCategoryNames = new StringBuilder("");
            for (IndicatorDTO indicatorDTO : indicatorDTOS) {
                for (IndicatorCategoryDTO indicatorCategoryDTO : indicatorCategoryByIds) {
                    if (indicatorCategoryDTO.getIndicatorCategoryId().equals(indicatorDTO.getIndicatorCategoryId())) {
                        indicatorCategoryNames.append(indicatorCategoryDTO.getIndicatorCategoryName()).append(",");
                        break;
                    }
                }
            }
            quoteReminder.append("指标类型配置【").append(indicatorCategoryNames.deleteCharAt(indicatorCategoryNames.length() - 1)).append("】已被指标配置中的【指标类型】引用 无法删除\n");
        }
        if (quoteReminder.length() != 0) {
            throw new ServiceException(quoteReminder.toString());
        }
    }

    /**
     * 物理删除指标类型表信息
     *
     * @param indicatorCategoryId 指标类型表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteIndicatorCategoryByIndicatorCategoryId(Long indicatorCategoryId) {
        return indicatorCategoryMapper.deleteIndicatorCategoryByIndicatorCategoryId(indicatorCategoryId);
    }

    /**
     * 指标类型详情
     *
     * @param indicatorCategoryId
     * @return
     */
    @Override
    public IndicatorCategoryDTO detailIndicatorCategory(Long indicatorCategoryId) {
        if (StringUtils.isNull(indicatorCategoryId)) {
            throw new ServiceException("指标类型id不能为空");
        }
        IndicatorCategoryDTO indicatorCategoryDTO = indicatorCategoryMapper.selectIndicatorCategoryByIndicatorCategoryId(indicatorCategoryId);
        if (StringUtils.isNull(indicatorCategoryDTO)) {
            throw new ServiceException("指标类型已不存在");
        }
        return indicatorCategoryDTO;
    }

    /**
     * 逻辑删除指标类型表信息
     *
     * @param indicatorCategoryId 指标类型表
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteIndicatorCategoryByIndicatorCategoryId(Long indicatorCategoryId) {
        ArrayList<Long> indicatorCategoryIds = new ArrayList<>();
        indicatorCategoryIds.add(indicatorCategoryId);
        return logicDeleteIndicatorCategoryByIndicatorCategoryIds(indicatorCategoryIds);
    }

    /**
     * 物理删除指标类型表信息
     *
     * @param indicatorCategoryDTO 指标类型表
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteIndicatorCategoryByIndicatorCategoryId(IndicatorCategoryDTO indicatorCategoryDTO) {
        IndicatorCategory indicatorCategory = new IndicatorCategory();
        BeanUtils.copyProperties(indicatorCategoryDTO, indicatorCategory);
        return indicatorCategoryMapper.deleteIndicatorCategoryByIndicatorCategoryId(indicatorCategory.getIndicatorCategoryId());
    }

    /**
     * 物理批量删除指标类型表
     *
     * @param indicatorCategoryDtos 需要删除的指标类型表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteIndicatorCategoryByIndicatorCategoryIds(List<IndicatorCategoryDTO> indicatorCategoryDtos) {
        List<Long> stringList = new ArrayList<>();
        for (IndicatorCategoryDTO indicatorCategoryDTO : indicatorCategoryDtos) {
            stringList.add(indicatorCategoryDTO.getIndicatorCategoryId());
        }
        return indicatorCategoryMapper.deleteIndicatorCategoryByIndicatorCategoryIds(stringList);
    }

    /**
     * 批量新增指标类型表信息
     *
     * @param indicatorCategoryDtos 指标类型表对象
     */
    @Override
    @Transactional
    public int insertIndicatorCategorys(List<IndicatorCategoryDTO> indicatorCategoryDtos) {
        List<IndicatorCategory> indicatorCategoryList = new ArrayList<>();
        for (IndicatorCategoryDTO indicatorCategoryDTO : indicatorCategoryDtos) {
            IndicatorCategory indicatorCategory = new IndicatorCategory();
            BeanUtils.copyProperties(indicatorCategoryDTO, indicatorCategory);
            indicatorCategory.setCreateBy(SecurityUtils.getUserId());
            indicatorCategory.setCreateTime(DateUtils.getNowDate());
            indicatorCategory.setUpdateTime(DateUtils.getNowDate());
            indicatorCategory.setUpdateBy(SecurityUtils.getUserId());
            indicatorCategory.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            indicatorCategoryList.add(indicatorCategory);
        }
        return indicatorCategoryMapper.batchIndicatorCategory(indicatorCategoryList);
    }

    /**
     * 批量修改指标类型表信息
     *
     * @param indicatorCategoryDtos 指标类型表对象
     */
    @Override
    @Transactional
    public int updateIndicatorCategorys(List<IndicatorCategoryDTO> indicatorCategoryDtos) {
        List<IndicatorCategory> indicatorCategoryList = new ArrayList<>();

        for (IndicatorCategoryDTO indicatorCategoryDTO : indicatorCategoryDtos) {
            IndicatorCategory indicatorCategory = new IndicatorCategory();
            BeanUtils.copyProperties(indicatorCategoryDTO, indicatorCategory);
            indicatorCategory.setCreateBy(SecurityUtils.getUserId());
            indicatorCategory.setCreateTime(DateUtils.getNowDate());
            indicatorCategory.setUpdateTime(DateUtils.getNowDate());
            indicatorCategory.setUpdateBy(SecurityUtils.getUserId());
            indicatorCategoryList.add(indicatorCategory);
        }
        return indicatorCategoryMapper.updateIndicatorCategorys(indicatorCategoryList);
    }
}

