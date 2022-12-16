package net.qixiaowei.system.manage.service.impl.basic;

import java.util.List;
import net.qixiaowei.integration.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.domain.basic.DictionaryType;
import net.qixiaowei.system.manage.api.dto.basic.DictionaryTypeDTO;
import net.qixiaowei.system.manage.mapper.basic.DictionaryTypeMapper;
import net.qixiaowei.system.manage.service.basic.IDictionaryTypeService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


/**
* DictionaryTypeService业务层处理
* @author TANGMICHI
* @since 2022-10-15
*/
@Service
public class DictionaryTypeServiceImpl implements IDictionaryTypeService{
    @Autowired
    private DictionaryTypeMapper dictionaryTypeMapper;

    /**
    * 查询字典类型表
    *
    * @param dictionaryTypeId 字典类型表主键
    * @return 字典类型表
    */
    @Override
    public DictionaryTypeDTO selectDictionaryTypeByDictionaryTypeId(Long dictionaryTypeId)
    {
    return dictionaryTypeMapper.selectDictionaryTypeByDictionaryTypeId(dictionaryTypeId);
    }

    /**
     * 根据type类型查询字典类型表
     * @param dictionaryType 字典类型表主键
     * @return
     */
    @Override
    public DictionaryTypeDTO selectDictionaryTypeByDictionaryType(String dictionaryType) {
        return dictionaryTypeMapper.selectDictionaryTypeByDictionaryType(dictionaryType);
    }

    /**
    * 查询字典类型表列表
    *
    * @param dictionaryTypeDTO 字典类型表
    * @return 字典类型表
    */
    @Override
    public List<DictionaryTypeDTO> selectDictionaryTypeList(DictionaryTypeDTO dictionaryTypeDTO)
    {
    DictionaryType dictionaryType=new DictionaryType();
    BeanUtils.copyProperties(dictionaryTypeDTO,dictionaryType);
    return dictionaryTypeMapper.selectDictionaryTypeList(dictionaryType);
    }

    /**
    * 新增字典类型表
    *
    * @param dictionaryTypeDTO 字典类型表
    * @return 结果
    */
    @Override
    public DictionaryTypeDTO insertDictionaryType(DictionaryTypeDTO dictionaryTypeDTO){
    DictionaryType dictionaryType=new DictionaryType();
    BeanUtils.copyProperties(dictionaryTypeDTO,dictionaryType);
    dictionaryType.setCreateBy(SecurityUtils.getUserId());
    dictionaryType.setCreateTime(DateUtils.getNowDate());
    dictionaryType.setUpdateTime(DateUtils.getNowDate());
    dictionaryType.setUpdateBy(SecurityUtils.getUserId());
    dictionaryType.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        dictionaryTypeMapper.insertDictionaryType(dictionaryType);
        dictionaryTypeDTO.setDictionaryTypeId(dictionaryType.getDictionaryTypeId());
    return dictionaryTypeDTO;
    }

    /**
    * 修改字典类型表
    *
    * @param dictionaryTypeDTO 字典类型表
    * @return 结果
    */
    @Override
    public int updateDictionaryType(DictionaryTypeDTO dictionaryTypeDTO)
    {
    DictionaryType dictionaryType=new DictionaryType();
    BeanUtils.copyProperties(dictionaryTypeDTO,dictionaryType);
    dictionaryType.setUpdateTime(DateUtils.getNowDate());
    dictionaryType.setUpdateBy(SecurityUtils.getUserId());
    return dictionaryTypeMapper.updateDictionaryType(dictionaryType);
    }

    /**
    * 逻辑批量删除字典类型表
    *
    * @param dictionaryTypeIds 主键集合
    * @return 结果
    */
    @Override
    public int logicDeleteDictionaryTypeByDictionaryTypeIds(List<Long> dictionaryTypeIds){
    return dictionaryTypeMapper.logicDeleteDictionaryTypeByDictionaryTypeIds(dictionaryTypeIds,SecurityUtils.getUserId(),DateUtils.getNowDate());
    }

