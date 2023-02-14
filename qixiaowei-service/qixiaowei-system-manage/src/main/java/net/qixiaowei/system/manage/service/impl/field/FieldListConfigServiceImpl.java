package net.qixiaowei.system.manage.service.impl.field;

import java.util.*;

import cn.hutool.core.util.StrUtil;
import net.qixiaowei.integration.common.enums.field.system.EmployeeField;
import net.qixiaowei.integration.common.enums.field.system.PostField;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.system.manage.api.dto.field.FieldConfigDTO;
import net.qixiaowei.system.manage.api.vo.field.FieldListConfigVO;
import net.qixiaowei.system.manage.api.vo.field.FieldListHeaderVO;
import net.qixiaowei.system.manage.logic.manager.FieldListConfigManager;
import net.qixiaowei.system.manage.service.field.IFieldConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.domain.field.FieldListConfig;
import net.qixiaowei.system.manage.api.dto.field.FieldListConfigDTO;
import net.qixiaowei.system.manage.mapper.field.FieldListConfigMapper;
import net.qixiaowei.system.manage.service.field.IFieldListConfigService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


/**
 * FieldListConfigService业务层处理
 *
 * @author hzk
 * @since 2023-02-08
 */
@Service
public class FieldListConfigServiceImpl implements IFieldListConfigService {

    private static final Set<String> NEED_CONCAT = new HashSet<>();

    static {
        NEED_CONCAT.add(BusinessType.POST.getCode() + StrUtil.COLON + PostField.POST_RANK.getCode());
        NEED_CONCAT.add(BusinessType.POST.getCode() + StrUtil.COLON + PostField.POST_RANK_LOWER.getCode());
        NEED_CONCAT.add(BusinessType.POST.getCode() + StrUtil.COLON + PostField.POST_RANK_UPPER.getCode());
        NEED_CONCAT.add(BusinessType.EMPLOYEE.getCode() + StrUtil.COLON + EmployeeField.NATIONALITY.getCode());
        NEED_CONCAT.add(BusinessType.EMPLOYEE.getCode() + StrUtil.COLON + EmployeeField.NATION.getCode());
        NEED_CONCAT.add(BusinessType.EMPLOYEE.getCode() + StrUtil.COLON + EmployeeField.EMPLOYEE_RANK.getCode());
    }

    @Autowired
    private FieldListConfigMapper fieldListConfigMapper;
    @Autowired
    private IFieldConfigService fieldConfigService;

    @Autowired
    private FieldListConfigManager fieldListConfigManager;

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
        if (NEED_CONCAT.contains(businessType + StrUtil.COLON + fieldName)) {
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

