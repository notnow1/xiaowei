package net.qixiaowei.system.manage.service.impl.tenant;

import java.util.*;

import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.domain.tenant.TenantContractAuth;
import net.qixiaowei.system.manage.api.dto.tenant.TenantContractAuthDTO;
import net.qixiaowei.system.manage.mapper.tenant.TenantContractAuthMapper;
import net.qixiaowei.system.manage.service.tenant.ITenantContractAuthService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


/**
 * TenantContractAuthService业务层处理
 *
 * @author hzk
 * @since 2023-01-31
 */
@Service
public class TenantContractAuthServiceImpl implements ITenantContractAuthService {
    @Autowired
    private TenantContractAuthMapper tenantContractAuthMapper;


    /**
     * 新增合同授权菜单信息
     *
     * @param tenantContractId 租户合同ID
     * @param menuIds          菜单集合
     */
    @Override
    public void insertTenantContractAuth(Long tenantContractId, Set<Long> menuIds) {
        if (StringUtils.isEmpty(menuIds) || StringUtils.isNull(tenantContractId)) {
            return;
        }
        Long userId = SecurityUtils.getUserId();
        Date nowDate = DateUtils.getNowDate();
        // 新增用户与角色管理
        List<TenantContractAuth> list = new ArrayList<>();
        for (Long menuId : menuIds) {
            TenantContractAuth tenantContractAuth = new TenantContractAuth();
            tenantContractAuth.setTenantContractId(tenantContractId);
            tenantContractAuth.setMenuId(menuId);
            tenantContractAuth.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            tenantContractAuth.setCreateBy(userId);
            tenantContractAuth.setCreateTime(nowDate);
            tenantContractAuth.setUpdateBy(userId);
            tenantContractAuth.setUpdateTime(nowDate);
            list.add(tenantContractAuth);
        }
        if (list.size() > 0) {
            tenantContractAuthMapper.batchTenantContractAuth(list);
        }
    }

    /**
     * 更新合同授权菜单信息
     *
     * @param tenantContractId 租户合同ID
     * @param menuIds          菜单集合
     */
    @Override
    public void updateTenantContractAuth(Long tenantContractId, Set<Long> menuIds) {
        //查找当前合同授权菜单
        List<TenantContractAuthDTO> tenantContractAuthDTOS = tenantContractAuthMapper.selectTenantContractAuthByTenantContractId(tenantContractId);
        if (StringUtils.isEmpty(tenantContractAuthDTOS)) {
            this.insertTenantContractAuth(tenantContractId, menuIds);
        } else { //更新合同授权菜单
            Map<Long, Long> tenantContractAuthMap = new HashMap<>();
            tenantContractAuthDTOS.forEach(tenantContractAuthDTO -> tenantContractAuthMap.put(tenantContractAuthDTO.getMenuId(), tenantContractAuthDTO.getTenantContractAuthId()));
            Long userId = SecurityUtils.getUserId();
            Date nowDate = DateUtils.getNowDate();
            Set<Long> insertMenuIds = new HashSet<>();
            if (StringUtils.isNotEmpty(menuIds)) {
                for (Long menuId : menuIds) {
                    if (tenantContractAuthMap.containsKey(menuId)) {
                        tenantContractAuthMap.remove(menuId);
                    } else {
                        insertMenuIds.add(menuId);
                    }
                }
            }
            //新增
            if (StringUtils.isNotEmpty(insertMenuIds)) {
                this.insertTenantContractAuth(tenantContractId, insertMenuIds);
            }
            if (StringUtils.isNotEmpty(tenantContractAuthMap)) {
                Set<Long> removeTenantContractAuthIds = new HashSet<>(tenantContractAuthMap.values());
                if (StringUtils.isNotEmpty(removeTenantContractAuthIds)) {
                    //执行假删除
                    tenantContractAuthMapper.logicDeleteTenantContractAuthByTenantContractAuthIds(new ArrayList<>(removeTenantContractAuthIds), userId, nowDate);
                }
            }
        }
    }

