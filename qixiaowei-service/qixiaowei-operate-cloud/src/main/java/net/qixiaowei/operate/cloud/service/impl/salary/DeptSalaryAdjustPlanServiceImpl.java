package net.qixiaowei.operate.cloud.service.impl.salary;

import java.util.List;
import net.qixiaowei.integration.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.salary.DeptSalaryAdjustPlan;
import net.qixiaowei.operate.cloud.excel.salary.DeptSalaryAdjustPlanExcel;
import net.qixiaowei.operate.cloud.api.dto.salary.DeptSalaryAdjustPlanDTO;
import net.qixiaowei.operate.cloud.mapper.salary.DeptSalaryAdjustPlanMapper;
import net.qixiaowei.operate.cloud.service.salary.IDeptSalaryAdjustPlanService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;


/**
* DeptSalaryAdjustPlanService业务层处理
* @author Graves
* @since 2022-12-11
*/
@Service
public class DeptSalaryAdjustPlanServiceImpl implements IDeptSalaryAdjustPlanService{
    @Autowired
    private DeptSalaryAdjustPlanMapper deptSalaryAdjustPlanMapper;

    /**
    * 查询部门调薪计划表
    *
    * @param deptSalaryAdjustPlanId 部门调薪计划表主键
    * @return 部门调薪计划表
    */
    @Override
    public DeptSalaryAdjustPlanDTO selectDeptSalaryAdjustPlanByDeptSalaryAdjustPlanId(Long deptSalaryAdjustPlanId)
    {
    return deptSalaryAdjustPlanMapper.selectDeptSalaryAdjustPlanByDeptSalaryAdjustPlanId(deptSalaryAdjustPlanId);
    }

    /**
    * 查询部门调薪计划表列表
    *
    * @param deptSalaryAdjustPlanDTO 部门调薪计划表
    * @return 部门调薪计划表
    */
    @Override
    public List<DeptSalaryAdjustPlanDTO> selectDeptSalaryAdjustPlanList(DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO)
    {
    DeptSalaryAdjustPlan deptSalaryAdjustPlan=new DeptSalaryAdjustPlan();
    BeanUtils.copyProperties(deptSalaryAdjustPlanDTO,deptSalaryAdjustPlan);
    return deptSalaryAdjustPlanMapper.selectDeptSalaryAdjustPlanList(deptSalaryAdjustPlan);
    }

    /**
    * 新增部门调薪计划表
    *
    * @param deptSalaryAdjustPlanDTO 部门调薪计划表
    * @return 结果
    */
    @Override
    public DeptSalaryAdjustPlanDTO insertDeptSalaryAdjustPlan(DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO){
    DeptSalaryAdjustPlan deptSalaryAdjustPlan=new DeptSalaryAdjustPlan();
    BeanUtils.copyProperties(deptSalaryAdjustPlanDTO,deptSalaryAdjustPlan);
    deptSalaryAdjustPlan.setCreateBy(SecurityUtils.getUserId());
    deptSalaryAdjustPlan.setCreateTime(DateUtils.getNowDate());
    deptSalaryAdjustPlan.setUpdateTime(DateUtils.getNowDate());
    deptSalaryAdjustPlan.setUpdateBy(SecurityUtils.getUserId());
    deptSalaryAdjustPlan.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
    deptSalaryAdjustPlanMapper.insertDeptSalaryAdjustPlan(deptSalaryAdjustPlan);
    deptSalaryAdjustPlanDTO.setDeptSalaryAdjustPlanId(deptSalaryAdjustPlan.getDeptSalaryAdjustPlanId());
    return deptSalaryAdjustPlanDTO;
    }

