package chat.console.controller;

import chat.console.common.UserInfo;
import chat.core.common.domain.Constants;
import chat.core.common.utility.Md5Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @auther a-de
 * @date 2018/12/11 15:22
 */
public class BaseController {
    @Autowired
    private UserInfo userInfo;

    protected boolean isLogined() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        Cookie cookie = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (int i = 0; i < cookies.length; i++) {
                if (Constants.CONSOLE_USER_TOKEN.equals(cookies[i].getName())) {
                    cookie = cookies[i];
                }
            }
        }
        if (cookie == null) {
            return false;
        }

        String resultStr = userInfo.getUsername() + "_" + userInfo.getPassword();
        if (Md5Manager.md5(resultStr).equals(cookie.getValue())) {
            return true;
        }
        return false;
    }
}
