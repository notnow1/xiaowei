package net.qixiaowei.operate.cloud.service.salary;

import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryPayDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryPayDetailsDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryStructureDTO;
import net.qixiaowei.operate.cloud.api.vo.salary.SalaryPayImportTempDataVO;
import net.qixiaowei.operate.cloud.excel.salary.SalaryPayExcel;

import java.io.IOException;
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
     * 处理返回
     *
     * @param result 返回集合
     * @return 返回集合
     */
    void handleResult(List<SalaryPayDTO> result);

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
     * @param list      列表
     * @param sheetName sheet名称
     */
    Map<Object, Object> importSalaryPay(SalaryPayImportTempDataVO salaryPayImportTempDataVO, List<Map<Integer, String>> list, String sheetName);

    /**
     * 导出Excel
     *
     * @param salaryPayDTO
     * @return
     */
    List<SalaryPayExcel> exportSalaryPay(SalaryPayDTO salaryPayDTO);

    /**
     * 薪酬架构报表
     *
     * @param salaryStructureDTO 薪酬架构报表
     * @return
     */
    SalaryStructureDTO selectSalaryPayStructure(SalaryStructureDTO salaryStructureDTO);


    /**
     * 查询薪酬架构报表列表
     *
     * @param salaryStructureDTO 薪酬架构报表
     * @return
     */
    TableDataInfo selectSalaryPayStructureList(SalaryStructureDTO salaryStructureDTO);

    /**
     * 通过Id集合查找发行表
     *
     * @param isSelect     是否选中
     * @param salaryPayIds 薪酬发行ID集合
     * @return List
     */
    List<SalaryPayDTO> selectSalaryPayBySalaryPay(Integer isSelect, List<Long> salaryPayIds);

    /**
     * 为一级工资二级工资附名称
     *
     * @param salaryPayDetailsDTO 发薪详情表
     */
    void salarySetName(SalaryPayDetailsDTO salaryPayDetailsDTO);


    /**
     * 根据员工ID查询工资条
     *
     * @param employeeId 员工ID
     * @return List
     */
    List<SalaryPayDTO> selectByEmployeeId(Long employeeId);
}
