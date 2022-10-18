package net.qixiaowei.system.manage.service.impl.productPackage;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.domain.productPackage.ProductPackage;
import net.qixiaowei.system.manage.api.dto.productPackage.ProductPackageDTO;
import net.qixiaowei.system.manage.api.vo.productPackage.ProductPackageVO;
import net.qixiaowei.system.manage.mapper.productPackage.ProductPackageMapper;
import net.qixiaowei.system.manage.service.productPackage.IProductPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * ProductPackageService业务层处理
 *
 * @author TANGMICHI
 * @since 2022-10-09
 */
@Service
public class ProductPackageServiceImpl implements IProductPackageService {
    @Autowired
    private ProductPackageMapper productPackageMapper;

    /**
     * 查询产品包
     *
     * @param productPackageId 产品包主键
     * @return 产品包
     */
    @Override
    public ProductPackageDTO selectProductPackageByProductPackageId(Long productPackageId) {
        return productPackageMapper.selectProductPackageByProductPackageId(productPackageId);
    }

    /**
     * 查询产品包列表
     *
     * @param productPackageDTO 产品包
     * @return 产品包
     */
    @Override
    public List<ProductPackageDTO> selectProductPackageList(ProductPackageDTO productPackageDTO) {
        ProductPackage productPackage = new ProductPackage();
        BeanUtils.copyProperties(productPackageDTO, productPackage);
        return productPackageMapper.selectProductPackageList(productPackage);
    }

    /**
     * 查询所有产品包列表
     *
     * @return 产品包集合
     */
    @Override
    public List<ProductPackageVO> selectProductPackageAll() {
        return productPackageMapper.selectProductPackageAll();
    }


    /**
     * 新增产品包
     *
     * @param productPackageDTO 产品包
     * @return 结果
     */
    @Override
    public int insertProductPackage(ProductPackageDTO productPackageDTO) {
        int sort =1;
        ProductPackageDTO productPackageDTO1 = productPackageMapper.selectProductPackageByProductPackageName(productPackageDTO.getProductPackageName());
        if (null != productPackageDTO1){
            throw new ServiceException("产品包已存在");
        }
        List<ProductPackageDTO> productPackageDTOList1 = productPackageMapper.selectProductPackageList(new ProductPackage());

        ProductPackage productPackage = new ProductPackage();
        BeanUtils.copyProperties(productPackageDTO, productPackage);
        productPackage.setCreateBy(SecurityUtils.getUserId());
        productPackage.setCreateTime(DateUtils.getNowDate());
        productPackage.setUpdateTime(DateUtils.getNowDate());
        productPackage.setUpdateBy(SecurityUtils.getUserId());
        if (!StringUtils.isEmpty(productPackageDTOList1)){
            sort = productPackageDTOList1.get(productPackageDTOList1.size() - 1).getSort();
            productPackage.setSort(sort+1);
        }else {
            productPackage.setSort(sort);
        }
        productPackage.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        return productPackageMapper.insertProductPackage(productPackage);
    }

    /**
     * 修改产品包
     *
     * @param productPackageDTO 产品包
     * @return 结果
     */
    @Override
    public int updateProductPackage(ProductPackageDTO productPackageDTO) {
        ProductPackage productPackage = new ProductPackage();
        BeanUtils.copyProperties(productPackageDTO, productPackage);
        productPackage.setUpdateTime(DateUtils.getNowDate());
        productPackage.setUpdateBy(SecurityUtils.getUserId());
        return productPackageMapper.updateProductPackage(productPackage);
    }

