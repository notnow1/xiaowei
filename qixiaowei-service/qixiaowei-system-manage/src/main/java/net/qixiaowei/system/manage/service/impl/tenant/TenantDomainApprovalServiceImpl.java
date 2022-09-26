package net.qixiaowei.system.manage.service.impl.tenant;

import java.util.List;
import net.qixiaowei.integration.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.system.manage.api.domain.tenant.TenantDomainApproval;
import net.qixiaowei.system.manage.api.dto.tenant.TenantDomainApprovalDTO;
import net.qixiaowei.system.manage.mapper.tenant.TenantDomainApprovalMapper;
import net.qixiaowei.system.manage.service.tenant.ITenantDomainApprovalService;


/**
* TenantDomainApprovalService业务层处理
* @author TANGMICHI
* @since 2022-09-24
*/
@Service
public class TenantDomainApprovalServiceImpl implements ITenantDomainApprovalService{
    @Autowired
    private TenantDomainApprovalMapper tenantDomainApprovalMapper;

    /**
    * 查询租户域名申请
    *
    * @param tenantDomainApprovalId 租户域名申请主键
    * @return 租户域名申请
    */
    @Override
    public TenantDomainApprovalDTO selectTenantDomainApprovalByTenantDomainApprovalId(Long tenantDomainApprovalId)
    {
    return tenantDomainApprovalMapper.selectTenantDomainApprovalByTenantDomainApprovalId(tenantDomainApprovalId);
    }

    /**
    * 查询租户域名申请列表
    *
    * @param tenantDomainApprovalDTO 租户域名申请
    * @return 租户域名申请
    */
    @Override
    public List<TenantDomainApprovalDTO> selectTenantDomainApprovalList(TenantDomainApprovalDTO tenantDomainApprovalDTO)
    {
    TenantDomainApproval tenantDomainApproval=new TenantDomainApproval();
    BeanUtils.copyProperties(tenantDomainApprovalDTO,tenantDomainApproval);
    return tenantDomainApprovalMapper.selectTenantDomainApprovalList(tenantDomainApproval);
    }

    /**
    * 新增租户域名申请
    *
    * @param tenantDomainApprovalDTO 租户域名申请
    * @return 结果
    */
    @Transactional
    @Override
    public int insertTenantDomainApproval(TenantDomainApprovalDTO tenantDomainApprovalDTO){
    TenantDomainApproval tenantDomainApproval=new TenantDomainApproval();
    BeanUtils.copyProperties(tenantDomainApprovalDTO,tenantDomainApproval);
    tenantDomainApproval.setCreateTime(DateUtils.getNowDate());
    tenantDomainApproval.setUpdateTime(DateUtils.getNowDate());
    return tenantDomainApprovalMapper.insertTenantDomainApproval(tenantDomainApproval);
    }

    /**
    * 修改租户域名申请
    *
    * @param tenantDomainApprovalDTO 租户域名申请
    * @return 结果
    */
    @Transactional
    @Override
    public int updateTenantDomainApproval(TenantDomainApprovalDTO tenantDomainApprovalDTO)
    {
    TenantDomainApproval tenantDomainApproval=new TenantDomainApproval();
    BeanUtils.copyProperties(tenantDomainApprovalDTO,tenantDomainApproval);
    tenantDomainApproval.setCreateTime(DateUtils.getNowDate());
    tenantDomainApproval.setUpdateTime(DateUtils.getNowDate());
    return tenantDomainApprovalMapper.updateTenantDomainApproval(tenantDomainApproval);
    }

    /**
    * 逻辑批量删除租户域名申请
    *
    * @param tenantDomainApprovalDtos 需要删除的租户域名申请主键
    * @return 结果
    */

    @Transactional
    @Override
    public int logicDeleteTenantDomainApprovalByTenantDomainApprovalIds(List<TenantDomainApprovalDTO> tenantDomainApprovalDtos){
            List<Long> stringList = new ArrayList();
            for (TenantDomainApprovalDTO tenantDomainApprovalDTO : tenantDomainApprovalDtos) {
                stringList.add(tenantDomainApprovalDTO.getTenantDomainApprovalId());
            }
    return tenantDomainApprovalMapper.logicDeleteTenantDomainApprovalByTenantDomainApprovalIds(stringList,tenantDomainApprovalDtos.get(0).getUpdateBy(),DateUtils.getNowDate());
    }

    /**
    * 物理删除租户域名申请信息
    *
    * @param tenantDomainApprovalId 租户域名申请主键
    * @return 结果
    */

