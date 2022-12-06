package net.qixiaowei.operate.cloud.service.impl.salary;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.salary.DeptAnnualBonus;
import net.qixiaowei.operate.cloud.api.dto.salary.DeptAnnualBonusDTO;
import net.qixiaowei.operate.cloud.mapper.salary.DeptAnnualBonusMapper;
import net.qixiaowei.operate.cloud.service.salary.IDeptAnnualBonusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
* DeptAnnualBonusService业务层处理
* @author TANGMICHI
* @since 2022-12-06
*/
@Service
public class DeptAnnualBonusServiceImpl implements IDeptAnnualBonusService{
    @Autowired
    private DeptAnnualBonusMapper deptAnnualBonusMapper;

    /**
    * 查询部门年终奖表
    *
    * @param deptAnnualBonusId 部门年终奖表主键
    * @return 部门年终奖表
    */
    @Override
    public DeptAnnualBonusDTO selectDeptAnnualBonusByDeptAnnualBonusId(Long deptAnnualBonusId)
    {
    return deptAnnualBonusMapper.selectDeptAnnualBonusByDeptAnnualBonusId(deptAnnualBonusId);
    }

    /**
    * 查询部门年终奖表列表
    *
    * @param deptAnnualBonusDTO 部门年终奖表
    * @return 部门年终奖表
    */
    @Override
    public List<DeptAnnualBonusDTO> selectDeptAnnualBonusList(DeptAnnualBonusDTO deptAnnualBonusDTO)
    {
    DeptAnnualBonus deptAnnualBonus=new DeptAnnualBonus();
    BeanUtils.copyProperties(deptAnnualBonusDTO,deptAnnualBonus);
    return deptAnnualBonusMapper.selectDeptAnnualBonusList(deptAnnualBonus);
    }

    /**
    * 新增部门年终奖表
    *
    * @param deptAnnualBonusDTO 部门年终奖表
    * @return 结果
    */
    @Override
    public DeptAnnualBonusDTO insertDeptAnnualBonus(DeptAnnualBonusDTO deptAnnualBonusDTO){
    DeptAnnualBonus deptAnnualBonus=new DeptAnnualBonus();
    BeanUtils.copyProperties(deptAnnualBonusDTO,deptAnnualBonus);
    deptAnnualBonus.setCreateBy(SecurityUtils.getUserId());
    deptAnnualBonus.setCreateTime(DateUtils.getNowDate());
    deptAnnualBonus.setUpdateTime(DateUtils.getNowDate());
    deptAnnualBonus.setUpdateBy(SecurityUtils.getUserId());
    deptAnnualBonus.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
    deptAnnualBonusMapper.insertDeptAnnualBonus(deptAnnualBonus);
    deptAnnualBonusDTO.setDeptAnnualBonusId(deptAnnualBonus.getDeptAnnualBonusId());
    return deptAnnualBonusDTO;
    }

    /**
    * 修改部门年终奖表
    *
    * @param deptAnnualBonusDTO 部门年终奖表
    * @return 结果
    */
    @Override
    public int updateDeptAnnualBonus(DeptAnnualBonusDTO deptAnnualBonusDTO)
    {
    DeptAnnualBonus deptAnnualBonus=new DeptAnnualBonus();
    BeanUtils.copyProperties(deptAnnualBonusDTO,deptAnnualBonus);
    deptAnnualBonus.setUpdateTime(DateUtils.getNowDate());
    deptAnnualBonus.setUpdateBy(SecurityUtils.getUserId());
    return deptAnnualBonusMapper.updateDeptAnnualBonus(deptAnnualBonus);
    }

    /**
    * 逻辑批量删除部门年终奖表
    *
    * @param deptAnnualBonusIds 主键集合
    * @return 结果
    */
    @Override
    public int logicDeleteDeptAnnualBonusByDeptAnnualBonusIds(List<Long> deptAnnualBonusIds){
    return deptAnnualBonusMapper.logicDeleteDeptAnnualBonusByDeptAnnualBonusIds(deptAnnualBonusIds,SecurityUtils.getUserId(),DateUtils.getNowDate());
    }

    /**
    * 物理删除部门年终奖表信息
    *
    * @param deptAnnualBonusId 部门年终奖表主键
    * @return 结果
    */
    @Override
    public int deleteDeptAnnualBonusByDeptAnnualBonusId(Long deptAnnualBonusId)
    {
    return deptAnnualBonusMapper.deleteDeptAnnualBonusByDeptAnnualBonusId(deptAnnualBonusId);
    }

    /**
     * 部门年终奖预制数据
     * @param annualBonusYear
     * @return
     */
    @Override
    public DeptAnnualBonusDTO addPrefabricate(int annualBonusYear) {
        return null;
    }

