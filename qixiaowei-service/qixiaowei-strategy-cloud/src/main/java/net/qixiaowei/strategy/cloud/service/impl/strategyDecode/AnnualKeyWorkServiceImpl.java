package net.qixiaowei.strategy.cloud.service.impl.strategyDecode;

import com.alibaba.nacos.shaded.com.google.common.collect.ImmutableMap;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.dto.product.ProductDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.AreaDTO;
import net.qixiaowei.operate.cloud.api.remote.product.RemoteProductService;
import net.qixiaowei.operate.cloud.api.remote.targetManager.RemoteAreaService;
import net.qixiaowei.strategy.cloud.api.domain.strategyDecode.AnnualKeyWork;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.AnnualKeyWorkDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.AnnualKeyWorkDetailDTO;
import net.qixiaowei.strategy.cloud.mapper.plan.PlanBusinessUnitMapper;
import net.qixiaowei.strategy.cloud.mapper.strategyDecode.AnnualKeyWorkMapper;
import net.qixiaowei.strategy.cloud.service.strategyDecode.IAnnualKeyWorkDetailService;
import net.qixiaowei.strategy.cloud.service.strategyDecode.IAnnualKeyWorkService;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDepartmentService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndustryService;
import net.qixiaowei.system.manage.api.remote.user.RemoteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


/**
 * AnnualKeyWorkService业务层处理
 *
 * @author Graves
 * @since 2023-03-14
 */
@Service
public class AnnualKeyWorkServiceImpl implements IAnnualKeyWorkService {

    public static Map<String, String> BUSINESS_UNIT_DECOMPOSE_MAP = ImmutableMap.of(
            "region", "区域",
            "department", "部门",
            "product", "产品",
            "industry", "行业"
    );

    @Autowired
    private AnnualKeyWorkMapper annualKeyWorkMapper;

    @Autowired
    private IAnnualKeyWorkDetailService annualKeyWorkDetailService;

    @Autowired
    private RemoteAreaService areaService;

    @Autowired
    private RemoteProductService productService;

    @Autowired
    private RemoteDepartmentService departmentService;

    @Autowired
    private RemoteIndustryService industryService;

    @Autowired
    private PlanBusinessUnitMapper planBusinessUnitMapper;

    @Autowired
    private RemoteUserService remoteUserService;

