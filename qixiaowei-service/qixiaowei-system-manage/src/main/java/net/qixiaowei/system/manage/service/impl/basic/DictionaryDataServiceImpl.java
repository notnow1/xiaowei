package net.qixiaowei.system.manage.service.impl.basic;

import java.util.List;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.operate.cloud.api.dto.product.ProductDTO;
import net.qixiaowei.operate.cloud.api.remote.dictionary.RemoteDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.domain.basic.DictionaryData;
import net.qixiaowei.system.manage.api.dto.basic.DictionaryDataDTO;
import net.qixiaowei.system.manage.mapper.basic.DictionaryDataMapper;
import net.qixiaowei.system.manage.service.basic.IDictionaryDataService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;

import org.springframework.util.CollectionUtils;


/**
 * DictionaryDataService业务层处理
 *
 * @author TANGMICHI
 * @since 2022-10-07
 */
@Service
public class DictionaryDataServiceImpl implements IDictionaryDataService {
    @Autowired
    private DictionaryDataMapper dictionaryDataMapper;
    @Autowired
    private RemoteDictionaryService remoteDictionaryService;

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
    @Transactional
    @Override
    public int insertDictionaryData(DictionaryDataDTO dictionaryDataDTO) {
        int i = 0;
        DictionaryData dictionaryData = new DictionaryData();
        BeanUtils.copyProperties(dictionaryDataDTO, dictionaryData);
        //根据字典类型ID查询字典数据表
        List<DictionaryDataDTO> dictionaryDataDTOS = dictionaryDataMapper.selectDictionaryTypeId(dictionaryDataDTO.getDictionaryTypeId());
        dictionaryData.setCreateBy(SecurityUtils.getUserId());
        dictionaryData.setDefaultFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        dictionaryData.setCreateTime(DateUtils.getNowDate());
        dictionaryData.setUpdateTime(DateUtils.getNowDate());
        dictionaryData.setUpdateBy(SecurityUtils.getUserId());
        dictionaryData.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        if (CollectionUtils.isEmpty(dictionaryDataDTOS)) {
            dictionaryData.setSort(1);
            i = dictionaryDataMapper.insertDictionaryData(dictionaryData);
        } else {
            dictionaryData.setSort(dictionaryDataDTOS.get(0).getSort() + 1);
            i = dictionaryDataMapper.insertDictionaryData(dictionaryData);
        }

        return i;
    }

    /**
     * 修改字典数据表
     *
     * @param dictionaryDataDTO 字典数据表
     * @return 结果
     */
    @Transactional
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
     * @param dictionaryDataDtos 需要删除的字典数据表主键
     * @return 结果
     */

    @Transactional
    @Override
    public int logicDeleteDictionaryDataByDictionaryDataIds(List<DictionaryDataDTO> dictionaryDataDtos) {
        List<Long> stringList = new ArrayList();
        for (DictionaryDataDTO dictionaryDataDTO : dictionaryDataDtos) {
            stringList.add(dictionaryDataDTO.getDictionaryDataId());
        }
        // todo 暂时不知道被谁引用
        return dictionaryDataMapper.logicDeleteDictionaryDataByDictionaryDataIds(stringList, dictionaryDataDtos.get(0).getUpdateBy(), DateUtils.getNowDate());
    }

    /**
     * 物理删除字典数据表信息
     *
     * @param dictionaryDataId 字典数据表主键
     * @return 结果
     */

    @Transactional
    @Override
    public int deleteDictionaryDataByDictionaryDataId(Long dictionaryDataId) {
        return dictionaryDataMapper.deleteDictionaryDataByDictionaryDataId(dictionaryDataId);
    }

    /**
     * 逻辑删除字典数据表信息
     *
     * @param dictionaryDataDTO 字典数据表
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteDictionaryDataByDictionaryDataId(DictionaryDataDTO dictionaryDataDTO) {
        DictionaryData dictionaryData = new DictionaryData();
        dictionaryData.setDictionaryDataId(dictionaryDataDTO.getDictionaryDataId());
        dictionaryData.setUpdateTime(DateUtils.getNowDate());
        dictionaryData.setUpdateBy(SecurityUtils.getUserId());
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductCategory(dictionaryDataDTO.getDictionaryDataId().toString());
        productDTO.setListingFlag(Integer.parseInt(dictionaryDataDTO.getDictionaryDataId().toString()));
        R<List<ProductDTO>> listR = remoteDictionaryService.queryDictionaryType(productDTO);
        List<ProductDTO> data = listR.getData();
        if (!CollectionUtils.isEmpty(data)){

        }
        // todo 暂时不知道被谁引用
        return dictionaryDataMapper.logicDeleteDictionaryDataByDictionaryDataId(dictionaryData, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除字典数据表信息
     *
     * @param dictionaryDataDTO 字典数据表
     * @return 结果
     */
    @Transactional
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
    @Transactional
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
    @Transactional
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
    @Transactional
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

