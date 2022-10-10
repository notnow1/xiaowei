package net.qixiaowei.system.manage.service.impl.basic;

import java.util.List;
import net.qixiaowei.integration.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.domain.basic.EmployeeInfo;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeInfoDTO;
import net.qixiaowei.system.manage.mapper.basic.EmployeeInfoMapper;
import net.qixiaowei.system.manage.service.basic.IEmployeeInfoService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


/**
* EmployeeInfoService业务层处理
* @author TANGMICHI
* @since 2022-10-09
*/
@Service
public class EmployeeInfoServiceImpl implements IEmployeeInfoService{
    @Autowired
    private EmployeeInfoMapper employeeInfoMapper;

    /**
    * 查询员工信息
    *
    * @param employeeInfoId 员工信息主键
    * @return 员工信息
    */
    @Override
    public EmployeeInfoDTO selectEmployeeInfoByEmployeeInfoId(Long employeeInfoId)
    {
    return employeeInfoMapper.selectEmployeeInfoByEmployeeInfoId(employeeInfoId);
    }

    /**
    * 查询员工信息列表
    *
    * @param employeeInfoDTO 员工信息
    * @return 员工信息
    */
    @Override
    public List<EmployeeInfoDTO> selectEmployeeInfoList(EmployeeInfoDTO employeeInfoDTO)
    {
    EmployeeInfo employeeInfo=new EmployeeInfo();
    BeanUtils.copyProperties(employeeInfoDTO,employeeInfo);
    return employeeInfoMapper.selectEmployeeInfoList(employeeInfo);
    }

    /**
    * 新增员工信息
    *
    * @param employeeInfoDTO 员工信息
    * @return 结果
    */
    @Override
    public int insertEmployeeInfo(EmployeeInfoDTO employeeInfoDTO){
    EmployeeInfo employeeInfo=new EmployeeInfo();
    BeanUtils.copyProperties(employeeInfoDTO,employeeInfo);
    employeeInfo.setCreateBy(SecurityUtils.getUserId());
    employeeInfo.setCreateTime(DateUtils.getNowDate());
    employeeInfo.setUpdateTime(DateUtils.getNowDate());
    employeeInfo.setUpdateBy(SecurityUtils.getUserId());
    return employeeInfoMapper.insertEmployeeInfo(employeeInfo);
    }

    /**
    * 修改员工信息
    *
    * @param employeeInfoDTO 员工信息
    * @return 结果
    */
    @Override
    public int updateEmployeeInfo(EmployeeInfoDTO employeeInfoDTO)
    {
    EmployeeInfo employeeInfo=new EmployeeInfo();
    BeanUtils.copyProperties(employeeInfoDTO,employeeInfo);
    employeeInfo.setUpdateTime(DateUtils.getNowDate());
    employeeInfo.setUpdateBy(SecurityUtils.getUserId());
    return employeeInfoMapper.updateEmployeeInfo(employeeInfo);
    }

    /**
    * 逻辑批量删除员工信息
    *
    * @param employeeInfoDtos 需要删除的员工信息主键
    * @return 结果
    */
    @Override
    public int logicDeleteEmployeeInfoByEmployeeInfoIds(List<EmployeeInfoDTO> employeeInfoDtos){
            List<Long> stringList = new ArrayList();
            for (EmployeeInfoDTO employeeInfoDTO : employeeInfoDtos) {
                stringList.add(employeeInfoDTO.getEmployeeInfoId());
            }
    return employeeInfoMapper.logicDeleteEmployeeInfoByEmployeeInfoIds(stringList,employeeInfoDtos.get(0).getUpdateBy(),DateUtils.getNowDate());
    }

    /**
    * 物理删除员工信息信息
    *
    * @param employeeInfoId 员工信息主键
    * @return 结果
    */
    @Override
    public int deleteEmployeeInfoByEmployeeInfoId(Long employeeInfoId)
    {
    return employeeInfoMapper.deleteEmployeeInfoByEmployeeInfoId(employeeInfoId);
    }