    /**
     * 查询年度重点工作表
     *
     * @param annualKeyWorkId 年度重点工作表主键
     * @return 年度重点工作表
     */
    @Override
    public AnnualKeyWorkDTO selectAnnualKeyWorkByAnnualKeyWorkId(Long annualKeyWorkId) {
        AnnualKeyWorkDTO annualKeyWorkDTO = annualKeyWorkMapper.selectAnnualKeyWorkByAnnualKeyWorkId(annualKeyWorkId);
        if (StringUtils.isNull(annualKeyWorkDTO)) {
            throw new ServiceException("年度重点工作表已不存在");
        }
        String businessUnitDecompose = annualKeyWorkDTO.getBusinessUnitDecompose();
        if (StringUtils.isNotEmpty(businessUnitDecompose)) {
            StringBuilder businessUnitDecomposeNames = new StringBuilder();
            List<String> businessUnitDecomposeList = Arrays.asList(businessUnitDecompose.split(";"));
            businessUnitDecomposeList.forEach(decompose -> {
                if (BUSINESS_UNIT_DECOMPOSE_MAP.containsKey(decompose)) {
                    businessUnitDecomposeNames.append(BUSINESS_UNIT_DECOMPOSE_MAP.get(decompose)).append(";");
                }
            });
            List<Map<String, Object>> businessUnitDecomposes = new ArrayList<>();
            for (String business : businessUnitDecomposeList) {
                Map<String, Object> businessUnitDecomposeMap = new HashMap<>();
                businessUnitDecomposeMap.put("label", BUSINESS_UNIT_DECOMPOSE_MAP.get(business));
                businessUnitDecomposeMap.put("value", business);
                businessUnitDecomposes.add(businessUnitDecomposeMap);
            }
            annualKeyWorkDTO.setBusinessUnitDecomposes(businessUnitDecomposes);
            annualKeyWorkDTO.setBusinessUnitDecomposeName(businessUnitDecomposeNames.substring(0, businessUnitDecomposeNames.length() - 1));
        }
        setDecomposeValue(annualKeyWorkDTO, businessUnitDecompose);
        // todo 详情
        List<AnnualKeyWorkDetailDTO> annualKeyWorkDetailDTOS = annualKeyWorkDetailService.selectAnnualKeyWorkDetailByAnnualKeyWorkId(annualKeyWorkId);
        if (StringUtils.isEmpty(annualKeyWorkDetailDTOS)) {
            return annualKeyWorkDTO;
        }
        // 部门名称赋值
        List<Long> departmentIds = annualKeyWorkDetailDTOS.stream().map(AnnualKeyWorkDetailDTO::getDepartmentId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(departmentIds)) {
            R<List<DepartmentDTO>> departmentDTOSR = departmentService.selectdepartmentIds(departmentIds, SecurityConstants.INNER);
            List<DepartmentDTO> departmentDTOS = departmentDTOSR.getData();
            if (StringUtils.isNotEmpty(departmentDTOS)) {
                for (AnnualKeyWorkDetailDTO annualKeyWorkDetailDTO : annualKeyWorkDetailDTOS) {
                    for (DepartmentDTO departmentDTO : departmentDTOS) {
                        if (departmentDTO.getDepartmentId().equals(annualKeyWorkDetailDTO.getDepartmentId())) {
                            annualKeyWorkDetailDTO.setDepartmentName(departmentDTO.getDepartmentName());
                            break;
                        }
                    }
                }
            }
        }
        return annualKeyWorkDTO;
    }

    /**
     * 根据维度进行赋值
     *
     * @param annualKeyWorkDTO      年度重点工作DTO
     * @param businessUnitDecompose 业务单元维度
     */
    private void setDecomposeValue(AnnualKeyWorkDTO annualKeyWorkDTO, String businessUnitDecompose) {
        Long areaId = annualKeyWorkDTO.getAreaId();
        Long departmentId = annualKeyWorkDTO.getDepartmentId();
        Long productId = annualKeyWorkDTO.getProductId();
        Long industryId = annualKeyWorkDTO.getIndustryId();
        if (businessUnitDecompose.contains("region")) {
            if (StringUtils.isNotNull(areaId)) {
                R<AreaDTO> areaDTOR = areaService.getById(areaId, SecurityConstants.INNER);
                AreaDTO areaDTO = areaDTOR.getData();
                if (StringUtils.isNotNull(areaDTO))
                    annualKeyWorkDTO.setAreaName(areaDTO.getAreaName());
            }
        }
        if (businessUnitDecompose.contains("department")) {
            if (StringUtils.isNotNull(departmentId)) {
                R<DepartmentDTO> departmentDTOR = departmentService.selectdepartmentId(departmentId, SecurityConstants.INNER);
                DepartmentDTO departmentDTO = departmentDTOR.getData();
                if (StringUtils.isNotNull(departmentDTO))
                    annualKeyWorkDTO.setDepartmentName(departmentDTO.getDepartmentName());
            }
        }
        if (businessUnitDecompose.contains("product")) {
            if (StringUtils.isNotNull(productId)) {
                R<ProductDTO> productDTOR = productService.remoteSelectById(productId, SecurityConstants.INNER);
                ProductDTO productDTO = productDTOR.getData();
                if (StringUtils.isNotNull(productDTO))
                    annualKeyWorkDTO.setProductName(productDTO.getProductName());
            }
        }
        if (businessUnitDecompose.contains("industry")) {
            if (StringUtils.isNotNull(industryId)) {
                R<IndustryDTO> industryDTOR = industryService.selectById(industryId, SecurityConstants.INNER);
                IndustryDTO industryDTO = industryDTOR.getData();
                if (StringUtils.isNotNull(industryDTO))
                    annualKeyWorkDTO.setIndustryName(industryDTO.getIndustryName());
            }
        }
    }

