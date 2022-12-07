package net.qixiaowei.operate.cloud.mapper.salary;

import net.qixiaowei.operate.cloud.api.domain.salary.DeptAnnualBonus;
import net.qixiaowei.operate.cloud.api.dto.salary.DeptAnnualBonusDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


/**
* DeptAnnualBonusMapper接口
* @author TANGMICHI
* @since 2022-12-06
*/
public interface DeptAnnualBonusMapper{
    /**
    * 查询部门年终奖表
    *
    * @param deptAnnualBonusId 部门年终奖表主键
    * @return 部门年终奖表
    */
    DeptAnnualBonusDTO selectDeptAnnualBonusByDeptAnnualBonusId(@Param("deptAnnualBonusId")Long deptAnnualBonusId);

    /**
     * 根据年份和一级部门年终奖
     *
     * @param deptAnnualBonus 部门年终奖
     * @return 部门年终奖表
     */
    DeptAnnualBonusDTO selectDeptAnnualBonusByAnnualBonusYear(@Param("deptAnnualBonus")DeptAnnualBonus deptAnnualBonus);

    /**
    * 批量查询部门年终奖表
    *
    * @param deptAnnualBonusIds 部门年终奖表主键集合
    * @return 部门年终奖表
    */
    List<DeptAnnualBonusDTO> selectDeptAnnualBonusByDeptAnnualBonusIds(@Param("deptAnnualBonusIds") List<Long> deptAnnualBonusIds);

    /**
    * 查询部门年终奖表列表
    *
    * @param deptAnnualBonus 部门年终奖表
    * @return 部门年终奖表集合
    */
    List<DeptAnnualBonusDTO> selectDeptAnnualBonusList(@Param("deptAnnualBonus")DeptAnnualBonus deptAnnualBonus);

    /**
    * 新增部门年终奖表
    *
    * @param deptAnnualBonus 部门年终奖表
    * @return 结果
    */
    int insertDeptAnnualBonus(@Param("deptAnnualBonus")DeptAnnualBonus deptAnnualBonus);

    /**
    * 修改部门年终奖表
    *
    * @param deptAnnualBonus 部门年终奖表
    * @return 结果
    */
    int updateDeptAnnualBonus(@Param("deptAnnualBonus")DeptAnnualBonus deptAnnualBonus);

    /**
    * 批量修改部门年终奖表
    *
    * @param deptAnnualBonusList 部门年终奖表
    * @return 结果
    */
    int updateDeptAnnualBonuss(@Param("deptAnnualBonusList")List<DeptAnnualBonus> deptAnnualBonusList);
    /**
    * 逻辑删除部门年终奖表
    *
    * @param deptAnnualBonus
    * @return 结果
    */
    int logicDeleteDeptAnnualBonusByDeptAnnualBonusId(@Param("deptAnnualBonus")DeptAnnualBonus deptAnnualBonus);

    /**
    * 逻辑批量删除部门年终奖表
    *
    * @param deptAnnualBonusIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteDeptAnnualBonusByDeptAnnualBonusIds(@Param("deptAnnualBonusIds")List<Long> deptAnnualBonusIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除部门年终奖表
    *
    * @param deptAnnualBonusId 部门年终奖表主键
    * @return 结果
    */
    int deleteDeptAnnualBonusByDeptAnnualBonusId(@Param("deptAnnualBonusId")Long deptAnnualBonusId);

    /**
    * 物理批量删除部门年终奖表
    *
    * @param deptAnnualBonusIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteDeptAnnualBonusByDeptAnnualBonusIds(@Param("deptAnnualBonusIds")List<Long> deptAnnualBonusIds);

    /**
    * 批量新增部门年终奖表
    *
    * @param DeptAnnualBonuss 部门年终奖表列表
    * @return 结果
    */
    int batchDeptAnnualBonus(@Param("deptAnnualBonuss")List<DeptAnnualBonus> DeptAnnualBonuss);
}
