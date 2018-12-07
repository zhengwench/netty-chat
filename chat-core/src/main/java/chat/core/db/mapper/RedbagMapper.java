package chat.core.db.mapper;

import chat.core.db.model.Redbag;
import chat.core.db.model.query.RedBagQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auther a-de
 * @date 2018/11/6 14:48
 */
public interface RedbagMapper {
    int insert(Redbag redbag);

    int update(Redbag redbag);

    int queryPageCount(@Param("query") RedBagQuery query);

    List<Redbag> queryPageList(@Param("query") RedBagQuery query);

    Redbag queryById(@Param("id") Long id);

    Redbag queryByIdForLock(@Param("id") Long id);
}
