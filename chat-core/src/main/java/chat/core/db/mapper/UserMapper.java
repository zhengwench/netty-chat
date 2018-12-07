package chat.core.db.mapper;

import chat.core.db.model.User;
import chat.core.db.model.dto.UserDto;
import chat.core.db.model.dto.UserInfoDto;
import chat.core.db.model.dto.UserOnlineDto;
import chat.core.db.model.query.UserQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auther a-de
 * @date 2018/10/10 21:02
 */
public interface UserMapper {
    int insert(User user);

    int update(User user);

    User queryById(@Param("id") Long id);

    User queryByDomainIdAndId(@Param("id") Long id, @Param("domainId") Long domainId);

    List<User> queryPageList(@Param("query") UserQuery query);

    List<UserDto> queryDtoPageList(@Param("query") UserQuery query);

    int queryPageCount(@Param("query") UserQuery query);

    User queryByUserName(@Param("userName") String userName, @Param("domainId") Long domainId);

    int remove(@Param("id") Long id);

    User queryByDomainIdAndToken(@Param("domainId") Long domainId, @Param("token") String token);

    User queryByDomainIdAndSysToken(@Param("domainId") Long domainId, @Param("sysToken") String sysToken);

    int updateLoginIp(@Param("loginIp") String loginIp, @Param("id") Long id);

    List<UserOnlineDto> queryOnlineList(@Param("domainId") Long domainId, @Param("roomId") Long roomId, @Param("count") Integer count);

    int removeToken(@Param("id") Long id);

    int removeSysToken(@Param("id") Long id);

    UserInfoDto queryUserInfo(@Param("domainId") Long domainId, @Param("token") String token);
}
