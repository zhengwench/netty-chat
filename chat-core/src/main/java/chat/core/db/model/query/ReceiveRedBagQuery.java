package chat.core.db.model.query;

import java.util.Date;

/**
 * @auther a-de
 * @date 2018/11/6 21:37
 */
public class ReceiveRedBagQuery extends BaseQuery {
    private Long id;
    private Long receiverId;
    private String receiverName;
    private Long domainId;
    private Long redbagId;
    private Integer status;
    private Date startTime;
    private Date endTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
