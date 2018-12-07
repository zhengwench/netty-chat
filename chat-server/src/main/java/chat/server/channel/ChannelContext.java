package chat.server.channel;

import io.netty.channel.Channel;

import java.io.Serializable;

/**
 * @auther a-de
 * @date 2018/11/5 15:11
 */
public class ChannelContext implements Serializable {
    private String domain;
    private String roomId;
    private String ip;
    private Long userId;
    private String token;
    private Channel channel;

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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
