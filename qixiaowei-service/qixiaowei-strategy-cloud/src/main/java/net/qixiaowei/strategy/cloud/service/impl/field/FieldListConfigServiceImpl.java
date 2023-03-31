package net.qixiaowei.strategy.cloud.service.impl.field;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.enums.field.BaseField;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.integration.tenant.annotation.IgnoreTenant;
import net.qixiaowei.integration.tenant.service.ITenantService;
import net.qixiaowei.integration.tenant.utils.TenantUtils;
import net.qixiaowei.strategy.cloud.api.domain.field.FieldConfig;
import net.qixiaowei.strategy.cloud.api.domain.field.FieldListConfig;
import net.qixiaowei.strategy.cloud.api.dto.field.FieldConfigDTO;
import net.qixiaowei.strategy.cloud.api.dto.field.FieldListConfigDTO;
import net.qixiaowei.strategy.cloud.api.vo.field.FieldListConfigVO;
import net.qixiaowei.strategy.cloud.api.vo.field.FieldListHeaderVO;
import net.qixiaowei.strategy.cloud.logic.manager.FieldConfigManager;
import net.qixiaowei.strategy.cloud.logic.manager.FieldListConfigManager;
import net.qixiaowei.strategy.cloud.mapper.field.FieldListConfigMapper;
import net.qixiaowei.strategy.cloud.service.field.IFieldConfigService;
import net.qixiaowei.strategy.cloud.service.field.IFieldListConfigService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


/**
 * FieldListConfigService业务层处理
 *
 * @author hzk
 * @since 2023-02-08
 */
@Service
public class FieldListConfigServiceImpl implements IFieldListConfigService {

    private static final Set<String> NEED_CONCAT = new HashSet<>();

    private static final Logger log = LoggerFactory.getLogger(FieldListConfigServiceImpl.class);

    static {
        // NEED_CONCAT.add(BusinessType.POST.getCode() + StrUtil.COLON + PostField.POST_RANK.getCode());

    }

    @Autowired
    private FieldListConfigMapper fieldListConfigMapper;
    @Autowired
    private IFieldConfigService fieldConfigService;

    @Autowired
    private FieldListConfigManager fieldListConfigManager;

    @Autowired
    private FieldConfigManager fieldConfigManager;

    @Autowired
    private ITenantService tenantService;

    /**
     * 查询字段列表配置表
     *
     * @param fieldListConfigId 字段列表配置表主键
     * @return 字段列表配置表
     */
    @Override
    public FieldListConfigDTO selectFieldListConfigByFieldListConfigId(Long fieldListConfigId) {
        return fieldListConfigMapper.selectFieldListConfigByFieldListConfigId(fieldListConfigId);
    }

    /**
     * 查询字段列表配置表列表
     *
     * @param businessType 业务类型
     * @return 字段列表配置表
     */
    @Override
    public List<FieldListHeaderVO> selectHeaderFieldListConfigList(Integer businessType) {
        Long userId = SecurityUtils.getUserId();
        Integer countFieldHeaderListOfBusinessTypeAndUserId = fieldListConfigMapper.countFieldHeaderListOfBusinessTypeAndUserId(businessType, userId);
        if (countFieldHeaderListOfBusinessTypeAndUserId == 0) {
            this.initUserFieldList(businessType, userId);
        }
        List<FieldListHeaderVO> fieldListHeaderVOS = fieldListConfigMapper.selectFieldHeaderListOfBusinessTypeAndUserId(businessType, userId);
        for (FieldListHeaderVO fieldListHeaderVO : fieldListHeaderVOS) {
            String fieldName = fieldListHeaderVO.getFieldName();
            fieldName = this.parseFieldName(fieldName, businessType);
            fieldListHeaderVO.setFieldName(fieldName);
        }
        return fieldListHeaderVOS;
    }

    /**
     * 查询字段列表配置表列表
     *
     * @param businessType 业务类型
     * @return 字段列表配置表
     */
    @Override
    public List<FieldListConfigVO> selectFieldListConfigList(Integer businessType) {
        Long userId = SecurityUtils.getUserId();
        List<FieldListConfigVO> fieldListConfigVOS = fieldListConfigMapper.selectFieldListConfigListOfBusinessTypeAndUserId(businessType, userId);
        for (FieldListConfigVO fieldListConfigVO : fieldListConfigVOS) {
            String fieldName = fieldListConfigVO.getFieldName();
            fieldName = this.parseFieldName(fieldName, businessType);
            fieldListConfigVO.setFieldName(fieldName);
        }
        return fieldListConfigVOS;
    }

