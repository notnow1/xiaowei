package net.qixiaowei.system.manage.service.tenant;

import java.util.List;
import net.qixiaowei.system.manage.api.dto.tenant.TenantContractDTO;


/**
* TenantContractService接口
* @author TANGMICHI
* @since 2022-09-24
*/
public interface ITenantContractService{
    /**
    * 查询租户合同信息
    *
    * @param tenantContractId 租户合同信息主键
    * @return 租户合同信息
    */
    TenantContractDTO selectTenantContractByTenantContractId(Long tenantContractId);

    /**
    * 查询租户合同信息列表
    *
    * @param tenantContractDTO 租户合同信息
    * @return 租户合同信息集合
    */
    List<TenantContractDTO> selectTenantContractList(TenantContractDTO tenantContractDTO);

    /**
    * 新增租户合同信息
    *
    * @param tenantContractDTO 租户合同信息
    * @return 结果
    */
    int insertTenantContract(TenantContractDTO tenantContractDTO);

    /**
    * 修改租户合同信息
    *
    * @param tenantContractDTO 租户合同信息
    * @return 结果
    */
    int updateTenantContract(TenantContractDTO tenantContractDTO);

    /**
    * 批量修改租户合同信息
    *
    * @param tenantContractDtos 租户合同信息
    * @return 结果
    */
    int updateTenantContracts(List<TenantContractDTO> tenantContractDtos);

    /**
    * 批量新增租户合同信息
    *
    * @param tenantContractDtos 租户合同信息
    * @return 结果
    */
    int insertTenantContracts(List<TenantContractDTO> tenantContractDtos);

    /**
    * 逻辑批量删除租户合同信息
    *
    * @param TenantContractDtos 需要删除的租户合同信息集合
    * @return 结果
    */
    int logicDeleteTenantContractByTenantContractIds(List<TenantContractDTO> TenantContractDtos);

    /**
    * 逻辑删除租户合同信息信息
    *
    * @param tenantContractDTO
    * @return 结果
    */
    int logicDeleteTenantContractByTenantContractId(TenantContractDTO tenantContractDTO);
    /**
    * 逻辑批量删除租户合同信息
    *
    * @param TenantContractDtos 需要删除的租户合同信息集合
    * @return 结果
    */
    int deleteTenantContractByTenantContractIds(List<TenantContractDTO> TenantContractDtos);

    /**
    * 逻辑删除租户合同信息信息
    *
    * @param tenantContractDTO
    * @return 结果
    */
    int deleteTenantContractByTenantContractId(TenantContractDTO tenantContractDTO);


    /**
    * 删除租户合同信息信息
    *
    * @param tenantContractId 租户合同信息主键
    * @return 结果
    */
    int deleteTenantContractByTenantContractId(Long tenantContractId);
}
