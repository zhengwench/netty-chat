package chat.core.db.model;

import java.util.Date;

/**
 * @auther a-de
 * @date 2018/11/6 21:02
 */
public class BlackIp extends AbstractBaseObject {
    /**
     * id
     */
    private Long id;
    /**
     * domainId
     */
    private Long domainId;
    /**
     * IP
     */
    private String ip;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
