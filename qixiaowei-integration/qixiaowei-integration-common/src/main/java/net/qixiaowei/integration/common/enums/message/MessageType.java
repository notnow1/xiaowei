package net.qixiaowei.integration.common.enums.message;

/**
 * @description 消息类型
 * @Author hzk
 * @Date 2022-12-08 19:50
 **/
public enum MessageType {

    PRIVATE_MESSAGE(1, "站内信"),

    SMS(2, "短信"),

    EMAIL(3, "邮件");


    private Integer code;
    private String info;

    MessageType(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    /**
     * 根据消息类型获取消息枚举
     *
     * @param code 消息类型code
     * @return {@link MessageType}
     */
    public static MessageType getMessageType(Integer code) {
        for (MessageType messageType : MessageType.values()) {
            if (messageType.getCode().equals(code)) {
                return messageType;
            }
        }
        return null;
    }
}
