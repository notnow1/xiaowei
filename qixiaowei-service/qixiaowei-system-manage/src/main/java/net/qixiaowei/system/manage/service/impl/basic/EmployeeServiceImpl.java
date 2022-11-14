package net.qixiaowei.system.manage.service.impl.basic;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.CheckObjectIsNullUtils;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.domain.basic.Employee;
import net.qixiaowei.system.manage.api.domain.basic.EmployeeInfo;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.excel.basic.EmployeeExcel;
import net.qixiaowei.system.manage.mapper.basic.DepartmentMapper;
import net.qixiaowei.system.manage.mapper.basic.EmployeeInfoMapper;
import net.qixiaowei.system.manage.mapper.basic.EmployeeMapper;
import net.qixiaowei.system.manage.mapper.basic.PostMapper;
import net.qixiaowei.system.manage.mapper.user.UserMapper;
import net.qixiaowei.system.manage.service.basic.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
    private PostMapper postMapper;
    @Autowired
    private UserMapper userMapper;

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
        //员工信息表
        EmployeeInfo employeeInfo = new EmployeeInfo();
        BeanUtils.copyProperties(employeeDTO, employeeInfo);
        employeeInfo.setUpdateTime(DateUtils.getNowDate());
        employeeInfo.setUpdateBy(SecurityUtils.getUserId());
        employeeInfo.setEmployeeId(employee.getEmployeeId());
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
            String username = employeeDTOList.stream().map(EmployeeDTO::getUserName).distinct().collect(Collectors.toList()).toString();
            String employeeDepartmentName = employeeDTOList.stream().map(EmployeeDTO::getEmployeeDepartmentName).distinct().collect(Collectors.toList()).toString();
            String employeePostName = employeeDTOList.stream().map(EmployeeDTO::getEmployeePostName).distinct().collect(Collectors.toList()).toString();
            String indicatorName = employeeDTOList.stream().map(EmployeeDTO::getIndicatorName).distinct().collect(Collectors.toList()).toString();
            for (EmployeeDTO employeeDTO : employeeDTOList) {
                if (StringUtils.isNotBlank(username)) {
                    userErreo.append("人员" + employeeDTO.getEmployeeName() + "已被用户" + username + "引用  无法删除！\n");
                }
                if (StringUtils.isNotBlank(employeeDepartmentName)) {
                    deptErreo.append("人员" + employeeDTO.getEmployeeName() + "已被组织" + employeeDepartmentName + "引用  无法删除！\n");
                }
                if (StringUtils.isNotBlank(employeePostName)) {
                    postErreo.append("人员" + employeeDTO.getEmployeeName() + "已被岗位" + employeePostName + "引用  无法删除！\n");
                }
                if (StringUtils.isNotBlank(indicatorName)) {
                    decomposeErreo.append("人员" + employeeDTO.getEmployeeName() + "已被目标分解" + indicatorName + "引用  无法删除！\n");
                }
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
    public void importEmployee(List<EmployeeExcel> list) {
        if (StringUtils.isNotEmpty(list)){
            //code集合
            List<String> collect1 = this.selectEmployeeCodes(list);
            //身份证集合
            List<String> collect2 =this.selectEmployeeIdCard(list);
            //插入数据库数据
            List<Employee> employeeList = new ArrayList<>();
            //返回报错信息
            StringBuffer  employeeErreo = new StringBuffer();
            for (EmployeeExcel employeeExcel : list) {
                StringBuffer stringBuffer = this.validEmployee(employeeExcel);
                if (stringBuffer.length()>1){
                    employeeErreo.append(stringBuffer);
                }
                Employee employee = new Employee();
                BeanUtils.copyProperties(employeeExcel, employee);
                if (collect1.contains(employee.getEmployeeCode())){
                    employeeErreo.append("工号"+employee.getEmployeeCode()+"已存在"+"\r\n");
                }
                if (collect2.contains(employee.getIdentityCard())){
                    employeeErreo.append("身份证号"+employee.getIdentityCard()+"已存在"+"\r\n");
                }
                employee.setCreateBy(SecurityUtils.getUserId());
                employee.setCreateTime(DateUtils.getNowDate());
                employee.setUpdateTime(DateUtils.getNowDate());
                employee.setUpdateBy(SecurityUtils.getUserId());
                employee.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                employeeList.add(employee);
            }
            if (employeeErreo.length()>1){
                throw new ServiceException(employeeErreo.toString());
            }
            try {
                employeeMapper.batchEmployee(employeeList);
            } catch (Exception e) {
                throw new ServiceException("导入人员失败");
            }
        }else {
            throw new ServiceException("请填写excel数据！！！");
        }
    }

    /**
     * 检验身份证唯一
     * @param list
     * @return
     */
    private List<String> selectEmployeeIdCard(List<EmployeeExcel> list) {
        //数据库数据保证唯一性
        List<EmployeeDTO> employeeDTOList = new ArrayList<>();
        //code集合
        List<String> collect1 = new ArrayList<>();
        List<String> collect = list.stream().map(EmployeeExcel::getIdentityCard).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(collect)){
            employeeDTOList = employeeMapper.selectEmployeeByIdCards(collect);
        }
        if (StringUtils.isNotEmpty(employeeDTOList)){
            collect1 = employeeDTOList.stream().map(EmployeeDTO::getIdentityCard).collect(Collectors.toList());
        }
        return collect1;
    }

    /**
     * 检验code唯一
     * @param list
     * @return
     */
    private List<String> selectEmployeeCodes(List<EmployeeExcel> list) {
        //数据库数据保证唯一性
        List<EmployeeDTO> employeeDTOList = new ArrayList<>();
        //code集合
        List<String> collect1 = new ArrayList<>();
        List<String> collect = list.stream().map(EmployeeExcel::getEmployeeCode).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(collect)){
            employeeDTOList = employeeMapper.selectEmployeeByEmployeeCodes(collect);
        }
        if (StringUtils.isNotEmpty(employeeDTOList)){
            collect1 = employeeDTOList.stream().map(EmployeeDTO::getEmployeeCode).collect(Collectors.toList());
        }
        return collect1;
    }

    /**
     * 检验数据
     * @param employeeExcel
     */
    private StringBuffer validEmployee(EmployeeExcel employeeExcel) {
        StringBuffer validEmployeeErreo = new StringBuffer();
        if (StringUtils.isNotNull(employeeExcel)){
            if (StringUtils.isNull(employeeExcel.getEmployeeCode())){
                validEmployeeErreo.append("工号为必填项");
            }
            if (StringUtils.isNull(employeeExcel.getEmployeeName())){
                validEmployeeErreo.append("姓名为必填项");
            }
            if (StringUtils.isNull(employeeExcel.getEmploymentStatus())){
                validEmployeeErreo.append("用工关系状态为必填项");
            }
            if (StringUtils.isNull(employeeExcel.getIdentityCard())){
                validEmployeeErreo.append("证件号码为必填项");
            }
            if (StringUtils.isNull(employeeExcel.getEmploymentDate())){
                validEmployeeErreo.append("入职日期为必填项");
            }
            if (StringUtils.isNull(employeeExcel.getEmployeeMobile())){
                validEmployeeErreo.append("员工手机号为必填项");
            }
        }
        return validEmployeeErreo;
    }

    /**
     * 导出Excel
     * @param employeeDTO
     * @return
     */
    @Override
    public List<EmployeeExcel> exportEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        List<EmployeeDTO> employeeDTOList = employeeMapper.selectEmployeeList(employee);
        List<EmployeeExcel> employeeExcelList = new ArrayList<>();
        if (StringUtils.isNotEmpty(employeeDTOList)){
            for (EmployeeDTO dto : employeeDTOList) {
                EmployeeExcel employeeExcel = new EmployeeExcel();
                BeanUtils.copyProperties(dto, employeeExcel);
                //性别
                if (StringUtils.isNotNull(dto.getEmployeeGender())){
                    if (dto.getEmployeeGender() == 1) {
                        employeeExcel.setEmployeeGender("男");
                    } else {
                        employeeExcel.setEmployeeGender("女");
                    }
                }
                //用工关系状态
                if (null != dto.getEmploymentStatus()){
                    if (dto.getEmploymentStatus() == 1) {
                        employeeExcel.setEmploymentStatus("在职");
                    } else {
                        employeeExcel.setEmploymentStatus("离职");
                    }
                }
                //出生日期
                if (null != dto.getEmployeeBirthday()){
                    Date employeeBirthday = dto.getEmployeeBirthday();
                    employeeExcel.setEmployeeBirthday(DateUtils.format(employeeBirthday));
                }
                //婚姻状况
                if (null != dto.getMaritalStatus()){
                    if (dto.getMaritalStatus() == 0) {
                        employeeExcel.setMaritalStatus("未婚");
                    } else {
                        employeeExcel.setMaritalStatus("已婚");
                    }
                }
                //入职日期
                if (null != dto.getEmploymentDate()){
                    Date employmentDate = dto.getEmploymentDate();
                    employeeExcel.setEmploymentDate(DateUtils.format(employmentDate));
                }
                //离职日期
                if (null != dto.getDepartureDate()){
                    Date departureDate = dto.getDepartureDate();
                    employeeExcel.setDepartureDate(DateUtils.format(departureDate));
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
    public List<EmployeeDTO> unallocatedUserList() {
        return employeeMapper.unallocatedUserList();
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
        String username = employeeDTOList.stream().map(EmployeeDTO::getUserName).distinct().collect(Collectors.toList()).toString();
        String employeeDepartmentName = employeeDTOList.stream().map(EmployeeDTO::getEmployeeDepartmentName).distinct().collect(Collectors.toList()).toString();
        String employeePostName = employeeDTOList.stream().map(EmployeeDTO::getEmployeePostName).distinct().collect(Collectors.toList()).toString();
        String indicatorName = employeeDTOList.stream().map(EmployeeDTO::getIndicatorName).distinct().collect(Collectors.toList()).toString();
        for (EmployeeDTO employeeDTO2 : employeeDTOList) {
            if (StringUtils.isNotBlank(username)) {
                userErreo.append("人员" + employeeDTO2.getEmployeeName() + "已被用户" + username + "引用  无法删除！\n");
            }
            if (StringUtils.isNotBlank(employeeDepartmentName)) {
                deptErreo.append("人员" + employeeDTO2.getEmployeeName() + "已被组织" + employeeDepartmentName + "引用  无法删除！\n");
            }
            if (StringUtils.isNotBlank(employeePostName)) {
                postErreo.append("人员" + employeeDTO2.getEmployeeName() + "已被岗位" + employeePostName + "引用  无法删除！\n");
            }
            if (StringUtils.isNotBlank(indicatorName)) {
                decomposeErreo.append("人员" + employeeDTO.getEmployeeName() + "已被目标分解" + indicatorName + "引用  无法删除！\n");
            }
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
        List<Long> stringList = new ArrayList();
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
        List<Employee> employeeList = new ArrayList();

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
}

