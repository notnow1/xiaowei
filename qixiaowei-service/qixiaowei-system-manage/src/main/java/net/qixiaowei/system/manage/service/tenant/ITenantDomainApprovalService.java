package net.qixiaowei.system.manage.service.tenant;

import java.util.List;
import net.qixiaowei.system.manage.api.dto.tenant.TenantDomainApprovalDTO;


/**
* TenantDomainApprovalService接口
* @author TANGMICHI
* @since 2022-09-24
*/
public interface ITenantDomainApprovalService{
    /**
    * 查询租户域名申请
    *
    * @param tenantDomainApprovalId 租户域名申请主键
    * @return 租户域名申请
    */
    TenantDomainApprovalDTO selectTenantDomainApprovalByTenantDomainApprovalId(Long tenantDomainApprovalId);

    /**
    * 查询租户域名申请列表
    *
    * @param tenantDomainApprovalDTO 租户域名申请
    * @return 租户域名申请集合
    */
    List<TenantDomainApprovalDTO> selectTenantDomainApprovalList(TenantDomainApprovalDTO tenantDomainApprovalDTO);

    /**
    * 新增租户域名申请
    *
    * @param tenantDomainApprovalDTO 租户域名申请
    * @return 结果
    */
    int insertTenantDomainApproval(TenantDomainApprovalDTO tenantDomainApprovalDTO);

    /**
    * 修改租户域名申请
    *
    * @param tenantDomainApprovalDTO 租户域名申请
    * @return 结果
    */
    int updateTenantDomainApproval(TenantDomainApprovalDTO tenantDomainApprovalDTO);

    /**
    * 批量修改租户域名申请
    *
    * @param tenantDomainApprovalDtos 租户域名申请
    * @return 结果
    */
    int updateTenantDomainApprovals(List<TenantDomainApprovalDTO> tenantDomainApprovalDtos);

    /**
    * 批量新增租户域名申请
    *
    * @param tenantDomainApprovalDtos 租户域名申请
    * @return 结果
    */
    int insertTenantDomainApprovals(List<TenantDomainApprovalDTO> tenantDomainApprovalDtos);

    /**
    * 逻辑批量删除租户域名申请
    *
    * @param TenantDomainApprovalDtos 需要删除的租户域名申请集合
    * @return 结果
    */
    int logicDeleteTenantDomainApprovalByTenantDomainApprovalIds(List<TenantDomainApprovalDTO> TenantDomainApprovalDtos);

    /**
    * 逻辑删除租户域名申请信息
    *
    * @param tenantDomainApprovalDTO
    * @return 结果
    */
    int logicDeleteTenantDomainApprovalByTenantDomainApprovalId(TenantDomainApprovalDTO tenantDomainApprovalDTO);
    /**
    * 逻辑批量删除租户域名申请
    *
    * @param TenantDomainApprovalDtos 需要删除的租户域名申请集合
    * @return 结果
    */
    int deleteTenantDomainApprovalByTenantDomainApprovalIds(List<TenantDomainApprovalDTO> TenantDomainApprovalDtos);

    /**
    * 逻辑删除租户域名申请信息
    *
    * @param tenantDomainApprovalDTO
    * @return 结果
    */
    int deleteTenantDomainApprovalByTenantDomainApprovalId(TenantDomainApprovalDTO tenantDomainApprovalDTO);


    /**
    * 删除租户域名申请信息
    *
    * @param tenantDomainApprovalId 租户域名申请主键
    * @return 结果
    */
    int deleteTenantDomainApprovalByTenantDomainApprovalId(Long tenantDomainApprovalId);
}
