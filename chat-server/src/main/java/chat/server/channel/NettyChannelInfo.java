package chat.server.channel;

/**
 * @auther a-de
 * @date 2018/11/11 12:52
 */
public class NettyChannelInfo {
    private String userId;

    private String domain;

    private String roomId;

    private String zoneKey;

    private String contextKey;

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

    @Override
    public String toString() {
        return "NettyChannelInfo{" +
                "userId='" + userId + '\'' +
                ", domain='" + domain + '\'' +
                ", roomId='" + roomId + '\'' +
                ", zoneKey='" + zoneKey + '\'' +
                ", contextKey='" + contextKey + '\'' +
                '}';
    }
}
