package chat.core.db.mapper;

import chat.core.db.model.Room;
import chat.core.db.model.query.RoomQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auther a-de
 * @date 2018/11/6 14:49
 */
public interface RoomMapper {
    int insert(Room room);

    int update(Room room);

    Room queryById(@Param("id") Long id);

    Room queryByIdAndDomainId(@Param("id") Long id, @Param("domainId") Long domainId);

    int queryPageCount(@Param("query") RoomQuery query);

    List<Room> queryPageList(@Param("query") RoomQuery query);

    List<Room> queryList(@Param("domainId") Long domainId);

    Room queryDefaultRoom(@Param("domainId") Long domainId);

    int remove(@Param("id") Long id);
}
