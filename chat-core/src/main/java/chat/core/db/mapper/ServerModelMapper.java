package chat.core.db.mapper;

import chat.core.db.model.ServerModel;
import chat.core.db.model.query.ServerModelQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auther a-de
 * @date 2018/11/24 19:52
 */
public interface ServerModelMapper {
    int insert(ServerModel serverModel);

    int update(ServerModel serverModel);

    int queryPageCount(@Param("query") ServerModelQuery query);

    List<ServerModel> queryPageList(@Param("query") ServerModelQuery query);

    int delete(@Param("id") Long id);

    ServerModel queryById(@Param("id") Long id);
}
