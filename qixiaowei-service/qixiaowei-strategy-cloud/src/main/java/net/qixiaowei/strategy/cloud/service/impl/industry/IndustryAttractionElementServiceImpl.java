package net.qixiaowei.strategy.cloud.service.impl.industry;

import java.util.List;

import net.qixiaowei.integration.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.strategy.cloud.api.domain.industry.IndustryAttractionElement;
import net.qixiaowei.strategy.cloud.api.dto.industry.IndustryAttractionElementDTO;
import net.qixiaowei.strategy.cloud.mapper.industry.IndustryAttractionElementMapper;
import net.qixiaowei.strategy.cloud.service.industry.IIndustryAttractionElementService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


/**
 * IndustryAttractionElementService业务层处理
 *
 * @author TANGMICHI
 * @since 2023-02-17
 */
@Service
public class IndustryAttractionElementServiceImpl implements IIndustryAttractionElementService {
    @Autowired
    private IndustryAttractionElementMapper industryAttractionElementMapper;

    /**
     * 查询行业吸引力要素表
     *
     * @param industryAttractionElementId 行业吸引力要素表主键
     * @return 行业吸引力要素表
     */
    @Override
    public IndustryAttractionElementDTO selectIndustryAttractionElementByIndustryAttractionElementId(Long industryAttractionElementId) {
        return industryAttractionElementMapper.selectIndustryAttractionElementByIndustryAttractionElementId(industryAttractionElementId);
    }

    /**
     * 查询行业吸引力要素表列表
     *
     * @param industryAttractionElementDTO 行业吸引力要素表
     * @return 行业吸引力要素表
     */
    @Override
    public List<IndustryAttractionElementDTO> selectIndustryAttractionElementList(IndustryAttractionElementDTO industryAttractionElementDTO) {
        IndustryAttractionElement industryAttractionElement = new IndustryAttractionElement();
        BeanUtils.copyProperties(industryAttractionElementDTO, industryAttractionElement);
        return industryAttractionElementMapper.selectIndustryAttractionElementList(industryAttractionElement);
    }

    /**
     * 新增行业吸引力要素表
     *
     * @param industryAttractionElementDTO 行业吸引力要素表
     * @return 结果
     */
    @Override
    public IndustryAttractionElementDTO insertIndustryAttractionElement(IndustryAttractionElementDTO industryAttractionElementDTO) {
        IndustryAttractionElement industryAttractionElement = new IndustryAttractionElement();
        BeanUtils.copyProperties(industryAttractionElementDTO, industryAttractionElement);
        industryAttractionElement.setCreateBy(SecurityUtils.getUserId());
        industryAttractionElement.setCreateTime(DateUtils.getNowDate());
        industryAttractionElement.setUpdateTime(DateUtils.getNowDate());
        industryAttractionElement.setUpdateBy(SecurityUtils.getUserId());
        industryAttractionElement.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        industryAttractionElementMapper.insertIndustryAttractionElement(industryAttractionElement);
        industryAttractionElementDTO.setIndustryAttractionElementId(industryAttractionElement.getIndustryAttractionElementId());
        return industryAttractionElementDTO;
    }

    /**
     * 修改行业吸引力要素表
     *
     * @param industryAttractionElementDTO 行业吸引力要素表
     * @return 结果
     */
    @Override
    public int updateIndustryAttractionElement(IndustryAttractionElementDTO industryAttractionElementDTO) {
        IndustryAttractionElement industryAttractionElement = new IndustryAttractionElement();
        BeanUtils.copyProperties(industryAttractionElementDTO, industryAttractionElement);
        industryAttractionElement.setUpdateTime(DateUtils.getNowDate());
        industryAttractionElement.setUpdateBy(SecurityUtils.getUserId());
        return industryAttractionElementMapper.updateIndustryAttractionElement(industryAttractionElement);
    }

    /**
     * 逻辑批量删除行业吸引力要素表
     *
     * @param industryAttractionElementIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteIndustryAttractionElementByIndustryAttractionElementIds(List<Long> industryAttractionElementIds) {
        return industryAttractionElementMapper.logicDeleteIndustryAttractionElementByIndustryAttractionElementIds(industryAttractionElementIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除行业吸引力要素表信息
     *
     * @param industryAttractionElementId 行业吸引力要素表主键
     * @return 结果
     */
    @Override
    public int deleteIndustryAttractionElementByIndustryAttractionElementId(Long industryAttractionElementId) {
        return industryAttractionElementMapper.deleteIndustryAttractionElementByIndustryAttractionElementId(industryAttractionElementId);
    }

