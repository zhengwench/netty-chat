package chat.core.db.mapper;


import chat.core.db.model.RoomUser;

/**
 * @auther a-de
 * @date 2018/11/6 14:48
 */
public interface RoomUserMapper {
    int insert(RoomUser roomManager);

    int update(RoomUser roomManager);
}
