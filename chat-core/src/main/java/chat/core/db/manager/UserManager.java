package chat.core.db.manager;

import chat.core.common.domain.PageResult;
import chat.core.common.domain.ResultDo;
import chat.core.db.model.User;
import chat.core.db.model.dto.UserDto;
import chat.core.db.model.dto.UserInfoDto;
import chat.core.db.model.query.UserQuery;

/**
 * @auther a-de
 * @date 2018/10/11 20:49
 */
public interface UserManager {
    User queryById(Long id);

    ResultDo logined(Long domainId, String username, String password, boolean sys);

    boolean insert(User user);

    boolean update(User user);

    User queryByDomainIdAndId(Long id, Long domainId);

    PageResult<User> queryPage(UserQuery query);

    PageResult<UserDto> queryPageDto(UserQuery query);

    User queryByUserName(String userName, Long domainId);

    boolean remove(Long id);

    User queryByDomainIdAndToken(Long domainId, String token);

    User queryByDomainIdAndSysToken(Long domainId, String sysToken);

    boolean updateLoginIp(Long id, String ip);

    ResultDo onlineUser(Long domainId, Long roomId);

    boolean removeToken(Long id);

    boolean removeSysToken(Long id);

    UserInfoDto queryUserInfo(Long domainId, String token);
}
