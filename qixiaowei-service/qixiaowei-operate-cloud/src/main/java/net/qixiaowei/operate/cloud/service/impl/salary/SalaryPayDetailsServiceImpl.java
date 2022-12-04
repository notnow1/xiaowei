package net.qixiaowei.operate.cloud.service.impl.salary;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.salary.SalaryPayDetails;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryPayDetailsDTO;
import net.qixiaowei.operate.cloud.excel.salary.SalaryPayDetailsExcel;
import net.qixiaowei.operate.cloud.mapper.salary.SalaryPayDetailsMapper;
import net.qixiaowei.operate.cloud.service.salary.ISalaryPayDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * SalaryPayDetailsService业务层处理
 *
 * @author Graves
 * @since 2022-11-17
 */
@Service
public class SalaryPayDetailsServiceImpl implements ISalaryPayDetailsService {
    @Autowired
    private SalaryPayDetailsMapper salaryPayDetailsMapper;

    /**
     * 查询工资发薪明细表
     *
     * @param salaryPayDetailsId 工资发薪明细表主键
     * @return 工资发薪明细表
     */
    @Override
    public SalaryPayDetailsDTO selectSalaryPayDetailsBySalaryPayDetailsId(Long salaryPayDetailsId) {
        return salaryPayDetailsMapper.selectSalaryPayDetailsBySalaryPayDetailsId(salaryPayDetailsId);
    }

    /**
     * 查询工资发薪明细表列表
     *
     * @param salaryPayDetailsDTO 工资发薪明细表
     * @return 工资发薪明细表
     */
    @Override
    public List<SalaryPayDetailsDTO> selectSalaryPayDetailsList(SalaryPayDetailsDTO salaryPayDetailsDTO) {
        SalaryPayDetails salaryPayDetails = new SalaryPayDetails();
        BeanUtils.copyProperties(salaryPayDetailsDTO, salaryPayDetails);
        return salaryPayDetailsMapper.selectSalaryPayDetailsList(salaryPayDetails);
    }

    /**
     * 新增工资发薪明细表
     *
     * @param salaryPayDetailsDTO 工资发薪明细表
     * @return 结果
     */
    @Override
    public SalaryPayDetailsDTO insertSalaryPayDetails(SalaryPayDetailsDTO salaryPayDetailsDTO) {
        SalaryPayDetails salaryPayDetails = new SalaryPayDetails();
        BeanUtils.copyProperties(salaryPayDetailsDTO, salaryPayDetails);
        salaryPayDetails.setCreateBy(SecurityUtils.getUserId());
        salaryPayDetails.setCreateTime(DateUtils.getNowDate());
        salaryPayDetails.setUpdateTime(DateUtils.getNowDate());
        salaryPayDetails.setUpdateBy(SecurityUtils.getUserId());
        salaryPayDetails.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        salaryPayDetailsMapper.insertSalaryPayDetails(salaryPayDetails);
        salaryPayDetailsDTO.setSalaryPayDetailsId(salaryPayDetails.getSalaryPayDetailsId());
        return salaryPayDetailsDTO;
    }

    /**
     * 修改工资发薪明细表
     *
     * @param salaryPayDetailsDTO 工资发薪明细表
     * @return 结果
     */
    @Override
    public int updateSalaryPayDetails(SalaryPayDetailsDTO salaryPayDetailsDTO) {
        SalaryPayDetails salaryPayDetails = new SalaryPayDetails();
        BeanUtils.copyProperties(salaryPayDetailsDTO, salaryPayDetails);
        salaryPayDetails.setUpdateTime(DateUtils.getNowDate());
        salaryPayDetails.setUpdateBy(SecurityUtils.getUserId());
        return salaryPayDetailsMapper.updateSalaryPayDetails(salaryPayDetails);
    }

