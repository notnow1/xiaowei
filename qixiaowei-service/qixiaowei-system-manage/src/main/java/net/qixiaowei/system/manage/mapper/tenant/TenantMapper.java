package net.qixiaowei.system.manage.mapper.tenant;

import java.util.Date;
import java.util.List;
import net.qixiaowei.system.manage.api.domain.tenant.Tenant;
import net.qixiaowei.system.manage.api.dto.tenant.TenantDTO;
import org.apache.ibatis.annotations.Param;


/**
* TenantMapper接口
* @author TANGMICHI
* @since 2022-09-26
*/
public interface TenantMapper{
    /**
    * 查询租户表
    *
    * @param tenantId 租户表主键
    * @return 租户表
    */
    TenantDTO selectTenantByTenantId(@Param("tenantId")Long tenantId);

    /**
     * 根据域名查询租户表
     *
     * @param domain 二级域名
     * @return 租户表
     */
    TenantDTO selectTenantByDomain(@Param("domain")String domain);

    /**
     * 根据租户编码查询租户表
     *
     * @param tenantCode 租户编码
     * @return 租户表
     */
    TenantDTO selectTenantByTenantCode(@Param("tenantCode")String tenantCode);

    /**
    * 查询租户表列表
    *
    * @param tenant 租户表
    * @return 租户表集合
    */
    List<TenantDTO> selectTenantList(@Param("tenant")Tenant tenant);


    /**
     * 查询有效的租户ID集合
     *
     * @return 租户ID集合
     */
    List<Long> getTenantIds();

    /**
    * 新增租户表
    *
    * @param tenant 租户表
    * @return 结果
    */
    int insertTenant(@Param("tenant")Tenant tenant);

    /**
    * 修改租户表
    *
    * @param tenant 租户表
    * @return 结果
    */
    int updateTenant(@Param("tenant")Tenant tenant);

    /**
    * 批量修改租户表
    *
    * @param tenantList 租户表
    * @return 结果
    */
    int updateTenants(@Param("tenantList")List<Tenant> tenantList);
    /**
    * 逻辑删除租户表
    *
    * @param tenant
    * @return 结果
    */
    int logicDeleteTenantByTenantId(@Param("tenant")Tenant tenant);

    /**
    * 逻辑批量删除租户表
    *
    * @param tenantIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteTenantByTenantIds(@Param("tenantIds")List<Long> tenantIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除租户表
    *
    * @param tenantId 租户表主键
    * @return 结果
    */
    int deleteTenantByTenantId(@Param("tenantId")Long tenantId);

    /**
    * 物理批量删除租户表
    *
    * @param tenantIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteTenantByTenantIds(@Param("tenantIds")List<Long> tenantIds,@Param("updateBy")Long updateBy,@Param("updateTime") Date updateTime);

    /**
    * 批量新增租户表
    *
    * @param Tenants 租户表列表
    * @return 结果
    */
    int batchTenant(@Param("tenants")List<Tenant> Tenants);
}
