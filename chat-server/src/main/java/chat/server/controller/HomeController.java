package chat.server.controller;

import com.alibaba.fastjson.JSONObject;
import chat.core.common.domain.ResultDo;
import chat.core.common.redis.RedisManager;
import chat.core.common.utility.Md5Manager;
import chat.core.db.manager.DomainConfigManager;
import chat.core.db.manager.RoleManager;
import chat.core.db.manager.RoomManager;
import chat.core.db.manager.UserManager;
import chat.core.db.model.DomainConfig;
import chat.core.db.model.Role;
import chat.core.db.model.Room;
import chat.core.db.model.User;
import chat.server.channel.DomainChannelMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.HOST;

/**
 * @auther a-de
 * @date 2018/11/4 22:08
 */
@Controller
public class HomeController {
    @Autowired
    private UserManager userManager;

    @Autowired
    private DomainConfigManager domainConfigManager;

    @Autowired
    private RoomManager roomManager;

    @Autowired
    private RoleManager roleManager;

    @RequestMapping(value = "/index")
    String index(HttpServletRequest req, Model model, String token, String roomId) {
        String domain = req.getHeader(HOST);
        DomainConfig domainConfig = domainConfigManager.queryByDomainName(domain);
        String websocket = domainConfig.getWebsocketUrl();
        model.addAttribute("token", token);
        model.addAttribute("domain", domain);
        model.addAttribute("websocket", websocket);
        if (StringUtils.isEmpty(roomId)) {
            Room room = roomManager.queryDefaultRoom(domainConfig.getId());
            roomId = room.getId() + "";
        }
        model.addAttribute("roomId", roomId);
        return "index";
    }

    @ResponseBody
    @RequestMapping(value = "/login")
    ResultDo login(HttpServletRequest req, String username, String password){
        ResultDo resultDo = new ResultDo();
        String domain = req.getHeader(HOST);
        DomainConfig domainConfig = domainConfigManager.queryByDomainName(domain);
        User user = userManager.queryByUserName(username,domainConfig.getId());
        if (user==null){
            resultDo.setErrorDesc("账号或密码错误");
            return resultDo;
        }
        String p = Md5Manager.md5(password,user.getSalt());
        if (!user.getPassword().equals(p)){
            resultDo.setErrorDesc("账号或密码错误");
            return resultDo;
        }
        user.setToken(UUID.randomUUID().toString().replace("-",""));
        if (userManager.update(user)){
            Role role = roleManager.queryById(user.getRoleId());
            JSONObject response = new JSONObject();
            response.put("userId",user.getId());
            response.put("token",user.getToken());
            response.put("userName",user.getUserName());
            response.put("userIcon",user.getIcon());
            if (role != null){
                response.put("roleName",role.getRoleName());
            }
            resultDo.setResult(response);
            return resultDo;
        }
        return resultDo;
    }

    @ResponseBody
    @RequestMapping(value = "/index1")
    String index1(){
        DomainChannelMap.sendAllMsg("localhost","hello world");
        User user = userManager.queryById(1L);
        return user.getUserName();
    }

    @ResponseBody
    @RequestMapping(value = "/index2")
    String index2(){
        RedisManager.putObject("abc","abc");
        String value =(String) RedisManager.getObject("abc");
        return value;
    }
}
