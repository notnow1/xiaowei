package net.qixiaowei.strategy.cloud.service.impl.industry;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.strategy.cloud.api.domain.industry.IndustryAttraction;
import net.qixiaowei.strategy.cloud.api.domain.industry.IndustryAttractionElement;
import net.qixiaowei.strategy.cloud.api.dto.industry.IndustryAttractionDTO;
import net.qixiaowei.strategy.cloud.mapper.industry.IndustryAttractionElementMapper;
import net.qixiaowei.strategy.cloud.mapper.industry.IndustryAttractionMapper;
import net.qixiaowei.strategy.cloud.service.industry.IIndustryAttractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * IndustryAttractionService业务层处理
 *
 * @author TANGMICHI
 * @since 2023-02-20
 */
@Service
public class IndustryAttractionServiceImpl implements IIndustryAttractionService {
    @Autowired
    private IndustryAttractionMapper industryAttractionMapper;
    @Autowired
    private IndustryAttractionElementMapper industryAttractionElementMapper;
    /**
     * 行业吸引力配置主表
     */
    private static final List<IndustryAttraction> INIT_INDUSTRY_ATTRACTION = new ArrayList<>(3);

    static {
        INIT_INDUSTRY_ATTRACTION.add(IndustryAttraction.builder().attractionElementName("成长性").status(1).build());
        INIT_INDUSTRY_ATTRACTION.add(IndustryAttraction.builder().attractionElementName("盈利性").status(1).build());
        INIT_INDUSTRY_ATTRACTION.add(IndustryAttraction.builder().attractionElementName("集中度").status(1).build());
    }

    /**
     * 行业吸引力配置预制详情成长性
     */
    private static final List<IndustryAttractionElement> INIT_INDUSTRY_ATTRACTION_ELEMENT_ONE = new ArrayList<>(4);

    static {
        INIT_INDUSTRY_ATTRACTION_ELEMENT_ONE.add(IndustryAttractionElement.builder().assessStandardName("高潜行业").assessStandardDescription("总产值三年复合增长率达到GDP年均增长率的四倍（约为25-30%）").displayColor("蓝色").sort(1).status(1).build());
        INIT_INDUSTRY_ATTRACTION_ELEMENT_ONE.add(IndustryAttractionElement.builder().assessStandardName("价值行业").assessStandardDescription("总产值三年复合增长率达到GDP年均增长率的两倍（约为13-15%）").displayColor("蓝色").sort(2).status(1).build());
        INIT_INDUSTRY_ATTRACTION_ELEMENT_ONE.add(IndustryAttractionElement.builder().assessStandardName("稳健行业").assessStandardDescription("总产值三年复合增长率大于等于GDP年均增长率小于两倍").displayColor("灰色").sort(3).status(1).build());
        INIT_INDUSTRY_ATTRACTION_ELEMENT_ONE.add(IndustryAttractionElement.builder().assessStandardName("谨慎行业").assessStandardDescription("总产值三年复合增长率小于GDP年均增长率").displayColor("红色").sort(4).status(1).build());
    }

    /**
     * 行业吸引力配置预制详情盈利性
     */
    private static final List<IndustryAttractionElement> INIT_INDUSTRY_ATTRACTION_ELEMENT_TWO = new ArrayList<>(4);

    static {
        INIT_INDUSTRY_ATTRACTION_ELEMENT_TWO.add(IndustryAttractionElement.builder().assessStandardName("超高利润行业").assessStandardDescription("平均毛利率超过80%").displayColor("蓝色").sort(1).status(1).build());
        INIT_INDUSTRY_ATTRACTION_ELEMENT_TWO.add(IndustryAttractionElement.builder().assessStandardName("高利润行业").assessStandardDescription("平均毛利率50%-80%").displayColor("蓝色").sort(2).status(1).build());
        INIT_INDUSTRY_ATTRACTION_ELEMENT_TWO.add(IndustryAttractionElement.builder().assessStandardName("平均利润行业").assessStandardDescription("平均毛利率30%-50%").displayColor("灰色").sort(3).status(1).build());
        INIT_INDUSTRY_ATTRACTION_ELEMENT_TWO.add(IndustryAttractionElement.builder().assessStandardName("低利润行业").assessStandardDescription("平均毛利率低于30%").displayColor("红色").sort(4).status(1).build());
    }

