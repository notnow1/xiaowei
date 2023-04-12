package net.qixiaowei.system.manage.service.impl.basic;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.datascope.annotation.DataScope;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.integration.security.utils.UserUtils;
import net.qixiaowei.operate.cloud.api.dto.employee.EmployeeBudgetDTO;
import net.qixiaowei.operate.cloud.api.dto.product.ProductDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.EmpSalaryAdjustPlanDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.AreaDTO;
import net.qixiaowei.operate.cloud.api.remote.employee.RemoteEmployeeBudgetService;
import net.qixiaowei.operate.cloud.api.remote.product.RemoteProductService;
import net.qixiaowei.operate.cloud.api.remote.salary.RemoteSalaryAdjustPlanService;
import net.qixiaowei.operate.cloud.api.remote.targetManager.RemoteAreaService;
import net.qixiaowei.system.manage.api.domain.basic.Department;
import net.qixiaowei.system.manage.api.domain.basic.OfficialRankDecompose;
import net.qixiaowei.system.manage.api.domain.basic.OfficialRankSystem;
import net.qixiaowei.system.manage.api.dto.basic.*;
import net.qixiaowei.system.manage.api.dto.system.RegionDTO;
import net.qixiaowei.system.manage.mapper.basic.DepartmentPostMapper;
import net.qixiaowei.system.manage.mapper.basic.OfficialRankSystemMapper;
import net.qixiaowei.system.manage.mapper.basic.PostMapper;
import net.qixiaowei.system.manage.service.basic.IDepartmentService;
import net.qixiaowei.system.manage.service.basic.IOfficialRankDecomposeService;
import net.qixiaowei.system.manage.service.basic.IOfficialRankSystemService;
import net.qixiaowei.system.manage.service.system.IRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


/**
 * OfficialRankSystemService业务层处理
 *
 * @author Graves
 * @since 2022-10-07
 */
@Service
public class OfficialRankSystemServiceImpl implements IOfficialRankSystemService {
    @Autowired
    private OfficialRankSystemMapper officialRankSystemMapper;

    @Autowired
    private IOfficialRankDecomposeService officialRankDecomposeService;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private RemoteAreaService areaService;

    @Autowired
    private RemoteProductService productService;

    @Autowired
    private IDepartmentService departmentService;

    @Autowired
    private DepartmentPostMapper departmentPostMapper;

    @Autowired
    private IRegionService regionService;

    @Autowired
    private RemoteSalaryAdjustPlanService remoteSalaryAdjustPlanService;

    @Autowired
    private RemoteEmployeeBudgetService employeeBudgetService;

    /**
     * 查询职级体系表
     *
     * @param officialRankSystemId 职级体系表主键
     * @return 职级体系表
     */
    @Override
    public OfficialRankSystemDTO selectOfficialRankSystemByOfficialRankSystemId(Long officialRankSystemId) {
        return officialRankSystemMapper.selectOfficialRankSystemByOfficialRankSystemId(officialRankSystemId);
    }

    /**
     * 查询职级体系表列表
     *
     * @param officialRankSystemDTO 职级体系表
     * @return 职级体系表
     */
    @Override
    public List<OfficialRankSystemDTO> selectOfficialRankSystemList(OfficialRankSystemDTO officialRankSystemDTO) {
        OfficialRankSystem officialRankSystem = new OfficialRankSystem();
        BeanUtils.copyProperties(officialRankSystemDTO, officialRankSystem);
        List<OfficialRankSystemDTO> officialRankSystemDTOS = officialRankSystemMapper.selectOfficialRankSystemList(officialRankSystem);
        writeOfficialRank(officialRankSystemDTOS);
        return officialRankSystemDTOS;
    }

    @Override
    public void handleResult(List<OfficialRankSystemDTO> result) {
        if (StringUtils.isNotEmpty(result)) {
            Set<Long> userIds = result.stream().map(OfficialRankSystemDTO::getCreateBy).collect(Collectors.toSet());
            Map<Long, String> employeeNameMap = UserUtils.getEmployeeNameMap(userIds);
            result.forEach(entity -> {
                Long userId = entity.getCreateBy();
                entity.setCreateByName(employeeNameMap.get(userId));
            });
        }
    }

    /**
     * 分页查询list
     *
     * @param officialRankSystemDTO 职级体系表
     * @return
     */
    @DataScope(businessAlias = "ors")
    @Override
    public List<OfficialRankSystemDTO> selectOfficialRankSystemPageList(OfficialRankSystemDTO officialRankSystemDTO) {
        OfficialRankSystem officialRankSystem = new OfficialRankSystem();
        BeanUtils.copyProperties(officialRankSystemDTO, officialRankSystem);
        Map<String, Object> params = officialRankSystemDTO.getParams();
        officialRankSystem.setParams(params);
        List<OfficialRankSystemDTO> officialRankSystemDTOS = officialRankSystemMapper.selectOfficialRankSystemList(officialRankSystem);
        writeOfficialRank(officialRankSystemDTOS);
        this.handleResult(officialRankSystemDTOS);
        return officialRankSystemDTOS;
    }

