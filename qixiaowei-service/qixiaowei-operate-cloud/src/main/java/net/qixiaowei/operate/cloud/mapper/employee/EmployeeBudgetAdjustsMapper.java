package net.qixiaowei.operate.cloud.mapper.employee;

import java.util.List;
import net.qixiaowei.operate.cloud.api.domain.employee.EmployeeBudgetAdjusts;
import net.qixiaowei.operate.cloud.api.dto.employee.EmployeeBudgetAdjustsDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* EmployeeBudgetAdjustsMapper接口
* @author TANGMICHI
* @since 2022-11-22
*/
public interface EmployeeBudgetAdjustsMapper{
    /**
    * 查询人力预算调整表
    *
    * @param employeeBudgetAdjustsId 人力预算调整表主键
    * @return 人力预算调整表
    */
    EmployeeBudgetAdjustsDTO selectEmployeeBudgetAdjustsByEmployeeBudgetAdjustsId(@Param("employeeBudgetAdjustsId")Long employeeBudgetAdjustsId);


    /**
     * 根据人力预算明细表主键查询人力预算调整表
     *
     * @param employeeBudgetDetailsId 人力预算明细表主键
     * @return 人力预算调整表
     */
    List<EmployeeBudgetAdjustsDTO> selectEmployeeBudgetAdjustsByEmployeeBudgetDetailsId(@Param("employeeBudgetDetailsId")Long employeeBudgetDetailsId);
    /**
    * 批量查询人力预算调整表
    *
    * @param employeeBudgetAdjustsIds 人力预算调整表主键集合
    * @return 人力预算调整表
    */
    List<EmployeeBudgetAdjustsDTO> selectEmployeeBudgetAdjustsByEmployeeBudgetAdjustsIds(@Param("employeeBudgetAdjustsIds") List<Long> employeeBudgetAdjustsIds);

    /**
     * 根据人力预算明细表主键集合批量查询人力预算调整表
     *
     * @param employeeBudgetDetailsIds 人力预算明细表主键集合
     * @return 人力预算调整表
     */
    List<EmployeeBudgetAdjustsDTO> selectEmployeeBudgetAdjustsByEmployeeBudgetDetailsIds(@Param("employeeBudgetDetailsIds") List<Long> employeeBudgetDetailsIds);

    /**
    * 查询人力预算调整表列表
    *
    * @param employeeBudgetAdjusts 人力预算调整表
    * @return 人力预算调整表集合
    */
    List<EmployeeBudgetAdjustsDTO> selectEmployeeBudgetAdjustsList(@Param("employeeBudgetAdjusts")EmployeeBudgetAdjusts employeeBudgetAdjusts);

    /**
    * 新增人力预算调整表
    *
    * @param employeeBudgetAdjusts 人力预算调整表
    * @return 结果
    */
    int insertEmployeeBudgetAdjusts(@Param("employeeBudgetAdjusts")EmployeeBudgetAdjusts employeeBudgetAdjusts);

    /**
    * 修改人力预算调整表
    *
    * @param employeeBudgetAdjusts 人力预算调整表
    * @return 结果
    */
    int updateEmployeeBudgetAdjusts(@Param("employeeBudgetAdjusts")EmployeeBudgetAdjusts employeeBudgetAdjusts);

    /**
    * 批量修改人力预算调整表
    *
    * @param employeeBudgetAdjustsList 人力预算调整表
    * @return 结果
    */
    int updateEmployeeBudgetAdjustss(@Param("employeeBudgetAdjustsList")List<EmployeeBudgetAdjusts> employeeBudgetAdjustsList);
    /**
    * 逻辑删除人力预算调整表
    *
    * @param employeeBudgetAdjusts
    * @return 结果
    */
    int logicDeleteEmployeeBudgetAdjustsByEmployeeBudgetAdjustsId(@Param("employeeBudgetAdjusts")EmployeeBudgetAdjusts employeeBudgetAdjusts);

    /**
    * 逻辑批量删除人力预算调整表
    *
    * @param employeeBudgetAdjustsIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteEmployeeBudgetAdjustsByEmployeeBudgetAdjustsIds(@Param("employeeBudgetAdjustsIds")List<Long> employeeBudgetAdjustsIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除人力预算调整表
    *
    * @param employeeBudgetAdjustsId 人力预算调整表主键
    * @return 结果
    */
    int deleteEmployeeBudgetAdjustsByEmployeeBudgetAdjustsId(@Param("employeeBudgetAdjustsId")Long employeeBudgetAdjustsId);

    /**
    * 物理批量删除人力预算调整表
    *
    * @param employeeBudgetAdjustsIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteEmployeeBudgetAdjustsByEmployeeBudgetAdjustsIds(@Param("employeeBudgetAdjustsIds")List<Long> employeeBudgetAdjustsIds);

    /**
    * 批量新增人力预算调整表
    *
    * @param EmployeeBudgetAdjustss 人力预算调整表列表
    * @return 结果
    */
    int batchEmployeeBudgetAdjusts(@Param("employeeBudgetAdjustss")List<EmployeeBudgetAdjusts> EmployeeBudgetAdjustss);
}
