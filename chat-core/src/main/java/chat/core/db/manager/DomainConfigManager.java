package chat.core.db.manager;

import chat.core.common.domain.PageResult;
import chat.core.db.model.DomainConfig;
import chat.core.db.model.query.DomainConfigQuery;

import java.util.List;

/**
 * @auther a-de
 * @date 2018/10/12 13:38
 */
public interface DomainConfigManager {
    boolean insert(DomainConfig domainConfig);

    boolean create(DomainConfig domainConfig);

    boolean update(DomainConfig domainConfig);

    DomainConfig queryById(Long id);

    DomainConfig queryByIdCache(Long id);

    DomainConfig queryByDomainName(String domainName);

    DomainConfig queryByDomainNameCache(String domainName);

    PageResult<DomainConfig> queryPageDomainConfig(DomainConfigQuery query);

    List<DomainConfig> queryList();

    List<DomainConfig> queryParentList();

    boolean remove(Long id);
}
