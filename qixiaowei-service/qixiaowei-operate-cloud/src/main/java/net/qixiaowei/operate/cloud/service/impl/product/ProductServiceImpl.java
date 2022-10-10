package net.qixiaowei.operate.cloud.service.impl.product;

import java.util.Iterator;
import java.util.List;

import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.operate.cloud.api.domain.product.*;
import net.qixiaowei.operate.cloud.api.dto.product.*;
import net.qixiaowei.operate.cloud.mapper.product.*;
import net.qixiaowei.operate.cloud.service.product.IProductSpecificationParamService;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentPostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.service.product.IProductService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import org.springframework.util.CollectionUtils;


/**
 * ProductService业务层处理
 *
 * @author TANGMICHI
 * @since 2022-10-08
 */
@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductSpecificationMapper productSpecificationMapper;
    @Autowired
    private ProductSpecificationParamMapper productSpecificationParamMapper;
    @Autowired
    private ProductSpecificationDataMapper productSpecificationDataMapper;
    @Autowired
    private ProductFileMapper productFileMapper;

    /**
     * 查询产品表
     *
     * @param productId 产品表主键
     * @return 产品表
     */
    @Override
    public ProductDTO selectProductByProductId(Long productId) {
        List<ProductDataDTO> productDataDTOList = new ArrayList<>();
        //产品表
        ProductDTO productDTO = productMapper.selectProductByProductId(productId);
        //产品规格参数表
        List<ProductSpecificationParamDTO> productSpecificationParamDTOS = productSpecificationParamMapper.selectProductId(productId);

        //产品规格表
        List<ProductSpecificationDTO> productSpecificationDTOS = productSpecificationMapper.selectProductId(productId);
        //产品数据表
        List<ProductSpecificationDataDTO> productSpecificationDataDTOList = productSpecificationDataMapper.selectProductId(productId);
        //根据产品规格分组
        Map<Long, List<ProductSpecificationDataDTO>> collect = productSpecificationDataDTOList.stream().collect(Collectors.groupingBy(ProductSpecificationDataDTO::getProductSpecificationId));

        for (ProductSpecificationDTO productSpecificationDTO : productSpecificationDTOS) {
            //产品规格表
            ProductDataDTO productDataDTO = new ProductDataDTO();
            BeanUtils.copyProperties(productSpecificationDTO, productDataDTO);
            productDataDTO.setProductSpecificationDataDTOList(collect.get(productSpecificationDTO.getProductSpecificationId()));
            productDataDTOList.add(productDataDTO);
        }
        //产品规格参数表集合
        productDTO.setProductSpecificationParamDTOList(productSpecificationParamDTOS);
        productDTO.setProductDataDTOList(productDataDTOList);
        return productDTO;
    }

    /**
     * 查询产品表列表
     *
     * @param productDTO 产品表
     * @return 产品表
     */
    @Override
    public List<ProductDTO> selectProductList(ProductDTO productDTO) {
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        return productMapper.selectProductList(product);
    }

    /**
     * 新增产品表
     *
     * @param productDTO 产品表
     * @return 结果
     */
    @Transactional
    @Override
    public int insertProduct(ProductDTO productDTO) {
        int i = 0;
        ProductDTO productDTO1 = productMapper.selectProductByProductCode(productDTO.getProductCode());
        if (null != productDTO1) {
            throw new ServiceException("产品编码已存在！");
        }
        //封装产品表数据
        Product product = this.packProduct(productDTO);

        //父级id
        Long parentProductId = productDTO.getParentProductId();
        if (null != parentProductId) {
            ProductDTO productDTO2 = productMapper.selectProductByProductId(parentProductId);
            product.setParentProductId(productDTO2.getProductId());
            if (productDTO2.getAncestors() == null) {
                //拼接祖级id
                product.setAncestors(productDTO2.getParentProductId() + "," + productDTO2.getProductId());
            } else {
                //拼接祖级id
                product.setAncestors(productDTO2.getAncestors().trim() + "," + productDTO2.getProductId());
            }
        }
        //新增产品表
        try {
            i = productMapper.insertProduct(product);
        } catch (Exception e) {
            throw new ServiceException("新增产品表失败");
        }

        //新增产品参数表
        List<ProductSpecificationParamDTO> productSpecificationParamDTOList = productDTO.getProductSpecificationParamDTOList();
        //封装写入产品参数表数据
        List<ProductSpecificationParam> productSpecificationParamList = this.batchProductSpecificationParam(productSpecificationParamDTOList, product);
        //新增产品数据表
        List<ProductDataDTO> productDataDTOList = productDTO.getProductDataDTOList();

        //写入产品数据表数据
        this.batchProductSpecificationData(productDataDTOList, productSpecificationParamList, product);

        //新增产品文件表
        List<ProductFileDTO> productFileDTOList = productDTO.getProductFileDTOList();
        //封装写入产品文件表数据
        this.packProductFile(productFileDTOList, product);
        //新增产品附件表
        return i;
    }


    /**
     * 封装写入产品参数表数据
     *
     * @param productSpecificationParamDTOList
     * @param product
     */
    public List<ProductSpecificationParam> batchProductSpecificationParam(List<ProductSpecificationParamDTO> productSpecificationParamDTOList, Product product) {
        int i = 1;
        List<ProductSpecificationParam> productSpecificationParamList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(productSpecificationParamDTOList)) {
            for (ProductSpecificationParamDTO productSpecificationParamDTO : productSpecificationParamDTOList) {
                ProductSpecificationParam productSpecificationParam = new ProductSpecificationParam();
                BeanUtils.copyProperties(productSpecificationParamDTO, productSpecificationParam);
                //产品id
                productSpecificationParam.setProductId(product.getProductId());
                //排序
                productSpecificationParam.setSort(i);
                //删除标识
                productSpecificationParam.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                productSpecificationParam.setCreateBy(SecurityUtils.getUserId());
                productSpecificationParam.setCreateTime(DateUtils.getNowDate());
                productSpecificationParam.setUpdateTime(DateUtils.getNowDate());
                productSpecificationParam.setUpdateBy(SecurityUtils.getUserId());
                productSpecificationParamList.add(productSpecificationParam);
                i++;
            }
            if (!CollectionUtils.isEmpty(productSpecificationParamList)) {
                try {
                    productSpecificationParamMapper.batchProductSpecificationParam(productSpecificationParamList);
                } catch (Exception e) {
                    throw new ServiceException("新增产品参数表失败");
                }
            }
        }
        return productSpecificationParamList;
    }

    /**
     * 封装写入产品数据表数据
     *
     * @param productDataDTOList
     * @param productSpecificationParamList
     */
    public void batchProductSpecificationData(List<ProductDataDTO> productDataDTOList, List<ProductSpecificationParam> productSpecificationParamList, Product product) {
        if (!CollectionUtils.isEmpty(productDataDTOList) && !CollectionUtils.isEmpty(productSpecificationParamList)) {
            List<ProductSpecificationData> productSpecificationDataList = new ArrayList<>();
            //封装写入产品规格表数据
            List<ProductSpecification> productSpecificationList = this.batchProductSpecification(productDataDTOList, product);

            if (!CollectionUtils.isEmpty(productDataDTOList)) {
                for (int i = 0; i < productDataDTOList.size(); i++) {
                    List<ProductSpecificationDataDTO> productSpecificationDataDTOList = productDataDTOList.get(i).getProductSpecificationDataDTOList();

                    //如果产品数据为空
                    if (CollectionUtils.isEmpty(productSpecificationDataDTOList)) {
                        for (ProductSpecificationParam productSpecificationParam : productSpecificationParamList) {
                            ProductSpecificationData productSpecificationData = new ProductSpecificationData();
                            //产品ID
                            productSpecificationData.setProductId(productSpecificationParam.getProductId());
                            //产品规格ID
                            productSpecificationData.setProductSpecificationId(productSpecificationList.get(i).getProductSpecificationId());
                            //产品规格参数ID
                            productSpecificationData.setProductSpecificationParamId(productSpecificationParam.getProductSpecificationParamId());
                            productSpecificationData.setCreateBy(SecurityUtils.getUserId());
                            productSpecificationData.setCreateTime(DateUtils.getNowDate());
                            productSpecificationData.setUpdateTime(DateUtils.getNowDate());
                            productSpecificationData.setUpdateBy(SecurityUtils.getUserId());
                            productSpecificationData.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                            productSpecificationDataList.add(productSpecificationData);
                        }
                    } else {
                        //产品数据
                        for (int i1 = 0; i1 < productSpecificationDataDTOList.size(); i1++) {
                            ProductSpecificationData productSpecificationData = new ProductSpecificationData();
                            if (!CollectionUtils.isEmpty(productSpecificationDataDTOList)) {
                                BeanUtils.copyProperties(productSpecificationDataDTOList.get(i1), productSpecificationData);
                            }
                            //产品ID
                            productSpecificationData.setProductId(productSpecificationParamList.get(i1).getProductId());
                            //产品规格ID
                            productSpecificationData.setProductSpecificationId(productSpecificationList.get(i).getProductSpecificationId());
                            //产品规格参数ID
                            productSpecificationData.setProductSpecificationParamId(productSpecificationParamList.get(i1).getProductSpecificationParamId());
                            productSpecificationData.setCreateBy(SecurityUtils.getUserId());
                            productSpecificationData.setCreateTime(DateUtils.getNowDate());
                            productSpecificationData.setUpdateTime(DateUtils.getNowDate());
                            productSpecificationData.setUpdateBy(SecurityUtils.getUserId());
                            productSpecificationData.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                            productSpecificationDataList.add(productSpecificationData);
                        }
                    }
                }
            }

            try {
                if (!CollectionUtils.isEmpty(productSpecificationDataList)) {
                    productSpecificationDataMapper.batchProductSpecificationData(productSpecificationDataList);
                }
            } catch (Exception e) {
                throw new ServiceException("插入产品数据表失败");
            }
        }
    }

    /**
     * 封装写入产品规格表数据
     *
     * @param productDataDTOList
     */
    public List<ProductSpecification> batchProductSpecification(List<ProductDataDTO> productDataDTOList, Product product) {
        //产品规格表
        List<ProductSpecification> productSpecificationList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(productDataDTOList)) {
            for (ProductDataDTO productDataDTO : productDataDTOList) {
                //产品规格表
                ProductSpecification productSpecification = new ProductSpecification();
                BeanUtils.copyProperties(productDataDTO, productSpecification);
                //产品id
                productSpecification.setProductId(product.getProductId());
                productSpecification.setCreateBy(SecurityUtils.getUserId());
                productSpecification.setCreateTime(DateUtils.getNowDate());
                productSpecification.setUpdateTime(DateUtils.getNowDate());
                productSpecification.setUpdateBy(SecurityUtils.getUserId());
                productSpecification.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                productSpecificationList.add(productSpecification);
            }
            if (!CollectionUtils.isEmpty(productSpecificationList)) {
                try {
                    productSpecificationMapper.batchProductSpecification(productSpecificationList);
                } catch (Exception e) {
                    throw new ServiceException("插入产品规格表失败");
                }
            }
        }
        return productSpecificationList;
    }

    /**
     * 封装写入产品文件表数据
     *
     * @param productFileDTOList
     * @param product
     */
    public void packProductFile(List<ProductFileDTO> productFileDTOList, Product product) {
        int i = 1;
        if (!CollectionUtils.isEmpty(productFileDTOList)) {
            List<ProductFile> productFileList = new ArrayList<>();

            for (ProductFileDTO productFileDTO : productFileDTOList) {
                ProductFile productFile = new ProductFile();
                BeanUtils.copyProperties(productFileDTO, productFile);
                //产品id
                productFile.setProductId(product.getProductId());
                //排序
                productFile.setSort(i);
                //删除标识
                productFile.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                productFile.setCreateBy(SecurityUtils.getUserId());
                productFile.setCreateTime(DateUtils.getNowDate());
                productFile.setUpdateTime(DateUtils.getNowDate());
                productFile.setUpdateBy(SecurityUtils.getUserId());
                productFileList.add(productFile);
                i++;
            }
            if (!CollectionUtils.isEmpty(productFileList)) {
                try {
                    productFileMapper.batchProductFile(productFileList);
                } catch (Exception e) {
                    throw new ServiceException("新增产品文件表失败");
                }
            }
        }
    }

    /**
     * 封装产品表数据
     *
     * @param productDTO
     * @return
     */
    public Product packProduct(ProductDTO productDTO) {
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);

        product.setParentProductId(0L);
        product.setCreateBy(SecurityUtils.getUserId());
        product.setCreateTime(DateUtils.getNowDate());
        product.setUpdateTime(DateUtils.getNowDate());
        product.setUpdateBy(SecurityUtils.getUserId());
        product.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        return product;
    }

    /**
     * 修改产品表
     *
     * @param productDTO 产品表
     * @return 结果
     */
    @Transactional
    @Override
    public int updateProduct(ProductDTO productDTO) {
        int i = 0;
        ProductDTO productDTO1 = productMapper.selectProductByProductId(productDTO.getParentProductId());
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);

        if (productDTO1.getAncestors() == null) {
            //拼接祖级id
            product.setAncestors(productDTO1.getParentProductId() + "," + productDTO1.getProductId());
        } else {
            //拼接祖级id
            product.setAncestors(productDTO1.getAncestors().trim() + "," + productDTO1.getProductId());
        }


        //修改产品参数表
        List<ProductSpecificationParamDTO> productSpecificationParamDTOList = productDTO.getProductSpecificationParamDTOList();
        this.updateProductSpecificationParam(productSpecificationParamDTOList,productDTO);
        //新增产品数据表
        List<ProductDataDTO> productDataDTOList = productDTO.getProductDataDTOList();

        //写入产品数据表数据
