package chat.core.db.manager;

import chat.core.common.domain.PageResult;
import chat.core.db.model.Redbag;
import chat.core.db.model.query.RedBagQuery;

/**
 * @auther a-de
 * @date 2018/11/7 13:03
 */
public interface RedbagManager {
    boolean insert(Redbag redbag);

    boolean update(Redbag redbag);

    PageResult<Redbag> queryPage(RedBagQuery query);

    Redbag createRedbag(String domian, Redbag redbag);
}
