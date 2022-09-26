package net.qixiaowei.system.manage.service.impl.tenant;

import java.util.List;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.domain.tenant.TenantContacts;
import net.qixiaowei.system.manage.api.domain.tenant.TenantContract;
import net.qixiaowei.system.manage.api.domain.tenant.TenantDomainApproval;
import net.qixiaowei.system.manage.api.dto.tenant.TenantContactsDTO;
import net.qixiaowei.system.manage.mapper.tenant.TenantContactsMapper;
import net.qixiaowei.system.manage.mapper.tenant.TenantContractMapper;
import net.qixiaowei.system.manage.mapper.tenant.TenantDomainApprovalMapper;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.system.manage.api.domain.tenant.Tenant;
import net.qixiaowei.system.manage.api.dto.tenant.TenantDTO;
import net.qixiaowei.system.manage.mapper.tenant.TenantMapper;
import net.qixiaowei.system.manage.service.tenant.ITenantService;


/**
 * TenantService业务层处理
 *
 * @author TANGMICHI
 * @since 2022-09-24
 */
@Service
public class TenantServiceImpl implements ITenantService {
    @Autowired
    private TenantMapper tenantMapper;
    @Autowired
    private TenantContactsMapper tenantContactsMapper;
    @Autowired
    private TenantContractMapper tenantContractMapper;
    @Autowired
    private TenantDomainApprovalMapper tenantDomainApprovalMapper;

    /**
     * 查询租户表
     *
     * @param tenantId 租户表主键
     * @return 租户表
     */
    @Override
    public TenantDTO selectTenantByTenantId(Long tenantId) {
        return tenantMapper.selectTenantByTenantId(tenantId);
    }

    /**
     * 查询租户表列表
     *
     * @param tenantDTO 租户表
     * @return 租户表
     */
    @Override
    public List<TenantDTO> selectTenantList(TenantDTO tenantDTO) {
        Tenant tenant = new Tenant();
        BeanUtils.copyProperties(tenantDTO, tenant);
        return tenantMapper.selectTenantList(tenant);
    }

    /**
     * 新增租户表
     *
     * @param tenantDTO 租户表
     * @return 结果
     */
    @Transactional
    @Override
    public int insertTenant(TenantDTO tenantDTO) {
        int i = 0;

        //租户
        Tenant tenant = new Tenant();
        //租户联系人
        List<TenantContacts> tenantContactsList = new ArrayList<>();
        //租户合同
        List<TenantContract> tenantContractList = new ArrayList<>();
        //租户域名
        TenantDomainApproval tenantDomainApproval = new TenantDomainApproval();
        //copy
        BeanUtils.copyProperties(tenantDTO, tenant);
        tenant.setCreateBy(SecurityUtils.getUserId().toString());
        tenant.setUpdateBy(SecurityUtils.getUserId().toString());
        tenant.setCreateTime(DateUtils.getNowDate());
        tenant.setUpdateTime(DateUtils.getNowDate());
        tenant.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);

        //插入租户表
        try {
            i = tenantMapper.insertTenant(tenant);
        } catch (Exception e) {
            throw new ServiceException("插入租户表失败"+e);
        }
        //copy租户联系人
        tenantContactsList = tenantDTO.getTenantContactsDTOList().stream().distinct().map(info -> {
            TenantContacts tenantContacts = new TenantContacts();
            BeanUtils.copyProperties(info, tenantContacts);
            //租户id
            tenantContacts.setTenantId(tenant.getTenantId());
            tenantContacts.setCreateTime(DateUtils.getNowDate());
            tenantContacts.setUpdateTime(DateUtils.getNowDate());
            tenantContacts.setCreateBy(SecurityUtils.getUserId().toString());
            tenantContacts.setUpdateBy(SecurityUtils.getUserId().toString());
            tenantContacts.setDeleteFlag(0);
            return tenantContacts;
        }).collect(Collectors.toList());

        try {
            tenantContactsMapper.batchTenantContacts(tenantContactsList);
        } catch (Exception e) {
            throw new ServiceException("插入租户联系人失败"+e);
        }

        //copy租户合同人
        tenantContractList = tenantDTO.getTenantContractDTOList().stream().distinct().map(info -> {
            TenantContract tenantContract = new TenantContract();
            BeanUtils.copyProperties(info, tenantContract);
            //租户id
            tenantContract.setTenantId(tenant.getTenantId());
            tenantContract.setCreateTime(DateUtils.getNowDate());
            tenantContract.setUpdateTime(DateUtils.getNowDate());
            tenantContract.setCreateBy(SecurityUtils.getUserId().toString());
            tenantContract.setUpdateBy(SecurityUtils.getUserId().toString());
            tenantContract.setDeleteFlag(0);
            return tenantContract;
        }).collect(Collectors.toList());

