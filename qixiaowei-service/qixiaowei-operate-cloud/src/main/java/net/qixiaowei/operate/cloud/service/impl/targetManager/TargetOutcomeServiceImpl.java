package net.qixiaowei.operate.cloud.service.impl.targetManager;

import java.util.List;
import net.qixiaowei.integration.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetOutcome;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetOutcomeExcel;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetOutcomeDTO;
import net.qixiaowei.operate.cloud.mapper.targetManager.TargetOutcomeMapper;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetOutcomeService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;


/**
* TargetOutcomeService业务层处理
* @author TANGMICHI
* @since 2022-11-07
*/
@Service
public class TargetOutcomeServiceImpl implements ITargetOutcomeService{
    @Autowired
    private TargetOutcomeMapper targetOutcomeMapper;

    /**
    * 查询目标结果表
    *
    * @param targetOutcomeId 目标结果表主键
    * @return 目标结果表
    */
    @Override
    public TargetOutcomeDTO selectTargetOutcomeByTargetOutcomeId(Long targetOutcomeId)
    {
    return targetOutcomeMapper.selectTargetOutcomeByTargetOutcomeId(targetOutcomeId);
    }

    /**
    * 查询目标结果表列表
    *
    * @param targetOutcomeDTO 目标结果表
    * @return 目标结果表
    */
    @Override
    public List<TargetOutcomeDTO> selectTargetOutcomeList(TargetOutcomeDTO targetOutcomeDTO)
    {
    TargetOutcome targetOutcome=new TargetOutcome();
    BeanUtils.copyProperties(targetOutcomeDTO,targetOutcome);
    return targetOutcomeMapper.selectTargetOutcomeList(targetOutcome);
    }

    /**
    * 新增目标结果表
    *
    * @param targetOutcomeDTO 目标结果表
    * @return 结果
    */
    @Override
    public TargetOutcomeDTO insertTargetOutcome(TargetOutcomeDTO targetOutcomeDTO){
    TargetOutcome targetOutcome=new TargetOutcome();
    BeanUtils.copyProperties(targetOutcomeDTO,targetOutcome);
    targetOutcome.setCreateBy(SecurityUtils.getUserId());
    targetOutcome.setCreateTime(DateUtils.getNowDate());
    targetOutcome.setUpdateTime(DateUtils.getNowDate());
    targetOutcome.setUpdateBy(SecurityUtils.getUserId());
    targetOutcome.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
    targetOutcomeMapper.insertTargetOutcome(targetOutcome);
    targetOutcomeDTO.setTargetOutcomeId(targetOutcome.getTargetOutcomeId());
    return targetOutcomeDTO;
    }

    /**
    * 修改目标结果表
    *
    * @param targetOutcomeDTO 目标结果表
    * @return 结果
    */
    @Override
    public int updateTargetOutcome(TargetOutcomeDTO targetOutcomeDTO)
    {
    TargetOutcome targetOutcome=new TargetOutcome();
    BeanUtils.copyProperties(targetOutcomeDTO,targetOutcome);
    targetOutcome.setUpdateTime(DateUtils.getNowDate());
    targetOutcome.setUpdateBy(SecurityUtils.getUserId());
    return targetOutcomeMapper.updateTargetOutcome(targetOutcome);
    }

    /**
    * 逻辑批量删除目标结果表
    *
    * @param targetOutcomeIds 主键集合
    * @return 结果
    */
    @Override
    public int logicDeleteTargetOutcomeByTargetOutcomeIds(List<Long> targetOutcomeIds){
    return targetOutcomeMapper.logicDeleteTargetOutcomeByTargetOutcomeIds(targetOutcomeIds,SecurityUtils.getUserId(),DateUtils.getNowDate());
    }

    /**
    * 物理删除目标结果表信息
    *
    * @param targetOutcomeId 目标结果表主键
    * @return 结果
    */
    @Override
    public int deleteTargetOutcomeByTargetOutcomeId(Long targetOutcomeId)
    {
    return targetOutcomeMapper.deleteTargetOutcomeByTargetOutcomeId(targetOutcomeId);
    }

     /**
     * 逻辑删除目标结果表信息
     *
     * @param  targetOutcomeDTO 目标结果表
     * @return 结果
     */
     @Override
     public int logicDeleteTargetOutcomeByTargetOutcomeId(TargetOutcomeDTO targetOutcomeDTO)
     {
     TargetOutcome targetOutcome=new TargetOutcome();
     targetOutcome.setTargetOutcomeId(targetOutcomeDTO.getTargetOutcomeId());
     targetOutcome.setUpdateTime(DateUtils.getNowDate());
     targetOutcome.setUpdateBy(SecurityUtils.getUserId());
     return targetOutcomeMapper.logicDeleteTargetOutcomeByTargetOutcomeId(targetOutcome);
     }

