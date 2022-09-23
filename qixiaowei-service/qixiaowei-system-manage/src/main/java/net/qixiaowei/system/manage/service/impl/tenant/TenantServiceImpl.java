package net.qixiaowei.system.manage.service.impl.tenant;

import java.util.List;
import net.qixiaowei.integration.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import net.qixiaowei.integration.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.system.manage.api.domain.tenant.Tenant;
import net.qixiaowei.system.manage.api.dto.tenant.TenantDTO;
import net.qixiaowei.system.manage.mapper.tenant.TenantMapper;
import net.qixiaowei.system.manage.service.tenant.ITenantService;


/**
* TenantService业务层处理
* @author TMC
* @since 2022-09-23
*/
@Service
public class TenantServiceImpl implements ITenantService{
    @Autowired
    private TenantMapper tenantMapper;

    /**
    * 查询租户表
    *
    * @param tenantId 租户表主键
    * @return 租户表
    */
    @Override
    public TenantDTO selectTenantByTenantId(Long tenantId)
    {
    return tenantMapper.selectTenantByTenantId(tenantId);
    }

    /**
    * 查询租户表列表
    *
    * @param tenantDTO 租户表
    * @return 租户表
    */
    @Override
    public List<TenantDTO> selectTenantList(TenantDTO tenantDTO)
    {
    Tenant tenant=new Tenant();
    BeanUtils.copyProperties(tenantDTO,tenant);
    return tenantMapper.selectTenantList(tenant);
    }

    /**
    * 新增租户表
    *
    * @param tenantDTO 租户表
    * @return 结果
    */
    @Transactional
    @Override
    public int insertTenant(TenantDTO tenantDTO){
    Tenant tenant=new Tenant();
    BeanUtils.copyProperties(tenantDTO,tenant);
    tenant.setCreateBy(tenant.getCreateBy());
    tenant.setUpdateBy(tenant.getUpdateBy());
    tenant.setCreateTime(DateUtils.getNowDate());
    tenant.setUpdateTime(DateUtils.getNowDate());
    return tenantMapper.insertTenant(tenant);
    }

    /**
    * 修改租户表
    *
    * @param tenantDTO 租户表
    * @return 结果
    */
    @Transactional
    @Override
    public int updateTenant(TenantDTO tenantDTO)
    {
    Tenant tenant=new Tenant();
    BeanUtils.copyProperties(tenantDTO,tenant);
    tenant.setCreateBy(tenant.getCreateBy());
    tenant.setUpdateBy(tenant.getUpdateBy());
    tenant.setCreateTime(DateUtils.getNowDate());
    tenant.setUpdateTime(DateUtils.getNowDate());
    return tenantMapper.updateTenant(tenant);
    }

    /**
    * 批量删除租户表
    *
    * @param tenantDtos 需要删除的租户表主键
    * @return 结果
    */

    @Transactional
    @Override
    public int deleteTenantByTenantIds(List<TenantDTO> tenantDtos){
            List<Long> stringList = new ArrayList();
            for (TenantDTO tenantDTO : tenantDtos) {
                stringList.add(tenantDTO.getTenantId());
            }
    return tenantMapper.deleteTenantByTenantIds(stringList);
    }

    /**
    * 删除租户表信息
    *
    * @param tenantId 租户表主键
    * @return 结果
    */

    @Transactional
    @Override
    public int deleteTenantByTenantId(Long tenantId)
    {
    return tenantMapper.deleteTenantByTenantId(tenantId);
    }

     /**
     * 删除租户表信息
     *
     * @param  tenantDTO 租户表
     * @return 结果
     */
     @Transactional
     @Override
     public int deleteTenantByTenantId(TenantDTO tenantDTO)
     {
     Tenant tenant=new Tenant();
     BeanUtils.copyProperties(tenantDTO,tenant);
     return tenantMapper.deleteTenantByTenantId(tenant.getTenantId());
     }

    /**
    * 批量新增租户表信息
    *
    * @param tenantDtos 租户表对象
    */
    @Transactional
    public int insertTenants(List<TenantDTO> tenantDtos){
      List<Tenant> tenantList = new ArrayList();

    for (TenantDTO tenantDTO : tenantDtos) {
      Tenant tenant =new Tenant();
      BeanUtils.copyProperties(tenantDTO,tenant);
    tenant.setCreateBy(tenant.getCreateBy());
    tenant.setUpdateBy(tenant.getUpdateBy());
    tenant.setCreateTime(DateUtils.getNowDate());
    tenant.setUpdateTime(DateUtils.getNowDate());
      tenantList.add(tenant);
    }
    return tenantMapper.batchTenant(tenantList);
    }

    /**
    * 批量修改租户表信息
    *
    * @param tenantDtos 租户表对象
    */
    @Transactional
    public int updateTenants(List<TenantDTO> tenantDtos){
    int num = 0;
    List<Tenant> tenantList = new ArrayList();
    for (TenantDTO tenantDTO : tenantDtos) {
       Tenant tenant =new Tenant();
       BeanUtils.copyProperties(tenantDTO,tenant);
    tenant.setCreateBy(tenant.getCreateBy());
    tenant.setUpdateBy(tenant.getUpdateBy());
    tenant.setCreateTime(DateUtils.getNowDate());
    tenant.setUpdateTime(DateUtils.getNowDate());
       tenantList.add(tenant);
       num=num+tenantMapper.updateTenant(tenant);
    }
    return num;
    }
}

