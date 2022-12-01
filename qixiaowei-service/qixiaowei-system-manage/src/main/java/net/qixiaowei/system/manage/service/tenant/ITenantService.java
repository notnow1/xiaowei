package net.qixiaowei.system.manage.service.tenant;

import java.util.List;
import net.qixiaowei.system.manage.api.dto.tenant.TenantDTO;
import net.qixiaowei.system.manage.excel.tenant.TenantExcel;


/**
* TenantService接口
* @author TANGMICHI
* @since 2022-09-24
*/
public interface ITenantService{
    /**
    * 查询租户表
    *
    * @param tenantId 租户表主键
    * @return 租户表
    */
    TenantDTO selectTenantByTenantId(Long tenantId);

    /**
     * 查询企业信息
     *
     * @param
     * @return 租户表
     */
    TenantDTO selectTenantByTenantId();
    /**
    * 查询租户表列表
    *
    * @param tenantDTO 租户表
    * @return 租户表集合
    */
    List<TenantDTO> selectTenantList(TenantDTO tenantDTO);

    /**
    * 新增租户表
    *
    * @param tenantDTO 租户表
    * @return 结果
    */
    TenantDTO insertTenant(TenantDTO tenantDTO);


    /**
     * 导入Excel
     *
     * @param tenantExcels 租户表
     * @return 结果
     */
    void insertTenant(List<TenantExcel> tenantExcels);

    /**
    * 修改租户表
    *
    * @param tenantDTO 租户表
    * @return 结果
    */
    int updateTenant(TenantDTO tenantDTO);

    /**
    * 批量修改租户表
    *
    * @param tenantDtos 租户表
    * @return 结果
    */
    int updateTenants(List<TenantDTO> tenantDtos);

    /**
    * 批量新增租户表
    *
    * @param tenantDtos 租户表
    * @return 结果
    */
    int insertTenants(List<TenantDTO> tenantDtos);

    /**
    * 逻辑批量删除租户表
    *
    * @param TenantDtos 需要删除的租户表集合
    * @return 结果
    */
    int logicDeleteTenantByTenantIds(List<TenantDTO> TenantDtos);

    /**
    * 逻辑删除租户表信息
    *
    * @param tenantDTO
    * @return 结果
    */
    int logicDeleteTenantByTenantId(TenantDTO tenantDTO);
    /**
    * 逻辑批量删除租户表
    *
    * @param TenantDtos 需要删除的租户表集合
    * @return 结果
    */
    int deleteTenantByTenantIds(List<TenantDTO> TenantDtos);

    /**
    * 逻辑删除租户表信息
    *
    * @param tenantDTO
    * @return 结果
    */
    int deleteTenantByTenantId(TenantDTO tenantDTO);


    /**
    * 删除租户表信息
    *
    * @param tenantId 租户表主键
    * @return 结果
    */
    int deleteTenantByTenantId(Long tenantId);

    /**
     * 修改单个租户
     * @param tenantDTO
     * @return
     */
    int updateMyTenantDTO(TenantDTO tenantDTO);

    /**
     * 修改企业信息
     * @return
     */
    int updateMyTenant(TenantDTO tenantDTO);

    /**
     * 导出Excel
     * @param tenantDTO
     * @return
     */
    List<TenantExcel> exportTenant(TenantDTO tenantDTO);

}
