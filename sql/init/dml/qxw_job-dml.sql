USE `qxw_job`;
SET NAMES utf8mb4;

-- ----------------------------
-- initialization data
-- ----------------------------
INSERT INTO `xxl_job_group` VALUES (1,'wk-job','悟空定时任务',0,NULL,'2022-11-08 14:01:46'),(2,'qixiaowei-job','企小微定时任务',0,NULL,'2022-11-08 14:01:46');

INSERT INTO `xxl_job_info` VALUES (1,1,'定时放入公海','2022-11-08 12:03:03','2022-11-08 12:01:25','admin','','CRON','0 0 2 * * ?','DO_NOTHING','LAST','CrmCustomerJob','','SERIAL_EXECUTION',7200,0,'BEAN','','GLUE代码初始化','2018-11-03 22:21:31','',1,0,0),(2,1,'日程通知','2022-11-08 12:04:03','2022-11-08 12:04:03','admin','','CRON','0 0 0 * * ?','DO_NOTHING','FIRST','EventNoticeJob','','SERIAL_EXECUTION',0,0,'BEAN','','GLUE代码初始化','2022-11-08 12:04:03','',1,0,0),(3,1,'任务设置延期','2022-11-08 12:05:05','2022-11-08 12:05:05','admin','','CRON','0 0 1 * * ?','DO_NOTHING','FIRST','TaskJob','','SERIAL_EXECUTION',0,0,'BEAN','','GLUE代码初始化','2022-11-08 12:05:05','',1,0,0);

INSERT INTO `xxl_job_user`(`id`, `username`, `password`, `role`, `permission`) VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', 1, NULL);

INSERT INTO `xxl_job_lock` ( `lock_name`) VALUES ( 'schedule_lock');

commit;