    /**
     * 逻辑删除行业吸引力要素表信息
     *
     * @param industryAttractionElementDTO 行业吸引力要素表
     * @return 结果
     */
    @Override
    public int logicDeleteIndustryAttractionElementByIndustryAttractionElementId(IndustryAttractionElementDTO industryAttractionElementDTO) {
        IndustryAttractionElement industryAttractionElement = new IndustryAttractionElement();
        industryAttractionElement.setIndustryAttractionElementId(industryAttractionElementDTO.getIndustryAttractionElementId());
        industryAttractionElement.setUpdateTime(DateUtils.getNowDate());
        industryAttractionElement.setUpdateBy(SecurityUtils.getUserId());
        return industryAttractionElementMapper.logicDeleteIndustryAttractionElementByIndustryAttractionElementId(industryAttractionElement);
    }

    /**
     * 物理删除行业吸引力要素表信息
     *
     * @param industryAttractionElementDTO 行业吸引力要素表
     * @return 结果
     */

    @Override
    public int deleteIndustryAttractionElementByIndustryAttractionElementId(IndustryAttractionElementDTO industryAttractionElementDTO) {
        IndustryAttractionElement industryAttractionElement = new IndustryAttractionElement();
        BeanUtils.copyProperties(industryAttractionElementDTO, industryAttractionElement);
        return industryAttractionElementMapper.deleteIndustryAttractionElementByIndustryAttractionElementId(industryAttractionElement.getIndustryAttractionElementId());
    }

    /**
     * 物理批量删除行业吸引力要素表
     *
     * @param industryAttractionElementDtos 需要删除的行业吸引力要素表主键
     * @return 结果
     */

    @Override
    public int deleteIndustryAttractionElementByIndustryAttractionElementIds(List<IndustryAttractionElementDTO> industryAttractionElementDtos) {
        List<Long> stringList = new ArrayList();
        for (IndustryAttractionElementDTO industryAttractionElementDTO : industryAttractionElementDtos) {
            stringList.add(industryAttractionElementDTO.getIndustryAttractionElementId());
        }
        return industryAttractionElementMapper.deleteIndustryAttractionElementByIndustryAttractionElementIds(stringList);
    }

    /**
     * 批量新增行业吸引力要素表信息
     *
     * @param industryAttractionElementDtos 行业吸引力要素表对象
     */

    public int insertIndustryAttractionElements(List<IndustryAttractionElementDTO> industryAttractionElementDtos) {
        List<IndustryAttractionElement> industryAttractionElementList = new ArrayList();

        for (IndustryAttractionElementDTO industryAttractionElementDTO : industryAttractionElementDtos) {
            IndustryAttractionElement industryAttractionElement = new IndustryAttractionElement();
            BeanUtils.copyProperties(industryAttractionElementDTO, industryAttractionElement);
            industryAttractionElement.setCreateBy(SecurityUtils.getUserId());
            industryAttractionElement.setCreateTime(DateUtils.getNowDate());
            industryAttractionElement.setUpdateTime(DateUtils.getNowDate());
            industryAttractionElement.setUpdateBy(SecurityUtils.getUserId());
            industryAttractionElement.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            industryAttractionElementList.add(industryAttractionElement);
        }
        return industryAttractionElementMapper.batchIndustryAttractionElement(industryAttractionElementList);
    }

    /**
     * 批量修改行业吸引力要素表信息
     *
     * @param industryAttractionElementDtos 行业吸引力要素表对象
     */

    public int updateIndustryAttractionElements(List<IndustryAttractionElementDTO> industryAttractionElementDtos) {
        List<IndustryAttractionElement> industryAttractionElementList = new ArrayList();

        for (IndustryAttractionElementDTO industryAttractionElementDTO : industryAttractionElementDtos) {
            IndustryAttractionElement industryAttractionElement = new IndustryAttractionElement();
            BeanUtils.copyProperties(industryAttractionElementDTO, industryAttractionElement);
            industryAttractionElement.setCreateBy(SecurityUtils.getUserId());
            industryAttractionElement.setCreateTime(DateUtils.getNowDate());
            industryAttractionElement.setUpdateTime(DateUtils.getNowDate());
            industryAttractionElement.setUpdateBy(SecurityUtils.getUserId());
            industryAttractionElementList.add(industryAttractionElement);
        }
        return industryAttractionElementMapper.updateIndustryAttractionElements(industryAttractionElementList);
    }

}