    /**
    * 修改部门调薪计划表
    *
    * @param deptSalaryAdjustPlanDTO 部门调薪计划表
    * @return 结果
    */
    @Override
    public int updateDeptSalaryAdjustPlan(DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO)
    {
    DeptSalaryAdjustPlan deptSalaryAdjustPlan=new DeptSalaryAdjustPlan();
    BeanUtils.copyProperties(deptSalaryAdjustPlanDTO,deptSalaryAdjustPlan);
    deptSalaryAdjustPlan.setUpdateTime(DateUtils.getNowDate());
    deptSalaryAdjustPlan.setUpdateBy(SecurityUtils.getUserId());
    return deptSalaryAdjustPlanMapper.updateDeptSalaryAdjustPlan(deptSalaryAdjustPlan);
    }

    /**
    * 逻辑批量删除部门调薪计划表
    *
    * @param deptSalaryAdjustPlanIds 主键集合
    * @return 结果
    */
    @Override
    public int logicDeleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanIds(List<Long> deptSalaryAdjustPlanIds){
    return deptSalaryAdjustPlanMapper.logicDeleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanIds(deptSalaryAdjustPlanIds,SecurityUtils.getUserId(),DateUtils.getNowDate());
    }

    /**
    * 物理删除部门调薪计划表信息
    *
    * @param deptSalaryAdjustPlanId 部门调薪计划表主键
    * @return 结果
    */
    @Override
    public int deleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanId(Long deptSalaryAdjustPlanId)
    {
    return deptSalaryAdjustPlanMapper.deleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanId(deptSalaryAdjustPlanId);
    }

     /**
     * 逻辑删除部门调薪计划表信息
     *
     * @param  deptSalaryAdjustPlanDTO 部门调薪计划表
     * @return 结果
     */
     @Override
     public int logicDeleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanId(DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO)
     {
     DeptSalaryAdjustPlan deptSalaryAdjustPlan=new DeptSalaryAdjustPlan();
     deptSalaryAdjustPlan.setDeptSalaryAdjustPlanId(deptSalaryAdjustPlanDTO.getDeptSalaryAdjustPlanId());
     deptSalaryAdjustPlan.setUpdateTime(DateUtils.getNowDate());
     deptSalaryAdjustPlan.setUpdateBy(SecurityUtils.getUserId());
     return deptSalaryAdjustPlanMapper.logicDeleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanId(deptSalaryAdjustPlan);
     }

     /**
     * 物理删除部门调薪计划表信息
     *
     * @param  deptSalaryAdjustPlanDTO 部门调薪计划表
     * @return 结果
     */
     
     @Override
     public int deleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanId(DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO)
     {
     DeptSalaryAdjustPlan deptSalaryAdjustPlan=new DeptSalaryAdjustPlan();
     BeanUtils.copyProperties(deptSalaryAdjustPlanDTO,deptSalaryAdjustPlan);
     return deptSalaryAdjustPlanMapper.deleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanId(deptSalaryAdjustPlan.getDeptSalaryAdjustPlanId());
     }
     /**
     * 物理批量删除部门调薪计划表
     *
     * @param deptSalaryAdjustPlanDtos 需要删除的部门调薪计划表主键
     * @return 结果
     */
     
     @Override
     public int deleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanIds(List<DeptSalaryAdjustPlanDTO> deptSalaryAdjustPlanDtos){
     List<Long> stringList = new ArrayList();
     for (DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO : deptSalaryAdjustPlanDtos) {
     stringList.add(deptSalaryAdjustPlanDTO.getDeptSalaryAdjustPlanId());
     }
     return deptSalaryAdjustPlanMapper.deleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanIds(stringList);
     }

    /**
    * 批量新增部门调薪计划表信息
    *
    * @param deptSalaryAdjustPlanDtos 部门调薪计划表对象
    */
    
