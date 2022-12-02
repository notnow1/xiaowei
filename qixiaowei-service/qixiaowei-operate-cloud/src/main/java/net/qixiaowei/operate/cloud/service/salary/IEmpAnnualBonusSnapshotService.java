package net.qixiaowei.operate.cloud.service.salary;

import java.util.List;
import net.qixiaowei.operate.cloud.api.dto.salary.EmpAnnualBonusSnapshotDTO;



/**
* EmpAnnualBonusSnapshotService接口
* @author TANGMICHI
* @since 2022-12-02
*/
public interface IEmpAnnualBonusSnapshotService{
    /**
    * 查询个人年终奖发放快照信息表
    *
    * @param empAnnualBonusSnapshotId 个人年终奖发放快照信息表主键
    * @return 个人年终奖发放快照信息表
    */
    EmpAnnualBonusSnapshotDTO selectEmpAnnualBonusSnapshotByEmpAnnualBonusSnapshotId(Long empAnnualBonusSnapshotId);

    /**
    * 查询个人年终奖发放快照信息表列表
    *
    * @param empAnnualBonusSnapshotDTO 个人年终奖发放快照信息表
    * @return 个人年终奖发放快照信息表集合
    */
    List<EmpAnnualBonusSnapshotDTO> selectEmpAnnualBonusSnapshotList(EmpAnnualBonusSnapshotDTO empAnnualBonusSnapshotDTO);

    /**
    * 新增个人年终奖发放快照信息表
    *
    * @param empAnnualBonusSnapshotDTO 个人年终奖发放快照信息表
    * @return 结果
    */
    EmpAnnualBonusSnapshotDTO insertEmpAnnualBonusSnapshot(EmpAnnualBonusSnapshotDTO empAnnualBonusSnapshotDTO);

    /**
    * 修改个人年终奖发放快照信息表
    *
    * @param empAnnualBonusSnapshotDTO 个人年终奖发放快照信息表
    * @return 结果
    */
    int updateEmpAnnualBonusSnapshot(EmpAnnualBonusSnapshotDTO empAnnualBonusSnapshotDTO);

    /**
    * 批量修改个人年终奖发放快照信息表
    *
    * @param empAnnualBonusSnapshotDtos 个人年终奖发放快照信息表
    * @return 结果
    */
    int updateEmpAnnualBonusSnapshots(List<EmpAnnualBonusSnapshotDTO> empAnnualBonusSnapshotDtos);

    /**
    * 批量新增个人年终奖发放快照信息表
    *
    * @param empAnnualBonusSnapshotDtos 个人年终奖发放快照信息表
    * @return 结果
    */
    int insertEmpAnnualBonusSnapshots(List<EmpAnnualBonusSnapshotDTO> empAnnualBonusSnapshotDtos);

    /**
    * 逻辑批量删除个人年终奖发放快照信息表
    *
    * @param empAnnualBonusSnapshotIds 需要删除的个人年终奖发放快照信息表集合
    * @return 结果
    */
    int logicDeleteEmpAnnualBonusSnapshotByEmpAnnualBonusSnapshotIds(List<Long> empAnnualBonusSnapshotIds);

    /**
    * 逻辑删除个人年终奖发放快照信息表信息
    *
    * @param empAnnualBonusSnapshotDTO
    * @return 结果
    */
    int logicDeleteEmpAnnualBonusSnapshotByEmpAnnualBonusSnapshotId(EmpAnnualBonusSnapshotDTO empAnnualBonusSnapshotDTO);
    /**
    * 批量删除个人年终奖发放快照信息表
    *
    * @param EmpAnnualBonusSnapshotDtos
    * @return 结果
    */
    int deleteEmpAnnualBonusSnapshotByEmpAnnualBonusSnapshotIds(List<EmpAnnualBonusSnapshotDTO> EmpAnnualBonusSnapshotDtos);

    /**
    * 逻辑删除个人年终奖发放快照信息表信息
    *
    * @param empAnnualBonusSnapshotDTO
    * @return 结果
    */
    int deleteEmpAnnualBonusSnapshotByEmpAnnualBonusSnapshotId(EmpAnnualBonusSnapshotDTO empAnnualBonusSnapshotDTO);


    /**
    * 删除个人年终奖发放快照信息表信息
    *
    * @param empAnnualBonusSnapshotId 个人年终奖发放快照信息表主键
    * @return 结果
    */
    int deleteEmpAnnualBonusSnapshotByEmpAnnualBonusSnapshotId(Long empAnnualBonusSnapshotId);

}
