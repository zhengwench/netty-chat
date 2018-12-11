package chat.server.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import chat.core.common.utility.DateUtils;
import chat.core.db.manager.DomainConfigManager;
import chat.core.db.manager.ReceiveRedbagManager;
import chat.core.db.manager.RoleManager;
import chat.core.db.manager.RoomManager;
import chat.core.db.manager.UserManager;
import chat.core.db.model.DomainConfig;
import chat.core.db.model.ReceiveRedbag;
import chat.core.db.model.Room;
import chat.core.db.model.User;
import chat.server.channel.ChannelContext;
import chat.server.channel.ChannelContextMap;
import chat.server.channel.DomainChannelMap;
import chat.server.command.ClientCommandEnum;
import chat.server.command.ServerCommandEnum;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * @auther a-de
 * @date 2018/11/7 21:43
 */
@Component
public class MessageProcess {
    private Logger logger = LoggerFactory.getLogger(MessageProcess.class);

    @Autowired
    private ReceiveRedbagManager receiveRedbagManager;

    @Autowired
    private RoomManager roomManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    private RoleManager roleManager;

    @Autowired
    private DomainConfigManager domainConfigManager;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public void execute(Channel channel, String message) {
        JSONObject jsonObject = JSON.parseObject(message);
        String command = jsonObject.getString("command");
        String domain = jsonObject.getString("domain");
        String roomId = jsonObject.getString("roomId");
        String token = jsonObject.getString("token");
        String userId = jsonObject.getString("userId");
        String userName = jsonObject.getString("userName");
        String userIcon = jsonObject.getString("userIcon");
        String roleName = jsonObject.getString("roleName");
        String content = jsonObject.getString("content");

        //组装response基本信息
        JSONObject response = createResponse(userId, userName, userIcon, roleName);
        ClientCommandEnum commandEnum = ClientCommandEnum.getEnumBykey(command);
        switch (commandEnum) {
            //发送消息
            case C_MESSAGE:
                try {
                    //检测发送者信息
                    if (!checkSender(domain,roomId,userId,token)){
                        return;
                    }
                    sendMessage(userId, domain, roomId, response, content);
                } catch (Exception e) {
                    logger.error("处理发送消息异常", e);
                }
                break;
            //踢出聊天室
            case C_REMOVE_CHAT:
                try {
                    //检测发送者信息
                    if (!checkSender(domain,roomId,userId,token)){
                        return;
                    }
                    removeChat(channel,userId, domain, roomId, content);
                } catch (Exception e) {
                    logger.error("踢出用户异常", e);
                }
                break;
            //撤回消息
            case C_RECALL_MESSAGE:
                try {
                    //检测发送者信息
                    if (!checkSender(domain,roomId,userId,token)){
                        return;
                    }
                    recallMessage(channel,userId, domain, roomId, response, content);
                } catch (Exception e) {
                    logger.error("撤回消息异常", e);
                }
                break;
            //心跳检测
            case C_HEART_BEAT:
                try {
                    commandSingleResponse(channel, domain, roomId, response, ServerCommandEnum.S_HEART_BEAT);
                } catch (Exception e) {
                    logger.error("心跳发送异常", e);
                }
                break;
            //领取红包
            case C_RECEIVE_REDBAG:
                try {
                    //检测发送者信息
                    if (!checkSender(domain,roomId,userId,token)){
                        return;
                    }
                    receiveRedBag(domain, roomId, userId, response, content, channel);
                } catch (Exception e) {
                    logger.error("领取红包异常");
                }
                break;
            default:
                logger.info("【" + command + "】无效");
        }
    }

    /**
     * 检测发送者信息
     * @param domain
     * @param roomId
     * @param userId
     * @param token
     * @return
     */
    private boolean checkSender(String domain,String roomId,String userId,String token){
        if (StringUtils.isEmpty(token)||StringUtils.isEmpty(userId)||StringUtils.isEmpty(roomId)) {
            return false;
        }
        //校验sender信息
        return DomainChannelMap.checkSender(domain, roomId,userId, token);
    }

    public void connectError(Channel channel, ServerCommandEnum serverCommandEnum) {
        JSONObject response = new JSONObject();
        response.put("messageId", UUID.randomUUID().toString().replace("-", ""));
        response.put("command", serverCommandEnum.getKey());
        response.put("messageTime", DateUtils.getStrFromDate(new Date(), "HH:mm:ss"));
        channel.writeAndFlush(new TextWebSocketFrame(response.toJSONString()));
    }

