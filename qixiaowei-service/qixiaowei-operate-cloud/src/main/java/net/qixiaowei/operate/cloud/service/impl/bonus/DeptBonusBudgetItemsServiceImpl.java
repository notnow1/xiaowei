package net.qixiaowei.operate.cloud.service.impl.bonus;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.salary.DeptBonusBudgetItems;
import net.qixiaowei.operate.cloud.api.dto.salary.DeptBonusBudgetItemsDTO;
import net.qixiaowei.operate.cloud.mapper.bonus.DeptBonusBudgetItemsMapper;
import net.qixiaowei.operate.cloud.service.bonus.IDeptBonusBudgetItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
* DeptBonusBudgetItemsService业务层处理
* @author TANGMICHI
* @since 2022-11-29
*/
@Service
public class DeptBonusBudgetItemsServiceImpl implements IDeptBonusBudgetItemsService{
    @Autowired
    private DeptBonusBudgetItemsMapper deptBonusBudgetItemsMapper;

    /**
    * 查询部门奖金预算项目表
    *
    * @param deptBonusBudgetItemsId 部门奖金预算项目表主键
    * @return 部门奖金预算项目表
    */
    @Override
    public DeptBonusBudgetItemsDTO selectDeptBonusBudgetItemsByDeptBonusBudgetItemsId(Long deptBonusBudgetItemsId)
    {
    return deptBonusBudgetItemsMapper.selectDeptBonusBudgetItemsByDeptBonusBudgetItemsId(deptBonusBudgetItemsId);
    }

    /**
    * 查询部门奖金预算项目表列表
    *
    * @param deptBonusBudgetItemsDTO 部门奖金预算项目表
    * @return 部门奖金预算项目表
    */
    @Override
    public List<DeptBonusBudgetItemsDTO> selectDeptBonusBudgetItemsList(DeptBonusBudgetItemsDTO deptBonusBudgetItemsDTO)
    {
    DeptBonusBudgetItems deptBonusBudgetItems=new DeptBonusBudgetItems();
    BeanUtils.copyProperties(deptBonusBudgetItemsDTO,deptBonusBudgetItems);
    return deptBonusBudgetItemsMapper.selectDeptBonusBudgetItemsList(deptBonusBudgetItems);
    }

    /**
    * 新增部门奖金预算项目表
    *
    * @param deptBonusBudgetItemsDTO 部门奖金预算项目表
    * @return 结果
    */
    @Override
    public DeptBonusBudgetItemsDTO insertDeptBonusBudgetItems(DeptBonusBudgetItemsDTO deptBonusBudgetItemsDTO){
    DeptBonusBudgetItems deptBonusBudgetItems=new DeptBonusBudgetItems();
    BeanUtils.copyProperties(deptBonusBudgetItemsDTO,deptBonusBudgetItems);
    deptBonusBudgetItems.setCreateBy(SecurityUtils.getUserId());
    deptBonusBudgetItems.setCreateTime(DateUtils.getNowDate());
    deptBonusBudgetItems.setUpdateTime(DateUtils.getNowDate());
    deptBonusBudgetItems.setUpdateBy(SecurityUtils.getUserId());
    deptBonusBudgetItems.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
    deptBonusBudgetItemsMapper.insertDeptBonusBudgetItems(deptBonusBudgetItems);
    deptBonusBudgetItemsDTO.setDeptBonusBudgetItemsId(deptBonusBudgetItems.getDeptBonusBudgetItemsId());
    return deptBonusBudgetItemsDTO;
    }

    /**
    * 修改部门奖金预算项目表
    *
    * @param deptBonusBudgetItemsDTO 部门奖金预算项目表
    * @return 结果
    */
    @Override
    public int updateDeptBonusBudgetItems(DeptBonusBudgetItemsDTO deptBonusBudgetItemsDTO)
    {
    DeptBonusBudgetItems deptBonusBudgetItems=new DeptBonusBudgetItems();
    BeanUtils.copyProperties(deptBonusBudgetItemsDTO,deptBonusBudgetItems);
    deptBonusBudgetItems.setUpdateTime(DateUtils.getNowDate());
    deptBonusBudgetItems.setUpdateBy(SecurityUtils.getUserId());
    return deptBonusBudgetItemsMapper.updateDeptBonusBudgetItems(deptBonusBudgetItems);
    }

    /**
    * 逻辑批量删除部门奖金预算项目表
    *
    * @param deptBonusBudgetItemsIds 主键集合
    * @return 结果
    */
    @Override
    public int logicDeleteDeptBonusBudgetItemsByDeptBonusBudgetItemsIds(List<Long> deptBonusBudgetItemsIds){
    return deptBonusBudgetItemsMapper.logicDeleteDeptBonusBudgetItemsByDeptBonusBudgetItemsIds(deptBonusBudgetItemsIds,SecurityUtils.getUserId(),DateUtils.getNowDate());
    }

    /**
    * 物理删除部门奖金预算项目表信息
    *
    * @param deptBonusBudgetItemsId 部门奖金预算项目表主键
    * @return 结果
    */
    @Override
    public int deleteDeptBonusBudgetItemsByDeptBonusBudgetItemsId(Long deptBonusBudgetItemsId)
    {
    return deptBonusBudgetItemsMapper.deleteDeptBonusBudgetItemsByDeptBonusBudgetItemsId(deptBonusBudgetItemsId);
    }

