package net.qixiaowei.system.manage.service.impl.tenant;

import net.qixiaowei.integration.common.config.FileConfig;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.domain.tenant.Tenant;
import net.qixiaowei.system.manage.api.domain.tenant.TenantContacts;
import net.qixiaowei.system.manage.api.domain.tenant.TenantContract;
import net.qixiaowei.system.manage.api.domain.tenant.TenantDomainApproval;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDefaultDTO;
import net.qixiaowei.system.manage.api.dto.productPackage.ProductPackageDTO;
import net.qixiaowei.system.manage.api.dto.tenant.TenantContactsDTO;
import net.qixiaowei.system.manage.api.dto.tenant.TenantContractDTO;
import net.qixiaowei.system.manage.api.dto.tenant.TenantDTO;
import net.qixiaowei.system.manage.api.dto.tenant.TenantDomainApprovalDTO;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.config.tenant.TenantConfig;
import net.qixiaowei.system.manage.excel.tenant.TenantExcel;
import net.qixiaowei.system.manage.logic.tenant.TenantLogic;
import net.qixiaowei.system.manage.mapper.basic.EmployeeMapper;
import net.qixiaowei.system.manage.mapper.basic.IndustryDefaultMapper;
import net.qixiaowei.system.manage.mapper.productPackage.ProductPackageMapper;
import net.qixiaowei.system.manage.mapper.tenant.TenantContactsMapper;
import net.qixiaowei.system.manage.mapper.tenant.TenantContractMapper;
import net.qixiaowei.system.manage.mapper.tenant.TenantDomainApprovalMapper;
import net.qixiaowei.system.manage.mapper.tenant.TenantMapper;
import net.qixiaowei.system.manage.mapper.user.UserMapper;
import net.qixiaowei.system.manage.service.tenant.ITenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


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

    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private IndustryDefaultMapper industryDefaultMapper;

    @Autowired
    private ProductPackageMapper productPackageMapper;

    @Autowired
    private FileConfig fileConfig;

    @Autowired
    private TenantConfig tenantConfig;

    @Autowired
    private TenantLogic tenantLogic;

    /**
     * 查询租户表
     *
     * @param tenantId 租户表主键
     * @return 租户表
     */
    @Override
    public TenantDTO selectTenantByTenantId(Long tenantId) {
        TenantDTO tenantDTO = tenantMapper.selectTenantByTenantId(tenantId);
        if (StringUtils.isNull(tenantDTO)) {
            throw new ServiceException("租户数据不存在");
        }
        //租户登录背景图片URL
        tenantDTO.setLoginBackground(fileConfig.getFullDomain(tenantDTO.getLoginBackground()));
        //租户logo图片URL
        tenantDTO.setTenantLogo(fileConfig.getFullDomain(tenantDTO.getTenantLogo()));
        //行业
        IndustryDefaultDTO industryDefaultDTO = industryDefaultMapper.selectIndustryDefaultByIndustryId(tenantDTO.getTenantIndustry());
        //客服人员
        List<Long> collect1 = Arrays.asList(tenantDTO.getSupportStaff().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        //人员表
        List<EmployeeDTO> employeeDTOList = employeeMapper.selectEmployeeByEmployeeIds(collect1);
        if (StringUtils.isNotNull(industryDefaultDTO)) {
            //行业名称
            tenantDTO.setTenantIndustryName(industryDefaultDTO.getIndustryName());
        }
        if (StringUtils.isNotEmpty(employeeDTOList)) {
            //客服人员名称
            tenantDTO.setSupportStaffName(StringUtils.join(employeeDTOList.stream().map(EmployeeDTO::getEmployeeName).collect(Collectors.toList()), ","));
        }
        //租户联系人
        tenantDTO.setTenantContactsDTOList(tenantContactsMapper.selectTenantContactsByTenantId(tenantId));
        //租赁模块
        List<TenantContractDTO> tenantContractDTOS = tenantContractMapper.selectTenantContractByTenantId(tenantId);
        for (TenantContractDTO tenantContractDTO : tenantContractDTOS) {
            String productPackage = tenantContractDTO.getProductPackage();
            List<Long> collect = Arrays.asList(productPackage.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            List<ProductPackageDTO> productPackageDTOList = productPackageMapper.selectProductPackageByProductPackageIds(collect);
            if (!StringUtils.isEmpty(productPackageDTOList)) {
                tenantContractDTO.setProductPackage(StringUtils.join(productPackageDTOList.stream().map(ProductPackageDTO::getProductPackageName).collect(Collectors.toList()), ","));
            }
        }
        //合同时间
        if (StringUtils.isNotEmpty(tenantContractDTOS)) {
            //合同开始时间
            tenantDTO.setContractStartTime(tenantContractDTOS.get(tenantContractDTOS.size() - 1).getContractStartTime());
            //合同结束时间
            tenantDTO.setContractEndTime(tenantContractDTOS.get(tenantContractDTOS.size() - 1).getContractEndTime());
        }
        //租户合同
        tenantDTO.setTenantContractDTOList(tenantContractDTOS);
        //租户域名申请表
        List<TenantDomainApprovalDTO> tenantDomainApprovalDTOS = tenantDomainApprovalMapper.selectTenantDomainApprovalByTenantId(tenantId);
        tenantDTO.setTenantDomainApprovalDTOList(tenantDomainApprovalDTOS);
        if (!StringUtils.isEmpty(tenantDomainApprovalDTOS)) {
            //申请状态:0待审核;1审核通过;2审核驳回
            tenantDTO.setApprovalStatus(tenantDomainApprovalDTOS.get(tenantDomainApprovalDTOS.size() - 1).getApprovalStatus());
        }
        return tenantDTO;
    }

    /**
     * 查询企业信息
     *
     * @return
     */
    @Override
    public TenantDTO selectTenantByTenantId() {
        Long tenantId = SecurityUtils.getTenantId();
        TenantDTO tenantDTO = tenantMapper.selectTenantByTenantId(tenantId);
        if (StringUtils.isNull(tenantDTO)) {
            throw new ServiceException("租户数据不存在");
        }
        //租户登录背景图片URL
        tenantDTO.setLoginBackground(fileConfig.getFullDomain(tenantDTO.getLoginBackground()));
        //租户logo图片URL
        tenantDTO.setTenantLogo(fileConfig.getFullDomain(tenantDTO.getTenantLogo()));
        //行业
        IndustryDefaultDTO industryDefaultDTO = industryDefaultMapper.selectIndustryDefaultByIndustryId(tenantDTO.getTenantIndustry());
        //客服人员
        List<Long> collect1 = Arrays.asList(tenantDTO.getSupportStaff().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        //人员表
        List<EmployeeDTO> employeeDTOList = employeeMapper.selectEmployeeByEmployeeIds(collect1);
        if (StringUtils.isNotNull(industryDefaultDTO)) {
            //行业名称
            tenantDTO.setTenantIndustryName(industryDefaultDTO.getIndustryName());
        }
        if (StringUtils.isNotEmpty(employeeDTOList)) {
            //客服人员名称
            tenantDTO.setSupportStaffName(StringUtils.join(employeeDTOList.stream().map(EmployeeDTO::getEmployeeName).collect(Collectors.toList()), ","));
        }
        //租户联系人
        tenantDTO.setTenantContactsDTOList(tenantContactsMapper.selectTenantContactsByTenantId(tenantId));
        //租赁模块
        List<TenantContractDTO> tenantContractDTOS = tenantContractMapper.selectTenantContractByTenantId(tenantId);
        for (TenantContractDTO tenantContractDTO : tenantContractDTOS) {
            String productPackage = tenantContractDTO.getProductPackage();
            List<Long> collect = Arrays.asList(productPackage.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            List<ProductPackageDTO> productPackageDTOList = productPackageMapper.selectProductPackageByProductPackageIds(collect);
            if (!StringUtils.isEmpty(productPackageDTOList)) {
                tenantContractDTO.setProductPackage(StringUtils.join(productPackageDTOList.stream().map(ProductPackageDTO::getProductPackageName).collect(Collectors.toList()), ","));
            }
        }
        //合同时间
        if (StringUtils.isNotEmpty(tenantContractDTOS)) {
            //合同开始时间
            tenantDTO.setContractStartTime(tenantContractDTOS.get(tenantContractDTOS.size() - 1).getContractStartTime());
            //合同结束时间
            tenantDTO.setContractEndTime(tenantContractDTOS.get(tenantContractDTOS.size() - 1).getContractEndTime());
        }
        //租户合同
        tenantDTO.setTenantContractDTOList(tenantContractDTOS);
        //租户域名申请表
        List<TenantDomainApprovalDTO> tenantDomainApprovalDTOS = tenantDomainApprovalMapper.selectTenantDomainApprovalByTenantId(tenantId);
        tenantDTO.setTenantDomainApprovalDTOList(tenantDomainApprovalDTOS);
        if (!StringUtils.isEmpty(tenantDomainApprovalDTOS)) {
            //申请状态:0待审核;1审核通过;2审核驳回
            tenantDTO.setApprovalStatus(tenantDomainApprovalDTOS.get(tenantDomainApprovalDTOS.size() - 1).getApprovalStatus());
        }
        return tenantDTO;
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
    public TenantDTO insertTenant(TenantDTO tenantDTO) {
        String domain = tenantDTO.getDomain();
        if (tenantConfig.getExistedDomains().contains(domain)) {
            throw new ServiceException("创建租户失败:域名[" + domain + "]已经被占用!");
        }
        //租户
        Tenant tenant = new Tenant();
        //租户联系人
        List<TenantContacts> tenantContactsList = new ArrayList<>();
        //租户合同
        List<TenantContract> tenantContractList = new ArrayList<>();
        //copy
        BeanUtils.copyProperties(tenantDTO, tenant);
//        //拼接本人用户id
//        tenant.setSupportStaff(tenant.getSupportStaff() + "," + userDTO.getEmployeeId());
        tenant.setSupportStaff(tenant.getSupportStaff());
        tenant.setCreateBy(SecurityUtils.getUserId());
        tenant.setUpdateBy(SecurityUtils.getUserId());
        tenant.setCreateTime(DateUtils.getNowDate());
        tenant.setUpdateTime(DateUtils.getNowDate());
        tenant.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        //1、插入租户
        tenantMapper.insertTenant(tenant);
        //copy租户联系人
        tenantContactsList = tenantDTO.getTenantContactsDTOList().stream().distinct().map(info -> {
            TenantContacts tenantContacts = new TenantContacts();
            BeanUtils.copyProperties(info, tenantContacts);
            //租户id
            tenantContacts.setTenantId(tenant.getTenantId());
            tenantContacts.setCreateTime(DateUtils.getNowDate());
            tenantContacts.setUpdateTime(DateUtils.getNowDate());
            tenantContacts.setCreateBy(SecurityUtils.getUserId());
            tenantContacts.setUpdateBy(SecurityUtils.getUserId());
            tenantContacts.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            return tenantContacts;
        }).collect(Collectors.toList());
        //2、插入租户联系人
        tenantContactsMapper.batchTenantContacts(tenantContactsList);
        //copy租户合同人
        tenantContractList = tenantDTO.getTenantContractDTOList().stream().distinct().map(info -> {
            TenantContract tenantContract = new TenantContract();
            BeanUtils.copyProperties(info, tenantContract);
            //租户id
            tenantContract.setTenantId(tenant.getTenantId());
            tenantContract.setCreateTime(DateUtils.getNowDate());
            tenantContract.setUpdateTime(DateUtils.getNowDate());
            tenantContract.setCreateBy(SecurityUtils.getUserId());
            tenantContract.setUpdateBy(SecurityUtils.getUserId());
            tenantContract.setDeleteFlag(0);
            return tenantContract;
        }).collect(Collectors.toList());
        //3、插入租户合同
        tenantContractMapper.batchTenantContract(tenantContractList);
        //4、初始化租户数据
        tenantLogic.initTenantData(tenant);
        //返回数据
        tenantDTO.setTenantId(tenant.getTenantId());
        return tenantDTO;
    }

    /**
     * 导入租户
     *
     * @param tenantExcels 租户表
     */
    @Override
    public void insertTenant(List<TenantExcel> tenantExcels) {
        List<Tenant> tenantList = new ArrayList<>();
        for (TenantExcel tenantExcel : tenantExcels) {
            Tenant tenant = new Tenant();
            BeanUtils.copyProperties(tenantExcel, tenant);
            tenant.setCreateTime(DateUtils.getNowDate());
            tenant.setUpdateTime(DateUtils.getNowDate());
            tenant.setCreateBy(SecurityUtils.getUserId());
            tenant.setUpdateBy(SecurityUtils.getUserId());
            tenant.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            tenantList.add(tenant);
        }
        try {
            tenantMapper.batchTenant(tenantList);
        } catch (Exception e) {
            throw new ServiceException("导入租户失败");
        }
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
        String domain = tenantDTO.getDomain();
        if (tenantConfig.getExistedDomains().contains(domain)) {
            throw new ServiceException("修改租户失败:域名[" + domain + "]已经被占用!");
        }
        int i = 0;
        Tenant tenant = new Tenant();
        //租户联系人
        TenantContacts tenantContactsQuery = new TenantContacts();
        //数据库原本数据
        List<TenantContactsDTO> tenantContactsDTOList = new ArrayList<>();
        //接收数据
        List<TenantContactsDTO> tenantContactsDTOList1 = tenantDTO.getTenantContactsDTOList();
        //要删除的差集
        List<Long> tenantContactsIds = new ArrayList<>();
        BeanUtils.copyProperties(tenantDTO, tenant);
        tenant.setCreateTime(DateUtils.getNowDate());
        tenant.setUpdateTime(DateUtils.getNowDate());
        try {
            i = tenantMapper.updateTenant(tenant);
        } catch (Exception e) {
            throw new ServiceException("修改租户失败" + e);
        }
        //查询租户联系人
        tenantContactsQuery.setTenantId(tenant.getTenantId());
        //拿到租户联系人信息 可以赋值创建人创建时间
        tenantContactsDTOList = tenantContactsMapper.selectTenantContactsList(tenantContactsQuery);
        //sterm流求差集
        tenantContactsIds = tenantContactsDTOList.stream().filter(a ->
                !tenantContactsDTOList1.stream().map(TenantContactsDTO::getTenantContactsId).collect(Collectors.toList()).contains(a.getTenantContactsId())
        ).collect(Collectors.toList()).stream().map(TenantContactsDTO::getTenantContactsId).collect(Collectors.toList());

        if (!StringUtils.isEmpty(tenantContactsIds)) {
            //逻辑删除
            try {
                tenantContactsMapper.logicDeleteTenantContactsByTenantContactsIds(tenantContactsIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
            } catch (Exception e) {
                throw new ServiceException("删除租户联系人失败" + e);
            }
        }
        //去除已经删除的id
        for (int i1 = 0; i1 < tenantContactsDTOList1.size(); i1++) {
            if (tenantContactsIds.contains(tenantContactsDTOList1.get(i1).getTenantContactsId())) {
                tenantContactsDTOList1.remove(i1);
            }
        }
        //新增集合
        List<TenantContacts> tenantContactsAddList = new ArrayList<>();
        //修改集合
        List<TenantContacts> tenantContactsUpdateList = new ArrayList<>();
        //copy租户联系人
        for (TenantContactsDTO tenantContactsDTO : tenantContactsDTOList1) {
            TenantContacts tenantContacts = new TenantContacts();
            BeanUtils.copyProperties(tenantContactsDTO, tenantContacts);
            //租户id
            tenantContacts.setTenantId(tenant.getTenantId());
            tenantContacts.setCreateBy(tenantContactsDTOList.get(0).getCreateBy());
            tenantContacts.setCreateTime(tenantContactsDTOList.get(0).getCreateTime());
            tenantContacts.setUpdateBy(SecurityUtils.getUserId());
            tenantContacts.setUpdateTime(DateUtils.getNowDate());
            tenantContacts.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            //没有id回显 表示为新增数据
            if (null == tenantContactsDTO.getTenantContactsId()) {
                tenantContactsAddList.add(tenantContacts);
            } else {
                //修改
                tenantContacts.setTenantContactsId(tenantContactsDTO.getTenantContactsId());
                tenantContactsUpdateList.add(tenantContacts);
            }
        }
        if (!StringUtils.isEmpty(tenantContactsAddList)) {
            try {
                tenantContactsMapper.batchTenantContacts(tenantContactsAddList);
            } catch (Exception e) {
                throw new ServiceException("新增租户联系人失败" + e);
            }
        }
        if (!StringUtils.isEmpty(tenantContactsUpdateList)) {
            try {
                tenantContactsMapper.updateTenantContactss(tenantContactsUpdateList);
            } catch (Exception e) {
                throw new ServiceException("修改租户联系人失败" + e);
            }
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
        return tenantMapper.logicDeleteTenantByTenantIds(stringList, tenantDtos.get(0).getUpdateBy(), DateUtils.getNowDate());
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
     * 修改单个租户
     *
     * @param tenantDTO
     * @return
     */
    @Override
    public int updateMyTenantDTO(TenantDTO tenantDTO) {
        String domain = tenantDTO.getDomain();
        if (tenantConfig.getExistedDomains().contains(domain)) {
            throw new ServiceException("修改企业信息失败:域名[" + domain + "]已经被占用!");
        }
        //租户表
        Tenant tenant = new Tenant();
        //
        TenantDomainApproval tenantDomainApproval = new TenantDomainApproval();
        BeanUtils.copyProperties(tenantDTO, tenant);

        //租户登录背景图片URL
        tenant.setLoginBackground(fileConfig.getPathOfRemoveDomain(tenant.getLoginBackground()));
        //租户logo图片URL
        tenant.setTenantLogo(fileConfig.getPathOfRemoveDomain(tenant.getTenantLogo()));
        //查询租户数据
        TenantDTO tenantDTO1 = tenantMapper.selectTenantByTenantId(tenant.getTenantId());
        if (StringUtils.isNull(tenantDTO1)) {
            throw new ServiceException("租户信息不存在");
        }
        //对比域名是否修改 修改需要保存到域名申请表中
        if (!StringUtils.equals(tenantDTO1.getDomain(), tenant.getDomain())) {
            //租户id
            tenantDomainApproval.setTenantId(tenant.getTenantId());
            //申请域名
            tenantDomainApproval.setApprovalDomain(tenant.getDomain());
            //申请人用户id
            tenantDomainApproval.setApprovalUserId(SecurityUtils.getUserId());
            //申请人账号
            tenantDomainApproval.setApplicantUserAccount(SecurityUtils.getUserAccount());
            //提交时间
            tenantDomainApproval.setSubmissionTime(DateUtils.getNowDate());
            //申请状态
            tenantDomainApproval.setApprovalStatus(0);
            //删除标记
            tenantDomainApproval.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            tenantDomainApproval.setCreateBy(SecurityUtils.getUserId());
            tenantDomainApproval.setUpdateBy(SecurityUtils.getUserId());
            tenantDomainApproval.setCreateTime(DateUtils.getNowDate());
            tenantDomainApproval.setUpdateTime(DateUtils.getNowDate());
            tenantDomainApprovalMapper.insertTenantDomainApproval(tenantDomainApproval);
            // todo 开启工作流
        }
        tenant.setUpdateBy(SecurityUtils.getUserId());
        tenant.setUpdateTime(DateUtils.getNowDate());
        return tenantMapper.updateTenant(tenant);
    }

    /**
     * 修改企业信息
     *
     * @return
     */
    @Override
    public int updateMyTenant(TenantDTO tenantDTO) {
        String domain = tenantDTO.getDomain();
        if (tenantConfig.getExistedDomains().contains(domain)) {
            throw new ServiceException("修改企业信息失败:域名[" + domain + "]已经被占用!");
        }
        //租户表
        Tenant tenant = new Tenant();
        //
        TenantDomainApproval tenantDomainApproval = new TenantDomainApproval();
        BeanUtils.copyProperties(tenantDTO, tenant);
        tenant.setTenantId(SecurityUtils.getTenantId());
        //租户登录背景图片URL
        tenant.setLoginBackground(fileConfig.getPathOfRemoveDomain(tenant.getLoginBackground()));
        //租户logo图片URL
        tenant.setTenantLogo(fileConfig.getPathOfRemoveDomain(tenant.getTenantLogo()));
        //查询租户数据
        TenantDTO tenantDTO1 = tenantMapper.selectTenantByTenantId(tenant.getTenantId());
        if (StringUtils.isNull(tenantDTO1)) {
            throw new ServiceException("租户信息不存在");
        }
        //对比域名是否修改 修改需要保存到域名申请表中
        if (!StringUtils.equals(tenantDTO1.getDomain(), tenant.getDomain())) {
            //租户id
            tenantDomainApproval.setTenantId(tenant.getTenantId());
            //申请域名
            tenantDomainApproval.setApprovalDomain(tenant.getDomain());
            //申请人用户id
            tenantDomainApproval.setApprovalUserId(SecurityUtils.getUserId());
            //申请人账号
            tenantDomainApproval.setApplicantUserAccount(SecurityUtils.getUserAccount());
            //提交时间
            tenantDomainApproval.setSubmissionTime(DateUtils.getNowDate());
            //申请状态
            tenantDomainApproval.setApprovalStatus(0);
            //删除标记
            tenantDomainApproval.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            tenantDomainApproval.setCreateBy(SecurityUtils.getUserId());
            tenantDomainApproval.setUpdateBy(SecurityUtils.getUserId());
            tenantDomainApproval.setCreateTime(DateUtils.getNowDate());
            tenantDomainApproval.setUpdateTime(DateUtils.getNowDate());
            tenantDomainApprovalMapper.insertTenantDomainApproval(tenantDomainApproval);
            // todo 开启工作流
        }
        tenant.setUpdateBy(SecurityUtils.getUserId());
        tenant.setUpdateTime(DateUtils.getNowDate());
        return tenantMapper.updateTenant(tenant);
    }

    /**
     * 导出租户
     *
     * @param tenantDTO
     * @return
     */
    @Override
    public List<TenantExcel> exportTenant(TenantDTO tenantDTO) {
        Tenant tenant = new Tenant();
        BeanUtils.copyProperties(tenantDTO, tenant);
        List<TenantExcel> tenantExcelList = new ArrayList<>();
        List<TenantDTO> tenantDTOList = tenantMapper.selectTenantList(tenant);

        if (StringUtils.isNotEmpty(tenantDTOList)) {
            for (TenantDTO dto : tenantDTOList) {
                TenantExcel tenantExcel = new TenantExcel();
                BeanUtils.copyProperties(dto, tenantExcel);
                if (dto.getTenantStatus() == 0) {
                    tenantExcel.setTenantStatusName("待初始化");
                } else if (dto.getTenantStatus() == 1) {
                    tenantExcel.setTenantStatusName("正常");
                } else if (dto.getTenantStatus() == 2) {
                    tenantExcel.setTenantStatusName("禁用");
                } else if (dto.getTenantStatus() == 3) {
                    tenantExcel.setTenantStatusName("过期");
                }
                tenantExcelList.add(tenantExcel);
            }
        }

        return tenantExcelList;
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
        tenant.setTenantId(tenantDTO.getTenantId());
        tenant.setUpdateBy(SecurityUtils.getUserId());
        tenant.setUpdateTime(DateUtils.getNowDate());
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
        return tenantMapper.deleteTenantByTenantIds(stringList, tenantDtos.get(0).getUpdateBy(), DateUtils.getNowDate());
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

