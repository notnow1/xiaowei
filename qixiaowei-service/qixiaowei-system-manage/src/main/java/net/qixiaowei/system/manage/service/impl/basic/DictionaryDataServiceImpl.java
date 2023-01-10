package net.qixiaowei.system.manage.service.impl.basic;


import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.dto.product.ProductDTO;
import net.qixiaowei.operate.cloud.api.remote.product.RemoteProductService;
import net.qixiaowei.system.manage.api.domain.basic.DictionaryData;
import net.qixiaowei.system.manage.api.dto.basic.DictionaryDataDTO;
import net.qixiaowei.system.manage.api.dto.basic.DictionaryTypeDTO;
import net.qixiaowei.system.manage.mapper.basic.DictionaryDataMapper;
import net.qixiaowei.system.manage.service.basic.IDictionaryDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * DictionaryDataService业务层处理
 *
 * @author TANGMICHI
 * @since 2022-10-15
 */
@Service
public class DictionaryDataServiceImpl implements IDictionaryDataService {
    @Autowired
    private DictionaryDataMapper dictionaryDataMapper;

    @Autowired
    private RemoteProductService remoteDictionaryService;

    /**
     * 查询字典数据表
     *
     * @param dictionaryDataId 字典数据表主键
     * @return 字典数据表
     */
    @Override
    public DictionaryDataDTO selectDictionaryDataByDictionaryDataId(Long dictionaryDataId) {
        return dictionaryDataMapper.selectDictionaryDataByDictionaryDataId(dictionaryDataId);
    }
    /**
     * 根据字典id集合查询字典数据表
     *
     * @param dictionaryDataIds 字典数据表主键
     * @return 字典数据表
     */
    @Override
    public List<DictionaryDataDTO> selectDictionaryDataByDictionaryDataIds(List<Long> dictionaryDataIds) {
        return dictionaryDataMapper.selectDictionaryDataByDictionaryDataIds(dictionaryDataIds);
    }

    /**
     * 查询字典数据表列表
     *
     * @param dictionaryDataDTO 字典数据表
     * @return 字典数据表
     */
    @Override
    public List<DictionaryDataDTO> selectDictionaryDataList(DictionaryDataDTO dictionaryDataDTO) {
        DictionaryData dictionaryData = new DictionaryData();
        BeanUtils.copyProperties(dictionaryDataDTO, dictionaryData);
        return dictionaryDataMapper.selectDictionaryDataList(dictionaryData);
    }

    /**
     * 新增字典数据表
     *
     * @param dictionaryDataDTO 字典数据表
     * @return 结果
     */
    @Override
    public DictionaryDataDTO insertDictionaryData(DictionaryDataDTO dictionaryDataDTO) {
        DictionaryData dictionaryData = new DictionaryData();
        BeanUtils.copyProperties(dictionaryDataDTO, dictionaryData);
        if(StringUtils.isEmpty(dictionaryDataDTO.getDictionaryValue())){
            //根据type Id查询数据做排序 自增
          int i  = dictionaryDataMapper.selectDictionaryTypeId(dictionaryData.getDictionaryTypeId());
          //如果为空赋值
          if (dictionaryDataDTO.getSort() == null){
              dictionaryData.setSort(i+1);
          }
            dictionaryData.setDictionaryValue(String.valueOf(i+1));
        }
        dictionaryData.setDefaultFlag(0);
        dictionaryData.setCreateBy(SecurityUtils.getUserId());
        dictionaryData.setCreateTime(DateUtils.getNowDate());
        dictionaryData.setUpdateTime(DateUtils.getNowDate());
        dictionaryData.setUpdateBy(SecurityUtils.getUserId());
        dictionaryData.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        dictionaryDataMapper.insertDictionaryData(dictionaryData);
        dictionaryDataDTO.setDictionaryDataId(dictionaryData.getDictionaryDataId());
        return dictionaryDataDTO;
    }

