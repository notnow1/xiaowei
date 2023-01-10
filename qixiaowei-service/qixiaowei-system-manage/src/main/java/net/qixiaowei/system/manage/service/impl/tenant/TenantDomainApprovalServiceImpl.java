package net.qixiaowei.system.manage.service.impl.tenant;

import java.util.*;

import cn.hutool.json.JSONUtil;
import net.qixiaowei.integration.common.constant.BusinessConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.enums.message.BusinessSubtype;
import net.qixiaowei.integration.common.enums.message.MessageType;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.tenant.utils.TenantUtils;
import net.qixiaowei.message.api.dto.backlog.BacklogDTO;
import net.qixiaowei.message.api.dto.message.MessageReceiverDTO;
import net.qixiaowei.message.api.dto.message.MessageSendDTO;
import net.qixiaowei.message.api.remote.backlog.RemoteBacklogService;
import net.qixiaowei.message.api.remote.message.RemoteMessageService;
import net.qixiaowei.system.manage.api.domain.tenant.Tenant;
import net.qixiaowei.system.manage.mapper.tenant.TenantMapper;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.domain.tenant.TenantDomainApproval;
import net.qixiaowei.system.manage.api.dto.tenant.TenantDomainApprovalDTO;
import net.qixiaowei.system.manage.mapper.tenant.TenantDomainApprovalMapper;
import net.qixiaowei.system.manage.service.tenant.ITenantDomainApprovalService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


/**
 * TenantDomainApprovalService业务层处理
 *
 * @author TANGMICHI
 * @since 2022-10-09
 */
@Service
public class TenantDomainApprovalServiceImpl implements ITenantDomainApprovalService {
    @Autowired
    private TenantDomainApprovalMapper tenantDomainApprovalMapper;

    @Autowired
    private RemoteMessageService remoteMessageService;

    @Autowired
    private TenantMapper tenantMapper;

    @Autowired
    private RemoteBacklogService remoteBacklogService;


    /**
     * 处理租户域名申请
     *
     * @param tenantDomainApprovalDTO 租户域名申请
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int process(TenantDomainApprovalDTO tenantDomainApprovalDTO) {
        Long tenantDomainApprovalId = tenantDomainApprovalDTO.getTenantDomainApprovalId();
        Integer approvalStatus = tenantDomainApprovalDTO.getApprovalStatus();
        if (!Arrays.asList(1, 2).contains(approvalStatus)) {
            throw new ServiceException("域名申请处理状态非法");
        }
        TenantDomainApprovalDTO tenantDomainApproval = tenantDomainApprovalMapper.selectTenantDomainApprovalByTenantDomainApprovalId(tenantDomainApprovalId);
        if (StringUtils.isNull(tenantDomainApproval)) {
            throw new ServiceException("找不到域名申请记录");
        }
        if (!BusinessConstants.DISABLE.equals(tenantDomainApproval.getApprovalStatus())) {
            throw new ServiceException("域名申请已处理。");
        }
        String approvalDomain = tenantDomainApproval.getApprovalDomain();
        Long userId = SecurityUtils.getUserId();
        Date nowDate = DateUtils.getNowDate();
        TenantDomainApproval updateTenantDomainApproval = new TenantDomainApproval();
        updateTenantDomainApproval.setTenantDomainApprovalId(tenantDomainApprovalId);
        updateTenantDomainApproval.setApprovalTime(nowDate);
        updateTenantDomainApproval.setApprovalUserId(userId);
        updateTenantDomainApproval.setApprovalStatus(approvalStatus);
        updateTenantDomainApproval.setUpdateBy(userId);
        updateTenantDomainApproval.setUpdateTime(nowDate);
        int update = tenantDomainApprovalMapper.updateTenantDomainApproval(updateTenantDomainApproval);
        MessageSendDTO messageSendDTO = new MessageSendDTO();
        messageSendDTO.setMessageType(MessageType.PRIVATE_MESSAGE.getCode());
        Integer businessType;
        Integer businessSubtype;
        messageSendDTO.setBusinessId(tenantDomainApprovalId);
        messageSendDTO.setSendUserId(userId);
        messageSendDTO.setMessageTitle(BusinessSubtype.TENANT_DOMAIN_APPROVAL.getInfo());
        messageSendDTO.setHandleContent(true);
        List<MessageReceiverDTO> messageReceivers = new ArrayList<>();
        MessageReceiverDTO messageReceiverDTO = new MessageReceiverDTO();
        messageReceiverDTO.setUserId(tenantDomainApproval.getApplicantUserId());
        messageReceivers.add(messageReceiverDTO);
        messageSendDTO.setMessageReceivers(messageReceivers);
        Long tenantId = tenantDomainApproval.getTenantId();
        //审核通过
        if (approvalStatus.equals(1)) {
            Tenant tenant = new Tenant();
            tenant.setTenantId(tenantId);
            tenant.setDomain(approvalDomain);
            tenant.setUpdateBy(userId);
            tenant.setUpdateTime(nowDate);
            //租户新域名生效
            tenantMapper.updateTenant(tenant);
            Map<String, Object> paramMap = new HashMap<>();
            //域名生效时间
            paramMap.put("validity_time_domain", DateUtils.getCNOfTime(nowDate));
            //生效域名
            paramMap.put("domain", approvalDomain);
            String messageParam = JSONUtil.toJsonStr(paramMap);
            messageSendDTO.setMessageParam(messageParam);
            businessType = BusinessSubtype.TENANT_DOMAIN_APPROVAL_PASS.getParentBusinessType().getCode();
            businessSubtype = BusinessSubtype.TENANT_DOMAIN_APPROVAL_PASS.getCode();
        } else {//审核驳回
            businessType = BusinessSubtype.TENANT_DOMAIN_APPROVAL_REFUSE.getParentBusinessType().getCode();
            businessSubtype = BusinessSubtype.TENANT_DOMAIN_APPROVAL_REFUSE.getCode();
        }
        messageSendDTO.setBusinessType(businessType);
        messageSendDTO.setBusinessSubtype(businessSubtype);
        TenantUtils.execute(tenantId, () -> {
                    //给该租户的申请人发送消息
                    remoteMessageService.sendMessage(messageSendDTO, SecurityConstants.INNER);
                }
        );
        //将所有客服待办中的关联的域名任务都处理成已处理
        BacklogDTO backlogDTO = new BacklogDTO();
        backlogDTO.setBusinessType(BusinessSubtype.TENANT_DOMAIN_APPROVAL.getParentBusinessType().getCode());
        backlogDTO.setBusinessSubtype(BusinessSubtype.TENANT_DOMAIN_APPROVAL.getCode());
        backlogDTO.setBusinessId(tenantDomainApprovalId);
        remoteBacklogService.handled(backlogDTO, SecurityConstants.INNER);
        return update;
    }

    /**
     * 查询租户域名申请
     *
     * @param tenantDomainApprovalId 租户域名申请主键
     * @return 租户域名申请
     */
    @Override
    public TenantDomainApprovalDTO selectTenantDomainApprovalByTenantDomainApprovalId(Long tenantDomainApprovalId) {
        return tenantDomainApprovalMapper.selectTenantDomainApprovalByTenantDomainApprovalId(tenantDomainApprovalId);
    }