    /**
     * 逻辑删除部门年终奖表信息
     *
     * @param  deptAnnualBonusDTO 部门年终奖表
     * @return 结果
     */
     @Override
     public int logicDeleteDeptAnnualBonusByDeptAnnualBonusId(DeptAnnualBonusDTO deptAnnualBonusDTO)
     {
     DeptAnnualBonus deptAnnualBonus=new DeptAnnualBonus();
     deptAnnualBonus.setDeptAnnualBonusId(deptAnnualBonusDTO.getDeptAnnualBonusId());
     deptAnnualBonus.setUpdateTime(DateUtils.getNowDate());
     deptAnnualBonus.setUpdateBy(SecurityUtils.getUserId());
     return deptAnnualBonusMapper.logicDeleteDeptAnnualBonusByDeptAnnualBonusId(deptAnnualBonus);
     }

     /**
     * 物理删除部门年终奖表信息
     *
     * @param  deptAnnualBonusDTO 部门年终奖表
     * @return 结果
     */
     
     @Override
     public int deleteDeptAnnualBonusByDeptAnnualBonusId(DeptAnnualBonusDTO deptAnnualBonusDTO)
     {
     DeptAnnualBonus deptAnnualBonus=new DeptAnnualBonus();
     BeanUtils.copyProperties(deptAnnualBonusDTO,deptAnnualBonus);
     return deptAnnualBonusMapper.deleteDeptAnnualBonusByDeptAnnualBonusId(deptAnnualBonus.getDeptAnnualBonusId());
     }
     /**
     * 物理批量删除部门年终奖表
     *
     * @param deptAnnualBonusDtos 需要删除的部门年终奖表主键
     * @return 结果
     */
     
     @Override
     public int deleteDeptAnnualBonusByDeptAnnualBonusIds(List<DeptAnnualBonusDTO> deptAnnualBonusDtos){
     List<Long> stringList = new ArrayList();
     for (DeptAnnualBonusDTO deptAnnualBonusDTO : deptAnnualBonusDtos) {
     stringList.add(deptAnnualBonusDTO.getDeptAnnualBonusId());
     }
     return deptAnnualBonusMapper.deleteDeptAnnualBonusByDeptAnnualBonusIds(stringList);
     }

    /**
    * 批量新增部门年终奖表信息
    *
    * @param deptAnnualBonusDtos 部门年终奖表对象
    */
    
    public int insertDeptAnnualBonuss(List<DeptAnnualBonusDTO> deptAnnualBonusDtos){
      List<DeptAnnualBonus> deptAnnualBonusList = new ArrayList();

    for (DeptAnnualBonusDTO deptAnnualBonusDTO : deptAnnualBonusDtos) {
      DeptAnnualBonus deptAnnualBonus =new DeptAnnualBonus();
      BeanUtils.copyProperties(deptAnnualBonusDTO,deptAnnualBonus);
       deptAnnualBonus.setCreateBy(SecurityUtils.getUserId());
       deptAnnualBonus.setCreateTime(DateUtils.getNowDate());
       deptAnnualBonus.setUpdateTime(DateUtils.getNowDate());
       deptAnnualBonus.setUpdateBy(SecurityUtils.getUserId());
       deptAnnualBonus.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
      deptAnnualBonusList.add(deptAnnualBonus);
    }
    return deptAnnualBonusMapper.batchDeptAnnualBonus(deptAnnualBonusList);
    }

    /**
    * 批量修改部门年终奖表信息
    *
    * @param deptAnnualBonusDtos 部门年终奖表对象
    */
    
    public int updateDeptAnnualBonuss(List<DeptAnnualBonusDTO> deptAnnualBonusDtos){
     List<DeptAnnualBonus> deptAnnualBonusList = new ArrayList();

     for (DeptAnnualBonusDTO deptAnnualBonusDTO : deptAnnualBonusDtos) {
     DeptAnnualBonus deptAnnualBonus =new DeptAnnualBonus();
     BeanUtils.copyProperties(deptAnnualBonusDTO,deptAnnualBonus);
        deptAnnualBonus.setCreateBy(SecurityUtils.getUserId());
        deptAnnualBonus.setCreateTime(DateUtils.getNowDate());
        deptAnnualBonus.setUpdateTime(DateUtils.getNowDate());
        deptAnnualBonus.setUpdateBy(SecurityUtils.getUserId());
     deptAnnualBonusList.add(deptAnnualBonus);
     }
     return deptAnnualBonusMapper.updateDeptAnnualBonuss(deptAnnualBonusList);
    }
}

