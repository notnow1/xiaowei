package net.qixiaowei.system.manage.service.impl.log;

import java.util.List;

import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.system.manage.api.vo.log.OperationLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.domain.log.OperationLog;
import net.qixiaowei.system.manage.api.dto.log.OperationLogDTO;
import net.qixiaowei.system.manage.mapper.log.OperationLogMapper;
import net.qixiaowei.system.manage.service.log.IOperationLogService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


/**
 * OperationLogService业务层处理
 *
 * @author hzk
 * @since 2023-01-04
 */
@Service
public class OperationLogServiceImpl implements IOperationLogService {
    @Autowired
    private OperationLogMapper operationLogMapper;

    /**
     * 查询操作日志表
     *
     * @param operationLogId 操作日志表主键
     * @return 操作日志表
     */
    @Override
    public OperationLogDTO selectOperationLogByOperationLogId(Long operationLogId) {
        return operationLogMapper.selectOperationLogByOperationLogId(operationLogId);
    }

    /**
     * 查询操作日志表列表
     *
     * @param operationLogDTO 操作日志表
     * @return 操作日志表
     */
    @Override
    public List<OperationLogVO> selectOperationLogList(OperationLogDTO operationLogDTO) {
        OperationLog operationLog = new OperationLog();
        BeanUtils.copyProperties(operationLogDTO, operationLog);
        return operationLogMapper.selectOperationLogList(operationLog);
    }

    /**
     * 新增操作日志表
     *
     * @param operationLogDTO 操作日志表
     * @return 结果
     */
    @Override
    public OperationLogDTO insertOperationLog(OperationLogDTO operationLogDTO) {
        OperationLog operationLog = new OperationLog();
        BeanUtils.copyProperties(operationLogDTO, operationLog);
        operationLog.setCreateBy(SecurityUtils.getUserId());
        operationLog.setCreateTime(DateUtils.getNowDate());
        operationLog.setUpdateTime(DateUtils.getNowDate());
        operationLog.setUpdateBy(SecurityUtils.getUserId());
        operationLog.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        operationLogMapper.insertOperationLog(operationLog);
        operationLogDTO.setOperationLogId(operationLog.getOperationLogId());
        return operationLogDTO;
    }

    /**
     * 修改操作日志表
     *
     * @param operationLogDTO 操作日志表
     * @return 结果
     */
    @Override
    public int updateOperationLog(OperationLogDTO operationLogDTO) {
        OperationLog operationLog = new OperationLog();
        BeanUtils.copyProperties(operationLogDTO, operationLog);
        operationLog.setUpdateTime(DateUtils.getNowDate());
        operationLog.setUpdateBy(SecurityUtils.getUserId());
        return operationLogMapper.updateOperationLog(operationLog);
    }

    /**
     * 逻辑批量删除操作日志表
     *
     * @param operationLogIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteOperationLogByOperationLogIds(List<Long> operationLogIds) {
        return operationLogMapper.logicDeleteOperationLogByOperationLogIds(operationLogIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除操作日志表信息
     *
     * @param operationLogId 操作日志表主键
     * @return 结果
     */
    @Override
    public int deleteOperationLogByOperationLogId(Long operationLogId) {
        return operationLogMapper.deleteOperationLogByOperationLogId(operationLogId);
    }

    /**
     * 逻辑删除操作日志表信息
     *
     * @param operationLogDTO 操作日志表
     * @return 结果
     */
    @Override
    public int logicDeleteOperationLogByOperationLogId(OperationLogDTO operationLogDTO) {
        OperationLog operationLog = new OperationLog();
        operationLog.setOperationLogId(operationLogDTO.getOperationLogId());
        operationLog.setUpdateTime(DateUtils.getNowDate());
        operationLog.setUpdateBy(SecurityUtils.getUserId());
        return operationLogMapper.logicDeleteOperationLogByOperationLogId(operationLog);
    }

    /**
     * 物理删除操作日志表信息
     *
     * @param operationLogDTO 操作日志表
     * @return 结果
     */

    @Override
    public int deleteOperationLogByOperationLogId(OperationLogDTO operationLogDTO) {
        OperationLog operationLog = new OperationLog();
        BeanUtils.copyProperties(operationLogDTO, operationLog);
        return operationLogMapper.deleteOperationLogByOperationLogId(operationLog.getOperationLogId());
    }

    /**
     * 物理批量删除操作日志表
     *
     * @param operationLogDtos 需要删除的操作日志表主键
     * @return 结果
     */

    @Override
    public int deleteOperationLogByOperationLogIds(List<OperationLogDTO> operationLogDtos) {
        List<Long> stringList = new ArrayList();
        for (OperationLogDTO operationLogDTO : operationLogDtos) {
            stringList.add(operationLogDTO.getOperationLogId());
        }
        return operationLogMapper.deleteOperationLogByOperationLogIds(stringList);
    }

    /**
     * 批量新增操作日志表信息
     *
     * @param operationLogDtos 操作日志表对象
     */

    public int insertOperationLogs(List<OperationLogDTO> operationLogDtos) {
        List<OperationLog> operationLogList = new ArrayList();

        for (OperationLogDTO operationLogDTO : operationLogDtos) {
            OperationLog operationLog = new OperationLog();
            BeanUtils.copyProperties(operationLogDTO, operationLog);
            operationLog.setCreateBy(SecurityUtils.getUserId());
            operationLog.setCreateTime(DateUtils.getNowDate());
            operationLog.setUpdateTime(DateUtils.getNowDate());
            operationLog.setUpdateBy(SecurityUtils.getUserId());
            operationLog.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            operationLogList.add(operationLog);
        }
        return operationLogMapper.batchOperationLog(operationLogList);
    }

    /**
     * 批量修改操作日志表信息
     *
     * @param operationLogDtos 操作日志表对象
     */

    public int updateOperationLogs(List<OperationLogDTO> operationLogDtos) {
        List<OperationLog> operationLogList = new ArrayList();

        for (OperationLogDTO operationLogDTO : operationLogDtos) {
            OperationLog operationLog = new OperationLog();
            BeanUtils.copyProperties(operationLogDTO, operationLog);
            operationLog.setCreateBy(SecurityUtils.getUserId());
            operationLog.setCreateTime(DateUtils.getNowDate());
            operationLog.setUpdateTime(DateUtils.getNowDate());
            operationLog.setUpdateBy(SecurityUtils.getUserId());
            operationLogList.add(operationLog);
        }
        return operationLogMapper.updateOperationLogs(operationLogList);
    }
}

