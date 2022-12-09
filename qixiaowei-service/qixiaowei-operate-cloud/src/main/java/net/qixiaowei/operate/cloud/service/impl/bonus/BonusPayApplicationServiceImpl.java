package net.qixiaowei.operate.cloud.service.impl.bonus;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.bonus.BonusPayApplication;
import net.qixiaowei.operate.cloud.api.domain.bonus.BonusPayObjects;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusPayApplicationDTO;

import net.qixiaowei.operate.cloud.api.dto.bonus.BonusPayBudgetDeptDTO;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusPayObjectsDTO;
import net.qixiaowei.operate.cloud.mapper.bonus.BonusPayApplicationMapper;
import net.qixiaowei.operate.cloud.service.bonus.IBonusPayApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
* BonusPayApplicationService业务层处理
* @author TANGMICHI
* @since 2022-12-08
*/
@Service
public class BonusPayApplicationServiceImpl implements IBonusPayApplicationService{
    @Autowired
    private BonusPayApplicationMapper bonusPayApplicationMapper;

    /**
    * 查询奖金发放申请表
    *
    * @param bonusPayApplicationId 奖金发放申请表主键
    * @return 奖金发放申请表
    */
    @Override
    public BonusPayApplicationDTO selectBonusPayApplicationByBonusPayApplicationId(Long bonusPayApplicationId)
    {
    return bonusPayApplicationMapper.selectBonusPayApplicationByBonusPayApplicationId(bonusPayApplicationId);
    }

    /**
    * 查询奖金发放申请表列表
    *
    * @param bonusPayApplicationDTO 奖金发放申请表
    * @return 奖金发放申请表
    */
    @Override
    public List<BonusPayApplicationDTO> selectBonusPayApplicationList(BonusPayApplicationDTO bonusPayApplicationDTO)
    {
    BonusPayApplication bonusPayApplication=new BonusPayApplication();
    BeanUtils.copyProperties(bonusPayApplicationDTO,bonusPayApplication);
    return bonusPayApplicationMapper.selectBonusPayApplicationList(bonusPayApplication);
    }

    /**
    * 新增奖金发放申请表
    *
    * @param bonusPayApplicationDTO 奖金发放申请表
    * @return 结果
    */
    @Override
    public BonusPayApplicationDTO insertBonusPayApplication(BonusPayApplicationDTO bonusPayApplicationDTO){
        //奖金发放预算部门比例集合
        List<BonusPayBudgetDeptDTO> bonusPayBudgetDeptDTOs = bonusPayApplicationDTO.getBonusPayBudgetDeptDTOs();
        //奖金发放对象表集合
        List<BonusPayObjects> bonusPayObjectsAlls = new ArrayList<>();
        //获奖员工集合
        List<BonusPayObjectsDTO> bonusPayObjectsEmployeeDTOs = bonusPayApplicationDTO.getBonusPayObjectsEmployeeDTOs();
        //获奖员工集合
        List<BonusPayObjectsDTO> bonusPayObjectsDeptDTOs = bonusPayApplicationDTO.getBonusPayObjectsDeptDTOs();


        BonusPayApplication bonusPayApplication = new BonusPayApplication();
        BeanUtils.copyProperties(bonusPayApplicationDTO, bonusPayApplication);
        bonusPayApplication.setCreateBy(SecurityUtils.getUserId());
        bonusPayApplication.setCreateTime(DateUtils.getNowDate());
        bonusPayApplication.setUpdateTime(DateUtils.getNowDate());
        bonusPayApplication.setUpdateBy(SecurityUtils.getUserId());
        bonusPayApplication.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        bonusPayApplicationMapper.insertBonusPayApplication(bonusPayApplication);
        bonusPayApplicationDTO.setBonusPayApplicationId(bonusPayApplication.getBonusPayApplicationId());
        return bonusPayApplicationDTO;
    }

    /**
    * 修改奖金发放申请表
    *
    * @param bonusPayApplicationDTO 奖金发放申请表
    * @return 结果
    */
    @Override
    public int updateBonusPayApplication(BonusPayApplicationDTO bonusPayApplicationDTO)
    {
    BonusPayApplication bonusPayApplication=new BonusPayApplication();
    BeanUtils.copyProperties(bonusPayApplicationDTO,bonusPayApplication);
    bonusPayApplication.setUpdateTime(DateUtils.getNowDate());
    bonusPayApplication.setUpdateBy(SecurityUtils.getUserId());
    return bonusPayApplicationMapper.updateBonusPayApplication(bonusPayApplication);
    }

    /**
    * 逻辑批量删除奖金发放申请表
    *
    * @param bonusPayApplicationIds 主键集合
    * @return 结果
    */
    @Override
    public int logicDeleteBonusPayApplicationByBonusPayApplicationIds(List<Long> bonusPayApplicationIds){
    return bonusPayApplicationMapper.logicDeleteBonusPayApplicationByBonusPayApplicationIds(bonusPayApplicationIds,SecurityUtils.getUserId(),DateUtils.getNowDate());
    }

