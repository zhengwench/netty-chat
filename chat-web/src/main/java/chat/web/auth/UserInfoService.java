package chat.web.auth;

import com.shuangying.core.common.domain.Constants;
import com.shuangying.core.db.manager.DomainConfigManager;
import com.shuangying.core.db.manager.RoleManager;
import com.shuangying.core.db.manager.UserManager;
import com.shuangying.core.db.model.DomainConfig;
import com.shuangying.core.db.model.User;
import com.shuangying.web.exception.SysAuthException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpHeaders.HOST;

/**
 * @auther a-de
 * @date 2018/11/20 21:22
 */
@Aspect
@Component
public class UserInfoService {
    @Autowired
    private UserManager userManager;

    @Autowired
    private DomainConfigManager domainConfigManager;

    @Autowired
    private RoleManager roleManager;

    @Pointcut("@annotation(com.shuangying.web.auth.UserInfo)")
    public void methodPointcut(){

    }

    @Before("@annotation(userInfo)")
    public void before(UserInfo userInfo) throws SysAuthException {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        Cookie cookie = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (int i = 0; i < cookies.length; i++) {
                if (Constants.CHAT_USER_TOKEN.equals(cookies[i].getName())) {
                    cookie = cookies[i];
                }
            }
        }
        if (cookie == null) {
            throw new SysAuthException("未登陆");
        }
        String domain = request.getHeader(HOST);
        DomainConfig domainConfig = domainConfigManager.queryByDomainName(domain);
        if (domainConfig == null) {
            throw new SysAuthException("域名未开通");
        }
        User user = userManager.queryByDomainIdAndToken(domainConfig.getId(), cookie.getValue());
        if (user == null) {
            throw new SysAuthException("未登录");
        }

        SysAuthUser sysAuthUser = new SysAuthUser();
        sysAuthUser.setUserId(user.getId());
        sysAuthUser.setUserName(user.getUserName());
        sysAuthUser.setRoleId(user.getRoleId());
        sysAuthUser.setStatus(user.getStatus());
        sysAuthUser.setToken(user.getToken());
        request.setAttribute("userInfo",sysAuthUser);
    }
}