    /**
     * 职级分解维度下拉框
     *
     * @param type   分解类型
     * @param status 状态
     * @return dropList
     */
    @Override
    public List<Map<String, Object>> decomposeDrop(Integer type, Integer status) {
        List<Map<String, Object>> dropList = new ArrayList<>();
        switch (type) {
            case 1:// todo 1部门
                Department department1 = new Department();
                department1.setStatus(status);
                List<DepartmentDTO> listDepartment = departmentService.dropList(department1);
                if (StringUtils.isNotEmpty(listDepartment)) {
                    for (DepartmentDTO department : listDepartment) {
                        HashMap<String, Object> departmentMap = new HashMap<>();
                        departmentMap.put("rankDecomposeDimension", department.getDepartmentId().toString());
                        departmentMap.put("rankDecomposeDimensionName", department.getDepartmentName());
                        departmentMap.put("status", department.getStatus());
                        dropList.add(departmentMap);
                    }
                    return dropList;
                }
                break;
            case 2:// todo 2区域
                AreaDTO areaDTO = new AreaDTO();
                R<List<AreaDTO>> listArea = areaService.dropList(areaDTO, SecurityConstants.INNER);
                if (listArea.getCode() == 200 && StringUtils.isNotEmpty(listArea.getData())) {
                    List<AreaDTO> data = listArea.getData();
                    for (AreaDTO area : data) {
                        HashMap<String, Object> areaMap = new HashMap<>();
                        areaMap.put("rankDecomposeDimension", area.getAreaId().toString());
                        areaMap.put("rankDecomposeDimensionName", area.getAreaName());
                        dropList.add(areaMap);
                    }
                    return dropList;
                }
                break;
            case 3:// todo 3省份
                List<RegionDTO> regionDTOS = regionService.selectRegionByLevel(1);
                if (StringUtils.isNotEmpty(regionDTOS)) {
                    for (RegionDTO region : regionDTOS) {
                        HashMap<String, Object> regionMap = new HashMap<>();
                        regionMap.put("rankDecomposeDimension", region.getRegionId().toString());
                        regionMap.put("rankDecomposeDimensionName", region.getRegionName());
                        dropList.add(regionMap);
                    }
                    return dropList;
                }
                break;
            case 4:// todo 4产品
                ProductDTO productDTO = new ProductDTO();
                productDTO.setListingFlag(status);
                R<List<ProductDTO>> listProduct = productService.dropList(productDTO, SecurityConstants.INNER);
                if (listProduct.getCode() == 200 && StringUtils.isNotEmpty(listProduct.getData())) {
                    List<ProductDTO> data = listProduct.getData();
                    for (ProductDTO product : data) {
                        HashMap<String, Object> productMap = new HashMap<>();
                        productMap.put("rankDecomposeDimension", product.getProductId().toString());
                        productMap.put("rankDecomposeDimensionName", product.getProductName());
                        dropList.add(productMap);
                    }
                    return dropList;
                }
                break;
        }
        throw new ServiceException("请先配置分解维度");
    }

    /**
     * 通过ID集合查找职级等级列表
     *
     * @param officialRankSystemIds
     * @return
     */
    @Override
    public List<OfficialRankSystemDTO> selectOfficialRankSystemByOfficialRankSystemIds(List<Long> officialRankSystemIds) {
        return officialRankSystemMapper.selectOfficialRankSystemByOfficialRankSystemIds(officialRankSystemIds);
    }

    /**
     * 通过Id查找职级上下限
     *
     * @param officialRankSystemId
     * @return
     */
    @Override
    public List<String> selectOfficialRankByOfficialRankSystemId(Long officialRankSystemId) {
        OfficialRankSystemDTO officialRankSystemDTO = officialRankSystemMapper.selectOfficialRankSystemByOfficialRankSystemId(officialRankSystemId);
        List<String> rankList = new ArrayList<>();
        if (StringUtils.isNull(officialRankSystemDTO)) {
            throw new ServiceException("当前职级为空");
        }
        int rankStart = officialRankSystemDTO.getRankStart();
        int rankEnd = officialRankSystemDTO.getRankEnd();
        String rankPrefixCode;
        if (StringUtils.isNull(officialRankSystemDTO.getRankPrefixCode())) {
            rankPrefixCode = "";
        }
        rankPrefixCode = officialRankSystemDTO.getRankPrefixCode();
        while (rankStart < rankEnd) {
            rankStart++;
            rankList.add(rankPrefixCode + rankStart);
        }
        return rankList;
    }

