package net.qixiaowei.system.manage.service.impl.tenant;

import java.util.List;
import net.qixiaowei.integration.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.system.manage.api.domain.tenant.TenantContacts;
import net.qixiaowei.system.manage.api.dto.tenant.TenantContactsDTO;
import net.qixiaowei.system.manage.mapper.tenant.TenantContactsMapper;
import net.qixiaowei.system.manage.service.tenant.ITenantContactsService;


/**
* TenantContactsService业务层处理
* @author TANGMICHI
* @since 2022-09-24
*/
@Service
public class TenantContactsServiceImpl implements ITenantContactsService{
    @Autowired
    private TenantContactsMapper tenantContactsMapper;

    /**
    * 查询租户联系人表
    *
    * @param tenantContactsId 租户联系人表主键
    * @return 租户联系人表
    */
    @Override
    public TenantContactsDTO selectTenantContactsByTenantContactsId(Long tenantContactsId)
    {
    return tenantContactsMapper.selectTenantContactsByTenantContactsId(tenantContactsId);
    }

    /**
    * 查询租户联系人表列表
    *
    * @param tenantContactsDTO 租户联系人表
    * @return 租户联系人表
    */
    @Override
    public List<TenantContactsDTO> selectTenantContactsList(TenantContactsDTO tenantContactsDTO)
    {
    TenantContacts tenantContacts=new TenantContacts();
    BeanUtils.copyProperties(tenantContactsDTO,tenantContacts);
    return tenantContactsMapper.selectTenantContactsList(tenantContacts);
    }

    /**
    * 新增租户联系人表
    *
    * @param tenantContactsDTO 租户联系人表
    * @return 结果
    */
    @Transactional
    @Override
    public int insertTenantContacts(TenantContactsDTO tenantContactsDTO){
    TenantContacts tenantContacts=new TenantContacts();
    BeanUtils.copyProperties(tenantContactsDTO,tenantContacts);
    tenantContacts.setCreateTime(DateUtils.getNowDate());
    tenantContacts.setUpdateTime(DateUtils.getNowDate());
    return tenantContactsMapper.insertTenantContacts(tenantContacts);
    }

    /**
    * 修改租户联系人表
    *
    * @param tenantContactsDTO 租户联系人表
    * @return 结果
    */
    @Transactional
    @Override
    public int updateTenantContacts(TenantContactsDTO tenantContactsDTO)
    {
    TenantContacts tenantContacts=new TenantContacts();
    BeanUtils.copyProperties(tenantContactsDTO,tenantContacts);
    tenantContacts.setCreateTime(DateUtils.getNowDate());
    tenantContacts.setUpdateTime(DateUtils.getNowDate());
    return tenantContactsMapper.updateTenantContacts(tenantContacts);
    }

    /**
    * 逻辑批量删除租户联系人表
    *
    * @param tenantContactsDtos 需要删除的租户联系人表主键
    * @return 结果
    */

    @Transactional
    @Override
    public int logicDeleteTenantContactsByTenantContactsIds(List<TenantContactsDTO> tenantContactsDtos){
        List<Long> stringList= new ArrayList();
        stringList = tenantContactsDtos.stream().map(TenantContactsDTO::getTenantContactsId).collect(Collectors.toList());
      return tenantContactsMapper.logicDeleteTenantContactsByTenantContactsIds(stringList,tenantContactsDtos.get(0).getUpdateBy(),DateUtils.getNowDate());
    }

    /**
    * 物理删除租户联系人表信息
    *
    * @param tenantContactsId 租户联系人表主键
    * @return 结果
    */

    @Transactional
    @Override
    public int deleteTenantContactsByTenantContactsId(Long tenantContactsId)
    {
    return tenantContactsMapper.deleteTenantContactsByTenantContactsId(tenantContactsId);
    }

     /**
     * 逻辑删除租户联系人表信息
     *
     * @param  tenantContactsDTO 租户联系人表
     * @return 结果
     */
     @Transactional
     @Override
     public int logicDeleteTenantContactsByTenantContactsId(TenantContactsDTO tenantContactsDTO)
     {
     TenantContacts tenantContacts=new TenantContacts();
     BeanUtils.copyProperties(tenantContactsDTO,tenantContacts);
     return tenantContactsMapper.logicDeleteTenantContactsByTenantContactsId(tenantContacts);
     }

     /**
     * 物理删除租户联系人表信息
     *
     * @param  tenantContactsDTO 租户联系人表
     * @return 结果
     */
     @Transactional
     @Override
     public int deleteTenantContactsByTenantContactsId(TenantContactsDTO tenantContactsDTO)
     {
     TenantContacts tenantContacts=new TenantContacts();
     BeanUtils.copyProperties(tenantContactsDTO,tenantContacts);
     return tenantContactsMapper.deleteTenantContactsByTenantContactsId(tenantContacts.getTenantContactsId());
     }
     /**
     * 物理批量删除租户联系人表
     *
     * @param tenantContactsDtos 需要删除的租户联系人表主键
     * @return 结果
     */
     @Transactional
     @Override
     public int deleteTenantContactsByTenantContactsIds(List<TenantContactsDTO> tenantContactsDtos){
     List<Long> stringList = new ArrayList();
     for (TenantContactsDTO tenantContactsDTO : tenantContactsDtos) {
     stringList.add(tenantContactsDTO.getTenantContactsId());
     }
     return tenantContactsMapper.deleteTenantContactsByTenantContactsIds(stringList,tenantContactsDtos.get(0).getUpdateBy(),DateUtils.getNowDate());
     }

    /**
    * 批量新增租户联系人表信息
    *
    * @param tenantContactsDtos 租户联系人表对象
    */
    @Transactional
    public int insertTenantContactss(List<TenantContactsDTO> tenantContactsDtos){
      List<TenantContacts> tenantContactsList = new ArrayList();

    for (TenantContactsDTO tenantContactsDTO : tenantContactsDtos) {
      TenantContacts tenantContacts =new TenantContacts();
      BeanUtils.copyProperties(tenantContactsDTO,tenantContacts);
    tenantContacts.setCreateTime(DateUtils.getNowDate());
    tenantContacts.setUpdateTime(DateUtils.getNowDate());
      tenantContactsList.add(tenantContacts);
    }
    return tenantContactsMapper.batchTenantContacts(tenantContactsList);
    }

    /**
    * 批量修改租户联系人表信息
    *
    * @param tenantContactsDtos 租户联系人表对象
    */
    @Transactional
    public int updateTenantContactss(List<TenantContactsDTO> tenantContactsDtos){
     List<TenantContacts> tenantContactsList = new ArrayList();

     for (TenantContactsDTO tenantContactsDTO : tenantContactsDtos) {
     TenantContacts tenantContacts =new TenantContacts();
     BeanUtils.copyProperties(tenantContactsDTO,tenantContacts);
     tenantContacts.setCreateTime(DateUtils.getNowDate());
     tenantContacts.setUpdateTime(DateUtils.getNowDate());
     tenantContactsList.add(tenantContacts);
     }
     return tenantContactsMapper.updateTenantContactss(tenantContactsList);
    }
}