    /**
     * 查询租户域名申请列表
     *
     * @param tenantDomainApprovalDTO 租户域名申请
     * @return 租户域名申请
     */
    @Override
    public List<TenantDomainApprovalDTO> selectTenantDomainApprovalList(TenantDomainApprovalDTO tenantDomainApprovalDTO) {
        TenantDomainApproval tenantDomainApproval = new TenantDomainApproval();
        BeanUtils.copyProperties(tenantDomainApprovalDTO, tenantDomainApproval);
        return tenantDomainApprovalMapper.selectTenantDomainApprovalList(tenantDomainApproval);
    }

    /**
     * 新增租户域名申请
     *
     * @param tenantDomainApprovalDTO 租户域名申请
     * @return 结果
     */
    @Override
    public int insertTenantDomainApproval(TenantDomainApprovalDTO tenantDomainApprovalDTO) {
        TenantDomainApproval tenantDomainApproval = new TenantDomainApproval();
        BeanUtils.copyProperties(tenantDomainApprovalDTO, tenantDomainApproval);
        tenantDomainApproval.setCreateBy(SecurityUtils.getUserId());
        tenantDomainApproval.setCreateTime(DateUtils.getNowDate());
        tenantDomainApproval.setUpdateTime(DateUtils.getNowDate());
        tenantDomainApproval.setUpdateBy(SecurityUtils.getUserId());
        tenantDomainApproval.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        return tenantDomainApprovalMapper.insertTenantDomainApproval(tenantDomainApproval);
    }

    /**
     * 修改租户域名申请
     *
     * @param tenantDomainApprovalDTO 租户域名申请
     * @return 结果
     */
    @Override
    public int updateTenantDomainApproval(TenantDomainApprovalDTO tenantDomainApprovalDTO) {
        TenantDomainApproval tenantDomainApproval = new TenantDomainApproval();
        BeanUtils.copyProperties(tenantDomainApprovalDTO, tenantDomainApproval);
        tenantDomainApproval.setUpdateTime(DateUtils.getNowDate());
        tenantDomainApproval.setUpdateBy(SecurityUtils.getUserId());
        return tenantDomainApprovalMapper.updateTenantDomainApproval(tenantDomainApproval);
    }

    /**
     * 逻辑批量删除租户域名申请
     *
     * @param tenantDomainApprovalDtos 需要删除的租户域名申请主键
     * @return 结果
     */
    @Override
    public int logicDeleteTenantDomainApprovalByTenantDomainApprovalIds(List<TenantDomainApprovalDTO> tenantDomainApprovalDtos) {
        List<Long> stringList = new ArrayList();
        for (TenantDomainApprovalDTO tenantDomainApprovalDTO : tenantDomainApprovalDtos) {
            stringList.add(tenantDomainApprovalDTO.getTenantDomainApprovalId());
        }
        return tenantDomainApprovalMapper.logicDeleteTenantDomainApprovalByTenantDomainApprovalIds(stringList, tenantDomainApprovalDtos.get(0).getUpdateBy(), DateUtils.getNowDate());
    }

