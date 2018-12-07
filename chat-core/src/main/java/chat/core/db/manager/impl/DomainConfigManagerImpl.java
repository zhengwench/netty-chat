package chat.core.db.manager.impl;

import chat.core.common.domain.AppConfig;
import chat.core.common.domain.ManagerException;
import chat.core.common.domain.PageResult;
import chat.core.common.redis.RedisManager;
import chat.core.common.utility.Md5Manager;
import chat.core.common.utility.RandomUtils;
import chat.core.db.manager.DomainConfigManager;
import chat.core.db.mapper.DomainConfigMapper;
import chat.core.db.mapper.PermissionMapper;
import chat.core.db.mapper.RoleMapper;
import chat.core.db.mapper.RoomMapper;
import chat.core.db.mapper.UserMapper;
import chat.core.db.model.DomainConfig;
import chat.core.db.model.Role;
import chat.core.db.model.Room;
import chat.core.db.model.User;
import chat.core.db.model.query.DomainConfigQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @auther a-de
 * @date 2018/10/12 13:41
 */
@Component
public class DomainConfigManagerImpl implements DomainConfigManager {
    private Logger logger = LoggerFactory.getLogger(DomainConfigManagerImpl.class);

    @Autowired
    private DomainConfigMapper domainConfigMapper;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean insert(DomainConfig domainConfig) {
        try {
            if (domainConfigMapper.insert(domainConfig)>0){
                return true;
            }
        } catch (Exception e) {
            logger.error("DomainConfigManagerImpl insert异常",e);
            throw new ManagerException(e);
        }
        return false;
    }

