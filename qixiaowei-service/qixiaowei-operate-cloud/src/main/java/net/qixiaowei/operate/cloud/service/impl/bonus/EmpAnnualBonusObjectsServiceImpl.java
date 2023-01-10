package net.qixiaowei.operate.cloud.service.impl.bonus;

import java.util.List;
import net.qixiaowei.integration.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.bonus.EmpAnnualBonusObjects;

import net.qixiaowei.operate.cloud.api.dto.bonus.EmpAnnualBonusObjectsDTO;
import net.qixiaowei.operate.cloud.mapper.bonus.EmpAnnualBonusObjectsMapper;
import net.qixiaowei.operate.cloud.service.bonus.IEmpAnnualBonusObjectsService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


/**
* EmpAnnualBonusObjectsService业务层处理
* @author TANGMICHI
* @since 2022-12-02
*/
@Service
public class EmpAnnualBonusObjectsServiceImpl implements IEmpAnnualBonusObjectsService{
    @Autowired
    private EmpAnnualBonusObjectsMapper empAnnualBonusObjectsMapper;

    /**
    * 查询个人年终奖发放对象表
    *
    * @param empAnnualBonusObjectsId 个人年终奖发放对象表主键
    * @return 个人年终奖发放对象表
    */
    @Override
    public EmpAnnualBonusObjectsDTO selectEmpAnnualBonusObjectsByEmpAnnualBonusObjectsId(Long empAnnualBonusObjectsId)
    {
    return empAnnualBonusObjectsMapper.selectEmpAnnualBonusObjectsByEmpAnnualBonusObjectsId(empAnnualBonusObjectsId);
    }

    /**
    * 查询个人年终奖发放对象表列表
    *
    * @param empAnnualBonusObjectsDTO 个人年终奖发放对象表
    * @return 个人年终奖发放对象表
    */
    @Override
    public List<EmpAnnualBonusObjectsDTO> selectEmpAnnualBonusObjectsList(EmpAnnualBonusObjectsDTO empAnnualBonusObjectsDTO)
    {
    EmpAnnualBonusObjects empAnnualBonusObjects=new EmpAnnualBonusObjects();
    BeanUtils.copyProperties(empAnnualBonusObjectsDTO,empAnnualBonusObjects);
    return empAnnualBonusObjectsMapper.selectEmpAnnualBonusObjectsList(empAnnualBonusObjects);
    }

    /**
    * 新增个人年终奖发放对象表
    *
    * @param empAnnualBonusObjectsDTO 个人年终奖发放对象表
    * @return 结果
    */
    @Override
    public EmpAnnualBonusObjectsDTO insertEmpAnnualBonusObjects(EmpAnnualBonusObjectsDTO empAnnualBonusObjectsDTO){
    EmpAnnualBonusObjects empAnnualBonusObjects=new EmpAnnualBonusObjects();
    BeanUtils.copyProperties(empAnnualBonusObjectsDTO,empAnnualBonusObjects);
    empAnnualBonusObjects.setCreateBy(SecurityUtils.getUserId());
    empAnnualBonusObjects.setCreateTime(DateUtils.getNowDate());
    empAnnualBonusObjects.setUpdateTime(DateUtils.getNowDate());
    empAnnualBonusObjects.setUpdateBy(SecurityUtils.getUserId());
    empAnnualBonusObjects.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
    empAnnualBonusObjectsMapper.insertEmpAnnualBonusObjects(empAnnualBonusObjects);
    empAnnualBonusObjectsDTO.setEmpAnnualBonusObjectsId(empAnnualBonusObjects.getEmpAnnualBonusObjectsId());
    return empAnnualBonusObjectsDTO;
    }

    /**
    * 修改个人年终奖发放对象表
    *
    * @param empAnnualBonusObjectsDTO 个人年终奖发放对象表
    * @return 结果
    */
    @Override
    public int updateEmpAnnualBonusObjects(EmpAnnualBonusObjectsDTO empAnnualBonusObjectsDTO)
    {
    EmpAnnualBonusObjects empAnnualBonusObjects=new EmpAnnualBonusObjects();
    BeanUtils.copyProperties(empAnnualBonusObjectsDTO,empAnnualBonusObjects);
    empAnnualBonusObjects.setUpdateTime(DateUtils.getNowDate());
    empAnnualBonusObjects.setUpdateBy(SecurityUtils.getUserId());
    return empAnnualBonusObjectsMapper.updateEmpAnnualBonusObjects(empAnnualBonusObjects);
    }

    /**
    * 逻辑批量删除个人年终奖发放对象表
    *
    * @param empAnnualBonusObjectsIds 主键集合
    * @return 结果
    */
    @Override
    public int logicDeleteEmpAnnualBonusObjectsByEmpAnnualBonusObjectsIds(List<Long> empAnnualBonusObjectsIds){
    return empAnnualBonusObjectsMapper.logicDeleteEmpAnnualBonusObjectsByEmpAnnualBonusObjectsIds(empAnnualBonusObjectsIds,SecurityUtils.getUserId(),DateUtils.getNowDate());
    }

    /**
    * 物理删除个人年终奖发放对象表信息
    *
    * @param empAnnualBonusObjectsId 个人年终奖发放对象表主键
    * @return 结果
    */
    @Override
    public int deleteEmpAnnualBonusObjectsByEmpAnnualBonusObjectsId(Long empAnnualBonusObjectsId)
    {
    return empAnnualBonusObjectsMapper.deleteEmpAnnualBonusObjectsByEmpAnnualBonusObjectsId(empAnnualBonusObjectsId);
    }

