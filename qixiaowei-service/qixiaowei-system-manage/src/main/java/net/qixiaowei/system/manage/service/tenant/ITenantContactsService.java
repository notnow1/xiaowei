package net.qixiaowei.system.manage.service.tenant;

import java.util.List;
import net.qixiaowei.system.manage.api.dto.tenant.TenantContactsDTO;


/**
* TenantContactsService接口
* @author TANGMICHI
* @since 2022-10-09
*/
public interface ITenantContactsService{
    /**
    * 查询租户联系人表
    *
    * @param tenantContactsId 租户联系人表主键
    * @return 租户联系人表
    */
    TenantContactsDTO selectTenantContactsByTenantContactsId(Long tenantContactsId);

    /**
    * 查询租户联系人表列表
    *
    * @param tenantContactsDTO 租户联系人表
    * @return 租户联系人表集合
    */
    List<TenantContactsDTO> selectTenantContactsList(TenantContactsDTO tenantContactsDTO);

    /**
    * 新增租户联系人表
    *
    * @param tenantContactsDTO 租户联系人表
    * @return 结果
    */
    int insertTenantContacts(TenantContactsDTO tenantContactsDTO);

    /**
    * 修改租户联系人表
    *
    * @param tenantContactsDTO 租户联系人表
    * @return 结果
    */
    int updateTenantContacts(TenantContactsDTO tenantContactsDTO);

    /**
    * 批量修改租户联系人表
    *
    * @param tenantContactsDtos 租户联系人表
    * @return 结果
    */
    int updateTenantContactss(List<TenantContactsDTO> tenantContactsDtos);

    /**
    * 批量新增租户联系人表
    *
    * @param tenantContactsDtos 租户联系人表
    * @return 结果
    */
    int insertTenantContactss(List<TenantContactsDTO> tenantContactsDtos);

    /**
    * 逻辑批量删除租户联系人表
    *
    * @param TenantContactsDtos 需要删除的租户联系人表集合
    * @return 结果
    */
    int logicDeleteTenantContactsByTenantContactsIds(List<TenantContactsDTO> TenantContactsDtos);

    /**
    * 逻辑删除租户联系人表信息
    *
    * @param tenantContactsDTO
    * @return 结果
    */
    int logicDeleteTenantContactsByTenantContactsId(TenantContactsDTO tenantContactsDTO);
    /**
    * 逻辑批量删除租户联系人表
    *
    * @param TenantContactsDtos 需要删除的租户联系人表集合
    * @return 结果
    */
    int deleteTenantContactsByTenantContactsIds(List<TenantContactsDTO> TenantContactsDtos);

    /**
    * 逻辑删除租户联系人表信息
    *
    * @param tenantContactsDTO
    * @return 结果
    */
    int deleteTenantContactsByTenantContactsId(TenantContactsDTO tenantContactsDTO);


    /**
    * 删除租户联系人表信息
    *
    * @param tenantContactsId 租户联系人表主键
    * @return 结果
    */
    int deleteTenantContactsByTenantContactsId(Long tenantContactsId);
}
