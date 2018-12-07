package chat.core.db.manager.impl;

import chat.core.common.domain.ManagerException;
import chat.core.common.domain.PageResult;
import chat.core.common.redis.RedisManager;
import chat.core.db.manager.RoleManager;
import chat.core.db.mapper.PermissionMapper;
import chat.core.db.mapper.RoleMapper;
import chat.core.db.model.Permission;
import chat.core.db.model.Role;
import chat.core.db.model.dto.PermissionRoleDto;
import chat.core.db.model.query.RoleQuery;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther a-de
 * @date 2018/10/12 13:41
 */
@Component
public class RoleManagerImpl implements RoleManager {
    private Logger logger = LoggerFactory.getLogger(RoleManagerImpl.class);
    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<PermissionRoleDto> queryRolePerm(Long roleId,Integer backStatus) {
        try {
            Role role = roleMapper.queryById(roleId);
            if (role == null || StringUtils.isEmpty(role.getPermIds())){
                return null;
            }
            String[] pids = role.getPermIds().split(",");
            List<PermissionRoleDto> first = permissionMapper.queryRolePermAndParentId(pids,0L,backStatus);
            if (first!=null && first.size()>0) {
                for (PermissionRoleDto f : first) {
                    f.setList(permissionMapper.queryRolePermAndParentId(pids, f.getId(),backStatus));
                    if (f.getList() != null && f.getList().size() > 0) {
                        for (PermissionRoleDto s : f.getList()) {
                            s.setList(permissionMapper.queryRolePermAndParentId(pids, s.getId(),backStatus));
                        }
                    }
                }
            }
            return first;
        } catch (Exception e) {
            logger.error("RoleManagerImpl queryRolePerm异常",e);
            throw new ManagerException(e);
        }
    }

    @Override
    public List<Permission> queryRoleAllPerms(Long roleId) {
        try {
            Role role = roleMapper.queryById(roleId);
            if (role==null || StringUtils.isEmpty(role.getPermIds())){
                return null;
            }
            String[] pids = role.getPermIds().split(",");
            List<Permission> permissions = permissionMapper.queryRolePerm(pids);
            return permissions;
        } catch (Exception e) {
            logger.error("RoleManagerImpl queryRoleAllPerms异常",e);
            throw new ManagerException(e);
        }
    }

    @Override
    public List<PermissionRoleDto> queryPerms(Integer backStatus) {
        try {
            List<PermissionRoleDto> first = permissionMapper.queryPermsByParentId(0L,backStatus);
            if (first!=null && first.size()>0) {
                for (PermissionRoleDto f : first) {
                    f.setList(permissionMapper.queryPermsByParentId(f.getId(),backStatus));
                    if (f.getList() != null && f.getList().size() > 0) {
                        for (PermissionRoleDto s : f.getList()) {
                            s.setList(permissionMapper.queryPermsByParentId(s.getId(),backStatus));
                        }
                    }
                }
            }
            return first;
        } catch (Exception e) {
            logger.error("RoleManagerImpl queryPerms异常",e);
            throw new ManagerException(e);
        }
    }

    @Override
    public List<String> queryRoleAuthority(Long roleId, Integer backStatus) {
        Role role = roleMapper.queryById(roleId);
        if (role==null || StringUtils.isEmpty(role.getPermIds())){
            return null;
        }
        String[] pids = role.getPermIds().split(",");
        List<String> permissions = permissionMapper.queryRoleAuthority(pids,backStatus);
        return permissions;
    }

    @Override
    public boolean insert(Role role) {
        try {
            if (roleMapper.insert(role)>0){
                return true;
            }
        } catch (Exception e) {
            logger.error("RoleManagerImpl insert异常",e);
            throw new ManagerException(e);
        }
        return false;
    }

    @Override
    public boolean update(Role role) {
        try {
            if (roleMapper.update(role)>0) {
                String key = "chat_" + role.getId();
                RedisManager.expire(key.hashCode()+"", 0);
                return true;
            }
        } catch (Exception e) {
            logger.error("RoleManagerImpl update异常",e);
            throw new ManagerException(e);
        }
        return false;
    }

    @Override
    public Role queryById(Long id) {
        try {
            return roleMapper.queryById(id);
        } catch (Exception e) {
            logger.error("RoleManagerImpl queryById异常",e);
            throw new ManagerException(e);
        }
    }

    @Override
    public Role queryByIdAndDomainId(Long id, Long domainId) {
        try {
            return roleMapper.queryByIdAndDomainId(id,domainId);
        } catch (Exception e) {
            logger.error("RoleManagerImpl queryByIdAndDomainId异常",e);
            throw new ManagerException(e);
        }
    }

    @Override
    public PageResult<Role> queryPage(RoleQuery query) {
        PageResult<Role> pageResult = new PageResult<>();
        try {
            List<Role> list = new ArrayList<>();
            int count = roleMapper.queryPageCount(query);
            if (count>0){
                list = roleMapper.queryPageList(query);
            }

            pageResult.setData(list);
            pageResult.setiTotalDisplayRecords(count);
            pageResult.setiTotalRecords(count);
            pageResult.setPageNo(query.getCurrentPage());
            pageResult.setiDisplayLength(query.getPageSize());
        } catch (Exception e) {
            logger.error("RoleManagerImpl queryPage异常",e);
            throw new ManagerException(e);
        }
        return pageResult;
    }

    @Override
    public List<Role> queryList(Long domainId) {
        try {
            return roleMapper.queryList(domainId);
        } catch (Exception e) {
            logger.error("RoleManagerImpl queryList异常",e);
            throw new ManagerException(e);
        }
    }

    @Override
    public boolean remove(Long id) {
        try {
            if (roleMapper.remove(id)>0){
                String key = "chat_" + id;
                RedisManager.expire(key.hashCode()+"", 0);
                return true;
            }
        } catch (Exception e) {
            logger.error("RoleManagerImpl remove异常",e);
            throw new ManagerException(e);
        }
        return false;
    }

    @Override
    public boolean queryRolePermAndAuthority(Long roleId, String authority) {
        try {
            String key = "chat_"+roleId;
            try {
                if (RedisManager.exists(key.hashCode() + "")){
                    String authresult = RedisManager.hget(key.hashCode() + "",authority.hashCode() + "");
                    if ("true".equals(authresult)){
                        return true;
                    }
                    return false;
                }
            } catch (Exception e) {
                logger.error("redis 查询角色资源异常",e);
            }
            Role role = roleMapper.queryById(roleId);
            if (role == null || StringUtils.isEmpty(role.getPermIds())) {
                return false;
            }
            String[] pids = role.getPermIds().split(",");
            Permission permission = permissionMapper.queryRolePermAndAuthority(pids, authority);
            if (permission != null) {
                try {
                    RedisManager.hset(key.hashCode() + "", authority.hashCode() + "", "true");
                } catch (Exception e) {
                    logger.error("redis设置角色资源异常",e);
                }
                return true;
            }
        } catch (Exception e) {
            logger.error("角色 queryRolePermAndAuthority异常",e);
            throw new ManagerException(e);
        }
        return false;
    }
}
