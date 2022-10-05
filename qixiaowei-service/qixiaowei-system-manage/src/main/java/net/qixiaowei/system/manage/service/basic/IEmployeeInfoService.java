package net.qixiaowei.system.manage.service.basic;

import java.util.List;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeInfoDTO;


/**
* EmployeeInfoService接口
* @author TANGMICHI
* @since 2022-09-30
*/
public interface IEmployeeInfoService{
    /**
    * 查询员工信息
    *
    * @param employeeInfoId 员工信息主键
    * @return 员工信息
    */
    EmployeeInfoDTO selectEmployeeInfoByEmployeeInfoId(Long employeeInfoId);

    /**
    * 查询员工信息列表
    *
    * @param employeeInfoDTO 员工信息
    * @return 员工信息集合
    */
    List<EmployeeInfoDTO> selectEmployeeInfoList(EmployeeInfoDTO employeeInfoDTO);

    /**
    * 新增员工信息
    *
    * @param employeeInfoDTO 员工信息
    * @return 结果
    */
    int insertEmployeeInfo(EmployeeInfoDTO employeeInfoDTO);

    /**
    * 修改员工信息
    *
    * @param employeeInfoDTO 员工信息
    * @return 结果
    */
    int updateEmployeeInfo(EmployeeInfoDTO employeeInfoDTO);

    /**
    * 批量修改员工信息
    *
    * @param employeeInfoDtos 员工信息
    * @return 结果
    */
    int updateEmployeeInfos(List<EmployeeInfoDTO> employeeInfoDtos);

    /**
    * 批量新增员工信息
    *
    * @param employeeInfoDtos 员工信息
    * @return 结果
    */
    int insertEmployeeInfos(List<EmployeeInfoDTO> employeeInfoDtos);

    /**
    * 逻辑批量删除员工信息
    *
    * @param EmployeeInfoDtos 需要删除的员工信息集合
    * @return 结果
    */
    int logicDeleteEmployeeInfoByEmployeeInfoIds(List<EmployeeInfoDTO> EmployeeInfoDtos);

    /**
    * 逻辑删除员工信息信息
    *
    * @param employeeInfoDTO
    * @return 结果
    */
    int logicDeleteEmployeeInfoByEmployeeInfoId(EmployeeInfoDTO employeeInfoDTO);
    /**
    * 逻辑批量删除员工信息
    *
    * @param EmployeeInfoDtos 需要删除的员工信息集合
    * @return 结果
    */
    int deleteEmployeeInfoByEmployeeInfoIds(List<EmployeeInfoDTO> EmployeeInfoDtos);

    /**
    * 逻辑删除员工信息信息
    *
    * @param employeeInfoDTO
    * @return 结果
    */
    int deleteEmployeeInfoByEmployeeInfoId(EmployeeInfoDTO employeeInfoDTO);


    /**
    * 删除员工信息信息
    *
    * @param employeeInfoId 员工信息主键
    * @return 结果
    */
    int deleteEmployeeInfoByEmployeeInfoId(Long employeeInfoId);
}
