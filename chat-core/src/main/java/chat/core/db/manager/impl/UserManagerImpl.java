package chat.core.db.manager.impl;

import chat.core.common.domain.ManagerException;
import chat.core.common.domain.PageResult;
import chat.core.common.domain.ResultDo;
import chat.core.common.redis.RedisManager;
import chat.core.common.utility.Md5Manager;
import chat.core.db.manager.RoleManager;
import chat.core.db.manager.UserManager;
import chat.core.db.mapper.UserMapper;
import chat.core.db.mapper.UserRobotMapper;
import chat.core.db.model.Role;
import chat.core.db.model.User;
import chat.core.db.model.dto.UserDto;
import chat.core.db.model.dto.UserInfoDto;
import chat.core.db.model.dto.UserOnlineDto;
import chat.core.db.model.query.UserQuery;
import chat.core.db.model.query.UserRobotQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @auther a-de
 * @date 2018/10/11 20:50
 */
@Component
public class UserManagerImpl implements UserManager {
    private Logger logger = LoggerFactory.getLogger(UserManagerImpl.class);
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRobotMapper userRobotMapper;

    @Autowired
    private RoleManager roleManager;

    @Override
    public User queryById(Long id) {
        try {
            User user = userMapper.queryById(id);
            return user;
        } catch (Exception e) {
            logger.error("用户 queryById异常",e);
            throw new ManagerException(e);
        }
    }

    @Override
    public ResultDo logined(Long domainId, String username, String password, boolean sys) {
        ResultDo resultDo = new ResultDo<>();
        try {
            User user = userMapper.queryByUserName(username, domainId);
            if (user == null) {
                resultDo.setErrorDesc("账号或密码错误");
                return resultDo;
            }
            String p = Md5Manager.md5(password, user.getSalt());
            if (!user.getPassword().equals(p)) {
                resultDo.setErrorDesc("账号或密码错误");
                return resultDo;
            }
            String token = UUID.randomUUID().toString().replace("-", "");
            if (sys){
                user.setSysToken(token);
            }else {
                user.setToken(token);
            }
            if (userMapper.update(user) > 0) {
                Map map = new HashMap();
                map.put("userId", user.getId());
                map.put("token", token);
                map.put("userName", user.getUserName());
                map.put("userIcon", user.getIcon());
                map.put("roomId", user.getRoomId());
                if (!sys){
                    Role role = roleManager.queryById(user.getRoleId());
                    if (role != null) {
                        List<String> authority = roleManager.queryRoleAuthority(role.getId(), 2);
                        map.put("roleName", role.getRoleName());
                        map.put("authority", authority);
                    }
                }

                resultDo.setResult(map);
                return resultDo;
            }
        } catch (Exception e) {
            logger.error("用户 logined异常", e);
            throw new ManagerException(e);
        }
        return resultDo;
    }

    @Override
    public boolean insert(User user) {
        try {
            if (userMapper.insert(user)>0){
                return true;
            }
        } catch (Exception e) {
            logger.error("用户 insert异常",e);
            throw new ManagerException(e);
        }
        return false;
    }

    @Override
    public boolean update(User user) {
        try {
            if (userMapper.update(user)>0){
                return true;
            }
        } catch (Exception e) {
            logger.error("用户 update异常",e);
            throw new ManagerException(e);
        }
        return false;
    }

    @Override
    public User queryByDomainIdAndId(Long id, Long domainId) {
        try {
            return userMapper.queryByDomainIdAndId(id,domainId);
        } catch (Exception e) {
            logger.error("用户 queryByDomainIdAndId异常",e);
            throw new ManagerException(e);
        }
    }

    @Override
    public PageResult<User> queryPage(UserQuery query) {
        PageResult<User> pageResult = new PageResult<>();
        try {
            List<User> list = new ArrayList<>();
            int count = userMapper.queryPageCount(query);
            if (count>0){
                list = userMapper.queryPageList(query);
            }

            pageResult.setData(list);
            pageResult.setiTotalDisplayRecords(count);
            pageResult.setiTotalRecords(count);
            pageResult.setPageNo(query.getCurrentPage());
            pageResult.setiDisplayLength(query.getPageSize());
        } catch (Exception e) {
            logger.error("用户 queryPage异常",e);
            throw new ManagerException(e);
        }
        return pageResult;
    }

    @Override
    public PageResult<UserDto> queryPageDto(UserQuery query) {
        PageResult<UserDto> pageResult = new PageResult<>();
        try {
            List<UserDto> list = new ArrayList<>();
            int count = userMapper.queryPageCount(query);
            if (count>0){
                list = userMapper.queryDtoPageList(query);
            }

            pageResult.setData(list);
            pageResult.setiTotalDisplayRecords(count);
            pageResult.setiTotalRecords(count);
            pageResult.setPageNo(query.getCurrentPage());
            pageResult.setiDisplayLength(query.getPageSize());
        } catch (Exception e) {
            logger.error("用户 queryPage异常",e);
            throw new ManagerException(e);
        }
        return pageResult;
    }

