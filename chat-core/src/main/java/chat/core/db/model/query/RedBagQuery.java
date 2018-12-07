package chat.core.db.model.query;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @auther a-de
 * @date 2018/11/6 21:26
 */
public class RedBagQuery extends BaseQuery {
    /**
     * 红包发放人
     */
    private String sendMemberName;
    /**
     * domainId
     */
    private Long domainId;
    /**
     * 查询开始时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    /**
     * 查询结束时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    public String getSendMemberName() {
        return sendMemberName;
    }

    public void setSendMemberName(String sendMemberName) {
        this.sendMemberName = sendMemberName;
    }

    public Long getDomainId() {
        return domainId;
    }

    public void setDomainId(Long domainId) {
        this.domainId = domainId;
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
