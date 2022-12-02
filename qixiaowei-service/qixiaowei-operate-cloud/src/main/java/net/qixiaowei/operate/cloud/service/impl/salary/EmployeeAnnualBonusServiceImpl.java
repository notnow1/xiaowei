package net.qixiaowei.operate.cloud.service.impl.salary;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.salary.EmployeeAnnualBonus;
import net.qixiaowei.operate.cloud.api.dto.salary.EmployeeAnnualBonusDTO;
import net.qixiaowei.operate.cloud.mapper.salary.EmployeeAnnualBonusMapper;
import net.qixiaowei.operate.cloud.service.salary.IEmployeeAnnualBonusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
* EmployeeAnnualBonusService业务层处理
* @author TANGMICHI
* @since 2022-12-02
*/
@Service
public class EmployeeAnnualBonusServiceImpl implements IEmployeeAnnualBonusService{
    @Autowired
    private EmployeeAnnualBonusMapper employeeAnnualBonusMapper;

    /**
    * 查询个人年终奖表
    *
    * @param employeeAnnualBonusId 个人年终奖表主键
    * @return 个人年终奖表
    */
    @Override
    public EmployeeAnnualBonusDTO selectEmployeeAnnualBonusByEmployeeAnnualBonusId(Long employeeAnnualBonusId)
    {
    return employeeAnnualBonusMapper.selectEmployeeAnnualBonusByEmployeeAnnualBonusId(employeeAnnualBonusId);
    }

    /**
    * 查询个人年终奖表列表
    *
    * @param employeeAnnualBonusDTO 个人年终奖表
    * @return 个人年终奖表
    */
    @Override
    public List<EmployeeAnnualBonusDTO> selectEmployeeAnnualBonusList(EmployeeAnnualBonusDTO employeeAnnualBonusDTO)
    {
    EmployeeAnnualBonus employeeAnnualBonus=new EmployeeAnnualBonus();
    BeanUtils.copyProperties(employeeAnnualBonusDTO,employeeAnnualBonus);
    return employeeAnnualBonusMapper.selectEmployeeAnnualBonusList(employeeAnnualBonus);
    }

    /**
    * 新增个人年终奖表
    *
    * @param employeeAnnualBonusDTO 个人年终奖表
    * @return 结果
    */
    @Override
    public EmployeeAnnualBonusDTO insertEmployeeAnnualBonus(EmployeeAnnualBonusDTO employeeAnnualBonusDTO){
    EmployeeAnnualBonus employeeAnnualBonus=new EmployeeAnnualBonus();
    BeanUtils.copyProperties(employeeAnnualBonusDTO,employeeAnnualBonus);
    employeeAnnualBonus.setCreateBy(SecurityUtils.getUserId());
    employeeAnnualBonus.setCreateTime(DateUtils.getNowDate());
    employeeAnnualBonus.setUpdateTime(DateUtils.getNowDate());
    employeeAnnualBonus.setUpdateBy(SecurityUtils.getUserId());
    employeeAnnualBonus.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
    employeeAnnualBonusMapper.insertEmployeeAnnualBonus(employeeAnnualBonus);
    employeeAnnualBonusDTO.setEmployeeAnnualBonusId(employeeAnnualBonus.getEmployeeAnnualBonusId());
    return employeeAnnualBonusDTO;
    }

    /**
    * 修改个人年终奖表
    *
    * @param employeeAnnualBonusDTO 个人年终奖表
    * @return 结果
    */
    @Override
    public int updateEmployeeAnnualBonus(EmployeeAnnualBonusDTO employeeAnnualBonusDTO)
    {
    EmployeeAnnualBonus employeeAnnualBonus=new EmployeeAnnualBonus();
    BeanUtils.copyProperties(employeeAnnualBonusDTO,employeeAnnualBonus);
    employeeAnnualBonus.setUpdateTime(DateUtils.getNowDate());
    employeeAnnualBonus.setUpdateBy(SecurityUtils.getUserId());
    return employeeAnnualBonusMapper.updateEmployeeAnnualBonus(employeeAnnualBonus);
    }

    /**
    * 逻辑批量删除个人年终奖表
    *
    * @param employeeAnnualBonusIds 主键集合
    * @return 结果
    */
    @Override
    public int logicDeleteEmployeeAnnualBonusByEmployeeAnnualBonusIds(List<Long> employeeAnnualBonusIds){
    return employeeAnnualBonusMapper.logicDeleteEmployeeAnnualBonusByEmployeeAnnualBonusIds(employeeAnnualBonusIds,SecurityUtils.getUserId(),DateUtils.getNowDate());
    }

    /**
    * 物理删除个人年终奖表信息
    *
    * @param employeeAnnualBonusId 个人年终奖表主键
    * @return 结果
    */
    @Override
    public int deleteEmployeeAnnualBonusByEmployeeAnnualBonusId(Long employeeAnnualBonusId)
    {
    return employeeAnnualBonusMapper.deleteEmployeeAnnualBonusByEmployeeAnnualBonusId(employeeAnnualBonusId);
    }

