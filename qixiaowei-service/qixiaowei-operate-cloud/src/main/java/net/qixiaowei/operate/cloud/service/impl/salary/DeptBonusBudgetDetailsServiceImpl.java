package net.qixiaowei.operate.cloud.service.impl.salary;

import java.util.List;
import net.qixiaowei.integration.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.salary.DeptBonusBudgetDetails;
import net.qixiaowei.operate.cloud.api.dto.salary.DeptBonusBudgetDetailsDTO;
import net.qixiaowei.operate.cloud.mapper.salary.DeptBonusBudgetDetailsMapper;
import net.qixiaowei.operate.cloud.service.salary.IDeptBonusBudgetDetailsService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;


/**
* DeptBonusBudgetDetailsService业务层处理
* @author TANGMICHI
* @since 2022-11-29
*/
@Service
public class DeptBonusBudgetDetailsServiceImpl implements IDeptBonusBudgetDetailsService{
    @Autowired
    private DeptBonusBudgetDetailsMapper deptBonusBudgetDetailsMapper;

    /**
    * 查询部门奖金预算明细表
    *
    * @param deptBonusBudgetDetailsId 部门奖金预算明细表主键
    * @return 部门奖金预算明细表
    */
    @Override
    public DeptBonusBudgetDetailsDTO selectDeptBonusBudgetDetailsByDeptBonusBudgetDetailsId(Long deptBonusBudgetDetailsId)
    {
    return deptBonusBudgetDetailsMapper.selectDeptBonusBudgetDetailsByDeptBonusBudgetDetailsId(deptBonusBudgetDetailsId);
    }

    /**
    * 查询部门奖金预算明细表列表
    *
    * @param deptBonusBudgetDetailsDTO 部门奖金预算明细表
    * @return 部门奖金预算明细表
    */
    @Override
    public List<DeptBonusBudgetDetailsDTO> selectDeptBonusBudgetDetailsList(DeptBonusBudgetDetailsDTO deptBonusBudgetDetailsDTO)
    {
    DeptBonusBudgetDetails deptBonusBudgetDetails=new DeptBonusBudgetDetails();
    BeanUtils.copyProperties(deptBonusBudgetDetailsDTO,deptBonusBudgetDetails);
    return deptBonusBudgetDetailsMapper.selectDeptBonusBudgetDetailsList(deptBonusBudgetDetails);
    }

    /**
    * 新增部门奖金预算明细表
    *
    * @param deptBonusBudgetDetailsDTO 部门奖金预算明细表
    * @return 结果
    */
    @Override
    public DeptBonusBudgetDetailsDTO insertDeptBonusBudgetDetails(DeptBonusBudgetDetailsDTO deptBonusBudgetDetailsDTO){
    DeptBonusBudgetDetails deptBonusBudgetDetails=new DeptBonusBudgetDetails();
    BeanUtils.copyProperties(deptBonusBudgetDetailsDTO,deptBonusBudgetDetails);
    deptBonusBudgetDetails.setCreateBy(SecurityUtils.getUserId());
    deptBonusBudgetDetails.setCreateTime(DateUtils.getNowDate());
    deptBonusBudgetDetails.setUpdateTime(DateUtils.getNowDate());
    deptBonusBudgetDetails.setUpdateBy(SecurityUtils.getUserId());
    deptBonusBudgetDetails.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
    deptBonusBudgetDetailsMapper.insertDeptBonusBudgetDetails(deptBonusBudgetDetails);
    deptBonusBudgetDetailsDTO.setDeptBonusBudgetDetailsId(deptBonusBudgetDetails.getDeptBonusBudgetDetailsId());
    return deptBonusBudgetDetailsDTO;
    }

    /**
    * 修改部门奖金预算明细表
    *
    * @param deptBonusBudgetDetailsDTO 部门奖金预算明细表
    * @return 结果
    */
    @Override
    public int updateDeptBonusBudgetDetails(DeptBonusBudgetDetailsDTO deptBonusBudgetDetailsDTO)
    {
    DeptBonusBudgetDetails deptBonusBudgetDetails=new DeptBonusBudgetDetails();
    BeanUtils.copyProperties(deptBonusBudgetDetailsDTO,deptBonusBudgetDetails);
    deptBonusBudgetDetails.setUpdateTime(DateUtils.getNowDate());
    deptBonusBudgetDetails.setUpdateBy(SecurityUtils.getUserId());
    return deptBonusBudgetDetailsMapper.updateDeptBonusBudgetDetails(deptBonusBudgetDetails);
    }

    /**
    * 逻辑批量删除部门奖金预算明细表
    *
    * @param deptBonusBudgetDetailsIds 主键集合
    * @return 结果
    */
    @Override
    public int logicDeleteDeptBonusBudgetDetailsByDeptBonusBudgetDetailsIds(List<Long> deptBonusBudgetDetailsIds){
    return deptBonusBudgetDetailsMapper.logicDeleteDeptBonusBudgetDetailsByDeptBonusBudgetDetailsIds(deptBonusBudgetDetailsIds,SecurityUtils.getUserId(),DateUtils.getNowDate());
    }

    /**
    * 物理删除部门奖金预算明细表信息
    *
    * @param deptBonusBudgetDetailsId 部门奖金预算明细表主键
    * @return 结果
    */
    @Override
    public int deleteDeptBonusBudgetDetailsByDeptBonusBudgetDetailsId(Long deptBonusBudgetDetailsId)
    {
    return deptBonusBudgetDetailsMapper.deleteDeptBonusBudgetDetailsByDeptBonusBudgetDetailsId(deptBonusBudgetDetailsId);
    }

