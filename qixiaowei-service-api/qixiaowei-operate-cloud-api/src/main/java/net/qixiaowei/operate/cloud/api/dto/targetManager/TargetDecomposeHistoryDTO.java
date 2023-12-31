package net.qixiaowei.operate.cloud.api.dto.targetManager;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 目标分解历史版本表
* @author TANGMICHI
* @since 2022-10-31
*/
@Data
@Accessors(chain = true)
public class TargetDecomposeHistoryDTO {

    //查询检验
    public interface QueryTargetDecomposeHistoryDTO extends Default{

    }
    //新增检验
    public interface AddTargetDecomposeHistoryDTO extends Default{

    }

    //删除检验
    public interface DeleteTargetDecomposeHistoryDTO extends Default{

    }
    //修改检验
    public interface UpdateTargetDecomposeHistoryDTO extends Default{

    }
    /**
    * ID
    */
    private  Long targetDecomposeHistoryId;
    /**
    * 目标分解ID
    */
    private  Long targetDecomposeId;
    /**
     * 目标年度
     */
    private  Integer targetYear;

    /**
     * 指标名称
     */
    private  String indicatorName;

    /**
     * 指标id
     */
    private  Long indicatorId;

    /**
     * 分解维度
     */
    private  String decompositionDimension;

    /**
     * 字段名称
     */
    private List<Map<String, String>> fileNameList;

    /**
     * 时间维度:1年度;2半年度;3季度;4月度;5周
     */
    private  Integer timeDimension;
    /**
    * 版本号
    */
    private  String version;
    /**
    * 预测周期
    */
    private  String forecastCycle;
    /**
     * 目标分解详情信息
     */
    @NotEmpty(message = "目标分解详情信息不能为空",groups = {TargetDecomposeHistoryDTO.UpdateTargetDecomposeHistoryDTO.class})
    @Valid
    private List<DecomposeDetailsSnapshotDTO> decomposeDetailsSnapshotDTOS;
    /**
    * 删除标记:0未删除;1已删除
    */
    private  Integer deleteFlag;
    /**
    * 创建人
    */
    private  Long createBy;
    /**
    * 创建时间
    */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    private  Date  createTime;
    /**
    * 更新人
    */
    private  Long updateBy;
    /**
    * 更新时间
    */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    private  Date  updateTime;

}