//        this.batchProductSpecificationData(productDataDTOList, productSpecificationParamList, product);

        //新增产品文件表
        List<ProductFileDTO> productFileDTOList = productDTO.getProductFileDTOList();
        //封装写入产品文件表数据
        this.packProductFile(productFileDTOList, product);
        //新增产品附件表
        return i;
    }

    /**
     * 修改产品参数表
     * @param productSpecificationParamDTOList
     * @param productDTO
     */
    private void updateProductSpecificationParam(List<ProductSpecificationParamDTO> productSpecificationParamDTOList,ProductDTO productDTO) {
        //数据库存在的产品参数集合
        List<ProductSpecificationParamDTO> productSpecificationParamDTOS = productSpecificationParamMapper.selectProductId(productDTO.getProductId());
        //sterm流求差集
        List<Long> collect = productSpecificationParamDTOList.stream().filter(a ->
                !productSpecificationParamDTOS.stream().map(ProductSpecificationParamDTO::getProductSpecificationParamId).collect(Collectors.toList()).contains(a.getProductSpecificationParamId())
        ).collect(Collectors.toList()).stream().map(ProductSpecificationParamDTO::getProductSpecificationParamId).collect(Collectors.toList());
        //不为空删除
        if (!CollectionUtils.isEmpty(collect)){
            try {
                productSpecificationParamMapper.logicDeleteProductSpecificationParamByProductSpecificationParamIds(collect,SecurityUtils.getUserId(),DateUtils.getNowDate());
            } catch (Exception e) {
                throw new ServiceException("删除产品规格参数失败");
            }
        }
        //去除已经删除的id
        for (int i = 0; i < productSpecificationParamDTOList.size(); i++) {
            if (collect.contains(productSpecificationParamDTOList.get(i).getProductSpecificationParamId())){
                productSpecificationParamDTOList.remove(i);
            }
        }
        if (!CollectionUtils.isEmpty(productSpecificationParamDTOList)){
            int i = 1;
            //新增
            List<ProductSpecificationParam> productSpecificationParamAddList = new ArrayList<>();
            //修改
            List<ProductSpecificationParam> productSpecificationParamUpdateList = new ArrayList<>();
            for (ProductSpecificationParamDTO productSpecificationParamDTO : productSpecificationParamDTOList) {
                ProductSpecificationParam productSpecificationParam = new ProductSpecificationParam();
                BeanUtils.copyProperties(productSpecificationParamDTO,productSpecificationParam);
                //排序
                productSpecificationParam.setSort(i);
                if (null == productSpecificationParamDTO.getProductSpecificationParamId()){
                    //产品id
                    productSpecificationParam.setProductId(productDTO.getProductId());
                    //删除标识
                    productSpecificationParam.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                    productSpecificationParam.setCreateBy(SecurityUtils.getUserId());
                    productSpecificationParam.setCreateTime(DateUtils.getNowDate());
                    productSpecificationParam.setUpdateTime(DateUtils.getNowDate());
                    productSpecificationParam.setUpdateBy(SecurityUtils.getUserId());
                }else {
                    productSpecificationParam.setUpdateTime(DateUtils.getNowDate());
                    productSpecificationParam.setUpdateBy(SecurityUtils.getUserId());
                }
                i++;
            }
            if (CollectionUtils.isEmpty(productSpecificationParamAddList)){
                try {
                    productSpecificationParamMapper.batchProductSpecificationParam(productSpecificationParamAddList);
                } catch (Exception e) {
                    throw new ServiceException("新增产品规格参数失败");
                }
            }
            if (CollectionUtils.isEmpty(productSpecificationParamUpdateList)){
                try {
                    productSpecificationParamMapper.updateProductSpecificationParams(productSpecificationParamUpdateList);
                } catch (Exception e) {
                    throw new ServiceException("修改产品规格参数失败");
                }
            }
        }

    }

    /**
     * 逻辑批量删除产品表
     *
     * @param productDtos 需要删除的产品表主键
     * @return 结果
     */

    @Transactional
    @Override
    public int logicDeleteProductByProductIds(List<ProductDTO> productDtos) {
        List<Long> stringList = new ArrayList();
        for (ProductDTO productDTO : productDtos) {
            stringList.add(productDTO.getProductId());
        }
        return productMapper.logicDeleteProductByProductIds(stringList, productDtos.get(0).getUpdateBy(), DateUtils.getNowDate());
    }

    /**
     * 物理删除产品表信息
     *
     * @param productId 产品表主键
     * @return 结果
     */

    @Transactional
    @Override
    public int deleteProductByProductId(Long productId) {
        return productMapper.deleteProductByProductId(productId);
    }

    /**
     * 逻辑删除产品表信息
     *
     * @param productDTO 产品表
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteProductByProductId(ProductDTO productDTO) {
        Product product = new Product();
        product.setProductId(productDTO.getProductId());
        product.setUpdateTime(DateUtils.getNowDate());
        product.setUpdateBy(SecurityUtils.getUserId());
        return productMapper.logicDeleteProductByProductId(product, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除产品表信息
     *
     * @param productDTO 产品表
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteProductByProductId(ProductDTO productDTO) {
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        return productMapper.deleteProductByProductId(product.getProductId());
    }

    /**
     * 物理批量删除产品表
     *
     * @param productDtos 需要删除的产品表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteProductByProductIds(List<ProductDTO> productDtos) {
        List<Long> stringList = new ArrayList();
        for (ProductDTO productDTO : productDtos) {
            stringList.add(productDTO.getProductId());
        }
        return productMapper.deleteProductByProductIds(stringList);
    }

    /**
     * 批量新增产品表信息
     *
     * @param productDtos 产品表对象
     */
    @Transactional
    public int insertProducts(List<ProductDTO> productDtos) {
        List<Product> productList = new ArrayList();

        for (ProductDTO productDTO : productDtos) {
            Product product = new Product();
            BeanUtils.copyProperties(productDTO, product);
            product.setCreateBy(SecurityUtils.getUserId());
            product.setCreateTime(DateUtils.getNowDate());
            product.setUpdateTime(DateUtils.getNowDate());
            product.setUpdateBy(SecurityUtils.getUserId());
            product.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            productList.add(product);
        }
        return productMapper.batchProduct(productList);
    }

    /**
     * 批量修改产品表信息
     *
     * @param productDtos 产品表对象
     */
    @Transactional
    public int updateProducts(List<ProductDTO> productDtos) {
        List<Product> productList = new ArrayList();

        for (ProductDTO productDTO : productDtos) {
            Product product = new Product();
            BeanUtils.copyProperties(productDTO, product);
            product.setCreateBy(SecurityUtils.getUserId());
            product.setCreateTime(DateUtils.getNowDate());
            product.setUpdateTime(DateUtils.getNowDate());
            product.setUpdateBy(SecurityUtils.getUserId());
            productList.add(product);
        }
        return productMapper.updateProducts(productList);
    }
}

