package net.qixiaowei.operate.cloud.service.impl.performance;

import java.util.List;
import net.qixiaowei.integration.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.performance.PerformancePercentageData;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformancePercentageDataDTO;
import net.qixiaowei.operate.cloud.mapper.performance.PerformancePercentageDataMapper;
import net.qixiaowei.operate.cloud.service.performance.IPerformancePercentageDataService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


/**
* PerformancePercentageDataService业务层处理
* @author Graves
* @since 2022-10-10
*/
@Service
public class PerformancePercentageDataServiceImpl implements IPerformancePercentageDataService{
    @Autowired
    private PerformancePercentageDataMapper performancePercentageDataMapper;

    /**
    * 查询绩效比例数据表
    *
    * @param performancePercentageDataId 绩效比例数据表主键
    * @return 绩效比例数据表
    */
    @Override
    public PerformancePercentageDataDTO selectPerformancePercentageDataByPerformancePercentageDataId(Long performancePercentageDataId)
    {
    return performancePercentageDataMapper.selectPerformancePercentageDataByPerformancePercentageDataId(performancePercentageDataId);
    }

    /**
    * 查询绩效比例数据表列表
    *
    * @param performancePercentageDataDTO 绩效比例数据表
    * @return 绩效比例数据表
    */
    @Override
    public List<PerformancePercentageDataDTO> selectPerformancePercentageDataList(PerformancePercentageDataDTO performancePercentageDataDTO)
    {
    PerformancePercentageData performancePercentageData=new PerformancePercentageData();
    BeanUtils.copyProperties(performancePercentageDataDTO,performancePercentageData);
    return performancePercentageDataMapper.selectPerformancePercentageDataList(performancePercentageData);
    }

    /**
    * 新增绩效比例数据表
    *
    * @param performancePercentageDataDTO 绩效比例数据表
    * @return 结果
    */
    @Override
    public int insertPerformancePercentageData(PerformancePercentageDataDTO performancePercentageDataDTO){
    PerformancePercentageData performancePercentageData=new PerformancePercentageData();
    BeanUtils.copyProperties(performancePercentageDataDTO,performancePercentageData);
    performancePercentageData.setCreateBy(SecurityUtils.getUserId());
    performancePercentageData.setCreateTime(DateUtils.getNowDate());
    performancePercentageData.setUpdateTime(DateUtils.getNowDate());
    performancePercentageData.setUpdateBy(SecurityUtils.getUserId());
    performancePercentageData.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
    return performancePercentageDataMapper.insertPerformancePercentageData(performancePercentageData);
    }

    /**
    * 修改绩效比例数据表
    *
    * @param performancePercentageDataDTO 绩效比例数据表
    * @return 结果
    */
    @Override
    public int updatePerformancePercentageData(PerformancePercentageDataDTO performancePercentageDataDTO)
    {
    PerformancePercentageData performancePercentageData=new PerformancePercentageData();
    BeanUtils.copyProperties(performancePercentageDataDTO,performancePercentageData);
    performancePercentageData.setUpdateTime(DateUtils.getNowDate());
    performancePercentageData.setUpdateBy(SecurityUtils.getUserId());
    return performancePercentageDataMapper.updatePerformancePercentageData(performancePercentageData);
    }

    /**
    * 逻辑批量删除绩效比例数据表
    *
    * @param performancePercentageDataDtos 需要删除的绩效比例数据表主键
    * @return 结果
    */
    @Override
    public int logicDeletePerformancePercentageDataByPerformancePercentageDataIds(List<PerformancePercentageDataDTO> performancePercentageDataDtos){
            List<Long> stringList = new ArrayList();
            for (PerformancePercentageDataDTO performancePercentageDataDTO : performancePercentageDataDtos) {
                stringList.add(performancePercentageDataDTO.getPerformancePercentageDataId());
            }
    return performancePercentageDataMapper.logicDeletePerformancePercentageDataByPerformancePercentageDataIds(stringList,performancePercentageDataDtos.get(0).getUpdateBy(),DateUtils.getNowDate());
    }

    /**
    * 物理删除绩效比例数据表信息
    *
    * @param performancePercentageDataId 绩效比例数据表主键
    * @return 结果
    */
    @Override
    public int deletePerformancePercentageDataByPerformancePercentageDataId(Long performancePercentageDataId)
    {
    return performancePercentageDataMapper.deletePerformancePercentageDataByPerformancePercentageDataId(performancePercentageDataId);
    }

