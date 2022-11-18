package net.qixiaowei.operate.cloud.service.impl.salary;

import java.util.List;
import net.qixiaowei.integration.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.salary.EmolumentPlan;
import net.qixiaowei.operate.cloud.api.dto.salary.EmolumentPlanDTO;
import net.qixiaowei.operate.cloud.mapper.salary.EmolumentPlanMapper;
import net.qixiaowei.operate.cloud.service.salary.IEmolumentPlanService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;


/**
* EmolumentPlanService业务层处理
* @author TANGMICHI
* @since 2022-11-18
*/
@Service
public class EmolumentPlanServiceImpl implements IEmolumentPlanService{
    @Autowired
    private EmolumentPlanMapper emolumentPlanMapper;

    /**
    * 查询薪酬规划表
    *
    * @param emolumentPlanId 薪酬规划表主键
    * @return 薪酬规划表
    */
    @Override
    public EmolumentPlanDTO selectEmolumentPlanByEmolumentPlanId(Long emolumentPlanId)
    {
    return emolumentPlanMapper.selectEmolumentPlanByEmolumentPlanId(emolumentPlanId);
    }

    /**
    * 查询薪酬规划表列表
    *
    * @param emolumentPlanDTO 薪酬规划表
    * @return 薪酬规划表
    */
    @Override
    public List<EmolumentPlanDTO> selectEmolumentPlanList(EmolumentPlanDTO emolumentPlanDTO)
    {
    EmolumentPlan emolumentPlan=new EmolumentPlan();
    BeanUtils.copyProperties(emolumentPlanDTO,emolumentPlan);
    return emolumentPlanMapper.selectEmolumentPlanList(emolumentPlan);
    }

    /**
    * 新增薪酬规划表
    *
    * @param emolumentPlanDTO 薪酬规划表
    * @return 结果
    */
    @Override
    public EmolumentPlanDTO insertEmolumentPlan(EmolumentPlanDTO emolumentPlanDTO){
    EmolumentPlan emolumentPlan=new EmolumentPlan();
    BeanUtils.copyProperties(emolumentPlanDTO,emolumentPlan);
    emolumentPlan.setCreateBy(SecurityUtils.getUserId());
    emolumentPlan.setCreateTime(DateUtils.getNowDate());
    emolumentPlan.setUpdateTime(DateUtils.getNowDate());
    emolumentPlan.setUpdateBy(SecurityUtils.getUserId());
    emolumentPlan.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
    emolumentPlanMapper.insertEmolumentPlan(emolumentPlan);
    emolumentPlanDTO.setEmolumentPlanId(emolumentPlan.getEmolumentPlanId());
    return emolumentPlanDTO;
    }

    /**
    * 修改薪酬规划表
    *
    * @param emolumentPlanDTO 薪酬规划表
    * @return 结果
    */
    @Override
    public int updateEmolumentPlan(EmolumentPlanDTO emolumentPlanDTO)
    {
    EmolumentPlan emolumentPlan=new EmolumentPlan();
    BeanUtils.copyProperties(emolumentPlanDTO,emolumentPlan);
    emolumentPlan.setUpdateTime(DateUtils.getNowDate());
    emolumentPlan.setUpdateBy(SecurityUtils.getUserId());
    return emolumentPlanMapper.updateEmolumentPlan(emolumentPlan);
    }

    /**
    * 逻辑批量删除薪酬规划表
    *
    * @param emolumentPlanIds 主键集合
    * @return 结果
    */
    @Override
    public int logicDeleteEmolumentPlanByEmolumentPlanIds(List<Long> emolumentPlanIds){
    return emolumentPlanMapper.logicDeleteEmolumentPlanByEmolumentPlanIds(emolumentPlanIds,SecurityUtils.getUserId(),DateUtils.getNowDate());
    }

    /**
    * 物理删除薪酬规划表信息
    *
    * @param emolumentPlanId 薪酬规划表主键
    * @return 结果
    */
    @Override
    public int deleteEmolumentPlanByEmolumentPlanId(Long emolumentPlanId)
    {
    return emolumentPlanMapper.deleteEmolumentPlanByEmolumentPlanId(emolumentPlanId);
    }

