USE `qxw_job`;
SET NAMES utf8mb4;

-- ----------------------------
-- init of xxl_job_info add empAdjustUpdate
-- ----------------------------
INSERT INTO xxl_job_info (job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time) VALUES(2, '个人调薪生效更新', '2023-01-16 15:01:34', '2023-01-16 15:02:33', 'wangchen', '', 'CRON', '0 0 0 * * ?', 'DO_NOTHING', 'FIRST', 'empAdjustUpdateOfDay', '', 'SERIAL_EXECUTION', 0, 1, 'BEAN', '', 'GLUE代码初始化', '2023-01-16 15:01:34', '', 1, 0, 1673884800000);
