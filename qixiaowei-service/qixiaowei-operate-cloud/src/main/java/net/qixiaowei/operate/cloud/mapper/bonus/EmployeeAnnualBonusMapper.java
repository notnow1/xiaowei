package net.qixiaowei.operate.cloud.mapper.bonus;

import java.util.List;
import net.qixiaowei.operate.cloud.api.domain.bonus.EmployeeAnnualBonus;
import net.qixiaowei.operate.cloud.api.dto.bonus.EmployeeAnnualBonusDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* EmployeeAnnualBonusMapper接口
* @author TANGMICHI
* @since 2022-12-05
*/
public interface EmployeeAnnualBonusMapper{
    /**
    * 查询个人年终奖表
    *
    * @param employeeAnnualBonusId 个人年终奖表主键
    * @return 个人年终奖表
    */
    EmployeeAnnualBonusDTO selectEmployeeAnnualBonusByEmployeeAnnualBonusId(@Param("employeeAnnualBonusId")Long employeeAnnualBonusId);

    /**
     * 根据个人年终奖表年份查询个人年终奖表
     *
     * @param annualBonusYear 个人年终奖表年份
     * @return 个人年终奖表
     */
    EmployeeAnnualBonusDTO selectEmployeeAnnualBonusByAnnualBonusYear(@Param("annualBonusYear")int annualBonusYear);

    /**
    * 批量查询个人年终奖表
    *
    * @param employeeAnnualBonusIds 个人年终奖表主键集合
    * @return 个人年终奖表
    */
    List<EmployeeAnnualBonusDTO> selectEmployeeAnnualBonusByEmployeeAnnualBonusIds(@Param("employeeAnnualBonusIds") List<Long> employeeAnnualBonusIds);

    /**
    * 查询个人年终奖表列表
    *
    * @param employeeAnnualBonus 个人年终奖表
    * @return 个人年终奖表集合
    */
    List<EmployeeAnnualBonusDTO> selectEmployeeAnnualBonusList(@Param("employeeAnnualBonus")EmployeeAnnualBonus employeeAnnualBonus);

    /**
    * 新增个人年终奖表
    *
    * @param employeeAnnualBonus 个人年终奖表
    * @return 结果
    */
    int insertEmployeeAnnualBonus(@Param("employeeAnnualBonus")EmployeeAnnualBonus employeeAnnualBonus);

    /**
    * 修改个人年终奖表
    *
    * @param employeeAnnualBonus 个人年终奖表
    * @return 结果
    */
    int updateEmployeeAnnualBonus(@Param("employeeAnnualBonus")EmployeeAnnualBonus employeeAnnualBonus);

    /**
    * 批量修改个人年终奖表
    *
    * @param employeeAnnualBonusList 个人年终奖表
    * @return 结果
    */
    int updateEmployeeAnnualBonuss(@Param("employeeAnnualBonusList")List<EmployeeAnnualBonus> employeeAnnualBonusList);
    /**
    * 逻辑删除个人年终奖表
    *
    * @param employeeAnnualBonus
    * @return 结果
    */
    int logicDeleteEmployeeAnnualBonusByEmployeeAnnualBonusId(@Param("employeeAnnualBonus")EmployeeAnnualBonus employeeAnnualBonus);

    /**
    * 逻辑批量删除个人年终奖表
    *
    * @param employeeAnnualBonusIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteEmployeeAnnualBonusByEmployeeAnnualBonusIds(@Param("employeeAnnualBonusIds")List<Long> employeeAnnualBonusIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除个人年终奖表
    *
    * @param employeeAnnualBonusId 个人年终奖表主键
    * @return 结果
    */
    int deleteEmployeeAnnualBonusByEmployeeAnnualBonusId(@Param("employeeAnnualBonusId")Long employeeAnnualBonusId);

    /**
    * 物理批量删除个人年终奖表
    *
    * @param employeeAnnualBonusIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteEmployeeAnnualBonusByEmployeeAnnualBonusIds(@Param("employeeAnnualBonusIds")List<Long> employeeAnnualBonusIds);

    /**
    * 批量新增个人年终奖表
    *
    * @param EmployeeAnnualBonuss 个人年终奖表列表
    * @return 结果
    */
    int batchEmployeeAnnualBonus(@Param("employeeAnnualBonuss")List<EmployeeAnnualBonus> EmployeeAnnualBonuss);
}
