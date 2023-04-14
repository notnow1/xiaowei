package net.qixiaowei.operate.cloud.service.salary;

import net.qixiaowei.operate.cloud.api.dto.salary.OfficialRankEmolumentDTO;
import net.qixiaowei.operate.cloud.excel.salary.OfficialRankEmolumentExcel;
import net.qixiaowei.system.manage.api.dto.basic.OfficialRankDecomposeDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * OfficialRankEmolumentService接口
 *
 * @author Graves
 * @since 2022-11-30
 */
public interface IOfficialRankEmolumentService {
    /**
     * 查询职级薪酬表
     *
     * @param officialRankEmolumentDTO 职级薪酬表主键
     * @return 职级薪酬表
     */
    OfficialRankEmolumentDTO selectOfficialRankEmolumentByOfficialRankEmolumentId(OfficialRankEmolumentDTO officialRankEmolumentDTO);

    /**
     * 查询职级薪酬表列表
     *
     * @param officialRankEmolumentDTO 职级薪酬表
     * @return 职级薪酬表集合
     */
    List<OfficialRankEmolumentDTO> selectOfficialRankEmolumentList(OfficialRankEmolumentDTO officialRankEmolumentDTO);

    /**
     * 新增职级薪酬表
     *
     * @param officialRankEmolumentDTO 职级薪酬表
     * @return 结果
     */
    OfficialRankEmolumentDTO insertOfficialRankEmolument(OfficialRankEmolumentDTO officialRankEmolumentDTO);

    /**
     * 修改职级薪酬表
     *
     * @param officialRankEmolumentDTO 职级薪酬表
     * @return 结果
     */
    int updateOfficialRankEmolument(OfficialRankEmolumentDTO officialRankEmolumentDTO);

    /**
     * 批量修改职级薪酬表
     *
     * @param officialRankEmolumentDtos 职级薪酬表
     * @return 结果
     */
    int updateOfficialRankEmoluments(List<OfficialRankEmolumentDTO> officialRankEmolumentDtos);

    /**
     * 批量新增职级薪酬表
     *
     * @param officialRankEmolumentDtos 职级薪酬表
     * @return 结果
     */
    int insertOfficialRankEmoluments(List<OfficialRankEmolumentDTO> officialRankEmolumentDtos);

    /**
     * 逻辑批量删除职级薪酬表
     *
     * @param officialRankEmolumentIds 需要删除的职级薪酬表集合
     * @return 结果
     */
    int logicDeleteOfficialRankEmolumentByOfficialRankEmolumentIds(List<Long> officialRankEmolumentIds);

    /**
     * 逻辑删除职级薪酬表信息
     *
     * @param officialRankEmolumentDTO
     * @return 结果
     */
    int logicDeleteOfficialRankEmolumentByOfficialRankEmolumentId(OfficialRankEmolumentDTO officialRankEmolumentDTO);

    /**
     * 批量删除职级薪酬表
     *
     * @param OfficialRankEmolumentDtos
     * @return 结果
     */
    int deleteOfficialRankEmolumentByOfficialRankEmolumentIds(List<OfficialRankEmolumentDTO> OfficialRankEmolumentDtos);

    /**
     * 逻辑删除职级薪酬表信息
     *
     * @param officialRankEmolumentDTO
     * @return 结果
     */
    int deleteOfficialRankEmolumentByOfficialRankEmolumentId(OfficialRankEmolumentDTO officialRankEmolumentDTO);


    /**
     * 删除职级薪酬表信息
     *
     * @param officialRankEmolumentId 职级薪酬表主键
     * @return 结果
     */
    int deleteOfficialRankEmolumentByOfficialRankEmolumentId(Long officialRankEmolumentId);

    /**
     * 导入Excel
     *
     * @param officialRankEmolumentDTO 职级DTO
     * @param file                     文件
     */
    int importOfficialRankEmolument(OfficialRankEmolumentDTO officialRankEmolumentDTO, MultipartFile file);

    /**
     * 导出Excel
     *
     * @param officialRankEmolumentDTO
     * @return
     */
    List<OfficialRankEmolumentExcel> exportOfficialRankEmolument(OfficialRankEmolumentDTO officialRankEmolumentDTO);

    /**
     * 查看该职级的分解信息
     *
     * @param officialRankEmolumentDTO 职级体系ID
     * @return
     */
    List<OfficialRankDecomposeDTO> selectOfficialDecomposeList(OfficialRankEmolumentDTO officialRankEmolumentDTO);

    /**
     * 根据岗位ID获取职级确定薪酬提示
     *
     * @param postId 岗位id
     * @return String
     */
    String selectByPostId(Long postId);

    /**
     * 职级确定薪酬详情
     *
     * @param officialRankSystemId 职级体系ID
     * @param officialRank         职级
     * @return 结果
     */
    String officialRankInfo(Long officialRankSystemId, Integer officialRank);
}
