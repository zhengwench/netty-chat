package chat.core.db.model.query;

/**
 * @auther a-de
 * @date 2018/11/19 13:10
 */
public class UserRobotQuery extends BaseQuery {
    private Long domainId;

    private Long roomId;

    public Long getDomainId() {
        return domainId;
    }

    public void setDomainId(Long domainId) {
        this.domainId = domainId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
}
