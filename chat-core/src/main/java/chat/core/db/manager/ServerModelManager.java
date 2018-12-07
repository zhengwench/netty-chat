package chat.core.db.manager;

import chat.core.common.domain.PageResult;
import chat.core.db.model.ServerModel;
import chat.core.db.model.query.ServerModelQuery;

/**
 * @auther a-de
 * @date 2018/11/24 20:17
 */
public interface ServerModelManager {
    boolean insert(ServerModel serverModel);

    boolean update(ServerModel serverModel);

    PageResult<ServerModel> queryPage(ServerModelQuery query);

    boolean delete(Long id);

    ServerModel queryById(Long id);
}
