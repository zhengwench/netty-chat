package chat.core.db.manager;

import chat.core.common.domain.PageResult;
import chat.core.db.model.SystemDict;
import chat.core.db.model.query.SystemDictQuery;

import java.util.List;

/**
 * @auther a-de
 * @date 2018/10/12 13:40
 */
public interface SystemDictManager {
    boolean insert(SystemDict systemDict);

    boolean update(SystemDict systemDict);

    boolean remove(Long id, Long domainId);

    SystemDict queryById(Long id, Long domainId);

    SystemDict queryByKey(Long domainId, String group, String key);

    List<SystemDict> queryGroupAllByDomainId(Long domainId, String group);

    PageResult<SystemDict> queryGroupAllPage(SystemDictQuery query);

    List<SystemDict> queryGroupKey(Long domainId, String sysGroup, String sysKey);
}
