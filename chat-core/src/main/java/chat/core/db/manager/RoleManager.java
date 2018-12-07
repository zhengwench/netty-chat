package chat.core.db.manager;

import chat.core.common.domain.PageResult;
import chat.core.db.model.Permission;
import chat.core.db.model.Role;
import chat.core.db.model.dto.PermissionRoleDto;
import chat.core.db.model.query.RoleQuery;

import java.util.List;

/**
 * @auther a-de
 * @date 2018/10/12 13:40
 */
public interface RoleManager {
    List<PermissionRoleDto> queryRolePerm(Long roleId, Integer backStatus);

    List<Permission> queryRoleAllPerms(Long roleId);

    List<PermissionRoleDto> queryPerms(Integer backStatus);

    List<String> queryRoleAuthority(Long roleId, Integer backStatus);

    boolean insert(Role role);

    boolean update(Role role);

    Role queryById(Long id);

    Role queryByIdAndDomainId(Long id, Long domainId);

    PageResult<Role> queryPage(RoleQuery query);

    List<Role> queryList(Long domainId);

    boolean remove(Long id);

    boolean queryRolePermAndAuthority(Long roleId, String permission);
}
