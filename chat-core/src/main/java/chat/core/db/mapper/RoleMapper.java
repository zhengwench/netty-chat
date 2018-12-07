package chat.core.db.mapper;

import chat.core.db.model.Role;
import chat.core.db.model.query.RoleQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auther a-de
 * @date 2018/10/10 21:02
 */
public interface RoleMapper {
    int insert(Role role);

    int update(Role role);

    Role queryById(@Param("id") Long id);

    Role queryByIdAndDomainId(@Param("id") Long id, @Param("domainId") Long domainId);

    List<Role> queryPageList(@Param("query") RoleQuery query);

    int queryPageCount(@Param("query") RoleQuery query);

    List<Role> queryList(@Param("domainId") Long domainId);

    int remove(@Param("id") Long id);
}
