package net.qixiaowei.system.manage.service.impl.basic;

import net.qixiaowei.integration.common.constant.BusinessConstants;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.domain.basic.Industry;
import net.qixiaowei.system.manage.api.domain.basic.IndustryDefault;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDefaultDTO;
import net.qixiaowei.system.manage.mapper.basic.IndustryDefaultMapper;
import net.qixiaowei.system.manage.service.basic.IIndustryDefaultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * IndustryDefaultService业务层处理
 *
 * @author Graves
 * @since 2022-09-26
 */
@Service
public class IndustryDefaultServiceImpl implements IIndustryDefaultService {
    @Autowired
    private IndustryDefaultMapper industryDefaultMapper;

    /**
     * 查询默认行业
     *
     * @param industryId 默认行业主键
     * @return 默认行业
     */
    @Override
    public IndustryDefaultDTO selectIndustryDefaultByIndustryId(Long industryId) {
        return industryDefaultMapper.selectIndustryDefaultByIndustryId(industryId);
    }

    /**
     * 查询默认行业列表
     *
     * @param industryDefaultDTO 默认行业
     * @return 默认行业
     */
    @Override
    public List<IndustryDefaultDTO> selectIndustryDefaultList(IndustryDefaultDTO industryDefaultDTO) {
        IndustryDefault industryDefault = new IndustryDefault();
        BeanUtils.copyProperties(industryDefaultDTO, industryDefault);
        return industryDefaultMapper.selectIndustryDefaultList(industryDefault);
    }

    /**
     * 新增默认行业
     *
     * @param industryDefaultDTO 默认行业
     * @return 结果
     */
    @Transactional
    @Override
    public int insertIndustryDefault(IndustryDefaultDTO industryDefaultDTO) {
        String industryCode = industryDefaultDTO.getIndustryCode();
        if (StringUtils.isEmpty(industryCode)) {
            throw new ServiceException("行业编码不能为空");
        }
        if (industryDefaultMapper.checkUnique(industryCode) > 0) {
            throw new ServiceException("行业编码重复");
        }
        String parentAncestors = "";//仅在非一级行业时有用
        Integer parentLevel = 1;
        Long parentIndustryId = industryDefaultDTO.getParentIndustryId();
        if (StringUtils.isNotNull(parentIndustryId)) {// 一级行业
            IndustryDefaultDTO parentIndustry = industryDefaultMapper.selectIndustryDefaultByIndustryId(parentIndustryId);
            if (parentIndustry == null) {
                throw new ServiceException("该上级行业不存在");
            }
            parentAncestors = parentIndustry.getAncestors();
            parentLevel = parentIndustry.getLevel();
            Integer status = parentIndustry.getStatus();
            // 如果父节点不为正常状态,则不允许新增子节点
            if (BusinessConstants.DISABLE.equals(status)) {
                throw new ServiceException("上级行业失效，不允许新增子节点");
            }
        }
        IndustryDefault industryDefault = new IndustryDefault();
        BeanUtils.copyProperties(industryDefaultDTO, industryDefault);
        if (StringUtils.isNotNull(parentIndustryId)) {
            String ancestors = parentAncestors;
            if (StringUtils.isNotEmpty(ancestors)) {
                ancestors = ancestors + ",";
            }
            ancestors = ancestors + parentIndustryId;
            industryDefault.setAncestors(ancestors);
            industryDefault.setLevel(parentLevel + 1);
        } else {
            industryDefault.setParentIndustryId(0L);
            industryDefault.setAncestors(parentAncestors);
            industryDefault.setLevel(parentLevel);
        }
        industryDefault.setCreateTime(DateUtils.getNowDate());
        industryDefault.setUpdateTime(DateUtils.getNowDate());
        industryDefault.setCreateBy(SecurityUtils.getUserId());
        industryDefault.setUpdateBy(SecurityUtils.getUserId());
        industryDefault.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        return industryDefaultMapper.insertIndustryDefault(industryDefault);
    }

    /**
     * 默认行业唯一性校验
     *
     * @param IndustryCode
     * @return
     */
    private int checkUnique(String IndustryCode) {
        return industryDefaultMapper.checkUnique(IndustryCode);
    }

    /**
     * 修改默认行业
     *
     * @param industryDefaultDTO 默认行业
     * @return 结果
     */
    @Transactional
    @Override
    public int updateIndustryDefault(IndustryDefaultDTO industryDefaultDTO) {
        IndustryDefault industryDefault = new IndustryDefault();
        if (checkUnique(industryDefaultDTO.getIndustryCode()) > 0) {
            throw new ServiceException("该默认行业已存在");
        }
        BeanUtils.copyProperties(industryDefaultDTO, industryDefault);
        industryDefault.setUpdateTime(DateUtils.getNowDate());
        industryDefault.setUpdateBy(SecurityUtils.getUserId());
        return industryDefaultMapper.updateIndustryDefault(industryDefault);
    }

