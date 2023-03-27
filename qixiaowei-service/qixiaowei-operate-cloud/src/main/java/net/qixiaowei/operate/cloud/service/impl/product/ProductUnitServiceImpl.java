package net.qixiaowei.operate.cloud.service.impl.product;

import java.util.List;

import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.datascope.annotation.DataScope;
import net.qixiaowei.integration.security.utils.UserUtils;
import net.qixiaowei.operate.cloud.api.dto.product.ProductDTO;
import net.qixiaowei.operate.cloud.mapper.product.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.product.ProductUnit;
import net.qixiaowei.operate.cloud.api.dto.product.ProductUnitDTO;
import net.qixiaowei.operate.cloud.mapper.product.ProductUnitMapper;
import net.qixiaowei.operate.cloud.service.product.IProductUnitService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


/**
 * ProductUnitService业务层处理
 *
 * @author TANGMICHI
 * @since 2022-10-08
 */
@Service
public class ProductUnitServiceImpl implements IProductUnitService {
    @Autowired
    private ProductUnitMapper productUnitMapper;

    @Autowired
    private ProductMapper productMapper;

    /**
     * 查询产品单位表
     *
     * @param productUnitId 产品单位表主键
     * @return 产品单位表
     */
    @Override
    public ProductUnitDTO selectProductUnitByProductUnitId(Long productUnitId) {
        return productUnitMapper.selectProductUnitByProductUnitId(productUnitId);
    }

    /**
     * 查询产品单位表列表
     *
     * @param productUnitDTO 产品单位表
     * @return 产品单位表
     */
    @DataScope(businessAlias = "pu")
    @Override
    public List<ProductUnitDTO> selectProductUnitList(ProductUnitDTO productUnitDTO) {
        ProductUnit productUnit = new ProductUnit();
        BeanUtils.copyProperties(productUnitDTO, productUnit);
        List<ProductUnitDTO> productUnitDTOS = productUnitMapper.selectProductUnitList(productUnit);
        this.handleResult(productUnitDTOS);
        return productUnitDTOS;
    }

    @Override
    public void handleResult(List<ProductUnitDTO> result) {
        if (StringUtils.isNotEmpty(result)) {
            Set<Long> userIds = result.stream().map(ProductUnitDTO::getCreateBy).collect(Collectors.toSet());
            Map<Long, String> employeeNameMap = UserUtils.getEmployeeNameMap(userIds);
            result.forEach(entity -> {
                Long userId = entity.getCreateBy();
                entity.setCreateByName(employeeNameMap.get(userId));
            });
        }
    }

    /**
     * 新增产品单位表
     *
     * @param productUnitDTO 产品单位表
     * @return 结果
     */
    @Transactional
    @Override
    public ProductUnitDTO insertProductUnit(ProductUnitDTO productUnitDTO) {
        ProductUnit productUnit = new ProductUnit();
        BeanUtils.copyProperties(productUnitDTO, productUnit);
        ProductUnitDTO productUnitDTO1 = productUnitMapper.selectProductUnitByProductUnitCode(productUnitDTO.getProductUnitCode());
        if (StringUtils.isNotNull(productUnitDTO1)) {
            throw new ServiceException("产品单位编码已存在");
        }
        productUnit.setCreateBy(SecurityUtils.getUserId());
        productUnit.setCreateTime(DateUtils.getNowDate());
        productUnit.setUpdateTime(DateUtils.getNowDate());
        productUnit.setUpdateBy(SecurityUtils.getUserId());
        productUnit.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        productUnitMapper.insertProductUnit(productUnit);
        productUnitDTO.setProductUnitId(productUnit.getProductUnitId());
        return productUnitDTO;
    }

    /**
     * 修改产品单位表
     *
     * @param productUnitDTO 产品单位表
     * @return 结果
     */
    @Transactional
    @Override
    public int updateProductUnit(ProductUnitDTO productUnitDTO) {
        ProductUnit productUnit = new ProductUnit();
        BeanUtils.copyProperties(productUnitDTO, productUnit);
        productUnit.setUpdateTime(DateUtils.getNowDate());
        productUnit.setUpdateBy(SecurityUtils.getUserId());
        return productUnitMapper.updateProductUnit(productUnit);
    }

    /**
     * 逻辑批量删除产品单位表
     *
     * @param productUnitIds 需要删除的产品单位表主键
     * @return 结果
     */