    /**
     * 查询年度重点工作表列表
     *
     * @param annualKeyWorkDTO 年度重点工作表
     * @return 年度重点工作表
     */
    @Override
    public List<AnnualKeyWorkDTO> selectAnnualKeyWorkList(AnnualKeyWorkDTO annualKeyWorkDTO) {
        AnnualKeyWork annualKeyWork = new AnnualKeyWork();
        BeanUtils.copyProperties(annualKeyWorkDTO, annualKeyWork);
        List<AnnualKeyWorkDTO> annualKeyWorkDTOS = annualKeyWorkMapper.selectAnnualKeyWorkList(annualKeyWork);

        return annualKeyWorkDTOS;
    }

    /**
     * 新增年度重点工作表
     *
     * @param annualKeyWorkDTO 年度重点工作表
     * @return 结果
     */
    @Override
    public AnnualKeyWorkDTO insertAnnualKeyWork(AnnualKeyWorkDTO annualKeyWorkDTO) {
        AnnualKeyWork annualKeyWork = new AnnualKeyWork();
        BeanUtils.copyProperties(annualKeyWorkDTO, annualKeyWork);
        annualKeyWork.setCreateBy(SecurityUtils.getUserId());
        annualKeyWork.setCreateTime(DateUtils.getNowDate());
        annualKeyWork.setUpdateTime(DateUtils.getNowDate());
        annualKeyWork.setUpdateBy(SecurityUtils.getUserId());
        annualKeyWork.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        annualKeyWorkMapper.insertAnnualKeyWork(annualKeyWork);
        annualKeyWorkDTO.setAnnualKeyWorkId(annualKeyWork.getAnnualKeyWorkId());
        return annualKeyWorkDTO;
    }

    /**
     * 修改年度重点工作表
     *
     * @param annualKeyWorkDTO 年度重点工作表
     * @return 结果
     */
    @Override
    public int updateAnnualKeyWork(AnnualKeyWorkDTO annualKeyWorkDTO) {
        AnnualKeyWork annualKeyWork = new AnnualKeyWork();
        BeanUtils.copyProperties(annualKeyWorkDTO, annualKeyWork);
        annualKeyWork.setUpdateTime(DateUtils.getNowDate());
        annualKeyWork.setUpdateBy(SecurityUtils.getUserId());
        return annualKeyWorkMapper.updateAnnualKeyWork(annualKeyWork);
    }

    /**
     * 逻辑批量删除年度重点工作表
     *
     * @param annualKeyWorkIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteAnnualKeyWorkByAnnualKeyWorkIds(List<Long> annualKeyWorkIds) {
        return annualKeyWorkMapper.logicDeleteAnnualKeyWorkByAnnualKeyWorkIds(annualKeyWorkIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除年度重点工作表信息
     *
     * @param annualKeyWorkId 年度重点工作表主键
     * @return 结果
     */
    @Override
    public int deleteAnnualKeyWorkByAnnualKeyWorkId(Long annualKeyWorkId) {
        return annualKeyWorkMapper.deleteAnnualKeyWorkByAnnualKeyWorkId(annualKeyWorkId);
    }

    /**
     * 逻辑删除年度重点工作表信息
     *
     * @param annualKeyWorkDTO 年度重点工作表
     * @return 结果
     */
    @Override
    public int logicDeleteAnnualKeyWorkByAnnualKeyWorkId(AnnualKeyWorkDTO annualKeyWorkDTO) {
        AnnualKeyWork annualKeyWork = new AnnualKeyWork();
        annualKeyWork.setAnnualKeyWorkId(annualKeyWorkDTO.getAnnualKeyWorkId());
        annualKeyWork.setUpdateTime(DateUtils.getNowDate());
        annualKeyWork.setUpdateBy(SecurityUtils.getUserId());
        return annualKeyWorkMapper.logicDeleteAnnualKeyWorkByAnnualKeyWorkId(annualKeyWork);
    }

