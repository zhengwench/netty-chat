package chat.core.db.mapper;

import chat.core.db.model.BlackIp;
import chat.core.db.model.query.BlackIpQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auther a-de
 * @date 2018/11/6 21:03
 */
public interface BlackIpMapper {
    int insert(BlackIp blackIp);

    int delete(@Param("id") Long id, @Param("domainId") Long domainId);

    BlackIp queryById(@Param("id") Long id);

    BlackIp queryByIdAndDomainId(@Param("id") Long id, @Param("domainId") Long domainId);

    int queryPageCount(@Param("query") BlackIpQuery query);

    List<BlackIp> queryPageList(@Param("query") BlackIpQuery query);

    BlackIp queryByIp(@Param("domainId") Long domainId, @Param("ip") String ip);
}
