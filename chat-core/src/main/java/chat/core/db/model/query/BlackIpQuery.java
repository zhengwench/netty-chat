package chat.core.db.model.query;

/**
 * @auther a-de
 * @date 2018/11/7 12:40
 */
public class BlackIpQuery extends BaseQuery {
    private Long domainId;
    private String ip;

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
}