     /**
     * 逻辑删除个人年终奖发放对象表信息
     *
     * @param  empAnnualBonusObjectsDTO 个人年终奖发放对象表
     * @return 结果
     */
     @Override
     public int logicDeleteEmpAnnualBonusObjectsByEmpAnnualBonusObjectsId(EmpAnnualBonusObjectsDTO empAnnualBonusObjectsDTO)
     {
     EmpAnnualBonusObjects empAnnualBonusObjects=new EmpAnnualBonusObjects();
     empAnnualBonusObjects.setEmpAnnualBonusObjectsId(empAnnualBonusObjectsDTO.getEmpAnnualBonusObjectsId());
     empAnnualBonusObjects.setUpdateTime(DateUtils.getNowDate());
     empAnnualBonusObjects.setUpdateBy(SecurityUtils.getUserId());
     return empAnnualBonusObjectsMapper.logicDeleteEmpAnnualBonusObjectsByEmpAnnualBonusObjectsId(empAnnualBonusObjects);
     }

     /**
     * 物理删除个人年终奖发放对象表信息
     *
     * @param  empAnnualBonusObjectsDTO 个人年终奖发放对象表
     * @return 结果
     */
     
     @Override
     public int deleteEmpAnnualBonusObjectsByEmpAnnualBonusObjectsId(EmpAnnualBonusObjectsDTO empAnnualBonusObjectsDTO)
     {
     EmpAnnualBonusObjects empAnnualBonusObjects=new EmpAnnualBonusObjects();
     BeanUtils.copyProperties(empAnnualBonusObjectsDTO,empAnnualBonusObjects);
     return empAnnualBonusObjectsMapper.deleteEmpAnnualBonusObjectsByEmpAnnualBonusObjectsId(empAnnualBonusObjects.getEmpAnnualBonusObjectsId());
     }
     /**
     * 物理批量删除个人年终奖发放对象表
     *
     * @param empAnnualBonusObjectsDtos 需要删除的个人年终奖发放对象表主键
     * @return 结果
     */
     
     @Override
     public int deleteEmpAnnualBonusObjectsByEmpAnnualBonusObjectsIds(List<EmpAnnualBonusObjectsDTO> empAnnualBonusObjectsDtos){
     List<Long> stringList = new ArrayList();
     for (EmpAnnualBonusObjectsDTO empAnnualBonusObjectsDTO : empAnnualBonusObjectsDtos) {
     stringList.add(empAnnualBonusObjectsDTO.getEmpAnnualBonusObjectsId());
     }
     return empAnnualBonusObjectsMapper.deleteEmpAnnualBonusObjectsByEmpAnnualBonusObjectsIds(stringList);
     }

    /**
    * 批量新增个人年终奖发放对象表信息
    *
    * @param empAnnualBonusObjectsDtos 个人年终奖发放对象表对象
    */
    
    @Override
    public int insertEmpAnnualBonusObjectss(List<EmpAnnualBonusObjectsDTO> empAnnualBonusObjectsDtos){
      List<EmpAnnualBonusObjects> empAnnualBonusObjectsList = new ArrayList();

    for (EmpAnnualBonusObjectsDTO empAnnualBonusObjectsDTO : empAnnualBonusObjectsDtos) {
      EmpAnnualBonusObjects empAnnualBonusObjects =new EmpAnnualBonusObjects();
      BeanUtils.copyProperties(empAnnualBonusObjectsDTO,empAnnualBonusObjects);
       empAnnualBonusObjects.setCreateBy(SecurityUtils.getUserId());
       empAnnualBonusObjects.setCreateTime(DateUtils.getNowDate());
       empAnnualBonusObjects.setUpdateTime(DateUtils.getNowDate());
       empAnnualBonusObjects.setUpdateBy(SecurityUtils.getUserId());
       empAnnualBonusObjects.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
      empAnnualBonusObjectsList.add(empAnnualBonusObjects);
    }
    return empAnnualBonusObjectsMapper.batchEmpAnnualBonusObjects(empAnnualBonusObjectsList);
    }

    /**
    * 批量修改个人年终奖发放对象表信息
    *
    * @param empAnnualBonusObjectsDtos 个人年终奖发放对象表对象
    */
    
    @Override
    public int updateEmpAnnualBonusObjectss(List<EmpAnnualBonusObjectsDTO> empAnnualBonusObjectsDtos){
     List<EmpAnnualBonusObjects> empAnnualBonusObjectsList = new ArrayList();

     for (EmpAnnualBonusObjectsDTO empAnnualBonusObjectsDTO : empAnnualBonusObjectsDtos) {
     EmpAnnualBonusObjects empAnnualBonusObjects =new EmpAnnualBonusObjects();
     BeanUtils.copyProperties(empAnnualBonusObjectsDTO,empAnnualBonusObjects);
        empAnnualBonusObjects.setCreateBy(SecurityUtils.getUserId());
        empAnnualBonusObjects.setCreateTime(DateUtils.getNowDate());
        empAnnualBonusObjects.setUpdateTime(DateUtils.getNowDate());
        empAnnualBonusObjects.setUpdateBy(SecurityUtils.getUserId());
     empAnnualBonusObjectsList.add(empAnnualBonusObjects);
     }
     return empAnnualBonusObjectsMapper.updateEmpAnnualBonusObjectss(empAnnualBonusObjectsList);
    }
}

