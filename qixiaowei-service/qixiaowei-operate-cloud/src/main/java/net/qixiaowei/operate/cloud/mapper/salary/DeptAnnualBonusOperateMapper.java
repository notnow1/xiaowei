package net.qixiaowei.operate.cloud.mapper.salary;

import java.util.List;
import net.qixiaowei.operate.cloud.api.domain.salary.DeptAnnualBonusOperate;
import net.qixiaowei.operate.cloud.api.dto.salary.DeptAnnualBonusOperateDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* DeptAnnualBonusOperateMapper接口
* @author TANGMICHI
* @since 2022-12-06
*/
public interface DeptAnnualBonusOperateMapper{
    /**
    * 查询部门年终奖经营绩效结果表
    *
    * @param deptAnnualBonusOperateId 部门年终奖经营绩效结果表主键
    * @return 部门年终奖经营绩效结果表
    */
    DeptAnnualBonusOperateDTO selectDeptAnnualBonusOperateByDeptAnnualBonusOperateId(@Param("deptAnnualBonusOperateId")Long deptAnnualBonusOperateId);


    /**
    * 批量查询部门年终奖经营绩效结果表
    *
    * @param deptAnnualBonusOperateIds 部门年终奖经营绩效结果表主键集合
    * @return 部门年终奖经营绩效结果表
    */
    List<DeptAnnualBonusOperateDTO> selectDeptAnnualBonusOperateByDeptAnnualBonusOperateIds(@Param("deptAnnualBonusOperateIds") List<Long> deptAnnualBonusOperateIds);

    /**
    * 查询部门年终奖经营绩效结果表列表
    *
    * @param deptAnnualBonusOperate 部门年终奖经营绩效结果表
    * @return 部门年终奖经营绩效结果表集合
    */
    List<DeptAnnualBonusOperateDTO> selectDeptAnnualBonusOperateList(@Param("deptAnnualBonusOperate")DeptAnnualBonusOperate deptAnnualBonusOperate);

    /**
    * 新增部门年终奖经营绩效结果表
    *
    * @param deptAnnualBonusOperate 部门年终奖经营绩效结果表
    * @return 结果
    */
    int insertDeptAnnualBonusOperate(@Param("deptAnnualBonusOperate")DeptAnnualBonusOperate deptAnnualBonusOperate);

    /**
    * 修改部门年终奖经营绩效结果表
    *
    * @param deptAnnualBonusOperate 部门年终奖经营绩效结果表
    * @return 结果
    */
    int updateDeptAnnualBonusOperate(@Param("deptAnnualBonusOperate")DeptAnnualBonusOperate deptAnnualBonusOperate);

    /**
    * 批量修改部门年终奖经营绩效结果表
    *
    * @param deptAnnualBonusOperateList 部门年终奖经营绩效结果表
    * @return 结果
    */
    int updateDeptAnnualBonusOperates(@Param("deptAnnualBonusOperateList")List<DeptAnnualBonusOperate> deptAnnualBonusOperateList);
    /**
    * 逻辑删除部门年终奖经营绩效结果表
    *
    * @param deptAnnualBonusOperate
    * @return 结果
    */
    int logicDeleteDeptAnnualBonusOperateByDeptAnnualBonusOperateId(@Param("deptAnnualBonusOperate")DeptAnnualBonusOperate deptAnnualBonusOperate);

    /**
    * 逻辑批量删除部门年终奖经营绩效结果表
    *
    * @param deptAnnualBonusOperateIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteDeptAnnualBonusOperateByDeptAnnualBonusOperateIds(@Param("deptAnnualBonusOperateIds")List<Long> deptAnnualBonusOperateIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除部门年终奖经营绩效结果表
    *
    * @param deptAnnualBonusOperateId 部门年终奖经营绩效结果表主键
    * @return 结果
    */
    int deleteDeptAnnualBonusOperateByDeptAnnualBonusOperateId(@Param("deptAnnualBonusOperateId")Long deptAnnualBonusOperateId);

    /**
    * 物理批量删除部门年终奖经营绩效结果表
    *
    * @param deptAnnualBonusOperateIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteDeptAnnualBonusOperateByDeptAnnualBonusOperateIds(@Param("deptAnnualBonusOperateIds")List<Long> deptAnnualBonusOperateIds);

    /**
    * 批量新增部门年终奖经营绩效结果表
    *
    * @param DeptAnnualBonusOperates 部门年终奖经营绩效结果表列表
    * @return 结果
    */
    int batchDeptAnnualBonusOperate(@Param("deptAnnualBonusOperates")List<DeptAnnualBonusOperate> DeptAnnualBonusOperates);
}
