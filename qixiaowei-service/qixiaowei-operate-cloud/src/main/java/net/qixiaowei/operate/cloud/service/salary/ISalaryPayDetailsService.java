package net.qixiaowei.operate.cloud.service.salary;

import java.util.List;

import net.qixiaowei.operate.cloud.api.dto.salary.SalaryPayDetailsDTO;
import net.qixiaowei.operate.cloud.excel.salary.SalaryPayDetailsExcel;


/**
 * SalaryPayDetailsService接口
 *
 * @author Graves
 * @since 2022-11-17
 */
public interface ISalaryPayDetailsService {
    /**
     * 查询工资发薪明细表
     *
     * @param salaryPayDetailsId 工资发薪明细表主键
     * @return 工资发薪明细表
     */
    SalaryPayDetailsDTO selectSalaryPayDetailsBySalaryPayDetailsId(Long salaryPayDetailsId);

    /**
     * 查询工资发薪明细表列表
     *
     * @param salaryPayDetailsDTO 工资发薪明细表
     * @return 工资发薪明细表集合
     */
    List<SalaryPayDetailsDTO> selectSalaryPayDetailsList(SalaryPayDetailsDTO salaryPayDetailsDTO);

    /**
     * 新增工资发薪明细表
     *
     * @param salaryPayDetailsDTO 工资发薪明细表
     * @return 结果
     */
    SalaryPayDetailsDTO insertSalaryPayDetails(SalaryPayDetailsDTO salaryPayDetailsDTO);

    /**
     * 修改工资发薪明细表
     *
     * @param salaryPayDetailsDTO 工资发薪明细表
     * @return 结果
     */
    int updateSalaryPayDetails(SalaryPayDetailsDTO salaryPayDetailsDTO);

    /**
     * 批量修改工资发薪明细表
     *
     * @param salaryPayDetailsDtos 工资发薪明细表
     * @return 结果
     */
    int updateSalaryPayDetailss(List<SalaryPayDetailsDTO> salaryPayDetailsDtos);

    /**
     * 批量新增工资发薪明细表
     *
     * @param salaryPayDetailsDtos 工资发薪明细表
     * @return 结果
     */
    int insertSalaryPayDetailss(List<SalaryPayDetailsDTO> salaryPayDetailsDtos);

    /**
     * 逻辑批量删除工资发薪明细表
     *
     * @param salaryPayDetailsIds 需要删除的工资发薪明细表集合
     * @return 结果
     */
    int logicDeleteSalaryPayDetailsBySalaryPayDetailsIds(List<Long> salaryPayDetailsIds);

    /**
     * 逻辑删除工资发薪明细表信息
     *
     * @param salaryPayDetailsDTO
     * @return 结果
     */
    int logicDeleteSalaryPayDetailsBySalaryPayDetailsId(SalaryPayDetailsDTO salaryPayDetailsDTO);

    /**
     * 批量删除工资发薪明细表
     *
     * @param SalaryPayDetailsDtos
     * @return 结果
     */
    int deleteSalaryPayDetailsBySalaryPayDetailsIds(List<SalaryPayDetailsDTO> SalaryPayDetailsDtos);

    /**
     * 逻辑删除工资发薪明细表信息
     *
     * @param salaryPayDetailsDTO
     * @return 结果
     */
    int deleteSalaryPayDetailsBySalaryPayDetailsId(SalaryPayDetailsDTO salaryPayDetailsDTO);


    /**
     * 删除工资发薪明细表信息
     *
     * @param salaryPayDetailsId 工资发薪明细表主键
     * @return 结果
     */
    int deleteSalaryPayDetailsBySalaryPayDetailsId(Long salaryPayDetailsId);

    /**
     * 导入Excel
     *
     * @param list
     */
    void importSalaryPayDetails(List<SalaryPayDetailsExcel> list);

    /**
     * 导出Excel
     *
     * @param salaryPayDetailsDTO
     * @return
     */
    List<SalaryPayDetailsExcel> exportSalaryPayDetails(SalaryPayDetailsDTO salaryPayDetailsDTO);

    /**
     * 通过工资ID返回详情
     *
     * @param salaryPayId
     * @return
     */
    List<SalaryPayDetailsDTO> selectSalaryPayDetailsBySalaryPayId(Long salaryPayId);

    /**
     * 通过工资ID删除列表
     *
     * @param salaryPayId
     * @return
     */
    int logicDeleteSalaryPayDetailsBySalaryPayId(Long salaryPayId);

    /**
     * 通过工资ID列表删除列表
     *
     * @param salaryPayIds
     * @return
     */
    int logicDeleteSalaryPayDetailsBySalaryPayIds(List<Long> salaryPayIds);
}