    /**
     * 查询租户合同授权表
     *
     * @param tenantContractAuthId 租户合同授权表主键
     * @return 租户合同授权表
     */
    @Override
    public TenantContractAuthDTO selectTenantContractAuthByTenantContractAuthId(Long tenantContractAuthId) {
        return tenantContractAuthMapper.selectTenantContractAuthByTenantContractAuthId(tenantContractAuthId);
    }

    /**
     * 查询租户合同授权表列表
     *
     * @param tenantContractAuthDTO 租户合同授权表
     * @return 租户合同授权表
     */
    @Override
    public List<TenantContractAuthDTO> selectTenantContractAuthList(TenantContractAuthDTO tenantContractAuthDTO) {
        TenantContractAuth tenantContractAuth = new TenantContractAuth();
        BeanUtils.copyProperties(tenantContractAuthDTO, tenantContractAuth);
        return tenantContractAuthMapper.selectTenantContractAuthList(tenantContractAuth);
    }

    /**
     * 根据租户合同ID查询租户合同授权表列表
     *
     * @param tenantContractId 租户合同ID
     * @return 租户合同授权表集合
     */
    @Override
    public List<TenantContractAuthDTO> selectTenantContractAuthsByTenantContractId(Long tenantContractId) {
        return tenantContractAuthMapper.selectTenantContractAuthByTenantContractId(tenantContractId);
    }

    /**
     * 根据租户合同ID查询租户合同授权菜单集合
     *
     * @param tenantContractId 租户合同ID
     * @return 租户合同授权菜单集合
     */
    @Override
    public Set<Long> selectTenantContractAuthMenuIdsByTenantContractId(Long tenantContractId) {
        return tenantContractAuthMapper.selectTenantContractAuthMenuIdsByTenantContractId(tenantContractId);
    }

    /**
     * 新增租户合同授权表
     *
     * @param tenantContractAuthDTO 租户合同授权表
     * @return 结果
     */
    @Override
    public TenantContractAuthDTO insertTenantContractAuth(TenantContractAuthDTO tenantContractAuthDTO) {
        TenantContractAuth tenantContractAuth = new TenantContractAuth();
        BeanUtils.copyProperties(tenantContractAuthDTO, tenantContractAuth);
        tenantContractAuth.setCreateBy(SecurityUtils.getUserId());
        tenantContractAuth.setCreateTime(DateUtils.getNowDate());
        tenantContractAuth.setUpdateTime(DateUtils.getNowDate());
        tenantContractAuth.setUpdateBy(SecurityUtils.getUserId());
        tenantContractAuth.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        tenantContractAuthMapper.insertTenantContractAuth(tenantContractAuth);
        tenantContractAuthDTO.setTenantContractAuthId(tenantContractAuth.getTenantContractAuthId());
        return tenantContractAuthDTO;
    }

    /**
     * 修改租户合同授权表
     *
     * @param tenantContractAuthDTO 租户合同授权表
     * @return 结果
     */
    @Override
    public int updateTenantContractAuth(TenantContractAuthDTO tenantContractAuthDTO) {
        TenantContractAuth tenantContractAuth = new TenantContractAuth();
        BeanUtils.copyProperties(tenantContractAuthDTO, tenantContractAuth);
        tenantContractAuth.setUpdateTime(DateUtils.getNowDate());
        tenantContractAuth.setUpdateBy(SecurityUtils.getUserId());
        return tenantContractAuthMapper.updateTenantContractAuth(tenantContractAuth);
    }

    /**
     * 逻辑批量删除租户合同授权表
     *
     * @param tenantContractAuthIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteTenantContractAuthByTenantContractAuthIds(List<Long> tenantContractAuthIds) {
        return tenantContractAuthMapper.logicDeleteTenantContractAuthByTenantContractAuthIds(tenantContractAuthIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除租户合同授权表信息
     *
     * @param tenantContractAuthId 租户合同授权表主键
     * @return 结果
     */
    @Override
    public int deleteTenantContractAuthByTenantContractAuthId(Long tenantContractAuthId) {
        return tenantContractAuthMapper.deleteTenantContractAuthByTenantContractAuthId(tenantContractAuthId);
    }

