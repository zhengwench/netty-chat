package chat.server.command;

/**
 * @auther a-de
 * @date 2018/11/8 12:39
 */
public enum ClientCommandEnum {
    DEFAULT("DEFAULT",""),
    C_MESSAGE("C_MESSAGE","发送消息"),
    C_REMOVE_CHAT("C_REMOVE_CHAT","踢出房间"),
    C_RECALL_MESSAGE("C_RECALL_MESSAGE","撤回消息"),
    C_HEART_BEAT("C_HEART_BEAT","心跳检测"),
    C_SEND_REDBAG("C_SEND_REDBAG","发红包"),
    C_RECEIVE_REDBAG("C_RECEIVE_REDBAG","领取红包"),
    C_REDBAG_RECEIVE_RECORD("C_REDBAG_RECEIVE_RECORD","红包领取记录"),
    ;
    private String key;
    private String desc;

    public String getKey() {
        return key;
    }

    public String getDesc() {
        return desc;
    }

    ClientCommandEnum(String _key, String _desc) {
        this.key = _key;
        this.desc = _desc;
    }

    public static ClientCommandEnum getEnumBykey(String key) {
        for (ClientCommandEnum clientCommandEnum : ClientCommandEnum.values()) {
            if (clientCommandEnum.getKey().equals(key)) {
                return clientCommandEnum;
            }
        }
        return DEFAULT;
    }
}
