package net.qixiaowei.operate.cloud.service.impl.salary;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.salary.EmpSalaryAdjustPerform;
import net.qixiaowei.operate.cloud.api.dto.salary.EmpSalaryAdjustPerformDTO;
import net.qixiaowei.operate.cloud.mapper.salary.EmpSalaryAdjustPerformMapper;
import net.qixiaowei.operate.cloud.service.salary.IEmpSalaryAdjustPerformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * EmpSalaryAdjustPerformService业务层处理
 *
 * @author Graves
 * @since 2022-12-14
 */
@Service
public class EmpSalaryAdjustPerformServiceImpl implements IEmpSalaryAdjustPerformService {
    @Autowired
    private EmpSalaryAdjustPerformMapper empSalaryAdjustPerformMapper;

    /**
     * 查询个人调薪绩效记录表
     *
     * @param empSalaryAdjustPerformId 个人调薪绩效记录表主键
     * @return 个人调薪绩效记录表
     */
    @Override
    public EmpSalaryAdjustPerformDTO selectEmpSalaryAdjustPerformByEmpSalaryAdjustPerformId(Long empSalaryAdjustPerformId) {
        return empSalaryAdjustPerformMapper.selectEmpSalaryAdjustPerformByEmpSalaryAdjustPerformId(empSalaryAdjustPerformId);
    }

    /**
     * 查询个人调薪绩效记录表列表
     *
     * @param empSalaryAdjustPerformDTO 个人调薪绩效记录表
     * @return 个人调薪绩效记录表
     */
    @Override
    public List<EmpSalaryAdjustPerformDTO> selectEmpSalaryAdjustPerformList(EmpSalaryAdjustPerformDTO empSalaryAdjustPerformDTO) {
        EmpSalaryAdjustPerform empSalaryAdjustPerform = new EmpSalaryAdjustPerform();
        BeanUtils.copyProperties(empSalaryAdjustPerformDTO, empSalaryAdjustPerform);
        return empSalaryAdjustPerformMapper.selectEmpSalaryAdjustPerformList(empSalaryAdjustPerform);
    }

    /**
     * 新增个人调薪绩效记录表
     *
     * @param empSalaryAdjustPerformDTO 个人调薪绩效记录表
     * @return 结果
     */
    @Override
    public EmpSalaryAdjustPerformDTO insertEmpSalaryAdjustPerform(EmpSalaryAdjustPerformDTO empSalaryAdjustPerformDTO) {
        EmpSalaryAdjustPerform empSalaryAdjustPerform = new EmpSalaryAdjustPerform();
        BeanUtils.copyProperties(empSalaryAdjustPerformDTO, empSalaryAdjustPerform);
        empSalaryAdjustPerform.setCreateBy(SecurityUtils.getUserId());
        empSalaryAdjustPerform.setCreateTime(DateUtils.getNowDate());
        empSalaryAdjustPerform.setUpdateTime(DateUtils.getNowDate());
        empSalaryAdjustPerform.setUpdateBy(SecurityUtils.getUserId());
        empSalaryAdjustPerform.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        empSalaryAdjustPerformMapper.insertEmpSalaryAdjustPerform(empSalaryAdjustPerform);
        empSalaryAdjustPerformDTO.setEmpSalaryAdjustPerformId(empSalaryAdjustPerform.getEmpSalaryAdjustPerformId());
        return empSalaryAdjustPerformDTO;
    }

    /**
     * 修改个人调薪绩效记录表
     *
     * @param empSalaryAdjustPerformDTO 个人调薪绩效记录表
     * @return 结果
     */
    @Override
    public int updateEmpSalaryAdjustPerform(EmpSalaryAdjustPerformDTO empSalaryAdjustPerformDTO) {
        EmpSalaryAdjustPerform empSalaryAdjustPerform = new EmpSalaryAdjustPerform();
        BeanUtils.copyProperties(empSalaryAdjustPerformDTO, empSalaryAdjustPerform);
        empSalaryAdjustPerform.setUpdateTime(DateUtils.getNowDate());
        empSalaryAdjustPerform.setUpdateBy(SecurityUtils.getUserId());
        return empSalaryAdjustPerformMapper.updateEmpSalaryAdjustPerform(empSalaryAdjustPerform);
    }

