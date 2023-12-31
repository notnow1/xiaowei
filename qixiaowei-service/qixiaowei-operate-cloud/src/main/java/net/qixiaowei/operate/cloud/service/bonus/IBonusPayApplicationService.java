package net.qixiaowei.operate.cloud.service.bonus;

import java.util.List;

import net.qixiaowei.operate.cloud.api.dto.bonus.BonusPayApplicationDTO;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusPayObjectsDTO;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusPayStandingDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryItemDTO;


/**
* BonusPayApplicationService接口
* @author TANGMICHI
* @since 2022-12-08
*/
public interface IBonusPayApplicationService{
    /**
    * 查询奖金发放申请表
    *
    * @param bonusPayApplicationId 奖金发放申请表主键
    * @return 奖金发放申请表
    */
    BonusPayApplicationDTO selectBonusPayApplicationByBonusPayApplicationId(Long bonusPayApplicationId);

    /**
    * 查询奖金发放申请表列表
    *
    * @param bonusPayApplicationDTO 奖金发放申请表
    * @return 奖金发放申请表集合
    */
    List<BonusPayApplicationDTO> selectBonusPayApplicationList(BonusPayApplicationDTO bonusPayApplicationDTO);
    /**
     * 处理返回
     *
     * @param result 返回集合
     * @return 返回集合
     */
    void handleResult(List<BonusPayApplicationDTO> result);
    /**
     * 生成奖项编码
     *
     * @return 奖项编码
     */
    String generateAwardCode();

    /**
    * 新增奖金发放申请表
    *
    * @param bonusPayApplicationDTO 奖金发放申请表
    * @return 结果
    */
    BonusPayApplicationDTO insertBonusPayApplication(BonusPayApplicationDTO bonusPayApplicationDTO);

    /**
    * 修改奖金发放申请表
    *
    * @param bonusPayApplicationDTO 奖金发放申请表
    * @return 结果
    */
    int updateBonusPayApplication(BonusPayApplicationDTO bonusPayApplicationDTO);

    /**
    * 批量修改奖金发放申请表
    *
    * @param bonusPayApplicationDtos 奖金发放申请表
    * @return 结果
    */
    int updateBonusPayApplications(List<BonusPayApplicationDTO> bonusPayApplicationDtos);

    /**
    * 批量新增奖金发放申请表
    *
    * @param bonusPayApplicationDtos 奖金发放申请表
    * @return 结果
    */
    int insertBonusPayApplications(List<BonusPayApplicationDTO> bonusPayApplicationDtos);

    /**
    * 逻辑批量删除奖金发放申请表
    *
    * @param bonusPayApplicationIds 需要删除的奖金发放申请表集合
    * @return 结果
    */
    int logicDeleteBonusPayApplicationByBonusPayApplicationIds(List<Long> bonusPayApplicationIds);

    /**
    * 逻辑删除奖金发放申请表信息
    *
    * @param bonusPayApplicationDTO
    * @return 结果
    */
    int logicDeleteBonusPayApplicationByBonusPayApplicationId(BonusPayApplicationDTO bonusPayApplicationDTO);
    /**
    * 批量删除奖金发放申请表
    *
    * @param BonusPayApplicationDtos
    * @return 结果
    */
    int deleteBonusPayApplicationByBonusPayApplicationIds(List<BonusPayApplicationDTO> BonusPayApplicationDtos);

    /**
    * 逻辑删除奖金发放申请表信息
    *
    * @param bonusPayApplicationDTO
    * @return 结果
    */
    int deleteBonusPayApplicationByBonusPayApplicationId(BonusPayApplicationDTO bonusPayApplicationDTO);


    /**
    * 删除奖金发放申请表信息
    *
    * @param bonusPayApplicationId 奖金发放申请表主键
    * @return 结果
    */
    int deleteBonusPayApplicationByBonusPayApplicationId(Long bonusPayApplicationId);

    /**
     * 分页查询奖金发放台账
     * @param bonusPayApplicationDTO
     * @return
     */
    List<BonusPayStandingDTO> bonusGrantStandingList(BonusPayApplicationDTO bonusPayApplicationDTO);

    /**
     * 根据人员id查询个人年终奖 奖金发放对象ID(员工id)
     * @param employeeIds
     * @return
     */
    List<BonusPayObjectsDTO> selectBonusPayApplicationByEmployeeIds(List<Long> employeeIds);

    /**
     * 根据部门id查询个人年终奖 (申请部门,预算部门,获奖部门)
     * @param departmentIds
     * @return
     */
    List<BonusPayApplicationDTO> selectBonusPayApplicationByDepartmentIds(List<Long> departmentIds);

    /**
     * 查询奖金发放申请奖项类别下拉框
     * @param bonusPayApplicationDTO
     * @return
     */
    List<SalaryItemDTO> applyByYear(BonusPayApplicationDTO bonusPayApplicationDTO);
}
