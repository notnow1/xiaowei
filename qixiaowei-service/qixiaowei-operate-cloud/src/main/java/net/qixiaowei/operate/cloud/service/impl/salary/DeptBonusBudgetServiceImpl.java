package net.qixiaowei.operate.cloud.service.impl.salary;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.salary.DeptBonusBudget;
import net.qixiaowei.operate.cloud.api.dto.employee.EmployeeBudgetDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.DeptBonusBudgetDTO;
import net.qixiaowei.operate.cloud.mapper.ExampleMapper;
import net.qixiaowei.operate.cloud.mapper.employee.EmployeeBudgetMapper;
import net.qixiaowei.operate.cloud.mapper.salary.DeptBonusBudgetMapper;
import net.qixiaowei.operate.cloud.service.salary.IDeptBonusBudgetService;
import net.qixiaowei.system.manage.api.domain.basic.OfficialRankDecompose;
import net.qixiaowei.system.manage.api.dto.basic.OfficialRankSystemDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteOfficialRankSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
* DeptBonusBudgetService业务层处理
* @author TANGMICHI
* @since 2022-11-29
*/
@Service
public class DeptBonusBudgetServiceImpl implements IDeptBonusBudgetService{
    @Autowired
    private DeptBonusBudgetMapper deptBonusBudgetMapper;
    @Autowired
    private RemoteOfficialRankSystemService remoteOfficialRankSystemService;
    @Autowired
    private EmployeeBudgetMapper employeeBudgetMapper;

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
     * 新增部门奖金包预算预制数据
     * 1查询所有职级
     * 2根据职级id查询人力预算数据
     * 3计算不存在 在人力预算数据里面的职级体系
     * @param budgetYear
     * @return
     */
    @Override
    public DeptBonusBudgetDTO addDeptBonusBudgetTamount(int budgetYear) {
        //远程查询所有职级
        R<List<OfficialRankSystemDTO>> listR = remoteOfficialRankSystemService.selectByIds(new ArrayList<>(), SecurityConstants.INNER);
        List<OfficialRankSystemDTO> data = listR.getData();
        if (StringUtils.isEmpty(data)){
            throw new ServiceException("无职级数据 请联系管理员！");
        }
        List<Long> collect = data.stream().map(OfficialRankSystemDTO::getOfficialRankSystemId).collect(Collectors.toList());

        List<EmployeeBudgetDTO> employeeBudgetDTOList = employeeBudgetMapper.selectEmployeeBudgetByOfficialRankSystemIds(collect);
        if (StringUtils.isNotEmpty(employeeBudgetDTOList)){
            List<Long> collect1 = employeeBudgetDTOList.stream().map(EmployeeBudgetDTO::getOfficialRankSystemId).collect(Collectors.toList());

        }
        return null;
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

