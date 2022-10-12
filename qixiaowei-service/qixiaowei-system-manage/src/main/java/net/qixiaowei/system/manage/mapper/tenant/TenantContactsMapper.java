package net.qixiaowei.system.manage.mapper.tenant;

import java.util.List;
import net.qixiaowei.system.manage.api.domain.tenant.TenantContacts;
import net.qixiaowei.system.manage.api.dto.tenant.TenantContactsDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* TenantContactsMapper接口
* @author TANGMICHI
* @since 2022-10-09
*/
public interface TenantContactsMapper{
    /**
    * 查询租户联系人表
    *
    * @param tenantContactsId 租户联系人表主键
    * @return 租户联系人表
    */
    TenantContactsDTO selectTenantContactsByTenantContactsId(@Param("tenantContactsId")Long tenantContactsId);

    /**
     * 根据租户id查询租户联系人表
     *
     * @param tenantId 租户联系人表
     * @return 租户联系人表
     */
    List<TenantContactsDTO> selectTenantContactsByTenantId(@Param("tenantId")Long tenantId);

    /**
    * 查询租户联系人表列表
    *
    * @param tenantContacts 租户联系人表
    * @return 租户联系人表集合
    */
    List<TenantContactsDTO> selectTenantContactsList(@Param("tenantContacts")TenantContacts tenantContacts);

    /**
    * 新增租户联系人表
    *
    * @param tenantContacts 租户联系人表
    * @return 结果
    */
    int insertTenantContacts(@Param("tenantContacts")TenantContacts tenantContacts);

    /**
    * 修改租户联系人表
    *
    * @param tenantContacts 租户联系人表
    * @return 结果
    */
    int updateTenantContacts(@Param("tenantContacts")TenantContacts tenantContacts);

    /**
    * 批量修改租户联系人表
    *
    * @param tenantContactsList 租户联系人表
    * @return 结果
    */
    int updateTenantContactss(@Param("tenantContactsList")List<TenantContacts> tenantContactsList);
    /**
    * 逻辑删除租户联系人表
    *
    * @param tenantContacts
    * @return 结果
    */
    int logicDeleteTenantContactsByTenantContactsId(@Param("tenantContacts")TenantContacts tenantContacts);

    /**
    * 逻辑批量删除租户联系人表
    *
    * @param tenantContactsIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteTenantContactsByTenantContactsIds(@Param("tenantContactsIds")List<Long> tenantContactsIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除租户联系人表
    *
    * @param tenantContactsId 租户联系人表主键
    * @return 结果
    */
    int deleteTenantContactsByTenantContactsId(@Param("tenantContactsId")Long tenantContactsId);

    /**
    * 物理批量删除租户联系人表
    *
    * @param tenantContactsIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteTenantContactsByTenantContactsIds(@Param("tenantContactsIds")List<Long> tenantContactsIds);

    /**
    * 批量新增租户联系人表
    *
    * @param TenantContactss 租户联系人表列表
    * @return 结果
    */
    int batchTenantContacts(@Param("tenantContactss")List<TenantContacts> TenantContactss);
}
