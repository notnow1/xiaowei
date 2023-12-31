package net.qixiaowei.operate.cloud.mapper.salary;

import java.util.List;

import net.qixiaowei.operate.cloud.api.domain.salary.SalaryItem;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusPayApplicationDTO;
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
     * 部门奖金预算预制数据 从工资条配置中取值，取所有部门级且二级工资项目属于奖金的工资项目
     */
    List<SalaryItemDTO> selectSalaryAddPrefabricate();

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
     * 查询工资项列表
     *
     * @param salaryItem 工资项
     * @return 工资项集合
     */
    List<SalaryItemDTO> selectSalaryItemEditList(@Param("salaryItem") SalaryItem salaryItem);

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
     * 根据id集合获取数据
     *
     * @param salaryItemIds
     * @return
     */
    List<SalaryItemDTO> getSalaryItemByIds(@Param("salaryItemIds") List<Long> salaryItemIds);

    /**
     * 根据id获取数据
     *
     * @param thirdLevelItem
     * @return
     */
    SalaryItemDTO getSalaryItemByThirdLevelItem(@Param("thirdLevelItem") String thirdLevelItem);

    /**
     * 通过ID集合查找工资项
     *
     * @param salaryItemIds
     * @return
     */
    List<SalaryItemDTO> selectSalaryItemBySalaryItemIds(@Param("salaryItemIds") List<Long> salaryItemIds);

    /**
     * 查询公司级的工资项id
     *
     * @return
     */
    List<SalaryItemDTO> selectCompanyBonusList();

    /**
     * 所有二级工资项目为奖金且级别为部门级的三级工资项目
     *
     * @return
     */
    List<SalaryItemDTO> selectSalaryItemByBonus();

    /**
     * 根据id查询所有二级工资项目为奖金且级别为部门级的三级工资项目
     *
     * @return
     */
    List<SalaryItemDTO> selectSalaryItemByBonusId(@Param("salaryItemId") Long salaryItemId);

    /**
     * 查询最大排序值
     *
     * @return Integer
     */
    Integer selectMaxSort();

    /**
     * 查找二级为奖金的三级工资条
     *
     * @return
     */
    List<SalaryItemDTO> applyBonusList();


    /**
     * 根据年份查询和奖项类别id查询部门奖金预算的奖项类别
     *
     * @return
     */
    List<SalaryItemDTO> applyByYear(@Param("bonusPayApplicationDTO") BonusPayApplicationDTO bonusPayApplicationDTO);

    /**
     * 根据id查询所有二级工资项目为奖金且级别为公司级的三级工资项目
     *
     * @return
     */
    List<SalaryItemDTO> selectSalaryItemByCompany(@Param("salaryItemId") Long salaryItemId);
}