    /**
     * 行业吸引力配置预制详情集中度
     */
    private static final List<IndustryAttractionElement> INIT_INDUSTRY_ATTRACTION_ELEMENT_Three = new ArrayList<>(4);

    static {
        INIT_INDUSTRY_ATTRACTION_ELEMENT_Three.add(IndustryAttractionElement.builder().assessStandardName("极高寡占型").assessStandardDescription("CR8 ≥ 70%").displayColor("红色").sort(1).status(1).build());
        INIT_INDUSTRY_ATTRACTION_ELEMENT_Three.add(IndustryAttractionElement.builder().assessStandardName("低集中寡占型").assessStandardDescription("40% ≤ CR8<70%").displayColor("蓝色").sort(2).status(1).build());
        INIT_INDUSTRY_ATTRACTION_ELEMENT_Three.add(IndustryAttractionElement.builder().assessStandardName("低集中竞争型").assessStandardDescription("20% ≤ CR8<40%").displayColor("蓝色").sort(3).status(1).build());
        INIT_INDUSTRY_ATTRACTION_ELEMENT_Three.add(IndustryAttractionElement.builder().assessStandardName("分散竞争型").assessStandardDescription("CR8<20%").displayColor("灰色").sort(4).status(1).build());
    }

    /**
     * 查询行业吸引力表
     *
     * @param industryAttractionId 行业吸引力表主键
     * @return 行业吸引力表
     */
    @Override
    public IndustryAttractionDTO selectIndustryAttractionByIndustryAttractionId(Long industryAttractionId) {
        return industryAttractionMapper.selectIndustryAttractionByIndustryAttractionId(industryAttractionId);
    }

    /**
     * 查询行业吸引力表列表
     *
     * @param industryAttractionDTO 行业吸引力表
     * @return 行业吸引力表
     */
    @Override
    public List<IndustryAttractionDTO> selectIndustryAttractionList(IndustryAttractionDTO industryAttractionDTO) {
        IndustryAttraction industryAttraction = new IndustryAttraction();
        BeanUtils.copyProperties(industryAttractionDTO, industryAttraction);
        return industryAttractionMapper.selectIndustryAttractionList(industryAttraction);
    }

    /**
     * 新增行业吸引力表
     *
     * @param industryAttractionDTO 行业吸引力表
     * @return 结果
     */
    @Override
    public IndustryAttractionDTO insertIndustryAttraction(IndustryAttractionDTO industryAttractionDTO) {
        IndustryAttraction industryAttraction = new IndustryAttraction();
        BeanUtils.copyProperties(industryAttractionDTO, industryAttraction);
        industryAttraction.setCreateBy(SecurityUtils.getUserId());
        industryAttraction.setCreateTime(DateUtils.getNowDate());
        industryAttraction.setUpdateTime(DateUtils.getNowDate());
        industryAttraction.setUpdateBy(SecurityUtils.getUserId());
        industryAttraction.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        industryAttractionMapper.insertIndustryAttraction(industryAttraction);
        industryAttractionDTO.setIndustryAttractionId(industryAttraction.getIndustryAttractionId());
        return industryAttractionDTO;
    }

    /**
     * 修改行业吸引力表
     *
     * @param industryAttractionDTO 行业吸引力表
     * @return 结果
     */
    @Override
    public int updateIndustryAttraction(IndustryAttractionDTO industryAttractionDTO) {
        IndustryAttraction industryAttraction = new IndustryAttraction();
        BeanUtils.copyProperties(industryAttractionDTO, industryAttraction);
        industryAttraction.setUpdateTime(DateUtils.getNowDate());
        industryAttraction.setUpdateBy(SecurityUtils.getUserId());
        return industryAttractionMapper.updateIndustryAttraction(industryAttraction);
    }

