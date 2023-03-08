package net.qixiaowei.integration.common.web.domain;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import net.qixiaowei.integration.common.enums.PageSearchCondition;
import net.qixiaowei.integration.common.utils.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * Entity基类
 */
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 搜索值:多个字段共用一个查询值
     */
    private String searchValue;

    /**
     * 创建者
     */
    private Long createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新者
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标记:0未删除;1已删除
     */
    private Integer deleteFlag;

    /**
     * 请求参数
     */
    private Map<String, Object> params;


    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params) {
        if (null != params) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String key = entry.getKey();
                if (PageSearchCondition.endsWithCodeOfList(key)) {
                    Object value = entry.getValue();
                    if (value instanceof String) {
                        String StringValue = (String) value;
                        List<String> newValue;
                        if (StringUtils.isEmpty(StringValue)) {
                            newValue = new ArrayList<>();
                            entry.setValue(newValue);
                            continue;
                        }
                        //处理中文分号，统一替换为英文分号
                        if (StringValue.contains("；")) {
                            StringValue = StringValue.replace("；", ";");
                        }
                        newValue = StrUtil.splitTrim(StringValue, ";", -1);
                        entry.setValue(newValue);
                    }
                }
            }
        }
        this.params = params;
    }
}
