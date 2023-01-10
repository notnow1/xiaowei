package net.qixiaowei.operate.cloud.mapper.salary;

import net.qixiaowei.operate.cloud.api.domain.salary.SalaryPayDetails;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryPayDetailsDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


/**
 * SalaryPayDetailsMapper接口
 *
 * @author Graves
 * @since 2022-11-17
 */
public interface SalaryPayDetailsMapper {
    /**
     * 查询工资发薪明细表
     *
     * @param salaryPayDetailsId 工资发薪明细表主键
     * @return 工资发薪明细表
     */
    SalaryPayDetailsDTO selectSalaryPayDetailsBySalaryPayDetailsId(@Param("salaryPayDetailsId") Long salaryPayDetailsId);


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
    List<SalaryPayDetailsDTO> selectSalaryPayDetailsList(@Param("salaryPayDetails") SalaryPayDetails salaryPayDetails);

    /**
     * 新增工资发薪明细表
     *
     * @param salaryPayDetails 工资发薪明细表
     * @return 结果
     */
    int insertSalaryPayDetails(@Param("salaryPayDetails") SalaryPayDetails salaryPayDetails);

    /**
     * 修改工资发薪明细表
     *
     * @param salaryPayDetails 工资发薪明细表
     * @return 结果
     */
    int updateSalaryPayDetails(@Param("salaryPayDetails") SalaryPayDetails salaryPayDetails);

    /**
     * 批量修改工资发薪明细表
     *
     * @param salaryPayDetailsList 工资发薪明细表
     * @return 结果
     */
    int updateSalaryPayDetailss(@Param("salaryPayDetailsList") List<SalaryPayDetails> salaryPayDetailsList);

    /**
     * 逻辑删除工资发薪明细表
     *
     * @param salaryPayDetails
     * @return 结果
     */
    int logicDeleteSalaryPayDetailsBySalaryPayDetailsId(@Param("salaryPayDetails") SalaryPayDetails salaryPayDetails);

    /**
     * 逻辑批量删除工资发薪明细表
     *
     * @param salaryPayDetailsIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteSalaryPayDetailsBySalaryPayDetailsIds(@Param("salaryPayDetailsIds") List<Long> salaryPayDetailsIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除工资发薪明细表
     *
     * @param salaryPayDetailsId 工资发薪明细表主键
     * @return 结果
     */
    int deleteSalaryPayDetailsBySalaryPayDetailsId(@Param("salaryPayDetailsId") Long salaryPayDetailsId);

    /**
     * 物理批量删除工资发薪明细表
     *
     * @param salaryPayDetailsIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteSalaryPayDetailsBySalaryPayDetailsIds(@Param("salaryPayDetailsIds") List<Long> salaryPayDetailsIds);

    /**
     * 批量新增工资发薪明细表
     *
     * @param SalaryPayDetailss 工资发薪明细表列表
     * @return 结果
     */
    int batchSalaryPayDetails(@Param("salaryPayDetailss") List<SalaryPayDetails> SalaryPayDetailss);

    /**
     * 通过工资ID返回详情
     *
     * @param salaryPayId
     * @return
     */
    List<SalaryPayDetailsDTO> selectSalaryPayDetailsBySalaryPayId(@Param("salaryPayId") Long salaryPayId);

    /**
     * 通过工资ID删除列表
     *
     * @param salaryPayId
     * @param updateBy
     * @param updateTime
     * @return
     */
    int logicDeleteSalaryPayDetailsBySalaryPayId(@Param("salaryPayId") Long salaryPayId, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);

    /**
     * 通过工资ID列表删除列表
     *
     * @param salaryPayIds
     * @param updateTime
     * @param updateBy
     * @return
     */
    int logicDeleteSalaryPayDetailsBySalaryPayIds(@Param("salaryPayIds") List<Long> salaryPayIds, @Param("updateTime") Date updateTime, @Param("updateBy") Long updateBy);

    /**
     * 通过发薪集合查找详情表
     *
     * @param salaryPayIds
     * @return
     */
    List<SalaryPayDetailsDTO> selectSalaryPayDetailsBySalaryPayIds(@Param("salaryPayIds") List<Long> salaryPayIds);

    /**
     * 根据工资项查询工资详情表
     *
     * @param salaryItemIds 工资项ID集合
     * @return List
     */
    List<SalaryPayDetailsDTO> selectSalaryPayDetailsByItemIds(@Param("salaryItemIds") List<Long> salaryItemIds);
}
