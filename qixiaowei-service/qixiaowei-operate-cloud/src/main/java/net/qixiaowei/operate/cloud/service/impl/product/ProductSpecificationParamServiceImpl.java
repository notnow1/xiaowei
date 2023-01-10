package net.qixiaowei.operate.cloud.service.impl.product;

import java.util.List;

import net.qixiaowei.integration.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.product.ProductSpecificationParam;
import net.qixiaowei.operate.cloud.api.dto.product.ProductSpecificationParamDTO;
import net.qixiaowei.operate.cloud.mapper.product.ProductSpecificationParamMapper;
import net.qixiaowei.operate.cloud.service.product.IProductSpecificationParamService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


/**
 * ProductSpecificationParamService业务层处理
 *
 * @author TANGMICHI
 * @since 2022-10-08
 */
@Service
public class ProductSpecificationParamServiceImpl implements IProductSpecificationParamService {
    @Autowired
    private ProductSpecificationParamMapper productSpecificationParamMapper;

    /**
     * 查询产品规格参数表
     *
     * @param productSpecificationParamId 产品规格参数表主键
     * @return 产品规格参数表
     */
    @Override
    public ProductSpecificationParamDTO selectProductSpecificationParamByProductSpecificationParamId(Long productSpecificationParamId) {
        return productSpecificationParamMapper.selectProductSpecificationParamByProductSpecificationParamId(productSpecificationParamId);
    }

    /**
     * 查询产品规格参数表列表
     *
     * @param productSpecificationParamDTO 产品规格参数表
     * @return 产品规格参数表
     */
    @Override
    public List<ProductSpecificationParamDTO> selectProductSpecificationParamList(ProductSpecificationParamDTO productSpecificationParamDTO) {
        ProductSpecificationParam productSpecificationParam = new ProductSpecificationParam();
        BeanUtils.copyProperties(productSpecificationParamDTO, productSpecificationParam);
        return productSpecificationParamMapper.selectProductSpecificationParamList(productSpecificationParam);
    }

    /**
     * 新增产品规格参数表
     *
     * @param productSpecificationParamDTO 产品规格参数表
     * @return 结果
     */
    @Transactional
    @Override
    public int insertProductSpecificationParam(ProductSpecificationParamDTO productSpecificationParamDTO) {
        ProductSpecificationParam productSpecificationParam = new ProductSpecificationParam();
        BeanUtils.copyProperties(productSpecificationParamDTO, productSpecificationParam);
        productSpecificationParam.setCreateBy(SecurityUtils.getUserId());
        productSpecificationParam.setCreateTime(DateUtils.getNowDate());
        productSpecificationParam.setUpdateTime(DateUtils.getNowDate());
        productSpecificationParam.setUpdateBy(SecurityUtils.getUserId());
        productSpecificationParam.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        return productSpecificationParamMapper.insertProductSpecificationParam(productSpecificationParam);
    }

    /**
     * 修改产品规格参数表
     *
     * @param productSpecificationParamDTO 产品规格参数表
     * @return 结果
     */
    @Transactional
    @Override
    public int updateProductSpecificationParam(ProductSpecificationParamDTO productSpecificationParamDTO) {
        ProductSpecificationParam productSpecificationParam = new ProductSpecificationParam();
        BeanUtils.copyProperties(productSpecificationParamDTO, productSpecificationParam);
        productSpecificationParam.setUpdateTime(DateUtils.getNowDate());
        productSpecificationParam.setUpdateBy(SecurityUtils.getUserId());
        return productSpecificationParamMapper.updateProductSpecificationParam(productSpecificationParam);
    }

    /**
     * 逻辑批量删除产品规格参数表
     *
     * @param productSpecificationParamDtos 需要删除的产品规格参数表主键
     * @return 结果
     */

    @Transactional
    @Override
    public int logicDeleteProductSpecificationParamByProductSpecificationParamIds(List<ProductSpecificationParamDTO> productSpecificationParamDtos) {
        List<Long> stringList = new ArrayList();
        for (ProductSpecificationParamDTO productSpecificationParamDTO : productSpecificationParamDtos) {
            stringList.add(productSpecificationParamDTO.getProductSpecificationParamId());
        }
        return productSpecificationParamMapper.logicDeleteProductSpecificationParamByProductSpecificationParamIds(stringList, productSpecificationParamDtos.get(0).getUpdateBy(), DateUtils.getNowDate());
    }

    /**
     * 物理删除产品规格参数表信息
     *
     * @param productSpecificationParamId 产品规格参数表主键
     * @return 结果
     */