    /**
     * 通过Id查找职级上下限
     *
     * @param officialRankSystemId 职级ID
     * @return
     */
    @Override
    public List<Map<String, String>> selectOfficialRankMapBySystemId(Long officialRankSystemId) {
        if (StringUtils.isNull(officialRankSystemId)) {
            throw new ServiceException("职级ID不能为空");
        }
        List<Map<String, String>> list = new ArrayList<>();
        OfficialRankSystemDTO officialRankSystemDTO = officialRankSystemMapper.selectOfficialRankSystemByOfficialRankSystemId(officialRankSystemId);
        String rankPrefixCode = officialRankSystemDTO.getRankPrefixCode();
        Integer rankStart = officialRankSystemDTO.getRankStart();
        Integer rankEnd = officialRankSystemDTO.getRankEnd();
        for (int i = rankStart; i < rankEnd + 1; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("officialRank", String.valueOf(i));
            map.put("officialRankName", rankPrefixCode + String.valueOf(i));
            list.add(map);
        }
        return list;
    }

    /**
     * 根据岗位查询调薪的职级下拉列表
     *
     * @param postId 岗位ID
     * @return List
     */
    @Override
    public List<Map<String, String>> selectOfficialRankMapByPostId(Long postId) {
        if (StringUtils.isNull(postId)) {
            throw new ServiceException("岗位ID不能为空");
        }
        List<Map<String, String>> list = new ArrayList<>();
        OfficialRankSystemDTO officialRankSystemDTO = officialRankSystemMapper.selectOfficialRankSystemByPostId(postId);
        String rankPrefixCode = officialRankSystemDTO.getRankPrefixCode();
        Integer postRankUpper = officialRankSystemDTO.getPostRankUpper();
        Integer postRankLower = officialRankSystemDTO.getPostRankLower();
        for (int i = postRankLower; i < postRankUpper + 1; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("officialRankName", rankPrefixCode + i);
            map.put("officialRank", String.valueOf(i));
            list.add(map);
        }
        return list;
    }

    /**
     * 查询岗位职级一览表
     *
     * @param officialRankSystemDTO 职级体系
     * @return List
     */
    @Override
    public Map<String, Object> selectRankViewList(OfficialRankSystemDTO officialRankSystemDTO) {
        // 当前的职级体系
        Long officialRankSystemId = officialRankSystemDTO.getOfficialRankSystemId();
        if (StringUtils.isNull(officialRankSystemId)) {
            officialRankSystemId = officialRankSystemMapper.selectFirstOfficialRankSystem();
            if (StringUtils.isNull(officialRankSystemId)) {
                throw new ServiceException("请先配置职级体系");
            }
        }
        List<String> performanceNames = new ArrayList<>();
        Map<Integer, String> rankMap = selectOfficialRankBySystemId(officialRankSystemId, performanceNames);
        String performanceName = performanceNames.get(0);
        List<Integer> rankList = new ArrayList<>(rankMap.keySet());
        Collections.sort(rankList);
        // 查询所有一级部门
        List<DepartmentDTO> departmentDTOS = departmentService.selectDepartmentByLevel(1);
        if (StringUtils.isEmpty(departmentDTOS)) {
            return null;
        }
        List<Long> departmentIds = departmentDTOS.stream().map(DepartmentDTO::getDepartmentId).collect(Collectors.toList());
        List<DepartmentPostDTO> departmentPostDTOS = departmentPostMapper.selectPostByRankSystemId(departmentIds, officialRankSystemId);
        List<Map<String, Object>> departmentNames = new ArrayList<>();
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("label", 1);
        objectMap.put("value", performanceName);
        departmentNames.add(objectMap);
        for (int i = 0; i < departmentDTOS.size(); i++) {
            DepartmentDTO departmentDTO = departmentDTOS.get(i);
            List<DepartmentPostDTO> departmentPostDTOList = new ArrayList<>();
            objectMap = new HashMap<>();
            objectMap.put("label", i + 2);
            objectMap.put("value", departmentDTO.getDepartmentName());
            departmentNames.add(objectMap);
            for (DepartmentPostDTO postDTO : departmentPostDTOS) {
                if (departmentDTO.getDepartmentId().equals(postDTO.getDepartmentId())) {
                    departmentPostDTOList.add(postDTO);
                }
            }
            departmentDTO.setDepartmentPostDTOList(departmentPostDTOList);
        }
        Map<String, Object> maps = new HashMap<>();
        List<Map<Integer, Object>> list = new ArrayList<>();
        for (Integer rank : rankList) {
            Map<Integer, Object> map = new LinkedHashMap<>();
            map.put(1, rankMap.get(rank));
            for (int i = 0; i < departmentDTOS.size(); i++) {
                DepartmentDTO departmentDTO = departmentDTOS.get(i);
                List<DepartmentPostDTO> postDTOList = departmentDTO.getDepartmentPostDTOList();
                Set<String> postList = new HashSet<>();
                for (DepartmentPostDTO departmentPostDTO : postDTOList) {
                    if (StringUtils.isNull(departmentPostDTO.getPostRank())) {
                        continue;
                    }
                    if (departmentPostDTO.getPostRank().equals(rank)) {
                        postList.add(departmentPostDTO.getPostName());
                    }
                }
                map.put(i + 2, postList);
            }
            list.add(map);
        }
        maps.put("header", departmentNames);
        maps.put("tableValue", list);
        return maps;
    }

