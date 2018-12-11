package chat.server.channel;

/**
 * @auther a-de
 * @date 2018/11/11 12:52
 */
public class NettyChannelInfo {
    private String infoId;

    private String userId;

    private String domain;

    private String roomId;

    private String zoneKey;

    private String contextKey;

    private String token;

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getZoneKey() {
        return zoneKey;
    }

    public void setZoneKey(String zoneKey) {
        this.zoneKey = zoneKey;
    }

    public String getContextKey() {
        return contextKey;
    }

    public void setContextKey(String contextKey) {
        this.contextKey = contextKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "NettyChannelInfo{" +
                "infoId='" + infoId + '\'' +
                ", userId='" + userId + '\'' +
                ", domain='" + domain + '\'' +
                ", roomId='" + roomId + '\'' +
                ", zoneKey='" + zoneKey + '\'' +
                ", contextKey='" + contextKey + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
