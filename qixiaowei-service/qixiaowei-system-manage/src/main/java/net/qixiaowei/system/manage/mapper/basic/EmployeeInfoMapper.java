package net.qixiaowei.system.manage.mapper.basic;

import java.util.List;
import net.qixiaowei.system.manage.api.domain.basic.EmployeeInfo;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeInfoDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* EmployeeInfoMapper接口
* @author TANGMICHI
* @since 2022-09-30
*/
public interface EmployeeInfoMapper{
    /**
    * 查询员工信息
    *
    * @param employeeInfoId 员工信息主键
    * @return 员工信息
    */
    EmployeeInfoDTO selectEmployeeInfoByEmployeeInfoId(@Param("employeeInfoId")Long employeeInfoId);

    /**
    * 查询员工信息列表
    *
    * @param employeeInfo 员工信息
    * @return 员工信息集合
    */
    List<EmployeeInfoDTO> selectEmployeeInfoList(@Param("employeeInfo")EmployeeInfo employeeInfo);

    /**
    * 新增员工信息
    *
    * @param employeeInfo 员工信息
    * @return 结果
    */
    int insertEmployeeInfo(@Param("employeeInfo")EmployeeInfo employeeInfo);

    /**
    * 修改员工信息
    *
    * @param employeeInfo 员工信息
    * @return 结果
    */
    int updateEmployeeInfo(@Param("employeeInfo")EmployeeInfo employeeInfo);

    /**
    * 批量修改员工信息
    *
    * @param employeeInfoList 员工信息
    * @return 结果
    */
    int updateEmployeeInfos(@Param("employeeInfoList")List<EmployeeInfo> employeeInfoList);
    /**
    * 逻辑删除员工信息
    *
    * @param employeeInfo
    * @return 结果
    */
    int logicDeleteEmployeeInfoByEmployeeInfoId(@Param("employeeInfo")EmployeeInfo employeeInfo,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);

    /**
    * 逻辑批量删除员工信息
    *
    * @param employeeInfoIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteEmployeeInfoByEmployeeInfoIds(@Param("employeeInfoIds")List<Long> employeeInfoIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除员工信息
    *
    * @param employeeInfoId 员工信息主键
    * @return 结果
    */
    int deleteEmployeeInfoByEmployeeInfoId(@Param("employeeInfoId")Long employeeInfoId);

    /**
    * 物理批量删除员工信息
    *
    * @param employeeInfoIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteEmployeeInfoByEmployeeInfoIds(@Param("employeeInfoIds")List<Long> employeeInfoIds);

    /**
    * 批量新增员工信息
    *
    * @param EmployeeInfos 员工信息列表
    * @return 结果
    */
    int batchEmployeeInfo(@Param("employeeInfos")List<EmployeeInfo> EmployeeInfos);
}
