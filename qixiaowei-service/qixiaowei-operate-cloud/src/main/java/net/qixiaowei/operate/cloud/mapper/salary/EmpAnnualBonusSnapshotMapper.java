package net.qixiaowei.operate.cloud.mapper.salary;

import java.util.List;
import net.qixiaowei.operate.cloud.api.domain.salary.EmpAnnualBonusSnapshot;
import net.qixiaowei.operate.cloud.api.dto.salary.EmpAnnualBonusSnapshotDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* EmpAnnualBonusSnapshotMapper接口
* @author TANGMICHI
* @since 2022-12-02
*/
public interface EmpAnnualBonusSnapshotMapper{
    /**
    * 查询个人年终奖发放快照信息表
    *
    * @param empAnnualBonusSnapshotId 个人年终奖发放快照信息表主键
    * @return 个人年终奖发放快照信息表
    */
    EmpAnnualBonusSnapshotDTO selectEmpAnnualBonusSnapshotByEmpAnnualBonusSnapshotId(@Param("empAnnualBonusSnapshotId")Long empAnnualBonusSnapshotId);


    /**
    * 批量查询个人年终奖发放快照信息表
    *
    * @param empAnnualBonusSnapshotIds 个人年终奖发放快照信息表主键集合
    * @return 个人年终奖发放快照信息表
    */
    List<EmpAnnualBonusSnapshotDTO> selectEmpAnnualBonusSnapshotByEmpAnnualBonusSnapshotIds(@Param("empAnnualBonusSnapshotIds") List<Long> empAnnualBonusSnapshotIds);

    /**
    * 查询个人年终奖发放快照信息表列表
    *
    * @param empAnnualBonusSnapshot 个人年终奖发放快照信息表
    * @return 个人年终奖发放快照信息表集合
    */
    List<EmpAnnualBonusSnapshotDTO> selectEmpAnnualBonusSnapshotList(@Param("empAnnualBonusSnapshot")EmpAnnualBonusSnapshot empAnnualBonusSnapshot);

    /**
    * 新增个人年终奖发放快照信息表
    *
    * @param empAnnualBonusSnapshot 个人年终奖发放快照信息表
    * @return 结果
    */
    int insertEmpAnnualBonusSnapshot(@Param("empAnnualBonusSnapshot")EmpAnnualBonusSnapshot empAnnualBonusSnapshot);

    /**
    * 修改个人年终奖发放快照信息表
    *
    * @param empAnnualBonusSnapshot 个人年终奖发放快照信息表
    * @return 结果
    */
    int updateEmpAnnualBonusSnapshot(@Param("empAnnualBonusSnapshot")EmpAnnualBonusSnapshot empAnnualBonusSnapshot);

    /**
    * 批量修改个人年终奖发放快照信息表
    *
    * @param empAnnualBonusSnapshotList 个人年终奖发放快照信息表
    * @return 结果
    */
    int updateEmpAnnualBonusSnapshots(@Param("empAnnualBonusSnapshotList")List<EmpAnnualBonusSnapshot> empAnnualBonusSnapshotList);
    /**
    * 逻辑删除个人年终奖发放快照信息表
    *
    * @param empAnnualBonusSnapshot
    * @return 结果
    */
    int logicDeleteEmpAnnualBonusSnapshotByEmpAnnualBonusSnapshotId(@Param("empAnnualBonusSnapshot")EmpAnnualBonusSnapshot empAnnualBonusSnapshot);

    /**
    * 逻辑批量删除个人年终奖发放快照信息表
    *
    * @param empAnnualBonusSnapshotIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteEmpAnnualBonusSnapshotByEmpAnnualBonusSnapshotIds(@Param("empAnnualBonusSnapshotIds")List<Long> empAnnualBonusSnapshotIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除个人年终奖发放快照信息表
    *
    * @param empAnnualBonusSnapshotId 个人年终奖发放快照信息表主键
    * @return 结果
    */
    int deleteEmpAnnualBonusSnapshotByEmpAnnualBonusSnapshotId(@Param("empAnnualBonusSnapshotId")Long empAnnualBonusSnapshotId);

    /**
    * 物理批量删除个人年终奖发放快照信息表
    *
    * @param empAnnualBonusSnapshotIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteEmpAnnualBonusSnapshotByEmpAnnualBonusSnapshotIds(@Param("empAnnualBonusSnapshotIds")List<Long> empAnnualBonusSnapshotIds);

    /**
    * 批量新增个人年终奖发放快照信息表
    *
    * @param EmpAnnualBonusSnapshots 个人年终奖发放快照信息表列表
    * @return 结果
    */
    int batchEmpAnnualBonusSnapshot(@Param("empAnnualBonusSnapshots")List<EmpAnnualBonusSnapshot> EmpAnnualBonusSnapshots);
}