    /**
     * 命令response
     *
     * @param domain
     * @param roomId
     * @param response
     * @param serverCommandEnum
     */
    public void commandSingleResponse(Channel channel, String domain, String roomId, JSONObject response, ServerCommandEnum serverCommandEnum) {
        response.put("command", serverCommandEnum.getKey());
        channel.writeAndFlush(new TextWebSocketFrame(response.toJSONString()));
    }

    /**
     * 命令response
     *
     * @param domain
     * @param roomId
     * @param response
     * @param serverCommandEnum
     */
    public void commandAllResponse(String domain, String roomId, JSONObject response, ServerCommandEnum serverCommandEnum) {
        response.put("command", serverCommandEnum.getKey());
        sendRoomMsg(domain, roomId, response.toJSONString());
    }

    /**
     * 发送消息
     *
     * @param domain
     * @param roomId
     * @param response
     * @param contentStr
     */
    public void sendMessage(String userId, String domain, String roomId, JSONObject response, String contentStr) {
        Room room = roomManager.queryByIdCache(Long.parseLong(roomId));
        if (room == null) {
            return;
        }
        if (room.getForbidStatus() == 1) {
            User user = userManager.queryById(Long.parseLong(userId));
            if (!roleManager.queryRolePermAndAuthority(user.getRoleId(), "chat:forbidchat")) {
                return;
            }
        }
        response.put("command", ServerCommandEnum.S_MESSAGE.getKey());
        if (!StringUtils.isEmpty(contentStr)) {
            contentStr = StringEscapeUtils.escapeHtml(contentStr);
            //contentStr = StringEscapeUtils.escapeJavaScript(contentStr);
            //contentStr = StringEscapeUtils.escapeSql(contentStr);
        }
        response.put("content", contentStr);
        HistoryMessageHandler.insert(domain, roomId, response.toJSONString());
        sendRoomMsg(domain, roomId, response.toJSONString());
    }

    /**
     * 踢出用户
     *
     * @param domain
     * @param roomId
     * @param contentStr
     */
    public void removeChat(Channel channel,String userId, String domain, String roomId, String contentStr) {
        JSONObject content = JSON.parseObject(contentStr);
        String removeIdstr = content.getString("removeId");

        DomainConfig domainConfig = domainConfigManager.queryByDomainName(domain);
        if (domainConfig==null){
            logger.error("踢出用户服务为空");
            return;
        }

        User user = userManager.queryByDomainIdAndId(Long.parseLong(userId),domainConfig.getId());
        if (user == null) {
            logger.error("踢出操作用户为空");
            return;
        }

        if (!roleManager.queryRolePermAndAuthority(user.getRoleId(), "chat:remove")) {
            if (channel.isActive()){
                JSONObject response = new JSONObject();
                response.put("messageId", UUID.randomUUID().toString().replace("-", ""));
                response.put("command", ServerCommandEnum.S_AUTH_ERROR.getKey());
                channel.writeAndFlush(new TextWebSocketFrame(response.toJSONString()));
            }
            return;
        }
        Long removeId = Long.parseLong(removeIdstr);
        User removeUser = userManager.queryByDomainIdAndId(removeId,domainConfig.getId());
        if (removeUser!=null){
            if (userManager.remove(removeUser.getId())){
                ChannelContext context = DomainChannelMap.getAndRemoveRoomUserChannelContext(domain,roomId,removeId);
                if (context != null && context.getChannel()!=null){
                    if (context.getChannel().isActive()){
                        JSONObject response = new JSONObject();
                        response.put("messageId", UUID.randomUUID().toString().replace("-", ""));
                        response.put("command", ServerCommandEnum.S_REMOVE_CHAT.getKey());
                        JSONObject contentResponse = new JSONObject();
                        contentResponse.put("removeId", removeId);
                        response.put("content", contentResponse);
                        context.getChannel().writeAndFlush(new TextWebSocketFrame(response.toJSONString()));
                    }
                    context.getChannel().close();
                }
            }
        }
    }

