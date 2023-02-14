package net.qixiaowei.integration.common.enums.field.system;

public enum PostField {

    POST_NAME("post_name", "岗位名称"),
    POST_CODE("post_code", "岗位编码"),
    OFFICIAL_RANK_SYSTEM_ID("official_rank_system_id", "职级体系"),
    POST_RANK("post_rank", "岗位职级"),
    POST_RANK_LOWER("post_rank_lower", "职级下限"),
    POST_RANK_UPPER("post_rank_upper", "职级上限"),
    STATUS("status", "岗位状态"),
    ;

    private final String code;
    private final String info;

    PostField(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

}
