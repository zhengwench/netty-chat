package chat.web.dto;

import java.io.Serializable;

/**
 * @auther a-de
 * @date 2018/11/12 13:44
 */
public class MonitorOnlineDto implements Serializable {
    private String roomName;

    private Integer onlineCount;

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Integer getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(Integer onlineCount) {
        this.onlineCount = onlineCount;
    }
}