    /**
     * 逻辑批量删除个人调薪绩效记录表
     *
     * @param empSalaryAdjustPerformIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteEmpSalaryAdjustPerformByEmpSalaryAdjustPerformIds(List<Long> empSalaryAdjustPerformIds) {
        return empSalaryAdjustPerformMapper.logicDeleteEmpSalaryAdjustPerformByEmpSalaryAdjustPerformIds(empSalaryAdjustPerformIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除个人调薪绩效记录表信息
     *
     * @param empSalaryAdjustPerformId 个人调薪绩效记录表主键
     * @return 结果
     */
    @Override
    public int deleteEmpSalaryAdjustPerformByEmpSalaryAdjustPerformId(Long empSalaryAdjustPerformId) {
        return empSalaryAdjustPerformMapper.deleteEmpSalaryAdjustPerformByEmpSalaryAdjustPerformId(empSalaryAdjustPerformId);
    }

    /**
     * 根据计划ID获取近三次绩效结果
     *
     * @param empSalaryAdjustPlanId 个人调薪计划ID
     * @return List
     */
    @Override
    public List<EmpSalaryAdjustPerformDTO> selectEmpSalaryAdjustPerformByPlanId(Long empSalaryAdjustPlanId) {
        return empSalaryAdjustPerformMapper.selectEmpSalaryAdjustPerformByPlanId(empSalaryAdjustPlanId);
    }

    /**
     * 根据计划ID集合查询最近三次绩效结果
     *
     * @param empSalaryAdjustPlanIds 计划ID集合
     * @return List
     */
    @Override
    public List<EmpSalaryAdjustPerformDTO> selectEmpSalaryAdjustPerformByPlanIds(List<Long> empSalaryAdjustPlanIds) {
        return empSalaryAdjustPerformMapper.selectEmpSalaryAdjustPerformByPlanIds(empSalaryAdjustPlanIds);
    }

    /**
     * 逻辑删除个人调薪绩效记录表信息
     *
     * @param empSalaryAdjustPerformDTO 个人调薪绩效记录表
     * @return 结果
     */
    @Override
    public int logicDeleteEmpSalaryAdjustPerformByEmpSalaryAdjustPerformId(EmpSalaryAdjustPerformDTO empSalaryAdjustPerformDTO) {
        EmpSalaryAdjustPerform empSalaryAdjustPerform = new EmpSalaryAdjustPerform();
        empSalaryAdjustPerform.setEmpSalaryAdjustPerformId(empSalaryAdjustPerformDTO.getEmpSalaryAdjustPerformId());
        empSalaryAdjustPerform.setUpdateTime(DateUtils.getNowDate());
        empSalaryAdjustPerform.setUpdateBy(SecurityUtils.getUserId());
        return empSalaryAdjustPerformMapper.logicDeleteEmpSalaryAdjustPerformByEmpSalaryAdjustPerformId(empSalaryAdjustPerform);
    }

    /**
     * 物理删除个人调薪绩效记录表信息
     *
     * @param empSalaryAdjustPerformDTO 个人调薪绩效记录表
     * @return 结果
     */

    @Override
    public int deleteEmpSalaryAdjustPerformByEmpSalaryAdjustPerformId(EmpSalaryAdjustPerformDTO empSalaryAdjustPerformDTO) {
        EmpSalaryAdjustPerform empSalaryAdjustPerform = new EmpSalaryAdjustPerform();
        BeanUtils.copyProperties(empSalaryAdjustPerformDTO, empSalaryAdjustPerform);
        return empSalaryAdjustPerformMapper.deleteEmpSalaryAdjustPerformByEmpSalaryAdjustPerformId(empSalaryAdjustPerform.getEmpSalaryAdjustPerformId());
    }

