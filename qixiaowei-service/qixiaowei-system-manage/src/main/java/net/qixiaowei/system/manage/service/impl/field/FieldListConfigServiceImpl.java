package net.qixiaowei.system.manage.service.impl.field;

import java.util.Date;
import java.util.List;

import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.system.manage.api.vo.field.FieldListConfigVO;
import net.qixiaowei.system.manage.api.vo.field.FieldListHeaderVO;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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
    @Autowired
    private FieldListConfigMapper fieldListConfigMapper;

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
        return fieldListConfigMapper.selectFieldHeaderListOfBusinessTypeAndUserId(businessType, userId);
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
        return fieldListConfigMapper.selectFieldListConfigListOfBusinessTypeAndUserId(businessType, userId);
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

}

