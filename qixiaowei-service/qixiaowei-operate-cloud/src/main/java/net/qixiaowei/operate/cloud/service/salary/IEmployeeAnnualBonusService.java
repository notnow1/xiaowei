package net.qixiaowei.operate.cloud.service.salary;

import java.util.List;

import net.qixiaowei.operate.cloud.api.dto.salary.EmpAnnualBonusSnapshotDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.EmployeeAnnualBonusDTO;



/**
* EmployeeAnnualBonusService接口
* @author TANGMICHI
* @since 2022-12-02
*/
public interface IEmployeeAnnualBonusService{
    /**
    * 查询个人年终奖表
    *
    * @param employeeAnnualBonusId 个人年终奖表主键
    * @return 个人年终奖表
    */
    EmployeeAnnualBonusDTO selectEmployeeAnnualBonusByEmployeeAnnualBonusId(Long employeeAnnualBonusId);

    /**
    * 查询个人年终奖表列表
    *
    * @param employeeAnnualBonusDTO 个人年终奖表
    * @return 个人年终奖表集合
    */
    List<EmployeeAnnualBonusDTO> selectEmployeeAnnualBonusList(EmployeeAnnualBonusDTO employeeAnnualBonusDTO);

    /**
    * 新增个人年终奖表
    *
    * @param employeeAnnualBonusDTO 个人年终奖表
    * @return 结果
    */
    EmployeeAnnualBonusDTO insertEmployeeAnnualBonus(EmployeeAnnualBonusDTO employeeAnnualBonusDTO);

    /**
    * 修改个人年终奖表
    *
    * @param employeeAnnualBonusDTO 个人年终奖表
    * @return 结果
    */
    int updateEmployeeAnnualBonus(EmployeeAnnualBonusDTO employeeAnnualBonusDTO);

    /**
    * 批量修改个人年终奖表
    *
    * @param employeeAnnualBonusDtos 个人年终奖表
    * @return 结果
    */
    int updateEmployeeAnnualBonuss(List<EmployeeAnnualBonusDTO> employeeAnnualBonusDtos);

    /**
    * 批量新增个人年终奖表
    *
    * @param employeeAnnualBonusDtos 个人年终奖表
    * @return 结果
    */
    int insertEmployeeAnnualBonuss(List<EmployeeAnnualBonusDTO> employeeAnnualBonusDtos);

    /**
    * 逻辑批量删除个人年终奖表
    *
    * @param employeeAnnualBonusIds 需要删除的个人年终奖表集合
    * @return 结果
    */
    int logicDeleteEmployeeAnnualBonusByEmployeeAnnualBonusIds(List<Long> employeeAnnualBonusIds);

    /**
    * 逻辑删除个人年终奖表信息
    *
    * @param employeeAnnualBonusDTO
    * @return 结果
    */
    int logicDeleteEmployeeAnnualBonusByEmployeeAnnualBonusId(EmployeeAnnualBonusDTO employeeAnnualBonusDTO);
    /**
    * 批量删除个人年终奖表
    *
    * @param EmployeeAnnualBonusDtos
    * @return 结果
    */
    int deleteEmployeeAnnualBonusByEmployeeAnnualBonusIds(List<EmployeeAnnualBonusDTO> EmployeeAnnualBonusDtos);

    /**
    * 逻辑删除个人年终奖表信息
    *
    * @param employeeAnnualBonusDTO
    * @return 结果
    */
    int deleteEmployeeAnnualBonusByEmployeeAnnualBonusId(EmployeeAnnualBonusDTO employeeAnnualBonusDTO);


    /**
    * 删除个人年终奖表信息
    *
    * @param employeeAnnualBonusId 个人年终奖表主键
    * @return 结果
    */
    int deleteEmployeeAnnualBonusByEmployeeAnnualBonusId(Long employeeAnnualBonusId);

    /**
     * 个人年终奖表选择部门后预制数据
     * @param employeeAnnualBonusDTO
     * @return
     */
    List<EmpAnnualBonusSnapshotDTO> addPrefabricate(EmployeeAnnualBonusDTO employeeAnnualBonusDTO);

    /**
     * 新增提交个人年终奖表
     * @param employeeAnnualBonusDTO
     * @return
     */
    EmployeeAnnualBonusDTO submitSave(EmployeeAnnualBonusDTO employeeAnnualBonusDTO);

    /**
     * 实时查询个人年终奖表详情
     * @param employeeAnnualBonusDTO
     * @return
     */
    List<EmpAnnualBonusSnapshotDTO> realTimeDetails(EmployeeAnnualBonusDTO employeeAnnualBonusDTO);
}
