package net.qixiaowei.operate.cloud.service.impl.salary;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.salary.EmpSalaryAdjustSnap;
import net.qixiaowei.operate.cloud.api.dto.salary.EmpSalaryAdjustSnapDTO;
import net.qixiaowei.operate.cloud.mapper.salary.EmpSalaryAdjustSnapMapper;
import net.qixiaowei.operate.cloud.service.salary.IEmpSalaryAdjustSnapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * EmpSalaryAdjustSnapService业务层处理
 *
 * @author Graves
 * @since 2022-12-14
 */
@Service
public class EmpSalaryAdjustSnapServiceImpl implements IEmpSalaryAdjustSnapService {
    @Autowired
    private EmpSalaryAdjustSnapMapper empSalaryAdjustSnapMapper;

    /**
     * 查询个人调薪快照表
     *
     * @param empSalaryAdjustSnapId 个人调薪快照表主键
     * @return 个人调薪快照表
     */
    @Override
    public EmpSalaryAdjustSnapDTO selectEmpSalaryAdjustSnapByEmpSalaryAdjustSnapId(Long empSalaryAdjustSnapId) {
        return empSalaryAdjustSnapMapper.selectEmpSalaryAdjustSnapByEmpSalaryAdjustSnapId(empSalaryAdjustSnapId);
    }

    /**
     * 查询个人调薪快照表列表
     *
     * @param empSalaryAdjustSnapDTO 个人调薪快照表
     * @return 个人调薪快照表
     */
    @Override
    public List<EmpSalaryAdjustSnapDTO> selectEmpSalaryAdjustSnapList(EmpSalaryAdjustSnapDTO empSalaryAdjustSnapDTO) {
        EmpSalaryAdjustSnap empSalaryAdjustSnap = new EmpSalaryAdjustSnap();
        BeanUtils.copyProperties(empSalaryAdjustSnapDTO, empSalaryAdjustSnap);
        return empSalaryAdjustSnapMapper.selectEmpSalaryAdjustSnapList(empSalaryAdjustSnap);
    }

    /**
     * 新增个人调薪快照表
     *
     * @param empSalaryAdjustSnapDTO 个人调薪快照表
     * @return 结果
     */
    @Override
    public EmpSalaryAdjustSnapDTO insertEmpSalaryAdjustSnap(EmpSalaryAdjustSnapDTO empSalaryAdjustSnapDTO) {
        EmpSalaryAdjustSnap empSalaryAdjustSnap = new EmpSalaryAdjustSnap();
        BeanUtils.copyProperties(empSalaryAdjustSnapDTO, empSalaryAdjustSnap);
        empSalaryAdjustSnap.setCreateBy(SecurityUtils.getUserId());
        empSalaryAdjustSnap.setCreateTime(DateUtils.getNowDate());
        empSalaryAdjustSnap.setUpdateTime(DateUtils.getNowDate());
        empSalaryAdjustSnap.setUpdateBy(SecurityUtils.getUserId());
        empSalaryAdjustSnap.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        empSalaryAdjustSnapMapper.insertEmpSalaryAdjustSnap(empSalaryAdjustSnap);
        empSalaryAdjustSnapDTO.setEmpSalaryAdjustSnapId(empSalaryAdjustSnap.getEmpSalaryAdjustSnapId());
        return empSalaryAdjustSnapDTO;
    }

    /**
     * 修改个人调薪快照表
     *
     * @param empSalaryAdjustSnapDTO 个人调薪快照表
     * @return 结果
     */
    @Override
    public int updateEmpSalaryAdjustSnap(EmpSalaryAdjustSnapDTO empSalaryAdjustSnapDTO) {
        EmpSalaryAdjustSnap empSalaryAdjustSnap = new EmpSalaryAdjustSnap();
        BeanUtils.copyProperties(empSalaryAdjustSnapDTO, empSalaryAdjustSnap);
        empSalaryAdjustSnap.setUpdateTime(DateUtils.getNowDate());
        empSalaryAdjustSnap.setUpdateBy(SecurityUtils.getUserId());
        return empSalaryAdjustSnapMapper.updateEmpSalaryAdjustSnap(empSalaryAdjustSnap);
    }

    /**
     * 逻辑批量删除个人调薪快照表
     *
     * @param empSalaryAdjustSnapIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteEmpSalaryAdjustSnapByEmpSalaryAdjustSnapIds(List<Long> empSalaryAdjustSnapIds) {
        return empSalaryAdjustSnapMapper.logicDeleteEmpSalaryAdjustSnapByEmpSalaryAdjustSnapIds(empSalaryAdjustSnapIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除个人调薪快照表信息
     *
     * @param empSalaryAdjustSnapId 个人调薪快照表主键
     * @return 结果
     */
    @Override
    public int deleteEmpSalaryAdjustSnapByEmpSalaryAdjustSnapId(Long empSalaryAdjustSnapId) {
        return empSalaryAdjustSnapMapper.deleteEmpSalaryAdjustSnapByEmpSalaryAdjustSnapId(empSalaryAdjustSnapId);
    }

    /**
     * 根据计划ID获取调薪快照表
     *
     * @param empSalaryAdjustPlanId 计划ID
     * @return List
     */
    @Override
    public List<EmpSalaryAdjustSnapDTO> selectEmpSalaryAdjustSnapByEmpSalaryAdjustPlanId(Long empSalaryAdjustPlanId) {
        return empSalaryAdjustSnapMapper.selectEmpSalaryAdjustSnapByEmpSalaryAdjustPlanId(empSalaryAdjustPlanId);
    }

