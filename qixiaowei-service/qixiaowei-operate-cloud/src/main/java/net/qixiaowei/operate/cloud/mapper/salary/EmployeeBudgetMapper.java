package net.qixiaowei.operate.cloud.mapper.salary;

import java.util.List;
import net.qixiaowei.operate.cloud.api.domain.salary.EmployeeBudget;
import net.qixiaowei.operate.cloud.api.dto.salary.EmployeeBudgetDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* EmployeeBudgetMapper接口
* @author TANGMICHI
* @since 2022-11-18
*/
public interface EmployeeBudgetMapper{
    /**
    * 查询人力预算表
    *
    * @param employeeBudgetId 人力预算表主键
    * @return 人力预算表
    */
    EmployeeBudgetDTO selectEmployeeBudgetByEmployeeBudgetId(@Param("employeeBudgetId")Long employeeBudgetId);


    /**
    * 批量查询人力预算表
    *
    * @param employeeBudgetIds 人力预算表主键集合
    * @return 人力预算表
    */
    List<EmployeeBudgetDTO> selectEmployeeBudgetByEmployeeBudgetIds(@Param("employeeBudgetIds") List<Long> employeeBudgetIds);

    /**
    * 查询人力预算表列表
    *
    * @param employeeBudget 人力预算表
    * @return 人力预算表集合
    */
    List<EmployeeBudgetDTO> selectEmployeeBudgetList(@Param("employeeBudget")EmployeeBudget employeeBudget);

    /**
    * 新增人力预算表
    *
    * @param employeeBudget 人力预算表
    * @return 结果
    */
    int insertEmployeeBudget(@Param("employeeBudget")EmployeeBudget employeeBudget);

    /**
    * 修改人力预算表
    *
    * @param employeeBudget 人力预算表
    * @return 结果
    */
    int updateEmployeeBudget(@Param("employeeBudget")EmployeeBudget employeeBudget);

    /**
    * 批量修改人力预算表
    *
    * @param employeeBudgetList 人力预算表
    * @return 结果
    */
    int updateEmployeeBudgets(@Param("employeeBudgetList")List<EmployeeBudget> employeeBudgetList);
    /**
    * 逻辑删除人力预算表
    *
    * @param employeeBudget
    * @return 结果
    */
    int logicDeleteEmployeeBudgetByEmployeeBudgetId(@Param("employeeBudget")EmployeeBudget employeeBudget);

    /**
    * 逻辑批量删除人力预算表
    *
    * @param employeeBudgetIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteEmployeeBudgetByEmployeeBudgetIds(@Param("employeeBudgetIds")List<Long> employeeBudgetIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除人力预算表
    *
    * @param employeeBudgetId 人力预算表主键
    * @return 结果
    */
    int deleteEmployeeBudgetByEmployeeBudgetId(@Param("employeeBudgetId")Long employeeBudgetId);

    /**
    * 物理批量删除人力预算表
    *
    * @param employeeBudgetIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteEmployeeBudgetByEmployeeBudgetIds(@Param("employeeBudgetIds")List<Long> employeeBudgetIds);

    /**
    * 批量新增人力预算表
    *
    * @param EmployeeBudgets 人力预算表列表
    * @return 结果
    */
    int batchEmployeeBudget(@Param("employeeBudgets")List<EmployeeBudget> EmployeeBudgets);
}
