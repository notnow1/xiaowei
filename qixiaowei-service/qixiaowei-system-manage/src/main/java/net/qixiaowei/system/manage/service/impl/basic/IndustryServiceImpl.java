package net.qixiaowei.system.manage.service.impl.basic;

import java.util.List;

import net.qixiaowei.integration.common.constant.UserConstants;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.exception.auth.NotLoginException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.system.manage.api.domain.basic.IndustryDefault;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Random;

import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.domain.basic.Industry;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDTO;
import net.qixiaowei.system.manage.mapper.basic.IndustryMapper;
import net.qixiaowei.system.manage.service.basic.IIndustryService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


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
        Industry industry = new Industry();
        BeanUtils.copyProperties(industryDTO, industry);
        String industryCode = "";
        if (StringUtils.isEmpty(industryDTO.getIndustryCode())) {
            // todo 行业编码生成规律
            industryCode = "industryCode";
            industryDTO.setIndustryCode(industryCode);
        } else {
            industryCode = industryDTO.getIndustryCode();
            if (industryMapper.checkUnique(industryCode) > 0) {
                throw new ServiceException("行业编码重复");
            }
        }
        IndustryDTO parentIndustry = industryMapper.selectIndustryByIndustryId(industryDTO.getParentIndustryId());
        // 如果父节点不为正常状态,则不允许新增子节点
        Integer status = parentIndustry.getStatus();
        if (!UserConstants.INDUSTRY_NORMAL.equals(status)) {
            throw new ServiceException("该行业停用，不允许新增");
        }
        industry.setAncestors(parentIndustry.getAncestors() + "," + industry.getIndustryId());
        industry.setLevel(parentIndustry.getLevel() + 1);
        industry.setIndustryCode(industryCode);
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
        Industry industry = new Industry();
        BeanUtils.copyProperties(industryDTO, industry);
        IndustryDTO parentIndustry = industryMapper.selectIndustryByIndustryId(industryDTO.getParentIndustryId());
        // 如果父节点不为正常状态,则不允许编辑子节点
        Integer status = parentIndustry.getStatus();
        if (!UserConstants.INDUSTRY_NORMAL.equals(status)) {
            throw new ServiceException("该行业停用，不允许新增");
        }
        industry.setAncestors(parentIndustry.getAncestors() + "," + industry.getIndustryId());
        industry.setLevel(parentIndustry.getLevel() + 1);
        industry.setUpdateTime(DateUtils.getNowDate());
        industry.setUpdateBy(SecurityUtils.getUserId());
        return industryMapper.updateIndustry(industry);
    }

    /**
     * 逻辑批量删除行业
     *
     * @param industryDtos 需要删除的行业主键
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteIndustryByIndustryIds(List<IndustryDTO> industryDtos) {
        List<Long> stringList = new ArrayList<>();
        for (IndustryDTO industryDTO : industryDtos) {
            if (isQuote(industryDTO.getIndustryId())) {
                throw new NotLoginException("该分解维度已被引用");
            }
            stringList.add(industryDTO.getIndustryId());
        }
        return industryMapper.logicDeleteIndustryByIndustryIds(stringList, industryDtos.get(0).getUpdateBy(), DateUtils.getNowDate());
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
     * @param industryDTO 行业
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteIndustryByIndustryId(IndustryDTO industryDTO) {
        Industry industry = new Industry();
        BeanUtils.copyProperties(industryDTO, industry);
        return industryMapper.logicDeleteIndustryByIndustryId(industry, SecurityUtils.getUserId(), DateUtils.getNowDate());
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
     * todo 引用校验
     *
     * @param industryId
     * @return
     */
    private boolean isQuote(Long industryId) {
        return false;
    }
}