    /**
     * 通过Id查找职级上下限
     *
     * @param officialRankSystemId 职级ID
     * @param performanceNames     职级体系名称集合
     * @return Map
     */
    public Map<Integer, String> selectOfficialRankBySystemId(Long officialRankSystemId, List<String> performanceNames) {
        if (StringUtils.isNull(officialRankSystemId)) {
            throw new ServiceException("职级ID不能为空");
        }
        List<Map<String, String>> list = new ArrayList<>();
        OfficialRankSystemDTO officialRankSystemDTO = officialRankSystemMapper.selectOfficialRankSystemByOfficialRankSystemId(officialRankSystemId);
        if (StringUtils.isNull(officialRankSystemDTO)) {
            throw new ServiceException("请传入有存在的职级信息");
        }
        performanceNames.add(officialRankSystemDTO.getOfficialRankSystemName());
        String rankPrefixCode = officialRankSystemDTO.getRankPrefixCode();
        Integer rankStart = officialRankSystemDTO.getRankStart();
        Integer rankEnd = officialRankSystemDTO.getRankEnd();
        Map<Integer, String> map = new HashMap<>();
        for (int i = rankStart; i < rankEnd + 1; i++) {
            map.put(i, rankPrefixCode + String.valueOf(i));
        }
        return map;
    }

    /**
     * 拼接职级字段
     *
     * @param officialRankSystemDTOS 职级体系
     */
    private void writeOfficialRank(List<OfficialRankSystemDTO> officialRankSystemDTOS) {
        String rankPrefixCode;
        int rankStart;
        int rankEnd;
        for (OfficialRankSystemDTO officialRankSystemDTO : officialRankSystemDTOS) {
            StringBuilder officialRank = new StringBuilder("");
            rankPrefixCode = officialRankSystemDTO.getRankPrefixCode();
            if (StringUtils.isNull(rankPrefixCode)) {
                rankPrefixCode = "";
            }
            rankStart = officialRankSystemDTO.getRankStart();
            rankEnd = officialRankSystemDTO.getRankEnd();
            while (rankStart < rankEnd) {
                officialRank.append(rankPrefixCode).append(rankStart).append(",");
                rankStart++;
            }
            officialRank.append(rankPrefixCode).append(rankEnd);
            officialRankSystemDTO.setOfficialRank(officialRank.toString());
        }
    }

    /**
     * 新增职级体系表
     *
     * @param officialRankSystemDTO 职级体系表
     * @return 结果
     */
    @Transactional
    @Override
    public OfficialRankSystemDTO insertOfficialRankSystem(OfficialRankSystemDTO officialRankSystemDTO) {
        String officialRankSystemName = officialRankSystemDTO.getOfficialRankSystemName();
        Integer rankStart = officialRankSystemDTO.getRankStart();
        Integer rankEnd = officialRankSystemDTO.getRankEnd();
        String rankPrefixCode = officialRankSystemDTO.getRankPrefixCode();
        List<OfficialRankDecomposeDTO> officialRankDecomposeDTOS = officialRankSystemDTO.getOfficialRankDecomposeDTOS();
        if (StringUtils.isEmpty(officialRankSystemName)) {
            throw new ServiceException("职级体系名称不能为空");
        }
        if (StringUtils.isNull(rankStart)) {
            throw new ServiceException("起始级别不能为空");
        }
        if (StringUtils.isNull(rankEnd)) {
            throw new ServiceException("终止级别不能为空");
        }
        if (rankEnd < rankStart) {
            throw new ServiceException("终止级别应大于起始级别");
        }
        OfficialRankSystemDTO officialRankByName = officialRankSystemMapper.officialRankByName(officialRankSystemName);
//        OfficialRankSystemDTO officialRankByPrefixCode = officialRankSystemMapper.officialRankByPrefixCode(rankPrefixCode);
        if (StringUtils.isNotNull(officialRankByName)) {
            throw new ServiceException("职级体系名称已存在");
        }
        if (StringUtils.isNotNull(rankPrefixCode)) {
            if (rankPrefixCode.length() > 5) {
                throw new ServiceException("职级体系级别前缀长度不能大于5");
            }
        }
//        if (StringUtils.isNotNull(officialRankByPrefixCode)) {
//            throw new ServiceException("职级体系级别前缀重复");
//        }
        OfficialRankSystem officialRankSystem = new OfficialRankSystem();
        BeanUtils.copyProperties(officialRankSystemDTO, officialRankSystem);
        officialRankSystem.setCreateBy(SecurityUtils.getUserId());
        officialRankSystem.setCreateTime(DateUtils.getNowDate());
        officialRankSystem.setUpdateTime(DateUtils.getNowDate());
        officialRankSystem.setUpdateBy(SecurityUtils.getUserId());
        officialRankSystem.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        officialRankSystemMapper.insertOfficialRankSystem(officialRankSystem);
        if (StringUtils.isNotEmpty(officialRankDecomposeDTOS)) {
            for (OfficialRankDecomposeDTO officialRankDecomposeDTO : officialRankDecomposeDTOS) {
                if (StringUtils.isNull(officialRankDecomposeDTO.getDecomposeDimension())) {
                    throw new ServiceException("请选择分解维度");
                }
                if (StringUtils.isNull(officialRankDecomposeDTO.getSalaryFactor())) {
                    throw new ServiceException("职级分解的工资系数不能为空");
                }
            }
            List<OfficialRankDecompose> officialRankDecomposes = officialRankDecomposeService.insertOfficialRankDecomposes(officialRankDecomposeDTOS, officialRankSystem);// 新增操作
            officialRankSystemDTO.setOfficialRankDecomposes(officialRankDecomposes);
        }
        officialRankSystemDTO.setOfficialRankSystemId(officialRankSystem.getOfficialRankSystemId());
        return officialRankSystemDTO;
    }