    /**
     * 逻辑删除租户合同授权表信息
     *
     * @param tenantContractAuthDTO 租户合同授权表
     * @return 结果
     */
    @Override
    public int logicDeleteTenantContractAuthByTenantContractAuthId(TenantContractAuthDTO tenantContractAuthDTO) {
        TenantContractAuth tenantContractAuth = new TenantContractAuth();
        tenantContractAuth.setTenantContractAuthId(tenantContractAuthDTO.getTenantContractAuthId());
        tenantContractAuth.setUpdateTime(DateUtils.getNowDate());
        tenantContractAuth.setUpdateBy(SecurityUtils.getUserId());
        return tenantContractAuthMapper.logicDeleteTenantContractAuthByTenantContractAuthId(tenantContractAuth);
    }

    /**
     * 物理删除租户合同授权表信息
     *
     * @param tenantContractAuthDTO 租户合同授权表
     * @return 结果
     */

    @Override
    public int deleteTenantContractAuthByTenantContractAuthId(TenantContractAuthDTO tenantContractAuthDTO) {
        TenantContractAuth tenantContractAuth = new TenantContractAuth();
        BeanUtils.copyProperties(tenantContractAuthDTO, tenantContractAuth);
        return tenantContractAuthMapper.deleteTenantContractAuthByTenantContractAuthId(tenantContractAuth.getTenantContractAuthId());
    }

    /**
     * 物理批量删除租户合同授权表
     *
     * @param tenantContractAuthDtos 需要删除的租户合同授权表主键
     * @return 结果
     */

    @Override
    public int deleteTenantContractAuthByTenantContractAuthIds(List<TenantContractAuthDTO> tenantContractAuthDtos) {
        List<Long> stringList = new ArrayList();
        for (TenantContractAuthDTO tenantContractAuthDTO : tenantContractAuthDtos) {
            stringList.add(tenantContractAuthDTO.getTenantContractAuthId());
        }
        return tenantContractAuthMapper.deleteTenantContractAuthByTenantContractAuthIds(stringList);
    }

    /**
     * 批量新增租户合同授权表信息
     *
     * @param tenantContractAuthDtos 租户合同授权表对象
     */

    public int insertTenantContractAuths(List<TenantContractAuthDTO> tenantContractAuthDtos) {
        List<TenantContractAuth> tenantContractAuthList = new ArrayList();

        for (TenantContractAuthDTO tenantContractAuthDTO : tenantContractAuthDtos) {
            TenantContractAuth tenantContractAuth = new TenantContractAuth();
            BeanUtils.copyProperties(tenantContractAuthDTO, tenantContractAuth);
            tenantContractAuth.setCreateBy(SecurityUtils.getUserId());
            tenantContractAuth.setCreateTime(DateUtils.getNowDate());
            tenantContractAuth.setUpdateTime(DateUtils.getNowDate());
            tenantContractAuth.setUpdateBy(SecurityUtils.getUserId());
            tenantContractAuth.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            tenantContractAuthList.add(tenantContractAuth);
        }
        return tenantContractAuthMapper.batchTenantContractAuth(tenantContractAuthList);
    }

    /**
     * 批量修改租户合同授权表信息
     *
     * @param tenantContractAuthDtos 租户合同授权表对象
     */

    public int updateTenantContractAuths(List<TenantContractAuthDTO> tenantContractAuthDtos) {
        List<TenantContractAuth> tenantContractAuthList = new ArrayList();

        for (TenantContractAuthDTO tenantContractAuthDTO : tenantContractAuthDtos) {
            TenantContractAuth tenantContractAuth = new TenantContractAuth();
            BeanUtils.copyProperties(tenantContractAuthDTO, tenantContractAuth);
            tenantContractAuth.setCreateBy(SecurityUtils.getUserId());
            tenantContractAuth.setCreateTime(DateUtils.getNowDate());
            tenantContractAuth.setUpdateTime(DateUtils.getNowDate());
            tenantContractAuth.setUpdateBy(SecurityUtils.getUserId());
            tenantContractAuthList.add(tenantContractAuth);
        }
        return tenantContractAuthMapper.updateTenantContractAuths(tenantContractAuthList);
    }

}

