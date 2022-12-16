package net.qixiaowei.system.manage.service.impl.basic;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalObjectsDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.EmpSalaryAdjustPlanDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDetailsDTO;
import net.qixiaowei.operate.cloud.api.remote.performance.RemotePerformanceAppraisalService;
import net.qixiaowei.operate.cloud.api.remote.salary.RemoteSalaryAdjustPlanService;
import net.qixiaowei.operate.cloud.api.remote.targetManager.RemoteDecomposeService;
import net.qixiaowei.system.manage.api.domain.basic.*;
import net.qixiaowei.system.manage.api.dto.basic.*;
import net.qixiaowei.system.manage.api.dto.system.RegionDTO;
import net.qixiaowei.system.manage.api.vo.basic.EmployeeSalarySnapVO;
import net.qixiaowei.system.manage.excel.basic.EmployeeExcel;
import net.qixiaowei.system.manage.mapper.basic.*;
import net.qixiaowei.system.manage.mapper.system.RegionMapper;
import net.qixiaowei.system.manage.mapper.user.UserMapper;
import net.qixiaowei.system.manage.service.basic.ICountryService;
import net.qixiaowei.system.manage.service.basic.IDepartmentService;
import net.qixiaowei.system.manage.service.basic.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * EmployeeService业务层处理
 *
 * @author TANGMICHI
 * @since 2022-09-30
 */
