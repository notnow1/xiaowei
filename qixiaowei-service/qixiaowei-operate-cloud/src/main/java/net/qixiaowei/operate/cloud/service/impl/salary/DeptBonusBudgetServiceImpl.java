package net.qixiaowei.operate.cloud.service.impl.salary;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.salary.DeptBonusBudget;
import net.qixiaowei.operate.cloud.api.dto.salary.DeptBonusBudgetDTO;
import net.qixiaowei.operate.cloud.mapper.salary.DeptBonusBudgetMapper;
import net.qixiaowei.operate.cloud.service.salary.IDeptBonusBudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
* DeptBonusBudgetService业务层处理
* @author TANGMICHI
* @since 2022-11-29
*/
@Service
public class DeptBonusBudgetServiceImpl implements IDeptBonusBudgetService{
    @Autowired
    private DeptBonusBudgetMapper deptBonusBudgetMapper;

    /**
    * 查询部门奖金包预算表
    *
    * @param deptBonusBudgetId 部门奖金包预算表主键
    * @return 部门奖金包预算表
    */
    @Override
    public DeptBonusBudgetDTO selectDeptBonusBudgetByDeptBonusBudgetId(Long deptBonusBudgetId)
    {
    return deptBonusBudgetMapper.selectDeptBonusBudgetByDeptBonusBudgetId(deptBonusBudgetId);
    }

    /**
    * 查询部门奖金包预算表列表
    *
    * @param deptBonusBudgetDTO 部门奖金包预算表
    * @return 部门奖金包预算表
    */
    @Override
    public List<DeptBonusBudgetDTO> selectDeptBonusBudgetList(DeptBonusBudgetDTO deptBonusBudgetDTO)
    {
    DeptBonusBudget deptBonusBudget=new DeptBonusBudget();
    BeanUtils.copyProperties(deptBonusBudgetDTO,deptBonusBudget);
    return deptBonusBudgetMapper.selectDeptBonusBudgetList(deptBonusBudget);
    }

    /**
    * 新增部门奖金包预算表
    *
    * @param deptBonusBudgetDTO 部门奖金包预算表
    * @return 结果
    */
    @Override
    public DeptBonusBudgetDTO insertDeptBonusBudget(DeptBonusBudgetDTO deptBonusBudgetDTO){
    DeptBonusBudget deptBonusBudget=new DeptBonusBudget();
    BeanUtils.copyProperties(deptBonusBudgetDTO,deptBonusBudget);
    deptBonusBudget.setCreateBy(SecurityUtils.getUserId());
    deptBonusBudget.setCreateTime(DateUtils.getNowDate());
    deptBonusBudget.setUpdateTime(DateUtils.getNowDate());
    deptBonusBudget.setUpdateBy(SecurityUtils.getUserId());
    deptBonusBudget.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
    deptBonusBudgetMapper.insertDeptBonusBudget(deptBonusBudget);
    deptBonusBudgetDTO.setDeptBonusBudgetId(deptBonusBudget.getDeptBonusBudgetId());
    return deptBonusBudgetDTO;
    }

    /**
    * 修改部门奖金包预算表
    *
    * @param deptBonusBudgetDTO 部门奖金包预算表
    * @return 结果
    */
    @Override
    public int updateDeptBonusBudget(DeptBonusBudgetDTO deptBonusBudgetDTO)
    {
    DeptBonusBudget deptBonusBudget=new DeptBonusBudget();
    BeanUtils.copyProperties(deptBonusBudgetDTO,deptBonusBudget);
    deptBonusBudget.setUpdateTime(DateUtils.getNowDate());
    deptBonusBudget.setUpdateBy(SecurityUtils.getUserId());
    return deptBonusBudgetMapper.updateDeptBonusBudget(deptBonusBudget);
    }

    /**
    * 逻辑批量删除部门奖金包预算表
    *
    * @param deptBonusBudgetIds 主键集合
    * @return 结果
    */
    @Override
    public int logicDeleteDeptBonusBudgetByDeptBonusBudgetIds(List<Long> deptBonusBudgetIds){
    return deptBonusBudgetMapper.logicDeleteDeptBonusBudgetByDeptBonusBudgetIds(deptBonusBudgetIds,SecurityUtils.getUserId(),DateUtils.getNowDate());
    }

    /**
    * 物理删除部门奖金包预算表信息
    *
    * @param deptBonusBudgetId 部门奖金包预算表主键
    * @return 结果
    */
    @Override
    public int deleteDeptBonusBudgetByDeptBonusBudgetId(Long deptBonusBudgetId)
    {
    return deptBonusBudgetMapper.deleteDeptBonusBudgetByDeptBonusBudgetId(deptBonusBudgetId);
    }

