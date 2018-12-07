package chat.core.db.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @auther a-de
 * @date 2018/11/6 14:49
 */
public class ReceiveRedbag extends AbstractBaseObject {
    /**
     * id
     */
    private Long id;
    /**
     * domainID
     */
    private Long domainId;
    /**
     * 红包ID
     */
    private Long redbagId;
    /**
     * 领取人ID
     */
    private Long receiveUserId;
    /**
     * 领取人name
     */
    private String receiveUserName;
    /**
     * 发放人id
     */
    private Long sendUserId;
    /**
     * 红包发送人ID
     */
    private String sendUserName;
    /**
     * 红包发放人头像
     */
    private String sendUserIcon;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 状态(1-未兑换，2-已兑换，3-过期)
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;

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

    public Long getRedbagId() {
        return redbagId;
    }

    public void setRedbagId(Long redbagId) {
        this.redbagId = redbagId;
    }

    public Long getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(Long receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public String getReceiveUserName() {
        return receiveUserName;
    }

    public void setReceiveUserName(String receiveUserName) {
        this.receiveUserName = receiveUserName;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
