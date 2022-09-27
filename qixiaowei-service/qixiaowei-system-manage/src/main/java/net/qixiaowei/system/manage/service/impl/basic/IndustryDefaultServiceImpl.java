package net.qixiaowei.system.manage.service.impl.basic;

import java.util.List;

import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Random;

import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.domain.basic.IndustryDefault;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDefaultDTO;
import net.qixiaowei.system.manage.mapper.basic.IndustryDefaultMapper;
import net.qixiaowei.system.manage.service.basic.IIndustryDefaultService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


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
        IndustryDefault industryDefault = new IndustryDefault();
        BeanUtils.copyProperties(industryDefaultDTO, industryDefault);
        industryDefault.setCreateBy(SecurityUtils.getUserId());
        industryDefault.setCreateTime(DateUtils.getNowDate());
        industryDefault.setUpdateTime(DateUtils.getNowDate());
        industryDefault.setUpdateBy(SecurityUtils.getUserId());
        industryDefault.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        return industryDefaultMapper.insertIndustryDefault(industryDefault);
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
        List<Long> stringList = new ArrayList();
        for (IndustryDefaultDTO industryDefaultDTO : industryDefaultDtos) {
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

