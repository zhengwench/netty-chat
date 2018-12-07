package chat.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import chat.core.common.domain.AppConfig;
import chat.core.common.domain.PageResult;
import chat.core.common.domain.ResultDo;
import chat.core.common.redis.RedisManager;
import chat.core.common.utility.DateUtils;
import chat.core.common.utility.Md5Manager;
import chat.core.common.utility.RandomUtils;
import chat.core.db.manager.BlackIpManager;
import chat.core.db.manager.ReceiveRedbagManager;
import chat.core.db.manager.RedbagManager;
import chat.core.db.manager.RoleManager;
import chat.core.db.manager.RoomManager;
import chat.core.db.manager.SystemDictManager;
import chat.core.db.manager.UserManager;
import chat.core.db.manager.UserRobotManager;
import chat.core.db.model.BlackIp;
import chat.core.db.model.DomainConfig;
import chat.core.db.model.ReceiveRedbag;
import chat.core.db.model.Redbag;
import chat.core.db.model.Role;
import chat.core.db.model.Room;
import chat.core.db.model.SystemDict;
import chat.core.db.model.User;
import chat.core.db.model.dto.PermissionRoleDto;
import chat.core.db.model.dto.UserDto;
import chat.core.db.model.query.BlackIpQuery;
import chat.core.db.model.query.ReceiveRedBagQuery;
import chat.core.db.model.query.RedBagQuery;
import chat.core.db.model.query.RoleQuery;
import chat.core.db.model.query.RoomQuery;
import chat.core.db.model.query.SystemDictQuery;
import chat.core.db.model.query.UserQuery;
import chat.core.db.model.query.UserRobotQuery;
import chat.web.auth.SysAuth;
import chat.web.auth.SysAuthUser;
import chat.web.dto.MonitorOnlineDto;
import chat.web.http.HttpRequestClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @auther a-de
 * @date 2018/11/6 14:02
 */