    /**
     * 逻辑批量删除产品包
     *
     * @param productPackageIds 需要删除的产品包主键
     * @return 结果
     */
    @Override
    public int logicDeleteProductPackageByProductPackageIds(List<Long> productPackageIds) {
        return productPackageMapper.logicDeleteProductPackageByProductPackageIds(productPackageIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除产品包信息
     *
     * @param productPackageId 产品包主键
     * @return 结果
     */
    @Override
    public int deleteProductPackageByProductPackageId(Long productPackageId) {
        return productPackageMapper.deleteProductPackageByProductPackageId(productPackageId);
    }

    /**
     * 逻辑删除产品包信息
     *
     * @param productPackageDTO 产品包
     * @return 结果
     */
    @Override
    public int logicDeleteProductPackageByProductPackageId(ProductPackageDTO productPackageDTO) {
        ProductPackage productPackage = new ProductPackage();
        productPackage.setProductPackageId(productPackageDTO.getProductPackageId());
        productPackage.setUpdateTime(DateUtils.getNowDate());
        productPackage.setUpdateBy(SecurityUtils.getUserId());
        return productPackageMapper.logicDeleteProductPackageByProductPackageId(productPackage);
    }

    /**
     * 物理删除产品包信息
     *
     * @param productPackageDTO 产品包
     * @return 结果
     */

    @Override
    public int deleteProductPackageByProductPackageId(ProductPackageDTO productPackageDTO) {
        ProductPackage productPackage = new ProductPackage();
        BeanUtils.copyProperties(productPackageDTO, productPackage);
        return productPackageMapper.deleteProductPackageByProductPackageId(productPackage.getProductPackageId());
    }

    /**
     * 物理批量删除产品包
     *
     * @param productPackageDtos 需要删除的产品包主键
     * @return 结果
     */

    @Override
    public int deleteProductPackageByProductPackageIds(List<ProductPackageDTO> productPackageDtos) {
        List<Long> stringList = new ArrayList();
        for (ProductPackageDTO productPackageDTO : productPackageDtos) {
            stringList.add(productPackageDTO.getProductPackageId());
        }
        return productPackageMapper.deleteProductPackageByProductPackageIds(stringList);
    }

    /**
     * 批量新增产品包信息
     *
     * @param productPackageDtos 产品包对象
     */

    public int insertProductPackages(List<ProductPackageDTO> productPackageDtos) {
        int sort =1;
        List<String> collect = productPackageDtos.stream().map(ProductPackageDTO::getProductPackageName).collect(Collectors.toList());
        List<ProductPackageDTO> productPackageDTOList = productPackageMapper.selectProductPackageByProductPackageNames(collect);
        if (!StringUtils.isEmpty(productPackageDTOList)){
            throw new ServiceException("产品包已存在");
        }
        List<ProductPackageDTO> productPackageDTOList1 = productPackageMapper.selectProductPackageList(new ProductPackage());

        List<ProductPackage> productPackageList = new ArrayList();
        for (ProductPackageDTO productPackageDTO : productPackageDtos) {
            ProductPackage productPackage = new ProductPackage();
            BeanUtils.copyProperties(productPackageDTO, productPackage);
            productPackage.setCreateBy(SecurityUtils.getUserId());
            productPackage.setCreateTime(DateUtils.getNowDate());
            productPackage.setUpdateTime(DateUtils.getNowDate());
            productPackage.setUpdateBy(SecurityUtils.getUserId());
            if (!StringUtils.isEmpty(productPackageDTOList1)){
                sort = productPackageDTOList1.get(productPackageDTOList1.size() - 1).getSort();
                productPackage.setSort(sort+1);
            }else {
                productPackage.setSort(sort);
            }
            productPackage.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            productPackageList.add(productPackage);
            sort++;
        }
        return productPackageMapper.batchProductPackage(productPackageList);
    }

    /**
     * 批量修改产品包信息
     *
     * @param productPackageDtos 产品包对象
     */

    public int updateProductPackages(List<ProductPackageDTO> productPackageDtos) {
        List<ProductPackage> productPackageList = new ArrayList();

        for (ProductPackageDTO productPackageDTO : productPackageDtos) {
            ProductPackage productPackage = new ProductPackage();
            BeanUtils.copyProperties(productPackageDTO, productPackage);
            productPackage.setCreateBy(SecurityUtils.getUserId());
            productPackage.setCreateTime(DateUtils.getNowDate());
            productPackage.setUpdateTime(DateUtils.getNowDate());
            productPackage.setUpdateBy(SecurityUtils.getUserId());
            productPackageList.add(productPackage);
        }
        return productPackageMapper.updateProductPackages(productPackageList);
    }
}

