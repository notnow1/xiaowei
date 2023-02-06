package net.qixiaowei.system.manage.mapper.tenant;

import java.util.List;

import net.qixiaowei.system.manage.api.domain.tenant.TenantContractAuth;
import net.qixiaowei.system.manage.api.dto.tenant.TenantContractAuthDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.Set;


/**
 * TenantContractAuthMapper接口
 *
 * @author hzk
 * @since 2023-01-31
 */
public interface TenantContractAuthMapper {
    /**
     * 查询租户合同授权表
     *
     * @param tenantContractAuthId 租户合同授权表主键
     * @return 租户合同授权表
     */
    TenantContractAuthDTO selectTenantContractAuthByTenantContractAuthId(@Param("tenantContractAuthId") Long tenantContractAuthId);


    /**
     * 查询租户合同授权表
     *
     * @param tenantContractId 租户合同ID
     * @return 租户合同授权表
     */
    List<TenantContractAuthDTO> selectTenantContractAuthByTenantContractId(@Param("tenantContractId") Long tenantContractId);

    /**
     * 查询租户合同授权菜单集合
     *
     * @param tenantContractId 租户合同ID
     * @return 租户合同授权菜单ID
     */
    Set<Long> selectTenantContractAuthMenuIdsByTenantContractId(@Param("tenantContractId") Long tenantContractId);


    /**
     * 批量查询租户合同授权表
     *
     * @param tenantContractAuthIds 租户合同授权表主键集合
     * @return 租户合同授权表
     */
    List<TenantContractAuthDTO> selectTenantContractAuthByTenantContractAuthIds(@Param("tenantContractAuthIds") List<Long> tenantContractAuthIds);

    /**
     * 查询租户合同授权表列表
     *
     * @param tenantContractAuth 租户合同授权表
     * @return 租户合同授权表集合
     */
    List<TenantContractAuthDTO> selectTenantContractAuthList(@Param("tenantContractAuth") TenantContractAuth tenantContractAuth);

    /**
     * 新增租户合同授权表
     *
     * @param tenantContractAuth 租户合同授权表
     * @return 结果
     */
    int insertTenantContractAuth(@Param("tenantContractAuth") TenantContractAuth tenantContractAuth);

    /**
     * 修改租户合同授权表
     *
     * @param tenantContractAuth 租户合同授权表
     * @return 结果
     */
    int updateTenantContractAuth(@Param("tenantContractAuth") TenantContractAuth tenantContractAuth);

    /**
     * 批量修改租户合同授权表
     *
     * @param tenantContractAuthList 租户合同授权表
     * @return 结果
     */
    int updateTenantContractAuths(@Param("tenantContractAuthList") List<TenantContractAuth> tenantContractAuthList);

    /**
     * 逻辑删除租户合同授权表
     *
     * @param tenantContractAuth
     * @return 结果
     */
    int logicDeleteTenantContractAuthByTenantContractAuthId(@Param("tenantContractAuth") TenantContractAuth tenantContractAuth);

    /**
     * 逻辑批量删除租户合同授权表
     *
     * @param tenantContractAuthIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteTenantContractAuthByTenantContractAuthIds(@Param("tenantContractAuthIds") List<Long> tenantContractAuthIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除租户合同授权表
     *
     * @param tenantContractAuthId 租户合同授权表主键
     * @return 结果
     */
    int deleteTenantContractAuthByTenantContractAuthId(@Param("tenantContractAuthId") Long tenantContractAuthId);

    /**
     * 物理批量删除租户合同授权表
     *
     * @param tenantContractAuthIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteTenantContractAuthByTenantContractAuthIds(@Param("tenantContractAuthIds") List<Long> tenantContractAuthIds);

    /**
     * 批量新增租户合同授权表
     *
     * @param TenantContractAuths 租户合同授权表列表
     * @return 结果
     */
    int batchTenantContractAuth(@Param("tenantContractAuths") List<TenantContractAuth> TenantContractAuths);
}
