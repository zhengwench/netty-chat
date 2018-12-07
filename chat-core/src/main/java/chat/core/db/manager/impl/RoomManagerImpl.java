package chat.core.db.manager.impl;

import chat.core.common.domain.AppConfig;
import chat.core.common.domain.ManagerException;
import chat.core.common.domain.PageResult;
import chat.core.common.redis.RedisManager;
import chat.core.db.manager.RoomManager;
import chat.core.db.mapper.RoomMapper;
import chat.core.db.model.Room;
import chat.core.db.model.query.RoomQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther a-de
 * @date 2018/11/7 12:54
 */
@Component
public class RoomManagerImpl implements RoomManager {
    private Logger logger = LoggerFactory.getLogger(RoomManagerImpl.class);

    @Autowired
    private RoomMapper roomMapper;

    @Override
    public boolean insert(Room room) {
        try {
            if (roomMapper.insert(room) > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("房间insert 异常", e);
            throw new ManagerException(e);
        }
    }

    @Override
    public boolean update(Room room) {
        try {
            if (roomMapper.update(room) > 0) {
                try {
                    String idkey = AppConfig.RoomCacheById + room.getId();
                    RedisManager.removeObject(idkey);
                } catch (Exception e) {
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("房间update 异常", e);
            throw new ManagerException(e);
        }
    }

    @Override
    public Room queryById(Long id) {
        try {
            return roomMapper.queryById(id);
        } catch (Exception e) {
            logger.error("房间queryById 异常", e);
            throw new ManagerException(e);
        }
    }

    @Override
    public Room queryByIdAndDomainId(Long id, Long domainId) {
        try {
            return roomMapper.queryByIdAndDomainId(id,domainId);
        } catch (Exception e) {
            logger.error("房间queryByIdAndDomainId 异常", e);
            throw new ManagerException(e);
        }
    }

    @Override
    public Room queryByIdCache(Long id) {
        try {
            String key = AppConfig.RoomCacheById + id;
            try {
                if (RedisManager.existsObject(key)) {
                    Room room = (Room) RedisManager.getObject(key);
                    return room;
                }
            } catch (Exception e) {
                logger.error("redis id查询房间信息异常",e);
            }
            Room room = roomMapper.queryById(id);
            try {
                RedisManager.putObject(key, room);
            } catch (Exception e) {
            }
            return room;
        } catch (Exception e) {
            logger.error("房间queryByIdCache 异常", e);
            throw new ManagerException(e);
        }
    }

    @Override
    public PageResult queryPage(RoomQuery query) {
        PageResult<Room> pageResult = new PageResult<>();
        try {
            List<Room> list = new ArrayList<>();
            int count = roomMapper.queryPageCount(query);
            if (count > 0) {
                list = roomMapper.queryPageList(query);
            }

            pageResult.setData(list);
            pageResult.setiTotalDisplayRecords(count);
            pageResult.setiTotalRecords(count);
            pageResult.setPageNo(query.getCurrentPage());
            pageResult.setiDisplayLength(query.getPageSize());
        } catch (Exception e) {
            logger.error("房间queryPage异常", e);
            throw new ManagerException(e);
        }
        return pageResult;
    }

    @Override
    public List<Room> queryList(Long domainId) {
        try {
            return roomMapper.queryList(domainId);
        } catch (Exception e) {
            logger.error("房间queryList异常", e);
            throw new ManagerException(e);
        }
    }

    @Override
    public Room queryDefaultRoom(Long domainId) {
        try {
            return roomMapper.queryDefaultRoom(domainId);
        } catch (Exception e) {
            logger.error("房间queryByRoomType异常", e);
            throw new ManagerException(e);
        }
    }

    @Override
    public boolean remove(Long id) {
        try {
            if (roomMapper.remove(id) > 0) {
                try {
                    String idkey = AppConfig.RoomCacheById + id;
                    RedisManager.removeObject(idkey);
                } catch (Exception e) {
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("房间remove异常", e);
            throw new ManagerException(e);
        }
    }
}
