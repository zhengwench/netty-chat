package chat.core.db.model;

import java.util.Date;

/**
 * @auther a-de
 * @date 2018/11/6 15:15
 */
public class Room extends AbstractBaseObject {
    /**
     * id
     */
    private Long id;
    /**
     * domainId
     */
    private Long domainId;
    /**
     * 房间名
     */
    private String roomName;
    /**
     * 房间类型 (1-默认房间，2-拓展房间)
     */
    private Integer roomType;
    /**
     * 房间logo
     */
    private String roomLogo;
    /**
     * PC房间聊天背景
     */
    private String roomPcBg;
    /**
     * 手机版聊天背景
     */
    private String roomMobileBg;
    /**
     * 是否禁言(1-开启禁言，0-关闭禁言)
     */
    private Integer forbidStatus;
    /**
     * 是否公开房间(1-公开房间 2-非公开房间)
     */
    private Integer openRoom;
    /**
     * 备注
     */
    private String remark;
    /**
     * websocketUrl
     */
    private String websocketUrl;
    /**
     * httpUrl
     */
    private String httpUrl;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 删除标识
     */
    private Integer delFlag;

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

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Integer getRoomType() {
        return roomType;
    }

    public void setRoomType(Integer roomType) {
        this.roomType = roomType;
    }

    public String getRoomLogo() {
        return roomLogo;
    }

    public void setRoomLogo(String roomLogo) {
        this.roomLogo = roomLogo;
    }

    public String getRoomPcBg() {
        return roomPcBg;
    }

    public void setRoomPcBg(String roomPcBg) {
        this.roomPcBg = roomPcBg;
    }

    public String getRoomMobileBg() {
        return roomMobileBg;
    }

    public void setRoomMobileBg(String roomMobileBg) {
        this.roomMobileBg = roomMobileBg;
    }

    public Integer getForbidStatus() {
        return forbidStatus;
    }

    public void setForbidStatus(Integer forbidStatus) {
        this.forbidStatus = forbidStatus;
    }

    public Integer getOpenRoom() {
        return openRoom;
    }

    public void setOpenRoom(Integer openRoom) {
        this.openRoom = openRoom;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getWebsocketUrl() {
        return websocketUrl;
    }

    public void setWebsocketUrl(String websocketUrl) {
        this.websocketUrl = websocketUrl;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }
}