    /**
     * 逻辑批量删除行业吸引力表
     *
     * @param industryAttractionIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteIndustryAttractionByIndustryAttractionIds(List<Long> industryAttractionIds) {
        return industryAttractionMapper.logicDeleteIndustryAttractionByIndustryAttractionIds(industryAttractionIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除行业吸引力表信息
     *
     * @param industryAttractionId 行业吸引力表主键
     * @return 结果
     */
    @Override
    public int deleteIndustryAttractionByIndustryAttractionId(Long industryAttractionId) {
        return industryAttractionMapper.deleteIndustryAttractionByIndustryAttractionId(industryAttractionId);
    }

    /**
     * 初始化行业吸引力配置
     *
     * @return
     */
    @Override
    @Transactional
    public Boolean initIndustryAttraction() {
        int i = 0;
        boolean initSuccess = false;
        Long userId = SecurityUtils.getUserId();
        Date nowDate = DateUtils.getNowDate();
        //行业吸引力配置主表
        List<IndustryAttraction> industryAttractions = new ArrayList<>();
        //行业吸引力配置详情表
        List<IndustryAttractionElement> industryAttractionElements = new ArrayList<>();
        for (IndustryAttraction industryAttraction : INIT_INDUSTRY_ATTRACTION) {
            industryAttraction.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            industryAttraction.setCreateBy(userId);
            industryAttraction.setUpdateBy(userId);
            industryAttraction.setCreateTime(nowDate);
            industryAttraction.setUpdateTime(nowDate);
            industryAttractions.add(industryAttraction);
        }
        try {
            industryAttractionMapper.batchIndustryAttraction(industryAttractions);
        } catch (Exception e) {
            throw new ServiceException("插入行业吸引力配置主表失败！！！");
        }
        for (IndustryAttractionElement industryAttractionElement : INIT_INDUSTRY_ATTRACTION_ELEMENT_ONE) {
            industryAttractionElement.setIndustryAttractionId(industryAttractions.get(0).getIndustryAttractionId());
            industryAttractionElement.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            industryAttractionElement.setCreateBy(userId);
            industryAttractionElement.setUpdateBy(userId);
            industryAttractionElement.setCreateTime(nowDate);
            industryAttractionElement.setUpdateTime(nowDate);
            industryAttractionElements.add(industryAttractionElement);
        }
        for (IndustryAttractionElement industryAttractionElement : INIT_INDUSTRY_ATTRACTION_ELEMENT_TWO) {
            industryAttractionElement.setIndustryAttractionId(industryAttractions.get(1).getIndustryAttractionId());
            industryAttractionElement.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            industryAttractionElement.setCreateBy(userId);
            industryAttractionElement.setUpdateBy(userId);
            industryAttractionElement.setCreateTime(nowDate);
            industryAttractionElement.setUpdateTime(nowDate);
            industryAttractionElements.add(industryAttractionElement);
        }
        for (IndustryAttractionElement industryAttractionElement : INIT_INDUSTRY_ATTRACTION_ELEMENT_Three) {
            industryAttractionElement.setIndustryAttractionId(industryAttractions.get(2).getIndustryAttractionId());
            industryAttractionElement.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            industryAttractionElement.setCreateBy(userId);
            industryAttractionElement.setUpdateBy(userId);
            industryAttractionElement.setCreateTime(nowDate);
            industryAttractionElement.setUpdateTime(nowDate);
            industryAttractionElements.add(industryAttractionElement);
        }
        i = industryAttractionElementMapper.batchIndustryAttractionElement(industryAttractionElements);
        if (i == 12) {
            initSuccess = true;
        }
        return initSuccess;
    }