@Controller
@RequestMapping(value = "/sys")
public class SysController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(SysController.class);
    @Autowired
    private RoleManager roleManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    private BlackIpManager blackIpManager;

    @Autowired
    private RoomManager roomManager;

    @Autowired
    private RedbagManager redbagManager;

    @Autowired
    private ReceiveRedbagManager receiveRedbagManager;

    @Autowired
    private SystemDictManager systemDictManager;

    @Autowired
    private UserRobotManager userRobotManager;

    @Autowired
    private HttpRequestClient httpRequestClient;

    @RequestMapping(value = "/index")
    @SysAuth(rule = "")
    public String index(Model model) {
        SysAuthUser sysAuthUser = getUserInfo();
        List<PermissionRoleDto> permissionRoleDtos = roleManager.queryRolePerm(sysAuthUser.getRoleId(),1);
        model.addAttribute("perms", permissionRoleDtos);
        model.addAttribute("user", sysAuthUser);
        return "/sys/index";
    }

    @RequestMapping(value = "/welcome")
    public String welcome(Model model) {
        DomainConfig domainConfig = getDomainConfig();
        model.addAttribute("endtime",domainConfig.getEndTime());
        Integer remainDays = DateUtils.daysBetween(new Date(),domainConfig.getEndTime());
        model.addAttribute("remainDays",remainDays);
        return "/sys/welcome";
    }

    @ResponseBody
    @SysAuth(rule = "")
    @RequestMapping(value = "/monitorOnline")
    public ResultDo monitorOnline(){
        ResultDo resultDo = new ResultDo();
        DomainConfig domainConfig = getDomainConfig();
        ResultDo responseDo = new ResultDo();
        String rediskey = domainConfig.getDomainName()+"monitorOnline";
        try {
            if (RedisManager.existsObject(rediskey)){
                List<MonitorOnlineDto> list = (List<MonitorOnlineDto>)RedisManager.getObject(rediskey);
                if (list!=null && list.size()>0){
                    resultDo.setList(list);
                }
                return resultDo;
            }
        } catch (Exception e) {
            logger.error("redis查询monitorOnline异常",e);
        }
        List<Room> roomList = roomManager.queryList(domainConfig.getId());
        Set<String> httpUrls = new HashSet<>();
        httpUrls.add(domainConfig.getHttpUrl());
        if(roomList!=null && roomList.size()>0){
            for (Room r : roomList){
                if (!StringUtils.isEmpty(r.getHttpUrl())){
                    httpUrls.add(r.getHttpUrl());
                }
            }
        }
        Map<String,Integer> resultMap = new HashMap<>();
        if (httpUrls!=null && httpUrls.size()>0){
            for (String w : httpUrls){
                String url = w+"/api/monitorOnline";
                Map<String,Object> postMap = new HashMap<>();
                postMap.put("domain",domainConfig.getDomainName());
                String response = httpRequestClient.doPost(url,postMap,null);
                responseDo = JSON.parseObject(response,ResultDo.class);
                if (responseDo!=null && responseDo.isSuccess()){
                    try {
                        resultMap.putAll((Map) responseDo.getResult());
                    } catch (Exception e) {
                    }
                }
            }
        }
        List<MonitorOnlineDto> list = new ArrayList<>();
        MonitorOnlineDto monitorOnlineDto = null;
        if (resultMap!=null && resultMap.size()>0){
            for (Object key:resultMap.keySet()) {
                try {
                    Room room = roomManager.queryByIdCache(Long.parseLong(key+""));
                    if (room!=null){
                        monitorOnlineDto = new MonitorOnlineDto();
                        monitorOnlineDto.setRoomName(room.getRoomName());
                        monitorOnlineDto.setOnlineCount(resultMap.get(key));
                        list.add(monitorOnlineDto);
                    }
                } catch (Exception e) {
                }
            }
        }
        resultDo.setList(list);
        try {
            RedisManager.putObject(rediskey,list);
            RedisManager.expireObject(rediskey,30);
        } catch (Exception e) {
            logger.error("redis monitorOnline插入异常",e);
        }
        return resultDo;
    }

    @RequestMapping(value = "/login")
    public String login() {
        return "/sys/login";
    }

    @ResponseBody
    @RequestMapping(value = "/logined")
    public ResultDo logined(HttpServletRequest req, String username, String password, String verifyCode) {
        ResultDo resultDo = new ResultDo<>();
        DomainConfig domainConfig = getDomainConfig();
//        String code = req.getSession().getAttribute(AppConfig.LoginVerifyCode) + "";
//        if (StringUtils.isEmpty(verifyCode)) {
//            resultDo.setErrorDesc("验证码为空");
//            return resultDo;
//        }
//        if (!verifyCode.toLowerCase().equals(code.toLowerCase())) {
//            resultDo.setErrorDesc("验证码不正确");
//            return resultDo;
//        }
        resultDo = userManager.logined(domainConfig.getId(), username, password,true);
        return resultDo;
    }

    @SysAuth(rule = "")
    @ResponseBody
    @RequestMapping(value = "/updateCurrentPassword")
    public ResultDo updateCurrentPassword(String editPassword){
        ResultDo resultDo = new ResultDo();
        DomainConfig domainConfig = getDomainConfig();
        SysAuthUser sysAuthUser = getUserInfo();
        User user = userManager.queryByDomainIdAndId(sysAuthUser.getUserId(),domainConfig.getId());
        if (user == null){
            resultDo.setErrorDesc("数据问题");
            return resultDo;
        }
        user.setPassword(Md5Manager.md5(editPassword, user.getSalt()));
        resultDo.setSuccess(userManager.update(user));
        return resultDo;
    }

    @SysAuth(rule = "sys:user")
    @RequestMapping(value = "/user")
    public String user(Model model){
        DomainConfig domainConfig = getDomainConfig();
        String[] permArray = getPermsArray();
        model.addAttribute("permArray", permArray);
        List<Role> roles = roleManager.queryList(domainConfig.getId());
        List<Room> rooms = roomManager.queryList(domainConfig.getId());
        model.addAttribute("roles", roles);
        model.addAttribute("rooms", rooms);
        return "/sys/user";
    }


    @ResponseBody
    @SysAuth(rule = "sys:user")
    @RequestMapping(value = "/ajaxUser")
    public PageResult<UserDto> ajaxUser(UserQuery query) {
        DomainConfig domainConfig = getDomainConfig();
        query.setDomainId(domainConfig.getId());
        PageResult<UserDto> pageResult = userManager.queryPageDto(query);
        return pageResult;
    }

    @SysAuth(rule = "sys:addUser")
    @RequestMapping(value = "/addUser")
    public String addUser(Model model, Long id) {
        DomainConfig domainConfig = getDomainConfig();
        List<Role> roles = roleManager.queryList(domainConfig.getId());
        model.addAttribute("roles", roles);
        User user = userManager.queryByDomainIdAndId(id,domainConfig.getId());
        if (user == null){
            user = new User();
        }
        model.addAttribute("user", user);
        List<Room> roomList = roomManager.queryList(domainConfig.getId());
        model.addAttribute("roomList",roomList);
        if (id!=null && id>0){
            return "/sys/updateUser";
        }
        return "/sys/addUser";
    }

    @ResponseBody
    @RequestMapping(value = "/saveUser")
    @SysAuth(rule = "sys:addUser")
    public ResultDo saveUser(User user) {
        ResultDo resultDo = new ResultDo();
        DomainConfig domainConfig = getDomainConfig();
        if (user.getId() != null && user.getId() > 0) {
            User old = userManager.queryByDomainIdAndId(user.getId(),domainConfig.getId());
            if (old == null) {
                resultDo.setErrorDesc("数据不存在");
                return resultDo;
            }
            old.setUserName(user.getUserName());
            old.setRemark(user.getRemark());
            resultDo.setSuccess(userManager.update(old));
            return resultDo;
        }
        User insert = userManager.queryByUserName(user.getUserName(),domainConfig.getId());
        if (insert != null){
            resultDo.setErrorDesc("用户名已存在");
            return resultDo;
        }
        insert = new User();
        insert.setDomainId(domainConfig.getId());
        insert.setUserName(user.getUserName());
        int icon = RandomUtils.getRandom(25);
        insert.setIcon("userIcon"+icon);
        insert.setRoleId(user.getRoleId());
        insert.setRoomId(user.getRoomId());
        insert.setStatus(1);
        insert.setRemark(user.getRemark());
        insert.setSalt(UUID.randomUUID().toString().replace("-", ""));
        insert.setPassword(Md5Manager.md5(user.getPassword(), insert.getSalt()));
        insert.setDelFlag(1);
        resultDo.setSuccess(userManager.insert(insert));
        return resultDo;
    }

    @SysAuth(rule = "sys:updateUserPassword")
    @RequestMapping(value = "/updateUserPassword")
    public String updateUserPassword(Model model, Long id){
        DomainConfig domainConfig = getDomainConfig();
        model.addAttribute("id",id);
        return "/sys/updateUserPassword";
    }

    @SysAuth(rule = "sys:updateUserPassword")
    @RequestMapping(value = "/saveUserPassword")
    public ResultDo saveUserPassword(Long id,String password){
        ResultDo resultDo = new ResultDo();
        DomainConfig domainConfig = getDomainConfig();
        User user = userManager.queryByDomainIdAndId(id,domainConfig.getId());
        if (user==null){
            resultDo.setErrorDesc("数据不存在");
            return resultDo;
        }
        user.setPassword(Md5Manager.md5(password, user.getSalt()));
        resultDo.setSuccess(userManager.update(user));
        return resultDo;
    }

    @SysAuth(rule = "sys:updateUserRole")
    @RequestMapping(value = "/updateUserRole")
    public String updateUserRole(Model model, Long id){
        DomainConfig domainConfig = getDomainConfig();
        List<Role> roles = roleManager.queryList(domainConfig.getId());
        model.addAttribute("roles", roles);
        User user = userManager.queryByDomainIdAndId(id,domainConfig.getId());
        if (user == null){
            user = new User();
        }
        model.addAttribute("user", user);
        return "/sys/updateUserRole";
    }

    @SysAuth(rule = "sys:updateUserRole")
    @ResponseBody
    @RequestMapping(value = "/saveUserRole")
    public ResultDo saveUserRole(Long id,Long roleId){
        ResultDo resultDo = new ResultDo();
        DomainConfig domainConfig = getDomainConfig();
        User user = userManager.queryByDomainIdAndId(id,domainConfig.getId());
        if (user==null){
            resultDo.setErrorDesc("数据不存在");
            return resultDo;
        }
        user.setRoleId(roleId);
        resultDo.setSuccess(userManager.update(user));
        return resultDo;
    }

    @ResponseBody
    @RequestMapping(value = "/joinChat")
    @SysAuth(rule = "sys:joinChat")
    public ResultDo joinChat(Long id){
        DomainConfig domainConfig = getDomainConfig();
        ResultDo resultDo = new ResultDo();
        User old = userManager.queryByDomainIdAndId(id,domainConfig.getId());
        if (old == null) {
            resultDo.setErrorDesc("数据不存在");
            return resultDo;
        }
        old.setStatus(1);
        resultDo.setSuccess(userManager.update(old));
        return resultDo;
    }

    @ResponseBody
    @RequestMapping(value = "/unJoinChat")
    @SysAuth(rule = "sys:unJoinChat")
    public ResultDo unJoinChat(Long id){
        DomainConfig domainConfig = getDomainConfig();
        ResultDo resultDo = new ResultDo();
        User old = userManager.queryByDomainIdAndId(id,domainConfig.getId());
        if (old == null) {
            resultDo.setErrorDesc("数据不存在");
            return resultDo;
        }
        old.setStatus(0);
        resultDo.setSuccess(userManager.update(old));
        return resultDo;
    }

    @ResponseBody
    @RequestMapping(value = "/removeUser")
    @SysAuth(rule = "sys:removeUser")
    public ResultDo removeUser(Long id){
        DomainConfig domainConfig = getDomainConfig();
        ResultDo resultDo = new ResultDo();
        User old = userManager.queryByDomainIdAndId(id,domainConfig.getId());
        if (old == null) {
            resultDo.setErrorDesc("数据不存在");
            return resultDo;
        }
        resultDo.setSuccess(userManager.remove(id));
        return resultDo;
    }

    @SysAuth(rule = "sys:role")
    @RequestMapping(value = "/role")
    public String role(Model model) {
        String[] permArray = getPermsArray();
        model.addAttribute("permArray", permArray);
        return "sys/role";
    }

    @ResponseBody
    @SysAuth(rule = "sys:role")
    @RequestMapping(value = "/ajaxRole")
    public PageResult ajaxRole(RoleQuery query) {
        DomainConfig domainConfig = getDomainConfig();
        query.setDomainId(domainConfig.getId());
        PageResult pageResult = roleManager.queryPage(query);
        return pageResult;
    }

    @SysAuth(rule = "sys:addRole")
    @RequestMapping(value = "/addRole")
    public String addRole(Model model, Long id) {
        DomainConfig domainConfig = getDomainConfig();
        Role role = roleManager.queryByIdAndDomainId(id,domainConfig.getId());
        if (role == null){
            role = new Role();
        }
        List<Integer> idList = new ArrayList<>();
        if (!StringUtils.isEmpty(role.getPermIds())){
            String[] array = role.getPermIds().split(",");
            for (int i = 0;i<array.length;i++){
                idList.add(Integer.parseInt(array[i]));
            }
        }
        List<PermissionRoleDto> perms = roleManager.queryPerms(null);
        model.addAttribute("perms", perms);
        model.addAttribute("role",role);
        model.addAttribute("idList",idList);
        return "sys/addRole";
    }

    @ResponseBody
    @SysAuth(rule = "sys:addRole")
    @RequestMapping(value = "/saveRole")
    public ResultDo saveRole(Role role) {
        ResultDo resultDo = new ResultDo();
        DomainConfig domainConfig = getDomainConfig();
        if (role.getId()!=null && role.getId()>0){
            Role old = roleManager.queryByIdAndDomainId(role.getId(),domainConfig.getId());
            if (old == null){
                resultDo.setErrorDesc("数据不存在");
                return resultDo;
            }
            old.setPermIds(role.getPermIds());
            old.setRoleName(role.getRoleName());
            old.setRoleIcon(role.getRoleIcon());
            old.setRemark(role.getRemark());
            resultDo.setSuccess(roleManager.update(old));
            return resultDo;
        }
        Role insert = new Role();
        insert.setDomainId(domainConfig.getId());
        insert.setPermIds(role.getPermIds());
        insert.setRoleName(role.getRoleName());
        insert.setRoleIcon(role.getRoleIcon());
        insert.setRemark(role.getRemark());
        resultDo.setSuccess(roleManager.insert(insert));
        return resultDo;
    }

    @ResponseBody
    @SysAuth(rule = "sys:removeRole")
    @RequestMapping(value = "/removeRole")
    public ResultDo removeRole(Role role) {
        ResultDo resultDo = new ResultDo();
        DomainConfig domainConfig = getDomainConfig();
        return resultDo;
    }

    @SysAuth(rule = "sys:redbag")
    @RequestMapping(value = "/redbag")
    public String redbag(Model model){
        String[] permArray = getPermsArray();
        model.addAttribute("permArray", permArray);
        return "/sys/redbag";
    }

    @ResponseBody
    @SysAuth(rule = "sys:redbag")
    @RequestMapping(value = "/ajaxRedbag")
    public PageResult ajaxRedbag(RedBagQuery query){
        DomainConfig domainConfig = getDomainConfig();
        query.setDomainId(domainConfig.getId());
        PageResult<Redbag> pageResult = redbagManager.queryPage(query);
        return pageResult;
    }

    @SysAuth(rule = "sys:addRedbag")
    @RequestMapping(value = "/addRedbag")
    public String addRedbag(Model model){
        DomainConfig domainConfig = getDomainConfig();
        List<Room> roomList = roomManager.queryList(domainConfig.getId());
        model.addAttribute("roomList",roomList);
        return "/sys/addRedbag";
    }

    @ResponseBody
    @SysAuth(rule = "sys:addRedbag")
    @RequestMapping(value = "/saveRedbag")
    public ResultDo saveRedbag(Redbag redbag){
        ResultDo resultDo = new ResultDo();
        DomainConfig domainConfig = getDomainConfig();
        SysAuthUser sysAuthUser = getUserInfo();
        String url = domainConfig.getHttpUrl()+"/api/sendRedbag";
        Map<String,Object> postMap = new HashMap<>();
        postMap.put("domain",domainConfig.getDomainName());
        postMap.put("token",sysAuthUser.getToken());
        postMap.put("domainId",domainConfig.getId());
        postMap.put("sendUserId",sysAuthUser.getUserId());
        postMap.put("sendUserName",sysAuthUser.getUserName());
        postMap.put("roomId",redbag.getRoomId());
        postMap.put("amount",redbag.getAmount());
        postMap.put("count",redbag.getCount());
        postMap.put("remark",redbag.getRemark());
        String response = httpRequestClient.doPost(url,postMap,null);
        resultDo = JSON.parseObject(response,ResultDo.class);
        return resultDo;
    }

    @SysAuth(rule = "sys:receiveRedbag")
    @RequestMapping(value = "/receiveRedbag")
    public String receiveRedbag(Model model){
        String[] permArray = getPermsArray();
        model.addAttribute("permArray", permArray);
        return "/sys/receiveRedbag";
    }

    @ResponseBody
    @SysAuth(rule = "sys:receiveRedbag")
    @RequestMapping(value = "/ajaxReceiveRedbag")
    public PageResult ajaxReceiveRedbag(ReceiveRedBagQuery query){
        DomainConfig domainConfig = getDomainConfig();
        query.setDomainId(domainConfig.getId());
        query.setStartTime(DateUtils.addDate(new Date(),-3));
        PageResult<ReceiveRedbag> pageResult = receiveRedbagManager.queryPage(query);
        return pageResult;
    }

    @SysAuth(rule = "sys:exchangeRedbag")
    @RequestMapping(value = "/exchangeRedbag")
    public String exchangeRedbag(Model model, Long receiveId){
        model.addAttribute("receiveId",receiveId);
        return "/sys/exchangeRedbag";
    }

    @ResponseBody
    @SysAuth(rule = "sys:exchangeRedbag")
    @RequestMapping(value = "/postExchangeRedbag")
    public ResultDo postExchangeRedbag(Long receiveId,String remark){
        ResultDo resultDo = new ResultDo();
        DomainConfig domainConfig = getDomainConfig();
        ReceiveRedbag receiveRedbag = receiveRedbagManager.queryByIdAndDomainId(receiveId,domainConfig.getId());
        if (receiveRedbag==null){
            resultDo.setErrorDesc("数据不存在");
            return resultDo;
        }
        receiveRedbag.setStatus(2);
        receiveRedbag.setRemark(remark);
        resultDo.setSuccess(receiveRedbagManager.update(receiveRedbag));
        return resultDo;
    }

    @SysAuth(rule = "sys:blackIp")
    @RequestMapping(value = "/blackIp")
    public String blackIp(Model model){
        String[] permArray = getPermsArray();
        model.addAttribute("permArray", permArray);
        return "/sys/blackIp";
    }

    @ResponseBody
    @SysAuth(rule = "sys:blackIp")
    @RequestMapping(value = "/ajaxBlackIp")
    public PageResult ajaxBlackIp(BlackIpQuery query){
        DomainConfig domainConfig = getDomainConfig();
        query.setDomainId(domainConfig.getId());
        PageResult<BlackIp> pageResult = blackIpManager.queryPage(query);
        return pageResult;
    }

    @ResponseBody
    @SysAuth(rule = "sys:addBlackip")
    @RequestMapping(value = "/saveBlackIp")
    public ResultDo saveBlackIp(BlackIp blackIp){
        ResultDo resultDo = new ResultDo();
        DomainConfig domainConfig = getDomainConfig();
        BlackIp old = blackIpManager.queryByIp(domainConfig.getId(),blackIp.getIp());
        if (old != null){
            resultDo.setErrorDesc("ip已加入黑名单");
            return resultDo;
        }
        old = new BlackIp();
        old.setDomainId(domainConfig.getId());
        old.setIp(blackIp.getIp());
        resultDo.setSuccess(blackIpManager.insert(old));
        return resultDo;
    }

    @ResponseBody
    @SysAuth(rule = "sys:removeBlackip")
    @RequestMapping(value = "/removeBlackip")
    public ResultDo ajaxBlackIp(Long id){
        ResultDo resultDo = new ResultDo();
        DomainConfig domainConfig = getDomainConfig();
        BlackIp blackIp = blackIpManager.queryByIdAndDomainId(id,domainConfig.getId());
        if (blackIp==null){
            resultDo.setErrorDesc("数据不存在");
            return resultDo;
        }
        resultDo.setSuccess(blackIpManager.delete(domainConfig.getId(),id));
        return resultDo;
    }

    @SysAuth(rule = "sys:room")
    @RequestMapping(value = "/room")
    public String room(Model model){
        String[] permArray = getPermsArray();
        model.addAttribute("permArray", permArray);
        return "/sys/room";
    }

    @ResponseBody
    @SysAuth(rule = "sys:room")
    @RequestMapping(value = "/ajaxRoom")
    public PageResult ajaxRoom(RoomQuery query){
        DomainConfig domainConfig = getDomainConfig();
        query.setDomainId(domainConfig.getId());
        PageResult<Room> pageResult = roomManager.queryPage(query);
        return pageResult;
    }

    @SysAuth(rule = "sys:addRoom")
    @RequestMapping(value = "/addRoom")
    public String addRoom(Model model, Long id){
        DomainConfig domainConfig = getDomainConfig();
        logger.error("id:"+id);
        Room room = roomManager.queryByIdAndDomainId(id,domainConfig.getId());
        if (room == null){
            room = new Room();
        }
        model.addAttribute("room", room);
        return "/sys/addRoom";
    }

    @ResponseBody
    @SysAuth(rule = "sys:addRoom")
    @RequestMapping(value = "/saveRoom")
    public ResultDo saveRoom(Room room){
        ResultDo resultDo = new ResultDo();
        DomainConfig domainConfig = getDomainConfig();
        if (room.getId() != null && room.getId() > 0) {
            Room old = roomManager.queryByIdAndDomainId(room.getId(),domainConfig.getId());
            if (old == null) {
                resultDo.setErrorDesc("数据不存在");
                return resultDo;
            }
            old.setRoomName(room.getRoomName());
            old.setOpenRoom(room.getOpenRoom());
//            old.setRoomLogo(room.getRoomLogo());
            old.setRoomPcBg(room.getRoomPcBg());
            old.setRoomMobileBg(room.getRoomMobileBg());
            old.setRemark(room.getRemark());
            resultDo.setSuccess(roomManager.update(old));
            return resultDo;
        }
        Room insert = new Room();
        insert.setDomainId(domainConfig.getId());
        insert.setRoomName(room.getRoomName());
        insert.setForbidStatus(0);
        insert.setOpenRoom(room.getOpenRoom());
//        insert.setRoomLogo(room.getRoomLogo());
        insert.setRoomPcBg(room.getRoomPcBg());
        insert.setRoomMobileBg(room.getRoomMobileBg());
        insert.setRemark(room.getRemark());
        resultDo.setSuccess(roomManager.insert(insert));
        return resultDo;
    }

    @ResponseBody
    @SysAuth(rule = "sys:removeRoom")
    @RequestMapping(value = "/removeRoom")
    public ResultDo removeRoom(Long id){
        ResultDo resultDo = new ResultDo();
        DomainConfig domainConfig = getDomainConfig();
        SysAuthUser sysAuthUser = getUserInfo();
        Room old = roomManager.queryByIdAndDomainId(id,domainConfig.getId());
        if (old == null) {
            resultDo.setErrorDesc("数据不存在");
            return resultDo;
        }
        if (old.getRoomType() == 1){
            resultDo.setErrorDesc("默认房间不允许删除");
            return resultDo;
        }
        String url = domainConfig.getHttpUrl()+"/api/removeRoom";
        Map<String,Object> postMap = new HashMap<>();
        postMap.put("domain",domainConfig.getDomainName());
        postMap.put("token",sysAuthUser.getToken());
        postMap.put("roomId",id);
        String response = httpRequestClient.doPost(url,postMap,null);
        resultDo = JSON.parseObject(response,ResultDo.class);
        return resultDo;
    }

    @SysAuth(rule = "sys:tabMenu")
    @RequestMapping(value = "/tabMenu")
    public String tabMenu(Model model){
        String[] permArray = getPermsArray();
        model.addAttribute("permArray", permArray);
        return "/sys/tabMenu";
    }

    @ResponseBody
    @SysAuth(rule = "sys:tabMenu")
    @RequestMapping(value = "/ajaxTabMenu")
    public PageResult ajaxTabMenu(SystemDictQuery query){
        DomainConfig domainConfig = getDomainConfig();
        query.setDomainId(domainConfig.getId());
        query.setSysGroup(AppConfig.TabMenu);
        PageResult pageResult = systemDictManager.queryGroupAllPage(query);
        return pageResult;
    }

    @SysAuth(rule = "sys:addTabMenu")
    @RequestMapping(value = "/addTabMenu")
    public String addTabMenu(Model model, Long id){
        DomainConfig domainConfig = getDomainConfig();
        SystemDict systemDict = systemDictManager.queryById(id,domainConfig.getId());
        if (systemDict == null){
            systemDict = new SystemDict();
        }
        model.addAttribute("tabMenu",systemDict);
        return "/sys/addTabMenu";
    }

    @ResponseBody
    @SysAuth(rule = "sys:addTabMenu")
    @RequestMapping(value = "/saveTabMenu")
    public ResultDo saveTabMenu(SystemDict systemDict){
        ResultDo resultDo = new ResultDo();
        DomainConfig domainConfig = getDomainConfig();

        if (systemDict.getId()!=null && systemDict.getId()>0){
            SystemDict old = systemDictManager.queryById(systemDict.getId(),domainConfig.getId());
            if (old == null){
                resultDo.setErrorDesc("数据不存在");
                return resultDo;
            }
            old.setSysKey(systemDict.getSysKey());
            old.setSysType(systemDict.getSysType());
            old.setSysValue(systemDict.getSysValue());
            old.setRemark(systemDict.getRemark());
            resultDo.setSuccess(systemDictManager.update(old));
            return resultDo;
        }

        SystemDict insert = new SystemDict();
        insert.setDomainId(domainConfig.getId());
        insert.setSysGroup(AppConfig.TabMenu);
        insert.setSysKey(systemDict.getSysKey());
        insert.setSysType(systemDict.getSysType());
        insert.setSysValue(systemDict.getSysValue());
        insert.setRemark(systemDict.getRemark());
        resultDo.setSuccess(systemDictManager.insert(insert));
        return resultDo;
    }

    @ResponseBody
    @SysAuth(rule = "sys:removeTabMenu")
    @RequestMapping(value = "/removeTabMenu")
    public ResultDo removeTabMenu(Long id){
        ResultDo resultDo = new ResultDo();
        DomainConfig domainConfig = getDomainConfig();
        SystemDict systemDict = systemDictManager.queryById(id,domainConfig.getId());
        if (systemDict==null){
            resultDo.setErrorDesc("数据不存在");
            return resultDo;
        }
        resultDo.setSuccess(systemDictManager.remove(id,domainConfig.getId()));
        return resultDo;
    }

    @SysAuth(rule = "sys:webSite")
    @RequestMapping(value = "/webSite")
    public String webSite(Model model){
        DomainConfig domainConfig = getDomainConfig();
        List<SystemDict> systemDicts = systemDictManager.queryGroupAllByDomainId(domainConfig.getId(),AppConfig.WebSet);
        Map<String,String> map = new HashMap<>();
        if(systemDicts!=null && systemDicts.size()>0){
            for (SystemDict d:systemDicts) {
                map.put(d.getSysKey(),d.getSysValue());
            }
        }
        model.addAttribute("webSite",map);
        return "/sys/webSite";
    }

    @ResponseBody
    @SysAuth(rule = "sys:webSite")
    @RequestMapping(value = "/saveWebSite")
    public ResultDo saveWebSite(String web_name,String customer_url,String recharge_url,String web_url,String pc_notice,String mobile_notice,String pc_logo,
                                String m_logo){
        ResultDo resultDo = new ResultDo();
        DomainConfig domainConfig = getDomainConfig();
        saveWebSet(domainConfig.getId(),"web_name",web_name);
        saveWebSet(domainConfig.getId(),"customer_url",customer_url);
        saveWebSet(domainConfig.getId(),"recharge_url",recharge_url);
        saveWebSet(domainConfig.getId(),"web_url",web_url);
        saveWebSet(domainConfig.getId(),"pc_notice",pc_notice);
        saveWebSet(domainConfig.getId(),"mobile_notice",mobile_notice);
        saveWebSet(domainConfig.getId(),"pc_logo",pc_logo);
        saveWebSet(domainConfig.getId(),"m_logo",m_logo);
        resultDo.setSuccess(true);
        return resultDo;
    }

    @ResponseBody
    @SysAuth(rule = "sys:forbid")
    @RequestMapping(value = "/forbid")
    public ResultDo forbid(Long roomId){
        ResultDo resultDo = new ResultDo();
        DomainConfig domainConfig = getDomainConfig();
        SysAuthUser userInfo = getUserInfo();
        Room room = roomManager.queryByIdAndDomainId(roomId,domainConfig.getId());
        if (room==null){
            resultDo.setErrorDesc("数据不存在");
            return resultDo;
        }
        Integer result;
        if (room.getForbidStatus() == 1){
            result = 0;
            String url = domainConfig.getHttpUrl()+"/api/unForbid";
            Map<String,Object> postMap = new HashMap<>();
            postMap.put("domain",domainConfig.getDomainName());
            postMap.put("token",userInfo.getToken());
            postMap.put("roomId",roomId);
            String response = httpRequestClient.doPost(url,postMap,null);
            resultDo = JSON.parseObject(response,ResultDo.class);
        }else {
            result = 1;
            String url = domainConfig.getHttpUrl()+"/api/forbid";
            Map<String,Object> postMap = new HashMap<>();
            postMap.put("domain",domainConfig.getDomainName());
            postMap.put("token",userInfo.getToken());
            postMap.put("roomId",roomId);
            String response = httpRequestClient.doPost(url,postMap,null);
            resultDo = JSON.parseObject(response,ResultDo.class);
        }
        if (resultDo.isSuccess()){
            resultDo.setResult(result);
        }
        return resultDo;
    }

    @SysAuth(rule = "sys:userRobot")
    @RequestMapping(value = "/userRobot")
    public String userRobot(Model model){
        DomainConfig domainConfig = getDomainConfig();
        String[] permArray = getPermsArray();
        model.addAttribute("permArray", permArray);
        List<Room> rooms = roomManager.queryList(domainConfig.getId());
        model.addAttribute("rooms", rooms);
        return "/sys/userRobot";
    }

    @ResponseBody
    @SysAuth(rule = "sys:userRobot")
    @RequestMapping(value = "/ajaxRobot")
    public PageResult ajaxRobot(UserRobotQuery query) {
        DomainConfig domainConfig = getDomainConfig();
        query.setDomainId(domainConfig.getId());
        PageResult pageResult = userRobotManager.queryPage(query);
        return pageResult;
    }

    @SysAuth(rule = "sys:addRobot")
    @RequestMapping(value = "/addRobot")
    public String addRobot(Model model){
        DomainConfig domainConfig = getDomainConfig();
        List<Room> roomList = roomManager.queryList(domainConfig.getId());
        model.addAttribute("roomList",roomList);
        return "/sys/addUserRobot";
    }

    @ResponseBody
    @SysAuth(rule = "sys:addRobot")
    @RequestMapping(value = "/saveRobot")
    public ResultDo saveRobot(Integer count,Long roomId){
        ResultDo resultDo = new ResultDo();
        DomainConfig domainConfig = getDomainConfig();
        if (count == null || count<1){
            resultDo.setErrorDesc("请选择创建数量");
            return resultDo;
        }
        userRobotManager.insertBatch(count,domainConfig.getId(),roomId);
        resultDo.setSuccess(true);
        return resultDo;
    }

    @ResponseBody
    @SysAuth(rule = "sys:deleteBatchRobot")
    @RequestMapping(value = "/deleteBatchRobot")
    public ResultDo deleteBatch(String ids){
        ResultDo resultDo = new ResultDo();
        DomainConfig domainConfig = getDomainConfig();
        List<Long> idList = new ArrayList<>();
        String[] idstr = ids.split(",");
        if (idstr!=null && idstr.length>0){
            for (int i = 0;i<idstr.length;i++){
                try {
                    idList.add(Long.parseLong(idstr[i]));
                } catch (Exception e) {
                }
            }
        }
        userRobotManager.deleteBatch(idList,domainConfig.getId());
        resultDo.setSuccess(true);
        return resultDo;
    }

    @SysAuth(rule = "sys:sendMsg")
    @RequestMapping(value = "/sendMsg")
    public String sendMsg(Model model){
        DomainConfig domainConfig = getDomainConfig();
        List<Room> list = roomManager.queryList(domainConfig.getId());
        model.addAttribute("rooms",list);
        return "/sys/sendMsg";
    }

    @ResponseBody
    @SysAuth(rule = "sys:sendMsg")
    @RequestMapping(value = "/saveSendMsg")
    public ResultDo saveSendMsg(Long roomId,String msgType,String msg) {
        ResultDo resultDo = new ResultDo();
        DomainConfig domainConfig = getDomainConfig();
        String url = domainConfig.getHttpUrl()+"/api/sendMsg";
        Map<String,Object> postMap = new HashMap<>();
        postMap.put("domain",domainConfig.getDomainName());
        SysAuthUser userInfo = getUserInfo();
        postMap.put("token",userInfo.getToken());
        postMap.put("roomId",roomId);
        postMap.put("msg",bindMsg(msgType,msg));
        String response = httpRequestClient.doPost(url,postMap,null);
        resultDo = JSON.parseObject(response, ResultDo.class);
        return resultDo;
    }

    private void saveWebSet(Long domainId,String key,String value){
        SystemDict systemDict = systemDictManager.queryByKey(domainId,AppConfig.WebSet,key);
        if (systemDict ==null){
            systemDict = new SystemDict();
            systemDict.setDomainId(domainId);
            systemDict.setSysGroup(AppConfig.WebSet);
            systemDict.setSysKey(key);
            systemDict.setSysValue(value);
            systemDictManager.insert(systemDict);
            return;
        }
        systemDict.setSysValue(value);
        systemDictManager.update(systemDict);
        return;
    }

    private String bindMsg(String msgType,String msg){
        if ("S_LOTTERY_GOODNEWS".equals(msgType)){
            JSONObject response = new JSONObject();
            response.put("messageId", UUID.randomUUID().toString().replace("-", ""));
            response.put("command", "S_LOTTERY_GOODNEWS");
            response.put("content", msg);
            return response.toJSONString();
        }
        return null;
    }
}