    /**
     * 物理批量删除个人调薪绩效记录表
     *
     * @param empSalaryAdjustPerformDtoS 需要删除的个人调薪绩效记录表主键
     * @return 结果
     */

    @Override
    public int deleteEmpSalaryAdjustPerformByEmpSalaryAdjustPerformIds(List<EmpSalaryAdjustPerformDTO> empSalaryAdjustPerformDtoS) {
        List<Long> stringList = new ArrayList<>();
        for (EmpSalaryAdjustPerformDTO empSalaryAdjustPerformDTO : empSalaryAdjustPerformDtoS) {
            stringList.add(empSalaryAdjustPerformDTO.getEmpSalaryAdjustPerformId());
        }
        return empSalaryAdjustPerformMapper.deleteEmpSalaryAdjustPerformByEmpSalaryAdjustPerformIds(stringList);
    }

    /**
     * 批量新增个人调薪绩效记录表信息
     *
     * @param empSalaryAdjustPerformDtoS 个人调薪绩效记录表对象
     */

    @Override
    public int insertEmpSalaryAdjustPerforms(List<EmpSalaryAdjustPerformDTO> empSalaryAdjustPerformDtoS) {
        List<EmpSalaryAdjustPerform> empSalaryAdjustPerformList = new ArrayList<>();

        for (EmpSalaryAdjustPerformDTO empSalaryAdjustPerformDTO : empSalaryAdjustPerformDtoS) {
            EmpSalaryAdjustPerform empSalaryAdjustPerform = new EmpSalaryAdjustPerform();
            BeanUtils.copyProperties(empSalaryAdjustPerformDTO, empSalaryAdjustPerform);
            empSalaryAdjustPerform.setCreateBy(SecurityUtils.getUserId());
            empSalaryAdjustPerform.setCreateTime(DateUtils.getNowDate());
            empSalaryAdjustPerform.setUpdateTime(DateUtils.getNowDate());
            empSalaryAdjustPerform.setUpdateBy(SecurityUtils.getUserId());
            empSalaryAdjustPerform.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            empSalaryAdjustPerformList.add(empSalaryAdjustPerform);
        }
        return empSalaryAdjustPerformMapper.batchEmpSalaryAdjustPerform(empSalaryAdjustPerformList);
    }

    /**
     * 批量修改个人调薪绩效记录表信息
     *
     * @param empSalaryAdjustPerformDtoS 个人调薪绩效记录表对象
     */

    @Override
    public int updateEmpSalaryAdjustPerforms(List<EmpSalaryAdjustPerformDTO> empSalaryAdjustPerformDtoS) {
        List<EmpSalaryAdjustPerform> empSalaryAdjustPerformList = new ArrayList<>();

        for (EmpSalaryAdjustPerformDTO empSalaryAdjustPerformDTO : empSalaryAdjustPerformDtoS) {
            EmpSalaryAdjustPerform empSalaryAdjustPerform = new EmpSalaryAdjustPerform();
            BeanUtils.copyProperties(empSalaryAdjustPerformDTO, empSalaryAdjustPerform);
            empSalaryAdjustPerform.setCreateBy(SecurityUtils.getUserId());
            empSalaryAdjustPerform.setCreateTime(DateUtils.getNowDate());
            empSalaryAdjustPerform.setUpdateTime(DateUtils.getNowDate());
            empSalaryAdjustPerform.setUpdateBy(SecurityUtils.getUserId());
            empSalaryAdjustPerformList.add(empSalaryAdjustPerform);
        }
        return empSalaryAdjustPerformMapper.updateEmpSalaryAdjustPerforms(empSalaryAdjustPerformList);
    }

}

