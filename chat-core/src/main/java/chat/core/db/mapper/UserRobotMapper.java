package chat.core.db.mapper;

import chat.core.db.model.UserRobot;
import chat.core.db.model.dto.UserOnlineDto;
import chat.core.db.model.query.UserRobotQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auther a-de
 * @date 2018/11/19 12:50
 */
public interface UserRobotMapper {
    int insert(UserRobot userRobot);

    int insertBatch(List<UserRobot> list);

    int delete(@Param("id") Long id);

    int deleteBatch(@Param("list") List<Long> list, @Param("domainId") Long domainId);

    List<UserRobot> queryPageList(@Param("query") UserRobotQuery query);

    int queryPageCount(@Param("query") UserRobotQuery query);

    List<UserRobot> queryList(@Param("count") Integer count);

    List<UserOnlineDto> queryOnlineList(@Param("domainId") Long domainId, @Param("roomId") Long roomId, @Param("count") Integer count);
}
