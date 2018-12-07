package chat.core.db.manager;

import chat.core.common.domain.PageResult;
import chat.core.db.model.Room;
import chat.core.db.model.query.RoomQuery;

import java.util.List;

/**
 * @auther a-de
 * @date 2018/11/7 12:54
 */
public interface RoomManager {
    boolean insert(Room room);

    boolean update(Room room);

    Room queryById(Long id);

    Room queryByIdAndDomainId(Long id, Long domainId);

    Room queryByIdCache(Long id);

    PageResult queryPage(RoomQuery query);

    List<Room> queryList(Long domainId);

    Room queryDefaultRoom(Long domainId);

    boolean remove(Long id);
}
