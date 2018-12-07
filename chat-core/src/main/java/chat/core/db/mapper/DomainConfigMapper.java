package chat.core.db.mapper;

import chat.core.db.model.DomainConfig;
import chat.core.db.model.query.DomainConfigQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auther a-de
 * @date 2018/10/10 21:31
 */
public interface DomainConfigMapper {
    int insert(DomainConfig domainConfig);

    int update(DomainConfig domainConfig);

    DomainConfig queryById(@Param("id") Long id);

    DomainConfig queryByDomainName(@Param("domainName") String domainName);

    List<DomainConfig> queryPageList(@Param("query") DomainConfigQuery query);

    int queryPageCount(@Param("query") DomainConfigQuery query);

    List<DomainConfig> queryList();

    List<DomainConfig> queryParentList();

    int remove(@Param("id") Long id);
}
