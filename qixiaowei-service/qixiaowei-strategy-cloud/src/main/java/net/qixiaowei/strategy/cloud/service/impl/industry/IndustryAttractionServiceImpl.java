package net.qixiaowei.strategy.cloud.service.impl.industry;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.strategy.cloud.api.domain.industry.IndustryAttraction;
import net.qixiaowei.strategy.cloud.api.dto.industry.IndustryAttractionDTO;
import net.qixiaowei.strategy.cloud.mapper.industry.IndustryAttractionMapper;
import net.qixiaowei.strategy.cloud.service.industry.IIndustryAttractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
* IndustryAttractionService业务层处理
* @author TANGMICHI
* @since 2023-02-20
*/
@Service
public class IndustryAttractionServiceImpl implements IIndustryAttractionService{
    @Autowired
    private IndustryAttractionMapper industryAttractionMapper;

    /**
    * 查询行业吸引力表
    *
    * @param industryAttractionId 行业吸引力表主键
    * @return 行业吸引力表
    */
    @Override
    public IndustryAttractionDTO selectIndustryAttractionByIndustryAttractionId(Long industryAttractionId)
    {
    return industryAttractionMapper.selectIndustryAttractionByIndustryAttractionId(industryAttractionId);
    }

    /**
    * 查询行业吸引力表列表
    *
    * @param industryAttractionDTO 行业吸引力表
    * @return 行业吸引力表
    */
    @Override
    public List<IndustryAttractionDTO> selectIndustryAttractionList(IndustryAttractionDTO industryAttractionDTO)
    {
    IndustryAttraction industryAttraction=new IndustryAttraction();
    BeanUtils.copyProperties(industryAttractionDTO,industryAttraction);
    return industryAttractionMapper.selectIndustryAttractionList(industryAttraction);
    }

    /**
    * 新增行业吸引力表
    *
    * @param industryAttractionDTO 行业吸引力表
    * @return 结果
    */
    @Override
    public IndustryAttractionDTO insertIndustryAttraction(IndustryAttractionDTO industryAttractionDTO){
    IndustryAttraction industryAttraction=new IndustryAttraction();
    BeanUtils.copyProperties(industryAttractionDTO,industryAttraction);
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
    public int updateIndustryAttraction(IndustryAttractionDTO industryAttractionDTO)
    {
    IndustryAttraction industryAttraction=new IndustryAttraction();
    BeanUtils.copyProperties(industryAttractionDTO,industryAttraction);
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
    public int logicDeleteIndustryAttractionByIndustryAttractionIds(List<Long> industryAttractionIds){
    return industryAttractionMapper.logicDeleteIndustryAttractionByIndustryAttractionIds(industryAttractionIds,SecurityUtils.getUserId(),DateUtils.getNowDate());
    }

    /**
    * 物理删除行业吸引力表信息
    *
    * @param industryAttractionId 行业吸引力表主键
    * @return 结果
    */
    @Override
    public int deleteIndustryAttractionByIndustryAttractionId(Long industryAttractionId)
    {
    return industryAttractionMapper.deleteIndustryAttractionByIndustryAttractionId(industryAttractionId);
    }

     /**
     * 逻辑删除行业吸引力表信息
     *
     * @param  industryAttractionDTO 行业吸引力表
     * @return 结果
     */
     @Override
     public int logicDeleteIndustryAttractionByIndustryAttractionId(IndustryAttractionDTO industryAttractionDTO)
     {
     IndustryAttraction industryAttraction=new IndustryAttraction();
     industryAttraction.setIndustryAttractionId(industryAttractionDTO.getIndustryAttractionId());
     industryAttraction.setUpdateTime(DateUtils.getNowDate());
     industryAttraction.setUpdateBy(SecurityUtils.getUserId());
     return industryAttractionMapper.logicDeleteIndustryAttractionByIndustryAttractionId(industryAttraction);
     }

     /**
     * 物理删除行业吸引力表信息
     *
     * @param  industryAttractionDTO 行业吸引力表
     * @return 结果
     */
     
     @Override
     public int deleteIndustryAttractionByIndustryAttractionId(IndustryAttractionDTO industryAttractionDTO)
     {
     IndustryAttraction industryAttraction=new IndustryAttraction();
     BeanUtils.copyProperties(industryAttractionDTO,industryAttraction);
     return industryAttractionMapper.deleteIndustryAttractionByIndustryAttractionId(industryAttraction.getIndustryAttractionId());
     }
     /**
     * 物理批量删除行业吸引力表
     *
     * @param industryAttractionDtos 需要删除的行业吸引力表主键
     * @return 结果
     */
     
     @Override
     public int deleteIndustryAttractionByIndustryAttractionIds(List<IndustryAttractionDTO> industryAttractionDtos){
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
    
    public int insertIndustryAttractions(List<IndustryAttractionDTO> industryAttractionDtos){
      List<IndustryAttraction> industryAttractionList = new ArrayList();

    for (IndustryAttractionDTO industryAttractionDTO : industryAttractionDtos) {
      IndustryAttraction industryAttraction =new IndustryAttraction();
      BeanUtils.copyProperties(industryAttractionDTO,industryAttraction);
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
    
    public int updateIndustryAttractions(List<IndustryAttractionDTO> industryAttractionDtos){
     List<IndustryAttraction> industryAttractionList = new ArrayList();

     for (IndustryAttractionDTO industryAttractionDTO : industryAttractionDtos) {
     IndustryAttraction industryAttraction =new IndustryAttraction();
     BeanUtils.copyProperties(industryAttractionDTO,industryAttraction);
        industryAttraction.setCreateBy(SecurityUtils.getUserId());
        industryAttraction.setCreateTime(DateUtils.getNowDate());
        industryAttraction.setUpdateTime(DateUtils.getNowDate());
        industryAttraction.setUpdateBy(SecurityUtils.getUserId());
     industryAttractionList.add(industryAttraction);
     }
     return industryAttractionMapper.updateIndustryAttractions(industryAttractionList);
    }
}

