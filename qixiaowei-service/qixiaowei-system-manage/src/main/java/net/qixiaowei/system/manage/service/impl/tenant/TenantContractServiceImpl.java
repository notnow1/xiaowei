package net.qixiaowei.system.manage.service.impl.tenant;

import java.util.List;
import net.qixiaowei.integration.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.domain.tenant.TenantContract;
import net.qixiaowei.system.manage.api.dto.tenant.TenantContractDTO;
import net.qixiaowei.system.manage.mapper.tenant.TenantContractMapper;
import net.qixiaowei.system.manage.service.tenant.ITenantContractService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


/**
* TenantContractService业务层处理
* @author TANGMICHI
* @since 2022-10-09
*/
@Service
public class TenantContractServiceImpl implements ITenantContractService{
    @Autowired
    private TenantContractMapper tenantContractMapper;

    /**
    * 查询租户合同信息
    *
    * @param tenantContractId 租户合同信息主键
    * @return 租户合同信息
    */
    @Override
    public TenantContractDTO selectTenantContractByTenantContractId(Long tenantContractId)
    {
    return tenantContractMapper.selectTenantContractByTenantContractId(tenantContractId);
    }

    /**
    * 查询租户合同信息列表
    *
    * @param tenantContractDTO 租户合同信息
    * @return 租户合同信息
    */
    @Override
    public List<TenantContractDTO> selectTenantContractList(TenantContractDTO tenantContractDTO)
    {
    TenantContract tenantContract=new TenantContract();
    BeanUtils.copyProperties(tenantContractDTO,tenantContract);
    return tenantContractMapper.selectTenantContractList(tenantContract);
    }

    /**
    * 新增租户合同信息
    *
    * @param tenantContractDTO 租户合同信息
    * @return 结果
    */
    @Override
    public int insertTenantContract(TenantContractDTO tenantContractDTO){
    TenantContract tenantContract=new TenantContract();
    BeanUtils.copyProperties(tenantContractDTO,tenantContract);
    tenantContract.setCreateBy(SecurityUtils.getUserId());
    tenantContract.setCreateTime(DateUtils.getNowDate());
    tenantContract.setUpdateTime(DateUtils.getNowDate());
    tenantContract.setUpdateBy(SecurityUtils.getUserId());
    tenantContract.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
    return tenantContractMapper.insertTenantContract(tenantContract);
    }

    /**
    * 修改租户合同信息
    *
    * @param tenantContractDTO 租户合同信息
    * @return 结果
    */
    @Override
    public int updateTenantContract(TenantContractDTO tenantContractDTO)
    {
    TenantContract tenantContract=new TenantContract();
    BeanUtils.copyProperties(tenantContractDTO,tenantContract);
    tenantContract.setUpdateTime(DateUtils.getNowDate());
    tenantContract.setUpdateBy(SecurityUtils.getUserId());
    return tenantContractMapper.updateTenantContract(tenantContract);
    }

    /**
    * 逻辑批量删除租户合同信息
    *
    * @param tenantContractDtos 需要删除的租户合同信息主键
    * @return 结果
    */
    @Override
    public int logicDeleteTenantContractByTenantContractIds(List<TenantContractDTO> tenantContractDtos){
            List<Long> stringList = new ArrayList();
            for (TenantContractDTO tenantContractDTO : tenantContractDtos) {
                stringList.add(tenantContractDTO.getTenantContractId());
            }
    return tenantContractMapper.logicDeleteTenantContractByTenantContractIds(stringList,tenantContractDtos.get(0).getUpdateBy(),DateUtils.getNowDate());
    }

    /**
    * 物理删除租户合同信息信息
    *
    * @param tenantContractId 租户合同信息主键
    * @return 结果
    */
    @Override
    public int deleteTenantContractByTenantContractId(Long tenantContractId)
    {
    return tenantContractMapper.deleteTenantContractByTenantContractId(tenantContractId);
    }

     /**
     * 逻辑删除租户合同信息信息
     *
     * @param  tenantContractDTO 租户合同信息
     * @return 结果
     */
     @Override
     public int logicDeleteTenantContractByTenantContractId(TenantContractDTO tenantContractDTO)
     {
     TenantContract tenantContract=new TenantContract();
     tenantContract.setTenantContractId(tenantContractDTO.getTenantContractId());
     tenantContract.setUpdateTime(DateUtils.getNowDate());
     tenantContract.setUpdateBy(SecurityUtils.getUserId());
     return tenantContractMapper.logicDeleteTenantContractByTenantContractId(tenantContract);
     }

     /**
     * 物理删除租户合同信息信息
     *
     * @param  tenantContractDTO 租户合同信息
     * @return 结果
     */
     
     @Override
     public int deleteTenantContractByTenantContractId(TenantContractDTO tenantContractDTO)
     {
     TenantContract tenantContract=new TenantContract();
     BeanUtils.copyProperties(tenantContractDTO,tenantContract);
     return tenantContractMapper.deleteTenantContractByTenantContractId(tenantContract.getTenantContractId());
     }
     /**
     * 物理批量删除租户合同信息
     *
     * @param tenantContractDtos 需要删除的租户合同信息主键
     * @return 结果
     */
     
     @Override
     public int deleteTenantContractByTenantContractIds(List<TenantContractDTO> tenantContractDtos){
     List<Long> stringList = new ArrayList();
     for (TenantContractDTO tenantContractDTO : tenantContractDtos) {
     stringList.add(tenantContractDTO.getTenantContractId());
     }
     return tenantContractMapper.deleteTenantContractByTenantContractIds(stringList);
     }

    /**
    * 批量新增租户合同信息信息
    *
    * @param tenantContractDtos 租户合同信息对象
    */
    
    public int insertTenantContracts(List<TenantContractDTO> tenantContractDtos){
      List<TenantContract> tenantContractList = new ArrayList();

    for (TenantContractDTO tenantContractDTO : tenantContractDtos) {
      TenantContract tenantContract =new TenantContract();
      BeanUtils.copyProperties(tenantContractDTO,tenantContract);
       tenantContract.setCreateBy(SecurityUtils.getUserId());
       tenantContract.setCreateTime(DateUtils.getNowDate());
       tenantContract.setUpdateTime(DateUtils.getNowDate());
       tenantContract.setUpdateBy(SecurityUtils.getUserId());
       tenantContract.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
      tenantContractList.add(tenantContract);
    }
    return tenantContractMapper.batchTenantContract(tenantContractList);
    }

    /**
    * 批量修改租户合同信息信息
    *
    * @param tenantContractDtos 租户合同信息对象
    */
    
    public int updateTenantContracts(List<TenantContractDTO> tenantContractDtos){
     List<TenantContract> tenantContractList = new ArrayList();

     for (TenantContractDTO tenantContractDTO : tenantContractDtos) {
     TenantContract tenantContract =new TenantContract();
     BeanUtils.copyProperties(tenantContractDTO,tenantContract);
        tenantContract.setCreateBy(SecurityUtils.getUserId());
        tenantContract.setCreateTime(DateUtils.getNowDate());
        tenantContract.setUpdateTime(DateUtils.getNowDate());
        tenantContract.setUpdateBy(SecurityUtils.getUserId());
     tenantContractList.add(tenantContract);
     }
     return tenantContractMapper.updateTenantContracts(tenantContractList);
    }
}