    @Override
    @Transactional
    public boolean create(DomainConfig domainConfig) {
        if (domainConfigMapper.insert(domainConfig)>0){
            Long domainId = domainConfig.getId();
            if (domainConfig.getParentId()!=null && domainConfig.getParentId()>0){
                return true;
            }
            //创建房间
            Room insertRoom = new Room();
            insertRoom.setDomainId(domainId);
            insertRoom.setRoomName("直播间");
            insertRoom.setRoomType(1);
            insertRoom.setForbidStatus(0);
            insertRoom.setOpenRoom(1);
            insertRoom.setRoomLogo("");
            insertRoom.setRoomPcBg("");
            insertRoom.setRoomMobileBg("");
            insertRoom.setRemark("默认房间");
            if (roomMapper.insert(insertRoom)<1){
                throw new ManagerException("创建房间失败");
            }
            //创建默认管理员角色
            Role insertRole = new Role();
            insertRole.setDomainId(domainId);
            List<Long> ids = permissionMapper.queryAllIds();
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0;i<ids.size();i++){
                stringBuilder.append(ids.get(i));
                if (i<ids.size()-1){
                    stringBuilder.append(",");
                }
            }
            insertRole.setPermIds(stringBuilder.toString());
            insertRole.setRoleName("超级管理员");
            insertRole.setRoleIcon("user-icon-admin");
            insertRole.setRemark("超级管理员");
            if (roleMapper.insert(insertRole)<1){
                throw new ManagerException("创建角色失败");
            }

            //创建默认计划员
            Role planRole = new Role();
            planRole.setDomainId(domainId);
            planRole.setRoleName("计划员");
            planRole.setRoleIcon("user-icon-plan");
            planRole.setRemark("计划员");
            if (roleMapper.insert(planRole)<1){
                throw new ManagerException("创建计划员角色失败");
            }

            //创建默认会员
            Role vipRole = new Role();
            vipRole.setDomainId(domainId);
            vipRole.setRoleName("会员");
            vipRole.setRoleIcon("user-icon-vip");
            vipRole.setRemark("会员");
            if (roleMapper.insert(vipRole)<1){
                throw new ManagerException("创建会员角色失败");
            }

            User insertUser = new User();
            insertUser.setDomainId(domainId);
            insertUser.setUserName("admin");
            int icon = RandomUtils.getRandom(25);
            insertUser.setIcon("userIcon"+icon);
            insertUser.setRoleId(insertRole.getId());
            insertUser.setRoomId(insertRoom.getId());
            insertUser.setStatus(1);
            insertUser.setRemark("初始账号");
            insertUser.setSalt(UUID.randomUUID().toString().replace("-", ""));
            insertUser.setPassword(Md5Manager.md5("admin123456", insertUser.getSalt()));
            insertUser.setDelFlag(1);
            if (userMapper.insert(insertUser)<1){
                throw new ManagerException("创建初始用户失败");
            }
        }
        return false;
    }

    @Override
    public boolean update(DomainConfig domainConfig) {
        try {
            if (domainConfigMapper.update(domainConfig) > 0) {
                String idkey = AppConfig.DomainCacheById + domainConfig.getId();
                String domainkey = AppConfig.DomainCacheByDomain + domainConfig.getDomainName();
                try {
                    RedisManager.removeObject(idkey);
                    RedisManager.removeObject(domainkey);
                } catch (Exception e) {
                }
                return true;
            }
        } catch (Exception e) {
            logger.error("DomainConfigManagerImpl update异常", e);
            throw new ManagerException(e);
        }
        return false;
    }

    @Override
    public DomainConfig queryById(Long id) {
        try {
            return domainConfigMapper.queryById(id);
        } catch (Exception e) {
            logger.error("DomainConfigManagerImpl queryById异常",e);
            throw new ManagerException(e);
        }
    }

    @Override
    public DomainConfig queryByIdCache(Long id) {
        try {
            String key = AppConfig.DomainCacheById + id;
            try {
                if (RedisManager.existsObject(key)) {
                    DomainConfig cache = (DomainConfig) RedisManager.getObject(key);
                    return cache;
                }
            } catch (Exception e) {
                logger.error("redis 服务域名 查询异常",e);
            }
            DomainConfig domainConfig = domainConfigMapper.queryById(id);
            try {
                RedisManager.putObject(key, domainConfig);
            } catch (Exception e) {
            }
            return domainConfig;
        } catch (Exception e) {
            logger.error("DomainConfigManagerImpl queryByIdCache异常", e);
            throw new ManagerException(e);
        }
    }

    @Override
    public DomainConfig queryByDomainName(String domainName) {
        try {
            DomainConfig domainConfig = domainConfigMapper.queryByDomainName(domainName);
            if (domainConfig !=null && domainConfig.getParentId()!=null && domainConfig.getParentId()>0){
                return domainConfigMapper.queryById(domainConfig.getParentId());
            }
            return domainConfig;
        } catch (Exception e) {
            logger.error("DomainConfigManagerImpl queryByDomainName异常",e);
            throw new ManagerException(e);
        }
    }

    @Override
    public DomainConfig queryByDomainNameCache(String domainName) {
        DomainConfig domainConfig = null;
        String key = AppConfig.DomainCacheByDomain + domainName;
        try {
            if (RedisManager.existsObject(key)) {
                domainConfig = (DomainConfig) RedisManager.getObject(key);
                return domainConfig;
            }
        } catch (Exception e) {
            logger.error("redis查询异常",e);
        }
        try {
            domainConfig = queryByDomainName(domainName);
        } catch (Exception e) {
            logger.error("DomainConfigManagerImpl queryByDomainNameCache异常", e);
            throw new ManagerException(e);
        }
        try {
            RedisManager.putObject(key, domainConfig);
        } catch (Exception e) {
            logger.error("redis保存异常",e);
        }
        return domainConfig;
    }

    @Override
    public PageResult<DomainConfig> queryPageDomainConfig(DomainConfigQuery query) {
        PageResult<DomainConfig> pageResult = new PageResult<>();
        try {
            List<DomainConfig> list = new ArrayList<>();
            int count = domainConfigMapper.queryPageCount(query);
            if (count>0){
                list = domainConfigMapper.queryPageList(query);
            }
            pageResult.setData(list);
            pageResult.setiTotalDisplayRecords(count);
            pageResult.setiTotalRecords(count);
            pageResult.setPageNo(query.getCurrentPage());
            pageResult.setiDisplayLength(query.getPageSize());
        } catch (Exception e) {
            logger.error("DomainConfigManagerImpl queryPageDomainConfig异常",e);
            throw new ManagerException(e);
        }
        return pageResult;
    }

    @Override
    public List<DomainConfig> queryList() {
        try {
            return domainConfigMapper.queryList();
        } catch (Exception e) {
            logger.error("DomainConfigManagerImpl queryList异常",e);
            throw new ManagerException(e);
        }
    }

    @Override
    public List<DomainConfig> queryParentList() {
        try {
            return domainConfigMapper.queryParentList();
        } catch (Exception e) {
            logger.error("DomainConfigManagerImpl queryParentList异常",e);
            throw new ManagerException(e);
        }
    }

    @Override
    public boolean remove(Long id) {
        try {
            DomainConfig domainConfig = domainConfigMapper.queryById(id);
            if (domainConfig!=null) {
                if (domainConfigMapper.remove(id) > 0) {
                    try {
                        String idkey = AppConfig.DomainCacheById + domainConfig.getId();
                        String domainkey = AppConfig.DomainCacheByDomain + domainConfig.getDomainName();
                        RedisManager.removeObject(idkey);
                        RedisManager.removeObject(domainkey);
                    } catch (Exception e) {
                    }
                    return true;
                }
            }
        } catch (Exception e) {
            logger.error("DomainConfigManagerImpl remove异常",e);
            throw new ManagerException(e);
        }
        return false;
    }
}