     /**
     * 逻辑删除员工信息信息
     *
     * @param  employeeInfoDTO 员工信息
     * @return 结果
     */
     @Override
     public int logicDeleteEmployeeInfoByEmployeeInfoId(EmployeeInfoDTO employeeInfoDTO)
     {
     EmployeeInfo employeeInfo=new EmployeeInfo();
     employeeInfo.setEmployeeInfoId(employeeInfoDTO.getEmployeeInfoId());
     employeeInfo.setUpdateTime(DateUtils.getNowDate());
     employeeInfo.setUpdateBy(SecurityUtils.getUserId());
     return employeeInfoMapper.logicDeleteEmployeeInfoByEmployeeInfoId(employeeInfo);
     }

     /**
     * 物理删除员工信息信息
     *
     * @param  employeeInfoDTO 员工信息
     * @return 结果
     */
     
     @Override
     public int deleteEmployeeInfoByEmployeeInfoId(EmployeeInfoDTO employeeInfoDTO)
     {
     EmployeeInfo employeeInfo=new EmployeeInfo();
     BeanUtils.copyProperties(employeeInfoDTO,employeeInfo);
     return employeeInfoMapper.deleteEmployeeInfoByEmployeeInfoId(employeeInfo.getEmployeeInfoId());
     }
     /**
     * 物理批量删除员工信息
     *
     * @param employeeInfoDtos 需要删除的员工信息主键
     * @return 结果
     */
     
     @Override
     public int deleteEmployeeInfoByEmployeeInfoIds(List<EmployeeInfoDTO> employeeInfoDtos){
     List<Long> stringList = new ArrayList();
     for (EmployeeInfoDTO employeeInfoDTO : employeeInfoDtos) {
     stringList.add(employeeInfoDTO.getEmployeeInfoId());
     }
     return employeeInfoMapper.deleteEmployeeInfoByEmployeeInfoIds(stringList);
     }

    /**
    * 批量新增员工信息信息
    *
    * @param employeeInfoDtos 员工信息对象
    */
    
    public int insertEmployeeInfos(List<EmployeeInfoDTO> employeeInfoDtos){
      List<EmployeeInfo> employeeInfoList = new ArrayList();

    for (EmployeeInfoDTO employeeInfoDTO : employeeInfoDtos) {
      EmployeeInfo employeeInfo =new EmployeeInfo();
      BeanUtils.copyProperties(employeeInfoDTO,employeeInfo);
       employeeInfo.setCreateBy(SecurityUtils.getUserId());
       employeeInfo.setCreateTime(DateUtils.getNowDate());
       employeeInfo.setUpdateTime(DateUtils.getNowDate());
       employeeInfo.setUpdateBy(SecurityUtils.getUserId());
      employeeInfoList.add(employeeInfo);
    }
    return employeeInfoMapper.batchEmployeeInfo(employeeInfoList);
    }

    /**
    * 批量修改员工信息信息
    *
    * @param employeeInfoDtos 员工信息对象
    */
    
    public int updateEmployeeInfos(List<EmployeeInfoDTO> employeeInfoDtos){
     List<EmployeeInfo> employeeInfoList = new ArrayList();

     for (EmployeeInfoDTO employeeInfoDTO : employeeInfoDtos) {
     EmployeeInfo employeeInfo =new EmployeeInfo();
     BeanUtils.copyProperties(employeeInfoDTO,employeeInfo);
        employeeInfo.setCreateBy(SecurityUtils.getUserId());
        employeeInfo.setCreateTime(DateUtils.getNowDate());
        employeeInfo.setUpdateTime(DateUtils.getNowDate());
        employeeInfo.setUpdateBy(SecurityUtils.getUserId());
     employeeInfoList.add(employeeInfo);
     }
     return employeeInfoMapper.updateEmployeeInfos(employeeInfoList);
    }
}