    /**
     * 修改字典数据表
     *
     * @param dictionaryDataDTO 字典数据表
     * @return 结果
     */
    @Override
    public int updateDictionaryData(DictionaryDataDTO dictionaryDataDTO) {
        DictionaryData dictionaryData = new DictionaryData();
        BeanUtils.copyProperties(dictionaryDataDTO, dictionaryData);
        dictionaryData.setUpdateTime(DateUtils.getNowDate());
        dictionaryData.setUpdateBy(SecurityUtils.getUserId());
        return dictionaryDataMapper.updateDictionaryData(dictionaryData);
    }

    /**
     * 逻辑批量删除字典数据表
     *
     * @param dictionaryDataIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteDictionaryDataByDictionaryDataIds(List<Long> dictionaryDataIds) {
        //查询code
        List<DictionaryDataDTO> dictionaryDataDTOS = dictionaryDataMapper.selectDictionaryDataByDictionaryDataIds(dictionaryDataIds);
        for (DictionaryDataDTO dictionaryDataDTO : dictionaryDataDTOS) {
            //是否被引用
            this.quoteFlag(dictionaryDataDTO.getDictionaryType(), dictionaryDataDTO);
        }
        return dictionaryDataMapper.logicDeleteDictionaryDataByDictionaryDataIds(dictionaryDataIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除字典数据表信息
     *
     * @param dictionaryDataId 字典数据表主键
     * @return 结果
     */
    @Override
    public int deleteDictionaryDataByDictionaryDataId(Long dictionaryDataId) {
        return dictionaryDataMapper.deleteDictionaryDataByDictionaryDataId(dictionaryDataId);
    }

    /**
     * 根据枚举查询产品应用字典名称数据
     * @return
     */
    @Override
    public DictionaryTypeDTO selectDictionaryTypeByProduct() {
        return dictionaryDataMapper.selectDictionaryTypeByProduct("PRODUCT_CATEGORY");
    }

    /**
     * 根据typeId查询字典数据
     * @param dictionaryTypeId
     * @return
     */
    @Override
    public List<DictionaryDataDTO> selectDictionaryDataByProduct(Long dictionaryTypeId) {
        return dictionaryDataMapper.selectDictionaryDataByProduct(dictionaryTypeId);
    }

    /**
     * 逻辑删除字典数据表信息
     *
     * @param dictionaryDataDTO 字典数据表
     * @return 结果
     */
    @Override
    public int logicDeleteDictionaryDataByDictionaryDataId(DictionaryDataDTO dictionaryDataDTO) {
        DictionaryData dictionaryData = new DictionaryData();
        dictionaryData.setDictionaryDataId(dictionaryDataDTO.getDictionaryDataId());
        dictionaryData.setUpdateTime(DateUtils.getNowDate());
        dictionaryData.setUpdateBy(SecurityUtils.getUserId());
        //查询code
        DictionaryDataDTO dictionaryDataDTO1 = dictionaryDataMapper.selectDictionaryDataByDictionaryDataId(dictionaryData.getDictionaryDataId());
        //是否被引用
        this.quoteFlag(dictionaryDataDTO1.getDictionaryType(), dictionaryDataDTO1);


        return dictionaryDataMapper.logicDeleteDictionaryDataByDictionaryDataId(dictionaryData);
    }

