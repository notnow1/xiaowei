package net.qixiaowei.gen.main;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.sun.istack.internal.NotNull;
import net.qixiaowei.gen.main.utils.ExcelDiskUtils;


import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CodeGenerator {

    private static final String projectPath = System.getProperties().getProperty("user.dir");
    //项目名称
    private static final String module_name = "system-manage" ;
    //项目路径
    private static final String module_path = "system/manage" ;
    //包名 不加为默认值 如不加service创建类 加为service.tenant创建类
    private static final String extend_Package = "/user" ;
    //表名
    private static final String tables = "user" ;
    //生成文件的作者名
    private static final String author ="author" ;
    //数据库配置
    private static final String url = "jdbc:mysql://db-dev.qixiaowei.net:31194/system-manage" ;
    private static final String username = "qxwopr" ;
    private static final String password = "7fpJR7i2" ;


    //实体类 默认生成 不生成改为false
    private static final boolean default_entity = true;
    //DTO类
    private static final boolean default_DTO = true;
    //DTO类
    private static final boolean default_controller = true;
    //service类
    private static final boolean default_service = true;
    //service实现类
    private static final boolean default_serviceImpl = true;
    //mapper类
    private static final boolean default_mapper = true;
    //mapperxml类
    private static final boolean default_mapper_xml = true;


    private static final String api_default = "/qixiaowei-service-api" ;
    private static final String service_default = "/qixiaowei-service" ;
    private static final String common_default_path = "/src/main/java/net/qixiaowei/" ;
    //导入包名 如net.qixiaowei.system
    private static final String packagePath = "net/qixiaowei/" + module_path;
    private static final String qixiaowei_name = "/qixiaowei-" ;

    // 实体类 出参 入参 输出目录 因固定实体类和dto生成路径 不需要修改！！！！
    private static final String entityAndDtoPath = projectPath + api_default + qixiaowei_name + module_name + "-api" + common_default_path + module_path + "/api" ;


    //controller输出目录
    private static final String generatePath = projectPath + service_default + qixiaowei_name + module_name + common_default_path + module_path;

    //实体类package
    private static final String entityPackage = "domain" + extend_Package;
    //dto package
    private static final String dtoPackage = "dto" + extend_Package;
    //dto controller
    private static final String controllerPackage = "controller" + extend_Package;
    //service package
    private static final String servicePackage = "service" + extend_Package;
    //serviceImpl package
    private static final String serviceImplPackage = "service/impl" + extend_Package;
    //mapper package
    private static final String mapperPackage = "mapper" + extend_Package;
    private static final String mapperXmlPackage = "mapper" +"/"+module_path+extend_Package;
    //mapperXml路径
    private static final String mapperXmlPath = projectPath + service_default + qixiaowei_name + module_name + "/src/main/resources" ;


    public static void main(String[] args) {
        if (StringUtils.isNotBlank(tables)){
            System.out.println("你确定要生成:"+tables+"吗？"+"Y:生成 N:不生成");
            Scanner scanner = new Scanner(System.in);
            String generateFalg = scanner.nextLine();
            if (generateFalg.equals("N")){
                return;
            }
        }else {
            System.out.println("请输入表名!!!");
            return;
        }

        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author(author) // 设置作者
                            .outputDir(generatePath); // 指定输出目录
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tables) // 设置需要生成的表名
                            //Entity 策略配置
                            .entityBuilder()
                            .enableLombok() //开启lombok
                            .enableTableFieldAnnotation();//开启生成实体时生成字段注解 默认值:false（加上这个配置生成的实体字段会有 @TableField()注解）
                })
                .templateEngine(new EnhanceFreemarkerTemplateEngine())
                .execute();

        System.gc();    //回收资源
        //创建一个文件对象
        File file = new File(generatePath + "//" + "com");
        // todo 基于mybatis plus代码生成二次开发 会自带生成他的模板 生成自定义模板后 删除他的模板
        boolean b = ExcelDiskUtils.deleteFiles(file);
        System.out.println(b);

    }


    /**
     * 代码生成器支持自定义模版
     */
    public final static class EnhanceFreemarkerTemplateEngine extends FreemarkerTemplateEngine {

        @Override
        protected void outputCustomFile(@NotNull Map<String, String> customFile, @NotNull TableInfo tableInfo, @NotNull Map<String, Object> objectMap) {
            String substring = packagePath.substring(packagePath.lastIndexOf("/")).replaceAll("/", "");
            //对包名做处理
            String s = packagePath.replaceAll("/", ".");
            //表名
            String entityName = tableInfo.getEntityName();
            //自定义模板参数（定义的这些参数可以在我们的Freemark模板中使用&{}语法取到该值，比如 &{requestMapping}）
            objectMap.put("requestMapping", StringUtils.firstToLowerCase(entityName));
            objectMap.put("entityPackage", s + ".api." + entityPackage.replaceAll("/", "."));
            objectMap.put("packageName", s);
            objectMap.put("dtoPackage", s + ".api." + dtoPackage.replaceAll("/", "."));
            objectMap.put("controllerPackage", s + "." + controllerPackage.replaceAll("/", "."));
            objectMap.put("servicePackage", s + "." + servicePackage.replaceAll("/", "."));
            objectMap.put("servicePackageImpl", s + "." + serviceImplPackage.replaceAll("/", "."));
            objectMap.put("mapperPackage", s + "." + mapperPackage.replaceAll("/", "."));
            objectMap.put("mapperXmlPackage", s + "." + mapperXmlPackage.replaceAll("/", "."));
            objectMap.put("facadePackage", s + tableInfo.getEntityName().toLowerCase() + ".facade");
            objectMap.put("facadeImplPackage", s + tableInfo.getEntityName().toLowerCase() + ".facade.impl");
            objectMap.put("requestPackage", s + tableInfo.getEntityName().toLowerCase() + ".request");
            objectMap.put("responsePackage", s + tableInfo.getEntityName().toLowerCase() + ".response");
            objectMap.put("leftBrace", "{");
            objectMap.put("rightBrace", "}");
            //@RequiresPermissions("") 注解所需参数
            objectMap.put("packageClass", module_path.replaceAll("/", ":"));

            //设置要生成的文件名以及FreeMark模板文件路径
            customFile = new HashMap<>();
            if (default_entity) {
                // 实体类
                customFile.put(entityName + StringPool.DOT_JAVA, "/template/domain.java.ftl");
            }
            if (default_DTO) {          // dto类
                customFile.put(entityName + "DTO" + StringPool.DOT_JAVA, "/template/dto.java.ftl");
            }
            if (default_controller) {            // Controller类
                customFile.put(entityName + "Controller" + StringPool.DOT_JAVA, "/template/controller.java.ftl");
            }
            if (default_service) {          // Service接口类
                customFile.put("I" + entityName + "Service" + StringPool.DOT_JAVA, "/template/service.java.ftl");
            }
            if (default_serviceImpl) {         // Service实现类
                customFile.put(entityName + "ServiceImpl" + StringPool.DOT_JAVA, "/template/serviceImpl.java.ftl");
            }
            if (default_mapper) {
                // mapper类
                customFile.put(entityName + "Mapper" + StringPool.DOT_JAVA, "/template/mapper.java.ftl");
            }
            if (default_mapper_xml) {
                // mapperXml类
                customFile.put(entityName + "Mapper" + StringPool.DOT_XML, "/template/mapper.xml.ftl");
            }


            customFile.forEach((key, value) -> {
                String fileName = null;
                if (StringUtils.equals(key, entityName + StringPool.DOT_JAVA)) {
                    fileName = entityAndDtoPath + "/" + entityPackage + "/" + key;
                    //如果存在就删除
                    ExcelDiskUtils.deleteFile2(new File(fileName));
                }//
                else if (StringUtils.equals(key, entityName + "Controller" + StringPool.DOT_JAVA)) {
                    fileName = generatePath + "/" + controllerPackage + "/" + key;
                    //如果存在就删除
                    ExcelDiskUtils.deleteFile2(new File(fileName));
                }//
                else if (StringUtils.equals(key, entityName + "DTO" + StringPool.DOT_JAVA)) {
                    fileName = entityAndDtoPath + "/" + dtoPackage + "/" + key;
                    //如果存在就删除
                    ExcelDiskUtils.deleteFile2(new File(fileName));
                }//
                else if (StringUtils.equals(key, "I" + entityName + "Service" + StringPool.DOT_JAVA)) {
                    fileName = generatePath + "/" + servicePackage + "/" + key;
                    //如果存在就删除
                    ExcelDiskUtils.deleteFile2(new File(fileName));
                }//
                else if (StringUtils.equals(key, entityName + "ServiceImpl" + StringPool.DOT_JAVA)) {
                    fileName = generatePath + "/" + serviceImplPackage + "/" + key;
                    //如果存在就删除
                    ExcelDiskUtils.deleteFile2(new File(fileName));
                }//
                else if (StringUtils.equals(key, entityName + "Mapper" + StringPool.DOT_JAVA)) {
                    fileName = generatePath + "/" + mapperPackage + "/" + key;
                    //如果存在就删除
                    ExcelDiskUtils.deleteFile2(new File(fileName));
                } else if (StringUtils.equals(key, entityName + "Mapper" + StringPool.DOT_XML)) {
                    fileName = mapperXmlPath + "/" + mapperXmlPackage + "/" + key;
                    //如果存在就删除
                    ExcelDiskUtils.deleteFile2(new File(fileName));
                } else {
                    fileName = generatePath + "/" + entityName + "/" + key;
                }
                this.outputFile(new File(fileName), objectMap, value, this.getConfigBuilder().getInjectionConfig().isFileOverride());
            });


        }
    }


}