    /**
     * 逻辑批量删除工资发薪明细表
     *
     * @param salaryPayDetailsIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteSalaryPayDetailsBySalaryPayDetailsIds(List<Long> salaryPayDetailsIds) {
        return salaryPayDetailsMapper.logicDeleteSalaryPayDetailsBySalaryPayDetailsIds(salaryPayDetailsIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除工资发薪明细表信息
     *
     * @param salaryPayDetailsId 工资发薪明细表主键
     * @return 结果
     */
    @Override
    public int deleteSalaryPayDetailsBySalaryPayDetailsId(Long salaryPayDetailsId) {
        return salaryPayDetailsMapper.deleteSalaryPayDetailsBySalaryPayDetailsId(salaryPayDetailsId);
    }

    /**
     * 逻辑删除工资发薪明细表信息
     *
     * @param salaryPayDetailsDTO 工资发薪明细表
     * @return 结果
     */
    @Override
    public int logicDeleteSalaryPayDetailsBySalaryPayDetailsId(SalaryPayDetailsDTO salaryPayDetailsDTO) {
        SalaryPayDetails salaryPayDetails = new SalaryPayDetails();
        salaryPayDetails.setSalaryPayDetailsId(salaryPayDetailsDTO.getSalaryPayDetailsId());
        salaryPayDetails.setUpdateTime(DateUtils.getNowDate());
        salaryPayDetails.setUpdateBy(SecurityUtils.getUserId());
        return salaryPayDetailsMapper.logicDeleteSalaryPayDetailsBySalaryPayDetailsId(salaryPayDetails);
    }

    /**
     * 物理删除工资发薪明细表信息
     *
     * @param salaryPayDetailsDTO 工资发薪明细表
     * @return 结果
     */

    @Override
    public int deleteSalaryPayDetailsBySalaryPayDetailsId(SalaryPayDetailsDTO salaryPayDetailsDTO) {
        SalaryPayDetails salaryPayDetails = new SalaryPayDetails();
        BeanUtils.copyProperties(salaryPayDetailsDTO, salaryPayDetails);
        return salaryPayDetailsMapper.deleteSalaryPayDetailsBySalaryPayDetailsId(salaryPayDetails.getSalaryPayDetailsId());
    }

    /**
     * 物理批量删除工资发薪明细表
     *
     * @param salaryPayDetailsDtos 需要删除的工资发薪明细表主键
     * @return 结果
     */

    @Override
    public int deleteSalaryPayDetailsBySalaryPayDetailsIds(List<SalaryPayDetailsDTO> salaryPayDetailsDtos) {
        List<Long> stringList = new ArrayList<>();
        for (SalaryPayDetailsDTO salaryPayDetailsDTO : salaryPayDetailsDtos) {
            stringList.add(salaryPayDetailsDTO.getSalaryPayDetailsId());
        }
        return salaryPayDetailsMapper.deleteSalaryPayDetailsBySalaryPayDetailsIds(stringList);
    }

    /**
     * 批量新增工资发薪明细表信息
     *
     * @param salaryPayDetailsDtos 工资发薪明细表对象
     */

    public int insertSalaryPayDetailss(List<SalaryPayDetailsDTO> salaryPayDetailsDtos) {
        List<SalaryPayDetails> salaryPayDetailsList = new ArrayList<>();

        for (SalaryPayDetailsDTO salaryPayDetailsDTO : salaryPayDetailsDtos) {
            SalaryPayDetails salaryPayDetails = new SalaryPayDetails();
            BeanUtils.copyProperties(salaryPayDetailsDTO, salaryPayDetails);
            salaryPayDetails.setCreateBy(SecurityUtils.getUserId());
            salaryPayDetails.setCreateTime(DateUtils.getNowDate());
            salaryPayDetails.setUpdateTime(DateUtils.getNowDate());
            salaryPayDetails.setUpdateBy(SecurityUtils.getUserId());
            salaryPayDetails.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            salaryPayDetailsList.add(salaryPayDetails);
        }
        return salaryPayDetailsMapper.batchSalaryPayDetails(salaryPayDetailsList);
    }

    /**
     * 批量修改工资发薪明细表信息
     *
     * @param salaryPayDetailsDtos 工资发薪明细表对象
     */

