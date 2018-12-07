package chat.server.controller;

import com.alibaba.fastjson.JSONObject;
import chat.core.common.domain.AppConfig;
import chat.core.common.domain.ResultDo;
import chat.core.common.utility.DateUtils;
import chat.core.db.manager.DomainConfigManager;
import chat.core.db.manager.RedbagManager;
import chat.core.db.manager.RoomManager;
import chat.core.db.manager.UserManager;
import chat.core.db.model.DomainConfig;
import chat.core.db.model.Redbag;
import chat.core.db.model.Room;
import chat.core.db.model.User;
import chat.server.channel.ChannelContext;
import chat.server.channel.DomainChannelMap;
import chat.server.command.ServerCommandEnum;
import chat.server.handler.HistoryMessageHandler;
import chat.server.handler.MessageProcess;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @auther a-de
 * @date 2018/11/10 12:40
 */
@Controller
@RequestMapping(value = "/api")
public class ApiController {
    private Logger logger = LoggerFactory.getLogger(ApiController.class);
    @Autowired
    private UserManager userManager;

    @Autowired
    private DomainConfigManager domainConfigManager;

    @Autowired
    private RedbagManager redbagManager;

    @Autowired
    private RoomManager roomManager;

    @Autowired
    private MessageProcess messageProcess;

    @ResponseBody
    @RequestMapping(value = "/removeRoom")
    public ResultDo removeRoom(String domain,String token,Long roomId){
        ResultDo resultDo = new ResultDo();
        DomainConfig domainConfig = getDomainConfig(domain);
        if (domainConfig==null){
            resultDo.setErrorDesc(AppConfig.DomainError);
            return resultDo;
        }
        User user = userManager.queryByDomainIdAndToken(domainConfig.getId(),token);
        if (user==null){
            resultDo.setErrorDesc("用户信息错误");
            return resultDo;
        }
        Room room = roomManager.queryByIdAndDomainId(roomId,domainConfig.getId());
        if (room==null){
            resultDo.setErrorDesc("房间不存在");
            return resultDo;
        }
        if (roomManager.remove(room.getId())){
            DomainChannelMap.removeDomainRoomContext(domain,roomId+"");
            resultDo.setSuccess(true);
            return resultDo;
        }
        return resultDo;
    }

    @ResponseBody
    @RequestMapping(value = "/forbid")
    public ResultDo forbid(String domain,String token,Long roomId){
        ResultDo resultDo = new ResultDo();
        DomainConfig domainConfig = getDomainConfig(domain);
        if (domainConfig==null){
            resultDo.setErrorDesc(AppConfig.DomainError);
            return resultDo;
        }
        User user = userManager.queryByDomainIdAndToken(domainConfig.getId(),token);
        if (user==null){
            resultDo.setErrorDesc("用户信息错误");
            return resultDo;
        }
        Room room = roomManager.queryByIdAndDomainId(roomId,domainConfig.getId());
        if (room==null){
            resultDo.setErrorDesc("房间不存在");
            return resultDo;
        }
        room.setForbidStatus(1);
        if (roomManager.update(room)) {
            JSONObject response = new JSONObject();
            response.put("messageId", UUID.randomUUID().toString().replace("-", ""));
            response.put("command", ServerCommandEnum.S_FORBID_CHAT.getKey());
            messageProcess.sendRoomMsg(domain,roomId+"",response.toJSONString());
            resultDo.setSuccess(true);
            return resultDo;
        }
        return resultDo;
    }

    @ResponseBody
    @RequestMapping(value = "/unForbid")
    public ResultDo unForbid(String domain,String token,Long roomId){
        ResultDo resultDo = new ResultDo();
        DomainConfig domainConfig = getDomainConfig(domain);
        if (domainConfig==null){
            resultDo.setErrorDesc(AppConfig.DomainError);
            return resultDo;
        }
        User user = userManager.queryByDomainIdAndToken(domainConfig.getId(),token);
        if (user==null){
            resultDo.setErrorDesc("用户信息错误");
            return resultDo;
        }
        Room room = roomManager.queryByIdAndDomainId(roomId,domainConfig.getId());
        if (room==null){
            resultDo.setErrorDesc("房间不存在");
            return resultDo;
        }
        room.setForbidStatus(0);
        if (roomManager.update(room)) {
            JSONObject response = new JSONObject();
            response.put("messageId", UUID.randomUUID().toString().replace("-", ""));
            response.put("command", ServerCommandEnum.S_UNFORBID_CHAT.getKey());
            messageProcess.sendRoomMsg(domain,roomId+"",response.toJSONString());
            resultDo.setSuccess(true);
            return resultDo;
        }
        return resultDo;
    }