     /**
     * 逻辑删除薪酬规划表信息
     *
     * @param  emolumentPlanDTO 薪酬规划表
     * @return 结果
     */
     @Override
     public int logicDeleteEmolumentPlanByEmolumentPlanId(EmolumentPlanDTO emolumentPlanDTO)
     {
     EmolumentPlan emolumentPlan=new EmolumentPlan();
     emolumentPlan.setEmolumentPlanId(emolumentPlanDTO.getEmolumentPlanId());
     emolumentPlan.setUpdateTime(DateUtils.getNowDate());
     emolumentPlan.setUpdateBy(SecurityUtils.getUserId());
     return emolumentPlanMapper.logicDeleteEmolumentPlanByEmolumentPlanId(emolumentPlan);
     }

     /**
     * 物理删除薪酬规划表信息
     *
     * @param  emolumentPlanDTO 薪酬规划表
     * @return 结果
     */
     
     @Override
     public int deleteEmolumentPlanByEmolumentPlanId(EmolumentPlanDTO emolumentPlanDTO)
     {
     EmolumentPlan emolumentPlan=new EmolumentPlan();
     BeanUtils.copyProperties(emolumentPlanDTO,emolumentPlan);
     return emolumentPlanMapper.deleteEmolumentPlanByEmolumentPlanId(emolumentPlan.getEmolumentPlanId());
     }
     /**
     * 物理批量删除薪酬规划表
     *
     * @param emolumentPlanDtos 需要删除的薪酬规划表主键
     * @return 结果
     */
     
     @Override
     public int deleteEmolumentPlanByEmolumentPlanIds(List<EmolumentPlanDTO> emolumentPlanDtos){
     List<Long> stringList = new ArrayList();
     for (EmolumentPlanDTO emolumentPlanDTO : emolumentPlanDtos) {
     stringList.add(emolumentPlanDTO.getEmolumentPlanId());
     }
     return emolumentPlanMapper.deleteEmolumentPlanByEmolumentPlanIds(stringList);
     }

    /**
    * 批量新增薪酬规划表信息
    *
    * @param emolumentPlanDtos 薪酬规划表对象
    */
    
    public int insertEmolumentPlans(List<EmolumentPlanDTO> emolumentPlanDtos){
      List<EmolumentPlan> emolumentPlanList = new ArrayList();

    for (EmolumentPlanDTO emolumentPlanDTO : emolumentPlanDtos) {
      EmolumentPlan emolumentPlan =new EmolumentPlan();
      BeanUtils.copyProperties(emolumentPlanDTO,emolumentPlan);
       emolumentPlan.setCreateBy(SecurityUtils.getUserId());
       emolumentPlan.setCreateTime(DateUtils.getNowDate());
       emolumentPlan.setUpdateTime(DateUtils.getNowDate());
       emolumentPlan.setUpdateBy(SecurityUtils.getUserId());
       emolumentPlan.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
      emolumentPlanList.add(emolumentPlan);
    }
    return emolumentPlanMapper.batchEmolumentPlan(emolumentPlanList);
    }

    /**
    * 批量修改薪酬规划表信息
    *
    * @param emolumentPlanDtos 薪酬规划表对象
    */
    
    public int updateEmolumentPlans(List<EmolumentPlanDTO> emolumentPlanDtos){
     List<EmolumentPlan> emolumentPlanList = new ArrayList();

     for (EmolumentPlanDTO emolumentPlanDTO : emolumentPlanDtos) {
     EmolumentPlan emolumentPlan =new EmolumentPlan();
     BeanUtils.copyProperties(emolumentPlanDTO,emolumentPlan);
        emolumentPlan.setCreateBy(SecurityUtils.getUserId());
        emolumentPlan.setCreateTime(DateUtils.getNowDate());
        emolumentPlan.setUpdateTime(DateUtils.getNowDate());
        emolumentPlan.setUpdateBy(SecurityUtils.getUserId());
     emolumentPlanList.add(emolumentPlan);
     }
     return emolumentPlanMapper.updateEmolumentPlans(emolumentPlanList);
    }
}

