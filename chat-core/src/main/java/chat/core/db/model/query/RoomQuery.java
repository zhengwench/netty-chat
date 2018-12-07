package chat.core.db.model.query;

/**
 * @auther a-de
 * @date 2018/11/6 21:28
 */
public class RoomQuery extends BaseQuery {
    private Long domainId;

    private String roomName;

    private Integer openRoom;

    public Long getDomainId() {
        return domainId;
    }

    public void setDomainId(Long domainId) {
        this.domainId = domainId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Integer getOpenRoom() {
        return openRoom;
    }

    public void setOpenRoom(Integer openRoom) {
        this.openRoom = openRoom;
    }
}