    @Override
    public User queryByUserName(String userName, Long domainId) {
        try {
            return userMapper.queryByUserName(userName,domainId);
        } catch (Exception e) {
            logger.error("用户 queryByUserName异常",e);
            throw new ManagerException(e);
        }
    }

    @Override
    public boolean remove(Long id) {
        try {
            if (userMapper.remove(id)>0){
                return true;
            }
        } catch (Exception e) {
            logger.error("用户 remove异常",e);
            throw new ManagerException(e);
        }
        return false;
    }

    @Override
    public User queryByDomainIdAndToken(Long domainId, String token) {
        try {
            return userMapper.queryByDomainIdAndToken(domainId,token);
        } catch (Exception e) {
            logger.error("用户 queryByDomainIdAndToken异常",e);
            throw new ManagerException(e);
        }
    }

    @Override
    public User queryByDomainIdAndSysToken(Long domainId, String sysToken) {
        try {
            return userMapper.queryByDomainIdAndSysToken(domainId,sysToken);
        } catch (Exception e) {
            logger.error("用户 queryByDomainIdAndSysToken异常",e);
            throw new ManagerException(e);
        }
    }

    @Override
    public boolean updateLoginIp(Long id, String ip) {
        try {
            if (userMapper.updateLoginIp(ip,id)>0){
                return true;
            }
        } catch (Exception e) {
            logger.error("用户 updateLoginIp异常",e);
            throw new ManagerException(e);
        }
        return false;
    }

    @Override
    public ResultDo onlineUser(Long domainId, Long roomId) {
        try {
            ResultDo resultDo = new ResultDo();
            Map result = new HashMap();
            String key = "onlineUsers_"+domainId+"_"+roomId;

            try {
                if (RedisManager.existsObject(key)){
                    result = (Map)RedisManager.getObject(key);
                    resultDo.setResult(result);
                    return resultDo;
                }
            } catch (Exception e) {
                logger.error("redis查询在线用户数异常",e);
            }
            UserQuery userQuery = new UserQuery();
            userQuery.setDomainId(domainId);
            userQuery.setRoomId(roomId);
            int userCount = userMapper.queryPageCount(userQuery);

            UserRobotQuery robotQuery = new UserRobotQuery();
            robotQuery.setDomainId(domainId);
            robotQuery.setRoomId(roomId);
            int robotCount = userRobotMapper.queryPageCount(robotQuery);

            List<UserOnlineDto> list = new ArrayList<>();
            List<UserOnlineDto> robotList = new ArrayList<>();
            if (userCount > 500) {
                list = userMapper.queryOnlineList(domainId, roomId, 500);
            } else {
                list = userMapper.queryOnlineList(domainId, roomId, null);
                robotList = userRobotMapper.queryOnlineList(domainId,roomId,500 - userCount);
            }
            if (robotList!=null&&robotList.size()>0){
                list.addAll(robotList);
            }
            result.put("count",userCount + robotCount);
            result.put("list",list);
            try {
                RedisManager.putObject(key,result);
                RedisManager.expireObject(key,30*60);
            } catch (Exception e) {
                logger.error("redis插入在线用户数异常",e);
            }
            resultDo.setResult(result);
            return resultDo;
        } catch (Exception e) {
            logger.error("用户 查询在线用户异常",e);
            throw new ManagerException(e);
        }
    }

    @Override
    public boolean removeToken(Long id) {
        try {
            if (userMapper.removeToken(id)>0){
                return true;
            }
        } catch (Exception e) {
            logger.error("用户 removeToken异常",e);
            throw new ManagerException(e);
        }
        return false;
    }

    @Override
    public boolean removeSysToken(Long id) {
        try {
            if (userMapper.removeSysToken(id)>0){
                return true;
            }
        } catch (Exception e) {
            logger.error("用户 removeSysToken异常",e);
            throw new ManagerException(e);
        }
        return false;
    }

    @Override
    public UserInfoDto queryUserInfo(Long domainId, String token) {
        try {
            return userMapper.queryUserInfo(domainId,token);
        } catch (Exception e) {
            logger.error("用户 queryUserInfo异常",e);
            throw new ManagerException(e);
        }
    }

    @Override
    public int queryByRoleId(Long roleId) {
        try {
            return userMapper.queryByRoleId(roleId);
        } catch (Exception e) {
            logger.error("用户 queryByRoleId异常",e);
            throw new ManagerException(e);
        }
    }
}