    @Transactional
    @Override
    public int deleteProductSpecificationParamByProductSpecificationParamId(Long productSpecificationParamId) {
        return productSpecificationParamMapper.deleteProductSpecificationParamByProductSpecificationParamId(productSpecificationParamId);
    }

    /**
     * 逻辑删除产品规格参数表信息
     *
     * @param productSpecificationParamDTO 产品规格参数表
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteProductSpecificationParamByProductSpecificationParamId(ProductSpecificationParamDTO productSpecificationParamDTO) {
        ProductSpecificationParam productSpecificationParam = new ProductSpecificationParam();
        productSpecificationParam.setProductSpecificationParamId(productSpecificationParamDTO.getProductSpecificationParamId());
        productSpecificationParam.setUpdateTime(DateUtils.getNowDate());
        productSpecificationParam.setUpdateBy(SecurityUtils.getUserId());
        return productSpecificationParamMapper.logicDeleteProductSpecificationParamByProductSpecificationParamId(productSpecificationParam, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除产品规格参数表信息
     *
     * @param productSpecificationParamDTO 产品规格参数表
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteProductSpecificationParamByProductSpecificationParamId(ProductSpecificationParamDTO productSpecificationParamDTO) {
        ProductSpecificationParam productSpecificationParam = new ProductSpecificationParam();
        BeanUtils.copyProperties(productSpecificationParamDTO, productSpecificationParam);
        return productSpecificationParamMapper.deleteProductSpecificationParamByProductSpecificationParamId(productSpecificationParam.getProductSpecificationParamId());
    }

    /**
     * 物理批量删除产品规格参数表
     *
     * @param productSpecificationParamDtos 需要删除的产品规格参数表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteProductSpecificationParamByProductSpecificationParamIds(List<ProductSpecificationParamDTO> productSpecificationParamDtos) {
        List<Long> stringList = new ArrayList();
        for (ProductSpecificationParamDTO productSpecificationParamDTO : productSpecificationParamDtos) {
            stringList.add(productSpecificationParamDTO.getProductSpecificationParamId());
        }
        return productSpecificationParamMapper.deleteProductSpecificationParamByProductSpecificationParamIds(stringList);
    }

    /**
     * 批量新增产品规格参数表信息
     *
     * @param productSpecificationParamDtos 产品规格参数表对象
     */
    @Override
    @Transactional
    public int insertProductSpecificationParams(List<ProductSpecificationParamDTO> productSpecificationParamDtos) {
        List<ProductSpecificationParam> productSpecificationParamList = new ArrayList();

        for (ProductSpecificationParamDTO productSpecificationParamDTO : productSpecificationParamDtos) {
            ProductSpecificationParam productSpecificationParam = new ProductSpecificationParam();
            BeanUtils.copyProperties(productSpecificationParamDTO, productSpecificationParam);
            productSpecificationParam.setCreateBy(SecurityUtils.getUserId());
            productSpecificationParam.setCreateTime(DateUtils.getNowDate());
            productSpecificationParam.setUpdateTime(DateUtils.getNowDate());
            productSpecificationParam.setUpdateBy(SecurityUtils.getUserId());
            productSpecificationParam.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            productSpecificationParamList.add(productSpecificationParam);
        }
        return productSpecificationParamMapper.batchProductSpecificationParam(productSpecificationParamList);
    }

    /**
     * 批量修改产品规格参数表信息
     *
     * @param productSpecificationParamDtos 产品规格参数表对象
     */
    @Override
    @Transactional
    public int updateProductSpecificationParams(List<ProductSpecificationParamDTO> productSpecificationParamDtos) {
        List<ProductSpecificationParam> productSpecificationParamList = new ArrayList();

        for (ProductSpecificationParamDTO productSpecificationParamDTO : productSpecificationParamDtos) {
            ProductSpecificationParam productSpecificationParam = new ProductSpecificationParam();
            BeanUtils.copyProperties(productSpecificationParamDTO, productSpecificationParam);
            productSpecificationParam.setCreateBy(SecurityUtils.getUserId());
            productSpecificationParam.setCreateTime(DateUtils.getNowDate());
            productSpecificationParam.setUpdateTime(DateUtils.getNowDate());
            productSpecificationParam.setUpdateBy(SecurityUtils.getUserId());
            productSpecificationParamList.add(productSpecificationParam);
        }
        return productSpecificationParamMapper.updateProductSpecificationParams(productSpecificationParamList);
    }
}

