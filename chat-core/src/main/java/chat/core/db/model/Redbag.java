package chat.core.db.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @auther a-de
 * @date 2018/11/6 15:09
 */
public class Redbag extends AbstractBaseObject {
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
     * 发放人ID
     */
    private Long sendUserId;
    /**
     * 发放人姓名
     */
    private String sendUserName;
    /**
     * 发放人头像
     */
    private String sendUserIcon;
    /**
     * 总金额
     */
    private BigDecimal amount;
    /**
     * 个数
     */
    private Integer count;
    /**
     * 备注
     */
    private String remark;
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

    public Long getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(Long sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public String getSendUserIcon() {
        return sendUserIcon;
    }

    public void setSendUserIcon(String sendUserIcon) {
        this.sendUserIcon = sendUserIcon;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
