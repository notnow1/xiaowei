package net.qixiaowei.file.mapper;

import java.util.List;
import net.qixiaowei.file.api.domain.File;
import net.qixiaowei.file.api.dto.FileDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* FileMapper接口
* @author hzk
* @since 2022-10-28
*/
public interface FileMapper{
    /**
    * 查询文件表
    *
    * @param fileId 文件表主键
    * @return 文件表
    */
    FileDTO selectFileByFileId(@Param("fileId")Long fileId);


    /**
    * 批量查询文件表
    *
    * @param fileIds 文件表主键集合
    * @return 文件表
    */
    List<FileDTO> selectFileByFileId(@Param("fileIds") List<Long> fileIds);

    /**
    * 查询文件表列表
    *
    * @param file 文件表
    * @return 文件表集合
    */
    List<FileDTO> selectFileList(@Param("file")File file);

    /**
    * 新增文件表
    *
    * @param file 文件表
    * @return 结果
    */
    int insertFile(@Param("file")File file);

    /**
    * 修改文件表
    *
    * @param file 文件表
    * @return 结果
    */
    int updateFile(@Param("file")File file);

    /**
    * 批量修改文件表
    *
    * @param fileList 文件表
    * @return 结果
    */
    int updateFiles(@Param("fileList")List<File> fileList);
    /**
    * 逻辑删除文件表
    *
    * @param file
    * @return 结果
    */
    int logicDeleteFileByFileId(@Param("file")File file);

    /**
    * 逻辑批量删除文件表
    *
    * @param fileIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteFileByFileIds(@Param("fileIds")List<Long> fileIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除文件表
    *
    * @param fileId 文件表主键
    * @return 结果
    */
    int deleteFileByFileId(@Param("fileId")Long fileId);

    /**
    * 物理批量删除文件表
    *
    * @param fileIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteFileByFileIds(@Param("fileIds")List<Long> fileIds);

    /**
    * 批量新增文件表
    *
    * @param Files 文件表列表
    * @return 结果
    */
    int batchFile(@Param("files")List<File> Files);
}
