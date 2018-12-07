package chat.core.db.model.query;


/**
 * @auther a-de
 * @date 2018/10/11 19:54
 */
public class RoleQuery extends BaseQuery {
    private Long domainId;

    public Long getDomainId() {
        return domainId;
    }

    public void setDomainId(Long domainId) {
        this.domainId = domainId;
    }
}