    /**
     * 物理删除租户域名申请信息
     *
     * @param tenantDomainApprovalId 租户域名申请主键
     * @return 结果
     */
    @Override
    public int deleteTenantDomainApprovalByTenantDomainApprovalId(Long tenantDomainApprovalId) {
        return tenantDomainApprovalMapper.deleteTenantDomainApprovalByTenantDomainApprovalId(tenantDomainApprovalId);
    }

    /**
     * 逻辑删除租户域名申请信息
     *
     * @param tenantDomainApprovalDTO 租户域名申请
     * @return 结果
     */
    @Override
    public int logicDeleteTenantDomainApprovalByTenantDomainApprovalId(TenantDomainApprovalDTO tenantDomainApprovalDTO) {
        TenantDomainApproval tenantDomainApproval = new TenantDomainApproval();
        tenantDomainApproval.setTenantDomainApprovalId(tenantDomainApprovalDTO.getTenantDomainApprovalId());
        tenantDomainApproval.setUpdateTime(DateUtils.getNowDate());
        tenantDomainApproval.setUpdateBy(SecurityUtils.getUserId());
        return tenantDomainApprovalMapper.logicDeleteTenantDomainApprovalByTenantDomainApprovalId(tenantDomainApproval);
    }

    /**
     * 物理删除租户域名申请信息
     *
     * @param tenantDomainApprovalDTO 租户域名申请
     * @return 结果
     */

    @Override
    public int deleteTenantDomainApprovalByTenantDomainApprovalId(TenantDomainApprovalDTO tenantDomainApprovalDTO) {
        TenantDomainApproval tenantDomainApproval = new TenantDomainApproval();
        BeanUtils.copyProperties(tenantDomainApprovalDTO, tenantDomainApproval);
        return tenantDomainApprovalMapper.deleteTenantDomainApprovalByTenantDomainApprovalId(tenantDomainApproval.getTenantDomainApprovalId());
    }

    /**
     * 物理批量删除租户域名申请
     *
     * @param tenantDomainApprovalDtos 需要删除的租户域名申请主键
     * @return 结果
     */

    @Override
    public int deleteTenantDomainApprovalByTenantDomainApprovalIds(List<TenantDomainApprovalDTO> tenantDomainApprovalDtos) {
        List<Long> stringList = new ArrayList();
        for (TenantDomainApprovalDTO tenantDomainApprovalDTO : tenantDomainApprovalDtos) {
            stringList.add(tenantDomainApprovalDTO.getTenantDomainApprovalId());
        }
        return tenantDomainApprovalMapper.deleteTenantDomainApprovalByTenantDomainApprovalIds(stringList);
    }

    /**
     * 批量新增租户域名申请信息
     *
     * @param tenantDomainApprovalDtos 租户域名申请对象
     */

    @Override
    public int insertTenantDomainApprovals(List<TenantDomainApprovalDTO> tenantDomainApprovalDtos) {
        List<TenantDomainApproval> tenantDomainApprovalList = new ArrayList();

        for (TenantDomainApprovalDTO tenantDomainApprovalDTO : tenantDomainApprovalDtos) {
            TenantDomainApproval tenantDomainApproval = new TenantDomainApproval();
            BeanUtils.copyProperties(tenantDomainApprovalDTO, tenantDomainApproval);
            tenantDomainApproval.setCreateBy(SecurityUtils.getUserId());
            tenantDomainApproval.setCreateTime(DateUtils.getNowDate());
            tenantDomainApproval.setUpdateTime(DateUtils.getNowDate());
            tenantDomainApproval.setUpdateBy(SecurityUtils.getUserId());
            tenantDomainApproval.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            tenantDomainApprovalList.add(tenantDomainApproval);
        }
        return tenantDomainApprovalMapper.batchTenantDomainApproval(tenantDomainApprovalList);
    }

    /**
     * 批量修改租户域名申请信息
     *
     * @param tenantDomainApprovalDtos 租户域名申请对象
     */

    @Override
    public int updateTenantDomainApprovals(List<TenantDomainApprovalDTO> tenantDomainApprovalDtos) {
        List<TenantDomainApproval> tenantDomainApprovalList = new ArrayList();

        for (TenantDomainApprovalDTO tenantDomainApprovalDTO : tenantDomainApprovalDtos) {
            TenantDomainApproval tenantDomainApproval = new TenantDomainApproval();
            BeanUtils.copyProperties(tenantDomainApprovalDTO, tenantDomainApproval);
            tenantDomainApproval.setCreateBy(SecurityUtils.getUserId());
            tenantDomainApproval.setCreateTime(DateUtils.getNowDate());
            tenantDomainApproval.setUpdateTime(DateUtils.getNowDate());
            tenantDomainApproval.setUpdateBy(SecurityUtils.getUserId());
            tenantDomainApprovalList.add(tenantDomainApproval);
        }
        return tenantDomainApprovalMapper.updateTenantDomainApprovals(tenantDomainApprovalList);
    }
}

