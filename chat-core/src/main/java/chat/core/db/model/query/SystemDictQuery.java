package chat.core.db.model.query;

/**
 * @auther a-de
 * @date 2018/10/11 19:35
 */
public class SystemDictQuery extends BaseQuery {
    private Long domainId;
    private String sysGroup;
    private String sysKey;
    private Integer sysType;

    public Long getDomainId() {
        return domainId;
    }

    public void setDomainId(Long domainId) {
        this.domainId = domainId;
    }

    public String getSysGroup() {
        return sysGroup;
    }

    public void setSysGroup(String sysGroup) {
        this.sysGroup = sysGroup;
    }

    public String getSysKey() {
        return sysKey;
    }

    public void setSysKey(String sysKey) {
        this.sysKey = sysKey;
    }

    public Integer getSysType() {
        return sysType;
    }

    public void setSysType(Integer sysType) {
        this.sysType = sysType;
    }
}