    /**
     * 新增字段列表配置表
     *
     * @param fieldListConfigDTO 字段列表配置表
     * @return 结果
     */
    @Override
    public FieldListConfigDTO insertFieldListConfig(FieldListConfigDTO fieldListConfigDTO) {
        FieldListConfig fieldListConfig = new FieldListConfig();
        BeanUtils.copyProperties(fieldListConfigDTO, fieldListConfig);
        fieldListConfig.setCreateBy(SecurityUtils.getUserId());
        fieldListConfig.setCreateTime(DateUtils.getNowDate());
        fieldListConfig.setUpdateTime(DateUtils.getNowDate());
        fieldListConfig.setUpdateBy(SecurityUtils.getUserId());
        fieldListConfig.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        fieldListConfigMapper.insertFieldListConfig(fieldListConfig);
        fieldListConfigDTO.setFieldListConfigId(fieldListConfig.getFieldListConfigId());
        return fieldListConfigDTO;
    }

    /**
     * 修改字段列表配置表
     *
     * @param fieldListConfigDTO 字段列表配置表
     * @return 结果
     */
    @Override
    public int updateFieldListConfig(FieldListConfigDTO fieldListConfigDTO) {
        Long fieldListConfigId = fieldListConfigDTO.getFieldListConfigId();
        FieldListConfigDTO fieldListConfigOfDB = fieldListConfigMapper.selectFieldListConfigByFieldListConfigId(fieldListConfigId);
        if (StringUtils.isNull(fieldListConfigOfDB)) {
            throw new ServiceException("修改失败，找不到该字段列表配置。");
        }
        Long userId = SecurityUtils.getUserId();
        if (!userId.equals(fieldListConfigOfDB.getUserId())) {
            throw new ServiceException("修改失败，不允许修改其他用户字段列表配置。");
        }
        Date nowDate = DateUtils.getNowDate();
        FieldListConfig fieldListConfig = new FieldListConfig();
        fieldListConfig.setFieldListConfigId(fieldListConfigId);
        fieldListConfig.setFixationFlag(fieldListConfigDTO.getFixationFlag());
        fieldListConfig.setFieldWidth(fieldListConfigDTO.getFieldWidth());
        fieldListConfig.setUpdateTime(nowDate);
        fieldListConfig.setUpdateBy(userId);
        return fieldListConfigMapper.updateFieldListConfig(fieldListConfig);
    }

    /**
     * 逻辑批量删除字段列表配置表
     *
     * @param fieldListConfigIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteFieldListConfigByFieldListConfigIds(List<Long> fieldListConfigIds) {
        return fieldListConfigMapper.logicDeleteFieldListConfigByFieldListConfigIds(fieldListConfigIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除字段列表配置表信息
     *
     * @param fieldListConfigId 字段列表配置表主键
     * @return 结果
     */
    @Override
    public int deleteFieldListConfigByFieldListConfigId(Long fieldListConfigId) {
        return fieldListConfigMapper.deleteFieldListConfigByFieldListConfigId(fieldListConfigId);
    }

    /**
     * 表头信息修改
     *
     * @param businessType 业务类型
     * @return 结果
     */
    @IgnoreTenant
    @Transactional
    public int changeHead(Integer businessType) {
        if (StringUtils.isNull(businessType)) {
            throw new ServiceException("业务类型不能为空");
        }
        // 获得正常的租户列表
        List<Long> tenantIds = tenantService.getTenantIds();
        if (StringUtils.isEmpty(tenantIds)) {
            tenantIds = new ArrayList<>();
        }
        //加入租户管理平台
        tenantIds.add(0L);
        //循环租户执行
        Map<Long, String> results = new ConcurrentHashMap<>();
        tenantIds.forEach(tenantId -> {
            TenantUtils.execute(tenantId, () -> {
                try {
                    this.changeHeadData(businessType);
                } catch (Throwable e) {
                    results.put(tenantId, ExceptionUtil.getRootCauseMessage(e));
                    log.error(StrUtil.format("[多租户({}) 修改表头({})，发生异常：{}]",
                            tenantId, ExceptionUtils.getStackTrace(e)));
                }
            });
        });
        // 如果results非空，说明发生了异常，标记XXL-Job执行失败
        if (StringUtils.isNotEmpty(results)) {
            log.error(JSONUtil.toJsonStr(results));
        }
        return 1;
    }