    /**
     * 撤回消息
     *
     * @param domain
     * @param roomId
     * @param response
     * @param contentStr
     */
    public void recallMessage(Channel channel, String userId, String domain, String roomId, JSONObject response, String contentStr) {
        User user = userManager.queryById(Long.parseLong(userId));
        if (user == null) {
            return;
        }
        if (!roleManager.queryRolePermAndAuthority(user.getRoleId(), "chat:recall")) {
            if (channel.isActive()){
                response = new JSONObject();
                response.put("messageId", UUID.randomUUID().toString().replace("-", ""));
                response.put("command", ServerCommandEnum.S_AUTH_ERROR.getKey());
                channel.writeAndFlush(new TextWebSocketFrame(response.toJSONString()));
            }
            return;
        }
        JSONObject content = JSON.parseObject(contentStr);
        String messageId = content.getString("messageId");
        if (StringUtils.isEmpty(messageId)) {
            logger.error("撤回消息id为空");
            return;
        }
        HistoryMessageHandler.remove(domain, roomId, messageId);
        response.put("command", ServerCommandEnum.S_RECALL_MESSAGE.getKey());
        JSONObject contentResponse = new JSONObject();
        contentResponse.put("messageId", messageId);
        contentResponse.put("status", 1);
        response.put("content", contentResponse);
        sendRoomMsg(domain, roomId, response.toJSONString());
    }

    /**
     * 领取红包
     *
     * @param domain
     * @param roomId
     * @param response
     * @param contentStr
     */
    public void receiveRedBag(String domain, String roomId, String userId, JSONObject response, String contentStr, Channel channel) {
        JSONObject content = JSON.parseObject(contentStr);
        String redbagId = content.getString("redbagId");
        ReceiveRedbag receiveRedbag = null;
        try {
            receiveRedbag = receiveRedbagManager.userReceivedRedbag(domain, Long.parseLong(redbagId), Long.parseLong(userId));
        } catch (NumberFormatException e) {
            logger.error("查询已领取红包数据异常");
        }
        if (receiveRedbag != null) {
            response.put("command", ServerCommandEnum.S_RECEIVE_REDBAG.getKey());
            JSONObject contentResponse = new JSONObject();
            contentResponse.put("redbagId", redbagId);
            contentResponse.put("amount", receiveRedbag.getAmount());
            contentResponse.put("sendName", receiveRedbag.getSendUserName());
            contentResponse.put("sendIcon", receiveRedbag.getSendUserIcon());
            response.put("content", contentResponse);
            channel.writeAndFlush(new TextWebSocketFrame(response.toJSONString()));
            return;
        }
        try {
            receiveRedbag = receiveRedbagManager.receiveRedbag(domain, Long.parseLong(redbagId), Long.parseLong(userId));
        } catch (Exception e) {
            logger.error("领取红包异常");
            response.put("command", ServerCommandEnum.S_RECEIVE_REDBAG_ERROR.getKey());
            channel.writeAndFlush(new TextWebSocketFrame(response.toJSONString()));
            return;
        }
        if (receiveRedbag == null) {
            response.put("command", ServerCommandEnum.S_REDBAG_OUT.getKey());
            channel.writeAndFlush(new TextWebSocketFrame(response.toJSONString()));
            return;
        }
        response.put("command", ServerCommandEnum.S_RECEIVE_REDBAG.getKey());
        JSONObject contentResponse = new JSONObject();
        contentResponse.put("redbagId", redbagId);
        contentResponse.put("amount", receiveRedbag.getAmount());
        contentResponse.put("sendName", receiveRedbag.getSendUserName());
        contentResponse.put("sendIcon", receiveRedbag.getSendUserIcon());
        response.put("content", contentResponse);
        sendRoomMsg(domain, roomId, response.toJSONString());
        return;
    }

    /**
     * 组装基本返回信息
     *
     * @param userId
     * @param userName
     * @param userIcon
     * @param roleName
     * @return
     */
    private JSONObject createResponse(String userId, String userName, String userIcon, String roleName) {
        JSONObject response = new JSONObject();
        response.put("messageId", UUID.randomUUID().toString().replace("-", ""));
        response.put("userId", userId);
        response.put("userName", userName);
        response.put("userIcon", userIcon);
        response.put("roleName", roleName);
        response.put("messageTime", DateUtils.getStrFromDate(new Date(), "HH:mm:ss"));
        return response;
    }

    /**
     * 发送消息
     *
     * @param domain
     * @param roomId
     * @param response
     */
    public void sendRoomMsg(String domain, String roomId, String response) {
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, ChannelContextMap> channelMap = DomainChannelMap.sendRoomMsg(domain, roomId);
                if (channelMap != null && channelMap.size() > 0) {
                    for (String key : channelMap.keySet()) {
                        try {
                            threadPoolTaskExecutor.execute(new SendMsgThread(channelMap.get(key), response));
                        } catch (Exception e) {
                            logger.error("线程发送消息处理异常",e);
                        }
                    }
                }
            }
        });
    }
}