    /**
    * 物理删除字典类型表信息
    *
    * @param dictionaryTypeId 字典类型表主键
    * @return 结果
    */
    @Override
    public int deleteDictionaryTypeByDictionaryTypeId(Long dictionaryTypeId)
    {
    return dictionaryTypeMapper.deleteDictionaryTypeByDictionaryTypeId(dictionaryTypeId);
    }

     /**
     * 逻辑删除字典类型表信息
     *
     * @param  dictionaryTypeDTO 字典类型表
     * @return 结果
     */
     @Override
     public int logicDeleteDictionaryTypeByDictionaryTypeId(DictionaryTypeDTO dictionaryTypeDTO)
     {
     DictionaryType dictionaryType=new DictionaryType();
     dictionaryType.setDictionaryTypeId(dictionaryTypeDTO.getDictionaryTypeId());
     dictionaryType.setUpdateTime(DateUtils.getNowDate());
     dictionaryType.setUpdateBy(SecurityUtils.getUserId());
     return dictionaryTypeMapper.logicDeleteDictionaryTypeByDictionaryTypeId(dictionaryType);
     }

     /**
     * 物理删除字典类型表信息
     *
     * @param  dictionaryTypeDTO 字典类型表
     * @return 结果
     */
     
     @Override
     public int deleteDictionaryTypeByDictionaryTypeId(DictionaryTypeDTO dictionaryTypeDTO)
     {
     DictionaryType dictionaryType=new DictionaryType();
     BeanUtils.copyProperties(dictionaryTypeDTO,dictionaryType);
     return dictionaryTypeMapper.deleteDictionaryTypeByDictionaryTypeId(dictionaryType.getDictionaryTypeId());
     }
     /**
     * 物理批量删除字典类型表
     *
     * @param dictionaryTypeDtos 需要删除的字典类型表主键
     * @return 结果
     */
     
     @Override
     public int deleteDictionaryTypeByDictionaryTypeIds(List<DictionaryTypeDTO> dictionaryTypeDtos){
     List<Long> stringList = new ArrayList();
     for (DictionaryTypeDTO dictionaryTypeDTO : dictionaryTypeDtos) {
     stringList.add(dictionaryTypeDTO.getDictionaryTypeId());
     }
     return dictionaryTypeMapper.deleteDictionaryTypeByDictionaryTypeIds(stringList);
     }

    /**
    * 批量新增字典类型表信息
    *
    * @param dictionaryTypeDtos 字典类型表对象
    */
    
    public int insertDictionaryTypes(List<DictionaryTypeDTO> dictionaryTypeDtos){
      List<DictionaryType> dictionaryTypeList = new ArrayList();

    for (DictionaryTypeDTO dictionaryTypeDTO : dictionaryTypeDtos) {
      DictionaryType dictionaryType =new DictionaryType();
      BeanUtils.copyProperties(dictionaryTypeDTO,dictionaryType);
       dictionaryType.setCreateBy(SecurityUtils.getUserId());
       dictionaryType.setCreateTime(DateUtils.getNowDate());
       dictionaryType.setUpdateTime(DateUtils.getNowDate());
       dictionaryType.setUpdateBy(SecurityUtils.getUserId());
       dictionaryType.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
      dictionaryTypeList.add(dictionaryType);
    }
    return dictionaryTypeMapper.batchDictionaryType(dictionaryTypeList);
    }

    /**
    * 批量修改字典类型表信息
    *
    * @param dictionaryTypeDtos 字典类型表对象
    */
    
    public int updateDictionaryTypes(List<DictionaryTypeDTO> dictionaryTypeDtos){
     List<DictionaryType> dictionaryTypeList = new ArrayList();

     for (DictionaryTypeDTO dictionaryTypeDTO : dictionaryTypeDtos) {
     DictionaryType dictionaryType =new DictionaryType();
     BeanUtils.copyProperties(dictionaryTypeDTO,dictionaryType);
        dictionaryType.setCreateBy(SecurityUtils.getUserId());
        dictionaryType.setCreateTime(DateUtils.getNowDate());
        dictionaryType.setUpdateTime(DateUtils.getNowDate());
        dictionaryType.setUpdateBy(SecurityUtils.getUserId());
     dictionaryTypeList.add(dictionaryType);
     }
     return dictionaryTypeMapper.updateDictionaryTypes(dictionaryTypeList);
    }
}

