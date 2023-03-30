package net.qixiaowei.strategy.cloud.service.impl.strategyDecode;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.enums.strategy.PlanBusinessUnitCode;
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
import net.qixiaowei.strategy.cloud.api.domain.strategyDecode.AnnualKeyWorkDetail;
import net.qixiaowei.strategy.cloud.api.dto.plan.PlanBusinessUnitDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.AnnualKeyWorkDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.AnnualKeyWorkDetailDTO;
import net.qixiaowei.strategy.cloud.mapper.plan.PlanBusinessUnitMapper;
import net.qixiaowei.strategy.cloud.mapper.strategyDecode.AnnualKeyWorkMapper;
import net.qixiaowei.strategy.cloud.service.strategyDecode.IAnnualKeyWorkDetailService;
import net.qixiaowei.strategy.cloud.service.strategyDecode.IAnnualKeyWorkService;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDTO;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDepartmentService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndustryService;
import net.qixiaowei.system.manage.api.remote.user.RemoteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private RemoteEmployeeService employeeService;

    /**
     * 查询年度重点工作表
     *
     * @param annualKeyWorkId 年度重点工作表主键
     * @return 年度重点工作表
     */
    @Override
    public AnnualKeyWorkDTO selectAnnualKeyWorkByAnnualKeyWorkId(Long annualKeyWorkId) {
        AnnualKeyWorkDTO annualKeyWorkDTO = annualKeyWorkMapper.selectAnnualKeyWorkByAnnualKeyWorkId(annualKeyWorkId);
        if (StringUtils.isNull(annualKeyWorkDTO))
            throw new ServiceException("年度重点工作表已不存在");
        Integer planRank = annualKeyWorkDTO.getPlanRank();
        if (StringUtils.isNotNull(planRank)) {
            if (planRank == 1)
                annualKeyWorkDTO.setPlanRankName("部门级");
            else
                annualKeyWorkDTO.setPlanRankName("公司级");
        }
        String businessUnitDecompose = annualKeyWorkDTO.getBusinessUnitDecompose();
        if (StringUtils.isNotEmpty(businessUnitDecompose)) {
            annualKeyWorkDTO.setBusinessUnitDecomposeName(PlanBusinessUnitCode.getBusinessUnitDecomposeName(businessUnitDecompose));
            annualKeyWorkDTO.setBusinessUnitDecomposes(PlanBusinessUnitCode.getDropList(businessUnitDecompose));
        }
        setDecomposeValue(annualKeyWorkDTO, businessUnitDecompose);
        // 详情
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
        // 部门负责人赋值
        List<Long> departmentEmployeeIds = annualKeyWorkDetailDTOS.stream().map(AnnualKeyWorkDetailDTO::getDepartmentEmployeeId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(departmentEmployeeIds)) {
            R<List<EmployeeDTO>> employeeDTOSR = employeeService.selectByEmployeeIds(departmentEmployeeIds, SecurityConstants.INNER);
            List<EmployeeDTO> employeeDTOS = employeeDTOSR.getData();
            if (StringUtils.isNotEmpty(employeeDTOS)) {
                for (AnnualKeyWorkDetailDTO annualKeyWorkDetailDTO : annualKeyWorkDetailDTOS) {
                    for (EmployeeDTO employeeDTO : employeeDTOS) {
                        if (employeeDTO.getEmployeeId().equals(annualKeyWorkDetailDTO.getDepartmentEmployeeId())) {
                            annualKeyWorkDetailDTO.setDepartmentEmployeeName(employeeDTO.getEmployeeName());
                            break;
                        }
                    }
                }
            }
        }
        annualKeyWorkDTO.setAnnualKeyWorkDetailDTOS(annualKeyWorkDetailDTOS);
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
        Map<String, Object> params = annualKeyWorkDTO.getParams();
        if (StringUtils.isNull(params))
            params = new HashMap<>();
        BeanUtils.copyProperties(annualKeyWorkDTO, annualKeyWork);
        if (StringUtils.isNotEmpty(annualKeyWorkDTO.getBusinessUnitName()))
            params.put("businessUnitName", annualKeyWorkDTO.getBusinessUnitName());
        String createByName = annualKeyWorkDTO.getCreateByName();
        List<String> createByList = new ArrayList<>();
        if (StringUtils.isNotEmpty(createByName)) {
            UserDTO userDTO = new UserDTO();
            userDTO.setEmployeeName(createByName);
            R<List<UserDTO>> userList = remoteUserService.remoteSelectUserList(userDTO, SecurityConstants.INNER);
            List<UserDTO> userListData = userList.getData();
            List<Long> employeeIds = userListData.stream().map(UserDTO::getUserId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(employeeIds)) {
                employeeIds.forEach(e -> {
                    createByList.add(String.valueOf(e));
                });
            } else {
                createByList.add("");
            }
        }
        params.put("createByList", createByList);
        this.queryEmployeeName(params);
        annualKeyWork.setParams(params);
        List<AnnualKeyWorkDTO> annualKeyWorkDTOS = annualKeyWorkMapper.selectAnnualKeyWorkList(annualKeyWork);
        if (StringUtils.isEmpty(annualKeyWorkDTOS)) {
            return annualKeyWorkDTOS;
        }
        // 赋值员工
        Set<Long> createBys = annualKeyWorkDTOS.stream().map(AnnualKeyWorkDTO::getCreateBy).collect(Collectors.toSet());
        R<List<UserDTO>> usersByUserIds = remoteUserService.getUsersByUserIds(createBys, SecurityConstants.INNER);
        List<UserDTO> userDTOList = usersByUserIds.getData();
        if (StringUtils.isNotEmpty(userDTOList)) {
            for (AnnualKeyWorkDTO annualKeyWorkDTO1 : annualKeyWorkDTOS) {
                for (UserDTO userDTO : userDTOList) {
                    if (annualKeyWorkDTO1.getCreateBy().equals(userDTO.getUserId())) {
                        annualKeyWorkDTO1.setCreateByName(userDTO.getEmployeeName());
                    }
                }
            }
        }
        List<Long> areaIds = annualKeyWorkDTOS.stream().map(AnnualKeyWorkDTO::getAreaId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
        List<Long> departmentIds = annualKeyWorkDTOS.stream().map(AnnualKeyWorkDTO::getDepartmentId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
        List<Long> productIds = annualKeyWorkDTOS.stream().map(AnnualKeyWorkDTO::getProductId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
        List<Long> industryIds = annualKeyWorkDTOS.stream().map(AnnualKeyWorkDTO::getIndustryId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
        List<AreaDTO> areaDTOS = new ArrayList<>();
        if (StringUtils.isNotEmpty(areaIds)) {
            R<List<AreaDTO>> areaDTOSR = areaService.selectAreaListByAreaIds(areaIds, SecurityConstants.INNER);
            areaDTOS = areaDTOSR.getData();
            if (StringUtils.isEmpty(areaDTOS))
                throw new ServiceException("当前区域配置的信息已删除 请联系管理员");
        }
        List<DepartmentDTO> departmentDTOS = new ArrayList<>();
        if (StringUtils.isNotEmpty(departmentIds)) {
            R<List<DepartmentDTO>> departmentDTOSR = departmentService.selectdepartmentIds(departmentIds, SecurityConstants.INNER);
            departmentDTOS = departmentDTOSR.getData();
            if (StringUtils.isEmpty(departmentDTOS))
                throw new ServiceException("当前部门配置的信息已删除 请联系管理员");
        }
        List<ProductDTO> productDTOS = new ArrayList<>();
        if (StringUtils.isNotEmpty(productIds)) {
            R<List<ProductDTO>> productDTOSR = productService.getName(productIds, SecurityConstants.INNER);
            productDTOS = productDTOSR.getData();
            if (StringUtils.isEmpty(productDTOS)) {
                throw new ServiceException("当前产品配置的信息已删除 请联系管理员");
            }
        }
        List<IndustryDTO> industryDTOS = new ArrayList<>();
        if (StringUtils.isNotEmpty(industryIds)) {
            R<List<IndustryDTO>> industryDTOSR = industryService.selectByIds(industryIds, SecurityConstants.INNER);
            industryDTOS = industryDTOSR.getData();
            if (StringUtils.isEmpty(industryDTOS)) {
                throw new ServiceException("当前行业配置的信息已删除 请联系管理员");
            }
        }
        for (AnnualKeyWorkDTO annualKeyDTO : annualKeyWorkDTOS) {
            String businessUnitDecompose = annualKeyDTO.getBusinessUnitDecompose();
            Long areaId = annualKeyDTO.getAreaId();
            Long industryId = annualKeyDTO.getIndustryId();
            Long productId = annualKeyDTO.getProductId();
            Long departmentId = annualKeyDTO.getDepartmentId();
            if (businessUnitDecompose.contains("region") && StringUtils.isNotEmpty(areaDTOS)) {
                List<AreaDTO> areaDTOS1 = areaDTOS.stream().filter(areaDTO -> areaDTO.getAreaId().equals(areaId)).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(areaDTOS1)) {
                    String areaName = areaDTOS1.get(0).getAreaName();
                    annualKeyDTO.setAreaName(areaName);
                }
            }
            if (businessUnitDecompose.contains("department") && StringUtils.isNotEmpty(departmentDTOS)) {
                List<DepartmentDTO> departmentDTOS1 = departmentDTOS.stream().filter(departmentDTO -> departmentDTO.getDepartmentId().equals(departmentId)).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(departmentDTOS1)) {
                    String departmentName = departmentDTOS1.get(0).getDepartmentName();
                    annualKeyDTO.setDepartmentName(departmentName);
                }
            }
            if (businessUnitDecompose.contains("product") && StringUtils.isNotEmpty(productDTOS)) {
                List<ProductDTO> productDTOS1 = productDTOS.stream().filter(productDTO -> productDTO.getProductId().equals(productId)).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(productDTOS1)) {
                    String productName = productDTOS1.get(0).getProductName();
                    annualKeyDTO.setProductName(productName);
                }
            }
            if (businessUnitDecompose.contains("industry") && StringUtils.isNotEmpty(industryDTOS)) {
                List<IndustryDTO> industryDTOS1 = industryDTOS.stream().filter(industryDTO -> industryDTO.getIndustryId().equals(industryId)).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(industryDTOS1)) {
                    String industryName = industryDTOS1.get(0).getIndustryName();
                    annualKeyDTO.setIndustryName(industryName);
                }
            }
        }
        return annualKeyWorkDTOS;
    }

    /**
     * 封装高级查询人员id
     *
     * @param params 参数
     */
    private void queryEmployeeName(Map<String, Object> params) {
        Map<String, Object> params2 = new HashMap<>();
        if (StringUtils.isNotEmpty(params)) {
            for (String key : params.keySet()) {
                switch (key) {
                    case "createByNameEqual":
                        params2.put("employeeNameEqual", params.get("createByNameEqual"));
                        break;
                    case "createByNameNotEqual":
                        params2.put("employeeNameNotEqual", params.get("createByNameNotEqual"));
                        break;
                    case "createByNameLike":
                        params2.put("employeeNameLike", params.get("createByNameLike"));
                        break;
                    case "createByNameNotLike":
                        params2.put("employeeNameNotLike", params.get("createByNameNotLike"));
                        break;
                    default:
                        break;
                }
            }
            if (StringUtils.isNotEmpty(params2)) {
                EmployeeDTO employeeDTO = new EmployeeDTO();
                employeeDTO.setParams(params2);
                R<List<EmployeeDTO>> listR = employeeService.selectRemoteList(employeeDTO, SecurityConstants.INNER);
                List<EmployeeDTO> data = listR.getData();
                if (StringUtils.isNotEmpty(data)) {
                    List<Long> employeeIds = data.stream().filter(f -> f.getEmployeeId() != null).map(EmployeeDTO::getEmployeeId).collect(Collectors.toList());
                    if (StringUtils.isNotEmpty(employeeIds)) {
                        R<List<UserDTO>> usersByUserIds = remoteUserService.selectByemployeeIds(employeeIds, SecurityConstants.INNER);
                        List<UserDTO> data1 = usersByUserIds.getData();
                        if (StringUtils.isNotEmpty(data1)) {
                            params.put("createBys", data1.stream().map(UserDTO::getUserId).collect(Collectors.toList()));
                        }
                    }
                }
            }
        }
    }

    /**
     * 新增年度重点工作表
     *
     * @param annualKeyWorkDTO 年度重点工作表
     * @return 结果
     */
    @Override
    @Transactional
    public AnnualKeyWorkDTO insertAnnualKeyWork(AnnualKeyWorkDTO annualKeyWorkDTO) {
        if (StringUtils.isNull(annualKeyWorkDTO))
            throw new ServiceException("请传参");
        Integer planRank = annualKeyWorkDTO.getPlanRank();
        Long planBusinessUnitId = annualKeyWorkDTO.getPlanBusinessUnitId();
        PlanBusinessUnitDTO planBusinessUnitDTO = planBusinessUnitMapper.selectPlanBusinessUnitByPlanBusinessUnitId(planBusinessUnitId);
        if (StringUtils.isNull(planBusinessUnitDTO))
            throw new ServiceException("当前的规划业务单元已经不存在");
        String businessUnitDecompose = planBusinessUnitDTO.getBusinessUnitDecompose();
        if (StringUtils.isNull(businessUnitDecompose))
            throw new ServiceException("规划业务单元维度数据异常 请检查规划业务单元配置");
        // 判断是否选择校验：region,department,product,industry,company
        AnnualKeyWork annualKeyWorkParams = getAnnualKeyWorkParams(annualKeyWorkDTO, businessUnitDecompose);
        // 是否重复
        Integer planYear = annualKeyWorkDTO.getPlanYear();
        annualKeyWorkParams.setPlanRank(planRank);
        annualKeyWorkParams.setPlanYear(planYear);
        annualKeyWorkParams.setPlanBusinessUnitId(annualKeyWorkDTO.getPlanBusinessUnitId());
        List<AnnualKeyWorkDTO> annualKeyWorkDTOS = annualKeyWorkMapper.selectAnnualKeyWorkList(annualKeyWorkParams);
        if (StringUtils.isNotEmpty(annualKeyWorkDTOS)) {
            throw new ServiceException("当前的规划业务单元内容重复 请重新筛选");
        }
        AnnualKeyWork annualKeyWork = new AnnualKeyWork();
        // 将规划业务单元的规划业务单元维度赋进主表
        BeanUtils.copyProperties(annualKeyWorkDTO, annualKeyWork);
        annualKeyWork.setBusinessUnitDecompose(businessUnitDecompose);
        annualKeyWork.setCreateBy(SecurityUtils.getUserId());
        annualKeyWork.setCreateTime(DateUtils.getNowDate());
        annualKeyWork.setUpdateTime(DateUtils.getNowDate());
        annualKeyWork.setUpdateBy(SecurityUtils.getUserId());
        annualKeyWork.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        annualKeyWorkMapper.insertAnnualKeyWork(annualKeyWork);
        Long annualKeyWorkId = annualKeyWork.getAnnualKeyWorkId();
        // 详情表
        List<AnnualKeyWorkDetailDTO> annualKeyWorkDetailDTOS = annualKeyWorkDTO.getAnnualKeyWorkDetailDTOS();
        if (StringUtils.isEmpty(annualKeyWorkDetailDTOS))
            return annualKeyWorkDTO;
        for (AnnualKeyWorkDetailDTO annualKeyWorkDetailDTO : annualKeyWorkDetailDTOS) {
            if (StringUtils.isNull(annualKeyWorkDetailDTO.getTaskNumber()))
                throw new ServiceException("未传入任务编号 请联系管理员");
            if (StringUtils.isNull(annualKeyWorkDetailDTO.getTaskName()))
                throw new ServiceException("请输入任务名称");
            annualKeyWorkDetailDTO.setAnnualKeyWorkId(annualKeyWorkId);
        }
        List<Long> dutyEmployeeIds = annualKeyWorkDetailDTOS.stream().map(AnnualKeyWorkDetailDTO::getDutyEmployeeId).filter(Objects::nonNull).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(dutyEmployeeIds)) {
            R<List<EmployeeDTO>> employeeDTOSR = employeeService.selectByEmployeeIds(dutyEmployeeIds, SecurityConstants.INNER);
            List<EmployeeDTO> employeeDTOS = employeeDTOSR.getData();
            if (StringUtils.isEmpty(employeeDTOS))
                throw new ServiceException("员工信息已不存在 请检查员工配置");
            for (AnnualKeyWorkDetailDTO annualKeyWorkDetailDTO : annualKeyWorkDetailDTOS) {
                if (StringUtils.isNotNull(annualKeyWorkDetailDTO.getDutyEmployeeId())) {
                    for (EmployeeDTO employeeDTO : employeeDTOS) {
                        if (annualKeyWorkDetailDTO.getDutyEmployeeId().equals(employeeDTO.getEmployeeId())) {
                            annualKeyWorkDetailDTO.setDutyEmployeeCode(employeeDTO.getEmployeeCode());
                            annualKeyWorkDetailDTO.setDutyEmployeeName(employeeDTO.getEmployeeName());
                            break;
                        }
                    }
                }
            }
        }
        List<Long> departmentIds = annualKeyWorkDetailDTOS.stream().map(AnnualKeyWorkDetailDTO::getDepartmentId).filter(Objects::nonNull).collect(Collectors.toList());
        if (planRank == 1 && StringUtils.isNotEmpty(departmentIds)) {
            R<List<DepartmentDTO>> departmentDTOSR = departmentService.selectdepartmentIds(departmentIds, SecurityConstants.INNER);
            List<DepartmentDTO> departmentDTOS = departmentDTOSR.getData();
            if (StringUtils.isEmpty(departmentDTOS))
                throw new ServiceException("部门信息已不存在 请检查组织信息配置");
        }
        annualKeyWorkDetailService.insertAnnualKeyWorkDetails(annualKeyWorkDetailDTOS);
        annualKeyWorkDTO.setAnnualKeyWorkId(annualKeyWorkId);
        return annualKeyWorkDTO;
    }

    /**
     * 获取年度重点工作的入参
     * 校验入参的ID是否与维度匹配
     *
     * @param annualKeyWorkDTO      年度重点工作DTO
     * @param businessUnitDecompose 维度
     * @return 年度重点工作入参DTO
     */
    private AnnualKeyWork getAnnualKeyWorkParams(AnnualKeyWorkDTO annualKeyWorkDTO, String businessUnitDecompose) {
        Long areaId = annualKeyWorkDTO.getAreaId();
        Long industryId = annualKeyWorkDTO.getIndustryId();
        Long productId = annualKeyWorkDTO.getProductId();
        Long departmentId = annualKeyWorkDTO.getDepartmentId();
        AnnualKeyWork annualKeyWorkParams = new AnnualKeyWork();
        if (businessUnitDecompose.contains("region")) {
            if (StringUtils.isNull(areaId)) {
                throw new ServiceException("请选择区域");
            }
            annualKeyWorkParams.setAreaId(areaId);
        }
        if (businessUnitDecompose.contains("department")) {
            if (StringUtils.isNull(departmentId)) {
                throw new ServiceException("请选择部门");
            }
            annualKeyWorkParams.setDepartmentId(departmentId);
        }
        if (businessUnitDecompose.contains("product")) {
            if (StringUtils.isNull(productId)) {
                throw new ServiceException("请选择产品");
            }
            annualKeyWorkParams.setProductId(productId);
        }
        if (businessUnitDecompose.contains("industry")) {
            if (StringUtils.isNull(industryId)) {
                throw new ServiceException("请选择行业");
            }
            annualKeyWorkParams.setIndustryId(industryId);
        }
        return annualKeyWorkParams;
    }

    /**
     * 修改年度重点工作表
     *
     * @param annualKeyWorkDTO 年度重点工作表
     * @return 结果
     */
    @Override
    @Transactional
    public int updateAnnualKeyWork(AnnualKeyWorkDTO annualKeyWorkDTO) {
        if (StringUtils.isNull(annualKeyWorkDTO)) {
            throw new ServiceException("请传参");
        }
        Long annualKeyWorkId = annualKeyWorkDTO.getAnnualKeyWorkId();
        Long planBusinessUnitId = annualKeyWorkDTO.getPlanBusinessUnitId();
        Integer planRank = annualKeyWorkDTO.getPlanRank();
        AnnualKeyWorkDTO annualKeyWorkById = annualKeyWorkMapper.selectAnnualKeyWorkByAnnualKeyWorkId(annualKeyWorkId);
        if (StringUtils.isNull(annualKeyWorkById)) {
            throw new ServiceException("当前年度重点工作已经不存在");
        }
        PlanBusinessUnitDTO planBusinessUnitDTO = planBusinessUnitMapper.selectPlanBusinessUnitByPlanBusinessUnitId(planBusinessUnitId);
        if (StringUtils.isNull(planBusinessUnitDTO)) {
            throw new ServiceException("当前的规划业务单元已经不存在");
        }
        String businessUnitDecompose = planBusinessUnitDTO.getBusinessUnitDecompose();
        if (StringUtils.isNull(businessUnitDecompose)) {
            throw new ServiceException("规划业务单元维度数据异常 请检查规划业务单元配置");
        }
        // 判断是否选择校验：region,department,product,industry,company
        AnnualKeyWork annualKeyWorkParams = getAnnualKeyWorkParams(annualKeyWorkDTO, businessUnitDecompose);
        // 是否重复
        Integer planYear = annualKeyWorkById.getPlanYear();
        annualKeyWorkParams.setPlanYear(planYear);
        annualKeyWorkParams.setPlanBusinessUnitId(annualKeyWorkById.getPlanBusinessUnitId());
        List<AnnualKeyWorkDTO> annualKeyWorkDTOS = annualKeyWorkMapper.selectAnnualKeyWorkList(annualKeyWorkParams);
        if (StringUtils.isNotEmpty(annualKeyWorkDTOS) && !annualKeyWorkId.equals(annualKeyWorkDTOS.get(0).getAnnualKeyWorkId()))
            throw new ServiceException("当前的规划业务单元内容重复 请重新筛选");
        AnnualKeyWork annualKeyWork = new AnnualKeyWork();
        BeanUtils.copyProperties(annualKeyWorkDTO, annualKeyWork);
        annualKeyWork.setUpdateTime(DateUtils.getNowDate());
        annualKeyWork.setUpdateBy(SecurityUtils.getUserId());
        annualKeyWorkMapper.updateAnnualKeyWork(annualKeyWork);
        List<AnnualKeyWorkDetailDTO> annualKeyWorkDetailDTOSAfter = annualKeyWorkDTO.getAnnualKeyWorkDetailDTOS();
        if (StringUtils.isEmpty(annualKeyWorkDetailDTOSAfter))
            return 1;
        for (AnnualKeyWorkDetailDTO annualKeyWorkDetailDTO : annualKeyWorkDetailDTOSAfter) {
            if (StringUtils.isNull(annualKeyWorkDetailDTO.getTaskNumber()))
                throw new ServiceException("未传入任务编号 请联系管理员");
            if (StringUtils.isNull(annualKeyWorkDetailDTO.getTaskName()))
                throw new ServiceException("请输入任务名称");
            annualKeyWorkDetailDTO.setAnnualKeyWorkId(annualKeyWorkId);
        }
        List<Long> dutyEmployeeIds = annualKeyWorkDetailDTOSAfter.stream().map(AnnualKeyWorkDetailDTO::getDutyEmployeeId).filter(Objects::nonNull).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(dutyEmployeeIds)) {
            R<List<EmployeeDTO>> employeeDTOSR = employeeService.selectByEmployeeIds(dutyEmployeeIds, SecurityConstants.INNER);
            List<EmployeeDTO> employeeDTOS = employeeDTOSR.getData();
            if (StringUtils.isEmpty(employeeDTOS))
                throw new ServiceException("员工信息已不存在 请检查员工配置");
            for (AnnualKeyWorkDetailDTO annualKeyWorkDetailDTO : annualKeyWorkDetailDTOSAfter) {
                if (StringUtils.isNotNull(annualKeyWorkDetailDTO.getDutyEmployeeId())) {
                    for (EmployeeDTO employeeDTO : employeeDTOS) {
                        if (annualKeyWorkDetailDTO.getDutyEmployeeId().equals(employeeDTO.getEmployeeId())) {
                            annualKeyWorkDetailDTO.setDutyEmployeeCode(employeeDTO.getEmployeeCode());
                            annualKeyWorkDetailDTO.setDutyEmployeeName(employeeDTO.getEmployeeName());
                            break;
                        }
                    }
                }
            }
        }
        List<Long> departmentIds = annualKeyWorkDetailDTOSAfter.stream().map(AnnualKeyWorkDetailDTO::getDepartmentId).filter(Objects::nonNull).collect(Collectors.toList());
        if (planRank == 1 && StringUtils.isNotEmpty(departmentIds)) {
            R<List<DepartmentDTO>> departmentDTOSR = departmentService.selectdepartmentIds(departmentIds, SecurityConstants.INNER);
            List<DepartmentDTO> departmentDTOS = departmentDTOSR.getData();
            if (StringUtils.isEmpty(departmentDTOS))
                throw new ServiceException("部门信息已不存在 请检查组织信息配置");
        }
        List<AnnualKeyWorkDetailDTO> annualKeyWorkDetailDTOSBefore = annualKeyWorkDetailService.selectAnnualKeyWorkDetailByAnnualKeyWorkId(annualKeyWorkId);
        operateAnnualKeyWorkDetail(annualKeyWorkDetailDTOSAfter, annualKeyWorkDetailDTOSBefore);
        return 1;
    }

    /**
     * 处理编辑详情
     *
     * @param annualKeyWorkDetailDTOSAfter  后
     * @param annualKeyWorkDetailDTOSBefore 前
     */
    private void operateAnnualKeyWorkDetail(List<AnnualKeyWorkDetailDTO> annualKeyWorkDetailDTOSAfter, List<AnnualKeyWorkDetailDTO> annualKeyWorkDetailDTOSBefore) {
        List<AnnualKeyWorkDetailDTO> editAnnualKeyWorkS = annualKeyWorkDetailDTOSAfter.stream().filter(gapAnalysisOpportunityDTO ->
                annualKeyWorkDetailDTOSBefore.stream().map(AnnualKeyWorkDetailDTO::getAnnualKeyWorkDetailId).collect(Collectors.toList())
                        .contains(gapAnalysisOpportunityDTO.getAnnualKeyWorkDetailId())).collect(Collectors.toList());
        List<AnnualKeyWorkDetailDTO> delAnnualKeyWorkS = annualKeyWorkDetailDTOSBefore.stream().filter(gapAnalysisOpportunityDTO ->
                !annualKeyWorkDetailDTOSAfter.stream().map(AnnualKeyWorkDetailDTO::getAnnualKeyWorkDetailId).collect(Collectors.toList())
                        .contains(gapAnalysisOpportunityDTO.getAnnualKeyWorkDetailId())).collect(Collectors.toList());
        List<AnnualKeyWorkDetailDTO> addAnnualKeyWorkS = annualKeyWorkDetailDTOSAfter.stream().filter(gapAnalysisOpportunityDTO ->
                !annualKeyWorkDetailDTOSBefore.stream().map(AnnualKeyWorkDetailDTO::getAnnualKeyWorkDetailId).collect(Collectors.toList())
                        .contains(gapAnalysisOpportunityDTO.getAnnualKeyWorkDetailId())).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(editAnnualKeyWorkS)) {
            for (AnnualKeyWorkDetailDTO editAnnualKeyWork : editAnnualKeyWorkS) {
                for (AnnualKeyWorkDetailDTO annualKeyWorkDetailDTO : annualKeyWorkDetailDTOSBefore) {
                    if (editAnnualKeyWork.getTaskNumber().equals(annualKeyWorkDetailDTO.getTaskNumber())) {
                        editAnnualKeyWork.setAnnualKeyWorkDetailId(annualKeyWorkDetailDTO.getAnnualKeyWorkDetailId());
                        break;
                    }
                }
            }
            annualKeyWorkDetailService.updateAnnualKeyWorkDetails(editAnnualKeyWorkS);
        }
        if (StringUtils.isNotEmpty(delAnnualKeyWorkS)) {
            List<Long> delAnnualKeyWorkDetailIds = editAnnualKeyWorkS.stream().map(AnnualKeyWorkDetailDTO::getAnnualKeyWorkDetailId).collect(Collectors.toList());
            annualKeyWorkDetailService.logicDeleteAnnualKeyWorkDetailByAnnualKeyWorkDetailIds(delAnnualKeyWorkDetailIds);
        }
        if (StringUtils.isNotEmpty(addAnnualKeyWorkS)) {
            annualKeyWorkDetailService.insertAnnualKeyWorkDetails(addAnnualKeyWorkS);
        }
    }

    /**
     * 逻辑批量删除年度重点工作表
     *
     * @param annualKeyWorkIds 主键集合
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteAnnualKeyWorkByAnnualKeyWorkIds(List<Long> annualKeyWorkIds) {
        if (StringUtils.isEmpty(annualKeyWorkIds))
            throw new ServiceException("请选择年度重点工作");
        List<AnnualKeyWorkDTO> annualKeyWorkDTOS = annualKeyWorkMapper.selectAnnualKeyWorkByAnnualKeyWorkIds(annualKeyWorkIds);
        if (annualKeyWorkDTOS.size() != annualKeyWorkIds.size())
            throw new ServiceException("部分年度重点工作已不存在");
        annualKeyWorkMapper.logicDeleteAnnualKeyWorkByAnnualKeyWorkIds(annualKeyWorkIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
        List<AnnualKeyWorkDetailDTO> annualKeyWorkDetailDTOS = annualKeyWorkDetailService.selectAnnualKeyWorkDetailByAnnualKeyWorkIds(annualKeyWorkIds);
        if (StringUtils.isEmpty(annualKeyWorkDetailDTOS))
            return 1;
        List<Long> annualKeyWorkDetailIds = annualKeyWorkDetailDTOS.stream().map(AnnualKeyWorkDetailDTO::getAnnualKeyWorkDetailId).collect(Collectors.toList());
        annualKeyWorkDetailService.logicDeleteAnnualKeyWorkDetailByAnnualKeyWorkDetailIds(annualKeyWorkDetailIds);
        return 1;
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
     * 查询列表
     *
     * @param annualKeyWorkDTO 年度重点工作表
     * @return List
     */
    @Override
    public List<AnnualKeyWorkDTO> remoteAnnualKeyWork(AnnualKeyWorkDTO annualKeyWorkDTO) {
        AnnualKeyWork annualKeyWork = new AnnualKeyWork();
        Map<String, Object> params = annualKeyWorkDTO.getParams();
        annualKeyWork.setParams(params);
        BeanUtils.copyProperties(annualKeyWorkDTO, annualKeyWork);
        return annualKeyWorkMapper.selectAnnualKeyWorkList(annualKeyWork);
    }

    /**
     * 年度重点工作获取部门是否引用
     *
     * @param annualKeyWorkDetailDTO 年度重点工作详情表
     * @return list
     */
    @Override
    public List<AnnualKeyWorkDetailDTO> remoteAnnualKeyWorkDepartment(AnnualKeyWorkDetailDTO annualKeyWorkDetailDTO) {
        Map<String, Object> params = annualKeyWorkDetailDTO.getParams();
        AnnualKeyWorkDetail annualKeyWorkDetail = new AnnualKeyWorkDetail();
        annualKeyWorkDetail.setParams(params);
        BeanUtils.copyProperties(annualKeyWorkDetailDTO, annualKeyWorkDetail);
        return annualKeyWorkMapper.remoteAnnualKeyWorkDepartment(annualKeyWorkDetail);
    }

    /**
     * 逻辑删除年度重点工作表信息
     *
     * @param annualKeyWorkDTO 年度重点工作表
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteAnnualKeyWorkByAnnualKeyWorkId(AnnualKeyWorkDTO annualKeyWorkDTO) {
        Long annualKeyWorkId = annualKeyWorkDTO.getAnnualKeyWorkId();
        AnnualKeyWorkDTO annualKeyWorkById = annualKeyWorkMapper.selectAnnualKeyWorkByAnnualKeyWorkId(annualKeyWorkId);
        if (StringUtils.isNull(annualKeyWorkById))
            throw new ServiceException("删除年度重点工作表已不存在");
        AnnualKeyWork annualKeyWork = new AnnualKeyWork();
        annualKeyWork.setAnnualKeyWorkId(annualKeyWorkId);
        annualKeyWork.setUpdateTime(DateUtils.getNowDate());
        annualKeyWork.setUpdateBy(SecurityUtils.getUserId());
        annualKeyWorkMapper.logicDeleteAnnualKeyWorkByAnnualKeyWorkId(annualKeyWork);
        List<AnnualKeyWorkDetailDTO> annualKeyWorkDetailDTOS = annualKeyWorkDetailService.selectAnnualKeyWorkDetailByAnnualKeyWorkId(annualKeyWorkId);
        if (StringUtils.isEmpty(annualKeyWorkDetailDTOS))
            return 1;
        List<Long> annualKeyWorkDetailIds = annualKeyWorkDetailDTOS.stream().map(AnnualKeyWorkDetailDTO::getAnnualKeyWorkDetailId).collect(Collectors.toList());
        annualKeyWorkDetailService.logicDeleteAnnualKeyWorkDetailByAnnualKeyWorkDetailIds(annualKeyWorkDetailIds);
        return 1;
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

