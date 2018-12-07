package chat.core.db.manager;

import chat.core.common.domain.PageResult;
import chat.core.db.model.UserRobot;
import chat.core.db.model.query.UserRobotQuery;

import java.util.List;

/**
 * @auther a-de
 * @date 2018/11/19 13:35
 */
public interface UserRobotManager {
    boolean insert(UserRobot userRobot);

    void insertBatch(Integer count, Long domainId, Long roomId);

    boolean delete(Long id);

    void deleteBatch(List<Long> list, Long domainId);

    PageResult<UserRobot> queryPage(UserRobotQuery query);

    List<UserRobot> queryList(Integer count);
}