    /**
     * 根据计划ID集合查询个人调薪快照表
     *
     * @param empSalaryAdjustPlanIds 计划ID集合查询
     * @return List
     */
    @Override
    public List<EmpSalaryAdjustSnapDTO> selectEmpSalaryAdjustSnapByEmpSalaryAdjustPlanIds(List<Long> empSalaryAdjustPlanIds) {
        return empSalaryAdjustSnapMapper.selectEmpSalaryAdjustSnapByEmpSalaryAdjustPlanIds(empSalaryAdjustPlanIds);
    }

    /**
     * 逻辑删除个人调薪快照表信息
     *
     * @param empSalaryAdjustSnapDTO 个人调薪快照表
     * @return 结果
     */
    @Override
    public int logicDeleteEmpSalaryAdjustSnapByEmpSalaryAdjustSnapId(EmpSalaryAdjustSnapDTO empSalaryAdjustSnapDTO) {
        EmpSalaryAdjustSnap empSalaryAdjustSnap = new EmpSalaryAdjustSnap();
        empSalaryAdjustSnap.setEmpSalaryAdjustSnapId(empSalaryAdjustSnapDTO.getEmpSalaryAdjustSnapId());
        empSalaryAdjustSnap.setUpdateTime(DateUtils.getNowDate());
        empSalaryAdjustSnap.setUpdateBy(SecurityUtils.getUserId());
        return empSalaryAdjustSnapMapper.logicDeleteEmpSalaryAdjustSnapByEmpSalaryAdjustSnapId(empSalaryAdjustSnap);
    }

    /**
     * 物理删除个人调薪快照表信息
     *
     * @param empSalaryAdjustSnapDTO 个人调薪快照表
     * @return 结果
     */

    @Override
    public int deleteEmpSalaryAdjustSnapByEmpSalaryAdjustSnapId(EmpSalaryAdjustSnapDTO empSalaryAdjustSnapDTO) {
        EmpSalaryAdjustSnap empSalaryAdjustSnap = new EmpSalaryAdjustSnap();
        BeanUtils.copyProperties(empSalaryAdjustSnapDTO, empSalaryAdjustSnap);
        return empSalaryAdjustSnapMapper.deleteEmpSalaryAdjustSnapByEmpSalaryAdjustSnapId(empSalaryAdjustSnap.getEmpSalaryAdjustSnapId());
    }

    /**
     * 物理批量删除个人调薪快照表
     *
     * @param empSalaryAdjustSnapDtos 需要删除的个人调薪快照表主键
     * @return 结果
     */

    @Override
    public int deleteEmpSalaryAdjustSnapByEmpSalaryAdjustSnapIds(List<EmpSalaryAdjustSnapDTO> empSalaryAdjustSnapDtos) {
        List<Long> stringList = new ArrayList<>();
        for (EmpSalaryAdjustSnapDTO empSalaryAdjustSnapDTO : empSalaryAdjustSnapDtos) {
            stringList.add(empSalaryAdjustSnapDTO.getEmpSalaryAdjustSnapId());
        }
        return empSalaryAdjustSnapMapper.deleteEmpSalaryAdjustSnapByEmpSalaryAdjustSnapIds(stringList);
    }

    /**
     * 批量新增个人调薪快照表信息
     *
     * @param empSalaryAdjustSnapDtos 个人调薪快照表对象
     */

    public int insertEmpSalaryAdjustSnaps(List<EmpSalaryAdjustSnapDTO> empSalaryAdjustSnapDtos) {
        List<EmpSalaryAdjustSnap> empSalaryAdjustSnapList = new ArrayList<>();

        for (EmpSalaryAdjustSnapDTO empSalaryAdjustSnapDTO : empSalaryAdjustSnapDtos) {
            EmpSalaryAdjustSnap empSalaryAdjustSnap = new EmpSalaryAdjustSnap();
            BeanUtils.copyProperties(empSalaryAdjustSnapDTO, empSalaryAdjustSnap);
            empSalaryAdjustSnap.setCreateBy(SecurityUtils.getUserId());
            empSalaryAdjustSnap.setCreateTime(DateUtils.getNowDate());
            empSalaryAdjustSnap.setUpdateTime(DateUtils.getNowDate());
            empSalaryAdjustSnap.setUpdateBy(SecurityUtils.getUserId());
            empSalaryAdjustSnap.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            empSalaryAdjustSnapList.add(empSalaryAdjustSnap);
        }
        return empSalaryAdjustSnapMapper.batchEmpSalaryAdjustSnap(empSalaryAdjustSnapList);
    }

    /**
     * 批量修改个人调薪快照表信息
     *
     * @param empSalaryAdjustSnapDtos 个人调薪快照表对象
     */

    public int updateEmpSalaryAdjustSnaps(List<EmpSalaryAdjustSnapDTO> empSalaryAdjustSnapDtos) {
        List<EmpSalaryAdjustSnap> empSalaryAdjustSnapList = new ArrayList<>();

        for (EmpSalaryAdjustSnapDTO empSalaryAdjustSnapDTO : empSalaryAdjustSnapDtos) {
            EmpSalaryAdjustSnap empSalaryAdjustSnap = new EmpSalaryAdjustSnap();
            BeanUtils.copyProperties(empSalaryAdjustSnapDTO, empSalaryAdjustSnap);
            empSalaryAdjustSnap.setCreateBy(SecurityUtils.getUserId());
            empSalaryAdjustSnap.setCreateTime(DateUtils.getNowDate());
            empSalaryAdjustSnap.setUpdateTime(DateUtils.getNowDate());
            empSalaryAdjustSnap.setUpdateBy(SecurityUtils.getUserId());
            empSalaryAdjustSnapList.add(empSalaryAdjustSnap);
        }
        return empSalaryAdjustSnapMapper.updateEmpSalaryAdjustSnaps(empSalaryAdjustSnapList);
    }
}

