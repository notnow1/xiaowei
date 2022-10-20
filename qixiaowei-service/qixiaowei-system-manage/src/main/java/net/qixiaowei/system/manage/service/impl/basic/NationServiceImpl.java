package net.qixiaowei.system.manage.service.impl.basic;

import java.util.List;
import net.qixiaowei.integration.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.domain.basic.Nation;
import net.qixiaowei.system.manage.api.dto.basic.NationDTO;
import net.qixiaowei.system.manage.mapper.basic.NationMapper;
import net.qixiaowei.system.manage.service.basic.INationService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


/**
* NationService业务层处理
* @author TANGMICHI
* @since 2022-10-20
*/
@Service
public class NationServiceImpl implements INationService{
    @Autowired
    private NationMapper nationMapper;

    /**
    * 查询民族表
    *
    * @param nationId 民族表主键
    * @return 民族表
    */
    @Override
    public NationDTO selectNationByNationId(Long nationId)
    {
    return nationMapper.selectNationByNationId(nationId);
    }

    /**
    * 查询民族表列表
    *
    * @param nationDTO 民族表
    * @return 民族表
    */
    @Override
    public List<NationDTO> selectNationList(NationDTO nationDTO)
    {
    Nation nation=new Nation();
    BeanUtils.copyProperties(nationDTO,nation);
    return nationMapper.selectNationList(nation);
    }

    /**
    * 新增民族表
    *
    * @param nationDTO 民族表
    * @return 结果
    */
    @Override
    public NationDTO insertNation(NationDTO nationDTO){
    Nation nation=new Nation();
    BeanUtils.copyProperties(nationDTO,nation);
    nation.setCreateBy(SecurityUtils.getUserId());
    nation.setCreateTime(DateUtils.getNowDate());
    nation.setUpdateTime(DateUtils.getNowDate());
    nation.setUpdateBy(SecurityUtils.getUserId());
    nation.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
    nationMapper.insertNation(nation);
    nationDTO.setNationId(nation.getNationId());
    return nationDTO;
    }

    /**
    * 修改民族表
    *
    * @param nationDTO 民族表
    * @return 结果
    */
    @Override
    public int updateNation(NationDTO nationDTO)
    {
    Nation nation=new Nation();
    BeanUtils.copyProperties(nationDTO,nation);
    nation.setUpdateTime(DateUtils.getNowDate());
    nation.setUpdateBy(SecurityUtils.getUserId());
    return nationMapper.updateNation(nation);
    }

    /**
    * 逻辑批量删除民族表
    *
    * @param nationIds 主键集合
    * @return 结果
    */
    @Override
    public int logicDeleteNationByNationIds(List<Long> nationIds){
    return nationMapper.logicDeleteNationByNationIds(nationIds,SecurityUtils.getUserId(),DateUtils.getNowDate());
    }

    /**
    * 物理删除民族表信息
    *
    * @param nationId 民族表主键
    * @return 结果
    */
    @Override
    public int deleteNationByNationId(Long nationId)
    {
    return nationMapper.deleteNationByNationId(nationId);
    }

     /**
     * 逻辑删除民族表信息
     *
     * @param  nationDTO 民族表
     * @return 结果
     */
     @Override
     public int logicDeleteNationByNationId(NationDTO nationDTO)
     {
     Nation nation=new Nation();
     nation.setNationId(nationDTO.getNationId());
     nation.setUpdateTime(DateUtils.getNowDate());
     nation.setUpdateBy(SecurityUtils.getUserId());
     return nationMapper.logicDeleteNationByNationId(nation);
     }

     /**
     * 物理删除民族表信息
     *
     * @param  nationDTO 民族表
     * @return 结果
     */
     
     @Override
     public int deleteNationByNationId(NationDTO nationDTO)
     {
     Nation nation=new Nation();
     BeanUtils.copyProperties(nationDTO,nation);
     return nationMapper.deleteNationByNationId(nation.getNationId());
     }
     /**
     * 物理批量删除民族表
     *
     * @param nationDtos 需要删除的民族表主键
     * @return 结果
     */
     
     @Override
     public int deleteNationByNationIds(List<NationDTO> nationDtos){
     List<Long> stringList = new ArrayList();
     for (NationDTO nationDTO : nationDtos) {
     stringList.add(nationDTO.getNationId());
     }
     return nationMapper.deleteNationByNationIds(stringList);
     }

    /**
    * 批量新增民族表信息
    *
    * @param nationDtos 民族表对象
    */
    
    public int insertNations(List<NationDTO> nationDtos){
      List<Nation> nationList = new ArrayList();

    for (NationDTO nationDTO : nationDtos) {
      Nation nation =new Nation();
      BeanUtils.copyProperties(nationDTO,nation);
       nation.setCreateBy(SecurityUtils.getUserId());
       nation.setCreateTime(DateUtils.getNowDate());
       nation.setUpdateTime(DateUtils.getNowDate());
       nation.setUpdateBy(SecurityUtils.getUserId());
       nation.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
      nationList.add(nation);
    }
    return nationMapper.batchNation(nationList);
    }

    /**
    * 批量修改民族表信息
    *
    * @param nationDtos 民族表对象
    */
    
    public int updateNations(List<NationDTO> nationDtos){
     List<Nation> nationList = new ArrayList();

     for (NationDTO nationDTO : nationDtos) {
     Nation nation =new Nation();
     BeanUtils.copyProperties(nationDTO,nation);
        nation.setCreateBy(SecurityUtils.getUserId());
        nation.setCreateTime(DateUtils.getNowDate());
        nation.setUpdateTime(DateUtils.getNowDate());
        nation.setUpdateBy(SecurityUtils.getUserId());
     nationList.add(nation);
     }
     return nationMapper.updateNations(nationList);
    }
}

