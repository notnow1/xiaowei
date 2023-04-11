package net.qixiaowei.operate.cloud.service.impl.product;

import net.qixiaowei.integration.common.config.FileConfig;
import net.qixiaowei.integration.common.constant.Constants;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.enums.PrefixCodeRule;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.CheckObjectIsNullUtils;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.datascope.annotation.DataScope;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.integration.security.utils.UserUtils;
import net.qixiaowei.operate.cloud.api.domain.product.*;
import net.qixiaowei.operate.cloud.api.dto.product.*;
import net.qixiaowei.operate.cloud.excel.product.ProductExcel;
import net.qixiaowei.operate.cloud.excel.product.ProductExportExcel;
import net.qixiaowei.operate.cloud.mapper.product.*;
import net.qixiaowei.operate.cloud.service.product.IProductService;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignDTO;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignParamDTO;
import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.*;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.AnnualKeyWorkDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMeasureDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMetricsDTO;
import net.qixiaowei.strategy.cloud.api.remote.businessDesign.RemoteBusinessDesignService;
import net.qixiaowei.strategy.cloud.api.remote.gap.RemoteGapAnalysisService;
import net.qixiaowei.strategy.cloud.api.remote.marketInsight.*;
import net.qixiaowei.strategy.cloud.api.remote.strategyDecode.RemoteAnnualKeyWorkService;
import net.qixiaowei.strategy.cloud.api.remote.strategyDecode.RemoteStrategyMeasureService;
import net.qixiaowei.strategy.cloud.api.remote.strategyDecode.RemoteStrategyMetricsService;
import net.qixiaowei.system.manage.api.dto.basic.DictionaryDataDTO;
import net.qixiaowei.system.manage.api.dto.basic.DictionaryTypeDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import net.qixiaowei.system.manage.api.dto.basic.OfficialRankDecomposeDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDictionaryDataService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndicatorService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteOfficialRankSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


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
    private ProductUnitMapper productUnitMapper;
    @Autowired
    private ProductSpecificationMapper productSpecificationMapper;
    @Autowired
    private ProductSpecificationParamMapper productSpecificationParamMapper;
    @Autowired
    private ProductSpecificationDataMapper productSpecificationDataMapper;
    @Autowired
    private ProductFileMapper productFileMapper;
    @Autowired
    private FileConfig fileConfig;
    @Autowired
    private RemoteDictionaryDataService remoteDictionaryDataService;
    @Autowired
    private RemoteIndicatorService remoteIndicatorService;
    @Autowired
    private RemoteOfficialRankSystemService remoteOfficialRankSystemService;
    @Autowired
    private RemoteAnnualKeyWorkService remoteAnnualKeyWorkService;
    @Autowired
    private RemoteStrategyMeasureService remoteStrategyMeasureService;
    @Autowired
    private RemoteStrategyMetricsService remoteStrategyMetricsService;
    @Autowired
    private RemoteGapAnalysisService remoteGapAnalysisService;
    @Autowired
    private RemoteBusinessDesignService remoteBusinessDesignService;



    @Autowired
    private RemoteMarketInsightCustomerService remoteMarketInsightCustomerService;
    @Autowired
    private RemoteMarketInsightIndustryService remoteMarketInsightIndustryService;
    @Autowired
    private RemoteMarketInsightMacroService remoteMarketInsightMacroService;
    @Autowired
    private RemoteMarketInsightOpponentService remoteMarketInsightOpponentService;
    @Autowired
    private RemoteMarketInsightSelfService remoteMarketInsightSelfService;

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
        this.packProductCategoryName(productDTO);
        //产品文件表
        List<ProductFileDTO> productFileDTOS = productFileMapper.selectProductFileByProductId(productId);
        //拼接文件路径
        for (ProductFileDTO productFileDTO : productFileDTOS) {
            productFileDTO.setProductFilePath(fileConfig.getFullDomain(productFileDTO.getProductFilePath()));
        }
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
        productDTO.setProductFileDTOList(productFileDTOS);
        return productDTO;
    }

    /**
     * 查询产品表列表-列表结构
     *
     * @param productDTO 产品表
     * @return 产品表集合
     */
    @Override
    public List<ProductDTO> selectProduct(ProductDTO productDTO) {
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        //查询数据
        return productMapper.selectProductList(product);
    }

    @Override
    public void handleResult(List<ProductDTO> result) {
        if (StringUtils.isNotEmpty(result)) {
            Set<Long> userIds = result.stream().map(ProductDTO::getCreateBy).collect(Collectors.toSet());
            Map<Long, String> employeeNameMap = UserUtils.getEmployeeNameMap(userIds);
            result.forEach(entity -> {
                Long userId = entity.getCreateBy();
                entity.setCreateByName(employeeNameMap.get(userId));
            });
        }
    }

    /**
     * 封装产品类别名称
     *
     * @param productDTO
     */
    private void packProductCategoryName(ProductDTO productDTO) {
        if (StringUtils.isNotNull(productDTO)) {
            String productCategory = productDTO.getProductCategory();
            if (StringUtils.isNotBlank(productCategory)) {
                //远程调用查询字典数据
                R<DictionaryDataDTO> info = remoteDictionaryDataService.info(Long.valueOf(productCategory), SecurityConstants.INNER);
                DictionaryDataDTO data = info.getData();
                if (StringUtils.isNotNull(data)) {
                    productDTO.setProductCategoryName(data.getDictionaryLabel());
                }
            }

        }
    }

    /**
     * 查询产品表列表
     *
     * @param productDTO 产品表
     * @return 产品表
     */
    @DataScope(businessAlias = "p")
    @Override
    public List<ProductDTO> selectProductList(ProductDTO productDTO) {
        Product product = new Product();
        Map<String, Object> params = productDTO.getParams();
        if (StringUtils.isNotEmpty(params)) {
            DictionaryDataDTO dictionaryDataDTO = new DictionaryDataDTO();
            Map<String, Object> params2 = new HashMap<>();
            for (String key : params.keySet()) {
                switch (key) {
                    case "dictionaryLabelEqual":
                        params2.put("dictionaryLabelEqual", params.get("dictionaryLabelEqual"));
                        break;
                    case "dictionaryLabelNotEqual":
                        params2.put("dictionaryLabelNotEqual", params.get("dictionaryLabelNotEqual"));
                        break;
                }
            }
            if (StringUtils.isNotEmpty(params2)) {
                dictionaryDataDTO.setParams(params2);
                R<List<DictionaryDataDTO>> listR = remoteDictionaryDataService.remoteDictionaryDataId(dictionaryDataDTO, SecurityConstants.INNER);
                if (listR.getCode() != 200) {
                    throw new ServiceException("远程查询字典表失败 请联系管理员");
                }
                List<DictionaryDataDTO> dictionaryDataDTOList = listR.getData();
                if (StringUtils.isNotEmpty(dictionaryDataDTOList)) {
                    List<Long> dictionaryDataIds = dictionaryDataDTOList.stream().map(DictionaryDataDTO::getDictionaryDataId).collect(Collectors.toList());
                    params.put("dictionaryDataIds", dictionaryDataIds);
                } else {
                    return new ArrayList<>();
                }
            }
        }
        BeanUtils.copyProperties(productDTO, product);
        //查询数据
        List<ProductDTO> productDTOList = productMapper.selectProductList(product);
        if (StringUtils.isNotEmpty(productDTOList)) {
            List<String> collect = productDTOList.stream().map(ProductDTO::getProductCategory).filter(StringUtils::isNotBlank).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)) {
                List<Long> collect2 = collect.stream().filter(StringUtils::isNotBlank).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(collect2)) {
                    //远程调用查询字典数据
                    R<List<DictionaryDataDTO>> listR = remoteDictionaryDataService.selectDictionaryDataByDictionaryDataIds(collect2, SecurityConstants.INNER);
                    List<DictionaryDataDTO> data = listR.getData();
                    if (StringUtils.isNotEmpty(data)) {
                        for (ProductDTO dto : productDTOList) {
                            for (DictionaryDataDTO datum : data) {
                                if (StringUtils.isNotBlank(dto.getProductCategory())) {
                                    if (Long.valueOf(dto.getProductCategory()).equals(datum.getDictionaryDataId())) {
                                        dto.setProductCategoryName(datum.getDictionaryLabel());
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
        this.handleResult(productDTOList);
        if (StringUtils.isNotEmpty(productDTO.getParams())){
            return productDTOList;
        }
        if (!CheckObjectIsNullUtils.isNull(productDTO) ) {
            return productDTOList;
        } else {
            if (StringUtils.isEmpty(productDTOList)) {
                return productDTOList;
            } else {
                return this.createTree(productDTOList, 0);
            }
        }
    }

    /**
     * 查询产品表列表-平铺下拉
     *
     * @param productDTO 产品表
     * @return 产品表集合
     */
    @Override
    public List<ProductDTO> selectDropList(ProductDTO productDTO) {
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        return productMapper.selectProductList(product);
    }

    /**
     * 树形结构
     *
     * @param lists
     * @param pid
     * @return
     */
    private List<ProductDTO> createTree(List<ProductDTO> lists, int pid) {
        List<ProductDTO> tree = new ArrayList<>();
        for (ProductDTO catelog : lists) {
            if (catelog.getParentProductId() == pid) {
                catelog.setChildren(createTree(lists, Integer.parseInt(catelog.getProductId().toString())));
                tree.add(catelog);
            }
        }
        return tree;
    }

    /**
     * 删除自己及下级树形结构
     *
     * @param productDTOList
     * @param productId
     * @return
     */
    private void remoteTree(List<ProductDTO> productDTOList, Long productId) {
        for (int i = productDTOList.size() - 1; i >= 0; i--) {
            List<ProductDTO> children = productDTOList.get(i).getChildren();
            if (children != null && children.size() > 0) {
                if (productDTOList.get(i).getProductId().equals(productId)) {
                    productDTOList.remove(productDTOList.get(i));
                } else {
                    remoteTree(children, productId);
                }
            } else {
                if (productDTOList.get(i).getProductId().equals(productId)) {
                    productDTOList.remove(productDTOList.get(i));
                }
            }
        }
    }

    /**
     * 树形数据转list
     *
     * @param ProductDTOList
     * @return
     */
    private List<ProductDTO> treeToList(List<ProductDTO> ProductDTOList) {
        List<ProductDTO> allSysMenuDto = new ArrayList<>();
        for (ProductDTO productdto : ProductDTOList) {
            List<ProductDTO> children = productdto.getChildren();
            allSysMenuDto.add(productdto);
            if (children != null && children.size() > 0) {
                allSysMenuDto.addAll(treeToList(children));
                productdto.setChildren(null);
            }
        }
        return allSysMenuDto;
    }

    /**
     * 生成产品编码
     *
     * @return 产品编码
     */
    @Override
    public String generateProductCode() {
        String productCode;
        int number = 1;
        String prefixCodeRule = PrefixCodeRule.PRODUCT.getCode();
        List<String> productCodes = productMapper.getProductCodes(prefixCodeRule);
        if (StringUtils.isNotEmpty(productCodes)) {
            for (String code : productCodes) {
                if (StringUtils.isEmpty(code) || code.length() != 8) {
                    continue;
                }
                code = code.replaceFirst(prefixCodeRule, "");
                try {
                    int codeOfNumber = Integer.parseInt(code);
                    if (number != codeOfNumber) {
                        break;
                    }
                    number++;
                } catch (Exception ignored) {
                }
            }
        }
        if (number > 1000000) {
            throw new ServiceException("流水号溢出，请联系管理员");
        }
        productCode = "000000" + number;
        productCode = prefixCodeRule + productCode.substring(productCode.length() - 6);
        return productCode;
    }

    /**
     * 新增产品表
     *
     * @param productDTO 产品表
     * @return 结果
     */
    @Transactional
    @Override
    public ProductDTO insertProduct(ProductDTO productDTO) {
        ProductDTO productDTO1 = productMapper.selectProductByProductCode(productDTO.getProductCode());
        if (null != productDTO1) {
            throw new ServiceException("产品编码已存在");
        }
        //封装产品表数据
        Product product = this.packProduct(productDTO);

        //父级id
        Long parentProductId = productDTO.getParentProductId();
        if (null != parentProductId && !parentProductId.equals(0L)) {
            ProductDTO productDTO2 = productMapper.selectProductByProductId(parentProductId);
            if (StringUtils.isNull(productDTO2)) {
                throw new ServiceException("上级不存在！请刷新页面重试");
            }
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
            productMapper.insertProduct(product);
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
        this.batchProductFile(productFileDTOList, product);
        productDTO.setProductId(product.getProductId());
        return productDTO;
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
        if (!StringUtils.isEmpty(productSpecificationParamDTOList)) {
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
            if (!StringUtils.isEmpty(productSpecificationParamList)) {
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
        if (!StringUtils.isEmpty(productDataDTOList) && !StringUtils.isEmpty(productSpecificationParamList)) {
            List<ProductSpecificationData> productSpecificationDataList = new ArrayList<>();
            //封装写入产品规格表数据
            List<ProductSpecification> productSpecificationList = this.batchProductSpecification(productDataDTOList, product);

            if (!StringUtils.isEmpty(productDataDTOList)) {
                for (int i = 0; i < productDataDTOList.size(); i++) {
                    List<ProductSpecificationDataDTO> productSpecificationDataDTOList = productDataDTOList.get(i).getProductSpecificationDataDTOList();

                    //如果产品数据为空
                    if (StringUtils.isEmpty(productSpecificationDataDTOList)) {
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
                            if (!StringUtils.isEmpty(productSpecificationDataDTOList)) {
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
                if (!StringUtils.isEmpty(productSpecificationDataList)) {
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
        if (!StringUtils.isEmpty(productDataDTOList)) {
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
            if (!StringUtils.isEmpty(productSpecificationList)) {
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
    public void batchProductFile(List<ProductFileDTO> productFileDTOList, Product product) {
        int i = 1;
        if (!StringUtils.isEmpty(productFileDTOList)) {
            List<ProductFile> productFileList = new ArrayList<>();

            for (ProductFileDTO productFileDTO : productFileDTOList) {
                ProductFile productFile = new ProductFile();
                BeanUtils.copyProperties(productFileDTO, productFile);
                //产品id
                productFile.setProductId(product.getProductId());
                //产品文件路径
                productFile.setProductFilePath(fileConfig.getPathOfRemoveDomain(productFileDTO.getProductFilePath()));
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
            if (!StringUtils.isEmpty(productFileList)) {
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

        product.setParentProductId(Constants.TOP_PARENT_ID);
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
        int num = 0;
        ProductDTO productDTO1 = productMapper.selectProductByProductId(productDTO.getProductId());
        if (StringUtils.isNull(productDTO1)) {
            throw new ServiceException("产品不存在！");
        }
        Product product = new Product();
        List<Product> productList = new ArrayList<>();
        if (productDTO.getParentProductId() == 0) {
            BeanUtils.copyProperties(productDTO, product);
            product.setUpdateTime(DateUtils.getNowDate());
            product.setAncestors("");
            product.setUpdateBy(SecurityUtils.getUserId());
            product.setParentProductId(Constants.TOP_PARENT_ID);
        } else {
            ProductDTO productDTO2 = productMapper.selectProductByProductId(productDTO.getParentProductId());
            BeanUtils.copyProperties(productDTO, product);
            product.setUpdateTime(DateUtils.getNowDate());
            product.setUpdateBy(SecurityUtils.getUserId());
            if (StringUtils.isBlank(productDTO2.getAncestors())) {
                product.setAncestors(productDTO2.getParentProductId() + "," + productDTO2.getProductId());
            } else {
                product.setAncestors(productDTO2.getAncestors() + "," + productDTO2.getParentProductId());
            }
        }
        Map<Long, Integer> map = new HashMap<>();
        List<ProductDTO> productDTOList = productMapper.selectAncestors(product.getProductId());
        for (int i1 = 0; i1 < productDTOList.size(); i1++) {
            map.put(productDTOList.get(i1).getProductId(), i1);
        }
        if (StringUtils.isNotEmpty(productDTOList) && productDTOList.size() > 1) {
            for (int i1 = 1; i1 < productDTOList.size(); i1++) {
                if (i1 == 1) {
                    Product product2 = new Product();
                    if (StringUtils.isBlank(product.getAncestors())) {
                        productDTOList.get(i1).setAncestors(product.getParentProductId() + "," + product.getProductId());
                    } else {
                        productDTOList.get(i1).setAncestors(product.getAncestors() + "," + product.getProductId());
                    }
                    productDTOList.get(i1).setLevel(product.getLevel() + 1);

                    productDTOList.get(i1).setUpdateTime(DateUtils.getNowDate());
                    productDTOList.get(i1).setUpdateBy(SecurityUtils.getUserId());
                    productDTOList.get(i1).setParentProductId(product.getProductId());
                    BeanUtils.copyProperties(productDTOList.get(i1), product2);
                    productList.add(product2);
                } else {
                    if (productDTOList.get(i1 - 1).getProductId().equals(productDTOList.get(i1).getParentProductId())) {
                        Product product2 = new Product();
                        //父级
                        ProductDTO productDTO2 = productDTOList.get(i1 - 1);
                        if (StringUtils.isBlank(productDTO2.getAncestors())) {
                            productDTOList.get(i1).setAncestors(productDTO2.getParentProductId() + "," + productDTO2.getProductId());
                        } else {
                            productDTOList.get(i1).setAncestors(productDTO2.getAncestors() + "," + productDTO2.getProductId());
                        }
                        productDTOList.get(i1).setLevel(productDTO2.getLevel() + 1);
                        productDTOList.get(i1).setUpdateTime(DateUtils.getNowDate());
                        productDTOList.get(i1).setUpdateBy(SecurityUtils.getUserId());
                        productDTOList.get(i1).setParentProductId(productDTO2.getProductId());
                        BeanUtils.copyProperties(productDTOList.get(i1), product2);
                        productList.add(product2);
                    } else {
                        Product product2 = new Product();

                        //父级
                        ProductDTO productDTO2 = productDTOList.get(map.get(productDTOList.get(i1).getParentProductId()));
                        if (StringUtils.isBlank(productDTO2.getAncestors())) {
                            productDTOList.get(i1).setAncestors(productDTO2.getParentProductId() + "," + productDTO2.getProductId());
                        } else {
                            productDTOList.get(i1).setAncestors(productDTO2.getAncestors() + "," + productDTO2.getProductId());
                        }
                        productDTOList.get(i1).setLevel(productDTO2.getLevel() + 1);
                        productDTOList.get(i1).setUpdateTime(DateUtils.getNowDate());
                        productDTOList.get(i1).setUpdateBy(SecurityUtils.getUserId());
                        productDTOList.get(i1).setParentProductId(productDTO2.getProductId());
                        BeanUtils.copyProperties(productDTOList.get(i1), product2);
                        productList.add(product2);
                    }
                }
            }

        }
        //产品参数表
        List<ProductSpecificationParamDTO> productSpecificationParamDTOList = new ArrayList<>();
        //修改产品参数表
        List<ProductSpecificationParam> productSpecificationParamList = new ArrayList<>();
        //新增产品数据表
        List<ProductDataDTO> productDataDTOList = new ArrayList<>();
        if (StringUtils.isNotEmpty(productDTO.getProductSpecificationParamDTOList())) {
            productSpecificationParamDTOList = productDTO.getProductSpecificationParamDTOList();
            //修改产品参数表
            productSpecificationParamList = this.updateProductSpecificationParam(productSpecificationParamDTOList, productDTO);
        }

        if (StringUtils.isNotEmpty(productDTO.getProductDataDTOList())) {
            productDataDTOList = productDTO.getProductDataDTOList();
            //修改产品规格表数据
            List<ProductDataDTO> productDataDTOS = this.updateProductSpecification(productDataDTOList, productDTO);
            //修改产品数据表数据
            this.updateProductSpecificationData(productDataDTOS, productSpecificationParamList, productDTO);
        }

        //修改产品文件表
        List<ProductFileDTO> productFileDTOList = productDTO.getProductFileDTOList();
        //修改产品文件表数据
        this.updateProductFile(productFileDTOList, productDTO);
        num = productMapper.updateProduct(product);
        if (StringUtils.isNotEmpty(productList)) {
            try {
                productMapper.updateProducts(productList);
            } catch (Exception e) {
                throw new ServiceException("批量修改产品失败!");
            }
        }
        return num;
    }

    /**
     * 修改产品文件表
     *
     * @param productFileDTOList
     * @param productDTO
     */
    private void updateProductFile(List<ProductFileDTO> productFileDTOList, ProductDTO productDTO) {
        //查询数据库已存在的文件id集合
        List<ProductFileDTO> productFileDTOS = productFileMapper.selectProductFileByProductId(productDTO.getProductId());
        //sterm流求差集
        List<Long> collect = productFileDTOS.stream().filter(a ->
                !productFileDTOList.stream().map(ProductFileDTO::getProductFileId).collect(Collectors.toList()).contains(a.getProductFileId())
        ).collect(Collectors.toList()).stream().map(ProductFileDTO::getProductFileId).collect(Collectors.toList());
        //删除不存在的id
        if (!StringUtils.isEmpty(collect)) {
            productFileMapper.logicDeleteProductFileByProductFileIds(collect, SecurityUtils.getUserId(), DateUtils.getNowDate());
        }
        //去除已经删除的文件id
        for (int i = 0; i < productFileDTOList.size(); i++) {
            if (collect.contains(productFileDTOList.get(i).getProductFileId())) {
                productFileDTOList.remove(i);
            }
        }
        if (!StringUtils.isEmpty(productFileDTOList)) {
            int i = 1;
            //新增
            List<ProductFile> productFileAddList = new ArrayList<>();
            //修改
            List<ProductFile> productFileUpdateList = new ArrayList<>();

            for (ProductFileDTO productFileDTO : productFileDTOList) {
                ProductFile productFile = new ProductFile();
                BeanUtils.copyProperties(productFileDTO, productFile);
                if (null == productFileDTO.getProductFileId()) {
                    //产品id
                    productFile.setProductId(productDTO.getProductId());
                    //排序
                    productFile.setSort(i);
                    //删除标识
                    productFile.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                    productFile.setCreateBy(SecurityUtils.getUserId());
                    productFile.setCreateTime(DateUtils.getNowDate());
                    productFile.setUpdateTime(DateUtils.getNowDate());
                    productFile.setUpdateBy(SecurityUtils.getUserId());
                    productFileAddList.add(productFile);
                } else {
                    productFile.setUpdateTime(DateUtils.getNowDate());
                    productFile.setUpdateBy(SecurityUtils.getUserId());
                    productFileUpdateList.add(productFile);
                }
                i++;
            }
            if (!StringUtils.isEmpty(productFileAddList)) {
                try {
                    productFileMapper.batchProductFile(productFileAddList);
                } catch (Exception e) {
                    throw new ServiceException("新增产品文件表失败");
                }
            }
            if (!StringUtils.isEmpty(productFileUpdateList)) {
                try {
                    productFileMapper.updateProductFiles(productFileUpdateList);
                } catch (Exception e) {
                    throw new ServiceException("修改产品文件表失败");
                }
            }
        }
    }

    /**
     * 修改产品规格表数据
     *
     * @param productDataDTOList
     * @param productDTO
     */
    private List<ProductDataDTO> updateProductSpecification(List<ProductDataDTO> productDataDTOList, ProductDTO productDTO) {
        //新增产品规格表
        List<ProductSpecification> productSpecificationAddList = new ArrayList<>();
        //修改产品规格表
        List<ProductSpecification> productSpecificationUpdateList = new ArrayList<>();

        List<ProductSpecificationDTO> productSpecificationDTOS = productSpecificationMapper.selectProductId(productDTO.getProductId());
        //sterm流求差集
        List<Long> collect = productSpecificationDTOS.stream().filter(a ->
                !productDataDTOList.stream().map(ProductDataDTO::getProductSpecificationId).collect(Collectors.toList()).contains(a.getProductSpecificationId())
        ).collect(Collectors.toList()).stream().map(ProductSpecificationDTO::getProductSpecificationId).collect(Collectors.toList());

        if (StringUtils.isNotEmpty(collect)) {
            try {
                productSpecificationMapper.logicDeleteProductSpecificationByProductSpecificationIds(collect, SecurityUtils.getUserId(), DateUtils.getNowDate());
            } catch (Exception e) {
                throw new ServiceException("批量删除规格表失败");
            }
        }
        //去除已经删除的id
        for (int i = 0; i < productDataDTOList.size(); i++) {
            if (collect.contains(productDataDTOList.get(i).getProductSpecificationId())) {
                productDataDTOList.remove(i);
            }
        }

        if (!StringUtils.isEmpty(productDataDTOList)) {
            for (ProductDataDTO productDataDTO : productDataDTOList) {
                //产品规格表
                ProductSpecification productSpecification = new ProductSpecification();
                BeanUtils.copyProperties(productDataDTO, productSpecification);

                if (StringUtils.isNull(productDataDTO.getProductSpecificationId())) {
                    productSpecification.setProductId(productDTO.getProductId());
                    productSpecification.setCreateTime(DateUtils.getNowDate());
                    productSpecification.setCreateBy(SecurityUtils.getUserId());
                    productSpecification.setUpdateTime(DateUtils.getNowDate());
                    productSpecification.setUpdateBy(SecurityUtils.getUserId());
                    productSpecification.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                    productSpecificationAddList.add(productSpecification);
                } else {
                    productSpecification.setUpdateTime(DateUtils.getNowDate());
                    productSpecification.setUpdateBy(SecurityUtils.getUserId());
                    productSpecificationUpdateList.add(productSpecification);
                }

            }

            if (!StringUtils.isEmpty(productSpecificationAddList)) {
                try {
                    productSpecificationMapper.batchProductSpecification(productSpecificationAddList);
                } catch (Exception e) {
                    throw new ServiceException("批量新增产品规格表失败");
                }
            }
            if (!StringUtils.isEmpty(productSpecificationUpdateList)) {
                try {
                    productSpecificationMapper.updateProductSpecifications(productSpecificationUpdateList);
                } catch (Exception e) {
                    throw new ServiceException("批量修改产品规格表失败");
                }
            }
        }
        int addi = 0;
        if (StringUtils.isNotEmpty(productDataDTOList)) {
            for (int i = 0; i < productDataDTOList.size(); i++) {
                if (StringUtils.isNull(productDataDTOList.get(i).getProductSpecificationId())) {
                    if (StringUtils.isNotEmpty(productSpecificationAddList)) {
                        productDataDTOList.get(i).setProductSpecificationId(productSpecificationAddList.get(addi).getProductSpecificationId());
                        addi++;
                    }
                }
            }
        }

        return productDataDTOList;
    }

    /**
     * 修改产品数据表数据
     *
     * @param productDataDTOList
     * @param productDTO
     */
    private void updateProductSpecificationData(List<ProductDataDTO> productDataDTOList, List<ProductSpecificationParam> productSpecificationParamList, ProductDTO productDTO) {
        //产品数据入参
        List<ProductSpecificationDataDTO> productSpecificationDataDTOList = new ArrayList<>();
        for (ProductDataDTO productDataDTO : productDataDTOList) {
            if (StringUtils.isNotNull(productDataDTO) && StringUtils.isNotEmpty(productDataDTO.getProductSpecificationDataDTOList())){
                productSpecificationDataDTOList.addAll(productDataDTO.getProductSpecificationDataDTOList());
            }
        }
        //查询数据库存在的产品数据
        List<ProductSpecificationDataDTO> productSpecificationDataDTOs = productSpecificationDataMapper.selectProductId(productDTO.getProductId());

        //sterm流求差集
        List<Long> collect = productSpecificationDataDTOs.stream().filter(a ->
                !productSpecificationDataDTOList.stream().map(ProductSpecificationDataDTO::getProductSpecificationDataId).collect(Collectors.toList()).contains(a.getProductSpecificationDataId())
        ).collect(Collectors.toList()).stream().map(ProductSpecificationDataDTO::getProductSpecificationDataId).collect(Collectors.toList());
        //批量删除
        if (!StringUtils.isEmpty(collect)) {
            productSpecificationDataMapper.logicDeleteProductSpecificationDataByProductSpecificationDataIds(collect, SecurityUtils.getUserId(), DateUtils.getNowDate());
        }

        //去除已经删除的id
        for (int i = 0; i < productDataDTOList.size(); i++) {
            List<ProductSpecificationDataDTO> productSpecificationDataDTOList1 = productDataDTOList.get(i).getProductSpecificationDataDTOList();
            for (int i1 = 0; i1 < productSpecificationDataDTOList1.size(); i1++) {
                if (collect.contains(productSpecificationDataDTOList1.get(i1).getProductSpecificationDataId())) {
                    productSpecificationDataDTOList1.remove(i1);
                }
            }
        }

        //新增
        List<ProductSpecificationData> productSpecificationDataAddList = new ArrayList<>();
        //修改
        List<ProductSpecificationData> productSpecificationDataUpdateList = new ArrayList<>();
        if (!StringUtils.isEmpty(productDataDTOList)) {
            for (int i = 0; i < productDataDTOList.size(); i++) {
                List<ProductSpecificationDataDTO> productSpecificationDataDTOList1 = productDataDTOList.get(i).getProductSpecificationDataDTOList();
                for (int i1 = 0; i1 < productSpecificationDataDTOList1.size(); i1++) {
                    ProductSpecificationData productSpecificationData = new ProductSpecificationData();
                    BeanUtils.copyProperties(productSpecificationDataDTOList1.get(i1), productSpecificationData);
                    if (null == productSpecificationDataDTOList1.get(i1).getProductSpecificationDataId()) {
                        //产品id
                        productSpecificationData.setProductId(productDTO.getProductId());
                        //产品规格id
                        productSpecificationData.setProductSpecificationId(productDataDTOList.get(i).getProductSpecificationId());
                        //产品参数id
                        productSpecificationData.setProductSpecificationParamId(productSpecificationParamList.get(i1).getProductSpecificationParamId());
                        productSpecificationData.setCreateBy(SecurityUtils.getUserId());
                        productSpecificationData.setCreateTime(DateUtils.getNowDate());
                        productSpecificationData.setUpdateTime(DateUtils.getNowDate());
                        productSpecificationData.setUpdateBy(SecurityUtils.getUserId());
                        productSpecificationData.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                        productSpecificationDataAddList.add(productSpecificationData);
                    } else {
                        productSpecificationData.setUpdateTime(DateUtils.getNowDate());
                        productSpecificationData.setUpdateBy(SecurityUtils.getUserId());
                        productSpecificationDataUpdateList.add(productSpecificationData);
                    }
                }
            }
        }
        if (!StringUtils.isEmpty(productSpecificationDataAddList)) {
            try {
                productSpecificationDataMapper.batchProductSpecificationData(productSpecificationDataAddList);
            } catch (Exception e) {
                throw new ServiceException("新增产品数据失败");
            }
        }
        if (!StringUtils.isEmpty(productSpecificationDataUpdateList)) {
            try {
                productSpecificationDataMapper.updateProductSpecificationDatasOfNull(productSpecificationDataUpdateList);
            } catch (Exception e) {
                throw new ServiceException("修改产品数据失败");
            }
        }
    }

    /**
     * 修改产品参数表
     *
     * @param productSpecificationParamDTOList
     * @param productDTO
     */
    private List<ProductSpecificationParam> updateProductSpecificationParam(List<ProductSpecificationParamDTO> productSpecificationParamDTOList, ProductDTO productDTO) {
        //返回数据
        List<ProductSpecificationParam> productSpecificationParamAllList = new ArrayList<>();
        //数据库存在的产品参数集合
        List<ProductSpecificationParamDTO> productSpecificationParamDTOS = productSpecificationParamMapper.selectProductId(productDTO.getProductId());
        //sterm流求差集
        List<Long> collect = productSpecificationParamDTOS.stream().filter(a ->
                !productSpecificationParamDTOList.stream().map(ProductSpecificationParamDTO::getProductSpecificationParamId).collect(Collectors.toList()).contains(a.getProductSpecificationParamId())
        ).collect(Collectors.toList()).stream().map(ProductSpecificationParamDTO::getProductSpecificationParamId).collect(Collectors.toList());
        //不为空删除
        if (!StringUtils.isEmpty(collect)) {
            try {
                productSpecificationParamMapper.logicDeleteProductSpecificationParamByProductSpecificationParamIds(collect, SecurityUtils.getUserId(), DateUtils.getNowDate());
            } catch (Exception e) {
                throw new ServiceException("删除产品规格参数失败");
            }
        }
        //去除已经删除的id
        for (int i = 0; i < productSpecificationParamDTOList.size(); i++) {
            if (collect.contains(productSpecificationParamDTOList.get(i).getProductSpecificationParamId())) {
                productSpecificationParamDTOList.remove(i);
            }
        }
        if (!StringUtils.isEmpty(productSpecificationParamDTOList)) {
            int i = 1;
            //新增
            List<ProductSpecificationParam> productSpecificationParamAddList = new ArrayList<>();
            //修改
            List<ProductSpecificationParam> productSpecificationParamUpdateList = new ArrayList<>();
            for (ProductSpecificationParamDTO productSpecificationParamDTO : productSpecificationParamDTOList) {
                ProductSpecificationParam productSpecificationParam = new ProductSpecificationParam();
                BeanUtils.copyProperties(productSpecificationParamDTO, productSpecificationParam);
                //排序
                productSpecificationParam.setSort(i);
                if (null == productSpecificationParamDTO.getProductSpecificationParamId()) {
                    //产品id
                    productSpecificationParam.setProductId(productDTO.getProductId());
                    //删除标识
                    productSpecificationParam.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                    productSpecificationParam.setCreateBy(SecurityUtils.getUserId());
                    productSpecificationParam.setCreateTime(DateUtils.getNowDate());
                    productSpecificationParam.setUpdateTime(DateUtils.getNowDate());
                    productSpecificationParam.setUpdateBy(SecurityUtils.getUserId());
                    productSpecificationParamAddList.add(productSpecificationParam);
                } else {
                    productSpecificationParam.setUpdateTime(DateUtils.getNowDate());
                    productSpecificationParam.setUpdateBy(SecurityUtils.getUserId());
                    productSpecificationParamUpdateList.add(productSpecificationParam);
                }
                i++;
            }
            if (!StringUtils.isEmpty(productSpecificationParamAddList)) {
                try {
                    productSpecificationParamMapper.batchProductSpecificationParam(productSpecificationParamAddList);
                    //添加新增
                    productSpecificationParamAllList.addAll(productSpecificationParamAddList);
                } catch (Exception e) {
                    throw new ServiceException("新增产品规格参数失败");
                }
            }
            if (!StringUtils.isEmpty(productSpecificationParamUpdateList)) {
                try {
                    productSpecificationParamMapper.updateProductSpecificationParams(productSpecificationParamUpdateList);
                    //添加修改
                    productSpecificationParamAllList.addAll(productSpecificationParamUpdateList);
                } catch (Exception e) {
                    throw new ServiceException("修改产品规格参数失败");
                }
            }
            if (!StringUtils.isEmpty(productSpecificationParamAllList)) {
                //排序
                productSpecificationParamAllList.stream().sorted(Comparator.comparing(ProductSpecificationParam::getSort));
            }
        }

        return productSpecificationParamAllList;
    }

    /**
     * 逻辑批量删除产品表
     *
     * @param productIds 需要删除的产品表主键
     * @return 结果
     */

    @Transactional
    @Override
    public int logicDeleteProductByProductIds(List<Long> productIds) {
        //产品引用
        StringBuffer productErreo = new StringBuffer();
        //分解引用
        StringBuffer decomposeErreo = new StringBuffer();
        int i = 0;
        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Long productId : productIds) {
            productDTOS.addAll(productMapper.selectAncestors(productId));
        }
        if (StringUtils.isNotEmpty(productDTOS) && productDTOS.get(0) != null) {
            List<ProductDTO> productDTOList = productDTOS.stream().distinct().collect(Collectors.toList());
            //远程职级分解
            List<Long> productIdList = new ArrayList<>();
            if (StringUtils.isNotEmpty(productDTOList)){
                productIdList= productDTOS.stream().map(ProductDTO::getProductId).collect(Collectors.toList());
            }

            R<List<OfficialRankDecomposeDTO>> officialRankDecomposeDTOList = remoteOfficialRankSystemService.selectOfficialDecomposeByDimensions(productIdList, 4, SecurityConstants.INNER);
            List<OfficialRankDecomposeDTO> officialRankDecomposeData = officialRankDecomposeDTOList.getData();
            if (StringUtils.isNotEmpty(officialRankDecomposeData)) {
                List<String> officialRankSystemNames = new ArrayList<>();
                for (OfficialRankDecomposeDTO officialRankDecomposeDatum : officialRankDecomposeData) {
                    if (productIdList.contains(officialRankDecomposeDatum.getDecomposeDimension())) {
                        officialRankSystemNames.add(officialRankDecomposeDatum.getOfficialRankSystemName());
                    }
                }
                if (StringUtils.isNotEmpty(officialRankSystemNames)) {
                    throw new ServiceException("数据被引用！");
                    //decomposeErreo.append("产品" + productDTO.getProductName() + "已被职级体系名称[" + StringUtils.join(",", officialRankSystemNames) + "] 职级分解引用\n");
                }
            }
            //是否被目标分解引用
            List<ProductDTO> productDTOList1 = productMapper.selectProductQuote(productIdList);
            //指标名称 远程调用
            if (StringUtils.isNotEmpty(productDTOList1)) {
                List<Long> indicatorIds = productDTOList1.stream().map(ProductDTO::getIndicatorId).collect(Collectors.toList());

                R<List<IndicatorDTO>> listR = remoteIndicatorService.selectIndicatorByIds(indicatorIds, SecurityConstants.INNER);
                List<IndicatorDTO> data = listR.getData();
                if (StringUtils.isNotEmpty(data)) {
                    String indicatorName = data.stream().map(IndicatorDTO::getIndicatorName).distinct().collect(Collectors.toList()).toString();
                    if (StringUtils.isNotBlank(indicatorName)) {
                        throw new ServiceException("数据被引用！");
                        //decomposeErreo.append("产品" + productDTO.getProductName() + "已被目标分解" + indicatorName + "引用\n");
                    }
                }
            }
            // 战略云-其他引用
            isStrategyQuote(productIds);
        }
        productErreo.append(decomposeErreo);
        if (productErreo.length() > 0) {
            throw new ServiceException(productErreo.toString());
        }

        try {
            i = productMapper.logicDeleteProductByProductIds(productIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
        } catch (Exception e) {
            throw new ServiceException("删除产品表失败");
        }

        //规格表
        try {
            productSpecificationMapper.logicDeleteProductSpecificationByProductIds(productIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
        } catch (Exception e) {
            throw new ServiceException("删除规格表失败");
        }
        //参数表
        try {
            productSpecificationParamMapper.logicDeleteProductSpecificationParamByProductIds(productIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
        } catch (Exception e) {
            throw new ServiceException("删除规格参数失败");
        }

        //数据表
        try {
            productSpecificationDataMapper.logicDeleteProductSpecificationDataByProductIds(productIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
        } catch (Exception e) {
            throw new ServiceException("删除产品数据失败");
        }
        //删除文件
        try {
            productFileMapper.logicDeleteProductFileByProductIds(productIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
        } catch (Exception e) {
            throw new ServiceException("删除产品文件表失败");
        }
        return i;
    }

    /**
     * 产品战略云引用
     *
     * @param productIds 产品ID集合
     */
    private void isStrategyQuote(List<Long> productIds) {
        Map<String, Object> params;
        AnnualKeyWorkDTO annualKeyWorkDTO = new AnnualKeyWorkDTO();
        params = new HashMap<>();
        params.put("productIdEqual", productIds);
        annualKeyWorkDTO.setParams(params);
        R<List<AnnualKeyWorkDTO>> remoteAnnualKeyWorkR = remoteAnnualKeyWorkService.remoteAnnualKeyWork(annualKeyWorkDTO, SecurityConstants.INNER);
        List<AnnualKeyWorkDTO> annualKeyWorkDTOS = remoteAnnualKeyWorkR.getData();
        if (remoteAnnualKeyWorkR.getCode() != 200) {
            throw new ServiceException("远程调用年度重点工作失败");
        }
        if (StringUtils.isNotEmpty(annualKeyWorkDTOS)) {
            throw new ServiceException("数据被引用!");
        }
        StrategyMeasureDTO strategyMeasureDTO = new StrategyMeasureDTO();
        params = new HashMap<>();
        params.put("productIdEqual", productIds);
        strategyMeasureDTO.setParams(params);
        R<List<StrategyMeasureDTO>> remoteStrategyMeasureR = remoteStrategyMeasureService.remoteStrategyMeasure(strategyMeasureDTO, SecurityConstants.INNER);
        List<StrategyMeasureDTO> strategyMeasureDTOS = remoteStrategyMeasureR.getData();
        if (remoteStrategyMeasureR.getCode() != 200) {
            throw new ServiceException("远程调用战略举措清单失败");
        }
        if (StringUtils.isNotEmpty(strategyMeasureDTOS)) {
            throw new ServiceException("数据被引用!");
        }
        StrategyMetricsDTO strategyMetricsDTO = new StrategyMetricsDTO();
        params = new HashMap<>();
        params.put("productIdEqual", productIds);
        strategyMetricsDTO.setParams(params);
        R<List<StrategyMetricsDTO>> remoteStrategyMetricsR = remoteStrategyMetricsService.remoteStrategyMetrics(strategyMetricsDTO, SecurityConstants.INNER);
        List<StrategyMetricsDTO> strategyMetricsDTOS = remoteStrategyMetricsR.getData();
        if (remoteStrategyMetricsR.getCode() != 200) {
            throw new ServiceException("远程调用战略衡量指标失败");
        }
        if (StringUtils.isNotEmpty(strategyMetricsDTOS)) {
            throw new ServiceException("数据被引用!");
        }
        GapAnalysisDTO gapAnalysisDTO = new GapAnalysisDTO();
        params = new HashMap<>();
        params.put("productIdEqual", productIds);
        gapAnalysisDTO.setParams(params);
        R<List<GapAnalysisDTO>> remoteGapAnalysisR = remoteGapAnalysisService.remoteGapAnalysis(gapAnalysisDTO, SecurityConstants.INNER);
        List<GapAnalysisDTO> gapAnalysisDTOS = remoteGapAnalysisR.getData();
        if (remoteGapAnalysisR.getCode() != 200) {
            throw new ServiceException("远程调用差距分析失败");
        }
        if (StringUtils.isNotEmpty(gapAnalysisDTOS)) {
            throw new ServiceException("数据被引用!");
        }
        BusinessDesignDTO businessDesignDTO = new BusinessDesignDTO();
        params = new HashMap<>();
        params.put("productIdEqual", productIds);
        businessDesignDTO.setParams(params);
        R<List<BusinessDesignDTO>> remoteBusinessDesignR = remoteBusinessDesignService.remoteBusinessDesign(businessDesignDTO, SecurityConstants.INNER);
        List<BusinessDesignDTO> businessDesignDTOS = remoteBusinessDesignR.getData();
        if (remoteBusinessDesignR.getCode() != 200) {
            throw new ServiceException("远程调用业务设计失败");
        }
        if (StringUtils.isNotEmpty(businessDesignDTOS)) {
            throw new ServiceException("数据被引用!");
        }
        BusinessDesignParamDTO businessDesignParamDTO = new BusinessDesignParamDTO();
        params = new HashMap<>();
        params.put("productIds", productIds);
        businessDesignParamDTO.setParams(params);
        R<List<BusinessDesignParamDTO>> businessDesignParamsDTOSR = remoteBusinessDesignService.remoteBusinessDesignParams(businessDesignParamDTO, SecurityConstants.INNER);
        List<BusinessDesignParamDTO> businessDesignParamsDTOS = businessDesignParamsDTOSR.getData();
        if (businessDesignParamsDTOSR.getCode() != 200) {
            throw new ServiceException("远程调用业务设计失败");
        }
        if (StringUtils.isNotEmpty(businessDesignParamsDTOS)) {
            throw new ServiceException("数据被引用!");
        }
        MarketInsightCustomerDTO marketInsightCustomerDTO = new MarketInsightCustomerDTO();
        params = new HashMap<>();
        params.put("productIds", productIds);
        marketInsightCustomerDTO.setParams(params);
        //看客户远程查询是否引用
        R<List<MarketInsightCustomerDTO>> marketInsightCustomerList = remoteMarketInsightCustomerService.remoteMarketInsightCustomerList(marketInsightCustomerDTO, SecurityConstants.INNER);
        List<MarketInsightCustomerDTO> marketInsightCustomerListData = marketInsightCustomerList.getData();
        if (StringUtils.isNotEmpty(marketInsightCustomerListData)){
            throw new ServiceException("数据被引用！");
        }
        MiCustomerInvestDetailDTO miCustomerInvestDetailDTO = new MiCustomerInvestDetailDTO();
        params = new HashMap<>();
        params.put("productIds", productIds);
        miCustomerInvestDetailDTO.setParams(params);
        //看客户投资计划详情远程查询是否引用
        R<List<MiCustomerInvestDetailDTO>> miCustomerInvestDetailList = remoteMarketInsightCustomerService.remoteMiCustomerInvestDetailList(miCustomerInvestDetailDTO, SecurityConstants.INNER);
        List<MiCustomerInvestDetailDTO> miCustomerInvestDetailListData = miCustomerInvestDetailList.getData();
        if (StringUtils.isNotEmpty(miCustomerInvestDetailListData)){
            throw new ServiceException("数据被引用！");
        }
        MarketInsightIndustryDTO marketInsightIndustryDTO = new MarketInsightIndustryDTO();
        params = new HashMap<>();
        params.put("productIds", productIds);
        marketInsightIndustryDTO.setParams(params);
        //看行业远程查询是否引用
        R<List<MarketInsightIndustryDTO>> marketInsightIndustryList = remoteMarketInsightIndustryService.remoteMarketInsightIndustryList(marketInsightIndustryDTO, SecurityConstants.INNER);
        List<MarketInsightIndustryDTO> marketInsightIndustryListData = marketInsightIndustryList.getData();
        if (StringUtils.isNotEmpty(marketInsightIndustryListData)){
            throw new ServiceException("数据被引用！");
        }
        MarketInsightMacroDTO marketInsightMacroDTO = new MarketInsightMacroDTO();
        params = new HashMap<>();
        params.put("productIds", productIds);
        marketInsightMacroDTO.setParams(params);
        //看宏观远程查询是否引用
        R<List<MarketInsightMacroDTO>> marketInsightMacroList = remoteMarketInsightMacroService.remoteMarketInsightMacroList(marketInsightMacroDTO, SecurityConstants.INNER);
        List<MarketInsightMacroDTO> marketInsightMacroListData = marketInsightMacroList.getData();
        if (StringUtils.isNotEmpty(marketInsightMacroListData)){
            throw new ServiceException("数据被引用！");
        }
        MarketInsightOpponentDTO marketInsightOpponentDTO = new MarketInsightOpponentDTO();
        params = new HashMap<>();
        params.put("productIds", productIds);
        marketInsightOpponentDTO.setParams(params);
        //看对手远程查询是否引用
        R<List<MarketInsightOpponentDTO>> marketInsightOpponentList = remoteMarketInsightOpponentService.remoteMarketInsightOpponentList(marketInsightOpponentDTO, SecurityConstants.INNER);
        List<MarketInsightOpponentDTO> marketInsightOpponentListData = marketInsightOpponentList.getData();
        if (StringUtils.isNotEmpty(marketInsightOpponentListData)){
            throw new ServiceException("数据被引用！");
        }
        MarketInsightSelfDTO marketInsightSelfDTO = new MarketInsightSelfDTO();
        params = new HashMap<>();
        params.put("productIds", productIds);
        marketInsightSelfDTO.setParams(params);
        //看自身远程查询是否引用
        R<List<MarketInsightSelfDTO>> marketInsightSelfList = remoteMarketInsightSelfService.remoteMarketInsightSelfList(marketInsightSelfDTO, SecurityConstants.INNER);
        List<MarketInsightSelfDTO> marketInsightSelfListData = marketInsightSelfList.getData();
        if (StringUtils.isNotEmpty(marketInsightSelfListData)){
            throw new ServiceException("数据被引用！");
        }
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
     * 查询产品是否用到枚举
     *
     * @param productDTO
     * @return
     */
    @Override
    public List<ProductDTO> queryProductQuote(ProductDTO productDTO) {
        Product product = new Product();
        product.setProductCategory(productDTO.getProductCategory());
        product.setListingFlag(productDTO.getListingFlag());
        return productMapper.queryProductQuote(product);
    }

    /**
     * 返回产品层级
     *
     * @return
     */
    @Override
    public List<Integer> selectLevel() {
        return productMapper.selectLevel();
    }

    /**
     * 查询上级产品
     *
     * @param productId
     * @return
     */
    @Override
    public List<ProductDTO> queryparent(Long productId) {
        Product product = new Product();
        List<ProductDTO> tree = this.createTree(productMapper.selectProductList(product), 0);
        if (StringUtils.isNotNull(productId)) {
            if (StringUtils.isNotEmpty(tree)) {
                this.remoteTree(tree, productId);
            }

        }
        return tree;

    }

    /**
     * 根据产品IDS获取产品列表
     *
     * @param productIds 产品IDS
     * @return
     */
    @Override
    public List<ProductDTO> selectProductList(List<Long> productIds) {
        return productMapper.selectProductListByProductIds(productIds);
    }

    @Override
    @Transactional
    public void importProduct(List<ProductExcel> list) {
        Product productData = new Product();
        List<ProductDTO> productDTOS = productMapper.selectProductList(productData);
        List<String> productCodeList = new ArrayList<>();
        if (StringUtils.isNotEmpty(productDTOS)) {
            productCodeList = productDTOS.stream().map(ProductDTO::getProductCode).collect(Collectors.toList());
        }
        //产品单位名称集合
        ProductUnit productUnit = new ProductUnit();
        List<ProductUnitDTO> productUnitDTOS = productUnitMapper.selectProductUnitList(productUnit);

        //产品类别值
        List<DictionaryDataDTO> dictionaryDataDTOList = new ArrayList<>();
        //远程调用字典数据
        R<DictionaryTypeDTO> dictionaryTypeDTOR = remoteDictionaryDataService.selectDictionaryTypeByCode("PRODUCT_CATEGORY", SecurityConstants.INNER);
        DictionaryTypeDTO dictionaryTypeDTO = dictionaryTypeDTOR.getData();
        if (StringUtils.isNotNull(dictionaryTypeDTO)) {
            R<List<DictionaryDataDTO>> listR = remoteDictionaryDataService.selectDictionaryDataByProduct(dictionaryTypeDTO.getDictionaryTypeId(), SecurityConstants.INNER);
            dictionaryDataDTOList = listR.getData().stream().filter(f ->f.getStatus() == 1).collect(Collectors.toList());
        }
        //新增list
        List<ProductExcel> successExcelList = new ArrayList<>();
        //失败List
        List<ProductExcel> errorExcelList = new ArrayList<>();

        if (StringUtils.isNotEmpty(list)) {
            try {
                //返回报错信息
                StringBuffer productErreo = new StringBuffer();
                //根据产品code进行分组
                Map<String, List<ProductExcel>> productExcelMap = list.parallelStream().collect(Collectors.groupingBy(ProductExcel::getProductCode, LinkedHashMap::new, Collectors.toList()));

                for (String key : productExcelMap.keySet()) {
                    Product product = new Product();
                    //分组后的数据
                    List<ProductExcel> productExcels = productExcelMap.get(key);
                    //产品规格参数集合
                    List<ProductSpecificationParam> productSpecificationParamList = new ArrayList<>();
                    //产品规格表
                    List<ProductSpecification> productSpecificationList = new ArrayList<>();
                    //产品规格数据表集合
                    List<ProductSpecificationData> productSpecificationDataList = new ArrayList<>();
                    if (StringUtils.isNotEmpty(productExcels)) {

                        for (int i = 0; i < productExcels.size(); i++) {
                            StringBuffer stringBuffer1 = this.validProduct(productExcels.get(i), productUnitDTOS, i, productCodeList);
                            if (stringBuffer1.length() > 1) {
                                errorExcelList.addAll(productExcels);
                                productErreo.append(stringBuffer1);
                                break;
                            }
                            if (i == 0) {
                                //父级产品编码
                                String parentProductCode = productExcels.get(i).getParentProductCode();
                                if (StringUtils.isBlank(parentProductCode)) {
                                    product.setLevel(1);
                                    product.setParentProductId(0L);
                                } else {
                                    ProductDTO productDTO = productMapper.selectProductByProductCode(parentProductCode);
                                    if (StringUtils.isNull(productDTO)) {
                                        errorExcelList.addAll(productExcels);
                                        productErreo.append(parentProductCode + "父级编码不存在！");
                                        break;
                                    } else {
                                        //祖籍编码
                                        String ancestors = productDTO.getAncestors();
                                        product.setParentProductId(productDTO.getProductId());
                                        if (ancestors == null) {
                                            //拼接祖级id
                                            product.setAncestors(productDTO.getParentProductId() + "," + productDTO.getProductId());
                                        } else {
                                            //拼接祖级id
                                            product.setAncestors(productDTO.getAncestors().trim() + "," + productDTO.getProductId());
                                        }
                                        product.setLevel(productDTO.getLevel() + 1);
                                    }
                                }

                                //产品单位
                                String productUnitName = productExcels.get(i).getProductUnitName();
                                //产品类别
                                String productCategoryName = productExcels.get(i).getProductCategoryName();
                                //是否上下架
                                String listingFlag = productExcels.get(i).getListingFlag();

                                if (StringUtils.isNotEmpty(productUnitDTOS)) {
                                    //产品单位id
                                    List<ProductUnitDTO> ProductUnitDTOS = productUnitDTOS.stream().filter(f -> StringUtils.equals(productUnitName, f.getProductUnitName())).collect(Collectors.toList());
                                    if (StringUtils.isNotEmpty(ProductUnitDTOS)) {
                                        product.setProductUnitId(ProductUnitDTOS.get(0).getProductUnitId());
                                    }
                                }

                                if (StringUtils.isNotEmpty(dictionaryDataDTOList)) {
                                    //产品类别
                                    List<DictionaryDataDTO> productCategoryNames = dictionaryDataDTOList.stream().filter(f -> StringUtils.equals(productCategoryName, f.getDictionaryLabel())).collect(Collectors.toList());
                                    if (StringUtils.isNotEmpty(productCategoryNames)) {
                                        product.setProductCategory(productCategoryNames.get(0).getDictionaryDataId().toString());
                                    }
                                }
                                if (StringUtils.isNotBlank(listingFlag)){
                                    //是否上下架
                                    if (StringUtils.equals(listingFlag, "上架")) {
                                        product.setListingFlag(1);
                                    } else if (StringUtils.equals(listingFlag, "下架")) {
                                        product.setListingFlag(0);
                                    }
                                }else {
                                    product.setListingFlag(1);
                                }
                                //产品描述
                                product.setProductDescription(productExcels.get(i).getProductDescription());
                                //产品编码
                                product.setProductCode(productExcels.get(i).getProductCode());
                                //产品名称
                                product.setProductName(productExcels.get(i).getProductName());
                                product.setDeleteFlag(0);
                                product.setCreateBy(SecurityUtils.getUserId());
                                product.setCreateTime(DateUtils.getNowDate());
                                product.setUpdateTime(DateUtils.getNowDate());
                                product.setUpdateBy(SecurityUtils.getUserId());
                                ProductDTO productDTO = productMapper.selectProductByProductCode(productExcels.get(i).getProductCode());
                                if (StringUtils.isNotNull(productDTO)) {
                                    errorExcelList.addAll(productExcels);
                                    productErreo.append(productExcels.get(i).getProductCode() + "编码已存在！");
                                    break;
                                }
                                productMapper.insertProduct(product);
                                //产品参数集合
                                List<ProductSpecificationParamDTO> productSpecificationParamDTOList = productExcels.get(i).getProductSpecificationParamDTOList();
                                if (StringUtils.isNotEmpty(productSpecificationParamDTOList)) {
                                    for (int i1 = 0; i1 < productSpecificationParamDTOList.size(); i1++) {
                                        ProductSpecificationParam productSpecificationParam = new ProductSpecificationParam();
                                        BeanUtils.copyProperties(productSpecificationParamDTOList.get(i1), productSpecificationParam);
                                        //产品id
                                        productSpecificationParam.setProductId(product.getProductId());
                                        //排序
                                        productSpecificationParam.setSort(i1 + 1);
                                        productSpecificationParam.setDeleteFlag(0);
                                        productSpecificationParam.setCreateBy(SecurityUtils.getUserId());
                                        productSpecificationParam.setCreateTime(DateUtils.getNowDate());
                                        productSpecificationParam.setUpdateTime(DateUtils.getNowDate());
                                        productSpecificationParam.setUpdateBy(SecurityUtils.getUserId());
                                        productSpecificationParamList.add(productSpecificationParam);
                                    }
                                }
                                if (StringUtils.isNotEmpty(productSpecificationParamList)) {
                                    //批量插入产品规格参数表
                                    productSpecificationParamMapper.batchProductSpecificationParam(productSpecificationParamList);
                                }
                                //只有一行数据 直接插入 并跳出循环
                                if (productExcels.size() == 1) {
                                    List<ProductDataDTO> productDataDTOList = productExcels.get(i).getProductDataDTOList();
                                    if (StringUtils.isNotEmpty(productDataDTOList)) {
                                        //excel导入封装产品规格表集合
                                        this.packproductDataList(product, productSpecificationList, productDataDTOList);
                                        //批量新增产品规格表
                                        if (StringUtils.isNotEmpty(productSpecificationList)) {
                                            productSpecificationMapper.batchProductSpecification(productSpecificationList);
                                        }
                                        for (int i1 = 0; i1 < productDataDTOList.size(); i1++) {
                                            List<ProductSpecificationDataDTO> productSpecificationDataDTOList = productDataDTOList.get(i1).getProductSpecificationDataDTOList();
                                            if (StringUtils.isNotEmpty(productSpecificationDataDTOList)) {
                                                //excel导入封装产品规格数据集合
                                                this.packProductSpecificationData(product, productSpecificationParamList, productSpecificationDataList, productSpecificationList, i1, productSpecificationDataDTOList);
                                            }
                                        }
                                        if (StringUtils.isNotEmpty(productSpecificationDataList)) {
                                            productSpecificationDataMapper.batchProductSpecificationData(productSpecificationDataList);
                                        }
                                    }
                                    successExcelList.addAll(productExcels);
                                    break;
                                }
                            }
                            List<ProductDataDTO> productDataDTOList = productExcels.get(i).getProductDataDTOList();
                            if (StringUtils.isNotEmpty(productDataDTOList)) {
                                //excel导入封装产品规格表集合
                                this.packproductDataList(product, productSpecificationList, productDataDTOList);

                                //批量新增产品规格表
                                if (StringUtils.isNotEmpty(productSpecificationList)) {
                                    productSpecificationMapper.batchProductSpecification(productSpecificationList);
                                }

                            }
                            for (int i1 = 0; i1 < productDataDTOList.size(); i1++) {
                                List<ProductSpecificationDataDTO> productSpecificationDataDTOList = productDataDTOList.get(i1).getProductSpecificationDataDTOList();
                                if (StringUtils.isNotEmpty(productSpecificationDataDTOList)) {
                                    //excel导入封装产品规格数据集合
                                    this.packProductSpecificationData(product, productSpecificationParamList, productSpecificationDataList, productSpecificationList, i1, productSpecificationDataDTOList);
                                }
                            }
                            //每次清空产品规格表数据
                            productSpecificationList.clear();
                            if (productExcels.size() - 1 == i) {
                                if (StringUtils.isNotEmpty(productSpecificationDataList)) {
                                    productSpecificationDataMapper.batchProductSpecificationData(productSpecificationDataList);
                                }
                                successExcelList.addAll(productExcels);
                            }
                        }
                    }
                }
                if (productErreo.length() > 1) {
                    throw new ServiceException(productErreo.toString());
                }
            } catch (ServiceException e) {
                throw e;
            } catch (Exception e) {
                throw new ServiceException("模板格式不正确！");
            }
        } else {
            throw new ServiceException("请填写excel数据！");
        }

    }

    /**
     * 将树按顺序打平(导出产品用)
     *
     * @return
     */
    @Override
    public List<Long> treeToList() {
        Product product = new Product();
        //查询数据
        List<ProductDTO> productDTOList = productMapper.selectProductList(product);
        if (StringUtils.isNotEmpty(productDTOList)) {
            List<String> collect = productDTOList.stream().map(ProductDTO::getProductCategory).filter(StringUtils::isNotBlank).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)) {
                List<Long> collect2 = collect.stream().filter(StringUtils::isNotBlank).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(collect2)) {
                    //远程调用查询字典数据
                    R<List<DictionaryDataDTO>> listR = remoteDictionaryDataService.selectDictionaryDataByDictionaryDataIds(collect2, SecurityConstants.INNER);
                    List<DictionaryDataDTO> data = listR.getData();
                    if (StringUtils.isNotEmpty(data)) {
                        for (ProductDTO dto : productDTOList) {
                            for (DictionaryDataDTO datum : data) {
                                if (StringUtils.isNotBlank(dto.getProductCategory())) {
                                    if (Long.valueOf(dto.getProductCategory()).equals(datum.getDictionaryDataId())) {
                                        dto.setProductCategoryName(datum.getDictionaryLabel());
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
        //树结构
        List<ProductDTO> tree = this.createTree(productDTOList, 0);
        //打平顺序返回
        List<ProductDTO> productDTOS = this.treeToList(tree);

        return StringUtils.isEmpty(productDTOS.stream().map(ProductDTO::getProductId).collect(Collectors.toList())) ? new ArrayList<>() : productDTOS.stream().map(ProductDTO::getProductId).collect(Collectors.toList());
    }

    /**
     * 导出产品列表
     *
     * @param productIds
     * @return
     */
    @Override
    public List<ProductExportExcel> exportProduct(List<Long> productIds) {
        List<ProductExportExcel> productExportExcelList = new ArrayList<>();
        if (StringUtils.isNotEmpty(productIds) && productIds.get(0) != null) {
            for (Long productId : productIds) {
                ProductDTO productDTO = productMapper.selectProductByProductId(productId);
                this.packProductCategoryName(productDTO);
                //取父级产品编码
                ProductDTO productDTO1 = new ProductDTO();
                if (StringUtils.isNull(productDTO)) {
                    throw new ServiceException("产品不存在 请刷新页面重试！");
                }
                if (StringUtils.isNotNull(productDTO.getParentProductId())) {
                    productDTO1 = productMapper.selectProductByProductId(productDTO.getParentProductId());
                }
                //根据产品规格id分组
                Map<Long, List<ProductSpecificationDataDTO>> productSpecificationDataDTOMap = new HashMap<>();
                //产品规格数据表
                List<ProductSpecificationDataDTO> productSpecificationDataDTOList = productSpecificationDataMapper.selectProductId(productId);
                if (StringUtils.isNotEmpty(productSpecificationDataDTOList)) {
                    //根据产品规格id分组
                    productSpecificationDataDTOMap = productSpecificationDataDTOList.parallelStream().collect(Collectors.groupingBy(ProductSpecificationDataDTO::getProductSpecificationId, LinkedHashMap::new, Collectors.toList()));
                }
                //产品规格表
                List<ProductSpecificationDTO> productSpecificationDTOS = productSpecificationMapper.selectProductId(productId);
                //产品规格参数表
                List<ProductSpecificationParamDTO> productSpecificationParamDTOS = productSpecificationParamMapper.selectProductId(productId);

                if (StringUtils.isNotEmpty(productSpecificationDTOS)) {
                    for (int i = 0; i < productSpecificationDTOS.size(); i++) {
                        ProductExportExcel productExportExcel = new ProductExportExcel();
                        //产品规格参数集合
                        List<String> productSpecificationParamList = new ArrayList<>();
                        if (StringUtils.isNotEmpty(productSpecificationParamDTOS)) {
                            productSpecificationParamList.addAll(productSpecificationParamDTOS.stream().map(ProductSpecificationParamDTO::getSpecificationParamName).collect(Collectors.toList()));
                            productExportExcel.setProductSpecificationParamList(productSpecificationParamList);
                        }

                        //产品规格数据集合
                        List<String> productDataList = new ArrayList<>();
                        if (StringUtils.isNotEmpty(productSpecificationDataDTOMap)) {
                            List<ProductSpecificationDataDTO> productSpecificationDataDTOList1 = productSpecificationDataDTOMap.get(productSpecificationDTOS.get(i).getProductSpecificationId());
                            if (StringUtils.isNotEmpty(productSpecificationDataDTOList1)) {
                                productDataList.addAll(productSpecificationDataDTOList1.stream().map(ProductSpecificationDataDTO::getValue).collect(Collectors.toList()));
                                productExportExcel.setProductDataList(productDataList);
                            }
                        }
                        //目录价,单位元
                        BigDecimal listPrice = productSpecificationDTOS.get(i).getListPrice();
                        //规格名称
                        String specificationName = productSpecificationDTOS.get(i).getSpecificationName();

                        //产品编码
                        String productCode = productDTO.getProductCode();
                        //产品名称
                        String productName = productDTO.getProductName();
                        if (StringUtils.isNotNull(productDTO1)) {
                            //父级产品编码
                            String productCode1 = productDTO1.getProductCode();
                            if (StringUtils.isNotBlank(productCode1)) {
                                productExportExcel.setParentProductCode(productCode1);
                            }
                        }
                        //产品单位
                        String productUnitName = productDTO.getProductUnitName();
                        //产品类别
                        String productCategoryName = productDTO.getProductCategoryName();
                        //是否上下架：0下架;1上架
                        Integer listingFlag = productDTO.getListingFlag();
                        //产品描述
                        String productDescription = productDTO.getProductDescription();


                        //产品编码
                        if (StringUtils.isNotBlank(productCode)) {
                            productExportExcel.setProductCode(productCode);
                        }
                        //产品名称
                        if (StringUtils.isNotBlank(productName)) {
                            productExportExcel.setProductName(productName);
                        }
                        //产品单位
                        if (StringUtils.isNotBlank(productUnitName)) {
                            productExportExcel.setProductUnitName(productUnitName);
                        }
                        //产品类别
                        if (StringUtils.isNotBlank(productCategoryName)) {
                            productExportExcel.setProductCategoryName(productCategoryName);
                        }
                        //是否上下架：0下架;1上架
                        if (StringUtils.isNotNull(listingFlag)) {
                            if (listingFlag == 0) {
                                productExportExcel.setListingFlag("下架");
                            } else if (listingFlag == 1) {
                                productExportExcel.setListingFlag("上架");
                            }
                        }else {
                            productExportExcel.setListingFlag("上架");
                        }
                        //产品描述
                        if (StringUtils.isNotBlank(productDescription)) {
                            productExportExcel.setProductDescription(productDescription);
                        }
                        //目标价
                        if (StringUtils.isNotNull(listPrice)) {
                            productExportExcel.setListPrice(listPrice.toString());
                        }else {
                            productExportExcel.setListPrice("0.00");
                        }
                        //规格名称
                        if (StringUtils.isNotNull(specificationName)) {
                            productExportExcel.setSpecificationName(specificationName);
                        }
                        productExportExcelList.add(productExportExcel);
                    }
                }

            }
        }
        return productExportExcelList;
    }

    @Override
    public List<ProductDTO> selectAncestors(Long productId) {
        return productMapper.selectAncestors(productId);
    }

    private void packProductSpecificationData(Product product, List<ProductSpecificationParam> productSpecificationParamList, List<ProductSpecificationData> productSpecificationDataList, List<ProductSpecification> productSpecificationList, int i1, List<ProductSpecificationDataDTO> productSpecificationDataDTOList) {
        for (int i2 = 0; i2 < productSpecificationDataDTOList.size(); i2++) {
            ProductSpecificationData productSpecificationData = new ProductSpecificationData();
            productSpecificationData.setValue(productSpecificationDataDTOList.get(i2).getValue());
            //产品id
            productSpecificationData.setProductId(product.getProductId());
            //产品规格参数ID
            productSpecificationData.setProductSpecificationParamId(productSpecificationParamList.get(i2).getProductSpecificationParamId());
            //产品规格id
            productSpecificationData.setProductSpecificationId(productSpecificationList.get(i1).getProductSpecificationId());
            productSpecificationData.setDeleteFlag(0);
            productSpecificationData.setCreateBy(SecurityUtils.getUserId());
            productSpecificationData.setCreateTime(DateUtils.getNowDate());
            productSpecificationData.setUpdateTime(DateUtils.getNowDate());
            productSpecificationData.setUpdateBy(SecurityUtils.getUserId());
            productSpecificationDataList.add(productSpecificationData);
        }
    }

    /**
     * excel导入封装产品规格表集合
     *
     * @param product
     * @param productSpecificationList
     * @param productDataDTOList
     */
    private void packproductDataList(Product product, List<ProductSpecification> productSpecificationList, List<ProductDataDTO> productDataDTOList) {
        for (ProductDataDTO productDataDTO : productDataDTOList) {
            ProductSpecification productSpecification = new ProductSpecification();
            BeanUtils.copyProperties(productDataDTO, productSpecification);
            //产品id
            productSpecification.setProductId(product.getProductId());
            productSpecification.setDeleteFlag(0);
            productSpecification.setCreateBy(SecurityUtils.getUserId());
            productSpecification.setCreateTime(DateUtils.getNowDate());
            productSpecification.setUpdateTime(DateUtils.getNowDate());
            productSpecification.setUpdateBy(SecurityUtils.getUserId());
            productSpecificationList.add(productSpecification);
        }
    }

    /**
     * 封装检验数据正确性
     *
     * @param productExcel
     * @param productUnitDTOS
     * @param i
     * @param productCodeListData
     */
    private StringBuffer validProduct(ProductExcel productExcel, List<ProductUnitDTO> productUnitDTOS, int i, List<String> productCodeListData) {
        StringBuffer validEmployeeErreo = new StringBuffer();
        if (StringUtils.isNotNull(productExcel)) {
            //产品编码
            String productCode = productExcel.getProductCode();
            if (i == 0) {
                if (StringUtils.isNotEmpty(productCodeListData)) {
                    if (productCodeListData.contains(productCode)) {
                        validEmployeeErreo.append("已存在该编码 "+productCode+"不允许重复新增");
                    }
                }
            }
            //产品名称
            String productName = productExcel.getProductName();
            //产品单位
            String productUnitName = productExcel.getProductUnitName();
            if (StringUtils.isBlank(productCode)) {
                validEmployeeErreo.append("产品编码为必填项");
            }
            if (StringUtils.isBlank(productName)) {
                validEmployeeErreo.append("产品名称为必填项");
            }
            if (StringUtils.isBlank(productUnitName)) {
                validEmployeeErreo.append("产品量纲不能为空");
            } else {
                //产品量纲不存在
                if (StringUtils.isNotEmpty(productUnitDTOS)) {
                    List<ProductUnitDTO> productUnitNames = productUnitDTOS.stream().filter(f -> StringUtils.equals(productUnitName, f.getProductUnitName())).collect(Collectors.toList());
                    if (StringUtils.isEmpty(productUnitNames)) {
                        validEmployeeErreo.append("Excel中[" + productUnitName + "]产品量纲不存在！");
                    }
                }
            }
        }
        return validEmployeeErreo;
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
        //产品引用
        StringBuffer productErreo = new StringBuffer();
        //分解引用
        StringBuffer decomposeErreo = new StringBuffer();
        int i = 0;

        List<ProductDTO> productDTOS = productMapper.selectAncestors(productDTO.getProductId());
        if (StringUtils.isEmpty(productDTOS)) {
            throw new ServiceException("数据不存在！ 请刷新页面重试!");
        }
        //远程职级分解
        List<Long> productIds = new ArrayList<>();
        productIds=productDTOS.stream().map(ProductDTO::getProductId).collect(Collectors.toList());
        R<List<OfficialRankDecomposeDTO>> officialRankDecomposeDTOList = remoteOfficialRankSystemService.selectOfficialDecomposeByDimensions(productIds, 4, SecurityConstants.INNER);
        List<OfficialRankDecomposeDTO> officialRankDecomposeData = officialRankDecomposeDTOList.getData();
        if (StringUtils.isNotEmpty(officialRankDecomposeData)) {
            List<String> officialRankSystemNames = new ArrayList<>();
            for (OfficialRankDecomposeDTO officialRankDecomposeDatum : officialRankDecomposeData) {
                Long decomposeDimension = officialRankDecomposeDatum.getDecomposeDimension();
                if (null != decomposeDimension){
                    if (productIds.contains(officialRankDecomposeDatum.getDecomposeDimension())){
                        officialRankSystemNames.add(officialRankDecomposeDatum.getOfficialRankSystemName());
                    }
                }
            }
            if (StringUtils.isNotEmpty(officialRankSystemNames)) {
                throw new ServiceException("数据被引用!");
                //decomposeErreo.append("产品" + dto.getProductName() + "已被职级体系名称[" + StringUtils.join(",", officialRankSystemNames) + "] 职级分解引用\n");
            }
        }

        //是否引用
        List<ProductDTO> productDTOList = productMapper.selectProductQuote(productIds);
        //战略云引用
        isStrategyQuote(productIds);
        //指标名称远程调用
        if (StringUtils.isNotEmpty(productDTOList)) {
            List<Long> indicatorIds = productDTOList.stream().map(ProductDTO::getIndicatorId).collect(Collectors.toList());
            R<List<IndicatorDTO>> listR = remoteIndicatorService.selectIndicatorByIds(indicatorIds, SecurityConstants.INNER);
            List<IndicatorDTO> data = listR.getData();
            if (StringUtils.isNotEmpty(data)) {
                String indicatorName = StringUtils.join(",", data.stream().map(IndicatorDTO::getIndicatorName).distinct().collect(Collectors.toList()));
                if (StringUtils.isNotBlank(indicatorName)) {
                    throw new ServiceException("数据被引用!");
                   //decomposeErreo.append("产品" + productName + "已被目标分解" + indicatorName + "引用\n");
                }
            }
        }
        productErreo.append(decomposeErreo);
        if (productErreo.length() > 0) {
            throw new ServiceException(productErreo.toString());
        }
        i = productMapper.logicDeleteProductByProductIds(productDTOS.stream().map(ProductDTO::getProductId).collect(Collectors.toList()), SecurityUtils.getUserId(), DateUtils.getNowDate());
        //规格表
        try {
            productSpecificationMapper.logicDeleteProductSpecificationByProductIds(productDTOS.stream().map(ProductDTO::getProductId).collect(Collectors.toList()), SecurityUtils.getUserId(), DateUtils.getNowDate());
        } catch (Exception e) {
            throw new ServiceException("删除规格表失败");
        }
        //参数表
        try {
            productSpecificationParamMapper.logicDeleteProductSpecificationParamByProductIds(productDTOS.stream().map(ProductDTO::getProductId).collect(Collectors.toList()), SecurityUtils.getUserId(), DateUtils.getNowDate());
        } catch (Exception e) {
            throw new ServiceException("删除规格参数失败");
        }

        //数据表
        try {
            productSpecificationDataMapper.logicDeleteProductSpecificationDataByProductIds(productDTOS.stream().map(ProductDTO::getProductId).collect(Collectors.toList()), SecurityUtils.getUserId(), DateUtils.getNowDate());
        } catch (Exception e) {
            throw new ServiceException("删除产品数据失败");
        }
        //删除文件
        try {
            productFileMapper.logicDeleteProductFileByProductIds(productDTOS.stream().map(ProductDTO::getProductId).collect(Collectors.toList()), SecurityUtils.getUserId(), DateUtils.getNowDate());
        } catch (Exception e) {
            throw new ServiceException("删除产品文件表失败");
        }
        return i;
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
    @Override
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
    @Override
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

