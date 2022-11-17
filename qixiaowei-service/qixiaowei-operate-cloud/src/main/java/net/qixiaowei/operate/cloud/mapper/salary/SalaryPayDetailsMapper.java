package net.qixiaowei.operate.cloud.mapper.salary;

import java.util.List;
import net.qixiaowei.operate.cloud.api.domain.salary.SalaryPayDetails;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryPayDetailsDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* SalaryPayDetailsMapper接口
* @author Graves
* @since 2022-11-17
*/
public interface SalaryPayDetailsMapper{
    /**
    * 查询工资发薪明细表
    *
    * @param salaryPayDetailsId 工资发薪明细表主键
    * @return 工资发薪明细表
    */
    SalaryPayDetailsDTO selectSalaryPayDetailsBySalaryPayDetailsId(@Param("salaryPayDetailsId")Long salaryPayDetailsId);


    /**
    * 批量查询工资发薪明细表
    *
    * @param salaryPayDetailsIds 工资发薪明细表主键集合
    * @return 工资发薪明细表
    */
    List<SalaryPayDetailsDTO> selectSalaryPayDetailsBySalaryPayDetailsIds(@Param("salaryPayDetailsIds") List<Long> salaryPayDetailsIds);

    /**
    * 查询工资发薪明细表列表
    *
    * @param salaryPayDetails 工资发薪明细表
    * @return 工资发薪明细表集合
    */
    List<SalaryPayDetailsDTO> selectSalaryPayDetailsList(@Param("salaryPayDetails")SalaryPayDetails salaryPayDetails);

    /**
    * 新增工资发薪明细表
    *
    * @param salaryPayDetails 工资发薪明细表
    * @return 结果
    */
    int insertSalaryPayDetails(@Param("salaryPayDetails")SalaryPayDetails salaryPayDetails);

    /**
    * 修改工资发薪明细表
    *
    * @param salaryPayDetails 工资发薪明细表
    * @return 结果
    */
    int updateSalaryPayDetails(@Param("salaryPayDetails")SalaryPayDetails salaryPayDetails);

    /**
    * 批量修改工资发薪明细表
    *
    * @param salaryPayDetailsList 工资发薪明细表
    * @return 结果
    */
    int updateSalaryPayDetailss(@Param("salaryPayDetailsList")List<SalaryPayDetails> salaryPayDetailsList);
    /**
    * 逻辑删除工资发薪明细表
    *
    * @param salaryPayDetails
    * @return 结果
    */
    int logicDeleteSalaryPayDetailsBySalaryPayDetailsId(@Param("salaryPayDetails")SalaryPayDetails salaryPayDetails);

    /**
    * 逻辑批量删除工资发薪明细表
    *
    * @param salaryPayDetailsIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteSalaryPayDetailsBySalaryPayDetailsIds(@Param("salaryPayDetailsIds")List<Long> salaryPayDetailsIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除工资发薪明细表
    *
    * @param salaryPayDetailsId 工资发薪明细表主键
    * @return 结果
    */
    int deleteSalaryPayDetailsBySalaryPayDetailsId(@Param("salaryPayDetailsId")Long salaryPayDetailsId);

    /**
    * 物理批量删除工资发薪明细表
    *
    * @param salaryPayDetailsIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteSalaryPayDetailsBySalaryPayDetailsIds(@Param("salaryPayDetailsIds")List<Long> salaryPayDetailsIds);

    /**
    * 批量新增工资发薪明细表
    *
    * @param SalaryPayDetailss 工资发薪明细表列表
    * @return 结果
    */
    int batchSalaryPayDetails(@Param("salaryPayDetailss")List<SalaryPayDetails> SalaryPayDetailss);
}
