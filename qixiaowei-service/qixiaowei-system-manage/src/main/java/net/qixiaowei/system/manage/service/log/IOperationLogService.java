package net.qixiaowei.system.manage.service.log;

import java.util.List;

import net.qixiaowei.system.manage.api.dto.log.OperationLogDTO;
import net.qixiaowei.system.manage.api.vo.log.OperationLogVO;


/**
 * OperationLogService接口
 *
 * @author hzk
 * @since 2023-01-04
 */
public interface IOperationLogService {
    /**
     * 查询操作日志表
     *
     * @param operationLogId 操作日志表主键
     * @return 操作日志表
     */
    OperationLogDTO selectOperationLogByOperationLogId(Long operationLogId);

    /**
     * 查询操作日志表列表
     *
     * @param operationLogDTO 操作日志表
     * @return 操作日志表集合
     */
    List<OperationLogVO> selectOperationLogList(OperationLogDTO operationLogDTO);

    /**
     * 新增操作日志表
     *
     * @param operationLogDTO 操作日志表
     * @return 结果
     */
    OperationLogDTO insertOperationLog(OperationLogDTO operationLogDTO);

    /**
     * 修改操作日志表
     *
     * @param operationLogDTO 操作日志表
     * @return 结果
     */
    int updateOperationLog(OperationLogDTO operationLogDTO);

    /**
     * 批量修改操作日志表
     *
     * @param operationLogDtos 操作日志表
     * @return 结果
     */
    int updateOperationLogs(List<OperationLogDTO> operationLogDtos);

    /**
     * 批量新增操作日志表
     *
     * @param operationLogDtos 操作日志表
     * @return 结果
     */
    int insertOperationLogs(List<OperationLogDTO> operationLogDtos);

    /**
     * 逻辑批量删除操作日志表
     *
     * @param operationLogIds 需要删除的操作日志表集合
     * @return 结果
     */
    int logicDeleteOperationLogByOperationLogIds(List<Long> operationLogIds);

    /**
     * 逻辑删除操作日志表信息
     *
     * @param operationLogDTO
     * @return 结果
     */
    int logicDeleteOperationLogByOperationLogId(OperationLogDTO operationLogDTO);

    /**
     * 批量删除操作日志表
     *
     * @param OperationLogDtos
     * @return 结果
     */
    int deleteOperationLogByOperationLogIds(List<OperationLogDTO> OperationLogDtos);

    /**
     * 逻辑删除操作日志表信息
     *
     * @param operationLogDTO
     * @return 结果
     */
    int deleteOperationLogByOperationLogId(OperationLogDTO operationLogDTO);


    /**
     * 删除操作日志表信息
     *
     * @param operationLogId 操作日志表主键
     * @return 结果
     */
    int deleteOperationLogByOperationLogId(Long operationLogId);
}
