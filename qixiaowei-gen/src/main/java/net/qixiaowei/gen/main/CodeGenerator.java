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

public class CodeGenerator {

    //数据库配置
    private static final String url = "jdbc:mysql://127.0.0.1:3306/test" ;
    private static final String username = "root" ;
    private static final String password = "123456" ;
    //表名
    private static final String tables = "apply_url,avail_data_source" ;
    //输出目录
    private static final String generatePath = "D://projectstest//qixiaowei-cloud-backend//qixiaowei-modules//qixiaowei-system//src//main//java//net//qixiaowei//system" ;
    //导入包名 如net.qixiaowei.system
    private static final String packagePath = "net//qixiaowei//system" ;

    public static void main(String[] args) {
        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author("tangdagui") // 设置作者
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
            String substring = packagePath.substring(packagePath.lastIndexOf("//")).replaceAll("//","");
            //对包名做处理
            String s = packagePath.replaceAll("//", ".");
            //表名
            String entityName = tableInfo.getEntityName();
            //自定义模板参数（定义的这些参数可以在我们的Freemark模板中使用&{}语法取到该值，比如 &{requestMapping}）
            objectMap.put("requestMapping", StringUtils.firstToLowerCase(entityName));
            objectMap.put("entityPackage", s + ".domain");
            objectMap.put("packageName", s);
            objectMap.put("daoPackage", s + ".dao");
            objectMap.put("controllerPackage", s + ".controller");
            objectMap.put("servicePackage", s + ".service");
            objectMap.put("servicePackageImpl", s + ".service.impl");
            objectMap.put("mapperPackage", s + ".mapper");
            objectMap.put("facadePackage", s + tableInfo.getEntityName().toLowerCase() + ".facade");
            objectMap.put("facadeImplPackage", s + tableInfo.getEntityName().toLowerCase() + ".facade.impl");
            objectMap.put("requestPackage", s + tableInfo.getEntityName().toLowerCase() + ".request");
            objectMap.put("responsePackage", s + tableInfo.getEntityName().toLowerCase() + ".response");
            objectMap.put("leftBrace", "{");
            objectMap.put("rightBrace", "}");
            //@RequiresPermissions("") 注解所需参数
            objectMap.put("packageClass",substring);

            //设置要生成的文件名以及FreeMark模板文件路径
            customFile = new HashMap<>();
            // todo 不想生成那个注释那个
            // 实体类
            customFile.put(entityName + StringPool.DOT_JAVA, "/template/domain.java.ftl");
            // Controller类
            customFile.put(entityName + "Controller" + StringPool.DOT_JAVA, "/template/controller.java.ftl");
            // Dao类
            //  customFile.put(entityName + "Dao" + StringPool.DOT_JAVA, "/template/dao.java.ftl");
            // Service接口类
            customFile.put("I" + entityName + "Service" + StringPool.DOT_JAVA, "/template/service.java.ftl");
            // Service实现类
            customFile.put(entityName + "ServiceImpl" + StringPool.DOT_JAVA, "/template/serviceImpl.java.ftl");
            // mapper类
            customFile.put(entityName + "Mapper" + StringPool.DOT_JAVA, "/template/mapper.java.ftl");
            // mapperXml类
            customFile.put(entityName + "Mapper" + StringPool.DOT_XML, "/template/mapper.xml.ftl");

            customFile.forEach((key, value) -> {
                String fileName = null;
                if (StringUtils.equals(key, entityName + StringPool.DOT_JAVA)) {
                    fileName = generatePath + "/" + "domain" + "/" + key;
                    //如果存在就删除
                    ExcelDiskUtils.deleteFile2(new File(fileName));
                }//
                else if (StringUtils.equals(key, entityName + "Controller" + StringPool.DOT_JAVA)) {
                    fileName = generatePath + "/" + "controller" + "/" + key;
                    //如果存在就删除
                    ExcelDiskUtils.deleteFile2(new File(fileName));
                }//
                else if (StringUtils.equals(key, entityName + "Dao" + StringPool.DOT_JAVA)) {
                    fileName = generatePath + "/" + "dao" + "/" + key;
                    //如果存在就删除
                    ExcelDiskUtils.deleteFile2(new File(fileName));
                }//
                else if (StringUtils.equals(key, "I" + entityName + "Service" + StringPool.DOT_JAVA)) {
                    fileName = generatePath + "/" + "service" + "/" + key;
                    //如果存在就删除
                    ExcelDiskUtils.deleteFile2(new File(fileName));
                }//
                else if (StringUtils.equals(key, entityName + "ServiceImpl" + StringPool.DOT_JAVA)) {
                    fileName = generatePath + "/" + "service" + "//impl" + "/" + key;
                    //如果存在就删除
                    ExcelDiskUtils.deleteFile2(new File(fileName));
                }//
                else if (StringUtils.equals(key, entityName + "Mapper" + StringPool.DOT_JAVA)) {
                    fileName = generatePath + "/" + "mapper" + "/" + key;
                    //如果存在就删除
                    ExcelDiskUtils.deleteFile2(new File(fileName));
                } else if (StringUtils.equals(key, entityName + "Mapper" + StringPool.DOT_XML)) {
                    fileName = "D://projectstest//qixiaowei-cloud-backend//qixiaowei-modules//qixiaowei-system//src//main//resources" + "/" + "mapper" + "/" + key;
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
