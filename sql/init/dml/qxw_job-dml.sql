USE `qxw_job`;
SET NAMES utf8mb4;

-- ----------------------------
-- initialization data
-- ----------------------------

-- ----------------------------
-- init of xxl_job_group
-- ----------------------------
INSERT INTO xxl_job_group (id, app_name, title, address_type, address_list, update_time) VALUES(1, 'wk-job', '悟空定时任务', 0, NULL, '2022-11-18 14:11:30');
INSERT INTO xxl_job_group (id, app_name, title, address_type, address_list, update_time) VALUES(2, 'qixiaowei-job', '企小微定时任务', 0, NULL, '2022-11-18 14:11:30');


-- ----------------------------
-- init of xxl_job_user
-- ----------------------------
INSERT INTO xxl_job_user (username, password, `role`, permission) VALUES('admin', 'e10adc3949ba59abbe56e057f20f883e', 1, NULL);


-- ----------------------------
-- init of xxl_job_lock
-- ----------------------------
INSERT INTO xxl_job_lock (lock_name) VALUES('schedule_lock');


-- ----------------------------
-- init of xxl_job_info
-- ----------------------------
INSERT INTO xxl_job_info (job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time) VALUES(1, '定时放入公海', '2022-11-08 12:03:03', '2022-11-08 12:01:25', 'admin', '', 'CRON', '0 0 2 * * ?', 'DO_NOTHING', 'LAST', 'CrmCustomerJob', '', 'SERIAL_EXECUTION', 7200, 0, 'BEAN', '', 'GLUE代码初始化', '2018-11-03 22:21:31', '', 1, 0, 1668794400000);
INSERT INTO xxl_job_info (job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time) VALUES(1, '日程通知', '2022-11-08 12:04:03', '2022-11-08 12:04:03', 'admin', '', 'CRON', '0 0 0 * * ?', 'DO_NOTHING', 'FIRST', 'EventNoticeJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2022-11-08 12:04:03', '', 1, 0, 1668787200000);
INSERT INTO xxl_job_info (job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time) VALUES(1, '任务设置延期', '2022-11-08 12:05:05', '2022-11-08 12:05:05', 'admin', '', 'CRON', '0 0 1 * * ?', 'DO_NOTHING', 'FIRST', 'TaskJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2022-11-08 12:05:05', '', 1, 0, 1668790800000);
INSERT INTO xxl_job_info (job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time) VALUES(2, '滚动预测生成【年度】历史版本', '2022-11-08 20:25:15', '2022-11-08 21:04:01', 'Graves', '', 'CRON', '0 0 0 1 1 ?', 'DO_NOTHING', 'FIRST', 'createTargetDecomposeHistoryOfYear', '', 'SERIAL_EXECUTION', 0, 1, 'BEAN', '', 'GLUE代码初始化', '2022-11-08 20:25:15', '', 1, 0, 1672502400000);
INSERT INTO xxl_job_info (job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time) VALUES(2, '滚动预测生成【半年度】历史版', '2022-11-08 20:38:40', '2022-11-08 21:03:59', 'Graves', '', 'CRON', '0 0 0 1 1,7 ?', 'DO_NOTHING', 'FIRST', 'createTargetDecomposeHistoryOfSemiAnnual', '', 'SERIAL_EXECUTION', 0, 1, 'BEAN', '', 'GLUE代码初始化', '2022-11-08 20:38:40', '', 1, 0, 1672502400000);
INSERT INTO xxl_job_info (job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time) VALUES(2, '滚动预测生成【季度】历史版本', '2022-11-08 20:42:21', '2022-11-08 21:03:56', 'Graves', '', 'CRON', '0 0 0 1 1,4,7,10 ?', 'DO_NOTHING', 'FIRST', 'createTargetDecomposeHistoryOfQuarter', '', 'SERIAL_EXECUTION', 0, 1, 'BEAN', '', 'GLUE代码初始化', '2022-11-08 20:42:21', '', 1, 0, 1672502400000);
INSERT INTO xxl_job_info (job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time) VALUES(2, '滚动预测生成【月度】历史版本', '2022-11-08 20:44:50', '2022-11-08 21:03:53', 'Graves', '', 'CRON', '0 0 0 1 * ?', 'DO_NOTHING', 'FIRST', 'createTargetDecomposeHistoryOfMonthly', '', 'SERIAL_EXECUTION', 0, 1, 'BEAN', '', 'GLUE代码初始化', '2022-11-08 20:44:50', '', 1, 0, 1669824000000);
INSERT INTO xxl_job_info (job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time) VALUES(2, '滚动预测生成【周】历史版本', '2022-11-08 21:03:03', '2022-11-08 21:03:50', 'Graves', '', 'CRON', '0 0 0 1/7 * ?', 'DO_NOTHING', 'FIRST', 'createTargetDecomposeHistoryOfWeekly', '', 'SERIAL_EXECUTION', 0, 1, 'BEAN', '', 'GLUE代码初始化', '2022-11-08 21:03:03', '', 1, 1668441600000, 1669046400000);

commit;