    /**
     * 根据不同枚举类型查询不同表 查看是否被引用
     * @param dictionaryType
     * @param dictionaryDataDTO
     */
    private void quoteFlag(String dictionaryType, DictionaryDataDTO dictionaryDataDTO) {
        StringBuffer dictDataErreo = new StringBuffer();
        switch (dictionaryType) {
            case "PRODUCT_CATEGORY":
                StringBuffer productErreo = new StringBuffer();
                ProductDTO productDTO = new ProductDTO();
                productDTO.setProductCategory(dictionaryDataDTO.getDictionaryDataId().toString());
                productDTO.setListingFlag(Integer.parseInt(dictionaryDataDTO.getDictionaryDataId().toString()));
                R<List<ProductDTO>> listR = remoteDictionaryService.queryProductQuote(productDTO, SecurityConstants.INNER);
                if (null != listR) {
                    List<ProductDTO> data = listR.getData();
                    if (!StringUtils.isEmpty(data)) {
                        productErreo.append("枚举值"+dictionaryDataDTO.getDictionaryLabel()+(dictionaryType=="PRODUCT_CATEGORY"?"被产品类别":"被产品上下架")+data.stream().map(ProductDTO::getProductName).collect(Collectors.toList())+"引用 无法删除！");
                    }
                }
                if (productErreo.length()>1){
                    dictDataErreo.append(productErreo);
                }
                break;
            default:
                break;
        }
        if (dictDataErreo.length()>1){
           throw new ServiceException(dictDataErreo.toString());
        }
    }


    /**
     * 物理删除字典数据表信息
     *
     * @param dictionaryDataDTO 字典数据表
     * @return 结果
     */

    @Override
    public int deleteDictionaryDataByDictionaryDataId(DictionaryDataDTO dictionaryDataDTO) {
        DictionaryData dictionaryData = new DictionaryData();
        BeanUtils.copyProperties(dictionaryDataDTO, dictionaryData);
        return dictionaryDataMapper.deleteDictionaryDataByDictionaryDataId(dictionaryData.getDictionaryDataId());
    }

    /**
     * 物理批量删除字典数据表
     *
     * @param dictionaryDataDtos 需要删除的字典数据表主键
     * @return 结果
     */

    @Override
    public int deleteDictionaryDataByDictionaryDataIds(List<DictionaryDataDTO> dictionaryDataDtos) {
        List<Long> stringList = new ArrayList();
        for (DictionaryDataDTO dictionaryDataDTO : dictionaryDataDtos) {
            stringList.add(dictionaryDataDTO.getDictionaryDataId());
        }
        return dictionaryDataMapper.deleteDictionaryDataByDictionaryDataIds(stringList);
    }

    /**
     * 批量新增字典数据表信息
     *
     * @param dictionaryDataDtos 字典数据表对象
     */

    @Override
    public int insertDictionaryDatas(List<DictionaryDataDTO> dictionaryDataDtos) {
        List<DictionaryData> dictionaryDataList = new ArrayList();

        for (DictionaryDataDTO dictionaryDataDTO : dictionaryDataDtos) {
            DictionaryData dictionaryData = new DictionaryData();
            BeanUtils.copyProperties(dictionaryDataDTO, dictionaryData);
            dictionaryData.setCreateBy(SecurityUtils.getUserId());
            dictionaryData.setCreateTime(DateUtils.getNowDate());
            dictionaryData.setUpdateTime(DateUtils.getNowDate());
            dictionaryData.setUpdateBy(SecurityUtils.getUserId());
            dictionaryData.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            dictionaryDataList.add(dictionaryData);
        }
        return dictionaryDataMapper.batchDictionaryData(dictionaryDataList);
    }

    /**
     * 批量修改字典数据表信息
     *
     * @param dictionaryDataDtos 字典数据表对象
     */

    @Override
    public int updateDictionaryDatas(List<DictionaryDataDTO> dictionaryDataDtos) {
        List<DictionaryData> dictionaryDataList = new ArrayList();

        for (DictionaryDataDTO dictionaryDataDTO : dictionaryDataDtos) {
            DictionaryData dictionaryData = new DictionaryData();
            BeanUtils.copyProperties(dictionaryDataDTO, dictionaryData);
            dictionaryData.setCreateBy(SecurityUtils.getUserId());
            dictionaryData.setCreateTime(DateUtils.getNowDate());
            dictionaryData.setUpdateTime(DateUtils.getNowDate());
            dictionaryData.setUpdateBy(SecurityUtils.getUserId());
            dictionaryDataList.add(dictionaryData);
        }
        return dictionaryDataMapper.updateDictionaryDatas(dictionaryDataList);
    }
}