     /**
     * 逻辑删除部门奖金预算明细表信息
     *
     * @param  deptBonusBudgetDetailsDTO 部门奖金预算明细表
     * @return 结果
     */
     @Override
     public int logicDeleteDeptBonusBudgetDetailsByDeptBonusBudgetDetailsId(DeptBonusBudgetDetailsDTO deptBonusBudgetDetailsDTO)
     {
     DeptBonusBudgetDetails deptBonusBudgetDetails=new DeptBonusBudgetDetails();
     deptBonusBudgetDetails.setDeptBonusBudgetDetailsId(deptBonusBudgetDetailsDTO.getDeptBonusBudgetDetailsId());
     deptBonusBudgetDetails.setUpdateTime(DateUtils.getNowDate());
     deptBonusBudgetDetails.setUpdateBy(SecurityUtils.getUserId());
     return deptBonusBudgetDetailsMapper.logicDeleteDeptBonusBudgetDetailsByDeptBonusBudgetDetailsId(deptBonusBudgetDetails);
     }

     /**
     * 物理删除部门奖金预算明细表信息
     *
     * @param  deptBonusBudgetDetailsDTO 部门奖金预算明细表
     * @return 结果
     */
     
     @Override
     public int deleteDeptBonusBudgetDetailsByDeptBonusBudgetDetailsId(DeptBonusBudgetDetailsDTO deptBonusBudgetDetailsDTO)
     {
     DeptBonusBudgetDetails deptBonusBudgetDetails=new DeptBonusBudgetDetails();
     BeanUtils.copyProperties(deptBonusBudgetDetailsDTO,deptBonusBudgetDetails);
     return deptBonusBudgetDetailsMapper.deleteDeptBonusBudgetDetailsByDeptBonusBudgetDetailsId(deptBonusBudgetDetails.getDeptBonusBudgetDetailsId());
     }
     /**
     * 物理批量删除部门奖金预算明细表
     *
     * @param deptBonusBudgetDetailsDtos 需要删除的部门奖金预算明细表主键
     * @return 结果
     */
     
     @Override
     public int deleteDeptBonusBudgetDetailsByDeptBonusBudgetDetailsIds(List<DeptBonusBudgetDetailsDTO> deptBonusBudgetDetailsDtos){
     List<Long> stringList = new ArrayList();
     for (DeptBonusBudgetDetailsDTO deptBonusBudgetDetailsDTO : deptBonusBudgetDetailsDtos) {
     stringList.add(deptBonusBudgetDetailsDTO.getDeptBonusBudgetDetailsId());
     }
     return deptBonusBudgetDetailsMapper.deleteDeptBonusBudgetDetailsByDeptBonusBudgetDetailsIds(stringList);
     }

    /**
    * 批量新增部门奖金预算明细表信息
    *
    * @param deptBonusBudgetDetailsDtos 部门奖金预算明细表对象
    */
    
    public int insertDeptBonusBudgetDetailss(List<DeptBonusBudgetDetailsDTO> deptBonusBudgetDetailsDtos){
      List<DeptBonusBudgetDetails> deptBonusBudgetDetailsList = new ArrayList();

    for (DeptBonusBudgetDetailsDTO deptBonusBudgetDetailsDTO : deptBonusBudgetDetailsDtos) {
      DeptBonusBudgetDetails deptBonusBudgetDetails =new DeptBonusBudgetDetails();
      BeanUtils.copyProperties(deptBonusBudgetDetailsDTO,deptBonusBudgetDetails);
       deptBonusBudgetDetails.setCreateBy(SecurityUtils.getUserId());
       deptBonusBudgetDetails.setCreateTime(DateUtils.getNowDate());
       deptBonusBudgetDetails.setUpdateTime(DateUtils.getNowDate());
       deptBonusBudgetDetails.setUpdateBy(SecurityUtils.getUserId());
       deptBonusBudgetDetails.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
      deptBonusBudgetDetailsList.add(deptBonusBudgetDetails);
    }
    return deptBonusBudgetDetailsMapper.batchDeptBonusBudgetDetails(deptBonusBudgetDetailsList);
    }

    /**
    * 批量修改部门奖金预算明细表信息
    *
    * @param deptBonusBudgetDetailsDtos 部门奖金预算明细表对象
    */
    
    public int updateDeptBonusBudgetDetailss(List<DeptBonusBudgetDetailsDTO> deptBonusBudgetDetailsDtos){
     List<DeptBonusBudgetDetails> deptBonusBudgetDetailsList = new ArrayList();

     for (DeptBonusBudgetDetailsDTO deptBonusBudgetDetailsDTO : deptBonusBudgetDetailsDtos) {
     DeptBonusBudgetDetails deptBonusBudgetDetails =new DeptBonusBudgetDetails();
     BeanUtils.copyProperties(deptBonusBudgetDetailsDTO,deptBonusBudgetDetails);
        deptBonusBudgetDetails.setCreateBy(SecurityUtils.getUserId());
        deptBonusBudgetDetails.setCreateTime(DateUtils.getNowDate());
        deptBonusBudgetDetails.setUpdateTime(DateUtils.getNowDate());
        deptBonusBudgetDetails.setUpdateBy(SecurityUtils.getUserId());
     deptBonusBudgetDetailsList.add(deptBonusBudgetDetails);
     }
     return deptBonusBudgetDetailsMapper.updateDeptBonusBudgetDetailss(deptBonusBudgetDetailsList);
    }

}