     /**
     * 物理删除目标结果表信息
     *
     * @param  targetOutcomeDTO 目标结果表
     * @return 结果
     */
     
     @Override
     public int deleteTargetOutcomeByTargetOutcomeId(TargetOutcomeDTO targetOutcomeDTO)
     {
     TargetOutcome targetOutcome=new TargetOutcome();
     BeanUtils.copyProperties(targetOutcomeDTO,targetOutcome);
     return targetOutcomeMapper.deleteTargetOutcomeByTargetOutcomeId(targetOutcome.getTargetOutcomeId());
     }
     /**
     * 物理批量删除目标结果表
     *
     * @param targetOutcomeDtos 需要删除的目标结果表主键
     * @return 结果
     */
     
     @Override
     public int deleteTargetOutcomeByTargetOutcomeIds(List<TargetOutcomeDTO> targetOutcomeDtos){
     List<Long> stringList = new ArrayList();
     for (TargetOutcomeDTO targetOutcomeDTO : targetOutcomeDtos) {
     stringList.add(targetOutcomeDTO.getTargetOutcomeId());
     }
     return targetOutcomeMapper.deleteTargetOutcomeByTargetOutcomeIds(stringList);
     }

    /**
    * 批量新增目标结果表信息
    *
    * @param targetOutcomeDtos 目标结果表对象
    */
    
    public int insertTargetOutcomes(List<TargetOutcomeDTO> targetOutcomeDtos){
      List<TargetOutcome> targetOutcomeList = new ArrayList();

    for (TargetOutcomeDTO targetOutcomeDTO : targetOutcomeDtos) {
      TargetOutcome targetOutcome =new TargetOutcome();
      BeanUtils.copyProperties(targetOutcomeDTO,targetOutcome);
       targetOutcome.setCreateBy(SecurityUtils.getUserId());
       targetOutcome.setCreateTime(DateUtils.getNowDate());
       targetOutcome.setUpdateTime(DateUtils.getNowDate());
       targetOutcome.setUpdateBy(SecurityUtils.getUserId());
       targetOutcome.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
      targetOutcomeList.add(targetOutcome);
    }
    return targetOutcomeMapper.batchTargetOutcome(targetOutcomeList);
    }

    /**
    * 批量修改目标结果表信息
    *
    * @param targetOutcomeDtos 目标结果表对象
    */
    
    public int updateTargetOutcomes(List<TargetOutcomeDTO> targetOutcomeDtos){
     List<TargetOutcome> targetOutcomeList = new ArrayList();

     for (TargetOutcomeDTO targetOutcomeDTO : targetOutcomeDtos) {
     TargetOutcome targetOutcome =new TargetOutcome();
     BeanUtils.copyProperties(targetOutcomeDTO,targetOutcome);
        targetOutcome.setCreateBy(SecurityUtils.getUserId());
        targetOutcome.setCreateTime(DateUtils.getNowDate());
        targetOutcome.setUpdateTime(DateUtils.getNowDate());
        targetOutcome.setUpdateBy(SecurityUtils.getUserId());
     targetOutcomeList.add(targetOutcome);
     }
     return targetOutcomeMapper.updateTargetOutcomes(targetOutcomeList);
    }
    /**
    * 导入Excel
    * @param list
    */
    @Override
    public void importTargetOutcome(List<TargetOutcomeExcel> list) {
    List<TargetOutcome> targetOutcomeList = new ArrayList<>();
        list.forEach(l -> {
        TargetOutcome targetOutcome = new TargetOutcome();
        BeanUtils.copyProperties(l, targetOutcome);
        targetOutcome.setCreateBy(SecurityUtils.getUserId());
        targetOutcome.setCreateTime(DateUtils.getNowDate());
        targetOutcome.setUpdateTime(DateUtils.getNowDate());
        targetOutcome.setUpdateBy(SecurityUtils.getUserId());
        targetOutcome.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        targetOutcomeList.add(targetOutcome);
        });
        try {
        targetOutcomeMapper.batchTargetOutcome(targetOutcomeList);
        } catch (Exception e) {
        throw new ServiceException("导入目标结果表失败");
        }
    }
    /**
    * 导出Excel
    * @param targetOutcomeDTO
    * @return
    */
    @Override
    public List<TargetOutcomeExcel> exportTargetOutcome(TargetOutcomeDTO targetOutcomeDTO) {
        TargetOutcome targetOutcome = new TargetOutcome();
        BeanUtils.copyProperties(targetOutcomeDTO, targetOutcome);
        List<TargetOutcomeDTO> targetOutcomeDTOList = targetOutcomeMapper.selectTargetOutcomeList(targetOutcome);
        List<TargetOutcomeExcel> targetOutcomeExcelList = new ArrayList<>();
        return targetOutcomeExcelList;
    }
}

