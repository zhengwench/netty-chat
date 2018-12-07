package chat.core.db.manager;

import chat.core.common.domain.PageResult;
import chat.core.db.model.ReceiveRedbag;
import chat.core.db.model.query.ReceiveRedBagQuery;

import java.util.List;

/**
 * @auther a-de
 * @date 2018/11/7 12:59
 */
public interface ReceiveRedbagManager {
    boolean insert(ReceiveRedbag receiveRedbag);

    boolean update(ReceiveRedbag receiveRedbag);

    PageResult<ReceiveRedbag> queryPage(ReceiveRedBagQuery query);

    ReceiveRedbag receiveRedbag(String domain, Long redBagId, Long userId);

    ReceiveRedbag queryById(Long id);

    ReceiveRedbag queryByIdAndDomainId(Long id, Long domainId);

    List<ReceiveRedbag> queryUserReceived(Long domainId, Long userId);

    ReceiveRedbag userReceivedRedbag(String domain, Long redBagId, Long userId);
}
