package net.qixiaowei.operate.cloud.service.salary;

import net.qixiaowei.operate.cloud.api.dto.salary.DeptAnnualBonusDTO;

import java.util.List;


/**
* DeptAnnualBonusService接口
* @author TANGMICHI
* @since 2022-12-06
*/
public interface IDeptAnnualBonusService{
    /**
    * 查询部门年终奖表
    *
    * @param deptAnnualBonusId 部门年终奖表主键
    * @return 部门年终奖表
    */
    DeptAnnualBonusDTO selectDeptAnnualBonusByDeptAnnualBonusId(Long deptAnnualBonusId);

    /**
    * 查询部门年终奖表列表
    *
    * @param deptAnnualBonusDTO 部门年终奖表
    * @return 部门年终奖表集合
    */
    List<DeptAnnualBonusDTO> selectDeptAnnualBonusList(DeptAnnualBonusDTO deptAnnualBonusDTO);

    /**
    * 新增部门年终奖表
    *
    * @param deptAnnualBonusDTO 部门年终奖表
    * @return 结果
    */
    DeptAnnualBonusDTO insertDeptAnnualBonus(DeptAnnualBonusDTO deptAnnualBonusDTO);

    /**
    * 修改部门年终奖表
    *
    * @param deptAnnualBonusDTO 部门年终奖表
    * @return 结果
    */
    int updateDeptAnnualBonus(DeptAnnualBonusDTO deptAnnualBonusDTO);

    /**
    * 批量修改部门年终奖表
    *
    * @param deptAnnualBonusDtos 部门年终奖表
    * @return 结果
    */
    int updateDeptAnnualBonuss(List<DeptAnnualBonusDTO> deptAnnualBonusDtos);

    /**
    * 批量新增部门年终奖表
    *
    * @param deptAnnualBonusDtos 部门年终奖表
    * @return 结果
    */
    int insertDeptAnnualBonuss(List<DeptAnnualBonusDTO> deptAnnualBonusDtos);

    /**
    * 逻辑批量删除部门年终奖表
    *
    * @param deptAnnualBonusIds 需要删除的部门年终奖表集合
    * @return 结果
    */
    int logicDeleteDeptAnnualBonusByDeptAnnualBonusIds(List<Long> deptAnnualBonusIds);

    /**
    * 逻辑删除部门年终奖表信息
    *
    * @param deptAnnualBonusDTO
    * @return 结果
    */
    int logicDeleteDeptAnnualBonusByDeptAnnualBonusId(DeptAnnualBonusDTO deptAnnualBonusDTO);
    /**
    * 批量删除部门年终奖表
    *
    * @param DeptAnnualBonusDtos
    * @return 结果
    */
    int deleteDeptAnnualBonusByDeptAnnualBonusIds(List<DeptAnnualBonusDTO> DeptAnnualBonusDtos);

    /**
    * 逻辑删除部门年终奖表信息
    *
    * @param deptAnnualBonusDTO
    * @return 结果
    */
    int deleteDeptAnnualBonusByDeptAnnualBonusId(DeptAnnualBonusDTO deptAnnualBonusDTO);


    /**
    * 删除部门年终奖表信息
    *
    * @param deptAnnualBonusId 部门年终奖表主键
    * @return 结果
    */
    int deleteDeptAnnualBonusByDeptAnnualBonusId(Long deptAnnualBonusId);

    /**
     * 部门年终奖预制数据
     * @param annualBonusYear
     * @return
     */
    DeptAnnualBonusDTO addPrefabricate(int annualBonusYear);
}