     /**
     * 逻辑删除部门奖金包预算表信息
     *
     * @param  deptBonusBudgetDTO 部门奖金包预算表
     * @return 结果
     */
     @Override
     public int logicDeleteDeptBonusBudgetByDeptBonusBudgetId(DeptBonusBudgetDTO deptBonusBudgetDTO)
     {
     DeptBonusBudget deptBonusBudget=new DeptBonusBudget();
     deptBonusBudget.setDeptBonusBudgetId(deptBonusBudgetDTO.getDeptBonusBudgetId());
     deptBonusBudget.setUpdateTime(DateUtils.getNowDate());
     deptBonusBudget.setUpdateBy(SecurityUtils.getUserId());
     return deptBonusBudgetMapper.logicDeleteDeptBonusBudgetByDeptBonusBudgetId(deptBonusBudget);
     }

     /**
     * 物理删除部门奖金包预算表信息
     *
     * @param  deptBonusBudgetDTO 部门奖金包预算表
     * @return 结果
     */
     
     @Override
     public int deleteDeptBonusBudgetByDeptBonusBudgetId(DeptBonusBudgetDTO deptBonusBudgetDTO)
     {
     DeptBonusBudget deptBonusBudget=new DeptBonusBudget();
     BeanUtils.copyProperties(deptBonusBudgetDTO,deptBonusBudget);
     return deptBonusBudgetMapper.deleteDeptBonusBudgetByDeptBonusBudgetId(deptBonusBudget.getDeptBonusBudgetId());
     }
     /**
     * 物理批量删除部门奖金包预算表
     *
     * @param deptBonusBudgetDtos 需要删除的部门奖金包预算表主键
     * @return 结果
     */
     
     @Override
     public int deleteDeptBonusBudgetByDeptBonusBudgetIds(List<DeptBonusBudgetDTO> deptBonusBudgetDtos){
     List<Long> stringList = new ArrayList();
     for (DeptBonusBudgetDTO deptBonusBudgetDTO : deptBonusBudgetDtos) {
     stringList.add(deptBonusBudgetDTO.getDeptBonusBudgetId());
     }
     return deptBonusBudgetMapper.deleteDeptBonusBudgetByDeptBonusBudgetIds(stringList);
     }

    /**
    * 批量新增部门奖金包预算表信息
    *
    * @param deptBonusBudgetDtos 部门奖金包预算表对象
    */
    
    public int insertDeptBonusBudgets(List<DeptBonusBudgetDTO> deptBonusBudgetDtos){
      List<DeptBonusBudget> deptBonusBudgetList = new ArrayList();

    for (DeptBonusBudgetDTO deptBonusBudgetDTO : deptBonusBudgetDtos) {
      DeptBonusBudget deptBonusBudget =new DeptBonusBudget();
      BeanUtils.copyProperties(deptBonusBudgetDTO,deptBonusBudget);
       deptBonusBudget.setCreateBy(SecurityUtils.getUserId());
       deptBonusBudget.setCreateTime(DateUtils.getNowDate());
       deptBonusBudget.setUpdateTime(DateUtils.getNowDate());
       deptBonusBudget.setUpdateBy(SecurityUtils.getUserId());
       deptBonusBudget.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
      deptBonusBudgetList.add(deptBonusBudget);
    }
    return deptBonusBudgetMapper.batchDeptBonusBudget(deptBonusBudgetList);
    }

    /**
    * 批量修改部门奖金包预算表信息
    *
    * @param deptBonusBudgetDtos 部门奖金包预算表对象
    */
    
    public int updateDeptBonusBudgets(List<DeptBonusBudgetDTO> deptBonusBudgetDtos){
     List<DeptBonusBudget> deptBonusBudgetList = new ArrayList();

     for (DeptBonusBudgetDTO deptBonusBudgetDTO : deptBonusBudgetDtos) {
     DeptBonusBudget deptBonusBudget =new DeptBonusBudget();
     BeanUtils.copyProperties(deptBonusBudgetDTO,deptBonusBudget);
        deptBonusBudget.setCreateBy(SecurityUtils.getUserId());
        deptBonusBudget.setCreateTime(DateUtils.getNowDate());
        deptBonusBudget.setUpdateTime(DateUtils.getNowDate());
        deptBonusBudget.setUpdateBy(SecurityUtils.getUserId());
     deptBonusBudgetList.add(deptBonusBudget);
     }
     return deptBonusBudgetMapper.updateDeptBonusBudgets(deptBonusBudgetList);
    }
}

