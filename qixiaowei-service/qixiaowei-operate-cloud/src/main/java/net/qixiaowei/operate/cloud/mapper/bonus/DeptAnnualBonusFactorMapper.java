package net.qixiaowei.operate.cloud.mapper.bonus;

import java.util.List;
import net.qixiaowei.operate.cloud.api.domain.salary.DeptAnnualBonusFactor;
import net.qixiaowei.operate.cloud.api.dto.salary.DeptAnnualBonusFactorDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* DeptAnnualBonusFactorMapper接口
* @author TANGMICHI
* @since 2022-12-06
*/
public interface DeptAnnualBonusFactorMapper{
    /**
    * 查询部门年终奖系数表
    *
    * @param deptAnnualBonusFactorId 部门年终奖系数表主键
    * @return 部门年终奖系数表
    */
    DeptAnnualBonusFactorDTO selectDeptAnnualBonusFactorByDeptAnnualBonusFactorId(@Param("deptAnnualBonusFactorId")Long deptAnnualBonusFactorId);


    /**
    * 批量查询部门年终奖系数表
    *
    * @param deptAnnualBonusFactorIds 部门年终奖系数表主键集合
    * @return 部门年终奖系数表
    */
    List<DeptAnnualBonusFactorDTO> selectDeptAnnualBonusFactorByDeptAnnualBonusFactorIds(@Param("deptAnnualBonusFactorIds") List<Long> deptAnnualBonusFactorIds);

    /**
    * 查询部门年终奖系数表列表
    *
    * @param deptAnnualBonusFactor 部门年终奖系数表
    * @return 部门年终奖系数表集合
    */
    List<DeptAnnualBonusFactorDTO> selectDeptAnnualBonusFactorList(@Param("deptAnnualBonusFactor")DeptAnnualBonusFactor deptAnnualBonusFactor);

    /**
    * 新增部门年终奖系数表
    *
    * @param deptAnnualBonusFactor 部门年终奖系数表
    * @return 结果
    */
    int insertDeptAnnualBonusFactor(@Param("deptAnnualBonusFactor")DeptAnnualBonusFactor deptAnnualBonusFactor);

    /**
    * 修改部门年终奖系数表
    *
    * @param deptAnnualBonusFactor 部门年终奖系数表
    * @return 结果
    */
    int updateDeptAnnualBonusFactor(@Param("deptAnnualBonusFactor")DeptAnnualBonusFactor deptAnnualBonusFactor);

    /**
    * 批量修改部门年终奖系数表
    *
    * @param deptAnnualBonusFactorList 部门年终奖系数表
    * @return 结果
    */
    int updateDeptAnnualBonusFactors(@Param("deptAnnualBonusFactorList")List<DeptAnnualBonusFactor> deptAnnualBonusFactorList);
    /**
    * 逻辑删除部门年终奖系数表
    *
    * @param deptAnnualBonusFactor
    * @return 结果
    */
    int logicDeleteDeptAnnualBonusFactorByDeptAnnualBonusFactorId(@Param("deptAnnualBonusFactor")DeptAnnualBonusFactor deptAnnualBonusFactor);

    /**
    * 逻辑批量删除部门年终奖系数表
    *
    * @param deptAnnualBonusFactorIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteDeptAnnualBonusFactorByDeptAnnualBonusFactorIds(@Param("deptAnnualBonusFactorIds")List<Long> deptAnnualBonusFactorIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除部门年终奖系数表
    *
    * @param deptAnnualBonusFactorId 部门年终奖系数表主键
    * @return 结果
    */
    int deleteDeptAnnualBonusFactorByDeptAnnualBonusFactorId(@Param("deptAnnualBonusFactorId")Long deptAnnualBonusFactorId);

    /**
    * 物理批量删除部门年终奖系数表
    *
    * @param deptAnnualBonusFactorIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteDeptAnnualBonusFactorByDeptAnnualBonusFactorIds(@Param("deptAnnualBonusFactorIds")List<Long> deptAnnualBonusFactorIds);

    /**
    * 批量新增部门年终奖系数表
    *
    * @param DeptAnnualBonusFactors 部门年终奖系数表列表
    * @return 结果
    */
    int batchDeptAnnualBonusFactor(@Param("deptAnnualBonusFactors")List<DeptAnnualBonusFactor> DeptAnnualBonusFactors);
}