     /**
     * 逻辑删除绩效比例数据表信息
     *
     * @param  performancePercentageDataDTO 绩效比例数据表
     * @return 结果
     */
     @Override
     public int logicDeletePerformancePercentageDataByPerformancePercentageDataId(PerformancePercentageDataDTO performancePercentageDataDTO)
     {
     PerformancePercentageData performancePercentageData=new PerformancePercentageData();
     performancePercentageData.setPerformancePercentageDataId(performancePercentageDataDTO.getPerformancePercentageDataId());
     performancePercentageData.setUpdateTime(DateUtils.getNowDate());
     performancePercentageData.setUpdateBy(SecurityUtils.getUserId());
     return performancePercentageDataMapper.logicDeletePerformancePercentageDataByPerformancePercentageDataId(performancePercentageData);
     }

     /**
     * 物理删除绩效比例数据表信息
     *
     * @param  performancePercentageDataDTO 绩效比例数据表
     * @return 结果
     */
     
     @Override
     public int deletePerformancePercentageDataByPerformancePercentageDataId(PerformancePercentageDataDTO performancePercentageDataDTO)
     {
     PerformancePercentageData performancePercentageData=new PerformancePercentageData();
     BeanUtils.copyProperties(performancePercentageDataDTO,performancePercentageData);
     return performancePercentageDataMapper.deletePerformancePercentageDataByPerformancePercentageDataId(performancePercentageData.getPerformancePercentageDataId());
     }
     /**
     * 物理批量删除绩效比例数据表
     *
     * @param performancePercentageDataDtos 需要删除的绩效比例数据表主键
     * @return 结果
     */
     
     @Override
     public int deletePerformancePercentageDataByPerformancePercentageDataIds(List<PerformancePercentageDataDTO> performancePercentageDataDtos){
     List<Long> stringList = new ArrayList();
     for (PerformancePercentageDataDTO performancePercentageDataDTO : performancePercentageDataDtos) {
     stringList.add(performancePercentageDataDTO.getPerformancePercentageDataId());
     }
     return performancePercentageDataMapper.deletePerformancePercentageDataByPerformancePercentageDataIds(stringList);
     }

    /**
    * 批量新增绩效比例数据表信息
    *
    * @param performancePercentageDataDtos 绩效比例数据表对象
    */
    
    public int insertPerformancePercentageDatas(List<PerformancePercentageDataDTO> performancePercentageDataDtos){
      List<PerformancePercentageData> performancePercentageDataList = new ArrayList();

    for (PerformancePercentageDataDTO performancePercentageDataDTO : performancePercentageDataDtos) {
      PerformancePercentageData performancePercentageData =new PerformancePercentageData();
      BeanUtils.copyProperties(performancePercentageDataDTO,performancePercentageData);
       performancePercentageData.setCreateBy(SecurityUtils.getUserId());
       performancePercentageData.setCreateTime(DateUtils.getNowDate());
       performancePercentageData.setUpdateTime(DateUtils.getNowDate());
       performancePercentageData.setUpdateBy(SecurityUtils.getUserId());
       performancePercentageData.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
      performancePercentageDataList.add(performancePercentageData);
    }
    return performancePercentageDataMapper.batchPerformancePercentageData(performancePercentageDataList);
    }

    /**
    * 批量修改绩效比例数据表信息
    *
    * @param performancePercentageDataDtos 绩效比例数据表对象
    */
    
    public int updatePerformancePercentageDatas(List<PerformancePercentageDataDTO> performancePercentageDataDtos){
     List<PerformancePercentageData> performancePercentageDataList = new ArrayList();

     for (PerformancePercentageDataDTO performancePercentageDataDTO : performancePercentageDataDtos) {
     PerformancePercentageData performancePercentageData =new PerformancePercentageData();
     BeanUtils.copyProperties(performancePercentageDataDTO,performancePercentageData);
        performancePercentageData.setCreateBy(SecurityUtils.getUserId());
        performancePercentageData.setCreateTime(DateUtils.getNowDate());
        performancePercentageData.setUpdateTime(DateUtils.getNowDate());
        performancePercentageData.setUpdateBy(SecurityUtils.getUserId());
     performancePercentageDataList.add(performancePercentageData);
     }
     return performancePercentageDataMapper.updatePerformancePercentageDatas(performancePercentageDataList);
    }
}