     /**
     * 逻辑删除个人年终奖表信息
     *
     * @param  employeeAnnualBonusDTO 个人年终奖表
     * @return 结果
     */
     @Override
     public int logicDeleteEmployeeAnnualBonusByEmployeeAnnualBonusId(EmployeeAnnualBonusDTO employeeAnnualBonusDTO)
     {
     EmployeeAnnualBonus employeeAnnualBonus=new EmployeeAnnualBonus();
     employeeAnnualBonus.setEmployeeAnnualBonusId(employeeAnnualBonusDTO.getEmployeeAnnualBonusId());
     employeeAnnualBonus.setUpdateTime(DateUtils.getNowDate());
     employeeAnnualBonus.setUpdateBy(SecurityUtils.getUserId());
     return employeeAnnualBonusMapper.logicDeleteEmployeeAnnualBonusByEmployeeAnnualBonusId(employeeAnnualBonus);
     }

     /**
     * 物理删除个人年终奖表信息
     *
     * @param  employeeAnnualBonusDTO 个人年终奖表
     * @return 结果
     */
     
     @Override
     public int deleteEmployeeAnnualBonusByEmployeeAnnualBonusId(EmployeeAnnualBonusDTO employeeAnnualBonusDTO)
     {
     EmployeeAnnualBonus employeeAnnualBonus=new EmployeeAnnualBonus();
     BeanUtils.copyProperties(employeeAnnualBonusDTO,employeeAnnualBonus);
     return employeeAnnualBonusMapper.deleteEmployeeAnnualBonusByEmployeeAnnualBonusId(employeeAnnualBonus.getEmployeeAnnualBonusId());
     }
     /**
     * 物理批量删除个人年终奖表
     *
     * @param employeeAnnualBonusDtos 需要删除的个人年终奖表主键
     * @return 结果
     */
     
     @Override
     public int deleteEmployeeAnnualBonusByEmployeeAnnualBonusIds(List<EmployeeAnnualBonusDTO> employeeAnnualBonusDtos){
     List<Long> stringList = new ArrayList();
     for (EmployeeAnnualBonusDTO employeeAnnualBonusDTO : employeeAnnualBonusDtos) {
     stringList.add(employeeAnnualBonusDTO.getEmployeeAnnualBonusId());
     }
     return employeeAnnualBonusMapper.deleteEmployeeAnnualBonusByEmployeeAnnualBonusIds(stringList);
     }

    /**
    * 批量新增个人年终奖表信息
    *
    * @param employeeAnnualBonusDtos 个人年终奖表对象
    */
    
    public int insertEmployeeAnnualBonuss(List<EmployeeAnnualBonusDTO> employeeAnnualBonusDtos){
      List<EmployeeAnnualBonus> employeeAnnualBonusList = new ArrayList();

    for (EmployeeAnnualBonusDTO employeeAnnualBonusDTO : employeeAnnualBonusDtos) {
      EmployeeAnnualBonus employeeAnnualBonus =new EmployeeAnnualBonus();
      BeanUtils.copyProperties(employeeAnnualBonusDTO,employeeAnnualBonus);
       employeeAnnualBonus.setCreateBy(SecurityUtils.getUserId());
       employeeAnnualBonus.setCreateTime(DateUtils.getNowDate());
       employeeAnnualBonus.setUpdateTime(DateUtils.getNowDate());
       employeeAnnualBonus.setUpdateBy(SecurityUtils.getUserId());
       employeeAnnualBonus.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
      employeeAnnualBonusList.add(employeeAnnualBonus);
    }
    return employeeAnnualBonusMapper.batchEmployeeAnnualBonus(employeeAnnualBonusList);
    }

    /**
    * 批量修改个人年终奖表信息
    *
    * @param employeeAnnualBonusDtos 个人年终奖表对象
    */
    
    public int updateEmployeeAnnualBonuss(List<EmployeeAnnualBonusDTO> employeeAnnualBonusDtos){
     List<EmployeeAnnualBonus> employeeAnnualBonusList = new ArrayList();

     for (EmployeeAnnualBonusDTO employeeAnnualBonusDTO : employeeAnnualBonusDtos) {
     EmployeeAnnualBonus employeeAnnualBonus =new EmployeeAnnualBonus();
     BeanUtils.copyProperties(employeeAnnualBonusDTO,employeeAnnualBonus);
        employeeAnnualBonus.setCreateBy(SecurityUtils.getUserId());
        employeeAnnualBonus.setCreateTime(DateUtils.getNowDate());
        employeeAnnualBonus.setUpdateTime(DateUtils.getNowDate());
        employeeAnnualBonus.setUpdateBy(SecurityUtils.getUserId());
     employeeAnnualBonusList.add(employeeAnnualBonus);
     }
     return employeeAnnualBonusMapper.updateEmployeeAnnualBonuss(employeeAnnualBonusList);
    }
}