    /**
     * 物理删除年度重点工作表信息
     *
     * @param annualKeyWorkDTO 年度重点工作表
     * @return 结果
     */

    @Override
    public int deleteAnnualKeyWorkByAnnualKeyWorkId(AnnualKeyWorkDTO annualKeyWorkDTO) {
        AnnualKeyWork annualKeyWork = new AnnualKeyWork();
        BeanUtils.copyProperties(annualKeyWorkDTO, annualKeyWork);
        return annualKeyWorkMapper.deleteAnnualKeyWorkByAnnualKeyWorkId(annualKeyWork.getAnnualKeyWorkId());
    }

    /**
     * 物理批量删除年度重点工作表
     *
     * @param annualKeyWorkDtos 需要删除的年度重点工作表主键
     * @return 结果
     */

    @Override
    public int deleteAnnualKeyWorkByAnnualKeyWorkIds(List<AnnualKeyWorkDTO> annualKeyWorkDtos) {
        List<Long> stringList = new ArrayList<>();
        for (AnnualKeyWorkDTO annualKeyWorkDTO : annualKeyWorkDtos) {
            stringList.add(annualKeyWorkDTO.getAnnualKeyWorkId());
        }
        return annualKeyWorkMapper.deleteAnnualKeyWorkByAnnualKeyWorkIds(stringList);
    }

    /**
     * 批量新增年度重点工作表信息
     *
     * @param annualKeyWorkDtos 年度重点工作表对象
     */

    public int insertAnnualKeyWorks(List<AnnualKeyWorkDTO> annualKeyWorkDtos) {
        List<AnnualKeyWork> annualKeyWorkList = new ArrayList<>();

        for (AnnualKeyWorkDTO annualKeyWorkDTO : annualKeyWorkDtos) {
            AnnualKeyWork annualKeyWork = new AnnualKeyWork();
            BeanUtils.copyProperties(annualKeyWorkDTO, annualKeyWork);
            annualKeyWork.setCreateBy(SecurityUtils.getUserId());
            annualKeyWork.setCreateTime(DateUtils.getNowDate());
            annualKeyWork.setUpdateTime(DateUtils.getNowDate());
            annualKeyWork.setUpdateBy(SecurityUtils.getUserId());
            annualKeyWork.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            annualKeyWorkList.add(annualKeyWork);
        }
        return annualKeyWorkMapper.batchAnnualKeyWork(annualKeyWorkList);
    }

    /**
     * 批量修改年度重点工作表信息
     *
     * @param annualKeyWorkDtos 年度重点工作表对象
     */

    public int updateAnnualKeyWorks(List<AnnualKeyWorkDTO> annualKeyWorkDtos) {
        List<AnnualKeyWork> annualKeyWorkList = new ArrayList<>();

        for (AnnualKeyWorkDTO annualKeyWorkDTO : annualKeyWorkDtos) {
            AnnualKeyWork annualKeyWork = new AnnualKeyWork();
            BeanUtils.copyProperties(annualKeyWorkDTO, annualKeyWork);
            annualKeyWork.setCreateBy(SecurityUtils.getUserId());
            annualKeyWork.setCreateTime(DateUtils.getNowDate());
            annualKeyWork.setUpdateTime(DateUtils.getNowDate());
            annualKeyWork.setUpdateBy(SecurityUtils.getUserId());
            annualKeyWorkList.add(annualKeyWork);
        }
        return annualKeyWorkMapper.updateAnnualKeyWorks(annualKeyWorkList);
    }

}