    /**
     * 修改职级体系表
     *
     * @param officialRankSystemDTO 职级体系表
     * @return 结果
     */
    @Transactional
    @Override
    public int updateOfficialRankSystem(OfficialRankSystemDTO officialRankSystemDTO) {
        Long officialRankSystemId = officialRankSystemDTO.getOfficialRankSystemId();
        String officialRankSystemName = officialRankSystemDTO.getOfficialRankSystemName();
        Integer rankStart = officialRankSystemDTO.getRankStart();
        Integer rankEnd = officialRankSystemDTO.getRankEnd();
        String rankPrefixCode = officialRankSystemDTO.getRankPrefixCode();
        Integer rankDecomposeDimensionAfter = officialRankSystemDTO.getRankDecomposeDimension();
        if (StringUtils.isNull(officialRankSystemId)) {
            throw new ServiceException("职级体系表id不能为空");
        }
        if (StringUtils.isNull(rankStart)) {
            throw new ServiceException("起始级别不能为空");
        }
        if (StringUtils.isNull(rankEnd)) {
            throw new ServiceException("终止级别不能为空");
        }
        if (rankEnd < rankStart) {
            throw new ServiceException("终止级别应大于起始级别");
        }
        OfficialRankSystemDTO officialRankByName = officialRankSystemMapper.officialRankByName(officialRankSystemName);
//        OfficialRankSystemDTO officialRankByPrefixCode = officialRankSystemMapper.officialRankByPrefixCode(rankPrefixCode);
        if (StringUtils.isNotNull(officialRankByName)) {
            if (!officialRankByName.getOfficialRankSystemId().equals(officialRankSystemId)) {
                throw new ServiceException("职级体系名称已存在");
            }
        }
        if (StringUtils.isNotNull(rankPrefixCode)) {
            if (rankPrefixCode.length() > 5) {
                throw new ServiceException("职级体系级别前缀长度不能大于5");
            }
        }
        OfficialRankSystem officialRankSystem = new OfficialRankSystem();
        BeanUtils.copyProperties(officialRankSystemDTO, officialRankSystem);
        officialRankSystem.setUpdateTime(DateUtils.getNowDate());
        officialRankSystem.setUpdateBy(SecurityUtils.getUserId());
        officialRankSystemMapper.updateOfficialRankSystem(officialRankSystem);
        List<OfficialRankDecomposeDTO> officialRankDecomposeDTOAfter = officialRankSystemDTO.getOfficialRankDecomposeDTOS();
        if (StringUtils.isEmpty(officialRankDecomposeDTOAfter)) {
            return 1;
        }
        List<Long> decomposeDimensions = new ArrayList<>();
        for (OfficialRankDecomposeDTO officialRankDecomposeDTO : officialRankDecomposeDTOAfter) {
            Long decomposeDimension = officialRankDecomposeDTO.getDecomposeDimension();
            if (decomposeDimensions.contains(decomposeDimension)) {
                throw new ServiceException("分解维度不能重复");
            }
            decomposeDimensions.add(decomposeDimension);
        }
        for (OfficialRankDecomposeDTO officialRankDecomposeDTO : officialRankDecomposeDTOAfter) {
            if (StringUtils.isNull(officialRankDecomposeDTO.getDecomposeDimension())) {
                throw new ServiceException("请选择分解维度");
            }
            if (StringUtils.isNull(officialRankDecomposeDTO.getSalaryFactor())) {
                throw new ServiceException("职级分解的工资系数不能为空");
            }
        }
        Integer rankDecomposeDimensionBefore = officialRankSystem.getRankDecomposeDimension();
        if (rankDecomposeDimensionAfter.equals(rankDecomposeDimensionBefore)) {// 操作
            List<OfficialRankDecomposeDTO> officialRankDecomposeDTOSBefore = officialRankDecomposeService.selectOfficialRankDecomposeByOfficialRankSystemId(officialRankSystemId);
            return operateRankDecompose(officialRankDecomposeDTOSBefore, officialRankDecomposeDTOAfter, officialRankSystem);
        } else {// 新增
            officialRankSystem.setRankDecomposeDimension(rankDecomposeDimensionAfter);
            officialRankDecomposeService.insertOfficialRankDecomposes(officialRankDecomposeDTOAfter, officialRankSystem);
            officialRankDecomposeService.logicDeleteOfficialRankDecomposeByOfficialRankDecompose(officialRankSystemId, rankDecomposeDimensionBefore);
            return 1;
        }
    }