    /**
     * 修改表头
     *
     * @param businessType 业务类型
     */
    public void changeHeadData(Integer businessType) {
        BusinessType businessTypeEnum = BusinessType.getBusinessType(businessType);
        if (StringUtils.isNull(businessTypeEnum)) {
            throw new ServiceException("业务类型错误");
        }
        // 表头字段层面
        List<FieldConfig> fieldConfigs = fieldConfigManager.initFieldConfig(businessType);
        List<FieldConfigDTO> fieldConfigDTOSAfter = new ArrayList<>();
        for (FieldConfig fieldConfig : fieldConfigs) {
            FieldConfigDTO fieldConfigDTO = new FieldConfigDTO();
            BeanUtils.copyProperties(fieldConfig, fieldConfigDTO);
            fieldConfigDTOSAfter.add(fieldConfigDTO);
        }
        if (StringUtils.isEmpty(fieldConfigDTOSAfter)) {
            return;
        }
        List<FieldConfigDTO> fieldConfigDTOSBefore = fieldConfigService.selectFieldConfigListOfBusinessType(businessType);
        // 初始化
        if (StringUtils.isEmpty(fieldConfigDTOSBefore) && StringUtils.isNotEmpty(fieldConfigDTOSAfter)) {
            initFieldConfig(businessType, fieldConfigDTOSAfter);
        }
        // 处理字段配置
        operateFieldConfig(fieldConfigDTOSAfter, fieldConfigDTOSBefore);
        // 处理用户层面
        operateFieldListConfig(businessType, fieldConfigDTOSAfter, fieldConfigDTOSBefore);
    }

    /**
     * 处理用户层面
     *
     * @param businessType          业务类新
     * @param fieldConfigDTOSAfter  字段配置-后
     * @param fieldConfigDTOSBefore 字段配置-前
     */
    private void operateFieldListConfig(Integer businessType, List<FieldConfigDTO> fieldConfigDTOSAfter, List<FieldConfigDTO> fieldConfigDTOSBefore) {
        List<FieldListConfig> fieldListConfigsAfter = fieldListConfigManager.initUserFieldListConfig(businessType, fieldConfigDTOSAfter);
        List<FieldListConfigDTO> fieldListConfigsDTOSAfter = new ArrayList<>();
        for (FieldListConfig fieldListConfig : fieldListConfigsAfter) {
            FieldListConfigDTO fieldListConfigDTO = new FieldListConfigDTO();
            BeanUtils.copyProperties(fieldListConfig, fieldListConfigDTO);
            fieldListConfigsDTOSAfter.add(fieldListConfigDTO);
        }
        List<Long> fieldConfigIds = fieldConfigDTOSBefore.stream().map(FieldConfigDTO::getFieldConfigId).collect(Collectors.toList());
        FieldListConfig fieldListConfig = new FieldListConfig();
        Map<String, Object> params = new HashMap<>();
        params.put("fieldConfigIds", fieldConfigIds);
        fieldListConfig.setParams(params);
        List<FieldListConfigDTO> fieldListConfigDTOSBeforeS = fieldListConfigMapper.selectFieldListConfigList(fieldListConfig);
        Map<Long, List<FieldListConfigDTO>> groupFieldListConfigDTOSBeforeS = fieldListConfigDTOSBeforeS.stream().collect(Collectors.groupingBy(FieldListConfigDTO::getUserId));
        List<FieldListConfigDTO> addFieldListConfigDTOS = new ArrayList<>();
        List<FieldListConfigDTO> delFieldListConfigDTOS = new ArrayList<>();
        for (Long userId : groupFieldListConfigDTOSBeforeS.keySet()) {
            List<FieldListConfigDTO> fieldListConfigDTOSBefore = groupFieldListConfigDTOSBeforeS.get(userId);
            delFieldListConfigDTOS.addAll(fieldListConfigDTOSBefore.stream().filter(f ->
                    !fieldListConfigsDTOSAfter.stream().map(FieldListConfigDTO::getFieldConfigId).collect(Collectors.toList()).contains(f.getFieldConfigId())).collect(Collectors.toList()));

            List<FieldListConfigDTO> addFieldListConfigsDTOSAfter = new ArrayList<>();
            for (FieldListConfigDTO fieldListConfigDTO : fieldListConfigsDTOSAfter) {
                List<Long> collect = fieldListConfigDTOSBefore.stream().map(FieldListConfigDTO::getFieldConfigId).collect(Collectors.toList());
                if (!collect.contains(fieldListConfigDTO.getFieldConfigId())) {
                    FieldListConfigDTO fieldListConfigDTO1 = new FieldListConfigDTO();
                    BeanUtils.copyProperties(fieldListConfigDTO, fieldListConfigDTO1);
                    fieldListConfigDTO1.setUserId(userId);
                    addFieldListConfigsDTOSAfter.add(fieldListConfigDTO1);
                }
            }
//            List<FieldListConfigDTO> addFieldListConfigsDTOSAfter = fieldListConfigsDTOSAfter.stream().filter(f ->
//                    !fieldListConfigDTOSBefore.stream().map(FieldListConfigDTO::getFieldConfigId).collect(Collectors.toList()).contains(f.getFieldConfigId())).collect(Collectors.toList());
            addFieldListConfigDTOS.addAll(addFieldListConfigsDTOSAfter);
        }
        if (StringUtils.isNotEmpty(delFieldListConfigDTOS)) {
            List<Long> delFieldListConfigIds = delFieldListConfigDTOS.stream().map(FieldListConfigDTO::getFieldListConfigId).collect(Collectors.toList());
            this.logicDeleteFieldListConfigByFieldListConfigIds(delFieldListConfigIds);
        }
        if (StringUtils.isNotEmpty(addFieldListConfigDTOS)) {
            addFieldListConfigDTOS.forEach(f -> f.setSort(0));
            this.insertFieldListConfigs(addFieldListConfigDTOS);
        }
    }

