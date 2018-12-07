package chat.core.db.mapper;

import chat.core.db.model.ReceiveRedbag;
import chat.core.db.model.query.ReceiveRedBagQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auther a-de
 * @date 2018/11/6 14:47
 */
public interface ReceiveRedbagMapper {
    int insert(ReceiveRedbag receiveRedbag);

    int update(ReceiveRedbag receiveRedbag);

    int queryPageCount(@Param("query") ReceiveRedBagQuery query);

    List<ReceiveRedbag> queryPageList(@Param("query") ReceiveRedBagQuery query);

    ReceiveRedbag queryById(@Param("id") Long id);

    ReceiveRedbag queryByIdAndDomainId(@Param("id") Long id, @Param("domainId") Long domainId);

    List<ReceiveRedbag> queryUserReceived(@Param("domainId") Long domainId, @Param("receiveUserId") Long receiveUserId);

    ReceiveRedbag userReceivedRedbag(@Param("domainId") Long domainId, @Param("receiveUserId") Long receiveUserId, @Param("redbagId") Long redbagId);
}
