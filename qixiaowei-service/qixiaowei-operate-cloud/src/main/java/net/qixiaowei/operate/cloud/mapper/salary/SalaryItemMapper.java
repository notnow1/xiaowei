package net.qixiaowei.operate.cloud.mapper.salary;

import java.util.List;

import net.qixiaowei.operate.cloud.api.domain.salary.SalaryItem;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryItemDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * SalaryItemMapper接口
 *
 * @author Graves
 * @since 2022-10-05
 */
public interface SalaryItemMapper {
    /**
     * 查询工资项
     *
     * @param salaryItemId 工资项主键
     * @return 工资项
     */
    SalaryItemDTO selectSalaryItemBySalaryItemId(@Param("salaryItemId") Long salaryItemId);

    /**
     * 查询工资项列表
     *
     * @param salaryItem 工资项
     * @return 工资项集合
     */
    List<SalaryItemDTO> selectSalaryItemList(@Param("salaryItem") SalaryItem salaryItem);

    /**
     * 新增工资项
     *
     * @param salaryItem 工资项
     * @return 结果
     */
    int insertSalaryItem(@Param("salaryItem") SalaryItem salaryItem);

    /**
     * 修改工资项
     *
     * @param salaryItem 工资项
     * @return 结果
     */
    int updateSalaryItem(@Param("salaryItem") SalaryItem salaryItem);

    /**
     * 批量修改工资项
     *
     * @param salaryItemList 工资项
     * @return 结果
     */
    int updateSalaryItems(@Param("salaryItemList") List<SalaryItem> salaryItemList);

    /**
     * 逻辑删除工资项
     *
     * @param salaryItem
     * @return 结果
     */
    int logicDeleteSalaryItemBySalaryItemId(@Param("salaryItem") SalaryItem salaryItem, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);

    /**
     * 逻辑批量删除工资项
     *
     * @param salaryItemIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteSalaryItemBySalaryItemIds(@Param("salaryItemIds") List<Long> salaryItemIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除工资项
     *
     * @param salaryItemId 工资项主键
     * @return 结果
     */
    int deleteSalaryItemBySalaryItemId(@Param("salaryItemId") Long salaryItemId);

    /**
     * 物理批量删除工资项
     *
     * @param salaryItemIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteSalaryItemBySalaryItemIds(@Param("salaryItemIds") List<Long> salaryItemIds);

    /**
     * 批量新增工资项
     *
     * @param SalaryItems 工资项列表
     * @return 结果
     */
    int batchSalaryItem(@Param("salaryItems") List<SalaryItem> SalaryItems);

    /**
     * 根据id集合判断是否存在
     *
     * @param salaryItemIds
     * @return
     */
    List<Long> isExist(@Param("salaryItemIds") List<Long> salaryItemIds);
}
