package net.qixiaowei.operate.cloud.service.impl.salary;

import java.util.List;
import net.qixiaowei.integration.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.salary.EmployeeBudget;
import net.qixiaowei.operate.cloud.excel.salary.EmployeeBudgetExcel;
import net.qixiaowei.operate.cloud.api.dto.salary.EmployeeBudgetDTO;
import net.qixiaowei.operate.cloud.mapper.salary.EmployeeBudgetMapper;
import net.qixiaowei.operate.cloud.service.salary.IEmployeeBudgetService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;


/**
* EmployeeBudgetService业务层处理
* @author TANGMICHI
* @since 2022-11-18
*/
@Service
public class EmployeeBudgetServiceImpl implements IEmployeeBudgetService{
    @Autowired
    private EmployeeBudgetMapper employeeBudgetMapper;

    /**
    * 查询人力预算表
    *
    * @param employeeBudgetId 人力预算表主键
    * @return 人力预算表
    */
    @Override
    public EmployeeBudgetDTO selectEmployeeBudgetByEmployeeBudgetId(Long employeeBudgetId)
    {
    return employeeBudgetMapper.selectEmployeeBudgetByEmployeeBudgetId(employeeBudgetId);
    }

    /**
    * 查询人力预算表列表
    *
    * @param employeeBudgetDTO 人力预算表
    * @return 人力预算表
    */
    @Override
    public List<EmployeeBudgetDTO> selectEmployeeBudgetList(EmployeeBudgetDTO employeeBudgetDTO)
    {
    EmployeeBudget employeeBudget=new EmployeeBudget();
    BeanUtils.copyProperties(employeeBudgetDTO,employeeBudget);
    return employeeBudgetMapper.selectEmployeeBudgetList(employeeBudget);
    }

    /**
    * 新增人力预算表
    *
    * @param employeeBudgetDTO 人力预算表
    * @return 结果
    */
    @Override
    public EmployeeBudgetDTO insertEmployeeBudget(EmployeeBudgetDTO employeeBudgetDTO){
    EmployeeBudget employeeBudget=new EmployeeBudget();
    BeanUtils.copyProperties(employeeBudgetDTO,employeeBudget);
    employeeBudget.setCreateBy(SecurityUtils.getUserId());
    employeeBudget.setCreateTime(DateUtils.getNowDate());
    employeeBudget.setUpdateTime(DateUtils.getNowDate());
    employeeBudget.setUpdateBy(SecurityUtils.getUserId());
    employeeBudget.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
    employeeBudgetMapper.insertEmployeeBudget(employeeBudget);
    employeeBudgetDTO.setEmployeeBudgetId(employeeBudget.getEmployeeBudgetId());
    return employeeBudgetDTO;
    }

    /**
    * 修改人力预算表
    *
    * @param employeeBudgetDTO 人力预算表
    * @return 结果
    */
    @Override
    public int updateEmployeeBudget(EmployeeBudgetDTO employeeBudgetDTO)
    {
    EmployeeBudget employeeBudget=new EmployeeBudget();
    BeanUtils.copyProperties(employeeBudgetDTO,employeeBudget);
    employeeBudget.setUpdateTime(DateUtils.getNowDate());
    employeeBudget.setUpdateBy(SecurityUtils.getUserId());
    return employeeBudgetMapper.updateEmployeeBudget(employeeBudget);
    }

    /**
    * 逻辑批量删除人力预算表
    *
    * @param employeeBudgetIds 主键集合
    * @return 结果
    */
    @Override
    public int logicDeleteEmployeeBudgetByEmployeeBudgetIds(List<Long> employeeBudgetIds){
    return employeeBudgetMapper.logicDeleteEmployeeBudgetByEmployeeBudgetIds(employeeBudgetIds,SecurityUtils.getUserId(),DateUtils.getNowDate());
    }

    /**
    * 物理删除人力预算表信息
    *
    * @param employeeBudgetId 人力预算表主键
    * @return 结果
    */
    @Override
    public int deleteEmployeeBudgetByEmployeeBudgetId(Long employeeBudgetId)
    {
    return employeeBudgetMapper.deleteEmployeeBudgetByEmployeeBudgetId(employeeBudgetId);
    }

     /**
     * 逻辑删除人力预算表信息
     *
     * @param  employeeBudgetDTO 人力预算表
     * @return 结果
     */
     @Override
     public int logicDeleteEmployeeBudgetByEmployeeBudgetId(EmployeeBudgetDTO employeeBudgetDTO)
     {
     EmployeeBudget employeeBudget=new EmployeeBudget();
     employeeBudget.setEmployeeBudgetId(employeeBudgetDTO.getEmployeeBudgetId());
     employeeBudget.setUpdateTime(DateUtils.getNowDate());
     employeeBudget.setUpdateBy(SecurityUtils.getUserId());
     return employeeBudgetMapper.logicDeleteEmployeeBudgetByEmployeeBudgetId(employeeBudget);
     }