    /**
     * 更新操作
     *
     * @param officialRankDecomposeDTOSBefore 以前的
     * @param officialRankDecomposeDTOAfter   以后的
     * @return 结果
     */
    private int operateRankDecompose(List<OfficialRankDecomposeDTO> officialRankDecomposeDTOSBefore, List<OfficialRankDecomposeDTO> officialRankDecomposeDTOAfter, OfficialRankSystem officialRankSystem) {
        // 交集
        List<OfficialRankDecomposeDTO> updateOfficialRankDecompose =
                officialRankDecomposeDTOAfter.stream().filter(officialRankDecomposeDTO ->
                        officialRankDecomposeDTOSBefore.stream().map(OfficialRankDecomposeDTO::getOfficialRankDecomposeId)
                                .collect(Collectors.toList()).contains(officialRankDecomposeDTO.getOfficialRankDecomposeId())
                ).collect(Collectors.toList());
        // 差集 Before中After的补集
        List<OfficialRankDecomposeDTO> delOfficialRankDecompose =
                officialRankDecomposeDTOSBefore.stream().filter(officialRankDecomposeDTO ->
                        !officialRankDecomposeDTOAfter.stream().map(OfficialRankDecomposeDTO::getOfficialRankDecomposeId)
                                .collect(Collectors.toList()).contains(officialRankDecomposeDTO.getOfficialRankDecomposeId())
                ).collect(Collectors.toList());
        // 差集 After中Before的补集
        List<OfficialRankDecomposeDTO> addOfficialRankDecompose =
                officialRankDecomposeDTOAfter.stream().filter(officialRankDecomposeDTO ->
                        !officialRankDecomposeDTOSBefore.stream().map(OfficialRankDecomposeDTO::getOfficialRankDecomposeId)
                                .collect(Collectors.toList()).contains(officialRankDecomposeDTO.getOfficialRankDecomposeId())
                ).collect(Collectors.toList());
        try {
            if (StringUtils.isNotEmpty(delOfficialRankDecompose)) {
                officialRankDecomposeService.deleteOfficialRankDecomposeByOfficialRankDecomposeIds(delOfficialRankDecompose);
            }
            if (StringUtils.isNotEmpty(addOfficialRankDecompose)) {
                officialRankDecomposeService.insertOfficialRankDecomposes(addOfficialRankDecompose, officialRankSystem);
            }
            if (StringUtils.isNotEmpty(updateOfficialRankDecompose)) {
                officialRankDecomposeService.updateOfficialRankDecomposes(updateOfficialRankDecompose, officialRankSystem);
            }
        } catch (ServiceException e) {
            throw new ServiceException(e.toString());
        }
        return 1;
    }

    /**
     * 逻辑批量删除职级体系表
     *
     * @param officialRankSystemIds 需要删除的职级体系表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteOfficialRankSystemByOfficialRankSystemIds(List<Long> officialRankSystemIds) {
        if (StringUtils.isEmpty(officialRankSystemIds)) {
            throw new ServiceException("职级体系id列表不能为空");
        }
        List<OfficialRankSystemDTO> existByOfficialRankSystemDTOS = officialRankSystemMapper.isExistByOfficialRankSystemIds(officialRankSystemIds);
        if (existByOfficialRankSystemDTOS.size() < officialRankSystemIds.size()) {
            throw new ServiceException("当前的职级体系已不存在");
        }
        isQuote(officialRankSystemIds, existByOfficialRankSystemDTOS);
        int i = officialRankDecomposeService.logicDeleteOfficialRankDecomposeByOfficialRankSystemIds(officialRankSystemIds);
        if (i == 0) {
            throw new ServiceException("删除失败，请检查相关联的的职级分解是否正常");
        }
        return officialRankSystemMapper.logicDeleteOfficialRankSystemByOfficialRankSystemIds(officialRankSystemIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除职级体系表信息
     *
     * @param officialRankSystemId 职级体系表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteOfficialRankSystemByOfficialRankSystemId(Long officialRankSystemId) {
        return officialRankSystemMapper.deleteOfficialRankSystemByOfficialRankSystemId(officialRankSystemId);
    }

    /**
     * 职级体系表详情
     *
     * @param officialRankSystemId 职级体系ID
     * @return
     */
    @Override
    public OfficialRankSystemDTO detailOfficialRankSystem(Long officialRankSystemId) {
        if (StringUtils.isNull(officialRankSystemId)) {
            throw new ServiceException("职级体系ID不能为空");
        }
        OfficialRankSystemDTO officialRankSystemDTO = officialRankSystemMapper.isExistByOfficialRankSystemId(officialRankSystemId);
        if (StringUtils.isNull(officialRankSystemDTO)) {
            throw new ServiceException("该职级体系已不存在");
        }
        Integer rankDecomposeDimension = officialRankSystemDTO.getRankDecomposeDimension();
        List<OfficialRankSystemDTO> officialRankSystemDTOS = new ArrayList<>();
        officialRankSystemDTOS.add(officialRankSystemDTO);
        writeOfficialRank(officialRankSystemDTOS);
        if (StringUtils.isNull(rankDecomposeDimension)) {
            return officialRankSystemDTO;
        }
        List<OfficialRankDecomposeDTO> officialRankDecomposeDTOS = officialRankDecomposeService.selectOfficialRankDecomposeAndNameByOfficialRankSystemId(officialRankSystemId, rankDecomposeDimension);
        officialRankSystemDTO.setOfficialRankDecomposeDTOS(officialRankDecomposeDTOS);
        return officialRankSystemDTO;
    }