    /**
     * 处理字段配置
     *
     * @param fieldConfigDTOSAfter  字段配置-后
     * @param fieldConfigDTOSBefore 字段配置-前
     */
    private void operateFieldConfig(List<FieldConfigDTO> fieldConfigDTOSAfter, List<FieldConfigDTO> fieldConfigDTOSBefore) {
        for (FieldConfigDTO afterFieldConfigDTO : fieldConfigDTOSAfter) {
            for (FieldConfigDTO fieldConfigDTO : fieldConfigDTOSBefore) {
                if (afterFieldConfigDTO.getFieldName().equals(fieldConfigDTO.getFieldName())) {
                    afterFieldConfigDTO.setFieldConfigId(fieldConfigDTO.getFieldConfigId());
                    break;
                }
            }
        }
        List<FieldConfigDTO> delFieldConfigDTOS = fieldConfigDTOSBefore.stream().filter(f -> !fieldConfigDTOSAfter.stream().map(FieldConfigDTO::getFieldName)
                .collect(Collectors.toList()).contains(f.getFieldName())).collect(Collectors.toList());
        List<FieldConfigDTO> editFieldConfigDTOS = fieldConfigDTOSAfter.stream().filter(f -> fieldConfigDTOSBefore.stream().map(FieldConfigDTO::getFieldName)
                .collect(Collectors.toList()).contains(f.getFieldName())).collect(Collectors.toList());
        List<FieldConfigDTO> addFieldConfigDTOS = fieldConfigDTOSAfter.stream().filter(f -> !fieldConfigDTOSBefore.stream().map(FieldConfigDTO::getFieldName)
                .collect(Collectors.toList()).contains(f.getFieldName())).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(editFieldConfigDTOS)) {
            fieldConfigService.updateFieldConfigs(editFieldConfigDTOS);
        }
        if (StringUtils.isNotEmpty(delFieldConfigDTOS)) {
            List<Long> delFieldConfigIds = delFieldConfigDTOS.stream().map(FieldConfigDTO::getFieldConfigId).collect(Collectors.toList());
            fieldConfigService.logicDeleteFieldConfigByFieldConfigIds(delFieldConfigIds);
        }
        if (StringUtils.isNotEmpty(addFieldConfigDTOS)) {
            List<FieldConfig> fieldConfigsList = fieldConfigService.insertFieldConfigs(addFieldConfigDTOS);
            for (FieldConfigDTO afterFieldConfigDTO : fieldConfigDTOSAfter) {
                for (FieldConfig fieldConfig : fieldConfigsList) {
                    if (afterFieldConfigDTO.getFieldName().equals(fieldConfig.getFieldName())) {
                        afterFieldConfigDTO.setFieldConfigId(fieldConfig.getFieldConfigId());
                        break;
                    }
                }
            }
        }
    }

    /**
     * 初始化-情况较少
     *
     * @param businessType         业务类型
     * @param fieldConfigDTOSAfter 字段配置-后
     */
    private void initFieldConfig(Integer businessType, List<FieldConfigDTO> fieldConfigDTOSAfter) {
        List<FieldConfig> fieldConfigsList = fieldConfigService.insertFieldConfigs(fieldConfigDTOSAfter);
        for (FieldConfigDTO fieldConfigDTO : fieldConfigDTOSAfter) {
            for (FieldConfig fieldConfig : fieldConfigsList) {
                if (fieldConfigDTO.getFieldName().equals(fieldConfig.getFieldName())) {
                    fieldConfigDTO.setFieldConfigId(fieldConfig.getFieldConfigId());
                    break;
                }
            }
        }
        List<FieldListConfig> fieldListConfigsAfter = fieldListConfigManager.initUserFieldListConfig(businessType, fieldConfigDTOSAfter);
        List<FieldListConfigDTO> fieldListConfigsDTOSAfter = new ArrayList<>();
        for (FieldListConfig fieldListConfig : fieldListConfigsAfter) {
            FieldListConfigDTO fieldListConfigDTO = new FieldListConfigDTO();
            BeanUtils.copyProperties(fieldListConfig, fieldListConfigDTO);
            fieldListConfigsDTOSAfter.add(fieldListConfigDTO);
        }
        fieldListConfigsDTOSAfter.forEach(f -> f.setUserId(SecurityUtils.getUserId()));
        this.insertFieldListConfigs(fieldListConfigsDTOSAfter);
    }

    /**
     * 逻辑删除字段列表配置表信息
     *
     * @param fieldListConfigDTO 字段列表配置表
     * @return 结果
     */
    @Override
    public int logicDeleteFieldListConfigByFieldListConfigId(FieldListConfigDTO fieldListConfigDTO) {
        FieldListConfig fieldListConfig = new FieldListConfig();
        fieldListConfig.setFieldListConfigId(fieldListConfigDTO.getFieldListConfigId());
        fieldListConfig.setUpdateTime(DateUtils.getNowDate());
        fieldListConfig.setUpdateBy(SecurityUtils.getUserId());
        return fieldListConfigMapper.logicDeleteFieldListConfigByFieldListConfigId(fieldListConfig);
    }

    /**
     * 物理删除字段列表配置表信息
     *
     * @param fieldListConfigDTO 字段列表配置表
     * @return 结果
     */

    @Override
    public int deleteFieldListConfigByFieldListConfigId(FieldListConfigDTO fieldListConfigDTO) {
        FieldListConfig fieldListConfig = new FieldListConfig();
        BeanUtils.copyProperties(fieldListConfigDTO, fieldListConfig);
        return fieldListConfigMapper.deleteFieldListConfigByFieldListConfigId(fieldListConfig.getFieldListConfigId());
    }

    /**
     * 物理批量删除字段列表配置表
     *
     * @param fieldListConfigDtos 需要删除的字段列表配置表主键
     * @return 结果
     */

    @Override
    public int deleteFieldListConfigByFieldListConfigIds(List<FieldListConfigDTO> fieldListConfigDtos) {
        List<Long> stringList = new ArrayList();
        for (FieldListConfigDTO fieldListConfigDTO : fieldListConfigDtos) {
            stringList.add(fieldListConfigDTO.getFieldListConfigId());
        }
        return fieldListConfigMapper.deleteFieldListConfigByFieldListConfigIds(stringList);
    }

    /**
     * 批量新增字段列表配置表信息
     *
     * @param fieldListConfigDtos 字段列表配置表对象
     */

    @Override
    public int insertFieldListConfigs(List<FieldListConfigDTO> fieldListConfigDtos) {
        List<FieldListConfig> fieldListConfigList = new ArrayList();

        for (FieldListConfigDTO fieldListConfigDTO : fieldListConfigDtos) {
            FieldListConfig fieldListConfig = new FieldListConfig();
            BeanUtils.copyProperties(fieldListConfigDTO, fieldListConfig);
            fieldListConfig.setCreateBy(SecurityUtils.getUserId());
            fieldListConfig.setCreateTime(DateUtils.getNowDate());
            fieldListConfig.setUpdateTime(DateUtils.getNowDate());
            fieldListConfig.setUpdateBy(SecurityUtils.getUserId());
            fieldListConfig.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            fieldListConfigList.add(fieldListConfig);
        }
        return fieldListConfigMapper.batchFieldListConfig(fieldListConfigList);
    }

    /**
     * 批量修改字段列表配置表信息
     *
     * @param fieldListConfigDtos 字段列表配置表对象
     */

    @Override
    public int updateFieldListConfigs(List<FieldListConfigDTO> fieldListConfigDtos) {
        List<FieldListConfig> fieldListConfigList = new ArrayList<>();
        int sort = 0;
        for (FieldListConfigDTO fieldListConfigDTO : fieldListConfigDtos) {
            FieldListConfig fieldListConfig = new FieldListConfig();
            fieldListConfig.setFieldListConfigId(fieldListConfigDTO.getFieldListConfigId());
            fieldListConfig.setShowFlag(fieldListConfigDTO.getShowFlag());
            fieldListConfig.setFixationFlag(fieldListConfigDTO.getFixationFlag());
            fieldListConfig.setFieldWidth(fieldListConfigDTO.getFieldWidth());
            fieldListConfig.setSort(sort);
            fieldListConfig.setCreateBy(SecurityUtils.getUserId());
            fieldListConfig.setCreateTime(DateUtils.getNowDate());
            fieldListConfig.setUpdateTime(DateUtils.getNowDate());
            fieldListConfig.setUpdateBy(SecurityUtils.getUserId());
            fieldListConfigList.add(fieldListConfig);
            sort++;
        }
        return fieldListConfigMapper.updateFieldListConfigs(fieldListConfigList);
    }

    /**
     * @description: 格式化字段名称
     * @Author: hzk
     * @date: 2023/2/10 14:25
     * @param: [fieldName]
     * @return: java.lang.String
     **/
    private String parseFieldName(String fieldName, Integer businessType) {
        String _id = "_id";
        if (fieldName.endsWith(_id)) {
            fieldName = fieldName.substring(0, fieldName.lastIndexOf("_id")).concat("_name");
        }
        if (fieldName.endsWith(BaseField.CREATE_BY.getCode()) || NEED_CONCAT.contains(businessType + StrUtil.COLON + fieldName)) {
            fieldName = fieldName.concat("_name");
        }
        return StrUtil.toCamelCase(fieldName);
    }

    /**
     * @description: 初始化用户字段列表
     * @Author: hzk
     * @date: 2023/2/9 20:12
     * @param: [businessType, userId]
     * @return: void
     **/
    private void initUserFieldList(Integer businessType, Long userId) {
        //找到字段配置
        List<FieldConfigDTO> fieldConfigDTOS = fieldConfigService.selectFieldConfigListOfBusinessType(businessType);
        if (StringUtils.isEmpty(fieldConfigDTOS)) {
            return;
        }
        //初始化用户字段列表。
        List<FieldListConfig> fieldListConfigs = fieldListConfigManager.initUserFieldListConfig(businessType, fieldConfigDTOS);
        this.addFieldListConfig(userId, fieldListConfigs);
    }

    /**
     * @description: 新增字段列表配置
     * @Author: hzk
     * @date: 2023/2/13 11:52
     * @param: [userId, fieldListConfigs]
     * @return: void
     **/
    private void addFieldListConfig(Long userId, List<FieldListConfig> fieldListConfigs) {
        if (StringUtils.isNotEmpty(fieldListConfigs)) {
            Long userIdOfInsert = SecurityUtils.getUserId();
            Date nowDate = DateUtils.getNowDate();
            for (FieldListConfig fieldListConfig : fieldListConfigs) {
                fieldListConfig.setUserId(userId);
                fieldListConfig.setCreateBy(userIdOfInsert);
                fieldListConfig.setCreateTime(nowDate);
                fieldListConfig.setUpdateTime(nowDate);
                fieldListConfig.setUpdateBy(userIdOfInsert);
                fieldListConfig.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            }
            fieldListConfigMapper.batchFieldListConfig(fieldListConfigs);
        }
    }

}

