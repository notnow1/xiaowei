package net.qixiaowei.operate.cloud.service.salary;

import net.qixiaowei.operate.cloud.api.dto.salary.SalaryPayDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryStructureDTO;
import net.qixiaowei.operate.cloud.excel.salary.SalaryPayExcel;

import java.util.List;
import java.util.Map;


/**
 * SalaryPayService接口
 *
 * @author Graves
 * @since 2022-11-17
 */
public interface ISalaryPayService {
    /**
     * 查询工资发薪表
     *
     * @param salaryPayId 工资发薪表主键
     * @return 工资发薪表
     */
    SalaryPayDTO selectSalaryPayBySalaryPayId(Long salaryPayId);

    /**
     * 查询工资发薪表列表
     *
     * @param salaryPayDTO 工资发薪表
     * @return 工资发薪表集合
     */
    List<SalaryPayDTO> selectSalaryPayList(SalaryPayDTO salaryPayDTO);

    /**
     * 新增工资发薪表
     *
     * @param salaryPayDTO 工资发薪表
     * @return 结果
     */
    SalaryPayDTO insertSalaryPay(SalaryPayDTO salaryPayDTO);

    /**
     * 修改工资发薪表
     *
     * @param salaryPayDTO 工资发薪表
     * @return 结果
     */
    int updateSalaryPay(SalaryPayDTO salaryPayDTO);

    /**
     * 批量修改工资发薪表
     *
     * @param salaryPayDtos 工资发薪表
     * @return 结果
     */
    int updateSalaryPays(List<SalaryPayDTO> salaryPayDtos);

    /**
     * 批量新增工资发薪表
     *
     * @param salaryPayDtos 工资发薪表
     * @return 结果
     */
    int insertSalaryPays(List<SalaryPayDTO> salaryPayDtos);

    /**
     * 逻辑批量删除工资发薪表
     *
     * @param salaryPayIds 需要删除的工资发薪表集合
     * @return 结果
     */
    int logicDeleteSalaryPayBySalaryPayIds(List<Long> salaryPayIds);

    /**
     * 逻辑删除工资发薪表信息
     *
     * @param salaryPayDTO
     * @return 结果
     */
    int logicDeleteSalaryPayBySalaryPayId(SalaryPayDTO salaryPayDTO);

    /**
     * 批量删除工资发薪表
     *
     * @param SalaryPayDtos
     * @return 结果
     */
    int deleteSalaryPayBySalaryPayIds(List<SalaryPayDTO> SalaryPayDtos);

    /**
     * 逻辑删除工资发薪表信息
     *
     * @param salaryPayDTO
     * @return 结果
     */
    int deleteSalaryPayBySalaryPayId(SalaryPayDTO salaryPayDTO);


    /**
     * 删除工资发薪表信息
     *
     * @param salaryPayId 工资发薪表主键
     * @return 结果
     */
    int deleteSalaryPayBySalaryPayId(Long salaryPayId);

    /**
     * 导入Excel
     *
     * @param list
     */
    void importSalaryPay(List<Map<Integer, String>> list);

    /**
     * 导出Excel
     *
     * @param salaryPayDTO
     * @return
     */
    List<SalaryPayExcel> exportSalaryPay(SalaryPayDTO salaryPayDTO);

    /**
     * 查询薪酬架构报表列表
     *
     * @param salaryStructureDTO
     * @return
     */
    SalaryStructureDTO selectSalaryPayStructureList(SalaryStructureDTO salaryStructureDTO);
}
