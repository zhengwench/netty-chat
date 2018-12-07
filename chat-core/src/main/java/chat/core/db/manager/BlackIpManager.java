package chat.core.db.manager;


import chat.core.common.domain.PageResult;
import chat.core.db.model.BlackIp;
import chat.core.db.model.query.BlackIpQuery;

/**
 * @auther a-de
 * @date 2018/11/7 12:37
 */
public interface BlackIpManager {
    boolean insert(BlackIp blackIp);

    boolean delete(Long domainId, Long id);

    BlackIp queryById(Long id);

    BlackIp queryByIdAndDomainId(Long id, Long domainId);

    PageResult<BlackIp> queryPage(BlackIpQuery query);

    BlackIp queryByIp(Long domainId, String ip);

    BlackIp queryByIpCache(Long domainId, String ip);
}