    /**
     * 逻辑删除职级体系表信息
     *
     * @param officialRankSystemDTO 职级体系表
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteOfficialRankSystemByOfficialRankSystemId(OfficialRankSystemDTO officialRankSystemDTO) {
        Long officialRankSystemId = officialRankSystemDTO.getOfficialRankSystemId();
        if (StringUtils.isNull(officialRankSystemId)) {
            throw new ServiceException("职级体系id不能为空");
        }
        List<Long> officialRankSystemIds = new ArrayList<>();
        officialRankSystemIds.add(officialRankSystemId);
        List<OfficialRankSystemDTO> existByOfficialRankSystemIds = officialRankSystemMapper.isExistByOfficialRankSystemIds(officialRankSystemIds);
        if (StringUtils.isEmpty(existByOfficialRankSystemIds)) {
            throw new ServiceException("当前的职级体系已不存在");
        }
        isQuote(officialRankSystemIds, existByOfficialRankSystemIds);
        int i = officialRankDecomposeService.logicDeleteOfficialRankDecomposeByOfficialRankSystemId(officialRankSystemId);
        if (i == 0) {
            throw new ServiceException("删除失败，请检查相关联的的职级分解是否正常");
        }
        OfficialRankSystem officialRankSystem = new OfficialRankSystem();
        BeanUtils.copyProperties(officialRankSystemDTO, officialRankSystem);
        return officialRankSystemMapper.logicDeleteOfficialRankSystemByOfficialRankSystemId(officialRankSystem, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 检查职级体系是否被引用
     *
     * @param officialRankSystemIds         职级体系id集合
     * @param existByOfficialRankSystemDTOS 职级集合
     */
    private void isQuote(List<Long> officialRankSystemIds, List<OfficialRankSystemDTO> existByOfficialRankSystemDTOS) {
        StringBuilder quoteReminder = new StringBuilder("");
        //岗位配置-职级体系
        List<PostDTO> postDTOS = postMapper.selectPostByOfficialRankSystemIds(officialRankSystemIds);
        if (StringUtils.isNotEmpty(postDTOS)) {
            throw new ServiceException("数据被引用!");
//            StringBuilder officialNames = new StringBuilder("");
//            for (PostDTO postDTO : postDTOS) {
//                for (OfficialRankSystemDTO officialRankSystemDTO : existByOfficialRankSystemDTOS) {
//                    if (officialRankSystemDTO.getOfficialRankSystemId().equals(postDTO.getOfficialRankSystemId())) {
//                        officialNames.append(officialRankSystemDTO.getOfficialRankSystemName()).append(",");
//                        break;
//                    }
//                }
//            }
//            quoteReminder.append("职级配置【").append(officialNames.deleteCharAt(officialNames.length() - 1)).append("】已被岗位配置中的【职级体系】引用 无法删除\n");
        }
        //人力预算调控-职级体系
        R<List<EmployeeBudgetDTO>> employeeBudgetR = employeeBudgetService.selectBySystemIds(officialRankSystemIds, SecurityConstants.INNER);
        if (employeeBudgetR.getCode() != 200) {
            throw new ServiceException("远程调用人力预算调控失败 请联系管理员");
        }
        List<EmployeeBudgetDTO> employeeBudgetDTOS = employeeBudgetR.getData();
        if (StringUtils.isNotEmpty(employeeBudgetDTOS)) {
            throw new ServiceException("数据被引用!");
//            StringBuilder officialNames = new StringBuilder("");
//            for (EmployeeBudgetDTO employeeBudgetDTO : employeeBudgetDTOS) {
//                for (OfficialRankSystemDTO officialRankSystemDTO : existByOfficialRankSystemDTOS) {
//                    if (officialRankSystemDTO.getOfficialRankSystemId().equals(employeeBudgetDTO.getOfficialRankSystemId())) {
//                        officialNames.append(officialRankSystemDTO.getOfficialRankSystemName()).append(",");
//                        break;
//                    }
//                }
//            }
//            quoteReminder.append("职级配置【").append(officialNames.deleteCharAt(officialNames.length() - 1)).append("】已被人力预算调控中的【职级体系】引用 无法删除\n");
        }
        //个人调薪计划-本次调整-调整职级
        R<List<EmpSalaryAdjustPlanDTO>> listR = remoteSalaryAdjustPlanService.selectBySystemIds(officialRankSystemIds, SecurityConstants.INNER);
        if (listR.getCode() != 200) {
            throw new ServiceException("远程调用个人调薪失败 请联系管理员");
        }
        List<EmpSalaryAdjustPlanDTO> empSalaryAdjustPlanDTOS = listR.getData();
        if (StringUtils.isNotEmpty(empSalaryAdjustPlanDTOS)) {
            throw new ServiceException("数据被引用!");
//            StringBuilder officialNames = new StringBuilder("");
//            for (EmpSalaryAdjustPlanDTO empSalaryAdjustPlanDTO : empSalaryAdjustPlanDTOS) {
//                for (OfficialRankSystemDTO officialRankSystemDTO : existByOfficialRankSystemDTOS) {
//                    if (officialRankSystemDTO.getOfficialRankSystemId().equals(empSalaryAdjustPlanDTO.getAdjustOfficialRankSystemId())) {
//                        officialNames.append(officialRankSystemDTO.getOfficialRankSystemName()).append(",");
//                        break;
//                    }
//                }
//            }
//            quoteReminder.append("职级配置【").append(officialNames.deleteCharAt(officialNames.length() - 1)).append("】已被个人调薪中的【调整职级】引用 无法删除\n");
        }
//        if (quoteReminder.length() != 0) {
//            throw new ServiceException(quoteReminder.toString());
//        }
    }

