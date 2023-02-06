package net.qixiaowei.system.manage.service.tenant;

import java.util.List;
import java.util.Set;

import net.qixiaowei.system.manage.api.dto.tenant.TenantContractAuthDTO;


/**
 * TenantContractAuthService接口
 *
 * @author hzk
 * @since 2023-01-31
 */
public interface ITenantContractAuthService {


    /**
     * 新增合同授权菜单信息
     *
     * @param tenantContractId 租户合同ID
     * @param menuIds          菜单集合
     */
    void insertTenantContractAuth(Long tenantContractId, Set<Long> menuIds);

    /**
     * 查询租户合同授权表
     *
     * @param tenantContractAuthId 租户合同授权表主键
     * @return 租户合同授权表
     */
    TenantContractAuthDTO selectTenantContractAuthByTenantContractAuthId(Long tenantContractAuthId);

    /**
     * 更新合同授权菜单信息
     *
     * @param tenantContractId 租户合同ID
     * @param menuIds          菜单集合
     */
     void updateTenantContractAuth(Long tenantContractId, Set<Long> menuIds);

    /**
     * 查询租户合同授权表列表
     *
     * @param tenantContractAuthDTO 租户合同授权表
     * @return 租户合同授权表集合
     */
    List<TenantContractAuthDTO> selectTenantContractAuthList(TenantContractAuthDTO tenantContractAuthDTO);

    /**
     * 根据租户合同ID查询租户合同授权表列表
     *
     * @param tenantContractId 租户合同ID
     * @return 租户合同授权表集合
     */
    List<TenantContractAuthDTO> selectTenantContractAuthsByTenantContractId(Long tenantContractId);

    /**
     * 根据租户合同ID查询租户合同授权菜单集合
     *
     * @param tenantContractId 租户合同ID
     * @return 租户合同授权菜单集合
     */
    Set<Long> selectTenantContractAuthMenuIdsByTenantContractId(Long tenantContractId);

    /**
     * 新增租户合同授权表
     *
     * @param tenantContractAuthDTO 租户合同授权表
     * @return 结果
     */
    TenantContractAuthDTO insertTenantContractAuth(TenantContractAuthDTO tenantContractAuthDTO);

    /**
     * 修改租户合同授权表
     *
     * @param tenantContractAuthDTO 租户合同授权表
     * @return 结果
     */
    int updateTenantContractAuth(TenantContractAuthDTO tenantContractAuthDTO);

    /**
     * 批量修改租户合同授权表
     *
     * @param tenantContractAuthDtos 租户合同授权表
     * @return 结果
     */
    int updateTenantContractAuths(List<TenantContractAuthDTO> tenantContractAuthDtos);

    /**
     * 批量新增租户合同授权表
     *
     * @param tenantContractAuthDtos 租户合同授权表
     * @return 结果
     */
    int insertTenantContractAuths(List<TenantContractAuthDTO> tenantContractAuthDtos);

    /**
     * 逻辑批量删除租户合同授权表
     *
     * @param tenantContractAuthIds 需要删除的租户合同授权表集合
     * @return 结果
     */
    int logicDeleteTenantContractAuthByTenantContractAuthIds(List<Long> tenantContractAuthIds);

    /**
     * 逻辑删除租户合同授权表信息
     *
     * @param tenantContractAuthDTO
     * @return 结果
     */
    int logicDeleteTenantContractAuthByTenantContractAuthId(TenantContractAuthDTO tenantContractAuthDTO);

    /**
     * 批量删除租户合同授权表
     *
     * @param TenantContractAuthDtos
     * @return 结果
     */
    int deleteTenantContractAuthByTenantContractAuthIds(List<TenantContractAuthDTO> TenantContractAuthDtos);

    /**
     * 逻辑删除租户合同授权表信息
     *
     * @param tenantContractAuthDTO
     * @return 结果
     */
    int deleteTenantContractAuthByTenantContractAuthId(TenantContractAuthDTO tenantContractAuthDTO);


    /**
     * 删除租户合同授权表信息
     *
     * @param tenantContractAuthId 租户合同授权表主键
     * @return 结果
     */
    int deleteTenantContractAuthByTenantContractAuthId(Long tenantContractAuthId);

}
