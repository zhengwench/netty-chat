package chat.core.db.mapper;

import chat.core.db.model.Permission;
import chat.core.db.model.dto.PermissionRoleDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auther a-de
 * @date 2018/10/10 21:02
 */
public interface PermissionMapper {
    List<Permission> queryRolePerm(@Param("ids") String[] ids);

    List<PermissionRoleDto> queryRolePermAndParentId(@Param("ids") String[] ids, @Param("parentId") Long parentId, @Param("backStatus") Integer backStatus);

    List<String> queryRoleAuthority(@Param("ids") String[] ids, @Param("backStatus") Integer backStatus);

    List<PermissionRoleDto> queryPermsByParentId(@Param("parentId") Long parentId, @Param("backStatus") Integer backStatus);

    List<Long> queryAllIds();

    Permission queryRolePermAndAuthority(@Param("ids") String[] ids, @Param("authority") String authority);
}
