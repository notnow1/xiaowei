package net.qixiaowei.system.manage.mapper.tenant;

import java.util.List;
import net.qixiaowei.system.manage.api.domain.tenant.TenantContract;
import net.qixiaowei.system.manage.api.dto.tenant.TenantContractDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* TenantContractMapper接口
* @author TANGMICHI
* @since 2022-10-09
*/
public interface TenantContractMapper{
    /**
    * 查询租户合同信息
    *
    * @param tenantContractId 租户合同信息主键
    * @return 租户合同信息
    */
    TenantContractDTO selectTenantContractByTenantContractId(@Param("tenantContractId")Long tenantContractId);

    /**
     * 根据租户id查询租户合同信息
     *
     * @param tenantId 租户合同信息
     * @return 租户合同信息
     */
    List<TenantContractDTO> selectTenantContractByTenantId(@Param("tenantId")Long tenantId);

    /**
    * 查询租户合同信息列表
    *
    * @param tenantContract 租户合同信息
    * @return 租户合同信息集合
    */
    List<TenantContractDTO> selectTenantContractList(@Param("tenantContract")TenantContract tenantContract);

    /**
    * 新增租户合同信息
    *
    * @param tenantContract 租户合同信息
    * @return 结果
    */
    int insertTenantContract(@Param("tenantContract")TenantContract tenantContract);

    /**
    * 修改租户合同信息
    *
    * @param tenantContract 租户合同信息
    * @return 结果
    */
    int updateTenantContract(@Param("tenantContract")TenantContract tenantContract);

    /**
    * 批量修改租户合同信息
    *
    * @param tenantContractList 租户合同信息
    * @return 结果
    */
    int updateTenantContracts(@Param("tenantContractList")List<TenantContract> tenantContractList);
    /**
    * 逻辑删除租户合同信息
    *
    * @param tenantContract
    * @return 结果
    */
    int logicDeleteTenantContractByTenantContractId(@Param("tenantContract")TenantContract tenantContract);

    /**
    * 逻辑批量删除租户合同信息
    *
    * @param tenantContractIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteTenantContractByTenantContractIds(@Param("tenantContractIds")List<Long> tenantContractIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除租户合同信息
    *
    * @param tenantContractId 租户合同信息主键
    * @return 结果
    */
    int deleteTenantContractByTenantContractId(@Param("tenantContractId")Long tenantContractId);

    /**
    * 物理批量删除租户合同信息
    *
    * @param tenantContractIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteTenantContractByTenantContractIds(@Param("tenantContractIds")List<Long> tenantContractIds);

    /**
    * 批量新增租户合同信息
    *
    * @param TenantContracts 租户合同信息列表
    * @return 结果
    */
    int batchTenantContract(@Param("tenantContracts")List<TenantContract> TenantContracts);
}