    /**
     * 物理删除职级体系表信息
     *
     * @param officialRankSystemDTO 职级体系表
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteOfficialRankSystemByOfficialRankSystemId(OfficialRankSystemDTO officialRankSystemDTO) {
        OfficialRankSystem officialRankSystem = new OfficialRankSystem();
        BeanUtils.copyProperties(officialRankSystemDTO, officialRankSystem);
        return officialRankSystemMapper.deleteOfficialRankSystemByOfficialRankSystemId(officialRankSystem.getOfficialRankSystemId());
    }

    /**
     * 物理批量删除职级体系表
     *
     * @param officialRankSystemDtos 需要删除的职级体系表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteOfficialRankSystemByOfficialRankSystemIds(List<OfficialRankSystemDTO> officialRankSystemDtos) {
        List<Long> stringList = new ArrayList<>();
        for (OfficialRankSystemDTO officialRankSystemDTO : officialRankSystemDtos) {
            stringList.add(officialRankSystemDTO.getOfficialRankSystemId());
        }
        return officialRankSystemMapper.deleteOfficialRankSystemByOfficialRankSystemIds(stringList);
    }

    /**
     * 批量新增职级体系表信息
     *
     * @param officialRankSystemDtos 职级体系表对象
     */
    @Override
    @Transactional
    public int insertOfficialRankSystems(List<OfficialRankSystemDTO> officialRankSystemDtos) {
        List<OfficialRankSystem> officialRankSystemList = new ArrayList<>();

        for (OfficialRankSystemDTO officialRankSystemDTO : officialRankSystemDtos) {
            OfficialRankSystem officialRankSystem = new OfficialRankSystem();
            BeanUtils.copyProperties(officialRankSystemDTO, officialRankSystem);
            officialRankSystem.setCreateBy(SecurityUtils.getUserId());
            officialRankSystem.setCreateTime(DateUtils.getNowDate());
            officialRankSystem.setUpdateTime(DateUtils.getNowDate());
            officialRankSystem.setUpdateBy(SecurityUtils.getUserId());
            officialRankSystem.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            officialRankSystemList.add(officialRankSystem);
        }
        return officialRankSystemMapper.batchOfficialRankSystem(officialRankSystemList);
    }

    /**
     * 批量修改职级体系表信息
     *
     * @param officialRankSystemDtos 职级体系表对象
     */
    @Override
    @Transactional
    public int updateOfficialRankSystems(List<OfficialRankSystemDTO> officialRankSystemDtos) {
        List<OfficialRankSystem> officialRankSystemList = new ArrayList<>();

        for (OfficialRankSystemDTO officialRankSystemDTO : officialRankSystemDtos) {
            OfficialRankSystem officialRankSystem = new OfficialRankSystem();
            BeanUtils.copyProperties(officialRankSystemDTO, officialRankSystem);
            officialRankSystem.setCreateBy(SecurityUtils.getUserId());
            officialRankSystem.setCreateTime(DateUtils.getNowDate());
            officialRankSystem.setUpdateTime(DateUtils.getNowDate());
            officialRankSystem.setUpdateBy(SecurityUtils.getUserId());
            officialRankSystemList.add(officialRankSystem);
        }
        return officialRankSystemMapper.updateOfficialRankSystems(officialRankSystemList);
    }
}

