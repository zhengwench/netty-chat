package chat.core.db.model;

import java.util.Date;

/**
 * @auther a-de
 * @date 2018/11/6 15:12
 */
public class RoomUser extends AbstractBaseObject {
    /**
     * id
     */
    private Long id;
    /**
     * domainId
     */
    private Long domainId;
    /**
     * 房间ID
     */
    private Long roomId;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 创建时间
     */
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
