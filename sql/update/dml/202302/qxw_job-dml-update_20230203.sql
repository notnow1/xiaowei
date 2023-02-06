USE `qxw_job`;
SET NAMES utf8mb4;

-- ----------------------------
-- init of xxl_job_info add maintainTenantStatus
-- ----------------------------
INSERT INTO xxl_job_info (job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time) VALUES(2, '维护租户状态', '2023-02-03 14:12:40', '2023-02-03 14:12:48', 'hzk', '', 'CRON', '0 0 0 * * ?', 'DO_NOTHING', 'FIRST', 'maintainTenantStatus', '', 'SERIAL_EXECUTION', 0, 1, 'BEAN', '', 'GLUE代码初始化', '2023-02-03 14:12:40', '', 1, 0, 1675440000000);