    @Transactional
    @Override
    public int logicDeleteProductUnitByProductUnitIds(List<Long> productUnitIds) {
        List<ProductUnitDTO> productUnitDTOS = productUnitMapper.selectProductUnitByProductUnitIds(productUnitIds);
        if (StringUtils.isEmpty(productUnitDTOS)) {
            throw new ServiceException("产品单位不存在");
        }
        for (ProductUnitDTO productUnitDTO : productUnitDTOS) {
            //查询是否被引用
            List<ProductDTO> productDTOList = productMapper.selectProductByProductUnitId(productUnitDTO.getProductUnitId());
            // 产品引用
            StringBuffer productErreo = new StringBuffer();
            if (!StringUtils.isEmpty(productDTOList)) {
                for (ProductDTO productDTO : productDTOList) {
                    productErreo.append("产品单位" + productUnitDTO.getProductUnitName() + "被" + productDTO.getProductName() + "引用" + "\n");
                }
            }
            if (productErreo.length() > 0) {
                throw new ServiceException(productErreo.toString());
            }
        }

        return productUnitMapper.logicDeleteProductUnitByProductUnitIds(productUnitIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除产品单位表信息
     *
     * @param productUnitId 产品单位表主键
     * @return 结果
     */

    @Transactional
    @Override
    public int deleteProductUnitByProductUnitId(Long productUnitId) {
        return productUnitMapper.deleteProductUnitByProductUnitId(productUnitId);
    }

    /**
     * 逻辑删除产品单位表信息
     *
     * @param productUnitDTO 产品单位表
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteProductUnitByProductUnitId(ProductUnitDTO productUnitDTO) {
        ProductUnit productUnit = new ProductUnit();
        productUnit.setProductUnitId(productUnitDTO.getProductUnitId());
        productUnit.setUpdateTime(DateUtils.getNowDate());
        productUnit.setUpdateBy(SecurityUtils.getUserId());
        ProductUnitDTO productUnitDTO1 = productUnitMapper.selectProductUnitByProductUnitId(productUnitDTO.getProductUnitId());
        //是否被引用
        List<ProductDTO> productDTOList = productMapper.selectProductByProductUnitId(productUnitDTO.getProductUnitId());
        // 产品引用
        StringBuffer productErreo = new StringBuffer();
        if (!StringUtils.isEmpty(productDTOList)) {
            for (ProductDTO productDTO : productDTOList) {
                productErreo.append("产品单位" + productUnitDTO1.getProductUnitName() + "被" + productDTO.getProductName() + "引用" + "\n");
            }
        }
        if (productErreo.length() > 0) {
            throw new ServiceException(productErreo.toString());
        }
        return productUnitMapper.logicDeleteProductUnitByProductUnitId(productUnit, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除产品单位表信息
     *
     * @param productUnitDTO 产品单位表
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteProductUnitByProductUnitId(ProductUnitDTO productUnitDTO) {
        ProductUnit productUnit = new ProductUnit();
        BeanUtils.copyProperties(productUnitDTO, productUnit);
        return productUnitMapper.deleteProductUnitByProductUnitId(productUnit.getProductUnitId());
    }

    /**
     * 物理批量删除产品单位表
     *
     * @param productUnitDtos 需要删除的产品单位表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteProductUnitByProductUnitIds(List<ProductUnitDTO> productUnitDtos) {
        List<Long> stringList = new ArrayList();
        for (ProductUnitDTO productUnitDTO : productUnitDtos) {
            stringList.add(productUnitDTO.getProductUnitId());
        }
        return productUnitMapper.deleteProductUnitByProductUnitIds(stringList);
    }

    /**
     * 批量新增产品单位表信息
     *
     * @param productUnitDtos 产品单位表对象
     */
    @Override
    @Transactional
    public int insertProductUnits(List<ProductUnitDTO> productUnitDtos) {
        List<ProductUnit> productUnitList = new ArrayList();

        for (ProductUnitDTO productUnitDTO : productUnitDtos) {
            ProductUnit productUnit = new ProductUnit();
            BeanUtils.copyProperties(productUnitDTO, productUnit);
            productUnit.setCreateBy(SecurityUtils.getUserId());
            productUnit.setCreateTime(DateUtils.getNowDate());
            productUnit.setUpdateTime(DateUtils.getNowDate());
            productUnit.setUpdateBy(SecurityUtils.getUserId());
            productUnit.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            productUnitList.add(productUnit);
        }
        return productUnitMapper.batchProductUnit(productUnitList);
    }

    /**
     * 批量修改产品单位表信息
     *
     * @param productUnitDtos 产品单位表对象
     */
    @Override
    @Transactional
    public int updateProductUnits(List<ProductUnitDTO> productUnitDtos) {
        List<ProductUnit> productUnitList = new ArrayList();

        for (ProductUnitDTO productUnitDTO : productUnitDtos) {
            ProductUnit productUnit = new ProductUnit();
            BeanUtils.copyProperties(productUnitDTO, productUnit);
            productUnit.setCreateBy(SecurityUtils.getUserId());
            productUnit.setCreateTime(DateUtils.getNowDate());
            productUnit.setUpdateTime(DateUtils.getNowDate());
            productUnit.setUpdateBy(SecurityUtils.getUserId());
            productUnitList.add(productUnit);
        }
        return productUnitMapper.updateProductUnits(productUnitList);
    }
}

