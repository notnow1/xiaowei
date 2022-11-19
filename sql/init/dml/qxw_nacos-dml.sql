USE `qxw_nacos`;
SET NAMES utf8mb4;

-- ----------------------------
-- initialization data
-- ----------------------------

-- ----------------------------
-- init of tenant_info
-- ----------------------------
INSERT INTO tenant_info (kp, tenant_id, tenant_name, tenant_desc, create_source, gmt_create, gmt_modified) VALUES('1', '12856852-866c-4a45-bd0b-d5592775b2a8', 'LOCAL_GROUP', '本地组', 'nacos', 1662630574081, 1662630574081);
INSERT INTO tenant_info (kp, tenant_id, tenant_name, tenant_desc, create_source, gmt_create, gmt_modified) VALUES('1', '8b847f4c-c18d-4fab-a7fa-fb835ebf13df', 'DEV_GROUP', '开发环境', 'nacos', 1662630676359, 1662630676359);
INSERT INTO tenant_info (kp, tenant_id, tenant_name, tenant_desc, create_source, gmt_create, gmt_modified) VALUES('1', 'e939d37b-6d2d-4913-932d-d1aba511acad', 'TEST_GROUP', '测试环境', 'nacos', 1662630710787, 1662630710787);
INSERT INTO tenant_info (kp, tenant_id, tenant_name, tenant_desc, create_source, gmt_create, gmt_modified) VALUES('1', '24d4108a-ada6-45d4-8531-54eb53e6afae', 'PROD_GROUP', '生产环境', 'nacos', 1662630728615, 1662630728615);
INSERT INTO tenant_info (kp, tenant_id, tenant_name, tenant_desc, create_source, gmt_create, gmt_modified) VALUES('1', '63afa625-4e97-4727-b835-2398a4e76051', 'SEATA_GROUP', 'seata', 'nacos', 1667546653437, 1667546653437);
INSERT INTO tenant_info (kp, tenant_id, tenant_name, tenant_desc, create_source, gmt_create, gmt_modified) VALUES('1', '2c63914d-bcd3-4865-b3c6-6b69309c61f9', 'SENTINEL_GROUP', 'sentinel', 'nacos', 1667546751869, 1667546751869);


-- ----------------------------
-- init of users
-- ----------------------------
INSERT INTO users (username, password, enabled) VALUES('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', 1);


-- ----------------------------
-- init of roles
-- ----------------------------
INSERT INTO roles (username, `role`) VALUES('nacos', 'ROLE_ADMIN');