    /**
    * 物理删除奖金发放申请表信息
    *
    * @param bonusPayApplicationId 奖金发放申请表主键
    * @return 结果
    */
    @Override
    public int deleteBonusPayApplicationByBonusPayApplicationId(Long bonusPayApplicationId)
    {
    return bonusPayApplicationMapper.deleteBonusPayApplicationByBonusPayApplicationId(bonusPayApplicationId);
    }

     /**
     * 逻辑删除奖金发放申请表信息
     *
     * @param  bonusPayApplicationDTO 奖金发放申请表
     * @return 结果
     */
     @Override
     public int logicDeleteBonusPayApplicationByBonusPayApplicationId(BonusPayApplicationDTO bonusPayApplicationDTO)
     {
     BonusPayApplication bonusPayApplication=new BonusPayApplication();
     bonusPayApplication.setBonusPayApplicationId(bonusPayApplicationDTO.getBonusPayApplicationId());
     bonusPayApplication.setUpdateTime(DateUtils.getNowDate());
     bonusPayApplication.setUpdateBy(SecurityUtils.getUserId());
     return bonusPayApplicationMapper.logicDeleteBonusPayApplicationByBonusPayApplicationId(bonusPayApplication);
     }

     /**
     * 物理删除奖金发放申请表信息
     *
     * @param  bonusPayApplicationDTO 奖金发放申请表
     * @return 结果
     */
     
     @Override
     public int deleteBonusPayApplicationByBonusPayApplicationId(BonusPayApplicationDTO bonusPayApplicationDTO)
     {
     BonusPayApplication bonusPayApplication=new BonusPayApplication();
     BeanUtils.copyProperties(bonusPayApplicationDTO,bonusPayApplication);
     return bonusPayApplicationMapper.deleteBonusPayApplicationByBonusPayApplicationId(bonusPayApplication.getBonusPayApplicationId());
     }
     /**
     * 物理批量删除奖金发放申请表
     *
     * @param bonusPayApplicationDtos 需要删除的奖金发放申请表主键
     * @return 结果
     */
     
     @Override
     public int deleteBonusPayApplicationByBonusPayApplicationIds(List<BonusPayApplicationDTO> bonusPayApplicationDtos){
     List<Long> stringList = new ArrayList();
     for (BonusPayApplicationDTO bonusPayApplicationDTO : bonusPayApplicationDtos) {
     stringList.add(bonusPayApplicationDTO.getBonusPayApplicationId());
     }
     return bonusPayApplicationMapper.deleteBonusPayApplicationByBonusPayApplicationIds(stringList);
     }

    /**
    * 批量新增奖金发放申请表信息
    *
    * @param bonusPayApplicationDtos 奖金发放申请表对象
    */
    
    public int insertBonusPayApplications(List<BonusPayApplicationDTO> bonusPayApplicationDtos){
      List<BonusPayApplication> bonusPayApplicationList = new ArrayList();

    for (BonusPayApplicationDTO bonusPayApplicationDTO : bonusPayApplicationDtos) {
      BonusPayApplication bonusPayApplication =new BonusPayApplication();
      BeanUtils.copyProperties(bonusPayApplicationDTO,bonusPayApplication);
       bonusPayApplication.setCreateBy(SecurityUtils.getUserId());
       bonusPayApplication.setCreateTime(DateUtils.getNowDate());
       bonusPayApplication.setUpdateTime(DateUtils.getNowDate());
       bonusPayApplication.setUpdateBy(SecurityUtils.getUserId());
       bonusPayApplication.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
      bonusPayApplicationList.add(bonusPayApplication);
    }
    return bonusPayApplicationMapper.batchBonusPayApplication(bonusPayApplicationList);
    }

    /**
    * 批量修改奖金发放申请表信息
    *
    * @param bonusPayApplicationDtos 奖金发放申请表对象
    */
    
    public int updateBonusPayApplications(List<BonusPayApplicationDTO> bonusPayApplicationDtos){
     List<BonusPayApplication> bonusPayApplicationList = new ArrayList();

     for (BonusPayApplicationDTO bonusPayApplicationDTO : bonusPayApplicationDtos) {
     BonusPayApplication bonusPayApplication =new BonusPayApplication();
     BeanUtils.copyProperties(bonusPayApplicationDTO,bonusPayApplication);
        bonusPayApplication.setCreateBy(SecurityUtils.getUserId());
        bonusPayApplication.setCreateTime(DateUtils.getNowDate());
        bonusPayApplication.setUpdateTime(DateUtils.getNowDate());
        bonusPayApplication.setUpdateBy(SecurityUtils.getUserId());
     bonusPayApplicationList.add(bonusPayApplication);
     }
     return bonusPayApplicationMapper.updateBonusPayApplications(bonusPayApplicationList);
    }

}