        try {
            tenantContractMapper.batchTenantContract(tenantContractList);
        } catch (Exception e) {
            throw new ServiceException("插入租户合同人失败"+e);
        }
        //返回条数
        return i;
    }


    /**
     * 修改租户表
     *
     * @param tenantDTO 租户表
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateTenant(TenantDTO tenantDTO) {
        int i = 0;
        Tenant tenant = new Tenant();
        //租户联系人
        TenantContacts tenantContactsQuery = new TenantContacts();
        List<TenantContacts> tenantContactsList = new ArrayList<>();
        //租户联系人主键
        List<Long> tenantContactsIds = new ArrayList<>();
        BeanUtils.copyProperties(tenantDTO, tenant);
        tenant.setCreateTime(DateUtils.getNowDate());
        tenant.setUpdateTime(DateUtils.getNowDate());
        try {
            i = tenantMapper.updateTenant(tenant);
        } catch (Exception e) {
            throw new ServiceException("修改租户失败"+e);
        }
        //查询租户联系人
        tenantContactsQuery.setTenantId(tenant.getTenantId());
        //拿到租户联系人信息 可以赋值创建人创建时间
        List<TenantContactsDTO> tenantContactsDTOList = tenantContactsMapper.selectTenantContactsList(tenantContactsQuery);
        //copy租户联系人
        tenantContactsList = tenantDTO.getTenantContactsDTOList().stream().map(info -> {
            TenantContacts tenantContacts = new TenantContacts();
            BeanUtils.copyProperties(info, tenantContacts);
            //租户id
            tenantContacts.setTenantId(tenant.getTenantId());
            tenantContacts.setCreateBy(tenantContactsDTOList.get(0).getCreateBy().toString());
            tenantContacts.setCreateTime(tenantContactsDTOList.get(0).getCreateTime());
            tenantContacts.setUpdateBy(SecurityUtils.getUserId().toString());
            tenantContacts.setUpdateTime(DateUtils.getNowDate());
            tenantContacts.setDeleteFlag(0);
            return tenantContacts;
        }).collect(Collectors.toList());

        //主键
        tenantContactsIds = tenantContactsList.stream().map(TenantContacts::getTenantId).collect(Collectors.toList());
        //先删除后插入
        try {
            tenantContactsMapper.deleteTenantContactsByTenantIds(tenantContactsIds,SecurityUtils.getUserId(),DateUtils.getNowDate());
        } catch (Exception e) {
            throw new ServiceException("删除租户联系人失败"+e);
        }
        try {
            tenantContactsMapper.batchTenantContacts(tenantContactsList);
        } catch (Exception e) {
            throw new ServiceException("修改租户联系人失败"+e);
        }
        return i;
    }

    /**
     * 逻辑批量删除租户表
     *
     * @param tenantDtos 需要删除的租户表主键
     * @return 结果
     */

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int logicDeleteTenantByTenantIds(List<TenantDTO> tenantDtos) {
        List<Long> stringList = new ArrayList();
        for (TenantDTO tenantDTO : tenantDtos) {
            stringList.add(tenantDTO.getTenantId());
        }
        return tenantMapper.logicDeleteTenantByTenantIds(stringList,tenantDtos.get(0).getUpdateBy(),DateUtils.getNowDate());
    }

    /**
     * 物理删除租户表信息
     *
     * @param tenantId 租户表主键
     * @return 结果
     */

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteTenantByTenantId(Long tenantId) {
        return tenantMapper.deleteTenantByTenantId(tenantId);
    }

    /**
     * 逻辑删除租户表信息
     *
     * @param tenantDTO 租户表
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int logicDeleteTenantByTenantId(TenantDTO tenantDTO) {
        Tenant tenant = new Tenant();
        BeanUtils.copyProperties(tenantDTO, tenant);
        return tenantMapper.logicDeleteTenantByTenantId(tenant);
    }

    /**
     * 物理删除租户表信息
     *
     * @param tenantDTO 租户表
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteTenantByTenantId(TenantDTO tenantDTO) {
        Tenant tenant = new Tenant();
        BeanUtils.copyProperties(tenantDTO, tenant);
        return tenantMapper.deleteTenantByTenantId(tenant.getTenantId());
    }

    /**
     * 物理批量删除租户表
     *
     * @param tenantDtos 需要删除的租户表主键
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteTenantByTenantIds(List<TenantDTO> tenantDtos) {
        List<Long> stringList = new ArrayList();
        for (TenantDTO tenantDTO : tenantDtos) {
            stringList.add(tenantDTO.getTenantId());
        }
        return tenantMapper.deleteTenantByTenantIds(stringList,tenantDtos.get(0).getUpdateBy(),DateUtils.getNowDate());
    }

    /**
     * 批量新增租户表信息
     *
     * @param tenantDtos 租户表对象
     */
    @Transactional(rollbackFor = Exception.class)
    public int insertTenants(List<TenantDTO> tenantDtos) {
        List<Tenant> tenantList = new ArrayList();

        for (TenantDTO tenantDTO : tenantDtos) {
            Tenant tenant = new Tenant();
            BeanUtils.copyProperties(tenantDTO, tenant);
            tenant.setCreateTime(DateUtils.getNowDate());
            tenant.setUpdateTime(DateUtils.getNowDate());
            tenantList.add(tenant);
        }
        return tenantMapper.batchTenant(tenantList);
    }

    /**
     * 批量修改租户表信息
     *
     * @param tenantDtos 租户表对象
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateTenants(List<TenantDTO> tenantDtos) {
        List<Tenant> tenantList = new ArrayList();

        for (TenantDTO tenantDTO : tenantDtos) {
            Tenant tenant = new Tenant();
            BeanUtils.copyProperties(tenantDTO, tenant);
            tenant.setCreateTime(DateUtils.getNowDate());
            tenant.setUpdateTime(DateUtils.getNowDate());
            tenantList.add(tenant);
        }
        return tenantMapper.updateTenants(tenantList);
    }
}

