package net.qixiaowei.system.manage.service.impl.tenant;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import io.seata.spring.annotation.GlobalTransactional;
import net.qixiaowei.integration.common.config.FileConfig;
import net.qixiaowei.integration.common.constant.BusinessConstants;
import net.qixiaowei.integration.common.constant.CacheConstants;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.enums.PrefixCodeRule;
import net.qixiaowei.integration.common.enums.tenant.TenantStatus;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.redis.service.RedisService;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.integration.security.utils.UserUtils;
import net.qixiaowei.integration.tenant.utils.TenantUtils;
import net.qixiaowei.system.manage.api.domain.tenant.Tenant;
import net.qixiaowei.system.manage.api.domain.tenant.TenantContacts;
import net.qixiaowei.system.manage.api.domain.tenant.TenantContract;
import net.qixiaowei.system.manage.api.domain.tenant.TenantDomainApproval;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDefaultDTO;
import net.qixiaowei.system.manage.api.dto.tenant.TenantContactsDTO;
import net.qixiaowei.system.manage.api.dto.tenant.TenantContractDTO;
import net.qixiaowei.system.manage.api.dto.tenant.TenantDTO;
import net.qixiaowei.system.manage.api.dto.tenant.TenantDomainApprovalDTO;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.api.vo.tenant.TenantInfoVO;
import net.qixiaowei.system.manage.api.vo.tenant.TenantLoginFormVO;
import net.qixiaowei.system.manage.api.vo.tenant.TenantRegisterResponseVO;
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
import net.qixiaowei.system.manage.service.system.IMenuService;
import net.qixiaowei.system.manage.service.tenant.ITenantContractAuthService;
import net.qixiaowei.system.manage.service.tenant.ITenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
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
    @Lazy
    @Autowired
    private TenantLogic tenantLogic;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ITenantContractAuthService tenantContractAuthService;

    @Autowired
    private IMenuService menuService;

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
            throw new ServiceException("企业数据不存在");
        }
        //租户登录背景图片URL
        tenantDTO.setLoginBackground(fileConfig.getFullDomain(tenantDTO.getLoginBackground()));
        //租户logo图片URL
        tenantDTO.setTenantLogo(fileConfig.getFullDomain(tenantDTO.getTenantLogo()));
        //行业
        IndustryDefaultDTO industryDefaultDTO = industryDefaultMapper.selectIndustryDefaultByIndustryId(tenantDTO.getTenantIndustry());
        //客服人员
        List<Long> collect1 = Arrays.stream(tenantDTO.getSupportStaff().split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
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
        List<TenantContactsDTO> tenantContactsDTOS = tenantContactsMapper.selectTenantContactsByTenantId(tenantId);
        tenantDTO.setTenantContactsDTOList(tenantContactsDTOS);
        //租赁模块
        List<TenantContractDTO> tenantContractDTOS = tenantContractMapper.selectTenantContractByTenantId(tenantId);
        this.handleResponseOfTenantContract(tenantContractDTOS);
        //租户合同
        tenantDTO.setTenantContractDTOList(tenantContractDTOS);
        //租户域名申请表
        List<TenantDomainApprovalDTO> tenantDomainApprovalDTOS = tenantDomainApprovalMapper.selectTenantDomainApprovalByTenantId(tenantId);
        tenantDTO.setTenantDomainApprovalDTOList(tenantDomainApprovalDTOS);
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

    @Override
    public void handleResult(List<TenantDTO> result) {
        if (StringUtils.isNotEmpty(result)) {
            Set<Long> userIds = result.stream().map(TenantDTO::getCreateBy).collect(Collectors.toSet());
            Map<Long, String> employeeNameMap = UserUtils.getEmployeeNameMap(userIds);
            result.forEach(entity -> {
                Long userId = entity.getCreateBy();
                entity.setCreateByName(employeeNameMap.get(userId));
            });
        }
    }

    /**
     * 生成租户编码
     *
     * @return 租户编码
     */
    @Override
    public String generateTenantCode() {
        String tenantCode;
        int number = 1;
        String prefixCodeRule = PrefixCodeRule.TENANT.getCode();
        List<String> tenantCodes = tenantMapper.getTenantCodes(prefixCodeRule);
        if (StringUtils.isNotEmpty(tenantCodes)) {
            for (String code : tenantCodes) {
                if (StringUtils.isEmpty(code) || code.length() != 8) {
                    continue;
                }
                code = code.replaceFirst(prefixCodeRule, "");
                try {
                    int codeOfNumber = Integer.parseInt(code);
                    if (number != codeOfNumber) {
                        break;
                    }
                    number++;
                } catch (Exception ignored) {
                }
            }
        }
        if (number > 1000000) {
            throw new ServiceException("流水号溢出，请联系管理员");
        }
        tenantCode = "000000" + number;
        tenantCode = prefixCodeRule + tenantCode.substring(tenantCode.length() - 6);
        return tenantCode;
    }

    /**
     * 新增租户表
     *
     * @param tenantDTO 租户表
     * @return 结果
     */
//    @GlobalTransactional(name = "system:manage:tenant:add", rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public TenantDTO insertTenant(TenantDTO tenantDTO) {
        String domain = getDomain(tenantDTO);
        String tenantCode = tenantDTO.getTenantCode();
        this.checkDomain(domain);
        TenantDTO selectTenantByTenantCode = tenantMapper.selectTenantByTenantCode(tenantCode);
        if (StringUtils.isNotNull(selectTenantByTenantCode)) {
            throw new ServiceException("企业编码已存在");
        }
        //租户
        Tenant tenant = new Tenant();
        Long userId = SecurityUtils.getUserId();
        Date nowDate = DateUtils.getNowDate();
        BeanUtils.copyProperties(tenantDTO, tenant);
        tenant.setDomain(domain);
        tenant.setSupportStaff(tenant.getSupportStaff());
        tenant.setCreateBy(userId);
        tenant.setUpdateBy(userId);
        tenant.setCreateTime(nowDate);
        tenant.setUpdateTime(nowDate);
        tenant.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        //插入租户
        tenantMapper.insertTenant(tenant);
        Long tenantId = tenant.getTenantId();
        //保存租户联系人
        List<TenantContactsDTO> tenantContactsDTOList = tenantDTO.getTenantContactsDTOList();
        this.saveTenantContacts(tenantContactsDTOList, tenantId, userId, nowDate);
        //保存租户合同
        List<TenantContractDTO> tenantContractDTOList = tenantDTO.getTenantContractDTOList();

        Set<Long> initMenuIds = this.saveTenantContract(tenantContractDTOList, tenantId, userId, nowDate);
        Date endTime = this.getSalesEndTime(tenantContractDTOList, nowDate);
        //初始化租户数据
        Boolean initSuccess = tenantLogic.initTenantData(tenant, endTime, initMenuIds);
        if (!initSuccess) {
            throw new ServiceException("初始化数据异常，请联系管理员");
        }
        tenant.setTenantStatus(BusinessConstants.NORMAL);
        tenantMapper.updateTenant(tenant);
        this.setTenantIdsCache();
        //返回数据
        tenantDTO.setTenantId(tenantId);
        return tenantDTO;
    }

    /**
     * @description: 注册租户用户
     * @Author: hzk
     * @date: 2023/2/21 15:39
     * @param: [tenantDTO]
     * @return: net.qixiaowei.system.manage.api.vo.tenant.TenantRegisterResponseVO
     **/
    @Override
    public TenantRegisterResponseVO registerUserInfo(TenantDTO tenantDTO) {
        //校验行业
        this.checkIndustry(tenantDTO);
        //租户
        String domain = this.getDomain();
        //找到客服人员
        String supportStaff = this.getSupportStaff();
        String tenantCode = this.generateTenantCode();
        Tenant tenant = new Tenant();
        Long userId = SecurityUtils.getUserId();
        Date nowDate = DateUtils.getNowDate();
        BeanUtils.copyProperties(tenantDTO, tenant);
        tenant.setTenantCode(tenantCode);
        tenant.setDomain(domain);
        tenant.setSupportStaff(supportStaff);
        tenant.setCreateBy(userId);
        tenant.setUpdateBy(userId);
        tenant.setCreateTime(nowDate);
        tenant.setUpdateTime(nowDate);
        tenant.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        tenant.setTenantStatus(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        //插入租户
        tenantMapper.insertTenant(tenant);
        Long tenantId = tenant.getTenantId();
        //构建试用合同
        List<TenantContractDTO> tenantContractDTOList = this.getTenantContractS(nowDate);
        Set<Long> initMenuIds = this.saveTenantContract(tenantContractDTOList, tenantId, userId, nowDate);
        Date endTime = this.getSalesEndTime(tenantContractDTOList, nowDate);
        //初始化租户数据
        Boolean initSuccess = tenantLogic.initTenantData(tenant, endTime, initMenuIds);
        if (initSuccess) {
            tenant.setTenantStatus(BusinessConstants.NORMAL);
            tenantMapper.updateTenant(tenant);
        }
        TenantRegisterResponseVO tenantRegisterResponseVO = new TenantRegisterResponseVO();
        tenantRegisterResponseVO.setDomain(domain);
        return tenantRegisterResponseVO;
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
        String domain = getDomain(tenantDTO);
        Long tenantId = tenantDTO.getTenantId();
        TenantDTO tenantOfDB = tenantMapper.selectTenantByTenantId(tenantId);
        if (StringUtils.isNull(tenantOfDB)) {
            throw new ServiceException("企业不存在");
        }
        if (!StringUtils.equalsIgnoreCase(domain, tenantOfDB.getDomain())) {
            this.checkDomain(domain);
        }
        String tenantCode = tenantDTO.getTenantCode();
        if (StringUtils.isNotEmpty(tenantCode) && !tenantCode.equals(tenantOfDB.getTenantCode())) {
            TenantDTO selectTenantByTenantCode = tenantMapper.selectTenantByTenantCode(tenantCode);
            if (StringUtils.isNotNull(selectTenantByTenantCode)) {
                throw new ServiceException("企业编码已存在");
            }
        }
        Integer tenantStatus = tenantDTO.getTenantStatus();
        Long userId = SecurityUtils.getUserId();
        Date nowDate = DateUtils.getNowDate();
        //编辑租户联系人
        this.handleEditTenantContacts(tenantDTO, userId, nowDate);
        //编辑合同
        Set<Long> initMenuIds = this.handleEditTenantContract(tenantDTO, userId, nowDate);
        List<TenantContractDTO> tenantContractDTOList = tenantDTO.getTenantContractDTOList();
        Date endTime = this.getSalesEndTime(tenantContractDTOList, nowDate);
        //更新租户信息
        Tenant updateTenant = new Tenant();
        BeanUtils.copyProperties(tenantDTO, updateTenant);
        //权限变更，需要处理
        if (!TenantStatus.DISABLE.getCode().equals(tenantStatus)) {
            tenantLogic.updateTenantAuth(updateTenant, initMenuIds, endTime);
        }
        int i;
        //如果当前租户状态是过期。新增加目前能使用的合同，则将租户的状态修改为正常
        if (TenantStatus.OVERDUE.getCode().equals(tenantOfDB.getTenantStatus()) && null != initMenuIds) {
            updateTenant.setTenantStatus(TenantStatus.NORMAL.getCode());
        }
        updateTenant.setDomain(domain);
        //管理员修改租户，不更改：租户登录背景图片URL、租户logo图片URL
        updateTenant.setLoginBackground(null);
        updateTenant.setTenantLogo(null);
        updateTenant.setUpdateBy(userId);
        updateTenant.setUpdateTime(nowDate);
        i = tenantMapper.updateTenant(updateTenant);
        this.setTenantIdsCache();
        return i;
    }

    /**
     * 初始化租户-销售云
     *
     * @return
     */
    @Override
    public void initTenantSales(Long tenantId) {
        //找到租户
        TenantDTO tenantDTO = tenantMapper.selectTenantByTenantId(tenantId);
        if (StringUtils.isNull(tenantDTO)) {
            throw new ServiceException("初始化失败，租户不存在");
        }
        //租户授权
        List<TenantContractDTO> tenantContractDTOS = tenantContractMapper.selectTenantContractByTenantId(tenantId);
        this.handleResponseOfTenantContract(tenantContractDTOS);
        Date nowDate = DateUtils.getNowDate();
        Date endTime = this.getSalesEndTime(tenantContractDTOS, nowDate);
        //初始化租户数据
        Tenant tenant = new Tenant();
        BeanUtils.copyProperties(tenantDTO, tenant);
        //走初始化
        tenantLogic.initTenantSales(tenant, endTime);
    }

    /**
     * 初始化租户-销售云基础信息（人员、部门）
     *
     * @return
     */
    @Override
    public void initTenantSalesBase(Long tenantId) {
        tenantLogic.initTenantSalesBase(tenantId);
    }

    /**
     * @description: 处理编辑租户合同
     * @Author: hzk
     * @date: 2023/2/1 17:12
     * @param: [tenantDTO, userId, nowDate]
     * @return: void
     **/
    private Set<Long> handleEditTenantContract(TenantDTO tenantDTO, Long userId, Date nowDate) {
        Set<Long> initMenuIds = null;
        List<TenantContractDTO> tenantContractDTOList = tenantDTO.getTenantContractDTOList();
        Long tenantId = tenantDTO.getTenantId();
        if (StringUtils.isNotEmpty(tenantContractDTOList)) {
            List<TenantContractDTO> addTenantContractList = new ArrayList<>();
            for (TenantContractDTO tenantContractDTO : tenantContractDTOList) {
                Long tenantContractId = tenantContractDTO.getTenantContractId();
                Set<Long> menuIds = tenantContractDTO.getMenuIds();
                menuIds = this.removeAdminMenu(menuIds);
                String productPackage = tenantContractDTO.getProductPackage();
                if (StringUtils.isNull(tenantContractId)) {
                    //添加到新增租户合同
                    addTenantContractList.add(tenantContractDTO);
                } else {
                    //编辑租户合同
                    TenantContract tenantContract = new TenantContract();
                    tenantContract.setTenantContractId(tenantContractId);
                    tenantContract.setProductPackage(productPackage);
                    tenantContract.setUpdateBy(userId);
                    tenantContract.setUpdateTime(nowDate);
                    tenantContractMapper.updateTenantContract(tenantContract);
                    //更新授权
                    tenantContractAuthService.updateTenantContractAuth(tenantContractId, menuIds);
                    Date contractStartTime = tenantContractDTO.getContractStartTime();
                    Date contractEndTime = tenantContractDTO.getContractEndTime();
                    //合同结束时间大于现在且合同开始时间小于现在的，才初始化菜单
                    if (this.containContractTime(nowDate, contractStartTime, contractEndTime)) {
                        if (StringUtils.isEmpty(initMenuIds)) {
                            initMenuIds = new HashSet<>();
                        }
                        if (StringUtils.isNotEmpty(menuIds)) {
                            if (null != initMenuIds) {
                                initMenuIds.addAll(menuIds);
                            }
                        }
                    }
                }
            }
            if (StringUtils.isNotEmpty(addTenantContractList)) {
                //新增租户合同
                Set<Long> addMenuIds = this.saveTenantContract(addTenantContractList, tenantId, userId, nowDate);
                if (null != addMenuIds) {
                    if (StringUtils.isEmpty(initMenuIds)) {
                        initMenuIds = new HashSet<>();
                    }
                    if (null != initMenuIds) {
                        if (StringUtils.isNotEmpty(addMenuIds)) {
                            initMenuIds.addAll(addMenuIds);
                        }
                    }
                }
            }
        }
        return initMenuIds;
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
    @Override
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
    @Override
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

    /**
     * 租户查询自己的登录界面信息
     */
    @Override
    public TenantLoginFormVO queryTenantLoginForm(HttpServletRequest request) {
        TenantLoginFormVO tenantLoginFormVO = new TenantLoginFormVO();
        String loginBackground = "";
        String serverName = StringUtils.isNotEmpty(request.getHeader("proxyHost")) ? request.getHeader("proxyHost") : request.getServerName();
        if (StringUtils.isNotEmpty(serverName)) {
            serverName = serverName.replace("." + tenantConfig.getMainDomain(), "");
            if (StringUtils.isNotEmpty(serverName) && !"www".equals(serverName)) {
                TenantDTO tenantDTO = tenantMapper.selectTenantByDomain(serverName);
                if (StringUtils.isNotNull(tenantDTO)) {
                    loginBackground = fileConfig.getFullDomain(tenantDTO.getLoginBackground());
                }
            }
        }
        tenantLoginFormVO.setLoginBackground(loginBackground);
        return tenantLoginFormVO;
    }

    /**
     * 租户查询自己的企业信息
     *
     * @return
     */
    @Override
    public TenantInfoVO queryTenantInfoOfSelf() {
        Long tenantId = SecurityUtils.getTenantId();
        TenantDTO tenantDTO = tenantMapper.selectTenantByTenantId(tenantId);
        if (StringUtils.isNull(tenantDTO)) {
            throw new ServiceException("企业不存在");
        }
        String domain = tenantDTO.getDomain();
        TenantInfoVO tenantInfoVO = new TenantInfoVO();
        tenantInfoVO.setTenantId(tenantId);
        tenantInfoVO.setTenantName(tenantDTO.getTenantName());
        tenantInfoVO.setTenantAddress(tenantDTO.getTenantAddress());
        tenantInfoVO.setTenantStatus(tenantDTO.getTenantStatus());
        //租户登录背景图片URL
        tenantInfoVO.setLoginBackground(fileConfig.getFullDomain(tenantDTO.getLoginBackground()));
        //租户logo图片URL
        tenantInfoVO.setTenantLogo(fileConfig.getFullDomain(tenantDTO.getTenantLogo()));
        //行业
        Long tenantIndustry = tenantDTO.getTenantIndustry();
        if (StringUtils.isNotNull(tenantIndustry)) {
            tenantInfoVO.setTenantIndustry(tenantIndustry);
            IndustryDefaultDTO industryDefaultDTO = industryDefaultMapper.selectIndustryDefaultByIndustryId(tenantDTO.getTenantIndustry());
            if (StringUtils.isNotNull(industryDefaultDTO)) {
                //行业名称
                tenantInfoVO.setTenantIndustryName(industryDefaultDTO.getIndustryName());
            }
        }
        //租赁模块
        List<TenantContractDTO> tenantContractDTOS = tenantContractMapper.selectTenantContractByTenantId(tenantId);
        //合同时间
        if (StringUtils.isNotEmpty(tenantContractDTOS)) {
            //合同开始时间
            tenantInfoVO.setContractStartTime(tenantContractDTOS.get(tenantContractDTOS.size() - 1).getContractStartTime());
            //合同结束时间
            tenantInfoVO.setContractEndTime(tenantContractDTOS.get(tenantContractDTOS.size() - 1).getContractEndTime());
        }
        //租户域名申请待审核
        TenantDomainApprovalDTO tenantDomainApprovalByWaiting = tenantDomainApprovalMapper.getTenantDomainApprovalByWaiting(tenantId);
        if (StringUtils.isNotNull(tenantDomainApprovalByWaiting)) {
            //如果存在待审核的，取审核中的域名
            tenantInfoVO.setHadApprovalDomain(true);
            domain = tenantDomainApprovalByWaiting.getApprovalDomain();
        }
        tenantInfoVO.setDomain(domain);
        return tenantInfoVO;
    }

    /**
     * 租户修改自己的企业信息
     *
     * @return
     */
    @Override
    public int updateMyTenant(TenantDTO tenantDTO) {
        Long tenantId = SecurityUtils.getTenantId();
        TenantDTO tenantByDB = tenantMapper.selectTenantByTenantId(tenantId);
        if (StringUtils.isNull(tenantByDB)) {
            throw new ServiceException("企业不存在");
        }
        if (!BusinessConstants.NORMAL.equals(tenantByDB.getTenantStatus())) {
            throw new ServiceException("修改企业信息失败:企业状态异常");
        }
        String domain = getDomain(tenantDTO);
        ;
        Long userId = SecurityUtils.getUserId();
        String userAccount = SecurityUtils.getUserAccount();
        Date nowDate = DateUtils.getNowDate();
        //查询是否有待审核的，没有待审核的才能修改
        Integer countTenantDomainApprovalByWaiting = tenantDomainApprovalMapper.countTenantDomainApprovalByWaiting(tenantId);
        if (countTenantDomainApprovalByWaiting == 0 && !StringUtils.equalsIgnoreCase(tenantByDB.getDomain(), domain)) {
            this.checkDomain(domain);
            //对比域名是否修改 修改需要保存到域名申请表中
            TenantDomainApproval tenantDomainApproval = new TenantDomainApproval();
            //租户id
            tenantDomainApproval.setTenantId(tenantId);
            //申请域名
            tenantDomainApproval.setApprovalDomain(domain);
            //申请人用户id
            tenantDomainApproval.setApplicantUserId(userId);
            //申请人账号
            tenantDomainApproval.setApplicantUserAccount(userAccount);
            //提交时间
            tenantDomainApproval.setSubmissionTime(nowDate);
            //申请状态
            tenantDomainApproval.setApprovalStatus(BusinessConstants.DISABLE);
            //删除标记
            tenantDomainApproval.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            tenantDomainApproval.setCreateBy(userId);
            tenantDomainApproval.setUpdateBy(userId);
            tenantDomainApproval.setCreateTime(nowDate);
            tenantDomainApproval.setUpdateTime(nowDate);
            tenantDomainApprovalMapper.insertTenantDomainApproval(tenantDomainApproval);
            // todo 开启工作流
            tenantLogic.sendBacklog(tenantDomainApproval.getTenantDomainApprovalId(), tenantByDB);
        }
        //可修改的租户信息
        Tenant tenant = new Tenant();
        tenant.setTenantId(tenantId);
        tenant.setTenantName(tenantDTO.getTenantName());
        tenant.setTenantAddress(tenantDTO.getTenantAddress());
        tenant.setTenantIndustry(tenantDTO.getTenantIndustry());
        //租户登录背景图片URL
        tenant.setLoginBackground(fileConfig.getPathOfRemoveDomain(tenantDTO.getLoginBackground()));
        //租户logo图片URL
        tenant.setTenantLogo(fileConfig.getPathOfRemoveDomain(tenantDTO.getTenantLogo()));
        tenant.setUpdateBy(userId);
        tenant.setUpdateTime(nowDate);
        return tenantMapper.updateTenant(tenant);
    }

    /**
     * @description: 获取有效租户ID集合
     * @Author: hzk
     * @date: 2022/12/16 19:43
     * @param: []
     * @return: java.util.List<java.lang.Long>
     **/
    @Override
    public List<Long> getTenantIds() {
        return this.setTenantIdsCache();
    }

    /**
     * 维护租户状态
     *
     * @return
     */
    @Override
    public void maintainTenantStatus() {
        //找到正常的租户数据
        List<Long> normalTenantIds = tenantMapper.getNormalTenantIds();
        if (StringUtils.isEmpty(normalTenantIds)) {
            return;
        }
        Long userId = SecurityUtils.getUserId();
        Date nowDate = DateUtils.getNowDate();
        for (Long tenantId : normalTenantIds) {
            //找到租户的合同
            List<TenantContractDTO> tenantContractDTOS = tenantContractMapper.selectTenantContractByTenantId(tenantId);
            if (StringUtils.isEmpty(tenantContractDTOS)) {
                return;
            }
            Set<Long> initMenuIds = null;
            boolean handleTenantStatus = true;
            for (TenantContractDTO tenantContractDTO : tenantContractDTOS) {
                Long tenantContractId = tenantContractDTO.getTenantContractId();
                Date contractStartTime = tenantContractDTO.getContractStartTime();
                Date contractEndTime = tenantContractDTO.getContractEndTime();
                if (this.containContractTime(nowDate, contractStartTime, contractEndTime)) {
                    //查找合同授权菜单
                    Set<Long> menuIds = tenantContractAuthService.selectTenantContractAuthMenuIdsByTenantContractId(tenantContractId);
                    handleTenantStatus = false;
                    if (StringUtils.isEmpty(initMenuIds)) {
                        initMenuIds = new HashSet<>();
                    }
                    if (StringUtils.isNotEmpty(menuIds)) {
                        if (null != initMenuIds) {
                            initMenuIds.addAll(menuIds);
                        }
                        tenantContractDTO.setMenuIds(menuIds);
                    }
                }
            }
            Tenant updateTenant = new Tenant();
            updateTenant.setTenantId(tenantId);
            //处理租户状态
            if (handleTenantStatus) {
                updateTenant.setTenantStatus(TenantStatus.OVERDUE.getCode());
                updateTenant.setUpdateBy(userId);
                updateTenant.setUpdateTime(nowDate);
                tenantMapper.updateTenant(updateTenant);
            }
            //处理租户合同授权
            tenantLogic.updateTenantAuth(updateTenant, initMenuIds, null);
        }
    }

    /**
     * @description: 设置租户ID集合的缓存
     * @Author: hzk
     * @date: 2022/12/16 20:01
     * @param: []
     * @return: java.util.List<java.lang.Long>
     **/
    public List<Long> setTenantIdsCache() {
        List<Long> tenantIds = tenantMapper.getTenantIds();
        if (StringUtils.isNotEmpty(tenantIds)) {
            String cacheKey = CacheConstants.TENANT_IDS_KEY;
            if (redisService.hasKey(cacheKey)) {
                redisService.deleteObject(cacheKey);
            }
            redisService.setCacheList(cacheKey, tenantIds);
        }
        return tenantIds;
    }

    /**
     * @description: 保存租户联系人
     * @Author: hzk
     * @date: 2023/1/31 16:58
     * @param: [tenantContactsDTOList, tenantId, userId, nowDate]
     * @return: void
     **/
    private void saveTenantContacts(List<TenantContactsDTO> tenantContactsDTOList, Long tenantId, Long userId, Date nowDate) {
        //租户联系人
        List<TenantContacts> tenantContactsList = new ArrayList<>();
        if (StringUtils.isNotEmpty(tenantContactsDTOList)) {
            for (TenantContactsDTO tenantContactsDTO : tenantContactsDTOList) {
                TenantContacts tenantContacts = new TenantContacts();
                BeanUtils.copyProperties(tenantContactsDTO, tenantContacts);
                //租户id
                tenantContacts.setTenantId(tenantId);
                tenantContacts.setCreateTime(nowDate);
                tenantContacts.setUpdateTime(nowDate);
                tenantContacts.setCreateBy(userId);
                tenantContacts.setUpdateBy(userId);
                tenantContacts.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                tenantContactsList.add(tenantContacts);
            }
        }
        if (StringUtils.isNotEmpty(tenantContactsList)) {
            //插入租户联系人
            tenantContactsMapper.batchTenantContacts(tenantContactsList);
        }
    }

    /**
     * @description: 保存租户合同信息
     * @Author: hzk
     * @date: 2023/1/31 18:08
     * @param: [tenantContractDTOList, tenantId, userId, nowDate]
     * @return: void
     **/
    private Set<Long> saveTenantContract(List<TenantContractDTO> tenantContractDTOList, Long tenantId, Long userId, Date nowDate) {
        Set<Long> initMenuIds = null;
        //租户合同
        if (StringUtils.isNotEmpty(tenantContractDTOList)) {
            for (TenantContractDTO tenantContractDTO : tenantContractDTOList) {
                TenantContract tenantContract = new TenantContract();
                BeanUtils.copyProperties(tenantContractDTO, tenantContract);
                //租户id
                tenantContract.setTenantId(tenantId);
                tenantContract.setCreateTime(nowDate);
                tenantContract.setUpdateTime(nowDate);
                tenantContract.setCreateBy(userId);
                tenantContract.setUpdateBy(userId);
                tenantContract.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                tenantContractMapper.insertTenantContract(tenantContract);
                Long tenantContractId = tenantContract.getTenantContractId();
                //保存合同授权
                Set<Long> menuIds = tenantContractDTO.getMenuIds();
                menuIds = this.removeAdminMenu(menuIds);
                tenantContractAuthService.insertTenantContractAuth(tenantContractId, menuIds);
                Date contractStartTime = tenantContractDTO.getContractStartTime();
                Date contractEndTime = tenantContractDTO.getContractEndTime();
                //合同结束时间大于现在且合同开始时间小于现在的，才初始化菜单
                if (this.containContractTime(nowDate, contractStartTime, contractEndTime)) {
                    if (StringUtils.isEmpty(initMenuIds)) {
                        initMenuIds = new HashSet<>();
                    }
                    if (StringUtils.isNotEmpty(menuIds)) {
                        if (null != initMenuIds) {
                            initMenuIds.addAll(menuIds);
                        }
                    }
                }
            }
        }
        return initMenuIds;
    }

    /**
     * @description: 去除管理员菜单
     * @Author: hzk
     * @date: 2023/2/6 14:16
     * @param: [menuIds]
     * @return: java.util.Set<java.lang.Long>
     **/
    private Set<Long> removeAdminMenu(Set<Long> menuIds) {
        if (StringUtils.isNotEmpty(menuIds)) {
            Set<Long> adminMenuIds = tenantConfig.getAdminMenuIds();
            menuIds = menuIds.stream().filter(menuId -> !adminMenuIds.contains(menuId)).collect(Collectors.toSet());
        }
        return menuIds;
    }

    /**
     * @description: 处理返回的租户合同
     * @Author: hzk
     * @date: 2023/2/1 11:00
     * @param: [tenantContractDTOS]
     * @return: void
     **/
    private void handleResponseOfTenantContract(List<TenantContractDTO> tenantContractDTOS) {
        if (StringUtils.isNotEmpty(tenantContractDTOS)) {
            for (TenantContractDTO tenantContractDTO : tenantContractDTOS) {
                Long tenantContractId = tenantContractDTO.getTenantContractId();
                Set<Long> selectMenuListByTenantContractId = menuService.selectMenuListByTenantContractId(tenantContractId);
                tenantContractDTO.setMenuIds(selectMenuListByTenantContractId);
            }
        }
    }

    /**
     * @description: 处理编辑租户联系人信息
     * @Author: hzk
     * @date: 2023/2/1 16:27
     * @param: [tenantDTO, userId, nowDate]
     * @return: void
     **/
    private void handleEditTenantContacts(TenantDTO tenantDTO, Long userId, Date nowDate) {
        List<TenantContactsDTO> tenantContactsDTOList = tenantDTO.getTenantContactsDTOList();
        Long tenantId = tenantDTO.getTenantId();
        //新增集合
        List<TenantContactsDTO> tenantContactsAddList = new ArrayList<>();
        //修改集合
        List<TenantContacts> tenantContactsUpdateList = new ArrayList<>();
        //查找旧的合同ID集合
        Set<Long> tenantContactsIds = tenantContactsMapper.selectTenantContactsIdsByTenantId(tenantId);
        if (StringUtils.isNotEmpty(tenantContactsIds) && StringUtils.isNotEmpty(tenantContactsDTOList)) {
            for (TenantContactsDTO tenantContactsDTO : tenantContactsDTOList) {
                Long tenantContactsId = tenantContactsDTO.getTenantContactsId();
                if (StringUtils.isNull(tenantContactsId)) {
                    //添加进新增集合
                    tenantContactsAddList.add(tenantContactsDTO);
                } else {
                    if (tenantContactsIds.contains(tenantContactsId)) {
                        TenantContacts tenantContacts = new TenantContacts();
                        BeanUtils.copyProperties(tenantContactsDTO, tenantContacts);
                        tenantContacts.setUpdateBy(userId);
                        tenantContacts.setUpdateTime(nowDate);
                        //添加进修改集合
                        tenantContactsUpdateList.add(tenantContacts);
                        //删除的集合去掉编辑的ID
                        tenantContactsIds.remove(tenantContactsId);
                    }
                }
            }
        }
        //新增
        this.saveTenantContacts(tenantContactsAddList, tenantId, userId, nowDate);
        //编辑
        if (StringUtils.isNotEmpty(tenantContactsUpdateList)) {
            tenantContactsMapper.updateTenantContactss(tenantContactsUpdateList);
        }
        //删除
        if (StringUtils.isNotEmpty(tenantContactsIds)) {
            tenantContactsMapper.logicDeleteTenantContactsByTenantContactsIds(new ArrayList<>(tenantContactsIds), userId, nowDate);
        }
    }

    /**
     * @description: 当前时间是否在合同时间内
     * @Author: hzk
     * @date: 2023/2/3 15:07
     * @param: [nowDate, contractStartTime, contractEndTime]
     * @return: boolean
     **/
    private boolean containContractTime(Date nowDate, Date contractStartTime, Date contractEndTime) {
        boolean containContractTime = false;
        if (StringUtils.isNotNull(contractEndTime) && StringUtils.isNotNull(contractStartTime)) {
            //结束时间为当天23:59:59
            contractEndTime = DateUtil.endOfDay(contractEndTime);
            if (!contractEndTime.before(nowDate) && !contractStartTime.after(nowDate)) {
                containContractTime = true;
            }
        }
        return containContractTime;
    }

    /**
     * @description: 构建试用合同
     * @Author: hzk
     * @date: 2023/2/21 15:35
     * @param: [nowDate]
     * @return: java.util.List<net.qixiaowei.system.manage.api.dto.tenant.TenantContractDTO>
     **/
    private List<TenantContractDTO> getTenantContractS(Date nowDate) {
        //初始化合同
        TenantContractDTO tenantContractDTO = new TenantContractDTO();
        //申请日期（YYMMDDHHmmss）
        String salesContractNo = DateUtils.dateTimeNow();
        //需要初始化的菜单
        Set<Long> menuIds = menuService.selectMenuIdsAll(true);
        //用户成功申请日期起，至后续7个自然日止。
        Date contractEndTime = DateUtils.addDays(nowDate, Optional.ofNullable(tenantConfig.getTrialDays()).orElse(7));
        tenantContractDTO.setMenuIds(menuIds);
        tenantContractDTO.setSalesContractNo(salesContractNo);
        tenantContractDTO.setSalesPersonnel("官网试用");
        tenantContractDTO.setContractAmount(BigDecimal.ZERO);
        tenantContractDTO.setContractStartTime(nowDate);
        tenantContractDTO.setContractEndTime(contractEndTime);
        return Collections.singletonList(tenantContractDTO);
    }

    /**
     * @description: 获取系统客服人员
     * @Author: hzk
     * @date: 2023/2/21 15:38
     * @param: []
     * @return: java.lang.String
     **/
    private String getSupportStaff() {
        String supportStaffMobile = tenantConfig.getSupportStaffMobile();
        UserDTO userDTO = userMapper.selectUserByUserAccount(supportStaffMobile);
        if (StringUtils.isNull(userDTO)) {
            throw new ServiceException("您本次的系统注册失败，请联系客服查询原因。");
        }
        Long employeeId = userDTO.getEmployeeId();
        if (StringUtils.isNull(employeeId)) {
            throw new ServiceException("您本次的系统注册失败，请联系客服查询原因。");
        }
        return employeeId.toString();
    }

    /**
     * @description: 生成域名
     * @Author: hzk
     * @date: 2023/2/21 16:09
     * @param: []
     * @return: java.lang.String
     **/
    private String getDomain() {
        String domain = RandomUtil.randomString(RandomUtil.BASE_CHAR, 8);
        TenantDTO tenantByDomain;
        do {
            tenantByDomain = tenantMapper.selectTenantByDomain(domain);
            if (StringUtils.isNotNull(tenantByDomain)) {
                domain = RandomUtil.randomString(RandomUtil.BASE_CHAR, 8);
            }
        } while (StringUtils.isNotNull(tenantByDomain));
        return domain;
    }

    /**
     * @description: 校验域名
     * @Author: hzk
     * @date: 2023/2/21 16:34
     * @param: [domain]
     * @return: void
     **/
    private void checkDomain(String domain) {
        // 正则条件
        String canonical = "^[0-9a-zA-Z-]{3,30}$";
        if (domain.startsWith(StrUtil.DASHED) || !ReUtil.isMatch(canonical, domain)) {
            throw new ServiceException("域名由3-30位字母、数字、中划线组成，不能以中划线开头");
        }
        if (tenantConfig.getExistedDomains().contains(domain)) {
            throw new ServiceException("域名已存在");
        }
        TenantDTO tenantByDomain = tenantMapper.selectTenantByDomain(domain);
        if (StringUtils.isNotNull(tenantByDomain)) {
            throw new ServiceException("域名已存在");

        }
    }

    /**
     * @description: 校验行业
     * @Author: hzk
     * @date: 2023/2/28 9:26
     * @param: [tenantDTO]
     * @return: void
     **/
    private void checkIndustry(TenantDTO tenantDTO) {
        Long tenantIndustry = tenantDTO.getTenantIndustry();
        IndustryDefaultDTO industryDefaultDTO = industryDefaultMapper.selectIndustryDefaultByIndustryId(tenantIndustry);
        if (StringUtils.isNull(industryDefaultDTO)) {
            throw new ServiceException("行业不存在");
        }
    }

    /**
     * @description: 域名获取-转为小写
     * @Author: hzk
     * @date: 2023/3/6 16:12
     * @param: [tenantDTO]
     * @return: java.lang.String
     **/
    private static String getDomain(TenantDTO tenantDTO) {
        String domain = tenantDTO.getDomain();
        if (StringUtils.isNotEmpty(domain)) {
            domain = domain.toLowerCase();
        }
        return domain;
    }

    /**
     * @description: 获取销售云结束时间
     * @Author: hzk
     * @date: 2023/4/4 11:50
     * @param: [tenantContractDTOList, nowDate]
     * @return: java.util.Date
     **/
    private Date getSalesEndTime(List<TenantContractDTO> tenantContractDTOList, Date nowDate) {
        Date endTime = DateUtils.getNowDate();
        //租户合同
        if (StringUtils.isNotEmpty(tenantContractDTOList)) {
            for (TenantContractDTO tenantContractDTO : tenantContractDTOList) {
                //获取合同授权
                Set<Long> menuIds = tenantContractDTO.getMenuIds();
                if (StringUtils.isNotEmpty(menuIds)) {
                    for (Long menuId : menuIds) {
                        if (TenantLogic.SALES_MENUS.contains(menuId)) {
                            Date contractStartTime = tenantContractDTO.getContractStartTime();
                            Date contractEndTime = tenantContractDTO.getContractEndTime();
                            //合同结束时间大于现在且合同开始时间小于现在的，才初始化菜单
                            if (this.containContractTime(nowDate, contractStartTime, contractEndTime)) {
                                if (endTime.before(contractEndTime)) {
                                    endTime = contractEndTime;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return DateUtil.endOfDay(endTime);
    }
}