    @ResponseBody
    @RequestMapping(value = "/removeUser")
    public ResultDo removeUser(String domain,String token,Long userId){
        ResultDo resultDo = new ResultDo();
        DomainConfig domainConfig = getDomainConfig(domain);
        if (domainConfig == null) {
            resultDo.setErrorDesc(AppConfig.DomainError);
            return resultDo;
        }
        User user = userManager.queryByDomainIdAndToken(domainConfig.getId(), token);
        if (user == null) {
            resultDo.setErrorDesc("用户信息错误");
            return resultDo;
        }

        User remove = userManager.queryById(userId);
        if (remove!=null){
            if (userManager.remove(userId)){
                List<ChannelContext> list = DomainChannelMap.getAndRemoveUserAllChannelContext(domain,userId);
                JSONObject response = new JSONObject();
                response.put("messageId", UUID.randomUUID().toString().replace("-", ""));
                response.put("command", ServerCommandEnum.S_REMOVE_CHAT.getKey());
                if (list!=null&&list.size()>0){
                    for (ChannelContext c:list) {
                        if (c.getChannel()!=null){
                            if (c.getChannel().isActive()){
                                c.getChannel().writeAndFlush(new TextWebSocketFrame(response.toJSONString()));
                            }
                            c.getChannel().close();
                        }
                    }
                }
            }
        }
        return resultDo;
    }

    @ResponseBody
    @RequestMapping(value = "/sendRedbag")
    public ResultDo sendRedbag(String domain,String token,Redbag redbag) {
        ResultDo resultDo = new ResultDo();
        try {
            DomainConfig domainConfig = getDomainConfig(domain);
            if (domainConfig == null) {
                resultDo.setErrorDesc(AppConfig.DomainError);
                return resultDo;
            }
            User user = userManager.queryByDomainIdAndToken(domainConfig.getId(), token);
            if (user == null) {
                resultDo.setErrorDesc("用户信息错误");
                return resultDo;
            }

            Redbag insert = new Redbag();
            insert.setDomainId(domainConfig.getId());
            insert.setSendUserId(user.getId());
            insert.setSendUserName(user.getUserName());
            insert.setSendUserIcon(user.getIcon());
            insert.setRoomId(redbag.getRoomId());
            insert.setAmount(redbag.getAmount());
            insert.setCount(redbag.getCount());
            insert.setRemark(redbag.getRemark());
            Redbag result = redbagManager.createRedbag(domainConfig.getDomainName(), insert);
            if (result!=null) {
                JSONObject response = new JSONObject();
                response.put("messageId", UUID.randomUUID().toString().replace("-", ""));
                response.put("command", ServerCommandEnum.S_SEND_REDBAG.getKey());
                response.put("userId", insert.getSendUserId());
                response.put("userName", insert.getSendUserName());
                response.put("userIcon", user.getIcon());
                response.put("messageTime", DateUtils.getStrFromDate(new Date(),"HH:mm:ss"));
                JSONObject content = new JSONObject();
                content.put("redbagId",result.getId());
                response.put("content",content);
                messageProcess.sendRoomMsg(domain,redbag.getRoomId() + "",response.toJSONString());
                HistoryMessageHandler.insert(domain, redbag.getRoomId() + "", response.toJSONString());
                resultDo.setSuccess(true);
                return resultDo;
            }else {
                resultDo.setErrorDesc("红包创建为空");
            }
        } catch (Exception e) {
            logger.error("生成红包异常",e);
            resultDo.setErrorDesc("生成红包异常");
            return resultDo;
        }
        return resultDo;
    }

    @ResponseBody
    @RequestMapping(value = "/monitorOnline")
    public ResultDo monitorOnline(String domain){
        ResultDo resultDo = new ResultDo();
        DomainConfig domainConfig = getDomainConfig(domain);
        if (domainConfig == null) {
            resultDo.setErrorDesc(AppConfig.DomainError);
            return resultDo;
        }
        Map<String,Integer> map = DomainChannelMap.monitorOnline(domainConfig.getDomainName());
        resultDo.setResult(map);
        return resultDo;
    }

    @ResponseBody
    @RequestMapping(value = "/monitorOnlineCount")
    public ResultDo monitorOnlineCount(){
        ResultDo resultDo = new ResultDo();
        Integer count = DomainChannelMap.monitorOnlineCount();
        resultDo.setResult(count);
        return resultDo;
    }

    @ResponseBody
    @RequestMapping(value = "sendMsg")
    public ResultDo sendMsg(String domain,String roomId,String msg){
        ResultDo resultDo = new ResultDo();
        messageProcess.sendRoomMsg(domain,roomId,msg);
        resultDo.setSuccess(true);
        return resultDo;
    }

    private DomainConfig getDomainConfig(String domain){
        DomainConfig domainConfig = domainConfigManager.queryByDomainName(domain);
        if (domainConfig == null || domainConfig.getStatus() != 1 || new Date().after(domainConfig.getEndTime()) ||
                new Date().before(domainConfig.getStartTime())) {
            return null;
        }
        return domainConfig;
    }
}
