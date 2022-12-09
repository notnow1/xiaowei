package net.qixiaowei.operate.cloud.service.impl.bonus;

import java.util.List;
import net.qixiaowei.integration.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.bonus.EmpAnnualBonusSnapshot;

import net.qixiaowei.operate.cloud.api.dto.bonus.EmpAnnualBonusSnapshotDTO;
import net.qixiaowei.operate.cloud.mapper.bonus.EmpAnnualBonusSnapshotMapper;
import net.qixiaowei.operate.cloud.service.bonus.IEmpAnnualBonusSnapshotService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


/**
* EmpAnnualBonusSnapshotService业务层处理
* @author TANGMICHI
* @since 2022-12-02
*/
@Service
public class EmpAnnualBonusSnapshotServiceImpl implements IEmpAnnualBonusSnapshotService{
    @Autowired
    private EmpAnnualBonusSnapshotMapper empAnnualBonusSnapshotMapper;

    /**
    * 查询个人年终奖发放快照信息表
    *
    * @param empAnnualBonusSnapshotId 个人年终奖发放快照信息表主键
    * @return 个人年终奖发放快照信息表
    */
    @Override
    public EmpAnnualBonusSnapshotDTO selectEmpAnnualBonusSnapshotByEmpAnnualBonusSnapshotId(Long empAnnualBonusSnapshotId)
    {
    return empAnnualBonusSnapshotMapper.selectEmpAnnualBonusSnapshotByEmpAnnualBonusSnapshotId(empAnnualBonusSnapshotId);
    }

    /**
    * 查询个人年终奖发放快照信息表列表
    *
    * @param empAnnualBonusSnapshotDTO 个人年终奖发放快照信息表
    * @return 个人年终奖发放快照信息表
    */
    @Override
    public List<EmpAnnualBonusSnapshotDTO> selectEmpAnnualBonusSnapshotList(EmpAnnualBonusSnapshotDTO empAnnualBonusSnapshotDTO)
    {
    EmpAnnualBonusSnapshot empAnnualBonusSnapshot=new EmpAnnualBonusSnapshot();
    BeanUtils.copyProperties(empAnnualBonusSnapshotDTO,empAnnualBonusSnapshot);
    return empAnnualBonusSnapshotMapper.selectEmpAnnualBonusSnapshotList(empAnnualBonusSnapshot);
    }

    /**
    * 新增个人年终奖发放快照信息表
    *
    * @param empAnnualBonusSnapshotDTO 个人年终奖发放快照信息表
    * @return 结果
    */
    @Override
    public EmpAnnualBonusSnapshotDTO insertEmpAnnualBonusSnapshot(EmpAnnualBonusSnapshotDTO empAnnualBonusSnapshotDTO){
    EmpAnnualBonusSnapshot empAnnualBonusSnapshot=new EmpAnnualBonusSnapshot();
    BeanUtils.copyProperties(empAnnualBonusSnapshotDTO,empAnnualBonusSnapshot);
    empAnnualBonusSnapshot.setCreateBy(SecurityUtils.getUserId());
    empAnnualBonusSnapshot.setCreateTime(DateUtils.getNowDate());
    empAnnualBonusSnapshot.setUpdateTime(DateUtils.getNowDate());
    empAnnualBonusSnapshot.setUpdateBy(SecurityUtils.getUserId());
    empAnnualBonusSnapshot.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
    empAnnualBonusSnapshotMapper.insertEmpAnnualBonusSnapshot(empAnnualBonusSnapshot);
    empAnnualBonusSnapshotDTO.setEmpAnnualBonusSnapshotId(empAnnualBonusSnapshot.getEmpAnnualBonusSnapshotId());
    return empAnnualBonusSnapshotDTO;
    }

    /**
    * 修改个人年终奖发放快照信息表
    *
    * @param empAnnualBonusSnapshotDTO 个人年终奖发放快照信息表
    * @return 结果
    */
    @Override
    public int updateEmpAnnualBonusSnapshot(EmpAnnualBonusSnapshotDTO empAnnualBonusSnapshotDTO)
    {
    EmpAnnualBonusSnapshot empAnnualBonusSnapshot=new EmpAnnualBonusSnapshot();
    BeanUtils.copyProperties(empAnnualBonusSnapshotDTO,empAnnualBonusSnapshot);
    empAnnualBonusSnapshot.setUpdateTime(DateUtils.getNowDate());
    empAnnualBonusSnapshot.setUpdateBy(SecurityUtils.getUserId());
    return empAnnualBonusSnapshotMapper.updateEmpAnnualBonusSnapshot(empAnnualBonusSnapshot);
    }

    /**
    * 逻辑批量删除个人年终奖发放快照信息表
    *
    * @param empAnnualBonusSnapshotIds 主键集合
    * @return 结果
    */
    @Override
    public int logicDeleteEmpAnnualBonusSnapshotByEmpAnnualBonusSnapshotIds(List<Long> empAnnualBonusSnapshotIds){
    return empAnnualBonusSnapshotMapper.logicDeleteEmpAnnualBonusSnapshotByEmpAnnualBonusSnapshotIds(empAnnualBonusSnapshotIds,SecurityUtils.getUserId(),DateUtils.getNowDate());
    }

    /**
    * 物理删除个人年终奖发放快照信息表信息
    *
    * @param empAnnualBonusSnapshotId 个人年终奖发放快照信息表主键
    * @return 结果
    */
    @Override
    public int deleteEmpAnnualBonusSnapshotByEmpAnnualBonusSnapshotId(Long empAnnualBonusSnapshotId)
    {
    return empAnnualBonusSnapshotMapper.deleteEmpAnnualBonusSnapshotByEmpAnnualBonusSnapshotId(empAnnualBonusSnapshotId);
    }

