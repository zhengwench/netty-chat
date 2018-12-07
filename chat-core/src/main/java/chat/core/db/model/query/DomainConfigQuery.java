package chat.core.db.model.query;

/**
 * @auther a-de
 * @date 2018/10/11 13:47
 */
public class DomainConfigQuery extends BaseQuery {
    private String domainName;

    private Long parentId;

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