    @Transactional
    @Override
    public int deleteTenantDomainApprovalByTenantDomainApprovalId(Long tenantDomainApprovalId)
    {
    return tenantDomainApprovalMapper.deleteTenantDomainApprovalByTenantDomainApprovalId(tenantDomainApprovalId);
    }

     /**
     * 逻辑删除租户域名申请信息
     *
     * @param  tenantDomainApprovalDTO 租户域名申请
     * @return 结果
     */
     @Transactional
     @Override
     public int logicDeleteTenantDomainApprovalByTenantDomainApprovalId(TenantDomainApprovalDTO tenantDomainApprovalDTO)
     {
     TenantDomainApproval tenantDomainApproval=new TenantDomainApproval();
     BeanUtils.copyProperties(tenantDomainApprovalDTO,tenantDomainApproval);
     return tenantDomainApprovalMapper.logicDeleteTenantDomainApprovalByTenantDomainApprovalId(tenantDomainApproval);
     }

     /**
     * 物理删除租户域名申请信息
     *
     * @param  tenantDomainApprovalDTO 租户域名申请
     * @return 结果
     */
     @Transactional
     @Override
     public int deleteTenantDomainApprovalByTenantDomainApprovalId(TenantDomainApprovalDTO tenantDomainApprovalDTO)
     {
     TenantDomainApproval tenantDomainApproval=new TenantDomainApproval();
     BeanUtils.copyProperties(tenantDomainApprovalDTO,tenantDomainApproval);
     return tenantDomainApprovalMapper.deleteTenantDomainApprovalByTenantDomainApprovalId(tenantDomainApproval.getTenantDomainApprovalId());
     }
     /**
     * 物理批量删除租户域名申请
     *
     * @param tenantDomainApprovalDtos 需要删除的租户域名申请主键
     * @return 结果
     */
     @Transactional
     @Override
     public int deleteTenantDomainApprovalByTenantDomainApprovalIds(List<TenantDomainApprovalDTO> tenantDomainApprovalDtos){
     List<Long> stringList = new ArrayList();
     for (TenantDomainApprovalDTO tenantDomainApprovalDTO : tenantDomainApprovalDtos) {
     stringList.add(tenantDomainApprovalDTO.getTenantDomainApprovalId());
     }
     return tenantDomainApprovalMapper.deleteTenantDomainApprovalByTenantDomainApprovalIds(stringList,tenantDomainApprovalDtos.get(0).getUpdateBy(),DateUtils.getNowDate());
     }

    /**
    * 批量新增租户域名申请信息
    *
    * @param tenantDomainApprovalDtos 租户域名申请对象
    */
    @Transactional
    public int insertTenantDomainApprovals(List<TenantDomainApprovalDTO> tenantDomainApprovalDtos){
      List<TenantDomainApproval> tenantDomainApprovalList = new ArrayList();

    for (TenantDomainApprovalDTO tenantDomainApprovalDTO : tenantDomainApprovalDtos) {
      TenantDomainApproval tenantDomainApproval =new TenantDomainApproval();
      BeanUtils.copyProperties(tenantDomainApprovalDTO,tenantDomainApproval);
    tenantDomainApproval.setCreateTime(DateUtils.getNowDate());
    tenantDomainApproval.setUpdateTime(DateUtils.getNowDate());
      tenantDomainApprovalList.add(tenantDomainApproval);
    }
    return tenantDomainApprovalMapper.batchTenantDomainApproval(tenantDomainApprovalList);
    }

    /**
    * 批量修改租户域名申请信息
    *
    * @param tenantDomainApprovalDtos 租户域名申请对象
    */
    @Transactional
    public int updateTenantDomainApprovals(List<TenantDomainApprovalDTO> tenantDomainApprovalDtos){
     List<TenantDomainApproval> tenantDomainApprovalList = new ArrayList();

     for (TenantDomainApprovalDTO tenantDomainApprovalDTO : tenantDomainApprovalDtos) {
     TenantDomainApproval tenantDomainApproval =new TenantDomainApproval();
     BeanUtils.copyProperties(tenantDomainApprovalDTO,tenantDomainApproval);
     tenantDomainApproval.setCreateTime(DateUtils.getNowDate());
     tenantDomainApproval.setUpdateTime(DateUtils.getNowDate());
     tenantDomainApprovalList.add(tenantDomainApproval);
     }
     return tenantDomainApprovalMapper.updateTenantDomainApprovals(tenantDomainApprovalList);
    }
}