    public int insertDeptSalaryAdjustPlans(List<DeptSalaryAdjustPlanDTO> deptSalaryAdjustPlanDtos){
      List<DeptSalaryAdjustPlan> deptSalaryAdjustPlanList = new ArrayList();

    for (DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO : deptSalaryAdjustPlanDtos) {
      DeptSalaryAdjustPlan deptSalaryAdjustPlan =new DeptSalaryAdjustPlan();
      BeanUtils.copyProperties(deptSalaryAdjustPlanDTO,deptSalaryAdjustPlan);
       deptSalaryAdjustPlan.setCreateBy(SecurityUtils.getUserId());
       deptSalaryAdjustPlan.setCreateTime(DateUtils.getNowDate());
       deptSalaryAdjustPlan.setUpdateTime(DateUtils.getNowDate());
       deptSalaryAdjustPlan.setUpdateBy(SecurityUtils.getUserId());
       deptSalaryAdjustPlan.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
      deptSalaryAdjustPlanList.add(deptSalaryAdjustPlan);
    }
    return deptSalaryAdjustPlanMapper.batchDeptSalaryAdjustPlan(deptSalaryAdjustPlanList);
    }

    /**
    * 批量修改部门调薪计划表信息
    *
    * @param deptSalaryAdjustPlanDtos 部门调薪计划表对象
    */
    
    public int updateDeptSalaryAdjustPlans(List<DeptSalaryAdjustPlanDTO> deptSalaryAdjustPlanDtos){
     List<DeptSalaryAdjustPlan> deptSalaryAdjustPlanList = new ArrayList();

     for (DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO : deptSalaryAdjustPlanDtos) {
     DeptSalaryAdjustPlan deptSalaryAdjustPlan =new DeptSalaryAdjustPlan();
     BeanUtils.copyProperties(deptSalaryAdjustPlanDTO,deptSalaryAdjustPlan);
        deptSalaryAdjustPlan.setCreateBy(SecurityUtils.getUserId());
        deptSalaryAdjustPlan.setCreateTime(DateUtils.getNowDate());
        deptSalaryAdjustPlan.setUpdateTime(DateUtils.getNowDate());
        deptSalaryAdjustPlan.setUpdateBy(SecurityUtils.getUserId());
     deptSalaryAdjustPlanList.add(deptSalaryAdjustPlan);
     }
     return deptSalaryAdjustPlanMapper.updateDeptSalaryAdjustPlans(deptSalaryAdjustPlanList);
    }
    /**
    * 导入Excel
    * @param list
    */
    @Override
    public void importDeptSalaryAdjustPlan(List<DeptSalaryAdjustPlanExcel> list) {
    List<DeptSalaryAdjustPlan> deptSalaryAdjustPlanList = new ArrayList<>();
        list.forEach(l -> {
        DeptSalaryAdjustPlan deptSalaryAdjustPlan = new DeptSalaryAdjustPlan();
        BeanUtils.copyProperties(l, deptSalaryAdjustPlan);
        deptSalaryAdjustPlan.setCreateBy(SecurityUtils.getUserId());
        deptSalaryAdjustPlan.setCreateTime(DateUtils.getNowDate());
        deptSalaryAdjustPlan.setUpdateTime(DateUtils.getNowDate());
        deptSalaryAdjustPlan.setUpdateBy(SecurityUtils.getUserId());
        deptSalaryAdjustPlan.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        deptSalaryAdjustPlanList.add(deptSalaryAdjustPlan);
        });
        try {
        deptSalaryAdjustPlanMapper.batchDeptSalaryAdjustPlan(deptSalaryAdjustPlanList);
        } catch (Exception e) {
        throw new ServiceException("导入部门调薪计划表失败");
        }
    }
    /**
    * 导出Excel
    * @param deptSalaryAdjustPlanDTO
    * @return
    */
    @Override
    public List<DeptSalaryAdjustPlanExcel> exportDeptSalaryAdjustPlan(DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO) {
        DeptSalaryAdjustPlan deptSalaryAdjustPlan = new DeptSalaryAdjustPlan();
        BeanUtils.copyProperties(deptSalaryAdjustPlanDTO, deptSalaryAdjustPlan);
        List<DeptSalaryAdjustPlanDTO> deptSalaryAdjustPlanDTOList = deptSalaryAdjustPlanMapper.selectDeptSalaryAdjustPlanList(deptSalaryAdjustPlan);
        List<DeptSalaryAdjustPlanExcel> deptSalaryAdjustPlanExcelList = new ArrayList<>();
        return deptSalaryAdjustPlanExcelList;
    }
}