@Service
public class EmployeeServiceImpl implements IEmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private EmployeeInfoMapper employeeInfoMapper;
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private DepartmentPostMapper departmentPostMapper;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RemoteDecomposeService remoteDecomposeService;
    @Autowired
    private OfficialRankSystemMapper officialRankSystemMapper;
    @Autowired
    private IDepartmentService departmentService;
    @Autowired
    private NationMapper nationMapper;
    @Autowired
    private CountryMapper countryMapper;
    @Autowired
    private ICountryService countryService;
    @Autowired
    private RegionMapper regionMapper;
    @Autowired
    private RemotePerformanceAppraisalService performanceAppraisalService;
    @Autowired
    private RemoteSalaryAdjustPlanService salaryAdjustPlanService;


    /**
     * 查询员工表
     *
     * @param employeeId 员工表主键
     * @return 员工表
     */
    @Override
    public EmployeeDTO selectEmployeeByEmployeeId(Long employeeId) {
        return employeeMapper.selectEmployeeByEmployeeId(employeeId);
    }

    @Override
    public List<EmployeeDTO> selectEmployeeByEmployeeIds(List<Long> employeeIds) {
        return employeeMapper.selectEmployeeByEmployeeIds(employeeIds);
    }

    /**
     * 查询员工表列表
     *
     * @param employeeDTO 员工表
     * @return 员工表
     */
    @Override
    public List<EmployeeDTO> selectEmployeeList(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        return employeeMapper.selectEmployeeList(employee);
    }

    /**
     * 查询员工表列表(下拉框)
     *
     * @param employeeDTO
     * @return
     */
    @Override
    public List<EmployeeDTO> selectDropEmployeeList(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        return employeeMapper.selectDropEmployeeList(employee);
    }

    /**
     * 根据code查询员工表列表
     *
     * @param employeeCodes code集合
     * @return
     */
    @Override
    public List<EmployeeDTO> selectCodeList(List<String> employeeCodes) {
        return employeeMapper.selectCodeList(employeeCodes);
    }

    /**
     * 通过部门，岗位，职级集合查询员工表
     *
     * @param idMaps id集合表
     * @return 员工表集合
     */
    public List<EmployeeDTO> selectEmployeeByPDRIds(Map<String, List<String>> idMaps) {
        List<String> departmentIds = idMaps.get("departmentIds");
        List<Long> departments = departmentIds.stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        List<String> postIds = idMaps.get("postIds");
        List<Long> post = departmentIds.stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        List<String> employeeRankNames = idMaps.get("rankNames");
        return employeeMapper.selectEmployeeByPDRIds(departments, post, employeeRankNames);
    }


    /**
     * 查询员工单条信息
     *
     * @param employeeDTO 员工表
     * @return
     */
    @Override
    public EmployeeDTO selectEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        return employeeMapper.selectEmployee(employee);
    }

    /**
     * 新增员工表
     *
     * @param employeeDTO 员工表
     * @return 结果
     */
    @Transactional
    @Override
    public EmployeeDTO insertEmployee(EmployeeDTO employeeDTO) {
        //查询是否已经存在员工
        EmployeeDTO employeeDTO1 = employeeMapper.selectEmployeeByEmployeeCode(employeeDTO.getEmployeeCode());
        if (null != employeeDTO1) {
            throw new ServiceException("工号已存在请重新添加！");
        }
        //员工表
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        employee.setCreateBy(SecurityUtils.getUserId());
        employee.setCreateTime(DateUtils.getNowDate());
        employee.setUpdateTime(DateUtils.getNowDate());
        employee.setUpdateBy(SecurityUtils.getUserId());
        employee.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        try {
            employeeMapper.insertEmployee(employee);
        } catch (Exception e) {
            throw new ServiceException("新增员工失败");
        }
        //员工信息表
        EmployeeInfo employeeInfo = new EmployeeInfo();
        BeanUtils.copyProperties(employeeDTO, employeeInfo);
        employeeInfo.setCreateBy(SecurityUtils.getUserId());
        employeeInfo.setCreateTime(DateUtils.getNowDate());
        employeeInfo.setUpdateTime(DateUtils.getNowDate());
        employeeInfo.setUpdateBy(SecurityUtils.getUserId());
        employeeInfo.setEmployeeId(employee.getEmployeeId());
        try {
            employeeInfoMapper.insertEmployeeInfo(employeeInfo);
        } catch (Exception e) {
            throw new ServiceException("新增员工信息失败");
        }
        employeeDTO.setEmployeeId(employee.getEmployeeId());
        return employeeDTO;
    }

    /**
     * 修改员工表
     *
     * @param employeeDTO 员工表
     * @return 结果
     */
    @Transactional
    @Override
    public int updateEmployee(EmployeeDTO employeeDTO) {
        int i = 0;
        //员工表
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        employee.setUpdateTime(DateUtils.getNowDate());
        employee.setUpdateBy(SecurityUtils.getUserId());
        employee.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        try {
            i = employeeMapper.updateEmployee(employee);
        } catch (Exception e) {
            throw new ServiceException("修改员工失败");
        }
        //查询人员主表
        EmployeeInfoDTO employeeInfoDTO = employeeInfoMapper.selectEmployeeInfoByEmployeeId(employeeDTO.getEmployeeId());
        if (StringUtils.isNull(employeeInfoDTO)) {
            throw new ServiceException("人员信息不存在！ 请联系管理员");
        }
        //员工信息表
        EmployeeInfo employeeInfo = new EmployeeInfo();
        BeanUtils.copyProperties(employeeDTO, employeeInfo);
        employeeInfo.setUpdateTime(DateUtils.getNowDate());
        employeeInfo.setUpdateBy(SecurityUtils.getUserId());
        employeeInfo.setEmployeeInfoId(employeeInfoDTO.getEmployeeInfoId());


        try {
            employeeInfoMapper.updateEmployeeInfo(employeeInfo);
        } catch (Exception e) {
            throw new ServiceException("修改员工信息失败");
        }
        return i;
    }

    /**
     * 逻辑批量删除员工表
     *
     * @param employeeIds 需要删除的员工表主键
     * @return 结果
     */

    @Transactional
    @Override
    public int logicDeleteEmployeeByEmployeeIds(List<Long> employeeIds) {
        int i = 0;
        //组织引用
        StringBuffer deptErreo = new StringBuffer();
        //岗位引用
        StringBuffer postErreo = new StringBuffer();
        //用户引用
        StringBuffer userErreo = new StringBuffer();
        //目标分解引用
        StringBuffer decomposeErreo = new StringBuffer();
        StringBuffer erreoEmp = new StringBuffer();
        // todo 校检是否被引用 被引用无法删除
        for (Long employeeId : employeeIds) {
            List<EmployeeDTO> employeeDTOList = departmentMapper.deleteFlagEmployee(employeeId);
            String username = employeeDTOList.stream().map(EmployeeDTO::getUserName).filter(Objects::nonNull).distinct().collect(Collectors.toList()).toString();
            String employeeDepartmentName = employeeDTOList.stream().map(EmployeeDTO::getEmployeeDepartmentName).filter(Objects::nonNull).distinct().collect(Collectors.toList()).toString();
            String employeePostName = employeeDTOList.stream().map(EmployeeDTO::getEmployeePostName).filter(Objects::nonNull).distinct().collect(Collectors.toList()).toString();
            //远程调用目标分解数据
            if (StringUtils.isNotEmpty(employeeDTOList)) {
                List<Long> collect = employeeDTOList.stream().map(EmployeeDTO::getEmployeeId).collect(Collectors.toList());
                TargetDecomposeDetailsDTO targetDecomposeDetailsDTO = new TargetDecomposeDetailsDTO();
                targetDecomposeDetailsDTO.setEmployeeIds(collect);
                //远程调用查看目标分解详情数据 获取目标分解主表id
                R<List<TargetDecomposeDetailsDTO>> decomposeDetails = remoteDecomposeService.getDecomposeDetails(targetDecomposeDetailsDTO, SecurityConstants.INNER);
                List<TargetDecomposeDetailsDTO> data = decomposeDetails.getData();
                if (StringUtils.isNotEmpty(data)) {
                    for (EmployeeDTO employeeDTO : employeeDTOList) {
                        for (TargetDecomposeDetailsDTO datum : data) {
                            if (employeeDTO.getEmployeeId() == datum.getEmployeeId()) {
                                employeeDTO.setTargetDecomposeId(datum.getTargetDecomposeId());
                            }
                        }
                    }
                    List<Long> collect2 = employeeDTOList.stream().map(EmployeeDTO::getTargetDecomposeId).collect(Collectors.toList());

                    //远程调用查看目标分解主表
                    R<List<TargetDecomposeDTO>> listR = remoteDecomposeService.selectBytargetDecomposeIds(collect2, SecurityConstants.INNER);
                    List<TargetDecomposeDTO> data1 = listR.getData();
                    if (StringUtils.isNotEmpty(data1)) {
                        for (EmployeeDTO employeeDTO : employeeDTOList) {
                            for (TargetDecomposeDTO targetDecomposeDTO : data1) {
                                if (employeeDTO.getTargetDecomposeId() == targetDecomposeDTO.getTargetDecomposeId()) {
                                    employeeDTO.setIndicatorName(targetDecomposeDTO.getIndicatorName());
                                }
                            }
                        }
                    }
                }
            }
            String indicatorName = employeeDTOList.stream().map(EmployeeDTO::getIndicatorName).filter(Objects::nonNull).distinct().collect(Collectors.toList()).toString();

            if (StringUtils.isNotBlank(username) && !StringUtils.equals(username, "[]")) {
                userErreo.append("人员" + employeeDTOList.get(0).getEmployeeName() + "已被用户" + username + "引用  无法删除！\n");
            }
            if (StringUtils.isNotBlank(employeeDepartmentName) && !StringUtils.equals(employeeDepartmentName, "[]")) {
                deptErreo.append("人员" + employeeDTOList.get(0).getEmployeeName() + "已被组织" + employeeDepartmentName + "引用  无法删除！\n");
            }
            if (StringUtils.isNotBlank(employeePostName) && !StringUtils.equals(employeePostName, "[]")) {
                postErreo.append("人员" + employeeDTOList.get(0).getEmployeeName() + "已被岗位" + employeePostName + "引用  无法删除！\n");
            }
            if (StringUtils.isNotBlank(indicatorName) && !StringUtils.equals(indicatorName, "[]")) {
                decomposeErreo.append("人员" + employeeDTOList.get(0).getEmployeeName() + "已被目标分解" + indicatorName + "引用  无法删除！\n");
            }


        }

        erreoEmp.append(deptErreo).append(postErreo).append(userErreo).append(decomposeErreo);
        if (erreoEmp.length() > 0) {
            throw new ServiceException(erreoEmp.toString());
        }
        return employeeMapper.logicDeleteEmployeeByEmployeeIds(employeeIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除员工表信息
     *
     * @param employeeId 员工表主键
     * @return 结果
     */

    @Transactional
    @Override
    public int deleteEmployeeByEmployeeId(Long employeeId) {
        return employeeMapper.deleteEmployeeByEmployeeId(employeeId);
    }

    /**
     * 导入Excel
     *
     * @param list
     */
    @Override
    @Transactional
    public void importEmployee(List<EmployeeExcel> list) throws ParseException {
        //所有员工
        Employee employee = new Employee();
        List<EmployeeDTO> employeeDTOList = employeeMapper.selectEmployeeList(employee);
        //所有员工code
        List<String> employeeCodes = new ArrayList<>();
        if (StringUtils.isNotEmpty(employeeDTOList)) {
            employeeCodes = employeeDTOList.stream().map(EmployeeDTO::getEmployeeCode).collect(Collectors.toList());
        }

        //所有民族
        Nation nation = new Nation();
        List<NationDTO> nationDTOS = nationMapper.selectNationList(nation);
        //所有国籍
        List<CountryDTO> countryDTOS = countryService.selectCountryList();

        //查询所有省市
        List<RegionDTO> regionProvinceNameAndCityNames = regionMapper.selectRegionByProvinceNameAndCityName();
        //查询所有省市区
        List<RegionDTO> regionProvinceNameAndCityNameAndDistrictNames = regionMapper.selectRegionByProvinceNameAndCityNameAndDistrictName();
        //所有岗位
        Post post = new Post();
        List<PostDTO> postDTOS = postMapper.selectPostList(post);
        //查询部门名称附加父级名称
        DepartmentDTO departmentDTO = new DepartmentDTO();
        List<DepartmentDTO> departmentDTOList = departmentService.selectDepartmentListName(departmentDTO);
        //部门岗位关联表
        DepartmentPost departmentPost = new DepartmentPost();
        List<DepartmentPostDTO> departmentPostDTOList = departmentPostMapper.selectDepartmentPostList(departmentPost);
        Map<Long, List<DepartmentPostDTO>> departmentPostMap = new HashMap<>();
        if (StringUtils.isNotEmpty(departmentPostDTOList)) {
            //根据部门id分组
            departmentPostMap = departmentPostDTOList.parallelStream().collect(Collectors.groupingBy(DepartmentPostDTO::getDepartmentId));
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        //新增list
        List<Employee> successExcelList = new ArrayList<>();
        //失败List
        List<EmployeeExcel> errorExcelList = new ArrayList<>();
        if (StringUtils.isNotEmpty(list)) {
            List<EmployeeInfo> employeeInfoList = new ArrayList<>();
            //工号集合检验唯一性
            List<String> employeeExcelCodes = list.stream().map(EmployeeExcel::getEmployeeCode).collect(Collectors.toList());
            //身份证集合检验唯一性
            List<String> excelIdentityCards = list.stream().map(EmployeeExcel::getIdentityCard).collect(Collectors.toList());
            //返回报错信息
            StringBuffer employeeErreo = new StringBuffer();
            for (EmployeeExcel employeeExcel : list) {
                //新增所有员工
                Employee employee2 = new Employee();
                //员工详细详细
                EmployeeInfo employeeInfo = new EmployeeInfo();
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer = this.validEmployee(employeeCodes,employeeExcel, employeeExcelCodes, excelIdentityCards, postDTOS, departmentPostMap, departmentDTOList);
                if (stringBuffer.length() > 1) {
                    errorExcelList.add(employeeExcel);
                    employeeErreo.append(stringBuffer);
                    continue;
                }

                //用工关系状态
                String employmentStatus = employeeExcel.getEmploymentStatus();
                //性别
                String employeeGender = employeeExcel.getEmployeeGender();
                //婚姻状况:0未婚;1已婚
                String maritalStatus = employeeExcel.getMaritalStatus();
                //国籍
                String nationalityName = employeeExcel.getNationalityName();
                //民族
                String nationName = employeeExcel.getNationName();
                //户口所在地名称
                String residentCityName = employeeExcel.getResidentCityName();
                //参保地名称
                String insuredCityName = employeeExcel.getInsuredCityName();
                //常住地名称
                String permanentAddressName = employeeExcel.getPermanentAddressName();
                //通信地址
                String contactAddress = employeeExcel.getContactAddress();
                //入职日期
                String employmentDate = employeeExcel.getEmploymentDate();
                //部门
                String departmentName = employeeExcel.getDepartmentName();
                //岗位
                String postName = employeeExcel.getPostName();
                //个人职级
                String employeeRankName = employeeExcel.getEmployeeRankName();

                //用工关系状态
                if (StringUtils.isNotBlank(employmentStatus)) {
                    if (StringUtils.equals(employmentStatus, "在职")) {
                        employee2.setEmploymentStatus(1);
                    } else if (StringUtils.equals(employmentStatus, "离职")) {
                        employee2.setEmploymentStatus(0);
                    }
                }
                //性别
                if (StringUtils.isNotBlank(employeeGender)) {
                    if (StringUtils.equals(employeeGender, "男")) {
                        employee2.setEmployeeGender(1);
                    } else if (StringUtils.equals(employeeGender, "女")) {
                        employee2.setEmployeeGender(2);
                    }
                }

                //婚姻状况
                if (StringUtils.isNotBlank(maritalStatus)) {
                    if (StringUtils.equals(maritalStatus, "已婚")) {
                        employee2.setMaritalStatus(1);
                    } else if (StringUtils.equals(maritalStatus, "未婚")) {
                        employee2.setMaritalStatus(0);
                    }
                }
                //国籍
                if (StringUtils.isNotBlank(nationalityName)) {
                    if (StringUtils.isNotEmpty(countryDTOS)) {
                        List<CountryDTO> countryDTO = countryDTOS.stream().filter(f -> StringUtils.equals(f.getCountryExcelName(), nationalityName)).collect(Collectors.toList());
                        if (StringUtils.isNotEmpty(countryDTO)) {
                            //国籍id
                            employee2.setNationality(countryDTO.get(0).getParentCountryId()+","+countryDTO.get(0).getCountryId().toString());
                        } else {
                            employee2.setNationality("1,54");
                        }
                    } else {
                        throw new ServiceException("国家未配置 请联系管理员!");
                    }
                } else {
                    employee2.setNationality("1,54");
                }
                //民族
                if (StringUtils.isNotBlank(nationName)) {
                    if (StringUtils.isNotEmpty(nationDTOS)) {
                        List<NationDTO> nationDTO = nationDTOS.stream().filter(f -> StringUtils.equals(f.getNationName(), nationName)).collect(Collectors.toList());
                        if (StringUtils.isNotEmpty(nationDTO)) {
                            //民族id
                            employee2.setNation(String.valueOf(nationDTO.get(0).getNationId()));
                        } else {
                            employee2.setNation("1");
                        }
                    } else {
                        throw new ServiceException("民族未配置 请联系管理员!");
                    }
                } else {
                    employee2.setNation("1");
                }
                //户口所在地 省市
                if (StringUtils.isNotBlank(residentCityName)) {
                    if (StringUtils.isNotEmpty(regionProvinceNameAndCityNames)) {
                        List<RegionDTO> provinceAndCityName = regionProvinceNameAndCityNames.stream().filter(f -> StringUtils.equals(f.getProvinceAndCityName(), residentCityName)).collect(Collectors.toList());
                        if (StringUtils.isNotEmpty(provinceAndCityName)) {
                            employee2.setResidentCity(provinceAndCityName.get(0).getProvinceAndCityCode());
                        }
                    } else {
                        throw new ServiceException("省市区未配置 请联系管理员!");
                    }
                }
                //参保地 省市
                if (StringUtils.isNotBlank(insuredCityName)) {
                    if (StringUtils.isNotEmpty(regionProvinceNameAndCityNames)) {
                        List<RegionDTO> provinceAndCityName = regionProvinceNameAndCityNames.stream().filter(f -> StringUtils.equals(f.getProvinceAndCityName(), insuredCityName)).collect(Collectors.toList());
                        if (StringUtils.isNotEmpty(provinceAndCityName)) {
                            employee2.setInsuredCity(provinceAndCityName.get(0).getProvinceAndCityCode());
                        }
                    } else {
                        throw new ServiceException("省市区未配置 请联系管理员!");
                    }
                }

                //常住地 省市区
                if (StringUtils.isNotBlank(permanentAddressName)) {
                    if (StringUtils.isNotEmpty(regionProvinceNameAndCityNameAndDistrictNames)) {
                        List<RegionDTO> provinceAndCityAndDistrictName = regionProvinceNameAndCityNameAndDistrictNames.stream().filter(f -> StringUtils.equals(f.getProvinceAndCityAndDistrictName(), permanentAddressName)).collect(Collectors.toList());
                        if (StringUtils.isNotEmpty(provinceAndCityAndDistrictName)) {
                            employee2.setPermanentAddress(provinceAndCityAndDistrictName.get(0).getProvinceAndCityAndDistrictCode());
                        }
                    } else {
                        throw new ServiceException("省市区未配置 请联系管理员!");
                    }
                }

                //通信地址 省市区
                if (StringUtils.isNotBlank(contactAddress)) {
                    if (StringUtils.isNotEmpty(regionProvinceNameAndCityNameAndDistrictNames)) {
                        List<RegionDTO> provinceAndCityAndDistrictName = regionProvinceNameAndCityNameAndDistrictNames.stream().filter(f -> StringUtils.equals(f.getProvinceAndCityAndDistrictName(), contactAddress)).collect(Collectors.toList());
                        if (StringUtils.isNotEmpty(provinceAndCityAndDistrictName)) {
                            employee2.setContactAddress(provinceAndCityAndDistrictName.get(0).getProvinceAndCityAndDistrictCode());
                        }
                    } else {
                        throw new ServiceException("省市区未配置 请联系管理员!");
                    }
                }
                //入职日期
                if (StringUtils.isNotBlank(employmentDate)) {
                    employee2.setEmploymentDate(simpleDateFormat.parse(employmentDate));
                }
                //部门
                if (StringUtils.isNotBlank(departmentName)) {
                    if (StringUtils.isNotEmpty(departmentDTOList)) {
                        List<DepartmentDTO> parentDepartmentExcelName = departmentDTOList.stream().filter(f -> StringUtils.equals(f.getParentDepartmentExcelName(), departmentName)).collect(Collectors.toList());
                        if (StringUtils.isNotEmpty(parentDepartmentExcelName)) {
                            employee2.setEmployeeDepartmentId(parentDepartmentExcelName.get(0).getDepartmentId());
                        } else {
                            throw new ServiceException(departmentName + "部门不存在 请联系管理员!");
                        }
                    }
                }
                //岗位
                if (StringUtils.isNotBlank(postName)) {
                    if (StringUtils.isNotEmpty(postDTOS)) {
                        List<PostDTO> postNames = postDTOS.stream().filter(f -> StringUtils.equals(f.getPostName(), postName)).collect(Collectors.toList());
                        if (StringUtils.isNotEmpty(postNames)) {
                            employee2.setEmployeePostId(postNames.get(0).getPostId());
                            //个人职级
                            if (StringUtils.isNotBlank(employeeRankName)) {
                                String rankPrefixCode = postNames.get(0).getRankPrefixCode();
                                if (StringUtils.isNotBlank(rankPrefixCode)) {
                                    employee2.setEmployeeRank(Integer.parseInt(employeeRankName.replace(rankPrefixCode, "")));
                                } else {
                                    employee2.setEmployeeRank(Integer.parseInt(employeeRankName));
                                }
                            }
                        } else {
                            throw new ServiceException(postName + "岗位不存在 请联系管理员!");
                        }
                    }
                }
                if (StringUtils.isNotBlank(employeeExcel.getEmployeeBasicWage())) {
                    //基本工资
                    employee2.setEmployeeBasicWage(new BigDecimal(employeeExcel.getEmployeeBasicWage()));
                }
                //工号
                employee2.setEmployeeCode(employeeExcel.getEmployeeCode());
                //姓名
                employee2.setEmployeeName(employeeExcel.getEmployeeName());
                //身份证号码
                employee2.setIdentityCard(employeeExcel.getIdentityCard());
                Pattern pt = Pattern.compile("(^[1-9]\\d{5}(19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}[0-9Xx]$)");
                Matcher matcher = pt.matcher(employeeExcel.getIdentityCard());
                if (matcher.find()){
                    String substring = employeeExcel.getIdentityCard().substring(6, 15);
                    String year = substring.substring(0, 4);
                    String month = substring.substring(4, 6);
                    String day = substring.substring(6,8);
                    String employeeBirthday = year+"/"+month+"/"+day;
                    Date parse = simpleDateFormat.parse(employeeBirthday);
                    //出生日期
                    employee2.setEmployeeBirthday(parse);
                }
                //手机
                employee2.setEmployeeMobile(employeeExcel.getEmployeeMobile());
                //邮箱
                employee2.setEmployeeEmail(employeeExcel.getEmployeeEmail());
                //微信
                employee2.setWechatCode(employeeExcel.getWechatCode());

                //详细通信地址
                employee2.setContactAddressDetail(employeeExcel.getContactAddressDetail());
                //紧急联系人姓名
                employee2.setEmergencyContact(employeeExcel.getEmergencyContact());
                //紧急联系人电话
                employee2.setEmergencyMobile(employeeExcel.getEmergencyMobile());
                employee2.setCreateBy(SecurityUtils.getUserId());
                employee2.setStatus(1);
                employee2.setCreateTime(DateUtils.getNowDate());
                employee2.setUpdateTime(DateUtils.getNowDate());
                employee2.setUpdateBy(SecurityUtils.getUserId());
                employee2.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                successExcelList.add(employee2);
                BeanUtils.copyProperties(employee2, employeeInfo);
                employeeInfo.setCreateBy(SecurityUtils.getUserId());
                employeeInfo.setCreateTime(DateUtils.getNowDate());
                employeeInfo.setUpdateTime(DateUtils.getNowDate());
                employeeInfo.setUpdateBy(SecurityUtils.getUserId());
                employeeInfo.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                employeeInfoList.add(employeeInfo);
            }
            //后续优化导入
            if (employeeErreo.length() > 1) {
                throw new ServiceException("人员导入失败，请检查Excel：\n" +
                        "1、*为必填字段；\n" +
                        "2、若某一数据有误导致导入失败，所有数据将会导入失败；\n" +
                        "3、员工工号、身份证号码有唯一性校验，若出现重复，可能会导致导入失败；\n" +
                        "4、岗位需在该部门下所属岗位，否则将会导入失败；个人职级需在岗位职级上下限范围内，否则将会导入失败;\n" +
                        "5、入职日期需按照YYYY/MM/DD格式进行录入，否则将会导入失败。\n" +
                        "6、员工工号录入时可使用英文字母以及数字，请勿使用中文，若使用中文会导致系统无法识别。\n" +
                        "7、编辑导入模板时，若涉及到需要填充数字的字段，请注意单元格格式，避免以“0”作为开头的数字被省略掉“0”。\n" +
                        "8、若模板中出现两条重复数据，此数据将会导入失败。\n" +
                        "9、户口所在地、参保地需要填写省/市，若仅填写省，则系统无法识别，会按照空导入；常住地、通信地址需要填写省/市/区（县），若仅填写省/市，则系统无法识别，会按照空导入。");
            }

            if (StringUtils.isNotEmpty(successExcelList)) {
                employeeMapper.batchEmployee(successExcelList);
            }
            if (StringUtils.isNotEmpty(employeeInfoList)) {
                for (int i = 0; i < employeeInfoList.size(); i++) {
                    employeeInfoList.get(i).setEmployeeId(successExcelList.get(i).getEmployeeId());
                }
                employeeInfoMapper.batchEmployeeInfo(employeeInfoList);
            }
        } else {
            throw new ServiceException("请填写excel数据！！！");
        }
    }


    /**
     * 检验数据
     *
     * @param employeeCodes
     * @param employeeExcel
     * @param employeeExcelCodes
     * @param excelIdentityCards
     * @param postDTOS
     * @param departmentPostMap
     * @param departmentDTOList
     */
    private StringBuffer validEmployee(List<String> employeeCodes, EmployeeExcel employeeExcel, List<String> employeeExcelCodes, List<String> excelIdentityCards, List<PostDTO> postDTOS, Map<Long, List<DepartmentPostDTO>> departmentPostMap, List<DepartmentDTO> departmentDTOList) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        StringBuffer validEmployeeErreo = new StringBuffer();
        if (StringUtils.isNotNull(employeeExcel)) {
            String employmentDate = employeeExcel.getEmploymentDate();
            //部门
            String departmentName = employeeExcel.getDepartmentName();
            //岗位
            String postName = employeeExcel.getPostName();
            //个人职级名称
            String employeeRankName = employeeExcel.getEmployeeRankName();
            if (StringUtils.isBlank(employeeExcel.getEmployeeCode())) {
                validEmployeeErreo.append("员工工号为必填项");
            }
            if (StringUtils.isBlank(employeeExcel.getEmployeeName())) {
                validEmployeeErreo.append("员工姓名为必填项");
            }
            if (StringUtils.isBlank(employeeExcel.getEmploymentStatus())) {
                validEmployeeErreo.append("用工关系状态为必填项");
            }
            if (StringUtils.isBlank(employeeExcel.getIdentityCard())) {
                validEmployeeErreo.append("证件号码为必填项");
            }
            if (StringUtils.isBlank(employmentDate)) {
                validEmployeeErreo.append("入职日期为必填项");
            } else {
                try {
                    Date parse = simpleDateFormat.parse(employmentDate);
                } catch (ParseException e) {
                    validEmployeeErreo.append("入职日期公式必须为yyyy/MM/dd");
                }
            }
            if (StringUtils.isBlank(departmentName)) {
                validEmployeeErreo.append("部门为必填项");
            }
            if (StringUtils.isBlank(postName)) {
                validEmployeeErreo.append("岗位为必填项");
            }
            if (StringUtils.isBlank(employeeExcel.getEmployeeRankName())) {
                validEmployeeErreo.append("个人职级为必填项");
            }
            if (StringUtils.isBlank(employeeExcel.getEmployeeMobile())) {
                validEmployeeErreo.append("员工手机号为必填项");
            }
            if (StringUtils.isBlank(employeeExcel.getEmployeeMobile())) {
                validEmployeeErreo.append("紧急联系人电话为必填项");
            }
            //工号
            if (StringUtils.isNotBlank(employeeExcel.getEmployeeCode())) {
                if (StringUtils.isNotEmpty(employeeExcel.getEmployeeCode())) {
                    if (employeeCodes.contains(employeeExcel.getEmployeeCode())) {
                        validEmployeeErreo.append("已存在该员工!");
                    }
                }
            }
            
            //员工工号
            List<String> employeeCodeList = employeeExcelCodes.stream().filter(f -> f.equals(employeeExcel.getEmployeeCode())).collect(Collectors.toList());
            if (employeeCodeList.size() > 1) {
                validEmployeeErreo.append("excle列表中员工工号重复");
            }
            //身份证号码
            List<String> identityCards = excelIdentityCards.stream().filter(f -> f.equals(employeeExcel.getIdentityCard())).collect(Collectors.toList());
            if (identityCards.size() > 1) {
                validEmployeeErreo.append("excle列表中身份证号码重复");
            }

            Pattern pt = Pattern.compile("(^[1-9]\\d{5}(19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}[0-9Xx]$)");
            Matcher matcher = pt.matcher(employeeExcel.getIdentityCard());
            if (!matcher.find()){
                validEmployeeErreo.append("身份证号格式不对");
            }
            //岗位是否属于这个部门
            if (StringUtils.isNotBlank(postName) && StringUtils.isNotBlank(departmentName)) {
                if (StringUtils.isNotEmpty(departmentDTOList)) {
                    List<DepartmentDTO> parentDepartmentExcelName = departmentDTOList.stream().filter(f -> StringUtils.equals(f.getParentDepartmentExcelName(), departmentName)).collect(Collectors.toList());
                    if (StringUtils.isNotEmpty(parentDepartmentExcelName)) {
                        Long departmentId = parentDepartmentExcelName.get(0).getDepartmentId();
                        List<DepartmentPostDTO> departmentPostDTOList = departmentPostMap.get(departmentId);
                        if (StringUtils.isNotEmpty(departmentPostDTOList)) {
                            if (StringUtils.isNotEmpty(postDTOS)) {
                                List<PostDTO> postNames = postDTOS.stream().filter(f -> StringUtils.equals(f.getPostName(), postName)).collect(Collectors.toList());
                                Long postId = postNames.get(0).getPostId();
                                List<DepartmentPostDTO> excelPostId = departmentPostDTOList.stream().filter(f -> f.getPostId() == postId).collect(Collectors.toList());
                                if (StringUtils.isEmpty(excelPostId)) {
                                    validEmployeeErreo.append(postName + "不属于" + departmentName + "部门");
                                }

                                //个人职级名称
                                if (StringUtils.isNotEmpty(employeeRankName)) {
                                    List<PostDTO> postIds = postDTOS.stream().filter(f -> f.getPostId() == postId).collect(Collectors.toList());
                                    if (StringUtils.isNotEmpty(postIds)) {
                                        String rankPrefixCode = postIds.get(0).getRankPrefixCode();
                                        if (StringUtils.isNotBlank(rankPrefixCode)) {
                                            if (employeeRankName.contains(rankPrefixCode)) {
                                                //下限
                                                Integer postRankLower = postIds.get(0).getPostRankLower();
                                                //上限
                                                Integer postRankUpper = postIds.get(0).getPostRankUpper();
                                                String replace = employeeRankName.replace(postIds.get(0).getRankPrefixCode(), "");
                                                int i = Integer.parseInt(replace);
                                                if (i > postRankUpper || i < postRankLower) {
                                                    validEmployeeErreo.append("个人职级需在岗位职级上下限范围内");
                                                }
                                            } else {
                                                validEmployeeErreo.append("职级与岗位不匹配");
                                            }
                                        } else {
                                            //下限
                                            Integer postRankLower = postIds.get(0).getPostRankLower();
                                            //上限
                                            Integer postRankUpper = postIds.get(0).getPostRankUpper();
                                            int i = Integer.parseInt(employeeRankName);
                                            if (i > postRankUpper || i < postRankLower) {
                                                validEmployeeErreo.append("个人职级需在岗位职级上下限范围内");
                                            }
                                        }

                                    }


                                }
                            }
                        }
                    }
                } else {
                    throw new ServiceException("部门不存在 请联系管理员!");
                }


            }

        }
        return validEmployeeErreo;
    }

    /**
     * 导出Excel
     *
     * @param employeeDTO
     * @return
     */
    @Override
    public List<EmployeeExcel> exportEmployee(EmployeeDTO employeeDTO) {
        //所有民族
        Nation nation = new Nation();
        List<NationDTO> nationDTOS = nationMapper.selectNationList(nation);
        //所有国籍
        List<CountryDTO> countryDTOS = countryService.selectCountryList();

        //查询所有省市
        List<RegionDTO> regionProvinceNameAndCityNames = regionMapper.selectRegionByProvinceNameAndCityName();
        //查询所有省市区
        List<RegionDTO> regionProvinceNameAndCityNameAndDistrictNames = regionMapper.selectRegionByProvinceNameAndCityNameAndDistrictName();
        //所有岗位
        Post post = new Post();
        List<PostDTO> postDTOS = postMapper.selectPostList(post);
        //查询部门名称附加父级名称
        DepartmentDTO departmentDTO = new DepartmentDTO();
        List<DepartmentDTO> departmentDTOList = departmentService.selectDepartmentListName(departmentDTO);
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        List<EmployeeDTO> employeeDTOList = employeeMapper.selectEmployeeList(employee);
        List<EmployeeExcel> employeeExcelList = new ArrayList<>();
        if (StringUtils.isNotEmpty(employeeDTOList)) {
            for (EmployeeDTO dto : employeeDTOList) {
                EmployeeExcel employeeExcel = new EmployeeExcel();
                BeanUtils.copyProperties(dto, employeeExcel);
                //性别
                if (null != dto.getEmployeeGender()) {
                    if (dto.getEmployeeGender() == 1) {
                        employeeExcel.setEmployeeGender("男");
                    } else {
                        employeeExcel.setEmployeeGender("女");
                    }
                }
                //用工关系状态
                if (null != dto.getEmploymentStatus()) {
                    if (dto.getEmploymentStatus() == 1) {
                        employeeExcel.setEmploymentStatus("在职");
                    } else {
                        employeeExcel.setEmploymentStatus("离职");
                    }
                }

                //婚姻状况
                if (null != dto.getMaritalStatus()) {
                    if (dto.getMaritalStatus() == 0) {
                        employeeExcel.setMaritalStatus("未婚");
                    } else {
                        employeeExcel.setMaritalStatus("已婚");
                    }
                }
                //国籍
                if (null != dto.getNationality()){
                    if (StringUtils.isNotEmpty(countryDTOS)){
                        List<CountryDTO> nationalitys = countryDTOS.stream().filter(f -> StringUtils.equals(f.getParentCountryId()+","+f.getCountryId(), dto.getNationality())).collect(Collectors.toList());
                        if (StringUtils.isNotEmpty(nationalitys)){
                            employeeExcel.setNationalityName(nationalitys.get(0).getCountryExcelName());
                        }
                    }else {
                        throw new ServiceException("国籍未配置 请联系管理员！");
                    }

                }
                //民族
                if (null != dto.getNation()){
                    if (StringUtils.isNotEmpty(nationDTOS)){
                        List<NationDTO> nationNames = nationDTOS.stream().filter(f -> StringUtils.equals(String.valueOf(f.getNationId()), dto.getNation())).collect(Collectors.toList());
                        if (StringUtils.isNotEmpty(nationNames)){
                            employeeExcel.setNationName(nationNames.get(0).getNationName());
                        }
                    }else {
                        throw new ServiceException("民族未配置 请联系管理员！");
                    }

                }
                //户口所在地
                if (null != dto.getResidentCity()){
                    if (StringUtils.isNotEmpty(regionProvinceNameAndCityNames)){
                        List<RegionDTO> collect = regionProvinceNameAndCityNames.stream().filter(f -> StringUtils.equals(f.getProvinceAndCityCode(),dto.getResidentCity())).collect(Collectors.toList());
                        if (StringUtils.isNotEmpty(collect)){
                            employeeExcel.setResidentCityName(collect.get(0).getProvinceAndCityName());
                        }
                    }

                }
                //参保地
                if (null != dto.getInsuredCity()){
                    if (StringUtils.isNotEmpty(regionProvinceNameAndCityNames)){
                        List<RegionDTO> collect = regionProvinceNameAndCityNames.stream().filter(f ->StringUtils.equals(f.getProvinceAndCityCode(),dto.getInsuredCity())).collect(Collectors.toList());
                        if (StringUtils.isNotEmpty(collect)){
                            employeeExcel.setInsuredCityName(collect.get(0).getProvinceAndCityName());
                        }
                    }
                }
                //常住地
                if (null != dto.getPermanentAddress()){
                    if (StringUtils.isNotEmpty(regionProvinceNameAndCityNameAndDistrictNames)){
                        List<RegionDTO> collect = regionProvinceNameAndCityNameAndDistrictNames.stream().filter(f -> StringUtils.equals(f.getProvinceAndCityAndDistrictCode(),dto.getPermanentAddress())).collect(Collectors.toList());
                        if (StringUtils.isNotEmpty(collect)){
                            employeeExcel.setPermanentAddressName(collect.get(0).getProvinceAndCityAndDistrictName());
                        }
                    }

                }
                //入职日期
                if (null != dto.getEmploymentDate()) {
                    Date employmentDate = dto.getEmploymentDate();
                    employeeExcel.setEmploymentDate(DateUtils.format(employmentDate));
                }
                //部门*
                if (null != dto.getEmployeeDepartmentId()){

                    if (StringUtils.isNotEmpty(departmentDTOList)){
                        List<DepartmentDTO> employeePostIds = departmentDTOList.stream().filter(f -> f.getDepartmentId() == dto.getEmployeePostId()).collect(Collectors.toList());
                        if (StringUtils.isNotEmpty(employeePostIds)){
                            employeeExcel.setDepartmentName(employeePostIds.get(0).getParentDepartmentExcelName());
                        }
                    }
                }
                //岗位*
                if (null != dto.getEmployeePostId()){
                if (StringUtils.isNotEmpty(postDTOS)){
                    List<PostDTO> employeePostIds = postDTOS.stream().filter(f -> f.getPostId() == dto.getEmployeePostId()).collect(Collectors.toList());
                    if (StringUtils.isNotEmpty(employeePostIds)){
                        employeeExcel.setPostName(employeePostIds.get(0).getPostName());
                    }
                }
                }
                //通信地址
                if (null != dto.getContactAddress()){
                    if (StringUtils.isNotEmpty(regionProvinceNameAndCityNameAndDistrictNames)){
                        List<RegionDTO> collect = regionProvinceNameAndCityNameAndDistrictNames.stream().filter(f -> StringUtils.equals(f.getProvinceAndCityAndDistrictCode(),dto.getContactAddress())).collect(Collectors.toList());
                        if (StringUtils.isNotEmpty(collect)){
                            employeeExcel.setContactAddress(collect.get(0).getProvinceAndCityAndDistrictName());
                        }
                    }
                }

                employeeExcelList.add(employeeExcel);
            }
        }
        return employeeExcelList;
    }


    /**
     * 查询未分配用户员工列表
     */
    @Override
    public List<EmployeeDTO> unallocatedUserList(Long userId) {
        return employeeMapper.unallocatedUserList(userId);
    }

    /**
     * 分页查询岗位薪酬报表
     *
     * @param employeeDTO
     * @return
     */
    @Override
    public List<EmployeeDTO> pagePostSalaryReportList(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        return employeeMapper.selectPostSalaryReportList(employee);
    }

    /**
     * 新增人力预算上年期末数集合
     *
     * @param employeeDTO
     * @return
     */
    @Override
    public List<OfficialRankSystemDTO> selecTamountLastYearList(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        OfficialRankSystemDTO officialRankSystemDTO = officialRankSystemMapper.selectOfficialRankSystemByOfficialRankSystemId(employeeDTO.getOfficialRankSystemId());
        if (StringUtils.isNull(officialRankSystemDTO)) {
            throw new ServiceException("职级不存在！请联系管理员");
        }
        List<EmployeeDTO> employeeDTOList = employeeMapper.selecTamountLastYearList(employee);
        List<OfficialRankSystemDTO> officialRankSystemDTOs = new ArrayList<>();
        //职级起始级别
        Integer rankStart = officialRankSystemDTO.getRankStart();
        //职级终止级别
        Integer rankEnd = officialRankSystemDTO.getRankEnd();

        for (Integer i = rankStart; i <= rankEnd; i++) {
            OfficialRankSystemDTO officialRankSystemDTO1 = new OfficialRankSystemDTO();
            //职级名称
            officialRankSystemDTO1.setRankCodeName(officialRankSystemDTO.getRankPrefixCode() + i);
            //职级级别
            officialRankSystemDTO1.setRankCode(i);
            officialRankSystemDTO1.setAmountLastYear(0);
            officialRankSystemDTOs.add(officialRankSystemDTO1);
        }

        //比对职级级别 相同的赋值 不同的赋0
        if (StringUtils.isNotEmpty(officialRankSystemDTOs) && StringUtils.isNotEmpty(employeeDTOList) && employeeDTOList.get(0) != null) {
            for (OfficialRankSystemDTO rankSystemDTO : officialRankSystemDTOs) {
                for (EmployeeDTO dto : employeeDTOList) {
                    if (StringUtils.equals(rankSystemDTO.getRankCodeName(), dto.getEmployeeRankName())) {

                        rankSystemDTO.setAmountLastYear(dto.getAmountLastYear());
                    }
                }
            }
        }

        return officialRankSystemDTOs;
    }

    /**
     * 根据部门 职级 获取人员信息集合
     *
     * @param list
     * @return
     */
    @Override
    public List<EmployeeDTO> selectByBudgeList(List<List<Long>> list) {
        return employeeMapper.selectByBudgeList(list);
    }

    /**
     * 根据Code集合
     *
     * @param assessmentList
     * @return
     */
    @Override
    public List<EmployeeDTO> selectByCodes(List<String> assessmentList) {
        return employeeMapper.selectByCodes(assessmentList);
    }

    /**
     * 相同部门下 相同职级的 在职人数
     *
     * @param departmentIds
     * @return
     */
    @Override
    public List<EmployeeDTO> selectDepartmentAndOfficialRankSystem(List<Long> departmentIds) {
        return employeeMapper.selectDepartmentAndOfficialRankSystem(departmentIds);
    }

    /**
     * 远程 查询部门下所有人员
     *
     * @param departmentId
     * @return
     */
    @Override
    public List<EmployeeDTO> selectEmployeeByDepts(Long departmentId) {
        return employeeMapper.selectEmployeeByDepts(departmentId);
    }

    /**
     * 根据部门id查询员工表列表
     *
     * @param employeeDepartmentId
     * @return
     */
    @Override
    public List<EmployeeDTO> queryEmployeeByDept(Long employeeDepartmentId) {
        return employeeMapper.queryEmployeeByDept(employeeDepartmentId);
    }

    /**
     * 查询一级部门下所有的人员 返回部门id和职级体系id
     *
     * @param departmentIdAll
     * @return
     */
    @Override
    public List<EmployeeDTO> selectParentDepartmentIdAndOfficialRankSystem(List<Long> departmentIdAll) {
        return employeeMapper.selectParentDepartmentIdAndOfficialRankSystem(departmentIdAll);
    }

    /**
     * 远程查询在职所有人员
     *
     * @return
     */
    @Override
    public List<EmployeeDTO> getAll() {
        return employeeMapper.getAll();
    }

    /**
     * 根据部门ID 集合查询人员
     *
     * @param departmentIds 人员ID集合
     * @return
     */
    @Override
    public List<EmployeeDTO> selectEmployeeByDepartmentIds(List<Long> departmentIds) {
        return employeeMapper.selectEmployeeByDepartmentIds(departmentIds);
    }


    /**
     * 逻辑删除员工表信息
     *
     * @param employeeDTO 员工表
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteEmployeeByEmployeeId(EmployeeDTO employeeDTO) {
        int i = 0;
        //组织引用
        StringBuffer deptErreo = new StringBuffer();
        //岗位引用
        StringBuffer postErreo = new StringBuffer();
        //用户引用
        StringBuffer userErreo = new StringBuffer();
        //目标分解引用
        StringBuffer decomposeErreo = new StringBuffer();
        StringBuffer erreoEmp = new StringBuffer();
        //员工表
        Employee employee = new Employee();
        employee.setEmployeeId(employeeDTO.getEmployeeId());
        employee.setUpdateTime(DateUtils.getNowDate());
        employee.setUpdateBy(SecurityUtils.getUserId());
        //查询数据
        EmployeeDTO employeeDTO1 = employeeMapper.selectEmployeeByEmployeeId(employeeDTO.getEmployeeId());
        if (null == employeeDTO1) {
            throw new ServiceException("数据不存在无法删除！");
        }
        //部门表
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setDepartmentLeaderId(employeeDTO1.getEmployeeId());
        departmentDTO.setExaminationLeaderId(employeeDTO1.getEmployeeId());
        // todo 校检是否被引用 被引用无法删除
        List<EmployeeDTO> employeeDTOList = departmentMapper.deleteFlagEmployee(employeeDTO.getEmployeeId());
        String username = employeeDTOList.stream().map(EmployeeDTO::getUserName).filter(Objects::nonNull).distinct().collect(Collectors.toList()).toString();
        String employeeDepartmentName = employeeDTOList.stream().map(EmployeeDTO::getEmployeeDepartmentName).filter(Objects::nonNull).distinct().collect(Collectors.toList()).toString();
        String employeePostName = employeeDTOList.stream().map(EmployeeDTO::getEmployeePostName).filter(Objects::nonNull).distinct().collect(Collectors.toList()).toString();
        String indicatorName = employeeDTOList.stream().map(EmployeeDTO::getIndicatorName).filter(Objects::nonNull).distinct().collect(Collectors.toList()).toString();

        if (StringUtils.isNotBlank(username) && !StringUtils.equals(username, "[]")) {
            userErreo.append("人员" + employeeDTOList.get(0).getEmployeeName() + "已被用户" + username + "引用  无法删除！\n");
        }
        if (StringUtils.isNotBlank(employeeDepartmentName) && !StringUtils.equals(employeeDepartmentName, "[]")) {
            deptErreo.append("人员" + employeeDTOList.get(0).getEmployeeName() + "已被组织" + employeeDepartmentName + "引用  无法删除！\n");
        }
        if (StringUtils.isNotBlank(employeePostName) && !StringUtils.equals(employeePostName, "[]")) {
            postErreo.append("人员" + employeeDTOList.get(0).getEmployeeName() + "已被岗位" + employeePostName + "引用  无法删除！\n");
        }
        if (StringUtils.isNotBlank(indicatorName) && !StringUtils.equals(indicatorName, "[]")) {
            decomposeErreo.append("人员" + employeeDTOList.get(0).getEmployeeName() + "已被目标分解" + indicatorName + "引用  无法删除！\n");
        }


        erreoEmp.append(deptErreo).append(postErreo).append(userErreo).append(decomposeErreo);
        if (erreoEmp.length() > 0) {
            throw new ServiceException(erreoEmp.toString());
        }
        try {
            i = employeeMapper.logicDeleteEmployeeByEmployeeId(employee, SecurityUtils.getUserId(), DateUtils.getNowDate());
        } catch (Exception e) {
            throw new ServiceException("删除员工信息失败");
        }
        return i;
    }

    /**
     * 物理删除员工表信息
     *
     * @param employeeDTO 员工表
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteEmployeeByEmployeeId(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        return employeeMapper.deleteEmployeeByEmployeeId(employee.getEmployeeId());
    }

    /**
     * 物理批量删除员工表
     *
     * @param employeeDtos 需要删除的员工表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteEmployeeByEmployeeIds(List<EmployeeDTO> employeeDtos) {
        List<Long> stringList = new ArrayList<>();
        for (EmployeeDTO employeeDTO : employeeDtos) {
            stringList.add(employeeDTO.getEmployeeId());
        }
        return employeeMapper.deleteEmployeeByEmployeeIds(stringList);
    }

    /**
     * 批量新增员工表信息
     *
     * @param employeeDtos 员工表对象
     */
    @Transactional
    public int insertEmployees(List<EmployeeDTO> employeeDtos) {
        List<Employee> employeeList = new ArrayList<>();

        for (EmployeeDTO employeeDTO : employeeDtos) {
            Employee employee = new Employee();
            BeanUtils.copyProperties(employeeDTO, employee);
            employee.setCreateBy(SecurityUtils.getUserId());
            employee.setCreateTime(DateUtils.getNowDate());
            employee.setUpdateTime(DateUtils.getNowDate());
            employee.setUpdateBy(SecurityUtils.getUserId());
            employee.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            employeeList.add(employee);
        }
        return employeeMapper.batchEmployee(employeeList);
    }

    /**
     * 批量修改员工表信息
     *
     * @param employeeDtos 员工表对象
     */
    @Transactional
    public int updateEmployees(List<EmployeeDTO> employeeDtos) {
        List<Employee> employeeList = new ArrayList();

        for (EmployeeDTO employeeDTO : employeeDtos) {
            Employee employee = new Employee();
            BeanUtils.copyProperties(employeeDTO, employee);
            employee.setCreateBy(SecurityUtils.getUserId());
            employee.setCreateTime(DateUtils.getNowDate());
            employee.setUpdateTime(DateUtils.getNowDate());
            employee.setUpdateBy(SecurityUtils.getUserId());
            employeeList.add(employee);
        }
        return employeeMapper.updateEmployees(employeeList);
    }

    /**
     * 新增人力预算上年期末数集合预制数据
     *
     * @param employeeDTO 员工DTO
     * @return EmployeeDTO
     */
    @Override
    public EmployeeDTO empSalaryAdjustPlan(EmployeeDTO employeeDTO) {
        Long employeeId = employeeDTO.getEmployeeId();
        if (StringUtils.isNull(employeeId)) {
            throw new ServiceException("员工ID不可以为空");
        }
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        List<EmployeeDTO> employeeDTOS = employeeMapper.selectPostSalaryReportList(employee);
        if (StringUtils.isEmpty(employeeDTOS)) {
            throw new ServiceException("员工信息已不存在 检查员工配置");
        }
        EmployeeDTO dto = employeeDTOS.get(0);
        // 最近三次绩效结果
        List<Map<String, String>> performanceResultList = new ArrayList<>();
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOS = getPerformanceAppraisalObject(employeeId);
        if (StringUtils.isNotEmpty(performanceAppraisalObjectsDTOS)) {
            for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOS) {
                setObjectFieldName(performanceAppraisalObjectsDTO);
                HashMap<String, String> map = new HashMap<>();
                map.put("appraisalResult", performanceAppraisalObjectsDTO.getAppraisalResult());
                map.put("cycleNumberName", performanceAppraisalObjectsDTO.getCycleNumberName());
                map.put("filingDate", DateUtils.localToString(performanceAppraisalObjectsDTO.getFilingDate()));
                performanceResultList.add(map);
                dto.setPerformanceResultList(performanceResultList);
            }
        }
        // 个人调薪计划
        List<EmpSalaryAdjustPlanDTO> salaryPlanList = getSalaryPlanList(employeeId);
        if (StringUtils.isNotEmpty(salaryPlanList)) {
            List<EmployeeSalarySnapVO> employeeSalarySnapVOS = new ArrayList<>();
            for (EmpSalaryAdjustPlanDTO empSalaryAdjustPlanDTO : salaryPlanList) {
                setPlanValue(empSalaryAdjustPlanDTO);
                EmployeeSalarySnapVO employeeSalarySnapVO = new EmployeeSalarySnapVO();
                BeanUtils.copyProperties(empSalaryAdjustPlanDTO, employeeSalarySnapVO);
            }
            dto.setEmployeeSalarySnapVOS(employeeSalarySnapVOS);
        }
        return dto;
    }

    /**
     * 给岗位类型赋值
     *
     * @param empSalaryAdjustPlanDTO 个人调薪计划DTO
     */
    private void setPlanValue(EmpSalaryAdjustPlanDTO empSalaryAdjustPlanDTO) {
        if (StringUtils.isNotNull(empSalaryAdjustPlanDTO) && StringUtils.isNotNull(empSalaryAdjustPlanDTO.getAdjustmentType())) {
            String adjustmentType = empSalaryAdjustPlanDTO.getAdjustmentType();
            List<String> adjustmentTypeList = Arrays.asList(adjustmentType.split(","));
            if (StringUtils.isNotEmpty(adjustmentTypeList)) {
                List<String> adjustmentList = new ArrayList<>();
                for (String adjustment : adjustmentTypeList) {
                    switch (adjustment) {//调整类型(1调岗;2调级;3调薪),多个用英文逗号隔开
                        case "1":
                            adjustmentList.add("调岗");
                            break;
                        case "2":
                            adjustmentList.add("调级");
                            break;
                        case "3":
                            adjustmentList.add("调薪");
                            break;
                    }
                }
                empSalaryAdjustPlanDTO.setAdjustmentTypeNameList(adjustmentList);
            }
        }
    }

    private List<EmpSalaryAdjustPlanDTO> getSalaryPlanList(Long employeeId) {
        R<List<EmpSalaryAdjustPlanDTO>> listR = salaryAdjustPlanService.selectByEmployeeId(employeeId, SecurityConstants.INNER);
        if (listR.getCode() != 200) {
            throw new ServiceException("远程调用个人调薪计划表失败 请联系管理员");
        }
        return listR.getData();
    }

    /**
     * 为绩效字段字段命名
     *
     * @param appraisalObjectsDTO 考核对象任务
     */
    private void setObjectFieldName(PerformanceAppraisalObjectsDTO appraisalObjectsDTO) {
        // 考核周期类型/考核周期
        if (StringUtils.isNotNull(appraisalObjectsDTO) && StringUtils.isNotNull(appraisalObjectsDTO.getCycleType())) {
            switch (appraisalObjectsDTO.getCycleType()) {
                case 1:
                    appraisalObjectsDTO.setCycleTypeName("月度");
                    appraisalObjectsDTO.setCycleNumberName(appraisalObjectsDTO.getCycleNumber().toString() + "月");
                    break;
                case 2:
                    appraisalObjectsDTO.setCycleTypeName("季度");
                    appraisalObjectsDTO.setCycleNumberName(appraisalObjectsDTO.getCycleNumber().toString() + "季度");
                    break;
                case 3:
                    appraisalObjectsDTO.setCycleTypeName("半年度");
                    if (appraisalObjectsDTO.getCycleNumber() == 1) {
                        appraisalObjectsDTO.setCycleNumberName("上半年");
                    } else {
                        appraisalObjectsDTO.setCycleNumberName("下半年");
                    }
                    break;
                case 4:
                    appraisalObjectsDTO.setCycleTypeName("年度");
                    appraisalObjectsDTO.setCycleNumberName("整年度");
                    break;
            }
        }
    }

    /**
     * 获取绩效对象信息
     *
     * @param employeeId 员工ID
     * @return R
     */
    private List<PerformanceAppraisalObjectsDTO> getPerformanceAppraisalObject(Long employeeId) {
        R<List<PerformanceAppraisalObjectsDTO>> listR = performanceAppraisalService.performanceResult(employeeId, SecurityConstants.INNER);
        if (listR.getCode() != 200) {
            throw new ServiceException("远程获取绩效信息失败 请联系管理员");
        }
        return listR.getData();
    }
}

