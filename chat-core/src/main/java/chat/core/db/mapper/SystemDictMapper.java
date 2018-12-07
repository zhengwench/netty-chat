package chat.core.db.mapper;

import chat.core.db.model.SystemDict;
import chat.core.db.model.query.SystemDictQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auther a-de
 * @date 2018/10/10 21:32
 */
public interface SystemDictMapper {
    int insert(SystemDict systemDict);

    int update(SystemDict systemDict);

    int remove(@Param("id") Long id, @Param("domainId") Long domainId);

    SystemDict queryById(@Param("id") Long id, @Param("domainId") Long domainId);

    SystemDict queryByKey(@Param("domainId") Long domainId, @Param("group") String group, @Param("key") String key);

    List<SystemDict> queryGroupAllByDomainId(@Param("domainId") Long domainId, @Param("group") String group);

    List<SystemDict> queryGroupAllPageList(@Param("query") SystemDictQuery query);

    int queryGroupAllPageCount(@Param("query") SystemDictQuery query);

    List<SystemDict> queryGroupKey(@Param("domainId") Long domainId, @Param("sysGroup") String sysGroup, @Param("sysKey") String sysKey);
}