    /**
     * 逻辑批量删除默认行业
     *
     * @param industryDefaultDtos 需要删除的默认行业主键
     * @return 结果
     */

    @Transactional
    @Override
    public int logicDeleteIndustryDefaultByIndustryIds(List<IndustryDefaultDTO> industryDefaultDtos) {
        List<Long> stringList = new ArrayList<>();
        for (IndustryDefaultDTO industryDefaultDTO : industryDefaultDtos) {
            if (industryDefaultMapper.selectIndustryDefaultByIndustryId(industryDefaultDTO.getIndustryId()) == null) {
                throw new ServiceException("该默认行业不存在");
            }
            stringList.add(industryDefaultDTO.getIndustryId());
        }
        return industryDefaultMapper.logicDeleteIndustryDefaultByIndustryIds(stringList, industryDefaultDtos.get(0).getUpdateBy(), DateUtils.getNowDate());
    }

    /**
     * 物理删除默认行业信息
     *
     * @param industryId 默认行业主键
     * @return 结果
     */

    @Transactional
    @Override
    public int deleteIndustryDefaultByIndustryId(Long industryId) {
        return industryDefaultMapper.deleteIndustryDefaultByIndustryId(industryId);
    }

    /**
     * 逻辑删除默认行业信息
     *
     * @param industryDefaultDTO 默认行业
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteIndustryDefaultByIndustryId(IndustryDefaultDTO industryDefaultDTO) {
        IndustryDefault industryDefault = new IndustryDefault();
        BeanUtils.copyProperties(industryDefaultDTO, industryDefault);
        return industryDefaultMapper.logicDeleteIndustryDefaultByIndustryId(industryDefault, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除默认行业信息
     *
     * @param industryDefaultDTO 默认行业
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteIndustryDefaultByIndustryId(IndustryDefaultDTO industryDefaultDTO) {
        IndustryDefault industryDefault = new IndustryDefault();
        BeanUtils.copyProperties(industryDefaultDTO, industryDefault);
        return industryDefaultMapper.deleteIndustryDefaultByIndustryId(industryDefault.getIndustryId());
    }

    /**
     * 物理批量删除默认行业
     *
     * @param industryDefaultDtos 需要删除的默认行业主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteIndustryDefaultByIndustryIds(List<IndustryDefaultDTO> industryDefaultDtos) {
        List<Long> stringList = new ArrayList();
        for (IndustryDefaultDTO industryDefaultDTO : industryDefaultDtos) {
            stringList.add(industryDefaultDTO.getIndustryId());
        }
        return industryDefaultMapper.deleteIndustryDefaultByIndustryIds(stringList);
    }

    /**
     * 批量新增默认行业信息
     *
     * @param industryDefaultDtos 默认行业对象
     */
    @Transactional
    public int insertIndustryDefaults(List<IndustryDefaultDTO> industryDefaultDtos) {
        List<IndustryDefault> industryDefaultList = new ArrayList();

        for (IndustryDefaultDTO industryDefaultDTO : industryDefaultDtos) {
            IndustryDefault industryDefault = new IndustryDefault();
            BeanUtils.copyProperties(industryDefaultDTO, industryDefault);
            industryDefault.setCreateBy(SecurityUtils.getUserId());
            industryDefault.setCreateTime(DateUtils.getNowDate());
            industryDefault.setUpdateTime(DateUtils.getNowDate());
            industryDefault.setUpdateBy(SecurityUtils.getUserId());
            industryDefault.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            industryDefaultList.add(industryDefault);
        }
        return industryDefaultMapper.batchIndustryDefault(industryDefaultList);
    }

    /**
     * 批量修改默认行业信息
     *
     * @param industryDefaultDtos 默认行业对象
     */
    @Transactional
    public int updateIndustryDefaults(List<IndustryDefaultDTO> industryDefaultDtos) {
        List<IndustryDefault> industryDefaultList = new ArrayList();

        for (IndustryDefaultDTO industryDefaultDTO : industryDefaultDtos) {
            IndustryDefault industryDefault = new IndustryDefault();
            BeanUtils.copyProperties(industryDefaultDTO, industryDefault);
            industryDefault.setCreateBy(SecurityUtils.getUserId());
            industryDefault.setCreateTime(DateUtils.getNowDate());
            industryDefault.setUpdateTime(DateUtils.getNowDate());
            industryDefault.setUpdateBy(SecurityUtils.getUserId());
            industryDefaultList.add(industryDefault);
        }
        return industryDefaultMapper.updateIndustryDefaults(industryDefaultList);
    }
}