     /**
     * 逻辑删除部门奖金预算项目表信息
     *
     * @param  deptBonusBudgetItemsDTO 部门奖金预算项目表
     * @return 结果
     */
     @Override
     public int logicDeleteDeptBonusBudgetItemsByDeptBonusBudgetItemsId(DeptBonusBudgetItemsDTO deptBonusBudgetItemsDTO)
     {
     DeptBonusBudgetItems deptBonusBudgetItems=new DeptBonusBudgetItems();
     deptBonusBudgetItems.setDeptBonusBudgetItemsId(deptBonusBudgetItemsDTO.getDeptBonusBudgetItemsId());
     deptBonusBudgetItems.setUpdateTime(DateUtils.getNowDate());
     deptBonusBudgetItems.setUpdateBy(SecurityUtils.getUserId());
     return deptBonusBudgetItemsMapper.logicDeleteDeptBonusBudgetItemsByDeptBonusBudgetItemsId(deptBonusBudgetItems);
     }

     /**
     * 物理删除部门奖金预算项目表信息
     *
     * @param  deptBonusBudgetItemsDTO 部门奖金预算项目表
     * @return 结果
     */
     
     @Override
     public int deleteDeptBonusBudgetItemsByDeptBonusBudgetItemsId(DeptBonusBudgetItemsDTO deptBonusBudgetItemsDTO)
     {
     DeptBonusBudgetItems deptBonusBudgetItems=new DeptBonusBudgetItems();
     BeanUtils.copyProperties(deptBonusBudgetItemsDTO,deptBonusBudgetItems);
     return deptBonusBudgetItemsMapper.deleteDeptBonusBudgetItemsByDeptBonusBudgetItemsId(deptBonusBudgetItems.getDeptBonusBudgetItemsId());
     }
     /**
     * 物理批量删除部门奖金预算项目表
     *
     * @param deptBonusBudgetItemsDtos 需要删除的部门奖金预算项目表主键
     * @return 结果
     */
     
     @Override
     public int deleteDeptBonusBudgetItemsByDeptBonusBudgetItemsIds(List<DeptBonusBudgetItemsDTO> deptBonusBudgetItemsDtos){
     List<Long> stringList = new ArrayList();
     for (DeptBonusBudgetItemsDTO deptBonusBudgetItemsDTO : deptBonusBudgetItemsDtos) {
     stringList.add(deptBonusBudgetItemsDTO.getDeptBonusBudgetItemsId());
     }
     return deptBonusBudgetItemsMapper.deleteDeptBonusBudgetItemsByDeptBonusBudgetItemsIds(stringList);
     }

    /**
    * 批量新增部门奖金预算项目表信息
    *
    * @param deptBonusBudgetItemsDtos 部门奖金预算项目表对象
    */
    
    public int insertDeptBonusBudgetItemss(List<DeptBonusBudgetItemsDTO> deptBonusBudgetItemsDtos){
      List<DeptBonusBudgetItems> deptBonusBudgetItemsList = new ArrayList();

    for (DeptBonusBudgetItemsDTO deptBonusBudgetItemsDTO : deptBonusBudgetItemsDtos) {
      DeptBonusBudgetItems deptBonusBudgetItems =new DeptBonusBudgetItems();
      BeanUtils.copyProperties(deptBonusBudgetItemsDTO,deptBonusBudgetItems);
       deptBonusBudgetItems.setCreateBy(SecurityUtils.getUserId());
       deptBonusBudgetItems.setCreateTime(DateUtils.getNowDate());
       deptBonusBudgetItems.setUpdateTime(DateUtils.getNowDate());
       deptBonusBudgetItems.setUpdateBy(SecurityUtils.getUserId());
       deptBonusBudgetItems.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
      deptBonusBudgetItemsList.add(deptBonusBudgetItems);
    }
    return deptBonusBudgetItemsMapper.batchDeptBonusBudgetItems(deptBonusBudgetItemsList);
    }

    /**
    * 批量修改部门奖金预算项目表信息
    *
    * @param deptBonusBudgetItemsDtos 部门奖金预算项目表对象
    */
    
    public int updateDeptBonusBudgetItemss(List<DeptBonusBudgetItemsDTO> deptBonusBudgetItemsDtos){
     List<DeptBonusBudgetItems> deptBonusBudgetItemsList = new ArrayList();

     for (DeptBonusBudgetItemsDTO deptBonusBudgetItemsDTO : deptBonusBudgetItemsDtos) {
     DeptBonusBudgetItems deptBonusBudgetItems =new DeptBonusBudgetItems();
     BeanUtils.copyProperties(deptBonusBudgetItemsDTO,deptBonusBudgetItems);
        deptBonusBudgetItems.setCreateBy(SecurityUtils.getUserId());
        deptBonusBudgetItems.setCreateTime(DateUtils.getNowDate());
        deptBonusBudgetItems.setUpdateTime(DateUtils.getNowDate());
        deptBonusBudgetItems.setUpdateBy(SecurityUtils.getUserId());
     deptBonusBudgetItemsList.add(deptBonusBudgetItems);
     }
     return deptBonusBudgetItemsMapper.updateDeptBonusBudgetItemss(deptBonusBudgetItemsList);
    }

}

