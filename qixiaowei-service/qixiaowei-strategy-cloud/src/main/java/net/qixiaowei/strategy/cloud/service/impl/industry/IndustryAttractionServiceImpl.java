package net.qixiaowei.strategy.cloud.service.impl.industry;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.strategy.cloud.api.domain.industry.IndustryAttraction;
import net.qixiaowei.strategy.cloud.api.domain.industry.IndustryAttractionElement;
import net.qixiaowei.strategy.cloud.api.dto.industry.IndustryAttractionDTO;
import net.qixiaowei.strategy.cloud.api.dto.industry.IndustryAttractionElementDTO;
import net.qixiaowei.strategy.cloud.mapper.industry.IndustryAttractionElementMapper;
import net.qixiaowei.strategy.cloud.mapper.industry.IndustryAttractionMapper;
import net.qixiaowei.strategy.cloud.service.industry.IIndustryAttractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


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
        INIT_INDUSTRY_ATTRACTION_ELEMENT_ONE.add(IndustryAttractionElement.builder().assessStandardName("高潜行业").assessStandardDescription("总产值三年复合增长率达到GDP年均增长率的四倍（约为25-30%）").displayColor("#1890FF").sort(1).status(1).build());
        INIT_INDUSTRY_ATTRACTION_ELEMENT_ONE.add(IndustryAttractionElement.builder().assessStandardName("价值行业").assessStandardDescription("总产值三年复合增长率达到GDP年均增长率的两倍（约为13-15%）").displayColor("#1890FF").sort(2).status(1).build());
        INIT_INDUSTRY_ATTRACTION_ELEMENT_ONE.add(IndustryAttractionElement.builder().assessStandardName("稳健行业").assessStandardDescription("总产值三年复合增长率大于等于GDP年均增长率小于两倍").displayColor("#C0C4CC").sort(3).status(1).build());
        INIT_INDUSTRY_ATTRACTION_ELEMENT_ONE.add(IndustryAttractionElement.builder().assessStandardName("谨慎行业").assessStandardDescription("总产值三年复合增长率小于GDP年均增长率").displayColor("#F94E4D").sort(4).status(1).build());
    }

    /**
     * 行业吸引力配置预制详情盈利性
     */
    private static final List<IndustryAttractionElement> INIT_INDUSTRY_ATTRACTION_ELEMENT_TWO = new ArrayList<>(4);

    static {
        INIT_INDUSTRY_ATTRACTION_ELEMENT_TWO.add(IndustryAttractionElement.builder().assessStandardName("超高利润行业").assessStandardDescription("平均毛利率超过80%").displayColor("#1890FF").sort(1).status(1).build());
        INIT_INDUSTRY_ATTRACTION_ELEMENT_TWO.add(IndustryAttractionElement.builder().assessStandardName("高利润行业").assessStandardDescription("平均毛利率50%-80%").displayColor("#1890FF").sort(2).status(1).build());
        INIT_INDUSTRY_ATTRACTION_ELEMENT_TWO.add(IndustryAttractionElement.builder().assessStandardName("平均利润行业").assessStandardDescription("平均毛利率30%-50%").displayColor("#C0C4CC").sort(3).status(1).build());
        INIT_INDUSTRY_ATTRACTION_ELEMENT_TWO.add(IndustryAttractionElement.builder().assessStandardName("低利润行业").assessStandardDescription("平均毛利率低于30%").displayColor("#F94E4D").sort(4).status(1).build());
    }

    /**
     * 行业吸引力配置预制详情集中度
     */
    private static final List<IndustryAttractionElement> INIT_INDUSTRY_ATTRACTION_ELEMENT_Three = new ArrayList<>(4);

    static {
        INIT_INDUSTRY_ATTRACTION_ELEMENT_Three.add(IndustryAttractionElement.builder().assessStandardName("极高寡占型").assessStandardDescription("CR8 ≥ 70%").displayColor("#F94E4D").sort(1).status(1).build());
        INIT_INDUSTRY_ATTRACTION_ELEMENT_Three.add(IndustryAttractionElement.builder().assessStandardName("低集中寡占型").assessStandardDescription("40% ≤ CR8 < 70%").displayColor("#1890FF").sort(2).status(1).build());
        INIT_INDUSTRY_ATTRACTION_ELEMENT_Three.add(IndustryAttractionElement.builder().assessStandardName("低集中竞争型").assessStandardDescription("20% ≤ CR8 < 40%").displayColor("#1890FF").sort(3).status(1).build());
        INIT_INDUSTRY_ATTRACTION_ELEMENT_Three.add(IndustryAttractionElement.builder().assessStandardName("分散竞争型").assessStandardDescription("CR8 < 20%").displayColor("#C0C4CC").sort(4).status(1).build());
    }

    /**
     * 查询行业吸引力表
     *
     * @param industryAttractionId 行业吸引力表主键
     * @return 行业吸引力表
     */
    @Override
    public IndustryAttractionDTO selectIndustryAttractionByIndustryAttractionId(Long industryAttractionId) {
        IndustryAttractionDTO industryAttractionDTO = industryAttractionMapper.selectIndustryAttractionByIndustryAttractionId(industryAttractionId);
        List<IndustryAttractionElementDTO> industryAttractionElementDTOS = industryAttractionElementMapper.selectIndustryAttractionElementByIndustryAttractionId(industryAttractionId);
        if (StringUtils.isNotEmpty(industryAttractionElementDTOS)) {
            industryAttractionDTO.setIndustryAttractionElementDTOS(industryAttractionElementDTOS);
        }
        return industryAttractionDTO;
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
        List<IndustryAttractionDTO> industryAttractionDTOS = industryAttractionMapper.selectIndustryAttractionList(industryAttraction);
        if (StringUtils.isNotEmpty(industryAttractionDTOS)) {
            List<Long> industryAttractionIds = industryAttractionDTOS.stream().map(IndustryAttractionDTO::getIndustryAttractionId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(industryAttractionIds)) {
                List<IndustryAttractionElementDTO> industryAttractionElementDTOList = industryAttractionElementMapper.selectIndustryAttractionElementByIndustryAttractionIds(industryAttractionIds);
                if (StringUtils.isNotEmpty(industryAttractionElementDTOList)) {
                    for (IndustryAttractionDTO attractionDTO : industryAttractionDTOS) {
                        List<IndustryAttractionElementDTO> industryAttractionElementDTOS = new ArrayList<>();
                        for (IndustryAttractionElementDTO industryAttractionElementDTO : industryAttractionElementDTOList) {
                            if (attractionDTO.getIndustryAttractionId().equals(industryAttractionElementDTO.getIndustryAttractionId())) {
                                industryAttractionElementDTOS.add(industryAttractionElementDTO);
                            }
                        }
                        attractionDTO.setIndustryAttractionElementDTOS(industryAttractionElementDTOS);
                    }
                }
            }
        }
        return industryAttractionDTOS;
    }

    /**
     * 新增行业吸引力表
     *
     * @param industryAttractionDTO 行业吸引力表
     * @return 结果
     */
    @Override
    @Transactional
    public IndustryAttractionDTO insertIndustryAttraction(IndustryAttractionDTO industryAttractionDTO) {
        IndustryAttractionDTO industryAttractionDTO1 = industryAttractionMapper.selectIndustryAttractionByAttractionElementName(industryAttractionDTO.getAttractionElementName());
        if (StringUtils.isNotNull(industryAttractionDTO1)) {
            throw new ServiceException(industryAttractionDTO.getAttractionElementName() + "已存在！请重新填写！");
        }
        List<IndustryAttractionElementDTO> industryAttractionElementDTOS = industryAttractionDTO.getIndustryAttractionElementDTOS();
        List<IndustryAttractionElement> industryAttractionElementList = new ArrayList<>();
        IndustryAttraction industryAttraction = new IndustryAttraction();
        industryAttraction.setAttractionElementName(industryAttractionDTO.getAttractionElementName());
        industryAttraction.setStatus(industryAttractionDTO.getStatus());
        industryAttraction.setCreateBy(SecurityUtils.getUserId());
        industryAttraction.setCreateTime(DateUtils.getNowDate());
        industryAttraction.setUpdateTime(DateUtils.getNowDate());
        industryAttraction.setUpdateBy(SecurityUtils.getUserId());
        industryAttraction.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        try {
            industryAttractionMapper.insertIndustryAttraction(industryAttraction);
        } catch (Exception e) {
            throw new ServiceException("新增行业吸引力失败");
        }
        if (StringUtils.isNotEmpty(industryAttractionElementDTOS)) {
            for (int i = 0; i < industryAttractionElementDTOS.size(); i++) {
                IndustryAttractionElement industryAttractionElement = new IndustryAttractionElement();
                BeanUtils.copyProperties(industryAttractionElementDTOS.get(i), industryAttractionElement);
                industryAttractionElement.setIndustryAttractionId(industryAttraction.getIndustryAttractionId());
                industryAttractionElement.setSort(i + 1);
                industryAttractionElement.setCreateBy(SecurityUtils.getUserId());
                industryAttractionElement.setCreateTime(DateUtils.getNowDate());
                industryAttractionElement.setUpdateTime(DateUtils.getNowDate());
                industryAttractionElement.setUpdateBy(SecurityUtils.getUserId());
                industryAttractionElement.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                industryAttractionElementList.add(industryAttractionElement);
            }
        }
        if (StringUtils.isNotEmpty(industryAttractionElementList)) {
            try {
                industryAttractionElementMapper.batchIndustryAttractionElement(industryAttractionElementList);
            } catch (Exception e) {
                throw new ServiceException("批量新增行业吸引力要素失败");
            }
        }
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
    @Transactional
    public int updateIndustryAttraction(IndustryAttractionDTO industryAttractionDTO) {
        //行业吸引力要素名称
        String attractionElementName = industryAttractionDTO.getAttractionElementName();

        int i = 0;
        //接收前端行业吸引力要素集合
        List<IndustryAttractionElementDTO> industryAttractionElementDTOS = industryAttractionDTO.getIndustryAttractionElementDTOS();
        //数据库已存在的行业吸引力要素集合
        List<IndustryAttractionElementDTO> industryAttractionElementDTOList = industryAttractionElementMapper.selectIndustryAttractionElementByIndustryAttractionId(industryAttractionDTO.getIndustryAttractionId());
        IndustryAttraction industryAttraction = new IndustryAttraction();
        BeanUtils.copyProperties(industryAttractionDTO, industryAttraction);
        IndustryAttractionDTO industryAttractionDTO1 = industryAttractionMapper.selectIndustryAttractionByIndustryAttractionId(industryAttractionDTO.getIndustryAttractionId());
        if (StringUtils.isNull(industryAttractionDTO1)){
            throw new ServiceException("数据不存在 请刷新页面重试！");
        }
        List<IndustryAttractionDTO> industryAttractionDTOS = industryAttractionMapper.selectIndustryAttractionList(new IndustryAttraction());
        if (StringUtils.isNotEmpty(industryAttractionDTOS)){

            List<IndustryAttractionDTO> IndustryAttractionList = industryAttractionDTOS.stream().filter(f -> !f.getAttractionElementName().equals(industryAttractionDTO1.getAttractionElementName())).collect(Collectors.toList());

          if (StringUtils.isNotEmpty(IndustryAttractionList)){
              List<String> collect = IndustryAttractionList.stream().map(IndustryAttractionDTO::getAttractionElementName).collect(Collectors.toList());
              if (collect.contains(attractionElementName)){

                  throw new ServiceException("行业吸引力要素名称重复 请重新修改！");
              }

          }
        }

        industryAttraction.setUpdateTime(DateUtils.getNowDate());
        industryAttraction.setUpdateBy(SecurityUtils.getUserId());
        try {
            i = industryAttractionMapper.updateIndustryAttraction(industryAttraction);
        } catch (Exception e) {
            throw new ServiceException("修改行业吸引力失败");
        }
        //差集
        List<Long> industryAttractionElementIds = new ArrayList<>();
        if (StringUtils.isNotEmpty(industryAttractionElementDTOList)) {
            if (StringUtils.isNotEmpty(industryAttractionElementDTOS)) {
                //stream流求差集
                industryAttractionElementIds = industryAttractionElementDTOList.stream().filter(a ->
                        !industryAttractionElementDTOS.stream().map(IndustryAttractionElementDTO::getIndustryAttractionElementId).collect(Collectors.toList()).contains(a.getIndustryAttractionElementId())
                ).collect(Collectors.toList()).stream().map(IndustryAttractionElementDTO::getIndustryAttractionElementId).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(industryAttractionElementIds)) {
                    try {
                        industryAttractionElementMapper.logicDeleteIndustryAttractionElementByIndustryAttractionElementIds(industryAttractionElementIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                    } catch (Exception e) {
                        throw new ServiceException("逻辑批量删除行业吸引力要素失败");
                    }
                }
                //新增集合
                List<IndustryAttractionElement> industryAttractionElementAddList = new ArrayList<>();
                //修改集合
                List<IndustryAttractionElement> industryAttractionElementUpdateList = new ArrayList<>();
                for (int i1 = 0; i1 < industryAttractionElementDTOS.size(); i1++) {
                    IndustryAttractionElement industryAttractionElement = new IndustryAttractionElement();
                    BeanUtils.copyProperties(industryAttractionElementDTOS.get(i1), industryAttractionElement);
                    industryAttractionElement.setSort(i1 + 1);
                    if (StringUtils.isNotNull(industryAttractionElementDTOS.get(i1).getIndustryAttractionElementId())) {
                        industryAttractionElement.setUpdateTime(DateUtils.getNowDate());
                        industryAttractionElement.setUpdateBy(SecurityUtils.getUserId());
                        industryAttractionElementUpdateList.add(industryAttractionElement);
                    } else {
                        industryAttractionElement.setIndustryAttractionId(industryAttraction.getIndustryAttractionId());
                        industryAttractionElement.setCreateBy(SecurityUtils.getUserId());
                        industryAttractionElement.setCreateTime(DateUtils.getNowDate());
                        industryAttractionElement.setUpdateTime(DateUtils.getNowDate());
                        industryAttractionElement.setUpdateBy(SecurityUtils.getUserId());
                        industryAttractionElement.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                        industryAttractionElementAddList.add(industryAttractionElement);
                    }
                }
                if (StringUtils.isNotEmpty(industryAttractionElementAddList)) {
                    try {
                        industryAttractionElementMapper.batchIndustryAttractionElement(industryAttractionElementAddList);
                    } catch (Exception e) {
                        throw new ServiceException("批量新增行业吸引力要素失败");
                    }
                }
                if (StringUtils.isNotEmpty(industryAttractionElementUpdateList)) {
                    try {
                        industryAttractionElementMapper.updateIndustryAttractionElements(industryAttractionElementUpdateList);
                    } catch (Exception e) {
                        throw new ServiceException("批量修改行业吸引力要素失败");
                    }
                }
            } else {
                industryAttractionElementIds = industryAttractionElementDTOList.stream().map(IndustryAttractionElementDTO::getIndustryAttractionElementId).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(industryAttractionElementIds)) {
                    try {
                        industryAttractionElementMapper.logicDeleteIndustryAttractionElementByIndustryAttractionElementIds(industryAttractionElementIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                    } catch (Exception e) {
                        throw new ServiceException("逻辑批量删除行业吸引力要素失败");
                    }
                }
            }

        } else {
            if (StringUtils.isNotEmpty(industryAttractionElementDTOS)) {
                List<IndustryAttractionElement> industryAttractionElementList = new ArrayList<>();
                for (int i1 = 0; i1 < industryAttractionElementDTOS.size(); i1++) {
                    IndustryAttractionElement industryAttractionElement = new IndustryAttractionElement();
                    BeanUtils.copyProperties(industryAttractionElementDTOS.get(i1), industryAttractionElement);
                    industryAttractionElement.setIndustryAttractionId(industryAttraction.getIndustryAttractionId());
                    industryAttractionElement.setSort(i1 + 1);
                    industryAttractionElement.setCreateBy(SecurityUtils.getUserId());
                    industryAttractionElement.setCreateTime(DateUtils.getNowDate());
                    industryAttractionElement.setUpdateTime(DateUtils.getNowDate());
                    industryAttractionElement.setUpdateBy(SecurityUtils.getUserId());
                    industryAttractionElement.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                    industryAttractionElementList.add(industryAttractionElement);
                }
                if (StringUtils.isNotEmpty(industryAttractionElementList)) {
                    try {
                        industryAttractionElementMapper.batchIndustryAttractionElement(industryAttractionElementList);
                    } catch (Exception e) {
                        throw new ServiceException("批量新增行业吸引力要素失败");
                    }
                }
            }
        }
        return i;
    }

    /**
     * 逻辑批量删除行业吸引力表
     *
     * @param industryAttractionIds 主键集合
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteIndustryAttractionByIndustryAttractionIds(List<Long> industryAttractionIds) {
        int i = 0;
        // todo 被行业引用不可删除
        i = industryAttractionMapper.logicDeleteIndustryAttractionByIndustryAttractionIds(industryAttractionIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
        List<IndustryAttractionElementDTO> industryAttractionElementDTOS = industryAttractionElementMapper.selectIndustryAttractionElementByIndustryAttractionIds(industryAttractionIds);
        if (StringUtils.isNotEmpty(industryAttractionElementDTOS)) {
            List<Long> industryAttractionElementIds = industryAttractionElementDTOS.stream().map(IndustryAttractionElementDTO::getIndustryAttractionElementId).collect(Collectors.toList());
            try {
                industryAttractionElementMapper.logicDeleteIndustryAttractionElementByIndustryAttractionElementIds(industryAttractionElementIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
            } catch (Exception e) {
                throw new ServiceException("逻辑批量删除行业吸引力要素失败");
            }
        }
        return i;
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
    @Transactional
    public int logicDeleteIndustryAttractionByIndustryAttractionId(IndustryAttractionDTO industryAttractionDTO) {
        int i = 0;
        // todo 被行业引用不可删除
        IndustryAttraction industryAttraction = new IndustryAttraction();
        industryAttraction.setIndustryAttractionId(industryAttractionDTO.getIndustryAttractionId());
        industryAttraction.setUpdateTime(DateUtils.getNowDate());
        industryAttraction.setUpdateBy(SecurityUtils.getUserId());
        i = industryAttractionMapper.logicDeleteIndustryAttractionByIndustryAttractionId(industryAttraction);
        List<IndustryAttractionElementDTO> industryAttractionElementDTOS = industryAttractionElementMapper.selectIndustryAttractionElementByIndustryAttractionId(industryAttractionDTO.getIndustryAttractionId());
        if (StringUtils.isNotEmpty(industryAttractionElementDTOS)) {
            List<Long> industryAttractionElementIds = industryAttractionElementDTOS.stream().map(IndustryAttractionElementDTO::getIndustryAttractionElementId).collect(Collectors.toList());
            try {
                industryAttractionElementMapper.logicDeleteIndustryAttractionElementByIndustryAttractionElementIds(industryAttractionElementIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
            } catch (Exception e) {
                throw new ServiceException("逻辑批量删除行业吸引力要素失败");
            }
        }
        return i;
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
    @Override
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
    @Override
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