    /**
     * 逻辑删除行业吸引力表信息
     *
     * @param industryAttractionDTO 行业吸引力表
     * @return 结果
     */
    @Override
    public int logicDeleteIndustryAttractionByIndustryAttractionId(IndustryAttractionDTO industryAttractionDTO) {
        IndustryAttraction industryAttraction = new IndustryAttraction();
        industryAttraction.setIndustryAttractionId(industryAttractionDTO.getIndustryAttractionId());
        industryAttraction.setUpdateTime(DateUtils.getNowDate());
        industryAttraction.setUpdateBy(SecurityUtils.getUserId());
        return industryAttractionMapper.logicDeleteIndustryAttractionByIndustryAttractionId(industryAttraction);
    }

    /**
     * 物理删除行业吸引力表信息
     *
     * @param industryAttractionDTO 行业吸引力表
     * @return 结果
     */

    @Override
    public int deleteIndustryAttractionByIndustryAttractionId(IndustryAttractionDTO industryAttractionDTO) {
        IndustryAttraction industryAttraction = new IndustryAttraction();
        BeanUtils.copyProperties(industryAttractionDTO, industryAttraction);
        return industryAttractionMapper.deleteIndustryAttractionByIndustryAttractionId(industryAttraction.getIndustryAttractionId());
    }

    /**
     * 物理批量删除行业吸引力表
     *
     * @param industryAttractionDtos 需要删除的行业吸引力表主键
     * @return 结果
     */

    @Override
    public int deleteIndustryAttractionByIndustryAttractionIds(List<IndustryAttractionDTO> industryAttractionDtos) {
        List<Long> stringList = new ArrayList();
        for (IndustryAttractionDTO industryAttractionDTO : industryAttractionDtos) {
            stringList.add(industryAttractionDTO.getIndustryAttractionId());
        }
        return industryAttractionMapper.deleteIndustryAttractionByIndustryAttractionIds(stringList);
    }

    /**
     * 批量新增行业吸引力表信息
     *
     * @param industryAttractionDtos 行业吸引力表对象
     */

    public int insertIndustryAttractions(List<IndustryAttractionDTO> industryAttractionDtos) {
        List<IndustryAttraction> industryAttractionList = new ArrayList();

        for (IndustryAttractionDTO industryAttractionDTO : industryAttractionDtos) {
            IndustryAttraction industryAttraction = new IndustryAttraction();
            BeanUtils.copyProperties(industryAttractionDTO, industryAttraction);
            industryAttraction.setCreateBy(SecurityUtils.getUserId());
            industryAttraction.setCreateTime(DateUtils.getNowDate());
            industryAttraction.setUpdateTime(DateUtils.getNowDate());
            industryAttraction.setUpdateBy(SecurityUtils.getUserId());
            industryAttraction.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            industryAttractionList.add(industryAttraction);
        }
        return industryAttractionMapper.batchIndustryAttraction(industryAttractionList);
    }

    /**
     * 批量修改行业吸引力表信息
     *
     * @param industryAttractionDtos 行业吸引力表对象
     */

    public int updateIndustryAttractions(List<IndustryAttractionDTO> industryAttractionDtos) {
        List<IndustryAttraction> industryAttractionList = new ArrayList();

        for (IndustryAttractionDTO industryAttractionDTO : industryAttractionDtos) {
            IndustryAttraction industryAttraction = new IndustryAttraction();
            BeanUtils.copyProperties(industryAttractionDTO, industryAttraction);
            industryAttraction.setCreateBy(SecurityUtils.getUserId());
            industryAttraction.setCreateTime(DateUtils.getNowDate());
            industryAttraction.setUpdateTime(DateUtils.getNowDate());
            industryAttraction.setUpdateBy(SecurityUtils.getUserId());
            industryAttractionList.add(industryAttraction);
        }
        return industryAttractionMapper.updateIndustryAttractions(industryAttractionList);
    }
}