    public int updateSalaryPayDetailss(List<SalaryPayDetailsDTO> salaryPayDetailsDtos) {
        List<SalaryPayDetails> salaryPayDetailsList = new ArrayList<>();

        for (SalaryPayDetailsDTO salaryPayDetailsDTO : salaryPayDetailsDtos) {
            SalaryPayDetails salaryPayDetails = new SalaryPayDetails();
            BeanUtils.copyProperties(salaryPayDetailsDTO, salaryPayDetails);
            salaryPayDetails.setCreateBy(SecurityUtils.getUserId());
            salaryPayDetails.setCreateTime(DateUtils.getNowDate());
            salaryPayDetails.setUpdateTime(DateUtils.getNowDate());
            salaryPayDetails.setUpdateBy(SecurityUtils.getUserId());
            salaryPayDetailsList.add(salaryPayDetails);
        }
        return salaryPayDetailsMapper.updateSalaryPayDetailss(salaryPayDetailsList);
    }

    /**
     * 导入Excel
     *
     * @param list
     */
    @Override
    public void importSalaryPayDetails(List<SalaryPayDetailsExcel> list) {
        List<SalaryPayDetails> salaryPayDetailsList = new ArrayList<>();
        list.forEach(l -> {
            SalaryPayDetails salaryPayDetails = new SalaryPayDetails();
            BeanUtils.copyProperties(l, salaryPayDetails);
            salaryPayDetails.setCreateBy(SecurityUtils.getUserId());
            salaryPayDetails.setCreateTime(DateUtils.getNowDate());
            salaryPayDetails.setUpdateTime(DateUtils.getNowDate());
            salaryPayDetails.setUpdateBy(SecurityUtils.getUserId());
            salaryPayDetails.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            salaryPayDetailsList.add(salaryPayDetails);
        });
        try {
            salaryPayDetailsMapper.batchSalaryPayDetails(salaryPayDetailsList);
        } catch (Exception e) {
            throw new ServiceException("导入工资发薪明细表失败");
        }
    }

    /**
     * 导出Excel
     *
     * @param salaryPayDetailsDTO
     * @return
     */
    @Override
    public List<SalaryPayDetailsExcel> exportSalaryPayDetails(SalaryPayDetailsDTO salaryPayDetailsDTO) {
        SalaryPayDetails salaryPayDetails = new SalaryPayDetails();
        BeanUtils.copyProperties(salaryPayDetailsDTO, salaryPayDetails);
        List<SalaryPayDetailsDTO> salaryPayDetailsDTOList = salaryPayDetailsMapper.selectSalaryPayDetailsList(salaryPayDetails);
        List<SalaryPayDetailsExcel> salaryPayDetailsExcelList = new ArrayList<>();
        return salaryPayDetailsExcelList;
    }

    /**
     * 通过工资ID返回详情
     *
     * @param salaryPayId
     * @return
     */
    @Override
    public List<SalaryPayDetailsDTO> selectSalaryPayDetailsBySalaryPayId(Long salaryPayId) {
        return salaryPayDetailsMapper.selectSalaryPayDetailsBySalaryPayId(salaryPayId);
    }

    /**
     * 通过工资ID列表删除列表
     *
     * @param salaryPayId
     * @return
     */
    @Override
    public int logicDeleteSalaryPayDetailsBySalaryPayId(Long salaryPayId) {
        return salaryPayDetailsMapper.logicDeleteSalaryPayDetailsBySalaryPayId(salaryPayId, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 通过工资ID列表删除列表
     *
     * @param salaryPayIds
     * @return
     */
    @Override
    public int logicDeleteSalaryPayDetailsBySalaryPayIds(List<Long> salaryPayIds) {
        return salaryPayDetailsMapper.logicDeleteSalaryPayDetailsBySalaryPayIds(salaryPayIds, DateUtils.getNowDate(), SecurityUtils.getUserId());
    }

    /**
     * 通过发薪集合查找详情表
     *
     * @param salaryPayIds 发薪Ids
     * @return
     */
    @Override
    public List<SalaryPayDetailsDTO> selectSalaryPayDetailsBySalaryPayIds(List<Long> salaryPayIds) {
        return salaryPayDetailsMapper.selectSalaryPayDetailsBySalaryPayIds(salaryPayIds);
    }
}

