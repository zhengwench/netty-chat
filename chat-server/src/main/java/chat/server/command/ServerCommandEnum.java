package chat.server.command;

/**
 * @auther a-de
 * @date 2018/11/8 12:39
 */
public enum ServerCommandEnum {
    DEFAULT("DEFAULT",""),
    S_SYSTEM_ERROR("S_SYSTEM_ERROR","系统错误"),
    S_AUTH_ERROR("S_AUTH_ERROR","没有权限"),

    //连接发生协议
    S_IP_ERROR("S_IP_ERROR","IP错误"),
    S_IP_BLACK("S_IP_BLACK","ip黑名单"),
    S_CONNECT_ERROR("S_CONNECT_ERROR","连接信息信息错误"),
    S_DOMAIN_ERROR("S_DOMAIN_ERROR","服务错误"),
    S_ROOM_ERROR("S_ROOM_ERROR","房间信息错误"),
    S_NOT_IN_ROOM("S_NOT_IN_ROOM","当前不在聊天室房间内"),
    S_LOGIN_INFO_LOSE("S_LOGIN_INFO_LOSE","登录信息失效"),
    S_LOGIN_ANOTHER("S_LOGIN_ANOTHER","已在另外设备登录"),

    S_JOIN_ROOM("S_JOIN_ROOM","进入房间"),
    S_MESSAGE("S_MESSAGE","发送消息"),
    S_OUT_CHAT("S_OUT_CHAT","离开聊天室"),
    S_REMOVE_CHAT("S_REMOVE_CHAT","踢出聊天室"),
    S_FORBID_CHAT("S_FORBID_CHAT","禁言"),
    S_UNFORBID_CHAT("S_UNFORBID_CHAT","取消禁言"),
    S_HISTORY_CHAT("S_HISTORY_CHAT","历史消息"),
    S_RECALL_MESSAGE("S_RECALL_MESSAGE","撤回消息"),
    S_HEART_BEAT("S_HEART_BEAT","心跳检测"),
    S_SEND_REDBAG("S_SEND_REDBAG","发红包"),
    S_RECEIVE_REDBAG("S_RECEIVE_REDBAG","领取红包"),
    S_RECEIVE_REDBAG_ERROR("S_RECEIVE_REDBAG_ERROR","领取红包异常"),
    S_REDBAG_OUT("S_REDBAG_OUT","红包已领取完"),
    S_LOTTERY_GOODNEWS("S_LOTTERY_GOODNEWS","中奖喜讯"),
    ;
    private String key;
    private String desc;

    public String getKey() {
        return key;
    }

    public String getDesc() {
        return desc;
    }

    ServerCommandEnum(String _key, String _desc) {
        this.key = _key;
        this.desc = _desc;
    }

    public static ServerCommandEnum getCodeDescBykey(String key) {
        for (ServerCommandEnum serverCommandEnum : ServerCommandEnum.values()) {
            if (serverCommandEnum.getKey().equals(key)) {
                return serverCommandEnum;
            }
        }
        return DEFAULT;
    }
}
