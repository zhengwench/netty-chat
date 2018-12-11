package chat.console.controller;

import chat.console.common.HttpRequestClient;
import chat.core.common.domain.AppConfig;
import chat.core.common.domain.PageResult;
import chat.core.common.domain.ResultDo;
import chat.core.common.redis.RedisManager;
import chat.core.common.utility.Md5Manager;
import chat.core.db.manager.DomainConfigManager;
import chat.core.db.manager.RoomManager;
import chat.core.db.manager.ServerModelManager;
import chat.core.db.model.DomainConfig;
import chat.core.db.model.Room;
import chat.core.db.model.ServerModel;
import chat.core.db.model.query.DomainConfigQuery;
import chat.core.db.model.query.RoomQuery;
import chat.core.db.model.query.ServerModelQuery;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @auther a-de
 * @date 2018/12/7 21:13
 */
@Controller
@RequestMapping(value = "/console")
public class ConsoleController extends BaseController{
    private Logger logger = LoggerFactory.getLogger(ConsoleController.class);

    @Autowired
    private DomainConfigManager domainConfigManager;

    @Autowired
    private RoomManager roomManager;

    @Autowired
    private ServerModelManager serverModelManager;

    @Autowired
    private HttpRequestClient httpRequestClient;

    @RequestMapping(value = "/index")
    String index(Model model, String token){
        if (!isLogined()){
            return "redirect:login";
        }
        return "index";
    }

    @ResponseBody
    @RequestMapping(value = "/index1")
    String index1(){
        RedisManager.lpush("index1","index1");
        return "index1";
    }

    @ResponseBody
    @RequestMapping(value = "/index2")
    String index2(){
        List<String> list = RedisManager.lrange("index1");
        StringBuilder stringBuilder = new StringBuilder();
        if (list!=null && list.size()>0){
            for (String s : list){
                stringBuilder.append(s);
            }
        }
        return stringBuilder.toString();
    }

    @RequestMapping(value = "/welcome")
    String welcome(){
        return "welcome";
    }

    @RequestMapping(value = "/login")
    String login(){
        return "login";
    }

    @ResponseBody
    @RequestMapping(value = "/logined")
    ResultDo logined(String username, String password){
        ResultDo resultDo = new ResultDo();
        String resultStr = username + "_" + password;
        String token = Md5Manager.md5(resultStr);
        resultDo.setResult(token);
        return resultDo;
    }

    @RequestMapping(value = "/domainConfig")
    String domainConfig(){
        return "domainConfig";
    }

    @ResponseBody
    @RequestMapping(value = "/ajaxDomainConfig")
    PageResult ajaxDomainConfig(DomainConfigQuery query){
        PageResult pageResult = new PageResult();
        if (!isLogined()){
            return pageResult;
        }
        pageResult = domainConfigManager.queryPageDomainConfig(query);
        return pageResult;
    }

    @RequestMapping(value = "/addDomainConfig")
    String addDomainConfig(Model model,Long id){
        if (!isLogined()){
            return "redirect:login";
        }
        DomainConfig old = domainConfigManager.queryById(id);
        if (old ==null){
            old = new DomainConfig();
        }
        model.addAttribute("domain",old);
        List<DomainConfig> parents = domainConfigManager.queryParentList();
        model.addAttribute("parents",parents);
        return "addDomainConfig";
    }

    @ResponseBody
    @RequestMapping(value = "/saveDomainConfig")
    ResultDo saveDomainConfig(DomainConfig domainConfig){
        ResultDo resultDo = new ResultDo();
        if (!isLogined()){
            resultDo.setErrorDesc("未登录");
            return resultDo;
        }
        if (domainConfig.getId()!=null && domainConfig.getId()>0){
            DomainConfig old = domainConfigManager.queryById(domainConfig.getId());
            if (old == null){
                resultDo.setErrorDesc("数据不存在");
                return resultDo;
            }
            old.setDomainName(domainConfig.getDomainName());
            old.setWebsocketUrl(domainConfig.getWebsocketUrl());
            old.setHttpUrl(domainConfig.getHttpUrl());
            old.setPublicKey(domainConfig.getPublicKey());
            old.setPrivateKey(domainConfig.getPrivateKey());
            old.setStartTime(domainConfig.getStartTime());
            old.setEndTime(domainConfig.getEndTime());
            old.setStatus(domainConfig.getStatus());
            old.setRemarks(domainConfig.getRemarks());
            if (domainConfigManager.update(old)){
                resultDo.setSuccess(true);
            }
            return resultDo;
        }
        DomainConfig old = domainConfigManager.queryByDomainName(domainConfig.getDomainName());
        if (old!=null){
            resultDo.setErrorDesc("服务域名已存在");
            return resultDo;
        }
        DomainConfig insert = new DomainConfig();
        if (domainConfig.getParentId()!=null&& domainConfig.getParentId()>0){
            insert.setParentId(domainConfig.getParentId());
        }else {
            insert.setParentId(0L);
        }
        insert.setDomainName(domainConfig.getDomainName());
        insert.setWebsocketUrl(domainConfig.getWebsocketUrl());
        insert.setHttpUrl(domainConfig.getHttpUrl());
        insert.setPublicKey(domainConfig.getPublicKey());
        insert.setPrivateKey(domainConfig.getPrivateKey());
        insert.setStartTime(domainConfig.getStartTime());
        insert.setEndTime(domainConfig.getEndTime());
        insert.setStatus(1);
        insert.setRemarks(domainConfig.getRemarks());
        if (domainConfigManager.create(insert)){
            resultDo.setSuccess(true);
        }
        return resultDo;
    }

