package net.qixiaowei.operate.cloud.service.salary;

import net.qixiaowei.operate.cloud.api.dto.salary.SalaryItemDTO;
import net.qixiaowei.operate.cloud.api.vo.salary.SalaryItemVO;
import net.qixiaowei.operate.cloud.excel.salary.SalaryItemExcel;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;


/**
 * SalaryItemService接口
 *
 * @author Graves
 * @since 2022-10-05
 */
public interface ISalaryItemService {
    /**
     * 查询工资项
     *
     * @param salaryItemId 工资项主键
     * @return 工资项
     */
    SalaryItemDTO selectSalaryItemBySalaryItemId(Long salaryItemId);

    /**
     * 查询工资项列表
     *
     * @param salaryItemDTO 工资项
     * @return 工资项集合
     */
    List<SalaryItemDTO> selectSalaryItemList(SalaryItemDTO salaryItemDTO);

    /**
     * 查询工资项分页列表
     *
     * @param salaryItemDTO 工资项
     * @return 工资项集合
     */
    List<SalaryItemDTO> selectSalaryItemPageList(SalaryItemDTO salaryItemDTO);

    /**
     * 处理返回
     *
     * @param result 返回集合
     * @return 返回集合
     */
    void handleResult(List<SalaryItemDTO> result);

    /**
     * 为一级工资二级工资附名称
     *
     * @param itemDTO
     */
    void salarySetName(SalaryItemDTO itemDTO);

    /**
     * 新增工资项
     *
     * @param salaryItemDTO 工资项
     * @return 结果
     */
    SalaryItemDTO insertSalaryItem(SalaryItemDTO salaryItemDTO);

    /**
     * 初始化工资项
     *
     * @return 结果
     */
    Boolean initSalaryItem(Long userId);

    /**
     * 修改工资项
     *
     * @param salaryItemDTO 工资项
     * @return 结果
     */
    int updateSalaryItem(SalaryItemDTO salaryItemDTO);

    /**
     * 批量修改工资项
     *
     * @param salaryItemDtos 工资项
     * @return 结果
     */
    int updateSalaryItems(List<SalaryItemDTO> salaryItemDtos);

    /**
     * 批量新增工资项
     *
     * @param salaryItemDtos 工资项
     * @return 结果
     */
    int insertSalaryItems(List<SalaryItemDTO> salaryItemDtos);

    /**
     * 逻辑批量删除工资项
     *
     * @param salaryItemIds 需要删除的工资项集合
     * @return 结果
     */
    int logicDeleteSalaryItemBySalaryItemIds(@RequestBody List<Long> salaryItemIds);

    /**
     * 逻辑删除工资项信息
     *
     * @param salaryItemDTO
     * @return 结果
     */
    int logicDeleteSalaryItemBySalaryItemId(SalaryItemDTO salaryItemDTO);

    /**
     * 逻辑批量删除工资项
     *
     * @param SalaryItemDtos 需要删除的工资项集合
     * @return 结果
     */
    int deleteSalaryItemBySalaryItemIds(List<SalaryItemDTO> SalaryItemDtos);

    /**
     * 逻辑删除工资项信息
     *
     * @param salaryItemDTO
     * @return 结果
     */
    int deleteSalaryItemBySalaryItemId(SalaryItemDTO salaryItemDTO);


    /**
     * 删除工资项信息
     *
     * @param salaryItemId 工资项主键
     * @return 结果
     */
    int deleteSalaryItemBySalaryItemId(Long salaryItemId);

    /**
     * 根据salaryId查询工资详情
     *
     * @param salaryId
     * @return
     */
    SalaryItemDTO detailSalaryItemBySalaryId(Long salaryId);

    /**
     * 导出工资条
     *
     * @param salaryItemDTO
     * @return
     */
    List<SalaryItemExcel> exportSalaryExcel(SalaryItemDTO salaryItemDTO);

    /**
     * 通过ID集合查找工资项
     *
     * @param salaryItemIds
     * @return
     */
    List<SalaryItemDTO> selectSalaryItemBySalaryItemIds(List<Long> salaryItemIds);

    /**
     * 查找二级为奖金的三级工资条
     *
     * @param salaryItemDTO 工资项
     * @return 结果
     */
    List<Map<String, String>> selectBonusItemList(SalaryItemDTO salaryItemDTO);

    /**
     * 查找二级为奖金的三级工资条
     *
     * @return 结果
     */
    List<SalaryItemDTO> applyBonusList();

    /**
     * 批量修改工资项
     *
     * @param salaryItemVOS 项目dto列表
     * @return 结果
     */
    int editSalaryItems(List<SalaryItemVO> salaryItemVOS);

    /**
     * 工资项编辑列表
     *
     * @param salaryItemDTO 工资项dto
     * @return 结果
     */
    List<SalaryItemVO> selectSalaryItemEditList(SalaryItemDTO salaryItemDTO);

    /**
     * 查找公司级奖项类别
     * @return
     */
    List<SalaryItemDTO> selectCompanyBonusList();
}
