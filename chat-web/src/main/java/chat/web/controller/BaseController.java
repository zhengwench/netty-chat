package chat.web.controller;

import chat.core.common.domain.AppConfig;
import chat.core.common.domain.Constants;
import chat.core.common.domain.ManagerException;
import chat.core.db.manager.DomainConfigManager;
import chat.core.db.manager.UserManager;
import chat.core.db.model.DomainConfig;
import chat.core.db.model.User;
import chat.web.auth.SysAuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.HOST;

/**
 * @auther a-de
 * @date 2018/11/6 14:01
 */
public class BaseController {
    @Autowired
    private DomainConfigManager domainConfigManager;

    @Autowired
    private UserManager userManager;

    protected DomainConfig getDomainConfig() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        String domain = request.getHeader(HOST);
        DomainConfig domainConfig = domainConfigManager.queryByDomainName(domain);
        if (domainConfig == null || domainConfig.getStatus() != 1 || new Date().after(domainConfig.getEndTime()) ||
                new Date().before(domainConfig.getStartTime())) {
            throw new ManagerException("【" + domain + "】" + AppConfig.DomainError);
        }
        return domainConfig;
    }

    protected String[] getPermsArray(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        if (request.getAttribute("permArray")!=null){
            String[] permArray = (String[]) request.getAttribute("permArray");
            return permArray;
        }
        return null;
    }

    protected SysAuthUser getUserInfo(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        return (SysAuthUser)request.getAttribute("userInfo");
    }

    protected User getUserInfo(Long domainId){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
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
            return null;
        }
        User user = userManager.queryByDomainIdAndToken(domainId, cookie.getValue());
        return user;
    }

    public static void main(String[] args){
        String aa = UUID.randomUUID().toString().replace("-","");
        System.out.print("aaa:"+aa);
    }

}