-- ----------------------------
-- init of config_info
-- ----------------------------
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('application-local.yml', 'LOCAL_GROUP', 'spring:
  autoconfigure:
    exclude: com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure
  mvc:
    pathmatch:
      matching-strategy: ant_path_matcher

mybatis:
# 配置mapper映射忽略大小写
  configuration:
    mapUnderscoreToCamelCase: true


# feign 配置
feign:
  sentinel:
    enabled: false
  okhttp:
    enabled: true
  httpclient:
    enabled: false
  client:
    config:
      default:
        connectTimeout: 10000
        readTimeout: 10000
  compression:
    request:
      enabled: true
    response:
      enabled: true

# 暴露监控端点
management:
  endpoints:
    web:
      exposure:
        include: ''*''

qxw:
  file:
    domain: http://127.0.0.1:9000/local', '23b75d76f245527ecd3e6aa0ec71e4b8', '2022-09-09 03:10:12', '2022-11-09 09:43:53', 'nacos', '0:0:0:0:0:0:0:1', '', '12856852-866c-4a45-bd0b-d5592775b2a8', '通用配置', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-gateway-local.yml', 'LOCAL_GROUP', 'spring:
  redis:
    host: localhost
    port: 6379
    password: qxw666
  cloud:
    gateway:
      discovery:
        locator:
          lowerCaseServiceId: true
          enabled: true
      routes:
        # 认证中心
        - id: qixiaowei-auth
          uri: lb://qixiaowei-auth
          predicates:
            - Path=/auth/**
          filters:
            # 验证码处理
            - CacheRequestFilter
            - ValidateCodeFilter
            - StripPrefix=1
        # 文件模块
        - id: qixiaowei-file
          uri: lb://qixiaowei-file
          predicates:
            - Path=/file/**
          filters:
            - StripPrefix=1
          # 系统模块
        - id: qixiaowei-system-manage
          uri: lb://qixiaowei-system-manage
          predicates:
            - Path=/system-manage/**
          filters:
            - StripPrefix=1
        # 经营云
        - id: qixiaowei-operate-cloud
          uri: lb://qixiaowei-operate-cloud
          predicates:
            - Path=/operate-cloud/**
          filters:
            - StripPrefix=1

# 安全配置
security:
  # 验证码
  captcha:
    enabled: false
    type: math
  # 防止XSS攻击
  xss:
    enabled: true
    excludeUrls:
      - /system/notice
  # 不校验白名单
  ignore:
    whites:
      - /auth/logout
      - /auth/login
      - /auth/register
      - /*/v2/api-docs
      - /csrf
', '79e94c2816b06a3af88e89392ec18e6c', '2022-09-09 03:10:12', '2022-11-09 08:41:51', 'nacos', '0:0:0:0:0:0:0:1', '', '12856852-866c-4a45-bd0b-d5592775b2a8', '网关模块', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-auth-local.yml', 'LOCAL_GROUP', 'spring:
  redis:
    host: localhost
    port: 6379
    password: qxw666
', '104349cbc2819f7b0d1affb6dfd8cab7', '2022-09-09 03:10:12', '2022-11-09 08:41:31', 'nacos', '0:0:0:0:0:0:0:1', '', '12856852-866c-4a45-bd0b-d5592775b2a8', '认证中心', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-system-manage-local.yml', 'LOCAL_GROUP', '# spring配置
spring:
  redis:
    host: localhost
    port: 6379
    password: qxw666
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://127.0.0.1:3306/system_manage?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: root
            password: qxw666


# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.system.manage
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml
', '3a12019612df19c26de78690d8599385', '2022-09-09 03:10:12', '2022-11-16 08:17:37', 'nacos', '0:0:0:0:0:0:0:1', '', '12856852-866c-4a45-bd0b-d5592775b2a8', '系统模块', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-file-local.yml', 'LOCAL_GROUP', '# spring配置
spring:
  servlet:
    multipart:
      maxFileSize: 20MB
      maxRequestSize: 200MB
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://127.0.0.1:3306/system_manage?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: root
            password: qxw666


# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.file
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml

# 本地文件上传
file:
  domain: http://127.0.0.1:19300
  path: D:/qixiaowei/uploadPath
  prefix: /statics
# Minio配置
minio:
  url: http://127.0.0.1:9000
  accessKey: minioadmin
  secretKey: minioadmin
  bucketName: local', '8c652e9312554b8ba18cdc9a198f9efe', '2022-09-17 09:45:30', '2022-11-16 08:17:50', 'nacos', '0:0:0:0:0:0:0:1', '', '12856852-866c-4a45-bd0b-d5592775b2a8', '文件服务配置', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-job-local.yml', 'LOCAL_GROUP', 'xxl:
  job:
    admin:
      # xxl-job admin address list, such as "http://address" or "http://address01,http://address02"
      addresses: http://127.0.0.1:7070/xxl-job-admin
    # xxl-job, access token
    accessToken: qixiaowei-cloud-job
    # xxl-job executor appname
    executor:
      appname: qixiaowei-job
      # xxl-job executor registry-address: default use address to registry , otherwise use ip:port if address is null
      address:
      # xxl-job executor server-info
      ip:
      port: 9999
      # xxl-job executor log-path
      logpath: /data/applogs/xxl-job/jobhandler
      # xxl-job executor log-retention-days
      logretentiondays: 30
', 'ebad3752bc4f381d317fb771ef387fa4', '2022-09-23 02:49:26', '2022-11-08 08:59:31', 'nacos', '0:0:0:0:0:0:0:1', '', '12856852-866c-4a45-bd0b-d5592775b2a8', '系统模块', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-message-local.yml', 'LOCAL_GROUP', '# spring配置
spring:
  redis:
    host: localhost
    port: 6379
    password: qxw666
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://localhost:3306/system-manage?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: root
            password: qixiaowei2022


# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.message
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml
', '29b0966ee0d0c57143c82f087dee2b9d', '2022-09-23 02:49:57', '2022-11-09 08:44:52', 'nacos', '0:0:0:0:0:0:0:1', '', '12856852-866c-4a45-bd0b-d5592775b2a8', '系统模块', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-operate-cloud-local.yml', 'LOCAL_GROUP', '# spring配置
spring:
  redis:
    host: localhost
    port: 6379
    password: qxw666
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://127.0.0.1:3306/operate_cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: root
            password: qxw666


# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.operate.cloud
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml
', 'e81532d8690b42f79be604a5c8051934', '2022-09-23 02:50:18', '2022-11-16 08:21:14', 'nacos', '0:0:0:0:0:0:0:1', '', '12856852-866c-4a45-bd0b-d5592775b2a8', '经营模块', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-sales-cloud-local.yml', 'LOCAL_GROUP', '# spring配置
spring:
  redis:
    host: localhost
    port: 6379
    password: qxw666
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://localhost:3306/sales-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: root
            password: qxw666


# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.sales.cloud
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml
', '7e9f186e66b538e212fd397f5986ad5f', '2022-09-23 02:50:40', '2022-11-09 08:43:26', 'nacos', '0:0:0:0:0:0:0:1', '', '12856852-866c-4a45-bd0b-d5592775b2a8', '系统模块', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-strategy-cloud-local.yml', 'LOCAL_GROUP', '# spring配置
spring:
  redis:
    host: localhost
    port: 6379
    password: qxw666
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://localhost:3306/strategy-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: root
            password: qxw666


# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.strategy.cloud
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml
', '812944e77a7360aaf31423b543ffa861', '2022-09-23 02:50:58', '2022-11-09 08:43:56', 'nacos', '0:0:0:0:0:0:0:1', '', '12856852-866c-4a45-bd0b-d5592775b2a8', '系统模块', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-talents-cloud-local.yml', 'LOCAL_GROUP', '# spring配置
spring:
  redis:
    host: localhost
    port: 6379
    password: qxw666
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://localhost:3306/talents-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: root
            password: qxw666


# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.talents.cloud
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml
', '74f4eb5dd2fffc32b88bad263cc14adc', '2022-09-23 02:51:15', '2022-11-09 08:44:05', 'nacos', '0:0:0:0:0:0:0:1', '', '12856852-866c-4a45-bd0b-d5592775b2a8', '系统模块', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-wisdom-cloud-local.yml', 'LOCAL_GROUP', '# spring配置
spring:
  redis:
    host: localhost
    port: 6379
    password: qxw666
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://localhost:3306/wisdom-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: root
            password: qxw666


# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.wisdom.cloud
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml
', '893d57dd12f1e9817cc32d3607da0529', '2022-09-23 02:51:34', '2022-11-09 08:44:14', 'nacos', '0:0:0:0:0:0:0:1', '', '12856852-866c-4a45-bd0b-d5592775b2a8', '系统模块', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-admin-flow-rules', 'SENTINEL_GROUP', '[]', 'd751713988987e9331980363e24189ce', '2022-11-04 07:47:41', '2022-11-04 07:47:41', NULL, '0:0:0:0:0:0:0:1', '', '2c63914d-bcd3-4865-b3c6-6b69309c61f9', NULL, NULL, NULL, 'text', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-admin-local.yml', 'LOCAL_GROUP', 'spring:
  redis:
    host: 127.0.0.1
    port: 6379
    password: qxw666
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/wk_crm?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
    username: root
    password: qxw666
  cloud:
    sentinel:
      filter:
        enabled: true
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow

wk:
  captcha:
    waterMark:
    jigsaw: classpath:public


seata:
  enabled: true
  application-id: ${spring.application.name}
  tx-service-group: ${spring.application.name}_tx_group
  config:
    type: nacos
    nacos:
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      server-addr: ${nacos.server-addr}
      group: SEATA_GROUP
      dataId: seata-server-${spring.profiles.active}.${spring.cloud.nacos.config.file-extension}
      username: ${nacos.username}
      password: ${nacos.password}
  registry:
    type: nacos
    nacos:
      application: seata-server
      group: SEATA_GROUP
      server-addr: ${nacos.server-addr}
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      username: ${nacos.username}
      password: ${nacos.password}', '339eb92850c6536cc802f2eada3cfddc', '2022-11-04 08:14:00', '2022-11-09 08:39:05', 'nacos', '0:0:0:0:0:0:0:1', '', '12856852-866c-4a45-bd0b-d5592775b2a8', '', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-authorization-local.yml', 'LOCAL_GROUP', 'spring:
  redis:
    host: 127.0.0.1
    port: 6379
    password: qxw666
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/wk_crm?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
    username: root
    password: qxw666
  cloud:
    sentinel:
      filter:
        enabled: false
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow', 'f8b9dd1ba7ebf56361296f2a8026cb9a', '2022-11-04 08:16:52', '2022-11-09 08:39:28', 'nacos', '0:0:0:0:0:0:0:1', '', '12856852-866c-4a45-bd0b-d5592775b2a8', '', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-crm-local.yml', 'LOCAL_GROUP', 'spring:
  redis:
    host: 127.0.0.1
    port: 6379
    password: qxw666
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/wk_crm?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
    username: root
    password: qxw666
  elasticsearch:
    rest:
      uris: 127.0.0.1:9200
      username:
      password:
  cloud:
    sentinel:
      filter:
        enabled: false
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow

seata:
  enabled: true
  application-id: ${spring.application.name}
  tx-service-group: ${spring.application.name}_tx_group
  config:
    type: nacos
    nacos:
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      server-addr: ${nacos.server-addr}
      group: SEATA_GROUP
      dataId: seata-server-${spring.profiles.active}.${spring.cloud.nacos.config.file-extension}
      username: ${nacos.username}
      password: ${nacos.password}
  registry:
    type: nacos
    nacos:
      application: seata-server
      group: SEATA_GROUP
      server-addr: ${nacos.server-addr}
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      username: ${nacos.username}
      password: ${nacos.password}

crm:
  enterprise:
    appKey: 21d217b0
    secretKey: 921b1bc0', 'c22aa4234fc49b8fad780195ca0c89ac', '2022-11-04 08:18:02', '2022-11-09 08:39:41', 'nacos', '0:0:0:0:0:0:0:1', '', '12856852-866c-4a45-bd0b-d5592775b2a8', '', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-bi-local.yml', 'LOCAL_GROUP', 'spring:
  redis:
    host: 127.0.0.1
    port: 6379
    password: qxw666
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    dynamic:
      primary: default
      druid:
        filters:
        testWhileIdle: true
        validationQuery: select 1
      datasource:
        #默认的bi配置
        default:
          url: jdbc:mysql://127.0.0.1:3306/wk_crm?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
          username: root
          password: qxw666
  cloud:
    sentinel:
      filter:
        enabled: true
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow

mybatis-plus:
  mapper-locations:
    - classpath:/mapper/${bi.xmlPath}/*.xml
    - classpath:/extend/mapper/xml/*.xml
bi:
  clickhouse: false
  xmlPath: xml
', 'c9d44cff73aa15a1c111acf8aa0962ba', '2022-11-04 08:19:57', '2022-11-09 08:39:51', 'nacos', '0:0:0:0:0:0:0:1', '', '12856852-866c-4a45-bd0b-d5592775b2a8', '', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-examine-local.yml', 'LOCAL_GROUP', 'spring:
  redis:
    host: 127.0.0.1
    port: 6379
    password: qxw666
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/wk_crm?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
    username: root
    password: qxw666
  cloud:
    sentinel:
      filter:
        enabled: false
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow

seata:
  enabled: true
  application-id: ${spring.application.name}
  tx-service-group: ${spring.application.name}_tx_group
  config:
    type: nacos
    nacos:
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      server-addr: ${nacos.server-addr}
      group: SEATA_GROUP
      dataId: seata-server-${spring.profiles.active}.${spring.cloud.nacos.config.file-extension}
      username: ${nacos.username}
      password: ${nacos.password}
  registry:
    type: nacos
    nacos:
      application: seata-server
      group: SEATA_GROUP
      server-addr: ${nacos.server-addr}
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      username: ${nacos.username}
      password: ${nacos.password}', 'ff326b3383544b3dc470f88ce10b5897', '2022-11-04 08:20:58', '2022-11-09 08:40:02', 'nacos', '0:0:0:0:0:0:0:1', '', '12856852-866c-4a45-bd0b-d5592775b2a8', '', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-gateway-local.yml', 'LOCAL_GROUP', 'spring:
  redis:
    host: 127.0.0.1
    port: 6379
    password: qxw666
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/qxw_nacos?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
    username: root
    password: qxw666
  resources:
    cache:
      cachecontrol:
        no-cache: true
    static-locations: file:public/,classpath:public/,classpath:/static,classpath:/resources,classpath:/META-INF/resources,file:D:/upload/public
    add-mappings: true
  mvc:
    throw-exception-if-no-handler-found: true
    favicon:
      enabled: false
  servlet:
    multipart:
      max-file-size: 100MB
      max-request-size: 100MB
  cloud:
    sentinel:
      filter:
        enabled: false
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow
    gateway:
      enabled: true
      discovery:
        locator:
          lower-case-service-id: true
          enabled: true

crm:
  gateway:
    ignoreUrl:
      - /authorization/getLoginQrInfo
      - /adminCompanyManager/queryCompanyLogoByDomain
      - /adminPay/notify
      - /adminCompanyManager/verifyEmailInfo
      - /adminUserApply/queryJsonByUserId
      - /adminUserApply/addUserApply
      - /crmMarketing/queryMarketingId
      - /crmMarketing/queryAddField
      - /crmMarketing/saveMarketingInfo
      - /adminUser/queryUserNumInfo
      - /crmCall/upload', '3f75260a64ca578dd06c6d9c14b892ea', '2022-11-04 08:21:49', '2022-11-09 08:40:53', 'nacos', '0:0:0:0:0:0:0:1', '', '12856852-866c-4a45-bd0b-d5592775b2a8', '', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-oa-local.yml', 'LOCAL_GROUP', 'spring:
  redis:
    host: 127.0.0.1
    port: 6379
    password: qxw666
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/wk_crm?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
    username: root
    password: qxw666
  cloud:
    sentinel:
      filter:
        enabled: false
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow

seata:
  enabled: true
  application-id: ${spring.application.name}
  tx-service-group: ${spring.application.name}_tx_group
  config:
    type: nacos
    nacos:
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      server-addr: ${nacos.server-addr}
      group: SEATA_GROUP
      dataId: seata-server-${spring.profiles.active}.${spring.cloud.nacos.config.file-extension}
      username: ${nacos.username}
      password: ${nacos.password}
  registry:
    type: nacos
    nacos:
      application: seata-server
      group: SEATA_GROUP
      server-addr: ${nacos.server-addr}
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      username: ${nacos.username}
      password: ${nacos.password}
', '87e60a4528ccb78dda20e1a8e01af0d9', '2022-11-04 08:22:24', '2022-11-09 08:40:41', 'nacos', '0:0:0:0:0:0:0:1', '', '12856852-866c-4a45-bd0b-d5592775b2a8', '', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-work-local.yml', 'LOCAL_GROUP', 'spring:
  redis:
    host: 127.0.0.1
    port: 6379
    password: qxw666
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/wk_crm?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
    username: root
    password: qxw666
  cloud:
    sentinel:
      filter:
        enabled: false
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow
', 'd91df623a66726d0681692c0de621edc', '2022-11-04 08:23:57', '2022-11-09 08:40:27', 'nacos', '0:0:0:0:0:0:0:1', '', '12856852-866c-4a45-bd0b-d5592775b2a8', '', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-core-local.yml', 'LOCAL_GROUP', 'spring:
  main:
    allow-bean-definition-overriding: true
  mvc:
    throw-exception-if-no-handler-found: true
    favicon:
      enabled: false
    date-format: yyyy-MM-dd HH:mm:ss
  servlet:
    multipart:
      max-file-size: 100MB
      max-request-size: 100MB
      # 上传文件临时目录，需要先创建此目录
      location: /mnt/upload/
  resources:
    add-mappings: false
  jackson:
    dateFormat: yyyy-MM-dd HH:mm:ss
    timeZone: GMT+8
mybatis-plus:
  configuration:
    call-setters-on-nulls: true
  mapper-locations: classpath:/mapper/xml/*.xml

ribbon:
  okhttp:
    enabled: true
feign:
  client:
    config:
      default:
        connect-timeout: 60000
        read-timeout: 60000

  sentinel:
    enabled: true
  httpclient:
    enabled: false
  okhttp:
    enabled: true
jetcache:
  statIntervalMinutes: 0
  areaInCacheName: false
  remote:
    default:
      type: redis
      keyConvertor: fastjson
      poolConfig:
        minIdle: 5
        maxIdle: 20
        maxTotal: 50
      host: ${spring.redis.host}
      port: ${spring.redis.port}
      password: ${spring.redis.password}
      expireAfterWriteInMillis: 1800000

crm:
  enableCache: true
  upload:
    #上传类型 1 本地 2 阿里云OSS 3 腾讯云COS 4 七牛云QNC 5 aws s3 6 minio
    config: 6
    oss:
      endpoint: http://127.0.0.1:9000
      accessKeyId: minioadmin
      accessKeySecret: minioadmin
      bucketName:
        0: local
        1: local-public
      publicUrl: http://localhost:9000/local-public/
    local:
      publicUrl: http://192.168.0.5:8443/
      uploadPath:
        0: D:/upload/private
        1: D:/upload/public', 'e353b9c88715d47bc4fa15798f7dbafc', '2022-11-04 14:25:21', '2022-11-11 06:12:55', 'nacos', '0:0:0:0:0:0:0:1', '', '12856852-866c-4a45-bd0b-d5592775b2a8', '', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('seata-server-local.yml', 'SEATA_GROUP', '#Transport configuration, for client and server
transport:
  compressor: none
  enableRmClientBatchSendRequest: true
  enableTcServerBatchSendResponse: false
  enableTmClientBatchSendRequest: false
  heartbeat: true
  rpcRmRequestTimeout: 30000
  rpcTcRequestTimeout: 30000
  rpcTmRequestTimeout: 30000
  serialization: seata
  server: NIO
  shutdown:
    wait: 3
  threadFactory:
    bossThreadPrefix: NettyBoss
    bossThreadSize: 1
    clientSelectorThreadPrefix: NettyClientSelector
    clientSelectorThreadSize: 1
    clientWorkerThreadPrefix: NettyClientWorkerThread
    serverExecutorThreadPrefix: NettyServerBizHandler
    shareBossWorker: false
    workerThreadPrefix: NettyServerNIOWorker
    workerThreadSize: default
  type: TCP

#Transaction routing rules configuration, only for the client
service:
  vgroupMapping:
    wk-admin_tx_group: default
    wk-crm_tx_group: default
    wk-examine_tx_group: default
    wk-jxc_tx_group: default
    wk-hrm_tx_group: default
    wk-oa_tx_group: default

#Transaction rule configuration, only for the client
client:
  rm:
    asyncCommitBufferLimit: 10000
    lock:
      retryInterval: 10
      retryPolicyBranchRollbackOnConflict: true
      retryTimes: 30
    reportRetryCount: 5
    reportSuccessEnable: false
    sagaBranchRegisterEnable: false
    sagaJsonParser: fastjson
    sqlParserType: druid
    tableMetaCheckEnable: true
    tableMetaCheckerInterval: 60000
    tccActionInterceptorOrder: -2147482648
  tm:
    commitRetryCount: 5
    defaultGlobalTransactionTimeout: 60000
    degradeCheck: false
    degradeCheckAllowTimes: 10
    degradeCheckPeriod: 2000
    interceptorOrder: -2147482648
    rollbackRetryCount: 5
  undo:
    compress:
      enable: true
      threshold: 64k
      type: zip
    dataValidation: true
    logSerialization: jackson
    logTable: undo_log
    onlyCareUpdateColumns: true
server:
  undo:
    logDeletePeriod: 86400000
    logSaveDays: 7

#For TCC transaction mode
tcc:
  fence:
    cleanPeriod: 1h
    logTableName: tcc_fence_log

#Log rule configuration, for client and server
log:
  exceptionRate: 100

#Transaction storage configuration, only for the server. The file, db, and redis configuration values are optional.
store:
  lock:
    mode: db
  mode: db
  publicKey: ''''
  session:
    mode: db

#These configurations are required if the ``store mode`` is ``db``. If ``store.mode,store.lock.mode,store.session.mode`` are not equal to ``db``, you can remove the configuration block.
store:
  db:
    branchTable: branch_table
    datasource: druid
    dbType: mysql
    distributedLockTable: distributed_lock
    driverClassName: com.mysql.cj.jdbc.Driver
    globalTable: global_table
    lockTable: lock_table
    maxConn: 30
    maxWait: 5000
    minConn: 5
    password: hzk666
    queryLimit: 100
    url: jdbc:mysql://127.0.0.1:3306/qxw_seata?useUnicode=true&rewriteBatchedStatements=true
    user: root

#Transaction rule configuration, only for the server
server:
  distributedLockExpireTime: 10000
  enableParallelRequestHandle: false
  maxCommitRetryTimeout: -1
  maxRollbackRetryTimeout: -1
  recovery:
    asynCommittingRetryPeriod: 1000
    committingRetryPeriod: 1000
    rollbackingRetryPeriod: 1000
    timeoutRetryPeriod: 1000
  rollbackRetryTimeoutUnlockEnable: false
  session:
    branchAsyncQueueSize: 5000
    enableBranchAsyncRemove: false
  xaerNotaRetryTimeout: 60000

#Metrics configuration, only for the server
metrics:
  enabled: false
  exporterList: prometheus
  exporterPrometheusPort: 9898
  registryType: compact

', 'f9a5f46f187683488940702c1a3e502e', '2022-11-05 03:04:23', '2022-11-05 03:31:12', 'nacos', '0:0:0:0:0:0:0:1', '', '63afa625-4e97-4727-b835-2398a4e76051', '', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('seata-server-dev.yml', 'SEATA_GROUP', '#Transport configuration, for client and server
transport:
  compressor: none
  enableRmClientBatchSendRequest: true
  enableTcServerBatchSendResponse: false
  enableTmClientBatchSendRequest: false
  heartbeat: true
  rpcRmRequestTimeout: 30000
  rpcTcRequestTimeout: 30000
  rpcTmRequestTimeout: 30000
  serialization: seata
  server: NIO
  shutdown:
    wait: 3
  threadFactory:
    bossThreadPrefix: NettyBoss
    bossThreadSize: 1
    clientSelectorThreadPrefix: NettyClientSelector
    clientSelectorThreadSize: 1
    clientWorkerThreadPrefix: NettyClientWorkerThread
    serverExecutorThreadPrefix: NettyServerBizHandler
    shareBossWorker: false
    workerThreadPrefix: NettyServerNIOWorker
    workerThreadSize: default
  type: TCP

#Transaction routing rules configuration, only for the client
service:
  vgroupMapping:
    wk-admin_tx_group: default
    wk-crm_tx_group: default
    wk-examine_tx_group: default
    wk-jxc_tx_group: default
    wk-hrm_tx_group: default
    wk-oa_tx_group: default

#Transaction rule configuration, only for the client
client:
  rm:
    asyncCommitBufferLimit: 10000
    lock:
      retryInterval: 10
      retryPolicyBranchRollbackOnConflict: true
      retryTimes: 30
    reportRetryCount: 5
    reportSuccessEnable: false
    sagaBranchRegisterEnable: false
    sagaJsonParser: fastjson
    sqlParserType: druid
    tableMetaCheckEnable: true
    tableMetaCheckerInterval: 60000
    tccActionInterceptorOrder: -2147482648
  tm:
    commitRetryCount: 5
    defaultGlobalTransactionTimeout: 60000
    degradeCheck: false
    degradeCheckAllowTimes: 10
    degradeCheckPeriod: 2000
    interceptorOrder: -2147482648
    rollbackRetryCount: 5
  undo:
    compress:
      enable: true
      threshold: 64k
      type: zip
    dataValidation: true
    logSerialization: jackson
    logTable: undo_log
    onlyCareUpdateColumns: true
server:
  undo:
    logDeletePeriod: 86400000
    logSaveDays: 7

#For TCC transaction mode
tcc:
  fence:
    cleanPeriod: 1h
    logTableName: tcc_fence_log

#Log rule configuration, for client and server
log:
  exceptionRate: 100

#Transaction storage configuration, only for the server. The file, db, and redis configuration values are optional.
store:
  lock:
    mode: db
  mode: db
  publicKey: ''''
  session:
    mode: db

#These configurations are required if the ``store mode`` is ``db``. If ``store.mode,store.lock.mode,store.session.mode`` are not equal to ``db``, you can remove the configuration block.
store:
  db:
    branchTable: branch_table
    datasource: druid
    dbType: mysql
    distributedLockTable: distributed_lock
    driverClassName: com.mysql.cj.jdbc.Driver
    globalTable: global_table
    lockTable: lock_table
    maxConn: 30
    maxWait: 5000
    minConn: 5
    password: hzk666
    queryLimit: 100
    url: jdbc:mysql://127.0.0.1:3306/qxw_seata?useUnicode=true&rewriteBatchedStatements=true
    user: root

#Transaction rule configuration, only for the server
server:
  distributedLockExpireTime: 10000
  enableParallelRequestHandle: false
  maxCommitRetryTimeout: -1
  maxRollbackRetryTimeout: -1
  recovery:
    asynCommittingRetryPeriod: 1000
    committingRetryPeriod: 1000
    rollbackingRetryPeriod: 1000
    timeoutRetryPeriod: 1000
  rollbackRetryTimeoutUnlockEnable: false
  session:
    branchAsyncQueueSize: 5000
    enableBranchAsyncRemove: false
  xaerNotaRetryTimeout: 60000

#Metrics configuration, only for the server
metrics:
  enabled: false
  exporterList: prometheus
  exporterPrometheusPort: 9898
  registryType: compact

', 'f9a5f46f187683488940702c1a3e502e', '2022-11-05 04:00:34', '2022-11-05 04:00:34', NULL, '0:0:0:0:0:0:0:1', '', '63afa625-4e97-4727-b835-2398a4e76051', '', NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('seata-server-test.yml', 'SEATA_GROUP', '#Transport configuration, for client and server
transport:
  compressor: none
  enableRmClientBatchSendRequest: true
  enableTcServerBatchSendResponse: false
  enableTmClientBatchSendRequest: false
  heartbeat: true
  rpcRmRequestTimeout: 30000
  rpcTcRequestTimeout: 30000
  rpcTmRequestTimeout: 30000
  serialization: seata
  server: NIO
  shutdown:
    wait: 3
  threadFactory:
    bossThreadPrefix: NettyBoss
    bossThreadSize: 1
    clientSelectorThreadPrefix: NettyClientSelector
    clientSelectorThreadSize: 1
    clientWorkerThreadPrefix: NettyClientWorkerThread
    serverExecutorThreadPrefix: NettyServerBizHandler
    shareBossWorker: false
    workerThreadPrefix: NettyServerNIOWorker
    workerThreadSize: default
  type: TCP

#Transaction routing rules configuration, only for the client
service:
  vgroupMapping:
    wk-admin_tx_group: default
    wk-crm_tx_group: default
    wk-examine_tx_group: default
    wk-jxc_tx_group: default
    wk-hrm_tx_group: default
    wk-oa_tx_group: default

#Transaction rule configuration, only for the client
client:
  rm:
    asyncCommitBufferLimit: 10000
    lock:
      retryInterval: 10
      retryPolicyBranchRollbackOnConflict: true
      retryTimes: 30
    reportRetryCount: 5
    reportSuccessEnable: false
    sagaBranchRegisterEnable: false
    sagaJsonParser: fastjson
    sqlParserType: druid
    tableMetaCheckEnable: true
    tableMetaCheckerInterval: 60000
    tccActionInterceptorOrder: -2147482648
  tm:
    commitRetryCount: 5
    defaultGlobalTransactionTimeout: 60000
    degradeCheck: false
    degradeCheckAllowTimes: 10
    degradeCheckPeriod: 2000
    interceptorOrder: -2147482648
    rollbackRetryCount: 5
  undo:
    compress:
      enable: true
      threshold: 64k
      type: zip
    dataValidation: true
    logSerialization: jackson
    logTable: undo_log
    onlyCareUpdateColumns: true
server:
  undo:
    logDeletePeriod: 86400000
    logSaveDays: 7

#For TCC transaction mode
tcc:
  fence:
    cleanPeriod: 1h
    logTableName: tcc_fence_log

#Log rule configuration, for client and server
log:
  exceptionRate: 100

#Transaction storage configuration, only for the server. The file, db, and redis configuration values are optional.
store:
  lock:
    mode: db
  mode: db
  publicKey: ''''
  session:
    mode: db

#These configurations are required if the ``store mode`` is ``db``. If ``store.mode,store.lock.mode,store.session.mode`` are not equal to ``db``, you can remove the configuration block.
store:
  db:
    branchTable: branch_table
    datasource: druid
    dbType: mysql
    distributedLockTable: distributed_lock
    driverClassName: com.mysql.cj.jdbc.Driver
    globalTable: global_table
    lockTable: lock_table
    maxConn: 30
    maxWait: 5000
    minConn: 5
    password: k0igme4F
    queryLimit: 100
    url: jdbc:mysql://mysql-qxw-headless.qixiaowei-stg1:3306/qxw_seata?useUnicode=true&rewriteBatchedStatements=true
    user: qxwopr

#Transaction rule configuration, only for the server
server:
  distributedLockExpireTime: 10000
  enableParallelRequestHandle: false
  maxCommitRetryTimeout: -1
  maxRollbackRetryTimeout: -1
  recovery:
    asynCommittingRetryPeriod: 1000
    committingRetryPeriod: 1000
    rollbackingRetryPeriod: 1000
    timeoutRetryPeriod: 1000
  rollbackRetryTimeoutUnlockEnable: false
  session:
    branchAsyncQueueSize: 5000
    enableBranchAsyncRemove: false
  xaerNotaRetryTimeout: 60000

#Metrics configuration, only for the server
metrics:
  enabled: false
  exporterList: prometheus
  exporterPrometheusPort: 9898
  registryType: compact

', '4d87abe77682569784cbe64537c360e2', '2022-11-05 04:00:51', '2022-11-05 15:04:36', 'nacos', '0:0:0:0:0:0:0:1', '', '63afa625-4e97-4727-b835-2398a4e76051', '', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('seata-server-prod.yml', 'SEATA_GROUP', '#Transport configuration, for client and server
transport:
  compressor: none
  enableRmClientBatchSendRequest: true
  enableTcServerBatchSendResponse: false
  enableTmClientBatchSendRequest: false
  heartbeat: true
  rpcRmRequestTimeout: 30000
  rpcTcRequestTimeout: 30000
  rpcTmRequestTimeout: 30000
  serialization: seata
  server: NIO
  shutdown:
    wait: 3
  threadFactory:
    bossThreadPrefix: NettyBoss
    bossThreadSize: 1
    clientSelectorThreadPrefix: NettyClientSelector
    clientSelectorThreadSize: 1
    clientWorkerThreadPrefix: NettyClientWorkerThread
    serverExecutorThreadPrefix: NettyServerBizHandler
    shareBossWorker: false
    workerThreadPrefix: NettyServerNIOWorker
    workerThreadSize: default
  type: TCP

#Transaction routing rules configuration, only for the client
service:
  vgroupMapping:
    wk-admin_tx_group: default
    wk-crm_tx_group: default
    wk-examine_tx_group: default
    wk-jxc_tx_group: default
    wk-hrm_tx_group: default
    wk-oa_tx_group: default

#Transaction rule configuration, only for the client
client:
  rm:
    asyncCommitBufferLimit: 10000
    lock:
      retryInterval: 10
      retryPolicyBranchRollbackOnConflict: true
      retryTimes: 30
    reportRetryCount: 5
    reportSuccessEnable: false
    sagaBranchRegisterEnable: false
    sagaJsonParser: fastjson
    sqlParserType: druid
    tableMetaCheckEnable: true
    tableMetaCheckerInterval: 60000
    tccActionInterceptorOrder: -2147482648
  tm:
    commitRetryCount: 5
    defaultGlobalTransactionTimeout: 60000
    degradeCheck: false
    degradeCheckAllowTimes: 10
    degradeCheckPeriod: 2000
    interceptorOrder: -2147482648
    rollbackRetryCount: 5
  undo:
    compress:
      enable: true
      threshold: 64k
      type: zip
    dataValidation: true
    logSerialization: jackson
    logTable: undo_log
    onlyCareUpdateColumns: true
server:
  undo:
    logDeletePeriod: 86400000
    logSaveDays: 7

#For TCC transaction mode
tcc:
  fence:
    cleanPeriod: 1h
    logTableName: tcc_fence_log

#Log rule configuration, for client and server
log:
  exceptionRate: 100

#Transaction storage configuration, only for the server. The file, db, and redis configuration values are optional.
store:
  lock:
    mode: db
  mode: db
  publicKey: ''''
  session:
    mode: db

#These configurations are required if the ``store mode`` is ``db``. If ``store.mode,store.lock.mode,store.session.mode`` are not equal to ``db``, you can remove the configuration block.
store:
  db:
    branchTable: branch_table
    datasource: druid
    dbType: mysql
    distributedLockTable: distributed_lock
    driverClassName: com.mysql.cj.jdbc.Driver
    globalTable: global_table
    lockTable: lock_table
    maxConn: 30
    maxWait: 5000
    minConn: 5
    password: hzk666
    queryLimit: 100
    url: jdbc:mysql://127.0.0.1:3306/qxw_seata?useUnicode=true&rewriteBatchedStatements=true
    user: root

#Transaction rule configuration, only for the server
server:
  distributedLockExpireTime: 10000
  enableParallelRequestHandle: false
  maxCommitRetryTimeout: -1
  maxRollbackRetryTimeout: -1
  recovery:
    asynCommittingRetryPeriod: 1000
    committingRetryPeriod: 1000
    rollbackingRetryPeriod: 1000
    timeoutRetryPeriod: 1000
  rollbackRetryTimeoutUnlockEnable: false
  session:
    branchAsyncQueueSize: 5000
    enableBranchAsyncRemove: false
  xaerNotaRetryTimeout: 60000

#Metrics configuration, only for the server
metrics:
  enabled: false
  exporterList: prometheus
  exporterPrometheusPort: 9898
  registryType: compact

', 'f9a5f46f187683488940702c1a3e502e', '2022-11-05 04:01:07', '2022-11-05 04:01:07', NULL, '0:0:0:0:0:0:0:1', '', '63afa625-4e97-4727-b835-2398a4e76051', '', NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-job-local.yml', 'LOCAL_GROUP', 'xxl:
  job:
    admin:
      # xxl-job admin address list, such as "http://address" or "http://address01,http://address02"
      addresses: http://127.0.0.1:7070/xxl-job-admin
    # xxl-job executor appname
    executor:
      appname: wk-job
      # xxl-job executor registry-address: default use address to registry , otherwise use ip:port if address is null
      address:
      # xxl-job executor server-info
      ip:
      port: 9998
      # xxl-job executor log-path
      logpath: /data/applogs/xxl-job/jobhandler
      # xxl-job executor log-retention-days
      logretentiondays: 30
    # xxl-job, access token
    accessToken: qixiaowei-cloud-job

spring:
  redis:
    host: 127.0.0.1
    port: 6379
    password: qxw666
    database: 12
    lettuce:
      pool:
        max-active: 300
', '0f4bb91897b6d20af3f6ff829dc4d0a2', '2022-11-05 06:35:33', '2022-11-09 08:40:14', 'nacos', '0:0:0:0:0:0:0:1', '', '12856852-866c-4a45-bd0b-d5592775b2a8', '', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('application-dev.yml', 'DEV_GROUP', 'spring:
  autoconfigure:
    exclude: com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure
  mvc:
    pathmatch:
      matching-strategy: ant_path_matcher

mybatis:
# 配置mapper映射忽略大小写
  configuration:
    mapUnderscoreToCamelCase: true


# feign 配置
feign:
  sentinel:
    enabled: false
  okhttp:
    enabled: true
  httpclient:
    enabled: false
  client:
    config:
      default:
        connectTimeout: 10000
        readTimeout: 10000
  compression:
    request:
      enabled: true
    response:
      enabled: true

# 暴露监控端点
management:
  endpoints:
    web:
      exposure:
        include: ''*''

qxw:
  file:
    domain: http://k8s-stg.qixiaowei.net:30711/dev', '409eb34b9b53b1f8eb93b58022d41abd', '2022-11-05 13:57:44', '2022-11-05 13:57:44', NULL, '0:0:0:0:0:0:0:1', '', '8b847f4c-c18d-4fab-a7fa-fb835ebf13df', NULL, NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-gateway-dev.yml', 'DEV_GROUP', 'spring:
  redis:
    host: db-dev.qixiaowei.net
    port: 31210
    password: ARxabyM7
  cloud:
    gateway:
      discovery:
        locator:
          lowerCaseServiceId: true
          enabled: true
      routes:
        # 认证中心
        - id: qixiaowei-auth
          uri: lb://qixiaowei-auth
          predicates:
            - Path=/auth/**
          filters:
            # 验证码处理
            - CacheRequestFilter
            - ValidateCodeFilter
            - StripPrefix=1
        # 文件模块
        - id: qixiaowei-file
          uri: lb://qixiaowei-file
          predicates:
            - Path=/file/**
          filters:
            - StripPrefix=1
          # 系统模块
        - id: qixiaowei-system-manage
          uri: lb://qixiaowei-system-manage
          predicates:
            - Path=/system-manage/**
          filters:
            - StripPrefix=1
        # 经营云
        - id: qixiaowei-operate-cloud
          uri: lb://qixiaowei-operate-cloud
          predicates:
            - Path=/operate-cloud/**
          filters:
            - StripPrefix=1

# 安全配置
security:
  # 验证码
  captcha:
    enabled: false
    type: math
  # 防止XSS攻击
  xss:
    enabled: true
    excludeUrls:
      - /system/notice
  # 不校验白名单
  ignore:
    whites:
      - /auth/logout
      - /auth/login
      - /auth/register
      - /*/v2/api-docs
      - /csrf
', '34e4c86cdc64993d9879413bca7ef28d', '2022-11-05 13:57:44', '2022-11-05 13:57:44', NULL, '0:0:0:0:0:0:0:1', '', '8b847f4c-c18d-4fab-a7fa-fb835ebf13df', NULL, NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-auth-dev.yml', 'DEV_GROUP', 'spring:
  redis:
    host: db-dev.qixiaowei.net
    port: 31210
    password: ARxabyM7
', '7c16bfd9f305e4f771de47f6429ce3a2', '2022-11-05 13:57:44', '2022-11-05 13:57:44', NULL, '0:0:0:0:0:0:0:1', '', '8b847f4c-c18d-4fab-a7fa-fb835ebf13df', NULL, NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-system-manage-dev.yml', 'DEV_GROUP', '# spring配置
spring:
  redis:
    host: db-dev.qixiaowei.net
    port: 31210
    password: ARxabyM7
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://db-dev.qixiaowei.net:31194/system_manage?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8
            username: qxwopr
            password: 7fpJR7i2


# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.system.manage
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml
', '34f8dacc1db3525a4df9438ffca1dacd', '2022-11-05 13:57:44', '2022-11-16 09:31:22', 'nacos', '0:0:0:0:0:0:0:1', '', '8b847f4c-c18d-4fab-a7fa-fb835ebf13df', '', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-file-dev.yml', 'DEV_GROUP', '# spring配置
spring:
  servlet:
    multipart:
      maxFileSize: 20MB
      maxRequestSize: 200MB
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://db-dev.qixiaowei.net:31194/system_manage?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8
            username: qxwopr
            password: 7fpJR7i2

# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.file
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml

# 本地文件上传
file:
  domain: http://127.0.0.1:19300
  path: D:/qixiaowei/uploadPath
  prefix: /statics
# Minio配置
minio:
  url: http://k8s-stg.qixiaowei.net:30711
  accessKey: qxwopr-dev
  secretKey: oL8bqxwopr
  bucketName: dev', '08472aff07d22a7c988d5afdce4c2501', '2022-11-05 13:57:44', '2022-11-16 09:31:40', 'nacos', '0:0:0:0:0:0:0:1', '', '8b847f4c-c18d-4fab-a7fa-fb835ebf13df', '', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-job-dev.yml', 'DEV_GROUP', 'xxl:
  job:
    admin:
      # xxl-job admin address list, such as "http://address" or "http://address01,http://address02"
      addresses: http://127.0.0.1:7070/xxl-job-admin
    # xxl-job, access token
    accessToken: qixiaowei-cloud-job
    # xxl-job executor appname
    executor:
      appname: qixiaowei-job
      # xxl-job executor registry-address: default use address to registry , otherwise use ip:port if address is null
      address:
      # xxl-job executor server-info
      ip:
      port: 9999
      # xxl-job executor log-path
      logpath: /data/applogs/xxl-job/jobhandler
      # xxl-job executor log-retention-days
      logretentiondays: 30
', 'ebad3752bc4f381d317fb771ef387fa4', '2022-11-05 13:57:44', '2022-11-16 09:32:07', 'nacos', '0:0:0:0:0:0:0:1', '', '8b847f4c-c18d-4fab-a7fa-fb835ebf13df', '', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-message-dev.yml', 'DEV_GROUP', '# spring配置
spring:
  redis:
    host: db-dev.qixiaowei.net
    port: 31210
    password: ARxabyM7
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://db-dev.qixiaowei.net:31194/system-manage?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: qxwopr
            password: 7fpJR7i2


# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.message
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml
', '121bcc97ac508b1e283fd6063fbe6d8d', '2022-11-05 13:57:44', '2022-11-05 13:57:44', NULL, '0:0:0:0:0:0:0:1', '', '8b847f4c-c18d-4fab-a7fa-fb835ebf13df', NULL, NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-operate-cloud-dev.yml', 'DEV_GROUP', '# spring配置
spring:
  redis:
    host: db-dev.qixiaowei.net
    port: 31210
    password: ARxabyM7
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://db-dev.qixiaowei.net:31194/operate_cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8
            username: qxwopr
            password: 7fpJR7i2


# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.operate.cloud
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml
', 'cf2ad9a7e170220d8fea5c64d64053be', '2022-11-05 13:57:44', '2022-11-16 09:32:53', 'nacos', '0:0:0:0:0:0:0:1', '', '8b847f4c-c18d-4fab-a7fa-fb835ebf13df', '', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-sales-cloud-dev.yml', 'DEV_GROUP', '# spring配置
spring:
  redis:
    host: db-dev.qixiaowei.net
    port: 31210
    password: ARxabyM7
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://db-dev.qixiaowei.net:31194/system-manage?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: qxwopr
            password: 7fpJR7i2


# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.sales.cloud
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml
', '6328a350a63144d8429eb27bfd9b65a7', '2022-11-05 13:57:44', '2022-11-05 13:57:44', NULL, '0:0:0:0:0:0:0:1', '', '8b847f4c-c18d-4fab-a7fa-fb835ebf13df', NULL, NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-strategy-cloud-dev.yml', 'DEV_GROUP', '# spring配置
spring:
  redis:
    host: db-dev.qixiaowei.net
    port: 31210
    password: ARxabyM7
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://db-dev.qixiaowei.net:31194/system-manage?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: qxwopr
            password: 7fpJR7i2


# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.strategy.cloud
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml
', '855d4575ccb40c877d516ab172f82ef2', '2022-11-05 13:57:44', '2022-11-05 13:57:44', NULL, '0:0:0:0:0:0:0:1', '', '8b847f4c-c18d-4fab-a7fa-fb835ebf13df', NULL, NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-talents-cloud-dev.yml', 'DEV_GROUP', '# spring配置
spring:
  redis:
    host: db-dev.qixiaowei.net
    port: 31210
    password: ARxabyM7
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://db-dev.qixiaowei.net:31194/system-manage?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: qxwopr
            password: 7fpJR7i2


# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.talents.cloud
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml
', '72d56f2fd00def152c4da1625c1cb821', '2022-11-05 13:57:44', '2022-11-05 13:57:44', NULL, '0:0:0:0:0:0:0:1', '', '8b847f4c-c18d-4fab-a7fa-fb835ebf13df', NULL, NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-wisdom-cloud-dev.yml', 'DEV_GROUP', '# spring配置
spring:
  redis:
    host: db-dev.qixiaowei.net
    port: 31210
    password: ARxabyM7
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://db-dev.qixiaowei.net:31194/system-manage?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: qxwopr
            password: 7fpJR7i2


# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.wisdom.cloud
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml
', '9ef329671f9c8e06ae2a0a9878f1887e', '2022-11-05 13:57:44', '2022-11-05 13:57:44', NULL, '0:0:0:0:0:0:0:1', '', '8b847f4c-c18d-4fab-a7fa-fb835ebf13df', NULL, NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-admin-dev.yml', 'DEV_GROUP', 'spring:
  redis:
    host: 127.0.0.1
    port: 6379
    password:
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/wk_crm?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
    username: root
    password: qxw666
  cloud:
    sentinel:
      filter:
        enabled: true
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow

wk:
  captcha:
    waterMark:
    jigsaw: classpath:public


seata:
  enabled: true
  application-id: ${spring.application.name}
  tx-service-group: ${spring.application.name}_tx_group
  config:
    type: nacos
    nacos:
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      server-addr: ${nacos.server-addr}
      group: SEATA_GROUP
      dataId: seata-server-${spring.profiles.active}.${spring.cloud.nacos.config.file-extension}
      username: ${nacos.username}
      password: ${nacos.password}
  registry:
    type: nacos
    nacos:
      application: seata-server
      group: SEATA_GROUP
      server-addr: ${nacos.server-addr}
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      username: ${nacos.username}
      password: ${nacos.password}', 'f33818a54bfe1b47ea5e9bea4cfd2c91', '2022-11-05 14:04:53', '2022-11-05 14:04:53', NULL, '0:0:0:0:0:0:0:1', '', '8b847f4c-c18d-4fab-a7fa-fb835ebf13df', '', NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-authorization-dev.yml', 'DEV_GROUP', 'spring:
  redis:
    host: 127.0.0.1
    port: 6379
    password: 123456
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/wk_crm?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
    username: root
    password: qxw666
  cloud:
    sentinel:
      filter:
        enabled: false
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow', '1a57c52fa0c326c264a6746e4d50b519', '2022-11-05 14:04:53', '2022-11-05 14:04:53', NULL, '0:0:0:0:0:0:0:1', '', '8b847f4c-c18d-4fab-a7fa-fb835ebf13df', '', NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-crm-dev.yml', 'DEV_GROUP', 'spring:
  redis:
    host: 127.0.0.1
    port: 6379
    password:
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/wk_crm?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
    username: root
    password: qxw666
  elasticsearch:
    rest:
      uris: 127.0.0.1:9200
      username:
      password:
  cloud:
    sentinel:
      filter:
        enabled: false
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow

seata:
  enabled: true
  application-id: ${spring.application.name}
  tx-service-group: ${spring.application.name}_tx_group
  config:
    type: nacos
    nacos:
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      server-addr: ${nacos.server-addr}
      group: SEATA_GROUP
      dataId: seata-server-${spring.profiles.active}.${spring.cloud.nacos.config.file-extension}
      username: ${nacos.username}
      password: ${nacos.password}
  registry:
    type: nacos
    nacos:
      application: seata-server
      group: SEATA_GROUP
      server-addr: ${nacos.server-addr}
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      username: ${nacos.username}
      password: ${nacos.password}

crm:
  enterprise:
    appKey: 21d217b0
    secretKey: 921b1bc0', '0f68e56faf3618b470b4469edcf97696', '2022-11-05 14:04:53', '2022-11-05 14:04:53', NULL, '0:0:0:0:0:0:0:1', '', '8b847f4c-c18d-4fab-a7fa-fb835ebf13df', '', NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-bi-dev.yml', 'DEV_GROUP', 'spring:
  redis:
    host: 127.0.0.1
    port: 6379
    password: 123456
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    dynamic:
      primary: default
      druid:
        filters:
        testWhileIdle: true
        validationQuery: select 1
      datasource:
        #默认的bi配置
        default:
          url: jdbc:mysql://127.0.0.1:3306/wk_crm?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
          username: root
          password: qxw666
  cloud:
    sentinel:
      filter:
        enabled: true
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow

mybatis-plus:
  mapper-locations:
    - classpath:/mapper/${bi.xmlPath}/*.xml
    - classpath:/extend/mapper/xml/*.xml
bi:
  clickhouse: false
  xmlPath: xml
', '7a877f90b6e18456bf0f21990c1de8f5', '2022-11-05 14:04:53', '2022-11-05 14:04:53', NULL, '0:0:0:0:0:0:0:1', '', '8b847f4c-c18d-4fab-a7fa-fb835ebf13df', '', NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-examine-dev.yml', 'DEV_GROUP', 'spring:
  redis:
    host: 127.0.0.1
    port: 6379
    password: 123456
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/wk_crm?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
    username: root
    password: qxw666
  cloud:
    sentinel:
      filter:
        enabled: false
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow

seata:
  enabled: true
  application-id: ${spring.application.name}
  tx-service-group: ${spring.application.name}_tx_group
  config:
    type: nacos
    nacos:
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      server-addr: ${nacos.server-addr}
      group: SEATA_GROUP
      dataId: seata-server-${spring.profiles.active}.${spring.cloud.nacos.config.file-extension}
      username: ${nacos.username}
      password: ${nacos.password}
  registry:
    type: nacos
    nacos:
      application: seata-server
      group: SEATA_GROUP
      server-addr: ${nacos.server-addr}
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      username: ${nacos.username}
      password: ${nacos.password}', '99dbacdabba9166c17541cd80bb1d7a7', '2022-11-05 14:04:53', '2022-11-05 14:04:53', NULL, '0:0:0:0:0:0:0:1', '', '8b847f4c-c18d-4fab-a7fa-fb835ebf13df', '', NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-gateway-dev.yml', 'DEV_GROUP', 'spring:
  redis:
    host: 127.0.0.1
    port: 6379
    password: 123456
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/qxw_nacos?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
    username: root
    password: qxw666
  resources:
    cache:
      cachecontrol:
        no-cache: true
    static-locations: file:public/,classpath:public/,classpath:/static,classpath:/resources,classpath:/META-INF/resources,file:D:/upload/public
    add-mappings: true
  mvc:
    throw-exception-if-no-handler-found: true
    favicon:
      enabled: false
  servlet:
    multipart:
      max-file-size: 100MB
      max-request-size: 100MB
  cloud:
    sentinel:
      filter:
        enabled: false
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow
    gateway:
      enabled: true
      discovery:
        locator:
          lower-case-service-id: true
          enabled: true

crm:
  gateway:
    ignoreUrl:
      - /authorization/getLoginQrInfo
      - /adminCompanyManager/queryCompanyLogoByDomain
      - /adminPay/notify
      - /adminCompanyManager/verifyEmailInfo
      - /adminUserApply/queryJsonByUserId
      - /adminUserApply/addUserApply
      - /crmMarketing/queryMarketingId
      - /crmMarketing/queryAddField
      - /crmMarketing/saveMarketingInfo
      - /adminUser/queryUserNumInfo
      - /crmCall/upload', '8fd18755b05f4c5f75cd5a11443561b4', '2022-11-05 14:04:53', '2022-11-05 14:04:53', NULL, '0:0:0:0:0:0:0:1', '', '8b847f4c-c18d-4fab-a7fa-fb835ebf13df', '', NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-oa-dev.yml', 'DEV_GROUP', 'spring:
  redis:
    host: 127.0.0.1
    port: 6379
    password: 123456
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/wk_crm?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
    username: root
    password: qxw666
  cloud:
    sentinel:
      filter:
        enabled: false
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow

seata:
  enabled: true
  application-id: ${spring.application.name}
  tx-service-group: ${spring.application.name}_tx_group
  config:
    type: nacos
    nacos:
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      server-addr: ${nacos.server-addr}
      group: SEATA_GROUP
      dataId: seata-server-${spring.profiles.active}.${spring.cloud.nacos.config.file-extension}
      username: ${nacos.username}
      password: ${nacos.password}
  registry:
    type: nacos
    nacos:
      application: seata-server
      group: SEATA_GROUP
      server-addr: ${nacos.server-addr}
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      username: ${nacos.username}
      password: ${nacos.password}
', '3c3841b528a36461941aeaabf7f92a39', '2022-11-05 14:04:53', '2022-11-05 14:04:53', NULL, '0:0:0:0:0:0:0:1', '', '8b847f4c-c18d-4fab-a7fa-fb835ebf13df', '', NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-work-dev.yml', 'DEV_GROUP', 'spring:
  redis:
    host: 127.0.0.1
    port: 6379
    password: 123456
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/wk_crm?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
    username: root
    password: qxw666
  cloud:
    sentinel:
      filter:
        enabled: false
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow
', 'b7f5edc176a773ca6cb3f19199343c88', '2022-11-05 14:04:53', '2022-11-05 14:04:53', NULL, '0:0:0:0:0:0:0:1', '', '8b847f4c-c18d-4fab-a7fa-fb835ebf13df', '', NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-core-dev.yml', 'DEV_GROUP', 'spring:
  main:
    allow-bean-definition-overriding: true
  mvc:
    throw-exception-if-no-handler-found: true
    favicon:
      enabled: false
    date-format: yyyy-MM-dd HH:mm:ss
  servlet:
    multipart:
      max-file-size: 100MB
      max-request-size: 100MB
      # 上传文件临时目录，需要先创建此目录
      location: /mnt/upload/
  resources:
    add-mappings: false
  jackson:
    dateFormat: yyyy-MM-dd HH:mm:ss
    timeZone: GMT+8
mybatis-plus:
  configuration:
    call-setters-on-nulls: true
  mapper-locations: classpath:/mapper/xml/*.xml

ribbon:
  okhttp:
    enabled: true
feign:
  client:
    config:
      default:
        connect-timeout: 60000
        read-timeout: 60000

  sentinel:
    enabled: true
  httpclient:
    enabled: false
  okhttp:
    enabled: true
jetcache:
  statIntervalMinutes: 0
  areaInCacheName: false
  remote:
    default:
      type: redis
      keyConvertor: fastjson
      poolConfig:
        minIdle: 5
        maxIdle: 20
        maxTotal: 50
      host: ${spring.redis.host}
      port: ${spring.redis.port}
      password: ${spring.redis.password}
      expireAfterWriteInMillis: 1800000

crm:
  enableCache: true
  upload:
    #上传类型 1 本地 2 阿里云OSS 3 腾讯云COS 4 七牛云QNC 5 aws s3
    config: 6
    oss:
      endpoint: http://k8s-stg.qixiaowei.net:30711
      accessKeyId: qxwopr-dev
      accessKeySecret: oL8bqxwopr
      bucketName:
        0: dev
        1: dev-public
      publicUrl: http://k8s-stg.qixiaowei.net:30711/dev-public
    local:
      publicUrl: http://192.168.0.5:8443/
      uploadPath:
        0: D:/upload/private
        1: D:/upload/public

', '05630966ae516106a778984d8a871298', '2022-11-05 14:04:53', '2022-11-10 08:12:32', 'nacos', '0:0:0:0:0:0:0:1', '', '8b847f4c-c18d-4fab-a7fa-fb835ebf13df', '', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-job-dev.yml', 'DEV_GROUP', 'xxl:
  job:
    admin:
      # xxl-job admin address list, such as "http://address" or "http://address01,http://address02"
      addresses: http://127.0.0.1:7070/xxl-job-admin
    # xxl-job executor appname
    executor:
      appname: wk-job
      # xxl-job executor registry-address: default use address to registry , otherwise use ip:port if address is null
      address:
      # xxl-job executor server-info
      ip:
      port: 9998
      # xxl-job executor log-path
      logpath: /data/applogs/xxl-job/jobhandler
      # xxl-job executor log-retention-days
      logretentiondays: 30
    # xxl-job, access token
    accessToken: qixiaowei-cloud-job

spring:
  redis:
    host: 127.0.0.1
    port: 6379
    password: 123456
    database: 12
    lettuce:
      pool:
        max-active: 300
', '147fb6d71406548790bfad6cf2641cf7', '2022-11-05 14:04:53', '2022-11-08 07:41:35', 'nacos', '0:0:0:0:0:0:0:1', '', '8b847f4c-c18d-4fab-a7fa-fb835ebf13df', '', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('application-test.yml', 'TEST_GROUP', 'spring:
  autoconfigure:
    exclude: com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure
  mvc:
    pathmatch:
      matching-strategy: ant_path_matcher

mybatis:
# 配置mapper映射忽略大小写
  configuration:
    mapUnderscoreToCamelCase: true


# feign 配置
feign:
  sentinel:
    enabled: false
  okhttp:
    enabled: true
  httpclient:
    enabled: false
  client:
    config:
      default:
        connectTimeout: 10000
        readTimeout: 10000
  compression:
    request:
      enabled: true
    response:
      enabled: true

# 暴露监控端点
management:
  endpoints:
    web:
      exposure:
        include: ''*''

qxw:
  file:
    domain: http://k8s-stg.qixiaowei.net:30711/test', '29ff4f56a2710143162bec8fd82ff3d6', '2022-11-05 14:11:08', '2022-11-05 14:11:08', NULL, '0:0:0:0:0:0:0:1', '', 'e939d37b-6d2d-4913-932d-d1aba511acad', NULL, NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-gateway-test.yml', 'TEST_GROUP', 'spring:
  redis:
    host: redis-qxw-headless.qixiaowei-stg1
    port: 6379
    password: m071zHwo
  cloud:
    gateway:
      discovery:
        locator:
          lowerCaseServiceId: true
          enabled: true
      routes:
        # 认证中心
        - id: qixiaowei-auth
          uri: lb://qixiaowei-auth
          predicates:
            - Path=/auth/**
          filters:
            # 验证码处理
            - CacheRequestFilter
            - ValidateCodeFilter
            - StripPrefix=1
        # 文件模块
        - id: qixiaowei-file
          uri: lb://qixiaowei-file
          predicates:
            - Path=/file/**
          filters:
            - StripPrefix=1
        # 系统模块
        - id: qixiaowei-system-manage
          uri: lb://qixiaowei-system-manage
          predicates:
            - Path=/system-manage/**
          filters:
            - StripPrefix=1
        # 经营云
        - id: qixiaowei-operate-cloud
          uri: lb://qixiaowei-operate-cloud
          predicates:
            - Path=/operate-cloud/**
          filters:
            - StripPrefix=1

# 安全配置
security:
  # 验证码
  captcha:
    enabled: false
    type: math
  # 防止XSS攻击
  xss:
    enabled: true
    excludeUrls:
      - /system/notice
  # 不校验白名单
  ignore:
    whites:
      - /auth/logout
      - /auth/login
      - /auth/register
      - /*/v2/api-docs
      - /csrf
', 'd7ca4f1fa4367347ffed2334eb17a747', '2022-11-05 14:11:08', '2022-11-05 14:11:08', NULL, '0:0:0:0:0:0:0:1', '', 'e939d37b-6d2d-4913-932d-d1aba511acad', NULL, NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-auth-test.yml', 'TEST_GROUP', 'spring:
  redis:
    host: redis-qxw-headless.qixiaowei-stg1
    port: 6379
    password: m071zHwo
', '5fb8ac74fc16e4620192a04f3a9c9b65', '2022-11-05 14:11:08', '2022-11-05 14:11:08', NULL, '0:0:0:0:0:0:0:1', '', 'e939d37b-6d2d-4913-932d-d1aba511acad', NULL, NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-system-manage-test.yml', 'TEST_GROUP', '# spring配置
spring:
  redis:
    host: redis-qxw-headless.qixiaowei-stg1
    port: 6379
    password: m071zHwo
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://mysql-qxw-headless.qixiaowei-stg1:3306/system_manage?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: qxwopr
            password: k0igme4F


# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.system.manage
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml
', '0927634694eeeb2f06a0a750b792e0bd', '2022-11-05 14:11:08', '2022-11-16 09:34:12', 'nacos', '0:0:0:0:0:0:0:1', '', 'e939d37b-6d2d-4913-932d-d1aba511acad', '', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-file-test.yml', 'TEST_GROUP', '# spring配置
spring:
  servlet:
    multipart:
      maxFileSize: 20MB
      maxRequestSize: 200MB
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://mysql-qxw-headless.qixiaowei-stg1:3306/system_manage?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: qxwopr
            password: k0igme4F

# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.file
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml

# 本地文件上传
file:
  domain: http://127.0.0.1:19300
  path: D:/qixiaowei/uploadPath
  prefix: /statics
# Minio配置
minio:
  url: http://k8s-stg.qixiaowei.net:30711
  accessKey: qxwopr-test
  secretKey: 8Krjs1oL8b
  bucketName: test', 'd745731ccba45347a4f8ad32973c13e8', '2022-11-05 14:11:08', '2022-11-16 09:34:25', 'nacos', '0:0:0:0:0:0:0:1', '', 'e939d37b-6d2d-4913-932d-d1aba511acad', '', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-job-test.yml', 'TEST_GROUP', 'xxl:
  job:
    admin:
      # xxl-job admin address list, such as "http://address" or "http://address01,http://address02"
      addresses: http://127.0.0.1:7070/xxl-job-admin
    # xxl-job, access token
    accessToken: qixiaowei-cloud-job
    # xxl-job executor appname
    executor:
      appname: qixiaowei-job
      # xxl-job executor registry-address: default use address to registry , otherwise use ip:port if address is null
      address:
      # xxl-job executor server-info
      ip:
      port: 9999
      # xxl-job executor log-path
      logpath: /data/applogs/xxl-job/jobhandler
      # xxl-job executor log-retention-days
      logretentiondays: 30
', 'ebad3752bc4f381d317fb771ef387fa4', '2022-11-05 14:11:08', '2022-11-16 09:32:17', 'nacos', '0:0:0:0:0:0:0:1', '', 'e939d37b-6d2d-4913-932d-d1aba511acad', '', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-message-test.yml', 'TEST_GROUP', '# spring配置
spring:
  redis:
    host: localhost
    port: 6379
    password:
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://localhost:3306/system-manage?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: root
            password: qixiaowei2022


# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.message
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml
', '61466f747fdee6c0461c8ec7d24b82fe', '2022-11-05 14:11:08', '2022-11-05 14:11:08', NULL, '0:0:0:0:0:0:0:1', '', 'e939d37b-6d2d-4913-932d-d1aba511acad', NULL, NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-operate-cloud-test.yml', 'TEST_GROUP', '# spring配置
spring:
  redis:
    host: redis-qxw-headless.qixiaowei-stg1
    port: 6379
    password: m071zHwo
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://mysql-qxw-headless.qixiaowei-stg1:3306/operate_cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: qxwopr
            password: k0igme4F


# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.operate.cloud
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml
', '817d55459ce51c0a8ee2cb27291c8501', '2022-11-05 14:11:08', '2022-11-16 09:34:42', 'nacos', '0:0:0:0:0:0:0:1', '', 'e939d37b-6d2d-4913-932d-d1aba511acad', '', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-sales-cloud-test.yml', 'TEST_GROUP', '# spring配置
spring:
  redis:
    host: localhost
    port: 6379
    password:
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://localhost:3306/sales-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: root
            password: qixiaowei2022


# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.sales.cloud
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml
', '70adc94704bfdf4fff2e87f945c3c308', '2022-11-05 14:11:08', '2022-11-05 14:11:08', NULL, '0:0:0:0:0:0:0:1', '', 'e939d37b-6d2d-4913-932d-d1aba511acad', NULL, NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-strategy-cloud-test.yml', 'TEST_GROUP', '# spring配置
spring:
  redis:
    host: localhost
    port: 6379
    password:
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://localhost:3306/strategy-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: root
            password: qixiaowei2022


# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.strategy.cloud
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml
', 'ca724432f4fcd8f89312bbb75bf00641', '2022-11-05 14:11:08', '2022-11-05 14:11:08', NULL, '0:0:0:0:0:0:0:1', '', 'e939d37b-6d2d-4913-932d-d1aba511acad', NULL, NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-talents-cloud-test.yml', 'TEST_GROUP', '# spring配置
spring:
  redis:
    host: localhost
    port: 6379
    password:
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://localhost:3306/talents-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: root
            password: qixiaowei2022


# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.talents.cloud
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml
', '9e1c44a646b2f429bd9bc91e7b68af8d', '2022-11-05 14:11:08', '2022-11-05 14:11:08', NULL, '0:0:0:0:0:0:0:1', '', 'e939d37b-6d2d-4913-932d-d1aba511acad', NULL, NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-wisdom-cloud-test.yml', 'TEST_GROUP', '# spring配置
spring:
  redis:
    host: localhost
    port: 6379
    password:
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://localhost:3306/wisdom-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: root
            password: qixiaowei2022


# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.wisdom.cloud
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml
', 'bf7fc032014cd7db69dcb77e9864a553', '2022-11-05 14:11:08', '2022-11-05 14:11:08', NULL, '0:0:0:0:0:0:0:1', '', 'e939d37b-6d2d-4913-932d-d1aba511acad', NULL, NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-admin-test.yml', 'TEST_GROUP', 'spring:
  redis:
    host: redis-qxw-headless.qixiaowei-stg1
    port: 6379
    password: m071zHwo
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    url: jdbc:mysql://mysql-qxw-headless.qixiaowei-stg1:3306/wk_crm?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
    username: qxwopr
    password: k0igme4F
  cloud:
    sentinel:
      filter:
        enabled: true
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow

wk:
  captcha:
    waterMark:
    jigsaw: classpath:public


seata:
  enabled: true
  application-id: ${spring.application.name}
  tx-service-group: ${spring.application.name}_tx_group
  config:
    type: nacos
    nacos:
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      server-addr: ${nacos.server-addr}
      group: SEATA_GROUP
      dataId: seata-server-${spring.profiles.active}.${spring.cloud.nacos.config.file-extension}
      username: ${nacos.username}
      password: ${nacos.password}
  registry:
    type: nacos
    nacos:
      application: seata-server
      group: SEATA_GROUP
      server-addr: ${nacos.server-addr}
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      username: ${nacos.username}
      password: ${nacos.password}', '0b79e859ddc2f879ed4e7364b4bba102', '2022-11-05 14:12:51', '2022-11-05 14:31:26', 'nacos', '0:0:0:0:0:0:0:1', '', 'e939d37b-6d2d-4913-932d-d1aba511acad', '', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-authorization-test.yml', 'TEST_GROUP', 'spring:
  redis:
    host: redis-qxw-headless.qixiaowei-stg1
    port: 6379
    password: m071zHwo
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    url: jdbc:mysql://mysql-qxw-headless.qixiaowei-stg1:3306/wk_crm?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
    username: qxwopr
    password: k0igme4F
  cloud:
    sentinel:
      filter:
        enabled: false
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow', '0cd2b83aca55683a684e2932b11718b5', '2022-11-05 14:12:51', '2022-11-05 14:32:17', 'nacos', '0:0:0:0:0:0:0:1', '', 'e939d37b-6d2d-4913-932d-d1aba511acad', '', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-crm-test.yml', 'TEST_GROUP', 'spring:
  redis:
    host: redis-qxw-headless.qixiaowei-stg1
    port: 6379
    password: m071zHwo
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    url: jdbc:mysql://mysql-qxw-headless.qixiaowei-stg1:3306/wk_crm?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
    username: qxwopr
    password: k0igme4F
  elasticsearch:
    rest:
      uris: 127.0.0.1:9200
      username:
      password:
  cloud:
    sentinel:
      filter:
        enabled: false
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow

seata:
  enabled: true
  application-id: ${spring.application.name}
  tx-service-group: ${spring.application.name}_tx_group
  config:
    type: nacos
    nacos:
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      server-addr: ${nacos.server-addr}
      group: SEATA_GROUP
      dataId: seata-server-${spring.profiles.active}.${spring.cloud.nacos.config.file-extension}
      username: ${nacos.username}
      password: ${nacos.password}
  registry:
    type: nacos
    nacos:
      application: seata-server
      group: SEATA_GROUP
      server-addr: ${nacos.server-addr}
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      username: ${nacos.username}
      password: ${nacos.password}

crm:
  enterprise:
    appKey: 21d217b0
    secretKey: 921b1bc0', '707874d788fa7bcb0d3aa0d45f6e43fa', '2022-11-05 14:12:51', '2022-11-05 14:33:31', 'nacos', '0:0:0:0:0:0:0:1', '', 'e939d37b-6d2d-4913-932d-d1aba511acad', '', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-bi-test.yml', 'TEST_GROUP', 'spring:
  redis:
    host: redis-qxw-headless.qixiaowei-stg1
    port: 6379
    password: m071zHwo
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    dynamic:
      primary: default
      druid:
        filters:
        testWhileIdle: true
        validationQuery: select 1
      datasource:
        #默认的bi配置
        default:
          url: jdbc:mysql://mysql-qxw-headless.qixiaowei-stg1:3306/wk_crm?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
          username: qxwopr
          password: k0igme4F
  cloud:
    sentinel:
      filter:
        enabled: true
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow

mybatis-plus:
  mapper-locations:
    - classpath:/mapper/${bi.xmlPath}/*.xml
    - classpath:/extend/mapper/xml/*.xml
bi:
  clickhouse: false
  xmlPath: xml
', '581257fa7871707c287e6866604b9cac', '2022-11-05 14:12:51', '2022-11-05 14:34:47', 'nacos', '0:0:0:0:0:0:0:1', '', 'e939d37b-6d2d-4913-932d-d1aba511acad', '', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-examine-test.yml', 'TEST_GROUP', 'spring:
  redis:
    host: redis-qxw-headless.qixiaowei-stg1
    port: 6379
    password: m071zHwo
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    url: jdbc:mysql://mysql-qxw-headless.qixiaowei-stg1:3306/wk_crm?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
    username: qxwopr
    password: k0igme4F
  cloud:
    sentinel:
      filter:
        enabled: false
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow

seata:
  enabled: true
  application-id: ${spring.application.name}
  tx-service-group: ${spring.application.name}_tx_group
  config:
    type: nacos
    nacos:
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      server-addr: ${nacos.server-addr}
      group: SEATA_GROUP
      dataId: seata-server-${spring.profiles.active}.${spring.cloud.nacos.config.file-extension}
      username: ${nacos.username}
      password: ${nacos.password}
  registry:
    type: nacos
    nacos:
      application: seata-server
      group: SEATA_GROUP
      server-addr: ${nacos.server-addr}
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      username: ${nacos.username}
      password: ${nacos.password}', 'f2154fd348a70f7d65a24ff6a1946cd4', '2022-11-05 14:12:51', '2022-11-05 14:37:33', 'nacos', '0:0:0:0:0:0:0:1', '', 'e939d37b-6d2d-4913-932d-d1aba511acad', '', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-gateway-test.yml', 'TEST_GROUP', 'spring:
  redis:
    host: redis-qxw-headless.qixiaowei-stg1
    port: 6379
    password: m071zHwo
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    url: jdbc:mysql://mysql-qxw-headless.qixiaowei-stg1:3306/qxw_nacos?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
    username: qxwopr
    password: k0igme4F
  resources:
    cache:
      cachecontrol:
        no-cache: true
    static-locations: file:public/,classpath:public/,classpath:/static,classpath:/resources,classpath:/META-INF/resources,file:D:/upload/public
    add-mappings: true
  mvc:
    throw-exception-if-no-handler-found: true
    favicon:
      enabled: false
  servlet:
    multipart:
      max-file-size: 100MB
      max-request-size: 100MB
  cloud:
    sentinel:
      filter:
        enabled: false
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow
    gateway:
      enabled: true
      discovery:
        locator:
          lower-case-service-id: true
          enabled: true

crm:
  gateway:
    ignoreUrl:
      - /authorization/getLoginQrInfo
      - /adminCompanyManager/queryCompanyLogoByDomain
      - /adminPay/notify
      - /adminCompanyManager/verifyEmailInfo
      - /adminUserApply/queryJsonByUserId
      - /adminUserApply/addUserApply
      - /crmMarketing/queryMarketingId
      - /crmMarketing/queryAddField
      - /crmMarketing/saveMarketingInfo
      - /adminUser/queryUserNumInfo
      - /crmCall/upload', 'f91f82145348605f3c82e1c285297a22', '2022-11-05 14:12:51', '2022-11-05 14:38:42', 'nacos', '0:0:0:0:0:0:0:1', '', 'e939d37b-6d2d-4913-932d-d1aba511acad', '', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-oa-test.yml', 'TEST_GROUP', 'spring:
  redis:
    host: redis-qxw-headless.qixiaowei-stg1
    port: 6379
    password: m071zHwo
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    url: jdbc:mysql://mysql-qxw-headless.qixiaowei-stg1:3306/wk_crm?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
    username: qxwopr
    password: k0igme4F
  cloud:
    sentinel:
      filter:
        enabled: false
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow

seata:
  enabled: true
  application-id: ${spring.application.name}
  tx-service-group: ${spring.application.name}_tx_group
  config:
    type: nacos
    nacos:
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      server-addr: ${nacos.server-addr}
      group: SEATA_GROUP
      dataId: seata-server-${spring.profiles.active}.${spring.cloud.nacos.config.file-extension}
      username: ${nacos.username}
      password: ${nacos.password}
  registry:
    type: nacos
    nacos:
      application: seata-server
      group: SEATA_GROUP
      server-addr: ${nacos.server-addr}
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      username: ${nacos.username}
      password: ${nacos.password}
', 'd3ba57316c8eb5becbc8abf40df5fcea', '2022-11-05 14:12:51', '2022-11-05 14:42:39', 'nacos', '0:0:0:0:0:0:0:1', '', 'e939d37b-6d2d-4913-932d-d1aba511acad', '', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-work-test.yml', 'TEST_GROUP', 'spring:
  redis:
    host: redis-qxw-headless.qixiaowei-stg1
    port: 6379
    password: m071zHwo
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    url: jdbc:mysql://mysql-qxw-headless.qixiaowei-stg1:3306/wk_crm?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
    username: qxwopr
    password: k0igme4F
  cloud:
    sentinel:
      filter:
        enabled: false
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow
', 'da28078169fde4d458f68ed4eaead224', '2022-11-05 14:12:51', '2022-11-05 14:43:33', 'nacos', '0:0:0:0:0:0:0:1', '', 'e939d37b-6d2d-4913-932d-d1aba511acad', '', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-core-test.yml', 'TEST_GROUP', 'spring:
  main:
    allow-bean-definition-overriding: true
  mvc:
    throw-exception-if-no-handler-found: true
    favicon:
      enabled: false
    date-format: yyyy-MM-dd HH:mm:ss
  servlet:
    multipart:
      max-file-size: 100MB
      max-request-size: 100MB
      # 上传文件临时目录，需要先创建此目录
      location: /mnt/
  resources:
    add-mappings: false
  jackson:
    dateFormat: yyyy-MM-dd HH:mm:ss
    timeZone: GMT+8
mybatis-plus:
  configuration:
    call-setters-on-nulls: true
  mapper-locations: classpath:/mapper/xml/*.xml

ribbon:
  okhttp:
    enabled: true
feign:
  client:
    config:
      default:
        connect-timeout: 60000
        read-timeout: 60000

  sentinel:
    enabled: true
  httpclient:
    enabled: false
  okhttp:
    enabled: true
jetcache:
  statIntervalMinutes: 0
  areaInCacheName: false
  remote:
    default:
      type: redis
      keyConvertor: fastjson
      poolConfig:
        minIdle: 5
        maxIdle: 20
        maxTotal: 50
      host: ${spring.redis.host}
      port: ${spring.redis.port}
      password: ${spring.redis.password}
      expireAfterWriteInMillis: 1800000

crm:
  enableCache: true
  upload:
    #上传类型 1 本地 2 阿里云OSS 3 腾讯云COS 4 七牛云QNC 5 aws s3
    config: 6
    oss:
      endpoint: http://k8s-stg.qixiaowei.net:30711
      accessKeyId: qxwopr-test
      accessKeySecret: 8Krjs1oL8b
      bucketName:
        0: test
        1: test-public
      publicUrl: http://k8s-stg.qixiaowei.net:30711/test-public/
    config: 1
    local:
      publicUrl: http://192.168.0.5:8443/
      uploadPath:
        0: D:/upload/private
        1: D:/upload/public

', '99c6741363ad1fad690f5cc26979efee', '2022-11-05 14:12:51', '2022-11-11 06:18:31', 'nacos', '0:0:0:0:0:0:0:1', '', 'e939d37b-6d2d-4913-932d-d1aba511acad', '', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-job-test.yml', 'TEST_GROUP', 'xxl:
  job:
    admin:
      # xxl-job admin address list, such as "http://address" or "http://address01,http://address02"
      addresses: http://127.0.0.1:7070/xxl-job-admin
    # xxl-job executor appname
    executor:
      appname: wk-job
      # xxl-job executor registry-address: default use address to registry , otherwise use ip:port if address is null
      address:
      # xxl-job executor server-info
      ip:
      port: 9998
      # xxl-job executor log-path
      logpath: /data/applogs/xxl-job/jobhandler
      # xxl-job executor log-retention-days
      logretentiondays: 30
    # xxl-job, access token
    accessToken: qixiaowei-cloud-job

spring:
  redis:
    host: redis-qxw-headless.qixiaowei-stg1
    port: 6379
    password: m071zHwo
    database: 12
    lettuce:
      pool:
        max-active: 300
', '5a2c5dffc48fc52f18a5cb9e22823957', '2022-11-05 14:12:51', '2022-11-10 08:18:11', 'nacos', '0:0:0:0:0:0:0:1', '', 'e939d37b-6d2d-4913-932d-d1aba511acad', '', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('application-default.yml', 'DEFAULT_GROUP', 'spring:
  autoconfigure:
    exclude: com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure
  mvc:
    pathmatch:
      matching-strategy: ant_path_matcher

mybatis:
# 配置mapper映射忽略大小写
  configuration:
    mapUnderscoreToCamelCase: true


# feign 配置
feign:
  sentinel:
    enabled: false
  okhttp:
    enabled: true
  httpclient:
    enabled: false
  client:
    config:
      default:
        connectTimeout: 10000
        readTimeout: 10000
  compression:
    request:
      enabled: true
    response:
      enabled: true

# 暴露监控端点
management:
  endpoints:
    web:
      exposure:
        include: ''*''

qxw:
  file:
    domain: http://127.0.0.1:9000/local', '23b75d76f245527ecd3e6aa0ec71e4b8', '2022-11-05 15:01:35', '2022-11-16 09:38:11', 'nacos', '0:0:0:0:0:0:0:1', '', '', '通用配置', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-gateway-default.yml', 'DEFAULT_GROUP', 'spring:
  redis:
    host: localhost
    port: 6379
    password: qxw666
  cloud:
    gateway:
      discovery:
        locator:
          lowerCaseServiceId: true
          enabled: true
      routes:
        # 认证中心
        - id: qixiaowei-auth
          uri: lb://qixiaowei-auth
          predicates:
            - Path=/auth/**
          filters:
            # 验证码处理
            - CacheRequestFilter
            - ValidateCodeFilter
            - StripPrefix=1
        # 文件模块
        - id: qixiaowei-file
          uri: lb://qixiaowei-file
          predicates:
            - Path=/file/**
          filters:
            - StripPrefix=1
          # 系统模块
        - id: qixiaowei-system-manage
          uri: lb://qixiaowei-system-manage
          predicates:
            - Path=/system-manage/**
          filters:
            - StripPrefix=1
        # 经营云
        - id: qixiaowei-operate-cloud
          uri: lb://qixiaowei-operate-cloud
          predicates:
            - Path=/operate-cloud/**
          filters:
            - StripPrefix=1

# 安全配置
security:
  # 验证码
  captcha:
    enabled: false
    type: math
  # 防止XSS攻击
  xss:
    enabled: true
    excludeUrls:
      - /system/notice
  # 不校验白名单
  ignore:
    whites:
      - /auth/logout
      - /auth/login
      - /auth/register
      - /*/v2/api-docs
      - /csrf
', '79e94c2816b06a3af88e89392ec18e6c', '2022-11-05 15:01:35', '2022-11-16 09:37:46', 'nacos', '0:0:0:0:0:0:0:1', '', '', '网关模块', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-auth-default.yml', 'DEFAULT_GROUP', 'spring:
  redis:
    host: localhost
    port: 6379
    password: qxw666
', '104349cbc2819f7b0d1affb6dfd8cab7', '2022-11-05 15:01:35', '2022-11-16 09:38:30', 'nacos', '0:0:0:0:0:0:0:1', '', '', '认证中心', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-system-manage-default.yml', 'DEFAULT_GROUP', '# spring配置
spring:
  redis:
    host: localhost
    port: 6379
    password: qxw666
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://127.0.0.1:3306/system_manage?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: root
            password: qxw666


# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.system.manage
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml
', '3a12019612df19c26de78690d8599385', '2022-11-05 15:01:35', '2022-11-16 09:39:55', 'nacos', '0:0:0:0:0:0:0:1', '', '', '系统模块', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-file-default.yml', 'DEFAULT_GROUP', '# spring配置
spring:
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://127.0.0.1:3306/system_manage?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: root
            password: qxw666


# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.file
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml

# 本地文件上传
file:
  domain: http://127.0.0.1:19300
  path: D:/qixiaowei/uploadPath
  prefix: /statics
# Minio配置
minio:
  url: http://127.0.0.1:9000
  accessKey: minioadmin
  secretKey: minioadmin
  bucketName: local', '59fd5a2a1e9659943237697c55128f24', '2022-11-05 15:01:35', '2022-11-16 09:38:59', 'nacos', '0:0:0:0:0:0:0:1', '', '', '文件服务配置', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-job-default.yml', 'DEFAULT_GROUP', 'xxl:
  job:
    admin:
      # xxl-job admin address list, such as "http://address" or "http://address01,http://address02"
      addresses: http://127.0.0.1:7070/xxl-job-admin
    # xxl-job, access token
    accessToken: qixiaowei-cloud-job
    # xxl-job executor appname
    executor:
      appname: qixiaowei-job
      # xxl-job executor registry-address: default use address to registry , otherwise use ip:port if address is null
      address:
      # xxl-job executor server-info
      ip:
      port: 9999
      # xxl-job executor log-path
      logpath: /data/applogs/xxl-job/jobhandler
      # xxl-job executor log-retention-days
      logretentiondays: 30
', 'ebad3752bc4f381d317fb771ef387fa4', '2022-11-05 15:01:35', '2022-11-16 09:39:18', 'nacos', '0:0:0:0:0:0:0:1', '', '', '系统模块', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-message-default.yml', 'DEFAULT_GROUP', '# spring配置
spring:
  redis:
    host: localhost
    port: 6379
    password:
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://localhost:3306/system-manage?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: root
            password: qixiaowei2022


# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.message
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml
', '61466f747fdee6c0461c8ec7d24b82fe', '2022-11-05 15:01:35', '2022-11-05 15:01:35', NULL, '0:0:0:0:0:0:0:1', '', '', '系统模块', NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-operate-cloud-default.yml', 'DEFAULT_GROUP', '# spring配置
spring:
  redis:
    host: localhost
    port: 6379
    password: qxw666
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://127.0.0.1:3306/operate_cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: root
            password: qxw666


# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.operate.cloud
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml
', 'e81532d8690b42f79be604a5c8051934', '2022-11-05 15:01:35', '2022-11-16 09:39:39', 'nacos', '0:0:0:0:0:0:0:1', '', '', '经营模块', '', '', 'yaml', '', NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-sales-cloud-default.yml', 'DEFAULT_GROUP', '# spring配置
spring:
  redis:
    host: localhost
    port: 6379
    password:
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://localhost:3306/sales-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: root
            password: qxw666


# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.sales.cloud
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml
', 'e293a8ccdde4afecfcde5804a3b6d05c', '2022-11-05 15:01:35', '2022-11-05 15:01:35', NULL, '0:0:0:0:0:0:0:1', '', '', '系统模块', NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-strategy-cloud-default.yml', 'DEFAULT_GROUP', '# spring配置
spring:
  redis:
    host: localhost
    port: 6379
    password:
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://localhost:3306/strategy-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: root
            password: qxw666


# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.strategy.cloud
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml
', 'ed7550ddcf4670c2ddd71f903d4d9069', '2022-11-05 15:01:35', '2022-11-05 15:01:35', NULL, '0:0:0:0:0:0:0:1', '', '', '系统模块', NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-talents-cloud-default.yml', 'DEFAULT_GROUP', '# spring配置
spring:
  redis:
    host: localhost
    port: 6379
    password:
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://localhost:3306/talents-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: root
            password: qxw666


# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.talents.cloud
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml
', '5e11ded867dc6929da1e3db87747eb04', '2022-11-05 15:01:35', '2022-11-05 15:01:35', NULL, '0:0:0:0:0:0:0:1', '', '', '系统模块', NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-wisdom-cloud-default.yml', 'DEFAULT_GROUP', '# spring配置
spring:
  redis:
    host: localhost
    port: 6379
    password:
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://localhost:3306/wisdom-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: root
            password: qxw666


# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.wisdom.cloud
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml
', '4a5ce4847080db7cb072754dc867ac7f', '2022-11-05 15:01:35', '2022-11-05 15:01:35', NULL, '0:0:0:0:0:0:0:1', '', '', '系统模块', NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-admin-default.yml', 'DEFAULT_GROUP', 'spring:
  redis:
    host: 127.0.0.1
    port: 6379
    password:
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/wk_crm?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
    username: root
    password: qxw666
  cloud:
    sentinel:
      filter:
        enabled: true
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow

wk:
  captcha:
    waterMark:
    jigsaw: classpath:public


seata:
  enabled: true
  application-id: ${spring.application.name}
  tx-service-group: ${spring.application.name}_tx_group
  config:
    type: nacos
    nacos:
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      server-addr: ${nacos.server-addr}
      group: SEATA_GROUP
      dataId: seata-server-${spring.profiles.active}.${spring.cloud.nacos.config.file-extension}
      username: ${nacos.username}
      password: ${nacos.password}
  registry:
    type: nacos
    nacos:
      application: seata-server
      group: SEATA_GROUP
      server-addr: ${nacos.server-addr}
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      username: ${nacos.username}
      password: ${nacos.password}', 'f33818a54bfe1b47ea5e9bea4cfd2c91', '2022-11-05 15:01:35', '2022-11-05 15:01:35', NULL, '0:0:0:0:0:0:0:1', '', '', '', NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-authorization-default.yml', 'DEFAULT_GROUP', 'spring:
  redis:
    host: 127.0.0.1
    port: 6379
    password: 123456
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/wk_crm?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
    username: root
    password: qxw666
  cloud:
    sentinel:
      filter:
        enabled: false
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow', '1a57c52fa0c326c264a6746e4d50b519', '2022-11-05 15:01:35', '2022-11-05 15:01:35', NULL, '0:0:0:0:0:0:0:1', '', '', '', NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-crm-default.yml', 'DEFAULT_GROUP', 'spring:
  redis:
    host: 127.0.0.1
    port: 6379
    password:
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/wk_crm?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
    username: root
    password: qxw666
  elasticsearch:
    rest:
      uris: 127.0.0.1:9200
      username:
      password:
  cloud:
    sentinel:
      filter:
        enabled: false
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow

seata:
  enabled: true
  application-id: ${spring.application.name}
  tx-service-group: ${spring.application.name}_tx_group
  config:
    type: nacos
    nacos:
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      server-addr: ${nacos.server-addr}
      group: SEATA_GROUP
      dataId: seata-server-${spring.profiles.active}.${spring.cloud.nacos.config.file-extension}
      username: ${nacos.username}
      password: ${nacos.password}
  registry:
    type: nacos
    nacos:
      application: seata-server
      group: SEATA_GROUP
      server-addr: ${nacos.server-addr}
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      username: ${nacos.username}
      password: ${nacos.password}

crm:
  enterprise:
    appKey: 21d217b0
    secretKey: 921b1bc0', '0f68e56faf3618b470b4469edcf97696', '2022-11-05 15:01:35', '2022-11-05 15:01:35', NULL, '0:0:0:0:0:0:0:1', '', '', '', NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-bi-default.yml', 'DEFAULT_GROUP', 'spring:
  redis:
    host: 127.0.0.1
    port: 6379
    password: 123456
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    dynamic:
      primary: default
      druid:
        filters:
        testWhileIdle: true
        validationQuery: select 1
      datasource:
        #默认的bi配置
        default:
          url: jdbc:mysql://127.0.0.1:3306/wk_crm?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
          username: root
          password: qxw666
  cloud:
    sentinel:
      filter:
        enabled: true
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow

mybatis-plus:
  mapper-locations:
    - classpath:/mapper/${bi.xmlPath}/*.xml
    - classpath:/extend/mapper/xml/*.xml
bi:
  clickhouse: false
  xmlPath: xml
', '7a877f90b6e18456bf0f21990c1de8f5', '2022-11-05 15:01:35', '2022-11-05 15:01:35', NULL, '0:0:0:0:0:0:0:1', '', '', '', NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-examine-default.yml', 'DEFAULT_GROUP', 'spring:
  redis:
    host: 127.0.0.1
    port: 6379
    password: 123456
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/wk_crm?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
    username: root
    password: qxw666
  cloud:
    sentinel:
      filter:
        enabled: false
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow

seata:
  enabled: true
  application-id: ${spring.application.name}
  tx-service-group: ${spring.application.name}_tx_group
  config:
    type: nacos
    nacos:
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      server-addr: ${nacos.server-addr}
      group: SEATA_GROUP
      dataId: seata-server-${spring.profiles.active}.${spring.cloud.nacos.config.file-extension}
      username: ${nacos.username}
      password: ${nacos.password}
  registry:
    type: nacos
    nacos:
      application: seata-server
      group: SEATA_GROUP
      server-addr: ${nacos.server-addr}
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      username: ${nacos.username}
      password: ${nacos.password}', '99dbacdabba9166c17541cd80bb1d7a7', '2022-11-05 15:01:35', '2022-11-05 15:01:35', NULL, '0:0:0:0:0:0:0:1', '', '', '', NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-gateway-default.yml', 'DEFAULT_GROUP', 'spring:
  redis:
    host: 127.0.0.1
    port: 6379
    password: 123456
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/qxw_nacos?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
    username: root
    password: qxw666
  resources:
    cache:
      cachecontrol:
        no-cache: true
    static-locations: file:public/,classpath:public/,classpath:/static,classpath:/resources,classpath:/META-INF/resources,file:D:/upload/public
    add-mappings: true
  mvc:
    throw-exception-if-no-handler-found: true
    favicon:
      enabled: false
  servlet:
    multipart:
      max-file-size: 100MB
      max-request-size: 100MB
  cloud:
    sentinel:
      filter:
        enabled: false
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow
    gateway:
      enabled: true
      discovery:
        locator:
          lower-case-service-id: true
          enabled: true

crm:
  gateway:
    ignoreUrl:
      - /authorization/getLoginQrInfo
      - /adminCompanyManager/queryCompanyLogoByDomain
      - /adminPay/notify
      - /adminCompanyManager/verifyEmailInfo
      - /adminUserApply/queryJsonByUserId
      - /adminUserApply/addUserApply
      - /crmMarketing/queryMarketingId
      - /crmMarketing/queryAddField
      - /crmMarketing/saveMarketingInfo
      - /adminUser/queryUserNumInfo
      - /crmCall/upload', '8fd18755b05f4c5f75cd5a11443561b4', '2022-11-05 15:01:35', '2022-11-05 15:01:35', NULL, '0:0:0:0:0:0:0:1', '', '', '', NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-oa-default.yml', 'DEFAULT_GROUP', 'spring:
  redis:
    host: 127.0.0.1
    port: 6379
    password: 123456
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/wk_crm?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
    username: root
    password: qxw666
  cloud:
    sentinel:
      filter:
        enabled: false
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow

seata:
  enabled: true
  application-id: ${spring.application.name}
  tx-service-group: ${spring.application.name}_tx_group
  config:
    type: nacos
    nacos:
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      server-addr: ${nacos.server-addr}
      group: SEATA_GROUP
      dataId: seata-server-${spring.profiles.active}.${spring.cloud.nacos.config.file-extension}
      username: ${nacos.username}
      password: ${nacos.password}
  registry:
    type: nacos
    nacos:
      application: seata-server
      group: SEATA_GROUP
      server-addr: ${nacos.server-addr}
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      username: ${nacos.username}
      password: ${nacos.password}
', '3c3841b528a36461941aeaabf7f92a39', '2022-11-05 15:01:35', '2022-11-05 15:01:35', NULL, '0:0:0:0:0:0:0:1', '', '', '', NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-work-default.yml', 'DEFAULT_GROUP', 'spring:
  redis:
    host: 127.0.0.1
    port: 6379
    password: 123456
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/wk_crm?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
    username: root
    password: qxw666
  cloud:
    sentinel:
      filter:
        enabled: false
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow
', 'b7f5edc176a773ca6cb3f19199343c88', '2022-11-05 15:01:35', '2022-11-05 15:01:35', NULL, '0:0:0:0:0:0:0:1', '', '', '', NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-core-default.yml', 'DEFAULT_GROUP', 'spring:
  main:
    allow-bean-definition-overriding: true
  mvc:
    throw-exception-if-no-handler-found: true
    favicon:
      enabled: false
    date-format: yyyy-MM-dd HH:mm:ss
  servlet:
    multipart:
      max-file-size: 100MB
      max-request-size: 100MB
      # 上传文件临时目录，需要先创建此目录
      location: /mnt/upload/
  resources:
    add-mappings: false
  jackson:
    dateFormat: yyyy-MM-dd HH:mm:ss
    timeZone: GMT+8
mybatis-plus:
  configuration:
    call-setters-on-nulls: true
  mapper-locations: classpath:/mapper/xml/*.xml

ribbon:
  okhttp:
    enabled: true
feign:
  client:
    config:
      default:
        connect-timeout: 60000
        read-timeout: 60000

  sentinel:
    enabled: true
  httpclient:
    enabled: false
  okhttp:
    enabled: true
jetcache:
  statIntervalMinutes: 0
  areaInCacheName: false
  remote:
    default:
      type: redis
      keyConvertor: fastjson
      poolConfig:
        minIdle: 5
        maxIdle: 20
        maxTotal: 50
      host: ${spring.redis.host}
      port: ${spring.redis.port}
      password: ${spring.redis.password}
      expireAfterWriteInMillis: 1800000

crm:
  enableCache: true
  upload:
    #上传类型 1 本地 2 阿里云OSS 3 腾讯云COS 4 七牛云QNC 5 aws s3
    config: 1
    local:
      publicUrl: http://192.168.0.5:8443/
      uploadPath:
        0: D:/upload/private
        1: D:/upload/public

', '64a3c4b6bf393450b08b34ef4e697ced', '2022-11-05 15:01:35', '2022-11-05 15:01:35', NULL, '0:0:0:0:0:0:0:1', '', '', NULL, NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-job-default.yml', 'DEFAULT_GROUP', 'xxl:
  job:
    admin:
      addresses: http://127.0.0.1:7070/xxl-job-admin
    executor:
      appname: wk-job
      ip:
      port: 9998
      logpath: /data/applogs/xxl-job/jobhandler
      logretentiondays: 30
    accessToken:
spring:
  redis:
    host: 127.0.0.1
    port: 6379
    password: 123456
    database: 12
    lettuce:
      pool:
        max-active: 300
', '853da2fbc6fb2f787cb9ef382400081d', '2022-11-05 15:01:35', '2022-11-05 15:01:35', NULL, '0:0:0:0:0:0:0:1', '', '', NULL, NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('application-prod.yml', 'PROD_GROUP', 'spring:
  autoconfigure:
    exclude: com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure
  mvc:
    pathmatch:
      matching-strategy: ant_path_matcher

mybatis:
# 配置mapper映射忽略大小写
  configuration:
    mapUnderscoreToCamelCase: true


# feign 配置
feign:
  sentinel:
    enabled: false
  okhttp:
    enabled: true
  httpclient:
    enabled: false
  client:
    config:
      default:
        connectTimeout: 10000
        readTimeout: 10000
  compression:
    request:
      enabled: true
    response:
      enabled: true

# 暴露监控端点
management:
  endpoints:
    web:
      exposure:
        include: ''*''

qxw:
  file:
    domain: http://k8s-stg.qixiaowei.net:30711/test', '29ff4f56a2710143162bec8fd82ff3d6', '2022-11-17 11:57:03', '2022-11-17 11:57:03', NULL, '0:0:0:0:0:0:0:1', '', '24d4108a-ada6-45d4-8531-54eb53e6afae', NULL, NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-gateway-prod.yml', 'PROD_GROUP', 'spring:
  redis:
    host: redis-qxw-headless.qixiaowei-stg1
    port: 6379
    password: m071zHwo
  cloud:
    gateway:
      discovery:
        locator:
          lowerCaseServiceId: true
          enabled: true
      routes:
        # 认证中心
        - id: qixiaowei-auth
          uri: lb://qixiaowei-auth
          predicates:
            - Path=/auth/**
          filters:
            # 验证码处理
            - CacheRequestFilter
            - ValidateCodeFilter
            - StripPrefix=1
        # 文件模块
        - id: qixiaowei-file
          uri: lb://qixiaowei-file
          predicates:
            - Path=/file/**
          filters:
            - StripPrefix=1
        # 系统模块
        - id: qixiaowei-system-manage
          uri: lb://qixiaowei-system-manage
          predicates:
            - Path=/system-manage/**
          filters:
            - StripPrefix=1
        # 经营云
        - id: qixiaowei-operate-cloud
          uri: lb://qixiaowei-operate-cloud
          predicates:
            - Path=/operate-cloud/**
          filters:
            - StripPrefix=1

# 安全配置
security:
  # 验证码
  captcha:
    enabled: false
    type: math
  # 防止XSS攻击
  xss:
    enabled: true
    excludeUrls:
      - /system/notice
  # 不校验白名单
  ignore:
    whites:
      - /auth/logout
      - /auth/login
      - /auth/register
      - /*/v2/api-docs
      - /csrf
', 'd7ca4f1fa4367347ffed2334eb17a747', '2022-11-17 11:57:03', '2022-11-17 11:57:03', NULL, '0:0:0:0:0:0:0:1', '', '24d4108a-ada6-45d4-8531-54eb53e6afae', NULL, NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-auth-prod.yml', 'PROD_GROUP', 'spring:
  redis:
    host: redis-qxw-headless.qixiaowei-stg1
    port: 6379
    password: m071zHwo
', '5fb8ac74fc16e4620192a04f3a9c9b65', '2022-11-17 11:57:03', '2022-11-17 11:57:03', NULL, '0:0:0:0:0:0:0:1', '', '24d4108a-ada6-45d4-8531-54eb53e6afae', NULL, NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-system-manage-prod.yml', 'PROD_GROUP', '# spring配置
spring:
  redis:
    host: redis-qxw-headless.qixiaowei-stg1
    port: 6379
    password: m071zHwo
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://mysql-qxw-headless.qixiaowei-stg1:3306/system_manage?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: qxwopr
            password: k0igme4F


# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.system.manage
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml
', '0927634694eeeb2f06a0a750b792e0bd', '2022-11-17 11:57:03', '2022-11-17 11:57:03', NULL, '0:0:0:0:0:0:0:1', '', '24d4108a-ada6-45d4-8531-54eb53e6afae', '', NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-file-prod.yml', 'PROD_GROUP', '# spring配置
spring:
  servlet:
    multipart:
      maxFileSize: 20MB
      maxRequestSize: 200MB
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://mysql-qxw-headless.qixiaowei-stg1:3306/system_manage?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: qxwopr
            password: k0igme4F

# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.file
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml

# 本地文件上传
file:
  domain: http://127.0.0.1:19300
  path: D:/qixiaowei/uploadPath
  prefix: /statics
# Minio配置
minio:
  url: http://k8s-stg.qixiaowei.net:30711
  accessKey: qxwopr-test
  secretKey: 8Krjs1oL8b
  bucketName: test', 'd745731ccba45347a4f8ad32973c13e8', '2022-11-17 11:57:03', '2022-11-17 11:57:03', NULL, '0:0:0:0:0:0:0:1', '', '24d4108a-ada6-45d4-8531-54eb53e6afae', '', NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-job-prod.yml', 'PROD_GROUP', 'xxl:
  job:
    admin:
      # xxl-job admin address list, such as "http://address" or "http://address01,http://address02"
      addresses: http://127.0.0.1:7070/xxl-job-admin
    # xxl-job, access token
    accessToken: qixiaowei-cloud-job
    # xxl-job executor appname
    executor:
      appname: qixiaowei-job
      # xxl-job executor registry-address: default use address to registry , otherwise use ip:port if address is null
      address:
      # xxl-job executor server-info
      ip:
      port: 9999
      # xxl-job executor log-path
      logpath: /data/applogs/xxl-job/jobhandler
      # xxl-job executor log-retention-days
      logretentiondays: 30
', 'ebad3752bc4f381d317fb771ef387fa4', '2022-11-17 11:57:03', '2022-11-17 11:57:03', NULL, '0:0:0:0:0:0:0:1', '', '24d4108a-ada6-45d4-8531-54eb53e6afae', '', NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-message-prod.yml', 'PROD_GROUP', '# spring配置
spring:
  redis:
    host: localhost
    port: 6379
    password:
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://localhost:3306/system-manage?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: root
            password: qixiaowei2022


# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.message
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml
', '61466f747fdee6c0461c8ec7d24b82fe', '2022-11-17 11:57:03', '2022-11-17 11:57:03', NULL, '0:0:0:0:0:0:0:1', '', '24d4108a-ada6-45d4-8531-54eb53e6afae', NULL, NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-operate-cloud-prod.yml', 'PROD_GROUP', '# spring配置
spring:
  redis:
    host: redis-qxw-headless.qixiaowei-stg1
    port: 6379
    password: m071zHwo
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://mysql-qxw-headless.qixiaowei-stg1:3306/operate_cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: qxwopr
            password: k0igme4F


# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.operate.cloud
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml
', '817d55459ce51c0a8ee2cb27291c8501', '2022-11-17 11:57:03', '2022-11-17 11:57:03', NULL, '0:0:0:0:0:0:0:1', '', '24d4108a-ada6-45d4-8531-54eb53e6afae', '', NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-sales-cloud-prod.yml', 'PROD_GROUP', '# spring配置
spring:
  redis:
    host: localhost
    port: 6379
    password:
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://localhost:3306/sales-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: root
            password: qixiaowei2022


# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.sales.cloud
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml
', '70adc94704bfdf4fff2e87f945c3c308', '2022-11-17 11:57:03', '2022-11-17 11:57:03', NULL, '0:0:0:0:0:0:0:1', '', '24d4108a-ada6-45d4-8531-54eb53e6afae', NULL, NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-strategy-cloud-prod.yml', 'PROD_GROUP', '# spring配置
spring:
  redis:
    host: localhost
    port: 6379
    password:
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://localhost:3306/strategy-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: root
            password: qixiaowei2022


# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.strategy.cloud
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml
', 'ca724432f4fcd8f89312bbb75bf00641', '2022-11-17 11:57:03', '2022-11-17 11:57:03', NULL, '0:0:0:0:0:0:0:1', '', '24d4108a-ada6-45d4-8531-54eb53e6afae', NULL, NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-talents-cloud-prod.yml', 'PROD_GROUP', '# spring配置
spring:
  redis:
    host: localhost
    port: 6379
    password:
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://localhost:3306/talents-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: root
            password: qixiaowei2022


# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.talents.cloud
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml
', '9e1c44a646b2f429bd9bc91e7b68af8d', '2022-11-17 11:57:03', '2022-11-17 11:57:03', NULL, '0:0:0:0:0:0:0:1', '', '24d4108a-ada6-45d4-8531-54eb53e6afae', NULL, NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('qixiaowei-wisdom-cloud-prod.yml', 'PROD_GROUP', '# spring配置
spring:
  redis:
    host: localhost
    port: 6379
    password:
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://localhost:3306/wisdom-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: root
            password: qixiaowei2022


# mybatis配置
mybatis:
    # 搜索指定包别名
  typeAliasesPackage: net.qixiaowei.wisdom.cloud
    # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml
', 'bf7fc032014cd7db69dcb77e9864a553', '2022-11-17 11:57:03', '2022-11-17 11:57:03', NULL, '0:0:0:0:0:0:0:1', '', '24d4108a-ada6-45d4-8531-54eb53e6afae', NULL, NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-admin-prod.yml', 'PROD_GROUP', 'spring:
  redis:
    host: redis-qxw-headless.qixiaowei-stg1
    port: 6379
    password: m071zHwo
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    url: jdbc:mysql://mysql-qxw-headless.qixiaowei-stg1:3306/wk_crm?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
    username: qxwopr
    password: k0igme4F
  cloud:
    sentinel:
      filter:
        enabled: true
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow

wk:
  captcha:
    waterMark:
    jigsaw: classpath:public


seata:
  enabled: true
  application-id: ${spring.application.name}
  tx-service-group: ${spring.application.name}_tx_group
  config:
    type: nacos
    nacos:
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      server-addr: ${nacos.server-addr}
      group: SEATA_GROUP
      dataId: seata-server-${spring.profiles.active}.${spring.cloud.nacos.config.file-extension}
      username: ${nacos.username}
      password: ${nacos.password}
  registry:
    type: nacos
    nacos:
      application: seata-server
      group: SEATA_GROUP
      server-addr: ${nacos.server-addr}
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      username: ${nacos.username}
      password: ${nacos.password}', '0b79e859ddc2f879ed4e7364b4bba102', '2022-11-17 11:57:03', '2022-11-17 11:57:03', NULL, '0:0:0:0:0:0:0:1', '', '24d4108a-ada6-45d4-8531-54eb53e6afae', '', NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-authorization-prod.yml', 'PROD_GROUP', 'spring:
  redis:
    host: redis-qxw-headless.qixiaowei-stg1
    port: 6379
    password: m071zHwo
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    url: jdbc:mysql://mysql-qxw-headless.qixiaowei-stg1:3306/wk_crm?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
    username: qxwopr
    password: k0igme4F
  cloud:
    sentinel:
      filter:
        enabled: false
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow', '0cd2b83aca55683a684e2932b11718b5', '2022-11-17 11:57:03', '2022-11-17 11:57:03', NULL, '0:0:0:0:0:0:0:1', '', '24d4108a-ada6-45d4-8531-54eb53e6afae', '', NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-crm-prod.yml', 'PROD_GROUP', 'spring:
  redis:
    host: redis-qxw-headless.qixiaowei-stg1
    port: 6379
    password: m071zHwo
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    url: jdbc:mysql://mysql-qxw-headless.qixiaowei-stg1:3306/wk_crm?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
    username: qxwopr
    password: k0igme4F
  elasticsearch:
    rest:
      uris: 127.0.0.1:9200
      username:
      password:
  cloud:
    sentinel:
      filter:
        enabled: false
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow

seata:
  enabled: true
  application-id: ${spring.application.name}
  tx-service-group: ${spring.application.name}_tx_group
  config:
    type: nacos
    nacos:
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      server-addr: ${nacos.server-addr}
      group: SEATA_GROUP
      dataId: seata-server-${spring.profiles.active}.${spring.cloud.nacos.config.file-extension}
      username: ${nacos.username}
      password: ${nacos.password}
  registry:
    type: nacos
    nacos:
      application: seata-server
      group: SEATA_GROUP
      server-addr: ${nacos.server-addr}
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      username: ${nacos.username}
      password: ${nacos.password}

crm:
  enterprise:
    appKey: 21d217b0
    secretKey: 921b1bc0', '707874d788fa7bcb0d3aa0d45f6e43fa', '2022-11-17 11:57:03', '2022-11-17 11:57:03', NULL, '0:0:0:0:0:0:0:1', '', '24d4108a-ada6-45d4-8531-54eb53e6afae', '', NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-bi-prod.yml', 'PROD_GROUP', 'spring:
  redis:
    host: redis-qxw-headless.qixiaowei-stg1
    port: 6379
    password: m071zHwo
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    dynamic:
      primary: default
      druid:
        filters:
        testWhileIdle: true
        validationQuery: select 1
      datasource:
        #默认的bi配置
        default:
          url: jdbc:mysql://mysql-qxw-headless.qixiaowei-stg1:3306/wk_crm?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
          username: qxwopr
          password: k0igme4F
  cloud:
    sentinel:
      filter:
        enabled: true
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow

mybatis-plus:
  mapper-locations:
    - classpath:/mapper/${bi.xmlPath}/*.xml
    - classpath:/extend/mapper/xml/*.xml
bi:
  clickhouse: false
  xmlPath: xml
', '581257fa7871707c287e6866604b9cac', '2022-11-17 11:57:03', '2022-11-17 11:57:03', NULL, '0:0:0:0:0:0:0:1', '', '24d4108a-ada6-45d4-8531-54eb53e6afae', '', NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-examine-prod.yml', 'PROD_GROUP', 'spring:
  redis:
    host: redis-qxw-headless.qixiaowei-stg1
    port: 6379
    password: m071zHwo
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    url: jdbc:mysql://mysql-qxw-headless.qixiaowei-stg1:3306/wk_crm?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
    username: qxwopr
    password: k0igme4F
  cloud:
    sentinel:
      filter:
        enabled: false
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow

seata:
  enabled: true
  application-id: ${spring.application.name}
  tx-service-group: ${spring.application.name}_tx_group
  config:
    type: nacos
    nacos:
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      server-addr: ${nacos.server-addr}
      group: SEATA_GROUP
      dataId: seata-server-${spring.profiles.active}.${spring.cloud.nacos.config.file-extension}
      username: ${nacos.username}
      password: ${nacos.password}
  registry:
    type: nacos
    nacos:
      application: seata-server
      group: SEATA_GROUP
      server-addr: ${nacos.server-addr}
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      username: ${nacos.username}
      password: ${nacos.password}', 'f2154fd348a70f7d65a24ff6a1946cd4', '2022-11-17 11:57:03', '2022-11-17 11:57:03', NULL, '0:0:0:0:0:0:0:1', '', '24d4108a-ada6-45d4-8531-54eb53e6afae', '', NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-gateway-prod.yml', 'PROD_GROUP', 'spring:
  redis:
    host: redis-qxw-headless.qixiaowei-stg1
    port: 6379
    password: m071zHwo
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    url: jdbc:mysql://mysql-qxw-headless.qixiaowei-stg1:3306/qxw_nacos?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
    username: qxwopr
    password: k0igme4F
  resources:
    cache:
      cachecontrol:
        no-cache: true
    static-locations: file:public/,classpath:public/,classpath:/static,classpath:/resources,classpath:/META-INF/resources,file:D:/upload/public
    add-mappings: true
  mvc:
    throw-exception-if-no-handler-found: true
    favicon:
      enabled: false
  servlet:
    multipart:
      max-file-size: 100MB
      max-request-size: 100MB
  cloud:
    sentinel:
      filter:
        enabled: false
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow
    gateway:
      enabled: true
      discovery:
        locator:
          lower-case-service-id: true
          enabled: true

crm:
  gateway:
    ignoreUrl:
      - /authorization/getLoginQrInfo
      - /adminCompanyManager/queryCompanyLogoByDomain
      - /adminPay/notify
      - /adminCompanyManager/verifyEmailInfo
      - /adminUserApply/queryJsonByUserId
      - /adminUserApply/addUserApply
      - /crmMarketing/queryMarketingId
      - /crmMarketing/queryAddField
      - /crmMarketing/saveMarketingInfo
      - /adminUser/queryUserNumInfo
      - /crmCall/upload', 'f91f82145348605f3c82e1c285297a22', '2022-11-17 11:57:03', '2022-11-17 11:57:03', NULL, '0:0:0:0:0:0:0:1', '', '24d4108a-ada6-45d4-8531-54eb53e6afae', '', NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-oa-prod.yml', 'PROD_GROUP', 'spring:
  redis:
    host: redis-qxw-headless.qixiaowei-stg1
    port: 6379
    password: m071zHwo
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    url: jdbc:mysql://mysql-qxw-headless.qixiaowei-stg1:3306/wk_crm?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
    username: qxwopr
    password: k0igme4F
  cloud:
    sentinel:
      filter:
        enabled: false
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow

seata:
  enabled: true
  application-id: ${spring.application.name}
  tx-service-group: ${spring.application.name}_tx_group
  config:
    type: nacos
    nacos:
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      server-addr: ${nacos.server-addr}
      group: SEATA_GROUP
      dataId: seata-server-${spring.profiles.active}.${spring.cloud.nacos.config.file-extension}
      username: ${nacos.username}
      password: ${nacos.password}
  registry:
    type: nacos
    nacos:
      application: seata-server
      group: SEATA_GROUP
      server-addr: ${nacos.server-addr}
      namespace: 63afa625-4e97-4727-b835-2398a4e76051
      username: ${nacos.username}
      password: ${nacos.password}
', 'd3ba57316c8eb5becbc8abf40df5fcea', '2022-11-17 11:57:03', '2022-11-17 11:57:03', NULL, '0:0:0:0:0:0:0:1', '', '24d4108a-ada6-45d4-8531-54eb53e6afae', '', NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-work-prod.yml', 'PROD_GROUP', 'spring:
  redis:
    host: redis-qxw-headless.qixiaowei-stg1
    port: 6379
    password: m071zHwo
    database: 12
    lettuce:
      pool:
        max-active: 300
  datasource:
    url: jdbc:mysql://mysql-qxw-headless.qixiaowei-stg1:3306/wk_crm?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
    username: qxwopr
    password: k0igme4F
  cloud:
    sentinel:
      filter:
        enabled: false
      transport:
        dashboard: 127.0.0.1:8079
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow
', 'da28078169fde4d458f68ed4eaead224', '2022-11-17 11:57:03', '2022-11-17 11:57:03', NULL, '0:0:0:0:0:0:0:1', '', '24d4108a-ada6-45d4-8531-54eb53e6afae', '', NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-core-prod.yml', 'PROD_GROUP', 'spring:
  main:
    allow-bean-definition-overriding: true
  mvc:
    throw-exception-if-no-handler-found: true
    favicon:
      enabled: false
    date-format: yyyy-MM-dd HH:mm:ss
  servlet:
    multipart:
      max-file-size: 100MB
      max-request-size: 100MB
      # 上传文件临时目录，需要先创建此目录
      location: /mnt/
  resources:
    add-mappings: false
  jackson:
    dateFormat: yyyy-MM-dd HH:mm:ss
    timeZone: GMT+8
mybatis-plus:
  configuration:
    call-setters-on-nulls: true
  mapper-locations: classpath:/mapper/xml/*.xml

ribbon:
  okhttp:
    enabled: true
feign:
  client:
    config:
      default:
        connect-timeout: 60000
        read-timeout: 60000

  sentinel:
    enabled: true
  httpclient:
    enabled: false
  okhttp:
    enabled: true
jetcache:
  statIntervalMinutes: 0
  areaInCacheName: false
  remote:
    default:
      type: redis
      keyConvertor: fastjson
      poolConfig:
        minIdle: 5
        maxIdle: 20
        maxTotal: 50
      host: ${spring.redis.host}
      port: ${spring.redis.port}
      password: ${spring.redis.password}
      expireAfterWriteInMillis: 1800000

crm:
  enableCache: true
  upload:
    #上传类型 1 本地 2 阿里云OSS 3 腾讯云COS 4 七牛云QNC 5 aws s3
    config: 6
    oss:
      endpoint: http://k8s-stg.qixiaowei.net:30711
      accessKeyId: qxwopr-test
      accessKeySecret: 8Krjs1oL8b
      bucketName:
        0: test
        1: test-public
      publicUrl: http://k8s-stg.qixiaowei.net:30711/test-public/
    config: 1
    local:
      publicUrl: http://192.168.0.5:8443/
      uploadPath:
        0: D:/upload/private
        1: D:/upload/public

', '99c6741363ad1fad690f5cc26979efee', '2022-11-17 11:57:03', '2022-11-17 11:57:03', NULL, '0:0:0:0:0:0:0:1', '', '24d4108a-ada6-45d4-8531-54eb53e6afae', '', NULL, NULL, 'yaml', NULL, NULL);
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES('wk-job-prod.yml', 'PROD_GROUP', 'xxl:
  job:
    admin:
      # xxl-job admin address list, such as "http://address" or "http://address01,http://address02"
      addresses: http://127.0.0.1:7070/xxl-job-admin
    # xxl-job executor appname
    executor:
      appname: wk-job
      # xxl-job executor registry-address: default use address to registry , otherwise use ip:port if address is null
      address:
      # xxl-job executor server-info
      ip:
      port: 9998
      # xxl-job executor log-path
      logpath: /data/applogs/xxl-job/jobhandler
      # xxl-job executor log-retention-days
      logretentiondays: 30
    # xxl-job, access token
    accessToken: qixiaowei-cloud-job

spring:
  redis:
    host: redis-qxw-headless.qixiaowei-stg1
    port: 6379
    password: m071zHwo
    database: 12
    lettuce:
      pool:
        max-active: 300
', '5a2c5dffc48fc52f18a5cb9e22823957', '2022-11-17 11:57:03', '2022-11-17 11:57:03', NULL, '0:0:0:0:0:0:0:1', '', '24d4108a-ada6-45d4-8531-54eb53e6afae', '', NULL, NULL, 'yaml', NULL, NULL);


-- ----------------------------
-- init of config_info_route
-- ----------------------------
INSERT INTO config_info_route (route_id, uri, predicates, filters, orders, description, intercept, status, create_time, update_time) VALUES('authorization', 'lb://wk-authorization', '[{"name":"Path","args":{"pattern":"/authorization*/**"}}]', '[]', 100, '用户认证相关接口', 0, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO config_info_route (route_id, uri, predicates, filters, orders, description, intercept, status, create_time, update_time) VALUES('admin', 'lb://wk-admin', '[{"name":"Path","args":{"pattern":"/admin*/**"}}]', '[]', 100, '系统管理相关接口', 1, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO config_info_route (route_id, uri, predicates, filters, orders, description, intercept, status, create_time, update_time) VALUES('login', 'lb://wk-authorization/login', '[{"name":"Path","args":{"pattern":"/login"}}]', '[]', 100, '用户登录相关接口', 0, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO config_info_route (route_id, uri, predicates, filters, orders, description, intercept, status, create_time, update_time) VALUES('logout', 'lb://wk-authorization/logout', '[{"name":"Path","args":{"pattern":"/logout"}}]', '[]', 100, '用户退出相关接口', 0, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO config_info_route (route_id, uri, predicates, filters, orders, description, intercept, status, create_time, update_time) VALUES('reLogin', 'lb://wk-authorization/reLogin', '[{"name":"Path","args":{"pattern":"/reLogin"}}]', '[]', 100, '用户重新登录相关接口', 0, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO config_info_route (route_id, uri, predicates, filters, orders, description, intercept, status, create_time, update_time) VALUES('crm', 'lb://wk-crm', '[{"name":"Path","args":{"pattern":"/crm*/**"}}]', '[]', 100, '客户管理相关接口', 1, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO config_info_route (route_id, uri, predicates, filters, orders, description, intercept, status, create_time, update_time) VALUES('hrm', 'lb://wk-hrm', '[{"name":"Path","args":{"pattern":"/hrm*/**"}}]', '[]', 100, '人力资源相关接口', 1, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO config_info_route (route_id, uri, predicates, filters, orders, description, intercept, status, create_time, update_time) VALUES('jxc', 'lb://wk-jxc', '[{"name":"Path","args":{"pattern":"/jxc*/**"}}]', '[]', 100, '进销存相关接口', 1, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO config_info_route (route_id, uri, predicates, filters, orders, description, intercept, status, create_time, update_time) VALUES('work', 'lb://wk-work', '[{"name":"Path","args":{"pattern":"/work*/**"}}]', '[]', 100, '项目管理相关接口', 1, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO config_info_route (route_id, uri, predicates, filters, orders, description, intercept, status, create_time, update_time) VALUES('oa', 'lb://wk-oa', '[{"name":"Path","args":{"pattern":"/oa*/**"}}]', '[]', 100, 'OA相关接口', 1, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO config_info_route (route_id, uri, predicates, filters, orders, description, intercept, status, create_time, update_time) VALUES('email', 'lb://wk-email', '[{"name":"Path","args":{"pattern":"/email*/**"}}]', '[]', 100, '邮箱相关接口', 1, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO config_info_route (route_id, uri, predicates, filters, orders, description, intercept, status, create_time, update_time) VALUES('km', 'lb://wk-km', '[{"name":"Path","args":{"pattern":"/km*/**"}}]', '[]', 100, '知识库相关接口', 1, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO config_info_route (route_id, uri, predicates, filters, orders, description, intercept, status, create_time, update_time) VALUES('bi', 'lb://wk-bi', '[{"name":"Path","args":{"pattern":"/bi*/**"}}]', '[]', 100, '商业智能相关接口', 1, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO config_info_route (route_id, uri, predicates, filters, orders, description, intercept, status, create_time, update_time) VALUES('queryShareUrl', 'lb://wk-km', '[{"name":"Path","args":{"pattern":"/documentShare/queryShareUrl/*"}}]', '[]', 100, '知识库分享接口', 0, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO config_info_route (route_id, uri, predicates, filters, orders, description, intercept, status, create_time, update_time) VALUES('crmCallUpload', 'lb://wk-crm/crmCall/upload', '[{"name":"Path","args":{"pattern":"/crmCall/upload"}}]', '[]', 98, '呼叫中心上传接口', 0, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO config_info_route (route_id, uri, predicates, filters, orders, description, intercept, status, create_time, update_time) VALUES('cloud', 'lb://wk-admin/cloud/', '[{"name":"Path","args":{"pattern":"/cloud/**"}}]', '[]', 100, '云平台的一些接口', 0, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO config_info_route (route_id, uri, predicates, filters, orders, description, intercept, status, create_time, update_time) VALUES('permission', 'lb://wk-authorization/permission', '[{"name":"Path","args":{"pattern":"/permission"}}]', '[]', 100, '用户权限验证接口', 0, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO config_info_route (route_id, uri, predicates, filters, orders, description, intercept, status, create_time, update_time) VALUES('examine', 'lb://wk-examine', '[{"name":"Path","args":{"pattern":"/examine*/**"}}]', '[]', 100, '审批相关接口', 1, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO config_info_route (route_id, uri, predicates, filters, orders, description, intercept, status, create_time, update_time) VALUES('scrm', 'lb://wk-scrm', '[{"name":"Path","args":{"pattern":"/scrm*/**"}}]', '[]', 100, 'scrm相关接口', 1, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO config_info_route (route_id, uri, predicates, filters, orders, description, intercept, status, create_time, update_time) VALUES('wxLogin', 'lb://wk-authorization/wxLogin', '[{"name":"Path","args":{"pattern":"/wxLogin"}}]', '[]', 100, '用户登录相关接口(企业微信侧边栏登录)', 0, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO config_info_route (route_id, uri, predicates, filters, orders, description, intercept, status, create_time, update_time) VALUES('module', 'lb://wk-module', '[{"name":"Path","args":{"pattern":"/module*/**"}}]', '[]', 100, '低代码', 1, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO config_info_route (route_id, uri, predicates, filters, orders, description, intercept, status, create_time, update_time) VALUES('crmCallUpload', 'lb://wk-crm/crmCall/pushInfo', '[{"name":"Path","args":{"pattern":"/crmCall/pushInfo"}}]', '[]', 99, '呼叫中心第三方接口', 0, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO config_info_route (route_id, uri, predicates, filters, orders, description, intercept, status, create_time, update_time) VALUES('finance', 'lb://wk-finance', '[{"name":"Path","args":{"pattern":"/finance*/**"}}]', '[]', 100, '财务系统相关接口', 1, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');


commit;