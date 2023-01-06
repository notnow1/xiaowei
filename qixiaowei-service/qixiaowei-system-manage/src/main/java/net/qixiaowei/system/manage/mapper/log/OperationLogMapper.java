package net.qixiaowei.system.manage.mapper.log;

import java.util.List;
import net.qixiaowei.system.manage.api.domain.log.OperationLog;
import net.qixiaowei.system.manage.api.dto.log.OperationLogDTO;
import net.qixiaowei.system.manage.api.vo.log.OperationLogVO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* OperationLogMapper接口
* @author hzk
* @since 2023-01-04
*/
public interface OperationLogMapper{
    /**
    * 查询操作日志表
    *
    * @param operationLogId 操作日志表主键
    * @return 操作日志表
    */
    OperationLogDTO selectOperationLogByOperationLogId(@Param("operationLogId")Long operationLogId);


    /**
    * 批量查询操作日志表
    *
    * @param operationLogIds 操作日志表主键集合
    * @return 操作日志表
    */
    List<OperationLogDTO> selectOperationLogByOperationLogIds(@Param("operationLogIds") List<Long> operationLogIds);

    /**
    * 查询操作日志表列表
    *
    * @param operationLog 操作日志表
    * @return 操作日志表集合
    */
    List<OperationLogVO> selectOperationLogList(@Param("operationLog")OperationLog operationLog);

    /**
    * 新增操作日志表
    *
    * @param operationLog 操作日志表
    * @return 结果
    */
    int insertOperationLog(@Param("operationLog")OperationLog operationLog);

    /**
    * 修改操作日志表
    *
    * @param operationLog 操作日志表
    * @return 结果
    */
    int updateOperationLog(@Param("operationLog")OperationLog operationLog);

    /**
    * 批量修改操作日志表
    *
    * @param operationLogList 操作日志表
    * @return 结果
    */
    int updateOperationLogs(@Param("operationLogList")List<OperationLog> operationLogList);
    /**
    * 逻辑删除操作日志表
    *
    * @param operationLog
    * @return 结果
    */
    int logicDeleteOperationLogByOperationLogId(@Param("operationLog")OperationLog operationLog);

    /**
    * 逻辑批量删除操作日志表
    *
    * @param operationLogIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteOperationLogByOperationLogIds(@Param("operationLogIds")List<Long> operationLogIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除操作日志表
    *
    * @param operationLogId 操作日志表主键
    * @return 结果
    */
    int deleteOperationLogByOperationLogId(@Param("operationLogId")Long operationLogId);

    /**
    * 物理批量删除操作日志表
    *
    * @param operationLogIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteOperationLogByOperationLogIds(@Param("operationLogIds")List<Long> operationLogIds);

    /**
    * 批量新增操作日志表
    *
    * @param OperationLogs 操作日志表列表
    * @return 结果
    */
    int batchOperationLog(@Param("operationLogs")List<OperationLog> OperationLogs);
}
