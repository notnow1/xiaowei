package net.qixiaowei.system.manage.service.impl.basic;

import net.qixiaowei.integration.common.constant.BusinessConstants;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.domain.basic.Industry;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDTO;
import net.qixiaowei.system.manage.mapper.basic.IndustryMapper;
import net.qixiaowei.system.manage.service.basic.IIndustryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * IndustryService业务层处理
 *
 * @author Graves
 * @since 2022-09-26
 */
@Service
public class IndustryServiceImpl implements IIndustryService {
    @Autowired
    private IndustryMapper industryMapper;

    /**
     * 查询行业
     *
     * @param industryId 行业主键
     * @return 行业
     */
    @Override
    public IndustryDTO selectIndustryByIndustryId(Long industryId) {
        return industryMapper.selectIndustryByIndustryId(industryId);
    }

    /**
     * 查询行业列表
     *
     * @param industryDTO 行业
     * @return 行业
     */
    @Override
    public List<IndustryDTO> selectIndustryList(IndustryDTO industryDTO) {
        Industry industry = new Industry();
        BeanUtils.copyProperties(industryDTO, industry);
        return industryMapper.selectIndustryList(industry);
    }

    /**
     * 新增行业
     *
     * @param industryDTO 行业
     * @return 结果
     */
    @Transactional
    @Override
    public int insertIndustry(IndustryDTO industryDTO) {
        String industryCode = industryDTO.getIndustryCode();
        if (StringUtils.isEmpty(industryCode)) {
            throw new ServiceException("行业编码不能为空");
        }
        if (industryMapper.checkUnique(industryCode) > 0) {
            throw new ServiceException("行业编码重复");
        }
        String parentAncestors = "";//仅在非一级行业时有用
        Integer parentLevel = 1;
        Long parentIndustryId = industryDTO.getParentIndustryId();
        if (StringUtils.isNotNull(parentIndustryId)) {
            IndustryDTO parentIndustry = industryMapper.selectIndustryByIndustryId(parentIndustryId);
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
        Industry industry = new Industry();
        BeanUtils.copyProperties(industryDTO, industry);
        if (StringUtils.isNotNull(parentIndustryId)) {
            String ancestors = parentAncestors;
            if (StringUtils.isNotEmpty(ancestors)) {
                ancestors = ancestors + ",";
            }
            ancestors = ancestors + parentIndustryId;
            industry.setAncestors(ancestors);
            industry.setLevel(parentLevel + 1);
        } else {
            industry.setParentIndustryId(0L);
            industry.setAncestors(parentAncestors);
            industry.setLevel(parentLevel);
        }
        industry.setCreateTime(DateUtils.getNowDate());
        industry.setUpdateTime(DateUtils.getNowDate());
        industry.setCreateBy(SecurityUtils.getUserId());
        industry.setUpdateBy(SecurityUtils.getUserId());
        industry.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        return industryMapper.insertIndustry(industry);
    }

    /**
     * 修改行业
     *
     * @param industryDTO 行业
     * @return 结果
     */
    @Transactional
    @Override
    public int updateIndustry(IndustryDTO industryDTO) {
        String industryCode = industryDTO.getIndustryCode();
        if (StringUtils.isEmpty(industryCode)) {
            throw new ServiceException("行业编码不能为空");
        }
        if (industryMapper.checkUnique(industryCode) > 0) {
            throw new ServiceException("行业编码重复");
        }
        Long parentIndustryId = industryDTO.getParentIndustryId();
        if (StringUtils.isNotNull(parentIndustryId)) {// 一级行业
            IndustryDTO parentIndustry = industryMapper.selectIndustryByIndustryId(parentIndustryId);
            if (parentIndustry == null) {
                throw new ServiceException("该上级行业不存在");
            }
            Integer status = parentIndustry.getStatus();
            // 如果父节点不为正常状态,则不允许新增子节点
            if (BusinessConstants.DISABLE.equals(status)) {
                throw new ServiceException("上级行业失效，不允许编辑子节点");
            }
        }
        Integer status = industryDTO.getStatus();
        Long industryId = industryDTO.getIndustryId();
        if (BusinessConstants.DISABLE.equals(status)) {//失效会影响子级
            //先查再批量更新
            List<Long> industryIds = industryMapper.selectSon(industryId);
            if (StringUtils.isEmpty(industryIds)) {
                industryIds = new ArrayList<>();
            }
            industryIds.add(industryId);
            industryMapper.updateStatus(status, SecurityUtils.getUserId(), DateUtils.getNowDate(), industryIds);
        }
        Industry industry = new Industry();
        BeanUtils.copyProperties(industryDTO, industry);
        industry.setUpdateTime(DateUtils.getNowDate());
        industry.setUpdateBy(SecurityUtils.getUserId());
        return industryMapper.updateIndustry(industry);
    }

    /**
     * 逻辑批量删除行业
     *
     * @param industryIds 需要删除的行业主键
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteIndustryByIndustryIds(List<Long> industryIds) {
        List<Long> exist = industryMapper.isExist(industryIds);
        if (StringUtils.isEmpty(exist)) {
            throw new ServiceException("该行业已不存在");
        }
        List<Long> longs = industryMapper.selectSons(exist);
        if (StringUtils.isNotEmpty(longs)) {
            industryIds.addAll(longs);
        }
        // todo 引用校验
        if (isQuote(industryIds)) {
            throw new ServiceException("存在被引用的行业");
        }
        return industryMapper.logicDeleteIndustryByIndustryIds(industryIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * todo 引用校验
     *
     * @param industryIds
     * @return
     */
    private boolean isQuote(List<Long> industryIds) {
        return false;
    }

    /**
     * 物理删除行业信息
     *
     * @param industryId 行业主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteIndustryByIndustryId(Long industryId) {
        return industryMapper.deleteIndustryByIndustryId(industryId);
    }

    /**
     * 逻辑删除行业信息
     *
     * @param industryId 行业
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteIndustryByIndustryId(Long industryId) {
        ArrayList<Long> industryIds = new ArrayList<>();
        industryIds.add(industryId);
        return logicDeleteIndustryByIndustryIds(industryIds);
    }

    /**
     * 物理删除行业信息
     *
     * @param industryDTO 行业
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteIndustryByIndustryId(IndustryDTO industryDTO) {
        Industry industry = new Industry();
        BeanUtils.copyProperties(industryDTO, industry);
        return industryMapper.deleteIndustryByIndustryId(industry.getIndustryId());
    }

    /**
     * 物理批量删除行业
     *
     * @param industryDtos 需要删除的行业主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteIndustryByIndustryIds(List<IndustryDTO> industryDtos) {
        List<Long> stringList = new ArrayList();
        for (IndustryDTO industryDTO : industryDtos) {
            stringList.add(industryDTO.getIndustryId());
        }
        return industryMapper.deleteIndustryByIndustryIds(stringList);
    }

    /**
     * 批量新增行业信息
     *
     * @param industryDtos 行业对象
     */
    @Transactional
    public int insertIndustrys(List<IndustryDTO> industryDtos) {
        List<Industry> industryList = new ArrayList();
        for (IndustryDTO industryDTO : industryDtos) {
            Industry industry = new Industry();
            BeanUtils.copyProperties(industryDTO, industry);
            industry.setCreateBy(SecurityUtils.getUserId());
            industry.setCreateTime(DateUtils.getNowDate());
            industry.setUpdateTime(DateUtils.getNowDate());
            industry.setUpdateBy(SecurityUtils.getUserId());
            industry.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            industryList.add(industry);
        }
        return industryMapper.batchIndustry(industryList);
    }

    /**
     * 批量修改行业信息
     *
     * @param industryDtos 行业对象
     */
    @Transactional
    public int updateIndustrys(List<IndustryDTO> industryDtos) {
        List<Industry> industryList = new ArrayList();

        for (IndustryDTO industryDTO : industryDtos) {
            Industry industry = new Industry();
            BeanUtils.copyProperties(industryDTO, industry);
            industry.setCreateBy(SecurityUtils.getUserId());
            industry.setCreateTime(DateUtils.getNowDate());
            industry.setUpdateTime(DateUtils.getNowDate());
            industry.setUpdateBy(SecurityUtils.getUserId());
            industryList.add(industry);
        }
        return industryMapper.updateIndustrys(industryList);
    }

    /**
     * todo 获取启用行业类型
     *
     * @return
     */
    @Override
    public int getEnableType() {
//        industryMapper.getEnableType();
        return 1;
    }

    /**
     * todo 修改启用行业类型
     *
     * @return
     */
    @Override
    public int updateEnableType(IndustryDTO industryDTO) {
//        industryMapper.setEnableType();
        return 1;
    }

    /**
     * 行业配置详情
     *
     * @param industryId
     * @return
     */
    @Override
    public IndustryDTO detailIndustry(Long industryId) {
        if (StringUtils.isNull(industryId)) {
            throw new ServiceException("行业配置id不能为空");
        }
        IndustryDTO industryDTO = industryMapper.selectIndustryByIndustryId(industryId);
        if (StringUtils.isNull(industryDTO)) {
            throw new ServiceException("该行业配置已不存在");
        }
        return industryDTO;
    }
}

