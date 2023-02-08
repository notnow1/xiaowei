package net.qixiaowei.system.manage.service.impl.field;

import java.util.List;
import net.qixiaowei.integration.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.domain.field.FieldConfig;
import net.qixiaowei.system.manage.api.dto.field.FieldConfigDTO;
import net.qixiaowei.system.manage.mapper.field.FieldConfigMapper;
import net.qixiaowei.system.manage.service.field.IFieldConfigService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


/**
* FieldConfigService业务层处理
* @author hzk
* @since 2023-02-08
*/
@Service
public class FieldConfigServiceImpl implements IFieldConfigService{
    @Autowired
    private FieldConfigMapper fieldConfigMapper;

    /**
    * 查询字段配置表
    *
    * @param fieldConfigId 字段配置表主键
    * @return 字段配置表
    */
    @Override
    public FieldConfigDTO selectFieldConfigByFieldConfigId(Long fieldConfigId)
    {
    return fieldConfigMapper.selectFieldConfigByFieldConfigId(fieldConfigId);
    }

    /**
    * 查询字段配置表列表
    *
    * @param fieldConfigDTO 字段配置表
    * @return 字段配置表
    */
    @Override
    public List<FieldConfigDTO> selectFieldConfigList(FieldConfigDTO fieldConfigDTO)
    {
    FieldConfig fieldConfig=new FieldConfig();
    BeanUtils.copyProperties(fieldConfigDTO,fieldConfig);
    return fieldConfigMapper.selectFieldConfigList(fieldConfig);
    }

    /**
    * 新增字段配置表
    *
    * @param fieldConfigDTO 字段配置表
    * @return 结果
    */
    @Override
    public FieldConfigDTO insertFieldConfig(FieldConfigDTO fieldConfigDTO){
    FieldConfig fieldConfig=new FieldConfig();
    BeanUtils.copyProperties(fieldConfigDTO,fieldConfig);
    fieldConfig.setCreateBy(SecurityUtils.getUserId());
    fieldConfig.setCreateTime(DateUtils.getNowDate());
    fieldConfig.setUpdateTime(DateUtils.getNowDate());
    fieldConfig.setUpdateBy(SecurityUtils.getUserId());
    fieldConfig.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
    fieldConfigMapper.insertFieldConfig(fieldConfig);
    fieldConfigDTO.setFieldConfigId(fieldConfig.getFieldConfigId());
    return fieldConfigDTO;
    }

    /**
    * 修改字段配置表
    *
    * @param fieldConfigDTO 字段配置表
    * @return 结果
    */
    @Override
    public int updateFieldConfig(FieldConfigDTO fieldConfigDTO)
    {
    FieldConfig fieldConfig=new FieldConfig();
    BeanUtils.copyProperties(fieldConfigDTO,fieldConfig);
    fieldConfig.setUpdateTime(DateUtils.getNowDate());
    fieldConfig.setUpdateBy(SecurityUtils.getUserId());
    return fieldConfigMapper.updateFieldConfig(fieldConfig);
    }

    /**
    * 逻辑批量删除字段配置表
    *
    * @param fieldConfigIds 主键集合
    * @return 结果
    */
    @Override
    public int logicDeleteFieldConfigByFieldConfigIds(List<Long> fieldConfigIds){
    return fieldConfigMapper.logicDeleteFieldConfigByFieldConfigIds(fieldConfigIds,SecurityUtils.getUserId(),DateUtils.getNowDate());
    }

    /**
    * 物理删除字段配置表信息
    *
    * @param fieldConfigId 字段配置表主键
    * @return 结果
    */
    @Override
    public int deleteFieldConfigByFieldConfigId(Long fieldConfigId)
    {
    return fieldConfigMapper.deleteFieldConfigByFieldConfigId(fieldConfigId);
    }

     /**
     * 逻辑删除字段配置表信息
     *
     * @param  fieldConfigDTO 字段配置表
     * @return 结果
     */
     @Override
     public int logicDeleteFieldConfigByFieldConfigId(FieldConfigDTO fieldConfigDTO)
     {
     FieldConfig fieldConfig=new FieldConfig();
     fieldConfig.setFieldConfigId(fieldConfigDTO.getFieldConfigId());
     fieldConfig.setUpdateTime(DateUtils.getNowDate());
     fieldConfig.setUpdateBy(SecurityUtils.getUserId());
     return fieldConfigMapper.logicDeleteFieldConfigByFieldConfigId(fieldConfig);
     }

     /**
     * 物理删除字段配置表信息
     *
     * @param  fieldConfigDTO 字段配置表
     * @return 结果
     */
     
     @Override
     public int deleteFieldConfigByFieldConfigId(FieldConfigDTO fieldConfigDTO)
     {
     FieldConfig fieldConfig=new FieldConfig();
     BeanUtils.copyProperties(fieldConfigDTO,fieldConfig);
     return fieldConfigMapper.deleteFieldConfigByFieldConfigId(fieldConfig.getFieldConfigId());
     }
     /**
     * 物理批量删除字段配置表
     *
     * @param fieldConfigDtos 需要删除的字段配置表主键
     * @return 结果
     */
     
     @Override
     public int deleteFieldConfigByFieldConfigIds(List<FieldConfigDTO> fieldConfigDtos){
     List<Long> stringList = new ArrayList();
     for (FieldConfigDTO fieldConfigDTO : fieldConfigDtos) {
     stringList.add(fieldConfigDTO.getFieldConfigId());
     }
     return fieldConfigMapper.deleteFieldConfigByFieldConfigIds(stringList);
     }

    /**
    * 批量新增字段配置表信息
    *
    * @param fieldConfigDtos 字段配置表对象
    */
    
    public int insertFieldConfigs(List<FieldConfigDTO> fieldConfigDtos){
      List<FieldConfig> fieldConfigList = new ArrayList();

    for (FieldConfigDTO fieldConfigDTO : fieldConfigDtos) {
      FieldConfig fieldConfig =new FieldConfig();
      BeanUtils.copyProperties(fieldConfigDTO,fieldConfig);
       fieldConfig.setCreateBy(SecurityUtils.getUserId());
       fieldConfig.setCreateTime(DateUtils.getNowDate());
       fieldConfig.setUpdateTime(DateUtils.getNowDate());
       fieldConfig.setUpdateBy(SecurityUtils.getUserId());
       fieldConfig.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
      fieldConfigList.add(fieldConfig);
    }
    return fieldConfigMapper.batchFieldConfig(fieldConfigList);
    }

    /**
    * 批量修改字段配置表信息
    *
    * @param fieldConfigDtos 字段配置表对象
    */
    
    public int updateFieldConfigs(List<FieldConfigDTO> fieldConfigDtos){
     List<FieldConfig> fieldConfigList = new ArrayList();

     for (FieldConfigDTO fieldConfigDTO : fieldConfigDtos) {
     FieldConfig fieldConfig =new FieldConfig();
     BeanUtils.copyProperties(fieldConfigDTO,fieldConfig);
        fieldConfig.setCreateBy(SecurityUtils.getUserId());
        fieldConfig.setCreateTime(DateUtils.getNowDate());
        fieldConfig.setUpdateTime(DateUtils.getNowDate());
        fieldConfig.setUpdateBy(SecurityUtils.getUserId());
     fieldConfigList.add(fieldConfig);
     }
     return fieldConfigMapper.updateFieldConfigs(fieldConfigList);
    }

}