     /**
     * 逻辑删除个人年终奖发放快照信息表信息
     *
     * @param  empAnnualBonusSnapshotDTO 个人年终奖发放快照信息表
     * @return 结果
     */
     @Override
     public int logicDeleteEmpAnnualBonusSnapshotByEmpAnnualBonusSnapshotId(EmpAnnualBonusSnapshotDTO empAnnualBonusSnapshotDTO)
     {
     EmpAnnualBonusSnapshot empAnnualBonusSnapshot=new EmpAnnualBonusSnapshot();
     empAnnualBonusSnapshot.setEmpAnnualBonusSnapshotId(empAnnualBonusSnapshotDTO.getEmpAnnualBonusSnapshotId());
     empAnnualBonusSnapshot.setUpdateTime(DateUtils.getNowDate());
     empAnnualBonusSnapshot.setUpdateBy(SecurityUtils.getUserId());
     return empAnnualBonusSnapshotMapper.logicDeleteEmpAnnualBonusSnapshotByEmpAnnualBonusSnapshotId(empAnnualBonusSnapshot);
     }

     /**
     * 物理删除个人年终奖发放快照信息表信息
     *
     * @param  empAnnualBonusSnapshotDTO 个人年终奖发放快照信息表
     * @return 结果
     */
     
     @Override
     public int deleteEmpAnnualBonusSnapshotByEmpAnnualBonusSnapshotId(EmpAnnualBonusSnapshotDTO empAnnualBonusSnapshotDTO)
     {
     EmpAnnualBonusSnapshot empAnnualBonusSnapshot=new EmpAnnualBonusSnapshot();
     BeanUtils.copyProperties(empAnnualBonusSnapshotDTO,empAnnualBonusSnapshot);
     return empAnnualBonusSnapshotMapper.deleteEmpAnnualBonusSnapshotByEmpAnnualBonusSnapshotId(empAnnualBonusSnapshot.getEmpAnnualBonusSnapshotId());
     }
     /**
     * 物理批量删除个人年终奖发放快照信息表
     *
     * @param empAnnualBonusSnapshotDtos 需要删除的个人年终奖发放快照信息表主键
     * @return 结果
     */
     
     @Override
     public int deleteEmpAnnualBonusSnapshotByEmpAnnualBonusSnapshotIds(List<EmpAnnualBonusSnapshotDTO> empAnnualBonusSnapshotDtos){
     List<Long> stringList = new ArrayList();
     for (EmpAnnualBonusSnapshotDTO empAnnualBonusSnapshotDTO : empAnnualBonusSnapshotDtos) {
     stringList.add(empAnnualBonusSnapshotDTO.getEmpAnnualBonusSnapshotId());
     }
     return empAnnualBonusSnapshotMapper.deleteEmpAnnualBonusSnapshotByEmpAnnualBonusSnapshotIds(stringList);
     }

    /**
    * 批量新增个人年终奖发放快照信息表信息
    *
    * @param empAnnualBonusSnapshotDtos 个人年终奖发放快照信息表对象
    */
    
    public int insertEmpAnnualBonusSnapshots(List<EmpAnnualBonusSnapshotDTO> empAnnualBonusSnapshotDtos){
      List<EmpAnnualBonusSnapshot> empAnnualBonusSnapshotList = new ArrayList();

    for (EmpAnnualBonusSnapshotDTO empAnnualBonusSnapshotDTO : empAnnualBonusSnapshotDtos) {
      EmpAnnualBonusSnapshot empAnnualBonusSnapshot =new EmpAnnualBonusSnapshot();
      BeanUtils.copyProperties(empAnnualBonusSnapshotDTO,empAnnualBonusSnapshot);
       empAnnualBonusSnapshot.setCreateBy(SecurityUtils.getUserId());
       empAnnualBonusSnapshot.setCreateTime(DateUtils.getNowDate());
       empAnnualBonusSnapshot.setUpdateTime(DateUtils.getNowDate());
       empAnnualBonusSnapshot.setUpdateBy(SecurityUtils.getUserId());
       empAnnualBonusSnapshot.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
      empAnnualBonusSnapshotList.add(empAnnualBonusSnapshot);
    }
    return empAnnualBonusSnapshotMapper.batchEmpAnnualBonusSnapshot(empAnnualBonusSnapshotList);
    }

    /**
    * 批量修改个人年终奖发放快照信息表信息
    *
    * @param empAnnualBonusSnapshotDtos 个人年终奖发放快照信息表对象
    */
    
    public int updateEmpAnnualBonusSnapshots(List<EmpAnnualBonusSnapshotDTO> empAnnualBonusSnapshotDtos){
     List<EmpAnnualBonusSnapshot> empAnnualBonusSnapshotList = new ArrayList();

     for (EmpAnnualBonusSnapshotDTO empAnnualBonusSnapshotDTO : empAnnualBonusSnapshotDtos) {
     EmpAnnualBonusSnapshot empAnnualBonusSnapshot =new EmpAnnualBonusSnapshot();
     BeanUtils.copyProperties(empAnnualBonusSnapshotDTO,empAnnualBonusSnapshot);
        empAnnualBonusSnapshot.setCreateBy(SecurityUtils.getUserId());
        empAnnualBonusSnapshot.setCreateTime(DateUtils.getNowDate());
        empAnnualBonusSnapshot.setUpdateTime(DateUtils.getNowDate());
        empAnnualBonusSnapshot.setUpdateBy(SecurityUtils.getUserId());
     empAnnualBonusSnapshotList.add(empAnnualBonusSnapshot);
     }
     return empAnnualBonusSnapshotMapper.updateEmpAnnualBonusSnapshots(empAnnualBonusSnapshotList);
    }

}