     /**
     * 物理删除人力预算表信息
     *
     * @param  employeeBudgetDTO 人力预算表
     * @return 结果
     */
     
     @Override
     public int deleteEmployeeBudgetByEmployeeBudgetId(EmployeeBudgetDTO employeeBudgetDTO)
     {
     EmployeeBudget employeeBudget=new EmployeeBudget();
     BeanUtils.copyProperties(employeeBudgetDTO,employeeBudget);
     return employeeBudgetMapper.deleteEmployeeBudgetByEmployeeBudgetId(employeeBudget.getEmployeeBudgetId());
     }
     /**
     * 物理批量删除人力预算表
     *
     * @param employeeBudgetDtos 需要删除的人力预算表主键
     * @return 结果
     */
     
     @Override
     public int deleteEmployeeBudgetByEmployeeBudgetIds(List<EmployeeBudgetDTO> employeeBudgetDtos){
     List<Long> stringList = new ArrayList();
     for (EmployeeBudgetDTO employeeBudgetDTO : employeeBudgetDtos) {
     stringList.add(employeeBudgetDTO.getEmployeeBudgetId());
     }
     return employeeBudgetMapper.deleteEmployeeBudgetByEmployeeBudgetIds(stringList);
     }

    /**
    * 批量新增人力预算表信息
    *
    * @param employeeBudgetDtos 人力预算表对象
    */
    
    public int insertEmployeeBudgets(List<EmployeeBudgetDTO> employeeBudgetDtos){
      List<EmployeeBudget> employeeBudgetList = new ArrayList();

    for (EmployeeBudgetDTO employeeBudgetDTO : employeeBudgetDtos) {
      EmployeeBudget employeeBudget =new EmployeeBudget();
      BeanUtils.copyProperties(employeeBudgetDTO,employeeBudget);
       employeeBudget.setCreateBy(SecurityUtils.getUserId());
       employeeBudget.setCreateTime(DateUtils.getNowDate());
       employeeBudget.setUpdateTime(DateUtils.getNowDate());
       employeeBudget.setUpdateBy(SecurityUtils.getUserId());
       employeeBudget.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
      employeeBudgetList.add(employeeBudget);
    }
    return employeeBudgetMapper.batchEmployeeBudget(employeeBudgetList);
    }

    /**
    * 批量修改人力预算表信息
    *
    * @param employeeBudgetDtos 人力预算表对象
    */
    
    public int updateEmployeeBudgets(List<EmployeeBudgetDTO> employeeBudgetDtos){
     List<EmployeeBudget> employeeBudgetList = new ArrayList();

     for (EmployeeBudgetDTO employeeBudgetDTO : employeeBudgetDtos) {
     EmployeeBudget employeeBudget =new EmployeeBudget();
     BeanUtils.copyProperties(employeeBudgetDTO,employeeBudget);
        employeeBudget.setCreateBy(SecurityUtils.getUserId());
        employeeBudget.setCreateTime(DateUtils.getNowDate());
        employeeBudget.setUpdateTime(DateUtils.getNowDate());
        employeeBudget.setUpdateBy(SecurityUtils.getUserId());
     employeeBudgetList.add(employeeBudget);
     }
     return employeeBudgetMapper.updateEmployeeBudgets(employeeBudgetList);
    }
    /**
    * 导入Excel
    * @param list
    */
    @Override
    public void importEmployeeBudget(List<EmployeeBudgetExcel> list) {
    List<EmployeeBudget> employeeBudgetList = new ArrayList<>();
        list.forEach(l -> {
        EmployeeBudget employeeBudget = new EmployeeBudget();
        BeanUtils.copyProperties(l, employeeBudget);
        employeeBudget.setCreateBy(SecurityUtils.getUserId());
        employeeBudget.setCreateTime(DateUtils.getNowDate());
        employeeBudget.setUpdateTime(DateUtils.getNowDate());
        employeeBudget.setUpdateBy(SecurityUtils.getUserId());
        employeeBudget.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        employeeBudgetList.add(employeeBudget);
        });
        try {
        employeeBudgetMapper.batchEmployeeBudget(employeeBudgetList);
        } catch (Exception e) {
        throw new ServiceException("导入人力预算表失败");
        }
    }
    /**
    * 导出Excel
    * @param employeeBudgetDTO
    * @return
    */
    @Override
    public List<EmployeeBudgetExcel> exportEmployeeBudget(EmployeeBudgetDTO employeeBudgetDTO) {
        EmployeeBudget employeeBudget = new EmployeeBudget();
        BeanUtils.copyProperties(employeeBudgetDTO, employeeBudget);
        List<EmployeeBudgetDTO> employeeBudgetDTOList = employeeBudgetMapper.selectEmployeeBudgetList(employeeBudget);
        List<EmployeeBudgetExcel> employeeBudgetExcelList = new ArrayList<>();
        return employeeBudgetExcelList;
    }
}

