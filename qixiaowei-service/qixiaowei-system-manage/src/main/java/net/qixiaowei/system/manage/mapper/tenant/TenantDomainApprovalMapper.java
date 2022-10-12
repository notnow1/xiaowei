package net.qixiaowei.system.manage.mapper.tenant;

import java.util.List;
import net.qixiaowei.system.manage.api.domain.tenant.TenantDomainApproval;
import net.qixiaowei.system.manage.api.dto.tenant.TenantDomainApprovalDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* TenantDomainApprovalMapper接口
* @author TANGMICHI
* @since 2022-10-09
*/
public interface TenantDomainApprovalMapper{
    /**
    * 查询租户域名申请
    *
    * @param tenantDomainApprovalId 租户域名申请主键
    * @return 租户域名申请
    */
    TenantDomainApprovalDTO selectTenantDomainApprovalByTenantDomainApprovalId(@Param("tenantDomainApprovalId")Long tenantDomainApprovalId);


    /**
     * 根据租户id查询租户域名申请
     *
     * @param tenantId 租户域名申请
     * @return 租户域名申请
     */
    List<TenantDomainApprovalDTO> selectTenantDomainApprovalByTenantId(@Param("tenantId")Long tenantId);

    /**
    * 查询租户域名申请列表
    *
    * @param tenantDomainApproval 租户域名申请
    * @return 租户域名申请集合
    */
    List<TenantDomainApprovalDTO> selectTenantDomainApprovalList(@Param("tenantDomainApproval")TenantDomainApproval tenantDomainApproval);

    /**
    * 新增租户域名申请
    *
    * @param tenantDomainApproval 租户域名申请
    * @return 结果
    */
    int insertTenantDomainApproval(@Param("tenantDomainApproval")TenantDomainApproval tenantDomainApproval);

    /**
    * 修改租户域名申请
    *
    * @param tenantDomainApproval 租户域名申请
    * @return 结果
    */
    int updateTenantDomainApproval(@Param("tenantDomainApproval")TenantDomainApproval tenantDomainApproval);

    /**
    * 批量修改租户域名申请
    *
    * @param tenantDomainApprovalList 租户域名申请
    * @return 结果
    */
    int updateTenantDomainApprovals(@Param("tenantDomainApprovalList")List<TenantDomainApproval> tenantDomainApprovalList);
    /**
    * 逻辑删除租户域名申请
    *
    * @param tenantDomainApproval
    * @return 结果
    */
    int logicDeleteTenantDomainApprovalByTenantDomainApprovalId(@Param("tenantDomainApproval")TenantDomainApproval tenantDomainApproval);

    /**
    * 逻辑批量删除租户域名申请
    *
    * @param tenantDomainApprovalIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteTenantDomainApprovalByTenantDomainApprovalIds(@Param("tenantDomainApprovalIds")List<Long> tenantDomainApprovalIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除租户域名申请
    *
    * @param tenantDomainApprovalId 租户域名申请主键
    * @return 结果
    */
    int deleteTenantDomainApprovalByTenantDomainApprovalId(@Param("tenantDomainApprovalId")Long tenantDomainApprovalId);

    /**
    * 物理批量删除租户域名申请
    *
    * @param tenantDomainApprovalIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteTenantDomainApprovalByTenantDomainApprovalIds(@Param("tenantDomainApprovalIds")List<Long> tenantDomainApprovalIds);

    /**
    * 批量新增租户域名申请
    *
    * @param TenantDomainApprovals 租户域名申请列表
    * @return 结果
    */
    int batchTenantDomainApproval(@Param("tenantDomainApprovals")List<TenantDomainApproval> TenantDomainApprovals);
}