    @ResponseBody
    @RequestMapping(value = "/removeDomainConfig")
    ResultDo removeDomainConfig(Long id){
        ResultDo resultDo = new ResultDo();
        if (!isLogined()){
            resultDo.setErrorDesc("未登录");
            return resultDo;
        }
        DomainConfig old = domainConfigManager.queryById(id);
        if (old == null){
            resultDo.setErrorDesc("数据不存在");
            return resultDo;
        }
        if (domainConfigManager.remove(id)){
            resultDo.setSuccess(true);
        }
        return resultDo;
    }

    @ResponseBody
    @RequestMapping(value = "/cleanHistory")
    ResultDo cleanHistory(String historyDomain,String historyRoomId){
        ResultDo resultDo = new ResultDo();
        String key = historyDomain + "_" + historyRoomId + "_HISORYMESSAGE";
        RedisManager.expire(key.hashCode() + "",0);
        resultDo.setSuccess(true);
        return resultDo;
    }

    @ResponseBody
    @RequestMapping(value = "/cleanChatRoom")
    ResultDo cleanChatRoom(String chatroomDomain){
        ResultDo resultDo = new ResultDo();
        String rediskey = chatroomDomain+"monitorOnline";
        RedisManager.expireObject(rediskey,0);
        resultDo.setSuccess(true);
        return resultDo;
    }

    @RequestMapping(value = "/room")
    public String room(){
        if (!isLogined()){
            return "redirect:login";
        }
        return "room";
    }

    @ResponseBody
    @RequestMapping(value = "/ajaxRoom")
    public PageResult ajaxRoom(String domain){
        PageResult<Room> pageResult = new PageResult<>();
        if (!isLogined()){
            return pageResult;
        }
        RoomQuery query = new RoomQuery();
        if (!StringUtils.isEmpty(domain)){
            DomainConfig domainConfig = domainConfigManager.queryByDomainName(domain);
            if (domainConfig == null){
                return pageResult;
            }
            query.setDomainId(domainConfig.getId());
        }
        pageResult = roomManager.queryPage(query);
        return pageResult;
    }

    @RequestMapping(value = "/editRoom")
    public String editRoom(Model model,Long id){
        if (!isLogined()){
            return "redirect:login";
        }
        Room room = roomManager.queryById(id);
        model.addAttribute("room",room);
        return "editRoom";
    }

    @ResponseBody
    @RequestMapping(value = "/saveRoom")
    public ResultDo saveRoom(Room room){
        ResultDo resultDo = new ResultDo();
        Room old = roomManager.queryById(room.getId());
        if (old==null){
            resultDo.setErrorDesc("数据不存在");
            return resultDo;
        }
        old.setWebsocketUrl(room.getWebsocketUrl());
        old.setHttpUrl(room.getHttpUrl());
        if (roomManager.update(old)){
            String idkey = AppConfig.RoomCacheById + room.getId();
            RedisManager.removeObject(idkey);
            resultDo.setSuccess(true);
            return resultDo;
        }
        return resultDo;
    }

    @RequestMapping(value = "/monitor")
    public String monitor(){
        return "monitor";
    }

    @ResponseBody
    @RequestMapping(value = "/ajaxMonitor")
    public PageResult ajaxMonitor(ServerModelQuery query) {
        PageResult<ServerModel> pageResult = serverModelManager.queryPage(query);
        return pageResult;
    }

    @ResponseBody
    @RequestMapping(value = "/queryOnlineCount")
    public ResultDo queryOnlineCount(Long id){
        ResultDo resultDo = new ResultDo();
        ServerModel serverModel = serverModelManager.queryById(id);
        Integer count = 0;
        if (serverModel!=null){
            String url = "http://" + serverModel.getInnerIp() + ":" + serverModel.getHttpPort() + "/api/monitorOnlineCount";
            try {
                String response = httpRequestClient.doPost(url, null, null);
                ResultDo<Integer> responseDo = JSON.parseObject(response, ResultDo.class);
                if (responseDo != null && responseDo.isSuccess()) {
                    count = responseDo.getResult();
                }
            } catch (Exception e) {
                logger.error("【" + url + "】请求数据异常", e);
            }
        }
        resultDo.setResult(count);
        return resultDo;
    }

    @ResponseBody
    @RequestMapping(value = "/deleteMonitorServer")
    public ResultDo deleteMonitorServer(Long id){
        ResultDo resultDo = new ResultDo();
        resultDo.setSuccess(serverModelManager.delete(id));
        return resultDo;
    }

    @RequestMapping(value = "/addMonitor")
    public String addMonitor(){
        return "addMonitor";
    }

    @ResponseBody
    @RequestMapping(value = "/saveMonitor")
    public ResultDo saveMonitor(ServerModel serverModel){
        ResultDo resultDo = new ResultDo();
        ServerModel insert = new ServerModel();
        insert.setServerIp(serverModel.getServerIp());
        insert.setServerDomain(serverModel.getServerDomain());
        insert.setInnerIp(serverModel.getInnerIp());
        insert.setWebsocketPort(serverModel.getWebsocketPort());
        insert.setHttpPort(serverModel.getHttpPort());
        insert.setRemark(serverModel.getRemark());
        resultDo.setSuccess(serverModelManager.insert(insert));
        return resultDo;
    }
}
